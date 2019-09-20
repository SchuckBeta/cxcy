/**
 *
 */
package com.oseasy.cms.modules.cms.entity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;

/**
 * 站点Entity


 */
public class SiteConfig extends DataEntity<SiteConfig> {

	private static final long serialVersionUID = 1L;

	private String siteId;	// 站点标题
	private String logoLeft;	// 站点租户logo
	private String logoRight;	// 站点logoSite

	private String theme;	// 主题
	private String bannerimage ; // banner图片 json格式存储

	private List<Category> categorys;

	public SiteConfig() {
		super();
	}

	public SiteConfig(String id) {
		this();
		this.id = id;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getBannerimage() {
		return bannerimage;
	}

	public void setBannerimage(String bannerimage) {
		this.bannerimage = bannerimage;
	}

	public String getLogoLeft() {
		return logoLeft;
	}

	public void setLogoLeft(String logoLeft) {
		this.logoLeft = logoLeft;
	}

	public String getLogoRight() {
		return logoRight;
	}

	public void setLogoRight(String logoRight) {
		this.logoRight = logoRight;
	}

	@Length(min=1, max=255)
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * 获取默认站点ID
	 */
	public static String defaultSiteId() {
		return CmsIds.SITE_TOP.getId();
	}

	/**
	 * 判断是否为默认（官方）站点
	 */
	public static boolean isDefault(String id) {
		return id != null && id.equals(defaultSiteId());
	}

	/**
	 * 获取当前编辑的站点编号
	 */
	public static String getCurrentSiteId() {
		String siteId = (String)CoreUtils.getCache(Site.SITE_ID);
		return StringUtils.isNotBlank(siteId)?siteId:defaultSiteId();
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

	public List<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}