package com.oseasy.cms.modules.cms.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 首页管理Entity.
 * @author zy
 * @version 2018-09-03
 */
public class CmsIndex extends DataEntity<CmsIndex> {

	private static final long serialVersionUID = 1L;
	private String modelname;		// 频道名称
	private String modelename;		// 频道英文名称
	private String ename;		// 描述
	private String sort;		// 排序
	private String isShow;		// 显示（0否1是）
	private String description;		// 描述
	private String picUrl;		// 描述

	private CmsIndex homeSCDT;
	private CmsIndex homeSCTZ;
	private CmsIndex homeSSDT;


	public CmsIndex() {
		super();
	}

	public CmsIndex(String id){
		super(id);
	}

	public CmsIndex(String tenantId,String modelname,String modelename,String ename,String sort,String isShow){
		this();
		this.tenantId = tenantId;
		this.modelname = modelname;
		this.modelename = modelename;
		this.ename = ename;
		this.sort = sort;
		this.isShow = isShow;
	}

	@Length(min=0, max=20, message="频道名称长度必须介于 0 和 20 之间")
	public String getModelname() {
		return modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public CmsIndex getHomeSCDT() {
		return homeSCDT;
	}

	public void setHomeSCDT(CmsIndex homeSCDT) {
		this.homeSCDT = homeSCDT;
	}

	public CmsIndex getHomeSCTZ() {
		return homeSCTZ;
	}

	public void setHomeSCTZ(CmsIndex homeSCTZ) {
		this.homeSCTZ = homeSCTZ;
	}

	public CmsIndex getHomeSSDT() {
		return homeSSDT;
	}

	public void setHomeSSDT(CmsIndex homeSSDT) {
		this.homeSSDT = homeSSDT;
	}

	@Length(min=0, max=20, message="频道英文名称长度必须介于 0 和 20 之间")
	public String getModelename() {
		return modelename;
	}

	public void setModelename(String modelename) {
		this.modelename = modelename;
	}

	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Length(min=0, max=11, message="显示（0否1是）长度必须介于 0 和 11 之间")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	@Length(min=0, max=500, message="描述长度必须介于 0 和 500 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}