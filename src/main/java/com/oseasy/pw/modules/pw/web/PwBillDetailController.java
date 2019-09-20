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
import com.oseasy.pw.modules.pw.entity.PwBillDetail;
import com.oseasy.pw.modules.pw.service.PwBillDetailService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 账单明细Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwBillDetail")
public class PwBillDetailController extends BaseController {

	@Autowired
	private PwBillDetailService pwBillDetailService;

	@ModelAttribute
	public PwBillDetail get(@RequestParam(required=false) String id) {
		PwBillDetail entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwBillDetailService.get(id);
		}
		if (entity == null){
			entity = new PwBillDetail();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwBillDetail:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwBillDetail pwBillDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwBillDetail> page = pwBillDetailService.findPage(new Page<PwBillDetail>(request, response), pwBillDetail);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwBillDetailList";
	}

	@RequiresPermissions("pw:pwBillDetail:view")
	@RequestMapping(value = "form")
	public String form(PwBillDetail pwBillDetail, Model model) {
		model.addAttribute("pwBillDetail", pwBillDetail);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwBillDetailForm";
	}

	@RequiresPermissions("pw:pwBillDetail:edit")
	@RequestMapping(value = "save")
	public String save(PwBillDetail pwBillDetail, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwBillDetail)){
			return form(pwBillDetail, model);
		}
		pwBillDetailService.save(pwBillDetail);
		addMessage(redirectAttributes, "保存账单明细成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwBillDetail/?repage";
	}

	@RequiresPermissions("pw:pwBillDetail:edit")
	@RequestMapping(value = "delete")
	public String delete(PwBillDetail pwBillDetail, RedirectAttributes redirectAttributes) {
		pwBillDetailService.delete(pwBillDetail);
		addMessage(redirectAttributes, "删除账单明细成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwBillDetail/?repage";
	}

}