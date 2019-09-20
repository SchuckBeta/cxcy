package com.oseasy.pro.modules.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.service.ProModelTlxyService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * proProjectMdController.
 *
 * @author zy
 * @version 2017-09-18
 */
@Controller
@RequestMapping(value = "${frontPath}/proModelTlxy")
public class FrontProModelTlxyController extends BaseController {

    @Autowired
    private ProModelService proModelService;

    @Autowired
    private ProjectPlanService projectPlanService;

    @Autowired
    private ProModelTlxyService proModelTlxyService;

    @Autowired
    private ProjectDeclareService projectDeclareService;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ActYwService actYwService;

    @ModelAttribute
    public ProModelTlxy get(@RequestParam(required = false) String id) {
        ProModelTlxy entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = proModelTlxyService.get(id);
        }
        if (entity == null) {
            entity = new ProModelTlxy();
        }
        return entity;
    }


    @RequestMapping(value = {"list", ""})
    public String list(ProModelTlxy proModelTlxy, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ProModelTlxy> page = proModelTlxyService.findPage(new Page<ProModelTlxy>(request, response), proModelTlxy);
        model.addAttribute("page", page);
        return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "proModelGzsmxxList";
    }


    @RequestMapping(value = "form")
    public String form(ProModelGzsmxx proModelGzsmxx, Model model) {
        model.addAttribute("proModelGzsmxx", proModelGzsmxx);
        //return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "proModelGzsmxxForm";
        return "/template/form/project/md_applyForm";
    }

    @RequestMapping(value = "/applyStep1")
    public String applyStep1(ProModelTlxy proModelTlxy, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        String parmact = null;
        if (StringUtil.isNotEmpty(proModelTlxy.getId())) {
            proModelTlxy = proModelTlxyService.get(proModelTlxy.getId());
            ProModel proModel = proModelTlxy.getProModel();
            if (StringUtil.isEmpty(proModel.getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModelTlxy.getId();
            }
            parmact = proModel.getActYwId();
        } else {
            proModelTlxy.setCreateDate(new Date());
            parmact = request.getParameter("actywId");
        }
        //proModelTlxy.getProModel().setActYwId(parmact);
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
        model.addAttribute("sysdate", DateUtil.formatDate(proModelTlxy.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("actYw", actYw);
        if(StringUtil.isNotEmpty(proModelTlxy.getModelId())){
            proModelTlxy.setPlanList(projectPlanService.findListByProjectId(proModelTlxy.getModelId()));

            proModelTlxy.setProModel(proModelService.get(proModelTlxy.getModelId()));
            model.addAttribute("teams", projectDeclareService.findTeams(cuser.getId(), ""));
            model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModelTlxy.getProModel().getTeamId(), proModelTlxy.getProModel().getId()));
            model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModelTlxy.getProModel().getTeamId(), proModelTlxy.getProModel().getId()));

        }
        model.addAttribute("proModelTlxy", proModelTlxy);
        model.addAttribute("cuser", cuser);
       return "template/formtheme/project/tlxy_applyForm";
    }

    //保存第一步
    @RequestMapping(value = "/applyStep2")
    public String applyStep2(ProModelTlxy proModelTlxy, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        String parmact = null;
        if (StringUtil.isNotEmpty(proModelTlxy.getId())) {
            proModelTlxy = proModelTlxyService.get(proModelTlxy.getId());
            if (StringUtil.isEmpty(proModelTlxy.getProModel().getDeclareId())
                    || StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModelTlxy.getProModel().getDeclareId())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=" + proModelTlxy.getId();
            }
            parmact = proModelTlxy.getProModel().getActYwId();
        } else {
            proModelTlxy.setCreateDate(new Date());
            parmact = request.getParameter("actywId");
        }
        //proModelTlxy.getProModel().setActYwId(parmact);

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
        model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModelTlxy.getProModel().getTeamId(), proModelTlxy.getProModel().getId()));
        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModelTlxy.getProModel().getTeamId(), proModelTlxy.getProModel().getId()));
        model.addAttribute("sysdate", DateUtil.formatDate(proModelTlxy.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("actYwId",parmact);
        if(proModelTlxy.getProModel()!=null){
            proModelTlxy.setPlanList(projectPlanService.findListByProjectId(proModelTlxy.getProModel().getId()));
        }
        if(proModelTlxy.getModelId()!=null){
            proModelTlxy.setProModel(proModelService.get(proModelTlxy.getModelId()));
        }
        model.addAttribute("proModelTlxy", proModelTlxy);
        model.addAttribute("cuser", cuser);
        return "template/formtheme/project/tlxy_applyStep2";
    }

    @RequestMapping(value = "/applyStep3")
    public String applyStep3(ProModelTlxy proModelTlxy, HttpServletRequest request, HttpServletResponse response, Model model) {
        User cuser = UserUtils.getUser();
        ProModel proModel = proModelTlxy.getProModel();
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
        proModelTlxy.setProModel(proModel);
        model.addAttribute("proModelTlxy", proModelTlxy);
        model.addAttribute("sysdate", DateUtil.formatDate(proModelTlxy.getCreateDate(), "yyyy-MM-dd"));
        model.addAttribute("cuser", cuser);
        return "template/formtheme/project/tlxy_applyStep3";
    }

    @RequestMapping(value = "/saveStep3")
    @ResponseBody
    public JSONObject saveStep3(ProModelTlxy proModelTlxy, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        JSONObject js = new JSONObject();
        AttachMentEntity attachMentEntity = proModelTlxy.getProModel().getAttachMentEntity();
        ProModel proModel = proModelService.get(proModelTlxy.getProModel().getId());
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
    public JSONObject ajaxSave(ProModelTlxy proModelTlxy, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelTlxy.getProModel().getYear())) {
            proModelTlxy.getProModel().setYear(commonService.getApplyYear(proModelTlxy.getProModel().getActYwId()));
        }
        js = commonService.onProjectApply(
                proModelTlxy.getProModel().getActYwId(),
                proModelTlxy.getProModel().getId(),
                proModelTlxy.getProModel().getYear());

        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        js = proModelTlxyService.saveStep1(proModelTlxy);
        js.put("id", proModelTlxy.getId());
        js.put("proModelId", proModelTlxy.getProModel().getId());
        js.put("proCategory", proModelTlxy.getProModel().getProCategory());
        return js;
    }

    //不校验保存第二步
    @RequestMapping(value = "ajaxUncheckSave2")
    @ResponseBody
    public JSONObject ajaxUncheckSave2(ProModelTlxy proModelTlxy, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelTlxy.getProModel().getYear())) {
            proModelTlxy.getProModel().setYear(commonService.getApplyYear(proModelTlxy.getProModel().getActYwId()));
        }
        js = proModelTlxyService.saveStep2(proModelTlxy);
        js.put("id", proModelTlxy.getId());
        js.put("proModelId", proModelTlxy.getProModel().getId());
        js.put("proCategory", proModelTlxy.getProModel().getProCategory());
        return js;
    }

    @RequestMapping(value = "ajaxSave2")
    @ResponseBody
    public JSONObject ajaxSave2(ProModelTlxy proModelTlxy, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        if (StringUtil.isEmpty(proModelTlxy.getProModel().getYear())) {
            proModelTlxy.getProModel().setYear(commonService.getApplyYear(proModelTlxy.getProModel().getActYwId()));
        }
        js = commonService.onGcontestApply(proModelTlxy.getProModel().getId(), proModelTlxy.getProModel().getActYwId(), proModelTlxy.getProModel().getTeamId(), proModelTlxy.getProModel().getYear());
//        if (StringUtils.isEmpty(proModelTlxy.getProModel().getTeamId())) {
//            js.put("msg", "请选择团队信息！");
//            js.put("ret", 0);
//        }
        if ("0".equals(js.getString(CoreJkey.JK_RET))) {
            return js;
        }
        js = proModelTlxyService.saveStep2(proModelTlxy);
        js.put("id", proModelTlxy.getId());
        js.put("proModelId", proModelTlxy.getProModel().getId());
        js.put("proCategory", proModelTlxy.getProModel().getProCategory());
        return js;
    }

    @RequestMapping(value = "ajaxSave3")
    @ResponseBody
    public JSONObject ajaxSave3(ProModelTlxy proModelTlxy, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        ProModel proModel = proModelTlxy.getProModel();
        if(proModelTlxy.getAttachMentEntity()==null && proModel.getAttachMentEntity()!=null){
            proModelTlxy.setAttachMentEntity(proModel.getAttachMentEntity());
        }

        if(StringUtil.isEmpty(proModel.getYear())){
            proModel.setYear(commonService.getApplyYear(proModel.getActYwId()));
        }
        js = commonService.onProjectSubmitStep3(proModelTlxy.getProModel().getId(), proModelTlxy.getProModel().getActYwId(), proModelTlxy.getProModel().getProCategory(), proModelTlxy.getProModel().getTeamId(), proModelTlxy.getProModel().getYear());
        if (!"1".equals(js.getString("ret"))) {
            return js;
        }
        try {
            proModelTlxyService.uploadFile(proModelTlxy);
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
    public JSONObject submit(ProModelTlxy proModelTlxy, Model model, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        js.put(CoreJkey.JK_RET, 1);
        if (StringUtil.isEmpty(proModelTlxy.getProModel().getYear())) {
            proModelTlxy.getProModel().setYear(commonService.getApplyYear(proModelTlxy.getProModel().getActYwId()));
        }
        js = commonService.onProjectSubmitStep3(proModelTlxy.getProModel().getId(), proModelTlxy.getProModel().getActYwId(), proModelTlxy.getProModel().getProCategory(), proModelTlxy.getProModel().getTeamId(), proModelTlxy.getProModel().getYear());
        if ("0".equals(js.getString(CoreJkey.JK_RET))) {
            return js;
        }
        try {
            proModelTlxyService.submit(proModelTlxy);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "提交失败,系统异常请联系管理员");
            return js;
        }

        ActYw actYw = actYwService.get(proModelTlxy.getProModel().getActYwId());
        if (actYw != null) {
            js.put("pptype", actYw.getProProject().getType());
            js.put("proProjectId", actYw.getProProject().getId());
        }
        js.put(CoreJkey.JK_MSG, "提交成功");
        js.put("actywId", proModelTlxy.getProModel().getActYwId());
        js.put("projectId", proModelTlxy.getProModel().getId());
        return js;
    }

    @RequestMapping(value = "ajaxWtparam")
    @ResponseBody
    public Wtparam ajaxWtparam() {
        return new Wtparam(IWparam.getFileTplPreFix(), Wtype.toJson());
    }

//	//下载word模板文档
//	@RequestMapping(value = "ajaxWord")
//	@ResponseBody
//	public Rstatus ajaxWord(String proId, String type, String vsn, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		//ProModelGzsmxx proModelGzsmxx = proModelGzsmxxService.get(proId);
//		ProModelGzsmxx proModelGzsmxx = proModelGzsmxxService.getByProModelId(proId);
//
//		if ((proModelGzsmxx == null) || StringUtil.isEmpty(proModelGzsmxx.getModelId())) {
//		  	return new Rstatus(false, "ProId 或 proModelGzsmxx 参数不能为空！");
//		}
//
//		ProModel proModel = proModelService.get(proModelGzsmxx.getModelId());
//		if ((proModel == null) || StringUtil.isEmpty(proModel.getTeamId())) {
//      		return new Rstatus(false, "ProModel Id 或 Team id 参数不能为空！");
//		}
//
//		String teamId = proModelGzsmxx.getProModel().getTeamId();
//		Team team = teamService.get(teamId);
//		List<BackTeacherExpansion> qytes = backTeacherExpansionService.getQYTeacher(teamId);
//		List<BackTeacherExpansion> xytes = backTeacherExpansionService.getXYTeacher(teamId);
//		List<StudentExpansion> tms = studentExpansionService.getStudentByTeamId(teamId);
//		if ((team == null) || ((qytes == null) && (xytes == null)) || (tms == null)) {
//      		return new Rstatus(false, "团队、导师参数不能为空！");
//		}
//
//		if (qytes == null) {
//		  qytes = Lists.newArrayList();
//		}
//
//		if (xytes == null) {
//		  xytes = Lists.newArrayList();
//		}
//
//		WproType wproType = WproType.getByKey(proModel.getProCategory());
//		if (wproType == null) {
//		  	return new Rstatus(false, "proCategory 项目类型未定义["+proModel.getProCategory()+"]");
//		}
//
////		IWparam wordParam = proModelGzsmxxService.initIWparam(type, vsn, proModel, proModelGzsmxx, team, xytes, qytes, tms);
////		if (wordParam != null) {
////			wordService.exeDownload(vsn, wordParam, request, response);
////			return null;
////		}
//		return new Rstatus(false, "模板下载失败！");
//	}

    @RequestMapping(value = "delete")
    public String delete(ProModelTlxy proModelTlxy, RedirectAttributes redirectAttributes) {
        proModelTlxyService.delete(proModelTlxy);
        addMessage(redirectAttributes, "删除proModelGzsmxx成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/proprojectmd/proModelGzsmxx/?repage";
    }

}