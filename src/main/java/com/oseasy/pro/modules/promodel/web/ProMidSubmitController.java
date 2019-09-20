package com.oseasy.pro.modules.promodel.web;

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
import com.oseasy.pro.modules.promodel.entity.ProMidSubmit;
import com.oseasy.pro.modules.promodel.service.ProMidSubmitService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 中期提交信息表Controller.
 * @author zy
 * @version 2017-12-01
 */
@Controller
@RequestMapping(value = "${adminPath}/promodel/proMidSubmit")
public class ProMidSubmitController extends BaseController {

	@Autowired
	private ProMidSubmitService proMidSubmitService;

	@ModelAttribute
	public ProMidSubmit get(@RequestParam(required=false) String id) {
		ProMidSubmit entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = proMidSubmitService.get(id);
		}
		if (entity == null) {
			entity = new ProMidSubmit();
		}
		return entity;
	}

	@RequiresPermissions("promodel:proMidSubmit:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProMidSubmit proMidSubmit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProMidSubmit> page = proMidSubmitService.findPage(new Page<ProMidSubmit>(request, response), proMidSubmit);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "proMidSubmitList";
	}

	@RequiresPermissions("promodel:proMidSubmit:view")
	@RequestMapping(value = "form")
	public String form(ProMidSubmit proMidSubmit, Model model) {
		model.addAttribute("proMidSubmit", proMidSubmit);
		return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "proMidSubmitForm";
	}

	@RequiresPermissions("promodel:proMidSubmit:edit")
	@RequestMapping(value = "save")
	public String save(ProMidSubmit proMidSubmit, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, proMidSubmit)) {
			return form(proMidSubmit, model);
		}
		proMidSubmitService.save(proMidSubmit);
		addMessage(redirectAttributes, "保存proMidSubmit成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/promodel/proMidSubmit/?repage";
	}

	@RequiresPermissions("promodel:proMidSubmit:edit")
	@RequestMapping(value = "delete")
	public String delete(ProMidSubmit proMidSubmit, RedirectAttributes redirectAttributes) {
		proMidSubmitService.delete(proMidSubmit);
		addMessage(redirectAttributes, "删除proMidSubmit成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/promodel/proMidSubmit/?repage";
	}

}