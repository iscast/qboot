package org.qboot.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qboot.common.constants.CacheConstants;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * common redis tools
 * @Author: iscast
 * @Date: 2020/8/22 19:20
 */
@Component
public class RedisTools {
    private static Logger logger = LoggerFactory.getLogger(RedisTools.class);
    @Autowired
    private RedissonClient redissonClient;
    private final Long DEFAULT_CACHE_SECONDS = 3600L;

    public RedisTools() {
    }

    public <V> V get(String key) {
        RBucket<V> bucket = null;
        V value = null;
        try {
            bucket = this.redissonClient.getBucket(key);
            if (bucket.isExists()) {
                value = bucket.get();
            }
        } catch (Exception e) {
            logger.warn("get {} = {}", new Object[]{key, value, ExceptionUtils.getStackTrace(e)});
        }
        return value;
    }

    public <V> void set(String key, V value) {
        this.set(key, value, this.DEFAULT_CACHE_SECONDS);
    }

    public <V> void setNoLimit(String key, V value) {

        RBucket bucket = null;
        try {
            bucket = this.redissonClient.getBucket(key);
            bucket.set(value);
            logger.debug("setNoLimit {} = {}", key, bucket.get());
        } catch (Exception var7) {
            logger.warn("setNoLimit {} = {}", new Object[]{key, bucket.get(), ExceptionUtils.getStackTrace(var7)});
        }

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

            logger.debug("del cache key:{} result:{}", key, isDel);
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
            logger.debug("expire cache key:{} result:{}", key, isExpire);
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
        return incrWithTime(key, value, null);
    }

    public Long incrWithTime(String key, Long value, Long time) {
        Long incrVaule = Math.abs(value);
        if (StringUtils.isBlank(key)) {
            return -1L;
        } else {
            RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);

            if (!atomicLong.isExists()) {
                atomicLong.set(incrVaule);
                if(null != time) {
                    atomicLong.expire(time, TimeUnit.SECONDS);
                }
                return incrVaule;
            }

            try {
                Long result = null;
                if(incrVaule > 0L) {
                    result = atomicLong.addAndGet(incrVaule);
                } else {
                    result = atomicLong.get();
                }

                return result;
            } catch (Exception e) {
                logger.error("increase redis longValue error:{}", ExceptionUtils.getStackTrace(e));
                return -1L;
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
        String key = CacheConstants.CACHE_LOCK_KEY + lockname;
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
        String key = CacheConstants.CACHE_LOCK_KEY + lockname;
        RLock lock = this.getRLock(key);
        lock.unlock();
        logger.debug("lockKey:{} 解锁成功", key);
    }

    public RLock getRLock(String key) {
        RLock rLock = this.redissonClient.getLock(key);
        return rLock;
    }
}
