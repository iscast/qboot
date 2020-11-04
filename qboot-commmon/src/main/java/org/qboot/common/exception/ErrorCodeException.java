package org.qboot.common.exception;

import org.qboot.common.error.IError;

/**
 * 通用异常
 * @author history
 */
public class ErrorCodeException extends RuntimeException implements IError {

    private static final long serialVersionUID = 1L;
    protected int errorCode;

    public ErrorCodeException(int errorCode, String errorInfo) {
        super(errorInfo);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(IError errorObj) {
        this(errorObj.getErrorCode(),errorObj.getErrorInfo());
    }

    public ErrorCodeException(int errorCode, String errorInfo, Throwable e) {
        super(errorInfo,e);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(int errorCode, Throwable e) {
        super(e);
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorInfo() {
        return getMessage();
    }
}
