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
import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.act.modules.actyw.service.ActYwGstatusService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 节点状态中间表Controller.
 * @author zy
 * @version 2018-01-15
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGstatus")
public class ActYwGstatusController extends BaseController {

	@Autowired
	private ActYwGstatusService actYwGstatusService;

	@ModelAttribute
	public ActYwGstatus get(@RequestParam(required=false) String id) {
		ActYwGstatus entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = actYwGstatusService.get(id);
		}
		if (entity == null){
			entity = new ActYwGstatus();
		}
		return entity;
	}

	@RequiresPermissions("actyw:actYwGstatus:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActYwGstatus actYwGstatus, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ActYwGstatus> page = actYwGstatusService.findPage(new Page<ActYwGstatus>(request, response), actYwGstatus);
		model.addAttribute("page", page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGstatusList";
	}

	@RequiresPermissions("actyw:actYwGstatus:view")
	@RequestMapping(value = "form")
	public String form(ActYwGstatus actYwGstatus, Model model) {
		model.addAttribute("actYwGstatus", actYwGstatus);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGstatusForm";
	}

	@RequiresPermissions("actyw:actYwGstatus:edit")
	@RequestMapping(value = "save")
	public String save(ActYwGstatus actYwGstatus, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actYwGstatus)){
			return form(actYwGstatus, model);
		}
		actYwGstatusService.save(actYwGstatus);
		addMessage(redirectAttributes, "保存节点状态中间表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGstatus/?repage";
	}

	@RequiresPermissions("actyw:actYwGstatus:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYwGstatus actYwGstatus, RedirectAttributes redirectAttributes) {
		actYwGstatusService.delete(actYwGstatus);
		addMessage(redirectAttributes, "删除节点状态中间表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGstatus/?repage";
	}

}