package org.iscast.base.dao;

import java.util.List;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysParamClass;

/**
 * <p>Title: SysParamClassDao</p>
 * <p>Description: 系统类型</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysParamClassDao extends CrudDao<SysParamClass> {

	public int changeById(SysParamClass sysParamClass);
	
	public List<SysParamClass> classIsUsed(SysParamClass sysParamClass);
}
