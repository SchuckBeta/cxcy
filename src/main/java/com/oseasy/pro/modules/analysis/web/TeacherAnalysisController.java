package com.oseasy.pro.modules.analysis.web;

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
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.util.common.utils.StringUtil;

@Controller
@RequestMapping(value = "${adminPath}/analysis/teacherAnalysis")
public class TeacherAnalysisController extends BaseController {
	@Autowired
	private BackTeacherExpansionService backTeacherExpansionService;

	@RequestMapping(value = "toPage")
	public String toPage(BackTeacherExpansion backTeacherExpansion,HttpServletRequest request,HttpServletResponse response,Model model) {

		Page<BackTeacherExpansion> newPage= new Page<BackTeacherExpansion>(request, response);
		String size = request.getParameter("pageSize");
		if (StringUtil.isEmpty(size)) {
			newPage.setPageSize(2);
		}
		/*Page<BackTeacherExpansion> page = backTeacherExpansionService.findTeacherExpansionPage(newPage, backTeacherExpansion); */
		Page<BackTeacherExpansion> page = backTeacherExpansionService.findPage(newPage, backTeacherExpansion);

		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.ANALYSIS.k()) + "teacherAnalysis";
	}
}
