package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 缴费记录Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwBillPrecords extends DataEntity<PwBillPrecords> {

	private static final long serialVersionUID = 1L;
	private String bid;		// 账单编号
	private String payName;		// 收款人姓名
	private Date payTime;		// 开始使用时间

	public PwBillPrecords() {
		super();
	}

	public PwBillPrecords(String id){
		super(id);
	}

	@Length(min=1, max=64, message="账单编号长度必须介于 1 和 64 之间")
	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	@Length(min=0, max=255, message="收款人姓名长度必须介于 0 和 255 之间")
	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

}