package org.iscast.common.constant;

/**
 * 缓存key前缀常量类
 * @author history
 * @date 2018年11月9日 上午11:38:11
 */
public class CacheConstants {
	
	/**
	 * 任务信息缓存前缀
	 */
	public static final String TASK_INFO = "RDP_TASK_INFO_";
	/**
	 * @see org.quartz.JobDetailI#getJobDataMap()
	 */
	public static final String JOB_DATA_MAP_KEY = "job_object";
	/**
	 * 执行任务锁
	 */
	public static final String TASK_LOCK = "RDP_TASK_LOCK_";
	
	
}
