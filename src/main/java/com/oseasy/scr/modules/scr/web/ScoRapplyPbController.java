package com.oseasy.scr.modules.scr.web;

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
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRapplyPb;
import com.oseasy.scr.modules.scr.service.ScoRapplyPbService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分申请配比Controller.
 * @author chenh
 * @version 2018-12-26
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRapplyPb")
public class ScoRapplyPbController extends BaseController {

	@Autowired
	private ScoRapplyPbService entityService;

	@ModelAttribute
	public ScoRapplyPb get(@RequestParam(required=false) String id) {
		ScoRapplyPb entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRapplyPb();
		}
		return entity;
	}

	@RequiresPermissions("scr:scoRapplyPb:view")
	@RequestMapping(value = {"list", ""})
	public String list(ScoRapplyPb entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoRapplyPb> page = entityService.findPage(new Page<ScoRapplyPb>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyPbList";
	}

	@RequiresPermissions("scr:scoRapplyPb:view")
	@RequestMapping(value = "form")
	public String form(ScoRapplyPb entity, Model model) {
		model.addAttribute("scoRapplyPb", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyPbForm";
	}

	@RequiresPermissions("scr:scoRapplyPb:edit")
	@RequestMapping(value = "save")
	public String save(ScoRapplyPb entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存学分申请配比成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapplyPb/?repage";
	}

	@RequiresPermissions("scr:scoRapplyPb:edit")
	@RequestMapping(value = "delete")
	public String delete(ScoRapplyPb entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除学分申请配比成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapplyPb/?repage";
	}

}