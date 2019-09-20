package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.DateUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * 流程年份表Entity.
 * @author zy
 * @version 2018-03-21
 */
public class ActYwYear extends DataEntity<ActYwYear> {

	private static final long serialVersionUID = 1L;
	private String rid;		//模板年份ID
	private String year;		// 年份
	private String actywId;		// actywId
	private Date nodeStartDate;  // 节点开始日期（申报）
 	private Date nodeEndDate;  // 节点结束日期（申报）

	private Date startDate;  // 项目开始日期（分年份）
 	private Date endDate;  // 项目结束日期（分年份）

	public ActYwYear() {
		super();
	}

	public ActYwYear(String id){
		super(id);
	}

	public Date getNodeStartDate() {
		try {
			return DateUtil.getStartDate(nodeStartDate);
		} catch (ParseException e) {
			return nodeStartDate;
		}
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setNodeStartDate(Date nodeStartDate) {
		this.nodeStartDate = nodeStartDate;
	}

	public Date getNodeEndDate() {
		try {
			return DateUtil.getEndDate(nodeEndDate);
		} catch (ParseException e) {
			return nodeEndDate;
		}
	}

	public void setNodeEndDate(Date nodeEndDate) {
		this.nodeEndDate = nodeEndDate;
	}

	@Length(min=1, max=10, message="年份长度必须介于 1 和 10 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Length(min=1, max=64, message="actywId长度必须介于 1 和 64 之间")
	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

}