package com.oseasy.sys.modules.sys.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.modules.sys.entity.User;

import org.hibernate.validator.constraints.Length;

/**
 * 学生信息表Entity
 * @author zy
 * @version 2017-03-14
 */
public class SysStudentExpansion extends User {
	
	private static final long serialVersionUID = 1L;
//	private User user;		// 人员基本信息ID
	private String projectExperience;		// 项目经历
	private String contestExperience;		// 大赛经历
	private String award;		// 获奖作品
	private String isOpen;		// 是否公开个人信息
	private Date graduation;		// 毕业时间
	private Date enterDate;  //入学时间
	private String instudy;//在读学位

	private String officeName; //学院 展示用 addBy张正

	private String  graduated; //是否毕业  1是 0否  默认否

	private String currState; // 现状  1在校 2毕业 3休学

	public String getInstudy() {
		return instudy;
	}

	public void setInstudy(String instudy) {
		this.instudy = instudy;
	}

	@JsonFormat(pattern = "yyyy")
	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	public SysStudentExpansion() {
		super();
	}

	public SysStudentExpansion(String id) {
		super(id);
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}


	public String getProjectExperience() {
		return projectExperience;
	}

	public void setProjectExperience(String projectExperience) {
		this.projectExperience = projectExperience;
	}
	
	public String getContestExperience() {
		return contestExperience;
	}

	public void setContestExperience(String contestExperience) {
		this.contestExperience = contestExperience;
	}
	
	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}
	
	@Length(min=0, max=1, message="是否公开个人信息长度必须介于 0 和 1 之间")
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}


	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public Date getGraduation() {
		return graduation;
	}

	public void setGraduation(Date graduation) {
		this.graduation = graduation;
	}

	public String  getGraduated() {
		if (graduation != null) {
			Date date = new Date();
			long time1 = date.getTime();
			long time2 = graduation.getTime();
			if (time1 >time2 ) {
				return "1";
			}else{
				return "0";
			}
		}
		return "0";
	}

	public void setGraduated(String graduated) {
		this.graduated = graduated;
	}

	public String getCurrState() {
		return currState;
	}

	public void setCurrState(String currState) {
		this.currState = currState;
	}
}