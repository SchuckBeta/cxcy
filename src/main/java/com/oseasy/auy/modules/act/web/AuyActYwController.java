package com.oseasy.auy.modules.act.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.act.common.config.IActPn;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.service.*;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.activiti.engine.RepositoryService;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActModelService;
import com.oseasy.act.modules.actyw.tool.apply.IAconfig;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.auy.modules.act.service.AuyActYwService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.pro.modules.gcontest.vo.GContestNodeVo;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FlowYwId;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.entity.SysNumberRuleDetail;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import com.oseasy.sys.modules.sys.utils.SysEnumUtils;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 项目流程关联Controller.
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYw")
public class AuyActYwController extends BaseController {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ActModelService actModelService;
	@Autowired
	private ActYwService actYwService;
    @Autowired
    private ProModelService proModelService;
	@Autowired
	private AuyActYwService auyActYwService;
    @Autowired
    private ActYwGtimeService actYwGtimeService;
    @Autowired
    private ActYwGroupService actYwGroupService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ProProjectService proProjectService;
    @Autowired
    private SysNumberRuleService sysNumberRuleService;
    @Autowired
    private ActYwPscrelService actYwPscrelService;
    @Autowired
    private ActYwScstepService actYwScstepService;
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

    @RequiresPermissions("actyw:actYw:view")
    @RequestMapping(value = "form")
    public String form(ActYw actYw, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        FlowType flowType = null;
        ActYw projectActYw = null;
        ActYw gcontestActYw = null;
        FlowProjectType[] flowProjectTypes = null;
        if ((actYw != null) && (actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
            flowType = FlowType.getByKey(actYw.getGroup().getFlowType());
            flowProjectTypes = flowType.getType().getTypes();
        } else {
            flowProjectTypes = FlowProjectType.values();
        }

        List<ActYwGroup> actYwGroups = null;
        if (flowType == null) {
            actYwGroups = actYwGroupService.findListByDeploy();
        } else {
            if ((flowType).equals(FlowType.FWT_XM)) {
                projectActYw = actYwService.get(ProjectNodeVo.YW_ID);

                if (projectActYw == null) {
                    addMessage(redirectAttributes, "项目数据不完整，固定大创项目不存在！");
                    if ((actYw != null) && (actYw.getGroup() != null)) {
                        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
                    }
                    return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
                }
            } else if ((flowType).equals(FlowType.FWT_DASAI)) {
                gcontestActYw = actYwService.get(GContestNodeVo.YW_ID);

                if (gcontestActYw == null) {
                    addMessage(redirectAttributes, "大赛数据不完整，固定大赛不存在！");
                    if (actYw.getGroup() != null) {
                        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
                    }
                    return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
                }
            } else if ((flowType).equals(FlowType.FWT_SCORE)) {

            }
            model.addAttribute(IAconfig.ACONFIG, IAconfig.genAconfig(flowType));
            actYwGroups = actYwGroupService.findListByDeploy(flowType);
        }

        /**
         * 新增项目时，不能选择固定的流程.
         */
        if (actYw.getIsNewRecord()) {
            actYwGroups = FlowYwId.filterGid(actYwGroups);
            if((Sval.EmPn.NCENTER.getPrefix()).equals(CoreSval.getCurrTenantType(TenantConfig.getConfig()))){
                ActYwGroupRelation groupRelation = new ActYwGroupRelation();
                List<ActYw> curActYws = Lists.newArrayList();
//                curActYws.addAll(actYwGroupRelationService.findListHasYrel(groupRelation));
//                curActYws.addAll(actYwService.findList(new ActYw()));//解决Bug 11793问题，已创建项目、大赛的流程不能再被选中
                actYwGroups = FlowYwId.filterActYw(actYwGroups,
                        StringUtil.sqlInByListIdss(actYwGroupRelationService.findListHasGrel(groupRelation)),
                        curActYws);
            }

            if (StringUtil.isEmpty(actYw.getShowTime())) {
                actYw.setShowTime(Const.SHOW);
            }
        }

        /**
         * 当执行修改操作时，实现图片信息回显.
         */
        if (!actYw.getIsNewRecord()) {
            if ((actYw.getProProject() != null) && StringUtil.isNotEmpty(actYw.getProProject().getMenuRid())) {
                Menu menu = systemService.getMenuById(actYw.getProProject().getMenuRid());
                if (menu != null) {
                    actYw.getProProject().setImgUrl(menu.getImgUrl());
                }
            }
        }
        if (actYw.getRelId() != null && actYw.getGroupId() != null) {
            ActYwGtime actYwGtimeOld = new ActYwGtime();
            actYwGtimeOld.setGrounpId(actYw.getGroupId());
            actYwGtimeOld.setProjectId(actYw.getRelId());
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
        model.addAttribute("promdActYw", actYwService.get(FlowYwId.FY_P_MD.getId()));
        model.addAttribute("projectActYw", projectActYw);
        model.addAttribute("gcontestActYw", gcontestActYw);
        model.addAttribute("flowYwId", FlowYwId.values());
        model.addAttribute("projectMarks", FlowProjectType.values());
        model.addAttribute("actYw", actYw);
        model.addAttribute("flowProjectTypes", flowProjectTypes);
        model.addAttribute("actYwGroups", actYwGroups);
        return IActPn.curPt().actywForm(Sval.EmPt.TM_ADMIN);
    }

    /**
     * 项目预发布.
     * @param actYw              实体
     * @param model              模型
     * @param request            请求
     * @param redirectAttributes 重定向
     * @return String
     */
    @RequiresPermissions("actyw:actYw:edit")
    @RequestMapping(value = "ajaxPreRelease")
    public String ajaxPreRelease(ActYw actYw, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, actYw)) {
            return form(actYw, model, redirectAttributes, request);
        }
        if ((actYw != null)) {
            if((!actYw.getIsNewRecord()) && StringUtil.isNotEmpty(actYw.getId()) && (!actYw.getIsPreRelease())){
                proModelService.clearPreReleaseData(actYw.getId());
            }

            if(ActYw.checkNeedNum(actYw)){
                //查询是否设置编号
                SysNumberRule sysNumberRule=sysNumberRuleService.getRuleByAppType(actYw.getId(),"");
                if(sysNumberRule==null){
                    addMessage(redirectAttributes, "请设置编号规则");
                    return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
                }
                //如果编号中有起始值 清零
                List<SysNumberRuleDetail> SysNumberRuleDetailList=sysNumberRuleService.findSysNumberRuleDetailList(sysNumberRule.getId());
                if(StringUtil.checkNotEmpty(SysNumberRuleDetailList)){
                    for(SysNumberRuleDetail sysNumberRuleDetail:SysNumberRuleDetailList){
                        if(sysNumberRuleDetail.getRuleType().equals(SysEnumUtils.CUSTOM_FLAG)){
                            if(StringUtil.isNotEmpty(sysNumberRuleDetail.getText())){
                                sysNumberRule.setIncreNum(Integer.parseInt(sysNumberRuleDetail.getText()));
                                sysNumberRuleService.update(sysNumberRule);
                            }
                        }
                    }
                }
            }
            actYwService.save(actYw);
            addMessage(redirectAttributes, "更新成功");
            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
            }
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
    }

    @RequiresPermissions("actyw:actYw:edit")
    @RequestMapping(value = "save")
    public String save(ActYw actYw, Model model, Boolean isUpdateYw, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, actYw)) {
            return form(actYw, model, redirectAttributes, request);
        }
        if ((actYw != null)) {
            if (StringUtil.isEmpty(actYw.getId())) {
                actYw.setIsNewRecord(true);
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroupId())) {
                actYw.setGroup(actYwGroupService.get(actYw.getGroupId()));
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                FlowType flowType = FlowType.getByKey(actYw.getGroup().getFlowType());
                StringBuffer proType = new StringBuffer();
                for (FlowProjectType fptype : flowType.getType().getTypes()) {
                    proType.append(fptype.getKey());
                    proType.append(",");
                }
                actYw.getProProject().setProType(proType.toString());
            }

            Boolean isTrue = auyActYwService.saveDeployTime(actYw, repositoryService, isUpdateYw, request);
            if (isTrue) {
                addMessage(redirectAttributes, "保存成功");
            } else {
                addMessage(redirectAttributes, "保存失败");
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
            }
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
    }

    @RequestMapping(value="updateProvPush", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult updateProvPush(@RequestBody ActYwScstep actYwScstep) {
    try{
            actYwScstepService.save(actYwScstep);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="stepList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult stepList() {
        try{
            List<ActYwScstep> stepList=actYwScstepService.stepList();
            return ApiResult.success(stepList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="getStep", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult getStep(String id) {
    try{
            Map<String,Object> map=new HashMap<>();
            map=actYwService.getStep(id);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

//    @RequestMapping(value = "saveRelActyw")
    @RequestMapping(value="saveRelActyw", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
   	@ResponseBody
    public ApiResult saveRelActyw(@RequestBody ActYw actYw, Model model, Boolean isUpdateYw, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try{
            if (!beanValidator(model, actYw)) {
    //            return form(actYw, model, redirectAttributes, request);
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE));
            }
            if ((actYw != null)) {
                auyActYwService.saveActywRel(actYw);
                if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                    return ApiResult.success(actYw.getId());
    //                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
                }
            }
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxCheckDeploy")
    public JSONObject ajaxCheckDeploy(ActYw actYw, Model model, Boolean isUpdateYw, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isNotEmpty(actYw.getId()) && (actYw.getIsDeploy() != null)) {
            if (actYw.getIsDeploy()) {
                String relId = actYw.getRelId();
                ProProject proProject = proProjectService.get(relId);
                //查询是否设置编号
                SysNumberRule sysNumberRule = sysNumberRuleService.getRuleByAppType(actYw.getId(),"");
                if(sysNumberRule==null && ActYw.checkNeedNum(actYw)){
                    js.put("ret", 0);
                    js.put("msg", "该类型项目没有设置编号规则，发布失败");
                    return js;
                }

                List<ActYw> actYwList = actYwService.findActYwListByProProject(proProject.getProType(), proProject.getType(),actYw.getTenantId());
                if (StringUtil.checkNotEmpty(actYwList)) {
                    js.put("ret", 0);
                    js.put("msg", "同一类型项目只能发布一个，该类型项目已经发布一个");
                    return js;
                }
                //如果编号中有起始值 清零
                List<SysNumberRuleDetail> SysNumberRuleDetailList = sysNumberRuleService.findSysNumberRuleDetailList(sysNumberRule.getId());
                if(StringUtil.checkNotEmpty(SysNumberRuleDetailList)){
                    for(SysNumberRuleDetail sysNumberRuleDetail:SysNumberRuleDetailList){
                        if(sysNumberRuleDetail.getRuleType().equals(SysEnumUtils.CUSTOM_FLAG)){
                            if(StringUtil.isNotEmpty(sysNumberRuleDetail.getText())){
                                sysNumberRule.setIncreNum(Integer.parseInt(sysNumberRuleDetail.getText()));
                                sysNumberRuleService.update(sysNumberRule);
                            }
                        }
                    }
                }
                js.put("ret", 1);
            } else {
                //流程状态
                if(actYw.getIsPreRelease()){
                    js.put("ret", 1);
                }else {
                    String res = auyActYwService.findStateHaveData(actYw.getId());
                    if ("3".equals(res)) {
                        js.put("ret", 0);
                        js.put("msg", "流程中含有未完成项目");
                    }
                }
            }
        }
        return js;
    }


    /**
     * 项目发布.
     * @param actYw              实体
     * @param model              模型
     * @param request            请求
     * @param redirectAttributes 重定向
     * @return String
     */
    @RequiresPermissions("actyw:actYw:edit")
    @RequestMapping(value = "ajaxDeploy")
    public String ajaxDeploy(ActYw actYw, Model model, Boolean isUpdateYw, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        //等验证发布时候放开
        ApiTstatus<ActYw> js = auyActYwService.ajaxCheckDeploy(actYw);
        if (js.getStatus()) {
            if (StringUtil.isNotEmpty(actYw.getId()) && (actYw.getIsDeploy() != null)) {
                ActYw newActYw = actYwService.get(actYw.getId());
                newActYw.setIsDeploy(actYw.getIsDeploy());
                if(newActYw.getIsDeploy()){
                    newActYw.setIsPreRelease(true);
                }
                return save(newActYw, model, isUpdateYw, request, redirectAttributes);
            }
        } else {
            addMessage(redirectAttributes, js.getMsg());
        }
        if (actYw.getGroup() != null) {
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
        }
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
    }

	@RequiresPermissions("actyw:actYw:edit")
	@RequestMapping(value = "delete")
	public String delete(ActYw actYw, RedirectAttributes redirectAttributes) {
		ActYw newActYw = new ActYw();
		newActYw.setGroup(actYw.getGroup());
		try {
            ApiTstatus<ActYw> rstats = auyActYwService.ajaxCheckDelete(actYw);
			if (rstats.getStatus()) {
				auyActYwService.deleteAll(actYw);
				addMessage(redirectAttributes, "删除成功");
			} else {
				addMessage(redirectAttributes, rstats.getMsg());
			}
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
		}
		redirectAttributes.addAttribute("actYw", newActYw);
		return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/list?repage&group.flowType=" + newActYw.getGroup().getFlowType();
	}

	@RequestMapping(value="delActYw", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult delActYw(@RequestBody ActYw actYw){
		try {
            if((actYw == null) || (actYw.getGroup() == null)){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "数据不存在或已被删除");
            }
			ActYw newActYw = new ActYw();
			newActYw.setGroup(actYw.getGroup());
            ApiTstatus<ActYw> rstats = auyActYwService.ajaxCheckDelete(actYw);
			if(rstats.getStatus()){
				auyActYwService.deleteAll(actYw);
				return ApiResult.success(actYw);
			}
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, rstats.getMsg());
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

    //查询项目发布给了多少学校
    @RequestMapping(value="getSchoolByActYw", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult getSchoolByActYw(@RequestBody ActYw actYw){
        try {
            List<ActYwPscrel> actYwPscrelList=actYwPscrelService.getSchoolByActYw(actYw.getId());
            return ApiResult.success(actYwPscrelList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    //自定义 -发布到学校 -发布
    @RequestMapping(value="relationModelAndSchool", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult relationModelAndSchool(ActYw actYw ,String schools){
        try {
            String[] list =  schools.split(",");
            List<String> schoolList=Lists.newArrayList(list);
            actYwService.saveRelation(actYw,schoolList);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //查询模板流程(校流程)
    @RequestMapping(value="getActywByModel", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getActywByModel(String proType){
        try {
            return ApiResult.success(actYwGroupService.getActYwGroupByModel(proType));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //查询模板流程 以及节点
    @RequestMapping(value="getModelGroupYwByProv", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getModelActYwByProv(String groupId){
        try {
            Map<String, Object> map=new HashedMap<>();
            //得到模板流程
            ActYwGroup actYwGroup=actYwService.getModelActYwByProv(groupId);
            map.put("actYwGroup",actYwGroup);
            List<ActYwGnode> gnodeList = new ArrayList<ActYwGnode>();
            if (actYwGroup!=null) {
                ActYwGnode actYwGnode = new ActYwGnode(actYwGroup);
                gnodeList = actYwGnodeService.findListBygMenu(actYwGnode);
                map.put("gnodeList",gnodeList);
            }
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    public boolean saveJson(ActYw actYw, Model model, Boolean isUpdateYw, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Boolean isTrue=false;
        if ((actYw != null)) {
            if (StringUtil.isEmpty(actYw.getId())) {
                actYw.setIsNewRecord(true);
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroupId())) {
                actYw.setGroup(actYwGroupService.get(actYw.getGroupId()));
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                FlowType flowType = FlowType.getByKey(actYw.getGroup().getFlowType());
                StringBuffer proType = new StringBuffer();
                for (FlowProjectType fptype : flowType.getType().getTypes()) {
                    proType.append(fptype.getKey());
                    proType.append(",");
                }
                actYw.getProProject().setProType(proType.toString());
            }

            isTrue = auyActYwService.saveDeployTime(actYw, repositoryService, isUpdateYw, request);
//          if (isTrue) {
//              addMessage(redirectAttributes, "保存成功");
//          } else {
//              addMessage(redirectAttributes, "保存失败");
//          }
        }
        return isTrue;
    }

    /*自定义项目大赛保存 王清腾*/
    @ResponseBody
    @RequestMapping(value = "ajaxUserDefinedSave")
    public ApiTstatus<String> ajaxUserDefinedSave(ActYw actYw, Model model, Boolean isUpdateYw, HttpServletRequest request) {
        ApiTstatus<String> req = new ApiTstatus<String>();

        if (!beanValidator(model, actYw)) {
            req.setStatus(false);
            req.setMsg("验证不通过，保存失败");
            return req;
        }
        if ((actYw != null)) {
            if (StringUtil.isEmpty(actYw.getId())) {
                actYw.setIsNewRecord(true);
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroupId())) {
                actYw.setGroup(actYwGroupService.get(actYw.getGroupId()));
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                FlowType flowType = FlowType.getByKey(actYw.getGroup().getFlowType());
                StringBuffer proType = new StringBuffer();
                for (FlowProjectType fptype : flowType.getType().getTypes()) {
                    proType.append(fptype.getKey());
                    proType.append(",");
                }
                actYw.getProProject().setProType(proType.toString());
            }

            Boolean isTrue = auyActYwService.saveDeployTime(actYw, repositoryService, isUpdateYw, request);
            if (isTrue) {
                req.setDatas(actYw.getId());
                req.setStatus(true);
                req.setMsg("保存成功");
            } else {
                req.setStatus(false);
                req.setMsg("保存失败");
            }

            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                req.setDatas(actYw.getId());
                req.setStatus(true);
                req.setMsg("保存成功");
            }
        }else {
            req.setStatus(false);
            req.setMsg("保存失败");
        }
        return req;
    }

    @RequestMapping(value = "ajaxCheckDelete")
    public JSONObject ajaxCheckDelete(ActYw actYw, Model model, Boolean isUpdateYw, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isNotEmpty(actYw.getId()) && (actYw.getIsDeploy() != null)) {
            //流程状态
            String res = auyActYwService.findStateHaveData(actYw.getId());
            if ("3".equals(res)) {
                js.put("ret", "0");
                js.put("msg", "流程中含有未完成项目");
            } else if ("2".equals(res)) {
                js.put("ret", "0");
                js.put("msg", "流程中含有项目");
            } else {
                actYwService.delete(actYw);
                js.put("ret", "1");
                js.put("msg", "删除成功");
            }
        }
        return js;
    }

    @RequestMapping(value = "ajaxDeployJson", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxDeployJson(@RequestBody ActYw actYw, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try{
            //等验证发布时候放开
//            ActYw newActYw = actYwService.get(actYw.getId());
            ActYw newActYw = actYwService.get(actYw.getId());
//            if (newActYw != null){
//                newActYw.setIsNewRecord(false);
//            }
            newActYw.setIsDeploy(actYw.getIsDeploy());
            if(actYw.getIsPreRelease()!=null){
                newActYw.setIsPreRelease(actYw.getIsPreRelease());
            }
            if(actYw.getIsUpdateYw()!=null){
                newActYw.setIsUpdateYw(actYw.getIsUpdateYw());
            }

            ApiTstatus<ActYw> js = auyActYwService.ajaxCheckDeploy(newActYw);
            if (js.getStatus()) {
                if (StringUtil.isNotEmpty(actYw.getId()) && (actYw.getIsDeploy() != null)) {
//                  ActYw newActYw = actYwService.get(actYw.getId());

                    if(newActYw.getIsDeploy()){
                        newActYw.setIsPreRelease(true);
                    }
                    Boolean isTrue= saveJson(newActYw, model, newActYw.getIsUpdateYw(), request, redirectAttributes);
                    if(!isTrue){
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"保存失败");
                    }
                }
            } else {
                return ApiResult.failed(ApiConst.CODE_SYSTEM_ERROR, js.getMsg());
            }
            return ApiResult.success();
        }catch (Exception e){
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+((e.getMessage() == null)?"":e.getMessage()));
        }
    }
}
