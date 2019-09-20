package com.oseasy.sys.modules.team.entity;

import org.apache.commons.lang3.StringUtils;

import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.sys.common.config.SysSval;

public class TeamDetails extends Team{
	/**
	 *
	 */
	private static final long serialVersionUID = 7381729122327433654L;
	//private String uname; //姓名
	private String degree;//学位
	private String professional;//专业
	private String domain;//技术领域
	private String teacherType;//导师来源
	private String officeId;//机构(学院)
	private String userType;//成员类型
	private String no;		// 工号 学号
	private String instudy;//在读学位
	private String state;//团队状态
	private String phone;//电话
	private String photo;//头像
	private String userId;
	private String teamId;
	private String domainlt;//擅长/技术领域,显示列表时用
	private String currState;
	private String mobile;
	private String oaNotifyId; //通知ID
	private String turId;
	private String curJoin;//当前在研
	private String leaderid;//负责人id
	private String avatar; //图像

	public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getLeaderid() {
		return leaderid;
	}

	public void setLeaderid(String leaderid) {
		this.leaderid = leaderid;
	}

	public String getCurJoin() {
		return curJoin;
	}

	public void setCurJoin(String curJoin) {
		this.curJoin = curJoin;
	}

	public String getTurId() {
		return turId;
	}

	public void setTurId(String turId) {
		this.turId = turId;
	}

	public String getOaNotifyId() {
		return oaNotifyId;
	}

	public void setOaNotifyId(String oaNotifyId) {
		this.oaNotifyId = oaNotifyId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCurrState() {
		return currState;
	}

	public void setCurrState(String currState) {
		this.currState = currState;
	}

	public String getDomainlt() {
		if (StringUtils.isNotBlank(domain)) {
			String domainNames = DictUtils.getDictLabels(domain, SysSval.DICT_TECHNOLOGY_FIELD, "未知");
			return domainNames;
		}
		return domainlt;
	}

	public void setDomainlt(String domainlt) {
		this.domainlt = domainlt;
	}

	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getInstudy() {
		return instudy;
	}
	public void setInstudy(String instudy) {
		this.instudy = instudy;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getProfessional() {
		return professional;
	}
	public void setProfessional(String professional) {
		this.professional = professional;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getTeacherType() {
		return teacherType;
	}
	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

}
