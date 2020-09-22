package org.qboot.base.dao;

import org.qboot.base.dto.DbTable;
import org.qboot.base.dto.DbTableColumn;
import org.qboot.base.dto.SysGen;
import org.qboot.common.dao.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysGenDao extends CrudDao<SysGen>{
    
	public List<DbTable> findTable(@Param("tableName") String tableName);
	public List<DbTableColumn> findColumnByTableName(@Param("tableName") String tableName);
	public String findPkType(@Param("tableName") String tableName);
}