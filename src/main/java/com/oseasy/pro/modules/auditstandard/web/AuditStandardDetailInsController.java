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
import com.oseasy.pro.modules.auditstandard.entity.AuditStandardDetailIns;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardDetailInsService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 评审标准详情记录Controller.
 * @author 9527
 * @version 2017-07-28
 */
@Controller
@RequestMapping(value = "${adminPath}/auditstandard/auditStandardDetailIns")
public class AuditStandardDetailInsController extends BaseController {

	@Autowired
	private AuditStandardDetailInsService auditStandardDetailInsService;

	@ModelAttribute
	public AuditStandardDetailIns get(@RequestParam(required=false) String id) {
		AuditStandardDetailIns entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = auditStandardDetailInsService.get(id);
		}
		if (entity == null) {
			entity = new AuditStandardDetailIns();
		}
		return entity;
	}

	@RequiresPermissions("auditstandard:auditStandardDetailIns:view")
	@RequestMapping(value = {"list", ""})
	public String list(AuditStandardDetailIns auditStandardDetailIns, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AuditStandardDetailIns> page = auditStandardDetailInsService.findPage(new Page<AuditStandardDetailIns>(request, response), auditStandardDetailIns);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.AUDITSTAN.k()) + "auditStandardDetailInsList";
	}

	@RequiresPermissions("auditstandard:auditStandardDetailIns:view")
	@RequestMapping(value = "form")
	public String form(AuditStandardDetailIns auditStandardDetailIns, Model model) {
		model.addAttribute("auditStandardDetailIns", auditStandardDetailIns);
		return ProSval.path.vms(ProEmskey.AUDITSTAN.k()) + "auditStandardDetailInsForm";
	}

	@RequiresPermissions("auditstandard:auditStandardDetailIns:edit")
	@RequestMapping(value = "save")
	public String save(AuditStandardDetailIns auditStandardDetailIns, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, auditStandardDetailIns)) {
			return form(auditStandardDetailIns, model);
		}
		auditStandardDetailInsService.save(auditStandardDetailIns);
		addMessage(redirectAttributes, "保存评审标准详情记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/auditstandard/auditStandardDetailIns/?repage";
	}

	@RequiresPermissions("auditstandard:auditStandardDetailIns:edit")
	@RequestMapping(value = "delete")
	public String delete(AuditStandardDetailIns auditStandardDetailIns, RedirectAttributes redirectAttributes) {
		auditStandardDetailInsService.delete(auditStandardDetailIns);
		addMessage(redirectAttributes, "删除评审标准详情记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/auditstandard/auditStandardDetailIns/?repage";
	}

}