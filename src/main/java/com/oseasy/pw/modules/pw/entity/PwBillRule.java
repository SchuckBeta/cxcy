package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 费用规则Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwBillRule extends DataEntity<PwBillRule> {

	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String amount;		// 金额
	private String unit;		// 单位
	private Integer cycle;		// 频度
	private Integer day;		// 计算日

	public PwBillRule() {
		super();
	}

	public PwBillRule(String id){
		super(id);
	}

	@Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Length(min=0, max=64, message="单位长度必须介于 0 和 64 之间")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

}