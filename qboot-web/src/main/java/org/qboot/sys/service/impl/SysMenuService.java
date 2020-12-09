package org.qboot.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.service.CrudService;
import org.qboot.common.utils.MyAssertTools;
import org.qboot.sys.dao.SysMenuDao;
import org.qboot.sys.dto.SysMenuDto;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

import static org.qboot.sys.exception.errorcode.SysModuleErrTable.*;


/**
 * <p>Title: SysMenuService</p>
 * <p>Description: 菜单service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysMenuService extends CrudService<SysMenuDao, SysMenuDto> {

	public List<SysMenuDto> findByParentIds(String parentIds){
        MyAssertTools.hasLength(parentIds, SYS_MENU_PARENTID_NULL);
		SysMenuDto sysMenu = new SysMenuDto();
		sysMenu.setParentIds(parentIds);
		return this.findList(sysMenu);
	}
	
	public List<SysMenuDto> findMenuByParentId(String parentId){
		SysMenuDto sysMenu = new SysMenuDto();
		if(StringUtils.isNotBlank(parentId)) {
			sysMenu.setParentId(parentId);
		}else {
			sysMenu.setParentId("0");
		}
		return this.findList(sysMenu);
	}
	
	public List<SysMenuDto> findParentMenuList(SysMenuDto menu){
		return this.d.findParentMenuList(menu);
	}
	
	@Override
	public int deleteById(Serializable id) {
		//删除下级菜单
		SysMenuDto sysMenu = this.findById(id);
		MyAssertTools.notNull(sysMenu, SYS_MENU_NO_EXIST);
		List<SysMenuDto> list = this.findByParentIds(sysMenu.getParentIds() + sysMenu.getId());
		for (SysMenuDto smenu : list) {
			this.d.deleteRoleMenuByMenuId(smenu.getId());
			super.deleteById(smenu.getId());
		}
		this.d.deleteRoleMenuByMenuId(id);
		return super.deleteById(id);
	}
	
	public void batchSave(List<SysMenuDto> list) {
	    MyAssertTools.notEmpty(list, SYS_MENU_LIST_EMPTY);
		for (SysMenuDto sysMenu : list) {
			this.update(sysMenu);
		}
	}
	
	public List<SysMenuDto> findByRoleId(String roleId){
		if (StringUtils.isEmpty(roleId)) {
			return this.findList(null);
		}
		return this.d.findByRoleId(roleId);
	}
	
	public SysMenuDto findByPermission(String permission){
	    MyAssertTools.hasLength(permission, SYS_MENU_PERMISSION_NULL);
		List<SysMenuDto> list = this.d.findByPermission(permission);
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	public List<SysMenuDto> findShowMenuByUserId(Long userId){
	    MyAssertTools.notNull(userId, SYS_MENU_USER_ID_NULL);
		return this.d.findByUserId(userId);
	}
	
	public List<SysMenuDto> findShowMenuAll(){
		SysMenuDto menu = new SysMenuDto();
		menu.setIsShow(SysConstants.YES);
		menu.setSortField("sort,parent_ids");
		menu.setDirection(SysConstants.ASC);
		return this.findList(menu);
	} 
	
	/**
	 * 获取用户的权限
	 * @param userId
	 * @return
	 */
	public List<SysMenuDto> qryAuth(Long userId){
		return this.d.findByUserId(userId);
	}
	
	public int changeShowFlag(String menuId, String isShow) {
		List<Long> childIds = this.d.findChildIdById(menuId);
		SysMenuDto menu = new SysMenuDto();
		menu.setIsShow(isShow);
		menu.setIds(childIds);
		return this.d.changeShowFlag(menu);
	}
}
