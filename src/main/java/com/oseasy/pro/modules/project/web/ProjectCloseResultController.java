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
import com.oseasy.pro.modules.project.entity.ProjectCloseResult;
import com.oseasy.pro.modules.project.service.ProjectCloseResultService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * project_close_resultController
 * @author zhangzheng
 * @version 2017-03-29
 * @Deprecated
 */
@Controller
@RequestMapping(value = "${frontPath}/project/projectCloseResult")
public class ProjectCloseResultController extends BaseController {

	@Autowired
	private ProjectCloseResultService projectCloseResultService;

	@ModelAttribute
	public ProjectCloseResult get(@RequestParam(required=false) String id) {
		ProjectCloseResult entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = projectCloseResultService.get(id);
		}
		if (entity == null) {
			entity = new ProjectCloseResult();
		}
		return entity;
	}

	@RequiresPermissions("project:projectCloseResult:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProjectCloseResult projectCloseResult, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProjectCloseResult> page = projectCloseResultService.findPage(new Page<ProjectCloseResult>(request, response), projectCloseResult);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectCloseResultList";
	}

	@RequiresPermissions("project:projectCloseResult:view")
	@RequestMapping(value = "form")
	public String form(ProjectCloseResult projectCloseResult, Model model) {
		model.addAttribute("projectCloseResult", projectCloseResult);
		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectCloseResultForm";
	}

	@RequiresPermissions("project:projectCloseResult:edit")
	@RequestMapping(value = "save")
	public String save(ProjectCloseResult projectCloseResult, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, projectCloseResult)) {
			return form(projectCloseResult, model);
		}
		projectCloseResultService.save(projectCloseResult);
		addMessage(redirectAttributes, "保存project_close_result成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/project/projectCloseResult/?repage";
	}

	@RequiresPermissions("project:projectCloseResult:edit")
	@RequestMapping(value = "delete")
	public String delete(ProjectCloseResult projectCloseResult, RedirectAttributes redirectAttributes) {
		projectCloseResultService.delete(projectCloseResult);
		addMessage(redirectAttributes, "删除project_close_result成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/project/projectCloseResult/?repage";
	}

}