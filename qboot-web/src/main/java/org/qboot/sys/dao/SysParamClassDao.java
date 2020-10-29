package org.qboot.sys.dao;

import java.util.List;

import org.qboot.sys.dto.SysParamClassDto;
import org.qboot.common.dao.CrudDao;

/**
 * <p>Title: SysParamClassDao</p>
 * <p>Description: 系统类型</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysParamClassDao extends CrudDao<SysParamClassDto> {

	public int changeById(SysParamClassDto sysParamClass);
	
	public List<SysParamClassDto> classIsUsed(SysParamClassDto sysParamClass);
}
