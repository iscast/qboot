package org.qboot.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

    private static Logger logger = LoggerFactory.getLogger(Base64Util.class);
    private final static Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private final static Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    public static String encode(String str) {
        byte[] textByte;
        try {
            textByte = str.getBytes(StandardCharsets.UTF_8);
            return BASE64_ENCODER.encodeToString(textByte);
        } catch (Exception e) {
            logger.warn("base64 encode fail", e);
            return str;
        }
    }

    public static String decode(String str) {

        try {
            return new String(BASE64_DECODER.decode(str), StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.warn("base64 decode fail", e);
            return str;
        }

    }

    public static String encode2(byte[] textByte) {
        return BASE64_ENCODER.encodeToString(textByte);

    }

    public static byte[] decode2(String str) {
        return BASE64_DECODER.decode(str);
    }

}
