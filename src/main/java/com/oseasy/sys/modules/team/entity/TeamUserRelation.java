package com.oseasy.sys.modules.team.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.entity.SysTeacherExpansion;

import org.hibernate.validator.constraints.Length;

/**
 * 团队人员关系表Entity
 * @author zhangzheng
 * @version 2017-03-06
 */
public class TeamUserRelation extends DataEntity<TeamUserRelation> {
	
	private static final long serialVersionUID = 1L;
	private String userType;		// 成员类型（1学生2导师）
	private User user;		// user_id
	private String teamId;		// team_id
	private String state;  //加入状态(0已加入，1申请加入，2负责人发邀请,3不同意，4忽略,5加入失败）
    
	private SysStudentExpansion student;  //addBy zhangzheng
	private SysTeacherExpansion teacher;  //addBy zhangzheng
    private Integer UserCount;//判断用户是否存在
    private boolean isSelf;

	private int weightVal;   //学分配比
	private int weightTotal;  //总配比


    public boolean getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public Integer getUserCount() {
		return UserCount;
	}

	public void setUserCount(Integer userCount) {
		UserCount = userCount;
	}

	public SysStudentExpansion getStudent() {
		return student;
	}

	public void setStudent(SysStudentExpansion student) {
		this.student = student;
	}

	public SysTeacherExpansion getTeacher() {
		return teacher;
	}

	public void setTeacher(SysTeacherExpansion teacher) {
		this.teacher = teacher;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public TeamUserRelation() {
		super();
	}

	public TeamUserRelation(String id) {
		super(id);
	}

	@Length(min=0, max=1, message="成员类型（0学生1导师）长度必须介于 0 和 1 之间")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=64, message="team_id长度必须介于 0 和 64 之间")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public int getWeightVal() {
		return weightVal;
	}

	public void setWeightVal(int weightVal) {
		this.weightVal = weightVal;
	}

	public int getWeightTotal() {
		return weightTotal;
	}

	public void setWeightTotal(int weightTotal) {
		this.weightTotal = weightTotal;
	}
}