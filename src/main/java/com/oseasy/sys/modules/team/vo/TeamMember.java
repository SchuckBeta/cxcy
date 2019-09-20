package com.oseasy.sys.modules.team.vo;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.sys.modules.team.entity.TeamDetails;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by wqt on 2018/5/31.
 */
public class TeamMember implements Serializable {
    protected static final Logger logger = Logger.getLogger(TeamMember.class);

    private String id;  //用户id
    private String name; //用户姓名
    private String userType; //用户类型
    private String no; //学号
    private String curJoin; // 当前在研
    private String curState; //当前状态
    private String state;
    private String officeName; //学院
    private String instudy;//在读学位
    private String professional;
    private String mobile;
    private String phone;//电话
    private String avatar; //图像
    private String sponsorId;
    private String userId;
    private String turId;


    private String domainlt;


    public static List<TeamMember> gen(List<TeamDetails> teamDetails) {
        List<TeamMember> tmembes = Lists.newArrayList();
        for (TeamDetails tdetails : teamDetails) {
            TeamMember teamMember = new TeamMember();
            teamMember.setId(tdetails.getUserId());
            teamMember.setNo(tdetails.getNo());
            teamMember.setMobile(tdetails.getMobile());
            teamMember.setPhone(tdetails.getPhone());
            teamMember.setName(tdetails.getuName());
            teamMember.setUserType(tdetails.getUserType());
            teamMember.setCurJoin(tdetails.getCurJoin());
            teamMember.setState(tdetails.getState());
            teamMember.setCurState(tdetails.getCurrState());
            teamMember.setOfficeName(tdetails.getOfficeId());
            teamMember.setProfessional(tdetails.getProfessional());
            teamMember.setDomainlt(tdetails.getDomainlt());
            teamMember.setAvatar(tdetails.getAvatar());
            teamMember.setUserId(tdetails.getUserId());
            teamMember.setSponsorId(tdetails.getSponsor());
            teamMember.setTurId(tdetails.getTurId());
            tmembes.add(teamMember);
        }
        return tmembes;
    }

    public String getTurId() {
        return turId;
    }

    public void setTurId(String turId) {
        this.turId = turId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCurJoin() {
        return curJoin;
    }

    public void setCurJoin(String curJoin) {
        this.curJoin = curJoin;
    }

    public String getCurState() {
        return curState;
    }

    public void setCurState(String curState) {
        if (StringUtil.isNotEmpty(curState)) {
            curState = DictUtils.getDictLabel(curState, "current_sate", "");
        }
        this.curState = curState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getInstudy() {
        return instudy;
    }

    public void setInstudy(String instudy) {
        this.instudy = instudy;
    }


    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        Office office = CoreUtils.getOffice(professional);
        this.professional = (office != null) ? office.getName() : "";
    }

    public String getDomainlt() {
        return domainlt;
    }

    public void setDomainlt(String domainlt) {
        this.domainlt = domainlt;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
