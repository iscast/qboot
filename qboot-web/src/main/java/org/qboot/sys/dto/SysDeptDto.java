package org.qboot.sys.dto;

import org.qboot.common.entity.TreeEntity;

/**
 * 部门实体
 * @author iscast
 * @date 2020-09-25
 */
public class SysDeptDto extends TreeEntity<String> {
	private static final long serialVersionUID = 1L;

	private String name;
	private Integer hasSub=0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHasSub() {
		return hasSub;
	}

	public void setHasSub(Integer hasSub) {
		this.hasSub = hasSub;
	}
}