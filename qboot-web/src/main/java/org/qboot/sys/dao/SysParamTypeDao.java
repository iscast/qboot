package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysParamTypeDto;

/**
 * <p>Title: SysParamTypeDao</p>
 * <p>Description: 系统类型</p>
 * @author iscast
 * @date 2018-08-08
 */
public interface SysParamTypeDao extends CrudDao<SysParamTypeDto> {

	int changeById(SysParamTypeDto sysParamType);
    SysParamTypeDto findByDto(SysParamTypeDto sysParamType);
}
