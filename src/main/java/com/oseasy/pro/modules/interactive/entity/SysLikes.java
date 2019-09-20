package com.oseasy.pro.modules.interactive.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 点赞表Entity.
 * @author 9527
 * @version 2017-06-30
 */
public class SysLikes extends DataEntity<SysLikes> {

	private static final long serialVersionUID = 1L;
	private String foreignId;		// 点赞对象主键
	private String userId;		// 评论人id
	private String ip;		// 点赞IP
	private String token;		// 点赞人标识

	public SysLikes() {
		super();
	}

	public SysLikes(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="点赞对象主键长度必须介于 1 和 64 之间")
	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	@NotNull(message="评论人id不能为空")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Length(min=1, max=64, message="点赞IP长度必须介于 1 和 64 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Length(min=1, max=64, message="点赞人标识长度必须介于 1 和 64 之间")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}