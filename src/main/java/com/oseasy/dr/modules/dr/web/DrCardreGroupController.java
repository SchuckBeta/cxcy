package com.oseasy.dr.modules.dr.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.dr.modules.dr.service.DrCardreGitemService;
import com.oseasy.dr.modules.dr.service.DrCardreGroupService;
import com.oseasy.dr.modules.dr.vo.GitemEstatus;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 卡记录规则组Controller.
 * @author chenh
 * @version 2018-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCardreGroup")
public class DrCardreGroupController extends BaseController {

	@Autowired
	private DrCardreGroupService drCardreGroupService;
	@Autowired
	private DrCardreGitemService drCardreGitemService;

	@ModelAttribute
	public DrCardreGroup get(@RequestParam(required=false) String id) {
		DrCardreGroup entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drCardreGroupService.get(id);
		}
		if (entity == null){
			entity = new DrCardreGroup();
		}
		return entity;
	}

	@RequiresPermissions("dr:drCardreGroup:view")
	@RequestMapping(value = {"list", ""})
	public String list(DrCardreGroup drCardreGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrCardreGroup> page = drCardreGroupService.findPageByg(new Page<DrCardreGroup>(request, response), drCardreGroup);
		model.addAttribute("page", page);
        model.addAttribute(GitemEstatus.GE_STATUSS, GitemEstatus.getAll());
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardreGroupList";
	}

	@RequiresPermissions("dr:drCardreGroup:view")
	@RequestMapping(value = "form")
	public String form(DrCardreGroup drCardreGroup, Model model) {
		model.addAttribute("drCardreGroup", drCardreGroup);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardreGroupForm";
	}

	@RequiresPermissions("dr:drCardreGroup:edit")
	@RequestMapping(value = "save")
	public String save(DrCardreGroup drCardreGroup, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, drCardreGroup)){
			return form(drCardreGroup, model);
		}
		drCardreGroupService.save(drCardreGroup);
		addMessage(redirectAttributes, "保存卡记录规则组成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardreGroup/?repage";
	}

	@RequiresPermissions("dr:drCardreGroup:edit")
	@RequestMapping(value = "delete")
	public String delete(DrCardreGroup drCardreGroup, RedirectAttributes redirectAttributes) {
		drCardreGroupService.delete(drCardreGroup);
		addMessage(redirectAttributes, "删除卡记录规则组成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardreGroup/?repage";
	}

    /**
     * 禁用规则组信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxIsShow/{id}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxIsShow(@PathVariable("id") String id, @RequestParam(required = true) Boolean isShow, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(id) || (isShow == null)){
            return new ApiTstatus<String>(false, "更新失败，ID和isShow不能为空");
        }
        drCardreGroupService.updateIsShow(new DrCardreGroup(id, isShow));
        return new ApiTstatus<String>(true, "更新成功", id);
    }

    /**
     * 删除规则组信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDelete/{id}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxDelete(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(id)){
            return new ApiTstatus<String>(false, "删除失败");
        }
        drCardreGroupService.deleteWL(new DrCardreGroup(id));
        drCardreGitemService.deleteWLPLByGid(id);
        return new ApiTstatus<String>(true, "删除成功", id);
    }

    /**
     * 获取规则组信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGroupAlls", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<Page<DrCardreGroup>> ajaxGroupAlls(DrCardreGroup drCardreGroup, Boolean isAll, HttpServletRequest request, HttpServletResponse response) {
        if(isAll){
            return new ApiTstatus<Page<DrCardreGroup>>(true, "查询成功", drCardreGroupService.findAllPageByg(new Page<DrCardreGroup>(request, response), drCardreGroup));
        }else{
            return new ApiTstatus<Page<DrCardreGroup>>(true, "查询成功", drCardreGroupService.findPageByg(new Page<DrCardreGroup>(request, response), drCardreGroup));
        }
    }

    /**
     * 保存修改规则组信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxSaveGroup", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCardreGroup> ajaxSaveGroup(DrCardreGroup drCardreGroup, HttpServletRequest request, HttpServletResponse response) {
        if((drCardreGroup == null) || StringUtil.isEmpty(drCardreGroup.getName())){
            return new ApiTstatus<DrCardreGroup>(false, "名称参数不能为空");
        }
        drCardreGroupService.save(drCardreGroup);
        return new ApiTstatus<DrCardreGroup>(true, "保存成功", drCardreGroup);
    }
}