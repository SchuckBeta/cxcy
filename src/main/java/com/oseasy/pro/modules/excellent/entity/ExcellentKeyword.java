package com.oseasy.pro.modules.excellent.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 优秀展示关键词Entity.
 * @author 9527
 * @version 2017-06-23
 */
public class ExcellentKeyword extends DataEntity<ExcellentKeyword> {

	private static final long serialVersionUID = 1L;
	private String excellentId;		// 优秀展示信息表id
	private String keyword;		// 关键字

	public ExcellentKeyword() {
		super();
	}

	public ExcellentKeyword(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="优秀展示信息表id长度必须介于 0 和 64 之间")
	public String getExcellentId() {
		return excellentId;
	}

	public void setExcellentId(String excellentId) {
		this.excellentId = excellentId;
	}

	@Length(min=0, max=16, message="关键字长度必须介于 0 和 16 之间")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}