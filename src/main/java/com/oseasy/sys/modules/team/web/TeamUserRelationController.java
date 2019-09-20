/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/oseasy/initiate">JeeSite</a> All rights reserved.
 */
package com.oseasy.sys.modules.team.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 团队人员关系表Controller
 * @author zhangzheng
 * @version 2017-03-06
 */
@Controller
@RequestMapping(value = "${adminPath}/team/teamUserRelation")
public class TeamUserRelationController extends BaseController {

	@Autowired
	private TeamUserRelationService teamUserRelationService;
	@Autowired
	private TeamService teamService;

	@ModelAttribute
	public TeamUserRelation get(@RequestParam(required=false) String id) {
		TeamUserRelation entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = teamUserRelationService.get(id);
		}
		if (entity == null) {
			entity = new TeamUserRelation();
		}
		return entity;
	}

	//执行同意团建
	@RequestMapping(value = "changeState")
	public String changeState(TeamUserRelation teamUserRelation,
							  HttpServletRequest request,
							  HttpServletResponse response,
							  RedirectAttributes redirectAttributes,Model model) {
		User user = UserUtils.getUser();
		teamUserRelation.setUser(user);
		//改变teamUserRelation 的state状态 改变team 的state状态
		teamUserRelationService.updateStateInTeam(teamUserRelation);
		addMessage(redirectAttributes, "成功");
		return CoreSval.REDIRECT + adminPath + "/oa/oaNotify/self";
	}


	@RequestMapping(value = {"list", ""})
	public String list(TeamUserRelation teamUserRelation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TeamUserRelation> page = teamUserRelationService.findPage(new Page<TeamUserRelation>(request, response), teamUserRelation);
		model.addAttribute("page", page);
		return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamUserRelationList";
	}

	@RequestMapping(value = "form")
	public String form(TeamUserRelation teamUserRelation, Model model) {
		model.addAttribute("teamUserRelation", teamUserRelation);
		return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamUserRelationForm";
	}

	@RequestMapping(value = "save")
	public String save(TeamUserRelation teamUserRelation, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, teamUserRelation)) {
			return form(teamUserRelation, model);
		}
		teamUserRelationService.save(teamUserRelation);
		addMessage(redirectAttributes, "保存团队人员关系成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/team/teamUserRelation/?repage";
	}

	@RequestMapping(value = "delete")
	public String delete(TeamUserRelation teamUserRelation, RedirectAttributes redirectAttributes) {
		teamUserRelationService.delete(teamUserRelation);
		addMessage(redirectAttributes, "删除团队人员关系成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/team/teamUserRelation/?repage";
	}

	//团队负责人发布
	@RequestMapping(value = "batInTeamUser")
	@ResponseBody
	public JSONObject batInTeamUser(String offices,String userIds,String teamId, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		JSONObject json=new JSONObject();
		try {
			//查询所有用户
			List<String> userList=teamUserRelationService.findAllUserId(null,offices,userIds);


			//删除久通知  不稳定先注释
/*			OaNotify oaNotify=new OaNotify();
			oaNotify.setsId(teamId);
			oaNotify.setType("7");
			logger.info("teamId:"+teamId);
			List<OaNotify> oanList= oaNotifyService.findList(oaNotify);

			for (OaNotify oaNotify2 : oanList) {
				//oaNotifyRecordDao.deleteByOaNotifyId(oaNotify2.getId());
				oaNotifyService.delete(oaNotify2);
			}*/

			//插入发布通知
			if (userList.size()>0) {
				User teamUser=  UserUtils.getUser();
				Team team = teamService.get(teamId);
				teamUserRelationService.inseRelOaNo(team, teamUser, userList);
			}
			json.put("success", true);
		} catch (Exception e) {
			json.put("success", false);
			logger.error(e.getMessage());
		}
		return json;
	}


	//团队负责人批量邀请
	@RequestMapping(value = "toInvite")
	@ResponseBody
	public JSONObject toInvite(String offices,String userIds,String userType,String teamId, HttpServletRequest request, HttpServletResponse response, Model model) {
		return teamUserRelationService.toInvite(offices, userIds, userType, teamId);
	}




	//团队负责人直接拉入
	@RequestMapping(value = "pullIn")
	@ResponseBody
	public JSONObject pullIn(String offices,String userIds,String userType,String teamId, HttpServletRequest request, HttpServletResponse response, Model model) {
		return teamUserRelationService.pullIn(offices, userIds, userType, teamId);
	}




}