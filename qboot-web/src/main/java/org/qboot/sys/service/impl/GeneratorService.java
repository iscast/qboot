package org.qboot.sys.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.config.FreeMarkerResolver;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.enums.GenEnum;
import org.qboot.common.service.BaseService;
import org.qboot.common.utils.GenTypeMappingUtils;
import org.qboot.sys.dto.DbTableColumnDto;
import org.qboot.sys.dto.DbTableDto;
import org.qboot.sys.dto.GenColumnInfoDto;
import org.qboot.sys.dto.SysGenDto;
import org.qboot.sys.exception.SysGenException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GeneratorService extends BaseService {

	/**
	 * 编辑默认不勾选
	 */
	private static final String[] defaultNotUpdates = { "id", "create_by", "create_date" };
	/**
	 * 列表默认不展示
	 */
	private static final String[] defaultNotLists = { "id", "create_by", "create_date", "update_by", "remarks" };
	/**
	 * 默认有的字段
	 */
	private static final String[] defaultFields = { "id", "create_by", "create_date", "update_by", "update_date", "remarks" };

	/**
	 * 待生成代码模板
	 */
	private static final String[] ftls = {"gen/mapper.xml.ftl","gen/entity.java.ftl","gen/dao.java.ftl","gen/service.java.ftl","gen/serviceImpl.java.ftl","gen/controller.java.ftl","gen/page.html.ftl","gen/menu.sql.ftl"};
	private static final String javaFolder = "/code/src/main/java/";
	private static final String resourcesFolder = "/code/src/main/resources/";
	private static final String staticFolder = "/code/src/main/webapp/static/";


	/**
	 * 第一次从表结构获取的默认生成方案
	 * 
	 * @param table
	 * @param columns
	 * @return
	 */
	public SysGenDto getDefaultGenInfo(DbTableDto table, List<DbTableColumnDto> columns) {
		Assert.notEmpty(columns, "DB 字段不存在");
		Assert.notNull(table, "表不存在");
		SysGenDto sysGen = new SysGenDto();
		String tableName = table.getName();
		sysGen.setTableName(table.getName());
		sysGen.setMenuName(table.getComment());
		// 模块名默认下滑线分隔第一组
		String[] split = tableName.split("_");
		sysGen.setModuleName(split[0]);
		if (split.length > 1) {
			StringBuilder sb = new StringBuilder();
			sysGen.setFunctionName(split[1]);
			for (int i=1;i<split.length;i++) {
				sb.append(split[i]);
			}
			sysGen.setFunctionName(sb.toString());
		}
		sysGen.setTemplate("1");
		sysGen.setClassName(camelToUnderline(tableName));
		
		sysGen.setColumnInfos(getGenColumnInfos(columns));
		return sysGen;
	}
	
	/**
	 * 根据列名获取相应的初始化列配置
	 * @param columns
	 * @return
	 */
	public List<GenColumnInfoDto> getGenColumnInfos(List<DbTableColumnDto> columns) {
		Assert.notEmpty(columns, "DB 字段不存在");
		List<GenColumnInfoDto> genColumnInfos = Lists.newArrayList();
		// 处理列
		for (DbTableColumnDto column : columns) {
			GenColumnInfoDto genColumnInfo = new GenColumnInfoDto();
			genColumnInfo.setDbColumnName(column.getName());
			genColumnInfo.setColumnComment(column.getComment());
			genColumnInfo.setColumnType(column.getColumnType());
			genColumnInfo.setColumnKey(column.getColumnKey());
			genColumnInfo.setExtra(column.getExtra());
			genColumnInfo.setDataType(column.getDataType());
			genColumnInfo.setMaxLength(column.getMaxlength());
			genColumnInfo.setJavaType(GenTypeMappingUtils.getDbJavaMapping(column.getDataType()));
			genColumnInfo.setJdbcType(GenTypeMappingUtils.getDbMybatisMapping(column.getDataType()));
			genColumnInfo.setJavaFieldName(columnToJava(column.getName()));
			genColumnInfo.setNullable(column.getNullable());
			//自增默认不插入
			if (!"auto_increment".equals(column.getExtra())) {
				genColumnInfo.setInsert(SysConstants.YES);
			}
			if (!ArrayUtils.contains(defaultNotUpdates, column.getName())) {
				genColumnInfo.setUpdate(SysConstants.YES);
			}
			if (!ArrayUtils.contains(defaultNotLists, column.getName())) {
				genColumnInfo.setList(SysConstants.YES);
			}
			if ("id".equals(column.getName())) {//默认是隐藏
				genColumnInfo.setInputType(GenEnum.InputType.HIDDEN.value());
			}
			if ("remarks".equals(column.getName())) {//默认文本域
				genColumnInfo.setInputType(GenEnum.InputType.TEXTAREA.value());
			}
			if ("Date".equals(genColumnInfo.getJavaType())) {//时间框
				genColumnInfo.setInputType(GenEnum.InputType.DATE.value());
			}
			if ("LONGVARCHAR".equals(genColumnInfo.getJdbcType())) {//大文本
				genColumnInfo.setInputType(GenEnum.InputType.TEXTAREA.value());
				genColumnInfo.setList(SysConstants.NO);
			}
			if (!"id".equals(column.getName()) && ("Integer".equals(genColumnInfo.getJavaType()) || "Long".equals(genColumnInfo.getJavaType()))) {//整数框
				genColumnInfo.setInputType(GenEnum.InputType.ZHENGSHU.value());
			}
			if ("Float".equals(genColumnInfo.getJavaType()) || "Double".equals(genColumnInfo.getJavaType()) || "BigDecimal".equals(genColumnInfo.getJavaType()) ) {//小数框
				genColumnInfo.setInputType(GenEnum.InputType.XIAOSHU.value());
			}
			genColumnInfo.setSort(column.getSort());
			genColumnInfos.add(genColumnInfo);
		}	
		return genColumnInfos;
	}

	public byte[] codeGen(SysGenDto sysGen) {
		Assert.notNull(sysGen, "生成方案为空");
		Assert.notNull(sysGen.getColumnInfos(), "生成方案为空");
		List<GenColumnInfoDto> columnInfos = sysGen.getColumnInfos();
		for (GenColumnInfoDto column : columnInfos) {
			String javaType = column.getJavaType();
			if (!ArrayUtils.contains(defaultFields, column.getDbColumnName())) {
				if ("BigDecimal".equals(javaType)) {
					sysGen.setHasBigDecimal(true);
				}
				if ("Date".equals(javaType)) {
					sysGen.setHasDate(true);
				}
				if (SysConstants.YES.equals(column.getSearch())) {
					sysGen.setHasSearch(true);
				}
			}
			
			//大字段
			if ("LONGVARCHAR".equals(column.getJdbcType())) {
				sysGen.setHasBlob(true);
			}
			//富文本
			if ("richtext".equals(column.getInputType())) {
				sysGen.setHasRichText(true);
			}
			//文件上传
			if ("picture".equals(column.getInputType()) || "file".equals(column.getInputType())) {
				sysGen.setHasFileUpload(true);
			}
		}
		try {
			return this.zipCode(sysGen);
		} catch (IOException e) {
			throw new SysGenException(SysConstants.GLOBAL_DEFAULT_ERROR, e);
		}
	}

	private byte[] zipCode(SysGenDto sysGen) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		for (String ftl : ftls) {
			String content = FreeMarkerResolver.renderTemplate(ftl, sysGen);
			logger.info("ftl {}:\r\n{}",ftl,content);
			zos.putNextEntry(new ZipEntry(this.getZipEntryName(ftl,sysGen)));
			IOUtils.write(content, zos);
			zos.closeEntry();
		}
		//这个地方有点反人类，按道理应该在取到byte[] 后关闭，测试，zos.flush 也无效，顾提前关闭，将流都刷入到数组中。
		zos.close();
		byte[] byteArray = bos.toByteArray();
		return byteArray;
	}
	
	private String getZipEntryName(String ftl, SysGenDto sysGen) {
		String name = null;
		String moduleName = sysGen.getModuleName();
		if(StringUtils.isNotBlank(moduleName)) {
			moduleName = moduleName.replace(".", "/");
		}

		String className = sysGen.getClassName();
		String functionName = sysGen.getFunctionName();

		if (ftl.contains("entity.java")) {
			name = javaFolder + moduleName + "/" + functionName + "/entity/" + className + "Dto.java";
		} else if (ftl.contains("dao.java")) {
			name = javaFolder + moduleName + "/" + functionName + "/dao/" + className + "Dao.java";
		} else if (ftl.contains("service.java")) {
			name = javaFolder + moduleName + "/" + functionName + "/service/" + className + "Service.java";
		} else if (ftl.contains("serviceImpl.java")) {
			name = javaFolder + moduleName + "/" + functionName + "/service/" + className + "ServiceImpl.java";
		} else if (ftl.contains("controller.java")) {
			name = javaFolder + moduleName + "/" + functionName + "/web/" + className + "Controller.java";
		} else if (ftl.contains("mapper.xml")) {
			name = resourcesFolder + "mappings/" + functionName + "/" + className + "Mapper.xml";
		} else if (ftl.contains("page.html")) {
			name = staticFolder + "pages/" + functionName + ".html";
		} else if (ftl.contains("menu.sql")) {
			name = resourcesFolder + "db/" + "sys_menu.sql";
		}
		Assert.hasLength(name, "zipEntryName 为空");
		return name;
	}
	
 	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return StringUtils.uncapitalize(camelToUnderline(columnName));
	}

	/**
	 * 驼峰
	 */
	public static String camelToUnderline(String name) {
		return WordUtils.capitalizeFully(name, new char[] { '_' }).replace("_", "");
	}

}
