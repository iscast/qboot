package org.qboot.common.exception;


/**
 * <p>Title: LoginPasswordEncoder</p>
 * <p>Description: 自定义登录密码加密</p>
 * @author history
 * @date 2018-09-11
 */
public class CommonExceptionCode {
	/**
	 * 参数非法
	 */
	public final static String PARAM_INVALID = "601";
	/**
	 * 参数绑定错误
	 */
	public final static String PARAM_BIND_ERROR = "602";
	/**
	 * 非法请求
	 */
	public final static String ILLEGAL = "603";
	/**
	 * 未知错误
	 */
	public final static String UNKNOWN = "699";
	
	/** 任务ID已经存在*/
	public static final String TASK_ID_ISEXIST = "task.id.isexist";
	
	/** 任务名称已经存在*/
	public static final String TASK_NAME_ISEXIST = "task.name.isexist";
	
	/** 任务时间表达式异常*/
	public static final String TASK_CRON_EXP_ERROR = "task.cron.exp.error";
	
	/** 添加定时任务调度异常 */
	public static final String TASK_CREATE_JOB_ERROR = "task.create.job.error";
	
	/** 停止定时任务调度异常 */
	public static final String TASK_DELETE_JOB_ERROR = "task.delete.job.error";
	
	/** 当前任务正在执行中 */
	public static final String TASK_IS_EXECUTING = "task.is.executing";
	
	/** 当前任务运行状态已为初始状态 */
	public static final String TASK_IS_EXEC_INIT = "task.is.exec.init";
	
	/** 任务运行状态修改异常 */
	public static final String TASK_RUNSTATUS_CHANGEERROR = "task.runstatus.changeerror";
	
	/** 启动任务异常 */
	public static final String TASK_INIT_ERROR = "task.init.error";
	
	/** 执行任务异常 */
	public static final String TASK_EXECUTE_ERROR = "task.execute.error";
	
	/** 没有可执行的任务 */
	public static final String TASK_EXECUTE_NULL = "task.execute.null";

	/** 任务正在执行中 */
	public static final String TASK_EXECUTING = "task.executing";

	/** 任务没有可运行的实例 */
	public static final String TASK_RUNNER_NULL = "task.runner.null";
	
	/** 获取运行中的任务异常 */
	public static final String TASK_GET_RUNNING_ERROR = "task.get.running.error";
	
	/**任务执行失败*/
	public static final int TASK_EXEC_STATUS_FAILED = 2;
	
	/**任务执行成功*/
	public static final int TASK_EXEC_STATUS_SUCCESS = 1;
	
	/**获取任务执行锁失败*/
	public static final String TASK_EXECUTE_LOCK_FAILED = "task.get.lock.fail";
}
