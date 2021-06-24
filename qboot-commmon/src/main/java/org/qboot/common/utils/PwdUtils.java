package org.qboot.common.utils;

import org.apache.commons.lang3.RandomStringUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2021/6/24 09:37
 */
public class PwdUtils {

    public static String encrypt(String password,String salt) {
        return CodecUtils.sha256(password + salt);
    }

    public static Tuple2<String, String> generatePwdAndSalt(String password) {
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String encryptPwd = encrypt(password, salt);
        return Tuples.of(encryptPwd, salt);
    }

    public static void main(String[] args) {
        System.out.println(generatePwdAndSalt("111111"));
        System.out.println(generatePwdAndSalt("222222"));
    }
}
