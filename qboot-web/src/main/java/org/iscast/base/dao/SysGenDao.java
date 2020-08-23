package org.iscast.base.dao;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.DbTable;
import org.iscast.base.dto.DbTableColumn;
import org.iscast.base.dto.SysGen;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysGenDao extends CrudDao<SysGen>{
    
	public List<DbTable> findTable(@Param("tableName") String tableName);
	public List<DbTableColumn> findColumnByTableName(@Param("tableName") String tableName);
	public String findPkType(@Param("tableName") String tableName);
}