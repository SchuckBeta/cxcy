package com.oseasy.pro.modules.project.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 项目通告Entity
 * @author zdk
 * @version 2017-03-30
 */
public class ProjectAnnounce extends DataEntity<ProjectAnnounce> {
	
	private static final long serialVersionUID = 1L;
	private String proType;		// 项目类型 1双创 2创业
	private Date beginDate;		// 申报开始时间
	private Date endDate;		// 申报结束时间
	private String content;		// content
	private String files;		// 附件ID
	private String projectState;		// 项目状态
	private String remark;		// 备注
	private Date midStartDate;		// 中期检查时间
	private Date midEndDate;		// 中期检查结束时间
	private Date finalStartDate;		// 结项审核时间
	private Date finalEndDate;		// 结项审核结束时间
	private String flowId;		// 流程id
	private Date pIniStart;		// 立项审核开始/结束时间
	private Date pIniEnd;		// 立项审核开始/结束时间
	private String name;		// 名称
	private String status;      // 状态
	
	/*
	private Date pInitStart = new Date(); 
	private Date pInitEnd = new Date();
*/
	
	public ProjectAnnounce() {
		super();
	}

	public ProjectAnnounce(String id) {
		super(id);
	}

	@Length(min=0, max=64, message="项目类型 1双创 2创业长度必须介于 0 和 1 之间")
	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=512, message="附件ID长度必须介于 0 和 512 之间")
	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}
	
	@Length(min=0, max=64, message="项目状态长度必须介于 0 和 64 之间")
	public String getProjectState() {
		return projectState;
	}

	public void setProjectState(String projectState) {
		this.projectState = projectState;
	}
	
	@Length(min=0, max=512, message="备注长度必须介于 0 和 512 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getMidStartDate() {
		return midStartDate;
	}

	public void setMidStartDate(Date midStartDate) {
		this.midStartDate = midStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getMidEndDate() {
		return midEndDate;
	}

	public void setMidEndDate(Date midEndDate) {
		this.midEndDate = midEndDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFinalStartDate() {
		return finalStartDate;
	}

	public void setFinalStartDate(Date finalStartDate) {
		this.finalStartDate = finalStartDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFinalEndDate() {
		return finalEndDate;
	}

	public void setFinalEndDate(Date finalEndDate) {
		this.finalEndDate = finalEndDate;
	}
	
	@Length(min=0, max=64, message="流程id长度必须介于 0 和 64 之间")
	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	

	
	
	public Date getpIniStart() {
		return pIniStart;
	}

	public Date getpIniEnd() {
		return pIniEnd;
	}

	public void setpIniStart(Date pIniStart) {
		this.pIniStart = pIniStart;
	}

	public void setpIniEnd(Date pIniEnd) {
		this.pIniEnd = pIniEnd;
	}

	@Length(min=0, max=64, message="名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}