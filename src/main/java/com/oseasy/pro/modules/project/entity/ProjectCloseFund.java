package com.oseasy.pro.modules.project.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

import org.hibernate.validator.constraints.Length;

/**
 * project_close_fundEntity
 * @author zhangzheng
 * @version 2017-03-29
 */
public class ProjectCloseFund extends DataEntity<ProjectCloseFund> {
	
	private static final long serialVersionUID = 1L;
	private String closeId;		// 项目结项申报表id
	private String place;		// 使用经费项目
	private Integer count;		// 使用金额(元)
	private String sort;		// 排序号
	
	public ProjectCloseFund() {
		super();
	}

	public ProjectCloseFund(String id) {
		super(id);
	}

	public String getCloseId() {
		return closeId;
	}

	public void setCloseId(String closeId) {
		this.closeId = closeId;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
}