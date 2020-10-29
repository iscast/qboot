package org.qboot.sys.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.qboot.sys.dao.SysMenuDao;
import org.qboot.sys.dto.SysMenuDto;
import org.qboot.common.constants.SysConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import org.qboot.common.service.CrudService;


/**
 * <p>Title: SysMenuService</p>
 * <p>Description: 菜单service</p>
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysMenuService extends CrudService<SysMenuDao, SysMenuDto> {

	public List<SysMenuDto> findByParentIds(String parentIds){
		Assert.hasLength(parentIds, "parentIdIsEmpty");
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
		Assert.notNull(sysMenu, "menuIsEmpty");
		List<SysMenuDto> list = this.findByParentIds(sysMenu.getParentIds() + sysMenu.getId());
		for (SysMenuDto smenu : list) {
			this.d.deleteRoleMenuByMenuId(smenu.getId());
			super.deleteById(smenu.getId());
		}
		this.d.deleteRoleMenuByMenuId(id);
		return super.deleteById(id);
	}
	
	public void batchSave(List<SysMenuDto> list) {
		Assert.notEmpty(list, "listIsEmpty");
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
		if (StringUtils.isEmpty(permission)) {
			return null;
		}
		List<SysMenuDto> list = this.d.findByPermission(permission);
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	public List<SysMenuDto> findShowMenuByUserId(Long userId){
		Assert.notNull(userId, "userIdIsEmpty");
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
	
	public int updateSelecter(SysMenuDto menu){
		return this.d.updateSelecter(menu);
	}
	
	public int changeShowFlag(String menuId, String isShow) {
		List<Long> childIds = this.d.findChildIdById(menuId);
		SysMenuDto menu = new SysMenuDto();
		menu.setIsShow(isShow);
		menu.setIds(childIds);
		return this.d.changeShowFlag(menu);
	}
}
