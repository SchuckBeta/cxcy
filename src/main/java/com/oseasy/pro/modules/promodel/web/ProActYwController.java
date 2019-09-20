package com.oseasy.pro.modules.promodel.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.common.config.IActPn;
import com.oseasy.com.common.config.Sval;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.apply.IAconfig;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.gcontest.vo.GContestNodeVo;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FlowYwId;
import com.oseasy.pro.modules.promodel.utils.ProActYwUtils;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.entity.SysNumberRuleDetail;
import com.oseasy.sys.modules.sys.service.SysNumberRuleService;
import com.oseasy.sys.modules.sys.utils.SysEnumUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程关联Controller.
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYw")
public class ProActYwController extends BaseController {
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ActYwGroupService actYwGroupService;
    @Autowired
    private SysNumberRuleService sysNumberRuleService;

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
    @RequestMapping(value = {"list", ""})
    public String list(ActYw actYw, HttpServletRequest request, HttpServletResponse response, Model model) {
        FlowType flowType = null;
        FlowProjectType[] flowProjectTypes = null;
        if ((actYw != null)) {
            if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
                flowType = FlowType.getByKey(actYw.getGroup().getFlowType());
                flowProjectTypes = flowType.getType().getTypes();
            }

            //Page<ActYw> page = actYwService.findPage(new Page<ActYw>(request, response), actYw);

            Page<ActYw> page = actYwService.fistPageByYear(new Page<ActYw>(request, response), actYw);

            model.addAttribute("page", page);
            model.addAttribute("flowProjectTypes", flowProjectTypes);
            //model.addAttribute("flowType", flowType);
            model.addAttribute("actYw", actYw);
            model.addAttribute(IAconfig.ACONFIG, IAconfig.genAconfig(flowType));
        }
        model.addAttribute("ywpId", ProjectNodeVo.YW_ID);
        model.addAttribute("ywgId", GContestNodeVo.YW_ID);
        return IActPn.curPt().actywList(Sval.EmPt.TM_ADMIN);
    }

    @ResponseBody
    @RequestMapping(value = "ajaxPreReleaseJson")
    public ApiResult ajaxPreReleaseJson(@RequestBody ActYw actYw, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try{
            if ((actYw != null)) {
                ActYw actYwOld=actYwService.get(actYw.getId());
                actYwOld.setIsPreRelease(actYw.getIsPreRelease());
                actYw=actYwOld;
                if((!actYw.getIsNewRecord()) && StringUtil.isNotEmpty(actYw.getId()) && (!actYw.getIsPreRelease())){
                    proModelService.clearPreReleaseData(actYw.getId());
                }

                if(ActYw.checkNeedNum(actYw)){
                  //查询是否设置编号
                    SysNumberRule sysNumberRule=sysNumberRuleService.getRuleByAppType(actYw.getId(),"");
                    if(sysNumberRule==null){
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"请设置编号规则");
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
                return ApiResult.success();
            }else {
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"项目信息不完整");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "getActYw", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getActYw(ActYw actYw){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("ywpId",ProjectNodeVo.YW_ID);
        hashMap.put("ywgId",GContestNodeVo.YW_ID);
        hashMap.put("actYw",actYw);
        return ApiResult.success(hashMap);
    }

    /**
     * 修改属性.
     *
     * @param actYw              项目流程
     * @param model              Model
     * @param request            HttpServletRequest
     * @param redirectAttributes RedirectAttributes
     * @return String
     */
    @RequiresPermissions("actyw:actYw:edit")
    @RequestMapping(value = "formProp")
    public String formProp(ActYw actYw, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        FlowType flowType = null;
        FlowYwId flowYwId = null;
        FlowProjectType fpType = null;
        if ((actYw.getGroup() != null) && StringUtil.isNotEmpty(actYw.getGroup().getFlowType())) {
            flowType = FlowType.getByKey(StringUtil.removeLastDotH(actYw.getGroup().getFlowType()));
            model.addAttribute("flowType", flowType);
        }

        if ((actYw.getGroup() == null) || (flowType == null)) {
            addMessage(redirectAttributes, "数据异常，流程不能为空！");
            if (actYw.getGroup() != null) {
                return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage&group.flowType=" + actYw.getGroup().getFlowType();
            }
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/?repage";
        }

        if ((actYw.getProProject() != null) && StringUtil.isNotEmpty(actYw.getProProject().getProType())) {
            fpType = FlowProjectType.getByKey(StringUtil.removeLastDotH(actYw.getProProject().getProType()));
            flowYwId = FlowYwId.getByFpType(fpType);
        }

        ActYw curActYw = null;
        boolean hasBase = false;
        if ((flowType).equals(FlowType.FWT_XM)) {
            curActYw = actYwService.get(FlowYwId.FY_P.getId());
            hasBase = true;
        } else if ((flowType).equals(FlowType.FWT_DASAI)) {
            curActYw = actYwService.get(FlowYwId.FY_G.getId());
            hasBase = true;
        }

        if (hasBase && (curActYw == null)) {
            addMessage(redirectAttributes, "项目或大赛数据不完整！");
            return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/actyw/actYw/list?repage";
        }

        List<ActYwGroup> actYwGroups = actYwGroupService.findListByDeploy(flowType);

        /**
         * 流程是否能更改.
         *  1、发布状态不能更换流程.
         *  2、固定的项目不能更换流程
         *  3、该类型流程没有发布或发布数量小于1
         */
        if ((!actYw.getIsDeploy()) && (flowYwId == null) && (actYwGroups != null) && (actYwGroups.size() > 0)) {
            model.addAttribute("showFlow", true);
        } else {
            model.addAttribute("showFlow", false);
        }

        model.addAttribute("curActYw", curActYw);
        model.addAttribute("fpType", fpType);
        model.addAttribute("flowYwId", flowYwId);
        model.addAttribute("actYwGroups", actYwGroups);
        model.addAttribute("actYw", actYw);
        model.addAttribute(IAconfig.ACONFIG, IAconfig.genAconfig(flowType));
        return IActPn.curPt().actywProp(Sval.EmPt.TM_ADMIN);
    }

    @RequestMapping(value = "getActYwList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getAuditStandardList(ActYw actYw, HttpServletRequest request, HttpServletResponse response){
        try {
            Page<ActYw> page = actYwService.fistPageByYear(new Page<ActYw>(request, response), actYw);
            List<ActYwGroup> actYwGroups = actYwGroupService.findListByCount(new ActYwGroup());
            List<ActYw> actYwList = page.getList();
            for(ActYw curyw : actYwList){
                curyw.setNumberRuleName(ProActYwUtils.isNumberRule(curyw.getId()));
                curyw.setNumberRuleText(ProActYwUtils.getNumberRule(curyw.getId()));

                if(StringUtil.isEmpty(curyw.getGroupId())){
                    continue;
                }

                for(ActYwGroup cur : actYwGroups){
                    if(StringUtil.isEmpty(cur.getId())){
                        continue;
                    }

                    if((cur.getId()).equals(curyw.getGroupId())){
                        curyw.setRgroup(cur);
                    }
                }
            }
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
