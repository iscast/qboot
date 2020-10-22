package org.qboot.common.constant;

/**
 * <p>Title: SysConstants</p>
 * <p>Description: 常量类</p>
 * @author history
 * @date 2018-09-08
 */
public class SysConstants {

	public static final String YES = "1";
	
	public static final String NO = "0";
	
	public static final String ASC = "asc";
	
	public static final String DESC = "desc";
	
	public static final String SEPARATOR = ",";

	public static final String SUCESS = "sucess";
	public static final String FAIL = "fail";

    public static final String SYS_ENABLE = "1";
    public static final String SYS_DISABLE = "0";


    /** system redis cache prefix begin */

    public static final String CACHE_PREFIX_DICT_TYPE = "qboot:sys:dict:type:";

    public static final String CACHE_PREFIX_PARAMTYPE_KEY = "qboot:sys:paramtype:key:";

    public static final String CACHE_PREFIX_OPTLOG_URL = "qboot:sys:optlog:url:";

    /** system redis cache prefix end */


	/**
	 * super Administrator id
	 */
	public static final String SUPER_ADMIN_ID = "1";
    /**
     * super Administrator name
     */
    public static final String SUPER_ADMIN_NAME = "superadmin";

    /**
     * task default group
     */
    public static final String TASK_DEFAULT_GROUP = "DEFAULT_GROUP";

	/**
	 * 当前执行任务ID map key
	 */
	public static final String TASK_MAP_PARAMS_KEY_TASKID = "taskId";
	/**
	 * 最后一次执行成功的开始时间  map key
	 */
	public static final String TASK_MAP_PARAMS_KEY_LAST_RUNTIME = "lastRunTime";
	/**
	 * 最后一次执行成功的执行结果   map key
	 */
	public static final String TASK_MAP_PARAMS_KEY_LAST_RESULT = "lastResult";
	
}
