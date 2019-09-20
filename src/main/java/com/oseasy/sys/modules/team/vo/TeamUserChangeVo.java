package com.oseasy.sys.modules.team.vo;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.team.entity.Team;

import org.hibernate.validator.constraints.Length;

/**
 * 团队人员变更表Entity.
 * @author 团队人员变更表
 * @version 2018-07-19
 */
public class TeamUserChangeVo extends DataEntity<TeamUserChangeVo> {

	private static final long serialVersionUID = 1L;
	private User user;		// 用户ID
	private String operType;		// 操作1：添加人员 2：删除人员
	private String teamId;		// 团队ID
	private Team team;		// 团队ID
	private User operUser;		// 操作人id

	public TeamUserChangeVo() {
		super();
	}

	public TeamUserChangeVo(String id){
		super(id);
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public User getOperUser() {
		return operUser;
	}

	public void setOperUser(User operUser) {
		this.operUser = operUser;
	}
}