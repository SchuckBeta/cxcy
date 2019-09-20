package com.oseasy.pro.modules.project.entity;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

/**
 * project_closeEntity
 * @author zhangzheng
 * @version 2017-03-29
 */
public class ProjectClose extends DataEntity<ProjectClose> {
	
	private static final long serialVersionUID = 1L;
	private String projectId;		// 项目id
	private String suggest;		// 导师建议及意见
	private Date suggestDate;		// 导师建议及意见时间
	private String status;		// 提交状态：1保存，2提交

	private List<ProSituation> proSituationList;  // 子表  组成员完成情况
	private List<ProProgress> proProgresseList;    // 子表 计划工作任务
	private List<ProjectCloseFund> projectCloseFundList; // 经费使用情况
	private List<ProjectCloseResult> projectCloseResultList;  //成果描述

	private List<TeamUserRelation>  teamUserRelationList = Lists.newArrayList(); //团队信息

	private List<TeamUserHistory>  teamUserHistoryList = Lists.newArrayList();

	private AttachMentEntity attachMentEntity;

	public List<TeamUserHistory> getTeamUserHistoryList() {
		return teamUserHistoryList;
	}

	public void setTeamUserHistoryList(List<TeamUserHistory> teamUserHistoryList) {
		this.teamUserHistoryList = teamUserHistoryList;
	}

	public ProjectClose() {
		super();
	}

	public ProjectClose(String id) {
		super(id);
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProSituation> getProSituationList() {
		return proSituationList;
	}

	public void setProSituationList(List<ProSituation> proSituationList) {
		this.proSituationList = proSituationList;
	}

	public List<ProProgress> getProProgresseList() {
		return proProgresseList;
	}

	public void setProProgresseList(List<ProProgress> proProgresseList) {
		this.proProgresseList = proProgresseList;
	}

	public List<ProjectCloseFund> getProjectCloseFundList() {
		return projectCloseFundList;
	}

	public void setProjectCloseFundList(List<ProjectCloseFund> projectCloseFundList) {
		this.projectCloseFundList = projectCloseFundList;
	}

	public List<ProjectCloseResult> getProjectCloseResultList() {
		return projectCloseResultList;
	}

	public void setProjectCloseResultList(List<ProjectCloseResult> projectCloseResultList) {
		this.projectCloseResultList = projectCloseResultList;
	}

	public List<TeamUserRelation> getTeamUserRelationList() {
		return teamUserRelationList;
	}

	public void setTeamUserRelationList(List<TeamUserRelation> teamUserRelationList) {
		this.teamUserRelationList = teamUserRelationList;
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}
}