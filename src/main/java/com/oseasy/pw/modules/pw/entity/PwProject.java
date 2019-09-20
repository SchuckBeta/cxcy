package com.oseasy.pw.modules.pw.entity;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.vo.SysAttachmentVo;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import java.util.List;

import org.hibernate.validator.constraints.Length;

/**
 * pwEntity.
 * @author zy
 * @version 2018-11-20
 */
public class PwProject extends DataExtEntity<PwProject> {

	private static final long serialVersionUID = 1L;
	private String eid;		// 申报单编号【pw_enter-&gt;id】
	private String name;		// 名称
	private String stype;		// 来源类型:0、默认;1、大创;2、大赛
	private String remarks;		//简介
	private AttachMentEntity attachMentEntity; //附件

	private List<SysAttachment> fileInfo;

	private List<SysAttachment> sysAttachmentList;

	public PwProject() {
		super();
	}

	public PwProject(String id){
		super(id);
	}

	@Length(min=1, max=64, message="申报单编号【pw_enter-&gt;id】长度必须介于 1 和 64 之间")
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public List<SysAttachment> getSysAttachmentList() {
		return sysAttachmentList;
	}

	public void setSysAttachmentList(List<SysAttachment> sysAttachmentList) {
		this.sysAttachmentList = sysAttachmentList;
	}

	@Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=200, message="来源类型:0、默认;1、大创;2、大赛长度必须介于 0 和 200 之间")
	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}

	public List<SysAttachment> getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(List<SysAttachment> fileInfo) {
		this.fileInfo = fileInfo;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {return true;}
		if (!(obj instanceof PwProject)) { return false;}
		PwProject re = (PwProject) obj;
		return name.equals(re.getName())&& remarks.equals(re.getRemarks());
	}
}