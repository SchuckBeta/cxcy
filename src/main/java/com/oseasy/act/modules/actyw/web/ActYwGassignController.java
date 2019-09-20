package com.oseasy.act.modules.actyw.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGrole;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwGroleService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 业务指派表Controller.
 * @author zy
 * @version 2018-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGassign")
public class ActYwGassignController extends BaseController {
	@Autowired
	private ActYwGassignService actYwGassignService;
	@Autowired
	protected ActYwGnodeService actYwGnodeService;
	@Autowired
	private ActYwGroleService actYwGroleService;

	@ModelAttribute
	public ActYwGassign get(@RequestParam(required=false) String id) {
		ActYwGassign entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = actYwGassignService.get(id);
		}
		if (entity == null){
			entity = new ActYwGassign();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(ActYwGassign actYwGassign, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ActYwGassign> page = actYwGassignService.findPage(new Page<ActYwGassign>(request, response), actYwGassign);
		model.addAttribute("page", page);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGassignList";
	}

	@RequestMapping(value = "form")
	public String form(ActYwGassign actYwGassign, Model model) {
		model.addAttribute("actYwGassign", actYwGassign);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGassignForm";
	}

	@RequestMapping(value = "toGassignView")
	public String toGassignView(ActYwGassign actYwGassign, HttpServletRequest request, Model model) {
		String promodelIds=request.getParameter("promodelIds");
		String ywId=request.getParameter(ActYwGroup.JK_ACTYW_ID);
		String gnodeId=request.getParameter(ActYwGroup.JK_GNODE_ID);
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName", secondName);
		}
		ActYwGnode actYwGnode=actYwGnodeService.getByg(gnodeId);
		model.addAttribute("actYwGnode", actYwGnode);
		List<ActYwGrole> roles=actYwGnode.getGroles();
//		JSONObject userJs=userService.findUserJsByRoles(roles);
//		Role role=actYwGnode.getGroles().get(0).getRole();
//		List<User> userList =userService.findListByRoleName(role.getEnname());

		List<Map<String,String>> userLists =actYwGroleService.findListByRoleNames(roles);
		model.addAttribute("promodelIds", promodelIds);
		model.addAttribute("gnodeId", gnodeId);
		model.addAttribute("ywId", ywId);
		model.addAttribute("userList", userLists);
		model.addAttribute("actYwGassign", actYwGassign);
		model.addAttribute("promodelNum", promodelIds.split(",").length);

		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwGassignUser";
	}

	@RequestMapping(value = "getGassignExpert" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult toGagetGassignExpertssignView(HttpServletRequest request, Model model) {
		ApiResult  result = new ApiResult();
		try {
			String promodelIds=request.getParameter("promodelIds");
			String ywId=request.getParameter(ActYwGroup.JK_ACTYW_ID);
			String gnodeId=request.getParameter(ActYwGroup.JK_GNODE_ID);
			ActYwGnode actYwGnode=actYwGnodeService.getByg(gnodeId);
			List<ActYwGrole> roles=actYwGnode.getGroles();
			List<Map<String,String>> userLists =actYwGroleService.findListByRoleNames(roles);
			result.setData(userLists);
			result.setStatus(ApiConst.STATUS_SUCCESS);
			result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
			return result;
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	@RequestMapping(value = "save")
	public String save(ActYwGassign actYwGassign, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, actYwGassign)){
			return form(actYwGassign, model);
		}

		if (StringUtil.isEmpty(actYwGassign.getType())){
		    addMessage(redirectAttributes, "保存失败");
		}

		actYwGassignService.save(actYwGassign);
		addMessage(redirectAttributes, "保存业务指派表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGassign/?repage";
	}

	@RequestMapping(value = "delete")
	public String delete(ActYwGassign actYwGassign, RedirectAttributes redirectAttributes) {
		actYwGassignService.delete(actYwGassign);
		addMessage(redirectAttributes, "删除业务指派表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGassign/?repage";
	}

	@RequestMapping(value = "ajaxGetToDoNumByUser")
	@ResponseBody
	public List<Map<String ,String>> ajaxGetToDoNumByUser(String userIds,String gnodeId) {
		List<String> userIdList= Arrays.asList(userIds.split(","));
		List<Map<String ,String>> listMap=new ArrayList<Map<String ,String>>();
		for(String userId: userIdList){
			Map<String ,String> map=new HashMap<String ,String>();
			String num=actYwGassignService.getToDoNumByUserId(userId,gnodeId);
			User user=UserUtils.get(userId);

			String name= user.getName();
			map.put("userId",userId);
			//map.put("userId",user.getr);
			if(user.getOffice()!=null&& StringUtil.isNotEmpty(user.getOffice().getName())){
				String officeName=user.getOffice().getName();
				map.put("officeName",officeName);
			}else{
				Office office=CoreUtils.getOffice(CoreIds.NCE_SYS_OFFICE_TOP.getId());
				map.put("officeName",office.getName());
			}

			map.put("name",name);
			map.put("num",num);
			listMap.add(map);
		}
		return listMap;
	}
}