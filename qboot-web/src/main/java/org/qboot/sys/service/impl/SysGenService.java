package org.qboot.sys.service.impl;

import com.alibaba.fastjson.JSON;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.GenTypeMappingUtils;
import org.qboot.sys.dao.SysGenDao;
import org.qboot.sys.dto.DbTableColumnDto;
import org.qboot.sys.dto.DbTableDto;
import org.qboot.sys.dto.GenColumnInfoDto;
import org.qboot.sys.dto.SysGenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;


/**
 * 代码生成
 * @author history
 */
@Service
public class SysGenService extends CrudService<SysGenDao, SysGenDto> {
	
	@Autowired
	private GeneratorService generatorService;
	
	@Override
	public SysGenDto findById(Serializable id) {
		 SysGenDto sysGen = super.findById(id);
		 Assert.notNull(sysGen,"生成方案不存在");
		 sysGen.setColumnInfos(JSON.parseArray(sysGen.getColumns(), GenColumnInfoDto.class));
		 return sysGen;
	}
	
	public List<DbTableDto> findTables() {
		return this.d.findTable("");
	}

	public DbTableDto findTableByName(String tableName) {
		Assert.hasLength(tableName, "表名为空");
		List<DbTableDto> list = this.d.findTable(tableName);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public List<DbTableColumnDto> findColumnByTableName(String tableName) {
		Assert.hasLength(tableName, "表名为空");
		return this.d.findColumnByTableName(tableName);
	}
	
	public String findPkType(String tableName) {
		Assert.hasLength(tableName, "表名为空");
		String dbPkType = this.d.findPkType(tableName);
		Assert.hasLength(dbPkType,tableName + " 需要主键，名为id");
		return GenTypeMappingUtils.getDbJavaMapping(dbPkType);
	}
	
	/**
	 * 根据表名获得默认的生成方案
	 * @param tableName
	 * @return
	 */
	public SysGenDto getDefaultGenInfoByTableName(String tableName) {
		DbTableDto table = this.findTableByName(tableName);
		Assert.notNull(table,tableName + " 不存在");
		List<DbTableColumnDto> columns = this.findColumnByTableName(tableName);
		SysGenDto sysGen = generatorService.getDefaultGenInfo(table, columns);
		sysGen.setPkType(this.findPkType(tableName));
		return sysGen;
	}
	
	/**
	 * 根据表名获得默认的生成方案
	 * @param tableName
	 * @return
	 */
	public List<GenColumnInfoDto> getDefaultGenInfosByTableName(String tableName) {
		DbTableDto table = this.findTableByName(tableName);
		Assert.notNull(table,tableName + " 不存在");
		List<DbTableColumnDto> columns = this.findColumnByTableName(tableName);
		return generatorService.getGenColumnInfos(columns);
	}
	
	public byte[] codeGen(String id) {
		Assert.hasLength(id, "方案Id 为空");
		SysGenDto sysGen = this.findById(id);
		Assert.notNull(sysGen,"方案为空");
		return this.generatorService.codeGen(sysGen);
	}
	
}
