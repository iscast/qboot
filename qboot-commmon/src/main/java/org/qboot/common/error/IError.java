package org.qboot.common.error;

import org.qboot.common.constants.SysConstants;

/**
 * 错误码规则：1位系统/业务级别标识+2位业务标识码+2位错误码
 * @Author: iscast
 * @Date: 2020/11/3 19:53
 */
public interface IError {

    //定义成功的错误码和错误信息
    int SUCC_CODE = SysConstants.GLOBAL_DEFAULT_SUCCESS;
    String SUCC_INFO = SysConstants.SUCESS;

    int getErrorCode();
    String getErrorInfo();
}

