package org.qboot.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.CacheConstants;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.service.BaseService;
import org.qboot.common.utils.RedisTools;
import org.qboot.sys.dto.SysUserDto;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
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
	@Autowired
	private SysUserService sysUserService;

	private static final Long LOGIN_FAIL_EXPIRE_TIME = 24 * 60 * 60L;
	private static final Long LOGIN_FAIL_COUNT = 5L;

	public void unLock(String loginName) {
		Assert.hasLength(loginName, "login Name IsEmpty");
		redisTools.del(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName);
		SysUserDto unlockUser = new SysUserDto();
		unlockUser.setLoginName(loginName);
		unlockUser.setStatus(SysConstants.SYS_ENABLE);
        unlockUser.setUpdateDate(new Date());
        String currentUser = SecurityUtils.getLoginName();
        if(StringUtils.isBlank(currentUser)) {
            currentUser = SysConstants.SYSTEM_CRATER_NAME;
        }
        unlockUser.setUpdateBy(currentUser);
		sysUserService.setStatus(unlockUser);
	}

	public Long getLoginFailCount(String loginName) {
		Long cnt = redisTools.get(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName);
		return null == cnt ? 0 : cnt;
	}

	public boolean isLocked(String loginName) {
		return getLoginFailCount(loginName) >= LOGIN_FAIL_COUNT;
	}

	public Long incrementLoginFailTimes(String loginName) {
		Long increment = redisTools.incrWithTime(CacheConstants.CACHE_PREFIX_LOGIN_FAILCOUNT + loginName, 1L, LOGIN_FAIL_EXPIRE_TIME);
		if(null != increment && increment >= LOGIN_FAIL_COUNT) {
			SysUserDto lockUser = new SysUserDto();
			lockUser.setLoginName(loginName);
			lockUser.setStatus(SysConstants.SYS_DISABLE);
            lockUser.setUpdateDate(new Date());
            lockUser.setUpdateBy(SysConstants.SYSTEM_CRATER_NAME);
			sysUserService.setStatus(lockUser);
		}
		return increment;
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
