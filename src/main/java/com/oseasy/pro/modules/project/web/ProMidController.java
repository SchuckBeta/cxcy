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
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.oseasy.pro.modules.project.entity.ProMid;
import com.oseasy.pro.modules.project.entity.ProProgress;
import com.oseasy.pro.modules.project.entity.ProSituation;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.project.service.ProMidService;
import com.oseasy.pro.modules.project.service.ProProgressService;
import com.oseasy.pro.modules.project.service.ProSituationService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
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

import net.sf.json.JSONObject;

/**
 * 国创项目中期检查表单Controller
 * @author 9527
 * @version 2017-03-29
 */
@Controller
@RequestMapping(value = "${frontPath}/project/proMid")
public class ProMidController extends BaseController {

	@Autowired
	private ProMidService proMidService;

	@Autowired
	ProjectDeclareService projectDeclareService;

	@Autowired
	private ProModelService proModelService;

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

//	@Autowired
//	ScoAllotRatioService scoAllotRatioService;

	@ModelAttribute
	public ProMid get(@RequestParam(required=false) String id) {
		ProMid entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = proMidService.get(id);
		}
		if (entity == null) {
			entity = new ProMid();
		}
		return entity;
	}

	/**
	 * 创建中期检查报告
	 * 先查询项目相关信息
	 * @param projectId 项目id
	 * @param model
	 * @return
     */
	@RequestMapping(value="creatMid")
	public String creatMid(String projectId, Model model) {
		if (StringUtil.isNotBlank(projectId)) {
			ProjectDeclare projectDeclare = projectDeclareService.get(projectId);
			model.addAttribute("projectDeclare",projectDeclare);
			//查找学生拓展信息
			SysStudentExpansion student=  sysStudentExpansionService.getByUserId(projectDeclare.getCreateBy().getId());
			model.addAttribute("student",student);

			//查找项目团队相关信息 projectDeclare.id
			Team team=teamService.get(projectDeclare.getTeamId());
			model.addAttribute("team",team);

			//查找学生
		/*	TeamUserRelation tur1=new TeamUserRelation();
			tur1.setTeamId(projectDeclare.getTeamId());
			List<TeamUserRelation> turStudents=teamUserRelationService.getStudents(tur1);*/

			List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());

			model.addAttribute("turStudents",turStudents);
			if (turStudents!=null &&turStudents.size()>0) {
				//组成项目组成员
				StringBuffer stuNames=new StringBuffer("");
				for(Map<String,String> turStudent:turStudents) {
					String name=turStudent.get("name");
					stuNames.append(name+"/");
				}
				String teamList=stuNames.toString().substring(0,stuNames.toString().length()-1);
				model.addAttribute("teamList",teamList);
			}
			//查找导师
			List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());

			//List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
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

//			//查找学分匹配规则
//			ProjectDeclare pro =  projectDeclareService.getScoreConfigure(projectId);
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
		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "proMidForm";
	}

	@RequestMapping(value="edit")
	public String edit(ProMid proMid,Model model) {
		String projectId=proMid.getProjectId();
		if (StringUtil.isNotBlank(projectId)) {
			ProjectDeclare projectDeclare = projectDeclareService.get(projectId);
			model.addAttribute("projectDeclare",projectDeclare);
			//查找学生拓展信息
			SysStudentExpansion student=  sysStudentExpansionService.getByUserId(projectDeclare.getCreateBy().getId());
			model.addAttribute("student",student);

			//查找项目团队相关信息 projectDeclare.id
			Team team=teamService.get(projectDeclare.getTeamId());
			model.addAttribute("team",team);

			//查找学生
		/*	TeamUserRelation tur1=new TeamUserRelation();
			tur1.setTeamId(projectDeclare.getTeamId());
			List<TeamUserRelation> turStudents=teamUserRelationService.getStudents(tur1);*/
			List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
			//组成项目组成员
			if (turStudents!=null &&turStudents.size()>0) {
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
			List<ProSituation> proSituationList=proSituationService.getByFid(proMid.getId());
			model.addAttribute("proSituationList",proSituationList);

			//查找当前项目进度
			List<ProProgress> proProgressList=proProgressService.getByFid(proMid.getId());
			model.addAttribute("proProgressList",proProgressList);

			//查找中期附件
			/*Map<String,String> map=new HashMap<String,String>();
			map.put("uid",proMid.getProjectId());
			map.put("file_step", FileStepEnum.S102.getValue());
			map.put("type",FileTypeEnum.S0.getValue());
			List<Map<String,String>> fileListMap=sysAttachmentService.getFileInfo(map);
			model.addAttribute("fileListMap",fileListMap);*/

			//查找中期附件
			SysAttachment sa=new SysAttachment();
			sa.setUid(proMid.getProjectId());
			sa.setFileStep(FileStepEnum.S102);
			sa.setType(FileTypeEnum.S0);
			List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
			model.addAttribute("fileListMap",fileListMap);



		}

		User user= UserUtils.getUser();
		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
			return ProSval.path.vms(ProEmskey.PROJECT.k()) + "proMidMasterEdit";
		}else{
			return ProSval.path.vms(ProEmskey.PROJECT.k()) + "proMidForm";
		}

	}

	@RequestMapping(value="view")
	public String view(ProMid proMid,Model model) {
		String projectId=proMid.getProjectId();
		if (StringUtil.isNotBlank(projectId)) {
			ProjectDeclare projectDeclare = projectDeclareService.get(projectId);
			model.addAttribute("projectDeclare",projectDeclare);
			//查找学生拓展信息
			SysStudentExpansion student=  sysStudentExpansionService.getByUserId(projectDeclare.getCreateBy().getId());
			model.addAttribute("student",student);

			//查找项目团队相关信息 projectDeclare.id
			Team team=teamService.get(projectDeclare.getTeamId());
			model.addAttribute("team",team);

			//查找学生
			List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
			model.addAttribute("turStudents",turStudents);
			//组成项目组成员
			StringBuffer stuNames=new StringBuffer("");
			for(Map<String,String> turStudent:turStudents) {
				String name=turStudent.get("name");
				stuNames.append(name+"/");
			}
			String teamList=stuNames.toString().substring(0,stuNames.toString().length()-1);
			model.addAttribute("teamList",teamList);
			//查找导师
			List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(projectDeclare.getTeamId(),projectDeclare.getId());
			model.addAttribute("turTeachers",turTeachers);
			//组成项目导师
			StringBuffer teaNames=new StringBuffer("");
			for (Map<String,String> turTeacher:turTeachers) {
				String name=turTeacher.get("name");
				teaNames.append(name+"/");
			}
			String teacher=teaNames.toString().substring(0,teaNames.toString().length()-1);
			model.addAttribute("teacher",teacher);

			//查找项目分工
			List<ProjectPlan> plans=projectPlanService.findListByProjectId(projectDeclare.getId());
			model.addAttribute("plans",plans);

			//查找组成员完成情况
			List<ProSituation> proSituationList=proSituationService.getByFid(proMid.getId());
			model.addAttribute("proSituationList",proSituationList);

			//查找当前项目进度
			List<ProProgress> proProgressList=proProgressService.getByFid(proMid.getId());
			model.addAttribute("proProgressList",proProgressList);

			//查找中期附件
//			Map<String,String> map=new HashMap<String,String>();
//			map.put("uid",proMid.getProjectId());
//			map.put("file_step", FileStepEnum.S102.getValue());
//			map.put("type",FileTypeEnum.S0.getValue());
//			List<Map<String,String>> fileListMap=sysAttachmentService.getFileInfo(map);
//			model.addAttribute("fileListMap",fileListMap);

			//查找中期附件
			SysAttachment sa=new SysAttachment();
			sa.setUid(proMid.getProjectId());
			sa.setFileStep(FileStepEnum.S102);
			sa.setType(FileTypeEnum.S0);
			List<SysAttachment> fileListMap =  sysAttachmentService.getFiles(sa);
			model.addAttribute("fileListMap",fileListMap);

		}

		return ProSval.path.vms(ProEmskey.PROJECT.k()) + "proMidView";
	}



	@RequestMapping(value = "submitMid")
	@ResponseBody
	public JSONObject submitMid(ProMid proMid, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);

		proMidService.save(proMid);
		//附件处理
		sysAttachmentService.saveByVo(proMid.getAttachMentEntity(),proMid.getProjectId(),FileTypeEnum.S0,FileStepEnum.S102);

		ProModel proModel=proModelService.get(proMid.getProjectId());
		String msg=proModelService.submitMid(proModel);
		js.put("msg", msg);
		return js;
	}

	@RequestMapping(value = "submitClose")
	@ResponseBody
	public JSONObject submitClose(ProMid proMid, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);

		proMidService.save(proMid);
		//附件处理
		sysAttachmentService.saveByVo(proMid.getAttachMentEntity(),proMid.getProjectId(),FileTypeEnum.S0,FileStepEnum.S103);

		ProModel proModel=proModelService.get(proMid.getProjectId());
		String msg=proModelService.submitClose(proModel);
		js.put("msg", msg);
		return js;
	}

	/**
	 * 保存中期检查报告（未实现），触发工作流（已完成）
     *
     */
	@RequestMapping(value = "save")
	public String save(ProMid proMid, Model model,
					   HttpServletRequest request,
					   RedirectAttributes redirectAttributes) {
		proMidService.save(proMid);
//		//附件处理
//		String[] arrUrl= request.getParameterValues("arrUrl");
//		String[] arrNames= request.getParameterValues("arrName");
//		List<Map<String,String>> fileListMap =FileUpUtils.getFileListMap(arrUrl,arrNames);
//		sysAttachmentService.saveList(fileListMap,
//				                      FileTypeEnum.S0,
//									  FileStepEnum.S102,
//				  			          proMid.getProjectId());
		//附件处理
		sysAttachmentService.saveByVo(proMid.getAttachMentEntity(),proMid.getProjectId(),FileTypeEnum.S0,FileStepEnum.S102);


		//更新学分配比权重
		for (TeamUserHistory tur:proMid.getTeamUserHistoryList()) {
			teamUserHistoryService.updateWeight(tur);
		}


		if (StringUtil.isNotBlank(proMid.getProjectId())) {
			ProjectDeclare projectDeclare = projectDeclareService.get(proMid.getProjectId());
			//判断状态是不是待提交中期报告或者是修改中期报告
			if (StringUtil.equals(projectDeclare.getStatus(), "3")
				||StringUtil.equals(projectDeclare.getStatus(), "4")	) {
				projectDeclareService.midSave(projectDeclare);}
		}
		return "redirect:/f/project/projectDeclare/curProject";
	}

	@RequestMapping(value = "saveMaster")
	public String saveMaster(ProMid proMid, Model model,
							 HttpServletRequest request) {
		proMid.setTutorSuggestDate(new Date());
		proMidService.saveSuggest(proMid);
		return "redirect:/f/project/projectDeclare/curProject";
	}






	@RequestMapping(value = "delete")
	public String delete(ProMid proMid, RedirectAttributes redirectAttributes) {
		proMidService.delete(proMid);
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/project/proMid/?repage";
	}

}