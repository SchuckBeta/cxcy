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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.project.entity.ProjectAuditInfo;
import com.oseasy.pro.modules.project.service.ProjectAuditInfoService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目审核信息Controller
 * @author 9527
 * @version 2017-03-11
 */
@Controller
@RequestMapping(value = "${frontPath}/project/projectAuditInfo")
public class ProjectAuditInfoController extends BaseController {

	@Autowired
	private ProjectAuditInfoService projectAuditInfoService;

	@ModelAttribute
	public ProjectAuditInfo get(@RequestParam(required=false) String id) {
		ProjectAuditInfo entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = projectAuditInfoService.get(id);
		}
		if (entity == null) {
			entity = new ProjectAuditInfo();
		}
		return entity;
	}

	@RequiresPermissions("project:projectAuditInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProjectAuditInfo projectAuditInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProjectAuditInfo> page = projectAuditInfoService.findPage(new Page<ProjectAuditInfo>(request, response), projectAuditInfo);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectAuditInfoList";
	}

	@RequiresPermissions("project:projectAuditInfo:view")
	@RequestMapping(value = "form")
	public String form(ProjectAuditInfo projectAuditInfo, Model model) {
		model.addAttribute("projectAuditInfo", projectAuditInfo);
		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectAuditInfoForm";
	}

	@RequiresPermissions("project:projectAuditInfo:edit")
	@RequestMapping(value = "save")
	public String save(ProjectAuditInfo projectAuditInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, projectAuditInfo)) {
			return form(projectAuditInfo, model);
		}
		projectAuditInfoService.save(projectAuditInfo);
		addMessage(redirectAttributes, "保存项目审核信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/project/projectAuditInfo/?repage";
	}

	@RequiresPermissions("project:projectAuditInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(ProjectAuditInfo projectAuditInfo, RedirectAttributes redirectAttributes) {
		projectAuditInfoService.delete(projectAuditInfo);
		addMessage(redirectAttributes, "删除项目审核信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/project/projectAuditInfo/?repage";
	}

}