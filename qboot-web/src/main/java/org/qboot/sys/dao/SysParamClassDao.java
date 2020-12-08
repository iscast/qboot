package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysParamClassDto;

/**
 * <p>Title: SysParamClassDao</p>
 * <p>Description: 系统类型</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysParamClassDao extends CrudDao<SysParamClassDto> {

    int changeById(SysParamClassDto sysParamClass);
}
