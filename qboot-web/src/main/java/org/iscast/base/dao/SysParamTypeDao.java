package org.iscast.base.dao;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysParamType;

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
