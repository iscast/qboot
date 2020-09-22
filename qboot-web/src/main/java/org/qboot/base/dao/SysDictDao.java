package org.qboot.base.dao;

import java.util.List;

import org.qboot.base.dto.SysDict;
import org.qboot.common.dao.CrudDao;

/**
 * <p>Title: SysDictDao</p>
 * <p>Description: 字段参数</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysDictDao extends CrudDao<SysDict>{
	
    public List<SysDict> findTypes(String type);

	public int setStatus(SysDict sysDict);
}