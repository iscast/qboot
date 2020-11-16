package org.qboot.common.exception;

import org.qboot.common.error.ErrorCode;
import org.qboot.common.error.IError;

/**
 * 通用异常
 * @author history
 */
public class ErrorCodeException extends RuntimeException implements IError {

    private static final long serialVersionUID = 1L;
    protected int errorCode;

    private ErrorCodeException() {}

    protected ErrorCodeException(int errorCode, String errorInfo) {
        super(errorInfo);
        this.errorCode = errorCode;
    }

    protected ErrorCodeException(IError errorObj) {
        this(errorObj.getErrorCode(),errorObj.getErrorInfo());
    }

    protected ErrorCodeException(int errorCode, String errorInfo, Throwable e) {
        super(errorInfo,e);
        this.errorCode = errorCode;
    }

    protected ErrorCodeException(int errorCode, Throwable e) {
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

    public IError getError() {
        return new ErrorCode(errorCode, getErrorInfo());
    }
}
