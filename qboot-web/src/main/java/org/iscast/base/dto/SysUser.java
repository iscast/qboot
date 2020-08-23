package org.iscast.base.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;

import org.iscast.common.entity.BaseEntity;

/**
 * <p>Title: SysUser</p>
 * <p>Description: 系统用户实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysUser extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	/**
	 * 部门
	 */
	@NotNull
	@Length(min = 1, max = 32)
	private String deptId;

	/**
	 * 登录名
	 */
	@NotNull
	@Length(min = 1, max = 50)
	private String loginName;

	/**
	 * 密码
	 */
	@JSONField(serialize = false)
	private String password;

	/**
	 * 盐
	 */
	@JSONField(serialize = false)
	private String salt;

	/**
	 * 姓名
	 */
	@NotNull
	@Length(min = 1, max = 50)
	private String name;

	/**
	 * 手机
	 */
	@Length(max = 50)
	private String mobile;

	/**
	 * 用户头像
	 */
	@Length(max = 100)
	private String photo;

	/**
	 * 邮箱
	 */
	@Length(max = 50)
	private String email;

	/**
	 * 用户类型，业务扩展用，读取字典
	 */
	@Length(max = 2)
	private String userType;

	/**
	 * 状态1：正常，0：禁用
	 */
	@NotNull
	@Length(min = 1, max = 1)
	@Pattern(regexp = "1|0")
	private String status;

	/**
	 * 数值备用字段1
	 */
    private Integer fldN1;
	/**
	 * 数值备用字段2
	 */
    private Integer fldN2;
	/**
	 * 字符串备用字段1
	 */
	@Length(max = 64)
    private String fldS1;
	/**
	 * 字符串备用字段2
	 */
	@Length(max = 64)
    private String fldS2;
    
	
	/** DB 字段截止 **/
	private String deptName;
	/**
	 * 用户所拥有的角色ID
	 */
	private List<String> roleIds;
	private List<SysMenu> menus;
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_STOP = "0";
	
	/**
	 * 通过角色查询用户
	 */
	private String roleId;
	/**
	 * 是否分配了某个角色
	 */
	private String roleAssigned;
	/**
	 * 图像完整路径
	 */
	private String photoFullUrl;
	/**
	 * 上次登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 登录ip
	 */
	private String lastLoginIp;
	/**
	 * 上次登录地区
	 */
	private String lastLoginArea;

	private String lang;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId == null ? null : deptId.trim();
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName == null ? null : loginName.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt == null ? null : salt.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo == null ? null : photo.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType == null ? null : userType.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public List<SysMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenu> menus) {
		this.menus = menus;
	}

	public String getPhotoFullUrl() {
		return photoFullUrl;
	}

	public void setPhotoFullUrl(String photoFullUrl) {
		this.photoFullUrl = photoFullUrl;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleAssigned() {
		return roleAssigned;
	}

	public void setRoleAssigned(String roleAssigned) {
		this.roleAssigned = roleAssigned;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginArea() {
		return lastLoginArea;
	}

	public void setLastLoginArea(String lastLoginArea) {
		this.lastLoginArea = lastLoginArea;
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