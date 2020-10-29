package org.qboot.sys.dao;

import org.qboot.sys.dto.SysParamType;
import org.qboot.common.dao.CrudDao;

/**
 * <p>Title: SysParamTypeDao</p>
 * <p>Description: 系统类型</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysParamTypeDao extends CrudDao<SysParamType> {

	public int changeById(SysParamType sysParamType);
}
