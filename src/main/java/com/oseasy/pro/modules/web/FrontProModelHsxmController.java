package com.oseasy.pro.modules.web;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.service.ProModelHsxmService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * proProjectMdController.
 *
 * @author zy
 * @version 2017-09-18
 */
@Controller
@RequestMapping(value = "${frontPath}/proModelHsxm")
public class FrontProModelHsxmController extends BaseController {

    @Autowired
    private ProModelService proModelService;

    @Autowired
    private ProjectPlanService projectPlanService;

    @Autowired
    private ProModelHsxmService proModelHsxmService;

    @Autowired
    private ProjectDeclareService projectDeclareService;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ActYwService actYwService;

    @ModelAttribute
    public ProModelHsxm get(@RequestParam(required = false) String id) {
        ProModelHsxm entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = proModelHsxmService.get(id);
        }
        if (entity == null) {
            entity = new ProModelHsxm();
        }
        return entity;
    }



    @RequestMapping(value = "/applyStep1")
    public String applyStep1(ProModelHsxm proModelHsxm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        String parmact = null;
        if (StringUtil.isNotEmpty(proModelHsxm.getId())) {
            proModelHsxm = proModelHsxmService.get(proModelHsxm.getId());
            ProModel proModel = proModelHsxm.getProModel();
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModelHsxm.getId();
            }
            parmact = proModel.getActYwId();
        } else {
            proModelHsxm.setCreateDate(new Date());
            parmact = request.getParameter("actywId");
        }
        //proModelHsxm.getProModel().setActYwId(parmact);
        if (StringUtil.isNotEmpty(parmact)) {
            ActYw actYw = actYwService.get(parmact);
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                        && StringUtil.isNotEmpty(proProject.getType())) {
                    model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
                }
            }
        }
        ActYw actYw = actYwService.get(parmact);
        model.addAttribute("sysdate", DateUtil.formatDate(proModelHsxm.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("actYw", actYw);
        if(StringUtil.isNotEmpty(proModelHsxm.getModelId())){
            proModelHsxm.setPlanList(projectPlanService.findListByProjectId(proModelHsxm.getModelId()));

            proModelHsxm.setProModel(proModelService.get(proModelHsxm.getModelId()));
            model.addAttribute("teams", projectDeclareService.findTeams(cuser.getId(), ""));
            model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getId()));
            model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getId()));

        }
        model.addAttribute("proModelHsxm", proModelHsxm);
        model.addAttribute("cuser", cuser);
       return "template/formtheme/project/hsxm_applyForm";
    }

    //保存第一步
    @RequestMapping(value = "/applyStep2")
    public String applyStep2(ProModelHsxm proModelHsxm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        String parmact = null;
        if (StringUtil.isNotEmpty(proModelHsxm.getId())) {
            proModelHsxm = proModelHsxmService.get(proModelHsxm.getId());
            if (StringUtil.isEmpty(proModelHsxm.getProModel().getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModelHsxm.getProModel().getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModelHsxm.getId();
            }
            parmact = proModelHsxm.getProModel().getActYwId();
        } else {
            proModelHsxm.setCreateDate(new Date());
            parmact = request.getParameter("actywId");
        }
        //proModelHsxm.getProModel().setActYwId(parmact);

        if (StringUtil.isNotEmpty(parmact)) {
            ActYw actYw = actYwService.get(parmact);
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                        && StringUtil.isNotEmpty(proProject.getType())) {
                    model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
                }
            }
        }
        model.addAttribute("teams", projectDeclareService.findTeams(cuser.getId(), ""));
        model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getId()));
        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getId()));
        model.addAttribute("sysdate", DateUtil.formatDate(proModelHsxm.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("actYwId",parmact);
        if(proModelHsxm.getProModel()!=null){
            proModelHsxm.setPlanList(projectPlanService.findListByProjectId(proModelHsxm.getProModel().getId()));
        }
        if(proModelHsxm.getModelId()!=null){
            proModelHsxm.setProModel(proModelService.get(proModelHsxm.getModelId()));
        }
        model.addAttribute("proModelHsxm", proModelHsxm);
        model.addAttribute("cuser", cuser);
        return "template/formtheme/project/hsxm_applyStep2";
    }

    @RequestMapping(value = "/applyStep3")
    public String applyStep3(ProModelHsxm proModelHsxm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        ProModel proModel = proModelHsxm.getProModel();
        if (StringUtil.isNotEmpty(proModel.getId())) {
            proModel = proModelService.get(proModel.getId());
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModel.getId();
            }
        }
        String parmact = proModel.getActYwId();
        if (StringUtil.isNotEmpty(parmact)) {
            ActYw actYw = actYwService.get(parmact);
            if (actYw != null) {
                ProProject proProject = actYw.getProProject();
                if (proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                        && StringUtil.isNotEmpty(proProject.getType())) {
                    model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
                }
            }
        }
        SysAttachment sa = new SysAttachment();
        sa.setUid(proModel.getId());
        sa.setFileStep(FileStepEnum.S1102);
        sa.setType(FileTypeEnum.S11);
        proModel.setFileInfo(sysAttachmentService.getFiles(sa));
        proModelHsxm.setProModel(proModel);
        model.addAttribute("proModelHsxm", proModelHsxm);
        model.addAttribute("sysdate", DateUtil.formatDate(proModelHsxm.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("cuser", cuser);
        return "template/formtheme/project/hsxm_applyStep3";
    }

    @RequestMapping(value = "/saveStep3")
    @ResponseBody
    public JSONObject saveStep3(ProModelHsxm proModelHsxm, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        AttachMentEntity attachMentEntity = proModelHsxm.getProModel().getAttachMentEntity();
        ProModel proModel = proModelService.get(proModelHsxm.getProModel().getId());
        proModel.setAttachMentEntity(attachMentEntity);
        if(StringUtil.isEmpty(proModel.getYear())){
            proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
        }
        js = commonService.onProjectSaveStep2(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(),proModel.getYear());
        if (!"1".equals(js.getString("ret"))) {
            return js;
        }
        try {
            proModelService.saveStep3(proModel);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put("ret", 0);
            js.put("msg", "保存失败,系统异常请联系管理员");
            return js;
        }
        js.put("ret", 1);
        js.put("msg", "保存成功");
        return js;
    }



    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public JSONObject ajaxSave(ProModelHsxm proModelHsxm, Model model, RedirectAttributes redirectAttributes) throws Exception{
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelHsxm.getProModel().getYear())) {
            proModelHsxm.getProModel().setYear(commonService.getApplyYear(proModelHsxm.getProModel().getActYwId()));
        }
        js = commonService.onProjectApply(
                proModelHsxm.getProModel().getActYwId(),
                proModelHsxm.getProModel().getId(),
                proModelHsxm.getProModel().getYear());

        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        js = proModelHsxmService.saveStep1(proModelHsxm);
        js.put("id", proModelHsxm.getId());
        js.put("proModelId", proModelHsxm.getProModel().getId());
        js.put("proCategory", proModelHsxm.getProModel().getProCategory());
        return js;
    }

    @RequestMapping(value="saveHsxmStep1", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveHsxmStep1(@RequestBody ProModelHsxm proModelHsxm){
        try {
            if (StringUtil.isEmpty(proModelHsxm.getProModel().getYear())) {
                proModelHsxm.getProModel().setYear(commonService.getApplyYear(proModelHsxm.getProModel().getActYwId()));
            }
            JSONObject js = commonService.onProjectApply(
                    proModelHsxm.getProModel().getActYwId(),
                    proModelHsxm.getProModel().getId(),
                    proModelHsxm.getProModel().getYear());

            if ("0".equals(js.getString("ret"))) {
                 return ApiResult.failed(ApiConst.CODE_OTHER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+js.getString("msg"));
            }
            proModelHsxmService.saveStep1(proModelHsxm);
            return ApiResult.success(proModelHsxm);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+"::保存失败,系统异常请联系管理员");
        }
    }


    @RequestMapping(value="saveHsxmStep2/{isCheck}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveHsxmSteps(@PathVariable String isCheck, @RequestBody ProModelHsxm proModelHsxm){
        try {
            JSONObject js = new JSONObject();
            if ((proModelHsxm.getProModel()!=null)&&StringUtil.isEmpty(proModelHsxm.getProModel().getYear())) {
                proModelHsxm.getProModel().setYear(commonService.getApplyYear(proModelHsxm.getProModel().getActYwId()));
            }
            if(StringUtil.isNotEmpty(isCheck) && isCheck.equals("1")){
                js = commonService.onGcontestApply(proModelHsxm.getProModel().getId(), proModelHsxm.getProModel().getActYwId(), proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getYear());
                if ("0".equals(js.getString(CoreJkey.JK_RET))) {
                    return ApiResult.failed(ApiConst.CODE_OTHER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+js.getString("msg"));
                }
            }
            js = proModelHsxmService.saveStep2(proModelHsxm);
            if ("0".equals(js.getString(CoreJkey.JK_RET))) {
                return ApiResult.failed(ApiConst.CODE_OTHER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+js.getString("msg"));
            }
            return ApiResult.success(proModelHsxm);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+"::保存失败,系统异常请联系管理员");
        }
    }

    @RequestMapping(value="saveHsxmStep3/{isCheck}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveHsxmStep3(@PathVariable String isCheck, @RequestBody ProModelHsxm proModelHsxm){
        try {
            JSONObject js = new JSONObject();
            if ((proModelHsxm.getProModel()!=null)&&StringUtil.isEmpty(proModelHsxm.getProModel().getYear())) {
                proModelHsxm.getProModel().setYear(commonService.getApplyYear(proModelHsxm.getProModel().getActYwId()));
            }
            js = commonService.onProjectSubmitStep3(proModelHsxm.getProModel().getId(), proModelHsxm.getProModel().getActYwId(), proModelHsxm.getProModel().getProCategory(), proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getYear());
            if ("0".equals(js.getString(CoreJkey.JK_RET))) {
                return ApiResult.failed(ApiConst.CODE_OTHER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_OTHER_ERROR)+":"+js.getString("msg"));
            }
            if(StringUtil.isNotEmpty(isCheck) && isCheck.equals("1")){
                proModelHsxmService.submit(proModelHsxm);
            }else {
                proModelHsxmService.updateFileList(proModelHsxm);
            }
            return ApiResult.success(proModelHsxm);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":保存失败,系统异常请联系管理员");
        }
    }


    //不校验保存第二步
    @RequestMapping(value = "ajaxUncheckSave2", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject ajaxUncheckSave2(ProModelHsxm proModelHsxm, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelHsxm.getProModel().getYear())) {
            proModelHsxm.getProModel().setYear(commonService.getApplyYear(proModelHsxm.getProModel().getActYwId()));
        }
        js = proModelHsxmService.saveStep2(proModelHsxm);
        js.put("id", proModelHsxm.getId());
        js.put("proModelId", proModelHsxm.getProModel().getId());
        js.put("proCategory", proModelHsxm.getProModel().getProCategory());
        return js;
    }

    @RequestMapping(value = "ajaxSave2",  method = RequestMethod.GET)
    @ResponseBody
    public JSONObject ajaxSave2(ProModelHsxm proModelHsxm, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if ((proModelHsxm.getProModel()!=null)&&StringUtil.isEmpty(proModelHsxm.getProModel().getYear())) {
            proModelHsxm.getProModel().setYear(commonService.getApplyYear(proModelHsxm.getProModel().getActYwId()));
        }
        js = commonService.onGcontestApply(proModelHsxm.getProModel().getId(), proModelHsxm.getProModel().getActYwId(), proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getYear());
        if ("0".equals(js.getString(CoreJkey.JK_RET))) {
            return js;
        }
        js = proModelHsxmService.saveStep2(proModelHsxm);
        js.put("id", proModelHsxm.getId());
        js.put("proModelId", proModelHsxm.getProModel().getId());
        js.put("proCategory", proModelHsxm.getProModel().getProCategory());
        return js;
    }

    @RequestMapping(value = "ajaxSave3")
    @ResponseBody
    public JSONObject ajaxSave3(ProModelHsxm proModelHsxm, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        ProModel proModel = proModelHsxm.getProModel();
        if(proModelHsxm.getAttachMentEntity()==null && proModel.getAttachMentEntity()!=null){
            proModelHsxm.setAttachMentEntity(proModel.getAttachMentEntity());
        }
        if(StringUtil.isEmpty(proModel.getYear())){
            proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
        }
        js = commonService.onProjectSubmitStep3(proModelHsxm.getProModel().getId(), proModelHsxm.getProModel().getActYwId(), proModelHsxm.getProModel().getProCategory(), proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getYear());
        if (!"1".equals(js.getString("ret"))) {
            return js;
        }
        try {
            proModelHsxmService.uploadFile(proModelHsxm);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put("ret", 0);
            js.put("msg", "保存失败,系统异常请联系管理员");
            return js;
        }
        js.put("ret", 1);
        js.put("msg", "保存成功");
        return js;
    }

    //第三步提交
    @RequestMapping(value = "submit")
    @ResponseBody
    public JSONObject submit(ProModelHsxm proModelHsxm, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        js.put(CoreJkey.JK_RET, 1);
        if (StringUtil.isEmpty(proModelHsxm.getProModel().getYear())) {
            proModelHsxm.getProModel().setYear(commonService.getApplyYear(proModelHsxm.getProModel().getActYwId()));
        }
        js = commonService.onProjectSubmitStep3(proModelHsxm.getProModel().getId(), proModelHsxm.getProModel().getActYwId(), proModelHsxm.getProModel().getProCategory(), proModelHsxm.getProModel().getTeamId(), proModelHsxm.getProModel().getYear());
        if ("0".equals(js.getString(CoreJkey.JK_RET))) {
            return js;
        }
        try {
            proModelHsxmService.submit(proModelHsxm);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "提交失败,系统异常请联系管理员");
            return js;
        }

        ActYw actYw = actYwService.get(proModelHsxm.getProModel().getActYwId());
        if (actYw != null) {
            js.put("pptype", actYw.getProProject().getType());
            js.put("proProjectId", actYw.getProProject().getId());
        }
        js.put(CoreJkey.JK_MSG, "提交成功");
        js.put("actywId", proModelHsxm.getProModel().getActYwId());
        js.put("projectId", proModelHsxm.getProModel().getId());
        return js;
    }

    @RequestMapping(value = "ajaxWtparam")
    @ResponseBody
    public Wtparam ajaxWtparam() {
        return new Wtparam(IWparam.getFileTplPreFix(), Wtype.toJson());
    }

    @RequestMapping(value = "delete")
    public String delete(ProModelHsxm proModelHsxm, RedirectAttributes redirectAttributes) {
        proModelHsxmService.delete(proModelHsxm);
        addMessage(redirectAttributes, "删除proModelGzsmxx成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/proprojectmd/proModelGzsmxx/?repage";
    }

}