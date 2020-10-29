package org.qboot.common.utils;

import org.qboot.common.entity.ResponeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * validate utils
 * @Author: iscast
 * @Date: 2020/10/13 16:13
 */
public class ValidateUtils {

    private static Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

    public static ResponeModel fieldError(BindingResult bindingResult) {

        FieldError fieldError = bindingResult.getFieldError();

        Thread thread = Thread.currentThread();
        // get caller and method
        StackTraceElement[] stackTrace = thread.getStackTrace();
        if(null != stackTrace && stackTrace.length>3) {
            StackTraceElement stackTraceElement = stackTrace[2];
            logger.warn("class {}.{} required args[{}] error:{}", stackTraceElement.getClassName(),
                    stackTraceElement.getMethodName(), fieldError.getField(), fieldError.getDefaultMessage());

        } else {
            logger.warn("required args[{}] error:{}", fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponeModel.error("required args[" + fieldError.getField() + "] can't be null or error format");

    }

}
