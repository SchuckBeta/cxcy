package com.oseasy.pro.modules.cert.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.cert.entity.SysCertPage;
import com.oseasy.pro.modules.cert.service.SysCertPageService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 证书模板页面Controller.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Controller
@RequestMapping(value = "${adminPath}/cert/sysCertPage")
public class SysCertPageController extends BaseController {

	@Autowired
	private SysCertPageService sysCertPageService;

	@ModelAttribute
	public SysCertPage get(@RequestParam(required=false) String id) {
		SysCertPage entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = sysCertPageService.get(id);
		}
		if (entity == null){
			entity = new SysCertPage();
		}
		return entity;
	}

	@RequiresPermissions("cert:sysCertPage:edit")
	@RequestMapping(value = "delete")
	public String delete(SysCertPage sysCertPage, RedirectAttributes redirectAttributes) {
		sysCertPageService.delete(sysCertPage);
		addMessage(redirectAttributes, "删除证书模板页面成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cert/sysCertPage/?repage";
	}

}