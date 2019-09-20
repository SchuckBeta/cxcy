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
import com.oseasy.pw.modules.pw.entity.PwCosMaterialPrecords;
import com.oseasy.pw.modules.pw.service.PwCosMaterialPrecordsService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 耗材购买记录Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwCosMaterialPrecords")
public class PwCosMaterialPrecordsController extends BaseController {

	@Autowired
	private PwCosMaterialPrecordsService pwCosMaterialPrecordsService;

	@ModelAttribute
	public PwCosMaterialPrecords get(@RequestParam(required=false) String id) {
		PwCosMaterialPrecords entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwCosMaterialPrecordsService.get(id);
		}
		if (entity == null){
			entity = new PwCosMaterialPrecords();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwCosMaterialPrecords:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwCosMaterialPrecords pwCosMaterialPrecords, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwCosMaterialPrecords> page = pwCosMaterialPrecordsService.findPage(new Page<PwCosMaterialPrecords>(request, response), pwCosMaterialPrecords);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwCosMaterialPrecordsList";
	}

	@RequiresPermissions("pw:pwCosMaterialPrecords:view")
	@RequestMapping(value = "form")
	public String form(PwCosMaterialPrecords pwCosMaterialPrecords, Model model) {
		model.addAttribute("pwCosMaterialPrecords", pwCosMaterialPrecords);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwCosMaterialPrecordsForm";
	}

	@RequiresPermissions("pw:pwCosMaterialPrecords:edit")
	@RequestMapping(value = "save")
	public String save(PwCosMaterialPrecords pwCosMaterialPrecords, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwCosMaterialPrecords)){
			return form(pwCosMaterialPrecords, model);
		}
		pwCosMaterialPrecordsService.save(pwCosMaterialPrecords);
		addMessage(redirectAttributes, "保存耗材购买记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwCosMaterialPrecords/?repage";
	}

	@RequiresPermissions("pw:pwCosMaterialPrecords:edit")
	@RequestMapping(value = "delete")
	public String delete(PwCosMaterialPrecords pwCosMaterialPrecords, RedirectAttributes redirectAttributes) {
		pwCosMaterialPrecordsService.delete(pwCosMaterialPrecords);
		addMessage(redirectAttributes, "删除耗材购买记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwCosMaterialPrecords/?repage";
	}

}