/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.oseasy.pro.modules.dasai.web;

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

import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.dasai.entity.OaDasai;
import com.oseasy.pro.modules.dasai.service.OaDasaiService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 大赛测试Controller
 * @author zhangzheng
 * @version 2017-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/dasai")
public class OaDasaiController extends BaseController {

	@Autowired
	private OaDasaiService oaDasaiService;

	@Autowired
	ActTaskService actTaskService;

	@ModelAttribute
	public OaDasai get(@RequestParam(required=false) String id) {
		OaDasai entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = oaDasaiService.get(id);
		}
		if (entity == null) {
			entity = new OaDasai();
		}
		return entity;
	}

    //我的报名 审核中列表
	@RequestMapping(value = "auditingList")
	public String audingList(OaDasai oaDasai, HttpServletRequest request, HttpServletResponse response, Model model) {
		oaDasai.setState("1");
		User user= UserUtils.getUser();
		oaDasai.setCreateBy(user);
		Page<OaDasai> page = oaDasaiService.findPage(new Page<OaDasai>(request, response), oaDasai);
		List<OaDasai> list=page.getList();
		for(OaDasai dasai:list) {
			dasai.getAct().setProcDefId(actTaskService.getProcessDefinitionIdByProInstId(dasai.getProcInsId()));
		}
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.DASAI.k()) + "auditingList";
	}


	//我的报名 审核完毕
	@RequestMapping(value = "auditedList")
	public String auditedList(OaDasai oaDasai, HttpServletRequest request, HttpServletResponse response, Model model) {
		oaDasai.setState("2");
		User user= UserUtils.getUser();
		oaDasai.setCreateBy(user);
		Page<OaDasai> page = oaDasaiService.findPage(new Page<OaDasai>(request, response), oaDasai);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.DASAI.k()) + "auditedList";
	}

	//大赛报名
	@RequestMapping(value = "form")
	public String form(OaDasai oaDasai, Model model) {
		model.addAttribute("oaDasai", oaDasai);
		return ProSval.path.vms(ProEmskey.DASAI.k()) + "oaDasaiForm";
	}


	@RequestMapping(value = "save")
	public String save(OaDasai oaDasai, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaDasai)) {
			return form(oaDasai, model);
		}
		oaDasaiService.save(oaDasai);
		addMessage(redirectAttributes, "保存大赛成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/oa/dasai/form?repage";
	}

	@RequestMapping(value = "audit1")
	public String  audit1(OaDasai oaDasai,HttpServletRequest request, Model model) {
		//查找大赛提交的表单  供评分老师查看
//		Act act=oaDasai.getAct();
//		String businessId=act.getBusinessId();
//		oaDasai= oaDasaiService.get(businessId);
		model.addAttribute("oaDasai", oaDasai);
		//工作流相关信息
//		model.addAttribute("act", act);
		return ProSval.path.vms(ProEmskey.DASAI.k()) + "markForm";
	}

	@RequestMapping(value = "audit2")
	public String  audit2(OaDasai oaDasai,HttpServletRequest request, Model model) {
		//查找大赛提交的表单  供评分老师查看
//		Act act=oaDasai.getAct();
//		String businessId=act.getBusinessId();
//		oaDasai= oaDasaiService.get(businessId);
		model.addAttribute("oaDasai", oaDasai);
		//工作流相关信息
//		model.addAttribute("act", act);
		return ProSval.path.vms(ProEmskey.DASAI.k()) + "markForm2";
	}

	//学院评分提交处理
	@RequestMapping(value = "saveAudit1")
	public String saveAudit1(OaDasai oaDasai,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Integer score=oaDasai.getScore();
		//执行工作流
		oaDasaiService.saveAudit1(oaDasai);
		addMessage(redirectAttributes, "评分成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/oa/dasai/markList?repage";
	}

	//班级评分提交处理
	@RequestMapping(value = "saveAudit2")
	public String saveAudit2(OaDasai oaDasai,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Integer score=oaDasai.getScore();
		//执行工作流
		oaDasaiService.saveAudit2(oaDasai);
		addMessage(redirectAttributes, "评分成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/oa/dasai/markList2?repage";
	}




	@RequestMapping(value = "markList")
	public String markList(Act act, HttpServletRequest request,HttpServletResponse response, Model model) {
		//查询代办任务
		act.setProcDefKey("multi_task");
		Page<Act> pageForSearch =new Page<Act>(request, response);

		Page<Act> page=actTaskService.todoListForPage(pageForSearch,act);
		model.addAttribute("page",page);
		return ProSval.path.vms(ProEmskey.DASAI.k()) + "markList";
	}


	@RequestMapping(value = "markList2")
	public String markList2(Act act, HttpServletRequest request,HttpServletResponse response, Model model) {
		//查询代办任务
		act.setProcDefKey("multi_task");
		Page<Act> pageForSearch =new Page<Act>(request, response);

		Page<Act> page=actTaskService.todoListForPage(pageForSearch,act);
		model.addAttribute("page",page);
		return ProSval.path.vms(ProEmskey.DASAI.k()) + "markList2";
	}


	@RequestMapping(value = "delete")
	public String delete(OaDasai oaDasai, RedirectAttributes redirectAttributes) {
		oaDasaiService.delete(oaDasai);
		addMessage(redirectAttributes, "删除大赛成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dasai/oaDasai/?repage";
	}

}