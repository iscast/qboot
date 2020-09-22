package org.qboot.web.security;

import java.io.Serializable;
import java.util.List;

import org.qboot.base.dto.SysRole;

/**
 * <p>Title: LoginUser</p>
 * <p>Description: 用户信息</p>
 * 
 * @author history
 * @date 2018-09-08
 */
public class QUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String deptId;
	private String deptName;
	private String loginName;
	private String name;
	private String status;
	private String lang;

	private Integer fldN1;
    private Integer fldN2;
    private String fldS1;
    private String fldS2;
	
	private List<SysRole> roles;

	public QUser(Long userId, String deptId, String deptName, String loginName,
                 String name, String status, Integer fldN1, Integer fldN2, String fldS1, String fldS2, String lang) {
		super();
		this.userId = userId;
		this.deptId = deptId;
		this.deptName = deptName;
		this.loginName = loginName;
		this.name = name;
		this.status = status;
		this.fldN1 = fldN1;
		this.fldN2 = fldN2;
		this.fldS1 = fldS1;
		this.fldS2 = fldS2;
		this.lang = lang;
	}


	public List<SysRole> getRoles() {
		return roles;
	}


	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
