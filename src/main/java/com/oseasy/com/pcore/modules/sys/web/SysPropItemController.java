package com.oseasy.com.pcore.modules.sys.web;

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
import com.oseasy.com.pcore.modules.sys.entity.SysPropItem;
import com.oseasy.com.pcore.modules.sys.service.SysPropItemService;
import com.oseasy.com.pcore.modules.sys.service.SysPropService;
import com.oseasy.com.pcore.modules.sys.vo.CorePropType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统功能配置项Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysPropItem")
public class SysPropItemController extends BaseController {

	@Autowired
	private SysPropService sysPropService;
	@Autowired
	private SysPropItemService sysPropItemService;

	@ModelAttribute
	public SysPropItem get(@RequestParam(required=false) String id) {
		SysPropItem entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = sysPropItemService.get(id);
		}
		if (entity == null){
			entity = new SysPropItem();
		}
		return entity;
	}

	@RequiresPermissions("sys:sysPropItem:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysPropItem sysPropItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysPropItem> page = sysPropItemService.findPage(new Page<SysPropItem>(request, response), sysPropItem);
		model.addAttribute("page", page);
        model.addAttribute(CorePropType.SYS_PROP_TYPES, CorePropType.values());
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysPropItemList";
	}

    @RequiresPermissions("sys:sysPropItem:view")
    @RequestMapping(value = "form")
    public String form(SysPropItem sysPropItem, Model model) {
        if(sysPropItem.getProp() != null){
            model.addAttribute("sysProp", sysPropService.get(sysPropItem.getProp().getId()));
        }
        model.addAttribute("sysPropItem", sysPropItem);
        model.addAttribute(CorePropType.SYS_PROP_TYPES, CorePropType.values());
        return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysPropItemForm";
    }

	@RequiresPermissions("sys:sysPropItem:edit")
	@RequestMapping(value = "save")
	public String save(SysPropItem sysPropItem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysPropItem)){
			return form(sysPropItem, model);
		}
		sysPropItemService.save(sysPropItem);
		addMessage(redirectAttributes, "保存系统功能配置项成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/sysPropItem/?repage";
	}

	@RequiresPermissions("sys:sysPropItem:edit")
	@RequestMapping(value = "delete")
	public String delete(SysPropItem sysPropItem, RedirectAttributes redirectAttributes) {
		sysPropItemService.delete(sysPropItem);
		addMessage(redirectAttributes, "删除系统功能配置项成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/sys/sysPropItem/?repage";
	}

}