package com.oseasy.pro.modules.auditstandard.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 评审标准、流程关系表Entity.
 * @author 9527
 * @version 2017-07-28
 */
public class AuditStandardFlow extends DataEntity<AuditStandardFlow> {

	private static final long serialVersionUID = 1L;
	private String flow;		// 流程
	private String node;		// 节点
	private String auditStandardId;		// 审核标准
	private String isEscoreNodes;//需要打分的子节点
	public AuditStandardFlow() {
		super();
	}

	public AuditStandardFlow(String id) {
		super(id);
	}

	public String getIsEscoreNodes() {
		return isEscoreNodes;
	}

	public void setIsEscoreNodes(String isEscoreNodes) {
		this.isEscoreNodes = isEscoreNodes;
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

	@Length(min=1, max=64, message="审核标准长度必须介于 1 和 64 之间")
	public String getAuditStandardId() {
		return auditStandardId;
	}

	public void setAuditStandardId(String auditStandardId) {
		this.auditStandardId = auditStandardId;
	}

}