package org.qboot.sys.dao;

import org.qboot.common.dao.CrudDao;
import org.qboot.sys.dto.SysMenuDto;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: SysMenuDao</p>
 * <p>Description: 菜单权限</p>
 * @author history
 * @date 2018-08-08
 */
public interface SysMenuDao extends CrudDao<SysMenuDto> {

	List<SysMenuDto> findByRoleId(String roleId);
	List<SysMenuDto> findByUserId(Long userId);
	List<SysMenuDto> findByPermission(String permission);
	int deleteRoleMenuByMenuId(Serializable menuId);
	List<SysMenuDto> findParentMenuList(SysMenuDto sysMenu);
	List<Long> findChildIdById(String menuId);
	int changeShowFlag(SysMenuDto sysMenu);
}