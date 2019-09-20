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
import com.oseasy.pro.modules.cert.entity.SysCertPageIns;
import com.oseasy.pro.modules.cert.service.SysCertPageInsService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 证书颁发记录页面表Controller.
 * @author 奔波儿灞
 * @version 2018-02-09
 */
@Controller
@RequestMapping(value = "${adminPath}/cert/sysCertPageIns")
public class SysCertPageInsController extends BaseController {

	@Autowired
	private SysCertPageInsService sysCertPageInsService;

	@ModelAttribute
	public SysCertPageIns get(@RequestParam(required=false) String id) {
		SysCertPageIns entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = sysCertPageInsService.get(id);
		}
		if (entity == null){
			entity = new SysCertPageIns();
		}
		return entity;
	}

	@RequiresPermissions("cert:sysCertPageIns:edit")
	@RequestMapping(value = "delete")
	public String delete(SysCertPageIns sysCertPageIns, RedirectAttributes redirectAttributes) {
		sysCertPageInsService.delete(sysCertPageIns);
		addMessage(redirectAttributes, "删除证书颁发记录页面表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cert/sysCertPageIns/?repage";
	}

}