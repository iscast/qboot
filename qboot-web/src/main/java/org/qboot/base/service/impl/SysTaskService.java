package org.qboot.base.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.qboot.base.dao.SysTaskDao;
import org.qboot.base.dto.SysTask;
import org.qboot.common.constant.CacheConstants;
import org.qboot.common.exception.QExceptionCode;
import org.qboot.common.exception.ServiceException;
import org.qboot.common.service.CrudService;
import org.qboot.common.task.job.AllowConcurrentExecutionJob;
import org.qboot.common.task.job.DisallowConcurrentExecutionJob;
import org.qboot.common.utils.QRedisson;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务服务层
 * @author history
 */
@Service
public class SysTaskService extends CrudService<SysTaskDao, SysTask> {
	
	public static final String DEFAULT_GROUP = "DEFAULT_GROUP";
	@Resource
	private Scheduler scheduler;
	@Resource
	private QRedisson qRedisson;
	@Resource
	SysTaskLogService sysTaskLogService ;

	@Override
	public PageInfo<SysTask> findByPage(SysTask sysTask) {
		if(sysTask.getStatus()==-1){
			sysTask.setStatus(null);
		}
		PageInfo<SysTask> result = super.findByPage(sysTask) ;
		Map<Long, String> runningTask = this.getRunningTask() ;
		Page<SysTask>  iniList = new Page<SysTask>();
		Page<SysTask>  runningList = new Page<SysTask>();
		result.getList().forEach(i -> {
			if(runningTask.containsKey(i.getId())){
				i.setRunStatus(SysTask.RUN_STATUS_RUNNING);
				runningList.add(i) ;
			}else{
				i.setRunStatus(SysTask.RUN_STATUS_INI);
				iniList.add(i) ;
			}
		});
		//内存状态条件查询
		if(sysTask.getRunStatus()==SysTask.RUN_STATUS_INI){
			result.setList(iniList);
		}else if(sysTask.getRunStatus()==SysTask.RUN_STATUS_RUNNING){
			result.setList(runningList);
		}
		return result;
	}

	/**
	 * 查询所有任务
	 * @return
	 */
	public List<SysTask> findAll(){
		return this.d.findAll() ;
	}
	
	/**
	 * 根据任务名称查询任务
	 * @param taskName
	 * @return
	 */
	public SysTask findByName( String taskName){
		return this.d.findByName(taskName) ;
	}
	
	/**
	 * 更新启用状态
	 * @param sysTask
	 * @return
	 */
	@Transactional
	public int updateStatus(SysTask task){
		int result = this.d.updateStatus(task) ;
		SysTask newTask = this.findById(task.getId());
		// 移除系统任务
		try {
			deleteScheduleJob(newTask);
		} catch (SchedulerException e) {
			logger.error("移除任务异常：{}", e.getMessage());
			throw new ServiceException(QExceptionCode.TASK_INIT_ERROR);
		}
		if(newTask.isEnabled()){
			// 加入系统任务
			try {
				createScheduleJob(newTask);
			} catch (SchedulerException e) {
				logger.error("新增任务异常：{}",e);
				throw new ServiceException(QExceptionCode.TASK_INIT_ERROR);
			}
		}
		qRedisson.set(newTask.toCacheKeyString(), newTask);
		logger.info("任务状态变更成功:{}.",newTask.toString());
		return  result;
	}
	@Override
	@Transactional
	public int save(SysTask task) {
		//先持久化任务
		int result = super.save(task) ;
		//加入系统任务
		if(task.isEnabled()){
			try {
				createScheduleJob(task);
			} catch (SchedulerException e) {
				logger.error("新增任务异常：{}",e.getMessage());
				throw new ServiceException(QExceptionCode.TASK_INIT_ERROR);
			}
		}
		qRedisson.set(task.toCacheKeyString(), task);
		logger.info("新增任务成功：{}",task.toString());
		return result;
	}

	@Override
	@Transactional
	public int updateById(SysTask task) {
		int result = super.updateById(task) ;
		SysTask newTask = d.findById(task.getId()) ;
		// 重置任务
		try {
			deleteScheduleJob(newTask);
			if(newTask.isEnabled()){ 
				// 任务为已启用状态
				createScheduleJob(newTask);
			}
		} catch (SchedulerException e) {
			logger.error("重置任务异常：{}", e.getMessage());
			throw new ServiceException(QExceptionCode.TASK_INIT_ERROR);
		}
		qRedisson.set(newTask.toCacheKeyString(), newTask);
		logger.info("修改任务信息成功：{}.",task.toString());
		return result;
	}

	/**
	 * 立即触发一次执行
	 */
	public void runOnce(Long taskId) throws ServiceException {
		SysTask task = this.findById(taskId);
		if (task == null) {
			throw new ServiceException(QExceptionCode.TASK_EXECUTE_NULL);
		}
		if(isRunning(taskId)){
			throw new ServiceException("当前任务正在执行中!!!");
		}
		qRedisson.set(task.toRunNowCacheNoticeString(), task.getId());
		if(task.isEnabled()){ 
			// 已启用的任务
			try {
				logger.info("准备执行任务：{}.",task.toString());
				runOnce(task);
				logger.info("触发任务成功：{}.",task.toString());
			} catch (SchedulerException e) {
				logger.error("执行任务异常：{}-{}.", QExceptionCode.TASK_EXECUTE_ERROR,task.toString(),e);
				throw new ServiceException(QExceptionCode.TASK_EXECUTE_ERROR);
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
				throw new ServiceException(QExceptionCode.TASK_EXECUTE_ERROR);
			}
		}
	}
	
	/**
	 * 初始化任务，将任务添加到schedule
	 * @throws SchedulerException 
	 */
	public void initTask() throws ServiceException {
		List<SysTask> tasks =  this.findAll();
		tasks.forEach(task -> {
			if (task.isEnabled()) {
				try {
					createScheduleJob(task);
					logger.info("添加任务调度成功：{}.",task.toString());
				} catch (SchedulerException e) {
					logger.error("添加任务调度失败：{}-{}.", QExceptionCode.TASK_INIT_ERROR,task.toString(),e);
					throw new ServiceException(QExceptionCode.TASK_INIT_ERROR);
				}
			}else{
				logger.info("任务未启用，不参与调度：{}.",task.toString());
			}
		});
	}
	/**
	 * 获取JobKey
	 * @param taskId
	 * @param group
	 * @return
	 */
	public JobKey getJobKey(Long taskId) {
		return JobKey.jobKey(taskId.toString(), DEFAULT_GROUP);
	}
	/**
	 * 获取triggerKey
	 * @param taskId
	 * @param group
	 * @return
	 */
	public TriggerKey getTriggerKey(Long taskId) {
		return TriggerKey.triggerKey(taskId.toString(), DEFAULT_GROUP);
	}
	/**
	 * 检测任务是否存在
	 * @param taskId
	 * @param group
	 * @return
	 * @throws SchedulerException
	 */
	public boolean checkExists(Long taskId) throws SchedulerException {
		return scheduler.checkExists(getJobKey(taskId));
	}
	
	/**
	 * 获取计划中的任务对象
	 * @param taskId
	 * @param group
	 * @return
	 * @throws SchedulerException
	 */
	public SysTask getJobObject(Long taskId) throws SchedulerException {
		JobDetail jobDetail = scheduler.getJobDetail(getJobKey(taskId));
		return (SysTask)jobDetail.getJobDataMap().get(CacheConstants.JOB_DATA_MAP_KEY);
	}
	
	/**
	 * 将任务加入调度中心
	 * @param scheduler
	 * @param task
	 * @throws SchedulerException
	 */
	public void createScheduleJob(SysTask task) throws SchedulerException {
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
		CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger()
				.withIdentity(getTriggerKey(task.getId()))
				.withSchedule(builder).build();
		
		scheduler.scheduleJob(detail, trigger);
	}
	
	/**
	 * 单次执行
	 * @param scheduler
	 * @param task
	 * @throws SchedulerException
	 */
	public void createSimpleJob(SysTask task) throws SchedulerException {
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
	 * @param scheduler
	 * @param task
	 * @throws SchedulerException
	 */
	public void updateScheduleJob(SysTask task) throws SchedulerException {
		TriggerKey triggerKey = getTriggerKey(task.getId());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		
		CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(task.getCronExp());
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
				.withSchedule(builder).build();
		scheduler.rescheduleJob(triggerKey, trigger);
	}
	
	/**
	 * 从调度中心删除任务
	 * @param scheduler
	 * @param task
	 * @throws SchedulerException
	 */
	public void deleteScheduleJob(SysTask task) throws SchedulerException {
		scheduler.deleteJob(getJobKey(task.getId()));
	}
	
	/**
	 * 暂停任务
	 * @param scheduler
	 * @param task
	 * @throws SchedulerException
	 */
	public void pauseScheduleJob(SysTask task) throws SchedulerException {
		scheduler.pauseJob(getJobKey(task.getId()));
	}
	
	/**
	 * 重启任务
	 * @param scheduler
	 * @param task
	 * @throws SchedulerException
	 */
	public void resumeScheduleJob(SysTask task) throws SchedulerException {
		scheduler.resumeJob(getJobKey(task.getId()));
	}
	
	/**
	 * 立即执行
	 * @param scheduler
	 * @param task
	 * @throws SchedulerException
	 */
	public void runOnce(SysTask task) throws SchedulerException {
		JobKey jobKey = getJobKey(task.getId());
		scheduler.triggerJob(jobKey);
	}
	
	/**    
	 * 所有系统中正在运行的任务
	 *     
	 * @return    
	 * @throws SchedulerException    
	 */    
	private Map<Long,String> getRunningTask(){    
		List<JobExecutionContext> executingJobs;
		try {
			executingJobs = scheduler.getCurrentlyExecutingJobs();
		} catch (SchedulerException e) {
			throw new ServiceException(QExceptionCode.TASK_GET_RUNNING_ERROR);
		}    
		Map<Long,String> runningTaskList = new HashMap<Long,String>();    
		for (JobExecutionContext executingJob : executingJobs) {    
			JobKey jobKey = executingJob.getJobDetail().getKey();    
			runningTaskList.put(Long.parseLong(jobKey.getName()),jobKey.getName());    
		}    
		return runningTaskList;    
	}
	
	/**    
	 * Task 是否下在运行
	 * @return    
	 * @throws SchedulerException    
	 */    
	private boolean isRunning(Long taskId){    
		List<JobExecutionContext> executingJobs;
		try {
			executingJobs = scheduler.getCurrentlyExecutingJobs();
		} catch (SchedulerException e) {
			throw new ServiceException(QExceptionCode.TASK_GET_RUNNING_ERROR);
		}    
		for (JobExecutionContext executingJob : executingJobs) {
			if(executingJob.getJobDetail().getKey().getName().equalsIgnoreCase(taskId.toString())){
				return true;
			}
		}    
		return false;    
	}
	/**
	 * 删除任务时清理所有任务日志
	 * @param id
	 */
	public void deleteTask(Long taskId) {
		this.deleteById(taskId) ;
		sysTaskLogService.deleteByTaskId(taskId) ;
	}

	public Long countByTaskName(SysTask sysTask) {
		return this.d.countByTaskName(sysTask);
	}
	/**
	 * 使任务信息更新部分生效到任务调度器
	 *  主要是时间表达式变更,这项变更必须重置任务调度器内对应的任务
	 *  注意,此处只更新有变更的任务
	 * @param updatedList
	 */
	public void reloadTask() {
		logger.debug("==============start============守护线程扫描定时任务更新=========start=================");
		List<SysTask> tasks = this.findAll() ;
		SysTask quartzTask ; 
		for (SysTask dbTask : tasks) {
			try {
				//更新redis中的任务信息缓存
				qRedisson.set(dbTask.toCacheKeyString(), dbTask);
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
	/**
	 * 更新上一次成功执行结果
	 * @param systask
	 */
	public int updateResult(SysTask systask) {
		systask.setUpdateDate(new Date());
		int result = d.updateResult(systask) ;
		//更新缓存
		SysTask newTask = this.findById(systask.getId()) ;
		qRedisson.set(newTask.toCacheKeyString(), newTask);
		return  result;
	}
	
}
