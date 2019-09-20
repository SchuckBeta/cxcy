/**
 *
 */
package com.oseasy.cms.modules.cms.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;

/**
 * 站点Entity


 */
public class Site extends DataEntity<Site> {
    public static final String SITE_ID = "siteId";
    private static final long serialVersionUID = 1L;
	private String name;	// 站点名称
	private String url;//域名
	private String theme;// 主题
	private String description;// 描述，填写有助于搜索引擎优化

	private String copyright;// 版权信息
	/**
	 * 模板路径
	 * 对应 webapp\WEB-INF\views\modules\${front}
	 *     webapp\static\modules\cms\${front}
	 *     使用见head.jsp
	 *     对应 FrontRouteEnum
	 */
	private String frontTemplateRoute;

	private String isCurrentsite;   //是否为当前站点 0:不是 1：是

	private CmsSiteconfig cmsSiteconfig; //具体配置

	public Site() {
		super();
		this.isNewRecord= true;
	}

	public Site(String id) {
		this();
		this.id = id;
	}

	public Site(String tenantId,String name,String url,String isCurrentsite,String theme){
		this();
		this.tenantId = tenantId;
		this.name = name;
		this.url = url;
		this.isCurrentsite = isCurrentsite;
		this.theme = theme;
	}

	public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getIsCurrentsite() {
		return isCurrentsite;
	}

	public void setIsCurrentsite(String isCurrentsite) {
		this.isCurrentsite = isCurrentsite;
	}



	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		String siteId = (String)CoreUtils.getCache(SITE_ID);
		return StringUtils.isNotBlank(siteId)?siteId:defaultSiteId();
	}

    /**
   	 * 模板路径
   	 */
   	public static final String TPL_BASE = "/WEB-INF/views/modules/cms/";

	public String getFrontTemplateRoute() {
		return frontTemplateRoute;
	}

	public void setFrontTemplateRoute(String frontTemplateRoute) {
		this.frontTemplateRoute = frontTemplateRoute;
	}

	public CmsSiteconfig getCmsSiteconfig() {
		return cmsSiteconfig;
	}

	public void setCmsSiteconfig(CmsSiteconfig cmsSiteconfig) {
		this.cmsSiteconfig = cmsSiteconfig;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}