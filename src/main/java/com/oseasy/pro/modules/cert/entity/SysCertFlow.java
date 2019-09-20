package com.oseasy.pro.modules.cert.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 证书模板-流程节点关系Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCertFlow extends DataEntity<SysCertFlow> {

	private static final long serialVersionUID = 1L;
	private String flow;		// 流程
	private String node;		// 节点
	private String certId;		// 证书模板id
	private String results;//下发证书时根据该字段作条件判断
	public SysCertFlow() {
		super();
	}

	public SysCertFlow(String id){
		super(id);
	}

	
	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	@Length(min=1, max=64, message="流程长度必须介于 1 和 64 之间")
	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	@Length(min=1, max=64, message="节点长度必须介于 1 和 64 之间")
	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	@Length(min=1, max=64, message="证书模板id长度必须介于 1 和 64 之间")
	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

}