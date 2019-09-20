package com.oseasy.pro.modules.gcontest.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandardDetailIns;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardDetailInsService;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardDetailService;
import com.oseasy.pro.modules.gcontest.entity.GAuditInfo;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.entity.GContestAward;
import com.oseasy.pro.modules.gcontest.service.GAuditInfoService;
import com.oseasy.pro.modules.gcontest.service.GContestAwardService;
import com.oseasy.pro.modules.gcontest.service.GContestService;
import com.oseasy.pro.modules.gcontest.vo.GContestNodeVo;
import com.oseasy.pro.modules.gcontest.vo.GContestVo;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectStandardDetailVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.SysStudentExpansionService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 大赛信息Controller
 * @author zy
 * @version 2017-03-11
 */
@Controller
@RequestMapping(value = "${adminPath}/gcontest/gContest")
public class GContestController extends BaseController {

	@Autowired
	private TeamService teamService;
	@Autowired
	private ProjectDeclareService projectDeclareService;
	@Autowired
	private ProModelService proModelService;
	@Autowired
	private UserService userService;
	@Autowired
	private SysAttachmentService sysAttachmentService;
	@Autowired
	private GContestService gContestService;
	@Autowired
	private SysStudentExpansionService sysStudentExpansionService;
	@Autowired
	GContestAwardService gContestAwardService;
	@Autowired
	private GAuditInfoService gAuditInfoService;
	@Autowired
	private FtpService ftpService;
	@Autowired
 	private CommonService commonService;
	@Autowired
	private AuditStandardDetailService auditStandardDetailService;
	@Autowired
	private AuditStandardDetailInsService auditStandardDetailInsService;

	@ModelAttribute
	public GContest get(@RequestParam(required=false) String id) {
		GContest entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = gContestService.get(id);
		}
		if (entity == null) {
			entity = new GContest();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list(GContest gContest, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GContest> page = gContestService.findPage(new Page<GContest>(request, response), gContest);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestList";
	}

	@RequestMapping(value = "form")
	public String form(GContest gContest, Model model) {
		User user = UserUtils.getUser();
		if (StringUtil.isNotBlank(gContest.getId())) {
			gContest = gContestService.get(gContest.getId());
			user=userService.findUserById(gContest.getDeclareId());
			SysStudentExpansion sse = new SysStudentExpansion();
			sse.setName(user.getName());
			sse.setEmail(user.getEmail());
			sse.setMobile(user.getMobile());
			sse.setCompany(user.getCompany());
			sse.setOffice(user.getOffice());
			sse.setProfessional(user.getProfessional());
			model.addAttribute("sse", sse);
			model.addAttribute("gContest", gContest);
			model.addAttribute("competitionNumber", gContest.getCompetitionNumber());
			if (gContest.getAuditState().equals("2")) {
				model.addAttribute("isHave", true);
			}
		}else {
			SysStudentExpansion sse = new SysStudentExpansion();
			sse.setName(user.getName());
			sse.setEmail(user.getEmail());
			sse.setMobile(user.getMobile());
			sse.setCompany(user.getCompany());
			sse.setOffice(user.getOffice());
			sse.setProfessional(user.getProfessional());
			sse.setNo(user.getNo());
			gContest.setDeclareId(user.getId());
			model.addAttribute("gContest", gContest);
			model.addAttribute("sse", sse);
			model.addAttribute("isHave", true);
		}
		//model.addAttribute("gContest", gContest);
		//关联团队
		List<Team> teams=	projectDeclareService.findTeams(user.getId(),gContest.getTeamId());
		model.addAttribute("sysdate", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
		model.addAttribute("teams", teams);
		//关联项目
		ProjectDeclare projectDeclare = new ProjectDeclare();
		projectDeclare.setLeader(user.getId());
		List<ProjectDeclare> projects=	projectDeclareService.findList(projectDeclare);
		model.addAttribute("projects", projects);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestForm";
	}

	//大赛变更
	@RequestMapping(value = "changeFrom")
	@ResponseBody
	public JSONObject changeFrom(GContest gContest,HttpServletRequest request, HttpServletResponse response,Model model) {
		JSONObject js=new JSONObject();

		String changeData=request.getParameter("changeData");
		JSONObject myJSONObject =JSONObject.fromObject(changeData);

		if (myJSONObject.size()>0) {
			gContestService.udpate(gContest);
			String state=gContest.getAuditState();
			if (state.equals("1")) {
				List<String> collegeExports=new ArrayList<String>() ;
				collegeExports=userService.getCollegeExperts(gContest.getCreateBy().getId());
				if (collegeExports.size()>0) {
					js=gContestService.updateFirst(myJSONObject,gContest);
				}else{
					js.put("ret", 0);
					js.put("msg", "学院专家不存在，变更失败!");
					return js;
				}
			}
			if (state.equals("2")) {
				List<String> collegeSecs=new ArrayList<String>() ;
				collegeSecs=userService.getCollegeSecs(gContest.getCreateBy().getId());
				if (collegeSecs.size()>0) {
					js=gContestService.updateSecond(myJSONObject,gContest);
				}else{
					js.put("ret", 0);
					js.put("msg", "学院秘书不存在，变更失败!");
					return js;
				}

			}
			if (state.equals("3")) {
				js=gContestService.updateThree(myJSONObject,gContest);
			}
			if (state.equals("4")) {
				js=gContestService.updateFour(myJSONObject,gContest);
			}
			if (state.equals("5")) {
				js=gContestService.updateFive(myJSONObject,gContest);
			}

			if (state.equals("6")) {
				js=gContestService.updateSix(myJSONObject,gContest);
			}

			if (state.equals("7")) {
				js=gContestService.updateSeven(myJSONObject,gContest);
			}

			if (state.equals("8")) {
				js=gContestService.updateSchoolFail(myJSONObject,gContest);
			}

			if (state.equals("9")) {
				js=gContestService.updateCollegeFail(myJSONObject,gContest);
			}

			String[] arrUrl= request.getParameterValues("arrUrl");
			String[] arrNames= request.getParameterValues("arrName");
			if (arrUrl!=null&&arrUrl.length>0) {
				for(int i=0;i<arrUrl.length;i++) {
					 try {
						String moveEnd=FtpUtil.moveFile(   arrUrl[i]);
						arrUrl[i]=moveEnd;
					} catch (IOException e) {
						logger.debug("大赛变更上传附件失败");
						e.printStackTrace();
						js.put("ret", 0);
						js.put("msg", "大赛变更上传附件失败");
						return js;
					}
					SysAttachment sysAttachment=new SysAttachment();
					sysAttachment.setUid(gContest.getId());
					sysAttachment.setType(FileTypeEnum.S2);
					sysAttachment.setName(arrNames[i]);
					sysAttachment.setUrl(arrUrl[i]);
					sysAttachment.setSuffix(arrNames[i].substring(arrNames[i].lastIndexOf(".")+1));
					sysAttachmentService.save(sysAttachment);
				}
			}
		}else{
			String[] arrUrl= request.getParameterValues("arrUrl");
			String[] arrNames= request.getParameterValues("arrName");
			if (arrUrl!=null&&arrUrl.length>0) {
				for(int i=0;i<arrUrl.length;i++) {
					 FtpUtil t = new FtpUtil();
					 try {
						String moveEnd=t.moveFile(   arrUrl[i]);
						arrUrl[i]=moveEnd;
					} catch (IOException e) {
						logger.debug("大赛变更上传附件失败");
						e.printStackTrace();
						js.put("ret", 0);
						js.put("msg", "大赛变更上传附件失败");
						return js;
					}
					SysAttachment sysAttachment=new SysAttachment();
					sysAttachment.setUid(gContest.getId());
					sysAttachment.setType(FileTypeEnum.S2);
					sysAttachment.setName(arrNames[i]);
					sysAttachment.setUrl(arrUrl[i]);
					sysAttachment.setSuffix(arrNames[i].substring(arrNames[i].lastIndexOf(".")+1));
					sysAttachmentService.save(sysAttachment);
				}
			}
			List<TeamUserHistory> stus = gContest.getStudentList();
			List<TeamUserHistory> teas = gContest.getTeacherList();
			String actywId = gContest.getActywId();
			String teamId = gContest.getTeamId();
			String proId = gContest.getId();
			gContestService.udpate(gContest);
			//先保存大赛，参加多个项目或大赛  团队值会发生变化
			commonService.disposeTeamUserHistoryForModify(stus, teas, actywId, teamId, proId);

			js.put("ret", 1);
			js.put("msg", "大赛变更成功!");
		}
		return js;
	}



	@RequestMapping(value = "delete")
	public String delete(GContest gContest, RedirectAttributes redirectAttributes) {
		gContestService.delete(gContest);
		addMessage(redirectAttributes, "删除大赛信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/?repage";
	}

	//大赛打分审核
	@RequestMapping(value = "auditView")
	public String  auditView(GContest gContest, HttpServletRequest request, Model model) {
		//查找大赛提交的表单  供评分老师查看
		String gcontestId=request.getParameter("gcontestId");
		gContest=gContestService.get(gcontestId);
		User declareUser=userService.findUserById(gContest.getDeclareId());
		SysStudentExpansion sse = new SysStudentExpansion();
		sse.setName(declareUser.getName());
		sse.setEmail(declareUser.getEmail());
		sse.setMobile(declareUser.getMobile());
		sse.setCompany(declareUser.getCompany());
		sse.setOffice(declareUser.getOffice());
		sse.setNo(declareUser.getNo());
		sse.setProfessional(declareUser.getProfessional());

		Map<String,String> map=new HashMap<String,String>();
		map.put("uid", gContest.getId());
		map.put("type",FileTypeEnum.S2.getValue());
		List<Map<String, String>>   sysAttachments=sysAttachmentService.getFileInfo(map);
		model.addAttribute("sysAttachments", sysAttachments);

        //查找项目团队相关信息 projectDeclare.id
        Team team=teamService.get(gContest.getTeamId());
        model.addAttribute("team",team);
        //查找学生
        TeamUserRelation tur1=new TeamUserRelation();
        tur1.setTeamId(gContest.getTeamId());
        //List<TeamUserRelation> turStudents=teamUserRelationService.getStudents(tur1);
		List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(),gContest.getId());
        model.addAttribute("turStudents",turStudents);
        //查找导师
        //List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
		List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(),gContest.getId());
        model.addAttribute("turTeachers",turTeachers);

        //审核意见
        if (gContest.getAuditState().equals("1")) {
        	//学院老师评分
			List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
     		model.addAttribute("infos", collegeinfos);
			//查找审核标准
			List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
			if (standardList!=null&&standardList.size()>0) {
				model.addAttribute("asList",standardList);
				String isScore=standardList.get(0).getIsEescoreNodes();
				String firstNode=GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_FIRST_ID);
				if (isScore!=null) {
					if (isScore.contains(firstNode)) {
						model.addAttribute("isScore", firstNode);
					}
				}
			}
			/*AuditStandardFlow entity = auditStandardFlowService.getByDef("audit1");
			if (entity!=null) {
				model.addAttribute("auditStandard", auditStandardService.get(entity.getAuditStandardId()));
				List<AuditStandardDetail> asList=auditStandardDetailService.findByFid(entity.getAuditStandardId());
				model.addAttribute("asList", asList);
			}*/

        }else if (gContest.getAuditState().equals("2")) {
        	List<GAuditInfo> infos= getInfo(gContest.getId(),"1");
			model.addAttribute("infos", infos);
			List<GAuditInfo> sortList= getCollegeSortByAudit(declareUser.getOffice().getId(),gContest.getId(),"1");
			model.addAttribute("sortCollegeList", sortList);
			//查找审核标准
			List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
			if (standardList!=null&&standardList.size()>0) {
				model.addAttribute("asList",standardList);
				String isScore=standardList.get(0).getIsEescoreNodes();
				String firstNode=GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_SECOND_ID);
				if (isScore!=null) {
					if (isScore.contains(firstNode)) {
						model.addAttribute("isScore", firstNode);
					}
				}
			}
        }else if (gContest.getAuditState().equals("3")) {
          	//List<GAuditInfo> infos= getInfo(gContest.getId(),"2");
          	List<GAuditInfo> infos= getSortInfo(gContest.getId(),"2");
     		model.addAttribute("infos", infos);
			//查找审核标准
			List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
			if (standardList!=null&&standardList.size()>0) {
				model.addAttribute("asList",standardList);
				String isScore=standardList.get(0).getIsEescoreNodes();
				String firstNode=GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_THREE_ID);
				if (isScore!=null) {
					if (isScore.contains(firstNode)) {
						model.addAttribute("isScore", firstNode);
					}
				}
			}
        }else if (gContest.getAuditState().equals("4")) {
        	List<GAuditInfo> collegeInfos= getSortInfo(gContest.getId(),"2");
        	model.addAttribute("collegeInfos", collegeInfos);
         	List<GAuditInfo> infos= getInfo(gContest.getId(),"3");
    		model.addAttribute("infos", infos);

			List<GAuditInfo> sortList= getSortByAudit(gContest.getId(),"3");
			model.addAttribute("sortSchoolList", sortList);
			//audit4
			//查找审核标准
			List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
			if (standardList!=null&&standardList.size()>0) {
				model.addAttribute("asList",standardList);
				String isScore=standardList.get(0).getIsEescoreNodes();
				String firstNode=GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_FOUR_ID);
				if (isScore!=null) {
					if (isScore.contains(firstNode)) {
						model.addAttribute("isScore", firstNode);
					}
				}
			}
       	}else if (gContest.getAuditState().equals("5")) {
       	 	List<GAuditInfo> collegeinfos= getSortInfo(gContest.getId(),"2");
       	 	List<GAuditInfo> schoolinfos= getSortInfo(gContest.getId(),"4");
       	 	collegeinfos.addAll(schoolinfos);
    		model.addAttribute("infos", collegeinfos);
			//audit5
			//查找审核标准
			List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_LY_ID),GContestNodeVo.YW_ID);
			if (standardList!=null&&standardList.size()>0) {
				model.addAttribute("asList",standardList);
				String isScore=standardList.get(0).getIsEescoreNodes();
				String firstNode=GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_FIVE_ID);
				if (isScore!=null) {
					if (isScore.contains(firstNode)) {
						model.addAttribute("isScore", firstNode);
					}
				}
			}
		}else if (gContest.getAuditState().equals("6")) {
      	 	List<GAuditInfo> wpinfos= getSortInfo(gContest.getId(),"4");
      	 	List<GAuditInfo> lyinfos= getSortInfo(gContest.getId(),"5");
      	 	wpinfos.addAll(lyinfos);
      	 	model.addAttribute("infos", wpinfos);
			//audit6
			//查找审核标准
			List<ProjectStandardDetailVo> standardList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_PJ_ID),GContestNodeVo.YW_ID);
			if (standardList!=null&&standardList.size()>0) {
				model.addAttribute("asList",standardList);
				String isScore=standardList.get(0).getIsEescoreNodes();
				String firstNode=GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_SIX_ID);
				if (isScore!=null) {
					if (isScore.contains(firstNode)) {
						model.addAttribute("isScore", firstNode);
					}
				}
			}
       	}
		model.addAttribute("gContest", gContest);
		if (StringUtil.isNotEmpty(gContest.getpId())) {
			ProjectDeclare pd=projectDeclareService.get(gContest.getpId());
			if (pd!=null) {
				model.addAttribute("relationProject", pd.getName());
			}else{
				ProModel pm=proModelService.get(gContest.getpId());
				model.addAttribute("relationProject", pm.getpName());
			}
		}
		model.addAttribute("sse", sse);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestAuditForm";
	}

	//大赛查看
	@RequestMapping(value = "auditedView")
	public String  audited(GContest gContest, HttpServletRequest request, Model model) {
		//查找大赛提交的表单  供评分老师查看
		String gcontestId=request.getParameter("gcontestId");
		String state=request.getParameter("state");
		model.addAttribute("state", state);
		gContest=gContestService.get(gcontestId);
		User declareUser=userService.findUserById(gContest.getDeclareId());
		SysStudentExpansion sse = new SysStudentExpansion();
		sse.setName(declareUser.getName());
		sse.setEmail(declareUser.getEmail());
		sse.setMobile(declareUser.getMobile());
		sse.setCompany(declareUser.getCompany());
		sse.setOffice(declareUser.getOffice());
		sse.setNo(declareUser.getNo());
		sse.setProfessional(declareUser.getProfessional());
		//sse.setGraduation(declareUser.get);
//			sse.setZhuanyeAndendDate("电子信息2016");
		//附件
		SysAttachment sysAttachment=new SysAttachment();
		sysAttachment.setUid(gContest.getId());
		List<SysAttachment> sysAttachments=sysAttachmentService.findList(sysAttachment);
		model.addAttribute("sysAttachments", sysAttachments);

        //查找项目团队相关信息 projectDeclare.id
        Team team=teamService.get(gContest.getTeamId());
        model.addAttribute("team",team);
        //查找学生
        //List<TeamUserRelation> turStudents=teamUserRelationService.getStudents(tur1);
		List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(),gContest.getId());
        model.addAttribute("turStudents",turStudents);
        //查找导师
        //List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
		List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(),gContest.getId());
        model.addAttribute("turTeachers",turTeachers);


		User loginUser = UserUtils.getUser();
        //审核意见根据状态得到不同的审核结果
        if (state.equals("1")) {
        	//学院老师评分
			List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
     		model.addAttribute("infos", collegeinfos);
			GAuditInfo userGai= getInfoByUserId(loginUser.getId(),gContest.getId(),"1");
			if (userGai!=null) {
				//完成评分标准
				model.addAttribute("userGai", userGai);
				AuditStandardDetailIns auditStandardDetailIns=new AuditStandardDetailIns();
				auditStandardDetailIns.setAuditInfoId(userGai.getId());
				List<AuditStandardDetailIns> asdiList= auditStandardDetailInsService.findList(auditStandardDetailIns);
				if (asdiList!=null) {
					model.addAttribute("asdiList", asdiList);
				}
			}
        }else if (state.equals("2")) {
        	List<GAuditInfo> infos= getInfo(gContest.getId(),"1");
     		model.addAttribute("infos", infos);
			GAuditInfo userGai= getInfoByUserId(loginUser.getId(),gContest.getId(),"2");
			if (userGai!=null) {
				//完成评分标准
				model.addAttribute("userGai", userGai);
				AuditStandardDetailIns auditStandardDetailIns=new AuditStandardDetailIns();
				auditStandardDetailIns.setAuditInfoId(userGai.getId());
				List<AuditStandardDetailIns> asdiList= auditStandardDetailInsService.findList(auditStandardDetailIns);
				if (asdiList!=null) {
					model.addAttribute("asdiList", asdiList);
				}
			}
        }else if (state.equals("3")) {
          	//List<GAuditInfo> infos= getInfo(gContest.getId(),"2");
        /*	List<GAuditInfo> collegeExportinfos= getInfo(gContest.getId(),"1");
     		model.addAttribute("collegeExportinfos", collegeExportinfos);*/
			GAuditInfo userGai= getInfoByUserId(loginUser.getId(),gContest.getId(),"3");
			if (userGai!=null) {
				//完成评分标准
				model.addAttribute("userGai", userGai);
				AuditStandardDetailIns auditStandardDetailIns=new AuditStandardDetailIns();
				auditStandardDetailIns.setAuditInfoId(userGai.getId());
				List<AuditStandardDetailIns> asdiList= auditStandardDetailInsService.findList(auditStandardDetailIns);
				if (asdiList!=null) {
					model.addAttribute("asdiList", asdiList);
				}
			}
          	List<GAuditInfo> infos= getSortInfo(gContest.getId(),"2");
     		model.addAttribute("infos", infos);
     		List<GAuditInfo> schoolExportinfos= getInfo(gContest.getId(),"3");
    		model.addAttribute("schoolExportinfos", schoolExportinfos);
        }else if (state.equals("4")) {
        	List<GAuditInfo> collegeExportinfos= getInfo(gContest.getId(),"1");
     		model.addAttribute("collegeExportinfos", collegeExportinfos);
        	List<GAuditInfo> collegeInfos= getSortInfo(gContest.getId(),"2");
        	model.addAttribute("collegeInfo", collegeInfos.get(0));
         	List<GAuditInfo> infos= getInfo(gContest.getId(),"3");
    		model.addAttribute("infos", infos);
			GAuditInfo userGai= getInfoByUserId(loginUser.getId(),gContest.getId(),"4");
			if (userGai!=null) {
				//完成评分标准
				model.addAttribute("userGai", userGai);
				AuditStandardDetailIns auditStandardDetailIns=new AuditStandardDetailIns();
				auditStandardDetailIns.setAuditInfoId(userGai.getId());
				List<AuditStandardDetailIns> asdiList= auditStandardDetailInsService.findList(auditStandardDetailIns);
				if (asdiList!=null) {
					model.addAttribute("asdiList", asdiList);
				}
			}
	   	}else if (state.equals("5")) {
			List<GAuditInfo> collegeExportinfos= getInfo(gContest.getId(),"1");
			model.addAttribute("collegeExportinfos", collegeExportinfos);
			List<GAuditInfo> collegeInfos= getSortInfo(gContest.getId(),"2");
			model.addAttribute("collegeInfo", collegeInfos.get(0));
			List<GAuditInfo> schoolExportinfos= getInfo(gContest.getId(),"3");
			model.addAttribute("schoolExportinfos", schoolExportinfos);
			List<GAuditInfo> schoolinfos= getSortInfo(gContest.getId(),"4");
			model.addAttribute("schoolinfos", schoolinfos);
			GAuditInfo userGai= getInfoByUserId(loginUser.getId(),gContest.getId(),"5");
			if (userGai!=null) {
				//完成评分标准
				model.addAttribute("userGai", userGai);
				AuditStandardDetailIns auditStandardDetailIns=new AuditStandardDetailIns();
				auditStandardDetailIns.setAuditInfoId(userGai.getId());
				List<AuditStandardDetailIns> asdiList= auditStandardDetailInsService.findList(auditStandardDetailIns);
				if (asdiList!=null) {
					model.addAttribute("asdiList", asdiList);
				}
			}
	   	}else if (state.equals("6")) {
			List<GAuditInfo> collegeExportinfos= getInfo(gContest.getId(),"1");
			model.addAttribute("collegeExportinfos", collegeExportinfos);
			List<GAuditInfo> collegeInfos= getSortInfo(gContest.getId(),"2");
			model.addAttribute("collegeInfo", collegeInfos.get(0));
			List<GAuditInfo> schoolExportinfos= getInfo(gContest.getId(),"3");
			model.addAttribute("schoolExportinfos", schoolExportinfos);
			List<GAuditInfo> schoolinfos= getSortInfo(gContest.getId(),"4");
			model.addAttribute("schoolinfo", schoolinfos.get(0));
			List<GAuditInfo> lyinfos= getSortInfo(gContest.getId(),"5");
			model.addAttribute("lyinfo", lyinfos.get(0));
			GAuditInfo userGai= getInfoByUserId(loginUser.getId(),gContest.getId(),"6");
			if (userGai!=null) {
				//完成评分标准
				model.addAttribute("userGai", userGai);
				AuditStandardDetailIns auditStandardDetailIns=new AuditStandardDetailIns();
				auditStandardDetailIns.setAuditInfoId(userGai.getId());
				List<AuditStandardDetailIns> asdiList= auditStandardDetailInsService.findList(auditStandardDetailIns);
				if (asdiList!=null) {
					model.addAttribute("asdiList", asdiList);
				}
			}
       	}else if (state.equals("7")) {
       		List<GAuditInfo> collegeExportinfos= getInfo(gContest.getId(),"1");
     		model.addAttribute("collegeExportinfos", collegeExportinfos);
       		List<GAuditInfo> collegeInfos= getSortInfo(gContest.getId(),"2");
       		model.addAttribute("collegeInfo", collegeInfos==null||collegeInfos.size()==0?null:collegeInfos.get(0));
       		List<GAuditInfo> schoolExportinfos= getInfo(gContest.getId(),"3");
    		model.addAttribute("schoolExportinfos", schoolExportinfos);
       	 	List<GAuditInfo> schoolinfos= getSortInfo(gContest.getId(),"4");
    		model.addAttribute("schoolinfo", schoolinfos==null||schoolinfos.size()==0?null:schoolinfos.get(0));
      	 	List<GAuditInfo> lyinfos= getSortInfo(gContest.getId(),"5");
      	 	model.addAttribute("lyinfo", lyinfos==null||lyinfos.size()==0?null:lyinfos.get(0));
      	 	List<GAuditInfo> pjinfos= getSortInfo(gContest.getId(),"6");
      	 	model.addAttribute("pjinfos", pjinfos==null||pjinfos.size()==0?null:pjinfos.get(0));
       	}

        model.addAttribute("loginUser", loginUser);
		model.addAttribute("gContest", gContest);
		if (StringUtil.isNotEmpty(gContest.getpId())) {
			ProjectDeclare pd=projectDeclareService.get(gContest.getpId());
			if (pd!=null) {
				model.addAttribute("relationProject", pd.getName());
			}else{
				ProModel pm=proModelService.get(gContest.getpId());
				model.addAttribute("relationProject", pm.getpName());
			}
		}

		model.addAttribute("sse", sse);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestAuditedForm";
	}

	//学校管理员打分完毕审核处理 网评分
	@RequestMapping(value = "schoolAuditedList")
	public String schoolAuditedList(GContest gContest, HttpServletRequest request, HttpServletResponse response, Model model) {
	 	Map<String,Object> param =new HashMap<String,Object>();
	 	model.addAttribute("gContest",gContest);
	 	User user= UserUtils.getUser();

    	List<Role> roles=user.getRoleList();
    	//判断角色身份
    	if (roles.size()>0) {
    		for(int i=0;i<roles.size();i++) {
    			String roleName=roles.get(i).getEnname();
    			if (roleName.equals("schoolSec")) {
    				param.put("auditState", "5");
    				model.addAttribute("state", "5");
    				if (gContest.getpName()!=null) {
    					param.put("name", gContest.getpName());
    				}
    				if (gContest.getFinancingStat()!=null) {
    					param.put("financingStat", gContest.getFinancingStat());
    				}
    				if (gContest.getType()!=null) {
    					param.put("type", gContest.getType());
    				}
    				Page<Map<String,String>> page = gContestService.getEndGcontestList(new Page<Map<String,String>>(request, response), param);

					model.addAttribute("page", page);
    				model.addAttribute("param", param);
    		        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolAuditedList";
    			}
        	}
    	}
		model.addAttribute("page", new Page());
	    return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolAuditedList";
	}

	//大赛变更列表
	@RequestMapping(value = "gContestChangeList")
	public String gContestChangeList(GContest gContest, HttpServletRequest request,
					   HttpServletResponse response, Model model) {
	 	Map<String,Object> param =new HashMap<String,Object>();
	 	model.addAttribute("gContest",gContest);
		User user= UserUtils.getUser();
		//3学院秘书可以查看本院大赛不能变更
		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)) {
			param.put("collegeId", user.getOffice().getId());
			model.addAttribute("college", "college");
		}
		if (gContest.getpName()!=null) {
			param.put("name", gContest.getpName());
		}
		if (gContest.getFinancingStat()!=null) {
			param.put("financingStat", gContest.getFinancingStat());
		}
		if (gContest.getType()!=null) {
			param.put("type", gContest.getType());
		}
		if (gContest.getYear()!=null) {
			param.put("year", gContest.getYear());
		}
		Page<Map<String,String>> page = gContestService.getGcontestChangeList(new Page<Map<String,String>>(request, response), param);
		if (UserUtils.isAdminOrSuperAdmin(UserUtils.getUser())) {
			model.addAttribute("isAdmin", "1");
		}
		model.addAttribute("page", page);
		model.addAttribute("param", param);
        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestChangeList";

	}

	//删除大赛
    @RequestMapping(value="gcontestDelete")
    public String gcontestDelete(GContest gContest) {
		gContestService.deleteGContest(gContest);
        return CoreSval.REDIRECT+"/a/gcontest/gContest/gContestChangeList";
    }


	//大赛变更页面
	@RequestMapping(value = "changeGcontest")
	public String  changeGcontest(HttpServletRequest request, Model model) {
		//查找大赛提交的表单  供评分老师查看
		String gcontestId=request.getParameter("gcontestId");
		GContest gContest=gContestService.get(gcontestId);
		String state=gContest.getAuditState();
		model.addAttribute("state", state);
		User declareUser=userService.findUserById(gContest.getDeclareId());
		SysStudentExpansion sse = new SysStudentExpansion();
		sse.setName(declareUser.getName());
		sse.setEmail(declareUser.getEmail());
		sse.setMobile(declareUser.getMobile());
		sse.setCompany(declareUser.getCompany());
		sse.setNo(declareUser.getNo());
		sse.setProfessional(declareUser.getProfessional());
		//附件
		/*SysAttachment sysAttachment=new SysAttachment();
		sysAttachment.setUid(gContest.getId());
		List<SysAttachment> sysAttachments=sysAttachmentService.findList(sysAttachment);
		model.addAttribute("sysAttachments", sysAttachments);*/
		Map<String,String> map=new HashMap<String,String>();
		map.put("uid", gContest.getId());
		map.put("type",FileTypeEnum.S2.getValue());
		List<Map<String, String>>   sysAttachments=sysAttachmentService.getFileInfo(map);
		model.addAttribute("sysAttachments", sysAttachments);
        //查找项目团队相关信息 projectDeclare.id
        Team team=teamService.get(gContest.getTeamId());
        model.addAttribute("team",team);
		//查找学生
		//List<TeamUserRelation> turStudents=teamUserRelationService.getStudents(tur1);
		List<Map<String,String>> turStudents=projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(),gContest.getId());
		model.addAttribute("turStudents",turStudents);
		//查找导师
		//List<TeamUserRelation>  turTeachers=teamUserRelationService.getTeachers(tur1);
		List<Map<String,String>> turTeachers=projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(),gContest.getId());
		model.addAttribute("turTeachers",turTeachers);

    	GContestVo vo=new GContestVo();
		vo.setTeamStudent(projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(),gContest.getId()));
		vo.setTeamTeacher(projectDeclareService.findTeamTeacherFromTUH(gContest.getTeamId(),gContest.getId()));
		//关联团队
		model.addAttribute("teams", projectDeclareService.findTeams(declareUser.getId(),gContest.getTeamId()));
		model.addAttribute("studentExpansion", sysStudentExpansionService.getByUserId(declareUser.getId()));
		model.addAttribute("gContestVo", vo);

		if("7".equals(gContest.getAuditState())&&StringUtil.isEmpty(gContest.getProcInsId())
			|| "import".equals(gContest.getProcInsId())){
			User loginUser = UserUtils.getUser();
			model.addAttribute("loginUser", loginUser);
			model.addAttribute("isImport", "1");
			model.addAttribute("gContest", gContest);
			model.addAttribute("sse", sse);
			return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestChangeForm";
		}

        //审核意见
        if (state.equals("1")) {
        	//学院老师评分
        	List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
        	List<GAuditInfo> colleges= new ArrayList<GAuditInfo> ();
        	//找到学院专家
        	List<User> users= userService.getCollegeExpertUsers(gContest.getDeclareId());
        	if (collegeinfos.size()!=users.size()) {
        		for(User user:users) {
        			if (collegeinfos.size()>0) {
        				boolean add=true;
        				for(GAuditInfo gi:collegeinfos) {
            				if (user.getId().equals(gi.getAuditId())) {
            					colleges.add(gi);
            					add=false;
            					break;
            				}
            			}
        				if (add) {
        					GAuditInfo newGi=new GAuditInfo();
            				newGi.setCreateBy(user);
            				colleges.add(newGi);
        				}
        			}else{
            			GAuditInfo newGi=new GAuditInfo();
        				newGi.setCreateBy(user);
        				colleges.add(newGi);
        			}
        		}
        		model.addAttribute("infocolleges", colleges);
        	}else{
        		model.addAttribute("infocolleges", collegeinfos);
        	}
        	//找到学校专家
        	List<GAuditInfo> schools= new ArrayList<GAuditInfo> ();
        	List<User> userScholls= userService.getSchoolExpertUsers();
        	for(User user:userScholls) {
				GAuditInfo newGi=new GAuditInfo();
				newGi.setCreateBy(user);
				schools.add(newGi);
    		}
     		model.addAttribute("schoolinfos", schools);
        }else if (state.equals("2")) {
        	//学院老师评分
        	List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
     		model.addAttribute("infocolleges", collegeinfos);
     		//找到学校专家
        	List<GAuditInfo> schools= new ArrayList<GAuditInfo> ();
        	List<User> userScholls= userService.getSchoolExpertUsers();
        	for(User user:userScholls) {
				GAuditInfo newGi=new GAuditInfo();
				newGi.setCreateBy(user);
				schools.add(newGi);
    		}
     		model.addAttribute("schoolinfos", schools);
        }else if (state.equals("3")) {
        	//学院老师评分
        	List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
      		model.addAttribute("infocolleges", collegeinfos);
      		//学院平均分
          	List<GAuditInfo> collegeSecinfos= getSortInfo(gContest.getId(),"2");
          	if (collegeSecinfos.size()>0) {
          		model.addAttribute("collegeSecinfo", collegeSecinfos.get(0));
          	}
          	//学校平均分
          	List<GAuditInfo> schoolExportinfos= getSortInfo(gContest.getId(),"3");

     		List<GAuditInfo> schools= new ArrayList<GAuditInfo> ();
        	//找到学校专家
        	List<User> users= userService.getSchoolExpertUsers();
        	if (schoolExportinfos.size()!=users.size()) {
        		for(User user:users) {
        			if (schoolExportinfos.size()>0) {
        				boolean add=true;
        				for(GAuditInfo gi:schoolExportinfos) {
            				if (user.getId().equals(gi.getAuditId())) {
            					schools.add(gi);
            					add=false;
            					break;
            				}
            			}
        				if (add) {
        					GAuditInfo newGi=new GAuditInfo();
            				newGi.setCreateBy(user);
            				schools.add(newGi);
        				}
        			}else{
        				GAuditInfo newGi=new GAuditInfo();
        				newGi.setCreateBy(user);
        				schools.add(newGi);
        			}
        		}
        		model.addAttribute("schoolinfos", schools);
        	}else{
        		model.addAttribute("schoolinfos", schoolExportinfos);
        	}
        }else if (state.equals("4")) {
        	//学院老师评分
        	List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
      		model.addAttribute("infocolleges", collegeinfos);
      		//学院平均分
          	List<GAuditInfo> collegeSecinfos= getSortInfo(gContest.getId(),"2");
     		model.addAttribute("collegeSecinfo", collegeSecinfos.get(0));
        	//学校老师平均分
         	List<GAuditInfo> schoolinfos= getInfo(gContest.getId(),"3");
    		model.addAttribute("schoolinfos", schoolinfos);
       	}else if (state.equals("5")) {
    	   	//学院老师评分
       		List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
     		model.addAttribute("infocolleges", collegeinfos);
     		//学院平均分
         	List<GAuditInfo> collegeSecinfos= getSortInfo(gContest.getId(),"2");
    		model.addAttribute("collegeSecinfo", collegeSecinfos.get(0));
    		//学校老师平均分
        	List<GAuditInfo> schoolinfos= getInfo(gContest.getId(),"3");
        	model.addAttribute("schoolinfos", schoolinfos);
        	//学校网评平均分
       	 	List<GAuditInfo> schoolSecinfos= getSortInfo(gContest.getId(),"4");
    		model.addAttribute("schoolSecinfo", schoolSecinfos.get(0));
       	}else if (state.equals("6")) {
    	 //学院老师评分
      		List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
    		model.addAttribute("infocolleges", collegeinfos);
    		//学院平均分
        	List<GAuditInfo> collegeSecinfos= getSortInfo(gContest.getId(),"2");
        	model.addAttribute("collegeSecinfo", collegeSecinfos.get(0));
			//学校老师平均分
		   	List<GAuditInfo> schoolinfos= getInfo(gContest.getId(),"3");
		   	model.addAttribute("schoolinfos", schoolinfos);
		   	//学校网评平均分
      	 	List<GAuditInfo> schoolSecinfos= getSortInfo(gContest.getId(),"4");
      	 	model.addAttribute("schoolSecinfo", schoolSecinfos.get(0));
      	 	List<GAuditInfo> schoolLyinfos= getSortInfo(gContest.getId(),"5");
      	 	if (schoolLyinfos!=null) {
      	 		model.addAttribute("schoolLyinfo", schoolLyinfos.get(0));
      	 	}
       	}else if (state.equals("7")) {
       		//学院老师评分
      		List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
    		model.addAttribute("infocolleges", collegeinfos);
    		//学院平均分
        	List<GAuditInfo> collegeSecinfos= getSortInfo(gContest.getId(),"2");
        	model.addAttribute("collegeSecinfo", collegeSecinfos.get(0));
			//学校老师平均分
		   	List<GAuditInfo> schoolinfos= getInfo(gContest.getId(),"3");
		   	model.addAttribute("schoolinfos", schoolinfos);
		   	//学校网评平均分
      	 	List<GAuditInfo> schoolSecinfos= getSortInfo(gContest.getId(),"4");
      	 	model.addAttribute("schoolSecinfo", schoolSecinfos.get(0));
      		//学校路演平均分
      	 	List<GAuditInfo> schoolLyinfos= getSortInfo(gContest.getId(),"5");
      	 	model.addAttribute("schoolLyinfo", schoolLyinfos.get(0));
      	 	//学校评级
      	 	List<GAuditInfo> schoolEndinfos= getSortInfo(gContest.getId(),"6");
      	 	model.addAttribute("schoolEndinfo", schoolEndinfos.get(0));
      	 	//获奖
      	 	GContestAward gContestAward=gContestAwardService.getByGid(gContest.getId());
      	 	if (gContestAward!=null) {
      	 		model.addAttribute("gContestAward",gContestAward);
      	 	}
       	}else if (state.equals("8")) {
       		//学院老师评分
      		List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
    		model.addAttribute("infocolleges", collegeinfos);
    		//学院平均分
        	List<GAuditInfo> collegeSecinfos= getSortInfo(gContest.getId(),"2");
        	model.addAttribute("collegeSecinfo", collegeSecinfos.get(0));
			//学校老师平均分
		   	List<GAuditInfo> schoolinfos= getInfo(gContest.getId(),"3");
		   	model.addAttribute("schoolinfos", schoolinfos);
			//学校网评平均分
      	 	List<GAuditInfo> schoolSecinfos= getSortInfo(gContest.getId(),"4");
      	 	model.addAttribute("schoolSecinfo", schoolSecinfos.get(0));
       	}else if (state.equals("9")) {
       		//学院老师评分
      		List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"1");
    		model.addAttribute("infocolleges", collegeinfos);
    		//学院平均分
        	List<GAuditInfo> collegeSecinfos= getSortInfo(gContest.getId(),"2");
        	model.addAttribute("collegeSecinfo", collegeSecinfos.get(0));
     		//找到学校专家
        	List<GAuditInfo> schools= new ArrayList<GAuditInfo> ();
        	List<User> userScholls= userService.getSchoolExpertUsers();
        	for(User user:userScholls) {
				GAuditInfo newGi=new GAuditInfo();
				newGi.setCreateBy(user);
				schools.add(newGi);
    		}
     		model.addAttribute("schoolinfos", schools);

       	}
        User loginUser = UserUtils.getUser();
        model.addAttribute("loginUser", loginUser);
		model.addAttribute("gContest", gContest);
		model.addAttribute("sse", sse);
		return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gContestChangeForm";
	}



  //院级专家打分列表
    @RequestMapping(value = "collegeExportScoreEnd")
    public String collegeExportScoreEnd(GContest gContest, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        /*act.setProcDefKey("gcontest");  //大赛流程名称
        act.setTaskDefKey("audit1");   // 表示大赛流程阶段 见流程图的userTask的id
        Page<Act> pageForSearch =new Page<Act>(request, response);
        Page<Act> page=actTaskService.todoListForPage(pageForSearch,act);*/
    	Map<String,Object> param =new HashMap<String,Object>();
    	User user = UserUtils.getUser();
    	if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) {
    		param.put("auditState", "1");
    		param.put("collegeId",user.getOffice().getId());
    		model.addAttribute("state", "1");
    		model.addAttribute("auditName","评分");
    	}else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)) {
    		param.put("auditState", "2");
    		param.put("collegeId",user.getOffice().getId());
    		model.addAttribute("state", "2");
    		model.addAttribute("auditName","审核");
    	}else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) {
    		param.put("auditState", "3");
    		model.addAttribute("state", "3");
    		model.addAttribute("auditName","评分");
    	}else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) {

    		param.put("auditState", "4");
    		model.addAttribute("state", "4");
    		model.addAttribute("auditName","审核");
    	}else{
    		 model.addAttribute("gContest",gContest);
    		 return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "collegeExportScoreEndList";
    	}
		if (gContest.getpName()!=null) {
			param.put("name", gContest.getpName());
		}
		if (gContest.getFinancingStat()!=null) {
			param.put("financingStat", gContest.getFinancingStat());
		}
		if (gContest.getType()!=null) {
			param.put("type", gContest.getType());
		}
		Page<Map<String,String>> page = gContestService.getEndGcontestList(new Page<Map<String,String>>(request, response), param);
		model.addAttribute("page", page);
		model.addAttribute("param", param);
        model.addAttribute("gContest",gContest);
        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "collegeExportScoreEndList";
    }

	//将要审核的和已经审核合并
	//@RequestMapping(value = "auditContestList")
	@RequestMapping(value = "collegeExportScore")
    public String auditContestList(GContest gContest, HttpServletRequest request,HttpServletResponse response, Model model) {
    	Map<String,Object> param =new HashMap<String,Object>();
    	User user = UserUtils.getUser();
		model.addAttribute("menuName", "参赛项目网评");
    	if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYZJ)) {
    		param.put("auditState", "1");
    		param.put("collegeId",user.getOffice().getId());
    		model.addAttribute("state", "1");
    		model.addAttribute("auditName","评分");
    	}else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XYMS)) {
    		param.put("auditState", "2");
    		param.put("collegeId",user.getOffice().getId());
			model.addAttribute("userId",user.getId());
    		model.addAttribute("state", "2");
    		model.addAttribute("auditName","审核");
    	}else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXZJ)) {
    		param.put("auditState", "3");
    		model.addAttribute("state", "3");
    		model.addAttribute("auditName","评分");
    	}else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) {

    		param.put("auditState", "4");
    		model.addAttribute("state", "4");
    		model.addAttribute("auditName","审核");
    	}else{
			model.addAttribute("gContest",gContest);
			Page page=new Page();
			page.setCount(0);
			page.initialize();
			model.addAttribute("page",page);
			return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gcontestAuditList";
    	}
		if (gContest.getpName()!=null) {
			param.put("name", gContest.getpName());
		}
		if (gContest.getFinancingStat()!=null) {
			param.put("financingStat", gContest.getFinancingStat());
		}
		if (gContest.getType()!=null) {
			param.put("type", gContest.getType());
		}
		param.put("auditId", user.getId());
		Page<Map<String,String>> page = gContestService.auditContestList(new Page<Map<String,String>>(request, response), param);
		int todoCount=gContestService.todoCount(param);
		int hasdoCount=gContestService.hasdoCount(param);
		model.addAttribute("todoCount",(todoCount-hasdoCount));
		model.addAttribute("userId", user.getId());
		model.addAttribute("page", page);
		model.addAttribute("param", param);
        model.addAttribute("gContest",gContest);
        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "gcontestAuditList";
    }


	//学院网评分提交处理
	@RequestMapping(value = "saveAuditWangping")
	public String saveAudit1(GContest gContest,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		//执行工作流
		if (gContest.getAuditState().equals("1")) {
			Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit1");   // 表示大赛流程阶段 见流程图的userTask的id
			gContestService.saveAudit1(gContest,act);
			addMessage(redirectAttributes, "学院专家评分成功");
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/collegeExportScore/";
		}else if (gContest.getAuditState().equals("2")) {
			Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit2");   // 表示大赛流程阶段 见流程图的userTask的id
			gContestService.saveAudit2(gContest,act);
			addMessage(redirectAttributes, "学院秘书审核成功");
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/collegeExportScore/";
		}else if (gContest.getAuditState().equals("3")) {
			Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit3");   // 表示大赛流程阶段 见流程图的userTask的id
			gContestService.saveAudit3(gContest,act);
			addMessage(redirectAttributes, "学校专家评分成功");
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/collegeExportScore/";
		}else if (gContest.getAuditState().equals("4")) {
			Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit4");   // 表示大赛流程阶段 见流程图的userTask的id
			gContestService.saveAudit4(gContest,act);
			addMessage(redirectAttributes, "学校秘书审核成功");
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/collegeExportScore/";
		}else if (gContest.getAuditState().equals("5")) {
			Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit5");   // 表示大赛流程阶段 见流程图的userTask的id
			gContestService.saveAudit5(gContest,act);
			addMessage(redirectAttributes, "学校路演审核成功");
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/schoolActAuditList/";
		}else if (gContest.getAuditState().equals("6")) {
			Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit6");   // 表示大赛流程阶段 见流程图的userTask的id
			gContestService.saveAudit6(gContest,act);
			addMessage(redirectAttributes, "学校评级审核成功");
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/schoolEndAuditList/";
		}else{
			return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/gcontest/gContest/collegeExportScore/?repage";
		}
	}

	//学校管理员路演审核处理
	@RequestMapping(value = "schoolActAuditList")
	public String schoolActAuditList(GContest gContest, HttpServletRequest request,
					   HttpServletResponse response, Model model) {
	 	Map<String,Object> param =new HashMap<String,Object>();
	 	model.addAttribute("gContest",gContest);
	 	User user= UserUtils.getUser();
		model.addAttribute("menuName", "参赛项目路演");
		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) {
		 	param.put("auditState", "5");
			model.addAttribute("state", "5");
			if (gContest.getpName()!=null) {
				param.put("name", gContest.getpName());
			}
			if (gContest.getFinancingStat()!=null) {
				param.put("financingStat", gContest.getFinancingStat());
			}
			if (gContest.getType()!=null) {
				param.put("type", gContest.getType());
			}
			Page<Map<String,String>> page = gContestService.auditContestList(new Page<Map<String,String>>(request, response), param);
			model.addAttribute("page", page);
			model.addAttribute("param", param);
			int todoCount=gContestService.todoCount(param);
			model.addAttribute("todoCount",todoCount);
			model.addAttribute("userId", user.getId());
	        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolActAuditList";
		}
		Page page=new Page();
		page.setCount(0);
		page.initialize();
		model.addAttribute("page",page);

	 	return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolActAuditList";
	}

	//学院结果评定
	@RequestMapping(value = "schoolEndAuditList")
	public String schoolEndAuditList(GContest gContest, HttpServletRequest request,
					   HttpServletResponse response, Model model) {
		Map<String,Object> param =new HashMap<String,Object>();
		User user= UserUtils.getUser();
		model.addAttribute("gContest",gContest);
		model.addAttribute("menuName", "校赛结果评定");
		if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXGLY)) {
		 	param.put("auditState", "6");
			model.addAttribute("state", "6");
			if (gContest.getpName()!=null) {
				param.put("name", gContest.getpName());
			}
			if (gContest.getFinancingStat()!=null) {
				param.put("financingStat", gContest.getFinancingStat());
			}
			if (gContest.getType()!=null) {
				param.put("type", gContest.getType());
			}
			Page<Map<String,String>> page = gContestService.auditContestList(new Page<Map<String,String>>(request, response), param);
			model.addAttribute("page", page);
			model.addAttribute("param", param);
			int todoCount=gContestService.todoCount(param);
			model.addAttribute("todoCount",todoCount);
			model.addAttribute("userId", user.getId());
	        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolEndAuditList";
		}
		Page page=new Page();
		page.setCount(0);
		page.initialize();
		model.addAttribute("page",page);
    	return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolEndAuditList";
	}

/*	@RequestMapping(value = "schoolEndAuditedList")
	public String schoolEndAuditedList(GContest gContest, HttpServletRequest request,
					   HttpServletResponse response, Model model) {
		Map<String,Object> param =new HashMap<String,Object>();
		User user= UserUtils.getUser();
		model.addAttribute("gContest",gContest);

		String userType=user.getUserType();

		if (userType.equals("6")) {
		 	param.put("auditState", "7");
			model.addAttribute("state", "7");
			if (gContest.getpName()!=null) {
				param.put("name", gContest.getpName());
			}
			if (gContest.getFinancingStat()!=null) {
				param.put("financingStat", gContest.getFinancingStat());
			}
			if (gContest.getType()!=null) {
				param.put("type", gContest.getType());
			}
			Page<Map<String,String>> page = gContestService.getEndGcontestList(new Page<Map<String,String>>(request, response), param);

			model.addAttribute("page", page);
			model.addAttribute("param", param);
	        return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolEndAuditedList";
		}

    	return ProSval.path.vms(ProEmskey.GCONTEST.k()) + "schoolEndAuditedList";
	}
	*/
	@ResponseBody
	@RequestMapping(value = "findTeamPerson")
	public List<Map<String,String>> findTeamPerson(@RequestParam(required=true) String id) {
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		List<Map<String,String>> list1=projectDeclareService.findTeamStudent(id);
		List<Map<String,String>> list2=projectDeclareService.findTeamTeacher(id);
		for(Map<String,String> map:list1) {
			list.add(map);
		}
		for(Map<String,String> map:list2) {
			list.add(map);
		}
		return list;
	}

	/**
     * 获得评审意见、评分
     * @param gId 大赛id
     * @param auditStep 审核步骤 ('1','院级专家评审';'2','院级评分';'3','校级专家评审';'4','校级管理员评审';'5','路演评分';
	 *                     '6','校级评级';)
     * @return
     */
    private  List<GAuditInfo> getInfo(String gId,String auditStep) {
		GAuditInfo pai=new GAuditInfo();
        pai.setGId(gId);
        pai.setAuditLevel(auditStep);
        List<GAuditInfo> infos= gAuditInfoService.getInfo(pai);
        return infos;
    }

	/**
	 * 获得评审意见、评分
	 * @param gId 大赛id
	 * @param auditStep 审核步骤 ('1','院级专家评审';'2','院级评分';'3','校级专家评审';'4','校级管理员评审';'5','路演评分';
	 *                     '6','校级评级';)
	 * @return
	 */
	private  GAuditInfo getInfoByUserId(String userId,String gId,String auditStep) {
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gId);
		pai.setAuditLevel(auditStep);
		pai.setAuditId(userId);
		GAuditInfo info= gAuditInfoService.getInfoByUserId(pai);
		return info;
	}

    /**
     * 获得评审意见、评分,排名
     * @param gId 大赛id
     * @param auditStep 审核步骤 ('1','院级专家评审';'2','院级评分';'3','校级专家评审';'4','校级管理员评审';'5','路演评分';
	 *                     '6','校级评级';)
     * @return
     */
    private  List<GAuditInfo> getSortInfo(String gId,String auditStep) {
		GAuditInfo pai=new GAuditInfo();
        pai.setGId(gId);
        pai.setAuditLevel(auditStep);
        List<GAuditInfo> infos= gAuditInfoService.getSortInfo(pai);
        return infos;
    }

	/**
    * 获得评审意见、评分,排名
    * @param gId 大赛id
    * @param auditStep 审核步骤 ('1','院级专家评审';'2','院级评分';'3','校级专家评审';'4','校级管理员评审';'5','路演评分';
 *                     '6','校级评级';)
    * @return
    */
   	private  List<GAuditInfo> getSortByAudit(String gId,String auditStep) {
		GAuditInfo pai=new GAuditInfo();
       	pai.setGId(gId);
       	pai.setAuditLevel(auditStep);
       	List<GAuditInfo> infos= gAuditInfoService.getSortByAudit(pai);
       	return infos;
   	}

	/**
	    * 获得评审意见、评分,排名
	    * @param gId 大赛id
	    * @param auditStep 审核步骤 ('1','院级专家评审';'2','院级评分';'3','校级专家评审';'4','校级管理员评审';'5','路演评分';
		*                     '6','校级评级';)
	    * @return
	    */
	private  List<GAuditInfo> getCollegeSortByAudit(String collegeId,String gId,String auditStep) {
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gId);
		pai.setAuditLevel(auditStep);
		pai.setCollegeId(collegeId);
		List<GAuditInfo> infos= gAuditInfoService.getSortByAudit(pai);
		return infos;
	}

	@RequestMapping(value = "delFile")
	@ResponseBody
	public JSONObject delFile( HttpServletRequest request) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "删除成功");
		String arrUrl= request.getParameter("arrUrl");
		String id= request.getParameter("id");
		try {
			if (id!=null&&!"null".equals(id))sysAttachmentService.delete(new SysAttachment(id));
			ftpService.del(arrUrl);
		} catch (Exception e) {
			js.put("ret", 0);
			js.put("msg", "删除失败");
		}
		return js;
	}


}