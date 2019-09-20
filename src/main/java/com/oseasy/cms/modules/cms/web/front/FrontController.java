/**
 *
 */
package com.oseasy.cms.modules.cms.web.front;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.Article;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.CmsArticleData;
import com.oseasy.cms.modules.cms.entity.CmsIndex;
import com.oseasy.cms.modules.cms.entity.CmsLink;
import com.oseasy.cms.modules.cms.entity.CmsSiteconfig;
import com.oseasy.cms.modules.cms.entity.Comment;
import com.oseasy.cms.modules.cms.entity.Link;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.enums.CategoryModel;
import com.oseasy.cms.modules.cms.enums.CmsIndexManager;
import com.oseasy.cms.modules.cms.service.ArticleDataService;
import com.oseasy.cms.modules.cms.service.ArticleService;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.service.CmsArticleService;
import com.oseasy.cms.modules.cms.service.CmsIndexResourceService;
import com.oseasy.cms.modules.cms.service.CmsIndexService;
import com.oseasy.cms.modules.cms.service.CmsLinkService;
import com.oseasy.cms.modules.cms.service.CmsSiteconfigService;
import com.oseasy.cms.modules.cms.service.CommentService;
import com.oseasy.cms.modules.cms.service.LinkService;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.common.utils.sms.SMSUtilAlidayu;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.security.shiro.session.SessionDAO;
import com.oseasy.com.pcore.common.servlet.ValidateCodeServlet;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 网站Controller
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontController extends BaseController{
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleDataService articleDataService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private CmsSiteconfigService cmsSiteconfigService;
	@Autowired
	private CmsLinkService cmsLinkService;
	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	private CmsIndexResourceService cmsIndexResourceService;
	@Autowired
	private CmsIndexService cmsIndexService;
	@Autowired
	private CmsArticleService cmsArticleService;
	@Autowired
	private SysTenantService sysTenantService;


	// 展现方式为2：栏目第一条内容详情
	public static String MODEL_TWO = "2";
	@RequestMapping(value = "/loginUserId", method = RequestMethod.GET)
	@ResponseBody
	public String loginUserId() {
		return UserUtils.getUser().getId();
	}

	/*静态页面*/
	@RequestMapping(value = "page-{pageName}")
	public String viewPages(@PathVariable String pageName, Model model) {
	    return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "pages/" + pageName;
	}

	/*静态页面2*/
	@RequestMapping(value = "html-{pageName}")
	public String htmlviewPages(@PathVariable String pageName, Model model) {
		return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "html/" + pageName;
	}

	@RequestMapping(value = "help", method = RequestMethod.GET)
	public String help(HttpServletRequest request, HttpServletResponse response, Model model) {
	    return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "frontHelp";
	}

	@RequestMapping(value="resetNotifyShow")
	@ResponseBody
	public String resetNotifyShow(HttpServletRequest request, HttpServletResponse response) {
		if (request.getSession().getAttribute("notifyShow")!=null) {//登录成功后重置是否弹出消息
			request.getSession().removeAttribute("notifyShow");
		}
		return "1";
	}


	/**
	 * 查找bannerList
	 */
	@RequestMapping(value="cms/index/bannerList")
	@ResponseBody
	public ApiResult indexBannerList() {
		try{
			Site site=siteService.getAutoSite();
			CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndBanner(site.getId());
			if(cmsSiteconfig!=null){
				Map<String,Object> map= Maps.newHashMap();
				map.put("bannerList", cmsSiteconfig.getPicUrl());
				return ApiResult.success(map);
			}else{
				return ApiResult.failed(ApiConst.CODE_NULL_ERROR,ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR)+":"+"网站配置为空");
			}


		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 * 查找轮播图
	 */
	@RequestMapping(value="cms/index/indexDynamicImg")
	@ResponseBody
	public ApiResult indexDynamicImg() {
		try{
			Map<String,Object> map= Maps.newHashMap();
			Site site=siteService.getAutoSite();
			CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"dynamicImg");
			if(cmsSiteconfig!=null){
				map.put("dynamicImg", cmsSiteconfig.getPicUrl());
			}
			CmsSiteconfig cmsSiteconfigmsgImg=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"msgImg");
			if(cmsSiteconfig!=null){
				map.put("msgImg", cmsSiteconfigmsgImg.getPicUrl());
			}

			return ApiResult.success(map);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 * 查找indexLogo
	 */
	@RequestMapping(value="cms/index/indexLogo")
	@ResponseBody
	public ApiResult indexLogo() {
		try{
			Map<String,Object> map= Maps.newHashMap();
			Site site=siteService.getAutoSite();
			CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"logoLeft");
			if(cmsSiteconfig!=null){
				map.put("logoLeft", cmsSiteconfig.getPicUrl());
			}
			CmsSiteconfig cmsSiteconfigRight=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"logoRight");
			if(cmsSiteconfig!=null){
				map.put("logoRight", cmsSiteconfigRight.getPicUrl());
			}
			return ApiResult.success(map);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="cms/index/getBcrumbsName", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getBcrumbsName(String code){
		try {
			CmsIndex cmsIndex=cmsIndexService.getByEname(code);
			return ApiResult.success(cmsIndex);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "cms/index/cmsLinkList")
	@ResponseBody
	public ApiResult cmsLinkList() {
		try {
			CmsLink cmsLink=new CmsLink();
			List<CmsLink> cmsLinkList=cmsLinkService.findFrontList(cmsLink);
			Map<String,Object> map= Maps.newHashMap();
			map.put("cmsLinkList", cmsLinkList);

			Site site=siteService.getAutoSite();
			CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"linkType");
			if(cmsSiteconfig!=null){
				map.put("linkType",cmsSiteconfig.getLinkType());
			}else{
				map.put("linkType","1");
			}
			return ApiResult.success(map);
		}catch (Exception e){
			e.printStackTrace();
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	/**
	 * 大赛热点
	 */
	@RequestMapping(value="cms/index/gcontestShow")
	@ResponseBody
	public ApiResult indexGcontestShow(HttpServletRequest request,Model model, HttpServletResponse response) {
		//大赛热点
		try{
			Map<String,Object> map= Maps.newHashMap();
			cmsArticleService.getGcontestShow(map);
			CmsIndex cmsIndex=cmsIndexService.getByEname(CmsIndexManager.HOMEGCONTEST.getCode());
			map.put("title", cmsIndex.getModelname());
			map.put("ename", cmsIndex.getEname());
			return ApiResult.success(map);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
		大赛热点列表查询接口
	 */
	@RequestMapping(value="cms/index/homeGcontestList")
	@ResponseBody
	public ApiResult homeGcontestList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){
		cmsArticle.setModule("0000000277");
		try{
			return ApiResult.success(cmsArticleService.findPage(new Page<CmsArticle>(request, response),cmsArticle));
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}






	/**
	 * 登录成功，进入管理首页
	 */
	@RequestMapping
	public String index(Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		Site site=siteService.getAutoSite();
		if(site!=null){
			CoreUtils.putCache("siteId", String.valueOf(site.getId()));
		}
		User user = UserUtils.getUser();
		if (StringUtil.isNotEmpty(user.getName())) {
			model.addAttribute("user", user);
		}
		CmsLink cmsLink=new CmsLink();
		List<CmsLink> cmsLinkList=cmsLinkService.findList(cmsLink);
		Map<String,Object> map= Maps.newHashMap();
		map.put("cmsLinkList", cmsLinkList);
        model.addAttribute("cmsLinkList", cmsLinkList);
        String tenantId = TenantConfig.getCacheTenant();
		SysTenant sysTenant = sysTenantService.findByTenantId(tenantId);
		if(SysTenant.checkYKH(sysTenant)) {
			return CorePages.ERROR_NOTOPENTENANT.getIdxUrl();
		}
        if(tenantId.equals(CoreIds.NPR_SYS_TENANT.getId())){
			if(sysTenant == null){
				return CoreSval.REDIRECT + CorePages.ERROR_404;
			}
			String provinceFrontUrl = "http://"+sysTenant.getDomainName()+CoreSval.getProvinceFrontPath();
			//return CoreSval.REDIRECT+"http://p.com:8180/tf";
        	return CoreSval.REDIRECT+provinceFrontUrl;
		}
        return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "indexForTemplate";
	}

	@RequestMapping(value = "sendSmsCode", method = RequestMethod.GET)
	@ResponseBody
	public String sendSmsCode(HttpServletRequest request, HttpServletResponse response, Model model) {
		String p=request.getParameter("mobile");
    	String code=SMSUtilAlidayu.sendSms(p);
		if (code!=null) {
			request.getSession().setAttribute("server_sms_code", code);
			return "1";
		}else{
			return "0";
		}
	}


	@RequestMapping(value = "staticPage-{resid}")
	public String htmlResViewPages(@PathVariable String resid, Model model) {
		model.addAttribute("resource", cmsIndexResourceService.get(resid));
		return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "staticPage";
	}
	/*内容模板静态文件*/
	@RequestMapping(value = "cms/{template}/{pageName}")
	public String modelCms(@PathVariable String pageName, @PathVariable String template,Model model) {
		return "template/cms/"+template+"/"+pageName;
	}

	/**
	 * 网站首页
	 */
	@RequestMapping(value = "index-{siteId}${urlSuffix}")
	public String index(@PathVariable String siteId, Model model) {
		if (siteId.equals("1")) {
			return CoreSval.REDIRECT+CoreSval.getFrontPath();
		}
		Site site = CmsUtils.getSite(siteId);
		// 否则显示子站第一个栏目
		List<Category> mainNavList = CmsUtils.getMainNavList(siteId);
		if (mainNavList.size() > 0) {
			String firstCategoryId = CmsUtils.getMainNavList(siteId).get(0).getId();
			return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/list-"+firstCategoryId+CoreSval.getUrlSuffix();
		}else{
			model.addAttribute("site", site);
			return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+"/frontListCategory";
		}
	}

	/**
	 * 内容列表
	 */
	@RequestMapping(value = "list-{categoryId}${urlSuffix}")
	public String list(@PathVariable String categoryId, @RequestParam(required=false, defaultValue="1") Integer pageNo,
					   @RequestParam(required=false, defaultValue="15") Integer pageSize, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null) {
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
			return CorePages.ERROR_404.getIdxUrl();
		}
		Site site = siteService.get(category.getSite().getId());
		model.addAttribute("site", site);
		// 2：简介类栏目，栏目第一条内容
		if ("2".equals(category.getShowModes()) && "article".equals(category.getModule())) {
			// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
			List<Category> categoryList = Lists.newArrayList();
			if (category.getParent().getId().equals("1")) {
				categoryList.add(category);
			}else{
				categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
			}
			model.addAttribute("category", category);
			model.addAttribute("categoryList", categoryList);
			// 获取文章内容
			Page<Article> page = new Page<Article>(1, 1, -1);
			Article article = new Article(category);
			page = articleService.findPage(page, article, false);
			if (page.getList().size()>0) {
				article = page.getList().get(0);
				article.setArticleData(articleDataService.get(article.getId()));
				articleService.updateHitsAddOne(article.getId());
			}
			model.addAttribute("article", article);
			CmsUtils.addViewConfigAttribute(model, category);
			CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
			return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+"/"+getTpl(article);
		}else{
			List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
			// 展现方式为1 、无子栏目或公共模型，显示栏目内容列表
			if ("1".equals(category.getShowModes())||categoryList.size()==0) {
				// 有子栏目并展现方式为1，则获取第一个子栏目；无子栏目，则获取同级分类列表。
				if (categoryList.size()>0) {
					category = categoryList.get(0);
				}else{
					// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
					if (category.getParent().getId().equals("1")) {
						categoryList.add(category);
					}else{
						categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
					}
				}
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				// 获取内容列表
				if ("article".equals(category.getModule())) {
					Page<Article> page = new Page<Article>(pageNo, pageSize);
					//System.out.println(page.getPageNo());
					page = articleService.findPage(page, new Article(category), false);
					model.addAttribute("page", page);
					// 如果第一个子栏目为简介类栏目，则获取该栏目第一篇文章
					if ("2".equals(category.getShowModes())) {
						Article article = new Article(category);
						if (page.getList().size()>0) {
							article = page.getList().get(0);
							article.setArticleData(articleDataService.get(article.getId()));
							articleService.updateHitsAddOne(article.getId());
						}
						model.addAttribute("article", article);
						CmsUtils.addViewConfigAttribute(model, category);
						CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
						return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+"/"+getTpl(article);
					}
				}else if ("link".equals(category.getModule())) {
					Page<Link> page = new Page<Link>(1, -1);
					page = linkService.findPage(page, new Link(category), false);
					model.addAttribute("page", page);
				}
				String view = "/frontList";
				CmsUtils.addViewConfigAttribute(model, category);
				site =siteService.get(category.getSite().getId());
				return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+siteService.get(category.getSite().getId()).getCmsSiteconfig().getTheme()+view;
			}
			// 有子栏目：显示子栏目列表
			else{
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				String view = "/frontListCategory";
				CmsUtils.addViewConfigAttribute(model, category);
				return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+view;
			}
		}
	}

	/**
	 * 内容列表（通过url自定义视图）
	 */
	@RequestMapping(value = "listc-{categoryId}-{customView}${urlSuffix}")
	public String listCustom(@PathVariable String categoryId, @PathVariable String customView, @RequestParam(required=false, defaultValue="1") Integer pageNo,
							 @RequestParam(required=false, defaultValue="15") Integer pageSize, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null) {
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
            return CorePages.ERROR_404.getIdxUrl();
		}
		Site site = siteService.get(category.getSite().getId());
		model.addAttribute("site", site);
		List<Category> categoryList = categoryService.findByParentId(category.getId(), category.getSite().getId());
		model.addAttribute("category", category);
		model.addAttribute("categoryList", categoryList);
		CmsUtils.addViewConfigAttribute(model, category);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+"/frontListCategory"+customView;
	}

	/**
	 * 显示内容
	 */
	@RequestMapping(value = "view-{categoryId}-{contentId}${urlSuffix}")
	public String view(@PathVariable String categoryId, @PathVariable String contentId, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null) {
			Site site = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", site);
            return CorePages.ERROR_404.getIdxUrl();
		}
		model.addAttribute("site", category.getSite());
		if ("article".equals(category.getModule())) {
			// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
			List<Category> categoryList = Lists.newArrayList();
			if (category.getParent().getId().equals("1")) {
				categoryList.add(category);
			}else{
				categoryList = categoryService.findByParentId(category.getParent().getId(), category.getSite().getId());
			}
			// 获取文章内容
			Article article = articleService.get(contentId);
			if (article==null || !Article.DEL_FLAG_NORMAL.equals(article.getDelFlag())) {
				return CorePages.ERROR_404.getIdxUrl();
			}
			// 文章阅读次数+1
			articleService.updateHitsAddOne(contentId);
			// 获取推荐文章列表
			List<Object[]> relationList = articleService.findByIds(articleDataService.get(article.getId()).getRelation());
			// 将数据传递到视图
			model.addAttribute("category", categoryService.get(article.getCategory().getId()));
			model.addAttribute("categoryList", categoryList);
			article.setArticleData(articleDataService.get(article.getId()));
			model.addAttribute("article", article);
			model.addAttribute("relationList", relationList);
			CmsUtils.addViewConfigAttribute(model, article.getCategory());
			CmsUtils.addViewConfigAttribute(model, article.getViewConfig());
			Site site = siteService.get(category.getSite().getId());
			model.addAttribute("site", site);
			return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+"/"+getTpl(article);
		}
		return CorePages.ERROR_404.getIdxUrl();
	}

	/**
	 * 内容评论
	 */
	@RequestMapping(value = "comment", method=RequestMethod.GET)
	public String comment(String theme, Comment comment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Comment> page = new Page<Comment>(request, response);
		Comment c = new Comment();
		c.setCategory(comment.getCategory());
		c.setContentId(comment.getContentId());
		c.setDelFlag(Comment.DEL_FLAG_NORMAL);
		page = commentService.findPage(page, c);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+theme+"/frontComment";
	}

	/**
	 * 内容评论保存
	 */
	@ResponseBody
	@RequestMapping(value = "comment", method=RequestMethod.POST)
	public String commentSave(Comment comment, String validateCode,@RequestParam(required=false) String replyId, HttpServletRequest request) {
		if (StringUtil.isNotBlank(validateCode)) {
			if (ValidateCodeServlet.validate(request, validateCode)) {
				if (StringUtil.isNotBlank(replyId)) {
					Comment replyComment = commentService.get(replyId);
					if (replyComment != null) {
						comment.setContent("<div class=\"reply\">"+replyComment.getName()+":<br/>"
								+replyComment.getContent()+"</div>"+comment.getContent());
					}
				}
				comment.setIp(request.getRemoteAddr());
				comment.setCreateDate(new Date());
				comment.setDelFlag(Comment.DEL_FLAG_AUDIT);
				commentService.save(comment);
				return "{result:1, message:'提交成功。'}";
			}else{
				return "{result:2, message:'验证码不正确。'}";
			}
		}else{
			return "{result:2, message:'验证码不能为空。'}";
		}
	}

	/**
	 * 站点地图
	 */
	@RequestMapping(value = "map-{siteId}${urlSuffix}")
	public String map(@PathVariable String siteId, Model model) {
		Site site = CmsUtils.getSite(siteId!=null?siteId:Site.defaultSiteId());
		model.addAttribute("site", site);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+"/frontMap";
	}

	private String getTpl(Article article) {
		if (StringUtil.isBlank(article.getCustomContentView())) {
			String view = null;
			Category c = article.getCategory();
			boolean goon = true;
			do{
				if (c.getParent() == null || c.getParent().isRoot()) {
					goon = false;
				}else{
					c = c.getParent();
				}
			}while(goon);
			return StringUtil.isBlank(view) ? Article.DEFAULT_TEMPLATE : view;
		}else{
			return article.getCustomContentView();
		}
	}

	//首页布局接口
	@RequestMapping(value = "/cms/index/indexLayout")
	@ResponseBody
	public ApiResult indexLayout(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<CmsIndex> indexList = cmsIndexService.findIndexList();
			return ApiResult.success(indexList);
		}catch (Exception e){
			e.printStackTrace();
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//首页布局接口
	@RequestMapping(value = "/cms/index/indexInLayout")
	@ResponseBody
	public ApiResult indexInLayout(HttpServletRequest request, HttpServletResponse response) {
		try {

			List<CmsIndex> indexList = cmsIndexService.findInIndexList();
			return ApiResult.success(indexList);
		}catch (Exception e){
			e.printStackTrace();
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	/**
	 首页栏目接口
	 */
	@RequestMapping(value = "cmsCategoryList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult cmsCategoryList(Category cmsCategory, HttpServletRequest request, HttpServletResponse response) {
		Page pages =new Page<Category>(request, response);
		pages.setPageSize(10000);
		cmsCategory.setIsShow(1);
		Page<Category> page = categoryService.findPage(pages, cmsCategory);
		try {
			return ApiResult.success(page);

		} catch (Exception e) {
			logger.error(e.getMessage());

			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
		}
	}

	@RequestMapping(value="getArticleList", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult getArticleList(@RequestBody Category category, HttpServletRequest request, HttpServletResponse response){
		try {
			// 获取内容列表
			CmsArticle article = new CmsArticle(category);
			article.setPublishStatus("1");
			article.setNowDate(DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG+" "+DateUtil.FMT_HMS000));
			Page<CmsArticle> page = cmsArticleService.frontArticleListPage(new Page<CmsArticle>(request, response), article);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//前台一般内容接口
	@RequestMapping(value = "categoryToArticle")
	public String cmsArticleList(String id, HttpServletRequest request, HttpServletResponse response,Model model) {
		String categoryId = id;
		//当前点击的栏目
		Category category = categoryService.get(categoryId);
		if (category == null) {
			Site sites = CmsUtils.getSite(Site.defaultSiteId());
			model.addAttribute("site", sites);
			return CorePages.ERROR_404.getIdxUrl();
		}
		Site site = siteService.getAutoSite();
		model.addAttribute("site", site);
		//判断是否是表单链接,链接类型：0000000278
		if(CategoryModel.LINKMODELPARAM.equals(category.getModule())){
			return category.getHref();
		}
		// 展现方式为2：栏目第一条内容详情

		if (MODEL_TWO.equals(category.getShowModes())) {
			//有子栏目，显示第一个子栏目的第一条数据
			CmsArticle article = new CmsArticle(category);
			// 获取文章内容
			article.setPublishStatus("1");
			List<CmsArticle> firstCmsArticleList = cmsArticleService.findList(article);
			CmsArticle firstCmsArticle = new CmsArticle();
			if(firstCmsArticleList.size() > 0){
				firstCmsArticle = firstCmsArticleList.get(0);
			}
			// 文章阅读次数+1
			cmsArticleService.updateHitsAddOne(firstCmsArticle.getId());
			//获取相关推荐
			List<CmsArticle> cmsArticleAbout = cmsArticleService.getCmsArticleAboutList(firstCmsArticle);
			model.addAttribute("cmsArticleAbout", cmsArticleAbout);
			model.addAttribute("article", firstCmsArticle);
			//根据站点主题跳转
			//return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/"+site.getCmsSiteconfig().getTheme()+"/"+getTpl(article);
			return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/basic/frontViewArticle";
		} else {
			// 展现方式为1 ：栏目内容列表
			List<Category> categoryList = categoryService.findByParentId(categoryId, category.getSite().getId());
			model.addAttribute("category", category);
			model.addAttribute("categoryList", categoryList);
			// 获取内容列表
			CmsArticle article = new CmsArticle(category);
			article.setPublishStatus("1");
			article.setNowDate(DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG+" "+DateUtil.FMT_HMS000));
			//Page<CmsArticle> page = cmsArticleService.findNormalContentPage(new Page<CmsArticle>(request, response), article);
			Page<CmsArticle> page = cmsArticleService.frontArticleListPage(new Page<CmsArticle>(request, response), article);
			model.addAttribute("page", page);
			//图文：0000000272，普通：0000000273
			if ("0000000272".equals(category.getContenttype())) {
				return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/basic/noticeImgList";
			} else {
				return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/basic/noticeList";
			}

		}
	}



	/**
	 * 文章点赞接口
	 */
	@RequestMapping(value="cmsArticleLikes")
	@ResponseBody
	public ApiResult  cmsArticleLikes(CmsArticleData cmsArticleData){
		try{
			cmsArticleService.updateArticleLikes(cmsArticleData);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

}
