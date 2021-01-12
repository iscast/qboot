package org.qboot.common.task.job;

import java.util.Map;

import org.qboot.common.constants.SysConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Job模板接口 定时任务需实现此接口
 * @author iscast
 * @date 2020-09-25
 */
public interface BaseJob {
	
	Logger logger = LoggerFactory.getLogger(BaseJob.class) ;
	/**
	 * Job具体实现
	 * @param params 执行参数,来源于 {@link SysTask.params},请务必保证 {@link SysTask.params}为标准JSON参数.<br/>
	 * 		另外,系统自带的传入参数有 :<br/>
	 * 			taskId 			Long	当前任务ID					{@link SysConstants.TASK_MAP_PARAMS_KEY_TASKID}<br/>
	 * 			lastRunTime		Date	上一次执行成功的开始执行时间 		{@link SysConstants.TASK_MAP_PARAMS_KEY_LAST_RUNTIME}<br/>
	 * 			lastResult		String	上一次执行成功的执行结果 			{@link SysConstants.TASK_MAP_PARAMS_KEY_LAST_RESULT}<br/>
	 * @return 执行结果描述,将持久化至 {@link SysTaskLog.execResult}
	 */
	String execute(Map<String, Object> params);
}
