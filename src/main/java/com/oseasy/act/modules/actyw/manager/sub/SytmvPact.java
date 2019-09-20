package com.oseasy.act.modules.actyw.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;
import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytmvo;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmTenant;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmvTenant;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvPact extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 当前流程group.id.
     */
    private String id;
    private String targetId;
    private String scid;

    /**
     * 当前租户.
     */
    private SytmvTenant sytmvTenant;

    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/
    /**
     * 当前项目流程.
     */
    private ActYw actYw;
    /**
     * 当前流程.
     */
    private ActYwGroup group;
    /**
     * 当前流程模板.
     */
    private ActYwGroup groupTpl;
    /**
     * 当前流程节点.
     */
    private List<ActYwGnode> gnodes;
    /**
     * 当前流程主题.
     */
    private List<ActYwGtheme> gthemes;
    /**
     * 当前流程角色.
     */
    private List<ActYwGrole> groles;
    /**
     * 当前流程时间.
     */
    private List<ActYwGtime> gtimes;
    /**
     * 当前流程状态.
     */
    private List<ActYwGstatus> gstatuss;
    /**
     * 当前流程表单.
     */
    private List<ActYwGform> gforms;

    /**
     * 当前流程指派.
     */
    private List<ActYwGassign> gassigns;

    /**
     * 当前流程指派规则.
     */
    private List<ActYwEtAssignRule> etAssignRules;

    /**
     * 当前流程指派专家.
     */
    private List<ActYwEtAuditNum> etAuditNums;

    public SytmvPact() {}

    public SytmvPact(String targetId, String id, String scid, SytmvTenant sytmvTenant) {
        this.id = id;
        this.scid = scid;
        this.targetId = targetId;
        this.sytmvTenant = sytmvTenant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public SytmvTenant getSytmvTenant() {
        return sytmvTenant;
    }

    public void setSytmvTenant(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }

    public ActYwGroup getGroup() {
        return group;
    }

    public void setGroup(ActYwGroup group) {
        this.group = group;
    }

    public ActYwGroup getGroupTpl() {
        return groupTpl;
    }

    public void setGroupTpl(ActYwGroup groupTpl) {
        this.groupTpl = groupTpl;
    }

    public List<ActYwGnode> getGnodes() {
        if(this.gnodes == null){
            this.gnodes = Lists.newArrayList();
        }
        return gnodes;
    }

    public void setGnodes(List<ActYwGnode> gnodes) {
        this.gnodes = gnodes;
    }

    public ActYw getActYw() {
        return actYw;
    }

    public void setActYw(ActYw actYw) {
        this.actYw = actYw;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public List<ActYwEtAuditNum> getEtAuditNums() {
        return etAuditNums;
    }

    public void setEtAuditNums(List<ActYwEtAuditNum> etAuditNums) {
        this.etAuditNums = etAuditNums;
    }

    public List<ActYwEtAssignRule> getEtAssignRules() {
        return etAssignRules;
    }

    public void setEtAssignRules(List<ActYwEtAssignRule> etAssignRules) {
        this.etAssignRules = etAssignRules;
    }

    public List<ActYwGassign> getGassigns() {
        if(this.gassigns == null){
            this.gassigns = Lists.newArrayList();
        }
        return gassigns;
    }

    public void setGassigns(List<ActYwGassign> gassigns) {
        this.gassigns = gassigns;
    }

    public List<ActYwGform> getGforms() {
        if(this.gforms == null){
            this.gforms = Lists.newArrayList();
        }
        return gforms;
    }

    public void setGforms(List<ActYwGform> gforms) {
        this.gforms = gforms;
    }

    public List<ActYwGstatus> getGstatuss() {
        if(this.gstatuss == null){
            this.gstatuss = Lists.newArrayList();
        }
        return gstatuss;
    }

    public void setGstatuss(List<ActYwGstatus> gstatuss) {
        this.gstatuss = gstatuss;
    }

    public List<ActYwGtime> getGtimes() {
        if(this.gtimes == null){
            this.gtimes = Lists.newArrayList();
        }
        return gtimes;
    }

    public void setGtimes(List<ActYwGtime> gtimes) {
        this.gtimes = gtimes;
    }

    public List<ActYwGrole> getGroles() {
        if(this.groles == null){
            this.groles = Lists.newArrayList();
        }
        return groles;
    }

    public void setGroles(List<ActYwGrole> groles) {
        this.groles = groles;
    }

    public List<ActYwGtheme> getGthemes() {
        if(this.gthemes == null){
            this.gthemes = Lists.newArrayList();
        }
        return gthemes;
    }

    public void setGthemes(List<ActYwGtheme> gthemes) {
        this.gthemes = gthemes;
    }
}
