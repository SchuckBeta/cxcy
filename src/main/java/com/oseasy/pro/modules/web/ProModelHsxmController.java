package com.oseasy.pro.modules.web;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.service.ProModelHsxmService;
import com.oseasy.util.common.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 互联网+大赛模板Controller.
 * @author zy
 * @version 2018-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow.hsxm/proModelHsxm")
public class ProModelHsxmController extends BaseController {

	@Autowired
	private ProModelHsxmService proModelHsxmService;
	@ModelAttribute
	public ProModelHsxm get(@RequestParam(required=false) String id) {
		ProModelHsxm entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = proModelHsxmService.get(id);
		}
		if (entity == null){
			entity = new ProModelHsxm();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(ProModelHsxm proModelHsxm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProModelHsxm> page = proModelHsxmService.findPage(new Page<ProModelHsxm>(request, response), proModelHsxm);
		model.addAttribute("page", page);
		return "modules/workflow.mdgc/proModelHsxmList";
	}

	@RequestMapping(value = "form")
	public String form(ProModelHsxm proModelHsxm, Model model) {
		model.addAttribute("proModelHsxm", proModelHsxm);
		return "modules/workflow.hsxm/proModelHsxmForm";
	}

	@RequestMapping(value = "save")
	public String save(ProModelHsxm proModelHsxm, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, proModelHsxm)){
			return form(proModelHsxm, model);
		}
		proModelHsxmService.save(proModelHsxm);
		addMessage(redirectAttributes, "保存互联网+大赛模板成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/workflow.hsxm/proModelHsxm/?repage";
	}

	@RequestMapping(value = "delete")
	public String delete(ProModelHsxm proModelHsxm, RedirectAttributes redirectAttributes) {
		proModelHsxmService.delete(proModelHsxm);
		addMessage(redirectAttributes, "删除互联网+大赛模板成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/workflow.hsxm/proModelMdGc/?repage";
	}

	@RequestMapping(value = "export")
	public void export(ProModelHsxm proModelHsxm, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
//		List<ProModelMdGcVo> proModelMdGcVOS = proModelMdGcService.exportData(new Page<ProModelMdGc>(request, response), proModelMdGc);
//		ExportParams params = new ExportParams("填写数据说明：红色名称为必填信息。项目年份举例：2016。", "大赛导出信息", ExcelType.XSSF);
//		params.setFreezeCol(2);
//		map.put(NormalExcelConstants.DATA_LIST, proModelMdGcVOS);
//		map.put(NormalExcelConstants.CLASS, ProModelMdGcVo.class);
//		map.put(NormalExcelConstants.PARAMS, params);
//		map.put(NormalExcelConstants.FILE_NAME, "大赛导出信息");
//		PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
		proModelHsxmService.exportDataQuery(request, response, proModelHsxm);
	}
}