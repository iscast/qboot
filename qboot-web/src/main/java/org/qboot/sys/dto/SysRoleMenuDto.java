package org.qboot.sys.dto;


/**
 * <p>Title: SysRoleMenu</p>
 * <p>Description: 角色权限关系实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysRoleMenuDto {

	private String roleId;
	private String menuId;

	public SysRoleMenuDto(String roleId, String menuId) {
		super();
		this.roleId = roleId;
		this.menuId = menuId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
}
