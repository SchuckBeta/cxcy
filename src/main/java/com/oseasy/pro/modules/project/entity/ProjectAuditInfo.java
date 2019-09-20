package com.oseasy.pro.modules.project.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 项目审核信息Entity
 * @author 9527
 * @version 2017-03-11
 */
public class ProjectAuditInfo extends DataEntity<ProjectAuditInfo> {
	
	private static final long serialVersionUID = 1L;
	private String projectId;		// 项目id
	private String auditId;		// 审核人id
	private String auditStep;		// 审核阶段：1立项评审 2中期检查评分 3中期检查评级 4结项评分 5结项评级 6结果评定
	private String auditName;		// 评审名称：如评分、答辩等
	private String orgId;		// 所属机构ID
	private String suggest;		// 意见
	private String grade;		// 评级结果,字典 type = "project_degree"
	private float score;		// 分数
	private String procInsId;		// 流程实例id
	private String gnodeVesion;		// 流程实例id


	private String userId;
	private String userName;

	
	public ProjectAuditInfo() {
		super();
	}

	public ProjectAuditInfo(String id) {
		super(id);
	}

	public String getGnodeVesion() {
		return gnodeVesion;
	}

	public void setGnodeVesion(String gnodeVesion) {
		this.gnodeVesion = gnodeVesion;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	

	public String getAuditStep() {
		return auditStep;
	}

	public void setAuditStep(String auditStep) {
		this.auditStep = auditStep;
	}
	

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}