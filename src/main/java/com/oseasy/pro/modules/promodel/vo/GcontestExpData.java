package com.oseasy.pro.modules.promodel.vo;

public class GcontestExpData {
	private String promodelid;
	private Integer indxnum;//序号
	private String name;//项目名称
	private String type;//项目类别
	private String groupName;//项目组别
	private String leaderName;//负责人姓名
	private String leaderEnter;//负责人入学年份
	private String leaderPro;//负责人专业
	private String leaderIdNo;//负责人身份证
	private String leaderNo;//负责人学号
	private String leaderMobile;//负责人电话
	private Integer teamNum;//参与学生人数，包含负责人
	private String office;//项目所属学院
	private String summary;//项目简介
	
	public String getPromodelid() {
		return promodelid;
	}
	public void setPromodelid(String promodelid) {
		this.promodelid = promodelid;
	}
	public Integer getIndxnum() {
		return indxnum;
	}
	public void setIndxnum(Integer indxnum) {
		this.indxnum = indxnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public String getLeaderEnter() {
		return leaderEnter;
	}
	public void setLeaderEnter(String leaderEnter) {
		this.leaderEnter = leaderEnter;
	}
	public String getLeaderPro() {
		return leaderPro;
	}
	public void setLeaderPro(String leaderPro) {
		this.leaderPro = leaderPro;
	}
	public String getLeaderIdNo() {
		return leaderIdNo;
	}
	public void setLeaderIdNo(String leaderIdNo) {
		this.leaderIdNo = leaderIdNo;
	}
	public String getLeaderNo() {
		return leaderNo;
	}
	public void setLeaderNo(String leaderNo) {
		this.leaderNo = leaderNo;
	}
	public String getLeaderMobile() {
		return leaderMobile;
	}
	public void setLeaderMobile(String leaderMobile) {
		this.leaderMobile = leaderMobile;
	}
	public Integer getTeamNum() {
		return teamNum;
	}
	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
