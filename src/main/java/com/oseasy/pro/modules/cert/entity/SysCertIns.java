package com.oseasy.pro.modules.cert.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 证书信息记录Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCertIns extends DataEntity<SysCertIns> {

	private static final long serialVersionUID = 1L;
	private String certId;		// 生成证书的模板id
	private String certName;		// 证书名称
	private String proid;		// 业务表ID
	private String flow;		// 流程
	private String gnode;		// 节点
	private String award;		// 授予人

	public SysCertIns() {
		super();
	}

	public SysCertIns(String id){
		super(id);
	}

	@Length(min=0, max=64, message="生成证书的模板id长度必须介于 0 和 64 之间")
	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	@Length(min=0, max=64, message="证书名称长度必须介于 0 和 64 之间")
	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
	}

	@Length(min=0, max=64, message="业务表ID长度必须介于 0 和 64 之间")
	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}

	@Length(min=0, max=64, message="流程长度必须介于 0 和 64 之间")
	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	@Length(min=0, max=64, message="节点长度必须介于 0 和 64 之间")
	public String getGnode() {
		return gnode;
	}

	public void setGnode(String gnode) {
		this.gnode = gnode;
	}

	@Length(min=0, max=64, message="授予人长度必须介于 0 和 64 之间")
	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

}