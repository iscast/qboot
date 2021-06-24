package org.qboot.common.utils;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.qboot.common.constants.CacheConstants;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        V value = null;
        try {
            RBucket<V> bucket = this.redissonClient.getBucket(key);
            if (bucket.isExists()) {
                value = bucket.get();
            }
        } catch (Exception e) {
            logger.error("getCache {}", key,e);
        }
        return value;
    }

    public Set<String> getKeys(String pattern) {
        Set<String> result = new HashSet<>();
        try {
            RKeys keys = redissonClient.getKeys();
            Iterable<String> keysByPattern = keys.getKeysByPattern(pattern);
            keysByPattern.forEach(result::add);
        } catch (Exception e) {
            logger.error("getCacheKeys By Patten {} fail", pattern, e);
        }
        return result;
    }

    public <V> void set(String key, V value) {
        this.set(key, value, this.DEFAULT_CACHE_SECONDS);
    }

    public <V> void setNoLimit(String key, V value) {
        try {
            RBucket bucket = this.redissonClient.getBucket(key);
            bucket.set(value);
        } catch (Exception e) {
            logger.error("setCacheNoLimit {} fail", key, e);
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
        } catch (Exception e) {
            logger.error("setCache {} fail", key, e);
        }

    }

    public boolean del(String key) {
        boolean isDel = false;
        try {
            RBucket<?> bucket = this.redissonClient.getBucket(key);
            if (bucket.isExists()) {
                isDel = bucket.delete();
            }
        } catch (Exception e) {
            logger.error("delCache {} fail", key, e);
        }
        return isDel;
    }

    public boolean expire(String key, long secondsToLive) {
        boolean isExpire = false;
        try {
            RBucket<?> bucket = this.redissonClient.getBucket(key);
            if (bucket.isExists()) {
                isExpire = bucket.expire(secondsToLive, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("expireCache {} fail", key, e);
        }

        return isExpire;
    }

    public <K, V> Map<K, V> getMap(String key) {
        Map value = null;
        try {
            RMap<K, V> map = this.redissonClient.getMap(key);
            if (map.isExists()) {
                value = map.readAllMap();
            }
        } catch (Exception e) {
            logger.error("getCacheMap {} fail", key, e);
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
        } catch (Exception e) {
            logger.error("setCacheMap {} fail", key, e);
        }
        return map;
    }

    public Long incr(String key, Long time) {
        if (StringUtils.isBlank(key)) {
            return -1L;
        }

        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
        try {
            if (!atomicLong.isExists()) {
                atomicLong.set(1L);
                if(null != time) {
                    atomicLong.expire(time, TimeUnit.SECONDS);
                }
                return 1L;
            }
            return atomicLong.incrementAndGet();
        } catch (Exception e) {
            logger.error("increase Cache {} fail", key, ExceptionUtils.getStackTrace(e));
        }
        return -1L;
    }

    public Long decr(String key) {
        if (StringUtils.isBlank(key)) {
            return -1L;
        }

        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
        try {
            if (!atomicLong.isExists()) {
                return -1L;
            }
            return atomicLong.decrementAndGet();
        } catch (Exception e) {
            logger.error("decrease Cache {} fail", key, ExceptionUtils.getStackTrace(e));
        }
        return -1L;
    }

    public Long getLong(String key) {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
        if (null != atomicLong && atomicLong.isExists()) {
            return atomicLong.get();
        }
        return -1L;
    }

    public <V> List<V> getList(String key) {
        List value = null;
        try {
            RList<V> rList = this.redissonClient.getList(key);
            if (rList.isExists()) {
                value = rList.readAll();
            }
        } catch (Exception e) {
            logger.error("getCacheList {} fail", key, e);
        }
        return value;
    }

    public <V> PageInfo<V> getPage(String key, int pages, int pageLimit) {
        PageInfo<V> result = new PageInfo<>();
        result.setTotal(0);
        if(pages <= 0 || pageLimit <= 0) {
            result.setPages(1);
            result.setPageSize(0);
            return result;
        }

        result.setPageSize(pageLimit);
        result.setPages(pages--);

        try {
            RList<V> rList = this.redissonClient.getList(key);
            if (null == rList || !rList.isExists()) {
                logger.error("getCachePage {} fail no exist", key);
                return result;
            }
            int start = pages * pageLimit;
            int end = start + pageLimit;
            int size = rList.size();
            if(start >= size) {
                logger.error("getCachePage {} fail pages:{} greater than size{}", key, pages, size);
                result.setTotal(size);
                return result;
            }
            if(end > size) {
                end = size;
            }

            RList<V> subLst = rList.subList(start, end);
            result.setList(subLst.readAll());
            result.setTotal(size);
        } catch (Exception e) {
            logger.error("getCachePage {} fail", key, e);
        }
        return result;
    }

    public <V> void setList(String key, List<V> value) {
        this.setList(key, value, this.DEFAULT_CACHE_SECONDS);
    }

    public <V> void setList(String key, List<V> value, long cacheSeconds) {
        try {
            RList rList = this.redissonClient.getList(key);
            if (cacheSeconds <= 0L) {
                cacheSeconds = this.DEFAULT_CACHE_SECONDS;
            }
            rList.expire(cacheSeconds, TimeUnit.SECONDS);
            rList.addAll(value);
        } catch (Exception e) {
            logger.error("setCacheList {} fail", key,e);
        }

    }

    public boolean lock(String lockname, long timeout) {
        String key = CacheConstants.CACHE_LOCK_KEY + lockname;
        RLock lock = this.getRLock(key);

        try {
            boolean flag = false;
            try {
                boolean getLock = lock.tryLock(1L, timeout, TimeUnit.SECONDS);
                if (!getLock) {
                    return flag;
                }
            } catch (Exception e) {
                logger.error("setCacheLock {} fail", key, e);
                this.unlock(key);
                return flag;
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
    }

    public RLock getRLock(String key) {
        RLock rLock = this.redissonClient.getLock(key);
        return rLock;
    }
}
