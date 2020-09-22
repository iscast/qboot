package org.qboot.base.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.qboot.base.dao.SysRoleDao;
import org.qboot.base.dto.SysRole;
import org.qboot.base.dto.SysRoleDept;
import org.qboot.base.dto.SysRoleMenu;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.i18n.MessageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Title: SysRoleService</p>
 * <p>Description: 系统角色service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysRoleService extends CrudService<SysRoleDao, SysRole> {

	@Override
	public int save(SysRole t) {
		int cnt = super.save(t);
		if(cnt > 0) {
			SysRole role = d.findByName(t.getName());
			if(role != null) {
				t.setId(role.getId());
			}
		}
		if(StringUtils.isNotBlank(t.getAuthMenuIds())) {
			t.setMenuIds(Arrays.asList(t.getAuthMenuIds().split(",")));
			this.saveRoleMenus(t.getId(), t.getMenuIds());
		}
		return cnt;
	}
	@Override
	public int deleteById(Serializable id) {
		//删除role_menu
		this.d.deleteRoleMenuByRoleId(id);
		//删除user_role
		this.d.deleteUserRoleByRoleId(id);
		//删除role_dept
		this.d.deleteRoleDeptByRoleId(id);
		return this.d.delete((java.lang.String)id);
	}
	@Override
	public int update(SysRole t) {
		this.d.deleteRoleMenuByRoleId(t.getId());
		if(StringUtils.isNotBlank(t.getAuthMenuIds())) {
			t.setMenuIds(Arrays.asList(t.getAuthMenuIds().split(",")));
			this.saveRoleMenus(t.getId(), t.getMenuIds());
		}
		//删除role_dept
		this.d.deleteRoleDeptByRoleId(t.getId());
		this.saveRoleDepts(t.getId(), t.getDeptIds());
		return super.update(t);
	}

	private int saveRoleMenus(String roleId, List<String> menuIds) {
		if (null != menuIds && !menuIds.isEmpty()) {
			List<SysRoleMenu> roleMenus = new ArrayList<>();
			for (String menuId : menuIds) {
				roleMenus.add(new SysRoleMenu(roleId, menuId));
			}
			return this.d.insertRoleMenu(roleMenus);
		}
		return 0;
	}
	
	private int saveRoleDepts(String roleId, List<String> deptIds) {
		if (null != deptIds && !deptIds.isEmpty()) {
			List<SysRoleDept> roleDepts =  new ArrayList<>();
			for (String deptId : deptIds) {
				roleDepts.add(new SysRoleDept(roleId, deptId));
			}
			return this.d.insertRoleDept(roleDepts);
		}
		return 0;
	}

	public List<SysRole> findByUserId(Long userId) {
		Assert.notNull(userId, MessageUtil.getMessage("sys.response.msg.userIdIsEmpty","userId 为空"));
		return  this.d.findByUserId(userId);
	}
	
	public List<String> findDeptIdsByRoleId(String roleId){
		Assert.hasLength(roleId, MessageUtil.getMessage("sys.response.msg.roleIdIsEmpty","roleId 为空"));
		List<String> depts = this.d.selectDeptIdsByRoleId(roleId);
		return depts;
	}
	public int removeUsersByRoleId(String roleId, List<Long> userIds) {
		Assert.hasLength(roleId, MessageUtil.getMessage("sys.response.msg.roleIdIsEmpty","roleId 为空"));
		Assert.notEmpty(userIds, MessageUtil.getMessage("sys.response.msg.removedUserNotExist","移除用户为空"));
		userIds.forEach((userId) -> {
			this.d.deleteUserRoleByRoleIdAndUserId(roleId, userId);
		});
		return userIds.size();
	}
	
	public int addUsersByRoleId(String roleId, List<Long> userIds) {
		Assert.hasLength(roleId, MessageUtil.getMessage("sys.response.msg.roleIdIsEmpty","roleId 为空"));
		Assert.notEmpty(userIds,MessageUtil.getMessage("sys.response.msg.removedUserNotExist","移除用户为空"));
		userIds.forEach((userId) -> {
			this.d.deleteUserRoleByRoleIdAndUserId(roleId, userId);
			this.d.insertUserRole(roleId, userId);
		});
		return userIds.size();
	}

	public List<String> selectMenuIdsByRoleId(Serializable roleId) {
		Assert.notNull(roleId,MessageUtil.getMessage("sys.response.msg.idIsEmpty","ID为空"));
		return this.d.selectMenuIdsByRoleId(roleId);
	}

	@Transactional(readOnly = true)
	public SysRole findByName(String name) {
		Assert.notNull(name, MessageUtil.getMessage("sys.response.msg.roleNameIsEmpty","角色为空"));
		Assert.hasLength(name.toString(), MessageUtil.getMessage("sys.response.msg.roleNameIsEmpty","角色为空"));
		return d.findByName(name);
	}

}
