package com.oseasy.act.modules.actyw.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.oseasy.act.modules.actyw.entity.ActYwSgtype;
import com.oseasy.act.modules.actyw.service.ActYwSgtypeService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 状态条件Controller.
 * @author zy
 * @version 2018-02-01
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwSgtype")
public class ActYwSgtypeController extends BaseController {

	@Autowired
	private ActYwSgtypeService actYwSgtypeService;

	@ModelAttribute
	public ActYwSgtype get(@RequestParam(required=false) String id) {
		ActYwSgtype entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = actYwSgtypeService.get(id);
		}
		if (entity == null){
			entity = new ActYwSgtype();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list(ActYwSgtype actYwSgtype, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<ActYwSgtype> page = actYwSgtypeService.findPage(new Page<ActYwSgtype>(request, response), actYwSgtype);
//		model.addAttribute("page", page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwSgtypeList";
	}


	@RequestMapping(value="getActYwSgtypeList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getActYwSgtypeList(ActYwSgtype actYwSgtype, HttpServletRequest request, HttpServletResponse response){
		try {
			Page<ActYwSgtype> page = actYwSgtypeService.findPage(new Page<ActYwSgtype>(request, response), actYwSgtype);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	@RequestMapping(value = "form")
	public String form(ActYwSgtype actYwSgtype, Model model, HttpServletRequest request) {
		model.addAttribute("actYwSgtype", actYwSgtype);
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwSgtypeForm";
	}

	@RequestMapping(value = "save")
	public String save(ActYwSgtype actYwSgtype, Model model, RedirectAttributes redirectAttributes) {
		boolean res = actYwSgtypeService.checkName(actYwSgtype);
		model.addAttribute("actYwSgtype", actYwSgtype);
		if(res){
			actYwSgtypeService.save(actYwSgtype);
			addMessage(redirectAttributes, "保存状态类型成功");
		}else{
			addMessage(redirectAttributes, "状态类型重复");
		}
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwSgtype/list?repage";
	}

	@RequestMapping(value="saveActYwSgtype", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveActYwSgtype(@RequestBody ActYwSgtype actYwSgtype){
		try {
			actYwSgtypeService.save(actYwSgtype);
			return ApiResult.success(actYwSgtype);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="checkActYwSgtypeName", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Boolean checkActYwSgtypeName(@RequestBody ActYwSgtype actYwSgtype){
		return actYwSgtypeService.checkName(actYwSgtype);
	}



	@RequestMapping(value = "saveByStatus")
	public String saveByStatus(ActYwSgtype actYwSgtype, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, actYwSgtype)){
			return form(actYwSgtype, model,request);
		}
		boolean res = actYwSgtypeService.checkName(actYwSgtype);
		if(res){
			actYwSgtypeService.save(actYwSgtype);
			addMessage(redirectAttributes, "保存状态条件成功");
		}else{
			addMessage(redirectAttributes, "状态类型重复");
		}
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwStatus/list?repage";
	}

	@RequestMapping(value = "ajaxSave")
	@ResponseBody
	public JSONObject ajaxSave(ActYwSgtype actYwSgtype, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js= new JSONObject();
		boolean res = actYwSgtypeService.checkName(actYwSgtype);
		if(res){
			actYwSgtypeService.save(actYwSgtype);
			js.put("ret",1);
			js.put("msg","条件类型保存成功");
		}else{
			js.put("ret",0);
			js.put("msg","名称重复");
		}
		return js;
	}


//	@RequestMapping(value = "ajaxGetList")
//	@ResponseBody
//	public JSONObject ajaxGetList() {
//		JSONObject js= new JSONObject();
//		ActYwSgtype actYwSgtype=new ActYwSgtype();
//		List<ActYwSgtype> list=actYwSgtypeService.findList(actYwSgtype);
//		js.put("list",list);
//		return js;
//	}
//

	@RequestMapping(value = "delete")
	public String delete(ActYwSgtype actYwSgtype, RedirectAttributes redirectAttributes) {
		boolean isInUse=actYwSgtypeService.getInuseActYwSgtype(actYwSgtype);
		if(isInUse){
			addMessage(redirectAttributes, "该条件已经被使用");
		}else {
			actYwSgtypeService.delete(actYwSgtype);
			addMessage(redirectAttributes, "删除状态条件成功");
		}
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwSgtype/?repage";
	}

	@RequestMapping(value="delActYwSgtype", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult delActYwSgtype(@RequestBody ActYwSgtype actYwSgtype){
		try {
			boolean isInUse=actYwSgtypeService.getInuseActYwSgtype(actYwSgtype);
			if(isInUse){
				return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"该条件已经被使用");
			}
			actYwSgtypeService.deleteAll(actYwSgtype);
			return ApiResult.success(actYwSgtype);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "checkName")
	@ResponseBody
	public boolean checkName(ActYwSgtype actYwSgtype, RedirectAttributes redirectAttributes) {
		return 	actYwSgtypeService.checkName(actYwSgtype);
	}


}