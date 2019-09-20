package com.oseasy.sys.modules.team.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 团队人员变更表Entity.
 * @author 团队人员变更表
 * @version 2018-07-19
 */
public class TeamUserChange extends DataEntity<TeamUserChange> {

	private static final long serialVersionUID = 1L;
	private String userId;		// 用户ID
	private String operType;		// 操作1：添加人员 2：删除人员 3：变更人员
	private String msg;
	private String teamId;		// 团队ID
	private String operUserId;		// 操作人id
	private String duty;		// 团队职责1负责人2组员3导师
	private String proId;		// 项目ID
	public TeamUserChange() {
		super();
	}

	public TeamUserChange(String id){
		super(id);
	}

	@Length(min=0, max=64, message="用户ID长度必须介于 0 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Length(min=0, max=1, message="操作1：添加人员 2：删除人员长度必须介于 0 和 1 之间")
	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	@Length(min=0, max=64, message="团队ID长度必须介于 0 和 64 之间")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	@Length(min=0, max=64, message="操作人id长度必须介于 0 和 64 之间")
	public String getOperUserId() {
		return operUserId;
	}

	public void setOperUserId(String operUserId) {
		this.operUserId = operUserId;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

}