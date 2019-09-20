package com.oseasy.pro.modules.web;

import com.google.common.collect.Lists;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.cms.modules.cms.entity.CmsIndex;
import com.oseasy.cms.modules.cms.enums.CmsIndexManager;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.service.CmsArticleService;
import com.oseasy.cms.modules.cms.service.CmsIndexService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.pro.modules.course.entity.Course;
import com.oseasy.pro.modules.course.entity.CourseCategory;
import com.oseasy.pro.modules.course.entity.CourseTeacher;
import com.oseasy.pro.modules.course.service.CourseCategoryService;
import com.oseasy.pro.modules.course.service.CourseService;
import com.oseasy.pro.modules.course.service.CourseTeacherService;
import com.oseasy.pro.modules.course.vo.CourseRes;
import com.oseasy.pro.modules.course.vo.SentuHttpType;
import com.oseasy.pro.modules.interactive.service.SysViewsService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.sys.modules.sys.service.SysConfigService;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.SentuxueyuanConfig;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangzheng on 2017/6/29.
 */
@Controller
@RequestMapping(value = "${frontPath}/course")
public class CourseFrontController extends BaseController {

    @Autowired
    private FtpService ftpService;
    @Autowired
    private CourseService courseService;
    @Autowired
	CourseCategoryService courseCategoryService;
    @Autowired
	CourseTeacherService courseTeacherService;
    @Autowired
    SysAttachmentService sysAttachmentService;
    @Autowired
    CmsIndexService cmsIndexService;
    @Autowired
    private CmsArticleService cmsArticleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SysConfigService sysConfigService;

    @ModelAttribute
    public Course get(@RequestParam(required=false) String id) {
        Course entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = courseService.get(id);
        }
        if (entity == null) {
            entity = new Course();
        }
        return entity;
    }

    @RequestMapping(value = "view")
    public String view(Course course, Model model, HttpServletRequest request) {
        //更改浏览量
/*        course.setViews(course.getViews()+1);
        courseService.updateViews(course);*/
		/*更改浏览量 */
        SysViewsService.updateViews(course.getId(),request,CacheUtils.COURSE_VIEWS_QUEUE);
      //查找课程专业分类 授课老师
        if (StringUtil.isNotBlank(course.getId())) {
            List<CourseCategory> categoryList = courseCategoryService.getByCourseId(course.getId());  //查找课程专业分类
            course.setCategoryList(categoryList);
            List<CourseTeacher> teacherList =  courseTeacherService.getByCourseId(course.getId());  //查找授课老师
            course.setTeacherList(teacherList);
            //查找课件
            SysAttachment sa=new SysAttachment();
            sa.setUid(course.getId());
            sa.setType(FileTypeEnum.S9);
            sa.setFileStep(FileStepEnum.S900);
            List<SysAttachment> attachmentList =  sysAttachmentService.getFiles(sa);
            course.setAttachmentList(attachmentList);
        }

        model.addAttribute("course", course);
        CmsArticle article = cmsArticleService.get(course.getCmsArtileId());
        if(null == article){
            article = new CmsArticle();
        }
        article.setCmsCategory(categoryService.get(article.getCategoryId()));
        model.addAttribute("article", article);
        //查找推荐课程  专业分类,状态分类相关的 按置顶，发布时间倒序排序
        List<Course> courseList =courseService.findRecommedList(course) ;
        CmsIndex cmsIndex = cmsIndexService.getByEname(CmsIndexManager.HOMECOURSE.getCode());
        model.addAttribute("courseList", courseList);
        model.addAttribute("cmsCourse", cmsIndex);
        return ProSval.path.vms(ProEmskey.COURSE.k()) + "front/courseView";
    }

    @RequestMapping(value = "resourse")
    public String frontCourseResourse(HttpServletRequest request, Model model) {
        if(UserUtils.checkToLogin()){
            return CoreSval.LOGIN_REDIRECT;
        }

        SentuxueyuanConfig config = SysConfigUtil.getSysConfigVo().getSentuxueyuan();
        if(config == null){
            model.addAttribute(CoreJkey.JK_MSG, "配置不存在.");
            return CorePages.ERROR_MSG.getIdxUrl();
        }

        CourseRes cres = new CourseRes(config, request);
        String url = CourseRes.gen(SentuHttpType.GET, cres, UserUtils.getUser());
        if(StringUtil.isEmpty(url)){
            return CorePages.ERROR_404.getIdxUrl();
        }

        System.out.println("--------------------------------------------------分隔符 结束--------------");
        return CoreSval.REDIRECT + url;
//        try {
//            CourseRes.doPost(url, CourseRes.genPostParam(cres));
////            CourseRes.doPost2(url, CourseRes.genPostPmap(cres));
//        } catch (Exception e) {
////            e.printStackTrace();
//            model.addAttribute(CoreJkey.JK_MSG, e.getMessage());
//            System.out.println("--------------------------------------------------分隔符 结束--------------");
////            return SysIdx.ERROR_MSG.getIdxUrl();
//        }
//        System.out.println("--------------------------------------------------分隔符 结束--------------");
//        return CoreSval.REDIRECT + CourseRes.HTTP_WEB;
    }

    @RequestMapping(value = "frontCourseList")
    public String frontCourseList(Course course, HttpServletRequest request, HttpServletResponse response, Model model) {
//        Page pageForSearche=new Page<Course>(request, response);
//        pageForSearche.setPageSize(6);
//        Page<Course> page = courseService.findFrontPage(pageForSearche, course);
//        List<CourseTeacher>  allTeachers = Lists.newArrayList();
//        //专业课程分类处理
//        for(Course courseItem:page.getList()) {
//            List<CourseCategory>  categoryList =courseCategoryService.getByCourseId(courseItem.getId());
//            courseItem.setCategoryList(categoryList);
//            List<CourseTeacher> teacherList =  courseTeacherService.getByCourseId(courseItem.getId());  //查找授课老师
//            courseItem.setTeacherList(teacherList);
//            if (teacherList!=null && teacherList.size()>0) {
//                for (CourseTeacher teacher: teacherList) {
//                  if (!allTeachers.contains(teacher)&&allTeachers.size()<6) {
//                      allTeachers.add(teacher);
//                  }
//                }
//            }
//
//            //查找课件
//            SysAttachment sa=new SysAttachment();
//            sa.setUid(courseItem.getId());
//            sa.setType(FileTypeEnum.S9);
//            sa.setFileStep(FileStepEnum.S900);
//            List<SysAttachment> attachmentList =  sysAttachmentService.getFiles(sa);
//            courseItem.setAttachmentList(attachmentList);
//        }
//
//        model.addAttribute("page", page);
//        model.addAttribute("allTeachers", allTeachers);
        CmsIndex cmsIndex = cmsIndexService.getByEname(CmsIndexManager.HOMECOURSE.getCode());
        model.addAttribute("cmsCourse", cmsIndex);

        return ProSval.path.vms(ProEmskey.COURSE.k()) + "front/frontCourseList";
    }

    @RequestMapping(value="getCourseList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getCourseList(Course course, HttpServletRequest request, HttpServletResponse response){
        try {
            Page pageForSearche=new Page<Course>(request, response);
            pageForSearche.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
            Page<Course> page = courseService.findFrontPage(pageForSearche, course);
            List<CourseTeacher>  allTeachers = Lists.newArrayList();
            //专业课程分类处理
            for(Course courseItem:page.getList()) {
                List<CourseCategory>  categoryList =courseCategoryService.getByCourseId(courseItem.getId());
                courseItem.setCategoryList(categoryList);
                List<CourseTeacher> teacherList =  courseTeacherService.getByCourseId(courseItem.getId());  //查找授课老师
                courseItem.setTeacherList(teacherList);
                if (teacherList!=null && teacherList.size()>0) {
                    for (CourseTeacher teacher: teacherList) {
                        if (!allTeachers.contains(teacher)&&allTeachers.size()<6) {
                            allTeachers.add(teacher);
                        }
                    }
                }

                //查找课件
                SysAttachment sa=new SysAttachment();
                sa.setUid(courseItem.getId());
                sa.setType(FileTypeEnum.S9);
                sa.setFileStep(FileStepEnum.S900);
                List<SysAttachment> attachmentList =  sysAttachmentService.getFiles(sa);
                courseItem.setAttachmentList(attachmentList);
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("page", page);
            hashMap.put("allTeachers", allTeachers);
            return ApiResult.success(hashMap);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="getTeacherList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getTeacherList(String ids){
        try {
            String[] idList = ids.split(",");
            ArrayList<User> arrayList = new ArrayList<>();
            for (String id : idList) {
                User user = UserUtils.get(id);
                if (user != null) {
                    arrayList.add(user);
                }
            }
            return ApiResult.success(arrayList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 前台更改下载次数，下载课件
     * @param course
     * @param response
     */
    @RequestMapping(value = "downLoad")
    public void downLoad(Course course, String fileName, String url, HttpServletResponse response)
            throws ServletException, IOException {
        //更改下载次数
        course.setDownloads(course.getDownloads()+1);
        courseService.updateDownloads(course);
        //下载课件
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data;charset=UTF-8");
        fileName= URLDecoder.decode(fileName,"UTF-8");
        ftpService.downloadUrlFile(url,fileName,response);
    }

}
