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
    public static final ErrorCode SYS_USER_LOGINNAME_DUPLICATE = new ErrorCode(30102, "sys.user.loginName.duplicate");
    public static final ErrorCode SYS_USER_FAIL_UPDATE = new ErrorCode(30103, "sys.user.updateId.not.exists");
    public static final ErrorCode SYS_USER_UPDATE_OVER_RIGHT = new ErrorCode(30104, "sys.user.update.over.right");
    public static final ErrorCode SYS_USER_UPDATE_NO_ADMIN = new ErrorCode(30105, "sys.user.update.no.admin");
    public static final ErrorCode SYS_USER_DEL_NO_ADMIN = new ErrorCode(30106, "sys.user.del.no.admin");
    public static final ErrorCode SYS_USER_DEL_NO_SELF = new ErrorCode(30107, "sys.user.del.no.self");
    public static final ErrorCode SYS_USER_UPDATE_NO_SELF = new ErrorCode(30108, "sys.user.update.no.self");
    public static final ErrorCode SYS_USER_INIT_PWD_DENIED = new ErrorCode(30109, "sys.user.init.pwd.denied");
    public static final ErrorCode SYS_USER_ORIGINAL_PWD_INCORRECT = new ErrorCode(30110, "sys.user.original.pwd.incorrect");
    public static final ErrorCode SYS_USER_PWD_CHANGE_NO_ADMIN = new ErrorCode(30111, "sys.user.pwd.change.no.admin");
    public static final ErrorCode SYS_USER_LOGINNAME_EMPTY = new ErrorCode(30112, "sys.user.loginname.empty");
    public static final ErrorCode SYS_USER_PWD_EMPTY = new ErrorCode(30113, "sys.user.pwd.empty");
    public static final ErrorCode SYS_USER_SALT_EMPTY = new ErrorCode(30114, "sys.user.salt.empty");
    public static final ErrorCode SYS_USER_UERID_EMPTY = new ErrorCode(30115, "sys.user.id.empty");
    public static final ErrorCode SYS_USER_NAME_EMPTY = new ErrorCode(30116, "sys.user.name.empty");
    public static final ErrorCode SYS_USER_LOGIN_STATUS_EMPTY = new ErrorCode(30117, "sys.user.login.status.empty");
    public static final ErrorCode SYS_USER_PWD_UPDATE_FAIL = new ErrorCode(30118, "sys.user.pwd.update.fail");
    public static final ErrorCode SYS_USER_STATUS_UPDATE_FAIL = new ErrorCode(30119, "sys.user.status.update.fail");


    public static final ErrorCode SYS_USER_SAVE_FAIL = new ErrorCode(30140, "sys.user.save.fail");
    public static final ErrorCode SYS_USER_UPDATE_FAIL = new ErrorCode(30141, "sys.user.update.fail");
    public static final ErrorCode SYS_USER_DELETE_FAIL = new ErrorCode(30142, "sys.user.delete.fail");
    public static final ErrorCode SYS_USER_QUERY_FAIL = new ErrorCode(30143, "sys.user.query.fail");

    public static final ErrorCode SYS_USER_LOGIN_FIRST_TIME_HINT = new ErrorCode(30180, "sys.user.login.first.time.hint");
    public static final ErrorCode SYS_USER_LANG_INCORRECT = new ErrorCode(30180, "sys.user.lang.incorrect");
}
