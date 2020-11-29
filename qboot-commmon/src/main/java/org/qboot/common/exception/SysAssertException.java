package org.qboot.common.exception;

import org.qboot.common.error.ErrorCode;

/**
 * 断言异常类
 * @Author: iscast
 * @Date: 2020/11/16 16:26
 */
public class SysAssertException extends ErrorCodeException {

    public SysAssertException(ErrorCode errorCode) {
        super(errorCode);
    }
}
