package org.qboot.sys.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.qboot.common.entity.BaseEntity;

import javax.validation.constraints.NotNull;

/**
 * 系统类型参数
 * @author iscast
 * @date 2020-09-25
 */
public class SysParamTypeDto extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * 分类类型
	 */
	@NotNull
	private String paramTypeClass;

    /**
     * 分类名称
     */
    @NotNull
    private String paramTypeName;

	/**
	 * 类型ID.不允许修改
	 */
	@NotNull
	private Integer paramTypeId;

	/**
	 * 分类code
	 */
	@NotNull
	private String paramTypeCode;

	/**
	 * 删除标识
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer physicsFlag;

	public String getParamTypeClass() {
		return paramTypeClass;
	}


	public void setParamTypeClass(String paramTypeClass) {
		this.paramTypeClass = paramTypeClass;
	}


	public Integer getParamTypeId() {
		return paramTypeId;
	}


	public void setParamTypeId(Integer paramTypeId) {
		this.paramTypeId = paramTypeId;
	}


	public String getParamTypeCode() {
		return paramTypeCode;
	}


	public void setParamTypeCode(String paramTypeCode) {
		this.paramTypeCode = paramTypeCode;
	}


	public String getParamTypeName() {
		return paramTypeName;
	}


	public void setParamTypeName(String paramTypeName) {
		this.paramTypeName = paramTypeName;
	}


	public Integer getPhysicsFlag() {
		return physicsFlag;
	}


	public void setPhysicsFlag(Integer physicsFlag) {
		this.physicsFlag = physicsFlag;
	}
}