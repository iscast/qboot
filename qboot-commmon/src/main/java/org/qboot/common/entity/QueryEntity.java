package org.qboot.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.qboot.common.utils.SQLFilterUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:07
 */
public class QueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sortField;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String direction;
    @JsonIgnore
    private Integer page = 1;
    @JsonIgnore
    private Integer limit = 10;
    @JsonIgnore
    private Integer maxLimit = 1000;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> ext;

    public QueryEntity() {
    }

    public Map<String, Object> addProperty(String key, Object value) {
        if (null == value) {
            return this.ext;
        } else {
            if (this.ext == null) {
                this.ext = new HashMap(1);
            }

            this.ext.put(key, value);
            return this.ext;
        }
    }

    public String getSortField() {
        return SQLFilterUtils.sqlFilter(this.sortField);
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getDirection() {
        return SQLFilterUtils.sqlFilter(this.direction);
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Map<String, Object> getExt() {
        return this.ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        if (null == this.limit) {
            this.limit = 10;
        } else if (null != this.maxLimit && this.limit > this.maxLimit) {
            this.limit = this.maxLimit;
        }

        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
