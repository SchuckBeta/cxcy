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
import com.oseasy.pw.modules.pw.entity.PwBill;
import com.oseasy.pw.modules.pw.service.PwBillService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 账单Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwBill")
public class PwBillController extends BaseController {

	@Autowired
	private PwBillService pwBillService;

	@ModelAttribute
	public PwBill get(@RequestParam(required=false) String id) {
		PwBill entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwBillService.get(id);
		}
		if (entity == null){
			entity = new PwBill();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwBill:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwBill pwBill, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwBill> page = pwBillService.findPage(new Page<PwBill>(request, response), pwBill);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwBillList";
	}

	@RequiresPermissions("pw:pwBill:view")
	@RequestMapping(value = "form")
	public String form(PwBill pwBill, Model model) {
		model.addAttribute("pwBill", pwBill);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwBillForm";
	}

	@RequiresPermissions("pw:pwBill:edit")
	@RequestMapping(value = "save")
	public String save(PwBill pwBill, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwBill)){
			return form(pwBill, model);
		}
		pwBillService.save(pwBill);
		addMessage(redirectAttributes, "保存账单成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwBill/?repage";
	}

	@RequiresPermissions("pw:pwBill:edit")
	@RequestMapping(value = "delete")
	public String delete(PwBill pwBill, RedirectAttributes redirectAttributes) {
		pwBillService.delete(pwBill);
		addMessage(redirectAttributes, "删除账单成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwBill/?repage";
	}

}