package org.qboot.base.controller;

import org.apache.commons.lang3.StringUtils;
import org.qboot.base.dto.SysDept;
import org.qboot.base.service.impl.SysDeptService;
import org.qboot.common.constant.SysConstants;
import org.qboot.common.controller.BaseController;
import org.qboot.common.utils.TreeHelper;
import org.qboot.web.dto.ResponeModel;
import org.qboot.web.security.QUser;
import org.qboot.web.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: DeptController</p>
 * <p>Description: 部门controller</p>
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/dept")
public class DeptController extends BaseController {

	@Autowired
	private SysDeptService sysDeptService;
	private TreeHelper<SysDept> treeHelper = new TreeHelper<SysDept>();

	/**
	 * 用户管理那里的部门查询,只能看到自己及以下部门
	 * @param sysDept
	 * @return
	 */
	@PostMapping("/qryAllWithScope")
	public ResponeModel qryAllWithScope(SysDept sysDept) {
		QUser user = SecurityUtils.getUser();
		String deptId = user.getDeptId();
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return this.qryAll(sysDept);
		}
		//如果是非超级管理员返回自己部门及以下
		SysDept currentDept = sysDeptService.findById(deptId);
		Assert.notNull(currentDept, "当前所属部门为空");
		List<SysDept> list = sysDeptService.findByParentIds(currentDept.getParentIds() + deptId);
		list.add(currentDept);
		return ResponeModel.ok(list);
	}

	@PreAuthorize("hasAuthority('sys:dept:qry')")
	@GetMapping("/qryAll")
	public ResponeModel qryAll(SysDept sysDept) {
		sysDept.setSortField("sort,parent_ids");
		sysDept.setDirection(SysConstants.ASC);
		List<SysDept> list = sysDeptService.findList(sysDept);
		//数据机构调整
		return ResponeModel.ok(treeHelper.treeGridList(list));
	}

	@PreAuthorize("hasAuthority('sys:dept:qry')")
	@GetMapping("/qryDepts")
	public ResponeModel qryParentMenus(@RequestParam(required=false) String parentId) {
		List<SysDept> list = sysDeptService.findByParentIds(parentId);
		return ResponeModel.ok(list);
	}
	
	
	@PreAuthorize("hasAuthority('sys:dept:qry')")
	@RequestMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysDept sysDept = sysDeptService.findById(id);
		if(sysDept != null) {
			return ResponeModel.ok(sysDept);
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:dept:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysDept sysDept, BindingResult bindingResult) {
		SysDept parent = null;
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
	
	@PreAuthorize("hasAuthority('sys:dept:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysDept sysDept, BindingResult bindingResult) {
		SysDept parent = null;
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
