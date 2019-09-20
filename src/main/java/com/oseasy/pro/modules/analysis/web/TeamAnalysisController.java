package com.oseasy.pro.modules.analysis.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.analysis.service.TeamAnalysisService;
import com.oseasy.pro.modules.analysis.vo.AnnerVo;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/analysis/teamAnalysis")
public class TeamAnalysisController extends BaseController {
	@Autowired
	private TeamAnalysisService teamAnalysisService;
	@RequestMapping(value = "toPage")
	public String toPage(HttpServletRequest request, HttpServletResponse response,Model model) {
		Calendar calendar=Calendar.getInstance();
		List<String> years=new ArrayList<String>();
		for(int i=calendar.get(Calendar.YEAR);i>=2010;i--) {
			years.add(i+"");
		}
		model.addAttribute("years", years);
		return ProSval.path.vms(ProEmskey.ANALYSIS.k()) + "teamAnalysis";
	}
	/**
	 * 获得 双创项目导师比例柱状图 数据
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getTeamTeacherByYear")
	@ResponseBody
	public List<AnnerVo> getTeamTeacherByYear (String year) {
		List<AnnerVo> list = new ArrayList<AnnerVo>();
		list = teamAnalysisService.findTeamTeacherByYear(year);
		return list;
	}
	@RequestMapping(value = "getTeamTeacherByCollege")
	@ResponseBody
	public JSONObject getTeamTeacherByCollege (String year) {
		JSONObject list = new JSONObject();
		list = teamAnalysisService.findTeamTeacherByCollege(year);
		return list;
	}
	/**
	 * 获得 双创团队数柱状图 数据
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getTeamNumByYear")
	@ResponseBody
	public List<AnnerVo> getTeamNumByYear (String year) {
		List<AnnerVo> list = new ArrayList<AnnerVo>();
		list = teamAnalysisService.findTeamNumByYear(year);
		return list;
	}

	@RequestMapping(value = "getTeamNum")
	@ResponseBody
	public JSONObject getTeamNum(HttpServletRequest request, HttpServletResponse response) {
		String year=request.getParameter("year");
		return teamAnalysisService.getTeamNum(year);
	}
	@RequestMapping(value = "getTeamMemsNum")
	@ResponseBody
	public JSONObject getTeamMemsNum(HttpServletRequest request, HttpServletResponse response) {
		String year=request.getParameter("year");
		return teamAnalysisService.getTeamMemsNum(year);
	}
	@RequestMapping(value = "getTeamYearMemsNum")
	@ResponseBody
	public JSONObject getTeamYearMemsNum(HttpServletRequest request, HttpServletResponse response) {
		return teamAnalysisService.getTeamYearMemsNum();
	}
}
