package com.oseasy.act.modules.actyw.tool.apply;

import java.io.Serializable;

/**
 * 流程申请参数.
 * @author chenhao
 */
public class IAparam implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;//申请ID
    private String actywId;//业务流程ID
    private String gnodeId;//当前流程节点ID

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getActywId() {
        return actywId;
    }
    public void setActywId(String actywId) {
        this.actywId = actywId;
    }
    public String getGnodeId() {
        return gnodeId;
    }
    public void setGnodeId(String gnodeId) {
        this.gnodeId = gnodeId;
    }
}
