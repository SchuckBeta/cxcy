package com.oseasy.act.modules.actyw.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * 专家指派任务Entity.
 * @author zy
 * @version 2019-01-03
 */
public class ActYwEtAssignProject extends DataExtEntity<ActYwEtAssignProject> {
	private static final long serialVersionUID = 1L;
	private String actywId;		// 项目类别编号
	private String actywName;		// 项目类别名称
	private String gnodeId;		// 审核节点编号
	private String gnodeName;		// 审核节点名称
	private String todoNum;		// 	待审核数
	private String hasNum;		// 	已经审核数
	private String expertNum;		// 需求专家数

	//查询专家任务参数
	private String userId;		// 专家userId

	public ActYwEtAssignProject() {
		super();
	}

	public ActYwEtAssignProject(String id){
		super(id);
	}

	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public String getActywName() {
		return actywName;
	}

	public void setActywName(String actywName) {
		this.actywName = actywName;
	}

	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	public String getGnodeName() {
		return gnodeName;
	}

	public void setGnodeName(String gnodeName) {
		this.gnodeName = gnodeName;
	}

	public String getTodoNum() {
		return todoNum;
	}

	public void setTodoNum(String todoNum) {
		this.todoNum = todoNum;
	}

	public String getHasNum() {
		return hasNum;
	}

	public void setHasNum(String hasNum) {
		this.hasNum = hasNum;
	}

	public String getExpertNum() {
		return expertNum;
	}

	public void setExpertNum(String expertNum) {
		this.expertNum = expertNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}