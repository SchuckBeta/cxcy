package com.oseasy.pro.modules.cert.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 下发证书进度信息Entity.
 * @author 奔波儿灞
 * @version 2018-03-02
 */
public class CertMakeInfo extends DataEntity<CertMakeInfo> {

	private static final long serialVersionUID = 1L;
	private String actywid;		// actywid
	private String certid;		// 证书模板id
	private String certname;		// 证书名称
	private String total;		// 总数
	private String success;		// 成功数
	private String fail;		// 失败数
	private String isComplete;		// 是否结束：0-未结束，1-结束
	private String msg;		// 存储相关信息
	private String errmsg;		// 错误信息

	public CertMakeInfo() {
		super();
	}

	public CertMakeInfo(String id){
		super(id);
	}

	@Length(min=0, max=128, message="actywid长度必须介于 0 和 128 之间")
	public String getActywid() {
		return actywid;
	}

	public void setActywid(String actywid) {
		this.actywid = actywid;
	}

	@Length(min=0, max=128, message="证书模板id长度必须介于 0 和 128 之间")
	public String getCertid() {
		return certid;
	}

	public void setCertid(String certid) {
		this.certid = certid;
	}

	@Length(min=0, max=128, message="证书名称长度必须介于 0 和 128 之间")
	public String getCertname() {
		return certname;
	}

	public void setCertname(String certname) {
		this.certname = certname;
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

	@Length(min=0, max=2000, message="存储相关信息长度必须介于 0 和 2000 之间")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Length(min=0, max=2000, message="错误信息长度必须介于 0 和 2000 之间")
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}