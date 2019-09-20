package com.oseasy.scr.modules.scr.entity;

import java.io.Serializable;

/**
 * Created by PW on 2019/1/17.
 */
public class ScoRapplyVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private ScoRuleDetail rdetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScoRuleDetail getRdetail() {
        return rdetail;
    }

    public void setRdetail(ScoRuleDetail rdetail) {
        this.rdetail = rdetail;
    }
}
