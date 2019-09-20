package com.oseasy.scr.modules.scr.entity;

import java.io.Serializable;

import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * Created by PW on 2019/1/17.
 */
public class ScoRapplyValid implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private ScoRapply apply;
    private User user;
    private ScoRapplyCert scoRapplyCert;

    public ScoRapplyCert getScoRapplyCert() {
        return scoRapplyCert;
    }

    public void setScoRapplyCert(ScoRapplyCert scoRapplyCert) {
        this.scoRapplyCert = scoRapplyCert;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScoRapply getApply() {
        return apply;
    }

    public void setApply(ScoRapply apply) {
        this.apply = apply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
