package org.qboot.sys.dto;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import com.alibaba.fastjson.annotation.JSONField;
import org.qboot.common.entity.BaseEntity;

/**
 * 系统用户实体
 * @author iscast
 * @date 2020-09-25
 */
public class SysUserDto extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * 部门
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String deptId;

	/**
	 * 登录名
	 */
	@NotNull
	@Length(min = 3, max = 50)
	private String loginName;

	/**
	 * 姓名
	 */
	@NotNull
	@Length(min = 3, max = 50)
	private String name;

	/**
	 * 手机
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
	@Length(max = 20)
	private String mobile;

	/**
	 * 邮箱
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Email
	private String email;

	/**
	 * 用户类型
	 */
	@Length(max = 2)
	private String userType;

	/**
	 * 状态1：正常，2：禁用 3：初始化
	 */
	private Integer status;

    /**
     * 密码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JSONField(serialize = false)
    private String password;

    /**
     * 盐
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JSONField(serialize = false)
    private String salt;

	/**
	 * 数值备用字段1
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer fldN1;
	/**
	 * 数值备用字段2
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer fldN2;
	/**
	 * 字符串备用字段1
	 */
	@Length(max = 64)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fldS1;
	/**
	 * 字符串备用字段2
	 */
	@Length(max = 256)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fldS2;

    /**
     * 语言
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lang;
	
	/** DB 字段截止 **/
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String deptName;


	/**
	 * 用户所拥有的角色ID
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> roleIds;

    @JsonInclude(JsonInclude.Include.NON_NULL)
	private List<SysMenuDto> menus;

	/**
	 * 通过角色查询用户
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String roleId;

    /**
     * 查询创建时间开始
     */
    @JSONField(serialize = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date startDate;

    /**
     * 查询创建时间结束
     */
    @JSONField(serialize = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date endDate;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

	public List<SysMenuDto> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenuDto> menus) {
		this.menus = menus;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}