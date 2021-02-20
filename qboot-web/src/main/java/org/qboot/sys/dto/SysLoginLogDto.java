package org.qboot.sys.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import org.qboot.common.constants.SysConstants;
import org.qboot.common.entity.BaseEntity;

/**
 * 登陆日志实体
 * @author iscast
 * @date 2020-09-25
 */
public class SysLoginLogDto extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    private String name;
	/**
	 * 登录状态0:成功;1.密码错误；2.已禁用;3.锁定24小时;7.修改密码;8.初始化密码;9.系统错误；
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
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 结束时间
	 */
	private Date endDate;

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
		return "SysLoginLog [status=" + status + ", loginTime=" + loginTime + ", ip=" + ip
				+ ", area=" + area + ", userAgent=" + userAgent + ", deviceName=" + deviceName + ", browserName="
				+ browserName + ", loginName=" + loginName + ", name=" + name + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}
	
	
}