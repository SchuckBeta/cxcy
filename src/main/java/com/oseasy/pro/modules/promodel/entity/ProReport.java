package com.oseasy.pro.modules.promodel.entity;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import java.util.List;

/**
 * 学生提交信息表Entity.
 * @author pw
 * @version 2017-12-01
 */
public class ProReport extends DataEntity<ProReport> {
	private static final long serialVersionUID = 1L;
	private String proModelId;		// 自定义表id
	private String gnodeId;		// gnode表id
	private String gnodeName;		// gnode表gnodeName
	private String state;		// 保留字段
	private String stageResult;//阶段成果
	private AttachMentEntity attachMentEntity; //附件
	private List<SysAttachment> files;//附件

	public ProReport() {
		super();
	}

	public String getProModelId() {
		return proModelId;
	}

	public void setProModelId(String proModelId) {
		this.proModelId = proModelId;
	}

	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStageResult() {
		return stageResult;
	}

	public void setStageResult(String stageResult) {
		this.stageResult = stageResult;
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}

	public List<SysAttachment> getFiles() {
		return files;
	}

	public void setFiles(List<SysAttachment> files) {
		this.files = files;
	}

	public String getGnodeName() {
		return gnodeName;
	}

	public void setGnodeName(String gnodeName) {
		this.gnodeName = gnodeName;
	}
}