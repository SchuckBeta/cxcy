package com.oseasy.scr.modules.scr.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * 学分规则配比Entity.
 * @author chenh
 * @version 2018-12-26
 */
public class ScoRulePb extends DataExtEntity<ScoRulePb> {

	private static final long serialVersionUID = 1L;
	private ScoRule rule;		// 规则编号
	private String num;		// 人数
	private String val;		// 配比值：5:3:2

	public ScoRulePb() {
		super();
	}

	public ScoRulePb(String id){
		super(id);
	}

	public ScoRule getRule() {
		return rule;
	}

	public void setRule(ScoRule rule) {
		this.rule = rule;
	}

	@Length(min=1, max=64, message="人数长度必须介于 1 和 64 之间")
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@Length(min=1, max=64, message="配比值：5:3:2长度必须介于 1 和 64 之间")
	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}