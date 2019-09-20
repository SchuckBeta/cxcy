package com.oseasy.pro.modules.auditstandard.vo;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 评审标准Entity.
 * @author 9527
 * @version 2017-07-28
 */
public class AuditStandardVo extends DataEntity<AuditStandardVo> {

	private static final long serialVersionUID = 1L;
	private String name;		// 标准名称
	private String isEscore;		// 是否需要评分
	private String totalScore;		// 总分
	private String flow;
	private String node;
	private String flowName;
	private String nodeName;
	private String isEscoreNodes;//需要打分的子节点
	private String relationId;//关系id
	private List<Map<String,String>> childs;

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getIsEscoreNodes() {
		return isEscoreNodes;
	}

	public void setIsEscoreNodes(String isEscoreNodes) {
		this.isEscoreNodes = isEscoreNodes;
	}

	public List<Map<String, String>> getChilds() {
		return childs;
	}

	public void setChilds(List<Map<String, String>> childs) {
		this.childs = childs;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public AuditStandardVo() {
		super();
	}

	public AuditStandardVo(String id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsEscore() {
		return isEscore;
	}

	public void setIsEscore(String isEscore) {
		this.isEscore = isEscore;
	}

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

}