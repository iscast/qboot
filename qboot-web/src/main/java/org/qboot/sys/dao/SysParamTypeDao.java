package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysParamTypeDto;

/**
 * 系统类型
 * @author iscast
 * @date 2020-09-25
 */
public interface SysParamTypeDao extends CrudDao<SysParamTypeDto> {
    SysParamTypeDto findByDto(SysParamTypeDto sysParamType);
}
