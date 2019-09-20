package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 状态条件Entity.
 * @author zy
 * @version 2018-02-01
 */
public class ActYwSgtype extends DataEntity<ActYwSgtype> {

	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String regType;		// 正则类型：1、eq;2、区间

	public ActYwSgtype() {
		super();
	}

	public ActYwSgtype(String id){
		super(id);
	}

	@Length(min=0, max=64, message="名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=200, message="正则类型：1、eq;2、区间长度必须介于 0 和 200 之间")
	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

}