package com.oseasy.sys.modules.team.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 团队管理Entity
 * @author 刘波
 * @version 2017-03-30
 */
/*@JsonInclude(Include.NON_NULL) */
public class Team extends DataEntity<Team> {
	public static final String state0="0";//建设中
	public static final String state1="1";//建设完毕
	public static final String state2="2";//解散
	public static final String state3="3";//待审核
	public static final String state4="4";//审核不通过
	public static final String FIRST_TEACHER="firstTeacher";//第一导师

	private static final long serialVersionUID = 1L;
	private String name;		// 团队名称
	private String sponsor;		// 团队负责人
	private String firstTeacher;		// 第一导师
	private String state;		// 团队状态(0.建设中,1建设完毕，2解散，3.待审核，4不通过）
	private String summary;		// 团队信息简介
	private String projectName;		// 项目名称
	private String projectIntroduction;		// 项目简介
	private Integer enterpriseTeacherNum;		// 企业导师人数
	private Integer schoolTeacherNum;		// 校园导师人数
	private Integer memberNum;		// 成员人数
	private Date validDate;		// 有效期
	private String membership;		// 成员要求
	private Date validDateStart;		// 有效期开始时间
	private Date validDateEnd;		// 有效期结束时间
	private String localCollege;		// 所属学院
	private String memberNames;		// 组员
	private String schTeacherNames;		// 校园导师
	private String entTeacherNames;		// 企业导师
	private Date beginValidDate;		// 开始 有效期
	private Date endValidDate;		// 结束 有效期
    private Integer userCount;
	private Integer enterpriseNum;
	private Integer schoolNum;
	private String uName;//用户姓名
	private String teamUserType;//团队用户类型
	private String teacherType;//导师来源
    private String userName;
    private String entName;
    private String schName;
    private String startTime;
    private String endTime;
    private boolean isSelf; //是否对自己发布的团队
    private User user;
    private String oaNotifyType;

    private String sponsorId;//团队负责人id，用于页面传递负责人id

    private String inResearch; //是否在研

    private String creator;//创建人，只用于查询
    private String nameSch;//团队名称，只用于查询
//    private String stateSch;//团队状态，只用于查询
    private String number;//团队编号
    private boolean checkJoin;//是否已加入，控制申请加入按钮
    private boolean checkJoinTUR;//是否已在团队关系表包括申请和邀请，控制申请加入按钮

	private boolean checkIsHus=true;//是否已开始做项目

	private List<String> stateSch;//团队状态，只用于查询

	private List<String> teamIds;

	public boolean isCheckJoinTUR() {
		return checkJoinTUR;
	}

	public void setCheckJoinTUR(boolean checkJoinTUR) {
		this.checkJoinTUR = checkJoinTUR;
	}

	public String getNameSch() {
		return nameSch;
	}

	public void setNameSch(String nameSch) {
		this.nameSch = nameSch;
	}

//	public String getStateSch() {
//		return stateSch;
//	}
//
//	public void setStateSch(String stateSch) {
//		this.stateSch = stateSch;
//	}


	public String getFirstTeacher() {
		return firstTeacher;
	}

	public void setFirstTeacher(String firstTeacher) {
		this.firstTeacher = firstTeacher;
	}

	public boolean isCheckIsHus() {
		return checkIsHus;
	}

	public void setCheckIsHus(boolean checkIsHus) {
		this.checkIsHus = checkIsHus;
	}

	public boolean isCheckJoin() {
		return checkJoin;
	}

	public void setCheckJoin(boolean checkJoin) {
		this.checkJoin = checkJoin;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getInResearch() {
		return inResearch;
	}

	public void setInResearch(String inResearch) {
		this.inResearch = inResearch;
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public User getUser() {
		return user;
	}

	public String getOaNotifyType() {
		return oaNotifyType;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setOaNotifyType(String oaNotifyType) {
		this.oaNotifyType = oaNotifyType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}

	public String getSchName() {
		return schName;
	}

	public void setSchName(String schName) {
		this.schName = schName;
	}

	public String getTeamUserType() {
		return teamUserType;
	}

	public void setTeamUserType(String teamUserType) {
		this.teamUserType = teamUserType;
	}

	public String getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public Integer getEnterpriseNum() {
		return enterpriseNum==null?0:enterpriseNum;
	}

	public void setEnterpriseNum(Integer enterpriseNum) {
		this.enterpriseNum = enterpriseNum;
	}

	public Integer getSchoolNum() {
		return schoolNum==null?0:schoolNum;
	}

	public void setSchoolNum(Integer schoolNum) {
		this.schoolNum = schoolNum;
	}

	public Integer getUserCount() {
		return userCount==null?0:userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public Team() {
		super();
	}

	public Team(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="团队名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=64, message="团队负责人长度必须介于 0 和 64 之间")
	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	@Length(min=0, max=1, message="团队状态建设中,1建设完毕，2解散）长度必须介于 0 和 1 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Length(min=0, max=64, message="团队信息简介长度必须介于 0 和 64 之间")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Length(min=0, max=200, message="项目名称长度必须介于 0 和 200 之间")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Length(min=0, max=1024, message="项目简介长度必须介于 0 和 1024 之间")
	public String getProjectIntroduction() {
		return projectIntroduction;
	}

	public void setProjectIntroduction(String projectIntroduction) {
		this.projectIntroduction = projectIntroduction;
	}

	public Integer getEnterpriseTeacherNum() {
		return enterpriseTeacherNum==null?0:enterpriseTeacherNum;
	}

	public void setEnterpriseTeacherNum(Integer enterpriseTeacherNum) {
		this.enterpriseTeacherNum = enterpriseTeacherNum;
	}

	public Integer getSchoolTeacherNum() {
		return schoolTeacherNum==null?0:schoolTeacherNum;
	}

	public void setSchoolTeacherNum(Integer schoolTeacherNum) {
		this.schoolTeacherNum = schoolTeacherNum;
	}

	public Integer getMemberNum() {
		return memberNum==null?0:memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	@Length(min=0, max=64, message="成员要求长度必须介于 0 和 64 之间")
	public String getMembership() {
		return membership;
	}

	public void setMembership(String membership) {
		this.membership = membership;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidDateStart() {
		return validDateStart;
	}

	public void setValidDateStart(Date validDateStart) {
		this.validDateStart = validDateStart;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getValidDateEnd() {
		return validDateEnd;
	}

	public void setValidDateEnd(Date validDateEnd) {
		this.validDateEnd = validDateEnd;
	}

	@Length(min=0, max=64, message="所属学院长度必须介于 0 和 64 之间")
	public String getLocalCollege() {
		return localCollege;
	}

	public void setLocalCollege(String localCollege) {
		this.localCollege = localCollege;
	}

	@Length(min=0, max=1000, message="组员长度必须介于 0 和 1000 之间")
	public String getMemberNames() {
		return memberNames;
	}

	public void setMemberNames(String memberNames) {
		this.memberNames = memberNames;
	}

	@Length(min=0, max=1000, message="校园导师长度必须介于 0 和 1000 之间")
	public String getSchTeacherNames() {
		return schTeacherNames;
	}

	public void setSchTeacherNames(String schTeacherNames) {
		this.schTeacherNames = schTeacherNames;
	}

	@Length(min=0, max=1000, message="企业导师长度必须介于 0 和 1000 之间")
	public String getEntTeacherNames() {
		return entTeacherNames;
	}

	public void setEntTeacherNames(String entTeacherNames) {
		this.entTeacherNames = entTeacherNames;
	}

	public Date getBeginValidDate() {
		return beginValidDate;
	}

	public void setBeginValidDate(Date beginValidDate) {
		this.beginValidDate = beginValidDate;
	}

	public Date getEndValidDate() {
		return endValidDate;
	}

	public void setEndValidDate(Date endValidDate) {
		this.endValidDate = endValidDate;
	}

	public boolean getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public List<String> getStateSch() {
		return stateSch;
	}

	public void setStateSch(List<String> stateSch) {
		this.stateSch = stateSch;
	}

	public List<String> getTeamIds() {
		return teamIds;
	}

	public void setTeamIds(List<String> teamIds) {
		this.teamIds = teamIds;
	}
}