package com.oseasy.pro.modules.project.vo;

import java.util.Date;

public class ProModelNodeVo {
	private String id;//节点id
	private String name;//节点名称
	private Date beginDate;//节点开始时间gtime
	private Date endDate;//节点结束时间gtime
	private String result;//节点审核结果
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
