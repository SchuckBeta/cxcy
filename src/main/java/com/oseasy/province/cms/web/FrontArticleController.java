package com.oseasy.province.cms.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.act.utils.ThreadUtils;
import com.oseasy.auy.modules.cms.service.CmsOaNotifyService;
import com.oseasy.cms.common.config.CmsIds;
import com.oseasy.cms.common.config.SysCacheKeys;
import com.oseasy.cms.modules.cms.entity.*;
import com.oseasy.cms.modules.cms.enums.CategoryModel;
import com.oseasy.cms.modules.cms.service.*;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyKeywordService;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.UserVo;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.service.GContestService;
import com.oseasy.pro.modules.interactive.service.SysViewsService;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProCmsArticleService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.UrlUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by PW on 2019/4/22.
 */
@Controller
@RequestMapping(value = "${frontPath}/cms/index")
public class FrontArticleController extends BaseController {

    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private GContestService gContestService;
    @Autowired
    private UserService userService;
    @Autowired
    private CmsArticleService cmsArticleService;
    @Autowired
    private ProCmsArticleService proCmsArticleService;
    @Autowired
    private CmsConmmentService cmsConmmentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private CmsSiteconfigService cmsSiteconfigService;
    @Autowired
    private CmsLinkService cmsLinkService;
    @Autowired
    private OaNotifyService oaNotifyService;
    @Autowired
    OaNotifyKeywordService oaNotifyKeywordService;
    @Autowired
    CmsOaNotifyService cmsOaNotifyService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeacherKeywordService teacherKeywordService;
    @Autowired
    private SysViewsService sysViewsService;
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private CmsProvinceProjectService cmsProvinceProjectService;
    @Autowired
    private TeamUserRelationService teamUserRelationService;
    @Autowired
    private SysTenantService tenantService;
    //前台一般内容接口
    @RequestMapping(value = "getArticle", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult getArticle(String id, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String categoryId = id;
        //当前点击的栏目
        Category category = categoryService.get(categoryId);
        if (category == null) {
            return ApiResult.failed(ApiConst.CMS_CATEGORY_NULL_ERROR.getCode());
        }

        Site site=siteService.getAutoSite();

        hashMap.put("category", category);
        hashMap.put("site",site);
        //判断是否是表单链接,如果是则直接跳转链接,链接类型：0000000278
        if(CategoryModel.LINKMODELPARAM.equals(category.getModule())){
            hashMap.put("href", category.getHref());
            return ApiResult.success(hashMap);
        }
        // 展现方式为2：栏目第一条内容详情
        if ("2".equals(category.getShowModes())) {
            //有子栏目，显示第一个子栏目的第一条数据
            CmsArticle article = new CmsArticle(category);
            // 获取文章内容
            List<CmsArticle> firstCmsArticleList = cmsArticleService.findList(article);
            CmsArticle firstCmsArticle = new CmsArticle();
            if(StringUtil.checkNotEmpty(firstCmsArticleList)){
                firstCmsArticle = firstCmsArticleList.get(0);
            }else{
                return ApiResult.success(firstCmsArticle);
            }
            // 文章阅读次数+1
            cmsArticleService.updateHitsAddOne(firstCmsArticle.getId());
            //获取相关推荐
            List<CmsArticle> cmsArticleAbout = cmsArticleService.getCmsArticleAboutList(firstCmsArticle);
            hashMap.put("cmsArticleAbout", cmsArticleAbout);
            hashMap.put("article", firstCmsArticle);
        } else {
            // 展现方式为1 ：栏目内容列表
            List<Category> categoryList = categoryService.findByParentId(categoryId, category.getSite().getId());
            hashMap.put("categoryList", categoryList);
            // 获取内容列表
            CmsArticle article = new CmsArticle(category);
            article.setPublishStatus("1");
            article.setNowDate(DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG+" "+DateUtil.FMT_HMS000));
            Page<CmsArticle> page = cmsArticleService.frontArticleListPage(new Page<CmsArticle>(request, response), article);
            hashMap.put("page", page);
            //判断是单个文章还是文章列表
            hashMap.put("hasPageList", true);
            //图文：0000000272，普通：0000000273
            hashMap.put("isTextList", "0000000272".equals(category.getContenttype()));
        }
        return ApiResult.success(hashMap);
    }

    /**
     根据文章id获取单条文章
     */
    @RequestMapping(value="getOneCmsArticleDetail", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult getCmsArticleDetail(CmsArticle cmsArticle){
        cmsArticle = cmsArticleService.get(cmsArticle);
        HashMap<String, Object> hashMap = new HashMap<>();
        if (cmsArticle == null) {
            Site site = CmsUtils.getSite(Site.defaultSiteId());
            hashMap.put("site", site);
            return ApiResult.failed(ApiConst.CMS_ARTICLE_NULL_ERROR.getCode());
        }
        // 文章阅读次数+1
        cmsArticleService.updateHitsAddOne(cmsArticle.getId());
        //获取相关推荐
        List<CmsArticle> cmsArticleAbout = cmsArticleService.getCmsArticleAboutList(cmsArticle);
        List<String> keywordsList = Lists.newArrayList();
        if(cmsArticle.getKeywords() != null){
            String[] keywords = cmsArticle.getKeywords().split(",");
            for(String keyword : keywords){
                keywordsList.add(keyword);
            }
        }
        cmsArticle.setCmsCategory(categoryService.get(cmsArticle.getCategoryId()));
        hashMap.put("keywordsList",keywordsList);
        hashMap.put("article",cmsArticle);
        CmsConmment cmsConmment = new CmsConmment();
        cmsConmment.setCnt(cmsArticle);
        Integer commentCounts = cmsConmmentService.getArticleCommentCounts(cmsConmment);
        hashMap.put("commentCounts",commentCounts);
        hashMap.put("cmsArticleAbout",cmsArticleAbout);
        //优秀项目或大赛获奖作品则跳转到该详情页面,0000000276,1:优秀项目，7：大赛
        if(CategoryModel.PROJECTMODELPARAM.equals(cmsArticle.getModule()) && cmsArticle.getPrType().equals("1")){
            if(StringUtil.isNotEmpty(cmsArticle.getPrId())){
                ProModel promodel=proModelService.getExcellentById(cmsArticle.getPrId());
                ProjectDeclare projectDeclare=projectDeclareService.getExcellentById(cmsArticle.getPrId());
                if(promodel != null ){
                    proCmsArticleService.addPromodelCmsArticle(cmsArticle,promodel);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"2");
                    hashMap.put("stuList",stuList);
                    hashMap.put("teaList",teaList);
                }else if(projectDeclare!=null){
                    proCmsArticleService.addProjectDeclareCmsArticle(cmsArticle,projectDeclare);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(projectDeclare.getId(),projectDeclare.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(projectDeclare.getId(),projectDeclare.getTeamId(),"2");
                    hashMap.put("stuList",stuList);
                    hashMap.put("teaList",teaList);
                }
            }
            hashMap.put("article",cmsArticle);
            return ApiResult.success(hashMap);
        }else if(CategoryModel.PROJECTMODELPARAM.equals(cmsArticle.getModule()) && cmsArticle.getPrType().equals("7")){
            if(StringUtil.isNotEmpty(cmsArticle.getPrId())){
                ProModel promodel=proModelService.getGcontestExcellentById(cmsArticle.getPrId());
                GContest gContest=gContestService.getExcellentById(cmsArticle.getPrId());
                if(promodel!=null){
                    proCmsArticleService.addPromodelCmsArticle(cmsArticle,promodel);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"2");
                    hashMap.put("stuList",stuList);
                    hashMap.put("teaList",teaList);
                }else if(gContest!=null){
                    proCmsArticleService.addGContestCmsArticle(cmsArticle,gContest);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(gContest.getId(),gContest.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(gContest.getId(),gContest.getTeamId(),"2");
                    hashMap.put("stuList",stuList);
                    hashMap.put("teaList",teaList);
                }
            }
            hashMap.put("article",cmsArticle);
            return ApiResult.success(hashMap);
        }
        return ApiResult.success(hashMap);
    }

    /**
     首页双创赛事列表
     */
    @RequestMapping(value="matchArticleList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult matchArticleList(){
        try{
            Category category = categoryService.get(CmsIds.CATEGORY_SCMATCH_ID.getId());
            CmsArticle article = new CmsArticle(category);
            // 获取文章内容
            if(CoreSval.getTenantIsopen()) {
                String sysTenant = TenantConfig.getCacheTenant();
                if(sysTenant != null){
                    article.setTenantId(sysTenant);
                }
            }else{
                article.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
            }
            article.setPublishStatus("1");
            List<CmsArticle> articleList = cmsArticleService.findList(article);
            List<CmsArticle> matchArticleList = Lists.newArrayList();
            int j =6;
            if(j > articleList.size()){
                j= articleList.size();
            }
            for(int i=0; i < j;i++){
                matchArticleList.add(articleList.get(i));
            }
            return ApiResult.success(matchArticleList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode());
        }

    }

    /**
     友情链接
     */
    @RequestMapping(value="npCmsLinkList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult  getCmsLinks(){
        try{
            CmsLink cmsLink=new CmsLink();
            if(CoreSval.getTenantIsopen()) {
                cmsLink.setTenantId(TenantConfig.getCacheTenant());
            }else{
                cmsLink.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
            }
            Site site=siteService.getAutoSite();

            CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"linkType");
            List<CmsLink> cmsLinkList = cmsLinkService.findFrontList(cmsLink);
            for (CmsLink cmsLink1 : cmsLinkList) {
                //友情链接展现形式
                String linkType = CoreSval.Const.STEP_TWO;
                if (cmsSiteconfig != null) {
                    linkType = cmsSiteconfig.getLinkType();
                }
                cmsLink1.setSitetype(linkType);
            }
            return ApiResult.success(cmsLinkList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode());
        }
    }

    /**
     * 首页栏目列表
     */
    @RequestMapping(value="npCmsCategoryList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult  getCategorysIndex() {
        try{
            List<Category> categorys = Lists.newArrayList();
            SysCacheKeys ckey = SysCacheKeys.getByKey(SysCacheKeys.SITE_CATEGORYS_INDEX_SENCOND.getKey());
            if (ckey == null) {
                return ApiResult.success(categorys);
            }
            if ((categorys ==  null) || (categorys.isEmpty())) {
                Map<String, List<Category>> categorysMap = categoryService.getCategoryTrees("1");
                if (categorysMap == null) {
                    return null;
                }
                categorys = categorysMap.get(ckey.getKey());
            }
            return ApiResult.success(categorys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode());
        }
    }

    /**
     * 查看通知
     */
    @RequestMapping(value="noticeView", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult noticeView(String id) {
        HashMap map = new HashMap();
        //根据id得到notice对象
        if (StringUtil.isNotBlank(id)) {
            OaNotify oaNotify = oaNotifyService.get(id);
            if(oaNotify==null){
                return ApiResult.success(map);
            }
            String title = oaNotify.getTitle();
            oaNotify.setContent(OaUtils.convertFront(oaNotify.getContent()));
            String content=StringUtil.unescapeHtml3(oaNotify.getContent());
            content = content.replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL);
            map.put("title",title);
            map.put("content",content);
            map.put("oaNotify", oaNotify);
        }
        return ApiResult.success(map);
    }

    /**
     * 通知列表
     */
    @RequestMapping(value="noticeList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult noticeList( HttpServletRequest request, HttpServletResponse response) {
        try{
            //查询通知列表
            Page<OaNotify> pageForSearch =new Page<OaNotify>(request, response);
            OaNotify notify = getOaNotify(request);
            Page<OaNotify> page =  oaNotifyService.findLoginPage(pageForSearch,notify);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode());
        }
    }

    private OaNotify getOaNotify(HttpServletRequest request) {
        OaNotify notify = new OaNotify();
        if(CoreSval.getTenantIsopen()) {
            SysTenant sysTenant = CoreUtils.getTenant(UrlUtil.wwwPort(request));
            if(sysTenant != null){
                notify.setTenantId(sysTenant.getTenantId());
            }
        }else{
            notify.setTenantId(CoreSval.Const.DEFAULT_SCHOOL_TENANTID);
        }
        return notify;
    }

    /**
     * 查看双创动态、双创通知、省市动态
     */
    @RequestMapping(value="viewDynamic", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult viewDynamic(OaNotify oaNotify, HttpServletRequest request) {
        try {
            List list = Lists.newArrayList();
            if (oaNotify != null) {
                oaNotify = oaNotifyService.get(oaNotify.getId());
                if (StringUtil.isEmpty(oaNotify.getViews())) {
                    oaNotify.setViews("0");
                }
                if (StringUtil.isNotEmpty(oaNotify.getId())) {
                    oaNotify.setKeywords(oaNotifyKeywordService.findListByEsid(oaNotify.getId()));
                }
                if (StringUtil.isNotEmpty(oaNotify.getContent())) {
                    oaNotify.setContent(OaUtils.convertFront(oaNotify.getContent()));
                    oaNotify.setContent(StringEscapeUtils.unescapeHtml4(oaNotify.getContent()));
                }
                if (oaNotify != null && StringUtil.isNotEmpty(oaNotify.getContent())) {
                    oaNotify.setContent(oaNotify.getContent().replaceAll(FtpUtil.FTP_MARKER, FtpUtil.FTP_HTTPURL));
                }
                if (StringUtil.isNotEmpty(oaNotify.getId())) {
                    list= cmsOaNotifyService.getMore(oaNotify.getType(), oaNotify.getId(), oaNotify.getKeywords());
                }
                if (StringUtil.isNotEmpty(oaNotify.getId())) {
                    SysViewsService.updateViews(oaNotify.getId(), request, CacheUtils.DYNAMIC_VIEWS_QUEUE);
                }
            }
            HashMap map = new HashMap();
            map.put("oaNotify",oaNotify);
            map.put("more",list);
            return ApiResult.success(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode());
        }
    }


    /**
     * 查看导师详情
     */
    @RequestMapping(value="frontTeacherExpansionView", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult view(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request) {
        try {
            HashMap map = new HashMap();
            backTeacherExpansion = backTeacherExpansionService.get(backTeacherExpansion.getId());
            map.put("backTeacherExpansion", backTeacherExpansion);
            map.put("cuser", backTeacherExpansion.getUser().getId());
            if (StringUtil.isEmpty(backTeacherExpansion.getUser().getViews())) {
                backTeacherExpansion.getUser().setViews("0");
            }
            if (StringUtil.isEmpty(backTeacherExpansion.getUser().getLikes())) {
                backTeacherExpansion.getUser().setLikes("0");
            }
            List <String> tes=teacherKeywordService.getStringKeywordByTeacherid(backTeacherExpansion.getId());
            if (tes.size()>0) {
                backTeacherExpansion.setKeywords(tes);
            }

            String teaId = backTeacherExpansion.getUser().getId();
            String userId = UserUtils.getUser().getId();
            String mobile=backTeacherExpansion.getUser().getMobile();
            String email=backTeacherExpansion.getUser().getEmail();
            if (!teamService.findTeamByUserId(userId,teaId) && mobile!=null) {
                mobile=mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            }
            if(!teamService.findTeamByUserId(userId,teaId) && email!=null){
                email=email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4") ;
            }
            map.put("mobile", mobile);
            map.put("email", email);
            /*记录浏览量*/
            User user= UserUtils.getUser();
            if (user!=null&&StringUtil.isNotEmpty(user.getId())&&!user.getId().equals(backTeacherExpansion.getUser().getId())) {
                SysViewsService.updateViews(backTeacherExpansion.getUser().getId(), request,CacheUtils.USER_VIEWS_QUEUE);
            }
            /*记录浏览量*/
            /*查询谁看过它*/
            map.put("visitors", sysViewsService.getVisitors(backTeacherExpansion.getUser().getId()));
            String isNewJSP=request.getParameter("isNewJSP");
            if(StringUtil.isNotEmpty(isNewJSP) && CoreSval.Const.YES.equals(isNewJSP)){
                isNewJSP = CoreSval.Const.YES;
            }else{
                isNewJSP = CoreSval.Const.NO;
            }
            map.put("isNewJSP", isNewJSP);
            return ApiResult.success(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode());
        }
    }


    /**
     * 查找省厅背景和头部文字
     */
    @RequestMapping(value="backgroundurl", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult indexLogo(Site site) {
        try{
            Map<String,Object> map= Maps.newHashMap();
            site=siteService.getAutoSite();
            CmsSiteconfig cmsSiteconfig=cmsSiteconfigService.getBySiteIdAndType(site.getId(),"backgroundUrl");
            if(cmsSiteconfig!=null){
                map.put("backgroundUrl", cmsSiteconfig.getPicUrl());
            }
            List<CmsSiteconfig> cmsSiteconfigs=cmsSiteconfigService.getBySiteId(site.getId());
            if(StringUtil.checkNotEmpty(cmsSiteconfigs)){
                map.put("headtext", cmsSiteconfigs.get(0).getHeadText());
            }
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 当前站点
     */
    @RequestMapping(value="getNPSite", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult getNPSite(){
        try{
            return ApiResult.success(siteService.getAutoSite());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 站点基础配置
     */
    @RequestMapping(value="getSiteSysConfig", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult getSiteSysConfig(){
        try{
            HashMap map = new HashMap();
            map.put("webMaxUpFileSize",1000); // 1000
            map.put("ftpHttp",FtpUtil.FTP_HTTPURL);// ftp地址：http://192.168.0.132:82
            map.put("frontOrAdmin",CoreSval.getFrontPath()); // 前后台根目录：/f
            map.put("casHasLogin", CoreSval.Const.FALSE); // false
            String tenantConfig = TenantConfig.getCacheTenant();
            SysTenant tenant = sysTenantService.get(tenantConfig);
            map.put("hrefToBack", tenant.getDomainName()+"/a");
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="cmsProjectList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult cmsProjectList(CmsProvinceProject cmsProvinceProject, HttpServletRequest request, HttpServletResponse response){
        try{
            Page<CmsProvinceProject> page = cmsProvinceProjectService.findPage(new Page<CmsProvinceProject>(request, response),cmsProvinceProject);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }


    @RequestMapping(value="ajaxGetProvinceProject", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetProvinceProject(String id){
        try{
            CmsProvinceProject cmsProvinceProject = cmsProvinceProjectService.get(id);
            Map<String,Object> map=  new HashMap<>();
            List<Map<String, String>> stuList = null;
            List<Map<String, String>> teaList = null;
            if(null != cmsProvinceProject){
                CompletableFuture<List<Map<String, String>>> studentFuture = CompletableFuture.supplyAsync(() -> teamUserRelationService.findTeamStudentTeamId(cmsProvinceProject.getTeamId()), ThreadUtils.newFixedThreadPool());
                CompletableFuture<List<Map<String, String>>> teacherFuture = CompletableFuture.supplyAsync(() -> teamUserRelationService.findTeamTeacherTeamId(cmsProvinceProject.getTeamId()),ThreadUtils.newFixedThreadPool());
                CompletableFuture.allOf(studentFuture,teacherFuture).join();
                try {
                    stuList = studentFuture.get();
                    teaList = teacherFuture.get();
                } catch (InterruptedException e) {
                    logger.info("查询线程中断,ajaxFindTeamPerson");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    logger.info("执行异常,ajaxFindTeamPerson");
                    e.printStackTrace();
                }finally {
                    ThreadUtils.shutdown();
                }
            }
            map.put("cmsProvinceProject",cmsProvinceProject);
            map.put("stuList",stuList);
            map.put("teaList",teaList);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    //查询条件--学校（租户）
    @RequestMapping(value="schoolList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult schoolList(SysTenant sysTenant){
        try{
            //todo 根据登录的角色，过滤查询的租户id
            List<SysTenant> list = null;
            User curuser = UserUtils.getUser();
            String curpn = CoreSval.getTenantCurrpn();
            sysTenant.setStatus(CoreSval.Const.YES);
            if((Sval.EmPn.NCENTER.getPrefix()).equals(curpn)){
                sysTenant.setFilterIds(CoreIds.filterChangeTenantByNc(curuser));
                if(User.isSuper(curuser)){
                    list = tenantService.findListNtTpl(sysTenant);
                }else{
                    sysTenant.setIsTpl(CoreSval.Const.NO);
                    list = tenantService.findListNtTpl(sysTenant);
                }
//                sysTenant.setIsTpl(CoreSval.Const.NO);
//                list = tenantService.findListNtTpl(sysTenant);
            }
            if((Sval.EmPn.NPROVINCE.getPrefix()).equals(curpn)){
                sysTenant.setFilterIds(CoreIds.filterTenantByNsAdmin());
                sysTenant.setStatus("1");
                list = tenantService.findListNtTpl(sysTenant);
            }

            if(list == null){
                list = Lists.newArrayList();
            }
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }


}
