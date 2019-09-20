package com.oseasy.scr.modules.sco.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.service.ScoAffirmService;
import com.oseasy.scr.modules.sco.vo.ScoProjectVo;

/**
 * Created by zhangzheng on 2017/7/19.
 * 获取学生的 创新学分 创业学分 素质学分 列表
 */
@Controller
@RequestMapping(value = "${frontPath}/myscore")
public class ScoProjectFrontController extends BaseController {
    @Autowired
    ScoAffirmService scoAffirmService;

    //创新学分认定页面
    @RequestMapping(value = "innovationList")
    public String innovationList(HttpServletRequest request, HttpServletResponse response, Model model) {
        ScoProjectVo scoProjectVo = new ScoProjectVo();
        scoProjectVo.setUserId(UserUtils.getUser().getId());
        Page<ScoProjectVo> page = scoAffirmService.findInnovationPage(new Page<ScoProjectVo>(request, response), scoProjectVo);
        //设置学分认定标准  认定日期
//        for(ScoProjectVo projectVo:page.getList()) {
//            ScoAffirm scoAffirmForSeacher = new ScoAffirm();
//            scoAffirmForSeacher.setProId(projectVo.getProjectDeclare().getId());
//            ScoAffirm scoAffirm = scoAffirmService.findProjectScore(scoAffirmForSeacher);
//            projectVo.setScoAffirm(scoAffirm);
//        }
        model.addAttribute("page",page);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "myScore/innovationList";

    }
    //创业学分认定页面
    @RequestMapping(value = "businessList")
    public String businessList(HttpServletRequest request, HttpServletResponse response, Model model) {
        ScoProjectVo scoProjectVo = new ScoProjectVo();
        scoProjectVo.setUserId(UserUtils.getUser().getId());
        Page<ScoProjectVo> page = scoAffirmService.findBusinessPage(new Page<ScoProjectVo>(request, response), scoProjectVo);
//        //设置学分认定标准  认定日期
//        for(ScoProjectVo projectVo:page.getList()) {
//            ScoAffirm scoAffirmForSeacher = new ScoAffirm();
//            scoAffirmForSeacher.setProId(projectVo.getProjectDeclare().getId());
//            ScoAffirm scoAffirm = scoAffirmService.findProjectScore(scoAffirmForSeacher);
//            projectVo.setScoAffirm(scoAffirm);
//        }
        model.addAttribute("page",page);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "myScore/businessList";
    }

    //素质学分认定页面
    @RequestMapping(value = "qualityList")
    public String qualityList(HttpServletRequest request, HttpServletResponse response, Model model) {
        ScoProjectVo scoProjectVo = new ScoProjectVo();
        scoProjectVo.setUserId(UserUtils.getUser().getId());
        Page<ScoProjectVo> page = scoAffirmService.findQualityList(new Page<ScoProjectVo>(request, response), scoProjectVo);
//        //设置学分认定标准  认定日期
//        for(ScoProjectVo projectVo:page.getList()) {
//            ScoAffirm scoAffirmForSeacher = new ScoAffirm();
//            scoAffirmForSeacher.setProId(projectVo.getGContest().getId());
//            ScoAffirm scoAffirm = scoAffirmService.findProjectScore(scoAffirmForSeacher);
//            projectVo.setScoAffirm(scoAffirm);
//        }
        model.addAttribute("page",page);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "myScore/qualityList";
    }
}
