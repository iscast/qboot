package org.qboot.common.security;

import java.io.Serializable;
import java.util.List;

import org.qboot.sys.dto.SysRoleDto;
import org.qboot.sys.dto.SysUserDto;

/**
 * for spring Security Custom User
 * @author iscast
 * @date 2020-09-25
 */
public class CustomUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String deptId;
	private String deptName;
	private String loginName;
	private String name;
	private Integer status;
	private String lang;

	private Integer fldN1;
    private Integer fldN2;
    private String fldS1;
    private String fldS2;
	
	private List<SysRoleDto> roles;

	public CustomUser(SysUserDto sysUser) {
		super();
		this.userId = sysUser.getId();
		this.deptId = sysUser.getDeptId();
		this.deptName = sysUser.getDeptName();
		this.loginName = sysUser.getLoginName();
		this.name = sysUser.getName();
		this.status = sysUser.getStatus();
		this.fldN1 = sysUser.getFldN1();
		this.fldN2 = sysUser.getFldN2();
		this.fldS1 = sysUser.getFldS1();
		this.fldS2 = sysUser.getFldS2();
		this.lang = sysUser.getLang();
	}


	public List<SysRoleDto> getRoles() {
		return roles;
	}


	public void setRoles(List<SysRoleDto> roles) {
		this.roles = roles;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFldN1() {
		return fldN1;
	}

	public void setFldN1(Integer fldN1) {
		this.fldN1 = fldN1;
	}

	public Integer getFldN2() {
		return fldN2;
	}

	public void setFldN2(Integer fldN2) {
		this.fldN2 = fldN2;
	}

	public String getFldS1() {
		return fldS1;
	}

	public void setFldS1(String fldS1) {
		this.fldS1 = fldS1;
	}

	public String getFldS2() {
		return fldS2;
	}

	public void setFldS2(String fldS2) {
		this.fldS2 = fldS2;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
