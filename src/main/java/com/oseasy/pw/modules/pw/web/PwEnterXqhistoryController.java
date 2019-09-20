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
import com.oseasy.pw.modules.pw.entity.PwEnterXqhistory;
import com.oseasy.pw.modules.pw.service.PwEnterXqhistoryService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻续期历史Controller.
 * @author zy
 * @version 2018-01-02
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwEnterXqhistory")
public class PwEnterXqhistoryController extends BaseController {

	@Autowired
	private PwEnterXqhistoryService pwEnterXqhistoryService;

	@ModelAttribute
	public PwEnterXqhistory get(@RequestParam(required=false) String id) {
		PwEnterXqhistory entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwEnterXqhistoryService.get(id);
		}
		if (entity == null){
			entity = new PwEnterXqhistory();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwEnterXqhistory:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwEnterXqhistory pwEnterXqhistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwEnterXqhistory> page = pwEnterXqhistoryService.findPage(new Page<PwEnterXqhistory>(request, response), pwEnterXqhistory);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterXqhistoryList";
	}

	@RequiresPermissions("pw:pwEnterXqhistory:view")
	@RequestMapping(value = "form")
	public String form(PwEnterXqhistory pwEnterXqhistory, Model model) {
		model.addAttribute("pwEnterXqhistory", pwEnterXqhistory);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwEnterXqhistoryForm";
	}

	@RequiresPermissions("pw:pwEnterXqhistory:edit")
	@RequestMapping(value = "save")
	public String save(PwEnterXqhistory pwEnterXqhistory, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwEnterXqhistory)){
			return form(pwEnterXqhistory, model);
		}
		pwEnterXqhistoryService.save(pwEnterXqhistory);
		addMessage(redirectAttributes, "保存入驻续期历史成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterXqhistory/?repage";
	}

	@RequiresPermissions("pw:pwEnterXqhistory:edit")
	@RequestMapping(value = "delete")
	public String delete(PwEnterXqhistory pwEnterXqhistory, RedirectAttributes redirectAttributes) {
		pwEnterXqhistoryService.delete(pwEnterXqhistory);
		addMessage(redirectAttributes, "删除入驻续期历史成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwEnterXqhistory/?repage";
	}

}