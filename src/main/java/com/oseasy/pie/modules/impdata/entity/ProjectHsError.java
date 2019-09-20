package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 导入项目错误数据表（华师）Entity.
 * @author 9527
 * @version 2017-10-10
 */
public class ProjectHsError extends DataEntity<ProjectHsError> {

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String office;		// 学院
	private String name;		// 项目名称
	private String number;		// 项目编号
	private String type;		// 项目类型
	private String leader;		// 负责人姓名
	private String no;		// 负责人学号
	private String mobile;		// 负责人电话
	private String email;		// 负责人邮箱
	private String profes;		// 负责人专业
	private String grade;		// 负责人年级
	private String members;		// 团队成员及学号
	private String memNums;		// 团队总人数
	private String teachers;		// 指导教师
	private String teaNo;		// 指导教师工号
	private String teaTitle;		// 指导教师职称
	private String hasOut;		// 有否校外指导教师
	private String teaNums;		// 指导教师总人数
	private String level;		// 项目级别
	private String total;		// 经费总额
	private String rem;		// 酬金金额
	private String year;    //项目年份

	public ProjectHsError() {
		super();
	}

	public ProjectHsError(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=128, message="学院长度必须介于 0 和 128 之间")
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	@Length(min=0, max=128, message="项目名称长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=128, message="项目编号长度必须介于 0 和 128 之间")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Length(min=0, max=128, message="项目类型长度必须介于 0 和 128 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=128, message="负责人姓名长度必须介于 0 和 128 之间")
	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Length(min=0, max=128, message="负责人学号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=128, message="负责人电话长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min=0, max=128, message="负责人邮箱长度必须介于 0 和 128 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=128, message="负责人专业长度必须介于 0 和 128 之间")
	public String getProfes() {
		return profes;
	}

	public void setProfes(String profes) {
		this.profes = profes;
	}

	@Length(min=0, max=12, message="负责人年级长度必须介于 0 和 12 之间")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min=0, max=256, message="团队成员及学号长度必须介于 0 和 256 之间")
	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	@Length(min=0, max=3, message="团队总人数长度必须介于 0 和 3 之间")
	public String getMemNums() {
		return memNums;
	}

	public void setMemNums(String memNums) {
		this.memNums = memNums;
	}

	@Length(min=0, max=128, message="指导教师长度必须介于 0 和 128 之间")
	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

	@Length(min=0, max=128, message="指导教师工号长度必须介于 0 和 128 之间")
	public String getTeaNo() {
		return teaNo;
	}

	public void setTeaNo(String teaNo) {
		this.teaNo = teaNo;
	}

	@Length(min=0, max=128, message="指导教师职称长度必须介于 0 和 128 之间")
	public String getTeaTitle() {
		return teaTitle;
	}

	public void setTeaTitle(String teaTitle) {
		this.teaTitle = teaTitle;
	}

	@Length(min=0, max=10, message="有否校外指导教师长度必须介于 0 和 10 之间")
	public String getHasOut() {
		return hasOut;
	}

	public void setHasOut(String hasOut) {
		this.hasOut = hasOut;
	}

	@Length(min=0, max=3, message="指导教师总人数长度必须介于 0 和 3 之间")
	public String getTeaNums() {
		return teaNums;
	}

	public void setTeaNums(String teaNums) {
		this.teaNums = teaNums;
	}

	@Length(min=0, max=100, message="项目级别长度必须介于 0 和 100 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Length(min=0, max=15, message="经费总额长度必须介于 0 和 15 之间")
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Length(min=0, max=15, message="酬金金额长度必须介于 0 和 15 之间")
	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	@Length(min=0, max=15, message="项目年份长度必须介于 0 和 4 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}