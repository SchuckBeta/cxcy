package com.oseasy.auy.modules.act.web;

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

import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义审核信息Controller.
 * @author zy
 * @version 2017-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/promodel/actYwAuditInfo")
public class AuyActYwAuditInfoController extends BaseController {

	@Autowired
	private ActYwAuditInfoService actYwAuditInfoService;

	@ModelAttribute
	public ActYwAuditInfo get(@RequestParam(required=false) String id) {
		ActYwAuditInfo entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = actYwAuditInfoService.get(id);
		}
		if (entity == null) {
			entity = new ActYwAuditInfo();
		}
		return entity;
	}

	@RequiresPermissions("promodel:actYwAuditInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActYwAuditInfo actYwAuditInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ActYwAuditInfo> page = actYwAuditInfoService.findPage(new Page<ActYwAuditInfo>(request, response), actYwAuditInfo);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "actYwAuditInfoList";
	}

	@RequiresPermissions("promodel:actYwAuditInfo:view")
	@RequestMapping(value = "form")
	public String form(ActYwAuditInfo actYwAuditInfo, Model model) {
		model.addAttribute("actYwAuditInfo", actYwAuditInfo);
		return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "actYwAuditInfoForm";
	}

	@RequiresPermissions("promodel:actYwAuditInfo:edit")
	@RequestMapping(value = "save")
	public String save(ActYwAuditInfo actYwAuditInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actYwAuditInfo)) {
			return form(actYwAuditInfo, model);
		}
		actYwAuditInfoService.save(actYwAuditInfo);
		addMessage(redirectAttributes, "保存自定义审核信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/promodel/actYwAuditInfo/?repage";
	}

	@RequiresPermissions("promodel:actYwAuditInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYwAuditInfo actYwAuditInfo, RedirectAttributes redirectAttributes) {
		actYwAuditInfoService.delete(actYwAuditInfo);
		addMessage(redirectAttributes, "删除自定义审核信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/promodel/actYwAuditInfo/?repage";
	}

}