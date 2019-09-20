package com.oseasy.sys.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 导师信息表Entity
 * @author zj
 * @version 2017-03-25
 */
public class SysTeacherExpansion extends User {

	private static final long serialVersionUID = 1L;
	//	private User user;		// 人员基本信息ID
	private String arrangement;		// 层次
	private Integer discipline;		// 学科门类
	private String industry;		// 行业
	private String technicalTitle;		// 职称
	private String serviceIntention;		// 服务意向
	private String workUnit;		// 工作单位
	private String address;		// 联系地址
	private String resume;		// 工作简历
	private String recommendedUnits;		// 推荐单位
	private String result;		// 成果名称
	private String award;		// 获奖名称
	private Integer level;		// 级别
	private String reviewName;		// 评审项目名称
	private String joinReviewTime;		// 参与评审年份
	private String firstBank;		// 开户银行
	private Integer bankAccount;		// 银行账号
	private String teachertype;		// 导师来源

	public SysTeacherExpansion() {
		super();
	}

	public SysTeacherExpansion(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="层次长度必须介于 0 和 64 之间")
	public String getArrangement() {
		return arrangement;
	}

	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}

	public Integer getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Integer discipline) {
		this.discipline = discipline;
	}

	@Length(min=0, max=32, message="行业长度必须介于 0 和 32 之间")
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Length(min=0, max=20, message="职称长度必须介于 0 和 20 之间")
	public String getTechnicalTitle() {
		return technicalTitle;
	}

	public void setTechnicalTitle(String technicalTitle) {
		this.technicalTitle = technicalTitle;
	}

	@Length(min=0, max=16, message="服务意向长度必须介于 0 和 16 之间")
	public String getServiceIntention() {
		return serviceIntention;
	}

	public void setServiceIntention(String serviceIntention) {
		this.serviceIntention = serviceIntention;
	}

	@Length(min=0, max=128, message="工作单位长度必须介于 0 和 128 之间")
	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	@Length(min=0, max=128, message="联系地址长度必须介于 0 和 128 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=512, message="工作简历长度必须介于 0 和 512 之间")
	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	@Length(min=0, max=128, message="推荐单位长度必须介于 0 和 128 之间")
	public String getRecommendedUnits() {
		return recommendedUnits;
	}

	public void setRecommendedUnits(String recommendedUnits) {
		this.recommendedUnits = recommendedUnits;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Length(min=0, max=128, message="评审项目名称长度必须介于 0 和 128 之间")
	public String getReviewName() {
		return reviewName;
	}

	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	@Length(min=0, max=128, message="参与评审年份长度必须介于 0 和 128 之间")
	public String getJoinReviewTime() {
		return joinReviewTime;
	}

	public void setJoinReviewTime(String joinReviewTime) {
		this.joinReviewTime = joinReviewTime;
	}

	@Length(min=0, max=128, message="开户银行长度必须介于 0 和 128 之间")
	public String getFirstBank() {
		return firstBank;
	}

	public void setFirstBank(String firstBank) {
		this.firstBank = firstBank;
	}

	public Integer getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Integer bankAccount) {
		this.bankAccount = bankAccount;
	}
}