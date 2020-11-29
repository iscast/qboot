package org.qboot.common.exception.errorcode;

import org.qboot.common.error.ErrorCode;

import static org.qboot.common.constants.SysConstants.*;

/**
 * 系统错误码 定义常用错误码
 * 1            成功
 * 2xxxx        接口错误
 * 3xxxx        系统模块错误
 * 4xxxx        应用模块错误 应用场景自定义错误码都为4xxxxx
 * 6xxxx        系统错误相关(权限/参数错误/实体校验)
 * 7xxxx        数据库相关
 * 8xxxx        缓存相关
 * -1           通用失败
 * @Author: iscast
 * @Date: 2020/11/4 14:12
 */
public class SystemErrTable {

    public static final ErrorCode SUCESS = new ErrorCode(GLOBAL_DEFAULT_SUCCESS, GLOBAL_DEFAULT_SUCCESS_MSG);
    public static final ErrorCode ERR = new ErrorCode(GLOBAL_DEFAULT_ERROR, GLOBAL_DEFAULT_ERROR_MSG);

    public static final ErrorCode AUTH_FAIL = new ErrorCode(60000, "auth.no.login");
    public static final ErrorCode AUTH_NO_RIGHTS = new ErrorCode(60001, "auth.no.right");
    public static final ErrorCode AUTH_REPEAT_LOGIN = new ErrorCode(60002, "auth.repeat.login");

    public static final ErrorCode SYS_PARAM_ILLEGAL = new ErrorCode(62001, "sys.param.illegal");
    public static final ErrorCode SYS_PARAM_BIND_ERROR = new ErrorCode(62002, "sys.param.bind.error");
    public static final ErrorCode SYS_PARAM_ID_NULL_ERROR = new ErrorCode(62003, "sys.param.id.null.error");
    public static final ErrorCode SYS_SAVE_POJO_NULL_ERROR = new ErrorCode(62004, "sys.save.pojo.null.error");
    public static final ErrorCode SYS_UPDATE_POJO_NULL_ERROR = new ErrorCode(62005, "sys.update.pojo.null.error");
    public static final ErrorCode SYS_DELETE_POJO_NULL_ERROR = new ErrorCode(62006, "sys.delete.pojo.null.error");
    public static final ErrorCode SYS_PARAM_POJO_ID_NULL_ERROR = new ErrorCode(62007, "sys.param.pojo.id.null.error");

}
