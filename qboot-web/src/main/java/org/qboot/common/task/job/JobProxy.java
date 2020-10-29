package org.qboot.common.task.job;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.qboot.base.dto.SysTask;
import org.qboot.base.dto.SysTaskLog;
import org.qboot.base.service.impl.SysTaskLogService;
import org.qboot.base.service.impl.SysTaskService;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.exception.QExceptionCode;
import org.qboot.common.exception.ServiceException;
import org.qboot.common.utils.RedisTools;
import org.qboot.common.utils.SpringContextHolder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Job 代理类
 * 所有任务的入口并记录Job执行日志
 * @author history
 * @date 2018年11月8日 下午8:10:00
 */
@Component
public class JobProxy{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private SysTaskLogService sysTaskLogService;
	
	@Autowired
	private SysTaskService sysTaskService;
	
	@Autowired
	private RedisTools redisTools;
	
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		SysTask sysTask =  (SysTask)jobCtx.getMergedJobDataMap().get(CacheConstants.JOB_DATA_MAP_KEY);
		if (sysTask == null) {
			logger.warn("{},run_time:{}", QExceptionCode.TASK_EXECUTE_NULL,dateFormat.format(new Date(jobCtx.getJobRunTime())));
			throw new ServiceException(QExceptionCode.TASK_EXECUTE_NULL);
		}
		// 记录任务执行开始日志
		Long logId = null ;
		try {
			//获取最新任务信息
			long taskId = sysTask.getId() ;
			sysTask = redisTools.get(sysTask.toCacheKeyString()) ;
			if(sysTask == null){
				sysTask = sysTaskService.findById(taskId) ;
				logger.warn("未命中缓存:{}", sysTask.toString());
			}
			//重新获取到任务信息后,需要判断任务状态是否有变化,从而感知脚本更改状态的情况,注意:脚本变更需要等缓存失效或者守护线程重新规划了变更任务
			Object runNow = redisTools.get(sysTask.toRunNowCacheNoticeString()) ;
			if(sysTask.isDisabled()&&null==runNow){
				logger.warn("任务状态已变更为关闭,拒绝执行此次任务:{}.",sysTask.toString());
				try {
					//从内存移除
					sysTaskService.deleteScheduleJob(sysTask);
					logger.warn("从任务调度器中移除该任务:{}.",sysTask.toString());
				} catch (SchedulerException e) {
					logger.error("从内存移除任务失败:{}.",sysTask.toString());
				}
				return ;
			}else if(runNow!=null){
				//立即执行通知完成
				redisTools.del(sysTask.toRunNowCacheNoticeString()) ;
			}
			//插入日志
			logId = insertTaskLog(jobCtx.getFireTime(),sysTask.getId());
			//解析附加参数
			String paramJson = sysTask.getParams();
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotEmpty(paramJson)) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
				mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true) ;
				params = (Map<String, Object>)mapper.readValue(paramJson, HashMap.class) ;
			}
			
			// 获取任务具体执行服务
			BaseJob job = SpringContextHolder.getBean(sysTask.getTaskTarget(),BaseJob.class);
			
			if (job == null) {
				logger.error("{}, taskName:{},runTime:{}",
						QExceptionCode.TASK_EXECUTE_NULL,sysTask.getTaskName(),
						dateFormat.format(new Date(jobCtx.getJobRunTime()).toString()));
				
				updateTasktLog(logId, QExceptionCode.TASK_EXEC_STATUS_FAILED,
						String.format("%s, task_name:%s,run_time:%s", QExceptionCode.TASK_EXECUTE_NULL,
								sysTask.getTaskName(),dateFormat.format(new Date(jobCtx.getJobRunTime()).toString())),sysTask);
				
				throw new ServiceException(QExceptionCode.TASK_EXECUTE_NULL);
			}
		
			//放入TaskID
			params.put(SysConstants.TASK_MAP_PARAMS_KEY_TASKID, sysTask.getId());
			params.put(SysConstants.TASK_MAP_PARAMS_KEY_LAST_RUNTIME, sysTask.getLastRunTime()) ;
			params.put(SysConstants.TASK_MAP_PARAMS_KEY_LAST_RESULT, sysTask.getLastResult()) ;
			String execResult = job.execute(params);
			updateTasktLog(logId, QExceptionCode.TASK_EXEC_STATUS_SUCCESS,execResult,sysTask);
			logger.info("任务执行结束：{}", sysTask.toString());
			
		} catch (Exception e) {
			logger.error("执行定时任务异常:{}", sysTask.toString(), e);
			updateTasktLog(logId, QExceptionCode.TASK_EXEC_STATUS_FAILED, QExceptionCode.TASK_EXECUTE_ERROR,sysTask);
		} 
	}

	/**
	 * 记录任务开始执行日志
	 * @param beginTime 任务开始执行时间
	 * @param taskId 任务ID
	 * @return 日志ID
	 */
	public Long insertTaskLog(Date beginTime,Long taskId) {
		SysTaskLog taskLog = new SysTaskLog();
		taskLog.setTaskId(taskId);
		taskLog.setBeginTime(beginTime);
		taskLog.setExecIp(this.getHostAddress());
		taskLog.setCreateDate(new Date());
		taskLog.setUpdateDate(new Date());
		this.sysTaskLogService.save(taskLog);
		return taskLog.getId();
	}
	
	/**
	 * 更新任务执行日志
	 * @param logId
	 * @param execStatus
	 * @param execResult
	 */
	public void updateTasktLog(Long logId,int execStatus,String execResult,SysTask systask) {
		SysTaskLog taskLog = sysTaskLogService.findById(logId);
		if (taskLog == null) {
			logger.error(String.format("任务日志id为%s的记录无法获取，任务结果更新失败", logId));
			return;
		}
		taskLog.setEndTime(new Date());
		// 耗时
		long costTime = taskLog.getEndTime().getTime() - taskLog.getBeginTime().getTime(); 
		taskLog.setCostTime(costTime);
		taskLog.setExecStatus(execStatus);
		taskLog.setExecResult(execResult);
		taskLog.setUpdateDate(new Date());
		sysTaskLogService.updateById(taskLog);
		//记录上一次执行成功的时间
		if(QExceptionCode.TASK_EXEC_STATUS_SUCCESS==execStatus){
			systask.setLastResult(execResult);
			systask.setLastRunTime(taskLog.getBeginTime());
			sysTaskService.updateResult(systask) ;
		}
	}
	/**
	 * 获取本机IP地址
	 * @return
	 */
	private String getHostAddress(){
		InetAddress inetAddres;
		try {
			inetAddres = InetAddress.getLocalHost();
			return inetAddres.getHostAddress() ;
		} catch (UnknownHostException e) {
			logger.error("UnknownHostException",e);
		}
		return null ;
	}
}
