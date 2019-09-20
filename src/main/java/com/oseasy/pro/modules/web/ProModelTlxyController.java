package com.oseasy.pro.modules.web;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.service.ProModelTlxyService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 互联网+大赛模板Controller.
 * @author zy
 * @version 2018-06-05
 */
@Controller
@RequestMapping(value = "${adminPath}/workflow.tlxy/proModelTlxy")
public class ProModelTlxyController extends BaseController {

	@Autowired
	private ProModelTlxyService proModelTlxyService;

	@ModelAttribute
	public ProModelTlxy get(@RequestParam(required=false) String id) {
		ProModelTlxy entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = proModelTlxyService.get(id);
		}
		if (entity == null){
			entity = new ProModelTlxy();
		}
		return entity;
	}

	@RequiresPermissions("workflow.tlxy:proModelTlxy:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProModelTlxy proModelTlxy, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProModelTlxy> page = proModelTlxyService.findPage(new Page<ProModelTlxy>(request, response), proModelTlxy);
		model.addAttribute("page", page);
		return "modules/workflow.tlxy/proModelTlxyList";
	}

	@RequiresPermissions("workflow.tlxy:proModelTlxy:view")
	@RequestMapping(value = "form")
	public String form(ProModelTlxy proModelTlxy, Model model) {
		model.addAttribute("proModelTlxy", proModelTlxy);
		return "modules/workflow.tlxy/proModelTlxyForm";
	}

	@RequiresPermissions("workflow.tlxy:proModelTlxy:edit")
	@RequestMapping(value = "save")
	public String save(ProModelTlxy proModelTlxy, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, proModelTlxy)){
			return form(proModelTlxy, model);
		}
		proModelTlxyService.save(proModelTlxy);
		addMessage(redirectAttributes, "保存铜陵学院模板成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/workflow.tlxy/proModelTlxy/?repage";
	}

	@RequiresPermissions("workflow.tlxy:proModelMdGc:edit")
	@RequestMapping(value = "delete")
	public String delete(ProModelTlxy proModelTlxy, RedirectAttributes redirectAttributes) {
		proModelTlxyService.delete(proModelTlxy);
		addMessage(redirectAttributes, "删除互联网+大赛模板成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/workflow.tlxy/proModelMdGc/?repage";
	}

	@RequestMapping(value = "export")
	public void export(ProModelTlxy proModelTlxy, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
//		List<ProModelMdGcVo> proModelMdGcVOS = proModelMdGcService.exportData(new Page<ProModelMdGc>(request, response), proModelMdGc);
//		ExportParams params = new ExportParams("填写数据说明：红色名称为必填信息。项目年份举例：2016。", "大赛导出信息", ExcelType.XSSF);
//		params.setFreezeCol(2);
//		map.put(NormalExcelConstants.DATA_LIST, proModelMdGcVOS);
//		map.put(NormalExcelConstants.CLASS, ProModelMdGcVo.class);
//		map.put(NormalExcelConstants.PARAMS, params);
//		map.put(NormalExcelConstants.FILE_NAME, "大赛导出信息");
//		PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
		proModelTlxyService.exportDataQuery(request, response, proModelTlxy);
	}

	// ajax 批量审核评分
	@RequestMapping(value = "ajax/ajaxSaveNum")
	@ResponseBody
	//proModelId 选中项目promodeid
	//num 修改后编号页面传值
	//type 修改级别
	public JSONObject ajaxSaveNum(String  proModelId, String num, String type) {
		return proModelTlxyService.ajaxSaveNum(proModelId,num,type);
	}

}