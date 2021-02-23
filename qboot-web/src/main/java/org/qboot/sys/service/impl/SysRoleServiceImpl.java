package org.qboot.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dao.SysRoleDao;
import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.dto.SysRoleDeptDto;
import org.qboot.sys.dto.SysRoleMenuDto;
import org.qboot.common.service.CrudService;
import org.qboot.sys.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;

/**
 * 系统角色service
 * @author history
 * @date 2020-09-25
 */
@Service
public class SysRoleServiceImpl extends CrudService<SysRoleDao, SysRoleDto> implements SysRoleService {

	@Override
	public int save(SysRoleDto t) {
		int cnt = super.save(t);
		if(cnt > 0) {
			SysRoleDto role = d.findByName(t.getName());
			if(role != null) {
				t.setId(role.getId());
			}
		}
		if(StringUtils.isNotBlank(t.getAuthMenuIds())) {
			t.setMenuIds(Arrays.asList(StringUtils.split(t.getAuthMenuIds(), SysConstants.GAP_COMMA)));
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
	public int update(SysRoleDto t) {
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

	public List<SysRoleDto> findByUserId(String userId) {
        MyAssertTools.notNull(userId, SYS_ROLE_USER_ID_NULL);
		return  this.d.findByUserId(userId);
	}
	
	public List<String> findDeptIdsByRoleId(String roleId){
        MyAssertTools.hasLength(roleId, SYS_ROLE_ID_NULL);
		List<String> depts = this.d.selectDeptIdsByRoleId(roleId);
		return depts;
	}
	public int removeUsersByRoleId(String roleId, List<String> userIds) {
        MyAssertTools.hasLength(roleId, SYS_ROLE_ID_NULL);
        MyAssertTools.notEmpty(userIds, SYS_ROLE_USER_IDS_NULL);
		userIds.forEach((userId) -> {
			this.d.deleteUserRoleByRoleIdAndUserId(roleId, userId);
		});
		return userIds.size();
	}
	
	public int addUsersByRoleId(String roleId, List<String> userIds) {
        MyAssertTools.hasLength(roleId, SYS_ROLE_ID_NULL);
        MyAssertTools.notEmpty(userIds, SYS_ROLE_USER_IDS_NULL);
		userIds.forEach((userId) -> {
			this.d.deleteUserRoleByRoleIdAndUserId(roleId, userId);
			this.d.insertUserRole(roleId, userId);
		});
		return userIds.size();
	}

	public List<String> selectMenuIdsByRoleId(Serializable roleId) {
        MyAssertTools.notNull(roleId, SYS_ROLE_ID_NULL);
		return this.d.selectMenuIdsByRoleId(roleId);
	}

	@Transactional(readOnly = true)
	public SysRoleDto findByName(String name) {
        MyAssertTools.hasLength(name, SYS_ROLE_NAME_NULL);
		return d.findByName(name);
	}


    private int saveRoleMenus(String roleId, List<String> menuIds) {
        if (null != menuIds && !menuIds.isEmpty()) {
            List<SysRoleMenuDto> roleMenus = new ArrayList<>();
            for (String menuId : menuIds) {
                roleMenus.add(new SysRoleMenuDto(roleId, menuId));
            }
            return this.d.insertRoleMenu(roleMenus);
        }
        return 0;
    }

    private int saveRoleDepts(String roleId, List<String> deptIds) {
        if (null != deptIds && !deptIds.isEmpty()) {
            List<SysRoleDeptDto> roleDepts =  new ArrayList<>();
            for (String deptId : deptIds) {
                roleDepts.add(new SysRoleDeptDto(roleId, deptId));
            }
            return this.d.insertRoleDept(roleDepts);
        }
        return 0;
    }
}
