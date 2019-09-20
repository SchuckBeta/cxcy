package com.oseasy.com.pcore.modules.sys.web;

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
import com.oseasy.com.pcore.modules.sys.entity.SysConfig;
import com.oseasy.com.pcore.modules.sys.service.CoreConfigService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统配置Controller.
 * @author 9527
 * @version 2017-10-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sysconfig/sysConfig")
public class SysConfigController extends BaseController {

	@Autowired
	private CoreConfigService sysConfigService;

	@ModelAttribute
	public SysConfig get(@RequestParam(required=false) String id) {
		SysConfig entity = null;
		if (StringUtil.isNotBlank(id)) {
			entity = sysConfigService.get(id);
		}
		if (entity == null) {
			entity = new SysConfig();
		}
		return entity;
	}


	@RequestMapping(value = "form")
	public String form(SysConfig sysConfig, Model model) {
		model.addAttribute("sysConfig", sysConfig);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysConfigForm";
	}

	/**
	 * @deprecated
	 */
	@RequiresPermissions("sysconfig:sysConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysConfig sysConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysConfig> page = sysConfigService.findPage(new Page<SysConfig>(request, response), sysConfig);
		model.addAttribute("page", page);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysConfigList";
	}

	@RequestMapping(value = "properties")
	public String properties(Model model) {
		SysConfig sysConfig=new SysConfig();
		List<SysConfig> sysConfigList=sysConfigService.findList(sysConfig);
		if(StringUtil.checkNotEmpty(sysConfigList)){
			sysConfig = sysConfigList.get(0);
		}
		model.addAttribute("sysConfig", sysConfig);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysConfigForm";
	}

	@RequestMapping(value = "save")
	public String saveSysConfig(SysConfig sysConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysConfig)) {
			return form(sysConfig, model);
		}


		sysConfigService.saveSysConfig(sysConfig);
		addMessage(redirectAttributes, "保存系统配置成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sysconfig/sysConfig/properties?repage";
	}

	@RequiresPermissions("sysconfig:sysConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(SysConfig sysConfig, RedirectAttributes redirectAttributes) {
		sysConfigService.delete(sysConfig);
		addMessage(redirectAttributes, "删除系统配置成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sysconfig/sysConfig/?repage";
	}

}