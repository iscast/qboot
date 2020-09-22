package org.qboot.base.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.qboot.common.entity.TreeEntity;

/**
 * <p>Title: SysDept</p>
 * <p>Description: 部门实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysDept extends TreeEntity<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 部门名称
	 */
	@NotNull
	@Length(min=1,max=50)
	private String name;
	
	private String type;
	private Integer hasSub=0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getType() {
		if(super.getParentId() == null || super.getParentId().equals("0")) {
			return "0";
		}else {
			String[] parentIds = super.getParentIds().split(",");
			type = String.valueOf(parentIds.length);
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getHasSub() {
		return hasSub;
	}

	public void setHasSub(Integer hasSub) {
		this.hasSub = hasSub;
	}

}