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
import com.oseasy.pro.modules.cert.entity.SysCertElement;
import com.oseasy.pro.modules.cert.service.SysCertElementService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 证书模板元素Controller.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Controller
@RequestMapping(value = "${adminPath}/cert/sysCertElement")
public class SysCertElementController extends BaseController {

	@Autowired
	private SysCertElementService sysCertElementService;

	@ModelAttribute
	public SysCertElement get(@RequestParam(required=false) String id) {
		SysCertElement entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = sysCertElementService.get(id);
		}
		if (entity == null){
			entity = new SysCertElement();
		}
		return entity;
	}

	@RequiresPermissions("cert:sysCertElement:edit")
	@RequestMapping(value = "delete")
	public String delete(SysCertElement sysCertElement, RedirectAttributes redirectAttributes) {
		sysCertElementService.delete(sysCertElement);
		addMessage(redirectAttributes, "删除证书模板元素成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cert/sysCertElement/?repage";
	}

}