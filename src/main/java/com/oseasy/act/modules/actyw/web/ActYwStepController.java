package com.oseasy.act.modules.actyw.web;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwStep;
import com.oseasy.act.modules.actyw.service.ActYwStepService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 状态条件Controller.
 * @author zy
 * @version 2018-02-01
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwStep")
public class ActYwStepController extends BaseController {

	@Autowired
	private ActYwStepService actYwStepService;

	@ModelAttribute
	public ActYwStep get(@RequestParam(required=false) String id) {
		ActYwStep entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = actYwStepService.get(id);
		}
		if (entity == null){
			entity = new ActYwStep();
		}
		return entity;
	}


	@RequestMapping(value="getActYwStepByGroupId", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult getActYwStepByGroupId(String groupId) {
	try{
			ActYwStep actYwStep=actYwStepService.getActYwStepByGroupId(groupId);
			return ApiResult.success(actYwStep);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="saveActYwStep", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult saveActYwStep(@RequestBody ActYwStep actYwStep) {
	try{
			actYwStepService.save(actYwStep);
			return ApiResult.success(actYwStep);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="saveStep", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult saveStep(@RequestBody ActYwStep actYwStep) {
	try{
//			actYwStepService.saveStep(actYwStep);
			return ApiResult.success(actYwStep);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = {"list", ""})
	public String list(ActYwStep actYwSgtype, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<ActYwSgtype> page = actYwSgtypeService.findPage(new Page<ActYwSgtype>(request, response), actYwSgtype);
//		model.addAttribute("page", page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwSgtypeList";
	}

	@RequestMapping(value = "form")
	public String form(ActYwStep actYwStep, Model model, HttpServletRequest request) {
		model.addAttribute("actYwStep", actYwStep);
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwSgtypeForm";
	}

	@RequestMapping(value = "save")
	public String save(ActYwStep actYwStep, Model model, RedirectAttributes redirectAttributes) {
		actYwStepService.save(actYwStep);
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwSgtype/list?repage";
	}


	@RequestMapping(value = "ajaxSave")
	@ResponseBody
	public JSONObject ajaxSave(ActYwStep actYwStep, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js= new JSONObject();
		actYwStepService.save(actYwStep);
		return js;
	}

	@RequestMapping(value = "delete")
	public String delete(ActYwStep actYwStep, RedirectAttributes redirectAttributes) {
		actYwStepService.delete(actYwStep);
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwSgtype/?repage";
	}

}