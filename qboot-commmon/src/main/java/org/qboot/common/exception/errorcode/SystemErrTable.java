package org.qboot.common.exception.errorcode;

import org.qboot.common.error.ErrorCode;

import static org.qboot.common.constants.SysConstants.*;

/**
 * 系统错误码
 * 系统已定义常用错误码，其他场景自定义4xxxxx
 * 1            成功
 * 2xxxx        接口错误
 * 3xxxx        系统模块错误
 * 4xxxx        应用级别错误
 * 6xxxx        权限相关
 * 7xxxx        数据库相关
 * 8xxxx        缓存相关
 * -1           通用失败
 * @Author: iscast
 * @Date: 2020/11/4 14:12
 */
public class SystemErrTable {


    public static final ErrorCode SUCESS = new ErrorCode(GLOBAL_DEFAULT_SUCCESS, "err");
    public static final ErrorCode ERR = new ErrorCode(GLOBAL_DEFAULT_ERROR, "err");

    public static final ErrorCode AUTH_FAIL = new ErrorCode(60000, "auth.no.login");
    public static final ErrorCode AUTH_NO_RIGHTS = new ErrorCode(60001, "auth.no.right");
    public static final ErrorCode AUTH_REPEAT_LOGIN = new ErrorCode(60002, "auth.repeat.login");




}
