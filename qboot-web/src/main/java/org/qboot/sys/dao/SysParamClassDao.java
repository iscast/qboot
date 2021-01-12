package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysParamClassDto;

/**
 * 系统类型
 * @author iscast
 * @date 2020-09-25
 */
public interface SysParamClassDao extends CrudDao<SysParamClassDto> {

    int changeById(SysParamClassDto sysParamClass);
}
