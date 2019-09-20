package com.oseasy.pro.modules.gcontest.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 大赛审核信息Entity
 * @author zy
 * @version 2017-03-11
 */
public class GAuditInfo extends DataEntity<GAuditInfo> {
	
	private static final long serialVersionUID = 1L;
	private String gId;		// 大赛id
	private String auditId;		// 审核人id
	private String auditLevel;		// 审核级别流程
	private String auditName;		// 评审名称，如评分、答辩等
	private String orgId;		// 所属机构ID
	private String suggest;		// 意见
	private float score;		// 分数
	private String sort;		// 排名
	private String grade;		// 结论1是合格0是不合格
	private String procInsId;		// 流程实例id
	private String collegeId;		// 学院id
	private String schoolId;		// 学校id
	
	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public GAuditInfo() {
		super();
	}

	public GAuditInfo(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="大赛id长度必须介于 0 和 64 之间")
	public String getGId() {
		return gId;
	}

	public void setGId(String gId) {
		this.gId = gId;
	}
	
	@Length(min=0, max=64, message="审核人id长度必须介于 0 和 64 之间")
	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	
	@Length(min=0, max=64, message="审核级别：1代表校级 2代表省级长度必须介于 0 和 64 之间")
	public String getAuditLevel() {
		return auditLevel;
	}

	public void setAuditLevel(String auditLevel) {
		this.auditLevel = auditLevel;
	}
	
	@Length(min=0, max=64, message="评审名称，如评分、答辩等长度必须介于 0 和 64 之间")
	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	
	@Length(min=0, max=64, message="所属机构ID长度必须介于 0 和 64 之间")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Length(min=0, max=512, message="意见长度必须介于 0 和 512 之间")
	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	@Length(min=0, max=64, message="流程实例id长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
}