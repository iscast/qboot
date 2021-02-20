package org.qboot.common.constants;

/**
 * qboot system constants
 * @author icast
 * @date 2020-09-25
 */
public class SysConstants {

	public static final String YES = "1";
	
	public static final String NO = "0";
	
	public static final String ASC = "asc";
	
	public static final String DESC = "desc";
	
	public static final String SEPARATOR = ",";

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String FAIL = "fail";

    public static final String SYS_ENABLE = "1";
    public static final String SYS_DISABLE = "0";

    public static final Integer SYS_ENABLED = 1;
    public static final Integer SYS_DISABLED = 2;

    public static final Integer SYS_DELFLAG_NORMAL = 1;
    public static final Integer SYS_DELFLAG_DEL = 0;

    /**
     * default success code
     */
    public final static int GLOBAL_DEFAULT_SUCCESS = 0;
    /**
     * default error response code
     */
    public final static int GLOBAL_DEFAULT_ERROR = -1;

    public final static String GLOBAL_DEFAULT_SUCCESS_MSG = SUCCESS;

    public final static String GLOBAL_DEFAULT_ERROR_MSG = ERROR;

	/**
	 * super Administrator id
	 */
	public static final Long SUPER_ADMIN_ID = 1L;
    /**
     * super Administrator name
     */
    public static final String SUPER_ADMIN_NAME = "superadmin";

    public static final String SYSTEM_CRATER_NAME = "system";

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


	/** log param begin */

    /** log param end */

	/** login status begin */
    public static final int SYS_USER_PWD_STATUS_NORMAL = 0;
    public static final int SYS_USER_PWD_STATUS_INIT = 1;
    public static final int SYS_USER_PWD_STATUS_CHANGED = 2;

    public static final String  SYS_USER_LOGIN_STATUS_SUCCESS = "0";
    public static final String  SYS_USER_LOGIN_STATUS_PASSWORD_WRONG = "1";
    public static final String  SYS_USER_LOGIN_STATUS_STAUTS_STOP = "2";
    public static final String  SYS_USER_LOGIN_STATUS_LOCK_24 = "3";
    public static final String  SYS_USER_LOGIN_STATUS_CHANGE_PWD = "7";
    public static final String  SYS_USER_LOGIN_STATUS_INIT_PWD = "8";
    public static final String  SYS_USER_LOGIN_STATUS_SYS_ERR = "9";

    public static final String STATUS_NORMAL = "1";
    public static final String STATUS_STOP = "0";

	/** login status end */

    public static final char GAP_VERTICAL = '|';
    public static final char GAP_COMMA = ',';
    public static final char GAP_M_L_BRACKET = '[';
    public static final char GAP_COLON = ':';
    public static final char GAP_M_R_BRACKET = ']';


    /**任务执行失败*/
    public static final int TASK_EXEC_STATUS_FAILED = 2;

    /**任务执行成功*/
    public static final int TASK_EXEC_STATUS_SUCCESS = 1;

}
