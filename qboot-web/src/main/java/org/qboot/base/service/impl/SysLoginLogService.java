package org.qboot.base.service.impl;

import java.util.Date;
import java.util.List;

import org.qboot.common.utils.i18n.MessageUtil;
import org.qboot.base.dao.SysLoginLogDao;
import org.qboot.base.dto.SysLoginLog;
import org.qboot.common.constant.SysConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;

import org.qboot.base.dto.SysUser;
import org.qboot.common.service.CrudService;

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
        Assert.hasLength(status, MessageUtil.getMessage("sys.response.msg.loginStatusIsEmpty","登录状态为空"));

        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setFirstLogin(firstLogin);
        loginLog.setStatus(status);
        loginLog.setBrowserName(browser);
        loginLog.setDeviceName(deviceName);
        loginLog.setIp(ip);
        loginLog.setArea(area);
        loginLog.setUserAgent(userAgent);
        loginLog.setLoginTime(new Date());
        loginLog.setCreateBy(loginName);
        loginLog.setUpdateBy(loginName);

		SysUser user = sysUserService.findByLoginName(loginName);
		if (null == user) {
            logger.warn("user is not exist, login info {}", loginLog.toString());
            return ;
		}

        logger.info("保存登录记录：{}", loginLog.toString());
        loginLog.setUserId(user.getId().toString());
        this.save(loginLog);

	}

	public SysLoginLog findLastLoginInfo(String userId) {
		Assert.hasLength(userId,MessageUtil.getMessage("sys.response.msg.userIdIsEmpty","用户id 为空"));
		SysLoginLog loginLog = new SysLoginLog();
		loginLog.setPage(1);
		loginLog.setLimit(2);
		loginLog.setUserId(userId);
		loginLog.setStatus(SysLoginLog.SUCCESS);
		loginLog.setSortField("l.login_time");
		loginLog.setDirection(SysConstants.DESC);
		PageInfo<SysLoginLog> findByPage = this.findByPage(loginLog);
		List<SysLoginLog> list = findByPage.getList();
		if (!list.isEmpty()) {
			return list.get(list.size()-1);
		}
		return null;
	}
	
}
