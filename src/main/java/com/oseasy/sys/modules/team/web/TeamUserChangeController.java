package com.oseasy.sys.modules.team.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.enums.TeamChangeEnum;
import com.oseasy.sys.modules.sys.enums.TeamDutyEnum;
import com.oseasy.sys.modules.team.entity.TeamUserChange;
import com.oseasy.sys.modules.team.service.TeamUserChangeService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 团队人员变更表Controller.
 * @author 团队人员变更表
 * @version 2018-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/team/teamUserChange")
public class TeamUserChangeController extends BaseController {
	@Autowired
	private TeamUserChangeService teamUserChangeService;

	@ModelAttribute
	public TeamUserChange get(@RequestParam(required=false) String id) {
		TeamUserChange entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = teamUserChangeService.get(id);
		}
		if (entity == null){
			entity = new TeamUserChange();
		}
		return entity;
	}

	@RequiresPermissions("team:teamUserChange:view")
	@RequestMapping(value = {"list", ""})
	public String list(TeamUserChange teamUserChange, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TeamUserChange> page = teamUserChangeService.findPage(new Page<TeamUserChange>(request, response), teamUserChange);
		model.addAttribute("page", page);
		return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamUserChangeList";
	}

	@RequiresPermissions("team:teamUserChange:view")
	@RequestMapping(value = "form")
	public String form(TeamUserChange teamUserChange, Model model) {
		model.addAttribute("teamUserChange", teamUserChange);
		return SysSval.path.vms(SysEmskey.TEAM.k()) + "teamUserChangeForm";
	}

	@RequiresPermissions("team:teamUserChange:edit")
	@RequestMapping(value = "save")
	public String save(TeamUserChange teamUserChange, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, teamUserChange)){
			return form(teamUserChange, model);
		}
		teamUserChangeService.save(teamUserChange);
		addMessage(redirectAttributes, "保存团队人员变更表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/team/teamUserChange/?repage";
	}

	@RequiresPermissions("team:teamUserChange:edit")
	@RequestMapping(value = "delete")
	public String delete(TeamUserChange teamUserChange, RedirectAttributes redirectAttributes) {
		teamUserChangeService.delete(teamUserChange);
		addMessage(redirectAttributes, "删除团队人员变更表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/team/teamUserChange/?repage";
	}

	@RequestMapping(value = "ajax/getTeamUserChangeList")
	@ResponseBody
	public JSONObject getTeamUserChangeList(String proModelId, String teamId) {
		JSONObject js = new JSONObject();
		try {
			TeamUserChange teamUserChange=new TeamUserChange();
			teamUserChange.setProId(proModelId);
			teamUserChange.setTeamId(teamId);
			List<TeamUserChange> teamUserChangeList=teamUserChangeService.findList(teamUserChange);
			JSONArray jsList = new JSONArray();
			for(TeamUserChange teamUserChangeIndex:teamUserChangeList){
				JSONObject jsObject = new JSONObject();
				String duty=teamUserChangeIndex.getDuty();
				String dutyName=TeamDutyEnum.getByValue(duty).getName();
				jsObject.put("duty",duty);

				String operType=teamUserChangeIndex.getOperType();
				jsObject.put("operType",operType);

				String operUserName=UserUtils.get(teamUserChangeIndex.getOperUserId()).getName();
				String userName=UserUtils.get(teamUserChangeIndex.getUserId()).getName();
				String content="";
				if(TeamChangeEnum.ADD.getValue().equals(operType)){
					content=operUserName+TeamChangeEnum.ADD.getName()+dutyName+userName;
				}else if (TeamChangeEnum.DEL.getValue().equals(operType)){
					content=operUserName+TeamChangeEnum.DEL.getName()+dutyName+userName;
				}else if(TeamChangeEnum.MOD.getValue().equals(operType)){
					content=operUserName+"将"+userName+TeamChangeEnum.MOD.getName()+"为"+dutyName;
				}
				jsObject.put("content",content);

				jsObject.put("operUserId",teamUserChangeIndex.getOperUserId());
				jsObject.put("operUserName", operUserName);
				jsObject.put("userId",teamUserChangeIndex.getUserId());
				jsObject.put("name", UserUtils.get(teamUserChangeIndex.getUserId()).getName());
				jsObject.put("date", DateUtil.formatDateTime(teamUserChangeIndex.getCreateDate()));
				jsList.add(jsObject);
			}

			js.put("list",jsList);
			js.put("ret", "1");
			return js;
		} catch (Exception e) {
			logger.error(e.getMessage());

			js.put("ret", "0");
			js.put("msg", "保存失败,出现了未知的错误，请重试或者联系管理员");
			return js;
		}
	}
}