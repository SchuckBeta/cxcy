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
import com.oseasy.pro.modules.cert.entity.SysCertFlow;
import com.oseasy.pro.modules.cert.service.SysCertFlowService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 证书模板-流程节点关系Controller.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Controller
@RequestMapping(value = "${adminPath}/cert/sysCertFlow")
public class SysCertFlowController extends BaseController {

	@Autowired
	private SysCertFlowService sysCertFlowService;

	@ModelAttribute
	public SysCertFlow get(@RequestParam(required=false) String id) {
		SysCertFlow entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = sysCertFlowService.get(id);
		}
		if (entity == null){
			entity = new SysCertFlow();
		}
		return entity;
	}

	@RequiresPermissions("cert:sysCertFlow:edit")
	@RequestMapping(value = "delete")
	public String delete(SysCertFlow sysCertFlow, RedirectAttributes redirectAttributes) {
		sysCertFlowService.delete(sysCertFlow);
		addMessage(redirectAttributes, "删除证书模板-流程节点关系成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cert/sysCertFlow/?repage";
	}

}