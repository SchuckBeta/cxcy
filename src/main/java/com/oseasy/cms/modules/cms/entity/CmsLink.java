package com.oseasy.cms.modules.cms.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

import java.util.Date;

/**
 * 友情链接Entity.
 * @author zy
 * @version 2018-08-30
 */
public class CmsLink extends DataEntity<CmsLink> {

	private static final long serialVersionUID = 1L;
	private String linkname;		// 名称
	private String sitelink;		// 网站地址
	private String sitetype;		// 网站类别（1文字链接，2图片链接）
	private String logo;		// Logo
	private String sort;		// 排序
	private String isShow;		// 显示（0否1是）
	private String description;		// 描述

	public CmsLink() {
		super();
	}

	public CmsLink(String id){
		super(id);
	}

	public CmsLink(String tenantId, String linkname, String sitelink, String sitetype, String logo, String sort, String isShow, Date createDate, Date updateDate){
		this();
		this.tenantId = tenantId;
		this.linkname = linkname;
		this.sitelink = sitelink;
		this.sitetype = sitetype;
		this.logo = logo;
		this.sort = sort;
		this.isShow = isShow;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Length(min=0, max=30, message="名称长度必须介于 0 和 30 之间")
	public String getLinkname() {
		return linkname;
	}

	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}

	@Length(min=0, max=50, message="网站地址长度必须介于 0 和 50 之间")
	public String getSitelink() {
		return sitelink;
	}

	public void setSitelink(String sitelink) {
		this.sitelink = sitelink;
	}

	@Length(min=0, max=11, message="网站类别（1文字链接，2图片链接）长度必须介于 0 和 11 之间")
	public String getSitetype() {
		return sitetype;
	}

	public void setSitetype(String sitetype) {
		this.sitetype = sitetype;
	}

	@Length(min=0, max=255, message="Logo长度必须介于 0 和 255 之间")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}