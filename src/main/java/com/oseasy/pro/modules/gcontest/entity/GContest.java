package com.oseasy.pro.modules.gcontest.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.oseasy.act.common.persistence.ActEntity;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;

/**
 * 大赛信息Entity
 * @author zy
 * @version 2017-03-11
 */
public class GContest extends ActEntity<GContest> {

	private static final long serialVersionUID = 1L;
	private String actywId;//流程业务表
	private String pName;		// 项目名称
	private String declareId;		// 申报人ID
	private String profession;		// 专业
	private String current;		// 现况
	private String tel;		// 联系电话
	private String universityId;		// 所属学院id
	private String type;		// 项目类型
	private String level;		// 项目级别
	private String gScore;		// 评审中打分
	private String comment;		// 意见
	private String grade;		// 结论1是合格0是不合格
	private String introduction;		// 项目简介
	private String financingStat;		// 融资情况 0 未，1 100w一下 2 100w以上
	private String auditState;		// 审核状态  	0：保存未提交 	1：待学院专家打分2：待学院秘书审核	3：待学校专家打分	4：待学校网评打分	5：待学校秘书路演审核	 6：学校评级终审	7：结束
	private String collegeResult;		// 学院检查结果(1是合格0是不合格)
	private float collegeScore;		// 学院检查评分
	private String collegeSug;		// 学院审核意见
	private String schoolResult;		// 学校检查结果(1是合格0是不合格)
	private float schoolScore;		// 学校检查评分
	private String schoolSug;			// 学校审核意见

	private String schoolluyanResult;		// 学院检查结果(1是合格0是不合格)
	private float schoolluyanScore;		// 学院检查评分
	private String schoolluyanSug;		// 学院审核意见
	private String schoolendResult;		// 学校检查结果(1是合格0是不合格)
	private float schoolendScore;		// 学校检查评分
	private String schoolendSug;			// 学校审核意见
	private Date subTime;		// 提交时间
	private String currentSystem;		//当前赛制 ：校赛 省赛 国赛    默认校赛

	private float collegeExportScore;		// 学院检查评分
	private float schoolExportScore;		// 学校检查评分

	private Date regTime;		// 工商登记时间
	private String goodStatement;		// 诚信声明 0不同意 1同意
	private String teamId;		// 团队ID

	private String pId;		// 关联项目ID
	private String source;		// source
	private String competitionNumber;		// 大赛编号
	private String announceId;				//通告id
	private String year;//大赛年份

	private String snames;  //项目组成员
	private String tnames;  //指导老师
	private String sourceName;     //来源名称

	private String belongsField; //所属领域

	private List<SysAttachment> attList=Lists.newArrayList();

	private AttachMentEntity attachMentEntity;

	private List<TeamUserHistory>  teamUserRelationList = Lists.newArrayList(); //团队信息

	private List<TeamUserHistory> teacherList = Lists.newArrayList();
	private List<TeamUserHistory> studentList = Lists.newArrayList();
	private List<TeamUserHistory> teamUserHistoryList = Lists.newArrayList(); //团队信息

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public List<TeamUserHistory> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<TeamUserHistory> teacherList) {
		this.teacherList = teacherList;
	}

	public List<TeamUserHistory> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<TeamUserHistory> studentList) {
		this.studentList = studentList;
	}

	public List<TeamUserHistory> getTeamUserHistoryList() {
		return teamUserHistoryList;
	}

	public void setTeamUserHistoryList(List<TeamUserHistory> teamUserHistoryList) {
		this.teamUserHistoryList = teamUserHistoryList;
	}

	public String getSnames() {
		return snames;
	}

	public void setSnames(String snames) {
		this.snames = snames;
	}

	public String getTnames() {
		return tnames;
	}

	public void setTnames(String tnames) {
		this.tnames = tnames;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	//评分标准list
	private List<String> scoreList;

	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}

	public List<String> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<String> scoreList) {
		this.scoreList = scoreList;
	}
	private int snumber;

	public GContest() {
		super();
	}

	public GContest(String id) {
		super(id);
	}

	public String getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(String announceId) {
		this.announceId = announceId;
	}

	public List<SysAttachment> getAttList() {
		return attList;
	}

	public void setAttList(List<SysAttachment> attList) {
		this.attList = attList;
	}

	private HashMap<String,Object> vars; //工作流查询用的map addBy zhangzheng

	public String getCurrentSystem() {
		return currentSystem;
	}

	public void setCurrentSystem(String currentSystem) {
		this.currentSystem = currentSystem;
	}

	public String getSchoolResult() {
		return schoolResult;
	}

	public void setSchoolResult(String schoolResult) {
		this.schoolResult = schoolResult;
	}

	public float getSchoolScore() {
		return schoolScore;
	}

	public void setSchoolScore(float schoolScore) {
		this.schoolScore = schoolScore;
	}

	public float getCollegeExportScore() {
		return collegeExportScore;
	}

	public void setCollegeExportScore(float collegeExportScore) {
		this.collegeExportScore = collegeExportScore;
	}

	public float getSchoolExportScore() {
		return schoolExportScore;
	}

	public void setSchoolExportScore(float schoolExportScore) {
		this.schoolExportScore = schoolExportScore;
	}

	public HashMap<String,Object> getVars() {
		HashMap<String,Object> vars=new HashMap<String, Object>();
		vars.put("number",competitionNumber);  //大赛编号
		vars.put("name",pName);              //项目名称
		vars.put("type",type);               //项目类型
		vars.put("level",level);             //项目类型
		vars.put("financingStat",financingStat);  //融资情况
		vars.put("gScore",gScore);             //打分情况
		vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,grade);             //审核
		vars.put("teacher",teamId);          //指导老师 需要子表处理
		vars.put("collegeResult",collegeResult);  //评比结果
		vars.put("state",auditState);        //项目状态
		vars.put("declareId",declareId);     //大赛申报人id
		vars.put("leaderNumber",declareId);  //学号  需要查子表处理
		vars.put("teamList",teamId);         //项目组成员  需要子表处理
		vars.put("teacher",teamId);          //指导老师 需要子表处理
		return vars;
	}

	public String getBelongsField() {
		return belongsField;
	}

	public void setBelongsField(String belongsField) {
		this.belongsField = belongsField;
	}

	public String getgScore() {
		return gScore;
	}

	public void setgScore(String gScore) {
		this.gScore = gScore;
	}

	public String getCollegeResult() {
		return collegeResult;
	}

	public void setCollegeResult(String collegeResult) {
		this.collegeResult = collegeResult;
	}

	public float getCollegeScore() {
		return collegeScore;
	}

	public void setCollegeScore(float collegeScore) {
		this.collegeScore = collegeScore;
	}

	public String getCollegeSug() {
		return collegeSug;
	}

	public void setCollegeSug(String collegeSug) {
		this.collegeSug = collegeSug;
	}

	public String getSchoolluyanResult() {
		return schoolluyanResult;
	}

	public void setSchoolluyanResult(String schoolluyanResult) {
		this.schoolluyanResult = schoolluyanResult;
	}

	public float getSchoolluyanScore() {
		return schoolluyanScore;
	}

	public void setSchoolluyanScore(float schoolluyanScore) {
		this.schoolluyanScore = schoolluyanScore;
	}

	public String getSchoolluyanSug() {
		return schoolluyanSug;
	}

	public void setSchoolluyanSug(String schoolluyanSug) {
		this.schoolluyanSug = schoolluyanSug;
	}

	public String getSchoolendResult() {
		return schoolendResult;
	}

	public void setSchoolendResult(String schoolendResult) {
		this.schoolendResult = schoolendResult;
	}

	public float getSchoolendScore() {
		return schoolendScore;
	}

	public void setSchoolendScore(float schoolendScore) {
		this.schoolendScore = schoolendScore;
	}

	public String getSchoolendSug() {
		return schoolendSug;
	}

	public void setSchoolendSug(String schoolendSug) {
		this.schoolendSug = schoolendSug;
	}

	public Date getSubTime() {
		return subTime;
	}

	public void setSubTime(Date subTime) {
		this.subTime = subTime;
	}

	public void setSchoolSug(String schoolSug) {
		this.schoolSug = schoolSug;
	}

	public String getSchoolSug() {
		return schoolSug;
	}

	public void setShoolSug(String schoolSug) {
		this.schoolSug = schoolSug;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	@Length(min=0, max=64, message="申报人ID长度必须介于 0 和 64 之间")
	public String getDeclareId() {
		return declareId;
	}

	public void setDeclareId(String declareId) {
		this.declareId = declareId;
	}

	@Length(min=0, max=64, message="专业长度必须介于 0 和 64 之间")
	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	@Length(min=0, max=64, message="现况长度必须介于 0 和 64 之间")
	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	@Length(min=0, max=32, message="联系电话长度必须介于 0 和 32 之间")
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Length(min=0, max=64, message="所属学院id长度必须介于 0 和 64 之间")
	public String getUniversityId() {
		return universityId;
	}

	public void setUniversityId(String universityId) {
		this.universityId = universityId;
	}

	@Length(min=0, max=1, message="项目类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=1, message="项目级别长度必须介于 0 和 1 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Length(min=0, max=1, message="融资情况 0 未，1 100w一下 2 100w以上长度必须介于 0 和 1 之间")
	public String getFinancingStat() {
		return financingStat;
	}

	public void setFinancingStat(String financingStat) {
		this.financingStat = financingStat;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	@Length(min=0, max=1, message="诚信声明 0不同意 1同意长度必须介于 0 和 1 之间")
	public String getGoodStatement() {
		return goodStatement;
	}

	public void setGoodStatement(String goodStatement) {
		this.goodStatement = goodStatement;
	}
/*
	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}*/

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	@Length(min=0, max=64, message="团队ID长度必须介于 0 和 64 之间")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	@Length(min=0, max=64, message="关联项目ID长度必须介于 0 和 64 之间")
	public String getPId() {
		return pId;
	}

	public void setPId(String pId) {
		this.pId = pId;
	}

	@Length(min=0, max=64, message="source长度必须介于 0 和 64 之间")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Length(min=1, max=64, message="大赛编号长度必须介于 1 和 64 之间")
	public String getCompetitionNumber() {
		return competitionNumber;
	}

	public void setCompetitionNumber(String competitionNumber) {
		this.competitionNumber = competitionNumber;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public List<TeamUserHistory> getTeamUserRelationList() {
		return teamUserRelationList;
	}

	public void setTeamUserRelationList(List<TeamUserHistory> teamUserRelationList) {
		this.teamUserRelationList = teamUserRelationList;
	}

	public int getSnumber() {
		return snumber;
	}

	public void setSnumber(int snumber) {
		this.snumber = snumber;
	}
}