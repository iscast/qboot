package org.qboot.sys.exception;

import org.qboot.common.error.IError;
import org.qboot.common.exception.ErrorCodeException;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/11/4 11:18
 */
public class SysMenuException extends ErrorCodeException {
    public SysMenuException(IError errorObj) {
        super(errorObj);
    }
}
