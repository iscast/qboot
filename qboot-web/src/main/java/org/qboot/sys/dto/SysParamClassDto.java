package org.qboot.sys.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.qboot.common.entity.BaseEntity;

import javax.validation.constraints.NotNull;

/**
 * 系统参数
 * @author iscast
 * @date 2020-09-25
 */
public class SysParamClassDto extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@NotNull
	private String paramTypeClass;

	/**
	 * 键
	 */
	@NotNull
	private String paramTypeName;

	/**
	 * 使用状态
	 */
	private Integer visible;
	
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

	public String getParamTypeName() {
		return paramTypeName;
	}

	public void setParamTypeName(String paramTypeName) {
		this.paramTypeName = paramTypeName;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer getPhysicsFlag() {
		return physicsFlag;
	}

	public void setPhysicsFlag(Integer physicsFlag) {
		this.physicsFlag = physicsFlag;
	}

	
}