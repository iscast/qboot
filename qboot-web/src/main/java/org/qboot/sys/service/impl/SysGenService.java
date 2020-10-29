package org.qboot.sys.service.impl;

import org.qboot.common.service.CrudService;
import org.qboot.sys.dao.SysGenDao;
import org.qboot.sys.dto.DbTable;
import org.qboot.sys.dto.DbTableColumn;
import org.qboot.sys.dto.GenColumnInfo;
import org.qboot.sys.dto.SysGen;
import org.qboot.sys.vo.SysProjectGenVO;
import org.qboot.common.utils.GenTypeMappingUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * 代码生成
 * @author history
 *
 */
@Service
public class SysGenService extends CrudService<SysGenDao, SysGen> {
	
	@Autowired
	private GeneratorService generatorService;
	
	@Override
	public SysGen findById(Serializable id) {
		 SysGen sysGen = super.findById(id);
		 Assert.notNull(sysGen,"生成方案不存在");
		 sysGen.setColumnInfos(JSON.parseArray(sysGen.getColumns(), GenColumnInfo.class));
		 return sysGen;
	}
	
	public List<DbTable> findTables() {
		return this.d.findTable("");
	}

	public DbTable findTableByName(String tableName) {
		Assert.hasLength(tableName, "表名为空");
		List<DbTable> list = this.d.findTable(tableName);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public List<DbTableColumn> findColumnByTableName(String tableName) {
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
	public SysGen getDefaultGenInfoByTableName(String tableName) {
		DbTable table = this.findTableByName(tableName);
		Assert.notNull(table,tableName + " 不存在");
		List<DbTableColumn> columns = this.findColumnByTableName(tableName);
		SysGen sysGen = generatorService.getDefaultGenInfo(table, columns);
		sysGen.setPkType(this.findPkType(tableName));
		return sysGen;
	}
	
	/**
	 * 根据表名获得默认的生成方案
	 * @param tableName
	 * @return
	 */
	public List<GenColumnInfo> getDefaultGenInfosByTableName(String tableName) {
		DbTable table = this.findTableByName(tableName);
		Assert.notNull(table,tableName + " 不存在");
		List<DbTableColumn> columns = this.findColumnByTableName(tableName);
		return generatorService.getGenColumnInfos(columns);
	}
	
	public byte[] codeGen(String id) {
		Assert.hasLength(id, "方案Id 为空");
		SysGen sysGen = this.findById(id);
		Assert.notNull(sysGen,"方案为空");
		return this.generatorService.codeGen(sysGen);
	}

	public byte[] projectGen(SysProjectGenVO sysProjectGenVo) throws IOException {
		Assert.notNull(sysProjectGenVo,"项目生成方案为空");
		return this.generatorService.projectGen(sysProjectGenVo);
	}
	
}
