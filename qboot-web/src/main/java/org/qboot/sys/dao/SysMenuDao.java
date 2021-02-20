package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysMenuDto;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单权限
 * @author iscast
 * @date 2020-09-25
 */
public interface SysMenuDao extends CrudDao<SysMenuDto> {

	List<SysMenuDto> findByRoleId(String roleId);
	List<SysMenuDto> findByUserId(String userId);
	List<SysMenuDto> findByPermission(String permission);
	int deleteRoleMenuByMenuId(Serializable menuId);
	List<SysMenuDto> findParentMenuList(SysMenuDto sysMenu);
	List<String> findChildIdById(String menuId);
	int changeShowFlag(SysMenuDto sysMenu);
}