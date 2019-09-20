package com.oseasy.pro.modules.interactive.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 浏览表Entity.
 * @author 9527
 * @version 2017-06-30
 */
public class SysViews extends DataEntity<SysViews> {

	private static final long serialVersionUID = 1L;
	private String foreignId;		// 浏览对象主键
	private String userId;		// 浏览人id
	private String ip;		// 浏览IP
	private String token;		// 浏览人标识

	public SysViews() {
		super();
	}

	public SysViews(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="浏览对象主键长度必须介于 1 和 64 之间")
	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	@NotNull(message="浏览人id不能为空")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Length(min=1, max=64, message="浏览IP长度必须介于 1 和 64 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Length(min=1, max=64, message="浏览人标识长度必须介于 1 和 64 之间")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}