package org.qboot.base.dao;

import org.qboot.base.dto.DbTable;
import org.qboot.base.dto.DbTableColumn;
import org.qboot.base.dto.SysGen;
import org.qboot.common.dao.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysGenDao extends CrudDao<SysGen>{
    
	List<DbTable> findTable(@Param("tableName") String tableName);
	List<DbTableColumn> findColumnByTableName(@Param("tableName") String tableName);
	String findPkType(@Param("tableName") String tableName);
}