package com.oseasy.sys.modules.sys.web;

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
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.entity.TeacherKeyword;
import com.oseasy.sys.modules.sys.service.TeacherKeywordService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * teacherKeywordController.
 * @author zy
 * @version 2017-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/teacherkeyword/teacherKeyword")
public class TeacherKeywordController extends BaseController {

	@Autowired
	private TeacherKeywordService teacherKeywordService;

	@ModelAttribute
	public TeacherKeyword get(@RequestParam(required=false) String id) {
		TeacherKeyword entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = teacherKeywordService.get(id);
		}
		if (entity == null) {
			entity = new TeacherKeyword();
		}
		return entity;
	}

	@RequiresPermissions("sys:teacherkeyword:teacherKeyword:view")
	@RequestMapping(value = {"list", ""})
	public String list(TeacherKeyword teacherKeyword, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TeacherKeyword> page = teacherKeywordService.findPage(new Page<TeacherKeyword>(request, response), teacherKeyword);
		model.addAttribute("page", page);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "teacherkeyword/teacherKeywordList";
	}

	@RequiresPermissions("sys:teacherkeyword:teacherKeyword:view")
	@RequestMapping(value = "form")
	public String form(TeacherKeyword teacherKeyword, Model model) {
		model.addAttribute("teacherKeyword", teacherKeyword);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "teacherkeyword/teacherKeywordForm";
	}

	@RequiresPermissions("sys:teacherkeyword:teacherKeyword:edit")
	@RequestMapping(value = "save")
	public String save(TeacherKeyword teacherKeyword, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, teacherKeyword)) {
			return form(teacherKeyword, model);
		}
		teacherKeywordService.save(teacherKeyword);
		addMessage(redirectAttributes, "保存teacherKeyword成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/teacherkeyword/teacherKeyword/?repage";
	}

	@RequiresPermissions("sys:teacherkeyword:teacherKeyword:edit")
	@RequestMapping(value = "delete")
	public String delete(TeacherKeyword teacherKeyword, RedirectAttributes redirectAttributes) {
		teacherKeywordService.delete(teacherKeyword);
		addMessage(redirectAttributes, "删除teacherKeyword成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/teacherkeyword/teacherKeyword/?repage";
	}

}