package org.qboot.common.error;

/**
 * 通用错误码
 * @Author: iscast
 * @Date: 2020/11/4 09:49
 */
public class ErrorCode implements IError {

    private int errorCode;
    private String errorInfo;

    public ErrorCode(int errorCode, String errorInfo) {
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorInfo() {
        return this.errorInfo;
    }
}
