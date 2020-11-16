package org.qboot.sys.exception;

import org.qboot.common.error.IError;
import org.qboot.common.exception.ErrorCodeException;

/**
 * 任务错误
 * @Author: iscast
 * @Date: 2020/11/4 10:11
 */
public class SysTaskException extends ErrorCodeException {
    public SysTaskException(IError errorObj) {
        super(errorObj);
    }
}
