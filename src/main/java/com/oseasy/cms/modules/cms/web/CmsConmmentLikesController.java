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
import com.oseasy.cms.modules.cms.entity.CmsConmmentLikes;
import com.oseasy.cms.modules.cms.service.CmsConmmentLikesService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 评论Controller.
 * @author chenh
 * @version 2018-09-04
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/cmsConmmentLikes")
public class CmsConmmentLikesController extends BaseController {

	@Autowired
	private CmsConmmentLikesService cmsConmmentLikesService;

	@ModelAttribute
	public CmsConmmentLikes get(@RequestParam(required=false) String id) {
		CmsConmmentLikes entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsConmmentLikesService.get(id);
		}
		if (entity == null){
			entity = new CmsConmmentLikes();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsConmmentLikes:view")
	@RequestMapping(value = {"list", ""})
	public String list(CmsConmmentLikes cmsConmmentLikes, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsConmmentLikes> page = cmsConmmentLikesService.findPage(new Page<CmsConmmentLikes>(request, response), cmsConmmentLikes);
		model.addAttribute(Page.PAGE, page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsConmmentLikesList";
	}

	@RequiresPermissions("cms:cmsConmmentLikes:view")
	@RequestMapping(value = "form")
	public String form(CmsConmmentLikes cmsConmmentLikes, Model model) {
		model.addAttribute("cmsConmmentLikes", cmsConmmentLikes);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsConmmentLikesForm";
	}

	@RequiresPermissions("cms:cmsConmmentLikes:edit")
	@RequestMapping(value = "save")
	public String save(CmsConmmentLikes cmsConmmentLikes, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, cmsConmmentLikes)){
			return form(cmsConmmentLikes, model);
		}
		cmsConmmentLikesService.save(cmsConmmentLikes);
		addMessage(redirectAttributes, "保存评论点赞成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsConmmentLikes/?repage";
	}

	@RequiresPermissions("cms:cmsConmmentLikes:edit")
	@RequestMapping(value = "delete")
	public String delete(CmsConmmentLikes cmsConmmentLikes, RedirectAttributes redirectAttributes) {
		cmsConmmentLikesService.delete(cmsConmmentLikes);
		addMessage(redirectAttributes, "删除评论点赞成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsConmmentLikes/?repage";
	}

}