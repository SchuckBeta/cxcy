package com.oseasy.act.modules.actyw.web;

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

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwGtime;
import com.oseasy.act.modules.actyw.service.ActYwGtimeService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 时间和组件关联关系Controller.
 * @author zy
 * @version 2017-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGtime")
public class ActYwGtimeController extends BaseController {

	@Autowired
	private ActYwGtimeService actYwGtimeService;

	@ModelAttribute
	public ActYwGtime get(@RequestParam(required=false) String id) {
		ActYwGtime entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = actYwGtimeService.get(id);
		}
		if (entity == null) {
			entity = new ActYwGtime();
		}
		return entity;
	}

	@RequiresPermissions("actyw:actYwGtime:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActYwGtime actYwGtime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ActYwGtime> page = actYwGtimeService.findPage(new Page<ActYwGtime>(request, response), actYwGtime);
		model.addAttribute("page", page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGtimeList";
	}

	@RequiresPermissions("actyw:actYwGtime:view")
	@RequestMapping(value = "form")
	public String form(ActYwGtime actYwGtime, Model model) {
		model.addAttribute("actYwGtime", actYwGtime);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGtimeForm";
	}

	@RequiresPermissions("actyw:actYwGtime:edit")
	@RequestMapping(value = "save")
	public String save(ActYwGtime actYwGtime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actYwGtime)) {
			return form(actYwGtime, model);
		}
		actYwGtimeService.save(actYwGtime);
		addMessage(redirectAttributes, "保存时间成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGtime/?repage";
	}

	@RequiresPermissions("actyw:actYwGtime:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYwGtime actYwGtime, RedirectAttributes redirectAttributes) {
		actYwGtimeService.delete(actYwGtime);
		addMessage(redirectAttributes, "删除时间成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGtime/?repage";
	}

}