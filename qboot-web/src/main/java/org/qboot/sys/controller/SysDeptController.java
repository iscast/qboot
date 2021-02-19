package org.qboot.sys.controller;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.CustomUser;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.TreeHelper;
import org.qboot.common.utils.ValidateUtils;
import org.qboot.sys.dto.SysDeptDto;
import org.qboot.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 部门
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@Controller
@RequestMapping("${admin.path}/sys/dept")
public class SysDeptController extends BaseController {

	@Autowired
	private SysDeptService sysDeptService;
	private TreeHelper<SysDeptDto> treeHelper = new TreeHelper<SysDeptDto>();

	/**
	 * 用户管理那里的部门查询,只能看到自己及以下部门
	 * @param sysDept
	 * @return
	 */
	@PostMapping("/qryAllWithScope")
	public ResponeModel qryAllWithScope(SysDeptDto sysDept) {
		CustomUser user = SecurityUtils.getUser();
		String deptId = user.getDeptId();
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return this.qryAll(sysDept);
		}
		//如果是非超级管理员返回自己部门及以下
		SysDeptDto currentDept = sysDeptService.findById(deptId);
		Assert.notNull(currentDept, "当前所属部门为空");
		List<SysDeptDto> list = sysDeptService.findByParentIds(currentDept.getParentIds() + deptId);
		list.add(currentDept);
		return ResponeModel.ok(list);
	}

	@PreAuthorize("hasAuthority('sys:dept:qry')")
	@GetMapping("/qryAll")
	public ResponeModel qryAll(SysDeptDto sysDept) {
		List<SysDeptDto> list = sysDeptService.findList(sysDept);
		//数据机构调整
		return ResponeModel.ok(treeHelper.treeGridList(list));
	}

	@PreAuthorize("hasAuthority('sys:dept:qry')")
	@GetMapping("/qryDepts")
	public ResponeModel qryParentMenus(@RequestParam(required=false) String parentId) {
		List<SysDeptDto> list = sysDeptService.findByParentIds(parentId);
		return ResponeModel.ok(list);
	}
	
	
	@PreAuthorize("hasAuthority('sys:dept:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysDeptDto sysDept = sysDeptService.findById(id);
		if(sysDept != null) {
			return ResponeModel.ok(sysDept);
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dept:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysDeptDto sysDept, BindingResult bindingResult) {
        ValidateUtils.checkBind(bindingResult);
		SysDeptDto parent = null;
		if (StringUtils.isNotEmpty(sysDept.getParentId())) {
			parent = sysDeptService.findById(sysDept.getParentId());
		} 
		treeHelper.setParent(sysDept, parent);
		int cnt = sysDeptService.save(sysDept);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dept:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysDeptDto sysDept, BindingResult bindingResult) {
        ValidateUtils.checkBind(bindingResult);
		SysDeptDto parent = null;
		if (StringUtils.isNotEmpty(sysDept.getParentId())) {
			parent = sysDeptService.findById(sysDept.getParentId());
		} 
		treeHelper.setParent(sysDept, parent);
		int cnt = sysDeptService.update(sysDept);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:dept:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysDeptService.deleteById(id);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
}
