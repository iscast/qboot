package org.qboot.base.service.impl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.qboot.common.service.BaseService;
import org.qboot.common.utils.QRedisson;
import org.qboot.common.utils.i18n.MessageUtil;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>
 * Title: LoginSecurityService
 * </p>
 * <p>
 * Description: 安全登录
 * </p>
 * 
 * @author history
 * @date 2018-08-08
 */
@Service
public class LoginSecurityService extends BaseService {

	private String LOCK_PREFIX = "LOGIN_CNT_";

	private String USER_NAME_SESSION_ID_PREFIX = "USERNAME_SESSION_ID:";

	@Autowired
	private QRedisson qRedisson;
	@Autowired(required = false)
	private RedissonClient redissonClient;

	public void unLock(String loginName) {
		Assert.hasLength(loginName, MessageUtil.getMessage("sys.response.msg.loginNameIsEmpty", "loginName 为空"));
		qRedisson.del(LOCK_PREFIX + loginName);
	}

	public Long getLoginFailCount(String loginName) {
		try {
			Long cnt = qRedisson.get(LOCK_PREFIX + loginName);
			return null == cnt ? 0 : cnt;
		} finally {
			qRedisson.expire(LOCK_PREFIX + loginName, 24 * 60 * 60);
		}
	}

	public boolean isLocked(String loginName) {
		return getLoginFailCount(loginName) >= 5;
	}

	public Long incrementLoginFailTimes(String loginName) {
		try {
			Long increment = qRedisson.incr(LOCK_PREFIX + loginName, 1L);
			return increment;
		} finally {
			qRedisson.expire(LOCK_PREFIX + loginName, 24 * 60 * 60);
		}
	}

	public void clearLoginFailTimes(String loginName) {
		qRedisson.del(LOCK_PREFIX + loginName);
	}

	/**
	 * 清除登录用户session
	 * 
	 * @param loginName
	 *            loginName
	 */
	public void clearUserSessions(String loginName) {
		logger.info("[强制退出] loginName={}", loginName);
		Set<Object> sessionIdSet = redissonClient.getSet(USER_NAME_SESSION_ID_PREFIX + loginName);
		if (sessionIdSet != null) {
			sessionIdSet.forEach((sessionId) -> qRedisson.del("redisson_spring_session:" + sessionId));
		}
		qRedisson.del(USER_NAME_SESSION_ID_PREFIX + loginName);
	}

	/**
	 * 设置用户名与session对应的缓存集合
	 * 
	 * @param loginName
	 *            loginName
	 * @param sessionId
	 *            sessionId
	 */
	public void setUserSessionId(String loginName, String sessionId) {
		RSet<String> set = redissonClient.getSet(USER_NAME_SESSION_ID_PREFIX + loginName);
		set.add(sessionId);
		set.expire(24 * 60 * 60, TimeUnit.SECONDS);
	}
}
