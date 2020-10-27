package org.qboot.base.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import org.qboot.common.constant.SysConstants;
import org.qboot.common.entity.BaseEntity;

/**
 * <p>Title: SysLoginLog</p>
 * <p>Description: 登陆日志实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysLoginLog extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	@NotNull
	@Length(min = 1, max = 32)
	private Long userId;
	/**
	 * 登录状态0:成功;1.密码错误；2.已禁用;3.系统错误
	 */
	@NotNull
	@Length(min = 1, max = 1)
	private String status;
	/**
	 * 登录时间
	 */
	@NotNull
	private Date loginTime;
	/**
	 * IP地址
	 */
	@Length(max = 16)
	private String ip;
	/**
	 * 登录地区
	 */
	@Length(max = 20)
	private String area;
	/**
	 * 用户代理
	 */
	@NotNull
	@Length(min = 1, max = 1000)
	private String userAgent;
	/**
	 * 设备名称
	 */
	@Length(max = 100)
	private String deviceName;
	/**
	 * 浏览器名称
	 */
	@Length(max = 100)
	private String browserName;
	/********* 割 *******/

	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 结束时间
	 */
	private Date endDate;
	
	/**
	 * 是否需要修改密码：0-正常；1-初始化密码；2-已修改密码
	 */
	private int firstLogin = SysConstants.SYS_USER_PWD_STATUS_NORMAL;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getBrowserName() {
		return browserName;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(int firstLogin) {
		this.firstLogin = firstLogin;
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

	@Override
	public String toString() {
		return "SysLoginLog [userId=" + userId + ", status=" + status + ", loginTime=" + loginTime + ", ip=" + ip
				+ ", area=" + area + ", userAgent=" + userAgent + ", deviceName=" + deviceName + ", browserName="
				+ browserName + ", loginName=" + loginName + ", userName=" + userName + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", firstLogin=" + firstLogin + "]";
	}
	
	
}