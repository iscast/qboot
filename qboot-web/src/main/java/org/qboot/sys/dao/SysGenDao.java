package org.qboot.sys.dao;

import org.qboot.sys.dto.DbTable;
import org.qboot.sys.dto.DbTableColumn;
import org.qboot.sys.dto.SysGen;
import org.qboot.common.dao.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysGenDao extends CrudDao<SysGen>{
    
	List<DbTable> findTable(@Param("tableName") String tableName);
	List<DbTableColumn> findColumnByTableName(@Param("tableName") String tableName);
	String findPkType(@Param("tableName") String tableName);
}