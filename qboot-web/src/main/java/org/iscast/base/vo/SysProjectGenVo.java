package org.iscast.base.vo;

import java.io.Serializable;

public class SysProjectGenVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 项目名
	 */
	private String projectName;
	
	/**
	 * 项目类型1.web项目2.interface项目3.web+interface项目
	 */
	private int projectType;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getProjectType() {
		return projectType;
	}

	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}

}
