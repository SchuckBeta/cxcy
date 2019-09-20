/**
 *
 */
package com.oseasy.cms.modules.cms.web;

import static com.oseasy.com.mqserver.modules.oa.entity.OaNotify.Type_Enum.TYPE3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.auy.modules.cms.service.CmsBackTeacherExpansionService;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.CmsConmment;
import com.oseasy.cms.modules.cms.entity.CmsIndex;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.enums.CategoryModel;
import com.oseasy.cms.modules.cms.enums.CmsIndexManager;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.service.CmsArticleService;
import com.oseasy.cms.modules.cms.service.CmsConmmentService;
import com.oseasy.cms.modules.cms.service.CmsIndexService;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifySent;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.UserVo;
import com.oseasy.pro.modules.course.entity.Course;
import com.oseasy.pro.modules.course.service.CourseService;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.service.GContestService;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProBackTeacherExpansionService;
import com.oseasy.pro.modules.promodel.service.ProCmsArticleService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.SysOaNotifyService;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 网站Controller
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontAuyController extends BaseController{
	@Autowired
    private ProBackTeacherExpansionService proBackTeacherExpansionService;
    @Autowired
    ExcellentShowService excellentShowService;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private GContestService gContestService;
    @Autowired
    private SysOaNotifyService sysOaNotifyService;
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private TeacherKeywordService teacherKeywordService;
    @Autowired
    private UserService userService;
    @Autowired
    private CmsBackTeacherExpansionService cmsBackTeacherExpansionService;
    @Autowired
    private CmsIndexService cmsIndexService;
    @Autowired
    private CmsArticleService cmsArticleService;
    @Autowired
    private ProCmsArticleService proCmsArticleService;
    @Autowired
    private CmsConmmentService cmsConmmentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseService courseService;

    /**
     * 查找名师讲堂
     */
    @RequestMapping(value="cms/index/courseList")
    @ResponseBody
    public ApiResult indexCourseList(HttpServletRequest request,Model model, HttpServletResponse response) {
        //查找名师讲堂展示
        try{
            List<Course> courseList=courseService.findFrontCourse();
            Map<String,Object> map= Maps.newHashMap();
            map.put("courseList", courseList);

            CmsIndex cmsIndex=cmsIndexService.getByEname(CmsIndexManager.HOMECOURSE.getCode());
            map.put("title", cmsIndex.getModelname());
            map.put("ename", cmsIndex.getEname());
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequestMapping(value="cms/index/teacherDetail")
    @ResponseBody
    public ApiResult teacherDetail(String id, HttpServletRequest request, HttpServletResponse response){
        try{
            HashMap<String, Object> hashMap = new HashMap<>();
            BackTeacherExpansion backTeacherExpansion=backTeacherExpansionService.get(id);
            List<ProjectExpVo> projectExpVo = proBackTeacherExpansionService.findProjectByTeacherId(backTeacherExpansion.getUser().getId());//查询项目经历
            List<GContestUndergo> gContest = proBackTeacherExpansionService.findGContestByTeacherId(backTeacherExpansion.getUser().getId()); //查询大赛经历
            hashMap.put("backTeacherExpansion",backTeacherExpansion);
            hashMap.put("projectExpVo",projectExpVo);
            hashMap.put("gContest",gContest);
            return ApiResult.success(hashMap);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 优秀项目页面.
     **/
    @RequestMapping(value = "pageProject")
    public String pageProject(ExcellentShow excellentShow, HttpServletRequest request,HttpServletResponse response, Model model) {
//
//      Map<String,Object> param = Maps.newHashMap();
//      if (StringUtil.isNotEmpty(excellentShow.getType())) {
//          param.put("type", excellentShow.getType());
//          model.addAttribute("type", excellentShow.getType());
//      }
//      if (StringUtil.isNotEmpty(excellentShow.getContent())) {
//          param.put("key",excellentShow.getContent());
//          model.addAttribute("key", excellentShow.getContent());
//      }
//      Page<Map<String,String>> page = excellentShowService.findAllProjectShow(new Page<Map<String,String>>(request, response), param);
//
//      model.addAttribute("page", page);
//      model.addAttribute("excellentShow", excellentShow);
        CmsIndex cmsIndex = cmsIndexService.getByEname(CmsIndexManager.HOMEPROJECT.getCode());
        model.addAttribute("cmsProject", cmsIndex);
        return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "pages/pageProject";
    }

    @RequestMapping(value="toRegister")
    public String toRegister(HttpServletRequest request, HttpServletResponse response) {
        if((SysConfigUtil.getSysConfigVo() == null) || (SysConfigUtil.getSysConfigVo().getRegisterConf() == null) || (SysConfigUtil.getSysConfigVo().getRegisterConf().getTeacherRegister() == null)){
            logger.warn("注册配置属性不能为空(SysConfigUtil.sysConfigVo.registerConf.teacherRegister)");
            request.setAttribute("teacherRegister", "0");//默认只支持学生注册
        }else{
            request.setAttribute("teacherRegister", SysConfigUtil.getSysConfigVo().getRegisterConf().getTeacherRegister());
        }
        request.setAttribute("loginPage", "1");
        return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "studentregister";
    }


    /**
     * 导师
     */
    @RequestMapping(value="cms/index/teacherList")
    @ResponseBody
    public ApiResult teacherList(HttpServletRequest request,Model model, HttpServletResponse response) {
        try{
            int firstNum=3;
            if(StringUtil.isNotEmpty(request.getParameter("firstNum"))){
                firstNum=Integer.valueOf(request.getParameter("firstNum"));
            }

            int siteNum=5;
            if(StringUtil.isNotEmpty(request.getParameter("siteNum"))){
                siteNum=Integer.valueOf(request.getParameter("siteNum"));
            }
            Map<String,Object> map= Maps.newHashMap();
            //查找栏目导师
            BackTeacherExpansion btef =new BackTeacherExpansion();
            btef.setFirstShow("1");
            List<BackTeacherExpansion> firstTeachers=backTeacherExpansionService.findIndexTeacherList(btef);
            //List<BackTeacherExpansion> firstExperts=backTeacherExpansionService.findExpertList(btef);
            List<BackTeacherExpansion> firstTeacherList = Lists.newArrayList();
            firstTeacherList.addAll(firstTeachers);
            //firstTeacherList.addAll(firstExperts);
            List<BackTeacherExpansion> firstTeacherListnew =new ArrayList<BackTeacherExpansion> ();
            //首页只展示3个
            if (firstTeacherList!=null) {
                if (firstTeacherList.size()>firstNum) {
                    for(int i=0;i<firstNum;i++) {
                        BackTeacherExpansion b=firstTeacherList.get(i);
                        b.setKeywords(backTeacherExpansionService.getKeys(b.getId()));
                        firstTeacherListnew.add(b);
                    }
                }else{
                    for(int i=0;i<firstTeacherList.size();i++) {
                        BackTeacherExpansion b=firstTeacherList.get(i);
                        b.setKeywords(backTeacherExpansionService.getKeys(b.getId()));
                        firstTeacherListnew.add(b);
                    }
                }
                map.put("firstTeacherList", firstTeacherListnew);
            }
            //查找首页导师
            BackTeacherExpansion btes =new BackTeacherExpansion();
            btes.setSiteShow("1");
            List<BackTeacherExpansion> siteTeachers=backTeacherExpansionService.findIndexTeacherList(btes);
            //List<BackTeacherExpansion> siteExperts=backTeacherExpansionService.findExpertList(btes);
            List<BackTeacherExpansion> siteTeacherList = Lists.newArrayList();
            siteTeacherList.addAll(siteTeachers);
            //siteTeacherList.addAll(siteExperts);
            List<BackTeacherExpansion> siteTeacherListnew =new ArrayList<BackTeacherExpansion>();
            //栏位只展示4个
            if (siteTeacherList!=null) {
                if (siteTeacherList.size() > siteNum) {
                    for (int i = 0; i < siteNum; i++) {
                        if (siteTeacherList.get(i).getId() != null) {
                            List<String> tes = teacherKeywordService.getStringKeywordByTeacherid(siteTeacherList.get(i).getId());
                            if (tes.size() > 0) {
                                siteTeacherList.get(i).setKeywords(tes);
                            }
                        }
                        siteTeacherListnew.add(siteTeacherList.get(i));
                    }
                } else {
                    for (int i = 0; i < siteTeacherList.size(); i++) {
                        if (siteTeacherList.get(i).getId() != null) {
                            List<String> tes = teacherKeywordService.getStringKeywordByTeacherid(siteTeacherList.get(i).getId());
                            if (tes.size() > 0) {
                                siteTeacherList.get(i).setKeywords(tes);
                            }
                        }
                        siteTeacherListnew.add(siteTeacherList.get(i));
                    }
                }
                map.put("siteTeacherList", siteTeacherListnew);
            }
            CmsIndex cmsIndex = cmsIndexService.getByEname(CmsIndexManager.HOMETEACHER.getCode());
            map.put("title", cmsIndex.getModelname());
            map.put("ename", cmsIndex.getEname());
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 查找通知公告
     */
    @RequestMapping(value="cms/index/noticeIndex")
    @ResponseBody
    public ApiResult indexNoticeIndex(HttpServletRequest request,Model model, HttpServletResponse response) {
        //查找通知公告
        try{
            OaNotify oaNotify=new OaNotify();
            oaNotify.setSendType(OaNotifySendType.DIS_DIRECRIONAL.getVal());
            oaNotify.setType(TYPE3.getValue());
            oaNotify.setStatus("1");
            List<OaNotify> oaNotifyList= sysOaNotifyService.findList(oaNotify);
            Map<String,Object> map= Maps.newHashMap();
            map.put("noticeList", oaNotifyList);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /*导师风采页面*/
    @RequestMapping(value = "pageTeacher")
    public String pageTeacher( BackTeacherExpansion backTeacherExpansion,HttpServletRequest request,HttpServletResponse response, Model model) {
        String teacherType=request.getParameter("teacherType");
//      if (StringUtil.isEmpty(teacherType)) {
//          teacherType="1";
//      }
//      backTeacherExpansion.setTeachertype(teacherType);
//      Page<BackTeacherExpansion> page = backTeacherExpansionService.findTeacherPage(new Page<BackTeacherExpansion>(request, response),backTeacherExpansion);
//      BackTeacherExpansion backTeacherExpansionNew =backTeacherExpansionService.findTeacherByTopShow(teacherType);
//      if (backTeacherExpansionNew!=null) {
//          model.addAttribute("backTeacherExpansionNew", backTeacherExpansionNew);
//      }
//      model.addAttribute("page", page);
        CmsIndex cmsIndex = cmsIndexService.getByEname(CmsIndexManager.HOMETEACHER.getCode());
        model.addAttribute("cmsTeacher", cmsIndex);
        model.addAttribute("teacherType", teacherType);
        return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "pages/pageTeacher";
    }

    /**
     * 导师风采数据
     * teacherType 为空的时候 查询所有的
     * teacherType 为1的时候查询校内导师
     * teacherType 为2的时候查询企业导师
     * teacherType 为3的时候查询专家评委
     */
    @RequestMapping(value="cms/index/teacherPageList")
    @ResponseBody
    public ApiResult teacherPageList(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response){
        try{
            return ApiResult.success(cmsBackTeacherExpansionService.findTeacherPage(new Page<BackTeacherExpansion>(request, response),backTeacherExpansion));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="cms/index/provinceTeacherPageList")
    @ResponseBody
    public ApiResult provinceTeacherPageList(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response){
        try{
            return ApiResult.success(cmsBackTeacherExpansionService.findProvinceTeacherPage(new Page<BackTeacherExpansion>(request, response),backTeacherExpansion));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //页面未读通知消息
    @RequestMapping(value="unReadOaNotify")
    @ResponseBody
    public List<OaNotifySent> unReadOaNotify(OaNotify oaNotify) {
        return sysOaNotifyService.unRead(oaNotify);

    }
    //关闭操作
    @RequestMapping(value="closeButton")
    @ResponseBody
    public String closeButton(HttpServletRequest request) {
        String oaNotifyId = request.getParameter("send_id");
        OaNotify oaNotify = sysOaNotifyService.get(oaNotifyId);
        sysOaNotifyService.updateReadFlag(oaNotify);
        return "1";
    }
    //完善信息
    @RequestMapping(value="infoPerfect")
    public String infoPerfect(HttpServletRequest request,Model model) {
        String userType=request.getParameter("userType");
        model.addAttribute("userType", userType);
        if ("2".equals(userType)) {
            model.addAttribute("teaEid", backTeacherExpansionService.getByUserId(UserUtils.getUser().getId()).getId());
        }
        return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "infoPerfect";
    }


    /**
     根据文章id获取单条文章
     */
    @RequestMapping(value="getOneCmsArticle")
    public String getCmsArticleDetail(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response,Model model){
        if(StringUtil.isEmpty(cmsArticle.getId())){
            return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/basic/frontViewArticle";
        }
        cmsArticle = cmsArticleService.get(cmsArticle);
        if (cmsArticle == null) {
            Site site = CmsUtils.getSite(Site.defaultSiteId());
            model.addAttribute("site", site);
            return CorePages.ERROR_404.getIdxUrl();
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
        model.addAttribute("keywordsList",keywordsList);
        model.addAttribute("article",cmsArticle);
        CmsConmment  cmsConmment = new CmsConmment();
        cmsConmment.setCnt(cmsArticle);
        Integer commentCounts = cmsConmmentService.getArticleCommentCounts(cmsConmment);
        model.addAttribute("commentCounts",commentCounts);
        model.addAttribute("cmsArticleAbout",cmsArticleAbout);

        //优秀项目或大赛获奖作品则跳转到该详情页面,0000000276,1:优秀项目，7：大赛
        if(CategoryModel.PROJECTMODELPARAM.equals(cmsArticle.getModule()) && cmsArticle.getPrType().equals("1")){
//          ProModel promodel=proModelService.get(cmsArticle.getPrId());
            if(StringUtil.isNotEmpty(cmsArticle.getPrId())){
                ProModel promodel=proModelService.getExcellentById(cmsArticle.getPrId());
                ProjectDeclare projectDeclare=projectDeclareService.getExcellentById(cmsArticle.getPrId());
                if(promodel != null ){
                    proCmsArticleService.addPromodelCmsArticle(cmsArticle,promodel);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"2");
                    model.addAttribute("stuList",stuList);
                    model.addAttribute("teaList",teaList);
                }else if(projectDeclare!=null){
                    proCmsArticleService.addProjectDeclareCmsArticle(cmsArticle,projectDeclare);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(projectDeclare.getId(),projectDeclare.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(projectDeclare.getId(),projectDeclare.getTeamId(),"2");
                    model.addAttribute("stuList",stuList);
                    model.addAttribute("teaList",teaList);
                }
            }
            model.addAttribute("article",cmsArticle);
            return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/basic/excProView";

        }else if(CategoryModel.PROJECTMODELPARAM.equals(cmsArticle.getModule()) && cmsArticle.getPrType().equals("7")){
            if(StringUtil.isNotEmpty(cmsArticle.getPrId())){
                ProModel promodel=proModelService.getGcontestExcellentById(cmsArticle.getPrId());
                GContest gContest=gContestService.getExcellentById(cmsArticle.getPrId());
                if(promodel!=null){
                    proCmsArticleService.addPromodelCmsArticle(cmsArticle,promodel);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(promodel.getId(),promodel.getTeamId(),"2");
                    model.addAttribute("stuList",stuList);
                    model.addAttribute("teaList",teaList);
                }else if(gContest!=null){
                    proCmsArticleService.addGContestCmsArticle(cmsArticle,gContest);
                    List<UserVo> stuList=userService.getUserByPorIdAndTeamId(gContest.getId(),gContest.getTeamId(),"1");
                    List<UserVo> teaList=userService.getUserByPorIdAndTeamId(gContest.getId(),gContest.getTeamId(),"2");
                    model.addAttribute("stuList",stuList);
                    model.addAttribute("teaList",teaList);
                }
            }
            model.addAttribute("article",cmsArticle);
            return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/basic/excProView";
        }

        return CmsSval.path.vms(CmsEmskey.CMS.k()) + "front/themes/basic/frontViewArticle";
    }


    /**
     * 查找首页优秀项目展示
     */
    @RequestMapping(value="cms/index/projectList")
    @ResponseBody
    //查找优秀项目展示
    public ApiResult projectList(HttpServletRequest request,Model model, HttpServletResponse response) {
        try{
            Map<String,Object> map=proCmsArticleService.findIndexProjectList();
            CmsIndex cmsIndex=cmsIndexService.getByEname(CmsIndexManager.HOMEPROJECT.getCode());
            map.put("title", cmsIndex.getModelname());
            map.put("ename", cmsIndex.getEname());
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    /**
     * 查找二级优秀项目展示
     */
    @RequestMapping(value="cms/index/projectSecondList")
    @ResponseBody
    //查找优秀项目展示
    public ApiResult projectSecondList(CmsArticle cmsArticle,HttpServletRequest request,Model model, HttpServletResponse response) {
        try{
            Page page =proCmsArticleService.findSecondProjectList(new Page<CmsArticle>(request, response),cmsArticle);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
    获奖列表查询接口
     */
    @RequestMapping(value="cms/index/excellent/gcontestList")
    @ResponseBody
    public ApiResult gcontestList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){

        try{
            cmsArticle.setPublishStatus("1");
            cmsArticle.setNowDate(DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG+" "+DateUtil.FMT_HMS000));
            return ApiResult.success(proCmsArticleService.findGcontestFrontPage(new Page<CmsArticle>(request, response),cmsArticle));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
    /**
        优秀项目列表查询接口
     */
    @RequestMapping(value="cms/index/excellent/projectList")
    @ResponseBody
    public ApiResult projectList(CmsArticle cmsArticle, HttpServletRequest request, HttpServletResponse response){

        try{
            //查询发布的项目
            cmsArticle.setPublishStatus("1");
            cmsArticle.setNowDate(DateUtil.getDate(DateUtil.FMT_YYYYMMDD_ZG+" "+DateUtil.FMT_HMS000));
            Page<CmsArticle> page = proCmsArticleService.findProjectFrontPage(new Page<CmsArticle>(request, response), cmsArticle);

            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
