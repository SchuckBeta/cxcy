package com.oseasy.act.modules.actyw.web;

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

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwEtAuditNum;
import com.oseasy.act.modules.actyw.service.ActYwEtAuditNumService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 指派专家组的项目Controller.
 * @author zy
 * @version 2019-01-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwEtAuditNum")
public class  ActYwEtAuditNumController extends BaseController {

	@Autowired
	private ActYwEtAuditNumService entityService;

	@ModelAttribute
	public ActYwEtAuditNum get(@RequestParam(required=false) String id) {
		ActYwEtAuditNum entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ActYwEtAuditNum();
		}
		return entity;
	}

	@RequiresPermissions("actyw:actYwEtAuditNum:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActYwEtAuditNum entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ActYwEtAuditNum> page = entityService.findPage(new Page<ActYwEtAuditNum>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwEtAuditNumList";
	}

	@RequiresPermissions("actyw:actYwEtAuditNum:view")
	@RequestMapping(value = "form")
	public String form(ActYwEtAuditNum entity, Model model) {
		model.addAttribute("actYwEtAuditNum", entity);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwEtAuditNumForm";
	}

	@RequiresPermissions("actyw:actYwEtAuditNum:edit")
	@RequestMapping(value = "save")
	public String save(ActYwEtAuditNum entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存指派专家组的项目成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwEtAuditNum/?repage";
	}

	@RequiresPermissions("actyw:actYwEtAuditNum:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYwEtAuditNum entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除指派专家组的项目成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwEtAuditNum/?repage";
	}

}