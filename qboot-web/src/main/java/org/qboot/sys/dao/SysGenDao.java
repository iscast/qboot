package org.qboot.sys.dao;

import org.qboot.sys.dto.DbTableDto;
import org.qboot.sys.dto.DbTableColumnDto;
import org.qboot.sys.dto.SysGenDto;
import org.qboot.common.dao.CrudDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysGenDao extends CrudDao<SysGenDto>{
    
	List<DbTableDto> findTable(@Param("tableName") String tableName);
	List<DbTableColumnDto> findColumnByTableName(@Param("tableName") String tableName);
	String findPkType(@Param("tableName") String tableName);
}