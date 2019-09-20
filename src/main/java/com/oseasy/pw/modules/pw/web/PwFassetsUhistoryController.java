package com.oseasy.pw.modules.pw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwFassetsUhistory;
import com.oseasy.pw.modules.pw.service.PwFassetsUhistoryService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 固定资产使用记录Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwFassetsUhistory")
public class PwFassetsUhistoryController extends BaseController {

    @Autowired
    private PwFassetsUhistoryService pwFassetsUhistoryService;

    @ModelAttribute
    public PwFassetsUhistory get(@RequestParam(required = false) String id) {
        PwFassetsUhistory entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = pwFassetsUhistoryService.get(id);
        }
        if (entity == null) {
            entity = new PwFassetsUhistory();
        }
        return entity;
    }

    @RequiresPermissions("pw:pwFassetsUhistory:view")
    @RequestMapping(value = {"list", ""})
    public String list() {
        //Page<PwFassetsUhistory> page = pwFassetsUhistoryService.findPage(new Page<PwFassetsUhistory>(request, response), pwFassetsUhistory);
        //model.addAttribute("page", page);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFassetsUhistoryList";
    }

    @RequestMapping(value = "listpage")
    @ResponseBody
    public ApiResult listpage(PwFassetsUhistory pwFassetsUhistory, HttpServletRequest request, HttpServletResponse response) {
        try{
            Page<PwFassetsUhistory> page = pwFassetsUhistoryService.findPage(new Page<PwFassetsUhistory>(request, response), pwFassetsUhistory);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequiresPermissions("pw:pwFassetsUhistory:view")
    @RequestMapping(value = "form")
    public String form(PwFassetsUhistory pwFassetsUhistory, Model model) {
        model.addAttribute("pwFassetsUhistory", pwFassetsUhistory);
        return PwSval.path.vms(PwEmskey.PW.k()) + "pwFassetsUhistoryForm";
    }

    @RequiresPermissions("pw:pwFassetsUhistory:edit")
    @RequestMapping(value = "save")
    public String save(PwFassetsUhistory pwFassetsUhistory, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, pwFassetsUhistory)) {
            return form(pwFassetsUhistory, model);
        }
        pwFassetsUhistoryService.save(pwFassetsUhistory);
        addMessage(redirectAttributes, "保存固定资产使用记录成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassetsUhistory/?repage";
    }

    @RequiresPermissions("pw:pwFassetsUhistory:edit")
    @RequestMapping(value = "delete")
    public String delete(PwFassetsUhistory pwFassetsUhistory, RedirectAttributes redirectAttributes) {
        pwFassetsUhistoryService.delete(pwFassetsUhistory);
        addMessage(redirectAttributes, "删除固定资产使用记录成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/pw/pwFassetsUhistory/?repage";
    }

}