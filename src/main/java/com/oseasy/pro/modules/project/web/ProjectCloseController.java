package com.oseasy.pro.modules.project.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.project.entity.ProProgress;
import com.oseasy.pro.modules.project.entity.ProSituation;
import com.oseasy.pro.modules.project.entity.ProjectClose;
import com.oseasy.pro.modules.project.entity.ProjectCloseFund;
import com.oseasy.pro.modules.project.entity.ProjectCloseResult;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.project.service.ProProgressService;
import com.oseasy.pro.modules.project.service.ProSituationService;
import com.oseasy.pro.modules.project.service.ProjectCloseFundService;
import com.oseasy.pro.modules.project.service.ProjectCloseResultService;
import com.oseasy.pro.modules.project.service.ProjectCloseService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.SysStudentExpansionService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * project_closeController
 * @author zhangzheng
 * @version 2017-03-29
 */
@Controller
@RequestMapping(value = "${frontPath}/project/projectClose")
public class ProjectCloseController extends BaseController {

	@Autowired
	private ProjectCloseService projectCloseService;

	@Autowired
	ProjectDeclareService projectDeclareService;

	@Autowired
	SysStudentExpansionService sysStudentExpansionService;

	@Autowired
	TeamService teamService;

	@Autowired
	TeamUserRelationService teamUserRelationService;
	@Autowired
	TeamUserHistoryService teamUserHistoryService;

	@Autowired
	ProjectPlanService projectPlanService;

	@Autowired
	SysAttachmentService sysAttachmentService;

	@Autowired
	ProSituationService proSituationService;

	@Autowired
	ProProgressService proProgressService;

	@Autowired
	ProjectCloseFundService projectCloseFundService;

	@Autowired
	ProjectCloseResultService projectCloseResultService;
//	@Autowired
//	ScoAllotRatioService scoAllotRatioService;

	@ModelAttribute
	public ProjectClose get(@RequestParam(required=false) String id) {
		ProjectClose entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = projectCloseService.get(id);
		}
		if (entity == null) {
			entity = new ProjectClose();
		}
		return entity;
	}
	/**
	 * 创建结项报告报告
	 * 先查询项目相关信息
	 * @param projectId 项目id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="createClose")
	public String createClose(String projectId, Model model) {
		if (StringUtil.isNotBlank(projectId)) {
			ProjectDeclare projectDeclare = projectDeclareService.get(projectId);
			model.addAttribute("projectDeclare",projectDeclare);
			//查找学生拓展信息
			SysStudentExpansion student=  sysStudentExpansionService.getByUserId(projectDeclare.getCreateBy().getId());
			model.addAttribute("student",student);

			//查找项目团队相关信息 projectDeclare.id
			Team team=teamService.get(projectDeclare.getTeamId());
			model.addAttribute("team",team);

			/*//查找学生
			TeamUserRelation tur1=new TeamUserRelation();
			tur1.setTeamId(projectDeclare.getTeamId());
			List<TeamUserRelation> turStudents=teamUserRelationService.getStudents(tur1);
			model.addAttribute("turStudents",turStudents);

			//查找导师
			List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
			model.addAttribute("turTeachers",turTeachers);*/

			//查找学生
			List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
			model.addAttribute("turStudents",turStudents);
			//查找导师
			List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
			model.addAttribute("turTeachers",turTeachers);

			//查找项目分工
			List<ProjectPlan> plans=projectPlanService.findListByProjectId(projectDeclare.getId());
			model.addAttribute("plans",plans);

			//查找学分匹配规则
			ProjectDeclare pro =  projectDeclareService.getScoreConfigure(projectId);
//            if(pro != null){
//                //根据 type（学分类型)、item（学分项）、category（课程、项目、大赛、技能大类）、subdivision（课程、项目、大赛小类）、number(人数)查询后台配比
//                ScoRatioVo scoRatioVo = new ScoRatioVo();
//                if (StringUtil.equals(pro.getType(),"1")||StringUtil.equals(pro.getType(),"2")) { //创新训练、创业训练
//                    scoRatioVo.setType("0000000123"); //设置查询的学分类型（创新学分）
//                }
//                if (StringUtil.equals(pro.getType(),"3")) { //创业实践
//                    scoRatioVo.setType("0000000124"); //设置查询的学分类型（创业学分）
//                }
//                scoRatioVo.setItem("0000000128"); //双创项目
//                scoRatioVo.setCategory("1"); //大学生创新创业训练项目
//                scoRatioVo.setSubdivision(pro.getType());
//                scoRatioVo.setNumber(pro.getSnumber());
//                ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
//                if (ratioResult!=null) {
//                    model.addAttribute("ratio", ratioResult.getRatio());
//                }else{
//                    model.addAttribute("ratio","");
//                }
//            }
		}
		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectCloseForm";
	}

	@RequestMapping(value="edit")
    public String edit(ProjectClose projectClose,Model model) {
		String projectId=projectClose.getProjectId();
		if (StringUtil.isNotBlank(projectId)) {
			ProjectDeclare projectDeclare = projectDeclareService.get(projectId);
			model.addAttribute("projectDeclare",projectDeclare);
			//查找学生拓展信息
			SysStudentExpansion student=  sysStudentExpansionService.getByUserId(projectDeclare.getCreateBy().getId());
			model.addAttribute("student",student);

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

			//查找组成员完成情况
			List<ProSituation> proSituationList=proSituationService.getByFid(projectClose.getId());
			model.addAttribute("proSituationList",proSituationList);

			//查找当前项目进度
			List<ProProgress> proProgressList=proProgressService.getByFid(projectClose.getId());
			model.addAttribute("proProgressList",proProgressList);

			//查找经费使用情况
			List<ProjectCloseFund> projectCloseFundList=projectCloseFundService.getByCloseId(projectClose.getId());
			model.addAttribute("projectCloseFundList",projectCloseFundList);

			//查找成果描述
			List<ProjectCloseResult> projectCloseResultList=projectCloseResultService.getByCloseId(projectClose.getId());
			model.addAttribute("projectCloseResultList",projectCloseResultList);

//			Map<String,String> map=new HashMap<String,String>();
//			map.put("uid",projectClose.getProjectId());
//			map.put("file_step", FileStepEnum.S103.getValue());
//			map.put("type",FileTypeEnum.S0.getValue());
//			List<Map<String,String>> fileListMap=sysAttachmentService.getFileInfo(map);
//			model.addAttribute("fileListMap",fileListMap);

			//查找结项附件
			SysAttachment sa=new SysAttachment();
			sa.setUid(projectClose.getProjectId());
			sa.setFileStep(FileStepEnum.S103);
			sa.setType(FileTypeEnum.S0);
			List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
			model.addAttribute("fileListMap",fileListMap);

		}

		User user= UserUtils.getUser();
		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
			return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectCloseMasterEdit";
		}else{
			return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectCloseForm";
		}

	}


	@RequestMapping(value="view")
	public String view(ProjectClose projectClose, Model model) {
		String projectId=projectClose.getProjectId();
		if (StringUtil.isNotBlank(projectId)) {
			ProjectDeclare projectDeclare = projectDeclareService.get(projectId);
			model.addAttribute("projectDeclare",projectDeclare);
			//查找学生拓展信息
			SysStudentExpansion student=  sysStudentExpansionService.getByUserId(projectDeclare.getCreateBy().getId());
			model.addAttribute("student",student);

			//查找项目团队相关信息 projectDeclare.id
			Team team=teamService.get(projectDeclare.getTeamId());
			model.addAttribute("team",team);

			List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
			model.addAttribute("turStudents",turStudents);
			if (turStudents!=null &&turStudents.size()>0) {
				//组成项目组成员
				StringBuffer stuNames = new StringBuffer("");
				for (Map<String, String> turStudent : turStudents) {
					String name = turStudent.get("name");
					stuNames.append(name + "/");
				}
				String teamList = stuNames.toString().substring(0, stuNames.toString().length() - 1);
				model.addAttribute("teamList", teamList);
			}
			//查找导师
			//List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
			List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
			model.addAttribute("turTeachers",turTeachers);
			//组成项目导师
			if (turTeachers!=null &&turTeachers.size()>0) {
				StringBuffer teaNames = new StringBuffer("");
				for (Map<String, String> turTeacher : turTeachers) {
					String name = turTeacher.get("name");
					teaNames.append(name + "/");
				}
				String teacher = teaNames.toString().substring(0, teaNames.toString().length() - 1);
				model.addAttribute("teacher", teacher);
			}


			//查找项目分工
			List<ProjectPlan> plans=projectPlanService.findListByProjectId(projectDeclare.getId());
			model.addAttribute("plans",plans);

			//查找组成员完成情况
			List<ProSituation> proSituationList=proSituationService.getByFid(projectClose.getId());
			model.addAttribute("proSituationList",proSituationList);

			//查找当前项目进度
			List<ProProgress> proProgressList=proProgressService.getByFid(projectClose.getId());
			model.addAttribute("proProgressList",proProgressList);

			//查找经费使用情况
			List<ProjectCloseFund> projectCloseFundList=projectCloseFundService.getByCloseId(projectClose.getId());
			model.addAttribute("projectCloseFundList",projectCloseFundList);

			//查找成果描述
			List<ProjectCloseResult> projectCloseResultList=projectCloseResultService.getByCloseId(projectClose.getId());
			model.addAttribute("projectCloseResultList",projectCloseResultList);

			//查找结项附件
			SysAttachment sa=new SysAttachment();
			sa.setUid(projectClose.getProjectId());
			sa.setFileStep(FileStepEnum.S103);
			sa.setType(FileTypeEnum.S0);
			List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
			model.addAttribute("fileListMap",fileListMap);

//			//查找学分匹配规则
//			ProjectDeclare pro =  projectDeclareService.getScoreConfigure(projectId);
//	        if(pro != null){
//    			//根据 type（学分类型)、item（学分项）、category（课程、项目、大赛、技能大类）、subdivision（课程、项目、大赛小类）、number(人数)查询后台配比
//    			ScoRatioVo scoRatioVo = new ScoRatioVo();
//    			if (StringUtil.equals(pro.getType(),"1")||StringUtil.equals(pro.getType(),"2")) { //创新训练、创业训练
//    				scoRatioVo.setType("0000000123"); //设置查询的学分类型（创新学分）
//    			}
//    			if (StringUtil.equals(pro.getType(),"3")) { //创业实践
//    				scoRatioVo.setType("0000000124"); //设置查询的学分类型（创业学分）
//    			}
//    			scoRatioVo.setItem("0000000128"); //双创项目
//    			scoRatioVo.setCategory("1"); //大学生创新创业训练项目
//    			scoRatioVo.setSubdivision(pro.getType());
//    			scoRatioVo.setNumber(pro.getSnumber());
//    			ScoRatioVo ratioResult = scoAllotRatioService.findRatio(scoRatioVo);
//    			if (ratioResult!=null) {
//    				model.addAttribute("ratio", ratioResult.getRatio());
//    			}else{
//    				model.addAttribute("ratio","");
//    			}
//            }
		}
		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "projectCloseView";
	}
	/**
	 * 保存结项报告（未实现），触发工作流（已完成）
	 *
	 */
	@RequestMapping(value = "save")
	public String save(ProjectClose projectClose,
					   	Model model,
					   HttpServletRequest request,
					   	RedirectAttributes redirectAttributes) {
		projectCloseService.save(projectClose);
//		//附件处理
//		String[] arrUrl= request.getParameterValues("arrUrl");
//		String[] arrNames= request.getParameterValues("arrName");
//		List<Map<String,String>> fileListMap = FileUpUtils.getFileListMap(arrUrl,arrNames);
//		sysAttachmentService.saveList(fileListMap,
//										FileTypeEnum.S0,
//										FileStepEnum.S103,
//										projectClose.getProjectId());
		//附件处理
		sysAttachmentService.saveByVo(projectClose.getAttachMentEntity(),projectClose.getProjectId(),FileTypeEnum.S0,FileStepEnum.S103);

		//更新学分配比权重
		for (TeamUserHistory tur:projectClose.getTeamUserHistoryList()) {
			teamUserHistoryService.updateWeight(tur);
		}



		if (StringUtil.isNotBlank(projectClose.getProjectId())) {
			ProjectDeclare projectDeclare = projectDeclareService.get(projectClose.getProjectId());
			//判断当前状态是不是待提交结项报告
			if (StringUtil.equals(projectDeclare.getStatus(), "6")) {
				projectDeclareService.closeSave(projectDeclare);
			}
		}

		return "redirect:/f/project/projectDeclare/curProject";
	}

	@RequestMapping(value = "saveMaster")
	public String saveMaster(ProjectClose projectClose, Model model,
							 HttpServletRequest request) {
		projectClose.setSuggestDate(new Date());
		projectCloseService.saveSuggest(projectClose);
		return "redirect:/f/project/projectDeclare/curProject";
	}


	@RequestMapping(value = "delete")
	public String delete(ProjectClose projectClose, RedirectAttributes redirectAttributes) {
		projectCloseService.delete(projectClose);
		addMessage(redirectAttributes, "删除project_close成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/project/projectClose/?repage";
	}

}