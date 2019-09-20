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
import com.oseasy.pro.modules.cert.entity.SysCertIns;
import com.oseasy.pro.modules.cert.service.SysCertInsService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 证书信息记录Controller.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Controller
@RequestMapping(value = "${adminPath}/cert/sysCertIns")
public class SysCertInsController extends BaseController {

	@Autowired
	private SysCertInsService sysCertInsService;

	@ModelAttribute
	public SysCertIns get(@RequestParam(required=false) String id) {
		SysCertIns entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = sysCertInsService.get(id);
		}
		if (entity == null){
			entity = new SysCertIns();
		}
		return entity;
	}

	@RequiresPermissions("cert:sysCertIns:edit")
	@RequestMapping(value = "delete")
	public String delete(SysCertIns sysCertIns, RedirectAttributes redirectAttributes) {
		sysCertInsService.delete(sysCertIns);
		addMessage(redirectAttributes, "删除证书信息记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cert/sysCertIns/?repage";
	}

}