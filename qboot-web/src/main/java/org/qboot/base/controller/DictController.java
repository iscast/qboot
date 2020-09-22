package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.base.dto.SysDict;
import org.qboot.base.service.impl.SysDictService;
import org.qboot.common.controller.BaseController;
import org.qboot.web.dto.ResponeModel;
import org.qboot.web.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: DictController</p>
 * <p>Description: 系统字典参数</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private SysDictService sysDictService;

	@PreAuthorize("hasAuthority('sys:dict:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(SysDict sysDict, BindingResult bindingResult) {
		PageInfo<SysDict> page = sysDictService.findByPage(sysDict);
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:dict:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysDict sysDict = sysDictService.findById(id);
		return ResponeModel.ok(sysDict);
	}
	
	@PreAuthorize("hasAuthority('sys:dict:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysDict sysDict, BindingResult bindingResult) {
		sysDict.setCreateBy(SecurityUtils.getLoginName());
		sysDict.setStatus("1");
		sysDict.setSort(0);
		int cnt = sysDictService.save(sysDict);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysDict sysDict,BindingResult bindingResult) {
		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		int cnt = sysDictService.update(sysDict);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PostMapping("/setStatus")
	public ResponeModel setStatus(@RequestParam String id, @RequestParam String status) {
		SysDict sysDict = new SysDict();
		sysDict.setId(id);
		sysDict.setStatus(status);
		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		sysDict.setUpdateDate(new Date());
		int cnt = sysDictService.setStatus(sysDict);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:dict:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam String id) {
		SysDict sysDict = new SysDict();
		sysDict.setId(id);
		sysDict.setStatus("0");
		sysDict.setUpdateBy(SecurityUtils.getLoginName());
		sysDict.setUpdateDate(new Date());
		int cnt = sysDictService.setStatus(sysDict);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	

	@RequestMapping("/getDictType")
	public ResponeModel getDictType(@RequestParam String dictType) {
		List<SysDict> dicts = sysDictService.findTypes(dictType);
		return ResponeModel.ok(dicts);
	}
	
}
