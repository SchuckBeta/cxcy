/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.oseasy.pro.modules.dasai.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.act.common.persistence.ActEntity;

/**
 * 大赛测试Entity
 * @author zhangzheng
 * @version 2017-02-24
 */
public class OaDasai   extends ActEntity<OaDasai> {
	
	private static final long serialVersionUID = 1L;

	private String projectName;		// 项目名称
	private String state;		// 提交状态 1:审核中 2:审核完成
	private Integer score;      //分数


	
	public OaDasai() {
		super();
	}

	public OaDasai(String id) {
		super(id);
	}

	
	@Length(min=0, max=255, message="项目名称长度必须介于 0 和 255 之间")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Length(min=0, max=1, message="提交状态 0:草稿 1:审核中 2:通过长度必须介于 0 和 1 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
}