package com.oseasy.pro.modules.project.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 项目周报Entity
 * @author 张正
 * @version 2017-03-29
 */
public class ProjectWeekly extends DataEntity<ProjectWeekly> {
	
	private static final long serialVersionUID = 1L;
	private String projectId;		// 项目id
	private String plan;		// 下阶段计划任务
	private String problem;		// 存在的问题
	private String achieved;		// 完成情况
	private String suggest;		// 导师建议及意见
	private Date suggestDate;		// 导师建议及意见时间
	private Date startDate;		// 阶段开始时间
	private Date endDate;		// 阶段结束时间
	private Date startPlanDate;		// 阶段开始时间
	private Date endPlanDate;		// 阶段结束时间
	private String status;		// 提交状态：1保存，2提交
	private String lastId;//上周周报id
	public ProjectWeekly() {
		super();
	}

	public ProjectWeekly(String id) {
		super(id);
	}

	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}
	
	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}
	
	public String getAchieved() {
		return achieved;
	}

	public void setAchieved(String achieved) {
		this.achieved = achieved;
	}
	
	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getSuggestDate() {
		return suggestDate;
	}

	public void setSuggestDate(Date suggestDate) {
		this.suggestDate = suggestDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartPlanDate() {
		return startPlanDate;
	}

	public void setStartPlanDate(Date startPlanDate) {
		this.startPlanDate = startPlanDate;
	}

	public Date getEndPlanDate() {
		return endPlanDate;
	}

	public void setEndPlanDate(Date endPlanDate) {
		this.endPlanDate = endPlanDate;
	}
}