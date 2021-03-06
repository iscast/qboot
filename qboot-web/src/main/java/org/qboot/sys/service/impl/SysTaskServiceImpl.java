package org.qboot.sys.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.task.job.AllowConcurrentExecutionJob;
import org.qboot.common.task.job.DisallowConcurrentExecutionJob;
import org.qboot.common.utils.RedisTools;
import org.qboot.sys.dao.SysTaskDao;
import org.qboot.sys.dto.SysTaskDto;
import org.qboot.sys.exception.SysTaskException;
import org.qboot.sys.service.SysTaskService;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * task service
 * @author history
 */
@Service
public class SysTaskServiceImpl extends CrudService<SysTaskDao, SysTaskDto> implements SysTaskService{

	@Resource
	private Scheduler scheduler;
	@Resource
	private RedisTools redisTools;

	@Override
	public PageInfo<SysTaskDto> findByPage(SysTaskDto sysTask) {
		PageInfo<SysTaskDto> result = super.findByPage(sysTask) ;
		Map<String, String> runningTask = this.getRunningTask() ;
		Page<SysTaskDto>  iniList = new Page();
		Page<SysTaskDto>  runningList = new Page();
		result.getList().forEach(i -> {
			if(runningTask.containsKey(i.getId())){
				i.setRunStatus(SysTaskDto.RUN_STATUS_RUNNING);
				runningList.add(i) ;
			}else{
				i.setRunStatus(SysTaskDto.RUN_STATUS_INI);
				iniList.add(i) ;
			}
		});
		//内存状态条件查询
		if(sysTask.getRunStatus()== SysTaskDto.RUN_STATUS_INI){
			result.setList(iniList);
		}else if(sysTask.getRunStatus()== SysTaskDto.RUN_STATUS_RUNNING){
			result.setList(runningList);
		}
		return result;
	}

    @Override
	public List<SysTaskDto> findAll(){
		return this.d.findAll() ;
	}

    @Override
	public SysTaskDto findByName(String taskName){
		return this.d.findByName(taskName) ;
	}

    @Override
	@Transactional
	public int updateStatus(SysTaskDto task){
		int result = this.d.updateStatus(task) ;
		SysTaskDto newTask = this.findById(task.getId());
		// 移除系统任务
		try {
			deleteScheduleJob(newTask);
		} catch (SchedulerException e) {
			logger.error("移除任务异常：{}", e.getMessage());
            throw new SysTaskException(SYS_TASK_INIT_ERROR);
		}
		if(newTask.isEnabled()){
			// 加入系统任务
			try {
				createScheduleJob(newTask);
			} catch (SchedulerException e) {
				logger.error("新增任务异常：{}",e);
                throw new SysTaskException(SYS_TASK_INIT_ERROR);
			}
		}
		redisTools.set(newTask.toCacheKeyString(), newTask);
		logger.info("任务状态变更成功:{}.",newTask.toString());
		return  result;
	}

	@Override
	@Transactional
	public int save(SysTaskDto task) {
		//先持久化任务
		int result = super.save(task) ;
		//加入系统任务
		if(task.isEnabled()){
			try {
				createScheduleJob(task);
			} catch (SchedulerException e) {
				logger.error("新增任务异常：{}",e.getMessage());
                throw new SysTaskException(SYS_TASK_INIT_ERROR);
			}
		}
		redisTools.set(task.toCacheKeyString(), task);
		logger.info("新增任务成功：{}",task.toString());
		return result;
	}

	@Override
	@Transactional
	public int update(SysTaskDto task) {
		int result = super.update(task) ;
		SysTaskDto newTask = d.findById(task.getId()) ;
		// 重置任务
		try {
			deleteScheduleJob(newTask);
			if(newTask.isEnabled()){ 
				// 任务为已启用状态
				createScheduleJob(newTask);
			}
		} catch (SchedulerException e) {
			logger.error("重置任务异常：{}", e.getMessage());
            throw new SysTaskException(SYS_TASK_INIT_ERROR);
		}
		redisTools.set(newTask.toCacheKeyString(), newTask);
		logger.info("修改任务信息成功：{}.",task.toString());
		return result;
	}

    @Override
	public void runOnce(String taskId) {
		SysTaskDto task = this.findById(taskId);
		if (task == null) {
            throw new SysTaskException(SYS_TASK_EXECUTE_NULL);
		}
		if(isRunning(taskId)){
            throw new SysTaskException(SYS_TASK_IS_EXECUTING);
		}
		redisTools.set(task.toRunNowCacheNoticeString(), task.getId());
		if(task.isEnabled()){ 
			// 已启用的任务
			try {
				logger.info("准备执行任务：{}.",task.toString());
				runOnce(task);
				logger.info("触发任务成功：{}.",task.toString());
			} catch (SchedulerException e) {
				logger.error("执行任务异常：execute error {}.", task.toString(), e);
                throw new SysTaskException(SYS_TASK_EXECUTE_ERROR);
			}
		}else{ 
			// 未启用的任务
			try {
				if(scheduler.getJobDetail(getJobKey(task.getId())) != null){
					runOnce(task);
				}else{
					createSimpleJob(task);
				}
			} catch (SchedulerException e) {
				logger.error(e.toString());
                throw new SysTaskException(SYS_TASK_EXECUTE_ERROR);
			}
		}
	}

    @Override
	public void initTask() {
		List<SysTaskDto> tasks =  this.findAll();
		tasks.forEach(task -> {
			if (task.isEnabled()) {
				try {
					createScheduleJob(task);
					logger.info("添加任务调度成功：{}.",task.toString());
				} catch (SchedulerException e) {
					logger.error("添加任务调度失败：task init error : {}.", task.toString(), e);
                    throw new SysTaskException(SYS_TASK_INIT_ERROR);
                }
			}else{
				logger.info("任务未启用，不参与调度：{}.",task.toString());
			}
		});
	}


    @Override
	public void deleteScheduleJob(SysTaskDto task) throws SchedulerException {
		scheduler.deleteJob(getJobKey(task.getId()));
	}

    @Override
	public void pauseScheduleJob(SysTaskDto task) throws SchedulerException {
		scheduler.pauseJob(getJobKey(task.getId()));
	}

    @Override
	public void resumeScheduleJob(SysTaskDto task) throws SchedulerException {
		scheduler.resumeJob(getJobKey(task.getId()));
	}

    @Override
	public void runOnce(SysTaskDto task) throws SchedulerException {
		JobKey jobKey = getJobKey(task.getId());
		scheduler.triggerJob(jobKey);
	}

    @Override
	public Long countByTaskName(SysTaskDto sysTask) {
		return this.d.countByTaskName(sysTask);
	}

    @Override
	public void reloadTask() {
		logger.debug("==============start============守护线程扫描定时任务更新=========start=================");
		List<SysTaskDto> tasks = this.findAll() ;
		SysTaskDto quartzTask ;
		for (SysTaskDto dbTask : tasks) {
			try {
				//更新redis中的任务信息缓存
				redisTools.set(dbTask.toCacheKeyString(), dbTask);
				//1.删除已规划的
				if(dbTask.isDisabled()&&checkExists(dbTask.getId())){
					deleteScheduleJob(dbTask);
					logger.info("检测到需禁用的任务并从任务调度器移除:{}.",dbTask.toString());
					continue ;
				}
				//2.新增待规划的
				if(dbTask.isEnabled()&&!checkExists(dbTask.getId())){
					//不存在且为启用状态,创建调度任务
					createScheduleJob(dbTask);
					logger.info("检测到新开启的任务并加入任务调度器:{}.",dbTask.toString());
					continue ;
				}
				//3.已规划且时间表达式变更的
				if(checkExists(dbTask.getId())){
					quartzTask = getJobObject(dbTask.getId()) ;
					if(!dbTask.getCronExp().equalsIgnoreCase(quartzTask.getCronExp())){
						//已变更的,重新规划
						deleteScheduleJob(dbTask);
						createScheduleJob(dbTask);
						logger.info("检测到时间表达式变更的任务并更新任务调度器:{}-->{} | {}.",quartzTask.getCronExp(),dbTask.getCronExp(),dbTask.toString());
						continue ;
					}
				}
				//调度器没有,数据库也是关闭状态,直接忽略
				logger.debug("任务无变化,无需处理:{}.",dbTask.toString());
			} catch (Exception e) {
				logger.error("自动检测任务更新状态异常:{}.",dbTask.toString());
			}
		}
		logger.debug("==============end============守护线程扫描定时任务更新===========end===================");
	}

	@Override
	public int updateResult(SysTaskDto systask) {
		systask.setUpdateDate(new Date());
		int result = d.updateResult(systask) ;
		//更新缓存
		SysTaskDto newTask = this.findById(systask.getId()) ;
		redisTools.set(newTask.toCacheKeyString(), newTask);
		return  result;
	}

    /**
     * 获取JobKey
     * @param taskId
     * @return
     */
    private JobKey getJobKey(String taskId) {
        return JobKey.jobKey(taskId, SysConstants.TASK_DEFAULT_GROUP);
    }

    /**
     * 获取triggerKey
     * @param taskId
     * @return
     */
    private TriggerKey getTriggerKey(String taskId) {
        return TriggerKey.triggerKey(taskId, SysConstants.TASK_DEFAULT_GROUP);
    }
    /**
     * 检测任务是否存在
     * @param taskId
     * @return
     * @throws SchedulerException
     */
    private boolean checkExists(String taskId) throws SchedulerException {
        return scheduler.checkExists(getJobKey(taskId));
    }

    /**
     * 获取计划中的任务对象
     * @param taskId
     * @return
     * @throws SchedulerException
     */
    private SysTaskDto getJobObject(String taskId) throws SchedulerException {
        JobDetail jobDetail = scheduler.getJobDetail(getJobKey(taskId));
        return (SysTaskDto)jobDetail.getJobDataMap().get(CacheConstants.JOB_DATA_MAP_KEY);
    }

    /**
     * 将任务加入调度中心
     * @param task
     * @throws SchedulerException
     */
    private void createScheduleJob(SysTaskDto task) throws SchedulerException {
        // 设置是否允许并发执行
        Class<? extends Job> clazz = DisallowConcurrentExecutionJob.class;
        if(task.isConcurrentExecutionAllow()){
            clazz = AllowConcurrentExecutionJob.class;
        }
        JobDetail detail = JobBuilder.newJob(clazz)
                .withIdentity(getJobKey(task.getId()))
                .build();
        detail.getJobDataMap().put(CacheConstants.JOB_DATA_MAP_KEY, task);

        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(task.getCronExp());
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(task.getId()))
                .withSchedule(builder).build();

        scheduler.scheduleJob(detail, trigger);
    }

    /**
     * 单次执行
     * @param task
     * @throws SchedulerException
     */
    private void createSimpleJob(SysTaskDto task) throws SchedulerException {
        // 设置是否允许并发执行
        Class<? extends Job> clazz = DisallowConcurrentExecutionJob.class;
        JobDetail detail = JobBuilder.newJob(clazz)
                .withIdentity(getJobKey(task.getId()))
                .build();
        detail.getJobDataMap().put(CacheConstants.JOB_DATA_MAP_KEY, task);

        SimpleScheduleBuilder builder =  SimpleScheduleBuilder.simpleSchedule();
        SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(task.getId()))
                .withSchedule(builder).build();
        scheduler.scheduleJob(detail, trigger);
    }


    /**
     * 更新调度中心的任务
     * @param task
     * @throws SchedulerException
     */
    private void updateScheduleJob(SysTaskDto task) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(task.getId());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(task.getCronExp());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                .withSchedule(builder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 所有系统中正在运行的任务
     * @return
     * @throws SchedulerException
     */
    private Map<String,String> getRunningTask(){
        List<JobExecutionContext> executingJobs;
        try {
            executingJobs = scheduler.getCurrentlyExecutingJobs();
        } catch (SchedulerException e) {
            throw new SysTaskException(SYS_TASK_GET_RUNNING_ERROR);
        }
        Map<String,String> runningTaskList = new HashMap();
        for (JobExecutionContext executingJob : executingJobs) {
            JobKey jobKey = executingJob.getJobDetail().getKey();
            runningTaskList.put(jobKey.getName(),jobKey.getName());
        }
        return runningTaskList;
    }

    /**
     * Task 是否下在运行
     * @return
     * @throws SchedulerException
     */
    private boolean isRunning(String taskId){
        List<JobExecutionContext> executingJobs;
        try {
            executingJobs = scheduler.getCurrentlyExecutingJobs();
        } catch (SchedulerException e) {
            throw new SysTaskException(SYS_TASK_GET_RUNNING_ERROR);
        }
        for (JobExecutionContext executingJob : executingJobs) {
            if(executingJob.getJobDetail().getKey().getName().equalsIgnoreCase(taskId)){
                return true;
            }
        }
        return false;
    }
}
