package org.qboot.sys.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.qboot.common.entity.BaseEntity;

import javax.validation.constraints.NotNull;

/**
 * 系统字典
 * @author iscast
 * @date 2020-09-25
 */
public class SysDictDto extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	/**
     * 类型
     */
	@NotNull
    private String type;

    /**
     * 编码
     */
	@NotNull
    private String code;

    /**
     * 名称
     */
	@NotNull
    private String name;

    /**
     * 排序
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer sort;

    /**
     * 是否启用1：是，0：否
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    
    public String getCode() {
        return code;
    }

    
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    
    public Integer getSort() {
        return sort;
    }

    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}