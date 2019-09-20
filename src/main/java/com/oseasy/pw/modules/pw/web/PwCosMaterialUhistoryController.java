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
import com.oseasy.pw.modules.pw.entity.PwCosMaterialUhistory;
import com.oseasy.pw.modules.pw.service.PwCosMaterialUhistoryService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 耗材使用记录Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwCosMaterialUhistory")
public class PwCosMaterialUhistoryController extends BaseController {

	@Autowired
	private PwCosMaterialUhistoryService pwCosMaterialUhistoryService;

	@ModelAttribute
	public PwCosMaterialUhistory get(@RequestParam(required=false) String id) {
		PwCosMaterialUhistory entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwCosMaterialUhistoryService.get(id);
		}
		if (entity == null){
			entity = new PwCosMaterialUhistory();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwCosMaterialUhistory:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwCosMaterialUhistory pwCosMaterialUhistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwCosMaterialUhistory> page = pwCosMaterialUhistoryService.findPage(new Page<PwCosMaterialUhistory>(request, response), pwCosMaterialUhistory);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwCosMaterialUhistoryList";
	}

	@RequiresPermissions("pw:pwCosMaterialUhistory:view")
	@RequestMapping(value = "form")
	public String form(PwCosMaterialUhistory pwCosMaterialUhistory, Model model) {
		model.addAttribute("pwCosMaterialUhistory", pwCosMaterialUhistory);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwCosMaterialUhistoryForm";
	}

	@RequiresPermissions("pw:pwCosMaterialUhistory:edit")
	@RequestMapping(value = "save")
	public String save(PwCosMaterialUhistory pwCosMaterialUhistory, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwCosMaterialUhistory)){
			return form(pwCosMaterialUhistory, model);
		}
		pwCosMaterialUhistoryService.save(pwCosMaterialUhistory);
		addMessage(redirectAttributes, "保存耗材使用记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwCosMaterialUhistory/?repage";
	}

	@RequiresPermissions("pw:pwCosMaterialUhistory:edit")
	@RequestMapping(value = "delete")
	public String delete(PwCosMaterialUhistory pwCosMaterialUhistory, RedirectAttributes redirectAttributes) {
		pwCosMaterialUhistoryService.delete(pwCosMaterialUhistory);
		addMessage(redirectAttributes, "删除耗材使用记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwCosMaterialUhistory/?repage";
	}

}