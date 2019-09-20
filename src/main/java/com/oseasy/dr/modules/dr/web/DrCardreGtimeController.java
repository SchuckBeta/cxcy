package com.oseasy.dr.modules.dr.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.dr.common.config.DrIds;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.dr.modules.dr.entity.DrCardreGtime;
import com.oseasy.dr.modules.dr.service.DrCardreGroupService;
import com.oseasy.dr.modules.dr.service.DrCardreGtimeService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 卡记录规则时间Controller.
 * @author chenh
 * @version 2018-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCardreGtime")
public class DrCardreGtimeController extends BaseController {

	@Autowired
	private DrCardreGtimeService drCardreGtimeService;
	@Autowired
	private DrCardreGroupService drCardreGroupService;

	@ModelAttribute
	public DrCardreGtime get(@RequestParam(required=false) String id) {
		DrCardreGtime entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drCardreGtimeService.get(id);
		}
		if (entity == null){
			entity = new DrCardreGtime();
		}
		return entity;
	}

	@RequiresPermissions("dr:drCardreGtime:view")
	@RequestMapping(value = {"list", ""})
	public String list(DrCardreGtime drCardreGtime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrCardreGtime> page = drCardreGtimeService.findPage(new Page<DrCardreGtime>(request, response), drCardreGtime);
		model.addAttribute("page", page);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardreGtimeList";
	}

	@RequiresPermissions("dr:drCardreGtime:view")
	@RequestMapping(value = "form")
	public String form(DrCardreGtime drCardreGtime, Model model) {
		model.addAttribute("drCardreGtime", drCardreGtime);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardreGtimeForm";
	}

	@RequiresPermissions("dr:drCardreGtime:edit")
	@RequestMapping(value = "save")
	public String save(DrCardreGtime drCardreGtime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, drCardreGtime)){
			return form(drCardreGtime, model);
		}
		drCardreGtimeService.save(drCardreGtime);
		addMessage(redirectAttributes, "保存卡记录规则时间成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardreGtime/?repage";
	}

	@RequiresPermissions("dr:drCardreGtime:edit")
	@RequestMapping(value = "delete")
	public String delete(DrCardreGtime drCardreGtime, RedirectAttributes redirectAttributes) {
		drCardreGtimeService.delete(drCardreGtime);
		addMessage(redirectAttributes, "删除卡记录规则时间成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardreGtime/?repage";
	}

    /**
     * 获取规则组时间信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGtimesByGid/{gid}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCardreGroup> ajaxGtimesByGid(@PathVariable("gid") String gid, HttpServletRequest request, HttpServletResponse response) {
        DrCardreGroup grp = new DrCardreGroup(DrIds.DR_CARD_GID.getId());
        grp.setDrCreGtimes(drCardreGtimeService.findList(new DrCardreGtime(grp)));
        return new ApiTstatus<DrCardreGroup>(true, "查询成功", grp);
    }

    /**
     * 保存修改规则组时间信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxSavePl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCardreGroup> ajaxSavePl(@RequestBody JSONObject group, HttpServletRequest request, HttpServletResponse response) {
        DrCardreGroup drCardreGroup = (DrCardreGroup) JsonMapper.fromJsonString(group.toString(), DrCardreGroup.class);
        if((drCardreGroup == null) || StringUtil.isEmpty(drCardreGroup.getId())){
            return new ApiTstatus<DrCardreGroup>(false, "规则参数不能为空（ID）");
        }
        if(StringUtil.isEmpty(drCardreGroup.getId())){
            drCardreGroup.setId(DrIds.DR_CARD_GID.getId());
        }
        if(StringUtil.checkEmpty(drCardreGroup.getDrCreGtimes())){
            return new ApiTstatus<DrCardreGroup>(false, "规则时间不能为空（ID）");
        }
        drCardreGtimeService.deleteWLPLByGid(drCardreGroup.getId());
        drCardreGtimeService.savePl(drCardreGroup.getDrCreGtimes());
        DrCardreGroup grp = new DrCardreGroup(DrIds.DR_CARD_GID.getId());
        grp.setDrCreGtimes(drCardreGtimeService.findList(new DrCardreGtime(grp)));
        return new ApiTstatus<DrCardreGroup>(true, "保存完成", grp);
    }

    /**
     * 保存修改规则组时间信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDelete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<String>> ajaxDelete(String ids, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<String>>(false, "规则参数不能为空（IDS）");
        }
        List<String> idList = Arrays.asList(StringUtil.split(ids));
        drCardreGtimeService.deleteWLPLByIds(idList);
        return new ApiTstatus<List<String>>(true, "更新完成", idList);
    }

    /**
     * 保存修改规则组时间信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDeletePlByGid", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCardreGroup> ajaxDeletePlByGid(DrCardreGroup drCardreGroup, HttpServletRequest request, HttpServletResponse response) {
        if((drCardreGroup == null) || StringUtil.isEmpty(drCardreGroup.getId())){
            return new ApiTstatus<DrCardreGroup>(false, "规则参数不能为空（ID）");
        }
        drCardreGtimeService.deleteWLPLByGid(drCardreGroup.getId());
        drCardreGtimeService.savePl(drCardreGroup.getDrCreGtimes());
        return new ApiTstatus<DrCardreGroup>(true, "保存完成", drCardreGroupService.getByg(drCardreGroup.getId()));
    }

}