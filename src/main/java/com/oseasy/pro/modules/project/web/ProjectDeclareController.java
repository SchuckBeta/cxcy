package com.oseasy.pro.modules.project.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.authorize.service.AuthorizeService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectAnnounceService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.project.vo.ProjectDeclareVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.utils.ProProcessDefUtils;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.service.SysStudentExpansionService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 项目申报Controller
 *
 * @author 9527
 * @version 2017-03-11
 */
@Controller
public class ProjectDeclareController extends BaseController {
    public final static Logger logger = Logger.getLogger(ProjectDeclareController.class);
    @Autowired
    ProjectAnnounceService projectAnnounceService;
    @Autowired
    SysStudentExpansionService sysStudentExpansionService;
    @Autowired
    SysAttachmentService sysAttachmentService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private ProjectPlanService projectPlanService;
//    @Autowired
//    ScoAllotRatioService scoAllotRatioService;
    @Autowired
    TeamUserRelationService teamUserRelationService;
    @Autowired
    TeamUserHistoryService teamUserHistoryService;
    @Autowired
    private CommonService commonService;
    @Autowired
    AuthorizeService authorizeService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ProModelService proModelService;

    @ModelAttribute
    public ProjectDeclare get(@RequestParam(required = false) String id) {
        ProjectDeclare entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = projectDeclareService.get(id);
        }
        if (entity == null) {
            entity = new ProjectDeclare();
        }
        return entity;
    }

    @RequestMapping(value = {"${frontPath}/project/projectDeclare/list"})
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
//        User user = UserUtils.getUser();
//        ProjectDeclareListVo vo = new ProjectDeclareListVo();
//        vo.setUserid(user.getId());
//        Page<ProjectDeclareListVo> page = projectDeclareService
//                .getMyProjectListPlus(new Page<ProjectDeclareListVo>(request, response), vo);
//        model.addAttribute("page", page);
//        model.addAttribute("user", user);

		/*
         * for(ProjectDeclareListVo map:page.getList()) { String projectId =
		 * map.getId(); ProjectDeclare pro =
		 * projectDeclareService.getScoreConfigure(projectId); //根据
		 * type（学分类型)、item（学分项）、category（课程、项目、大赛、技能大类）、subdivision（课程、项目、大赛小类）、
		 * number(人数)查询后台配比 ScoRatioVo scoRatioVo = new ScoRatioVo(); if
		 * (StringUtil.equals(pro.getType(),"1")||StringUtil.equals(pro.getType(
		 * ),"2")) { //创新训练、创业训练 scoRatioVo.setType("0000000123");
		 * //设置查询的学分类型（创新学分） } if (StringUtil.equals(pro.getType(),"3")) {
		 * //创业实践 scoRatioVo.setType("0000000124"); //设置查询的学分类型（创业学分） }
		 * scoRatioVo.setItem("0000000128"); //双创项目 scoRatioVo.setCategory("1");
		 * //大学生创新创业训练项目 scoRatioVo.setSubdivision(pro.getType());
		 * scoRatioVo.setNumber(pro.getSnumber()); ScoRatioVo ratioResult =
		 * scoAllotRatioService.findRatio(scoRatioVo); if (ratioResult!=null) {
		 * map.setHasConfig("true"); map.setRatio(ratioResult.getRatio());
		 * }else{ map.setHasConfig("false"); }
		 *
		 * }
		 */

        return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectDeclareList";
    }

    @RequestMapping(value="${frontPath}/project/getProjectDeclareList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getProjectDeclareList(HttpServletRequest request, HttpServletResponse response){
        try {
            User user = UserUtils.getUser();
            ProjectDeclareListVo vo = new ProjectDeclareListVo();
            vo.setUserid(user.getId());
            Page<ProjectDeclareListVo> page = projectDeclareService.getMyProjectListPlus(new Page<ProjectDeclareListVo>(request, response), vo);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="${frontPath}/project/getActByPromodelId", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getActByPromodelId(String proModelId){
        try {
            return ApiResult.success(ProProcessDefUtils.getActByPromodelId(proModelId));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    // 学分配比页面
    @RequestMapping(value = "${frontPath}/project/projectDeclare/scoreConfig")
    public String scoreConfig(String projectId, Model model) {
        // 查找后台配比规则 后台设置的配比
        ProjectDeclare pro = projectDeclareService.getScoreConfigure(projectId);
        if(pro != null){
            // 根据type（学分类型)、item（学分项）、category（课程、项目、大赛、技能大类）、subdivision（课程、项目、大赛小类）、number(人数)查询后台配比
//            ScoRatioVo scoRatioVo = new ScoRatioVo();
//            if (StringUtil.equals(pro.getType(), "1") || StringUtil.equals(pro.getType(), "2")) { // 创新训练、创业训练
//                scoRatioVo.setType("0000000123"); // 设置查询的学分类型（创新学分）
//            }
//            if (StringUtil.equals(pro.getType(), "3")) { // 创业实践
//                scoRatioVo.setType("0000000124"); // 设置查询的学分类型（创业学分）
//            }
//            scoRatioVo.setItem("0000000128"); // 双创项目
//            scoRatioVo.setCategory("1"); // 大学生创新创业训练项目
//            scoRatioVo.setSubdivision(pro.getType());
//            scoRatioVo.setNumber(pro.getSnumber());
//            ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
//            model.addAttribute("ratio", ratioResult.getRatio());
            // 查找组成员的信息
            TeamUserRelation teamUserRelation = new TeamUserRelation();
            teamUserRelation.setTeamId(pro.getTeamId());
            // List<TeamUserRelation> studentList= teamUserRelationService.getStudents(teamUserRelation);
            List<Map<String, String>> studentList = projectDeclareService.findTeamStudentFromTUH(pro.getTeamId(), pro.getId());
            model.addAttribute("studentList", studentList);
            // 判断是否能保存配比信息（如果是提交立项报告前的阶段能保存，提交立项报告后的阶段只能查看）
            boolean isSubmit = false;
            if (StringUtil.equals(pro.getStatus(), "7") || StringUtil.equals(pro.getStatus(), "8")
                    || StringUtil.equals(pro.getStatus(), "9")) {
                isSubmit = false;
            } else {
                isSubmit = true;
            }
            model.addAttribute("isSubmit", isSubmit);
        }
        return ProSval.path.vms(ProEmskey.PROJECT.k()) + "scoreConfig";
    }

    // ajax 请求，页面传入type(项目类型），snumber（团队学生人数），返回后台的学分配比比例
    @RequestMapping("${frontPath}/project/projectDeclare/findRatio")
    @ResponseBody
    public String frontFindRatio(String type, int snumber) {
        return findRatioDis(type, snumber);
    }

    // ajax 请求，页面传入type(项目类型），snumber（团队学生人数），返回后台的学分配比比例
    @RequestMapping("${adminPath}/project/projectDeclare/findRatio")
    @ResponseBody
    public String backFindRatio(String type, int snumber) {
        return findRatioDis(type, snumber);
    }

    private String findRatioDis(String type, int snumber) {
        Boolean bl = authorizeService.checkMenuByNum(5);
        // 是否授权
//        if (bl) {
//            ScoRatioVo scoRatioVo = new ScoRatioVo();
//            if (StringUtil.equals(type, "1") || StringUtil.equals(type, "2")) { // 创新训练、创业训练
//                scoRatioVo.setType("0000000123"); // 设置查询的学分类型（创新学分）
//            }
//            if (StringUtil.equals(type, "3")) { // 创业实践
//                scoRatioVo.setType("0000000124"); // 设置查询的学分类型（创业学分）
//            }
//            scoRatioVo.setItem("0000000128"); // 双创项目
//            scoRatioVo.setCategory("1"); // 大学生创新创业训练项目
//            scoRatioVo.setSubdivision(type);
//            scoRatioVo.setNumber(snumber);
//            ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
//            if (ratioResult != null) {
//                return ratioResult.getRatio();
//            } else {
//                return "";
//            }
//        } else {
//            return "";
//        }
		return "";
    }

    // 保存学分配比
    @RequestMapping(value = "${frontPath}/project/projectDeclare/saveScoreConfig")
    @ResponseBody
    public boolean saveScoreConfig(ProjectDeclare projectDeclare) {
        for (TeamUserHistory tur : projectDeclare.getTeamUserHistoryList()) {
            teamUserHistoryService.updateWeight(tur);
        }
        return true;
    }

    // 判断项目是否能提交结项报告 （如果后台配置了规则，但该项目没有给成员配比，则不能提交结项报告）
    @RequestMapping(value = "${frontPath}/project/projectDeclare/canSumitClose")
    @ResponseBody
    public boolean canSumitClose(String projectId) {
        // 校验学分是否授权
        boolean isAuthorize = CoreUtils.checkMenuByNum(5);
        // 已授权
        if (isAuthorize) {
            ProjectDeclare pro = projectDeclareService.getScoreConfigure(projectId);
            if(pro == null){
                return false;
            }
//            // 根据type（学分类型)、item（学分项）、category（课程、项目、大赛、技能大类）、subdivision（课程、项目、大赛小类）、number(人数)查询后台配比
//            ScoRatioVo scoRatioVo = new ScoRatioVo();
//            if (StringUtil.equals(pro.getType(), "1") || StringUtil.equals(pro.getType(), "2")) { // 创新训练、创业训练
//                scoRatioVo.setType("0000000123"); // 设置查询的学分类型（创新学分）
//            }
//            if (StringUtil.equals(pro.getType(), "3")) { // 创业实践
//                scoRatioVo.setType("0000000124"); // 设置查询的学分类型（创业学分）
//            }
//            scoRatioVo.setItem("0000000128"); // 双创项目
//            scoRatioVo.setCategory("1"); // 大学生创新创业训练项目
//            scoRatioVo.setSubdivision(pro.getType());
//            scoRatioVo.setNumber(pro.getSnumber());
//            ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
            boolean hasConfig = false; // 判断后台是否配置了规则
//            if (ratioResult != null) {
//                hasConfig = true;
//            } else {
//                hasConfig = false;
//            }

            // 判断该项目有没有给成员配比（如果team_user_relation表的weight_val的sum不为0则是配比了）
            // int weightTotal =
            // teamUserRelationService.getWeightTotalByTeamId(pro.getTeamId());
            int weightTotal = teamUserHistoryService.getWeightTotalByTeamId(pro.getTeamId(), pro.getId());
            if (hasConfig && weightTotal == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @RequestMapping(value = {"${frontPath}/project/projectDeclare/curProject"})
    public String curProject(ProjectDeclare projectDeclare, HttpServletRequest request, HttpServletResponse response,
                             Model model) {
        User user = UserUtils.getUser();
        model.addAttribute("user", user);
        List<Map<String, String>> list = projectDeclareService.getCurProProject();
        if (list != null && list.size() > 0) {
            model.addAttribute("pp", JSONArray.fromObject(list));
        } else {
            model.addAttribute("pp", new JSONArray());
        }
        return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectTimeIndex";
    }

    @RequestMapping(value = {"${frontPath}/project/projectDeclare/getTimeIndexSecondTabs"})
    @ResponseBody
    public JSONArray getTimeIndexSecondTabs(HttpServletRequest request) {
        String pptype = request.getParameter("pptype");
        String actywId = request.getParameter("actywId");
        User user = UserUtils.getUser();
        if (StringUtil.isNotEmpty(user.getId()) && StringUtil.isNotEmpty(pptype) && StringUtil.isNotEmpty(actywId)) {
            List<Map<String, String>> list = projectDeclareService.getTimeIndexSecondTabs(pptype, actywId, user.getId());
            if (list != null && list.size() > 0) {
                return JSONArray.fromObject(list);
            }
        }
        return new JSONArray();
    }

    @RequestMapping(value = "${frontPath}/project/projectDeclare/getTimeIndexData")
    @ResponseBody
    public JSONObject getTimeIndexData(String pptype, String actywId, String projectId, HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            return projectDeclareService.getTimeIndexData(pptype, actywId, projectId);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return new JSONObject();
    }

    @RequestMapping(value = "${frontPath}/project/projectDeclare/form")
    public String form(ProjectDeclare projectDeclare, Model model, HttpServletRequest request) {
        User user = UserUtils.getUser();
        if (StringUtil.isNotEmpty(projectDeclare.getId())) {
            if (StringUtil.isEmpty(projectDeclare.getLeader()) || StringUtil.isEmpty(user.getId())
                    || !user.getId().equals(projectDeclare.getLeader())) {
                return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/project/projectDeclare/viewForm?id="
                        + projectDeclare.getId();
            }
        }
        ProjectDeclareVo vo = new ProjectDeclareVo();
		/*
		 * if (projectDeclare.getId()==null) { Map<String,String> map=new
		 * HashMap<String,String>(); map.put("projectType", "1");
		 * map.put("file_step", FileTypeEnum.S200.getValue());
		 * map.put("type",FileSourceEnum.S1.getValue()); List<Map<String,
		 * String>> list=projectAnnounceService.findCurInfo(map); if
		 * (list==null||list.size()==0) { return CoreSval.REDIRECT+frontPath; }else{
		 * vo.setProjectAnnounce(list.get(0)); } }
		 */
        model.addAttribute("levelList", DictUtils.getDictList("project_degree"));
        model.addAttribute("resultTypeList", DictUtils.getDictList("project_result_type"));
        model.addAttribute("project_type", DictUtils.getDictList(FlowPcategoryType.PCT_XM.getKey()));
        model.addAttribute("project_extend", DictUtils.getDictList("project_extend"));
        model.addAttribute("sysdate", DateUtil.formatDate(
                (projectDeclare.getCreateDate() == null ? new Date() : projectDeclare.getCreateDate()), "yyyy-MM-dd"));
        model.addAttribute("project_source", DictUtils.getDictList("project_source"));
        User leader = (projectDeclare.getLeader() == null ? UserUtils.getUser()
                : UserUtils.get(projectDeclare.getLeader()));
        User creater = (projectDeclare.getCreateBy() == null ? UserUtils.getUser()
                : UserUtils.get(projectDeclare.getCreateBy().getId()));
        model.addAttribute("teams", projectDeclareService.findTeams(leader.getId(), projectDeclare.getTeamId()));
        model.addAttribute("user", user);
        model.addAttribute("creater", creater);
        model.addAttribute("leader", leader);
        model.addAttribute("studentExpansion", sysStudentExpansionService.getByUserId(leader.getId()));
        vo.setPlans(projectPlanService.findListByProjectId(projectDeclare.getId()));
        vo.setProjectDeclare(projectDeclare);
        vo.setTeamStudent(
                projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(), projectDeclare.getId()));
        vo.setTeamTeacher(
                projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(), projectDeclare.getId()));
        SysAttachment sa = new SysAttachment();
        sa.setUid(projectDeclare.getId());
        sa.setFileStep(FileStepEnum.S100);
        sa.setType(FileTypeEnum.S0);
        vo.setFileInfo(sysAttachmentService.getFiles(sa));
        if (StringUtil.isEmpty(projectDeclare.getActywId())) {
			projectDeclare.setActywId(request.getParameter("actywId"));
		}
        model.addAttribute("projectDeclareVo", vo);
        return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectDeclareForm";
    }

    @RequestMapping(value = "${frontPath}/project/projectDeclare/viewForm")
    public String viewForm(ProjectDeclare projectDeclare, Model model) {
        ProjectDeclareVo vo = new ProjectDeclareVo();
        model.addAttribute("levelList", DictUtils.getDictList("project_degree"));
        model.addAttribute("resultTypeList", DictUtils.getDictList("project_result_type"));
        model.addAttribute("project_type", DictUtils.getDictList(FlowPcategoryType.PCT_XM.getKey()));
        model.addAttribute("project_extend", DictUtils.getDictList("project_extend"));
        model.addAttribute("sysdate", DateUtil.formatDate(
                (projectDeclare.getCreateDate() == null ? new Date() : projectDeclare.getCreateDate()), "yyyy-MM-dd"));
        model.addAttribute("project_source", DictUtils.getDictList("project_source"));

        if(StringUtil.isNotEmpty(projectDeclare.getId())){
            projectDeclare = projectDeclareService.get(projectDeclare);
        }

        if(projectDeclare == null){
            logger.warn("当前项目不是大创项目类型,不支持该项目查看");
            addMessage(model, "当前项目不是大创项目类型,不支持该项目查看");
            return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectDeclareViewPro";
        }

        User user = UserUtils.getUser();
        User leader = UserUtils.get(projectDeclare.getLeader());
        User creater = null;
        if(projectDeclare.getCreateBy() != null){
            creater = UserUtils.get(projectDeclare.getCreateBy().getId());
        }
        List<Team> teams = null;
        SysStudentExpansion studentExpansion = null;
        if(leader != null){
            teams = projectDeclareService.findTeams(leader.getId(), projectDeclare.getTeamId());
            studentExpansion = sysStudentExpansionService.getByUserId(leader.getId());
        }
        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        model.addAttribute("creater", creater);
        model.addAttribute("leader", leader);
        model.addAttribute("studentExpansion", studentExpansion);
        vo.setPlans(projectPlanService.findListByProjectId(projectDeclare.getId()));
        vo.setProjectDeclare(projectDeclare);
        vo.setTeamStudent(
                projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(), projectDeclare.getId()));
        vo.setTeamTeacher(
                projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(), projectDeclare.getId()));
        SysAttachment sa = new SysAttachment();
        sa.setUid(projectDeclare.getId());
        sa.setFileStep(FileStepEnum.S100);
        sa.setType(FileTypeEnum.S0);
        vo.setFileInfo(sysAttachmentService.getFiles(sa));
        if (projectDeclare.getStatus() != null && !"0".equals(projectDeclare.getStatus())) {
            vo.setAuditInfo(projectDeclareService.getProjectAuditInfo(projectDeclare.getId()));
        }
        model.addAttribute("projectDeclareVo", vo);
        return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectDeclareFormView";
    }

    /**
     * 我的空间项目查看..
     * @param projectDeclare
     * @param model
     * @return String
     */
    @RequestMapping(value = "${frontPath}/project/projectDeclare/viewPro")
    public String viewPro(ProjectDeclare projectDeclare, Model model) {
        ProjectDeclareVo vo = new ProjectDeclareVo();
        model.addAttribute("levelList", DictUtils.getDictList("project_degree"));
        model.addAttribute("resultTypeList", DictUtils.getDictList("project_result_type"));
        model.addAttribute("project_type", DictUtils.getDictList(FlowPcategoryType.PCT_XM.getKey()));
        model.addAttribute("project_extend", DictUtils.getDictList("project_extend"));
        model.addAttribute("sysdate", DateUtil.formatDate(
                (projectDeclare.getCreateDate() == null ? new Date() : projectDeclare.getCreateDate()), "yyyy-MM-dd"));
        model.addAttribute("project_source", DictUtils.getDictList("project_source"));

        if(StringUtil.isNotEmpty(projectDeclare.getId())){
            projectDeclare = projectDeclareService.get(projectDeclare);
        }
        if(projectDeclare == null){
            logger.warn("当前项目不是大创项目类型,不支持该项目查看");
            addMessage(model, "当前项目不是大创项目类型,不支持该项目查看");
            return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectDeclareViewPro";
        }

        User user = UserUtils.getUser();
        User leader = UserUtils.get(projectDeclare.getLeader());
        User creater = null;
        if(projectDeclare.getCreateBy() != null){
            creater = UserUtils.get(projectDeclare.getCreateBy().getId());
        }
        List<Team> teams = null;
        SysStudentExpansion studentExpansion = null;
        if(leader != null){
            teams = projectDeclareService.findTeams(leader.getId(), projectDeclare.getTeamId());
            studentExpansion = sysStudentExpansionService.getByUserId(leader.getId());
        }
        model.addAttribute("teams", teams);
        model.addAttribute("user", user);
        model.addAttribute("creater", creater);
        model.addAttribute("leader", leader);
        model.addAttribute("studentExpansion", studentExpansion);
        vo.setPlans(projectPlanService.findListByProjectId(projectDeclare.getId()));
        vo.setProjectDeclare(projectDeclare);
        List<Map<String, String>> teamStudents =  projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(), projectDeclare.getId());
        List<Map<String, String>> teamTeachers =  projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(), projectDeclare.getId());
        //vo.setTeamStudent(projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(), projectDeclare.getId()));
        //vo.setTeamTeacher(projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(), projectDeclare.getId()));
        model.addAttribute("teamStudents", teamStudents);
        model.addAttribute("teamTeachers", teamTeachers);

        SysAttachment sa = new SysAttachment();
        sa.setUid(projectDeclare.getId());
        sa.setFileStep(FileStepEnum.S100);
        sa.setType(FileTypeEnum.S0);
        vo.setFileInfo(sysAttachmentService.getFiles(sa));
        if (projectDeclare.getStatus() != null && !"0".equals(projectDeclare.getStatus())) {
            vo.setAuditInfo(projectDeclareService.getProjectAuditInfo(projectDeclare.getId()));
        }
        model.addAttribute("projectDeclareVo", vo);
        return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectDeclareViewPro";
    }

    /*
     * private boolean checkIsGraduation(Date date,int year) { if (date!=null) {
     * Calendar now=Calendar.getInstance(); Calendar c=Calendar.getInstance();
     * c.setTime(date); c.add(Calendar.YEAR, year); if (c.before(now)) { return
     * true; } } return false; }
     */
    @RequestMapping(value = "${frontPath}/project/projectDeclare/save")
    @ResponseBody
    public JSONObject save(ProjectDeclareVo vo, Model model, RedirectAttributes redirectAttributes,
                           HttpServletRequest request) {
        JSONObject js = new JSONObject();
        User user = UserUtils.getUser();
        ProjectDeclare pd = vo.getProjectDeclare();
        if (StringUtil.isEmpty(pd.getYear())) {
            pd.setYear(commonService.getApplyYear(pd.getActywId()));
        }
        js = commonService.checkProjectApplyOnSave(pd.getId(), pd.getActywId(), pd.getType(), pd.getTeamId(),
                pd.getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        if (!beanValidator(model, pd)) {
            js.put("ret", 0);
            js.put("msg", "保存失败");
            return js;
        }
        pd.setCreateBy(user);
        projectDeclareService.saveProjectDeclareVo(vo);
        js.put("id", pd.getId());
        js.put("ret", 1);
        js.put("msg", "保存成功");
        return js;
    }

    @RequestMapping(value = "${frontPath}/project/projectDeclare/submit")
    @ResponseBody
    public JSONObject submit(ProjectDeclareVo vo, Model model, RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        JSONObject js = new JSONObject();
        User user = UserUtils.getUser();
        ProjectDeclare pd = vo.getProjectDeclare();
        if (StringUtil.isEmpty(pd.getYear())) {
            pd.setYear(commonService.getApplyYear(pd.getActywId()));
        }
        js = commonService.checkProjectApplyOnSubmit(pd.getId(), pd.getActywId(), pd.getType(), pd.getTeamId(),
                pd.getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        pd.setCreateBy(user);
        projectDeclareService.submitProjectDeclareVo(vo);
        ActYw actYw = actYwService.get(pd.getActywId());
        if (actYw != null) {
            js.put("pptype", actYw.getProProject().getType());
            js.put("proProjectId", actYw.getProProject().getId());
        }
        js.put("actywId", pd.getActywId());
        js.put("projectId", pd.getId());
        js.put("ret", 1);
        js.put("msg", "恭喜您，项目申报成功！");
        return js;
    }

    @RequestMapping(value = "${frontPath}/project/projectDeclare/delete")
    public String delete(ProjectDeclare projectDeclare, RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        String ftb = request.getParameter("ftb");
        projectDeclareService.delete(projectDeclare, ftb);
        addMessage(redirectAttributes, "删除项目申报成功");
        return CoreSval.REDIRECT + CoreSval.getFrontPath() + "/project/projectDeclare/list?repage";
    }

    @RequestMapping(value = "${frontPath}/project/projectDeclare/delProject", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult delProject(ProjectDeclare projectDeclare, HttpServletRequest request) {
        try {
            String ftb = request.getParameter("ftb");
            projectDeclareService.delete(projectDeclare, ftb);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "${frontPath}/project/projectDeclare/findTeamPerson")
    public List<Map<String, String>> findTeamPerson(@RequestParam(required = true) String id) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<Map<String, String>> list1 = projectDeclareService.findTeamStudent(id);
        List<Map<String, String>> list2 = projectDeclareService.findTeamTeacher(id);
        for (Map<String, String> map : list1) {
            list.add(map);
        }
        for (Map<String, String> map : list2) {
            list.add(map);
        }
        if (list.size() == 0) {
            list = null;
        }
        return list;
    }

    @RequestMapping("${frontPath}/project/projectDeclare/onProjectApply")
    @ResponseBody
    public JSONObject onProjectApply(String actywId, String pid) {
        ProModel m = null;
        String year = null;
        if (StringUtil.isEmpty(pid)){
            m = proModelService.get(pid);
        }
        if (m != null){
            year = m.getYear();
        }else{
            year = commonService.getApplyYear(actywId);
        }

        return commonService.onProjectApply(actywId, pid, year);
    }

    @RequestMapping("${frontPath}/project/projectDeclare/checkProjectTeam")
    @ResponseBody
    public JSONObject checkProjectTeam(String proid, String actywId, String lowType, String teamid) {
        ProjectDeclare p = null;
        ProModel m = null;
        if (StringUtil.isNotEmpty(proid)) {
            p = projectDeclareService.get(proid);
        }
        if (p == null) {
            m = proModelService.get(proid);
        }
        String year = null;
        ;
        if (p != null) {
            year = p.getYear();
        } else if (m != null) {
            year = m.getYear();
        } else {
            year = commonService.getApplyYear(actywId);
        }
        return commonService.checkProjectTeam(proid, actywId, lowType, teamid, year);
    }

    /**
     * 根据负责人获取项目.
     *
     * @param uid 用户ID
     * @return ActYwRstatus 结果状态
     */
    @ResponseBody
    @RequestMapping(value = "${frontPath}/project/projectDeclare/ajaxProByLeader/{uid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<ProModel>> ajaxProByLeader(@PathVariable("uid") String uid) {
        List<ProModel> proModels = projectDeclareService.findListAllByLeader(uid);
        if ((proModels == null) || (proModels.size() <= 0)) {
            return new ApiTstatus<List<ProModel>>(false, "查询失败或结果为空！");
        }
        return new ApiTstatus<List<ProModel>>(true, "查询成功", proModels);
    }

    /**
     * 根据负责人获取所有团队.
     *
     * @param uid 用户ID
     * @return ActYwRstatus 结果状态
     */
    @ResponseBody
    @RequestMapping(value = "${frontPath}/project/projectDeclare/ajaxTeamByLeader/{uid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<Team>> ajaxTeamByLeader(@PathVariable("uid") String uid) {
        List<Team> teams = projectDeclareService.findTeams(uid, null);
        if ((teams == null) || (teams.size() <= 0)) {
            return new ApiTstatus<List<Team>>(false, "查询失败或结果为空！");
        }
        return new ApiTstatus<List<Team>>(true, "查询成功", teams);
    }

    @RequestMapping(value = "${adminPath}/project/projectDeclare/checkNumber")
    @ResponseBody
    public String checkNumber(String num, String id) {
        Integer i = projectDeclareService.getByNumberAndId(num, id);
        if (i == null || i == 0) {
            return "true";
        }
        return "false";
    }
}