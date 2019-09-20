package com.oseasy.act.modules.actyw.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.common.config.IActPn;
import com.oseasy.act.modules.actyw.entity.ActYwGroupRelation;
import com.oseasy.act.modules.actyw.service.ActYwGroupRelationService;
import com.oseasy.com.common.config.Sval;
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

import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwGtheme;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.service.ActYwGthemeService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义流程Controller
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGroup")
public class ActYwGroupController extends BaseController {
	@Autowired
	private ActYwGroupService actYwGroupService;
	@Autowired
	private ActYwGthemeService actYwGthemeService;
	@Autowired
	private ActYwGroupRelationService actYwGroupRelationService;
	@ModelAttribute
	public ActYwGroup get(@RequestParam(required=false) String id) {
		ActYwGroup entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = actYwGroupService.get(id);
		}
		if (entity == null) {
			entity = new ActYwGroup();
		}
		return entity;
	}

	@RequiresPermissions("actyw:actYwGroup:view")
	@RequestMapping(value = {"list", ""})
	public String list(ActYwGroup actYwGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
//	    Page<ActYwGroup> page = actYwGroupService.findPageByCount(new Page<ActYwGroup>(request, response), actYwGroup);
//		model.addAttribute("page", page);
//		ActYwGtheme pactYwGtheme = new ActYwGtheme();
//        pactYwGtheme.setEnable(true);
//        model.addAttribute(FormTheme.FLOW_THEMES, actYwGthemeService.findList(pactYwGtheme));
//        model.addAttribute(FlowYwId.FLOW_YWIDS, FlowYwId.getAll());
//        model.addAttribute(FlowType.FLOW_TYPES, FlowType.values());
		return IActPn.curPt().groupList(Sval.EmPt.TM_ADMIN);
	}

	@RequestMapping(value = "getActYwGroupList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getActYwGroupList(ActYwGroup actYwGroup, HttpServletRequest request, HttpServletResponse response){
		try {
			Page<ActYwGroup> page = actYwGroupService.findPageByCount(new Page<ActYwGroup>(request, response), actYwGroup);
			return ApiResult.success(page);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	// 关系省平台的groupId 和 校模板的groupId
	@RequestMapping(value="relProvSchool", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult relProvSchool(String provGroupId,String modelGroupId){
		try {
			ActYwGroupRelation actYwGroupRelation=new ActYwGroupRelation();
			actYwGroupRelation.setProvGroupId(modelGroupId);
			List<ActYwGroupRelation> list=actYwGroupRelationService.findList(actYwGroupRelation);
			if(StringUtil.checkNotEmpty(list)){
				return ApiResult.failed(ApiConst.CODE_MODEL_ERROR,ApiConst.getErrMsg(ApiConst.CODE_MODEL_ERROR));
			}
			actYwGroupRelationService.saveByRelation(provGroupId,modelGroupId);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	// 查询省流程 模板流程
	@RequestMapping(value="getGroupByType", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getGroupByType(){
		try {
			List<ActYwGroup> list=actYwGroupService.getByTanentId();
			return ApiResult.success(list);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value="getFlowThemes", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getFlowThemes(){
		try {
			ActYwGtheme pactYwGtheme = new ActYwGtheme();
			pactYwGtheme.setEnable(true);
			List<ActYwGtheme> list = actYwGthemeService.findList(pactYwGtheme);
			return ApiResult.success(list);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}
	@RequestMapping(value="getFlowTypes", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getFlowTypes(){
		try {
//			HashMap<String, Object> hashMap = new HashMap<>();
//			hashMap.put(FlowType.FLOW_TYPES, Arrays.asList(FlowType.values()).toString());
			return ApiResult.success(Arrays.asList(FlowType.values()).toString());
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequiresPermissions("actyw:actYwGroup:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYwGroup actYwGroup, RedirectAttributes redirectAttributes) {
		try {
			actYwGroupService.deleteCheck(actYwGroup);
			addMessage(redirectAttributes, "删除自定义流程成功");
		} catch (Exception e){
			addMessage(redirectAttributes, "删除自定义流程失败");
		}
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGroup/?repage";
	}
	@RequestMapping(value = "delActYwGroup", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult delActYwGroup(@RequestBody ActYwGroup actYwGroup){
		try {
			if((actYwGroup == null) || StringUtil.isEmpty(actYwGroup.getId())){
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "数据不存在或已被删除");
			}
			ApiTstatus<ActYwGroup> rstatus = actYwGroupService.deleteCheck(actYwGroup);
			if(rstatus.getStatus()){
				return ApiResult.success(actYwGroup);
			}
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,rstatus.getMsg());
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

}