package org.qboot.sys.service.impl;

import org.qboot.common.constants.CacheConstants;
import org.qboot.common.service.BaseService;
import org.qboot.common.utils.RedisTools;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: LoginSecurityService</p>
 * <p>Description: 安全登录</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class LoginSecurityService extends BaseService {


	@Autowired
	private RedisTools redisTools;
	@Autowired(required = false)
	private RedissonClient redissonClient;

	public void unLock(String loginName) {
		Assert.hasLength(loginName, "login Name IsEmpty");
		redisTools.del(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName);
	}

	public Long getLoginFailCount(String loginName) {
		try {
			Long cnt = redisTools.get(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName);
			return null == cnt ? 0 : cnt;
		} finally {
			redisTools.expire(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName, 24 * 60 * 60);
		}
	}

	public boolean isLocked(String loginName) {
		return getLoginFailCount(loginName) >= 5;
	}

	public Long incrementLoginFailTimes(String loginName) {
		try {
			Long increment = redisTools.incr(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName, 1L);
			return increment;
		} finally {
			redisTools.expire(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName, 24 * 60 * 60);
		}
	}

	public void clearLoginFailTimes(String loginName) {
		redisTools.del(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName);
	}

	/**
	 * clear login user session
	 * @param loginName
	 */
	public void clearUserSessions(String loginName) {
		logger.info("force logout user loginName:{}", loginName);
		Set<Object> sessionIdSet = redissonClient.getSet(CacheConstants.CACHE_PREFIX_LOGIN_USERNAME_SESSION + loginName);
		if (sessionIdSet != null) {
			sessionIdSet.forEach((sessionId) -> redisTools.del("redisson_spring_session:" + sessionId));
		}
		redisTools.del(CacheConstants.CACHE_PREFIX_LOGIN_USERNAME_SESSION + loginName);
	}

	/**
     * add username and sessionId cache set
	 * @param loginName
	 * @param sessionId
	 */
	public void setUserSessionId(String loginName, String sessionId) {
		RSet<String> set = redissonClient.getSet(CacheConstants.CACHE_PREFIX_LOGIN_USERNAME_SESSION + loginName);
		set.add(sessionId);
		set.expire(24 * 60 * 60, TimeUnit.SECONDS);
	}
}
