package com.oseasy.scr.modules.sco.vo;

import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.scr.modules.sco.entity.ScoAffirm;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;

/**
 * Created by zhangzheng on 2017/7/21.
 * 根据 type（学分类型)、item（学分项）、
 *      category（课程、项目、大赛、技能大类）、
 *      subdivision（课程、项目、大赛小类）、
 *      number(人数)获得学分配比
 *      的查询实体
 */
public class ScoTeamRatioVo {

    private String type;		// 学分类型
    private String item;		// 学分项
    private String category;		// 课程、项目、大赛、技能大类
    private String subdivision;		// 课程、项目、大赛小类
    private TeamUserRelation teamUserRelation;		// 团队关系
    private ScoAffirm scoAffirm ;  //评分信息（认定时间、认定学分）
    private String ratio;		// 分配比例
    private User user;		// 用户
//    private ProjectDeclare projectDeclare;  //项目信息
//    private GContest GContest;   //大赛信息

    public ScoAffirm getScoAffirm() {
        return scoAffirm;
    }
    public void setScoAffirm(ScoAffirm scoAffirm) {
        this.scoAffirm = scoAffirm;
    }

    public TeamUserRelation getTeamUserRelation() {
        return teamUserRelation;
    }

    public void setTeamUserRelation(TeamUserRelation teamUserRelation) {
        this.teamUserRelation = teamUserRelation;
    }

//    public ProjectDeclare getProjectDeclare() {
//        return projectDeclare;
//    }
//
//    public void setProjectDeclare(ProjectDeclare projectDeclare) {
//        this.projectDeclare = projectDeclare;
//    }
//
//    public GContest getGContest() {
//        return GContest;
//    }
//
//    public void setGContest(com.oseasy.pro.modules.gcontest.entity.GContest GContest) {
//        this.GContest = GContest;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }


    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
