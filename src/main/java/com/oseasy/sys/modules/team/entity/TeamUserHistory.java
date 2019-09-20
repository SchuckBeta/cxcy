package com.oseasy.sys.modules.team.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 团队历史纪录Entity.
 * @author chenh
 * @version 2017-11-14
 */
public class TeamUserHistory extends DataEntity<TeamUserHistory> {
	public 	static final String finish_val0="0";//项目已提交，还未结束的
	public 	static final String finish_val1="1";//项目已提交，已结束的
	public 	static final String finish_val2="2";//项目已保存，未提交的
	private static final long serialVersionUID = 1L;
//	private String utype;		// 成员类型：1、学生，2、导师
	private String userType;		// 成员类型：1、学生，2、导师
	private User user;		// 用户ID
	private String teamId;		// 团队ID
	private String state;		// 状态
	private String weightVal;		// 根据学分配比 由leader分配。
	private String proId;		// 项目/大赛ID 入驻申报
	private String proType;		// 双创项目、双创大赛、科研
	private String proSubType;		// 【大创 中创 小创 互联网+ 蓝桥杯  数据字典】
	private String finish;		// 所作的项目或者大赛是否结束，0-未结束，1-结束
	private String userId;//接收页面传值userid
	private String userzz;//职责
	private String year;//项目年份
	
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getUserzz() {
		return userzz;
	}

	public void setUserzz(String userzz) {
		this.userzz = userzz;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public TeamUserHistory() {
		super();
	}

	public TeamUserHistory(String id) {
		super(id);
	}

	@Length(min=0, max=1, message="成员类型：1、学生，2、导师长度必须介于 0 和 1 之间")
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

	@Length(min=0, max=64, message="团队ID长度必须介于 0 和 64 之间")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Length(min=0, max=11, message="根据学分配比 由leader分配。长度必须介于 0 和 11 之间")
	public String getWeightVal() {
		if (StringUtil.isEmpty(weightVal)) {
			return null;
		}else{
			return weightVal;
		}
	}

	public void setWeightVal(String weightVal) {
		if (StringUtil.isEmpty(weightVal)) {
			this.weightVal = null;
		}else{
			this.weightVal = weightVal;
		}
	}

	@Length(min=0, max=64, message="项目/大赛ID长度必须介于 0 和 64 之间")
	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	@Length(min=0, max=64, message="双创项目、双创大赛、科研长度必须介于 0 和 64 之间")
	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	@Length(min=0, max=64, message="【大创 中创 小创 互联网+ 蓝桥杯  数据字典】长度必须介于 0 和 64 之间")
	public String getProSubType() {
		return proSubType;
	}

	public void setProSubType(String proSubType) {
		this.proSubType = proSubType;
	}

	@Length(min=1, max=1, message="所作的项目或者大赛是否结束，0-未结束，1-结束长度必须介于 1 和 1 之间")
	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	@Override
    public boolean equals(Object obj) {
        if(obj == this) {return true;}
        if (!(obj instanceof TeamUserHistory)) { return false;}
		TeamUserHistory re = (TeamUserHistory) obj;
		if(StringUtil.isNotEmpty(userzz)){
			return userId.equals(re.getUserId())&& userzz.equals(re.getUserzz());
		}else {
			return userId.equals(re.getUserId());
		}

    }

}