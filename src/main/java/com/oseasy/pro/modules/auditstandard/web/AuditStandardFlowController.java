package com.oseasy.pro.modules.auditstandard.web;

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
import com.oseasy.pro.modules.auditstandard.entity.AuditStandardFlow;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardFlowService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 评审标准、流程关系表Controller.
 * @author 9527
 * @version 2017-07-28
 */
@Controller
@RequestMapping(value = "${adminPath}/auditstandard/auditStandardFlow")
public class AuditStandardFlowController extends BaseController {

	@Autowired
	private AuditStandardFlowService auditStandardFlowService;

	@ModelAttribute
	public AuditStandardFlow get(@RequestParam(required=false) String id) {
		AuditStandardFlow entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = auditStandardFlowService.get(id);
		}
		if (entity == null) {
			entity = new AuditStandardFlow();
		}
		return entity;
	}

	@RequiresPermissions("auditstandard:auditStandardFlow:view")
	@RequestMapping(value = {"list", ""})
	public String list(AuditStandardFlow auditStandardFlow, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AuditStandardFlow> page = auditStandardFlowService.findPage(new Page<AuditStandardFlow>(request, response), auditStandardFlow);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.AUDITSTAN.k()) + "auditStandardFlowList";
	}

	@RequiresPermissions("auditstandard:auditStandardFlow:view")
	@RequestMapping(value = "form")
	public String form(AuditStandardFlow auditStandardFlow, Model model) {
		model.addAttribute("auditStandardFlow", auditStandardFlow);
		return ProSval.path.vms(ProEmskey.AUDITSTAN.k()) + "auditStandardFlowForm";
	}

	@RequiresPermissions("auditstandard:auditStandardFlow:edit")
	@RequestMapping(value = "save")
	public String save(AuditStandardFlow auditStandardFlow, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, auditStandardFlow)) {
			return form(auditStandardFlow, model);
		}
		auditStandardFlowService.save(auditStandardFlow);
		addMessage(redirectAttributes, "保存评审标准、流程关系表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/auditstandard/auditStandardFlow/?repage";
	}

	@RequiresPermissions("auditstandard:auditStandardFlow:edit")
	@RequestMapping(value = "delete")
	public String delete(AuditStandardFlow auditStandardFlow, RedirectAttributes redirectAttributes) {
		auditStandardFlowService.delete(auditStandardFlow);
		addMessage(redirectAttributes, "删除评审标准、流程关系表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/auditstandard/auditStandardFlow/?repage";
	}

}