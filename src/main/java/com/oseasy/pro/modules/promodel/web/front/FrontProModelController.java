package com.oseasy.pro.modules.promodel.web.front;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.modules.act.utils.ThreadUtils;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.exception.ApplyException;
import com.oseasy.act.modules.actyw.service.*;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.jobs.pro.ActYwUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProReport;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.service.ProReportService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FormPage;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.ConfRange;
import com.oseasy.sys.modules.sys.vo.PersonNumConf;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import net.sf.json.JSONObject;

/**
 * proModelController.
 *
 * @author zy
 * @version 2017-07-13
 */
@Controller
@RequestMapping(value = "${frontPath}/promodel/proModel")
public class FrontProModelController extends BaseController {

    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProModelMdService proModelMdService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private TeamService teamService;
    @Autowired
    SysAttachmentService sysAttachmentService;
    @Autowired
    TeamUserRelationService teamUserRelationService;
    @Autowired
    private ProReportService proReportService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    private ActYwGformService actYwGformService;
    @Autowired
    private ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ActYwPscrelService actYwPscrelService;

    @ModelAttribute
    public ProModel get(@RequestParam(required = false) String id) {
        ProModel entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = proModelService.get(id);
        }
        if (entity == null) {
            entity = new ProModel();
        }
        return entity;
    }


    @RequestMapping(value = {"list", ""})
    public String list(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ProModel> page = proModelService.findPage(new Page<ProModel>(request, response), proModel);
        model.addAttribute("page", page);
        return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "proModelList";
    }


    @RequestMapping(value = "form")
    public String form(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
		try{
			User user = systemService.getUser(proModel.getDeclareId());
			ActYw actYw = actYwService.get(proModel.getActYwId());
			IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(actYw);
			ProProject proProject = actYw.getProProject();
			if (proProject != null) {
				showFrontMessage(proProject, model);
			}
			if (proModel.getSubTime() != null) {
				model.addAttribute("sysdate", DateUtil.formatDate(proModel.getSubTime(), "yyyy-MM-dd"));
			} else {
				model.addAttribute("sysdate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
			}
			model.addAttribute("proProject", proProject);
			model.addAttribute("actYw", actYw);
			model.addAttribute("sse", user);
			//关联团队
			model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), ""));
			model.addAttribute("projectName", actYw.getProProject().getProjectName());
			model.addAttribute("team", teamService.get(proModel.getTeamId()));
			model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModel.getTeamId(), proModel.getId()));
			model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(), proModel.getId()));

			User cuser = UserUtils.getUser();
			User leader = UserUtils.getUser();
			if (StringUtil.isEmpty(proModel.getDeclareId())
					|| StringUtil.isEmpty(cuser.getId()) || !cuser.getId().equals(proModel.getDeclareId())) {
				return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/viewForm?id=?id=" + proModel.getId();
			}
			if (actYw != null && proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
					&& StringUtil.isNotEmpty(proProject.getType())) {
				model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
			}
			model.addAttribute("proModel", proModel);
			model.addAttribute("leader", leader);
			model.addAttribute("cuser", cuser);

			if (StringUtils.isEmpty(workFlow)) {
				return proModelService.applayForm(FormPageType.FPT_APPLY, model, request, response, proModel, proProject, actYw);
			}

			return workFlow.applayForm(FormPageType.FPT_APPLY, model, request, response, proModel, proProject, actYw);
		}catch (Exception e){
			logger.error("form 异常",e);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code",0);
		jsonObject.put("msg","form 异常");
		return jsonObject.toString();
    }


    /**
     * 报告 申请材料 logo
     *
     * @param proModel
     * @param model
     */
    private void setExtraInfo(ProModel proModel, Model model) {
        //报告（中期、结项等）
        ProReport report = new ProReport();
        report.setProModelId(proModel.getId());
        List<ProReport> reports = proReportService.findList(report);
        for (ProReport proReport : reports) {
            SysAttachment sa = new SysAttachment();
            sa.setUid(proModel.getId());
            sa.setType(FileTypeEnum.S11);
            sa.setGnodeId(proReport.getGnodeId());//每个节点的附件
            if(proReport.getGnodeId()!=null){
                ActYwGnode actYwGnode=actYwGnodeService.get(proReport.getGnodeId());
                proReport.setGnodeName(actYwGnode.getName());
            }
            proReport.setFiles(sysAttachmentService.getFiles(sa));
        }
        model.addAttribute("reports", reports);

        SysAttachment sa = new SysAttachment();
        sa.setUid(proModel.getId());
        sa.setType(FileTypeEnum.S11);
        List<SysAttachment> fileList = sysAttachmentService.getFiles(sa);
        if (!fileList.isEmpty()) {
            List<SysAttachment> applyFiles = new ArrayList<>();
            for (SysAttachment sysAttachment : fileList) {
                if (sysAttachment.getFileStep() == null) {
                    continue;
                }
                if (FileStepEnum.S1101.getValue().equals(sysAttachment.getFileStep().getValue())) {
                    model.addAttribute("logo", sysAttachment);//logo
                } else if (FileStepEnum.S1102.getValue().equals(sysAttachment.getFileStep().getValue())) {
                    applyFiles.add(sysAttachment);//申报附件
                }
            }
            model.addAttribute("applyFiles", applyFiles);//申报附件
        }
        //审核记录
        List<ActYwAuditInfo> actYwAuditInfos = proModelService.getFrontActYwAuditInfo(proModel.getId(), false);
        if (!actYwAuditInfos.isEmpty()) {
            model.addAttribute("actYwAuditInfos", actYwAuditInfos);
        }
    }

    /**
     * 查看项目详细信息
     * @param proModel
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "viewForm")
    public String viewForm(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 查看页面基本信息，所有的查看页面共有,与表单页面类型，和项目类型无关.
         */
        ActYw actYw = actYwService.get(proModel.getActYwId());
        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(actYw);
        if(actYw==null){
            return CorePages.ERROR_404.getIdxUrl();
        }
        ProProject proProject = actYw.getProProject();
        if (proProject != null) {
            showFrontMessage(proProject, model);
        }

        if (StringUtil.isNotEmpty(proModel.getDeclareId())) {
            model.addAttribute("sse", systemService.getUser(proModel.getDeclareId()));
        } else {
            logger.warn("申报人不存在！");
        }
        this.setExtraInfo(proModel, model);
        model.addAttribute("actYw", actYw);
        model.addAttribute("proModel", proModel);
        model.addAttribute("groupId", actYw.getGroupId());
        model.addAttribute("projectName", actYw.getProProject().getProjectName());
		CompletableFuture<Team> teamFuture = CompletableFuture.supplyAsync(() -> teamService.get(proModel.getTeamId()), ThreadUtils.newFixedThreadPool());
		CompletableFuture<List<Map<String, String>>> studentFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamStudentFromTUHByTeamId(proModel.getTeamId()), ThreadUtils.newFixedThreadPool());
		CompletableFuture<List<Map<String, String>>> teacherFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(), proModel.getId()), ThreadUtils.newFixedThreadPool());
        CompletableFuture.allOf(teamFuture,studentFuture,teacherFuture).join();
        List<Map<String, String>> studentList = null;
        List<Map<String, String>> teacherList = null;
        Team team = null;
        try {
            team = teamFuture.get();
            studentList = studentFuture.get();
            teacherList = teacherFuture.get();
        } catch (InterruptedException e) {
            logger.info("查询线程中断,ajaxFindTeamPerson");
            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.info("执行异常,ajaxFindTeamPerson");
            e.printStackTrace();
        }finally {
            ThreadUtils.shutdown();
        }
		model.addAttribute("team", team);
        model.addAttribute("teamStu",studentList);
        model.addAttribute("teamTea", teacherList);

        /**
         * 前台查看页面特殊需求参数及页面跳转.
         */
        if (org.springframework.util.StringUtils.isEmpty(workFlow)) {
            return proModelService.viewForm(FormPageType.FPT_VIEWA, model, request, response, proModel, actYw);
        }
        return workFlow.viewForm(FormPageType.FPT_VIEWF, model, request, response, proModel, actYw);
    }

    /**
     * 重新编辑
     * @param proModel
     */
    @RequestMapping(value = "editAfterApplyed")
    public void editAfterApplyed(ProModel proModel){

    }

    /**
     * 撤回
     * @param proModel proModel
     */
    @ResponseBody
    @RequestMapping(value = "revertAfterApplyed")
    public ApiResult revertAfterApplyed(ProModel proModel){
        String send = proModel.getIsSend();
        if (send == null){
            return ApiResult.failed(0,"该项目异常");
        }
        if (send.equals("1")){
            return ApiResult.failed(0,"已推送省级，不能撤回");
        }
        //1.没有被审核记录
        List<ActYwAuditInfo> list = Optional.ofNullable(actYwAuditInfoService.getProModelAuditInfo(proModel.getId())).orElse(new ArrayList<>());
        if (list.size() > 0){
            return ApiResult.failed(0,"该项目已经开始审核,不能撤销");
        }
        //2.撤销时间已过
        Date now = new Date();
        try{
            ActYwYear ayy = Optional.ofNullable(ActYwUtils.findActYwYear(proModel.getActYwId(),TenantConfig.getCacheTenant()))
                    .orElse(commonService.getApplyActYwYear(proModel.getActYwId())) ;
            if (ayy != null) {
                Date end = ayy.getNodeEndDate();
                if (end != null && end.before(now)) {
                    return ApiResult.failed(0,"项目申报时间已截止,不能撤销");
                }
                proModelService.revertBySelf(proModel);
                return ApiResult.success(1,"撤销成功");
            }

        }catch (Exception e){
           logger.error("撤销失败",e.getMessage());

        }
        return ApiResult.failed(0,"撤销失败");
    }

    /**
     * 前台查看页面特殊需求参数及页面跳转.
     */
    private String dealPageView(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
        FormTheme formTheme = actYw.getFtheme();
        if (formTheme != null) {
            FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
            if ((FormTheme.F_MR).equals(formTheme)) {
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            } else if ((FormTheme.F_MD).equals(formTheme)) {
                //参数实现已经移动至实现类FppMd
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService, proModelMdService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            } else if ((FormTheme.F_COM).equals(formTheme)) {
                //参数实现已经移动至实现类FppCom
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            } else {
                logger.error("当前流程主题未定义！");
            }
        } else {
            logger.error("流程主题不存在！");
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @RequestMapping(value = "save")
    public String save(ProModel proModel, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        if (!beanValidator(model, proModel)) {
            return form(proModel, model, request, response);
        }
        proModelService.save(proModel);
        addMessage(redirectAttributes, "保存proModel成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/promodel/proModel/?repage";
    }


    @ResponseBody
    @RequestMapping(value = "findTeamPerson")
    public JSONObject findTeamPerson(@RequestParam(required = true) String id, @RequestParam(required = false) String type, @RequestParam(required = true) String actywId) {
        //List<Map<String,String>>
        JSONObject js = new JSONObject();

        js.put("teamId", id);
        js.put("ret", 0);

        ActYw actYw = actYwService.get(actywId);
        String subType = actYw.getProProject().getType();//项目分类
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        PersonNumConf pnc = new PersonNumConf();
        if (StringUtil.isNotEmpty(type)) {
            pnc = SysConfigUtil.getProPersonNumConf(scv, subType, type);
        } else {
            pnc = SysConfigUtil.getGconPersonNumConf(scv, subType);
        }
        if (pnc != null) {
            Team teamNums = teamService.findTeamJoinInNums(id);
            if ("1".equals(pnc.getTeamNumOnOff())) {//团队人数范围
                ConfRange cr = pnc.getTeamNum();
                int min = Integer.valueOf(cr.getMin());
                int max = Integer.valueOf(cr.getMax());
                if (teamNums.getMemberNum() < min || teamNums.getMemberNum() > max) {
                    if (min == max) {
                        js.put("msg", DictUtils.getDictLabel(subType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队成员人数为" + min + "人");
                    } else {
                        js.put("msg", DictUtils.getDictLabel(subType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队成员人数为" + min + "~" + max + "人");
                    }
                    return js;
                }
            }
            if ("1".equals(pnc.getSchoolTeacherNumOnOff())) {//校园导师人数范围
                ConfRange cr = pnc.getSchoolTeacherNum();
                int min = Integer.valueOf(cr.getMin());
                int max = Integer.valueOf(cr.getMax());
                if (teamNums.getSchoolTeacherNum() < min || teamNums.getSchoolTeacherNum() > max) {
                    if (min == max) {
                        js.put("msg", DictUtils.getDictLabel(subType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队校园导师为" + min + "人");
                    } else {
                        js.put("msg", DictUtils.getDictLabel(subType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队校园导师为" + min + "~" + max + "人");
                    }
                    return js;
                }
            }
            if ("1".equals(pnc.getEnTeacherNumOnOff())) {//企业导师人数范围
                ConfRange cr = pnc.getEnTeacherNum();
                int min = Integer.valueOf(cr.getMin());
                int max = Integer.valueOf(cr.getMax());
                if (teamNums.getEnterpriseTeacherNum() < min || teamNums.getEnterpriseTeacherNum() > max) {
                    if (min == max) {
                        js.put("msg", DictUtils.getDictLabel(subType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目企业导师为" + min + "人");
                    } else {
                        js.put("msg", DictUtils.getDictLabel(subType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目企业导师为" + min + "~" + max + "人");
                    }
                    return js;
                }
            }
        }

		/*if (type != null) {
            if ((type.equals("1")||type.equals("2"))  && (stuNum>5||stuNum<1)) {
				js.put("ret",0);
				js.put("msg","该团队人数不符合，创新创业训练人数为1-5人。");
				return  js;
			}
			if ((type.equals("3"))  &&(stuNum>7||stuNum<1)) {
				js.put("ret",0);
				js.put("msg","该团队人数不符合，创业实践人数为1-7人。");
				return  js;
			}
		}*/

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<Map<String, String>> list1 = projectDeclareService.findTeamStudent(id);
        List<Map<String, String>> list2 = projectDeclareService.findTeamTeacher(id);
        for (Map<String, String> map : list1) {
            list.add(map);
        }
        for (Map<String, String> map : list2) {
            list.add(map);
        }
        js.put("ret", 1);
        js.put("map", list);
        return js;
    }


    @RequestMapping(value = "submit")
    @ResponseBody
    public JSONObject submit(ProModel proModel, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        ProModel proModelIndex = proModel.getProModel();
        AttachMentEntity attachMentEntity = proModelIndex.getAttachMentEntity();
        proModel.setAttachMentEntity(attachMentEntity);
        String gnodeId = request.getParameter("gnodeId");
        if (StringUtil.isNotEmpty(gnodeId)) {
            proModel.getAttachMentEntity().setGnodeId(gnodeId);
        }
        try {
            ActYw actYw = actYwService.get(proModel.getActYwId());
            if (actYw != null){
                String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
                IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
                js=workFlow.submit(proModel, js);
            }
        } catch (GroupErrorException e) {
            js.put("ret", 0);
            js.put("msg", e.getCode());
        }
        return js;
    }

    @RequestMapping(value = "checkProName")
   	@ResponseBody
   	public boolean linkList(ProModel proModel, HttpServletRequest request, HttpServletResponse response) {
        boolean isHave=proModelService.checkName(proModel);
        return !isHave;
   	}

    @RequestMapping(value = "submitMid")
    @ResponseBody
    public JSONObject submitMid(ProModel proModel, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        JSONObject js = new JSONObject();
        js.put("ret", 1);
        String gnodeId = request.getParameter("gnodeId");
        if (StringUtil.isNotEmpty(gnodeId)) {
            proModel.getAttachMentEntity().setGnodeId(gnodeId);
        }
        String msg = proModelService.submitMid(proModel);
        js.put("msg", msg);
        return js;
    }

    @RequestMapping(value = "delete")
    public String delete(ProModel proModel, RedirectAttributes redirectAttributes) {
        proModelService.delete(proModel);
        addMessage(redirectAttributes, "删除proModel成功");
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/?repage";
    }

    public static void showFrontMessage(ProProject proProject, Model model) {
        List<Dict> finalStatusMap = new ArrayList<Dict>();
        if (proProject.getFinalStatus() != null) {
            String finalStatus = proProject.getFinalStatus();
            if (finalStatus != null) {
                String[] finalStatuss = finalStatus.split(",");
                if (finalStatuss.length > 0) {
                    for (int i = 0; i < finalStatuss.length; i++) {
                        Dict dict = new Dict();
                        dict.setValue(finalStatuss[i]);
                        if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
                            dict.setLabel(DictUtils.getDictLabel(finalStatuss[i], "competition_college_prise", ""));
                        } else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
                            dict.setLabel(DictUtils.getDictLabel(finalStatuss[i], "project_result", ""));
                        }
                        finalStatusMap.add(dict);
                    }
                }
                model.addAttribute("finalStatusMap", finalStatusMap);
            }
        }
        //前台项目类型
        List<Dict> proTypeMap = new ArrayList<Dict>();
        if (proProject.getType() != null) {
            String proType = proProject.getType();
            if (proType != null) {
                String[] proTypes = proType.split(",");
                if (proTypes.length > 0) {
                    for (int i = 0; i < proTypes.length; i++) {
                        Dict dict = new Dict();
                        dict.setValue(proTypes[i]);
                        if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
                            dict.setLabel(DictUtils.getDictLabel(proTypes[i], "competition_type", ""));
                        } else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
                            dict.setLabel(DictUtils.getDictLabel(proTypes[i], "project_style", ""));
                        }
                        proTypeMap.add(dict);
                    }
                }
                model.addAttribute("proTypeMap", proTypeMap);
            }
        }
        //前台项目类别
            /*List<Map<Dict>> proCategoryMap=new ArrayList<Map<String, String>>();*/
        List<Dict> proCategoryMap = new ArrayList<Dict>();
        if (proProject.getProCategory() != null) {
            String proCategory = proProject.getProCategory();
            if (proCategory != null) {
                String[] proCategorys = proCategory.split(",");
                if (proCategorys.length > 0) {
                    for (int i = 0; i < proCategorys.length; i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        Dict dict = new Dict();
                        map.put("value", proCategorys[i]);
                        dict.setValue(proCategorys[i]);
                        if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
                            map.put("label", DictUtils.getDictLabel(proCategorys[i], "competition_net_type", ""));
                            dict.setLabel(DictUtils.getDictLabel(proCategorys[i], "competition_net_type", ""));
                        } else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
                            map.put("label", DictUtils.getDictLabel(proCategorys[i], FlowPcategoryType.PCT_XM.getKey(), ""));
                            dict.setLabel(DictUtils.getDictLabel(proCategorys[i], FlowPcategoryType.PCT_XM.getKey(), ""));
                        }
                        //proCategoryMap.add(map);
                        proCategoryMap.add(dict);
                    }
                }
                model.addAttribute("proCategoryMap", proCategoryMap);
            }
        }
        //前台项目类别
        List<Dict> prolevelMap = new ArrayList<Dict>();
        if (proProject.getLevel() != null) {
            String proLevel = proProject.getLevel();
            if (proLevel != null) {
                String[] proLevels = proLevel.split(",");
                if (proLevels.length > 0) {
                    for (int i = 0; i < proLevels.length; i++) {
                        Dict dict = new Dict();
                        dict.setValue(proLevels[i]);
                        dict.setLabel(DictUtils.getDictLabel(proLevels[i], "gcontest_level", ""));
                        prolevelMap.add(dict);
                    }
                }
                model.addAttribute("prolevelMap", prolevelMap);
            }
        }
    }

    /*下载模板*/
    @RequestMapping(value = "/downTemplate")
    public void downTemplate(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            String fileName = null;
            String ext = null;
            String path = rootpath + File.separator + "static" + File.separator + "project-word" + File.separator;
            File fi = new File(path + type + ".json");
            if (fi.exists()) {
                JsonParser jsonParser = new JsonParser();
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fi), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                JsonObject json=(JsonObject) jsonParser.parse(bufferedReader);
                fileName = json.get("fileName").getAsString();
                ext = json.get("ext").getAsString();
            }
            if (!fi.exists()) {
                return;
            }
            out = response.getOutputStream();
            String headStr = "attachment; filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            fi = new File(path + type + ext);
            fs = new FileInputStream(fi);
            out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = fs.read(b)) > 0) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(fs);
        }
    }

    @RequestMapping(value="getTeacherAuditTaskList")
    public String getMyAuditTaskIndex(HttpServletRequest request, HttpServletResponse response, Model model,ProModel proModel){
        Page<ProModel> page=proModelService.getFirstTeacherPromodel(new Page(request, response),proModel);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "front/frontTeacherTask";
    }
    //前台导师审核
    @RequestMapping(value="ajaxGetTeacherAuditTaskList", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxGetTeacherAuditTaskList(HttpServletRequest request, HttpServletResponse response, Model model,ProModel proModel){
        try {
            Page<ProModel> page=proModelService.getFirstTeacherPromodel(new Page(request, response),proModel);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /*
    前台导师审核跳转页
     */
    @RequestMapping(value = "auditForm")
    public String audit(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
//        String actionPath = request.getParameter("actionPath");
        String gnodeId = request.getParameter("gnodeId");
        String taskName = request.getParameter("taskName");
        if (StringUtil.isNotEmpty(taskName)) {
            try {
                model.addAttribute("taskName", new String(URLDecoder.decode(taskName, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.toString());
            }
        }
        String proModelId = request.getParameter("proModelId");
        proModel = proModelService.get(proModelId);
        if(proModel==null){
            return CorePages.ERROR_404.getIdxUrl();
        }
        ActYw actYw = actYwService.get(proModel.getActYwId());
        model.addAttribute("proModel", proModel);
        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(actYw);
        if (org.springframework.util.StringUtils.isEmpty(workFlow)) {
            proModelService.audit(gnodeId,proModelId, model);
        } else {
            workFlow.audit(gnodeId,proModelId, model);
        }
        if (proModel.getSubTime() != null) {
            model.addAttribute("sysdate", DateUtil.formatDate(proModel.getSubTime(), "yyyy-MM-dd"));
        } else {
            model.addAttribute("sysdate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        }
        if (proModel.getTeamId() != null) {
            Team team = teamService.get(proModel.getTeamId());
            model.addAttribute("team", team);
            model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModel.getTeamId(), proModel.getId()));
            model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(), proModel.getId()));
        }
        this.setExtraInfo(proModel, model);
        if (actYw != null) {
            model.addAttribute("actYw", actYw);
            model.addAttribute("proProject", actYw.getProProject());
        }
        model.addAttribute("gnodeId", gnodeId);
        //得到当前任务节点
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
        if(actYwNextGnode.getId().equals(gnodeId)){
            model.addAttribute("isFirst", "1");
        }
        //根据gnodeId得到下一个节点是否为网关，是否需要网关状态
        List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
        if (actYwStatusList != null) {
            model.addAttribute("actYwStatusList", actYwStatusList);
        }
        return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "front/frontTeacherAuditTask";
    }

    //前台导师审核
    @RequestMapping(value = "promodelGateAudit")
    public String promodelGateAudit(ProModel proModel, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response, Model model) {
        //proModelService.audit(proModel);
        String gnodeId = request.getParameter("gnodeId");
        ActYw actYw =actYwService.get(proModel.getActYwId());
        try {
            String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
            IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
            workFlow.auditByGateWay(proModel, gnodeId,request);
            //proModelService.auditWithGateWay(proModel, gnodeId);
        } catch (GroupErrorException e) {
            addMessage(redirectAttributes, e.getCode());
        }
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/promodel/proModel/getTeacherAuditTaskList";
    }
    //前台导师审核
    @RequestMapping(value="saveProModelGateAudit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult saveProModelGateAudit(ProModel proModel, HttpServletRequest request){
        try {
            String gnodeId = request.getParameter("gnodeId");
            ActYw actYw =actYwService.get(proModel.getActYwId());
            try {
                String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
                IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
                workFlow.auditByGateWay(proModel, gnodeId,request);
                //proModelService.auditWithGateWay(proModel, gnodeId);
            } catch (Exception e) {
                logger.error(ExceptionUtil.getStackTrace(e));
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ExceptionUtil.getStackTrace(e));
//                logger.error(e.getMessage());
//                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
            }
            return ApiResult.success("/f/promodel/proModel/getTeacherAuditTaskList");
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="getActYwAuditList/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getActYwAuditList(@PathVariable String id){
        try {
            return  ApiResult.success(proModelService.getActYwAuditInfo(id, true));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取省模板文件
     * @param schoolActYwId
     * @return
     */
    @RequestMapping(value="getProvinceDoc", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getProvinceDoc(String schoolActYwId,String fileStep){
        Optional.ofNullable(schoolActYwId).orElseThrow(()->new ApplyException("参数不能为空"));
        List<SysAttachment> sysAttachmentList = Optional.ofNullable(sysAttachmentService.getProvinceDoc(schoolActYwId,fileStep)).orElse(new ArrayList<>());
        return ApiResult.success(sysAttachmentList);
    }

}
