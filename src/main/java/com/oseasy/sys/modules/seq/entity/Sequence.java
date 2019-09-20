package com.oseasy.sys.modules.seq.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * 序列表Entity.
 * @author zy
 * @version 2018-10-08
 */
public class Sequence extends DataExtEntity<Sequence> {

	private static final long serialVersionUID = 1L;
	private String name;		// 序列名称
	private String currentValue;		// 序列起始值
	private String increment;		// 序列每次递增值

	public Sequence() {
		super();
	}

	public Sequence(String id){
		super(id);
	}

	@Length(min=1, max=50, message="序列名称长度必须介于 1 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=11, message="序列起始值长度必须介于 1 和 11 之间")
	public String getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	@Length(min=1, max=11, message="序列每次递增值长度必须介于 1 和 11 之间")
	public String getIncrement() {
		return increment;
	}

	public void setIncrement(String increment) {
		this.increment = increment;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}