package com.oseasy.act.modules.actyw.web;

import java.util.Arrays;
import java.util.List;

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
import com.oseasy.act.modules.actyw.dao.ActYwSgtypeDao;
import com.oseasy.act.modules.actyw.entity.ActYwSgtype;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.act.modules.actyw.service.ActYwStatusService;
import com.oseasy.act.modules.actyw.tool.process.vo.RegType;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;


/**
 * 节点状态表Controller.
 * @author zy
 * @version 2018-01-15
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwStatus")
public class ActYwStatusController extends BaseController {

	@Autowired
	private ActYwStatusService actYwStatusService;
	private static ActYwSgtypeDao actYwSgtypeDao= SpringContextHolder.getBean(ActYwSgtypeDao.class);
	@ModelAttribute
	public ActYwStatus get(@RequestParam(required=false) String id) {
		ActYwStatus entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = actYwStatusService.get(id);
		}
		if (entity == null){
			entity = new ActYwStatus();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list(ActYwStatus actYwStatus, HttpServletRequest request, HttpServletResponse response, Model model) {
//		Page<ActYwStatus> page = actYwStatusService.findPage(new Page<ActYwStatus>(request, response), actYwStatus);
//		model.addAttribute("page", page);
//        model.addAttribute("regTypes", Arrays.asList(RegType.values()));
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwStatusList";
	}

	@RequestMapping(value="getRegTypes", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getRegTypes(){
		String regTypes = RegType.getAll().toString();
		return ApiResult.success(regTypes);
	}

	@RequestMapping(value="getActYwStatusList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getActYwSgtypeList(ActYwStatus actYwStatus, HttpServletRequest request, HttpServletResponse response){
		try {
			Page<ActYwStatus> page = actYwStatusService.findPage(new Page<ActYwStatus>(request, response), actYwStatus);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="getActYwSgtypeAllList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getActYwSgtypeAllList(){
		try {
		List<ActYwSgtype> actYwSgtypeList = actYwSgtypeDao.findList(new ActYwSgtype());
			return ApiResult.success(actYwSgtypeList);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,ActYwStatus actYwStatus, Model model) {
		if(actYwStatus.getId()!=null && (RegType.RT_GE.getId()).equals(actYwStatus.getRegType())){
			String alias=actYwStatus.getAlias();
			String startNum=alias.substring(0,StringUtil.lastIndexOf(alias, StringUtil.LINE_M));
			model.addAttribute("startNum",startNum);
			String endNum=alias.substring(StringUtil.lastIndexOf(alias, StringUtil.LINE_M)+1);
			model.addAttribute("endNum",endNum);
		}
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		model.addAttribute("actYwStatus", actYwStatus);
        model.addAttribute("regTypes", Arrays.asList(RegType.values()));
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwStatusForm";
	}



	@RequestMapping(value = "saveStatus")
	@ResponseBody
	public Boolean saveStatus(ActYwStatus actYwStatus) {
		return actYwStatusService.saveStatus(actYwStatus);
	}

	@RequestMapping(value="delActYwStatus", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult delActYwStatus(@RequestBody ActYwStatus actYwStatus){
		try {
			Boolean res = actYwStatusService.deleteStatus(actYwStatus.getId());
			if(res){
				return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"该节点状态已经被使用");
			}
			return ApiResult.success(actYwStatus);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}

	}

	@RequestMapping(value = "deleteStatus")
	@ResponseBody
	public JSONObject deleteStatus(String statusId,String gnodeId) {
		boolean res=actYwStatusService.deleteStatusWithGnodeId(statusId,gnodeId);
		JSONObject js=new JSONObject();

		if(res){
			js.put("res","0");
			js.put("msg","该节点状态已经被使用");
		}else {
			js.put("res","1");
			js.put("msg","该节点状态删除成功");
		}
		return js;
	}

	@RequestMapping(value = "checkState")
	@ResponseBody
	public Boolean checkState(ActYwStatus actYwStatus, Model model, RedirectAttributes redirectAttributes) {
		return actYwStatusService.checkState(actYwStatus);
	}




	@RequestMapping(value = "save")
	public String save(HttpServletRequest request,ActYwStatus actYwStatus, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actYwStatus)){
			return form(request,actYwStatus, model);
		}
		if(StringUtil.isEmpty(actYwStatus.getId())){
			int num=actYwStatusService.getStatusValue(actYwStatus.getGtype());
			actYwStatus.setStatus(String.valueOf(num));
		}
		actYwStatusService.save(actYwStatus);
		addMessage(redirectAttributes, "保存节点状态表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwStatus/?repage";
	}

	@RequestMapping(value = "saveActYwStatus", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveActYwStatus(@RequestBody ActYwStatus actYwStatus){
		try {
			if(StringUtil.isEmpty(actYwStatus.getId())){
				int num=actYwStatusService.getStatusValue(actYwStatus.getGtype());
				actYwStatus.setStatus(String.valueOf(num));
			}
			actYwStatusService.save(actYwStatus);
			return ApiResult.success(actYwStatus);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

//	@ResponseBody
//	public JSONObject ajaxSave(ActYwStatus actYwSgtype, Model model, RedirectAttributes redirectAttributes) {
//		JSONObject js= new JSONObject();
//		boolean res = actYwSgtypeService.checkName(actYwSgtype);
//		if(res){
//			actYwSgtypeService.save(actYwSgtype);
//			js.put("ret",1);
//			js.put("msg","条件类型保存成功");
//		}else{
//			js.put("ret",0);
//			js.put("msg","名称重复");
//		}
//		return js;
//	}

	@RequestMapping(value = "saveCondition")
	@ResponseBody
	public JSONObject saveCondition(ActYwStatus actYwStatus, Model model, RedirectAttributes redirectAttributes) {
		JSONObject response = new JSONObject();
		if (!beanValidator(model, actYwStatus)){
			response.put("res", 0);
			response.put("msg", "条件状态，添加失败");
			return response;
		}
		if(StringUtil.isEmpty(actYwStatus.getId())){
			int num=actYwStatusService.getStatusValue(actYwStatus.getGtype());
			actYwStatus.setStatus(String.valueOf(num));
		}
		actYwStatusService.save(actYwStatus);
		response.put("res", 1);
		response.put("msg", "条件状态，添加成功");
//		addMessage(redirectAttributes, "保存节点状态表成功");
		return response;
	}


	@RequestMapping(value = "delete")
	public String delete(ActYwStatus actYwStatus, RedirectAttributes redirectAttributes) {
		boolean res=actYwStatusService.deleteStatus(actYwStatus.getId());
		if(res){
			addMessage(redirectAttributes, "该节点状态已经被使用");
		}else {
			addMessage(redirectAttributes, "删除节点状态表成功");
		}
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwStatus/?repage";
	}

}