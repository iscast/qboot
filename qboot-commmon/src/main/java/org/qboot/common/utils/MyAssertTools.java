package org.qboot.common.utils;

import org.qboot.common.error.ErrorCode;
import org.qboot.common.exception.ErrorCodeException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 内部断言工具
 * @Author: iscast
 * @Date: 2020/11/12 18:10
 */
public class MyAssertTools {

    public static void notNull(@Nullable Object object, ErrorCode errorCode) {
        if (object == null) {
            throw new ErrorCodeException(errorCode);
        }
    }

    public static void hasLength(@Nullable String text, ErrorCode errorCode) {
        if (!StringUtils.hasLength(text)) {
            throw new ErrorCodeException(errorCode);
        }
    }


    public static void notEmpty(@Nullable Collection<?> collection, ErrorCode errorCode) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ErrorCodeException(errorCode);
        }
    }


}
