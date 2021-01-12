package org.qboot.sys.dto;


/**
 * 角色权限关系实体
 * @author iscast
 * @date 2020-09-25
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
