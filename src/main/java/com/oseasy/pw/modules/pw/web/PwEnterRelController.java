package com.oseasy.pw.modules.pw.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwEnterRel;
import com.oseasy.pw.modules.pw.service.PwEnterRelService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻申报关联Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwEnterRel")
public class PwEnterRelController extends BaseController {

	@Autowired
	private PwEnterRelService pwEnterRelService;

	@ModelAttribute
	public PwEnterRel get(@RequestParam(required=false) String id) {
		PwEnterRel entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwEnterRelService.get(id);
		}
		if (entity == null){
			entity = new PwEnterRel();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwEnterRel:view")
	@RequestMapping(value = "form")
	public String form(PwEnterRel pwEnterRel, Model model) {
		model.addAttribute("pwEnterRel", pwEnterRel);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterRelForm";
	}

	@RequiresPermissions("pw:pwEnterRel:edit")
	@RequestMapping(value = "save")
	public String save(PwEnterRel pwEnterRel, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwEnterRel)){
			return form(pwEnterRel, model);
		}
		pwEnterRelService.save(pwEnterRel);
		addMessage(redirectAttributes, "保存入驻申报关联成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRel/?repage";
	}

	@RequiresPermissions("pw:pwEnterRel:edit")
	@RequestMapping(value = "delete")
	public String delete(PwEnterRel pwEnterRel, RedirectAttributes redirectAttributes) {
		pwEnterRelService.delete(pwEnterRel);
		addMessage(redirectAttributes, "删除入驻申报关联成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterRel/?repage";
	}

}