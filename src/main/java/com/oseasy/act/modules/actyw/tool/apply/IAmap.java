/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程申请响应参数.
 * @author chenhao
 */
public class IAmap implements Serializable{
    private static final long serialVersionUID = 1L;
    private String iapplysid;//申请子表ID
    private String itaskid;//任务ID
    private String itaskname;//任务名称
    private String ignodeid;//任务节点ID
    private Boolean isfirst;//当前结点是否为第一个节点(第一个节点没有驳回操作)
    private String iregType;//RegType 审核类型
    private Boolean iscore;//是否为学分节点
    private String iastatus;//当前结点状态 AmapStatus
    private String ivstatus;//当前结点状态 AmapVstatus
    private List<IAstatus> iastatuss;//当前可选状态审核页面使用

    public IAmap() {
        super();
        this.isfirst = false;
        this.iscore = false;
        this.iastatus = AmapStatus.NONE.getId();
        this.ivstatus = AmapVstatus.VIEW.getId();
    }

    public String getIgnodeid() {
        return ignodeid;
    }

    public void setIgnodeid(String ignodeid) {
        this.ignodeid = ignodeid;
    }

    public String getItaskname() {
        return itaskname;
    }

    public String getItaskid() {
        return itaskid;
    }

    public void setItaskid(String itaskid) {
        this.itaskid = itaskid;
    }

    public String setItaskname(String itaskname) {
        this.itaskname = itaskname;
        return itaskname;
    }

    public Boolean getIsfirst() {
        if(this.isfirst == null){
            this.isfirst = false;
        }
        return isfirst;
    }

    public void setIsfirst(boolean isfirst) {
        this.isfirst = isfirst;
    }

    public String getIastatus() {
        return iastatus;
    }

    public void setIastatus(String iastatus) {
        this.iastatus = iastatus;
    }

    public String getIvstatus() {
        return ivstatus;
    }

    public void setIvstatus(String ivstatus) {
        this.ivstatus = ivstatus;
    }

    public String getIapplysid() {
        return iapplysid;
    }

    public void setIapplysid(String iapplysid) {
        this.iapplysid = iapplysid;
    }

    public String getIregType() {
        return iregType;
    }

    public void setIregType(String iregType) {
        this.iregType = iregType;
    }

    public List<IAstatus> getIastatuss() {
        if(StringUtil.checkEmpty(this.iastatuss)){
            this.iastatuss = Lists.newArrayList();
            this.iastatuss.add(IAsupstatus.convert(PassNot.PASS));
            this.iastatuss.add(IAsupstatus.convert(PassNot.NOT));
        }
        return iastatuss;
    }

    public void setIastatuss(List<IAstatus> iastatuss) {
        this.iastatuss = iastatuss;
    }

    public Boolean getIscore() {
        if(this.iscore == null){
            this.iscore = false;
        }
        return iscore;
    }

    public void setIscore(boolean iscore) {
        this.iscore = iscore;
    }
}