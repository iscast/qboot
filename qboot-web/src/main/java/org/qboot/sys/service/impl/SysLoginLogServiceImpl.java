package org.qboot.sys.service.impl;

import org.qboot.common.service.CrudService;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dao.SysLoginLogDao;
import org.qboot.sys.dto.SysLoginLogDto;
import org.qboot.sys.service.SysLoginLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.qboot.sys.exception.errorcode.SysUserErrTable.SYS_USER_LOGIN_STATUS_EMPTY;

/**
 * 登录日志Service
 * @author history
 */
@Service
public class SysLoginLogServiceImpl extends CrudService<SysLoginLogDao, SysLoginLogDto> implements SysLoginLogService {

	protected Logger logger = LoggerFactory.getLogger(SysLoginLogServiceImpl.class);

	@Override
	public void loginLogByLoginName(String status, String loginName,String ip,String userAgent,String browser,String deviceName, String area) {
	    MyAssertTools.hasLength(status, SYS_USER_LOGIN_STATUS_EMPTY);
        SysLoginLogDto loginLog = new SysLoginLogDto();
        loginLog.setStatus(status);
        loginLog.setBrowserName(browser);
        loginLog.setDeviceName(deviceName);
        loginLog.setIp(ip);
        loginLog.setArea(area);
        loginLog.setUserAgent(userAgent);
        loginLog.setLoginTime(new Date());
        loginLog.setCreateBy(loginName);
        loginLog.setUpdateBy(loginName);
        this.save(loginLog);
	}
	
}
