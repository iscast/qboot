package org.qboot.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:14
 */
public class TreeEntity<PK extends Serializable> extends BaseEntity<PK> {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer sort;

    public TreeEntity() {
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return this.parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

