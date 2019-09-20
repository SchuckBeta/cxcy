package com.oseasy.cms.modules.cms.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 网站配置Entity.
 * @author zy
 * @version 2018-08-27
 */
public class CmsSiteconfig extends DataEntity<CmsSiteconfig> {

	private static final long serialVersionUID = 1L;
	private String siteId;		// 站点id
	private String theme;		// 主题

	private String type;		// 类型
	private String picUrl;		// 图片链接

	private String logoLeft;		// Logo_Left
	private String logoRight;		// Logo_Right
	private String bannerimage;		// Banner图片

	private String headText;		// 省厅头部文字


	private String linkType;		// 首页链接展示形式 1:图片链接 2：文字链接

	private List<Map<String,String>> picList;

	private List<Category> categorys;

	public CmsSiteconfig() {
		super();
		this.isNewRecord=true;
	}

	public CmsSiteconfig(String id){
		super(id);
	}
	public CmsSiteconfig(String tenantId,String siteId,String theme,String type,String picUrl,Date createDate,Date updateDate,String linkType){
		this();
		this.tenantId =tenantId;
		this.siteId = siteId;
		this.theme = theme;
		this.type = type;
		this.picUrl = picUrl;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.linkType = linkType;
	}
	public List<Map<String, String>> getPicList() {
		return picList;
	}

	public void setPicList(List<Map<String, String>> picList) {
		this.picList = picList;
	}

	@Length(min=0, max=20, message="站点id长度必须介于 0 和 20 之间")
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@Length(min=0, max=20, message="主题长度必须介于 0 和 20 之间")
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Length(min=0, max=1000, message="Logo_Left长度必须介于 0 和 1000 之间")
	public String getLogoLeft() {
		return logoLeft;
	}

	public void setLogoLeft(String logoLeft) {
		this.logoLeft = logoLeft;
	}

	@Length(min=0, max=1000, message="Logo_Right长度必须介于 0 和 1000 之间")
	public String getLogoRight() {
		return logoRight;
	}

	public void setLogoRight(String logoRight) {
		this.logoRight = logoRight;
	}

	@Length(min=0, max=1000, message="Banner图片长度必须介于 0 和 1000 之间")
	public String getBannerimage() {
		return bannerimage;
	}

	public void setBannerimage(String bannerimage) {
		this.bannerimage = bannerimage;
	}

	public List<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}

	/**
  	 * 模板路径
  	 */
  	public static final String TPL_BASE = "/WEB-INF/views/modules/cms/front/themes";

	/**
 	 * 获得模板方案路径。如：/WEB-INF/views/modules/cms/front/themes/initiate
 	 *
 	 * @return
 	 */
 	public String getSolutionPath() {
 		return TPL_BASE + "/" + getTheme();
 	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getHeadText() {
		return headText;
	}

	public void setHeadText(String headText) {
		this.headText = headText;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}