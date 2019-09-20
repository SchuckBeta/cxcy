package com.oseasy.pro.modules.project.web;

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

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 创建项目Controller.
 *
 * @author zhangyao
 * @version 2017-06-15
 */
@Controller
@RequestMapping(value = "${adminPath}/proproject/proProject")
public class ProProjectController extends BaseController {
    @Autowired
    private ProProjectService proProjectService;

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
    @RequestMapping(value = {"list", ""})
    public String list(ProProject proProject, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ProProject> page = proProjectService.findPage(new Page<ProProject>(request, response), proProject);
        model.addAttribute("page", page);
        return ActSval.path.vms(ActEmskey.PRO.k()) + "proProjectList";
    }

    @RequestMapping(value = "validateName")
    @ResponseBody
    public String validateName(String name) {
        if (proProjectService.getProProjectByName(name) != null) {
            return "1";
        }
        return "0";

    }

    @RequiresPermissions("proproject:proProject:edit")
    @RequestMapping(value = "delete")
    public String delete(ProProject proProject, RedirectAttributes redirectAttributes) {
        proProjectService.delete(proProject);
        addMessage(redirectAttributes, "删除创建项目成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/proproject/proProject/?repage";
    }


}