package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 导入导师错误数据表Entity
 * @author 9527
 * @version 2017-05-22
 */
public class TeacherError extends DataEntity<TeacherError> {
	
	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String companyId;		// 归属公司
	private String office;		// 归属机构
	private String professional;		// 专业
	private String loginName;		// 登录名
	private String password;		// 密码
	private String no;		// 工号
	private String name;		// 姓名
	private String email;		// 邮箱
	private String phone;		// 电话
	private String mobile;		// 手机
	private String userType;		// 用户类型
	private String loginFlag;		// 是否可登录
	private String idType;		// 证件
	private String national;		// 民族
	private String political;		// 政治面貌
	private String postCode;		// 邮编
	private String birthday;		// 出生年月
	private String sex;		// 1:男0:女
	private String country;		// 国家
	private String area;		// 地区
	private String domain;		// 擅长/技术领域
	private String degree;		// 学位
	private String education;		// 学历
	private String idNo;		// 证件号
	private String arrangement;		// 层次
	private String discipline;		// 学科门类
	private String industry;		// 行业
	private String technicalTitle;		// 职称
	private String serviceIntention;		// 服务意向
	private String workUnit;		// 工作单位
	private String address;		// 联系地址
	private String resume;		// 简历
	private String recommendedUnits;		// 推荐单位
	private String result;		// 成果名称
	private String award;		// 获奖名称
	private String level;		// 级别
	private String reviewName;		// 评审项目名称
	private String joinReviewTime;		// 参与评审年份
	private String firstBank;		// 开户银行
	private String bankAccount;		// 银行账号
	private String isOpen;		// 是否公开
	private String teachertype;		// 导师来源 1：校园导师  2：企业导师
	private String educationType;		// 学历类别
	private String mainexp;		// 主要经历
	private String rowIndex;		// 行号
	private String province;		//省市
	private String school;		//高校
	
	public TeacherError() {
		super();
	}

	public TeacherError(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}
	
	@Length(min=0, max=128, message="归属公司长度必须介于 0 和 128 之间")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}
	
	@Length(min=0, max=128, message="专业长度必须介于 0 和 128 之间")
	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	@Length(min=0, max=128, message="登录名长度必须介于 0 和 128 之间")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Length(min=0, max=128, message="密码长度必须介于 0 和 128 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=0, max=128, message="工号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	@Length(min=0, max=128, message="姓名长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=128, message="邮箱长度必须介于 0 和 128 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=128, message="电话长度必须介于 0 和 128 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=128, message="手机长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=128, message="用户类型长度必须介于 0 和 128 之间")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	@Length(min=0, max=64, message="是否可登录长度必须介于 0 和 64 之间")
	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	
	@Length(min=0, max=128, message="证件长度必须介于 0 和 128 之间")
	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}
	
	@Length(min=0, max=128, message="民族长度必须介于 0 和 128 之间")
	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}
	
	@Length(min=0, max=128, message="政治面貌长度必须介于 0 和 128 之间")
	public String getPolitical() {
		return political;
	}

	public void setPolitical(String political) {
		this.political = political;
	}
	
	@Length(min=0, max=128, message="邮编长度必须介于 0 和 128 之间")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	@Length(min=0, max=128, message="出生年月长度必须介于 0 和 128 之间")
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	@Length(min=0, max=128, message="1:男0:女长度必须介于 0 和 128 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=128, message="国家长度必须介于 0 和 128 之间")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Length(min=0, max=128, message="地区长度必须介于 0 和 128 之间")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	@Length(min=0, max=1024, message="擅长/技术领域长度必须介于 0 和 1024 之间")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Length(min=0, max=128, message="学位长度必须介于 0 和 128 之间")
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	@Length(min=0, max=128, message="学历长度必须介于 0 和 128 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	@Length(min=0, max=128, message="证件号长度必须介于 0 和 128 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Length(min=0, max=128, message="层次长度必须介于 0 和 128 之间")
	public String getArrangement() {
		return arrangement;
	}

	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}
	
	@Length(min=0, max=128, message="学科门类长度必须介于 0 和 128 之间")
	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	
	@Length(min=0, max=128, message="行业长度必须介于 0 和 128 之间")
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
	
	@Length(min=0, max=512, message="服务意向长度必须介于 0 和 512 之间")
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
	
	@Length(min=0, max=64, message="级别长度必须介于 0 和 64 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	@Length(min=0, max=128, message="评审项目名称长度必须介于 0 和 128 之间")
	public String getReviewName() {
		return reviewName;
	}

	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}
	
	@Length(min=0, max=32, message="参与评审年份长度必须介于 0 和 32 之间")
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
	
	@Length(min=0, max=11, message="银行账号长度必须介于 0 和 11 之间")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	@Length(min=0, max=11, message="是否公开长度必须介于 0 和 11 之间")
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	
	@Length(min=0, max=20, message="导师来源 1：校园导师  2：企业导师长度必须介于 0 和 20 之间")
	public String getTeachertype() {
		return teachertype;
	}

	public void setTeachertype(String teachertype) {
		this.teachertype = teachertype;
	}
	
	@Length(min=0, max=11, message="学历类别长度必须介于 0 和 11 之间")
	public String getEducationType() {
		return educationType;
	}

	public void setEducationType(String educationType) {
		this.educationType = educationType;
	}
	
	@Length(min=0, max=512, message="主要经历长度必须介于 0 和 512 之间")
	public String getMainexp() {
		return mainexp;
	}

	public void setMainexp(String mainexp) {
		this.mainexp = mainexp;
	}
	
}