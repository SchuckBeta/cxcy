package com.oseasy.pro.modules.state.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.project.service.ProjectAuditInfoService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.service.SysStudentExpansionService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by zhangzheng on 2017/3/27.
 */
@Controller
@RequestMapping(value = "${adminPath}/projectBase")
public class ProjectBaseController extends BaseController {
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
    SysAttachmentService sysAttachmentService;

    @Autowired
    SysStudentExpansionService sysStudentExpansionService;

    @RequestMapping(value="baseInfo")
    public String baseInfo( Model model, HttpServletRequest request) {

        String id=request.getParameter("id");
        ProjectDeclare projectDeclare = projectDeclareService.get(id);
        model.addAttribute("projectDeclare",projectDeclare);

        //查找学生拓展信息
        SysStudentExpansion student=  sysStudentExpansionService.getByUserId(projectDeclare.getCreateBy().getId());
        model.addAttribute("student",student);

        //查找项目团队相关信息 projectDeclare.id
        Team team=teamService.get(projectDeclare.getTeamId());
        model.addAttribute("team",team);

        //查找学生
       /* TeamUserRelation tur1=new TeamUserRelation();
        tur1.setTeamId(projectDeclare.getTeamId());
        List<TeamUserRelation> turStudents=teamUserRelationService.getStudents(tur1);
        model.addAttribute("turStudents",turStudents);
        //查找导师
        List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
        model.addAttribute("turTeachers",turTeachers);*/

        List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
        model.addAttribute("turStudents",turStudents);
        //查找导师
        List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
        model.addAttribute("turTeachers",turTeachers);


        //查找项目分工
        List<ProjectPlan> plans=projectPlanService.findListByProjectId(projectDeclare.getId());
        model.addAttribute("plans",plans);

        //查找项目附件
        Map<String,String> map=new HashMap<String,String>();
        map.put("uid", projectDeclare.getId());
        map.put("file_step", FileStepEnum.S100.getValue());
        map.put("type", FileTypeEnum.S0.getValue());
        List<Map<String,String>> fileInfo = sysAttachmentService.getFileInfo(map);
        model.addAttribute("fileInfo",fileInfo);

        return ProSval.path.vms(ProEmskey.STATE.k()) + "baseInfo";
    }

    @ResponseBody
    @RequestMapping(value="getDownloadUrl")
    public String getDownloadUrl(HttpServletRequest request) {
        String id=request.getParameter("id");
        String flag=request.getParameter("flag");
        Map<String,String> map=new HashMap<String,String>();
        map.put("uid", id);
        if (StringUtil.equals("1",flag)) {
            map.put("file_step", FileStepEnum.S100.getValue());
        }
        if (StringUtil.equals("2",flag)) {
            map.put("file_step", FileStepEnum.S102.getValue());
        }
        if (StringUtil.equals("3",flag)) {
            map.put("file_step", FileStepEnum.S103.getValue());
        }

        map.put("type", FileTypeEnum.S0.getValue());
        List<Map<String,String>> fileInfo = sysAttachmentService.getFileInfo(map);
        Map<String,String>  file=fileInfo.get(0);
        System.out.println(file.get("arrUrl"));
        String url=file.get("arrUrl");
        return url;
    }


}
