package org.qboot.base.dto;

import org.qboot.common.entity.BaseEntity;

/**
 * <p>Title: SysOperateLogInfoDto</p>
 * <p>Description: 操作日志管理实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysOperateLogInfoDto extends BaseEntity<Long>{

	private static final long serialVersionUID = 1L;
	
	public SysOperateLogInfoDto() {
		super();
	}
	
	public SysOperateLogInfoDto(String reqUri) {
		super();
		this.reqUri = reqUri.trim();
		this.uriType = 0;
	}

	/** ID */
	private Long logId;
	
	/** 请求URI */
	private String reqUri;
	
	/** URI名称 */
	private String uriName;
	
	/** 
	 * URI类型:1:菜单地址，2：操作地址，0：其它;默认为0 
	 * */
	private Integer uriType;
	
	/**
	 * 删除标识：0-删除；1-正常
	 */
	private Integer physicsFlag;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getReqUri() {
		return reqUri;
	}

	public void setReqUri(String reqUri) {
		this.reqUri = reqUri;
	}

	public String getUriName() {
		return uriName;
	}

	public void setUriName(String uriName) {
		this.uriName = uriName;
	}

	public Integer getPhysicsFlag() {
		return physicsFlag;
	}

	public void setPhysicsFlag(Integer physicsFlag) {
		this.physicsFlag = physicsFlag;
	}

	public Integer getUriType() {
		return uriType;
	}

	public void setUriType(Integer uriType) {
		this.uriType = uriType;
	}

}
