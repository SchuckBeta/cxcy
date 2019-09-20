package com.oseasy.act.modules.actyw.entity;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.oseasy.com.pcore.common.persistence.DataExtEntity;

/**
 * 专家指派任务Entity.
 * @author zy
 * @version 2019-01-03
 */
public class ActYwEtAssignQueryVo extends DataExtEntity<ActYwEtAssignQueryVo> {

	private static final long serialVersionUID = 1L;
	private String actywId;		// 项目类别编号
	private String actywName;		// 项目类别编号
	private List<ActYwGnode> gnodeList;		//节点列表

	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public List<ActYwGnode> getGnodeList() {
		return gnodeList;
	}

	public void setGnodeList(List<ActYwGnode> gnodeList) {
		this.gnodeList = gnodeList;
	}

	public String getActywName() {
		return actywName;
	}

	public void setActywName(String actywName) {
		this.actywName = actywName;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}