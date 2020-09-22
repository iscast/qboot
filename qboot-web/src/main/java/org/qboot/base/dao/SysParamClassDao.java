package org.qboot.base.dao;

import java.util.List;

import org.qboot.base.dto.SysParamClass;
import org.qboot.common.dao.CrudDao;

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
