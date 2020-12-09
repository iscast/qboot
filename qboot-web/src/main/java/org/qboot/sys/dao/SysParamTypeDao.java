package org.qboot.sys.dao;

import org.qboot.sys.dto.SysParamTypeDto;
import org.qboot.common.dao.CrudDao;

/**
 * <p>Title: SysParamTypeDao</p>
 * <p>Description: 系统类型</p>
 * @author iscast
 * @date 2018-08-08
 */
public interface SysParamTypeDao extends CrudDao<SysParamTypeDto> {

	int changeById(SysParamTypeDto sysParamType);
}
