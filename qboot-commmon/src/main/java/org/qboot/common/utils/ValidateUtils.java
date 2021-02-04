package org.qboot.common.utils;

import org.qboot.common.exception.ParamFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.qboot.common.constants.SysConstants.*;

/**
 * validate bean utils
 * @Author: iscast
 * @Date: 2020/10/13 16:13
 */
public class ValidateUtils {
    private static Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

    public static Boolean checkBind(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(null == fieldErrors || fieldErrors.size() == 0) {
            return true;
        }

        StringBuilder errFile = new StringBuilder("field error : ");
        for(FieldError fieldError : fieldErrors) {
            errFile.append(GAP_M_L_BRACKET);
            errFile.append(fieldError.getField());
            errFile.append(GAP_COLON);
            errFile.append(fieldError.getDefaultMessage());
            errFile.append(GAP_M_R_BRACKET);
        }
        logger.warn("{} {}", bindingResult.getObjectName(), errFile.toString());
        throw new ParamFailException(errFile.toString());
    }

}
