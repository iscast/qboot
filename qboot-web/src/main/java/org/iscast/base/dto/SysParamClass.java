package org.iscast.base.dto;

import javax.validation.constraints.NotNull;

import org.iscast.common.entity.BaseEntity;

/**
 * <p>Title: SysParamClass</p>
 * <p>Description: 系统参数实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysParamClass extends BaseEntity<String> {

	/**
	 * 
	 */
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
	@NotNull
	private Integer visible;
	
	/**
	 * 删除标识
	 */
	@NotNull
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