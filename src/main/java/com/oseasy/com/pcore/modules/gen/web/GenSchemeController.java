/**
 *
 */
package com.oseasy.com.pcore.modules.gen.web;

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
import com.oseasy.com.pcore.modules.gen.service.GenSchemeService;
import com.oseasy.com.pcore.modules.gen.service.GenTableService;
import com.oseasy.com.pcore.modules.gen.util.GenUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 生成方案Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/gen/genScheme")
public class GenSchemeController extends BaseController {

	@Autowired
	private GenSchemeService genSchemeService;

	@Autowired
	private GenTableService genTableService;

	@ModelAttribute
	public GenScheme get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			return genSchemeService.get(id);
		}else{
			return new GenScheme();
		}
	}

//	@RequiresPermissions("gen:genScheme:view")
	@RequestMapping(value = {"list", ""})
	public String list(GenScheme genScheme, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.getAdmin()) {
			genScheme.setCreateBy(user);
		}
        Page<GenScheme> page = genSchemeService.find(new Page<GenScheme>(request, response), genScheme);
        model.addAttribute("page", page);
		return CoreSval.path.vms(CoreEmskey.GEN.k()) + "genSchemeList";
	}

//	@RequiresPermissions("gen:genScheme:view")
	@RequestMapping(value = "form")
	public String form(GenScheme genScheme, Model model) {
		if (StringUtil.isBlank(genScheme.getPackageName())) {
			genScheme.setPackageName("com.oseasy.demo.modules");
		}
//		if (StringUtil.isBlank(genScheme.getFunctionAuthor())) {
//			genScheme.setFunctionAuthor(UserUtils.getUser().getName());
//		}
		model.addAttribute("genScheme", genScheme);
//		model.addAttribute("config", GenUtils.getConfig());
		model.addAttribute("config", GenUtils.getConfig(genSchemeService.findClass(new GenScheme())));
		model.addAttribute("tableList", genTableService.findAll());
		return CoreSval.path.vms(CoreEmskey.GEN.k()) + "genSchemeForm";
	}

	@RequiresPermissions("gen:genScheme:edit")
	@RequestMapping(value = "save")
	public String save(GenScheme genScheme, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, genScheme)) {
			return form(genScheme, model);
		}

		String result = genSchemeService.save(genScheme);
		addMessage(redirectAttributes, "操作生成方案'" + genScheme.getName() + "'成功<br/>"+result);
		return CoreSval.REDIRECT + adminPath + "/gen/genScheme/?repage";
	}

//	@RequiresPermissions("gen:genScheme:edit")
	@RequestMapping(value = "delete")
	public String delete(GenScheme genScheme, RedirectAttributes redirectAttributes) {
		genSchemeService.delete(genScheme);
		addMessage(redirectAttributes, "删除生成方案成功");
		return CoreSval.REDIRECT + adminPath + "/gen/genScheme/?repage";
	}

}
