package org.qboot.sys.exception;

import org.qboot.common.error.IError;
import org.qboot.common.exception.ErrorCodeException;

/**
 * 系统用户错误
 * @Author: iscast
 * @Date: 2020/11/4 10:11
 */
public class SysUserException extends ErrorCodeException {
    public SysUserException(IError errorObj) {
        super(errorObj);
    }
}
