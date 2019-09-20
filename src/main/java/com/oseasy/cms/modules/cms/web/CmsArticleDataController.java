package com.oseasy.cms.modules.cms.web;

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

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.CmsArticleData;
import com.oseasy.cms.modules.cms.service.CmsArticleDataService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 文章详情表Controller.
 * @author liangjie
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsArticleData")
public class CmsArticleDataController extends BaseController {

	@Autowired
	private CmsArticleDataService cmsArticleDataService;

	@ModelAttribute
	public CmsArticleData get(@RequestParam(required=false) String id) {
		CmsArticleData entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsArticleDataService.get(id);
		}
		if (entity == null){
			entity = new CmsArticleData();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsArticleData:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsArticleData cmsArticleData, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsArticleData> page = cmsArticleDataService.findPage(new Page<CmsArticleData>(request, response), cmsArticleData);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsArticleDataList";
	}

	@RequiresPermissions("cms:cmsArticleData:view")
	@RequestMapping(value = "form")
	public String form(CmsArticleData cmsArticleData, Model model) {
		model.addAttribute("cmsArticleData", cmsArticleData);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsArticleDataForm";
	}

	@RequiresPermissions("cms:cmsArticleData:edit")
	@RequestMapping(value = "save")
	public String save(CmsArticleData cmsArticleData, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsArticleData)){
			return form(cmsArticleData, model);
		}
		cmsArticleDataService.save(cmsArticleData);
		addMessage(redirectAttributes, "保存文章详情表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsArticleData/?repage";
	}

	@RequiresPermissions("cms:cmsArticleData:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsArticleData cmsArticleData, RedirectAttributes redirectAttributes) {
		cmsArticleDataService.delete(cmsArticleData);
		addMessage(redirectAttributes, "删除文章详情表成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsArticleData/?repage";
	}

}