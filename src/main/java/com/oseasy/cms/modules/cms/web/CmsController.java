/**
 *
 */
package com.oseasy.cms.modules.cms.web;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;

/**
 * 内容管理Controller
 *
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class CmsController extends BaseController {
	public static final String CMS_FORM_ADMIN = CoreSval.getAdminPath() + "/cms/form/";
	@Autowired
	private CategoryService categoryService;

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "")
	public String index() {
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsIndex";
	}

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "tree")
	public String tree(Model model) {
		model.addAttribute("categoryList", categoryService.find(true, null));
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsTree";
	}

	@RequiresPermissions("cms:view")
	@RequestMapping(value = "none")
	public String none() {
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsNone";
	}

}
