package org.qboot.base.dao;

import org.qboot.base.dto.SysDict;
import org.qboot.common.dao.CrudDao;

import java.util.List;

/**
 * <p>Title: SysDictDao</p>
 * <p>Description: 字段参数</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysDictDao extends CrudDao<SysDict>{
	
    List<SysDict> findTypes(String type);

	int setStatus(SysDict sysDict);
}