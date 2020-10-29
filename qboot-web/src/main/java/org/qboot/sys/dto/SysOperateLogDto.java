package org.qboot.sys.dto;

import java.util.Date;

import org.qboot.common.entity.BaseEntity;

/**
 * <p>Title: SysOperateLogDto</p>
 * <p>Description: 操作日志实体</p>
 * @author history
 * @date 2018-08-08
 */
public class SysOperateLogDto extends BaseEntity<Long>{

	private static final long serialVersionUID = 1L;

	/** 日志ID */
	private Long logId = 0L;
	
	/**
	 * uri名称
	 */
	private String uriName;

	/**
	 * 请求URI
	 */
	private String requestUri;

	/**
	 * 请求IP
	 */
	private String requestIP;

	/**
	 * 请求用户ID
	 */
	private String requestUserId;

	/**
	 * 请求时间
	 */
	private Date requestDate;
	
	/**
	 * 相应时长
	 */
	private Long responseTime;
	
	/**
	 * 请求参数
	 */
	private String requestParams;

    /**
     * 返回参数
     */
    private String responseParams;
	
	/**
	 * 请求记录开始时间
	 */
	private Long beginTime;
	
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 结束时间
	 */
	private Date endDate;
	

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getUriName() {
		return uriName;
	}

	public void setUriName(String uriName) {
		this.uriName = uriName;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public void setRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}

	public String getRequestUserId() {
		return requestUserId;
	}

	public void setRequestUserId(String requestUserId) {
		this.requestUserId = requestUserId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
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

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }

        @Override
	public String toString() {
		return "SysOperateLogDto [logId=" + logId + ", uriName=" + uriName + ", requestUri=" + requestUri
				+ ", requestIP=" + requestIP + ", requestUserId=" + requestUserId + ", requestDate=" + requestDate
				+ ", responseTime=" + responseTime + ", requestParams=" + requestParams + ", responseParams='" + responseParams  + "']";
	}

	
}
