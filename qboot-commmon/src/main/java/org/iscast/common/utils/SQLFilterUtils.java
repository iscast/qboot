package org.iscast.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 18:47
 */
public class SQLFilterUtils {
    public SQLFilterUtils() {
    }

    public static String sqlFilter(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        } else {
            str = StringUtils.replace(str, "'", "");
            str = StringUtils.replace(str, "\"", "");
            str = StringUtils.replace(str, ";", "");
            str = StringUtils.replace(str, "--", "");
            str = str.toLowerCase();
            String[] keywords = new String[]{"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};
            if (ArrayUtils.contains(keywords, str)) {
                throw new RuntimeException("sql语句有注入风险");
            } else {
                return str;
            }
        }
    }
}
