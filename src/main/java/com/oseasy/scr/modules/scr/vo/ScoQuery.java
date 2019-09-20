package com.oseasy.scr.modules.scr.vo;


import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.scr.modules.scr.entity.ScoRule;

import java.util.Date;
import java.util.List;

/**
 * 学分查询实体
 * Created by PW on 2019/1/9.
 */
public class ScoQuery extends DataExtEntity<ScoQuery> {
    private static final long serialVersionUID = 1L;

    private String creditType;  //学分类型：1-创新创业学分，2-课程学分
    private String credit;   //认定学分
    private String name;     //学分名称
    private Date applyDate;  //申请日期
    private Date auditDate;  //审核日期
    private String status;   //审核状态
    private String creditName; //学分类别名称
    private String className; //学分类型名称
    private String typeName;  //标准的类别名称
    private String projectName; //项目名称
    private String rid;  //
    private List<ScoRule> scoRuleList;  //
    private String userId;
    private User user;

    private Date auditStartDate;  //认定时间起始日期
    private Date auditEndDate;  //认定时间结束日期

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getAuditStartDate() {
        return auditStartDate;
    }

    public void setAuditStartDate(Date auditStartDate) {
        this.auditStartDate = auditStartDate;
    }

    public Date getAuditEndDate() {
        return auditEndDate;
    }

    public void setAuditEndDate(Date auditEndDate) {
        this.auditEndDate = auditEndDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public List<ScoRule> getScoRuleList() {
        return scoRuleList;
    }

    public void setScoRuleList(List<ScoRule> scoRuleList) {
        this.scoRuleList = scoRuleList;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
