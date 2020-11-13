package org.qboot.sys.exception.errorcode;

import org.qboot.common.error.ErrorCode;

/**
 * 系统模块错误 通用
 * @Author: iscast
 * @Date: 2020/11/4 10:13
 */
public class SysModuleErrTable {

    // menu 302xx
    public static final ErrorCode SYS_MENU_NO_AUTH = new ErrorCode(30200, "sys.menu.no.auth");
    public static final ErrorCode SYS_MENU_NO_EXIST = new ErrorCode(30201, "sys.menu.no.exist");
    public static final ErrorCode SYS_MENU_DUPLICATE = new ErrorCode(30202, "sys.menu.duplicate");
    public static final ErrorCode SYS_MENU_CANNOT_DISABLE = new ErrorCode(30203, "sys.menu.cannot.disable");
    public static final ErrorCode SYS_MENU_PARENTID_NULL = new ErrorCode(30204, "sys.menu.parentid.null");
    public static final ErrorCode SYS_MENU_LIST_EMPTY = new ErrorCode(30205, "sys.menu.list.empty");
    public static final ErrorCode SYS_MENU_PERMISSION_NULL = new ErrorCode(30206, "sys.menu.permission.null");
    public static final ErrorCode SYS_MENU_USER_ID_NULL = new ErrorCode(30207, "sys.menu.user.id.null");

    public static final ErrorCode SYS_MENU_SAVE_FAIL = new ErrorCode(30240, "sys.menu.save.fail");
    public static final ErrorCode SYS_MENU_UPDATE_FAIL = new ErrorCode(30241, "sys.menu.update.fail");
    public static final ErrorCode SYS_MENU_DELETE_FAIL = new ErrorCode(30242, "sys.menu.delete.fail");
    public static final ErrorCode SYS_MENU_QUERY_FAIL = new ErrorCode(30243, "sys.menu.query.fail");

    // role 303xx
    public static final ErrorCode SYS_ROLE_DUPLICATE = new ErrorCode(30302, "sys.role.duplicate");
    public static final ErrorCode SYS_ROLE_REMOVED_USER_NOT_EXIST = new ErrorCode(30303, "sys.role.removed.user.not.exist");
    public static final ErrorCode SYS_ROLE_ADD_USER_NOT_EXIST = new ErrorCode(30304, "sys.role.add.user.not.exist");
    public static final ErrorCode SYS_ROLE_USER_ID_NULL = new ErrorCode(30305, "sys.role.user.id.null");
    public static final ErrorCode SYS_ROLE_USER_IDS_NULL = new ErrorCode(30306, "sys.role.user.ids.null");
    public static final ErrorCode SYS_ROLE_ID_NULL = new ErrorCode(30307, "sys.role.id.null");
    public static final ErrorCode SYS_ROLE_NAME_NULL = new ErrorCode(30308, "sys.role.name.null");

    public static final ErrorCode SYS_ROLE_SAVE_FAIL = new ErrorCode(30340, "sys.role.save.fail");
    public static final ErrorCode SYS_ROLE_UPDATE_FAIL = new ErrorCode(30341, "sys.role.update.fail");
    public static final ErrorCode SYS_ROLE_DELETE_FAIL = new ErrorCode(30342, "sys.role.delete.fail");
    public static final ErrorCode SYS_ROLE_QUERY_FAIL = new ErrorCode(30343, "sys.role.query.fail");

    // param_type param_class 304xx
    public static final ErrorCode SYS_PARAM_CLASS_ID_NULL = new ErrorCode(30400, "sys.param.class.id.null");
    public static final ErrorCode SYS_PARAM_TYPE_ID_NULL = new ErrorCode(30401, "sys.param.type.id.null");
    public static final ErrorCode SYS_PARAM_TYPE_CLASS_NULL = new ErrorCode(30402, "sys.param.type.class.null");

    public static final ErrorCode SYS_PARAM_TYPE_SAVE_FAIL = new ErrorCode(30440, "sys.param.type.save.fail");
    public static final ErrorCode SYS_PARAM_TYPE_UPDATE_FAIL = new ErrorCode(30441, "sys.param.type.update.fail");
    public static final ErrorCode SYS_PARAM_TYPE_DELETE_FAIL = new ErrorCode(30442, "sys.param.type.delete.fail");
    public static final ErrorCode SYS_PARAM_TYPE_QUERY_FAIL = new ErrorCode(30443, "sys.param.type.query.fail");

    public static final ErrorCode SYS_PARAM_CLASS_SAVE_FAIL = new ErrorCode(30450, "sys.param.class.save.fail");
    public static final ErrorCode SYS_PARAM_CLASS_UPDATE_FAIL = new ErrorCode(30451, "sys.param.class.update.fail");
    public static final ErrorCode SYS_PARAM_CLASS_DELETE_FAIL = new ErrorCode(30452, "sys.param.class.delete.fail");
    public static final ErrorCode SYS_PARAM_CLASS_QUERY_FAIL = new ErrorCode(30453, "sys.param.class.query.fail");

    // dict 305xx
    public static final ErrorCode SYS_DICT_ID_NULL = new ErrorCode(30500, "sys.dict.id.null");
    public static final ErrorCode SYS_DICT_TYPE_NULL = new ErrorCode(30501, "sys.dict.type.null");
    public static final ErrorCode SYS_DICT_LIST_EMPTY = new ErrorCode(30502, "sys.dict.list.empty");
    public static final ErrorCode SYS_DICT_NO_EXIST = new ErrorCode(30502, "sys.dict.no.exist");

    public static final ErrorCode SYS_DICT_SAVE_FAIL = new ErrorCode(30540, "sys.dict.save.fail");
    public static final ErrorCode SYS_DICT_UPDATE_FAIL = new ErrorCode(30541, "sys.dict.update.fail");
    public static final ErrorCode SYS_DICT_DELETE_FAIL = new ErrorCode(30542, "sys.dict.delete.fail");
    public static final ErrorCode SYS_DICT_QUERY_FAIL = new ErrorCode(30543, "sys.dict.query.fail");

    // task 309xx

    public static final ErrorCode SYS_TASK_SAVE_FAIL = new ErrorCode(30940, "sys.task.save.fail");
    public static final ErrorCode SYS_TASK_UPDATE_FAIL = new ErrorCode(30941, "sys.task.update.fail");
    public static final ErrorCode SYS_TASK_DELETE_FAIL = new ErrorCode(30942, "sys.task.delete.fail");
    public static final ErrorCode SYS_TASK_QUERY_FAIL = new ErrorCode(30943, "sys.task.query.fail");
}
