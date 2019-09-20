package com.oseasy.auy.modules.act.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.oseasy.act.modules.actyw.entity.ActYwGtheme;
import com.oseasy.act.modules.actyw.service.ActYwGroupService;
import com.oseasy.act.modules.actyw.service.ActYwGthemeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义流程Controller
 * @author chenhao
 * @version 2017-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actYwGroup")
public class AuyActYwGroupController extends BaseController {
    @Autowired
    private ActYwGroupService actYwGroupService;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ActYwGthemeService actYwGthemeService;

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
    @RequestMapping(value = "form")
    public String form(ActYwGroup actYwGroup, Model model, HttpServletRequest request) {
        model.addAttribute("actYwGroup", actYwGroup);
        ActYwGtheme pactYwGtheme = new ActYwGtheme();
        pactYwGtheme.setEnable(true);
        model.addAttribute(FormTheme.FLOW_THEMES, actYwGthemeService.findList(pactYwGtheme));
        model.addAttribute(FlowType.FLOW_TYPES, FlowType.getAll());
        String secondName=request.getParameter("secondName");
        if(StringUtil.isNotEmpty(secondName)){
            model.addAttribute("secondName",secondName);
        }
        return IActPn.curPt().groupForm(Sval.EmPt.TM_ADMIN);
    }
    @RequiresPermissions("actyw:actYwGroup:edit")
    @RequestMapping(value = "save")
    public String save(ActYwGroup actYwGroup, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, actYwGroup)) {
            return form(actYwGroup, model,request);
        }

        if (StringUtil.isEmpty(actYwGroup.getKeyss())) {
          actYwGroup.setKeyss(SysNodeTool.genByKeyss(SysNoType.NO_FLOW));
        }

        Boolean isTrue = actYwGroupService.validKeyss(actYwGroup.getKeyss(), actYwGroup.getIsNewRecord());
        if (!isTrue) {
            addMessage(redirectAttributes, "自定义流程惟一标识 ["+actYwGroup.getKeyss()+"] 已经存在，修改失败");
            return form(actYwGroup, model,request);
        }

        if (StringUtil.isNotEmpty(actYwGroup.getStatus()) && (!actYwGroup.getTemp()) && (actYwGroup.getStatus()).equals(ActYwGroup.GROUP_DEPLOY_1)) {
            isTrue = true;
        }

        if (!isTrue) {
            addMessage(redirectAttributes, "流程不合法,没有流程节点，修改失败!");
            return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGroup/?repage";
        }

        actYwGroupService.save(actYwGroup);
        return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGroup/?repage";
    }

    @RequestMapping(value = "ajaxDeploy")
    public String ajaxDeploy(ActYwGroup actYwGroup, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (StringUtil.isNotEmpty(actYwGroup.getId()) && StringUtil.isNotEmpty(actYwGroup.getStatus())) {
            ActYwGroup newActYwGroup = actYwGroupService.get(actYwGroup.getId());
            if (newActYwGroup.getTemp()) {
                addMessage(redirectAttributes, "流程设计未提交,发布失败!");
                return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGroup/?repage";
            }
            //测试流程 取消发布动作
            if(actYwGroup.getStatus().equals(Const.NO)){
                //含有该流程已发布的
                List<ActYw> actList= actYwService.findActYwListByGroupId(actYwGroup.getId());
//                if(actList!=null && actList.size()>0){
//                    addMessage(redirectAttributes, "含该流程项目已发布,取消发布失败!");
//                    return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGroup/?repage";
//                }
//                //没有正式发布数据 可以取消发布。
//                List<ProModel> proList= proModelService.getListByGroupId(actYwGroup.getId());
//                if(proList!=null && proList.size()>0){
//                    addMessage(redirectAttributes, "流程已有数据,取消发布失败!");
//                    return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGroup/?repage";
//                }
                List<ActYw> actAllList= actYwService.findAllActYwListByGroupId(actYwGroup.getId());
                for(ActYw actYw:actAllList){
                    proModelService.clearPreReleaseData(actYw.getId());
                }
            }else{
                //发布动作
                List<ActYw> actList= actYwService.findAllActYwListByGroupId(actYwGroup.getId());
                Boolean isHaveRe=true;
                for(ActYw actYw:actList){
                    if(actYw.getIsPreRelease()){
                        isHaveRe=false;
                    }
                }
                if(isHaveRe){
                    for(ActYw actYw:actList){
                        proModelService.clearPreReleaseData(actYw.getId());
                    }
                }
            }
            newActYwGroup.setStatus(actYwGroup.getStatus());
            return save(newActYwGroup, model, redirectAttributes,request);
        }
        return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/actyw/actYwGroup/?repage";
    }


    @RequestMapping(value = "ajaxDeployJson")
    @ResponseBody
    public ApiResult ajaxDeployJson(ActYwGroup actYwGroup, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try{
            if((actYwGroup == null) || StringUtil.isEmpty(actYwGroup.getId())){
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, "数据不存在或已被删除");
            }

            if (StringUtil.isNotEmpty(actYwGroup.getId()) && StringUtil.isNotEmpty(actYwGroup.getStatus())) {
                ActYwGroup newActYwGroup = actYwGroupService.get(actYwGroup.getId());
                if (newActYwGroup.getTemp()) {
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"流程设计未提交,发布失败!");

                }
                //测试流程 取消发布动作
                if(actYwGroup.getStatus().equals(ActYwGroup.GROUP_DEPLOY_0)){
                    //含有该流程已发布的
                    List<ActYw> actList= actYwService.findActYwListByGroupId(actYwGroup.getId());
                    if(StringUtil.checkNotEmpty(actList)){
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"含该流程项目已发布,取消发布失败!");
                    }
                    //没有正式发布数据 可以取消发布。
                    List<ProModel> proList= proModelService.getListByGroupId(actYwGroup.getId());
                    if(proList!=null && proList.size()>0){
                        return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"流程已有数据,取消发布失败!");
                    }
                    List<ActYw> actAllList= actYwService.findAllActYwListByGroupId(actYwGroup.getId());
                    for(ActYw actYw:actAllList){
                        proModelService.clearPreReleaseData(actYw.getId());
                    }
                }else{
                    //发布动作
                    List<ActYw> actList= actYwService.findAllActYwListByGroupId(actYwGroup.getId());
                    Boolean isHaveRe=true;
                    for(ActYw actYw:actList){
                        if(actYw.getIsPreRelease()){
                            isHaveRe=false;
                        }
                    }
                    if(isHaveRe){
                        for(ActYw actYw:actList){
                            proModelService.clearPreReleaseData(actYw.getId());
                        }
                    }
                }
                newActYwGroup.setStatus(actYwGroup.getStatus());
                return saveJson(newActYwGroup, model, redirectAttributes,request);
            }else{
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"数据不完整");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    public ApiResult saveJson(ActYwGroup actYwGroup, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (StringUtil.isEmpty(actYwGroup.getKeyss())) {
          actYwGroup.setKeyss(SysNodeTool.genByKeyss(SysNoType.NO_FLOW));
        }
        Boolean isTrue = actYwGroupService.validKeyss(actYwGroup.getKeyss(), actYwGroup.getIsNewRecord());
        if (StringUtil.isNotEmpty(actYwGroup.getStatus()) && (!actYwGroup.getTemp()) && (actYwGroup.getStatus()).equals(ActYwGroup.GROUP_DEPLOY_1)) {
            isTrue = true;
        }
        if (!isTrue) {
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+"流程不合法,没有流程节点，修改失败!");
        }
        actYwGroupService.save(actYwGroup);
        return ApiResult.success();
    }

    /*发布王清腾*/
    @ResponseBody
    @RequestMapping(value = "ajaxRelease")
    public ApiTstatus<String> ajaxRelease(ActYwGroup actYwGroup, Model model, RedirectAttributes redirectAttributes) {
        ApiTstatus<String> req = new ApiTstatus<String>();
        if (StringUtil.isNotEmpty(actYwGroup.getId()) && StringUtil.isNotEmpty(actYwGroup.getStatus())) {
            ActYwGroup newActYwGroup = actYwGroupService.get(actYwGroup.getId());
            if (newActYwGroup.getTemp()) {
                req.setStatus(false);
                req.setMsg("流程设计未提交,发布失败");
                return req;
            }
            //测试流程
            if(actYwGroup.getStatus().equals("0")){
                //含有该流程已发布的
                List<ActYw> actList= actYwService.findActYwListByGroupId(actYwGroup.getId());
                if(actList!=null && actList.size()>0){
                    req.setStatus(false);
                    req.setMsg("含该流程项目已发布,取消发布失败");
                    return req;
                }
                //没有数据 可以取消发布。
                List<ProModel> proList= proModelService.getListByGroupId(actYwGroup.getId());
                if(proList!=null && proList.size()>0){
                    req.setStatus(false);
                    req.setMsg("流程已有数据,取消发布失败");
                    return req;
                }
            }
            newActYwGroup.setStatus(actYwGroup.getStatus());
            return ajaxSave(newActYwGroup, model);
        }
        req.setStatus(true);
        req.setMsg("流程已有数据,取消发布失败");
        return req;
    }

    /*王清腾添加下一步*/
    @ResponseBody
    @RequestMapping(value = "ajaxSave")
    public ApiTstatus<String> ajaxSave(ActYwGroup actYwGroup, Model model) {
        ApiTstatus<String> req = new ApiTstatus<String>();
        if (!beanValidator(model, actYwGroup)) {
            req.setStatus(false);
            req.setMsg("自定义流程不合格，请检查");
            return req;
        }

        if (StringUtil.isEmpty(actYwGroup.getKeyss())) {
            actYwGroup.setKeyss(SysNodeTool.genByKeyss(SysNoType.NO_FLOW));
        }

        Boolean isTrue = actYwGroupService.validKeyss(actYwGroup.getKeyss(), actYwGroup.getIsNewRecord());
        if (!isTrue) {
            req.setStatus(false);
            req.setMsg("自定义流程不合格，请检查");
            return req;
        }

        if (StringUtil.isNotEmpty(actYwGroup.getStatus()) && (!actYwGroup.getTemp()) && (actYwGroup.getStatus()).equals(ActYwGroup.GROUP_DEPLOY_1)) {
            isTrue = true;
        }

        if (!isTrue) {
            req.setStatus(false);
            req.setMsg("流程不合法,没有流程节点，修改失败!");
            return req;
        }

        actYwGroupService.save(actYwGroup);
        req.setStatus(true);
        req.setMsg("添加流程成功");
        req.setDatas(actYwGroup.getId());
        return req;
    }
}