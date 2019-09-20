package com.oseasy.scr.modules.sco.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.service.ScoAffirmService;
import com.oseasy.scr.modules.sco.service.ScoAuditingService;
import com.oseasy.scr.modules.sco.service.ScoCourseService;
import com.oseasy.scr.modules.sco.vo.ScoCourseVo;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;

/**
 * Created by zy on 2017/7/20.
 * 获取学生的 创新学分 创业学分 素质学分 列表
 */
@Controller
@RequestMapping(value = "${adminPath}/sco/scoreGrade")
public class ScoGradeController extends BaseController {
    @Autowired
    ScoAffirmService scoAffirmService;
    @Autowired
    ScoCourseService scoCourseService;
    @Autowired
    StudentExpansionService studentExpansionService;
    @Autowired
    SysAttachmentService sysAttachmentService;
    @Autowired
    ScoAuditingService scoAuditingService;

//
//    //创新学分认定页面
//    @RequestMapping(value = "createList")
//    public String createList(ScoProjectVo scoProjectVo,HttpServletRequest request, HttpServletResponse response, Model model) {
//        Page<ScoProjectVo> page = scoAffirmService.findScoProjectCreateVoPage(new Page<ScoProjectVo>(request, response), scoProjectVo);
//
//        model.addAttribute("scoProjectVo",scoProjectVo);
//        model.addAttribute("page",page);
//        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoCreateGradeList";
//    }



//    //创新学分详情页面
//    @RequestMapping(value = "createView")
//    public String CreateView(HttpServletRequest request, HttpServletResponse response, Model model) {
//        String id= request.getParameter("id");
//        if (id != null) {
//            List<ScoProjectVo> scoProjectVo =scoAffirmService.getScoGradeCreate(id);
//            if (scoProjectVo.size()>0) {
//                model.addAttribute("projectName",scoProjectVo.get(0).getProjectDeclare().getName());
//            }
//            model.addAttribute("scoProjectVoList",scoProjectVo);
//        }
//        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoCreateGradeView";
//    }
////    //创业学分认定页面
//    @RequestMapping(value = "startList")
//    public String startList(ScoProjectVo scoProjectVo,HttpServletRequest request, HttpServletResponse response, Model model) {
//        Page<ScoProjectVo> page = scoAffirmService.findScoProjectStartVoPage(new Page<ScoProjectVo>(request, response), scoProjectVo);
//
//        model.addAttribute("scoProjectVo",scoProjectVo);
//        model.addAttribute("page",page);
//        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoStartGradeList";
//    }
//
//    //创新学分详情页面
//    @RequestMapping(value = "startView")
//    public String startView(HttpServletRequest request, HttpServletResponse response, Model model) {
//        String id= request.getParameter("id");
//        if (id!=null) {
//            List<ScoProjectVo> scoProjectVo =scoAffirmService.getScoGradeStart(id);
//            if (scoProjectVo.size()>0) {
//                model.addAttribute("projectName",scoProjectVo.get(0).getProjectDeclare().getName());
//            }
//            model.addAttribute("scoProjectVoList",scoProjectVo);
//        }
//        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoStartGradeView";
//    }

//    //素质学分认定页面
//    @RequestMapping(value = "qualityList")
//    public String qualityList(ScoProjectVo scoProjectVo,HttpServletRequest request, HttpServletResponse response, Model model) {
//        Page<ScoProjectVo> page = scoAffirmService.findScoGontestVoPage(new Page<ScoProjectVo>(request, response), scoProjectVo);
//
//        model.addAttribute("scoProjectVo",scoProjectVo);
//        model.addAttribute("page",page);
//        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoQualityGradeList";
//    }
//
//    //素质学分认定页面
//    @RequestMapping(value = "QualityView")
//    public String QualityView(HttpServletRequest request, HttpServletResponse response, Model model) {
//        String id= request.getParameter("id");
//        if (id!=null) {
//            List<ScoProjectVo> scoProjectVo =scoAffirmService.getScoGradeQuality(id);
//            if (scoProjectVo.size()>0) {
//                model.addAttribute("projectName",scoProjectVo.get(0).getGContest().getpName());
//            }
//            model.addAttribute("scoProjectVoList",scoProjectVo);
//        }
//        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoQualityGradeView";
//    }
//

    //课程学分认定页面

    @RequestMapping(value = "courseList")
    public String courseList(ScoCourseVo scoCourseVo,HttpServletRequest request, HttpServletResponse response, Model model) {
//        Page<ScoCourseVo> page =  scoCourseService.findScoCourseVoPage(new Page<ScoCourseVo>(request, response), scoCourseVo);
//
//        model.addAttribute("scoCourseVo",scoCourseVo);
//        model.addAttribute("page",page);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoCourseGradeList";
    }

    @RequestMapping(value="getScoCourseGradeList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getScoCourseGradeList(ScoCourseVo scoCourseVo,HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            Page<ScoCourseVo> page =  scoCourseService.findScoCourseVoPage(new Page<ScoCourseVo>(request, response), scoCourseVo);
            return ApiResult.success(page);
        }catch (Exception e){
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //课程学分查询页面
    @RequestMapping(value = "courseQueryList")
    public String courseQueryList() {
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/courseQueryList";
    }
}
