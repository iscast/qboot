package org.qboot.common.constants;

/**
 * cache key prefix constants
 * @author iscast
 * @date 2018年11月9日 上午11:38:11
 */
public class CacheConstants {
	

	/**
	 * @see org.quartz.impl.JobDetailImpl#getJobDataMap()
	 */
	public static final String JOB_DATA_MAP_KEY = "job_object";

    /**
     * 任务信息缓存前缀
     */
    public static final String CACHE_PREFIX_SYS_TASK_INFO = "qboot:sys:task:info:";

	/**
	 * 执行任务锁
	 */
	public static final String TASK_LOCK = "SYS_TASK_LOCK_";

    /** system redis cache prefix begin */

    public static final String CACHE_PREFIX_SYS_DICT_TYPE = "qboot:sys:dict:";

    public static final String CACHE_PREFIX_SYS_PARAMTYPE_KEY = "qboot:sys:paramtype:";

    public static final String CACHE_PREFIX_SYS_OPTLOG_URL = "qboot:sys:optlog:url:";

    /** system redis cache prefix end */


    public static final String CACHE_PREFIX_LOGIN_FAILCOUNT = "qboot:login:failcount:";

    public static final String CACHE_PREFIX_LOGIN_USERNAME_SESSION = "qboot:login:username:session";
	
}
