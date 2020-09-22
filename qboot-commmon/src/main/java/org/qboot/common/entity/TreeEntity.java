package org.qboot.common.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:14
 */
public class TreeEntity<PK extends Serializable> extends BaseEntity<PK> {
    private static final long serialVersionUID = 1L;
    private String parentId;
    private String parentIds;
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

