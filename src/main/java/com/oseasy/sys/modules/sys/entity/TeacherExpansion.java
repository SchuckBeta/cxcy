/**
 *
 */
package com.oseasy.sys.modules.sys.entity;

import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 用户Entity 导师拓展表
 */
public class TeacherExpansion extends User {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String arrangement;// 层次
	private Integer discipline;// 学科门类
	private Integer industry;// 行业
	private Integer post;// 职务
	private Integer technicalTitle;// 职称
	private String serviceIntention;// 服务意向

	private String workUnit;// 工作单位
	private String address;// 联系地址
	private String resume;// 工作简历
	private String recommendedUnits;// 推荐单位
	private String result;// 成果名称
	private String award;// 获奖名称

	private String level;// 级别
	private String reviewName;// 评审项目名称
	private String joinReviewTime;// 参与评审年份
	private String firstBank;// 开户银行
	private String bankAccount;// 银行账号
	private String teacherNumber;// 教工号
	private String teachertype;		// 导师来源
	private String educationType;//学历类别

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public TeacherExpansion() {
        super();
    }

    public TeacherExpansion(String userId) {
        super();
        this.userId = userId;
    }

    public String getEducationType() {
		return educationType;
	}

	public void setEducationType(String educationType) {
		this.educationType = educationType;
	}

	public String getTeacherNumber() {
		return teacherNumber;
	}

	public void setTeacherNumber(String teacherNumber) {
		this.teacherNumber = teacherNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public Integer getPost() {
		return post;
	}

	public void setPost(Integer post) {
		this.post = post;
	}

	public Integer getTechnicalTitle() {
		return technicalTitle;
	}

	public void setTechnicalTitle(Integer technicalTitle) {
		this.technicalTitle = technicalTitle;
	}

	public String getServiceIntention() {
		return serviceIntention;
	}

	public void setServiceIntention(String serviceIntention) {
		this.serviceIntention = serviceIntention;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getReviewName() {
		return reviewName;
	}

	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	public String getJoinReviewTime() {
		return joinReviewTime;
	}

	public void setJoinReviewTime(String joinReviewTime) {
		this.joinReviewTime = joinReviewTime;
	}

	public String getFirstBank() {
		return firstBank;
	}

	public void setFirstBank(String firstBank) {
		this.firstBank = firstBank;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	public String toString() {
		return id;
	}
}