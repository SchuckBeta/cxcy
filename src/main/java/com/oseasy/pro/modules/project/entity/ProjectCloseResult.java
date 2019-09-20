package com.oseasy.pro.modules.project.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * project_close_resultEntity
 * @author zhangzheng
 * @version 2017-03-29
 */
public class ProjectCloseResult extends DataEntity<ProjectCloseResult> {
	
	private static final long serialVersionUID = 1L;
	private String closeId;		// 项目结项申报表id
	private String result;		// 项目成果
	private String reward;		// 奖励情况概述
	private String sort;		// 排序号
	
	public ProjectCloseResult() {
		super();
	}

	public ProjectCloseResult(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="项目结项申报表id长度必须介于 0 和 64 之间")
	public String getCloseId() {
		return closeId;
	}

	public void setCloseId(String closeId) {
		this.closeId = closeId;
	}
	
	@Length(min=0, max=500, message="项目成果长度必须介于 0 和 500 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Length(min=0, max=500, message="奖励情况概述长度必须介于 0 和 500 之间")
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
	
	@Length(min=0, max=11, message="排序号长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
}