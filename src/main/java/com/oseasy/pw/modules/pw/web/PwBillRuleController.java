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
import com.oseasy.pw.modules.pw.entity.PwBillRule;
import com.oseasy.pw.modules.pw.service.PwBillRuleService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 费用规则Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwBillRule")
public class PwBillRuleController extends BaseController {

	@Autowired
	private PwBillRuleService pwBillRuleService;

	@ModelAttribute
	public PwBillRule get(@RequestParam(required=false) String id) {
		PwBillRule entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwBillRuleService.get(id);
		}
		if (entity == null){
			entity = new PwBillRule();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwBillRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwBillRule pwBillRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwBillRule> page = pwBillRuleService.findPage(new Page<PwBillRule>(request, response), pwBillRule);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwBillRuleList";
	}

	@RequiresPermissions("pw:pwBillRule:view")
	@RequestMapping(value = "form")
	public String form(PwBillRule pwBillRule, Model model) {
		model.addAttribute("pwBillRule", pwBillRule);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwBillRuleForm";
	}

	@RequiresPermissions("pw:pwBillRule:edit")
	@RequestMapping(value = "save")
	public String save(PwBillRule pwBillRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwBillRule)){
			return form(pwBillRule, model);
		}
		pwBillRuleService.save(pwBillRule);
		addMessage(redirectAttributes, "保存费用规则成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwBillRule/?repage";
	}

	@RequiresPermissions("pw:pwBillRule:edit")
	@RequestMapping(value = "delete")
	public String delete(PwBillRule pwBillRule, RedirectAttributes redirectAttributes) {
		pwBillRuleService.delete(pwBillRule);
		addMessage(redirectAttributes, "删除费用规则成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwBillRule/?repage";
	}

}