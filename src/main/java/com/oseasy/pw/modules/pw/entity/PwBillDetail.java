package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 账单明细Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwBillDetail extends DataEntity<PwBillDetail> {

	private static final long serialVersionUID = 1L;
	private String bid;		// 账单编号
	private String name;		// 名称
	private String totalAmount;		// 总金额

	public PwBillDetail() {
		super();
	}

	public PwBillDetail(String id){
		super(id);
	}

	@Length(min=1, max=64, message="账单编号长度必须介于 1 和 64 之间")
	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	@Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}