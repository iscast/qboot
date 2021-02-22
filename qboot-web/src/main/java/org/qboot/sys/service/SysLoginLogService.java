package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysLoginLogDao;
import org.qboot.sys.dto.SysLoginLogDto;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录日志
 */
public interface SysLoginLogService extends ICrudService<SysLoginLogDao, SysLoginLogDto> {
    void saveLog(String status, String loginName, HttpServletRequest request);
}
