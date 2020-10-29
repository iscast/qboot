package org.qboot.sys.dto;

/**
 * <p>Title: SysRoleDept</p>
 * <p>Description: 系统角色部门关系实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysRoleDeptDto {

	private String roleId;
	private String deptId;
	
	public SysRoleDeptDto(String roleId, String deptId) {
		super();
		this.roleId = roleId;
		this.deptId = deptId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	
}