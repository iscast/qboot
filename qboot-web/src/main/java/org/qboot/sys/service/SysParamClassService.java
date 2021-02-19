package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysParamClassDao;
import org.qboot.sys.dto.SysParamClassDto;

/**
 * 系统类型
 */
public interface SysParamClassService extends ICrudService<SysParamClassDao, SysParamClassDto> {

	int changeById(SysParamClassDto sysParamClass);
}
