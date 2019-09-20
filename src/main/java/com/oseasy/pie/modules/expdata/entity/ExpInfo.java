package com.oseasy.pie.modules.expdata.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 导出数据信息Entity.
 * @author 奔波儿灞
 * @version 2017-12-27
 */
public class ExpInfo extends DataEntity<ExpInfo> {
	private static final long serialVersionUID = 1L;
	  public static final String JK_EXPINFO_ID = "eid";
	private String expType;		// 导出数据的类型
	private String total;		// 总数
	private String success;		// 成功数
	private String fail;		// 失败数
	private String isComplete;		// 是否结束：0-未结束，1-结束
	private Date startDate;		// 开始时间
	private Date endDate;		// 结束时间
	private String fileId;		// 文件信息表id
	private String errmsg;		// 错误信息
	private String totalThread;//总线程数
	private SysAttachment sa;
	public ExpInfo() {
		super();
	}

	public SysAttachment getSa() {
		return sa;
	}

	public void setSa(SysAttachment sa) {
		this.sa = sa;
	}

	public String getTotalThread() {
		return totalThread;
	}

	public void setTotalThread(String totalThread) {
		this.totalThread = totalThread;
	}

	public ExpInfo(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="导出数据的类型长度必须介于 0 和 64 之间")
	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	@Length(min=0, max=20, message="总数长度必须介于 0 和 20 之间")
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Length(min=0, max=20, message="成功数长度必须介于 0 和 20 之间")
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@Length(min=0, max=20, message="失败数长度必须介于 0 和 20 之间")
	public String getFail() {
		return fail;
	}

	public void setFail(String fail) {
		this.fail = fail;
	}

	@Length(min=0, max=2, message="是否结束：0-未结束，1-结束长度必须介于 0 和 2 之间")
	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Length(min=0, max=64, message="文件信息表id长度必须介于 0 和 64 之间")
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Length(min=0, max=2000, message="错误信息长度必须介于 0 和 2000 之间")
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}