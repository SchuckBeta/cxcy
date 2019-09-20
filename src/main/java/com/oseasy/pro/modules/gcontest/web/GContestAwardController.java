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
import com.oseasy.pro.modules.gcontest.entity.GContestAward;
import com.oseasy.pro.modules.gcontest.service.GContestAwardService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大赛获奖表Controller
 * @author zy
 * @version 2017-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/gcontest/gContestAward")
public class GContestAwardController extends BaseController {

	@Autowired
	private GContestAwardService gContestAwardService;

	@ModelAttribute
	public GContestAward get(@RequestParam(required=false) String id) {
		GContestAward entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = gContestAwardService.get(id);
		}
		if (entity == null) {
			entity = new GContestAward();
		}
		return entity;
	}

	@RequiresPermissions("gcontest:gContestAward:view")
	@RequestMapping(value = {"list", ""})
	public String list(GContestAward gContestAward, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GContestAward> page = gContestAwardService.findPage(new Page<GContestAward>(request, response), gContestAward);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestAwardList";
	}

	@RequiresPermissions("gcontest:gContestAward:view")
	@RequestMapping(value = "form")
	public String form(GContestAward gContestAward, Model model) {
		model.addAttribute("gContestAward", gContestAward);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestAwardForm";
	}

	@RequiresPermissions("gcontest:gContestAward:edit")
	@RequestMapping(value = "save")
	public String save(GContestAward gContestAward, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, gContestAward)) {
			return form(gContestAward, model);
		}
		gContestAwardService.save(gContestAward);
		addMessage(redirectAttributes, "保存大赛获奖表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContestAward/?repage";
	}

	@RequiresPermissions("gcontest:gContestAward:edit")
	@RequestMapping(value = "delete")
	public String delete(GContestAward gContestAward, RedirectAttributes redirectAttributes) {
		gContestAwardService.delete(gContestAward);
		addMessage(redirectAttributes, "删除大赛获奖表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContestAward/?repage";
	}

}