package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysGenDao;
import org.qboot.sys.dto.DbTableColumnDto;
import org.qboot.sys.dto.DbTableDto;
import org.qboot.sys.dto.GenColumnInfoDto;
import org.qboot.sys.dto.SysGenDto;

import java.io.Serializable;
import java.util.List;


/**
 * 代码生成
 */
public interface SysGenService extends ICrudService<SysGenDao, SysGenDto> {

    SysGenDto findById(Serializable id);
	
	List<DbTableDto> findTables();

	DbTableDto findTableByName(String tableName);
	
	List<DbTableColumnDto> findColumnByTableName(String tableName);
	
	String findPkType(String tableName);
	
	/**
	 * 根据表名获得默认的生成方案
	 * @param tableName
	 * @return
	 */
	SysGenDto getDefaultGenInfoByTableName(String tableName);
	
	/**
	 * 根据表名获得默认的生成方案
	 * @param tableName
	 * @return
	 */
	List<GenColumnInfoDto> getDefaultGenInfosByTableName(String tableName);
	
	byte[] codeGen(String id);
}
