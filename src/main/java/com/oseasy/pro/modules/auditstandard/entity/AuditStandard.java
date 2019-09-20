package com.oseasy.pro.modules.auditstandard.entity;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 评审标准Entity.
 * @author 9527
 * @version 2017-07-28
 */
public class AuditStandard extends DataEntity<AuditStandard> {

	private static final long serialVersionUID = 1L;
	private String name;		// 标准名称
	private String isEscore;		// 是否需要评分
	private String totalScore;		// 总分
	private List<String> checkPoint;
	private List<String> checkElement;
	private List<String> viewScore;
	
	
	public List<String> getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(List<String> checkPoint) {
		this.checkPoint = checkPoint;
	}

	public List<String> getCheckElement() {
		return checkElement;
	}

	public void setCheckElement(List<String> checkElement) {
		this.checkElement = checkElement;
	}

	public List<String> getViewScore() {
		return viewScore;
	}

	public void setViewScore(List<String> viewScore) {
		this.viewScore = viewScore;
	}

	public AuditStandard() {
		super();
	}

	public AuditStandard(String id) {
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