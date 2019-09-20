package com.oseasy.scr.modules.sco.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.entity.ScoAllotRatio;
import com.oseasy.scr.modules.sco.service.ScoAllotRatioService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分分配比例Controller.
 * @author 9527
 * @version 2017-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/sco/scoAllotRatio")
public class ScoAllotRatioController extends BaseController {

	@Autowired
	private ScoAllotRatioService scoAllotRatioService;

	@ModelAttribute
	public ScoAllotRatio get(@RequestParam(required=false) String id) {
		ScoAllotRatio entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = scoAllotRatioService.get(id);
		}
		if (entity == null) {
			entity = new ScoAllotRatio();
		}
		return entity;
	}

	@RequiresPermissions("sco:scoAllotRatio:view")
	@RequestMapping(value = {"list", ""})
	public String list(ScoAllotRatio scoAllotRatio, HttpServletRequest request, HttpServletResponse response, Model model) {
		String confId=request.getParameter("confId");
		if (StringUtil.isNotEmpty(confId)) {
			model.addAttribute("confId",confId);
			model.addAttribute("list", scoAllotRatioService.findAll(confId));
		}
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoAllotRatioList";
	}

	@RequiresPermissions("sco:scoAllotRatio:view")
	@RequestMapping(value = "form")
	public String form(ScoAllotRatio scoAllotRatio, Model model,HttpServletRequest request) {
		if (StringUtil.isEmpty(scoAllotRatio.getId())) {
			String confId=request.getParameter("confId");
			scoAllotRatio.setAffirmConfId(confId);
		}
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		model.addAttribute("scoAllotRatio", scoAllotRatio);
		return ScrSval.path.vms(ScrEmskey.SCO.k()) + "scoAllotRatioForm";
	}

	@RequiresPermissions("sco:scoAllotRatio:edit")
	@RequestMapping(value = "save")
	public String save(ScoAllotRatio scoAllotRatio, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, scoAllotRatio)) {
			return form(scoAllotRatio, model,request);
		}
		scoAllotRatioService.save(scoAllotRatio);
		addMessage(redirectAttributes, "保存学分分配比例成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sco/scoAllotRatio/?confId="+scoAllotRatio.getAffirmConfId();
	}

	@RequiresPermissions("sco:scoAllotRatio:edit")
	@RequestMapping(value = "delete")
	public String delete(ScoAllotRatio scoAllotRatio, RedirectAttributes redirectAttributes) {
		scoAllotRatioService.delete(scoAllotRatio);
		addMessage(redirectAttributes, "删除学分分配比例成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sco/scoAllotRatio/?confId="+scoAllotRatio.getAffirmConfId();
	}
	@RequestMapping(value = "checkNumber")
	@ResponseBody
	public boolean checkNumber(String id,String number,String confid) {
		if (scoAllotRatioService.checkNumber(id, number,confid)>0) {
			return false;
		}
		return true;
	}
}