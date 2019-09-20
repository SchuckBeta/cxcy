/**
 *
 */
package com.oseasy.pro.modules.promodel.web.front;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwForm;
import com.oseasy.act.modules.actyw.service.ActYwFormService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProReport;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.service.ProReportService;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 内容管理Controller
 *
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${frontPath}/cms")
public class FrontProCmsController extends BaseController {
    public static final String CMS_FORM_FRONT = CoreSval.getFrontPath() + "/cms/form/";
    @Autowired
    private ProReportService proReportService;
    @Autowired
    private ActYwFormService actYwFormService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProModelMdService proModelMdService;
    @Autowired
    SysAttachmentService sysAttachmentService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;

    /*流程模板静态文件*/
    @RequestMapping(value = "form/{template}/{pageName}")
    public String modelForm(@PathVariable String pageName, @PathVariable String template, HttpServletRequest request, HttpServletResponse response, Model model) {
        //标识流程以及模型
        String actywId = request.getParameter("actywId");
        //标识流程那个环节
        String gnodeId = request.getParameter("gnodeId");
        String pageType = request.getParameter("pageType");//区分查看和提交页面
        //项目Id
        String proModelId = request.getParameter("promodelId");
        ActYw actYw = actYwService.get(actywId);
        String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);

        if (actYw == null) {
            return CorePages.ERROR_MISS.getIdxUrl();
        }

        ProProject proProject = actYw.getProProject();
        model.addAttribute("sysdate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        model.addAttribute("proProject", proProject);
        model.addAttribute("actYw", actYw);
        if (proProject != null) {
            //配置展示信息
            showFrontMessage(proProject, model);
        }
        User user = UserUtils.getUser();
        if (StringUtil.isNotEmpty(gnodeId)) {
            model.addAttribute("gnodeId", gnodeId);
        }
        if (StringUtil.isEmpty(pageName)) {
            return CorePages.ERROR_MISS.getIdxUrl();
        }
        //根据匹配传页面需要参数，数据
        model.addAttribute("projectName", actYw.getProProject().getProjectName());
        //appfrom 申报表单
        if (pageName.contains("applyForm")) {
            if (org.springframework.util.StringUtils.isEmpty(workFlow)) {
                return proModelService.applayForm(FormPageType.FPT_APPLY, model, request, response, new ProModel(), proProject, actYw);
            } else {
                String url=workFlow.applayForm(FormPageType.FPT_APPLY, model, request, response, new ProModel(), proProject, actYw);
                return url;
            }
        } else {
            //中期表单以及结项表单等其他前台表单
            if (StringUtil.isEmpty(gnodeId)) {
                return CorePages.ERROR_MISS.getIdxUrl();
            }
            ActYwForm actYwForm = actYwFormService.get(pageName);
            String url = actYwForm.getPath();
            if (StringUtil.isEmpty(url)) {
                return CorePages.ERROR_MISS.getIdxUrl();
            }

            workFlow.reportForm(model, request, response, proModelId);

            //自定义项目
            //自定义项目
            if (CoreSval.VIEW.equals(pageType)) {
               url = url + "View";
            }else{
                boolean isReport= proModelService.checkIsHasReport(gnodeId,proModelId);
                if(!isReport){
                    model.addAttribute("msg", "该任务已处理");
                    return "modules/website/html/formMsg";
                }
            }
            if (FormTheme.F_MD.getKey().equals(actYw.getKeyType()) || FormTheme.F_MD_XM.getKey().equals(actYw.getKeyType())) {
                proMdMidAndClos(actYw.getProProject(), model, pageType, proModelId, actYw.getId(), gnodeId);
            } else {
                proCommonMidAndClos(model, pageType, proModelId, actYw, gnodeId);
            }
            return url;
        }
    }


    private void showFrontMessage(ProProject proProject, Model model) {
        //审核级别
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
                        //proCategoryMap.add(map);
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
                            map.put("label", DictUtils.getDictLabel(proCategorys[i], "project_type", ""));
                            dict.setLabel(DictUtils.getDictLabel(proCategorys[i], "project_type", ""));
                        }
                        //proCategoryMap.add(map);
                        proCategoryMap.add(dict);
                    }
                }
                model.addAttribute("proCategoryMap", proCategoryMap);
            }
        }
        //前台项目级别
        List<Dict> prolevelMap = new ArrayList<Dict>();
        if (proProject.getLevel() != null) {
            String proLevel = proProject.getLevel();
            if (proLevel != null) {
                String[] proLevels = proLevel.split(",");
                if (proLevels.length > 0) {
                    for (int i = 0; i < proLevels.length; i++) {
                        Dict dict = new Dict();
                        dict.setValue(proLevels[i]);
                        dict.setLabel(DictUtils.getDictLabel(proLevels[i], "competition_format", ""));
                        prolevelMap.add(dict);
                    }
                }
                model.addAttribute("prolevelMap", prolevelMap);
            }
        }
    }

    private void proMdMidAndClos(ProProject proProject, Model model, String pageType, String proModelId, String actywId, String gnodeId) {
        ProModelMd proModelMd = proModelMdService.getByProModelId(proModelId);
        if (proModelMd != null) {
            model.addAttribute("proModelMd", proModelMd);
            model.addAttribute("proModel", proModelMd.getProModel());
            model.addAttribute("sse", systemService.getUser(proModelMd.getProModel().getDeclareId()));
            model.addAttribute("proProject", proProject);
            model.addAttribute("wprefix", IWparam.getFileTplPreFix());
            model.addAttribute("wtypes", Wtype.toJson());
            ProModel proModelapp = proModelMd.getProModel();
            if (proModelapp.getTeamId() != null) {
                //查找团队信息
                model.addAttribute("team", teamService.get(proModelapp.getTeamId()));
                model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModelapp.getTeamId(), proModelapp.getId()));
                model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModelapp.getTeamId(), proModelapp.getId()));
            }
            SysAttachment sa = new SysAttachment();
            sa.setUid(proModelMd.getProModel().getId());
			/*sa.setType(FileTypeEnum.S10);
			sa.setFileStep(FileStepEnum.S2000);*/
            sa.setGnodeId(gnodeId);
            List<SysAttachment> fileListMap = sysAttachmentService.getFiles(sa);
            if (fileListMap != null) {
                model.addAttribute("sysAttachments", fileListMap);
            }
        }
    }

    private void proCommonMidAndClos(Model model, String pageType, String proModelId, ActYw actYw, String gnodeId) {
        ProModel proModel = proModelService.get(proModelId);
        model.addAttribute("sse", systemService.getUser(proModel.getDeclareId()));
        model.addAttribute("projectName", actYw.getProProject().getProjectName());
        model.addAttribute("team", teamService.get(proModel.getTeamId()));
        model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModel.getTeamId(), proModel.getId()));
        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(), proModel.getId()));
        model.addAttribute("proModel", proModel);
        model.addAttribute("gnode", actYwGnodeService.get(gnodeId));


        SysAttachment sa = new SysAttachment();
        sa.setUid(proModel.getId());
        sa.setType(FileTypeEnum.S11);
        List<SysAttachment> fileList = sysAttachmentService.getFiles(sa);
        if (!fileList.isEmpty()) {
            List<SysAttachment> applyFiles = new ArrayList<>();
            for (SysAttachment sysAttachment : fileList) {
                if (sysAttachment.getFileStep() != null) {
                    if (FileStepEnum.S1101.getValue().equals(sysAttachment.getFileStep().getValue())) {
                        model.addAttribute("logo", sysAttachment);//logo
                    } else if (FileStepEnum.S1102.getValue().equals(sysAttachment.getFileStep().getValue())) {
                        applyFiles.add(sysAttachment);//申报附件
                    }
                }
            }

            model.addAttribute("applyFiles", applyFiles);//申报附件
        }
        if (ProSval.VIEW.equals(pageType) || ProSval.EDIT.equals(pageType)) {
            ProReport pr = proReportService.getByGnodeId(proModelId, gnodeId);
            if (pr != null) {
                SysAttachment s = new SysAttachment();
                s.setUid(proModelId);
                s.setGnodeId(gnodeId);
                pr.setFiles(sysAttachmentService.getFiles(s));
                model.addAttribute("proReport", pr);
            }
        }
    }

}
