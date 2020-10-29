package org.qboot.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.qboot.sys.dao.SysLoginLogDao;
import org.qboot.sys.dto.SysLoginLog;
import org.qboot.sys.dto.SysUser;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.service.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * 登录日志Service
 * @author history
 */
@Service
public class SysLoginLogService extends CrudService<SysLoginLogDao, SysLoginLog>{

	protected Logger logger = LoggerFactory.getLogger(SysLoginLogService.class);
	
	@Autowired
	private SysUserService sysUserService;

	public void loginLogByLoginName(String status,String loginName,String ip,String userAgent,String browser,String deviceName, String area, int firstLogin) {
        Assert.hasLength(status, "loginStatusIsEmpty");

        SysLoginLog loginLog = initPojo(status, ip, userAgent, browser, deviceName, area, firstLogin);

        loginLog.setCreateBy(loginName);
        loginLog.setUpdateBy(loginName);

		SysUser user = sysUserService.findByLoginName(loginName);
		if (null == user) {
            logger.warn("user is not exist, login info {}", loginLog.toString());
            return ;
		}

        logger.info("saving user login log：{}", loginLog.toString());
        loginLog.setUserId(user.getId());
        this.save(loginLog);
	}

    public void loginLogByLoginId(String status,Long userId,String ip,String userAgent,String browser,String deviceName, String area) {

        SysLoginLog loginLog = initPojo(status, ip, userAgent, browser, deviceName, area, SysConstants.SYS_USER_PWD_STATUS_NORMAL);
        loginLog.setUserId(userId);
        SysUser user = sysUserService.findById(userId);
        if (null == user) {
            logger.warn("userId:[{}] is not exist, login info {}", userId, loginLog.toString());
            return ;
        }

        loginLog.setCreateBy(user.getLoginName());
        loginLog.setUpdateBy(user.getLoginName());
        this.save(loginLog);
    }

	public SysLoginLog findLastLoginInfo(Long userId) {
        Assert.notNull(userId, "userIdIsEmpty");
		SysLoginLog loginLog = new SysLoginLog();
		loginLog.setPage(1);
		loginLog.setLimit(2);
		loginLog.setUserId(userId);
		loginLog.setStatus(SysConstants.SYS_USER_LOGIN_STATUS_SUCCESS);
		loginLog.setSortField("l.login_time");
		loginLog.setDirection(SysConstants.DESC);
		PageInfo<SysLoginLog> findByPage = this.findByPage(loginLog);
		List<SysLoginLog> list = findByPage.getList();
		if (!list.isEmpty()) {
			return list.get(list.size()-1);
		}
		return null;
	}



    private SysLoginLog initPojo(String status, String ip, String userAgent, String browser, String deviceName, String area, int firstLogin) {
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setFirstLogin(firstLogin);
        loginLog.setStatus(status);
        loginLog.setBrowserName(browser);
        loginLog.setDeviceName(deviceName);
        loginLog.setIp(ip);
        loginLog.setArea(area);
        loginLog.setUserAgent(userAgent);
        loginLog.setLoginTime(new Date());
        return loginLog;
    }
	
}
