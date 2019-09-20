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
import com.oseasy.scr.modules.scr.entity.ScoRapplyMember;
import com.oseasy.scr.modules.scr.service.ScoRapplyMemberService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分申请成员Controller.
 * @author chenhao
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRapplyMember")
public class ScoRapplyMemberController extends BaseController {

	@Autowired
	private ScoRapplyMemberService entityService;

	@ModelAttribute
	public ScoRapplyMember get(@RequestParam(required=false) String id) {
		ScoRapplyMember entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRapplyMember();
		}
		return entity;
	}

	@RequiresPermissions("scr:scoRapplyMember:view")
	@RequestMapping(value = {"list", ""})
	public String list(ScoRapplyMember entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoRapplyMember> page = entityService.findPage(new Page<ScoRapplyMember>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyMemberList";
	}

	@RequiresPermissions("scr:scoRapplyMember:view")
	@RequestMapping(value = "form")
	public String form(ScoRapplyMember entity, Model model) {
		model.addAttribute("scoRapplyMember", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyMemberForm";
	}

	@RequiresPermissions("scr:scoRapplyMember:edit")
	@RequestMapping(value = "save")
	public String save(ScoRapplyMember entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存学分申请成员成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapplyMember/?repage";
	}

	@RequiresPermissions("scr:scoRapplyMember:edit")
	@RequestMapping(value = "delete")
	public String delete(ScoRapplyMember entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除学分申请成员成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/scr/scoRapplyMember/?repage";
	}

}