package com.oseasy.act.modules.actyw.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.service.*;
import com.oseasy.auy.modules.pcore.manager.OpenUtil;
import com.oseasy.com.common.config.*;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.activiti.engine.RepositoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.act.service.ActModelService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPtype;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.actyw.vo.MDSvl;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程关联Controller.
 *
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYw")
public class ActYwController extends BaseController {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ActModelService actModelService;
	@Autowired
	private ActYwService actYwService;
	@Autowired
	private ActYwGtimeService actYwGtimeService;
	@Autowired
	private ActYwGroupService actYwGroupService;
	@Autowired
	private ActYwGnodeService actYwGnodeService;
	@Autowired
	private ActYwYearService actYwYearService;
	@Autowired
	private ProProjectService proProjectService;
	@Autowired
	private SysTenantService tenantService;
	@Autowired
	private ActYwPscrelService actYwPscrelService;
	@Autowired
	private ActYwGroupRelationService actYwGroupRelationService;



	@ModelAttribute
	public ActYw get(@RequestParam(required = false) String id) {
		ActYw entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = actYwService.get(id);
		}
		if (entity == null) {
			entity = new ActYw();
		}
		return entity;
	}

	/**
	 * 项目显示到时间轴.
	 * @param actYw              实体
	 * @param model              模型
	 * @param request            请求
	 * @param redirectAttributes 重定向
	 * @return String
	 */
	@RequiresPermissions("actyw:actYw:edit")
	@RequestMapping(value = "ajaxShowAxis")
	public String ajaxShowAxis(ActYw actYw, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (StringUtil.isNotEmpty(actYw.getId()) && (actYw.getIsShowAxis() != null)) {
			ActYw newActYw = actYwService.get(actYw.getId());

			/**
			 * 修改使用中的证书,如果为true则修改其他的为false.
			 */
			if (actYw.getIsShowAxis()) {
				ActYw queryActYw = new ActYw();
				ActYwGroup group = new ActYwGroup(newActYw.getGroupId());
				group.setType(newActYw.getGroup().getType());
				queryActYw.setGroup(group);
				ProProject pproject = newActYw.getProProject();
				ProProject proProject = new ProProject();
				proProject.setProType(pproject.getProType());
				proProject.setType(pproject.getType());
				proProject.setProCategory(pproject.getProCategory());
				queryActYw.setProProject(proProject);
				queryActYw.setIsShowAxis(true);
				List<ActYw> actYws = actYwService.findList(queryActYw);
				if ((actYws != null) && (actYws.size() > 0)) {
					actYwService.updateIsShowAxisPL(actYws, false);
				}
			}

			newActYw.setIsShowAxis(actYw.getIsShowAxis());
			actYwService.save(newActYw);
			addMessage(redirectAttributes, "更新成功！");
		} else {
			addMessage(redirectAttributes, "更新失败！");
		}
		if (actYw.getGroup() != null) {
			return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
		}
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
	}

	/**
	 * 项目属性.
	 *
	 * @param actYw   实体
	 * @param model   模型
	 * @param request 请求
	 * @return String
	 */
	@ResponseBody
	@RequiresPermissions("actyw:actYw:edit")
	@RequestMapping(value = "ajaxProp")
	public ApiStatus ajaxProp(ActYw actYw, Model model, HttpServletRequest request) {
		if (StringUtil.isNotEmpty(actYw.getId())) {
			ActYw newActYw = get(actYw.getId());
			if (newActYw == null) {
				return new ApiStatus(false, "数据库不存在该项目！");
			}

			if (actYw.getProProject() == null) {
				return new ApiStatus(false, "数据不完整（ProProject）！");
			}

			if (StringUtil.isEmpty(actYw.getGroupId())) {
				return new ApiStatus(false, "关联流程不能为空！");
			}

			ApiStatus rstatus = new ApiStatus(true, "数据更新成功！");
			if (!(actYw.getGroupId()).equals(newActYw.getGroupId())) {
				newActYw.setGroupId(actYw.getGroupId());
				newActYw.setIsDeploy(false);
				rstatus.setMsg("数据更新成功(流程已更新，需要重新发布)！");
			}

			ProProject project = newActYw.getProProject();
			ProProject pproject = actYw.getProProject();
			ActYwGroup group = null;
			if (StringUtil.isNotEmpty(newActYw.getGroupId())) {
				group = actYwGroupService.get(newActYw.getGroupId());
				if ((pproject.getType()).equals(MDSvl.MD_PROJECY_TYPE)) {
					newActYw.setKeyType(FormTheme.F_MD.getKey());
				} else {
					newActYw.setKeyType(FormTheme.getById(group.getTheme()).getKey());
				}
				actYwService.save(newActYw);

				project.setProjectName(pproject.getProjectName());
				project.setProType(pproject.getProType());
				project.setType(pproject.getType());
				project.setProCategorys(pproject.getProCategorys());
				project.setLevel(pproject.getLevel());
				project.setFinalStatus(pproject.getFinalStatus());
				project.setRestCategory(pproject.isRestCategory());
				project.setRestMenu(pproject.isRestMenu());
				project.setYear(pproject.getYear());
				proProjectService.save(project);
			} else {
				newActYw.setIsDeploy(false);
				rstatus.setMsg("流程不能为空！");
			}
			return rstatus;
		}
		return new ApiStatus(false, "唯一标识不能为空！");
	}

	/**
	 * 流程模板推送.
	 * @param id
	 * @return ApiResult
	 */
	@RequestMapping(value="ajaxPushTpl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxPushTpl(String id, HttpServletRequest request, HttpServletResponse response){
		try{
			if(StringUtil.isEmpty(id)){
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "参数不能为空.");
			}

			ActYw curActYw = actYwService.get(id);
			if((curActYw == null) || StringUtil.isEmpty(curActYw.getTenantId()) || StringUtil.isEmpty(curActYw.getGroupId())){
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "当前流程参数不存在.");
			}

			SysTenant curTenant = tenantService.get(curActYw.getTenantId());
			if((curTenant == null) || StringUtil.isEmpty(curTenant.getType())){
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "租户不存在或类型未定义.");
			}
			ActYwGroup group = actYwGroupRelationService.getNscActYwGroupByProv(curActYw.getGroupId());
			if(StringUtil.isEmpty(group.getTenantId()) || StringUtil.isEmpty(group.getId())){
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "关联流程不存在.");
			}

			List<String> scTenantFails  = Lists.newArrayList();
			List<String> scTenantSuccesss  = Lists.newArrayList();
			ActYwPscrel actYwPscrel = new ActYwPscrel();
			actYwPscrel.setIspushed(CoreSval.Const.NO);
			actYwPscrel.setProvinceActywId(curActYw.getId());
			List<ActYwPscrel> actYwPscrels  = actYwPscrelService.findListByActywId(actYwPscrel);
			if(StringUtil.checkEmpty(actYwPscrels)){
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "推送失败,没有指定推送的学校.");
			}

			for (ActYwPscrel cur: actYwPscrels) {
				if((CoreSval.Const.YES).equals(cur.getIspushed())){
					continue;
				}

				if((curTenant.getType()).equals(Sval.EmPn.NCENTER.getPrefix())){
					if(OpenUtil.pushNsc(cur.getSchoolTenantId(), group.getTenantId(), group.getId(), cur.getId())){
						scTenantSuccesss.add(cur.getSchoolTenantId());
					}else{
						scTenantFails.add(cur.getSchoolTenantId());
					}
				}else if((curTenant.getType()).equals(Sval.EmPn.NPROVINCE.getPrefix())){
					if(OpenUtil.pushNsc(cur.getSchoolTenantId(), group.getTenantId(), group.getId(), cur.getId())){
						scTenantSuccesss.add(cur.getSchoolTenantId());
					}else{
						scTenantFails.add(cur.getSchoolTenantId());
					}
				}else if((curTenant.getType()).equals(Sval.EmPn.NSCHOOL.getPrefix())){
					if(OpenUtil.pushNsc(cur.getSchoolTenantId(), group.getTenantId(), group.getId(), cur.getId())){
						scTenantSuccesss.add(cur.getSchoolTenantId());
					}else{
						scTenantFails.add(cur.getSchoolTenantId());
					}
				}
			}
			Map<String, Object> sctsMap = Maps.newHashMap();
			sctsMap.put(CoreSval.Const.TRUE, scTenantSuccesss);
			sctsMap.put(CoreSval.Const.FALSE, scTenantFails);
			if(StringUtil.checkNotEmpty(scTenantSuccesss)){
				return ApiResult.success(sctsMap, "推送成功");
			}
			return ApiResult.failed(sctsMap, ApiConst.STATUS_FAIL, "推送失败.");
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
		}
	}

	/**
	 * 修改审核时间.
	 *
	 * @param actYw   实体
	 * @param model   模型
	 * @param request 请求
	 * @return Rstatus 执行结果状态
	 */
	@ResponseBody
	@RequiresPermissions("actyw:actYw:edit")
	@RequestMapping(value = "ajaxGtime")
	public ApiStatus ajaxGtime(ActYw actYw, Model model, HttpServletRequest request) {
		if (StringUtil.isNotEmpty(actYw.getId())) {
			ActYw newActYw = get(actYw.getId());
			if (newActYw == null) {
				return new ApiStatus(false, "数据库不存在该项目！");
			}

			actYw.setGroupId(newActYw.getGroupId());
			actYw.setRelId(newActYw.getRelId());
			Boolean newYear=false;
			ApiStatus rstatus=null;
			ProProject pproject = actYw.getProProject();
			newYear=true;
			String yearId=actYwYearService.getProByActywIdAndYear(actYw.getId(),pproject.getYear());
			if(StringUtil.isNotEmpty(yearId)){
				newYear=false;
			}
			//判断是否存在，不存在创建新的。
			if(newYear){
				rstatus = actYwService.addGtimeNewYear(pproject.getYear(),actYw, request);
			}else{
				rstatus = actYwService.addGtimeOld(pproject.getYear(),actYw, request);
			}
			if (rstatus.getStatus()) {
				//actYwService.save(actYw);
				ProProject project = newActYw.getProProject();
				if (actYw.getProProject() != null) {
					project.setStartDate(pproject.getStartDate());
					project.setEndDate(pproject.getEndDate());
					project.setNodeState(pproject.getNodeState());
					project.setYear(pproject.getYear());
					proProjectService.save(project);
				}
			}
			return rstatus;
		}
		return new ApiStatus(false, "项目唯一标识不能为空！");
	}

	/**
	 * 跳转修改审核时间页面.
	 *
	 * @param actYw   项目流程
	 * @param model   Model
	 * @param request Http请求
	 * @return String
	 */
	@RequiresPermissions("actyw:actYw:edit")
	@RequestMapping(value = "formGtime")
	public String formGtime(ActYw actYw, Model model, HttpServletRequest request) {
		if (StringUtil.isNotEmpty(actYw.getGroupId())) {
			model.addAttribute("actYwGroup", actYwGroupService.get(actYw.getGroupId()));
		}

		if ((actYw.getProProject() != null) && StringUtil.isNotEmpty(actYw.getProProject().getProType())) {
			FlowProjectType fpType = FlowProjectType.getByKey(StringUtil.removeLastDotH(actYw.getProProject().getProType()));
			model.addAttribute("fpType", fpType);
		}
		String yearId=request.getParameter("yearId");
		ActYwYear actYwYear=actYwYearService.get(yearId);
		model.addAttribute("actYwYear", actYwYear);
		if (StringUtil.isNotEmpty(actYw.getRelId()) && StringUtil.isNotEmpty(actYw.getGroupId())) {
			ActYwGtime actYwGtimeOld = new ActYwGtime();
			actYwGtimeOld.setGrounpId(actYw.getGroupId());
			actYwGtimeOld.setProjectId(actYw.getRelId());
			actYwGtimeOld.setYearId(yearId);
			List<ActYwGtime> gtimes = actYwGtimeService.findList(actYwGtimeOld);
			if(StringUtil.checkEmpty(gtimes)){
			    gtimes = ActYwGnode.convertToGtimes(actYwGnodeService.findListByMenu(new ActYwGnode(new ActYwGroup(actYw.getGroupId()))));
			}
			model.addAttribute("actYwGtimeList", gtimes);
		}
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		model.addAttribute("actYw", actYw);
		return ActSval.path.vms(ActEmskey.ACTYW.k()) + "actYwTimeForm";
	}

	@RequiresPermissions("actyw:actYw:edit")
	@RequestMapping(value = "deploy")
	public String deploy(ActYw actYw, Model model, Boolean isUpdateYw, RedirectAttributes redirectAttributes) {
		if (actYwService.deploy(actYw, repositoryService,isUpdateYw)) {
			addMessage(redirectAttributes, "发布成功, 部署后才能启动申报流程");
		} else {
			addMessage(redirectAttributes, "发布失败");
		}
		if (actYw.getGroup() != null) {
			return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
		}
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
	}


	@ResponseBody
    @RequestMapping(value="ajaxIsCurr", method = RequestMethod.POST, produces = "application/json")
	public ApiResult ajaxIsCurr(String id){
	    try {
	        if(StringUtil.isEmpty(id)){
	            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+": id 不能为空");
	        }
	        ActYw actYw = actYwService.updateCurr(id);
	        actYwService.save(actYw);
            return ApiResult.success(actYw);
	    }catch (Exception e){
	        logger.error(e.getMessage());
	        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
	    }
	}

	@ResponseBody
	@RequestMapping(value="ajaxCheckHasCurr", method = RequestMethod.GET, produces = "application/json")
	public ApiResult ajaxCheckHasCurr(String id, String ptype){
	    try {
	        if(StringUtil.isEmpty(id)){
	            return ApiResult.success(true);
	        }
	        return ApiResult.success(!actYwService.checkHasCurr(id, ptype));
	    }catch (Exception e){
	        logger.error(e.getMessage());
	        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
	    }
	}

	@ResponseBody
	@RequestMapping(value = "changeModel")
	public List<ActYwGnode> findTeamPerson(@RequestParam(required = true) String id, @RequestParam(required = false) Boolean isByg) {
		if (isByg == null) {
			isByg = false;
		}
	/*
     * List<Map<String,String>> list=new ArrayList<Map<String,String>>(); List<Map<String,String>>
     * list1=projectDeclareService.findTeamStudent(id); List<Map<String,String>>
     * list2=projectDeclareService.findTeamTeacher(id); for(Map<String,String> map:list1) {
     * list.add(map); } for(Map<String,String> map:list2) { list.add(map); }
     */
		List<ActYwGnode> sourcelist = new ArrayList<ActYwGnode>();
		if (StringUtil.isNotEmpty(id)) {
			ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(id));
			if (isByg) {
				sourcelist = actYwGnodeService.findListBygMenu(actYwGnode);
			} else {
				sourcelist = actYwGnodeService.findListByMenu(actYwGnode);
			}
		}
		return sourcelist;
	}

	/**
	 * 根据类型获取已发布流程.
	 * 如果类型为空，返回所有已发布流程.
	 *
	 * @param ftype    排除的ID
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<ActYwGroup> listData(@RequestParam(required = false) String ftype, HttpServletResponse response) {
		return actYwGroupService.listData(ftype);
	}

	/**
	 * 根据类型获取已发布项目流程.
	 * 如果类型为空，返回所有已发布流程.
	 *
	 * @param ftype    排除的ID
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "listYwData")
	public List<ActYw> listYwData(@RequestParam(required = false) String ftype, HttpServletResponse response) {
		return actYwService.findListByDeploy(ftype);
	}


	/**
	 * 获取所有的项目.
	 *
	 * @param type     类型
	 * @param isDeploy 是否发布
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "tree")
	public List<Map<String, Object>> tree(@RequestParam(required = false) String type, @RequestParam(required = false) String proType, @RequestParam(required = false) Boolean isDeploy, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		ActYw pactYw = new ActYw();
		ProProject proProject = new ProProject();
		if (StringUtil.isNotEmpty(type)) {
			proProject.setType(type);
		}

		if (StringUtil.isNotEmpty(proType)) {
			proProject.setProType(proType);
		}

		if (StringUtil.isEmpty(type) && StringUtil.isEmpty(proType)) {
			pactYw.setProProject(proProject);
		}

		if (isDeploy != null) {
			pactYw.setIsDeploy(isDeploy);
		}

		List<ActYw> list = actYwService.findList(pactYw);
		List<FlowProjectType> prolist = FlowProjectType.getList();
		for (FlowProjectType pro : prolist) {
			Map<String, Object> dictMap = Maps.newHashMap();
			dictMap.put("id", pro.getValue());
			dictMap.put("pId", CoreIds.NCE_SYS_TREE_PROOT.getId());
			dictMap.put("name", pro.getName());
			mapList.add(dictMap);
			for (ActYw actYw : list) {
				Map<String, Object> certRelMap = Maps.newHashMap();
				ProProject curProProject = actYw.getProProject();
				if ((curProProject == null) || StringUtil.isEmpty(curProProject.getProType())) {
					continue;
				}

				if ((pro.getKey()).equals(StringUtil.replace(curProProject.getProType(), StringUtil.DOTH, StringUtil.EMPTY))) {
					certRelMap.put("id", actYw.getId());
					certRelMap.put("pId", pro.getValue());
					certRelMap.put("name", curProProject.getProjectName());
					mapList.add(certRelMap);
				}
			}
		}
		return mapList;
	}

	/**
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxPctype", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public ApiTstatus<List<Dict>> ajaxPctype(String pctype) {
		List<Dict> projectStyles = DictUtils.getDictList(pctype);
		if(StringUtil.checkNotEmpty(projectStyles)){
			return new ApiTstatus<List<Dict>>(true, "查询成功！", projectStyles);
		}
		return new ApiTstatus<List<Dict>>(false, "查询失败！");
	}

	/**
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxPtype", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public ApiTstatus<List<Dict>> ajaxPtype(String ptype) {
	    List<Dict> projectStyles = DictUtils.getDictList(ptype);
	    if(StringUtil.checkNotEmpty(projectStyles)){
	        return new ApiTstatus<List<Dict>>(true, "查询成功！", projectStyles);
	    }
	    return new ApiTstatus<List<Dict>>(false, "查询失败！");
	}
	/**
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxProjectStyle", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public ApiTstatus<List<Dict>> ajaxProjectStyle() {
	    List<Dict> projectStyles = DictUtils.getDictList("project_style");
	    if(StringUtil.checkNotEmpty(projectStyles)){
	        return new ApiTstatus<List<Dict>>(true, "查询成功！", projectStyles);
	    }
	    return new ApiTstatus<List<Dict>>(false, "查询失败！");
	}

	@ResponseBody
	@RequestMapping(value = "/ajaxCompetitionTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public ApiTstatus<List<Dict>> ajaxCompetitionTypes() {
		List<Dict> competitionTypes = DictUtils.getDictList(FlowPtype.PTT_DASAI.getKey());
		if(StringUtil.checkNotEmpty(competitionTypes)){
			return new ApiTstatus<List<Dict>>(true, "查询成功！", competitionTypes);
		}
		return new ApiTstatus<List<Dict>>(false, "查询失败！");
	}
}
