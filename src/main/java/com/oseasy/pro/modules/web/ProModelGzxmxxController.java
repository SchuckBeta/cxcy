package com.oseasy.pro.modules.web;

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
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.service.ProModelGzsmxxService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * proProjectMdController.
 * @author zy
 * @version 2017-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/proprojectmd/proModelGzxmxx")
public class ProModelGzxmxxController extends BaseController {

	@Autowired
	private ProModelGzsmxxService proModelGzxmxxService;

	@ModelAttribute
	public ProModelGzsmxx get(@RequestParam(required=false) String id) {
		ProModelGzsmxx entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = proModelGzxmxxService.get(id);
		}
		if (entity == null) {
			entity = new ProModelGzsmxx();
		}
		return entity;
	}

	@RequiresPermissions("proprojectmd:proModelGzxmxx:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProModelGzsmxx proModelGzxmxx, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProModelGzsmxx> page = proModelGzxmxxService.findPage(new Page<ProModelGzsmxx>(request, response), proModelGzxmxx);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "proModelGzxmxxList";
	}


	@RequestMapping(value = "form")
	public String form(ProModelGzsmxx proModelGzxmxx, Model model) {
		model.addAttribute("proModelGzxmxx", proModelGzxmxx);
		//return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "proModelGzxmxxForm";
		return "/template/form/project/md_applyForm";
	}

	@RequiresPermissions("proprojectmd:proModelGzxmxx:edit")
	@RequestMapping(value = "save")
	public String save(ProModelGzsmxx proModelGzxmxx, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, proModelGzxmxx)) {
			return form(proModelGzxmxx, model);
		}
		proModelGzxmxxService.save(proModelGzxmxx);
		addMessage(redirectAttributes, "保存proProjectMd成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/proprojectmd/proModelGzxmxx/?repage";
	}

	@RequiresPermissions("proprojectmd:proModelGzxmxx:edit")
	@RequestMapping(value = "delete")
	public String delete(ProModelGzsmxx proModelGzxmxx, RedirectAttributes redirectAttributes) {
		proModelGzxmxxService.delete(proModelGzxmxx);
		addMessage(redirectAttributes, "删除proProjectMd成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/proprojectmd/proModelGzxmxx/?repage";
	}
}