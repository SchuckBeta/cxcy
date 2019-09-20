package com.oseasy.pro.modules.auditstandard.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 评审标准详情记录Entity.
 * @author 9527
 * @version 2017-07-28
 */
public class AuditStandardDetailIns extends DataEntity<AuditStandardDetailIns> {

	private static final long serialVersionUID = 1L;
	private String fid;		// 项目、大赛id
	private String auditInfoId;		// 审核记录表ID
	private String checkPoint;		// 检查要点
	private String checkElement;		// 审核元素
	private String viewScore;		// 参考分值
	private String score;		// 实际分值
	private String sort;		// 排序

	public AuditStandardDetailIns() {
		super();
	}

	public AuditStandardDetailIns(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="项目、大赛id长度必须介于 1 和 64 之间")
	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	@Length(min=1, max=64, message="审核记录表ID长度必须介于 1 和 64 之间")
	public String getAuditInfoId() {
		return auditInfoId;
	}

	public void setAuditInfoId(String auditInfoId) {
		this.auditInfoId = auditInfoId;
	}

	@Length(min=1, max=255, message="检查要点长度必须介于 1 和 255 之间")
	public String getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	@Length(min=1, max=255, message="审核元素长度必须介于 1 和 255 之间")
	public String getCheckElement() {
		return checkElement;
	}

	public void setCheckElement(String checkElement) {
		this.checkElement = checkElement;
	}

	public String getViewScore() {
		return viewScore;
	}

	public void setViewScore(String viewScore) {
		this.viewScore = viewScore;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Length(min=1, max=2, message="排序长度必须介于 1 和 2 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}