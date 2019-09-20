package com.oseasy.pro.modules.cert.vo;

/**
 * 证书模板-流程节点关系Entity.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
public class SysCertFlowVo{
	private String id;
	private String flow;		// 流程
	private String flowName;		// 流程
	private String node;		// 节点
	private String nodeName;		// 节点
	private String certId;		// 证书模板id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getCertId() {
		return certId;
	}
	public void setCertId(String certId) {
		this.certId = certId;
	}
	
}