package org.iscast.base.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iscast.common.utils.i18n.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.iscast.base.dao.SysMenuDao;
import org.iscast.base.dto.SysMenu;
import org.iscast.common.constant.QConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import org.iscast.common.service.CrudService;


/**
 * <p>Title: SysMenuService</p>
 * <p>Description: 菜单service</p>
 * 
 * @author history
 * @date 2018-08-08
 */
@Service
public class SysMenuService extends CrudService<SysMenuDao, SysMenu> {

	public List<SysMenu> findByParentIds(String parentIds){
		Assert.hasLength(parentIds, MessageUtil.getMessage("sys.response.msg.parentIdIsEmpty","parentIds为空"));
		SysMenu sysMenu = new SysMenu();
		sysMenu.setParentIds(parentIds);
		return this.findList(sysMenu);
	}
	
	public List<SysMenu> findMenuByParentId(String parentId){
		SysMenu sysMenu = new SysMenu();
		if(StringUtils.isNotBlank(parentId)) {
			sysMenu.setParentId(parentId);
		}else {
			sysMenu.setParentId("0");
		}
		return this.findList(sysMenu);
	}
	
	public List<SysMenu> findParentMenuList(SysMenu menu){
		return this.d.findParentMenuList(menu);
	}
	
	@Override
	public int deleteById(Serializable id) {
		//删除下级菜单
		SysMenu sysMenu = this.findById(id);
		Assert.notNull(sysMenu,MessageUtil.getMessage("sys.response.msg.menuIsEmpty","菜单为空"));
		List<SysMenu> list = this.findByParentIds(sysMenu.getParentIds() + sysMenu.getId());
		for (SysMenu smenu : list) {
			this.d.deleteRoleMenuByMenuId(smenu.getId());
			super.deleteById(smenu.getId());
		}
		this.d.deleteRoleMenuByMenuId(id);
		return super.deleteById(id);
	}
	
	public void batchSave(List<SysMenu> list) {
		Assert.notEmpty(list,MessageUtil.getMessage("sys.response.msg.listIsEmpty","批量数据为空"));
		for (SysMenu sysMenu : list) {
			this.update(sysMenu);
		}
	}
	
	public List<SysMenu> findByRoleId(String roleId){
		if (StringUtils.isEmpty(roleId)) {
			return this.findList(null);
		}
		return this.d.findByRoleId(roleId);
	}
	
	public SysMenu findByPermission(String permission){
		if (StringUtils.isEmpty(permission)) {
			return null;
		}
		List<SysMenu> list = this.d.findByPermission(permission);
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	public List<SysMenu> findShowMenuByUserId(Long userId){
		Assert.notNull(userId,MessageUtil.getMessage("sys.response.msg.userIdIsEmpty","用户id 为空"));
		return this.d.findByUserId(userId);
	}
	
	public List<SysMenu> findShowMenuAll(){
		SysMenu menu = new SysMenu();
		menu.setIsShow(QConstants.YES);
		menu.setSortField("sort,parent_ids");
		menu.setDirection(QConstants.ASC);
		return this.findList(menu);
	} 
	
	/**
	 * 获取用户的权限
	 * @param userId
	 * @return
	 */
	public List<SysMenu> qryAuth(Long userId){
		return this.d.findByUserId(userId);
	}
	
	public int updateSelecter(SysMenu menu){
		return this.d.updateSelecter(menu);
	}
	
	public int changeShowFlag(String menuId, String isShow) {
		List<Long> childIds = new ArrayList<Long>();
		childIds = this.d.findChildIdById(menuId);
		SysMenu menu = new SysMenu();
		menu.setIsShow(isShow);
		menu.setIds(childIds);
		return this.d.changeShowFlag(menu);
	}
}
