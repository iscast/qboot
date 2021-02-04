package org.qboot.common.exception;

import static org.qboot.common.exception.errorcode.SystemErrTable.PARAM_FIELD_ERROR;

/**
 * 参数校验错误
 * @Author: iscast
 * @date: 2021/2/4 13:02
 */
public class ParamFailException extends ErrorCodeException {
    public ParamFailException(String errMsg) {
        super(PARAM_FIELD_ERROR.getErrorCode(), errMsg);
    }
}
