package org.qboot.sys.dto;

/**
 * <p>Title: SysUserRole</p>
 * <p>Description: 用户角色关系实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysUserRoleDto {

	private String userId;
	private String roleId;

	public SysUserRoleDto(String userId, String roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
