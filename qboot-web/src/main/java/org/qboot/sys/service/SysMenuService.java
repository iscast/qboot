package org.qboot.sys.service;

import org.qboot.common.facade.ICrudService;
import org.qboot.sys.dao.SysMenuDao;
import org.qboot.sys.dto.SysMenuDto;

import java.io.Serializable;
import java.util.List;


/**
 * 菜单service
 */
public interface SysMenuService extends ICrudService<SysMenuDao, SysMenuDto> {

	List<SysMenuDto> findByParentIds(String parentIds);
	
	List<SysMenuDto> findMenuByParentId(String parentId);
	
	List<SysMenuDto> findParentMenuList(SysMenuDto menu);
	
	int deleteById(Serializable id);

	void batchSave(List<SysMenuDto> list);
	
	List<SysMenuDto> findByRoleId(String roleId);
	
	SysMenuDto findByPermission(String permission);
	
	List<SysMenuDto> findShowMenuByUserId(String userId);
	
	List<SysMenuDto> findShowMenuAll();
	
	/**
	 * 获取用户的权限
	 */
	List<SysMenuDto> qryAuth(String userId);
	
	int changeShowFlag(String menuId, String isShow);
}
