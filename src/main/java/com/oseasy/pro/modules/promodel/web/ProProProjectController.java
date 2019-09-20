package com.oseasy.pro.modules.promodel.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.promodel.service.ProProProjectService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 创建项目Controller.
 *
 * @author zhangyao
 * @version 2017-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/proproject/proProject")
public class ProProProjectController extends BaseController {
    @Autowired
    private ProProjectService proProjectService;
    @Autowired
    private ProProProjectService proProProjectService;

    @ModelAttribute
    public ProProject get(@RequestParam(required = false) String id) {
        ProProject entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = proProjectService.get(id);
        }
        if (entity == null) {
            entity = new ProProject();
        }
        return entity;
    }

    @RequiresPermissions("proproject:proProject:view")
    @RequestMapping(value = "form")
    public String form(ProProject proProject, Model model) {
        model.addAttribute("proProject", proProject);
        return ActSval.path.vms(ActEmskey.PRO.k()) + "proProjectForm";
    }

    @RequiresPermissions("proproject:proProject:edit")
    @RequestMapping(value = "save")
    public String save(ProProject proProject, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, proProject)) {
            return form(proProject, model);
        }
        if (proProject.getState().equals("1")) {
            proProProjectService.saveProProject(proProject);
        } else {
            proProjectService.save(proProject);
        }
        addMessage(redirectAttributes, "保存创建项目成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/proproject/proProject/?repage";
    }
}