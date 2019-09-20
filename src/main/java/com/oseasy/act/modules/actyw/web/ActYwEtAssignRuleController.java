package com.oseasy.act.modules.actyw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.modules.promodel.utils.ActYwUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwEtAssignRuleService;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 专家指派规则Controller.
 * @author zy
 * @version 2019-01-03
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actywAssignRule")
public class ActYwEtAssignRuleController extends BaseController {
	@Autowired
	private ActYwEtAssignRuleService entityService;

	@Autowired
	private ActYwService actYwService;

	@Autowired
	private ActYwGnodeService actYwGnodeService;

	@Autowired
	private ActYwGassignService actYwGassignService;

	@ModelAttribute
	public ActYwEtAssignRule get(@RequestParam(required=false) String id) {
		ActYwEtAssignRule entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ActYwEtAssignRule();
		}
		return entity;
	}

	@RequestMapping(value = {"proTaskList"})
	public String proTaskList(ActYwEtAssignRule entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		ActYwEtAssignRule actYwEtAssignRule=entityService.getByEntity(entity);
//		ActYw actyw=actYwService.get(entity.getActywId());
		ActYw actyw= ActYwUtils.getActYwOfCache(TenantConfig.getCacheTenant(),entity.getActywId());
		ActYwGnode actYwGnode=actYwGnodeService.get(entity.getGnodeId());
		model.addAttribute("proName", actyw.getProProject().getProjectName());
		model.addAttribute("gnodeName", actYwGnode.getName());
		model.addAttribute("actYwEtAssignRule", actYwEtAssignRule);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "proTaskList";
	}

	@RequestMapping(value = {"proProvTaskList"})
	public String proProvTaskList(ActYwEtAssignRule entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		ActYwEtAssignRule actYwEtAssignRule=entityService.getByEntity(entity);
//		ActYw actyw=actYwService.get(entity.getActywId());
		ActYw actyw= ActYwUtils.getActYwOfCache(TenantConfig.getCacheTenant(),entity.getActywId());
		ActYwGnode actYwGnode=actYwGnodeService.get(entity.getGnodeId());
		model.addAttribute("proName", actyw.getProProject().getProjectName());
		model.addAttribute("gnodeName", actYwGnode.getName());
		model.addAttribute("actYwEtAssignRule", actYwEtAssignRule);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "proProvTaskList";
	}

	@RequestMapping(value = {"expertTaskList"})
	public String expertTaskList(ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("actYwEtAssignRule", actYwEtAssignRule);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "expertTaskList";
	}

	@RequestMapping(value = {"expertProvTaskList"})
	public String expertProvTaskList(ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("actYwEtAssignRule", actYwEtAssignRule);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "expertTaskList";
	}


	//得到项目 节点 规则 列表
	@RequestMapping(value = "ajaxGetAssginList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxGetAssginList(ActYwEtAssignTaskVo actYwEtAssignTaskVo, HttpServletRequest request, HttpServletResponse response){
		try {
			Page<ActYwEtAssignTaskVo> page = entityService.findActYwEtAssignTaskVoPage(new Page<ActYwEtAssignTaskVo>(request, response), actYwEtAssignTaskVo);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	//得到项目 节点 规则 列表
	@RequestMapping(value = "ajaxProvGetAssginList", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxProvGetProvAssginList(ActYwEtAssignTaskVo actYwEtAssignTaskVo, HttpServletRequest request, HttpServletResponse response){
		try {
			Page<ActYwEtAssignTaskVo> page = entityService.findActYwEtAssignTaskVoPage(new Page<ActYwEtAssignTaskVo>(request, response), actYwEtAssignTaskVo);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	//得到项目节点 任务数
	@RequestMapping(value = "ajaxOther", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxOther(@RequestBody ActYwEtAssignTaskVo actYwEtAssignTaskVo, HttpServletRequest request, HttpServletResponse response){
		try {
			actYwEtAssignTaskVo= entityService.ajaxOther(actYwEtAssignTaskVo);
			return ApiResult.success(actYwEtAssignTaskVo);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//得到项目节点 任务数
	@RequestMapping(value = "ajaxProvOther", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxProvOther(@RequestBody ActYwEtAssignTaskVo actYwEtAssignTaskVo, HttpServletRequest request, HttpServletResponse response){
		try {
			actYwEtAssignTaskVo= entityService.ajaxOther(actYwEtAssignTaskVo);
			return ApiResult.success(actYwEtAssignTaskVo);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//手动分配专家列表 专家的审核数
	@RequestMapping(value = "ajaxGetManuallyExpertToDoNum", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxGetToDoNumByUser(@RequestBody ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		try {
			Long num=actYwGassignService.getToDoNum(actYwEtAssignTaskVo);
			String todoNum=String.valueOf(num);
			return ApiResult.success(todoNum);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//保存自动属性
	@RequestMapping(value = "ajaxSaveAuto" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxSaveAuto(@RequestBody ActYwEtAssignRule actYwEtAssignRule,HttpServletRequest request) {
		try {
			entityService.save(actYwEtAssignRule);
			return ApiResult.success(actYwEtAssignRule);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

//	//手动分配专家审核
//	@RequestMapping(value = "ajaxGetManuallyAssgin" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//	@ResponseBody
//	public ApiResult ajaxGetManuallyAssgin(@RequestBody ActYwEtAssignRule actYwEtAssignRule,HttpServletRequest request) {
//		try {
//			entityService.ajaxGetManuallyAssgin(actYwEtAssignRule);
//			return ApiResult.success(actYwEtAssignRule);
//		}catch (Exception e){
//			logger.error(ExceptionUtil.getStackTrace(e));
//			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
//		}
//	}

	//得到专家任务数
	@RequestMapping(value = "ajaxGetExpertAssginNum", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxGetExpertAssginNum(@RequestBody ActYwEtAssignTaskVo actYwEtAssignTaskVo, HttpServletRequest request, HttpServletResponse response){
		try {
			actYwEtAssignTaskVo= entityService.ajaxUserTask(actYwEtAssignTaskVo);
			return ApiResult.success(actYwEtAssignTaskVo);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	//保存规则标准
	@RequestMapping(value = "ajaxSave", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxSave(@RequestBody ActYwEtAssignRule actYwEtAssignRule, HttpServletRequest request, HttpServletResponse response){
		try {
			entityService.save(actYwEtAssignRule);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequiresPermissions("actyw:actYwEtAssignRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActYwEtAssignRule entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ActYwEtAssignRule> page = entityService.findPage(new Page<ActYwEtAssignRule>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwEtAssignRuleList";
	}

	@RequiresPermissions("actyw:actYwEtAssignRule:view")
	@RequestMapping(value = "form")
	public String form(ActYwEtAssignRule entity, Model model) {
		model.addAttribute("actYwEtAssignRule", entity);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwEtAssignRuleForm";
	}

	@RequiresPermissions("actyw:actYwEtAssignRule:edit")
	@RequestMapping(value = "save")
	public String save(ActYwEtAssignRule entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		entityService.save(entity);
		addMessage(redirectAttributes, "保存专家指派规则成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwEtAssignRule/?repage";
	}

	@RequiresPermissions("actyw:actYwEtAssignRule:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYwEtAssignRule entity, RedirectAttributes redirectAttributes) {
		entityService.delete(entity);
		addMessage(redirectAttributes, "删除专家指派规则成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwEtAssignRule/?repage";
	}

}