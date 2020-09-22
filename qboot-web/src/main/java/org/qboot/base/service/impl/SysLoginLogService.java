package org.qboot.base.service.impl;

import java.util.Date;
import java.util.List;

import org.qboot.common.utils.i18n.MessageUtil;
import org.qboot.base.dao.SysLoginLogDao;
import org.qboot.base.dto.SysLoginLog;
import org.qboot.common.constant.QConstants;
import org.qboot.web.security.SecurityUtils;
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
 * Copyright &copy; 2018 powered by huangdf, All rights reserved.
 * @author history
 */
@Service
public class SysLoginLogService extends CrudService<SysLoginLogDao, SysLoginLog>{

	protected Logger logger = LoggerFactory.getLogger(SysLoginLogService.class);
	
	@Autowired
	private SysUserService sysUserService;
	
	public void loginLogByUserId(String status, Long userId,String ip,String userAgent,String browser,String deviceName, String area, int firstLogin) {
			Assert.notNull(userId, MessageUtil.getMessage("sys.response.msg.userIdIsEmpty","用户id 为空"));
			Assert.hasLength(status, MessageUtil.getMessage("sys.response.msg.loginStatusIsEmpty","登录状态为空"));
			SysLoginLog loginLog = new SysLoginLog();
			loginLog.setFirstLogin(firstLogin);
			loginLog.setStatus(status);
			loginLog.setBrowserName(browser);
			loginLog.setDeviceName(deviceName);
			loginLog.setIp(ip);
			loginLog.setArea(area);
			loginLog.setUserId(String.valueOf(userId));
			loginLog.setUserAgent(userAgent);
			loginLog.setLoginTime(new Date());
			loginLog.setCreateBy(SecurityUtils.getLoginName());
			loginLog.setUpdateBy(SecurityUtils.getLoginName());
			logger.info("保存登录记录：{}", loginLog.toString());
			this.save(loginLog);
	}
	public void loginLogByLoginName(String status,String loginName,String ip,String userAgent,String browser,String deviceName, String area, int firstLogin) {
		SysUser user = sysUserService.findByLoginName(loginName);
		if (null != user) {
			this.loginLogByUserId(status, user.getId(), ip, userAgent,browser,deviceName,area, firstLogin);
		}else {
			logger.error("登录用户不存在：{}", loginName);
		}
	}
	public SysLoginLog findLastLoginInfo(String userId) {
		Assert.hasLength(userId,MessageUtil.getMessage("sys.response.msg.userIdIsEmpty","用户id 为空"));
		SysLoginLog loginLog = new SysLoginLog();
		loginLog.setPage(1);
		loginLog.setLimit(2);
		loginLog.setUserId(userId);
		loginLog.setStatus(SysLoginLog.SUCCESS);
		loginLog.setSortField("l.login_time");
		loginLog.setDirection(QConstants.DESC);
		PageInfo<SysLoginLog> findByPage = this.findByPage(loginLog);
		List<SysLoginLog> list = findByPage.getList();
		if (!list.isEmpty()) {
			return list.get(list.size()-1);
		}
		return null;
	}
	
}
