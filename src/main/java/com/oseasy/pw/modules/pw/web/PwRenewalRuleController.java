package com.oseasy.pw.modules.pw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwRenewalRule;
import com.oseasy.pw.modules.pw.service.PwRenewalRuleService;
import com.oseasy.pw.modules.pw.vo.SvalPw;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 续期配置Controller.
 * @author zy
 * @version 2018-01-04
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwRenewalRule")
public class PwRenewalRuleController extends BaseController {

	@Autowired
	private PwRenewalRuleService pwRenewalRuleService;

	@ModelAttribute
	public PwRenewalRule get(@RequestParam(required=false) String id) {
		PwRenewalRule entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwRenewalRuleService.get(id);
		}
		if (entity == null){
			entity = new PwRenewalRule();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list(PwRenewalRule pwRenewalRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwRenewalRule> page = pwRenewalRuleService.findPage(new Page<PwRenewalRule>(request, response), pwRenewalRule);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwRenewalRuleList";
	}


	@RequestMapping(value = "form")
	public String form(PwRenewalRule pwRenewalRule, Model model) {
	  if((pwRenewalRule == null) || StringUtil.isEmpty(pwRenewalRule.getId())){
	    pwRenewalRule = pwRenewalRuleService.getPwRenewalRule();
	  }

    if((pwRenewalRule == null)){
      pwRenewalRule = new PwRenewalRule();
    }

	  if(pwRenewalRule.getApplyMaxNum() == null){
	    pwRenewalRule.setApplyMaxNum(SvalPw.getEnterApplyMaxNum());
    }

    if(StringUtil.isEmpty(pwRenewalRule.getIsWarm())){
      pwRenewalRule.setIsWarm((SvalPw.getEnterExpireAuto()) ? Const.YES : Const.NO);
    }
    if(pwRenewalRule.getWarmTime() == null){
      pwRenewalRule.setWarmTime(SvalPw.getEnterExpireMaxDay());
    }

    if(StringUtil.isEmpty(pwRenewalRule.getIsHatback())){
      pwRenewalRule.setIsHatback((SvalPw.getEnterExitAuto()) ? Const.YES : Const.NO);
    }
    if(pwRenewalRule.getHatbackTime() == null){
      pwRenewalRule.setHatbackTime(SvalPw.getEnterExitMaxDay());
    }
    model.addAttribute("pwRenewalRule", pwRenewalRule);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwRenewalRuleForm";
	}


	@RequestMapping(value = "save")
	public String save(PwRenewalRule pwRenewalRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwRenewalRule)){
			return form(pwRenewalRule, model);
		}
		pwRenewalRuleService.save(pwRenewalRule);
		addMessage(redirectAttributes, "保存续期配置成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwRenewalRule/form?repage";
	}


	@RequestMapping(value = "delete")
	public String delete(PwRenewalRule pwRenewalRule, RedirectAttributes redirectAttributes) {
		pwRenewalRuleService.delete(pwRenewalRule);
		addMessage(redirectAttributes, "删除续期配置成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwRenewalRule/?repage";
	}

}