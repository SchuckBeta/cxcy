package com.oseasy.sys.modules.sys.web;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.config.SysSval.SysEmskey;
import com.oseasy.sys.modules.sys.entity.SysStudentExpansion;
import com.oseasy.sys.modules.sys.entity.UserInfo;
import com.oseasy.sys.modules.sys.service.SysStudentExpansionService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生信息表Controller
 * @author zy
 * @version 2017-03-14
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysStudentExpansion")
public class SysStudentExpansionController extends BaseController {

	@Autowired
	private SysStudentExpansionService sysStudentExpansionService;

	@ModelAttribute
	public SysStudentExpansion get(@RequestParam(required=false) String id) {
		SysStudentExpansion entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = sysStudentExpansionService.get(id);
		}
		if (entity == null) {
			entity = new SysStudentExpansion();
		}
		return entity;
	}

	/*@RequiresPermissions("sys:sysStudentExpansion:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysStudentExpansion sysStudentExpansion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysStudentExpansion> page = sysStudentExpansionService.findPage(new Page<SysStudentExpansion>(request, response), sysStudentExpansion);
		model.addAttribute("page", page);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "sysStudentExpansionList";
	}*/

	@RequiresPermissions("sys:sysStudentExpansion:view")
	@RequestMapping(value = "form")
	public String form(SysStudentExpansion sysStudentExpansion, Model model) {
		model.addAttribute("sysStudentExpansion", sysStudentExpansion);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "sysStudentExpansionForm";
	}

	@RequiresPermissions("sys:sysStudentExpansion:edit")
	@RequestMapping(value = "save")
	public String save(SysStudentExpansion sysStudentExpansion, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysStudentExpansion)) {
			return form(sysStudentExpansion, model);
		}
		sysStudentExpansionService.save(sysStudentExpansion);
		addMessage(redirectAttributes, "保存学生信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/sysStudentExpansion/?repage";
	}

	@RequiresPermissions("sys:sysStudentExpansion:edit")
	@RequestMapping(value = "delete")
	public String delete(SysStudentExpansion sysStudentExpansion, RedirectAttributes redirectAttributes) {
		sysStudentExpansionService.delete(sysStudentExpansion);
		addMessage(redirectAttributes, "删除学生信息成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/sysStudentExpansion/?repage";
	}


	@RequestMapping(value = "findUserInfo")
	public String findUserInfo(Model model) {
		User user=UserUtils.getUser();//获取当前登录的用户信息
		UserInfo userInfo=sysStudentExpansionService.findUserInfo(user.getId());
		model.addAttribute("userInfo", userInfo);
		return SysSval.path.vms(SysEmskey.SYS.k()) + "displayUserInfo";
	}

}