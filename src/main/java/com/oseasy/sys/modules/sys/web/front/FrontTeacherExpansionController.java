package com.oseasy.sys.modules.sys.web.front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 导师信息表Controller
 * @author l
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/frontTeacherExpansion")
public class FrontTeacherExpansionController extends BaseController {
    public static final String FRONT_TEACHER = CoreSval.getFrontPath() + "/sys/frontTeacherExpansion/form?id=";
	@Autowired
	private OfficeService officeService;
	@Autowired
	private BackTeacherExpansionService backTeacherExpansionService;
	@Autowired
	private StudentExpansionService studentExpansionService;
	@Autowired
	private TeamService teamService;
	@Autowired
	TeacherKeywordService teacherKeywordService;

	@ModelAttribute
	public BackTeacherExpansion get(@RequestParam(required=false) String id,Model model) {
		BackTeacherExpansion entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = backTeacherExpansionService.get(id);
		}
		if (entity == null) {
			entity=backTeacherExpansionService.getByUserId(id);
		}
		if(entity == null){
			entity = new BackTeacherExpansion();
		}

		List<Office> offices = officeService.findList(true);
		model.addAttribute("offices", offices);
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response, Model model) {
//		String currentId = UserUtils.getUser().getId();
//		backTeacherExpansion.setTeamLeaderId(currentId);
//
//		backTeacherExpansion.setIsFront("1");
//		backTeacherExpansion.setIsOpen("1");
//		Page<BackTeacherExpansion> page = backTeacherExpansionService.findPage(new Page<BackTeacherExpansion>(request, response), backTeacherExpansion);
//
//		Team team=new Team();
//		team.setSponsor(currentId);
//		team.setState("0");
//		List<Team> teams=teamService.findListByCreatorIdAndState(team);
//		String steaFullTeams=getFullStarffedTeams(teams, 2);//校园导师满员的
//		String eteaFullTeams=getFullStarffedTeams(teams, 3);//企业导师满员的
//		if (StringUtil.isNotEmpty(currentId)&&teams!=null&&teams.size()>0) {
//			model.addAttribute("canInvite",true);
//        	for (BackTeacherExpansion studentExp: page.getList()) {//每个被邀请人需单独判断
//        		if ((TeacherType.TY_QY.getKey()).equals(studentExp.getTeachertype())) {
//        			studentExp.setCanInviteTeamIds(removeFullStarffedTeams(eteaFullTeams, studentExp.getCanInviteTeamIds()));//剔除满员的
//        		}else{
//        			studentExp.setCanInviteTeamIds(removeFullStarffedTeams(steaFullTeams, studentExp.getCanInviteTeamIds()));//剔除满员的
//        		}
//        		if (StringUtil.isEmpty(studentExp.getCanInviteTeamIds())) {
//        			studentExp.setCanInvite(false);
//        		}else{
//        			studentExp.setCanInvite(true);
//        		}
//			}
//		}else{//没有可用于邀请的团队
//			model.addAttribute("canInvite", false);
//		}
//		model.addAttribute("page", page);
//		model.addAttribute("teams", teams);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "front/frontTeacherExpansionList";
	}


	@RequestMapping(value = "ajaxFrontTeacherList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxFrontTeacherList(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response){
	   	try {
			HashMap<String, Object> hashMap = new HashMap<>();
		    String currentId = UserUtils.getUser().getId();
			backTeacherExpansion.setTeamLeaderId(currentId);
			backTeacherExpansion.setIsFront("1");
			backTeacherExpansion.setIsOpen("1");
			backTeacherExpansion.setTenantId(TenantConfig.getCacheTenant());
			Page<BackTeacherExpansion> page = backTeacherExpansionService.findPage(new Page<BackTeacherExpansion>(request, response), backTeacherExpansion);
			Team team=new Team();
			team.setSponsor(currentId);
			team.setState("0");
			List<Team> teams=teamService.findListByCreatorIdAndState(team);
			String steaFullTeams=getFullStarffedTeams(teams, 2);//校园导师满员的
			String eteaFullTeams=getFullStarffedTeams(teams, 3);//企业导师满员的
			if (StringUtil.isNotEmpty(currentId)&&teams!=null&&teams.size()>0) {
				hashMap.put("canInvite",true);
				for (BackTeacherExpansion studentExp: page.getList()) {//每个被邀请人需单独判断
					if ((TeacherType.TY_QY.getKey()).equals(studentExp.getTeachertype())) {
						studentExp.setCanInviteTeamIds(removeFullStarffedTeams(eteaFullTeams, studentExp.getCanInviteTeamIds()));//剔除满员的
					}else{
						studentExp.setCanInviteTeamIds(removeFullStarffedTeams(steaFullTeams, studentExp.getCanInviteTeamIds()));//剔除满员的
					}
					if (StringUtil.isEmpty(studentExp.getCanInviteTeamIds())) {
						studentExp.setCanInvite(false);
					}else{
						studentExp.setCanInvite(true);
					}
				}
			}else{//没有可用于邀请的团队
				hashMap.put("canInvite", false);
			}
			hashMap.put("page", page);
			hashMap.put("teams", teams);
			return  ApiResult.success(hashMap);
	   }catch (Exception e){
		   logger.error(e.getMessage());
		   return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
	   }
   }


	private String removeFullStarffedTeams(String stuFullTeams,String canInviteTeamIds) {
		if (stuFullTeams==null) {
			return canInviteTeamIds;
		}
		if (StringUtil.isEmpty(canInviteTeamIds)) {
			return canInviteTeamIds;
		}
		List<String> canInviteTeamIdsList=new ArrayList<String>(Arrays.asList(canInviteTeamIds.split(",")));
		for(int i=0;i<canInviteTeamIdsList.size();i++) {
			if (stuFullTeams.contains(canInviteTeamIdsList.get(i))) {
				canInviteTeamIdsList.remove(i);
				i--;
			}
		}
		if (canInviteTeamIdsList!=null&&canInviteTeamIdsList.size()>0) {
			canInviteTeamIds=org.apache.commons.lang3.StringUtils.join(canInviteTeamIdsList,",");
		}else{
			canInviteTeamIds=null;
		}
		return canInviteTeamIds;
	}
	private String getFullStarffedTeams(List<Team> teams,int type) {
		if (teams!=null&&teams.size()>0) {
			List<String> list=new ArrayList<String>();
			for(Team t:teams) {
				if (type==1&&t.getUserCount()>=t.getMemberNum()) {//学生满
					list.add(t.getId());
				}else if (type==2&&t.getSchoolNum()>=t.getSchoolTeacherNum()) {//校内导师满
					list.add(t.getId());
				}else if (type==3&&t.getEnterpriseNum()>=t.getEnterpriseTeacherNum()) {//企业导师满
					list.add(t.getId());
				}
			}
			if (list.size()>0) {
				return org.apache.commons.lang3.StringUtils.join(list,",");
			}
		}
		return null;
	}

	@RequestMapping(value = "toInvite")
	@ResponseBody
	public JSONObject toInvite(String userIds,String userType,String teamId) {
		return studentExpansionService.toInvite(userIds, userType, teamId);
	}

	@RequestMapping(value = "invite")
	public String invite(BackTeacherExpansion backTeacherExpansion,Model model) {
		backTeacherExpansionService.ivite(backTeacherExpansion);
		return CoreSval.REDIRECT+CoreSval.getFrontPath()+"/sys/frontTeacherExpansion/?repage";
	}
	@RequestMapping(value = "addTeacherByStu")
	@ResponseBody
	public JSONObject addTeacherByStu(String name,String no,String mobile,String type,String office,String profes) {
		try {
			return backTeacherExpansionService.addTeacherByStu(name, no, mobile, type,office,profes);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			JSONObject js=new JSONObject();
			js.put("ret", 0);
			js.put("msg", "发生了未知的错误");
			return js;
		}
	}


}