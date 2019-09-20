package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;

/**
 * 导入项目错误数据表Entity
 * @author 9527
 * @version 2017-05-26
 */
public class ProjectError extends DataEntity<ProjectError> implements IitCheckEetyExt{

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String approvingYear;		// 立项年份
	private String number;		// 项目编号
	private String name;		// 项目名称
	private String type;		// 项目类型
	private String leader;		// 项目负责人
	private String leaderNo;		// 项目负责人学号
	private String teamStuNumber;		// 参与学生人数
	private String teamStuInfo;		// 项目其他成员信息
	private String teacher;		// 指导教师姓名
	private String financeGrant;		// 财政拨款（元）
	private String universityGrant;		// 校拨（元）
	private String totalGrant;		// 总经费（元）
	private String introduction;		// 项目简介
	private String province;//省（区、市）
	private String universityCode;//高校代码
	private String universityName;//高校名称
	public ProjectError() {
		super();
	}

	public ProjectError(String id) {
		super(id);
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getUniversityCode() {
		return universityCode;
	}

	public void setUniversityCode(String universityCode) {
		this.universityCode = universityCode;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=4, message="立项年份长度必须介于 0 和 4 之间")
	public String getApprovingYear() {
		return approvingYear;
	}

	public void setApprovingYear(String approvingYear) {
		this.approvingYear = approvingYear;
	}

	@Length(min=0, max=64, message="项目编号长度必须介于 0 和 64 之间")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Length(min=0, max=128, message="项目名称长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=64, message="项目类型长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="项目负责人长度必须介于 0 和 64 之间")
	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Length(min=0, max=64, message="项目负责人学号长度必须介于 0 和 64 之间")
	public String getLeaderNo() {
		return leaderNo;
	}

	public void setLeaderNo(String leaderNo) {
		this.leaderNo = leaderNo;
	}

	@Length(min=0, max=64, message="参与学生人数长度必须介于 0 和 64 之间")
	public String getTeamStuNumber() {
		return teamStuNumber;
	}

	public void setTeamStuNumber(String teamStuNumber) {
		this.teamStuNumber = teamStuNumber;
	}

	public String getTeamStuInfo() {
		return teamStuInfo;
	}

	public void setTeamStuInfo(String teamStuInfo) {
		this.teamStuInfo = teamStuInfo;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	@Length(min=0, max=20, message="财政拨款（元）长度必须介于 0 和 20 之间")
	public String getFinanceGrant() {
		return financeGrant;
	}

	public void setFinanceGrant(String financeGrant) {
		this.financeGrant = financeGrant;
	}

	@Length(min=0, max=20, message="校拨（元）长度必须介于 0 和 20 之间")
	public String getUniversityGrant() {
		return universityGrant;
	}

	public void setUniversityGrant(String universityGrant) {
		this.universityGrant = universityGrant;
	}

	@Length(min=0, max=20, message="总经费（元）长度必须介于 0 和 20 之间")
	public String getTotalGrant() {
		return totalGrant;
	}

	public void setTotalGrant(String totalGrant) {
		this.totalGrant = totalGrant;
	}

	@Length(min=0, max=1024, message="项目简介长度必须介于 0 和 1024 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}