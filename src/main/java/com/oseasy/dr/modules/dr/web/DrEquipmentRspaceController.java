package com.oseasy.dr.modules.dr.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.service.DrEquipmentRspaceService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 门禁设备场地Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drEquipmentRspace")
public class DrEquipmentRspaceController extends BaseController {

	@Autowired
	private DrEquipmentRspaceService drEquipmentRspaceService;

	@ModelAttribute
	public DrEquipmentRspace get(@RequestParam(required=false) String id) {
		DrEquipmentRspace entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drEquipmentRspaceService.get(id);
		}
		if (entity == null){
			entity = new DrEquipmentRspace();
		}
		return entity;
	}

	@RequiresPermissions("dr:drEquipmentRspace:view")
	@RequestMapping(value = {"list", ""})
	public String list(DrEquipmentRspace drEquipmentRspace, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrEquipmentRspace> page = drEquipmentRspaceService.findPage(new Page<DrEquipmentRspace>(request, response), drEquipmentRspace);
		model.addAttribute("page", page);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drEquipmentRspaceList";
	}

	@RequiresPermissions("dr:drEquipmentRspace:view")
	@RequestMapping(value = "form")
	public String form(DrEquipmentRspace drEquipmentRspace, Model model) {
		model.addAttribute("drEquipmentRspace", drEquipmentRspace);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drEquipmentRspaceForm";
	}

	@RequiresPermissions("dr:drEquipmentRspace:edit")
	@RequestMapping(value = "save")
	public String save(DrEquipmentRspace drEquipmentRspace, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, drEquipmentRspace)){
			return form(drEquipmentRspace, model);
		}
		drEquipmentRspaceService.save(drEquipmentRspace);
		addMessage(redirectAttributes, "保存门禁设备场地成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drEquipmentRspace/?repage";
	}

	@RequiresPermissions("dr:drEquipmentRspace:edit")
	@RequestMapping(value = "delete")
	public String delete(DrEquipmentRspace drEquipmentRspace, RedirectAttributes redirectAttributes) {
		drEquipmentRspaceService.delete(drEquipmentRspace);
		addMessage(redirectAttributes, "删除门禁设备场地成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drEquipmentRspace/?repage";
	}

	@RequestMapping(value = "ajaxSaveDoorRelation" ,method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public JSONObject ajaxSaveDoorRelation(@RequestBody JSONObject gps) {
		JSONArray jsData=gps.getJSONArray("deviceList");
		JSONObject js=drEquipmentRspaceService.saveDoorRelation(jsData);
		return js;
	}

	@RequestMapping(value = "ajaxDeleDoorRelation")
	@ResponseBody
	public JSONObject ajaxDeleDoorRelation(DrEquipmentRspace drEquipmentRspace, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {

		JSONObject js=drEquipmentRspaceService.deleDoorRelation(drEquipmentRspace.getId());
		return js;
	}

	/**
	 * 开门
	 * @param epmentid 设备ID(epment表主键)
	 * @param doorNo 门编号
	 * @return
	 */
	@RequestMapping(value = "/openEntranceGuardDoor", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiStatus openEntranceGuardDoor(@RequestParam String epmentid, @RequestParam String doorNo) {
		drEquipmentRspaceService.openEntranceGuardDoor(epmentid, doorNo);
		return new ApiStatus(true, "处理中！");
	}

	/**
	 * 关门
	 * @param epmentid 设备ID(epment表主键)
	 * @param doorNo 门编号
	 * @return
	 */
	@RequestMapping(value = "/closeEntranceGuardDoor", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiStatus closeEntranceGuardDoor(@RequestParam String epmentid, @RequestParam String doorNo) {
		drEquipmentRspaceService.closeEntranceGuardDoor(epmentid, doorNo);
		return new ApiStatus(true, "处理中！");
	}

	/**
     * 获取处理中状态
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDealing", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrEquipmentRspace>> ajaxDealing(String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<DrEquipmentRspace>>(true, "没有设备需要处理");
        }
        DrEquipmentRspace drErspace = new DrEquipmentRspace();
        drErspace.setIds(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
        List<DrEquipmentRspace> drErspaces = drEquipmentRspaceService.findList(drErspace);
        return new ApiTstatus<List<DrEquipmentRspace>>(true, "设备处理成功！", drErspaces);
    }

    /**
     * 重置处理中状态
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDealReset", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrEquipment>> ajaxDealReset(String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<DrEquipment>>(true, "没有需要重置");
        }
        drEquipmentRspaceService.updateDealStatusByPl(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)), DrCdealStatus.DCD_FAIL.getKey());
        return new ApiTstatus<List<DrEquipment>>(true, "重置成功！");
    }
}