package com.oseasy.pro.modules.gcontest.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 大赛获奖表Entity
 * @author zy
 * @version 2017-03-11
 */
public class GContestAward extends DataEntity<GContestAward> {
	
	private static final long serialVersionUID = 1L;
	private String contestId;		// 大赛id
	private String award;		// 1：金奖2：银奖3：铜奖
	private String money;		// money
	private String awardLevel;		// 1：国家2：省3：院校
	
	public GContestAward() {
		super();
	}

	public GContestAward(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="大赛id长度必须介于 0 和 64 之间")
	public String getContestId() {
		return contestId;
	}

	public void setContestId(String contestId) {
		this.contestId = contestId;
	}
	
	@Length(min=0, max=1, message="1：金奖2：银奖3：铜奖长度必须介于 0 和 1 之间")
	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}
	
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
	@Length(min=0, max=1, message="1：国家2：省3：院校长度必须介于 0 和 1 之间")
	public String getAwardLevel() {
		return awardLevel;
	}

	public void setAwardLevel(String awardLevel) {
		this.awardLevel = awardLevel;
	}
	
}