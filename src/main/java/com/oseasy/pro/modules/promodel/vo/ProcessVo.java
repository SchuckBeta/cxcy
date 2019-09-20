package com.oseasy.pro.modules.promodel.vo;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;

public class ProcessVo {
	private Date time;
	private String type;//1节点2周报
	private String gnodeId;//节点id
	private String gnodeName;//节点名称
	private String status;//节点状态 0执行过 1未执行过
	private String name;//节点名称
	private List<SysAttachment> fileList; //附件列表
	private String date;

	public ProcessVo() {
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	public String getGnodeName() {
		return gnodeName;
	}

	public void setGnodeName(String gnodeName) {
		this.gnodeName = gnodeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SysAttachment> getFileList() {
		return fileList;
	}

	public void setFileList(List<SysAttachment> fileList) {
		this.fileList = fileList;
	}
}
