package org.qboot.common.constants;

/**
 * cache key prefix constants
 * @author iscast
 * @date 2020-09-25
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

    /** system cache prefix begin */

    public final static String CACHE_LOCK_KEY = "qboot:lock:";

    public static final String CACHE_PREFIX_SYS_DICT_TYPE = "qboot:sys:dict:";

    public static final String CACHE_PREFIX_SYS_DICT_TYPE_SINGLE = "qboot:sys:dict:single:";

    public static final String CACHE_PREFIX_SYS_PARAMTYPE_KEY = "qboot:sys:paramtype:";

    public static final String CACHE_PREFIX_USER_QRY_KEY = "qboot:user:page:qry:";

    /** system cache prefix end */


    public static final String CACHE_PREFIX_LOGIN_FAILCOUNT = "qboot:login:failcount:";

    public static final String CACHE_PREFIX_LOGIN_USER = "qboot:login:user:";
	
}
