package com.oseasy.pro.modules.project.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 国创项目进度表单Entity
 * @author 9527
 * @version 2017-03-29
 * @Deprecated
 */
public class ProProgress extends DataEntity<ProProgress> {

	private static final long serialVersionUID = 1L;
	private String type;		// 1-中期检查表单,2-结项表单
	private String fId;		// 中期检查表单id、结项表单id
	private Date startDate;		// 实际完成开始时间
	private Date endDate;		// 实际完成结束时间
	private String situation;		// 实际完成情况
	private String result;		// 阶段性成果

	public ProProgress() {
		super();
	}

	public ProProgress(String id) {
		super(id);
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getFId() {
		return fId;
	}

	public void setFId(String fId) {
		this.fId = fId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}