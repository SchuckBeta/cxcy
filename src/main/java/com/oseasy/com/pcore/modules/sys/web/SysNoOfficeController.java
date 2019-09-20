package com.oseasy.com.pcore.modules.sys.web;

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
import com.oseasy.com.pcore.modules.sys.entity.SysNoOffice;
import com.oseasy.com.pcore.modules.sys.service.SysNoOfficeService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统机构编号Controller.
 * @author chenhao
 * @version 2017-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysNoOffice")
public class SysNoOfficeController extends BaseController {

	@Autowired
	private SysNoOfficeService sysNoOfficeService;

	@ModelAttribute
	public SysNoOffice get(@RequestParam(required=false) String id) {
		SysNoOffice entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = sysNoOfficeService.get(id);
		}
		if (entity == null) {
			entity = new SysNoOffice();
		}
		return entity;
	}

	@RequiresPermissions("sys:sysNoOffice:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysNoOffice sysNoOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysNoOffice> page = sysNoOfficeService.findPage(new Page<SysNoOffice>(request, response), sysNoOffice);
		model.addAttribute("page", page);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysNoOfficeList";
	}

	@RequiresPermissions("sys:sysNoOffice:view")
	@RequestMapping(value = "form")
	public String form(SysNoOffice sysNoOffice, Model model) {
		model.addAttribute("sysNoOffice", sysNoOffice);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysNoOfficeForm";
	}

	@RequiresPermissions("sys:sysNoOffice:edit")
	@RequestMapping(value = "save")
	public String save(SysNoOffice sysNoOffice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysNoOffice)) {
			return form(sysNoOffice, model);
		}
		sysNoOfficeService.save(sysNoOffice);
		addMessage(redirectAttributes, "保存系统机构编号成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/sysNoOffice/?repage";
	}

	@RequiresPermissions("sys:sysNoOffice:edit")
	@RequestMapping(value = "delete")
	public String delete(SysNoOffice sysNoOffice, RedirectAttributes redirectAttributes) {
		sysNoOfficeService.delete(sysNoOffice);
		addMessage(redirectAttributes, "删除系统机构编号成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/sysNoOffice/?repage";
	}

}