package com.oseasy.cms.modules.cms.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.FileTplService;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.com.pcore.common.web.BaseController;

/**
 * 站点Controller
 * @author SongLai

 */
@Controller
@RequestMapping(value = "${adminPath}/cms/template")
public class TemplateController extends BaseController {

    @Autowired
   	private FileTplService fileTplService;
    @Autowired
   	private SiteService siteService;

    @RequiresPermissions("cms:template:edit")
   	@RequestMapping(value = "")
   	public String index() {
   		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "tplIndex";
   	}

    @RequiresPermissions("cms:template:edit")
   	@RequestMapping(value = "tree")
   	public String tree(Model model, HttpServletRequest request) {
        fileTplService.setContext(request);
        Site site = siteService.get(Site.getCurrentSiteId());
   		model.addAttribute("templateList", fileTplService.getListForEdit(site.getCmsSiteconfig().getSolutionPath()));
   		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "tplTree";
   	}

    @RequiresPermissions("cms:template:edit")
   	@RequestMapping(value = "form")
   	public String form(String name, Model model, HttpServletRequest request) {
        fileTplService.setContext(request);
        model.addAttribute("template", fileTplService.getFileTpl(name));
   		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "tplForm";
   	}

    @RequiresPermissions("cms:template:edit")
   	@RequestMapping(value = "help")
   	public String help() {
   		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "tplHelp";
   	}
}
