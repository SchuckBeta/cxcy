package com.oseasy.pro.modules.excellent.web;

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
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.config.ProSval.ProEmskey;
import com.oseasy.pro.modules.excellent.entity.ExcellentKeyword;
import com.oseasy.pro.modules.excellent.service.ExcellentKeywordService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 优秀展示关键词Controller.
 * @author 9527
 * @version 2017-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/excellent/excellentKeyword")
public class ExcellentKeywordController extends BaseController {

	@Autowired
	private ExcellentKeywordService excellentKeywordService;

	@ModelAttribute
	public ExcellentKeyword get(@RequestParam(required=false) String id) {
		ExcellentKeyword entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = excellentKeywordService.get(id);
		}
		if (entity == null) {
			entity = new ExcellentKeyword();
		}
		return entity;
	}

    /**
     * @deprecated
     */
	@RequiresPermissions("excellent:excellentKeyword:view")
	@RequestMapping(value = {"list", ""})
	public String list(ExcellentKeyword excellentKeyword, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExcellentKeyword> page = excellentKeywordService.findPage(new Page<ExcellentKeyword>(request, response), excellentKeyword);
		model.addAttribute("page", page);
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "excellentKeywordList";
	}

	/**
	 * @deprecated
	 */
	@RequiresPermissions("excellent:excellentKeyword:view")
	@RequestMapping(value = "form")
	public String form(ExcellentKeyword excellentKeyword, Model model) {
		model.addAttribute("excellentKeyword", excellentKeyword);
		return ProSval.path.vms(ProEmskey.EXCELLENT.k()) + "excellentKeywordForm";
	}

	@RequiresPermissions("excellent:excellentKeyword:edit")
	@RequestMapping(value = "save")
	public String save(ExcellentKeyword excellentKeyword, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, excellentKeyword)) {
			return form(excellentKeyword, model);
		}
		excellentKeywordService.save(excellentKeyword);
		addMessage(redirectAttributes, "保存优秀展示关键词成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/excellent/excellentKeyword/?repage";
	}

	@RequiresPermissions("excellent:excellentKeyword:edit")
	@RequestMapping(value = "delete")
	public String delete(ExcellentKeyword excellentKeyword, RedirectAttributes redirectAttributes) {
		excellentKeywordService.delete(excellentKeyword);
		addMessage(redirectAttributes, "删除优秀展示关键词成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/excellent/excellentKeyword/?repage";
	}

}