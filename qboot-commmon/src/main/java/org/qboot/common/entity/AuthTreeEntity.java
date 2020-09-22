package org.qboot.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: iscast
 * @Date: 2020/8/22 19:13
 */
public class AuthTreeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;
    private List<AuthTreeEntity> list;
    private boolean checked = false;

    public AuthTreeEntity() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<AuthTreeEntity> getList() {
        return this.list;
    }

    public void setList(List<AuthTreeEntity> list) {
        this.list = list;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
