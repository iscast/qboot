package org.qboot.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:08
 */
public class BaseEntity<PK extends Serializable> extends QueryEntity {
    private static final long serialVersionUID = 1L;
    private PK id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updateBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date updateDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remarks;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long version;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer physicsFlag;

    public BaseEntity() {
    }

    public PK getId() {
        return this.id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getPhysicsFlag() {
        return physicsFlag;
    }

    public void setPhysicsFlag(Integer physicsFlag) {
        this.physicsFlag = physicsFlag;
    }
}
