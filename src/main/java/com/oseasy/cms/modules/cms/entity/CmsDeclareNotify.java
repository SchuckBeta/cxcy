package com.oseasy.cms.modules.cms.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * declareEntity.
 * @author 奔波儿灞
 * @version 2018-01-24
 */
public class CmsDeclareNotify extends DataEntity<CmsDeclareNotify> {

	private static final long serialVersionUID = 1L;
	private String content;		// 页面内容
	private String isRelease;		// 是否发布：0-否，1-是
	private String title;		// 标题
	private String type;		// 通知类型 字典
	private String categoryId;		// 栏目id
	private String views;		// 浏览量
	private Date releaseDate;		// 发布时间
	public CmsDeclareNotify() {
		super();
	}

	public CmsDeclareNotify(String id){
		super(id);
	}

	
	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(min=0, max=64, message="是否发布：0-否，1-是长度必须介于 0 和 64 之间")
	public String getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(String isRelease) {
		this.isRelease = isRelease;
	}

	@Length(min=0, max=255, message="标题长度必须介于 0 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min=0, max=64, message="通知类型 字典长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="栏目id长度必须介于 0 和 64 之间")
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}