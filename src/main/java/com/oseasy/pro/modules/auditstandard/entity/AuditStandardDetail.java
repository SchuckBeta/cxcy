package com.oseasy.pro.modules.auditstandard.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 评审标准详情Entity.
 * @author 9527
 * @version 2017-07-28
 */
public class AuditStandardDetail extends DataEntity<AuditStandardDetail> {

	private static final long serialVersionUID = 1L;
	private String manageId;		// 评审标准管理表ID
	private String checkPoint;		// 检查要点
	private String checkElement;		// 审核元素
	private String viewScore;		// 参考分值
	private String sort;		// 排序

	public AuditStandardDetail() {
		super();
	}

	public AuditStandardDetail(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="评审标准管理表ID长度必须介于 1 和 64 之间")
	public String getManageId() {
		return manageId;
	}

	public void setManageId(String manageId) {
		this.manageId = manageId;
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

	@Length(min=1, max=2, message="排序长度必须介于 1 和 2 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}