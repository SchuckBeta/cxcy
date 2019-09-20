package com.oseasy.pro.modules.analysis.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.promodel.entity.ProStudentExpansion;
import com.oseasy.pro.modules.promodel.service.ProStudentExpansionService;
import com.oseasy.util.common.utils.StringUtil;

@Controller
@RequestMapping(value = "${adminPath}/analysis/studentAnalysis")
public class StudentAnalysisController extends BaseController {
	@Autowired
	private ProStudentExpansionService proStudentExpansionService;


	@RequestMapping(value = "toPage")
	public String toPage(ProStudentExpansion studentExpansion,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProStudentExpansion> newPage= new Page<ProStudentExpansion>(request, response);
		Map<String,Object> param =new HashMap<String,Object>();
		String size = request.getParameter("pageSize");
		if (StringUtil.isEmpty(size)) {
			newPage.setPageSize(2);
		}
		if (studentExpansion.getUser()!=null&&studentExpansion.getUser().getOffice()!=null) {
			param.put("collegeId", studentExpansion.getUser().getOffice().getId());
		}
		if (studentExpansion.getUser()!=null&&studentExpansion.getUser().getProfessional()!=null) {
			param.put("professional", studentExpansion.getUser().getProfessional());
		}
		if (studentExpansion.getUser()!=null&&studentExpansion.getUser().getName()!=null) {
			param.put("name", studentExpansion.getUser().getName());
		}


		Page<ProStudentExpansion> pageNew = proStudentExpansionService.findStudentPage(newPage, param);
		model.addAttribute("page", pageNew);
		model.addAttribute("param", param);
		return ProSval.path.vms(ProEmskey.ANALYSIS.k()) + "studentAnalysis";
	}
//	@RequestMapping(value = "getData")
//	@ResponseBody
//	public JSONObject getData(HttpServletRequest request, HttpServletResponse response) {
//		return projectAnalysisService.getData();
//	}
}
