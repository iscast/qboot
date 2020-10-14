package org.qboot.base.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import org.qboot.common.entity.BaseEntity;

/**
 * <p>Title: SysRole</p>
 * <p>Description: 系统角色实体</p>
 * 
 * @author history
 * @date 2018-08-08
 */
public class SysRole extends BaseEntity<String>{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 角色名称
     */
	@NotNull
	@Length(min=1,max=50)
    private String name;
	
	/**下列字段为业务字典**/
	
	private List<String> menuIds;
	private List<String> deptIds;
	
	private String authMenuIds;
	
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

	public List<String> getMenuIds() {
		return menuIds;
	}


	public void setMenuIds(List<String> menuIds) {
		this.menuIds = menuIds;
	}


	public List<String> getDeptIds() {
		return deptIds;
	}


	public void setDeptIds(List<String> deptIds) {
		this.deptIds = deptIds;
	}


	public String getAuthMenuIds() {
		return authMenuIds;
	}


	public void setAuthMenuIds(String authMenuIds) {
		this.authMenuIds = authMenuIds;
	}

}