package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysParamTypeDao;
import org.qboot.sys.dto.SysParamTypeDto;

import java.util.List;

/**
 * 系统类型
 */
public interface SysParamTypeService extends ICrudService<SysParamTypeDao, SysParamTypeDto> {

    SysParamTypeDto findByDto(SysParamTypeDto sysParamType);

	List<SysParamTypeDto> findParamTypes(String paramTypeClass);
}
