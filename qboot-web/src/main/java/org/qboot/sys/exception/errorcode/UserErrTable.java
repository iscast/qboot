package org.qboot.sys.exception.errorcode;

import org.qboot.common.error.ErrorCode;

/**
 * 系统用户错误列表
 * @Author: iscast
 * @Date: 2020/11/4 10:13
 */
public class UserErrTable {

    public static final ErrorCode SYS_USER_NOTEXISTS = new ErrorCode(30100, "sys.user.not.exists");
    public static final ErrorCode SYS_USER_DUPLICATE = new ErrorCode(30101, "sys.user.duplicate");
    public static final ErrorCode SYS_USER_LOGINNAME_DUPLICATE = new ErrorCode(30101, "sys.user.loginName.duplicate");

    public static final ErrorCode SYS_USER_SAVE_FAIL = new ErrorCode(30140, "sys.user.save.fail");
    public static final ErrorCode SYS_USER_UPDATE_FAIL = new ErrorCode(30141, "sys.user.update.fail");
}
