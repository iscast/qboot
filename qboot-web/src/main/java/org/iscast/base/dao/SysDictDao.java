package org.iscast.base.dao;

import java.util.List;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysDict;

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