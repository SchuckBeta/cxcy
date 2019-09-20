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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwProject;
import com.oseasy.pw.modules.pw.service.PwProjectService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * pwController.
 * @author zy
 * @version 2018-11-20
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwProject")
public class PwProjectController extends BaseController {

	@Autowired
	private PwProjectService entityService;

	@ModelAttribute
	public PwProject get(@RequestParam(required=false) String id) {
		PwProject entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new PwProject();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwProject:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwProject entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwProject> page = entityService.findPage(new Page<PwProject>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwProjectList";
	}

	@RequiresPermissions("pw:pwProject:view")
	@RequestMapping(value = "form")
	public String form(PwProject entity, Model model) {
		model.addAttribute("pwProject", entity);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwProjectForm";
	}

	@RequiresPermissions("pw:pwProject:edit")
	@RequestMapping(value = "save")
	public String save(PwProject entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存pw成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwProject/?repage";
	}

	@RequiresPermissions("pw:pwProject:edit")
	@RequestMapping(value = "delete")
	public String delete(PwProject entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除pw成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwProject/?repage";
	}

}