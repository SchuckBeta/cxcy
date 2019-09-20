/**
 *
 */
package com.oseasy.cms.modules.cms.web;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.cms.modules.cms.entity.Link;
import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.cms.modules.cms.service.LinkService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 链接Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/cms/link")
public class LinkController extends BaseController {

	@Autowired
	private LinkService linkService;
	@Autowired
	private CategoryService categoryService;

	@ModelAttribute
	public Link get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			return linkService.get(id);
		}else{
			return new Link();
		}
	}

	@RequiresPermissions("cms:link:view")
	@RequestMapping(value = {"list", ""})
	public String list(Link link, HttpServletRequest request, HttpServletResponse response, Model model) {
//		User user = UserUtils.getUser();
//		if (!user.isAdmin() && !SecurityUtils.getSubject().isPermitted("cms:link:audit")) {
//			link.setUser(user);
//		}
        Page<Link> page = linkService.findPage(new Page<Link>(request, response), link, true);
        model.addAttribute("page", page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "linkList";
	}

	@RequiresPermissions("cms:link:view")
	@RequestMapping(value = "form")
	public String form(Link link, Model model) {
		// 如果当前传参有子节点，则选择取消传参选择
		if (link.getCategory()!=null && StringUtil.isNotBlank(link.getCategory().getId())) {
			List<Category> list = categoryService.findByParentId(link.getCategory().getId(), Site.getCurrentSiteId());
			if (list.size() > 0) {
				link.setCategory(null);
			}else{
				link.setCategory(categoryService.get(link.getCategory().getId()));
			}
		}
		model.addAttribute("link", link);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "linkForm";
	}

	@RequiresPermissions("cms:link:edit")
	@RequestMapping(value = "save")
	public String save(Link link, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, link)) {
			return form(link, model);
		}
		linkService.save(link);
		addMessage(redirectAttributes, "保存链接'" + StringUtil.abbr(link.getTitle(),50) + "'成功");
		return CoreSval.REDIRECT + adminPath + "/cms/link/?repage&category.id="+link.getCategory().getId();
	}

	@RequiresPermissions("cms:link:edit")
	@RequestMapping(value = "delete")
	public String delete(Link link, String categoryId, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		linkService.delete(link, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"发布":"删除")+"链接成功");
		return CoreSval.REDIRECT + adminPath + "/cms/link/?repage&category.id="+categoryId;
	}

	/**
	 * 链接选择列表
	 * @deprecated
	 */
	@RequiresPermissions("cms:link:view")
	@RequestMapping(value = "selectList")
	public String selectList(Link link, HttpServletRequest request, HttpServletResponse response, Model model) {
        list(link, request, response, model);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "linkSelectList";
	}

	/**
	 * 通过编号获取链接名称
	 */
	@RequiresPermissions("cms:link:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids) {
		List<Object[]> list = linkService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
	}
}
