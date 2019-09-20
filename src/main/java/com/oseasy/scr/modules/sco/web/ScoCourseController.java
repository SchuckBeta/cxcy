package com.oseasy.scr.modules.sco.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.entity.ScoApply;
import com.oseasy.scr.modules.sco.entity.ScoCourse;
import com.oseasy.scr.modules.sco.service.ScoApplyService;
import com.oseasy.scr.modules.sco.service.ScoCourseService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 课程Controller.
 *
 * @author 张正
 * @version 2017-07-13
 */
@Controller
@RequestMapping(value = "${adminPath}/sco/scoCourse")
public class ScoCourseController extends BaseController {

    @Autowired
    private ScoCourseService scoCourseService;
    @Autowired
    private ScoApplyService scoApplyService;

    @ModelAttribute
    public ScoCourse get(@RequestParam(required = false) String id) {
        ScoCourse entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = scoCourseService.get(id);
        }
        if (entity == null) {
            entity = new ScoCourse();
        }
        return entity;
    }

    @RequiresPermissions("scocourse:scoCourse:view")
    @RequestMapping(value = {"list", ""})
    public String list(ScoCourse scoCourse, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<ScoCourse> page = scoCourseService.findPage(new Page<ScoCourse>(request, response), scoCourse);
//		model.addAttribute("page", page);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoCourseList";
    }

    @RequiresPermissions("scocourse:scoCourse:view")
    @RequestMapping(value = "getScoCourseList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getScoCourseList(ScoCourse scoCourse, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<ScoCourse> page = scoCourseService.findPage(new Page<ScoCourse>(request, response), scoCourse);
            return ApiResult.success(page);
        } catch (Exception e) {
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg() + ":" + e.getMessage());
        }
    }

    @RequiresPermissions("scocourse:scoCourse:view")
    @RequestMapping(value = "saveScoCourse", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult updateScoCourse(@RequestBody ScoCourse scoCourse, Model model) {
        try {
            if (!beanValidator(model, scoCourse)) {
                return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg() + ":" + model);
            }
            scoCourseService.save(scoCourse);
            return ApiResult.success(scoCourse);
        } catch (Exception e) {
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg() + ":" + e.getMessage());
        }
    }

    @RequiresPermissions("scocourse:scoCourse:view")
    @RequestMapping(value = "form")
    public String form(ScoCourse scoCourse, Model model) {
        model.addAttribute("scoCourse", scoCourse);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoCourseForm";
    }

    @RequiresPermissions("scocourse:scoCourse:view")
    @RequestMapping(value = "view")
    public String view(ScoCourse scoCourse, Model model) {
        model.addAttribute("scoCourse", scoCourse);
        return ScrSval.path.vms(ScrEmskey.SCO.k()) + "grade/scoCourseGradeView";
    }

    @RequiresPermissions("scocourse:scoCourse:edit")
    @RequestMapping(value = "save")
    public String save(ScoCourse scoCourse, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, scoCourse)) {
            return form(scoCourse, model);
        }
        scoCourseService.save(scoCourse);
        addMessage(redirectAttributes, "保存课程成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sco/scoCourse/?repage";
    }

    @RequiresPermissions("scocourse:scoCourse:edit")
    @RequestMapping(value = "delete")
    public String delete(ScoCourse scoCourse, RedirectAttributes redirectAttributes) {
        ScoApply scoApply = new ScoApply();
        scoApply.setCourseId(scoCourse.getId());
        if (!scoApplyService.findList(scoApply).isEmpty()) {
            addMessage(redirectAttributes, "删除失败，该课程已被选择认定");
        } else {
            scoCourseService.delete(scoCourse);
            addMessage(redirectAttributes, "删除课程成功");
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/sco/scoCourse/?repage";
    }

    @RequestMapping(value = "delScoCourse", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult delScoCourse(ScoCourse scoCourse) {
        try {
            ScoApply scoApply = new ScoApply();
            scoApply.setCourseId(scoCourse.getId());
            scoApply.setAuditStatus("0");
            if (!scoApplyService.findList(scoApply).isEmpty()) {
                return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), "删除失败，该课程已被选择认定");
            }
            scoCourseService.delete(scoCourse);
            return ApiResult.success();
        } catch (Exception e) {
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg() + ":" + e.getMessage());
        }
    }


    @RequestMapping(value = "checkCode")
    @ResponseBody
    public boolean checkCode(String id, String code) {
        if (scoCourseService.checkCode(id, code) > 0) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "checkName")
    @ResponseBody
    public boolean checkName(String id, String name) {
        if (scoCourseService.checkName(id, name) > 0) {
            return false;
        }
        return true;
    }
}