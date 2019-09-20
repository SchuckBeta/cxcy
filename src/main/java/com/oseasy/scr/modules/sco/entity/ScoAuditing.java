package com.oseasy.scr.modules.sco.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * 学分记录审核表Entity.
 * @author zhangzheng
 * @version 2017-07-18
 */
public class ScoAuditing extends DataEntity<ScoAuditing> {

	private static final long serialVersionUID = 1L;
	private User user;		// 审核人ID
	private String type;		// 学分类型：1课程学分/2技能学分
	private String applyId;		// 学分申请ID
	private String procInsId;		// 流程实例ID
	private String scoreVal;		// 分数
	private String suggest;		// 意见

	private String auditStatus; //审核状态

	public ScoAuditing() {
		super();
	}

	public ScoAuditing(String id) {
		super(id);
	}

	@NotNull(message="审核人ID不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Length(min=0, max=1, message="学分类型：1课程学分/2技能学分长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=1, max=64, message="学分申请ID长度必须介于 1 和 64 之间")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	@Length(min=1, max=64, message="流程实例ID长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}

	public String getScoreVal() {
		return scoreVal;
	}

	public void setScoreVal(String scoreVal) {
		this.scoreVal = scoreVal;
	}

	@Length(min=0, max=2000, message="意见长度必须介于 0 和 2000 之间")
	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

}