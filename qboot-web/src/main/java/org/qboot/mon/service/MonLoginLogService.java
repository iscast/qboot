package org.qboot.mon.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.mon.dao.MonLoginLogDao;
import org.qboot.mon.dto.MonLoginLogDto;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录日志
 */
public interface MonLoginLogService extends ICrudService<MonLoginLogDao, MonLoginLogDto> {
    void saveLog(String status, String loginName, HttpServletRequest request);
}
