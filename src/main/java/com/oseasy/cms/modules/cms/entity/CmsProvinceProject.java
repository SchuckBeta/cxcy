package com.oseasy.cms.modules.cms.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * Created by PW on 2019/6/19.
 */
public class CmsProvinceProject extends DataEntity<CmsProvinceProject> {

    private static final long serialVersionUID = 1L;
    private String name;//项目/大赛名称
    private String subTime;//提交时间
    private String logo;//图片Logo
    private String schoolName;//高校名称
    private String resource;//项目来源
    private String technologyField;//所属领域
    private String teamId; //团队id
    private String teamSummary; //团队介绍
    private String projectIntrodution; //项目介绍
    private String step;
    private String queryKeywords; //查询关键字
    private String isInIndex; //是否推荐首页

    public String getIsInIndex() {
        return isInIndex;
    }

    public void setIsInIndex(String isInIndex) {
        this.isInIndex = isInIndex;
    }

    public String getQueryKeywords() {
        return queryKeywords;
    }

    public void setQueryKeywords(String queryKeywords) {
        this.queryKeywords = queryKeywords;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamSummary() {
        return teamSummary;
    }

    public void setTeamSummary(String teamSummary) {
        this.teamSummary = teamSummary;
    }

    public String getProjectIntrodution() {
        return projectIntrodution;
    }

    public void setProjectIntrodution(String projectIntrodution) {
        this.projectIntrodution = projectIntrodution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getTechnologyField() {
        return technologyField;
    }

    public void setTechnologyField(String technologyField) {
        this.technologyField = technologyField;
    }
}
