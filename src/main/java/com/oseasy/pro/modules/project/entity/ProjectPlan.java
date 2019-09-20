package com.oseasy.pro.modules.project.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 项目任务Entity
 * @author 9527
 * @version 2017-03-11
 */
public class ProjectPlan extends DataEntity<ProjectPlan> {
	
	private static final long serialVersionUID = 1L;
	private String projectId;		// 项目id
	private String content;		// 工作任务
	private String description;		// 任务描述
	private Date startDate;		// 任务开始时间
	private Date endDate;		// 任务截止时间	
	private String cost;		// 成本
	private String quality;		// 质量评价
	private String sort;		// 排序号
	public ProjectPlan() {
		super();
	}

	
	public ProjectPlan(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="项目id长度必须介于 0 和 64 之间")
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	@Length(min=0, max=20, message="成本长度必须介于 0 和 20 之间")
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	@Length(min=0, max=20, message="质量评价长度必须介于 0 和 20 之间")
	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}
	@Length(min=0, max=11, message="排序号长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}