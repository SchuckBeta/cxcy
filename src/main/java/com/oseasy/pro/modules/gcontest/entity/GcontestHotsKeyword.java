package com.oseasy.pro.modules.gcontest.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 大赛热点关键字Entity.
 * @author 9527
 * @version 2017-07-12
 */
public class GcontestHotsKeyword extends DataEntity<GcontestHotsKeyword> {

	private static final long serialVersionUID = 1L;
	private String gcontestHotsId;		// 大赛热点表id
	private String keyword;		// 关键字

	public GcontestHotsKeyword() {
		super();
	}

	public GcontestHotsKeyword(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="大赛热点表id长度必须介于 0 和 64 之间")
	public String getGcontestHotsId() {
		return gcontestHotsId;
	}

	public void setGcontestHotsId(String gcontestHotsId) {
		this.gcontestHotsId = gcontestHotsId;
	}

	@Length(min=0, max=16, message="关键字长度必须介于 0 和 16 之间")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}