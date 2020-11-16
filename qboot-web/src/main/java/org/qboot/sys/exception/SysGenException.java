package org.qboot.sys.exception;

import org.qboot.common.error.IError;
import org.qboot.common.exception.ErrorCodeException;

/**
 * 代码生成异常
 * @Author: iscast
 * @Date: 2020/11/4 11:18
 */
public class SysGenException extends ErrorCodeException {
    public SysGenException(IError errorObj) {
        super(errorObj);
    }

    public SysGenException(int errCode, Throwable e) {
        super(errCode, e);
    }
}
