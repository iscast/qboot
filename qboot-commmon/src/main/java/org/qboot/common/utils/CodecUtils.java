package org.qboot.common.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CodecUtils.class);

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

    public static String decodeHex(String hex) {
        try {
            return new String(Hex.decodeHex(hex.toCharArray()));
        } catch (DecoderException e) {
            logger.error("encode hex fail", e);
        }
        return null;
    }

    public static String encodeHex(String str) {
        return new String(Hex.encodeHex(str.getBytes()));
    }

}
