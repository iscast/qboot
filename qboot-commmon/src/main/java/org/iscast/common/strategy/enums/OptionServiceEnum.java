package org.iscast.common.strategy.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:26
 */
public enum OptionServiceEnum {
    DEFAULT("0000000", "DEFAULT");

    private final String code;
    private final String module;

    private OptionServiceEnum(String code, String module) {
        this.module = module;
        this.code = code;
    }

    public String getModule() {
        return this.module;
    }

    public String getCode() {
        return this.code;
    }

    public static String getModuleByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        } else {
            OptionServiceEnum[] values = values();
            int l = values.length;

            for(int i = 0; i < l; ++i) {
                OptionServiceEnum value = values[i];
                if (StringUtils.equals(code, value.getCode())) {
                    return value.getModule();
                }
            }

            return null;
        }
    }
}
