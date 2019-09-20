package com.oseasy.pro.modules.promodel.web;

import static com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum.S1102;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.modules.act.utils.ThreadUtils;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.service.*;
import com.oseasy.act.modules.actyw.vo.ActYwRuntimeException;import com.oseasy.act.common.config.ActSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.service.ProModelHsxmService;
import com.oseasy.pro.modules.workflow.service.ProvinceProModelService;
import com.oseasy.scr.modules.scr.service.ScrProModelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.utils.ProcessDefUtils;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormStyleType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.cms.modules.cms.web.CmsController;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProReport;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.service.ProReportService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FormPage;
import com.oseasy.pro.modules.promodel.vo.ProcessVo;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.vo.TeamStudentVo;
import com.oseasy.sys.modules.team.vo.TeamTeacherVo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * proModelController.
 * @author zy
 * @version 2017-07-13
 */
@Controller
@RequestMapping(value = "${adminPath}/promodel/proModel")
public class ProModelController extends BaseController {
    @Autowired
    private ProModelMdService proModelMdService;
    @Autowired
    private   ActYwStatusService actYwStatusService;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProvinceProModelService provinceProModelService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ActYwGformService actYwGformService;
    @Autowired
    private ProReportService proReportService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ProActTaskService proActTaskService;
	@Autowired
	private ActYwAuditInfoService actYwAuditInfoService;
	@Autowired
	private ActYwYearService actYwYearService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ScrProModelService scrProModelService;
	@Autowired
	private ActYwPscrelService actYwPscrelService;
    @Autowired
    private ProModelHsxmService proModelHsxmService;



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

    @RequestMapping(value = "getTaskAssignCountToDo")
    @ResponseBody
    public int getTaskAssignCountToDo(String actYwId) {
        return actTaskService.recordIdsAllAssign(actYwId);
    }

    @RequiresPermissions("promodel:proModel:view")
    @RequestMapping(value = {"list", ""})
    public String list(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ProModel> page = proModelService.findPage(new Page<ProModel>(request, response), proModel);
        model.addAttribute("page", page);
        return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "proModelList";
    }

    @RequestMapping(value = "view")
    public String view(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("proModel", proModel);
        return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "view";
    }

	@RequestMapping(value = "process")
   public String process(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
       	model.addAttribute("proModel", proModel);
		model.addAttribute("teamStu", projectDeclareService.findTeamStudentByTeamId(proModel.getTeamId(), proModel.getId()));
		model.addAttribute("teamTea", projectDeclareService.findTeamTeacherByTeamId(proModel.getTeamId(), proModel.getId()));
		List<ProcessVo>  proVoList=new ArrayList<ProcessVo>();
		//周报
		ProReport report = new ProReport();
		report.setProModelId(proModel.getId());
		List<ProReport> reports = proReportService.findList(report);
		for (ProReport proReport : reports) {
			SysAttachment sa=new SysAttachment();
			sa.setUid(proModel.getId());
			sa.setType(FileTypeEnum.S11);
			sa.setGnodeId(proReport.getGnodeId());//每个节点的附件
			proReport.setFiles(sysAttachmentService.getFiles(sa));
			ProcessVo processVo=new ProcessVo();
			processVo.setName("周报");
			processVo.setStatus("1");
			processVo.setFileList(proReport.getFiles());
			processVo.setTime(proReport.getUpdateDate());
			proVoList.add(processVo);
		}
		//附件

		ActYw actYw=actYwService.get(proModel.getActYwId());
		ActYwGnode actYwGnodeIndex = new ActYwGnode(new ActYwGroup(actYw.getGroupId()));
		List<ActYwGnode> sourcelist = actYwGnodeService.findListByMenu(actYwGnodeIndex);
		ActYwGnode curActYwGnode=new ActYwGnode();
		Boolean isCur =true;
		//判断项目是否结束
		if(! "1".equals(proModel.getState())){
			curActYwGnode = proActTaskService.getNodeByProInsId(proModel.getProcInsId());
		}

		for(int i=0;i<sourcelist.size();i++){
			ActYwGnode actYwGnode=sourcelist.get(i);
			SysAttachment sa=new SysAttachment();
			sa.setUid(proModel.getId());
			if(i==0){
				sa.setFileStep(S1102);
			}else {
				sa.setGnodeId(actYwGnode.getId());
			}
			List<SysAttachment> fileList =  sysAttachmentService.getFiles(sa);
			ProcessVo processVo=new ProcessVo();
			processVo.setName(actYwGnode.getName());

			if(isCur){//已经走过节点
				processVo.setStatus("1");
			}else{//未走过节点
				processVo.setStatus("0");
			}
			//判断项目进度
			if(!"1".equals(proModel.getState())){
				if (curActYwGnode != null && (actYwGnode.getId().equals(curActYwGnode.getId()) || actYwGnode.getId().equals(curActYwGnode.getParentId()))) {
					isCur = false;
				}
			}
			processVo.setType("node");
			if(StringUtil.checkNotEmpty(fileList)){
				processVo.setFileList(fileList);
                String dateStr = DateUtil.formatDate(fileList.get(0).getUpdateDate());
				processVo.setDate(dateStr);
                processVo.setTime(fileList.get(0).getUpdateDate());
			}
			proVoList.add(processVo);
		}
		Collections.sort(proVoList, new Comparator<ProcessVo>() {
			@Override
			public int compare(ProcessVo o1, ProcessVo o2) {
				if(o1.getTime()==null){
					return 1;
				}
				if(o2.getTime()==null){
					return -1;
				}
				int i = (int)o1.getTime().getTime() - (int)o2.getTime().getTime();
				return i;
			}
		});
		List<ProcessVo>  newProVoList=new ArrayList<ProcessVo>();
		for(int i=0;i<proVoList.size();i++){
			ProcessVo processVo=proVoList.get(i);
			if(!("1".equals(processVo.getStatus())&&StringUtil.isEmpty(processVo.getDate()))){
				newProVoList.add(processVo);
			}
		}
		model.addAttribute("timeLineData", newProVoList);

       return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "backTrack";
   }

    @RequestMapping(value = "auditForm")
    public String audit(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        String actionPath = request.getParameter("actionPath");
        String gnodeId = request.getParameter("gnodeId");
        String taskName = request.getParameter("taskName");
        if (StringUtils.isNotBlank(taskName)) {
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
			getTeamMembers(team,model);
			/*CompletableFuture<List<TeamStudentVo>> studentFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfStudentByTeamId(team.getId()), ThreadUtils.newFixedThreadPool());
			CompletableFuture<List<TeamTeacherVo>> teacherFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfTeacherByTeamId(team.getId()),ThreadUtils.newFixedThreadPool());
			CompletableFuture.allOf(studentFuture,teacherFuture).join();
			List<TeamStudentVo> teamStudentVos = null;
			List<TeamTeacherVo> teamTeacherVos = null;
			try {
				teamStudentVos = studentFuture.get();
				teamTeacherVos = teacherFuture.get();
			} catch (InterruptedException e) {
				logger.info("查询线程中断,queryRecordIds");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.info("执行异常,queryRecordIds");
				e.printStackTrace();
			}finally {
				ThreadUtils.shutdown();
			}
            model.addAttribute("teamStu", teamStudentVos);
            model.addAttribute("teamTea",teamTeacherVos);*/
        }

        this.setExtraInfo(proModel, model);

        if (actYw != null) {
            model.addAttribute("actYw", actYw);
            model.addAttribute("proProject", actYw.getProProject());
        }
        model.addAttribute("actionPath", actionPath);
        model.addAttribute("gnodeId", gnodeId);
        //得到当前任务节点
		ActYwGnode actYwGnode = actYwGnodeService.getByg(gnodeId);
		boolean isDelegate =actYwGnode.getIsDelegate();
		User user= UserUtils.getUser();
		List<Role> roleList=user.getRoleList();
		boolean isExpert =false;
		for(Role role:roleList){
			if(CoreSval.Rtype.EXPORT.getKey().equals(role.getRtype())){
				isExpert=true;
				break;
			}
		}
		if(isDelegate && isExpert){
			model.addAttribute("delegateExpert", "1");
			return "/template/form/project/tlxy_score";
		}
		if(actYwGnode.getIsDelegate()){
			model.addAttribute("isDelegate", "1");
			ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
			actYwAuditInfo.setPromodelId(proModelId);
			actYwAuditInfo.setGnodeId(gnodeId);
			actYwAuditInfo.setIsDelegate("1");
			List<ActYwAuditInfo> delegateList= actYwAuditInfoService.findList(actYwAuditInfo);// 审核记录，按时间正序排列
			if(StringUtil.checkNotEmpty(delegateList)){
				model.addAttribute("delegateList", delegateList);
			}
		}

		ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
		if(actYwNextGnode.getId().equals(gnodeId)){
			model.addAttribute("isFirst", "1");
		}
        //actYwGforms=actYwGnode.getGforms();
        ActYwGform af = new ActYwGform();
        af.setGnode(new ActYwGnode(gnodeId));
        List<ActYwGform> actYwGforms = actYwGformService.findList(af);
        String urlPath = "";
        if (actYwGforms != null && actYwGforms.size() > 0) {
            for (ActYwGform actYwGform : actYwGforms) {
                if (FormStyleType.FST_FORM.getKey().equals(actYwGform.getForm().getStyleType())) {
                    urlPath = actYwGform.getForm().getPath();
                    break;
                }
            }
        }
		proModelService.moneyList(proModel,gnodeId, model);

        //根据gnodeId得到下一个节点是否为网关，是否需要网关状态
        List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
        if (actYwStatusList != null) {
            model.addAttribute("actYwStatusList", actYwStatusList);
        }
        return urlPath;
    }


    @RequestMapping(value = "provAuditForm")
    public String provAuditForm(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        String actionPath = request.getParameter("pathUrl");
        String gnodeId = request.getParameter("gnodeId");
        String taskName = request.getParameter("taskName");
        if (StringUtils.isNotBlank(taskName)) {
            try {
                model.addAttribute("taskName", new String(URLDecoder.decode(taskName, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.toString());
            }
        }
        String provProModelId = request.getParameter("proModelId");
        ProvinceProModel prov=provinceProModelService.get(provProModelId);
        proModel = proModelService.getWith(prov.getModelId());
        if(proModel==null){
            return CorePages.ERROR_404.getIdxUrl();
        }

//        ProvinceProModel prov=provinceProModelService.getByProModelId(proModelId);
        ActYw actYw = actYwService.get(prov.getActYwId());

        model.addAttribute("proModel", proModel);
        model.addAttribute("proModelId", proModel.getId());
        model.addAttribute("actYwId", request.getParameter("actYwId"));

        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(actYw);
        if (org.springframework.util.StringUtils.isEmpty(workFlow)) {
            proModelService.audit(gnodeId,proModel.getId(), model);
        } else {
            workFlow.audit(gnodeId,proModel.getId(), model);
        }

        if (proModel.getSubTime() != null) {
            model.addAttribute("sysdate", DateUtil.formatDate(proModel.getSubTime(), "yyyy-MM-dd"));
        } else {
            model.addAttribute("sysdate", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        }

        if (proModel.getTeamId() != null) {
            Team team = teamService.getById(proModel.getTeamId());
            model.addAttribute("team", team);
			getTeamMembers(team,model);
			/*CompletableFuture<List<TeamStudentVo>> studentFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfStudentByTeamId(team.getId()), ThreadUtils.newFixedThreadPool());
			CompletableFuture<List<TeamTeacherVo>> teacherFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfTeacherByTeamId(team.getId()),ThreadUtils.newFixedThreadPool());
			CompletableFuture.allOf(studentFuture,teacherFuture).join();
			List<TeamStudentVo> teamStudentVos = null;
			List<TeamTeacherVo> teamTeacherVos = null;
			try {
				teamStudentVos = studentFuture.get();
				teamTeacherVos = teacherFuture.get();
			} catch (InterruptedException e) {
				logger.info("查询线程中断,queryRecordIds");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.info("执行异常,queryRecordIds");
				e.printStackTrace();
			}finally {
				ThreadUtils.shutdown();
			}
			model.addAttribute("teamStu", teamStudentVos);
			model.addAttribute("teamTea",teamTeacherVos);*/
        }

        this.setExtraInfo(prov.getId(), model);

        if (actYw != null) {
            model.addAttribute("actYw", actYw);
            model.addAttribute("proProject", actYw.getProProject());
        }
        model.addAttribute("actionPath", actionPath);
        model.addAttribute("gnodeId", gnodeId);
        //得到当前任务节点
        ActYwGnode actYwGnode = actYwGnodeService.getByg(gnodeId);
        boolean isDelegate =actYwGnode.getIsDelegate();
        User user= UserUtils.getUser();
        List<Role> roleList=user.getRoleList();
        boolean isExpert =false;
        for(Role role:roleList){
            if(CoreSval.Rtype.EXPORT.getKey().equals(role.getId())){
                isExpert=true;
                break;
            }
        }

        ActYwGform af = new ActYwGform();
        af.setGnode(new ActYwGnode(gnodeId));
        List<ActYwGform> actYwGforms = actYwGformService.findList(af);
        String urlPath = "";
        if (actYwGforms != null && actYwGforms.size() > 0) {
            for (ActYwGform actYwGform : actYwGforms) {
                if (FormStyleType.FST_FORM.getKey().equals(actYwGform.getForm().getStyleType())) {
                    urlPath = actYwGform.getForm().getPath();
                    break;
                }
            }
        }

        //根据gnodeId得到下一个节点是否为网关，是否需要网关状态
        List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
        if (actYwStatusList != null) {
            model.addAttribute("actYwStatusList", actYwStatusList);
        }
        return urlPath;
    }

    @RequestMapping(value="getActYwStatusListByGnodeId",  method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getActYwStatusListByGnodeId(String gnodeId, HttpServletRequest request){
        try {
            List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
            return ApiResult.success(actYwStatusList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_GATELIST_ERROR, ApiConst.getErrMsg(ApiConst.CODE_GATELIST_ERROR)+":"+e.getMessage());
        }
    }



    @RequestMapping(value = "promodelAudit")
    public String promodelAudit(ProModel proModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        proModelService.audit(proModel);
        //model.addAttribute("proModel", proModel);
        String actionPath = request.getParameter("actionPath");
        String gnodeId = request.getParameter("gnodeId");
        return CoreSval.REDIRECT + CmsController.CMS_FORM_ADMIN + "gContest/" + actionPath + "&gnodeId=" + gnodeId;
    }


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
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            gnodeId = actYwGnode.getParentId();
        }
        String actionPath = request.getParameter("actionPath");
        String actionUrl = actionPath + "?actywId=" + proModel.getActYwId() + "&gnodeId=" + gnodeId;
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/cms/form/gContest/" + actionUrl;
    }

	@RequestMapping(value = "promodelDelegateAudit")
    public String promodelDelegateAudit(ProModel proModel, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response, Model model) {
        //proModelService.audit(proModel);
        String gnodeId = request.getParameter("gnodeId");
//		ActYw actYw =actYwService.get(proModel.getActYwId());
		//委派审核
        proModelService.auditDelegate(proModel,gnodeId);
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            gnodeId = actYwGnode.getParentId();
        }
        String actionPath = request.getParameter("actionPath");
        String actionUrl = actionPath + "?actywId=" + proModel.getActYwId() + "&gnodeId=" + gnodeId;
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/cms/form/gContest/" + actionUrl;
    }


    @RequestMapping(value="saveProModelGateAudit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult saveProModelGateAudit(ProModel proModel, HttpServletRequest request){
        try {
            String gnodeId = request.getParameter("gnodeId");
//            ActYw actYw =actYwService.get(proModel.getActYwId());
              ActYw actYw = getActYw(proModel);
            try {
                String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
                IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
                workFlow.auditByGateWay(proModel, gnodeId,request);
                //proModelService.auditWithGateWay(proModel, gnodeId);
            } catch (GroupErrorException e) {
                logger.error(e.getMessage());
				logger.error(e.getCode());
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, e.getCode());
            }
            ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
            if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
                gnodeId = actYwGnode.getParentId();
            }
            String actionPath = request.getParameter("actionPath");
            String actionUrl = actionPath + "?actywId=" + proModel.getActYwId() + "&gnodeId=" + gnodeId;
            return ApiResult.success(actionUrl);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="saveProvProModelGateAudit")
    @ResponseBody
    public ApiResult saveProvProModelGateAudit(ProModel proModel, HttpServletRequest request){
        try {
            String gnodeId = request.getParameter("gnodeId");
            ProvinceProModel provinceProModel =provinceProModelService.getByProModelId(proModel.getId());
//            ProvinceProModel provinceProModel =provinceProModelService.getByProvinceProModelId(proModel.getId());
            try {

                provinceProModelService.auditWithGateWay(provinceProModel,proModel, gnodeId);
                //proModelService.auditWithGateWay(proModel, gnodeId);
            } catch (GroupErrorException e) {
                logger.error(e.getMessage());
                logger.error(e.getCode());
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR, e.getCode());
            }
            ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
            if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
                gnodeId = actYwGnode.getParentId();
            }
            String actionPath = request.getParameter("actionPath");
            String actionUrl = actionPath + "?actywId=" + provinceProModel.getActYwId() + "&gnodeId=" + gnodeId;
            return ApiResult.success(actionUrl,"评级成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, "评级异常");
        }
    }

    @RequiresPermissions("promodel:proModel:edit")
    @RequestMapping(value = "delete")
    public String delete(ProModel proModel, RedirectAttributes redirectAttributes) {
        proModelService.delete(proModel);
        addMessage(redirectAttributes, "删除proModel成功");
        return CoreSval.REDIRECT + CoreSval.getAdminPath() + "/promodel/proModel/?repage";
    }

    @RequestMapping(value = "viewForm")
    public String viewForm(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 查看页面基本信息，所有的查看页面共有,与表单页面类型，和项目类型无关.
         */
		ActYw actYw = getActYw(proModel);
		//     ActYw actYw = actYwService.get(proModel.getActYwId());
		if(actYw==null){
		    return CorePages.ERROR_404.getIdxUrl();
		}
        ProProject proProject = actYw.getProProject();
        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(actYw);
        if (proProject != null) {
            showFrontMessage(proProject, model);
        }
		if (StringUtil.isNotEmpty(proModel.getDeclareId())) {
			model.addAttribute("sse", systemService.getUser(proModel.getDeclareId()));
		} else {
			logger.warn("申报人不存在！");
		}
		this.setExtraInfo(proModel, model);
		model.addAttribute("secondName", "查看");
		model.addAttribute("actYw", actYw);
		model.addAttribute("taskName", "查看");
		model.addAttribute("proModel", proModel);
		model.addAttribute("groupId", actYw.getGroupId());
		model.addAttribute("projectName", actYw.getProProject().getProjectName());
		Team team = teamService.get(proModel.getTeamId());
		model.addAttribute("team", team);
		getTeamMembers(team,model);
		/*model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModel.getTeamId(), proModel.getId()));
		model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(), proModel.getId()));*/
		/**
		 * 后台查看页面特殊需求参数及页面跳转.
		 */
		if (org.springframework.util.StringUtils.isEmpty(workFlow)) {
			return proModelService.viewForm(FormPageType.FPT_VIEWA, model, request, response, proModel, actYw);
		}
		return workFlow.viewForm(FormPageType.FPT_VIEWA, model, request, response, proModel, actYw);
	}

    @RequestMapping(value="provViewForm", method = RequestMethod.GET)
    public String provViewForm(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 查看页面基本信息，所有的查看页面共有,与表单页面类型，和项目类型无关.
         */
        ProvinceProModel provinceProModel=provinceProModelService.getByProModelId(proModel.getId());
        ActYw actYw = actYwService.get(provinceProModel.getActYwId());

        if(actYw==null){
            return CorePages.ERROR_404.getIdxUrl();
        }
        ProProject proProject = actYw.getProProject();
        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(actYw);
        if (proProject != null) {
            showFrontMessage(proProject, model);
        }
        if (StringUtil.isNotEmpty(proModel.getDeclareId())) {
            model.addAttribute("sse", systemService.getUser(proModel.getDeclareId()));
        } else {
            logger.warn("申报人不存在！");
        }
        this.setExtraInfo(provinceProModel.getId(), model);
//        model.addAttribute("proModel", proModel);
        model.addAttribute("proModelId", proModel.getId());
        model.addAttribute("actYwId", request.getParameter("actYwId"));
        model.addAttribute("secondName", "查看");
        model.addAttribute("actYw", actYw);
        model.addAttribute("taskName", "查看");
        model.addAttribute("proModel", proModel);
        model.addAttribute("groupId", actYw.getGroupId());
        model.addAttribute("projectName", actYw.getProProject().getProjectName());
        Team team = teamService.get(proModel.getTeamId());
        model.addAttribute("team", team);
//		getTeamMembers(team,model);
        model.addAttribute("teamStu", projectDeclareService.findTeamStudentFromTUH(proModel.getTeamId(), proModel.getId()));
        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherFromTUH(proModel.getTeamId(), proModel.getId()));
        /**
         * 后台查看页面特殊需求参数及页面跳转.
         */
        if (org.springframework.util.StringUtils.isEmpty(workFlow)) {
            return proModelService.viewForm(FormPageType.FPT_VIEWA, model, request, response, proModel, actYw);
        }
        return workFlow.viewForm(FormPageType.FPT_VIEWA, model, request, response, proModel, actYw);
    }

    @RequestMapping(value = "projectEdit")
    public String projectEdit(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
		ActYw actYw = getActYw(proModel);
		// ActYw actYw = actYwService.get(proModel.getActYwId());
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
		model.addAttribute("secondName", "变更");
        model.addAttribute("actYw", actYw);
        model.addAttribute("taskName", request.getParameter("taskName"));

        model.addAttribute("groupId", actYw.getGroupId());
        model.addAttribute("projectName", actYw.getProProject().getProjectName());
		Team team=teamService.get(proModel.getTeamId());
        model.addAttribute("team", team);
		getTeamMembers(team,model);
        /*model.addAttribute("teamStu", projectDeclareService.findTeamStudentByTeamId(proModel.getTeamId(), proModel.getId()));
        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherByTeamId(proModel.getTeamId(), proModel.getId()));*/
        model.addAttribute("changeGnodes", proModelService.getProModelChangeGnode(proModel));
		proModel.setTeam(team);
		model.addAttribute("proModel", proModel);

		String key=FormTheme.getById(actYw.getGroup().getTheme()).getKey();
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
		workFlow.projectEdit(proModel, request,model);

		if(key!=null){
			return ProSval.path.vms(ProEmskey.PROMODEL.k()) + ""+key+"projectEdit";
		}else{
			return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "projectEdit";
		}
        //return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "projectEdit";
    }

    @RequestMapping(value = "ajaxProvViewForm", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxProvViewForm(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,Object> map=new HashMap<String,Object>();
            ProvinceProModel provinceProModel=provinceProModelService.getByProModelId(proModel.getId());
            ActYw actYw = actYwService.get(provinceProModel.getActYwId());
            if(actYw==null){
                return ApiResult.failed(ApiConst.CODE_NULL_ERROR, ApiConst.getErrMsg(ApiConst.CODE_NULL_ERROR));
            }
            proModel=provinceProModel.getProModel();
            if (StringUtil.isNotEmpty(proModel.getDeclareId())) {
                map.put("sse", systemService.getUser(proModel.getDeclareId()));
                //model.addAttribute("sse", systemService.getUser(proModel.getDeclareId()));
            } else {
                logger.warn("申报人不存在！");
            }
            this.setProvinceExtraInfo(provinceProModel.getId(), model,proModel.getId());
            map.put("secondName", "查看");
            map.put("actYw", actYw);
            map.put("taskName", "查看");
            map.put("proModel", proModel);
            map.put("groupId", actYw.getGroupId());
            map.put("projectName", actYw.getProProject().getProjectName());
            Team team = teamService.getById(proModel.getTeamId());
            map.put("team", team);
			CompletableFuture<List<TeamStudentVo>> studentFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfStudentByTeamId(team.getId()), ThreadUtils.newFixedThreadPool());
			CompletableFuture<List<TeamTeacherVo>> teacherFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfTeacherByTeamId(team.getId()),ThreadUtils.newFixedThreadPool());
			CompletableFuture.allOf(studentFuture,teacherFuture).join();
			List<TeamStudentVo> teamStudentVos = null;
			List<TeamTeacherVo> teamTeacherVos = null;
			try {
				teamStudentVos = studentFuture.get();
				teamTeacherVos = teacherFuture.get();
			} catch (InterruptedException e) {
				logger.info("查询线程中断,queryRecordIds");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.info("执行异常,queryRecordIds");
				e.printStackTrace();
			}finally {
				ThreadUtils.shutdown();
			}
//            getTeamMembers(team,model);
            map.put("teamStu",teamStudentVos);
            map.put("teamTea",teamTeacherVos);
//            HashMap<String, Object> modelAsMap = new HashMap<>();
            List<SysAttachment> applyFiles = (List<SysAttachment>) model.asMap().get("applyFiles");
            List<ActYwAuditInfo> actYwAuditInfos = (List<ActYwAuditInfo>) model.asMap().get("actYwAuditInfos");
            List<ProReport> reports = (List<ProReport>) model.asMap().get("reports");
            map.put("applyFiles", applyFiles);
            map.put("actYwAuditInfos", actYwAuditInfos);
            map.put("reports", reports);
            return ApiResult.success(map);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequestMapping(value = "provProjectEdit")
    public String provProjectEdit(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
//        ActYw actYw = getActYw(proModel);
        ProvinceProModel provinceProModel=provinceProModelService.getByProModelId(proModel.getId());
        ActYw actYw =actYwService.get(provinceProModel.getActYwId());
        proModel=get(proModel.getId());
        // ActYw actYw = actYwService.get(proModel.getActYwId());
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
        model.addAttribute("secondName", "变更");
        model.addAttribute("actYw", actYw);
        model.addAttribute("taskName", request.getParameter("taskName"));

        model.addAttribute("groupId", actYw.getGroupId());
        model.addAttribute("projectName", actYw.getProProject().getProjectName());
        Team team=teamService.get(proModel.getTeamId());
        model.addAttribute("team", team);
		getTeamMembers(team,model);
       /* model.addAttribute("teamStu", projectDeclareService.findTeamStudentByTeamId(proModel.getTeamId(), proModel.getId()));
        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherByTeamId(proModel.getTeamId(), proModel.getId()));*/
        model.addAttribute("changeGnodes", proModelService.getProModelChangeGnode(provinceProModel));
        proModel.setTeam(team);
        model.addAttribute("proModel", proModel);

        String key=FormTheme.getById(actYw.getGroup().getTheme()).getKey();
        String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
        workFlow.projectEdit(proModel, request,model);

        if(key!=null){
            return ProSval.path.vms(ProEmskey.PROMODEL.k()) + ""+key+"projectEdit";
        }else{
            return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "projectEdit";
        }
        //return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "projectEdit";
    }

	/**
	 * 项目变更
	 * @param id
	 * @param teamId
	 * @return
	 */
	@RequestMapping(value="getProChangedTeam/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getProChangedTeam(@PathVariable String id, String teamId){
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
			CompletableFuture<List<TeamStudentVo>> studentFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfStudentByTeamId(teamId), ThreadUtils.newFixedThreadPool());
			CompletableFuture<List<TeamTeacherVo>> teacherFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfTeacherByTeamId(teamId),ThreadUtils.newFixedThreadPool());
			CompletableFuture.allOf(studentFuture,teacherFuture).join();
			List<TeamStudentVo> teamStudentVos = null;
			List<TeamTeacherVo> teamTeacherVos = null;
			try {
				teamStudentVos = studentFuture.get();
				teamTeacherVos = teacherFuture.get();
			} catch (InterruptedException e) {
				logger.info("查询线程中断,queryRecordIds");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.info("执行异常,queryRecordIds");
				e.printStackTrace();
			}finally {
				ThreadUtils.shutdown();
			}
            hashMap.put("stus", teamStudentVos);
            hashMap.put("teas", teamTeacherVos);
            return ApiResult.success(hashMap);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "checkProName")
    @ResponseBody
    public boolean linkList(ProModel proModel, HttpServletRequest request, HttpServletResponse response) {
        boolean isHave=proModelService.checkName(proModel);
        return !isHave;
    }

    @RequestMapping(value = "gcontestEdit")
    public String gcontestEdit(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {

      //  ActYw actYw = actYwService.get(proModel.getActYwId());
		ActYw actYw = getActYw(proModel);
		if (actYw == null) {
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
		Team team = teamService.get(proModel.getTeamId());
        model.addAttribute("actYw", actYw);
		model.addAttribute("secondName", "变更");
        model.addAttribute("taskName", request.getParameter("taskName"));
        model.addAttribute("proModel", proModel);
        model.addAttribute("groupId", actYw.getGroupId());
        model.addAttribute("projectName", actYw.getProProject().getProjectName());
        model.addAttribute("team", team);

		getTeamMembers(team,model);
//        model.addAttribute("teamStu", projectDeclareService.findTeamStudentByTeamId(proModel.getTeamId(), proModel.getId()));
//        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherByTeamId(proModel.getTeamId(), proModel.getId()));
        model.addAttribute("changeGnodes", proModelService.getProModelChangeGnode(proModel));
		String key=FormTheme.getById(actYw.getGroup().getTheme()).getKey();
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
		workFlow.gcontestEdit(proModel, request,model);

		if(key!=null){
			return ProSval.path.vms(ProEmskey.PROMODEL.k()) + ""+key+"gcontestEdit";
		}else{
			return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "gcontestEdit";
		}

    }



	@RequestMapping(value = "provinceGcontestEdit")
	public String provinceGcontestEdit(ProModel proModel, Model model, HttpServletRequest request, HttpServletResponse response) {
		ProvinceProModel provinceProModel=provinceProModelService.getByProModelId(proModel.getId());
		ActYw actYw = actYwService.get(provinceProModel.getActYwId());
		proModel=get(proModel.getId());
		if (actYw == null) {
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
		Team team = teamService.get(proModel.getTeamId());
		model.addAttribute("actYw", actYw);
		model.addAttribute("secondName", "变更");
		model.addAttribute("taskName", request.getParameter("taskName"));
		model.addAttribute("proModel", proModel);
		model.addAttribute("groupId", actYw.getGroupId());
		model.addAttribute("projectName", actYw.getProProject().getProjectName());
		model.addAttribute("team", team);
		getTeamMembers(team,model);
//        model.addAttribute("teamStu", projectDeclareService.findTeamStudentByTeamId(proModel.getTeamId(), proModel.getId()));
//        model.addAttribute("teamTea", projectDeclareService.findTeamTeacherByTeamId(proModel.getTeamId(), proModel.getId()));
		model.addAttribute("changeGnodes", proModelService.getProModelChangeGnode(proModel));
		String key=FormTheme.getById(actYw.getGroup().getTheme()).getKey();
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
		workFlow.gcontestEdit(proModel, request,model);

		if(key!=null){
			return ProSval.path.vms(ProEmskey.PROMODEL.k()) + ""+key+"provinceGcontestEdit";
		}else{
			return ProSval.path.vms(ProEmskey.PROMODEL.k()) + "provinceGcontestEdit";
		}

	}

	private ActYw getActYw(ProModel proModel) {
		String cacheKey = ActSval.ck.cks(ActSval.ActEmskey.ACTYW, TenantConfig.getCacheTenant()) + proModel.getActYwId();
		ActYw actYw = null;
		if (JedisUtils.hasKey(cacheKey)) {
			actYw = (ActYw) JedisUtils.getObject(cacheKey);
		}
		if (actYw == null) {
			actYw = actYwService.get(proModel.getActYwId());
		}
		return actYw;
	}

	/**
	 * 报告 申请材料 logo
	 * @param proModel
	 * @param model
	 */
	public void setExtraInfo(ProModel proModel, Model model) {
		//报告（中期、结项等）
		ProReport report = new ProReport();
		report.setProModelId(proModel.getId());
		List<ProReport> reports = proReportService.findList(report);
		List<String> ids = new ArrayList<>();
		for (ProReport proReport : reports) {
			if (StringUtils.isBlank(proReport.getGnodeId())){
				continue;
			}
			SysAttachment sa=new SysAttachment();
			sa.setUid(proModel.getId());
			sa.setType(FileTypeEnum.S11);
			sa.setGnodeId(proReport.getGnodeId());//每个节点的附件
			ActYwGnode actYwGnode=actYwGnodeService.get(proReport.getGnodeId());
			if (actYwGnode != null){
				proReport.setGnodeName(actYwGnode.getName());
			}
			/*if(proReport.getGnodeId()!=null){
				ActYwGnode actYwGnode=actYwGnodeService.get(proReport.getGnodeId());
				proReport.setGnodeName(actYwGnode.getName());
			}*/
			List<SysAttachment> atts = sysAttachmentService.getFiles(sa);
			if (atts != null && atts.size() > 0){
				atts.forEach(item -> ids.add(item.getId()));
			}
			proReport.setFiles(atts);
		}
		model.addAttribute("reports", reports);

		SysAttachment sa=new SysAttachment();
		sa.setUid(proModel.getId());
		sa.setType(FileTypeEnum.S11);
		List<SysAttachment> fileList =  sysAttachmentService.getFiles(sa);
		if (!fileList.isEmpty()) {
			if (ids.size() > 0){
				fileList = fileList.stream().filter(item->!ids.contains(item.getId())).collect(Collectors.toList());
			}
			List<SysAttachment> applyFiles = new ArrayList<>();
			for (SysAttachment sysAttachment : fileList) {
				if(sysAttachment.getFileStep() == null){
					continue;
				}
				if (FileStepEnum.S1101.getValue().equals(sysAttachment.getFileStep().getValue())) {
					model.addAttribute("logo", sysAttachment);//logo
				}else if(S1102.getValue().equals(sysAttachment.getFileStep().getValue())){
					applyFiles.add(sysAttachment);//申报附件
				}
			}
			model.addAttribute("applyFiles", applyFiles);//申报附件
		}
		//审核记录
		List<ActYwAuditInfo> actYwAuditInfos = this.getActYwAuditInfo(proModel.getId());
		if (!actYwAuditInfos.isEmpty()) {
			model.addAttribute("actYwAuditInfos", actYwAuditInfos);
		}
	}

    public void setExtraInfo(String proId, Model model) {
    		//报告（中期、结项等）
    		ProReport report = new ProReport();
    		report.setProModelId(proId);
    		List<ProReport> reports = proReportService.findList(report);
    		for (ProReport proReport : reports) {
    			SysAttachment sa=new SysAttachment();
    			sa.setUid(proId);
    			sa.setType(FileTypeEnum.S11);
    			sa.setGnodeId(proReport.getGnodeId());//每个节点的附件
    			if(proReport.getGnodeId()!=null){
    				ActYwGnode actYwGnode=actYwGnodeService.get(proReport.getGnodeId());
    				proReport.setGnodeName(actYwGnode.getName());
    			}

    			proReport.setFiles(sysAttachmentService.getFiles(sa));
    		}
    		model.addAttribute("reports", reports);

    		SysAttachment sa=new SysAttachment();
    		sa.setUid(proId);
    		sa.setType(FileTypeEnum.S11);
    		List<SysAttachment> fileList =  sysAttachmentService.getFiles(sa);
    		if (!fileList.isEmpty()) {
    			List<SysAttachment> applyFiles = new ArrayList<>();
    			for (SysAttachment sysAttachment : fileList) {
    				if(sysAttachment.getFileStep() == null){
    					continue;
    				}
    				if (FileStepEnum.S1101.getValue().equals(sysAttachment.getFileStep().getValue())) {
    					model.addAttribute("logo", sysAttachment);//logo
    				}else if(S1102.getValue().equals(sysAttachment.getFileStep().getValue())){
    					applyFiles.add(sysAttachment);//申报附件
    				}
    			}
    			model.addAttribute("applyFiles", applyFiles);//申报附件
    		}
    		//审核记录
    		List<ActYwAuditInfo> actYwAuditInfos = this.getActYwAuditInfo(proId);
    		if (actYwAuditInfos!=null) {
    			model.addAttribute("actYwAuditInfos", actYwAuditInfos);
    		}
    	}


	public void setProvinceExtraInfo(String proId, Model model,String modelId) {
		//报告（中期、结项等）
		ProReport report = new ProReport();
		report.setProModelId(modelId);
		List<ProReport> reports = proReportService.findList(report);
		for (ProReport proReport : reports) {
			SysAttachment sa=new SysAttachment();
			sa.setUid(modelId);
			sa.setType(FileTypeEnum.S11);
			sa.setGnodeId(proReport.getGnodeId());//每个节点的附件
			if(proReport.getGnodeId()!=null){
				ActYwGnode actYwGnode=actYwGnodeService.get(proReport.getGnodeId());
				proReport.setGnodeName(actYwGnode.getName());
			}

			proReport.setFiles(sysAttachmentService.getFiles(sa));
		}
		model.addAttribute("reports", reports);

		SysAttachment sa=new SysAttachment();
		sa.setUid(modelId);
		sa.setType(FileTypeEnum.S11);
		List<SysAttachment> fileList =  sysAttachmentService.getFiles(sa);
		if (!fileList.isEmpty()) {
			List<SysAttachment> applyFiles = new ArrayList<>();
			for (SysAttachment sysAttachment : fileList) {
				if(sysAttachment.getFileStep() == null){
					continue;
				}
				if (FileStepEnum.S1101.getValue().equals(sysAttachment.getFileStep().getValue())) {
					model.addAttribute("logo", sysAttachment);//logo
				}else if(S1102.getValue().equals(sysAttachment.getFileStep().getValue())){
					applyFiles.add(sysAttachment);//申报附件
				}
			}
			model.addAttribute("applyFiles", applyFiles);//申报附件
		}
		//审核记录
		List<ActYwAuditInfo> actYwAuditInfos = this.getProvinceActYwAuditInfo(proId);
		if (actYwAuditInfos!=null) {
			model.addAttribute("actYwAuditInfos", actYwAuditInfos);
		}
	}

	@RequestMapping(value="getActYwAuditList/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getActYwAuditList(@PathVariable String id){
	    try {
            return  ApiResult.success(this.getActYwAuditInfo(id));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

	@RequestMapping(value="finishProject", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult finishProject(String proModelId){
		try {
			proModelService.deleteProcessInstanceById(proModelId);
			return  ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


    @RequestMapping(value="getActYwFiles/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getActYwFiles(@PathVariable String id){
        try {
            SysAttachment sa=new SysAttachment();
            sa.setUid(id);
            sa.setType(FileTypeEnum.S11);
            List<SysAttachment> fileList =  sysAttachmentService.getFiles(sa);
            List<SysAttachment> applyFiles = new ArrayList<>();
            if (!fileList.isEmpty()) {
                for (SysAttachment sysAttachment : fileList) {
                    if(sysAttachment.getFileStep() == null){
                        continue;
                    }
                    if (S1102.getValue().equals(sysAttachment.getFileStep().getValue())) {
                        applyFiles.add(sysAttachment);//申报附件
                    }
                }
            }
            return ApiResult.success(applyFiles);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

//    /**
//     * 后台查看页面特殊需求参数及页面跳转.
//     */
    private String dealPageViewA(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
        FormTheme formTheme = actYw.getFtheme();
        if(formTheme != null){
            FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
            if ((FormTheme.F_MR).equals(formTheme)) {
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            }else if ((FormTheme.F_MD).equals(formTheme)&& FlowProjectType.PMT_XM.equals(actYw.getFptype())) {
                //参数实现已经移动至实现类FppMd
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService, proModelMdService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            }else if ((FormTheme.F_MD).equals(formTheme)  && FlowProjectType.PMT_DASAI.equals(actYw.getFptype())) {
				//参数实现已经移动至实现类FppMd
				fpage.getParam().init(model, request, response, new Object[]{});
				fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService});
				return FormPage.getAbsUrl(actYw, fpageType, null);
			}else if ((FormTheme.F_COM).equals(formTheme)) {
                //参数实现已经移动至实现类FppCom
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            }else{
                logger.error("当前流程主题未定义！");
            }
        }else{
            logger.error("流程主题不存在！");
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

	/**
	 * 获取审核记录.
	 *
	 * @param proModelId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "auditInfo/{proModelId}")
	public List<ActYwAuditInfo> getActYwAuditInfo(@PathVariable String proModelId){
		return proModelService.getActYwAuditInfo(proModelId, true);
	}


	public List<ActYwAuditInfo> getProvinceActYwAuditInfo(@PathVariable String proModelId){
		return proModelService.getProvinceActYwAuditInfo(proModelId, true);
	}

	public static void  showFrontMessage(ProProject proProject ,Model model) {
		List<Dict> finalStatusMap=new ArrayList<Dict>();
		if (proProject.getFinalStatus()!=null) {
			String finalStatus=proProject.getFinalStatus();
			if (finalStatus!=null) {
				String[] finalStatuss=finalStatus.split(",");
				if (finalStatuss.length>0) {
					for(int i=0;i<finalStatuss.length;i++) {
						Dict dict=new Dict();
						dict.setValue(finalStatuss[i]);
						if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(finalStatuss[i],"competition_college_prise",""));
						}else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(finalStatuss[i],"project_result",""));
						}
						finalStatusMap.add(dict);
					}
				}
				model.addAttribute("finalStatusMap",finalStatusMap);
			}
		}
		//前台项目类型
		List<Dict> proTypeMap=new ArrayList<Dict>();
		if (proProject.getType()!=null) {
			String proType=proProject.getType();
			if (proType!=null) {
				String[] proTypes=proType.split(",");
				if (proTypes.length>0) {
					for(int i=0;i<proTypes.length;i++) {
						Dict dict=new Dict();

						dict.setValue(proTypes[i]);
						if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(proTypes[i],"competition_type",""));
						}else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(proTypes[i],"project_style",""));
						}
						//proCategoryMap.add(map);
						proTypeMap.add(dict);

					}
				}
				model.addAttribute("proTypeMap",proTypeMap);
			}
		}
		//前台项目类别
		/*List<Map<Dict>> proCategoryMap=new ArrayList<Map<String, String>>();*/
		List<Dict> proCategoryMap=new ArrayList<Dict>();
		if (proProject.getProCategory()!=null) {
			String proCategory=proProject.getProCategory();
			if (proCategory!=null) {
				String[] proCategorys=proCategory.split(",");
				if (proCategorys.length>0) {
					for(int i=0;i<proCategorys.length;i++) {
						Map<String, String> map=new HashMap<String, String>();
						Dict dict=new Dict();
						map.put("value",proCategorys[i]);
						dict.setValue(proCategorys[i]);
						if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
							map.put("label",DictUtils.getDictLabel(proCategorys[i],"competition_net_type",""));
							dict.setLabel(DictUtils.getDictLabel(proCategorys[i],"competition_net_type",""));
						}else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
							map.put("label",DictUtils.getDictLabel(proCategorys[i],FlowPcategoryType.PCT_XM.getKey(),""));
							dict.setLabel(DictUtils.getDictLabel(proCategorys[i],FlowPcategoryType.PCT_XM.getKey(),""));
						}
						//proCategoryMap.add(map);
						proCategoryMap.add(dict);
					}
				}
				model.addAttribute("proCategoryMap",proCategoryMap);
			}
		}
		//前台项目类别
		List<Dict> prolevelMap=new ArrayList<Dict>();
		if (proProject.getLevel()!=null) {
			String proLevel=proProject.getLevel();
			if (proLevel!=null) {
				String[] proLevels=proLevel.split(",");
				if (proLevels.length>0) {
					for(int i=0;i<proLevels.length;i++) {
						Dict dict=new Dict();
						dict.setValue(proLevels[i]);
						dict.setLabel(DictUtils.getDictLabel(proLevels[i],"gcontest_level",""));
						prolevelMap.add(dict);
					}
				}
				model.addAttribute("prolevelMap",prolevelMap);
			}
		}
	}

	@RequestMapping(value = "promodelDelete")
	public String promodelDelete(ProModel proModel, RedirectAttributes redirectAttributes) {
		String actywId=proModel.getActYwId();
		proModelService.promodelDelete(proModel);
		addMessage(redirectAttributes, "删除proModel成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/form/queryMenuList/?actywId="+actywId;
	}

	@RequestMapping(value = "ajaxPromodelDelete")
	@ResponseBody
	public JSONObject ajaxPromodelDelete(String ids,HttpServletRequest request,  RedirectAttributes redirectAttributes) {
		JSONObject  js= new JSONObject();
		if (ids != null){
			String[] idList=ids.split(",");
			try {
				if (idList.length > 0){
					List<ProModel> list = new ArrayList<>();
					for(int i=0;i<idList.length;i++){
						ProModel proModel=get(idList[i]);
						if (proModel != null && StringUtils.equals("1",proModel.getIsSend())){
							js.put("ret", "0");
							js.put("msg", "删除失败,"+proModel.getpName()+"已推荐省级");
							return js;
						}
						list.add(proModel);
					}
//					proModelService.promodelDeleteList(idList);
					proModelService.deleteProModels(list);
					js.put("ret", "1");
				}

			}catch (Exception e){
				js.put("ret", "0");
				js.put("msg", "删除失败,出现了未知的错误，请重试或者联系管理员");
			}
		}

		return js;
	}

    //保存项目变更
    @RequestMapping(value = "saveProjectEdit")
    @RequiresPermissions("promodel:promodel:modify")
    @ResponseBody
    public JSONObject saveProjectEdit(ProModel proModel, HttpServletRequest request) {
		JSONObject  js= new JSONObject();
		try {
			ActYw actYw = actYwService.get(proModel.getActYwId());
			String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
			IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
			js=workFlow.saveProjectEdit(proModel,request);
            return js;
        } catch (Exception e) {
            logger.error(e.getMessage());

            js.put("ret", "0");
            js.put("msg", e.getMessage());
            return js;
        }
    }

    @RequiresPermissions("promodel:promodel:modify")
    @RequestMapping(value = "saveProModelEdit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveProModelEdit(@RequestBody ProModel proModel){
        try {
            ActYw actYw = actYwService.get(proModel.getActYwId());
            String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
            IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
            JSONObject js=proModelService.saveProModelEdit(proModel);
            if(js.getString("ret").equals("0")){
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+js.getString("msg"));
            }
            return ApiResult.success(proModel);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":保存失败,出现了未知的错误，请重试或者联系管理员");
        }
    }

    @RequiresPermissions("promodel:promodel:modify")
    @RequestMapping(value = "saveHsxmProModelEdit", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveProModelEdit(@RequestBody ProModelHsxm proModelHsxm){
        try {
            JSONObject js =proModelHsxmService.saveHsxmProjectEdit(proModelHsxm);
            if(js.getString("ret").equals("0")){
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+js.getString("msg"));
            }
            return ApiResult.success(proModelHsxm);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":保存失败,出现了未知的错误，请重试或者联系管理员");
        }
    }

    //保存大赛变更
    @RequestMapping(value = "saveGcontestEdit")
    @RequiresPermissions("promodel:promodel:modify")
    @ResponseBody
    public JSONObject saveGcontestEdit(ProModel proModel, HttpServletRequest request) {
        try {
            return proModelService.saveGcontestEdit(proModel,request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            JSONObject js = new JSONObject();
            js.put("ret", "0");
            js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
            return js;
        }
    }

	/**
	 * 校验编号
	 * @param num
	 * @param id
	 * @return
	 */
    @RequestMapping(value = "checkNumber")
    @ResponseBody
    public String checkNumber(String num, String id) {
        Integer i = proModelService.getByNumberAndId(num, id);
        if (i == null || i == 0) {
            return "true";
        }
        return "false";
    }

	/**
	 * 项目变更按流程区别编号
	 * @param num
	 * @param id
	 * @param actYwId
	 * @return
	 */
	@RequestMapping(value = "checkNumberByActYwId")
	@ResponseBody
	public String checkNumberByActYwId(String num, String id,String actYwId) {
		Integer i = proModelService.checkNumberByActYwId(num, id,actYwId);
		if (i == null || i == 0) {
			return "true";
		}
		return "false";
	}


	// ajax 批量修改級別
	//ids 审核promodeid的结合用“，”分隔
	//projectLevel 级别所选的值
	@RequestMapping(value = "ajax/batchAuditLevel")
	@ResponseBody
	public JSONObject batchAuditLevel(String  ids, String finalStatus) {
		JSONObject js=new JSONObject();
		String[] idList=ids.split(",");
		proModelService.batchChangeLevel(idList,finalStatus);
		js.put("ret","1");
		return js;
	}

	// ajax 批量审核
	@RequestMapping(value = "ajax/batchAudit")
	@ResponseBody
	//ids 审核promodeid的结合用“，”分隔
	//grade 审核的结果 页面传值
	//gnodeId 审核的节点
	public JSONObject batchAudit(String  ids, String grade, String gnodeId) {
		JSONObject js=new JSONObject();
		String[] idList=ids.split(",");
		boolean ispass=proModelService.batchAudit(idList,grade,gnodeId);
		if(ispass){
			js.put("ret","1");
		}else{
			js.put("ret","0");
		}

		return js;
	}

	/**
	 * 省批量审核
	 * @param ids
	 * @param grade
	 * @param gnodeId
	 * @return
	 */
	@RequestMapping(value = "ajax/provinceBatchAudit")
	@ResponseBody
	public JSONObject provinceBatchAudit(String  ids, String grade, String gnodeId) {
		JSONObject js=new JSONObject();
		String[] idList=ids.split(",");
		boolean ispass=provinceProModelService.provinceBatchAudit(idList,grade,gnodeId);
		if(ispass){
			js.put("ret","1");
		}else{
			js.put("ret","0");
		}

		return js;
	}

	// ajax 批量审核评分
	@RequestMapping(value = "ajax/batchScoreAudit")
	@ResponseBody
	//ids 审核promodeid的结合用“，”分隔
	//score 审核的结果 页面传值
	//gnodeId 审核的节点
	public JSONObject batchScoreAudit(String  ids, String score, String gnodeId) {
		JSONObject js=new JSONObject();
		String[] idList=ids.split(",");
		proModelService.batchScoreAudit(idList,score,gnodeId);
		js.put("ret","1");
		return js;
	}

	@RequestMapping(value="getActYwGnode/{id}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult getActYwGnode(@PathVariable String id){
	    return ApiResult.success(ProcessDefUtils.getActYwGnode(id));
    }


	/**
	 * 获取推送省流程ID
	 * @param actywId
	 * @return
	 */
	@RequestMapping(value="getProvinceActYwId", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getProvinceActYwId(String actywId){
		if (StringUtil.isEmpty(actywId)){
			return ApiResult.failed(0,"学校流程参数为空");
		}
		String provinceActYwId = actYwPscrelService.findProvActYwId(actywId);
		if (StringUtil.isEmpty(provinceActYwId)){
			return ApiResult.failed(0,"省流程不存在");
		}
		return ApiResult.success(provinceActYwId);
	}


	/**
	 * 推送项目到省级平台
	 * @param actywId 省流程
	 * @return ApiResult
	 */
/*    @RequestMapping(value="sendProToProvince", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult sendProToProvince(String promodelId,String actywId)		{
		if (StringUtils.isBlank(promodelId) || StringUtils.isBlank(actywId)){
			return ApiResult.failed(0,"推荐参数不能为空");
		}
		List<String> proModelList = Arrays.asList(promodelId.split(","));
		for (String id:proModelList){
			ProModel proModel = get(id);
			if (proModel.getIsSend()!= null && proModel.getIsSend().equals("1")){
				return ApiResult.failed(0,proModel.getPName()+"已经推荐过了");
			}
		}

		ActYw provinceActYw = Optional.ofNullable(actYwService.getById(actywId)).orElse(new ActYw());
		if (provinceActYw.getIsDeploy() == null || !provinceActYw.getIsDeploy()){
			return ApiResult.failed(0,"省级流程不存在，不能推送");
		}
		Date now = new Date();
		ActYwYear ayy = Optional.ofNullable(commonService.getApplyActYwYear(actywId)).orElse(new ActYwYear());
		if (ayy.getYear() == null){
			return ApiResult.failed(0,"推荐失败,当前时间不可推送");
		}
		Date start = ayy.getNodeStartDate();
		Date end = ayy.getNodeEndDate();
		if (start != null && start.after(now)) {
			return ApiResult.failed(0,"推荐失败,推荐日期未开放");
		}
		if (end != null && end.before(now)) {
			return ApiResult.failed(0,"推荐失败,推荐日期已截止");
		}

		*//*ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
		actYwAuditInfo.setPromodelId(promodelId);
		List<ActYwAuditInfo> actYwAuditInfoList = Optional.ofNullable(actYwAuditInfoService.findList(actYwAuditInfo)).orElse(new ArrayList<>());
		if (actYwAuditInfoList.size() ==0){
			return ApiResult.failed(0,"推荐失败,请先立项审核");
		}
		actYwAuditInfoList.sort(Comparator.comparing(ActYwAuditInfo::getCreateDate));
        String key="";
        String firstGnodeId = actYwAuditInfoList.get(0).getGnodeId();
		List<ActYwStatus> actYwStatusList = Optional.ofNullable(scrProModelService.getActYwStatusByGnodeId(firstGnodeId))
				.orElse(new ArrayList<>());
		if (actYwStatusList.size() == 0){
			return ApiResult.failed(0,"推荐失败,审核节点网关不存在");
		}
		ActYwStatus actYwStatus = actYwStatusList.stream().filter(item->StringUtils.equals("1",item.getSign()))
				.findAny().orElse(new ActYwStatus());
		if (actYwStatus.getId() == null){
			return ApiResult.failed(0,"推荐失败,流程网关无可推荐判定节点");
		}
		key = actYwStatus.getStatus();
				Boolean flag = false;
       for (int i = 0; i<actYwAuditInfoList.size(); i++){
			if (StringUtils.equals(firstGnodeId,actYwAuditInfoList.get(i).getGnodeId()) && StringUtils.equals(key,actYwAuditInfoList.get(i).getGrade())){
				flag = true;
				break;
			}
	   }
       if (!flag){
		   return ApiResult.failed(0,"推荐失败,该项目立项审核没通过");
	   }*//*
        try{
			proModelList.forEach(item->{
				proModelService.sendProToProvince(item,TenantConfig.getCacheTenant(),actywId,provinceActYw,ayy.getYear());
				proModelService.updateIsSendById(item, CoreSval.Const.SEND_YES);
			});
            return ApiResult.success();
        }catch (ActYwRuntimeException e){

            return ApiResult.failed(e.hashCode());
        }

    }*/

	/**
	 * 推送项目到省级
	 * @param proModelList proModelList
	 * @param actywId actywId
	 * @return
	 */
	@RequestMapping(value="sendProToProvince/{actywId}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult sendProToProvince(@RequestBody List<ProModel> proModelList,@PathVariable String actywId)		{
		if (proModelList == null || proModelList.size() == 0 || StringUtils.isBlank(actywId)){
			return ApiResult.failed(0,"推荐参数不能为空");
		}
		List<ProModel> list = new ArrayList<>();
		list = proModelList.stream().filter(item->item.getIsSend().equals(CoreSval.Const.SEND_NO)).collect(Collectors.toList());
		if (list.size() == 0){
			return ApiResult.failed(0,"您选择的项目已推送,请勿重复推送");
		}
		ActYw provinceActYw = Optional.ofNullable(actYwService.getById(actywId)).orElse(new ActYw());
		if (provinceActYw.getIsDeploy() == null || !provinceActYw.getIsDeploy()){
			return ApiResult.failed(0,"省级流程不存在，不能推送");
		}
		Date now = new Date();
		ActYwYear ayy = Optional.ofNullable(commonService.getApplyActYwYear(actywId)).orElse(new ActYwYear());
		if (ayy.getYear() == null){
			return ApiResult.failed(0,"推荐失败,当前时间不可推送");
		}
		Date start = ayy.getNodeStartDate();
		Date end = ayy.getNodeEndDate();
		if (start != null && start.after(now)) {
			return ApiResult.failed(0,"推荐失败,推荐日期未开放");
		}
		if (end != null && end.before(now)) {
			return ApiResult.failed(0,"推荐失败,推荐日期已截止");
		}

		try{
			list.forEach(item->{
				proModelService.sendProToProvince(item.getId(),TenantConfig.getCacheTenant(),actywId,provinceActYw,ayy.getYear(),item.getSubTime());
				proModelService.updateIsSendById(item.getId(), CoreSval.Const.SEND_YES);
			});
			return ApiResult.success();
		}catch (ActYwRuntimeException e){

			return ApiResult.failed(e.hashCode());
		}

	}

    //推送项目到省级平台
    @RequestMapping(value="sendProToProvinceTest", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult sendProToProvinceTest(String promodelId,String actywId){
//		//省平台actywId
        if(StringUtil.isEmpty(actywId)){
            actywId="473f5a98f16646259161d26d062a925e";
        }
        try{
//            proModelService.sendProToProvince(promodelId,TenantConfig.getCacheTenant(),actywId);
            proModelService.updateIsSendById(promodelId, CoreSval.Const.SEND_YES);

            return ApiResult.success();
        }catch (ActYwRuntimeException e){


            return ApiResult.failed(e.hashCode());
        }

    }


    //保存省平台与校平台关联关系
    @RequestMapping(value="saveProvinceSchoolRelation", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult saveProvinceSchoolRelation(String procActywId,String schoolActywId){
        return ApiResult.success();
    }


	/**
	 *
	 * @param team
	 * @param model
	 */
	public void getTeamMembers(Team team,Model model){
		CompletableFuture<List<TeamStudentVo>> studentFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfStudentByTeamId(team.getId()), ThreadUtils.newFixedThreadPool());
		CompletableFuture<List<TeamTeacherVo>> teacherFuture = CompletableFuture.supplyAsync(() -> projectDeclareService.findTeamOfTeacherByTeamId(team.getId()),ThreadUtils.newFixedThreadPool());
		CompletableFuture.allOf(studentFuture,teacherFuture).join();
		List<TeamStudentVo> teamStudentVos = null;
		List<TeamTeacherVo> teamTeacherVos = null;
		try {
			teamStudentVos = studentFuture.get();
			teamTeacherVos = teacherFuture.get();
		} catch (InterruptedException e) {
			logger.info("查询线程中断,queryRecordIds");
			e.printStackTrace();
		} catch (ExecutionException e) {
			logger.info("执行异常,queryRecordIds");
			e.printStackTrace();
		}finally {
			ThreadUtils.shutdown();
		}

		model.addAttribute("teamStu", teamStudentVos);
		model.addAttribute("teamTea",teamTeacherVos);
	}

	public Team getById(String teamId){
		return teamService.getById(teamId);
	}


	/**
	 * 编辑项目
	 * @param proModel
	 * @return
	 */
	@RequestMapping(value="updateByProId", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult updateByProId(@RequestBody ProModel proModel){
		if (proModel == null){
			return ApiResult.failed(0,"参数为空");
		}
		try{
			List<String> idList = proModelService.getIdsByRanking(proModel.getRanking(),proModel.getId());
			if (idList.size() > 0){
				return ApiResult.failed(0,"大赛名次不能相同");
			}
			proModelService.updateByProId(proModel.getId(),proModel.getRanking());
			return ApiResult.success(1,"编辑成功");
		}catch (Exception e){
			logger.error(e.toString());
		}
		return ApiResult.failed(0,"编辑失败");
	}


}