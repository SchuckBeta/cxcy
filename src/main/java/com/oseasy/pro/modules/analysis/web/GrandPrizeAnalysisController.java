package com.oseasy.pro.modules.analysis.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;

@Controller
@RequestMapping(value = "${adminPath}/analysis/grandPrizeAnalysis")
public class GrandPrizeAnalysisController extends BaseController {
	@RequestMapping(value = "toPage")
	public String toPage(HttpServletRequest request, HttpServletResponse response) {
		return ProSval.path.vms(ProEmskey.ANALYSIS.k()) + "grandPrizeAnalysis";
	}
}
