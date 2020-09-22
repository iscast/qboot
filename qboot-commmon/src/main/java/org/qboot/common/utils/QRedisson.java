package org.qboot.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:20
 */
@Component
public class QRedisson {
    private static Logger logger = LoggerFactory.getLogger(QRedisson.class);
    @Autowired
    private RedissonClient redissonClient;
    private final Long DEFAULT_CACHE_SECONDS = 3600L;
    private final String LOCK_KEY = "REDISSON_LOCK_";

    public QRedisson() {
    }

    public <V> V get(String key) {
        RBucket<V> bucket = null;
        V value = null;
        try {
            bucket = this.redissonClient.getBucket(key);
            if (bucket.isExists()) {
                value = bucket.get();
            }

            logger.debug("get {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("get {} = {}", new Object[]{key, value, ExceptionUtils.getStackTrace(e)});
        }
        return value;
    }

    public <V> void set(String key, V value) {
        this.set(key, value, this.DEFAULT_CACHE_SECONDS);
    }

    public <V> void set(String key, V value, long cacheSeconds) {
        RBucket bucket = null;

        try {
            bucket = this.redissonClient.getBucket(key);
            if (cacheSeconds <= 0L) {
                cacheSeconds = this.DEFAULT_CACHE_SECONDS;
            }

            bucket.set(value, cacheSeconds, TimeUnit.SECONDS);
            logger.debug("set {} = {}", key, bucket.get());
        } catch (Exception var7) {
            logger.warn("set {} = {}", new Object[]{key, bucket.get(), ExceptionUtils.getStackTrace(var7)});
        }

    }

    public boolean del(String key) {
        RBucket<?> bucket = null;
        boolean isDel = false;

        try {
            bucket = this.redissonClient.getBucket(key);
            if (bucket.isExists()) {
                isDel = bucket.delete();
            }

            logger.debug("del {} :{}", key, isDel);
        } catch (Exception var5) {
            logger.warn("del {}", key, ExceptionUtils.getStackTrace(var5));
        }

        return isDel;
    }

    public boolean expire(String key, long secondsToLive) {
        RBucket<?> bucket = null;
        boolean isExpire = false;

        try {
            bucket = this.redissonClient.getBucket(key);
            if (bucket.isExists()) {
                isExpire = bucket.expire(secondsToLive, TimeUnit.SECONDS);
            }

            logger.debug("expire {} :{}", key, isExpire);
        } catch (Exception var7) {
            logger.warn("expire {}", key, ExceptionUtils.getStackTrace(var7));
        }

        return isExpire;
    }

    public <K, V> Map<K, V> getMap(String key) {
        RMap<K, V> map = null;
        Map value = null;

        try {
            map = this.redissonClient.getMap(key);
            if (map.isExists()) {
                value = map.readAllMap();
                logger.debug("getMap {} = {}", key, value);
            }
        } catch (Exception var5) {
            logger.warn("getMap {} = {}", new Object[]{key, value, ExceptionUtils.getStackTrace(var5)});
        }

        return value;
    }

    public <K, V> Map<K, V> setMap(String key, Map<K, V> value) {
        return this.setMap(key, value, this.DEFAULT_CACHE_SECONDS);
    }

    public <K, V> Map<K, V> setMap(String key, Map<K, V> value, long cacheSeconds) {
        RMap map = null;

        try {
            map = this.redissonClient.getMap(key);
            map.putAll(value);
            if (cacheSeconds <= 0L) {
                cacheSeconds = this.DEFAULT_CACHE_SECONDS;
            }

            map.expire(cacheSeconds, TimeUnit.SECONDS);
            logger.debug("setMap {} = {}", key, value);
        } catch (Exception var7) {
            logger.warn("setMap {} = {}", new Object[]{key, value, ExceptionUtils.getStackTrace(var7)});
        }

        return map;
    }

    public Long incr(String key, Long value) {
        Long decrVaule = -Math.abs(value);
        if (StringUtils.isBlank(key)) {
            return -1L;
        } else {
            RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
            if (!atomicLong.isExists()) {
                return -1L;
            } else {
                try {
                    Long result = atomicLong.addAndGet(decrVaule);
                    return result;
                } catch (Exception var6) {
                    logger.error("decr error:{}", ExceptionUtils.getStackTrace(var6));
                    return -1L;
                }
            }
        }
    }

    public <V> List<V> getList(String key) {
        RList<V> rList = null;
        List value = null;

        try {
            rList = this.redissonClient.getList(key);
            if (rList.isExists()) {
                value = rList.readAll();
                logger.debug("getList {} = {}", key, value);
            }
        } catch (Exception var5) {
            logger.warn("getList {} = {}", new Object[]{key, value, ExceptionUtils.getStackTrace(var5)});
        }

        return value;
    }

    public <V> void setList(String key, List<V> value) {
        this.setList(key, value, this.DEFAULT_CACHE_SECONDS);
    }

    public <V> void setList(String key, List<V> value, long cacheSeconds) {
        RList rList = null;

        try {
            rList = this.redissonClient.getList(key);
            if (cacheSeconds <= 0L) {
                cacheSeconds = this.DEFAULT_CACHE_SECONDS;
            }

            rList.expire(cacheSeconds, TimeUnit.SECONDS);
            rList.addAll(value);
            logger.debug("setList {} = {}", key, value);
        } catch (Exception var7) {
            logger.warn("setList {} = {}", new Object[]{key, value, ExceptionUtils.getStackTrace(var7)});
        }

    }

    public boolean lock(String lockname, long timeout) {
        String key = "REDISSON_LOCK_" + lockname;
        RLock lock = this.getRLock(key);

        try {
            boolean var7;
            try {
                boolean getLock = lock.tryLock(1L, timeout, TimeUnit.SECONDS);
                if (!getLock) {
                    logger.debug("lockKey:{} 获取锁失败", key);
                    var7 = false;
                    return var7;
                }

                logger.debug("lockKey:{} 加锁成功", key);
            } catch (Exception var11) {
                logger.error("lockKey:{} 加锁失败:{}", key, ExceptionUtils.getStackTrace(var11));
                this.unlock(key);
                var7 = false;
                return var7;
            }
        } finally {
            if (!lock.isLocked()) {
                lock.unlock();
            }

        }

        return true;
    }

    public boolean lock(String lockname) {
        return this.lock(lockname, 60L);
    }

    public void unlock(String lockname) {
        String key = "REDISSON_LOCK_" + lockname;
        RLock lock = this.getRLock(key);
        lock.unlock();
        logger.debug("lockKey:{} 解锁成功", key);
    }

    public RLock getRLock(String key) {
        RLock rLock = this.redissonClient.getLock(key);
        return rLock;
    }
}
