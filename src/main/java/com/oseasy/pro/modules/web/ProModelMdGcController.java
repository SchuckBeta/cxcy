package com.oseasy.pro.modules.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.service.ProModelMdGcService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 互联网+大赛模板Controller.
 * @author zy
 * @version 2018-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow.mdgc/proModelMdGc")
public class ProModelMdGcController extends BaseController {

	@Autowired
	private ProModelMdGcService proModelMdGcService;

	@ModelAttribute
	public ProModelMdGc get(@RequestParam(required=false) String id) {
		ProModelMdGc entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = proModelMdGcService.get(id);
		}
		if (entity == null){
			entity = new ProModelMdGc();
		}
		return entity;
	}

	@RequiresPermissions("workflow.mdgc:proModelMdGc:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProModelMdGc proModelMdGc, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProModelMdGc> page = proModelMdGcService.findPage(new Page<ProModelMdGc>(request, response), proModelMdGc);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "mdgc/proModelMdGcList";
	}

	@RequiresPermissions("workflow.mdgc:proModelMdGc:view")
	@RequestMapping(value = "form")
	public String form(ProModelMdGc proModelMdGc, Model model) {
		model.addAttribute("proModelMdGc", proModelMdGc);
		return ProSval.path.vms(ProEmskey.WORKFLOW.k()) + "mdgc/proModelMdGcForm";
	}

	@RequiresPermissions("workflow.mdgc:proModelMdGc:edit")
	@RequestMapping(value = "save")
	public String save(ProModelMdGc proModelMdGc, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, proModelMdGc)){
			return form(proModelMdGc, model);
		}
		proModelMdGcService.save(proModelMdGc);
		addMessage(redirectAttributes, "保存互联网+大赛模板成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/workflow.mdgc/proModelMdGc/?repage";
	}

	@RequiresPermissions("workflow.mdgc:proModelMdGc:edit")
	@RequestMapping(value = "delete")
	public String delete(ProModelMdGc proModelMdGc, RedirectAttributes redirectAttributes) {
		proModelMdGcService.delete(proModelMdGc);
		addMessage(redirectAttributes, "删除互联网+大赛模板成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/workflow.mdgc/proModelMdGc/?repage";
	}

	@RequestMapping(value = "export")
	public void export(ProModelMdGc proModelMdGc, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
//		List<ProModelMdGcVo> proModelMdGcVOS = proModelMdGcService.exportData(new Page<ProModelMdGc>(request, response), proModelMdGc);
//		ExportParams params = new ExportParams("填写数据说明：红色名称为必填信息。项目年份举例：2016。", "大赛导出信息", ExcelType.XSSF);
//		params.setFreezeCol(2);
//		map.put(NormalExcelConstants.DATA_LIST, proModelMdGcVOS);
//		map.put(NormalExcelConstants.CLASS, ProModelMdGcVo.class);
//		map.put(NormalExcelConstants.PARAMS, params);
//		map.put(NormalExcelConstants.FILE_NAME, "大赛导出信息");
//		PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
		proModelMdGcService.exportDataQuery(request, response, proModelMdGc);
	}
}