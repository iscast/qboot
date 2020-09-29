package org.qboot.base.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.base.dto.SysRole;
import org.qboot.base.service.impl.SysRoleService;
import org.qboot.common.controller.BaseController;
import org.qboot.web.dto.ResponeModel;
import org.qboot.web.security.QUser;
import org.qboot.web.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: RoleController</p>
 * <p>Description: sysRole</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@RestController
@RequestMapping("${admin.path}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SysRoleService sysRoleService;
	
	@PreAuthorize("hasAuthority('sys:role:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(@Validated SysRole sysRole, BindingResult bindingResult) {
		if(!SecurityUtils.isSuperAdmin()) {
			sysRole.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
		}
		PageInfo<SysRole> page = sysRoleService.findByPage(sysRole);
		return ResponeModel.ok(page);
	}
	
	@PostMapping("/qryAllWithScope")
	public ResponeModel qryAllWithScope() {
		QUser user = SecurityUtils.getUser();
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return this.qryAll();
		}
		//如果是非超级管理员返回自己拥有的角色
		List<SysRole> list = sysRoleService.findByUserId(user.getUserId());
		return ResponeModel.ok(list);
	}
	/**
	 * 添加修改用户时候会调用
	 * @return
	 */
	@PostMapping("/qryAll")
	public ResponeModel qryAll() {
		return ResponeModel.ok(this.sysRoleService.findList(null));
	}
	
	@PreAuthorize("hasAuthority('sys:role:qry')")
	@GetMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysRole sysRole = sysRoleService.findById(id);
		if(sysRole != null) {
			List<String> menuIds = sysRoleService.selectMenuIdsByRoleId(id);
			if(!CollectionUtils.isEmpty(menuIds)) {
				sysRole.setMenuIds(menuIds);
			}
			return ResponeModel.ok(sysRole);
		}else {
			return ResponeModel.error("sys.response.msg.failToFindRole");
		}
	}
	
	/**
	 * 角色关联的部门
	 * @param id
	 * @return
	 */
	@RequestMapping("/qryDeptIdsByRoleId")
	public ResponeModel qryDeptIdsByRoleId(@RequestParam String roleId) {
		return ResponeModel.ok(sysRoleService.findDeptIdsByRoleId(roleId));
	}
	
	@PreAuthorize("hasAuthority('sys:role:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysRole sysRole, BindingResult bindingResult) {
		SysRole role = sysRoleService.findByName(sysRole.getName());
		if(role != null) {
			return ResponeModel.error("sys.response.msg.roleDuplicate");
		}
		
		sysRole.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
		sysRole.setCreateDate(new Date());
		int cnt = sysRoleService.save(sysRole);
		if(cnt > 0) {
			SysRole newRole = sysRoleService.findByName(sysRole.getName());
			List<Long> list = new ArrayList();
			list.add(SecurityUtils.getUserId());
			sysRoleService.addUsersByRoleId(newRole.getId(), list);
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}

	@PreAuthorize("hasAuthority('sys:role:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysRole sysRole, BindingResult bindingResult) {
		SysRole role = sysRoleService.findByName(sysRole.getName());
		if(role != null && !String.valueOf(role.getId()).equals(String.valueOf(sysRole.getId()))) {
			return ResponeModel.error("sys.response.msg.roleDuplicate");
		}
		sysRole.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
		sysRole.setUpdateDate(new Date());
		int cnt = sysRoleService.update(sysRole);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:role:update')")
	@PostMapping("/removeUser")
	public ResponeModel removeUser(@RequestParam List<Long> userIds,String roleId) {
		Assert.notEmpty(userIds,"sys.response.msg.removedUserNotExist");
		int cnt = sysRoleService.removeUsersByRoleId(roleId, userIds);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:role:update')")
	@PostMapping("/addUser")
	public ResponeModel addUser(@RequestParam List<Long> userIds,String roleId) {
		Assert.notEmpty(userIds,"sys.response.msg.removedUserNotExist");
		int cnt = sysRoleService.addUsersByRoleId(roleId, userIds);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
	
	@PreAuthorize("hasAuthority('sys:role:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysRoleService.deleteById(id);
		if(cnt > 0) {
			return ResponeModel.ok();
		}
		return ResponeModel.error();
	}
}
