package com.oseasy.dr.modules.dr.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.thread.IThreadPvo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁卡出入记录Entity.
 * @author 奔波儿灞
 * @version 2018-04-08
 */
public class DrInoutRecord extends DataEntity<DrInoutRecord> implements IThreadPvo{

	private static final long serialVersionUID = 1L;
	private String uid;		// 用户ID
	private String cardId;		// 卡ID
    private DrCardreGroup group;        // 预警规则ID
	private String erspaceId;		// 设备空间ID
	private Date enterTime;		// 进门打卡时间
	private Date exitTime;		// 出门打卡时间
	private Date pcTime;//打卡时间(排序用)
	private String cardNo;//卡号
	private String eptId;//设备id
	private String rspType;//场地类型
	private String rspaceId;//场地id
	private String drNo;//设备端口号
	private String name;//场地门名称

	public DrCardreGroup getGroup() {
        return group;
    }

    public void setGroup(DrCardreGroup group) {
        this.group = group;
    }

    public String getCardNo() {
        if(StringUtil.isNotEmpty(this.cardNo)){
            this.cardNo = StringUtil.rmstart(this.cardNo, DrCard.RM_ZERO);
        }
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEptId() {
		return eptId;
	}

	public void setEptId(String eptId) {
		this.eptId = eptId;
	}

	public String getRspType() {
		return rspType;
	}

	public void setRspType(String rspType) {
		this.rspType = rspType;
	}

	public String getRspaceId() {
		return rspaceId;
	}

	public void setRspaceId(String rspaceId) {
		this.rspaceId = rspaceId;
	}

	public String getDrNo() {
		return drNo;
	}

	public void setDrNo(String drNo) {
		this.drNo = drNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DrInoutRecord() {
		super();
	}

	public DrInoutRecord(String id){
		super(id);
	}

	@Length(min=1, max=64, message="用户ID长度必须介于 1 和 64 之间")
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Length(min=1, max=64, message="卡ID长度必须介于 1 和 64 之间")
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Date getPcTime() {
		return pcTime;
	}

	public void setPcTime(Date pcTime) {
		this.pcTime = pcTime;
	}

	@Length(min=1, max=64, message="设备空间ID长度必须介于 1 和 64 之间")
	public String getErspaceId() {
		return erspaceId;
	}

	public void setErspaceId(String erspaceId) {
		this.erspaceId = erspaceId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="进门打卡时间不能为空")
	public Date getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="出门打卡时间不能为空")
	public Date getExitTime() {
		return exitTime;
	}

	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}

}