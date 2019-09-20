package com.oseasy.dr.modules.dr.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.dr.common.config.DrIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCardRule;
import com.oseasy.dr.modules.dr.service.DrCardRuleService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁预警Controller.
 * @author zy
 * @version 2018-04-13
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCardRule")
public class DrCardRuleController extends BaseController {

	@Autowired
	private DrCardRuleService drCardRuleService;

	@ModelAttribute
	public DrCardRule get(@RequestParam(required=false) String id) {
		DrCardRule entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drCardRuleService.get(id);
		}
		if (entity == null){
			entity = new DrCardRule();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(DrCardRule drCardRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrCardRule> page = drCardRuleService.findPage(new Page<DrCardRule>(request, response),drCardRule);
		model.addAttribute("page", page);
		return DrSval.path.vms(DrEmskey.DR.k()) + "DrCardRuleList";
	}


	@RequestMapping(value = "form")
	public String form(DrCardRule drCardRule, Model model) {
		DrCardRule drCardRuleIn=drCardRuleService.getDrCardRule();
		if(drCardRuleIn!=null){
			model.addAttribute("drCardRule", drCardRuleIn);
		}else{
			model.addAttribute("drCardRule", drCardRule);
		}
        model.addAttribute(CoreJkey.JK_UUID, DrIds.DR_CARD_GID.getId());
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardRuleSetForm";
	}


	@RequestMapping(value = "save")
	public String save(DrCardRule drCardRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, drCardRule)){
			return form(drCardRule, model);
		}
		//drCardRuleService.saveDrCardRule(drCardRule);
		drCardRuleService.saveDrCrule(drCardRule);
		addMessage(redirectAttributes, "保存门禁预警成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardRule/form/?repage";
	}


	@RequestMapping(value = "delete")
	public String delete(DrCardRule drCardRule, RedirectAttributes redirectAttributes) {
		drCardRuleService.delete(drCardRule);
		addMessage(redirectAttributes, "删除门禁预警成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardRule/?repage";
	}

}