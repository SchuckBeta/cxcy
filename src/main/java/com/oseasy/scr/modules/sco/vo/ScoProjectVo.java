package com.oseasy.scr.modules.sco.vo;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.scr.modules.sco.entity.ScoAffirm;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangzheng on 2017/7/19.
 * 创新学分认定、创业学分认定、素质学分认定 查询实体
 */
public class ScoProjectVo extends DataEntity<ScoProjectVo> {


//    private ProjectDeclare projectDeclare;  //项目信息
//    private GContest GContest;   //大赛信息
    private ScoAffirm scoAffirm ;  //评分信息（认定时间、认定学分）
    private float standard ;  //学分认定标准 根据项目项目级别和项目结果查询sco_affirm_criterion表得到
    private String scoreType;  //学分类型（创新学分或者创业学分） 根据项目类型得到
    private String teamUsers ;  //组成员 查询team_user_relation表得到
    private int weightVal;  //学分权重 根据teamId，userId 查询team_user_relation获得
    private int weightTotal; //总权重 根据teamId查询team_user_relation所有权重相加获得
    private float percent;  // weightVal/weightTotal
    private float score;  //认定学分 学分认定标准*percent
    private String pType;  //竞赛类型或者项目类型
    private String comType;  //竞赛级别
    private String userNum;  //团队人数
    private String keyWord;  //关键字

    private String ratioResult;  //配比

    private User user;  //查询条件 用户
    private Office office;  //查询条件 学院
    private String userId;  //查询条件 userId
    private Date beginDate;  //开始时间
    private Date endDate;  //结束时间

    public String getRatioResult() {
        return ratioResult;
    }

    public void setRatioResult(String ratioResult) {
        this.ratioResult = ratioResult;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
//    public void setGContest(GContest GContest) {
//        this.GContest = GContest;
//    }

    public ScoAffirm getScoAffirm() {
        return scoAffirm;
    }

    public void setScoAffirm(ScoAffirm scoAffirm) {
        this.scoAffirm = scoAffirm;
    }

    public float getStandard() {
        return standard;
    }

    public void setStandard(float standard) {
        this.standard = standard;
    }

    public float getPercent() {
        if (weightTotal>0) {
            return (float)weightVal/weightTotal;
        }else{
            return 1;
        }
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public String getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(String teamUsers) {
        this.teamUsers = teamUsers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWeightVal() {
        return weightVal;
    }

    public void setWeightVal(int weightVal) {
        this.weightVal = weightVal;
    }

    public int getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(int weightTotal) {
        this.weightTotal = weightTotal;
    }

    public float getScore() {
        if (scoAffirm!=null&&scoAffirm.getScoreStandard()>0) {
            float val = scoAffirm.getScoreStandard()*this.getPercent();
            BigDecimal b = new   BigDecimal(val);
            float   f1 = b.setScale(1,   BigDecimal.ROUND_HALF_UP).floatValue();
            if (f1>0&f1<0.5) {
                f1 = 0.5f;
            }
            return f1;
        }else{
            return 0;
        }
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getComType() {
        return comType;
    }

    public void setComType(String comType) {
        this.comType = comType;
    }
}
