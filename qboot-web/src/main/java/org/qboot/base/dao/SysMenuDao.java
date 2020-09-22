package org.qboot.base.dao;

import org.qboot.base.dto.SysMenu;
import org.qboot.common.dao.CrudDao;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: SysMenuDao</p>
 * <p>Description: 菜单权限</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysMenuDao extends CrudDao<SysMenu> {

	List<SysMenu> findByRoleId(String roleId);
	List<SysMenu> findByUserId(Long userId);
	List<SysMenu> findByPermission(String permission);
	int deleteRoleMenuByMenuId(Serializable menuId);
	int updateSelecter(SysMenu sysMenu);
	
	List<SysMenu> findParentMenuList(SysMenu sysMenu);
	
	List<Long> findChildIdById(String menuId);

	int changeShowFlag(SysMenu sysMenu);
}