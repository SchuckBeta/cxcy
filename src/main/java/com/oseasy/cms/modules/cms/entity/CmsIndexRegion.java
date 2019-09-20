package com.oseasy.cms.modules.cms.entity;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 首页区域管理Entity
 * @author daichanggeng
 * @version 2017-04-06
 */
public class CmsIndexRegion extends DataEntity<CmsIndexRegion> {

	private static final long serialVersionUID = 1L;
	private Category category;		// 栏目编号
	private String regionId;		// 区域编号
	private String regionName;		// 区域名
	private String regionType;		// 区域类型(1.banner区 2.大赛热点 3.优秀项目展示 4.导师风采 5.名师讲堂 6.双创动态  7.双创通知 8.省市动态 0.栏目区)
	private String regionModel;		// 区域模式：0,参数模式；1,模板模式(tpl）；2,标准模式(gen）
	private String regionState;		// 区域状态(0.无效,1有效）
	private String regionSort;		// 区域排序
	private List<CmsIndexResource> childResourceList = Lists.newArrayList(); 	// 拥有资源列表

	public CmsIndexRegion() {
		super();
	}

	public CmsIndexRegion(Category category) {
		super();
		this.category = category;
	}

	public CmsIndexRegion(String id, Category category) {
		super();
		this.id = id;
		this.setCategory(category);
	}

	public CmsIndexRegion(String id) {
		super(id);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Length(min=1, max=64, message="区域编号长度必须介于 1 和 64 之间")
	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	@Length(min=1, max=64, message="区域名长度必须介于 1 和 64 之间")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Length(min=1, max=64, message="区域类型长度必须介于 1 和 64 之间")
	public String getRegionType() {
		return regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	@Length(min=1, max=64, message="区域状态长度必须介于 1 和 64 之间")
	public String getRegionState() {
		return regionState;
	}

	public void setRegionState(String regionState) {
		this.regionState = regionState;
	}

	@Length(min=1, max=64, message="区域排序长度必须介于 1 和 64 之间")
	public String getRegionSort() {
		return regionSort;
	}

	public void setRegionSort(String regionSort) {
		this.regionSort = regionSort;
	}

	public List<CmsIndexResource> getChildResourceList() {
		return childResourceList;
	}

	public void setChildResourceList(List<CmsIndexResource> childResourceList) {
		this.childResourceList = childResourceList;
	}

	public String getRegionModel() {
		return regionModel;
	}

	public void setRegionModel(String regionModel) {
		this.regionModel = regionModel;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}