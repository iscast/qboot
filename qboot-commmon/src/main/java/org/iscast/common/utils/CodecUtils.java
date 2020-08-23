package org.iscast.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:23
 */
public class CodecUtils {
    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    public CodecUtils() {
    }

    public static String urlEncode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            return null;
        }
    }

    public static String urlDecode(String text) {
        try {
            return URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            return null;
        }
    }

    public static byte[] base64Decode(String data) {
        return Base64.decodeBase64(data);
    }

    public static String base64Encode(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    public static String md5(String data) {
        Assert.hasLength(data, "待md5 数据为空");
        return DigestUtils.md5Hex(data);
    }

    public static String sha1(String data) {
        Assert.hasLength(data, "待sha1 数据为空");
        return DigestUtils.sha1Hex(data);
    }

    public static String sha256(String data) {
        Assert.hasLength(data, "待sha256 数据为空");
        return DigestUtils.sha256Hex(data);
    }

    public static void main(String[] args) {
        String s = "123456rNBdNtjuefmwLGzXjHoN";

        for(int i = 0; i < 16; ++i) {
            s = sha256(s);
        }

        System.out.println(s);
    }
}
