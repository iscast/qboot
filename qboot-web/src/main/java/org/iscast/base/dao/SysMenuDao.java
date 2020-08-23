package org.iscast.base.dao;

import java.io.Serializable;
import java.util.List;

import org.iscast.common.dao.CrudDao;
import org.iscast.base.dto.SysMenu;

/**
 * <p>Title: SysMenuDao</p>
 * <p>Description: 菜单权限</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public interface SysMenuDao extends CrudDao<SysMenu> {

	public List<SysMenu> findByRoleId(String roleId);
	public List<SysMenu> findByUserId(Long userId);
	public List<SysMenu> findByPermission(String permission);
	public int deleteRoleMenuByMenuId(Serializable menuId);
	public int updateSelecter(SysMenu sysMenu);
	
	public List<SysMenu> findParentMenuList(SysMenu sysMenu);
	
	public List<Long> findChildIdById(String menuId);

	public int changeShowFlag(SysMenu sysMenu);
}