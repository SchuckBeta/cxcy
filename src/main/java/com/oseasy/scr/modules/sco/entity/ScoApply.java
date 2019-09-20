package com.oseasy.scr.modules.sco.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.oseasy.act.common.persistence.ActEntity;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;

/**
 * 学分课程申请Entity.
 * @author zhangzheng
 * @version 2017-07-13
 */
public class ScoApply extends ActEntity<ScoApply> {
	public static final String auditStatus1="1";//待提交认定
	public static final String auditStatus2="2";//待审核
	public static final String auditStatus3="3";//通过
	public static final String auditStatus4="4";//未通过
	private static final long serialVersionUID = 1L;
	private String userId;		// 申报人
	private String courseId;		// 课程ID
	private String procInsId;		// 流程实例ID
	//（可通过其他字段判断出来，如果是auditStatus==4，则courseStatus=="课程未达标"；如果auditStatus==3,则courseStatus=="课程达标",其他则为空
	private String courseStatus;		// 课程状态：1.课程未达标、2课程已达标
	private String auditStatus;		// 审核状态:1.待提交认定2、待审核 3、未通过4、通过
	private int realTime;		// 实际学时
	private float realScore;		// 实际成绩
	private float score;		// 实际认定学分

	private Date applyDate;	// 申请日期

	private ScoCourse scoCourse;  //学分课程

	private String keyword;
	private Date beginDate;
	private Date endDate;

	public HashMap<String,Object> getVars() {
		HashMap<String,Object> vars=new HashMap<String, Object>();
		vars.put("id",id);  						//课程学分id
		vars.put("userId",userId);  				//用户id
		vars.put("courseId",courseId);      		//课程id
		vars.put("courseStatus",courseStatus); 	//课程状态
		vars.put("auditStatus",auditStatus);  	//审核状态
		vars.put("realTime",realTime);  			//实际学时
		vars.put("realScore",realScore);   		//实际成绩
		vars.put("score",score);             		//实际认定学分
		vars.put("applyDate",applyDate);          //申请日期

		return vars;
	}


	private List<SysAttachment> attachmentList = Lists.newArrayList();  //附件
	private List<SysAttachment> sysAttachmentList; //附件(json)

	public List<SysAttachment> getSysAttachmentList() {
		return sysAttachmentList;
	}

	public void setSysAttachmentList(List<SysAttachment> sysAttachmentList) {
		this.sysAttachmentList = sysAttachmentList;
	}

	public ScoApply() {
		super();
	}

	public ScoApply(String id) {
		super(id);
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	//（可通过其他字段判断出来，如果是auditStatus==4，则courseStatus=="课程未达标"；如果auditStatus==3,则courseStatus=="课程达标",其他则为空
	public String getCourseStatus() {
//		String courseStatus="";
//		if (StringUtil.equals(auditStatus,"3"))	{
//			courseStatus = "课程未达标";
//		}
//		if (StringUtil.equals(auditStatus,"4")) {
//			courseStatus = "课程已达标";
//		}
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public int getRealTime() {
		return realTime;
	}

	public void setRealTime(int realTime) {
		this.realTime = realTime;
	}

	public float getRealScore() {
		return realScore;
	}

	public void setRealScore(float realScore) {
		this.realScore = realScore;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public ScoCourse getScoCourse() {
		return scoCourse;
	}

	public void setScoCourse(ScoCourse scoCourse) {
		this.scoCourse = scoCourse;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<SysAttachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<SysAttachment> attachmentList) {
		this.attachmentList = attachmentList;
	}
}