package com.oseasy.sys.modules.sys.entity;
import java.util.Date;

public class UserInfo {
	private String id;
	private String name;//姓名
	private String email;//邮箱
	private String phone;//电话
	private String mobile;//手机
	private String photo;//用户头像
	private String national;//民族
	private String political;//政治面貌
	private String professional;//专业
	private Date birthday;//出生日期
	private int sex;//性别
	private String area;//地区
	private String degree;//学位
	private String no;//学号
	private String education;//学历
	private String idType;//证件类型
	private Date temporaryDate;//休学时间
	private Date graduation;//毕业时间
	private String tClass;//班级
	private String userId; //用户id
	private String domain;//擅长领域
	private String sponsor;//担任角色
	private String pname;//项目名称
	private String finalResult;//项目结果
	private String level;//项目评级
	private Date beginDate;//项目开始时间
	private Date finalEndDate;//结束时间
	private String contestName;//大赛项目名称
	private String rewardSituation;//奖励情况
	private String type;//项目类型
	private String idNo;//证件号
	private String localCollege;//所属院系
	private String officeId;//机构(学院)
	private String userType;// 用户类型    1学生 2导师 3 教学秘书 4院级专家 5校级专家 6校级管理员
	
	
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getNational() {
		return national;
	}
	public void setNational(String national) {
		this.national = national;
	}
	public String getPolitical() {
		return political;
	}
	public void setPolitical(String political) {
		this.political = political;
	}
	public String getProfessional() {
		return professional;
	}
	public void setProfessional(String professional) {
		this.professional = professional;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}

	public Date getGraduation() {
		return graduation;
	}
	public void setGraduation(Date graduation) {
		this.graduation = graduation;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public Date getTemporaryDate() {
		return temporaryDate;
	}
	public void setTemporaryDate(Date temporaryDate) {
		this.temporaryDate = temporaryDate;
	}
	public String gettClass() {
		return tClass;
	}
	public void settClass(String tClass) {
		this.tClass = tClass;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getFinalResult() {
		return finalResult;
	}
	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getFinalEndDate() {
		return finalEndDate;
	}
	public void setFinalEndDate(Date finalEndDate) {
		this.finalEndDate = finalEndDate;
	}
	public String getContestName() {
		return contestName;
	}
	public void setContestName(String contestName) {
		this.contestName = contestName;
	}
	public String getRewardSituation() {
		return rewardSituation;
	}
	public void setRewardSituation(String rewardSituation) {
		this.rewardSituation = rewardSituation;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getLocalCollege() {
		return localCollege;
	}
	public void setLocalCollege(String localCollege) {
		this.localCollege = localCollege;
	}
	
	
}
