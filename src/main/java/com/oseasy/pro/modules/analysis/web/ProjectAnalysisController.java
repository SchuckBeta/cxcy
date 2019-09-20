package com.oseasy.pro.modules.analysis.web;

import java.util.ArrayList;
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
import com.oseasy.pro.modules.analysis.service.ProjectAnalysisService;
import com.oseasy.pro.modules.analysis.vo.AnnerVo;
import com.oseasy.pro.modules.analysis.vo.EchartVo;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/analysis/projectAnalysis")
public class ProjectAnalysisController extends BaseController {
	@Autowired
	private ProjectAnalysisService projectAnalysisService;
	@RequestMapping(value = "toPage")
	public String toPage(HttpServletRequest request, HttpServletResponse response,Model model) {
		List<String> years =projectAnalysisService.getYears();
		model.addAttribute("years",years);
		return ProSval.path.vms(ProEmskey.ANALYSIS.k()) + "projectAnalysis";
	}
	@RequestMapping(value = "getData")
	@ResponseBody
	public JSONObject getData(HttpServletRequest request, HttpServletResponse response) {
		return projectAnalysisService.getData();
	}

	/**
	 * 获得 双创项目学生比例柱状图 数据
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getPojectByYear")
	@ResponseBody
	public List<AnnerVo> getPojectByYear (String year) {
		List<AnnerVo> list=new ArrayList<>();
		list=projectAnalysisService.findAllPojectByYear(year);
	/*	//得到年份
		List<String> yearList= projectAnalysisService.getYears();
		//得到类别
		List<String> typeList= projectAnalysisService.getProjecTypes();

		List<String> categeries=new ArrayList<>();
		for(int i=0;i<yearList.size();i++) {
			categeries.add(yearList.get(i));
		}
		categeries.add("2014");
		categeries.add("2015");
		categeries.add("2016");
		categeries.add("2017");
		AnnerVo bar1=new AnnerVo("大创项目");  //统计年份
		AnnerVo bar2=new AnnerVo("创新项目");
		int[] gcNums=new int[yearList.size()];  // 国创项目数
		int[] cxNums=new int[yearList.size()];  // 创新项目数
		gcNums[0]=300;//2014年国创项目数
		cxNums[0]=200;//2014年创新项目数

		gcNums[1]=400;//2015年国创项目数
		cxNums[1]=300;//2014年创新项目数

		gcNums[2]=350;//2016年国创项目数
		cxNums[2]=400;//2016年创新项目数

		gcNums[3]=400;//2017年国创项目数
		cxNums[3]=500;//2017年创新项目数

		bar1.addData(gcNums[0]);
		bar1.addData(gcNums[1]);
		bar1.addData(gcNums[2]);
		bar1.addData(gcNums[3]);

		bar1.setCategories(categeries);
		bar2.addData(cxNums[0]);
		bar2.addData(cxNums[1]);
		bar2.addData(cxNums[2]);
		bar2.addData(cxNums[3]);

		bar2.setCategories(categeries);

		list.add(bar1);
		list.add(bar2);*/

		return list;
	}


	/**
	 * 获得 双创项目类型饼状图
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getProjectByType")
	@ResponseBody
	public List<EchartVo> getPojectByType (String year) {
		List<EchartVo> list=new ArrayList<>();
		list=projectAnalysisService.findAllPojectByType(year);
		return list;
	}

	/**
	 * 获得 双创项目类型以及类别饼状图
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getProjectByTypeAndCategory")
	@ResponseBody
	public List<EchartVo> getPojectByTypeAndCategory(String label,String year) {
		List<EchartVo> list=new ArrayList<>();
		list=projectAnalysisService.findPojectByTypeAndCategory(label,year);
		return list;
	}


	/**
	 * 获得 双创项目类型学院申报，立项数
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getProjectApplyByType")
	@ResponseBody
	public JSONObject getProjectApplyByType(String label,String year) {
		JSONObject list=new JSONObject();
		list=projectAnalysisService.findProjectApplyByType(label,year);
		return list;
	}

	/**
	 * 获得 学院申报，立项数
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "getProjectApplyLiByType")
	@ResponseBody
	public JSONObject getProjectApplyLiByType(String label,String year) {
		JSONObject list=new JSONObject();
		list=projectAnalysisService.findProjectApplyLiByType(label,year);
		return list;
	}
}
