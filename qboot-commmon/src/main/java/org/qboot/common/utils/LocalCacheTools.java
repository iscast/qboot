package org.qboot.common.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存类
 * @Author: iscast
 * @Date: 2021/2/5 15:03
 */
public class LocalCacheTools {
    private static Logger logger = LoggerFactory.getLogger(LocalCacheTools.class);

    private final static Integer DEFAULT_CACHE_TIME = 60;
    private static Cache<String, Long> lCache;
    static {
        lCache = CacheBuilder.newBuilder()
                .expireAfterWrite(DEFAULT_CACHE_TIME, TimeUnit.SECONDS)
                .build();
    }

    public static void setL(String key, Long value) {
        try {
            lCache.put(key,value);
        } catch (Exception ex) {
            logger.error("local cache set {} = {} fail ", ex.getMessage());
        }
    }

    public static Long getL(String key) {
        return lCache.getIfPresent(key);
    }
}
