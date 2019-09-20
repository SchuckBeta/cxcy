package com.oseasy.pro.modules.gcontest.web;

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
import com.oseasy.pro.modules.gcontest.entity.GAuditInfo;
import com.oseasy.pro.modules.gcontest.service.GAuditInfoService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大赛信息Controller
 * @author zy
 * @version 2017-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/gcontest/gAuditInfo")
public class GAuditInfoController extends BaseController {

	@Autowired
	private GAuditInfoService gAuditInfoService;

	@ModelAttribute
	public GAuditInfo get(@RequestParam(required=false) String id) {
		GAuditInfo entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = gAuditInfoService.get(id);
		}
		if (entity == null) {
			entity = new GAuditInfo();
		}
		return entity;
	}

	@RequiresPermissions("gcontest:gAuditInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(GAuditInfo gAuditInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GAuditInfo> page = gAuditInfoService.findPage(new Page<GAuditInfo>(request, response), gAuditInfo);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gAuditInfoList";
	}

	@RequiresPermissions("gcontest:gAuditInfo:view")
	@RequestMapping(value = "form")
	public String form(GAuditInfo gAuditInfo, Model model) {
		model.addAttribute("gAuditInfo", gAuditInfo);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gAuditInfoForm";
	}

	@RequiresPermissions("gcontest:gAuditInfo:edit")
	@RequestMapping(value = "save")
	public String save(GAuditInfo gAuditInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, gAuditInfo)) {
			return form(gAuditInfo, model);
		}
		gAuditInfoService.save(gAuditInfo);
		addMessage(redirectAttributes, "保存大赛信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gAuditInfo/?repage";
	}

	@RequiresPermissions("gcontest:gAuditInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(GAuditInfo gAuditInfo, RedirectAttributes redirectAttributes) {
		gAuditInfoService.delete(gAuditInfo);
		addMessage(redirectAttributes, "删除大赛信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gAuditInfo/?repage";
	}

}