package org.qboot.sys.controller;

import com.github.pagehelper.PageInfo;
import org.qboot.common.annotation.AccLog;
import org.qboot.common.controller.BaseController;
import org.qboot.common.entity.ResponeModel;
import org.qboot.common.security.CustomUser;
import org.qboot.common.security.SecurityUtils;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.service.impl.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * @author iscast
 * @date 2020-09-25
 */
@RestController
@RequestMapping("${admin.path}/sys/role")
public class SysRoleController extends BaseController {

	@Autowired
	private SysRoleService sysRoleService;
	
	@PreAuthorize("hasAuthority('sys:role:qry')")
	@PostMapping("/qryPage")
	public ResponeModel qryPage(@Validated SysRoleDto sysRole, BindingResult bindingResult) {
		if(!SecurityUtils.isSuperAdmin()) {
			sysRole.setCreateBy(SecurityUtils.getLoginName());
		}
		PageInfo<SysRoleDto> page = sysRoleService.findByPage(sysRole);
		return ResponeModel.ok(page);
	}
	
	@PostMapping("/qryAllWithScope")
	public ResponeModel qryAllWithScope() {
		CustomUser user = SecurityUtils.getUser();
		if (SecurityUtils.isSuperAdmin(user.getLoginName())) {
			return this.qryAll();
		}

		//如果是非超级管理员返回自己拥有的角色
		List<SysRoleDto> list = sysRoleService.findByUserId(user.getUserId());
		if(CollectionUtils.isEmpty(list)) {
            return ResponeModel.error(SYS_ROLE_QUERY_NULL);
        }
		return ResponeModel.ok(list);
	}
	/**
	 * 添加修改用户时候会调用
	 * @return
	 */
	@PostMapping("/qryAll")
	public ResponeModel qryAll() {
        List<SysRoleDto> list = this.sysRoleService.findList(null);
        if(CollectionUtils.isEmpty(list)) {
            return ResponeModel.error(SYS_ROLE_QUERY_NULL);
        }

        return ResponeModel.ok(list);
	}
	
	@PreAuthorize("hasAuthority('sys:role:qry')")
	@GetMapping("/get")
	public ResponeModel get(@RequestParam Serializable id) {
		SysRoleDto sysRole = sysRoleService.findById(id);
		if(sysRole != null) {
			List<String> menuIds = sysRoleService.selectMenuIdsByRoleId(id);
			if(!CollectionUtils.isEmpty(menuIds)) {
				sysRole.setMenuIds(menuIds);
			}
			return ResponeModel.ok(sysRole);
		}else {
			return ResponeModel.error(SYS_ROLE_QUERY_FAIL);
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

    @AccLog
	@PreAuthorize("hasAuthority('sys:role:save')")
	@PostMapping("/save")
	public ResponeModel save(@Validated SysRoleDto sysRole, BindingResult bindingResult) {
		SysRoleDto role = sysRoleService.findByName(sysRole.getName());
		if(role != null) {
			return ResponeModel.error(SYS_ROLE_DUPLICATE);
		}
		
		sysRole.setCreateBy(SecurityUtils.getLoginName());
		sysRole.setCreateDate(new Date());
		int cnt = sysRoleService.save(sysRole);
		if(cnt > 0) {
			SysRoleDto newRole = sysRoleService.findByName(sysRole.getName());
			List<Long> list = new ArrayList();
			list.add(SecurityUtils.getUserId());
			sysRoleService.addUsersByRoleId(newRole.getId(), list);
			return ok();
		}
		return ResponeModel.error(SYS_ROLE_SAVE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:role:update')")
	@PostMapping("/update")
	public ResponeModel update(@Validated SysRoleDto sysRole, BindingResult bindingResult) {
		SysRoleDto role = sysRoleService.findByName(sysRole.getName());
		if(role != null && !String.valueOf(role.getId()).equals(String.valueOf(sysRole.getId()))) {
			return ResponeModel.error(SYS_ROLE_DUPLICATE);
		}
		sysRole.setUpdateBy(SecurityUtils.getLoginName());
		sysRole.setUpdateDate(new Date());
		int cnt = sysRoleService.update(sysRole);
		if(cnt > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_ROLE_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:role:update')")
	@PostMapping("/removeUser")
	public ResponeModel removeUser(@RequestParam List<Long> userIds,String roleId) {
        MyAssertTools.notEmpty(userIds, SYS_ROLE_REMOVED_USER_NOT_EXIST);
		int cnt = sysRoleService.removeUsersByRoleId(roleId, userIds);
		if(cnt > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_ROLE_DELETE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:role:update')")
	@PostMapping("/addUser")
	public ResponeModel addUser(@RequestParam List<Long> userIds,String roleId) {
	    MyAssertTools.notEmpty(userIds, SYS_ROLE_ADD_USER_NOT_EXIST);
		int cnt = sysRoleService.addUsersByRoleId(roleId, userIds);
		if(cnt > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_ROLE_UPDATE_FAIL);
	}

    @AccLog
	@PreAuthorize("hasAuthority('sys:role:delete')")
	@PostMapping("/delete")
	public ResponeModel delete(@RequestParam Serializable id) {
        MyAssertTools.notNull(id, SYS_ROLE_ID_NULL);
		if(sysRoleService.deleteById(id) > 0) {
			return ok();
		}
		return ResponeModel.error(SYS_ROLE_DELETE_FAIL);
	}
}
