package com.oseasy.scr.modules.sco.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 学分分配比例Entity.
 * @author 9527
 * @version 2017-07-18
 */
public class ScoAllotRatio extends DataEntity<ScoAllotRatio> {

	private static final long serialVersionUID = 1L;
	private String affirmConfId;		// 学分认定配置表id
	private String number;		// 组人数
	private String ratio;		// 分配比例

	public ScoAllotRatio() {
		super();
	}

	public ScoAllotRatio(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="学分认定配置表id长度必须介于 1 和 64 之间")
	public String getAffirmConfId() {
		return affirmConfId;
	}

	public void setAffirmConfId(String affirmConfId) {
		this.affirmConfId = affirmConfId;
	}

	@Length(min=0, max=11, message="组人数长度必须介于 0 和 11 之间")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Length(min=0, max=64, message="分配比例长度必须介于 0 和 64 之间")
	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

}