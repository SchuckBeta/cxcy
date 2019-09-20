package com.oseasy.dr.modules.dr.web;

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
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.service.DrCardErspaceService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 卡设备Controller.
 * @author chenh
 * @version 2018-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCardErspace")
public class DrCardErspaceController extends BaseController {

	@Autowired
	private DrCardErspaceService drCardErspaceService;

	@ModelAttribute
	public DrCardErspace get(@RequestParam(required=false) String id) {
		DrCardErspace entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drCardErspaceService.get(id);
		}
		if (entity == null){
			entity = new DrCardErspace();
		}
		return entity;
	}

	@RequiresPermissions("dr:drCardErspace:view")
	@RequestMapping(value = {"list", ""})
	public String list(DrCardErspace drCardErspace, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrCardErspace> page = drCardErspaceService.findPage(new Page<DrCardErspace>(request, response), drCardErspace);
		model.addAttribute("page", page);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardErspaceList";
	}

	@RequiresPermissions("dr:drCardErspace:view")
	@RequestMapping(value = "form")
	public String form(DrCardErspace drCardErspace, Model model) {
		model.addAttribute("drCardErspace", drCardErspace);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardErspaceForm";
	}

	@RequiresPermissions("dr:drCardErspace:edit")
	@RequestMapping(value = "save")
	public String save(DrCardErspace drCardErspace, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, drCardErspace)){
			return form(drCardErspace, model);
		}
		drCardErspaceService.save(drCardErspace);
		addMessage(redirectAttributes, "保存卡设备成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardErspace/?repage";
	}

	@RequiresPermissions("dr:drCardErspace:edit")
	@RequestMapping(value = "delete")
	public String delete(DrCardErspace drCardErspace, RedirectAttributes redirectAttributes) {
		drCardErspaceService.delete(drCardErspace);
		addMessage(redirectAttributes, "删除卡设备成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardErspace/?repage";
	}

}