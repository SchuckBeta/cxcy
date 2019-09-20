package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 后台用户导入Entity
 * @author 9527
 * @version 2017-05-23
 */
public class BackUserError extends DataEntity<BackUserError> {

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
	private String roles;		// 角色

	public BackUserError() {
		super();
	}

	public BackUserError(String id) {
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

	@Length(min=0, max=1024, message="角色长度必须介于 0 和 1024 之间")
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

}