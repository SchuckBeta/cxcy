package com.oseasy.scr.modules.scr.entity;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.com.pcore.common.persistence.TreeExtEntity;
import com.oseasy.scr.modules.scr.vo.IScoObj;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 学分申请认定Entity.
 * @author liangjie
 * @version 2019-01-08
 */
public class ScoRapplyCert extends TreeExtEntity<ScoRapplyCert> implements IScoObj {

	private static final long serialVersionUID = 1L;
	private ScoRapplyCert parent;		// parent_id
	private String parentIds;		// parent_ids
	private String name;		// 证书名称
	private Integer sort;
	private String remarks;
	private List<ScoRapplyCert> children;
	private List<SysAttachment> sysAttachmentList; //附件
	private List<ScoRapplyCert> scoRapplyCertList;

	public List<ScoRapplyCert> getScoRapplyCertList() {
		return scoRapplyCertList;
	}

	public void setScoRapplyCertList(List<ScoRapplyCert> scoRapplyCertList) {
		this.scoRapplyCertList = scoRapplyCertList;
	}

	public List<SysAttachment> getSysAttachmentList() {
		return sysAttachmentList;
	}

	public void setSysAttachmentList(List<SysAttachment> sysAttachmentList) {
		this.sysAttachmentList = sysAttachmentList;
	}

	public List<ScoRapplyCert> getChildren() {
		return children;
	}

	public void setChildren(List<ScoRapplyCert> children) {
		this.children = children;
	}

	public ScoRapplyCert() {
		super();
	}

	public ScoRapplyCert(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="父级编号不能为空")
	public ScoRapplyCert getParent() {
		return parent;
	}

	public void setParent(ScoRapplyCert parent) {
		this.parent = parent;
	}

	@Length(min=0, max=2000, message="parent_ids长度必须介于 0 和 2000 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Length(min=1, max=64, message="证书名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Length(max=2000, message="备注长度必须介于 1 和 64 之间")
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : CoreIds.NCE_SYS_TREE_PROOT.getId();
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}