/**
 *
 */
package com.oseasy.cms.modules.cms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.cms.common.config.SysCacheKeys;
import com.oseasy.cms.modules.cms.entity.Article;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.CmsIndexRegion;
import com.oseasy.cms.modules.cms.entity.CmsIndexResource;
import com.oseasy.cms.modules.cms.entity.CmsLink;
import com.oseasy.cms.modules.cms.entity.CmsSiteconfig;
import com.oseasy.cms.modules.cms.entity.CmsTemplate;
import com.oseasy.cms.modules.cms.entity.Link;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.enums.ResTemplateEnum;
import com.oseasy.cms.modules.cms.service.ArticleService;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.service.CmsIndexResourceService;
import com.oseasy.cms.modules.cms.service.CmsLinkService;
import com.oseasy.cms.modules.cms.service.CmsSiteconfigService;
import com.oseasy.cms.modules.cms.service.LinkService;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.mapper.JaxbMapper;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.FreeMarkers;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 内容管理工具类
 */
public class CmsUtils {
    private final static Logger logger = LoggerFactory.getLogger(CmsUtils.class);
	private static SiteService siteService = SpringContextHolder.getBean(SiteService.class);
	private static CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
	private static CmsIndexResourceService cmsIndexResourceService = SpringContextHolder.getBean(CmsIndexResourceService.class);
	private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
	private static LinkService linkService = SpringContextHolder.getBean(LinkService.class);
    private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);
	private static CmsLinkService cmsLinkService = SpringContextHolder.getBean(CmsLinkService.class);
	private static CmsSiteconfigService cmsSiteconfigService = SpringContextHolder.getBean(CmsSiteconfigService.class);

	private static final String CMS_CACHE = "cmsCache";

	private CmsUtils() {
        super();
        throw new IllegalStateException("Utility class");
    }

	/**获取资源模板列表*/
	public static List<CmsTemplate> getTemplateList() {
		String path="/templates/modules/cms/";
		List<CmsTemplate> list = Lists.newArrayList();
		for(ResTemplateEnum rte:ResTemplateEnum.values()) {
			CmsTemplate ct=fileToObject(path,rte.getTempName());
			ct.setName(rte.getName());
			ct.setValue(rte.getValue());
			list.add(ct);
		}
		return list;
	}
	/**替换html代码中img的ftp链接*/
	public static String replaceFtpUrl(String content) {
		if (StringUtil.isNotEmpty(content)) {
			content	=content.replaceAll(FtpUtil.FTP_MARKER, FtpUtil.FTP_HTTPURL);
			content =StringEscapeUtils.unescapeHtml4(content);
		}
		return content;
	}
	/**根据模板获取html代码*/
	public static String getHtmlByTemplatePath(String path,Map<String ,Object> model) {
		CmsTemplate ct=fileToObject(path);
		if (ct!=null) {
			String html="";
			try {
				html = FreeMarkers.renderString(ct.getContent(), model);
			} catch (Exception e) {
                logger.error("渲染模板出错，请检查参数和模板" + e.getMessage());
                throw new RuntimeException(e);
			}
			return html;
		}
		return "";
	}
	/**获取模板模式资源的html代码*/
	public static String getHtmlByTemplate(String resid) {
		CmsIndexResource res=cmsIndexResourceService.get(resid);
		if (res!=null&&"1".equals(res.getResModel())&&"1".equals(res.getResState())) {
			ResTemplateEnum rte=ResTemplateEnum.getByValue(res.getTplUrl());
			if (rte!=null) {
				String path="/templates/modules/cms/";
				CmsTemplate ct=fileToObject(path,rte.getTempName());
				if (ct!=null&&res.getTplJson()!=null) {
					Map<String ,Object> model=(HashMap)JSONObject.toBean(JSONObject.fromObject(res.getTplJson()),HashMap.class);
					model.put("freemarkerGetFtpUrl", new FreemarkerGetFtpUrl());
					model.put("user", UserUtils.getUser());
					String html="";
					try {
						html = FreeMarkers.renderString(ct.getContent(), model);
					} catch (Exception e) {
					    logger.error(e.getMessage());
						return "渲染模板出错，请检查参数和模板";
					}
					return html;
				}
			}
		}
		return "";
	}
	public static CmsTemplate fileToObject(String path,String fileName) {
		String pathName =  path+ fileName;
		return fileToObject(pathName);
	}
	public static CmsTemplate fileToObject(String pathName) {
		try {
			Resource resource = new ClassPathResource(pathName);
			InputStream is = resource.getInputStream();
			if (is != null) {
  			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
  			StringBuilder sb = new StringBuilder();
  			while (true) {
  				String line = br.readLine();
  				if (line == null) {
  					break;
  				}
  				sb.append(line).append("\r\n");
  			}
  			if (is != null) {
  				is.close();
  			}
  			if (br != null) {
  				br.close();
  			}
  			return (CmsTemplate) JaxbMapper.fromXml(sb.toString(), CmsTemplate.class);
			}
		} catch (IOException e) {
            logger.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 获得站点列表
	 */
	public static List<Site> getSiteList() {
		@SuppressWarnings("unchecked")
		List<Site> siteList = (List<Site>)CacheUtils.get(CMS_CACHE, "siteList");
		if (siteList == null) {
			Page<Site> page = new Page<Site>(1, -1);
			page = siteService.findPage(page, new Site());
			siteList = page.getList();
			CacheUtils.put(CMS_CACHE, "siteList", siteList);
		}
		return siteList;
	}

	/**
	 * 获得站点信息,默认访问官方站点
	 */
	public static Site getAutoSite() {
		return siteService.getAutoSite();

	}

	/**
	 * 获得站点信息,默认访问官方站点
	 * @param siteId 站点编号
	 */
	public static Site getSite(String siteId) {
		for (Site site : getSiteList()) {
			if (site.getId().equals(siteId)) {

				return site;
			}
		}
		return new Site(siteId);
	}

	/**
	 * 获得主导航列表
	 * @param siteId 站点编号
	 */
	public static List<Category> getMainNavList(String siteId) {
		@SuppressWarnings("unchecked")
		List<Category> mainNavList = (List<Category>)CacheUtils.get(CMS_CACHE, "mainNavList_"+siteId);
		if (mainNavList == null) {
			Category category = new Category();
			category.setSite(new Site(siteId));
			category.setParent(new Category(CoreIds.NCE_SYS_TREE_ROOT.getId()));
			Page<Category> page = new Page<Category>(1, -1);
			page = categoryService.find(page, category);
			mainNavList = page.getList();
			CacheUtils.put(CMS_CACHE, "mainNavList_"+siteId, mainNavList);
		}
		return mainNavList;
	}

	/**
	 * 获取栏目
	 * @param categoryId 栏目编号
	 * @return
	 */
	public static Category getCategory(String categoryId) {
		return categoryService.get(categoryId);
	}

	/**
	 * 获得栏目列表
	 * @param siteId 站点编号
	 * @param parentId 分类父编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 */
	public static List<Category> getCategoryList(String siteId, String parentId, int number, String param) {
		Page<Category> page = new Page<>(1, number, -1);
		Category category = new Category();
		category.setSite(new Site(siteId));
		category.setParent(new Category(parentId));
		if (StringUtil.isNotBlank(param)) {
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
		}
		page = categoryService.find(page, category);
		return page.getList();
	}

	/**
	 * 获取栏目
	 * @param categoryIds 栏目编号
	 * @return
	 */
	public static List<Category> getCategoryListByIds(String categoryIds) {
		return categoryService.findByIds(categoryIds);
	}

	/**
	 * 获取文章
	 * @param articleId 文章编号
	 * @return
	 */
	public static Article getArticle(String articleId) {
		return articleService.get(articleId);
	}

	/**
	 * 获取文章列表
	 * @param siteId 站点编号
	 * @param categoryId 分类编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 * 			posid	推荐位（1：首页焦点图；2：栏目页文章推荐；）
	 * 			image	文章图片（1：有图片的文章）
	 *          orderBy 排序字符串
	 * @return
	 * ${fnc:getArticleList(category.site.id, category.id, not empty pageSize?pageSize:8, 'posid:2, orderBy: \"hits desc\"')}"
	 */
	public static List<Article> getArticleList(String siteId, String categoryId, int number, String param) {
		Page<Article> page = new Page<Article>(1, number, -1);
		Category category = new Category(categoryId, new Site(siteId));
		category.setParentIds(categoryId);
		Article article = new Article(category);
		if (StringUtil.isNotBlank(param)) {
			@SuppressWarnings({ "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
			if (new Integer(1).equals(map.get("posid")) || new Integer(2).equals(map.get("posid"))) {
				article.setPosid(String.valueOf(map.get("posid")));
			}
			if (new Integer(1).equals(map.get("image"))) {
				article.setImage(Const.YES);
			}
			if (StringUtil.isNotBlank((String)map.get("orderBy"))) {
				page.setOrderBy((String)map.get("orderBy"));
			}
		}
		article.setDelFlag(Article.DEL_FLAG_NORMAL);
		page = articleService.findPage(page, article, false);
		return page.getList();
	}

	/**
	 * 获取链接
	 * @param linkId 文章编号
	 * @return
	 */
	public static Link getLink(String linkId) {
		return linkService.get(linkId);
	}

	/**
	 * 获取链接列表
	 * @param siteId 站点编号
	 * @param categoryId 分类编号
	 * @param number 获取数目
	 * @param param  预留参数，例： key1:'value1', key2:'value2' ...
	 * @return
	 */
	public static List<Link> getLinkList(String siteId, String categoryId, int number, String param) {
		Page<Link> page = new Page<Link>(1, number, -1);
		Link link = new Link(new Category(categoryId, new Site(siteId)));
		if (StringUtil.isNotBlank(param)) {
			@SuppressWarnings({ "unused", "rawtypes" })
			Map map = JsonMapper.getInstance().fromJson("{"+param+"}", Map.class);
		}
		link.setDelFlag(Link.DEL_FLAG_NORMAL);
		page = linkService.findPage(page, link, false);
		return page.getList();
	}

	// ============== Cms Cache ==============

	public static Object getCache(String key) {
		return CacheUtils.get(CMS_CACHE, key);
	}

	public static void putCache(String key, Object value) {
		CacheUtils.put(CMS_CACHE, key, value);
	}

	public static void removeCache(String key) {
		CacheUtils.remove(CMS_CACHE, key);
	}

    /**
     * 获得文章动态URL地址
   	 * @param article
   	 * @return url
   	 */
    public static String getUrlDynamic(Article article) {
        if (StringUtil.isNotBlank(article.getLink())) {
            return article.getLink();
        }
        StringBuilder str = new StringBuilder();
        str.append(context.getContextPath()).append(CoreSval.getFrontPath());
        str.append("/view-").append(article.getCategory().getId()).append("-").append(article.getId()).append(CoreSval.getUrlSuffix());
        return str.toString();
    }

    /**
     * 获得栏目动态URL地址
   	 * @param category
   	 * @return url
   	 */
    public static String getUrlDynamic(Category category) {
        if (StringUtil.isNotBlank(category.getHref())) {
            if (!category.getHref().contains("://")) {
                return context.getContextPath()+CoreSval.getFrontPath()+category.getHref();
            }else{
                return category.getHref();
            }
        }
        StringBuilder str = new StringBuilder();
        str.append(context.getContextPath()).append(CoreSval.getFrontPath());
        str.append("/list-").append(category.getId()).append(CoreSval.getUrlSuffix());
        return str.toString();
    }

    /**
     * 从图片地址中去除ContextPath地址
   	 * @param src
   	 * @return src
   	 */
    public static String formatImageSrcToDb(String src) {
        if (StringUtil.isBlank(src)){ return src;}
        if (src.startsWith(context.getContextPath() + "/userfiles")) {
            return src.substring(context.getContextPath().length());
        }else{
            return src;
        }
    }

    /**
     * 从图片地址中加入ContextPath地址
   	 * @param src
   	 * @return src
   	 */
    public static String formatImageSrcToWeb(String src) {
        if (StringUtil.isBlank(src)) {return src;}
        if (src.startsWith(context.getContextPath() + "/userfiles")) {
            return src;
        }else{
            return context.getContextPath()+src;
        }
    }

    public static void addViewConfigAttribute(Model model, String param) {
        if (StringUtil.isNotBlank(param)) {
            @SuppressWarnings("rawtypes")
			Map map = JsonMapper.getInstance().fromJson(param, Map.class);
            if (map != null) {
                for(Object o : map.keySet()) {
                    model.addAttribute("viewConfig_"+o.toString(), map.get(o));
                }
            }
        }
    }

    public static void addViewConfigAttribute(Model model, Category category) {
        List<Category> categoryList = Lists.newArrayList();
        Category c = category;
        boolean goon = true;
        do{
            if (c.getParent() == null || c.getParent().isRoot()) {
                goon = false;
            }
            categoryList.add(c);
            c = c.getParent();
        }while(goon);
        Collections.reverse(categoryList);
        for(Category ca : categoryList) {
//        	addViewConfigAttribute(model, ca.getViewConfig());
        }
    }

	public static List<Category> getCategorysIndex() {
		List<Category> rcats = Lists.newArrayList();
		List<Category> cats = getCategorys(SysCacheKeys.SITE_CATEGORYS_INDEX_SENCOND.getKey(), "1");
		for (Category cur: cats) {
			if(!CoreUtils.checkCategory(cur.getId())){
				continue;
			}

			if(StringUtil.checkNotEmpty(cur.getChildList())){
				List<Category> subcats = Lists.newArrayList();
				for (Category csub: cur.getChildList()) {
					if(!CoreUtils.checkCategory(csub.getId())) {
						continue;
					}

					subcats.add(csub);
				}
				cur.setChildList(subcats);
			}
			rcats.add(cur);
		}
		return rcats;
	}

	public static String addCtxFront(String ctxFront, String href, Integer id) {
		if(href.contains("http")){
			return  href;
		}
		if(href.equals("categoryToArticle")){
			return href.equals("/") ? ctxFront : (ctxFront + "/" + href + "?id="+id);
		}
		return href.equals("/") ? ctxFront : ctxFront + '/' + href;
	}

	public static Boolean hasDSSH(String href) {
		User user = UserUtils.getUser();
		if (StringUtil.isEmpty(user.getTeacherType()) && StringUtil.isEmpty(user.getUserType())) {
			return !href.contains("getTeacherAuditTaskList");
		}
		return StringUtil.isNotEmpty(user.getTeacherType()) || !StringUtil.isNotEmpty(href) || user.getUserType().equals("2") || !href.contains("getTeacherAuditTaskList") && user.getUserType().equals("1");
	}

	public static List<CmsLink> getCmsLinks(){
		Site site=siteService.getAutoSite();
		 CmsLink cmsLink=new CmsLink();
		CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"linkType");
		List<CmsLink> cmsLinkList = cmsLinkService.findFrontList(cmsLink);
		for (CmsLink cmsLink1 : cmsLinkList) {
			String linkType = "2";
			if (cmsSiteconfig != null) {
				linkType = cmsSiteconfig.getLinkType();
			}
			cmsLink1.setSitetype(linkType);
		}
		if(StringUtil.checkEmpty(cmsLinkList)){
			return Lists.newArrayList();
		}
		return cmsLinkList;
	}

    /**
	 * 根据栏目根节点获取栏目列表
	 * @param parentId
	 * @return 取不到返回null
	 */
	public static List<Category> getCategorys( String key, String parentId) {
		SysCacheKeys ckey = SysCacheKeys.getByKey(key);
		if (ckey == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		List<Category> categorys = Lists.newArrayList();
//		禁用缓存
//		List<Category> categorys = (List<Category>) getCache(ckey.getKey()+"_"+parentId);
		if ((categorys ==  null) || (categorys.isEmpty())) {
			Map<String, List<Category>> categorysMap = categoryService.getCategoryTrees(parentId);
			if (categorysMap == null) {
				return null;
			}
			categorys = categorysMap.get(ckey.getKey());
//			CacheUtils.put(CMS_CACHE, ckey.getKey()+"_"+parentId, categorys);
		}
		return categorys;
	}


	public static Site curSite() {
		List<Site> sites = site();
		if (sites.size() == 1) {
			return site(Site.getCurrentSiteId(), null).get(0);
		}
		return null;
	}

	public static List<Site> site() {
		return site(Site.getCurrentSiteId(), null);
	}
	public static List<Site> siteByCategoryId(String categoryId) {
		if (StringUtil.isEmpty(categoryId)) {
			categoryId = CmsIds.SITE_CATEGORYS_TOP_INDEX.getId();
		}
		return site(Site.getCurrentSiteId(), categoryId, null, null);
	}
	/**
	 * 初始化网站栏目
	 * @param siteId
	 * @param categoryId
	 * @return 取不到返回null
	 */
	public static List<Site> site(String siteId, String categoryId) {
		if (StringUtil.isEmpty(categoryId)) {
			categoryId = CmsIds.SITE_CATEGORYS_TOP_INDEX.getId();
		}
		return site(siteId, categoryId, null, null);
	}

	/**
	 * 初始化网站栏目
	 * @param siteId
	 * @param categoryId
	 * @param regionId
	 * @return 取不到返回null
	 */
	public static List<Site> site(String siteId, String categoryId, String regionId) {
		return site(siteId, categoryId, regionId, null);
	}

	/**
	 * 初始化网站栏目区域及资源
	 * @param siteId
	 * @param categoryId
	 * @param regionId
	 * @param id
	 * @return 取不到返回null
	 */
	public static List<Site> site(String siteId, String categoryId, String regionId, String id) {
		List<Site> sites = Lists.newArrayList();
		Site site = siteService.get(siteId);
		if (site != null) {
			List<CmsIndexResource> cmsIndexResources = cmsIndexResourceService.findList(new CmsIndexResource(siteId, categoryId, regionId, id));
			site.getCmsSiteconfig().setCategorys(getSiteCategory(cmsIndexResources));
		}
		sites.add(site);
		return sites;
	}

	/**
	 * 根据资源获取站点栏目及栏目区域资源
	 * @param cmsIndexResources
	 * @return
	 */
	public static List<Category> getSiteCategory(List<CmsIndexResource> cmsIndexResources) {
		List<Category> categorys = Lists.newArrayList();
		for (CmsIndexResource cmsIndexResource : cmsIndexResources) {
			Category category = cmsIndexResource.getCmsIndexRegion().getCategory();

			if (!categorys.contains(category)) {
				categorys.add(category);
			}
		}

		for (Category category : categorys) {
			category.setChildRegionList(getSiteRegion(cmsIndexResources));
		}
		return categorys;
	}

	/**
	 * 根据资源获取站点区域及区域资源
	 * @param cmsIndexResources
	 * @return
	 */
	public static List<CmsIndexRegion> getSiteRegion(List<CmsIndexResource> cmsIndexResources) {
		List<CmsIndexRegion> regions = Lists.newArrayList();
		for (CmsIndexResource cmsIndexResource : cmsIndexResources) {
			CmsIndexRegion tempRegion =cmsIndexResource.getCmsIndexRegion();

			if (!regions.contains(tempRegion)) {
				regions.add(tempRegion);
			}
		}

		for (CmsIndexRegion region : regions) {
			List<CmsIndexResource> resources = Lists.newArrayList();
			for (CmsIndexResource cmsIndexResource : cmsIndexResources) {
				if (((region.getId()).equals(cmsIndexResource.getCmsIndexRegion().getId())) && (!resources.contains(cmsIndexResource))) {
					resources.add(cmsIndexResource);
				}
			}
			region.setChildResourceList(resources);
		}
		return regions;
	}


    public static List<Dict> getProCategoryByActywId(String actywId) {
        return categoryService.getProCategoryByActywId(actywId);
    }
}