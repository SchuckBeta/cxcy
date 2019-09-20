package com.oseasy.pro.modules.interactive.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 评论表Entity.
 * @author 9527
 * @version 2017-06-30
 */
public class SysComment extends DataEntity<SysComment> {

	private static final long serialVersionUID = 1L;
	private String likes;		// 点赞量
	private String foreignId;		// 评论对象主键
	private String content;		// 评论内容
	private String userId;		// 评论人id
	private String ip;		// 评论IP
	private String auditState;		// 审核状态：0-未通过，1-通过
	private String auditUserId;		// 审核人
	private Date auditDate;		// 审核时间

	public SysComment() {
		super();
	}

	public SysComment(String id) {
		super(id);
	}

	@Length(min=1, max=11, message="点赞量长度必须介于 1 和 11 之间")
	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	@Length(min=1, max=64, message="评论对象主键长度必须介于 1 和 64 之间")
	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	@Length(min=1, max=500, message="评论内容长度必须介于 1 和 500 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@NotNull(message="评论人id不能为空")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Length(min=1, max=64, message="评论IP长度必须介于 1 和 64 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Length(min=1, max=1, message="审核状态：0-未通过，1-通过长度必须介于 1 和 1 之间")
	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	@Length(min=0, max=64, message="审核人长度必须介于 0 和 64 之间")
	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

}