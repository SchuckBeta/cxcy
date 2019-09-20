package com.oseasy.com.mqserver.modules.oa.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 通知通告关键词Entity.
 * @author 9527
 * @version 2017-07-11
 */
public class OaNotifyKeyword extends DataEntity<OaNotifyKeyword> {

	private static final long serialVersionUID = 1L;
	private String notifyId;		// 通知通告信息表id
	private String keyword;		// 关键字

	public OaNotifyKeyword() {
		super();
	}

	public OaNotifyKeyword(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="通知通告信息表id长度必须介于 0 和 64 之间")
	public String getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}

	@Length(min=0, max=16, message="关键字长度必须介于 0 和 16 之间")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}