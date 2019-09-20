package com.oseasy.pro.modules.state.web;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.authorize.service.AuthorizeService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardDetailService;
import com.oseasy.pro.modules.project.entity.ProMid;
import com.oseasy.pro.modules.project.entity.ProjectAuditInfo;
import com.oseasy.pro.modules.project.entity.ProjectClose;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.project.enums.ProjectStatusEnum;
import com.oseasy.pro.modules.project.service.ProMidService;
import com.oseasy.pro.modules.project.service.ProjectActTaskService;
import com.oseasy.pro.modules.project.service.ProjectAuditInfoService;
import com.oseasy.pro.modules.project.service.ProjectCloseService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.pro.modules.project.vo.ProjectStandardDetailVo;
import com.oseasy.pro.modules.state.service.StateService;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.SysStudentExpansionService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.FloatUtils;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * Created by zhangzheng on 2017/3/10.
 * 国创大赛后台
 */
@Controller
@RequestMapping(value = "${adminPath}/state")
public class StateController extends BaseController {
	@Autowired
    private AuthorizeService authorizeService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    ProjectActTaskService  projectActTaskService;
    @Autowired
    ProjectDeclareService projectDeclareService;

    @Autowired
    ProjectAuditInfoService projectAuditInfoService;

    @Autowired
    TeamService teamService;

    @Autowired
    TeamUserRelationService teamUserRelationService;

    @Autowired
    ProjectPlanService projectPlanService;

    @Autowired
    SysStudentExpansionService sysStudentExpansionService;

    @Autowired
    SysAttachmentService sysAttachmentService;

    @Autowired
    StateService stateService;
    @Autowired
    UserService userService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ProMidService proMidService;

    @Autowired
    ProjectCloseService projectCloseService;
    @Autowired
    AuditStandardDetailService auditStandardDetailService;


    @ModelAttribute
    public  ProjectDeclare get(@RequestParam(required=false) String id) {
        ProjectDeclare  projectDeclare = null;
        if (StringUtils.isNotBlank(id)) {
            projectDeclare = projectDeclareService.get(id);
        }
        if (projectDeclare == null) {
            projectDeclare = new ProjectDeclare();
        }
        return projectDeclare;
    }


    //学院立项待审核列表页面跳转 查询待审核任务
    @RequestMapping(value = "setAuditList")

    public String setAuditList(ProjectDeclare projectDeclare, HttpServletRequest request,
                               HttpServletResponse response, Model model) {
        //如果是学校管理员跳转到
        User user = UserUtils.getUser();
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) { //学校管理员
            return CoreSval.REDIRECT+"/a/state/schoolSetList";
        }
        Act act=new Act();
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("set1");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
       // Page<Act> pageForSearch =new Page<Act>(request, response);
       // Page<Act> page=projectActTaskService.allListForPage(pageForSearch,act);
		Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);

//        Page<ProModel> page = proModelService.findPage(new Page<>(request, response), proModel, recordIds);
         Page<ProjectDeclare> page=projectActTaskService.allListByPage(pageForSearch,act,projectDeclare);
         model.addAttribute("page",page);
         return ProSval.path.vms(ProEmskey.STATE.k()) + "setAuditList";
    }




    //学院立项已审核列表页面跳转 查询已审核任务
    @RequestMapping(value = "setAuditedList")
    public String setAuditedList(Act act, HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("set1");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.historicList(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "setAuditedList";
    }

    //学校立项待审核列表页面跳转 查询待审核任务
    @RequestMapping(value = "schoolSetList")

    public String schoolSetList(ProjectDeclare projectDeclare, HttpServletRequest request,
                                HttpServletResponse response, Model model) {
        Act act=new Act();
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("set2");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始

        Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);
        Page<ProjectDeclare> page=projectActTaskService.allListByPage(pageForSearch,act,projectDeclare);
//        Page<Act> pageForSearch =new Page<Act>(request, response);
//        Page<Act> page=projectActTaskService.allListForPage(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "schoolSetList";
    }


    //学校立项已审核列表页面跳转 查询已审核任务
    @RequestMapping(value = "schoolSetedList")
    public String schoolSetedList(Act act, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("set2");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.historicList(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "schoolSetedList";
    }


    //立项院级审核跳转页面
    @RequestMapping(value = "collegeSet")
    public String collegeSet(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        //查找审核标准
        List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(ProjectNodeVo.getGNodeIdByNodeId(ProjectNodeVo.PNODE_START_ID),ProjectNodeVo.YW_ID);
        model.addAttribute("standardList",standardList);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "collegeSet";
    }

    //立项校级审核跳转页面
    @RequestMapping(value = "schoolSet")
    public String schoolSet(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        //根据projectDeclare.id 查询立项评审意见
        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");
        model.addAttribute("infos1",infos1);
        //查找审核标准
        List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(ProjectNodeVo.getGNodeIdByNodeId(ProjectNodeVo.PNODE_START_ID),ProjectNodeVo.YW_ID);
        model.addAttribute("standardList",standardList);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "schoolSet";
    }


    //立项院级审核处理
    @RequestMapping(value = "collegeSetSave")
    public String collegeSetSave(ProjectDeclare projectDeclare, Model model) {
        projectDeclareService.collegeSetSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/state/setAuditList";
    }



    //立项校级审核处理
    @RequestMapping(value = "schoolSetSave")
    public String schoolSetSave(ProjectDeclare projectDeclare, Model model) {
        projectDeclareService.schoolSetSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/state/schoolSetList";
    }




    //提交中期报告跳转页面
    @RequestMapping(value = "submitMidReport")
    public String submitMidReport(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "submitMidReport";
    }

    //保存中期报告，并完成相应的工作流
    @RequestMapping(value = "midReportSave")
    public String midReportSave(ProjectDeclare projectDeclare, Model model) {
        projectDeclareService.midSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/act/task/todo";
    }




    //学校学院专家中期评分列表页面跳转
    //查询待审核、已审核任务
    //另外学院专家，能看到本学院的B级、C级待提交中期报告的数据
    //如果是学校专家，能看到A级、A+级待提交中期报告的数据
    @RequestMapping(value = "middleAuditList")

    public String middleAuditList(ProjectDeclare projectDeclare, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        //判断用户类型如果是学院秘书或者管理员进来，跳转到middleRatingList
        User user = UserUtils.getUser();
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)||SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) { //学院秘书和学校管理员
            return CoreSval.REDIRECT+"/a/state/middleRatingList";  //跳转到中期评级页面
        }

        projectDeclare.setStatus("3"); //3.待提交中期报告
        List<ProjectDeclare> projectList = projectDeclareService.findList(projectDeclare);
        Act act=new Act();
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("middleScore");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
        Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);


        String officeMap="";
        String levelMap="";
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) { //4.学院专家
            officeMap="and syso.id = '"+user.getOffice().getId()+"'";
            projectDeclare.getSqlMap().put("officeMap",officeMap);
            levelMap = "and a.level in('3','4')";   //B级或者C级

            projectDeclare.getSqlMap().put("levelMap",levelMap);
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXZJ)) { //4.学校专家
            levelMap = "and a.level in('1','2')";  //A+级或者A级

            projectDeclare.getSqlMap().put("levelMap",levelMap);
        }

        projectDeclare.setStatus("");
       // Page<ProjectDeclare> page=projectActTaskService.allListByPage(pageForSearch,act,projectDeclare);
        Page<ProjectDeclare> page=projectActTaskService.allListAddProListByPage(pageForSearch,act,projectDeclare,projectList);

        //Page<Act> page=projectActTaskService.allListForPageAddProjectList(pageForSearch,act,projectList);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "middleAuditList";
    }


    //中期已评分列表页面跳转 查询已审核任务
    @RequestMapping(value = "middleAuditedList")
    public String middleAuditedList(Act act, HttpServletRequest request,
                                    HttpServletResponse response, Model model) {
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("middleScore");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.historicList(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "middleAuditedList";
    }

    //中期评级列表页面跳转
    // 查询待审核、已审核任务
    //todo 处在待学院专家中期评级的数据
    //另外学院秘书，能看到本学院的B级、C级待提交中期报告的数据
    //如果是学校管理员，能看到A级、A+级待提交中期报告的数据
    @RequestMapping(value = "middleRatingList")

    public String middleRatingList(ProjectDeclare projectDeclare, HttpServletRequest request,
                                   HttpServletResponse response, Model model) {

//        Page<Act> pageForSearch =new Page<Act>(request, response);
        //查找待提交中期报告的数据
        User user = UserUtils.getUser();

        ProjectDeclare projectDeclareMid=new ProjectDeclare();
        projectDeclareMid=projectDeclare;
        projectDeclareMid.setStatus("3"); //3.待提交中期报告
        String officeMap="";
        String levelMap="";
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)) { //4.学院教学秘书
            officeMap="and syso.id = '"+user.getOffice().getId()+"'";

            projectDeclareMid.getSqlMap().put("officeMap",officeMap);

            projectDeclareMid.getSqlMap().put("levelMap",levelMap);
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) { //4.学校管理员
            levelMap = "and a.level in('1','2')";  //A+级或者A级

            projectDeclareMid.getSqlMap().put("levelMap",levelMap);
        }

        List<ProjectDeclare> projectList = projectDeclareService.findList(projectDeclareMid);

        Act actForFinished = new Act();

        actForFinished.setProcDefKey("state_project_audit");  //国创项目流程名称
        actForFinished.setTaskDefKey("set");   // 中期检查的任务
        actForFinished.setStatus("middleScore");  //状态在结项评分中   closeScore2是学校专家结项评分  closeScore3 是学院专家评分 在流程图中主键ID可以看到
        List<Act> finishedList = projectActTaskService.finishedList(actForFinished);
//        Page<Act> page=projectActTaskService.allListForPageAddProjectList(pageForSearch,act,projectList);

        Act act=new Act();
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("middleRating");          // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
        Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);
        //Page<ProjectDeclare> page=projectActTaskService.allListByPage(pageForSearch,act,projectDeclare);
        projectDeclare.setStatus("");
        Page<ProjectDeclare> page=projectActTaskService.allMidListByPage(pageForSearch,act,projectDeclare,finishedList,projectList);
        //Page<Act> page=projectActTaskService.middleRatingList(pageForSearch,act,finishedList,projectList);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "middleRatingList";
    }


    //中期已评级列表页面跳转 查询已审核任务
    @RequestMapping(value = "middleRatedList")
    public String middleRatedList(Act act, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("middle");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.historicList(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "middleRatedList";
    }





    //专家中期评分页面
    @RequestMapping(value = "expCheck")
    public String expCheck(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        //根据projectDeclare.id 查询立项评审意见
        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");
        model.addAttribute("infos1",infos1);
        ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
        if (proMid!=null) {
            model.addAttribute("proMidId",proMid.getId());
        }
        //查找审核标准
        List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(ProjectNodeVo.getGNodeIdByNodeId(ProjectNodeVo.PNODE_MIDDLE_ID),ProjectNodeVo.YW_ID);
        model.addAttribute("standardList",standardList);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "expCheck";
    }

    //中期评分结果保存 保存评分、意见到子表 执行一步工作流
    @RequestMapping(value = "expCheckSave")
    public String expCheckSave(ProjectDeclare projectDeclare, Model model) {
        projectDeclareService.expCheckSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/state/middleAuditList";
    }

    //中期审核跳转页面
    @RequestMapping(value = "secCheck")
    public String secCheck(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        //根据projectDeclare.id 查询立项评审意见
        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");
        model.addAttribute("infos1",infos1);
        //中期评分意见
        List<ProjectAuditInfo> infos2= getInfo(projectDeclare.getId(),"2");
        model.addAttribute("infos2",infos2);

        ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
        if (proMid!=null) {
            model.addAttribute("proMidId",proMid.getId());
        }


        //查找审核标准
        List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(ProjectNodeVo.getGNodeIdByNodeId(ProjectNodeVo.PNODE_MIDDLE_ID),ProjectNodeVo.YW_ID);
        model.addAttribute("standardList",standardList);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "secCheck";
    }

    //中期评级保存
    @RequestMapping(value = "secCheckSave")
    public String secCheckSave(ProjectDeclare projectDeclare, Model model) {
        if (StringUtils.equals(projectDeclare.getMidResult(),"4")) {
            projectDeclare.setPass("4");
        }
        projectDeclareService.secCheckSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/state/middleRatingList";
    }


    //提交结项报告跳转页面
    @RequestMapping(value = "submitCloReport")
    public String submitCloReport(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "submitCloseReport";
    }

    //保存结项报告，并完成相应的工作流
    @RequestMapping(value = "closeReportSave")
    public String closeReportSave(ProjectDeclare projectDeclare, Model model) {
        projectDeclareService.closeSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/act/task/todo";
    }


    //结项评分待审核列表页面跳转
    //查询待审核、已审核任务
    //另外学院专家，能看到本学院的B级、C级待提交结项报告的数据
    //如果是学校专家，能看到A级、A+级待提交中期报告的数据
    //  秘书、管理员进来能看到专家们在评分（跳转到secCloseSearch）
    @RequestMapping(value = "closeAuditList")
    public String closeAuditList(ProjectDeclare projectDeclare, HttpServletRequest request,
                                 HttpServletResponse response, Model model) {
        //判断用户类型如果是学院秘书或者管理员进来，跳转到secCloseSearch
        User user = UserUtils.getUser();
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS) || SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) { //学院秘书和学校管理员
            return CoreSval.REDIRECT+"/a/state/secCloseSearch";
        }


        //查找待提交结项报告的数据
        ProjectDeclare projectDeclareForSearch = new ProjectDeclare();

        projectDeclareForSearch=projectDeclare;
        projectDeclareForSearch.setStatus("6"); //3.待提交结项报告
        String officeMap="";
        String levelMap="";
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) { //4.学院专家
            officeMap="and syso.id = '"+user.getOffice().getId()+"'";
            projectDeclareForSearch.getSqlMap().put("officeMap",officeMap);
            levelMap = "and a.level in('3','4')";   //B级或者C级
            projectDeclareForSearch.getSqlMap().put("levelMap",levelMap);
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXZJ)) { //5.学校专家
            levelMap = "and a.level in('1','2')";  //A+级或者A级
            projectDeclareForSearch.getSqlMap().put("levelMap",levelMap);
        }
        List<ProjectDeclare> projectList = projectDeclareService.findList(projectDeclareForSearch);

        Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);
        //Page<Act> page=projectActTaskService.allListForPageAddProjectList(pageForSearch,act,projectList);
        projectDeclare.setStatus("");
        //如果是专家，则查看其待审核已审核数据
        Act act=new Act();
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("closeScore");   // 表示立项阶段 见流程图的userTask的id
        Page<ProjectDeclare> page=projectActTaskService.allListAddProListByPage(pageForSearch,act,projectDeclare,projectList);

        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "closeAuditList";
    }

    //查看秘书、管理员中期检查的已审核任务， 但审核状态在结项评分中
    //另外学院秘书，能看到本学院的B级、C级待提交结项报告的数据
    //如果是学校管理员，能看到A级、A+级待提交中期报告的数据
    @RequestMapping(value = "secCloseSearch")
    public String secCloseSearch(ProjectDeclare projectDeclare, HttpServletRequest request,
                                 HttpServletResponse response, Model model ) {

        //查找待提交结项报告的数据
        User user = UserUtils.getUser();
        ProjectDeclare projectDeclareForSearch = new ProjectDeclare();

        projectDeclareForSearch=projectDeclare;
        projectDeclareForSearch.setStatus("6"); //3.待提交结项报告
        String officeMap="";
        String levelMap="";
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)) { //3.学院教学秘书
            officeMap="and syso.id = '"+user.getOffice().getId()+"'";
            projectDeclareForSearch.getSqlMap().put("officeMap",officeMap);
            levelMap = "and a.level in('3','4')";   //B级或者C级
            projectDeclareForSearch.getSqlMap().put("levelMap",levelMap);
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) { //6.学校管理员
            levelMap = "and a.level in('1','2')";  //A+级或者A级
            projectDeclareForSearch.getSqlMap().put("levelMap",levelMap);
        }
        List<ProjectDeclare> projectList = projectDeclareService.findList(projectDeclareForSearch);

        Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);
        Act act=new Act();
//        act.setProcDefKey("state_project_audit");  //国创项目流程名称
//        act.setTaskDefKey("closeScore");   // 表示立项阶段 见流程图的userTask的id

        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("middle");   // 中期检查的任务
        act.setStatus("closeScore");  //状态在结项评分中   closeScore2是学校专家结项评分  closeScore3 是学院专家评分 在流程图中主键ID可以看到
        projectDeclare.setStatus("");
        Page<ProjectDeclare> page=projectActTaskService.allListAddProListByPage(pageForSearch,act,projectDeclare,projectList);
//        Page<Act> pageForSearch =new Page<Act>(request, response);
       // Page<Act> page=projectActTaskService.finishedListForPage(pageForSearch,act,projectList);
        model.addAttribute("page",page);
            return ProSval.path.vms(ProEmskey.STATE.k()) + "secCloseSearch";
    }


    //结项评已审核列表页面跳转 查询已审核任务
    @RequestMapping(value = "closeAuditedList")
    public String closeAuditedList(Act act, HttpServletRequest request,
                                   HttpServletResponse response, Model model) {
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("closeScore");   // 表示立项阶段 见流程图的userTask的id
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.historicList(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "closeAuditedList";
    }

    //答辩评分待审核列表页面跳转 查询待审核任务
    @RequestMapping(value = "closeReplyingList")
    public String closeReplyingList(ProjectDeclare projectDeclare, HttpServletRequest request,
                                    HttpServletResponse response, Model model) {

        Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);
        //如果是专家，则查看其待审核已审核数据
        Act act=new Act();
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("closeReply");   // 表示答辩评分阶段 见流程图的userTask的id
        Page<ProjectDeclare> page=projectActTaskService.allListByPage(pageForSearch,act,projectDeclare);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "closeReplyingList";
    }


    //答辩评已审核列表页面跳转 查询已审核任务
    @RequestMapping(value = "closeReplyedList")
    public String closeReplyedList(Act act, HttpServletRequest request,
                                   HttpServletResponse response, Model model) {
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("closeReply");   // 表示答辩评分阶段 见流程图的userTask的id
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.historicList(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "closeReplyedList";
    }




    //专家结项评分页面
    @RequestMapping(value = "expClose")
    public String expClose(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        //根据projectDeclare.id 查询立项评审意见
        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");
        model.addAttribute("infos1",infos1);
        List<ProjectAuditInfo> infos2= getInfo(projectDeclare.getId(),"2");
        Iterator<ProjectAuditInfo> it=infos2.iterator();
        User me= UserUtils.getUser();
        while (it.hasNext()) {
            ProjectAuditInfo info=it.next();
            if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                it.remove();
            }

        }
        model.addAttribute("infos2",infos2);
        List<ProjectAuditInfo> infos3= getInfo(projectDeclare.getId(),"3");
        model.addAttribute("infos3",infos3);
        //查找中期报告id
        ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
        if (proMid!=null) {
            model.addAttribute("proMidId",proMid.getId());
        }
        //查找结项报告id
        ProjectClose projectClose=projectCloseService.getByProjectId(projectDeclare.getId());
        if (projectClose!=null) {
            model.addAttribute("proCloseId",projectClose.getId());
        }
        //查找审核标准
        List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(ProjectNodeVo.getGNodeIdByNodeId(ProjectNodeVo.PNODE_CLOSE_ID),ProjectNodeVo.YW_ID);
        model.addAttribute("standardList",standardList);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "expClose";
    }

    //结项评分结果保存 保存评分、意见到子表 执行一步工作流
    @RequestMapping(value = "expCloseSave")
    public String expCloseSave(ProjectDeclare projectDeclare, Model model) {
        projectDeclareService.expCloseSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/state/closeAuditList";
    }
    //答辩评分跳转页面
    @RequestMapping(value = "secClose")
    public String  secClose(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);
        //根据projectDeclare.id 查询立项评审意见
        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");
        model.addAttribute("infos1",infos1);
        List<ProjectAuditInfo> infos2= getInfo(projectDeclare.getId(),"2");
        model.addAttribute("infos2",infos2);
        List<ProjectAuditInfo> infos3= getInfo(projectDeclare.getId(),"3");
        model.addAttribute("infos3",infos3);
        List<ProjectAuditInfo> infos4= getInfo(projectDeclare.getId(),"4");
        model.addAttribute("infos4",infos4);
        //查找中期报告id
        ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
        model.addAttribute("proMidId",proMid.getId());
        //查找结项报告id
        ProjectClose projectClose=projectCloseService.getByProjectId(projectDeclare.getId());
        model.addAttribute("proCloseId",projectClose.getId());

        //查找审核标准
        List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(ProjectNodeVo.getGNodeIdByNodeId(ProjectNodeVo.PNODE_REPLY_ID),ProjectNodeVo.YW_ID);
        model.addAttribute("standardList",standardList);


        return ProSval.path.vms(ProEmskey.STATE.k()) + "secClose";
    }

    //答辩评分结果保存
    @RequestMapping(value = "secCloseSave")
    public String secCloseSave(ProjectDeclare projectDeclare, Model model,RedirectAttributes redirectAttributes) {
        projectDeclareService.secCloseSave(projectDeclare);
        addMessage(redirectAttributes, "答辩评分保存成功");
        return CoreSval.REDIRECT+"/a/state/closeReplyingList";
    }

    //已审核 查看
    @RequestMapping(value = "infoView")
    public String infoView(ProjectDeclare projectDeclare, Model model,HttpServletRequest request) {
        model.addAttribute("projectDeclare",projectDeclare);
        String  taskDefinitionKey=request.getParameter("taskDefinitionKey");

        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");  //立项评级
        model.addAttribute("infos1",infos1);
        List<ProjectAuditInfo> infos2= getInfo(projectDeclare.getId(),"2");  //中期评分
        model.addAttribute("infos2",infos2);
        List<ProjectAuditInfo> infos3= getInfo(projectDeclare.getId(),"3");
        model.addAttribute("infos3",infos3);
        List<ProjectAuditInfo> infos4= getInfo(projectDeclare.getId(),"4");
        model.addAttribute("infos4",infos4);
        List<ProjectAuditInfo> infos5= getInfo(projectDeclare.getId(),"5");
        model.addAttribute("infos5",infos5);
        List<ProjectAuditInfo> infos6= getInfo(projectDeclare.getId(),"6");
        model.addAttribute("infos6",infos6);

        model.addAttribute("infos1",infos1);
        model.addAttribute("infos2",infos2);
        model.addAttribute("infos3",infos3);
        model.addAttribute("infos4",infos4);
        model.addAttribute("infos5",infos5);
        model.addAttribute("infos6",infos6);

        User me= UserUtils.getUser();
        int showMiddleScore=0; //是否显示中期平均分 0不显示 1, 显示
        int showFinalScore=0; //是否显示结项平均分 0不显示 1, 显示
        int show2=0,show3=0,show4=0,show5=0,show6=0;
        int showMiddleReport=0; //是否显示中期报告  0不显示 1显示
        int showCloseReport=0; //是否显示结项报告  0不显示 1显示

//        ProjectAuditInfo myInfo = null;
//        //立项审核步骤
//        if ("set1".equals(taskDefinitionKey)||"set2".equals(taskDefinitionKey)) {
//            ProjectAuditInfo paiSearch = new ProjectAuditInfo();
//            User user =UserUtils.getUser();
//            paiSearch.setCreateBy(user);
//            paiSearch.setProjectId(projectDeclare.getId());
//            paiSearch.setAuditStep("1");
//            myInfo = projectAuditInfoService.findInfoByUserId(paiSearch);
//        }


        //中期评分步骤，只能看到自己的评分
        if ("middleScore2".equals(taskDefinitionKey)
                ||"middleScore3".equals(taskDefinitionKey)) {
            Iterator<ProjectAuditInfo> it=infos2.iterator();
            while (it.hasNext()) {
                ProjectAuditInfo info=it.next();
                if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                    it.remove();
                }

            }
            show2=1;
            showMiddleScore=1;
            showMiddleReport=1;
        }

        //中期评级步骤
        if ("middleRating4".equals(taskDefinitionKey)
                ||"middleRating5".equals(taskDefinitionKey)) {
            showMiddleScore=1;
            show2=1;
            show3=1;
            showMiddleReport=1;
        }

        //结项评分步骤
        if ("closeScore2".equals(taskDefinitionKey)
                ||"closeScore3".equals(taskDefinitionKey)) {
            Iterator<ProjectAuditInfo> it2=infos2.iterator();
            while (it2.hasNext()) {
                ProjectAuditInfo info=it2.next();
                if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                    it2.remove();
                }

            }

            showMiddleScore=1;
            showFinalScore=1;
            show2=1;
            show3=1;
            show4=1;
            Iterator<ProjectAuditInfo> it=infos4.iterator();
            while (it.hasNext()) {
                ProjectAuditInfo info=it.next();
                if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                    it.remove();
                }

            }
            showMiddleReport=1;
            showCloseReport=1;
        }

        //给学院秘书、学校管理员查看的数据
        if ("closeScore".equals(taskDefinitionKey)) {
            showMiddleScore=1;
            showFinalScore=1;
            show2=1;
            show3=1;
            show4=1;
            showMiddleReport=1;
            showCloseReport=1;
        }



        //   答辩评分步骤
        if ("closeReply4".equals(taskDefinitionKey)
                ||"closeReply5".equals(taskDefinitionKey)) {
            showMiddleScore=1;
            showFinalScore=1;
            show2=1;
            show3=1;
            show4=1;
            show5=1;
            showMiddleReport=1;
            showCloseReport=1;
        }

        //   答辩评分步骤
        if ("assess1".equals(taskDefinitionKey)
                ||"assess2".equals(taskDefinitionKey)) {
            showMiddleScore=1;
            showFinalScore=1;
            show2=1;
            show3=1;
            show4=1;
            show5=1;
            show6=1;
            showMiddleReport=1;
            showCloseReport=1;
        }

        if (showMiddleReport==1) { //查找中期报告id
            ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
            if(proMid!=null){
                model.addAttribute("proMidId",proMid.getId());
            }
        }
        if (showCloseReport==1) { //查找结项报告id
            ProjectClose projectClose=projectCloseService.getByProjectId(projectDeclare.getId());
            if(projectClose!=null){
                model.addAttribute("proCloseId",projectClose.getId());
            }
        }

        model.addAttribute("showMiddleScore",showMiddleScore);
        model.addAttribute("showFinalScore",showFinalScore);

        model.addAttribute("show2",show2);
        model.addAttribute("show3",show3);
        model.addAttribute("show4",show4);
        model.addAttribute("show5",show5);
        model.addAttribute("show6",show6);
        model.addAttribute("showMiddleReport",showMiddleReport);
        model.addAttribute("showCloseReport",showCloseReport);
//        model.addAttribute("myInfo",myInfo);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "infoView";
    }



    //结果评定待审核列表页面跳转 查询待审核任务
    @RequestMapping(value = "assessList")
    public String assessList(ProjectDeclare projectDeclare, HttpServletRequest request,
                             HttpServletResponse response, Model model) {
//        act.setProcDefKey("state_project_audit");  //国创项目流程名称
//        act.setTaskDefKey("assess");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
//        Page<Act> pageForSearch =new Page<Act>(request, response);
//        Page<Act> page=projectActTaskService.allListForPage(pageForSearch,act);
//        model.addAttribute("page",page);

        Page<ProjectDeclare> pageForSearch =new Page<ProjectDeclare>(request, response);
        //如果是专家，则查看其待审核已审核数据
        Act act=new Act();
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("assess");   // 表示答辩评分阶段 见流程图的userTask的id
        Page<ProjectDeclare> page=projectActTaskService.allListByPage(pageForSearch,act,projectDeclare);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "assessList";
    }


    //结果评定已审核列表页面跳转 查询已审核任务
    @RequestMapping(value = "assessedList")
    public String assessedList(Act act, HttpServletRequest request,
                               HttpServletResponse response, Model model) {
        act.setProcDefKey("state_project_audit");  //国创项目流程名称
        act.setTaskDefKey("assess");   // 表示立项阶段 见流程图的userTask的id 所有的立项的userTask都是以set开始
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.historicList(pageForSearch,act);
        model.addAttribute("page",page);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "assessedList";
    }


    //结果评定审核跳转页面
    @RequestMapping(value = "secAssess")
    public String  secAssess(ProjectDeclare projectDeclare, Model model) {
        //根据id查询业务表，获得entity  在ModelAttribute中实现了
        model.addAttribute("projectDeclare",projectDeclare);

        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");
        model.addAttribute("infos1",infos1);
        List<ProjectAuditInfo> infos2= getInfo(projectDeclare.getId(),"2");
        model.addAttribute("infos2",infos2);
        List<ProjectAuditInfo> infos3= getInfo(projectDeclare.getId(),"3");
        model.addAttribute("infos3",infos3);
        List<ProjectAuditInfo> infos4= getInfo(projectDeclare.getId(),"4");
        model.addAttribute("infos4",infos4);
        List<ProjectAuditInfo> infos5= getInfo(projectDeclare.getId(),"5");
        model.addAttribute("infos5",infos5);
        //计算平均分
        float averageScore=0;
        float replyScore = projectDeclare.getReplyScore();
        float finalScore = projectDeclare.getFinalScore();
        averageScore= FloatUtils.division(replyScore+finalScore,2);
        model.addAttribute("averageScore",averageScore);
        //查找中期报告id
        ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
        if (proMid!=null) {
            model.addAttribute("proMidId",proMid.getId());
        }

        //查找结项报告id
        ProjectClose projectClose=projectCloseService.getByProjectId(projectDeclare.getId());
        if (projectClose!=null) {
            model.addAttribute("proCloseId",projectClose.getId());
        }


        //查找审核标准
        List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(ProjectNodeVo.getGNodeIdByNodeId(ProjectNodeVo.PNODE_ASSESS_ID),ProjectNodeVo.YW_ID);
        model.addAttribute("standardList",standardList);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "secAssess";
    }

    //结果评定结项审核保存
    @RequestMapping(value = "secAssessSave")
    public String secAssessSave(ProjectDeclare projectDeclare, Model model) {
        projectDeclareService.secAssessSave(projectDeclare);
        return CoreSval.REDIRECT+"/a/state/assessList";
    }

    //项目查询
    @RequestMapping(value = "allList")
    public String allList(ProjectDeclare projectDeclare,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Model model) throws ParseException {

        Page<ProjectDeclare> pageForSearch= new Page<ProjectDeclare>(request, response);
        if (StringUtils.isNotBlank(projectDeclare.getStartDateStr())) {
            projectDeclare.setYear(projectDeclare.getStartDateStr());
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//            Date startDate = sdf.parse(projectDeclare.getStartDateStr());
//            projectDeclare.setStartDate(startDate);
//            projectDeclare.setEndDate(DateUtil.addYear(projectDeclare.getStartDate(),1));
        }
        User user=UserUtils.getUser();
        String officeMap="";
        String levelMap="";
        Page<ProjectDeclare> page=null;
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)||SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) {//3.学院教学秘书 4.学院专家
            officeMap="and syso.id = '"+user.getOffice().getId()+"'";
            projectDeclare.getSqlMap().put("officeMap",officeMap);
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) { //4.学院专家
            levelMap = "and a.level in('3','4')";   //B级或者C级
            projectDeclare.getSqlMap().put("levelMap",levelMap);
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXZJ)) { //4.学校专家
            levelMap = "and a.level in('1','2')";  //A+级或者A级
            projectDeclare.getSqlMap().put("levelMap",levelMap);
        }
        List<ProjectStatusEnum> projectStatusEnumList=ProjectStatusEnum.getAll();
        model.addAttribute("projectStatusEnumList",projectStatusEnumList);
        page = projectDeclareService.findPage(pageForSearch,projectDeclare);

        //根据procInsId 查找taskname
        if (page!=null) {
            List<ProjectDeclare> list=page.getList();
            for(ProjectDeclare item:list) {
                String procInsId=item.getProcInsId();
                if (StringUtils.isNotBlank(procInsId)) {
                    String taskName=ProjectStatusEnum.getNameByValue(item.getStatus());
                    item.getAct().setTaskName(taskName);
                }
            }
        }
        if (UserUtils.isAdminOrSuperAdmin(UserUtils.getUser())) {
            model.addAttribute("isAdmin", "1");
        }
        model.addAttribute("page",page);
        model.addAttribute(ActYwGroup.JK_ACTYW_ID, ProjectNodeVo.YW_ID);
        model.addAttribute(ActYwGroup.JK_GNODE_ID, ProjectNodeVo.P_START_ID);
        model.addAttribute(ActYwGroup.JK_ACTYW_ID, ProjectNodeVo.YW_ID);
        return ProSval.path.vms(ProEmskey.STATE.k()) + "allList";
    }

    //查询项目表单
    @RequestMapping(value = "projectDetail")
    public String projectDetail(ProjectDeclare projectDeclare,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {
        model.addAttribute("projectDeclare",projectDeclare);

        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");  //立项评级
        List<ProjectAuditInfo> infos2= getInfo(projectDeclare.getId(),"2");  //中期评分
        List<ProjectAuditInfo> infos3= getInfo(projectDeclare.getId(),"3");  //中期评级
        List<ProjectAuditInfo> infos4= getInfo(projectDeclare.getId(),"4"); //结项评分
        List<ProjectAuditInfo> infos5= getInfo(projectDeclare.getId(),"5"); //答辩评分
        List<ProjectAuditInfo> infos6= getInfo(projectDeclare.getId(),"6"); //结项审核


        User me=UserUtils.getUser();
        if (SysUserUtils.checkHasRole(me, RoleBizTypeEnum.XYZJ)||SysUserUtils.checkHasRole(me, RoleBizTypeEnum.XXZJ)) {  //如果是院级专家或者校级专家，只能看到自己的评分信息
            Iterator<ProjectAuditInfo> it2=infos2.iterator();
            while (it2.hasNext()) {
                ProjectAuditInfo info=it2.next();
                if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                    it2.remove();
                }
            }

            Iterator<ProjectAuditInfo> it=infos4.iterator();
            while (it.hasNext()) {
                ProjectAuditInfo info=it.next();
                if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                    it.remove();
                }
            }

        }


        model.addAttribute("infos1",infos1);
        model.addAttribute("infos2",infos2);
        model.addAttribute("infos3",infos3);
        model.addAttribute("infos4",infos4);
        model.addAttribute("infos5",infos5);
        model.addAttribute("infos6",infos6);

        //查找中期报告id
        ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
        if (proMid!=null) {
            model.addAttribute("proMidId",proMid.getId());
        }

        //查找结项报告id
        ProjectClose projectClose=projectCloseService.getByProjectId(projectDeclare.getId());
        if (projectClose!=null) {
            model.addAttribute("proCloseId",projectClose.getId());
        }

        //学院秘书或者学校管理员中期检查可以把待提交中期报告的数据变更为项目终止
        //因此在这儿加上一个标志
        String editFlag = request.getParameter("editFlag");
        model.addAttribute("editFlag",editFlag);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "projectDetail";
    }

    //项目变更
    @RequestMapping(value = "projectEdit")
    @RequiresPermissions("project:dcproject:modify")
    public String projectEdit(ProjectDeclare projectDeclare,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {
    	model.addAttribute("xfAuthorize",authorizeService.checkMenuByNum(5));
        model.addAttribute("projectDeclare",projectDeclare);
//        //查询studentList
//        List<SysStudentExpansion> studentList=sysStudentExpansionService.getStudentList();
//        model.addAttribute("studentList",studentList);


        //查找项目团队相关信息 projectDeclare.id
        Team team=teamService.get(projectDeclare.getTeamId());
        model.addAttribute("team",team);

        List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
        model.addAttribute("turStudents",turStudents);
        //查找导师
        //List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
        List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
        model.addAttribute("turTeachers",turTeachers);


        //查找项目分工
        List<ProjectPlan> plans=projectPlanService.findListByProjectId(projectDeclare.getId());
        model.addAttribute("plans",plans);

//        //查找项目附件
//        Map<String,String> map=new HashMap<String,String>();
//        map.put("file_step", FileStepEnum.S100.getValue());
//        map.put("uid", projectDeclare.getId());
//        map.put("type", FileTypeEnum.S0.getValue());
//        List<Map<String,String>> fileInfo = sysAttachmentService.getFileInfo(map);
//        model.addAttribute("fileInfo",fileInfo);

        //查找项目附件
        SysAttachment sa=new SysAttachment();
        sa.setUid(projectDeclare.getId());
        sa.setFileStep(FileStepEnum.S100);
        sa.setType(FileTypeEnum.S0);
        List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
        model.addAttribute("fileListMap",fileListMap);



        //查找审核信息
        List<ProjectAuditInfo> infos1= getInfo(projectDeclare.getId(),"1");  //立项评级
        List<ProjectAuditInfo> infos2= getInfo(projectDeclare.getId(),"2");  //中期评分
        List<ProjectAuditInfo> infos3= getInfo(projectDeclare.getId(),"3");  //中期评级
        List<ProjectAuditInfo> infos4= getInfo(projectDeclare.getId(),"4"); //结项评分
        List<ProjectAuditInfo> infos5= getInfo(projectDeclare.getId(),"5"); //答辩评分
        List<ProjectAuditInfo> infos6= getInfo(projectDeclare.getId(),"6"); //结项审核


        User me=UserUtils.getUser();
        if (SysUserUtils.checkHasRole(me, RoleBizTypeEnum.XYZJ)||SysUserUtils.checkHasRole(me, RoleBizTypeEnum.XXZJ)) {  //如果是院级专家或者校级专家，只能看到自己的评分信息
            Iterator<ProjectAuditInfo> it2=infos2.iterator();
            while (it2.hasNext()) {
                ProjectAuditInfo info=it2.next();
                if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                    it2.remove();
                }
            }

            Iterator<ProjectAuditInfo> it=infos4.iterator();
            while (it.hasNext()) {
                ProjectAuditInfo info=it.next();
                if (!StringUtils.equals(info.getCreateBy().getId(),me.getId())) {
                    it.remove();
                }
            }

        }


        model.addAttribute("infos1",infos1);
        model.addAttribute("infos2",infos2);
        model.addAttribute("infos3",infos3);
        model.addAttribute("infos4",infos4);
        model.addAttribute("infos5",infos5);
        model.addAttribute("infos6",infos6);

        //查找中期报告id
        ProMid proMid=proMidService.getByProjectId(projectDeclare.getId());
        if (proMid!=null) {
            model.addAttribute("proMidId",proMid.getId());
        }

        //查找结项报告id
        ProjectClose projectClose=projectCloseService.getByProjectId(projectDeclare.getId());
        if (projectClose!=null) {
            model.addAttribute("proCloseId",projectClose.getId());
        }
        model.addAttribute("changeGnodes", stateService.getProjectDeclareChangeGnode(projectDeclare));
        return ProSval.path.vms(ProEmskey.STATE.k()) + "projectEdit";

    }

    //保存项目变更
    @RequestMapping(value = "saveEdit")
    @RequiresPermissions("project:dcproject:modify")
    @ResponseBody
    public JSONObject saveEdit(ProjectDeclare projectDeclare,String modifyPros,
                           HttpServletRequest request) {
    	try {
    		return stateService.saveModify(projectDeclare, modifyPros);
		} catch (Exception e) {
			logger.error(e.getMessage());
			JSONObject js=new JSONObject();
			js.put("ret", "0");
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
			return js;
		}
    }

    //项目终止 先更改主表状态，再删除流程
    @RequestMapping(value = "closeProject")

    public String  closeProject(ProjectDeclare projectDeclare,RedirectAttributes redirectAttributes) {

        projectDeclare.setFinalResult("4"); //中期不合格(项目终止）
        projectDeclare.setStatus("8");   //项目终止
        projectDeclare.setMidScore(0f);
        projectDeclare.setFinalScore(0f);
        projectDeclareService.updateFinalResult(projectDeclare);
//        projectDeclareService.saveScore(projectDeclare.getId());

//        if (StringUtils.isNotBlank(projectDeclare.getProcInsId())) {
//            try{
//                runtimeService.deleteProcessInstance(projectDeclare.getProcInsId(),"");
//            }catch (Exception e) {
//                System.out.println("该项目的工作流已经走完");
//            }
//            try{
//                historyService.deleteHistoricProcessInstance(projectDeclare.getProcInsId());
//            }catch (Exception e) {
//                System.out.println("该项目没有历史记录");
//            }
//        }

        addMessage(redirectAttributes, "项目终止成功");
        return CoreSval.REDIRECT+"/a/state/middleRatingList";
    }


    //删除项目
    @RequestMapping(value="projectDelete")
    public String projectDelete(ProjectDeclare projectDeclare) {
        projectDeclareService.deleteProjectDeclare(projectDeclare);
        return CoreSval.REDIRECT+"/a/state/allList";
    }

    //删除项目
    @RequestMapping(value="projectDeleteAll")
    public String projectDeleteAll() {
        ProjectDeclare projectDeclare=new ProjectDeclare();
        List<ProjectDeclare> list=projectDeclareService.findList(projectDeclare);

        projectDeclareService.deleteAllProjectDeclare(list);
        return CoreSval.REDIRECT+"/a/state/allList";
    }
    /**
     * 获得评审意见、评分
     * @param projectId 项目id
     * @param auditStep 审核步骤 ('1','立项评审';'2','中期检查评分';'3','中期检查评级';'4','结项审核';'5','答辩分记录',6结果评定)
     * @return
     */
    private  List<ProjectAuditInfo> getInfo(String projectId,String auditStep) {
        ProjectAuditInfo pai=new ProjectAuditInfo();
        pai.setProjectId(projectId);
        pai.setAuditStep(auditStep);
        List<ProjectAuditInfo> infos= projectAuditInfoService.getInfo(pai);
        return infos;
    }


    //学校立项待审核列表页面跳转 查询待审核任务
    @RequestMapping(value = "changeSchoolSet")
    @ResponseBody
    public JSONObject changeSchoolSet( HttpServletRequest request, HttpServletResponse response, Model model) {
        JSONObject js=new JSONObject();
        try {
            String number = request.getParameter("number");
            ProjectDeclare projectDeclare = projectDeclareService.getByNo(number);
            if(projectDeclare==null){
                js.put("ret", "0");
                js.put("msg", "没有该编号的项目");
                return js;
            }
            String proInsId = projectDeclare.getProcInsId();
            String taskId = actTaskService.getTaskidByProcInsId(proInsId);
            String roleId = request.getParameter("roleId");
            List<String> claims = userService.findListByRoleIdAndOffice(roleId, projectDeclare.getCreateBy().getId());
            //set1 流程节点 collegeSec流程节点配置的角色
            if(StringUtil.checkEmpty(claims)){
                js.put("ret", "0");
                js.put("msg", "该角色下没有用户");
            }else{
                actTaskService.rollBackFlow(taskId, "collegeSec", claims, "set1");
                projectDeclare.setStatus("1");
                projectDeclare.setLevel("");
                projectDeclare.setNumber(number.substring(1));
                projectDeclareService.updateStatus(projectDeclare);
                js.put("ret", "1");
            }
        }catch (Exception e){
            logger.error(e.toString());
            js.put("ret", "0");
            js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
        }
        return js;
    }

    //学校立项待审核列表页面跳转 查询待审核任务
    @RequestMapping(value = "changeSchoolSetView")
    public String changeSchoolSetView(ProjectDeclare projectDeclare, HttpServletRequest request,
                               HttpServletResponse response, Model model) {
        return ProSval.path.vms(ProEmskey.STATE.k()) + "changeSchoolSetView";
    }
}
