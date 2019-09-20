package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 账单Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwBill extends DataEntity<PwBill> {

	private static final long serialVersionUID = 1L;
	private String name;		// 账单名称
	private String totalAmount;		// 总金额
	private String type;		// 账单类型
	private String status;		// 状态
	private String teamId;		// 团队编号
	private Date cfromDate;		// 账单周期起
	private Date ctoDate;		// 账单周期止
	private String settled;		// 是否结清

	public PwBill() {
		super();
	}

	public PwBill(String id){
		super(id);
	}

	@Length(min=0, max=255, message="账单名称长度必须介于 0 和 255 之间")
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

	@Length(min=0, max=1, message="账单类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min=1, max=64, message="团队编号长度必须介于 1 和 64 之间")
	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCfromDate() {
		return cfromDate;
	}

	public void setCfromDate(Date cfromDate) {
		this.cfromDate = cfromDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCtoDate() {
		return ctoDate;
	}

	public void setCtoDate(Date ctoDate) {
		this.ctoDate = ctoDate;
	}

	@Length(min=0, max=1, message="是否结清长度必须介于 0 和 1 之间")
	public String getSettled() {
		return settled;
	}

	public void setSettled(String settled) {
		this.settled = settled;
	}

}