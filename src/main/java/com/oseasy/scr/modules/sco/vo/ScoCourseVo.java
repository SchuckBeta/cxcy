package com.oseasy.scr.modules.sco.vo;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.scr.modules.sco.entity.ScoAffirm;
import com.oseasy.scr.modules.sco.entity.ScoApply;
import com.oseasy.scr.modules.sco.entity.ScoCourse;

/**
 * Created by zhangzheng on 2017/7/19.
 */
public class ScoCourseVo extends DataEntity<ScoCourseVo> {


    private ScoCourse scoCourse;  //课程信息
    private ScoAffirm scoAffirm ;  //评分信息（认定时间、认定学分）
    private float standard ;  //学分认定标准 根据项目项目级别和项目结果查询sco_affirm_criterion表得到
    private float percent;    //学分占比  根据团队人员关系表查询得到
    private String scoreType;  //学分类型（创新学分或者创业学分） 根据项目类型得到
    private User user;  //查询条件 用户
    private Office office;  //查询条件 学院
    private ScoApply scoApply;  //查询条件 审核
    private String keyWord;  //关键字

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public ScoCourse getScoCourse() {
        return scoCourse;
    }

    public void setScoCourse(ScoCourse scoCourse) {
        this.scoCourse = scoCourse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public ScoApply getScoApply() {
        return scoApply;
    }

    public void setScoApply(ScoApply scoApply) {
        this.scoApply = scoApply;
    }

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
        return percent;
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


}
