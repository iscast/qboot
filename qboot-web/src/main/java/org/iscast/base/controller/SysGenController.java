package org.iscast.base.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.commons.io.IOUtils;
import org.iscast.base.dto.GenColumnInfo;
import org.iscast.base.dto.SysGen;
import org.iscast.base.service.impl.SysGenService;
import org.iscast.base.vo.SysProjectGenVo;
import org.iscast.common.controller.BaseController;
import org.iscast.common.utils.CodecUtils;
import org.iscast.common.utils.IdGen;
import org.iscast.web.dto.ResponeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${admin.path}/sys/gen")
public class SysGenController extends BaseController {

	@Autowired
	private SysGenService sysGenService;

	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysGen sysGen) {
		PageInfo<SysGen> page = sysGenService.findByPage(sysGen);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysGen sysGen = sysGenService.findById(id);
		return ResponeModel.ok(sysGen);
	}

	@PreAuthorize("hasAuthority('sys:gen:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated @RequestBody SysGen sysGen, BindingResult bindingResult) {
		List<GenColumnInfo> columnInfos = sysGenService.getDefaultGenInfosByTableName(sysGen.getTableName());
		Collections.sort(columnInfos);
		sysGen.setColumns(JSON.toJSONString(columnInfos));
		sysGen.setId(IdGen.uuid());
		sysGen.setPkType("String");
		sysGen.setCreateBy("1");
		sysGen.setCreateDate(new Date());
		sysGen.setUpdateBy("1");
		sysGen.setUpdateDate(new Date());
		int cnt = sysGenService.save(sysGen);
		return ResponeModel.ok(cnt);
	}

	@PreAuthorize("hasAuthority('sys:gen:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated @RequestBody SysGen sysGen, BindingResult bindingResult) {
		List<GenColumnInfo> columnInfos = sysGen.getColumnInfos();
		Collections.sort(columnInfos);
		sysGen.setColumns(JSON.toJSONString(columnInfos));
		int cnt = sysGenService.updateById(sysGen);
		return ResponeModel.ok(cnt);
	}

	@PreAuthorize("hasAuthority('sys:gen:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysGenService.deleteById(id);
		return ResponeModel.ok(cnt);
	}

	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@PostMapping("/qryTables")
	public ResponeModel qryTables() {
		return ResponeModel.ok(sysGenService.findTables());
	}
	
	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@PostMapping("/qryTableInfo")
	public ResponeModel qryTableInfo(@RequestParam String tableName) {
		return ResponeModel.ok(sysGenService.getDefaultGenInfoByTableName(tableName));
	}
	
	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@PostMapping("/qryTableInfos")
	public ResponeModel qryTableInfos(@RequestParam String tableName) {
		return ResponeModel.ok(sysGenService.getDefaultGenInfosByTableName(tableName));
	}
	
	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@RequestMapping("/codeGen")
	public void codeGen(@RequestParam String id,HttpServletResponse response) throws Exception {
		byte[] codeGen = this.sysGenService.codeGen(id);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename="+ CodecUtils.urlEncode("代码生成.zip")); 
		response.setContentLength(codeGen.length);
		ServletOutputStream output = response.getOutputStream();
		IOUtils.write(codeGen, output);
		IOUtils.closeQuietly(output);
	}
	
	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@GetMapping("/projectGen")
	public void projectGen(@RequestParam(required=true) String projectName, @RequestParam(required=true) int projectType, 
			HttpServletResponse response) throws Exception {
		SysProjectGenVo sysProjectGenVo = new SysProjectGenVo();
		sysProjectGenVo.setProjectName(projectName);
		sysProjectGenVo.setProjectType(projectType);
		byte[] codeGen = this.sysGenService.projectGen(sysProjectGenVo);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename="+ CodecUtils.urlEncode("项目生成.zip")); 
		response.setContentLength(codeGen.length);
		ServletOutputStream output = response.getOutputStream();
		IOUtils.write(codeGen, output);
		IOUtils.closeQuietly(output);
	}
}
