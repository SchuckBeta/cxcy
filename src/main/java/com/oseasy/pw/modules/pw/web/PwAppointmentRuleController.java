package com.oseasy.pw.modules.pw.web;

import java.util.List;

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
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwAppointmentRule;
import com.oseasy.pw.modules.pw.service.PwAppointmentRuleService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 预约规则Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwAppointmentRule")
public class PwAppointmentRuleController extends BaseController {

	@Autowired
	private PwAppointmentRuleService pwAppointmentRuleService;

	@ModelAttribute
	public PwAppointmentRule get(@RequestParam(required=false) String id) {
		PwAppointmentRule entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwAppointmentRuleService.get(id);
		}
		if (entity == null){
			entity = new PwAppointmentRule();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list(PwAppointmentRule pwAppointmentRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwAppointmentRule> page = pwAppointmentRuleService.findPage(new Page<PwAppointmentRule>(request, response), pwAppointmentRule);
		model.addAttribute("page", page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentRuleList";
	}


	@RequestMapping(value = "form")
	public String form(PwAppointmentRule pwAppointmentRule, Model model) {
		PwAppointmentRule pwAppointmentRuleIn=pwAppointmentRuleService.getPwAppointmentRule();

		List<Dict> weekList = DictUtils.getDictList("pw_rule_week");
		model.addAttribute("weekList",weekList);
		if(pwAppointmentRuleIn!=null){
			model.addAttribute("pwAppointmentRule", pwAppointmentRuleIn);
		}else{
			model.addAttribute("pwAppointmentRule", pwAppointmentRule);
		}

		return PwSval.path.vms(PwEmskey.PW.k()) + "pwAppointmentRuleForm";
	}


	@RequestMapping(value = "save")
	public String save(PwAppointmentRule pwAppointmentRule, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, pwAppointmentRule)){
			return form(pwAppointmentRule, model);
		}
		pwAppointmentRuleService.save(pwAppointmentRule);
		addMessage(redirectAttributes, "保存预约规则成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwAppointmentRule/form?repage";
	}


	@RequestMapping(value = "delete")
	public String delete(PwAppointmentRule pwAppointmentRule, RedirectAttributes redirectAttributes) {
		pwAppointmentRuleService.delete(pwAppointmentRule);
		addMessage(redirectAttributes, "删除预约规则成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwAppointmentRule/?repage";
	}

}