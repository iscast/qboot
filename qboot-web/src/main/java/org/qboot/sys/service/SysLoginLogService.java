package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysLoginLogDao;
import org.qboot.sys.dto.SysLoginLogDto;

/**
 * 登录日志
 */
public interface SysLoginLogService extends ICrudService<SysLoginLogDao, SysLoginLogDto> {

	void loginLogByLoginName(String status,String loginName,String ip,String userAgent,String browser,String deviceName, String area, int firstLogin);

    void loginLogByLoginId(String status,String userId,String ip,String userAgent,String browser,String deviceName, String area);

	SysLoginLogDto findLastLoginInfo(String userId);
}
