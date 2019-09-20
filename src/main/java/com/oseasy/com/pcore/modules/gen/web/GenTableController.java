/**
 *
 */
package com.oseasy.com.pcore.modules.gen.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.gen.entity.GenScheme;
import com.oseasy.com.pcore.modules.gen.entity.GenTable;
import com.oseasy.com.pcore.modules.gen.service.GenSchemeService;
import com.oseasy.com.pcore.modules.gen.service.GenTableService;
import com.oseasy.com.pcore.modules.gen.util.GenUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 业务表Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/gen/genTable")
public class GenTableController extends BaseController {

	@Autowired
	private GenSchemeService genSchemeService;
	@Autowired
	private GenTableService genTableService;

	@ModelAttribute
	public GenTable get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			return genTableService.get(id);
		}else{
			return new GenTable();
		}
	}

//	@RequiresPermissions("gen:genTable:view")
	@RequestMapping(value = {"list", ""})
	public String list(GenTable genTable, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.getAdmin()) {
			genTable.setCreateBy(user);
		}
        Page<GenTable> page = genTableService.find(new Page<GenTable>(request, response), genTable);
        model.addAttribute("page", page);
		return CoreSval.path.vms(CoreEmskey.GEN.k()) + "genTableList";
	}

//	@RequiresPermissions("gen:genTable:view")
	@RequestMapping(value = "form")
	public String form(GenTable genTable, Model model) {
		// 获取物理表列表
		List<GenTable> tableList = genTableService.findTableListFormDb(new GenTable());
		model.addAttribute("tableList", tableList);
		// 验证表是否存在
		if (StringUtil.isBlank(genTable.getId()) && !genTableService.checkTableName(genTable.getName())) {
			addMessage(model, "下一步失败！" + genTable.getName() + " 表已经添加！");
			genTable.setName("");
		}
		// 获取物理表字段
		else{
			genTable = genTableService.getTableFormDb(genTable);
		}
		model.addAttribute("genTable", genTable);
		model.addAttribute("config", GenUtils.getConfig());
//		model.addAttribute("config", GenUtils.getConfig());
        model.addAttribute("config", GenUtils.getConfig(genSchemeService.findClass(new GenScheme())));
		return CoreSval.path.vms(CoreEmskey.GEN.k()) + "genTableForm";
	}

//	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "save")
	public String save(GenTable genTable, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, genTable)) {
			return form(genTable, model);
		}
		// 验证表是否已经存在
		if (StringUtil.isBlank(genTable.getId()) && !genTableService.checkTableName(genTable.getName())) {
			addMessage(model, "保存失败！" + genTable.getName() + " 表已经存在！");
			genTable.setName("");
			return form(genTable, model);
		}
		genTableService.save(genTable);
		addMessage(redirectAttributes, "保存业务表'" + genTable.getName() + "'成功");
		return CoreSval.REDIRECT + adminPath + "/gen/genTable/?repage";
	}

	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "delete")
	public String delete(GenTable genTable, RedirectAttributes redirectAttributes) {
		genTableService.delete(genTable);
		addMessage(redirectAttributes, "删除业务表成功");
		return CoreSval.REDIRECT + adminPath + "/gen/genTable/?repage";
	}

}
