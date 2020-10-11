package org.qboot.${functionName}.${moduleName}.web;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import org.qboot.common.controller.BaseController;
import org.qboot.web.dto.ResponeModel;
import org.qboot.${functionName}.${moduleName}.dto.${className}Dto;
import org.qboot.${functionName}.${moduleName}.service.${className}Service;

/**
 * ${menuName}controller
 * @author systemGenerated ${.now}
 */
@RestController
@RequestMapping("${r'${admin.path}'}/${moduleName}/${functionName}")
public class ${className}Controller extends BaseController {

	@Autowired
	private ${className}Service ${className?uncap_first}Service;

	@PreAuthorize("hasAuthority('${moduleName}:${functionName}:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(${className}Dto ${className?uncap_first}) {
		PageInfo<${className}Dto> page = ${className?uncap_first}Service.findByPage(${className?uncap_first});
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('${moduleName}:${functionName}:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam ${pkType} id) {
		${className}Dto ${className?uncap_first} = ${className?uncap_first}Service.findById(id);
		return ResponeModel.ok(${className?uncap_first});
	}
	
	@PreAuthorize("hasAuthority('${moduleName}:${functionName}:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated ${className}Dto ${className?uncap_first}, BindingResult bindingResult) {
		int cnt = ${className?uncap_first}Service.save(${className?uncap_first});
		return ResponeModel.ok(cnt);
	}
	
	@PreAuthorize("hasAuthority('${moduleName}:${functionName}:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated ${className}Dto ${className?uncap_first}, BindingResult bindingResult) {
		int cnt = ${className?uncap_first}Service.updateById(${className?uncap_first});
		return ResponeModel.ok(cnt);
	}
	
	@PreAuthorize("hasAuthority('${moduleName}:${functionName}:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = ${className?uncap_first}Service.deleteById(id);
		return ResponeModel.ok(cnt);
	}
}
