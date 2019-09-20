package com.oseasy.scr.modules.scr.entity;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * Created by PW on 2019/1/17.
 */
public class ScoCreditValue extends DataExtEntity<ScoCreditValue> {

    private static final long serialVersionUID = 1L;

    private String name;
    private String value;
    private String userId;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
