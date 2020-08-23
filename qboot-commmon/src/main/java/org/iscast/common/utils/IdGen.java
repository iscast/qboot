package org.iscast.common.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 22:32
 */
public class IdGen {
    private static SecureRandom random = new SecureRandom();

    public IdGen() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    public static String randomint() {
        return String.valueOf((new Random()).nextInt(899999) + 100000);
    }
}
