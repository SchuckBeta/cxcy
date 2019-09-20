package com.oseasy.dr.modules.dr.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrEquipment;
import com.oseasy.dr.modules.dr.entity.DrEquipmentRspace;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.service.DrEquipmentRspaceService;
import com.oseasy.dr.modules.dr.service.DrEquipmentService;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.dr.modules.dr.vo.DrKey;
import com.oseasy.dr.modules.dr.vo.EType;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpace;
import com.oseasy.pw.modules.pw.service.PwRoomService;
import com.oseasy.pw.modules.pw.service.PwSpaceService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 门禁设备Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drEquipment")
public class DrEquipmentController extends BaseController {

	@Autowired
	private DrEquipmentService drEquipmentService;

	@Autowired
	private DrEquipmentRspaceService drEquipmentRspaceService;

	@Autowired
	private PwSpaceService pwSpaceService;

	@Autowired
	private PwRoomService pwRoomService;

	@ModelAttribute
	public DrEquipment get(@RequestParam(required=false) String id) {
		DrEquipment entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drEquipmentService.get(id);
		}
		if (entity == null){
			entity = new DrEquipment();
		}
		return entity;
	}

	@RequiresPermissions("dr:drEquipment:view")
	@RequestMapping(value = {"list", ""})
	public String list(DrEquipment drEquipment, HttpServletRequest request, HttpServletResponse response, Model model) {
	    drEquipment.setDelFlags(Arrays.asList(new String[]{Const.YES, Const.NO}));
		Page<DrEquipment> page = drEquipmentService.findPage(new Page<DrEquipment>(request, response), drEquipment);
//		List<Object> drKeyList=DrKey.getDrKeys();
//		model.addAttribute("drKeyList", drKeyList);
//		model.addAttribute("page", page);
//        model.addAttribute(DrCdealStatus.DR_CDSTATUSS, DrCdealStatus.getDrCdealStatuss(true));
        List<DrKey> drKeyList=Arrays.asList(DrKey.values());
        model.addAttribute("drKeyList", drKeyList);
        model.addAttribute("page", page);
        model.addAttribute(DrCdealStatus.DR_CDSTATUSS, DrCdealStatus.getAll());
		return DrSval.path.vms(DrEmskey.DR.k()) + "drEquipmentList";
	}

	@RequestMapping(value="getDrEquipmentList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getDrEquipmentList(DrEquipment drEquipment, HttpServletRequest request, HttpServletResponse response){
		try {
			drEquipment.setDelFlags(Arrays.asList(new String[]{Const.YES, Const.NO}));
			Page<DrEquipment> page = drEquipmentService.findPage(new Page<DrEquipment>(request, response), drEquipment);
			return ApiResult.success(page);
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+ e.getMessage());
		}
	}

	@RequiresPermissions("dr:drEquipment:view")
	@RequestMapping(value = {"listByRoom"})
	public String listByRoom(DrEquipmentRspace drEquipmentRspace, HttpServletRequest request, HttpServletResponse response, Model model) {
	    Page<DrEquipmentRspace> page = drEquipmentRspaceService.findPageByRoom(new Page<DrEquipmentRspace>(request, response), drEquipmentRspace);
	    model.addAttribute("page", page);
	    model.addAttribute(DrKey.DR_KEYS, Arrays.asList(DrKey.values()));
	    model.addAttribute(DrCdealStatus.DR_CDSTATUSS, DrCdealStatus.getAll());
	    return DrSval.path.vms(DrEmskey.DR.k()) + "drEquipmentListByRoom";
	}

	@RequiresPermissions("dr:drEquipment:view")
	@RequestMapping(value = "form")
	public String form(DrEquipment drEquipment, Model model, HttpServletRequest request) {
		List<Object> drKeyList=DrKey.getDrKeys();
		if(drEquipment.getId()==null){
			drEquipment=new DrEquipment(true);
		}
		String secondName=request.getParameter("secondName");
		if(StringUtil.isNotEmpty(secondName)){
			model.addAttribute("secondName",secondName);
		}
		List<Object> eTypeList=EType.getETypes();
		model.addAttribute("eTypeList", eTypeList);
		model.addAttribute("drKeyList", drKeyList);
		model.addAttribute("drEquipment", drEquipment);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drEquipmentForm";
	}

	@RequiresPermissions("dr:drEquipment:view")
	@RequestMapping(value = "formSet")
	public String formSet(DrEquipment drEquipment, Model model) {
		List<DrKey> drKeyList=Arrays.asList(DrKey.values());
		if(drEquipment.getId()==null){
			drEquipment=new DrEquipment(true);
		}

		//List<PwSpace> pw=dao.getPwSpace();
		// 所有楼层
		List<PwSpace> pwSpacelist = pwSpaceService.findList(new PwSpace());
		// 所有房间
		List<PwRoom> pwRoomlist = pwRoomService.findListByJL(new PwRoom());

		List<Map<String,String>> treeList=pwSpaceService.findALLPwSpaceAndPwRoom(pwSpacelist,pwRoomlist);
		model.addAttribute("pwSpacelist", treeList);

		List<Map<String,String>> doorList=drEquipmentService.getAllList(drEquipment);

		List<Map<String,String>> doorRelationList=drEquipmentRspaceService.getAllRelationListByDrEquipment(drEquipment.getId());
		model.addAttribute("doorList", doorList);
		model.addAttribute("doorRelationList", doorRelationList);
		model.addAttribute("drKeyList", drKeyList);
		model.addAttribute("drEquipment", drEquipment);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drEquipmentSetForm";
	}

	@RequiresPermissions("dr:drEquipment:edit")
	@RequestMapping(value = "save")
	public String save(DrEquipment drEquipment, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, drEquipment)){
			return form(drEquipment, model, request);
		}
		//JSONObject js=drEquipmentService.saveDrEquipment(drEquipment);
		drEquipmentService.save(drEquipment);
        CacheUtils.remove(DrUtils.CACHE_DR_EQUIPMENT_MAP);
		addMessage(redirectAttributes, "保存门禁设备成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drEquipment/?repage";
	}

	@RequestMapping(value="saveDrEq", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult saveDrEq(@RequestBody DrEquipment drEquipment, Model model){
		try {
			if (!beanValidator(model, drEquipment)){
				return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE, ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":参数校验失败");
			}
			drEquipmentService.save(drEquipment);
			CacheUtils.remove(DrUtils.CACHE_DR_EQUIPMENT_MAP);
			return ApiResult.success();
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+ e.getMessage());
		}
	}

	@RequestMapping(value = "ajaxCheck")
	@ResponseBody
	public JSONObject ajaxCheck(DrEquipment drEquipment, Model model, RedirectAttributes redirectAttributes) {
		JSONObject js=drEquipmentService.ajaxCheck(drEquipment);
		return js;
	}

	@RequestMapping(value = "ajaxGetList")
	@ResponseBody
	public List<Map<String,String>> ajaxGetList(DrEquipment drEquipment, Model model, RedirectAttributes redirectAttributes) {
		List<Map<String,String>> js=drEquipmentService.getAllList(drEquipment);
		return js;
	}

	/**
	 * 测试设备连接.
	 * @param drEquipment
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "ajaxCheckConnect")
	@ResponseBody
	public ApiTstatus<DrEquipment> ajaxCheckConnect(DrEquipment drEquipment, Model model, RedirectAttributes redirectAttributes) {
	    return drEquipmentService.ajaxCheckConnect(drEquipment);
	}

	@RequiresPermissions("dr:drEquipment:edit")
	@RequestMapping(value = "delete")
	public String delete(DrEquipment drEquipment, RedirectAttributes redirectAttributes) {
	    if(StringUtil.checkNotEmpty(drEquipment.getDelFlags())){
	        drEquipment.setDelFlag(drEquipment.getDelFlags().get(0));
	        drEquipmentService.updateDelFlag(drEquipment);
	        CacheUtils.remove(DrUtils.CACHE_DR_EQUIPMENT_MAP);
	        addMessage(redirectAttributes, "操作成功");
	    }else{
	        drEquipmentService.deleteAll(drEquipment);
	        CacheUtils.remove(DrUtils.CACHE_DR_EQUIPMENT_MAP);
	        addMessage(redirectAttributes, "删除成功");
	    }
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drEquipment/?repage";
	}

    /**
     * 获取节点元素类型数据.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDrKey", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxDrKey(Integer key) {
        if(key == null){
            return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(DrKey.values()).toString()));
        }
        DrKey drKey = DrKey.getByKey(key);
        if(drKey != null){
            return new ApiTstatus<String>(true, "查询成功！", drKey.toString());
        }
        return new ApiTstatus<String>(false, "查询失败！");
    }

	/**
	 * 一键开启所有门
	 * @return
	 */
	@RequestMapping(value = "/openEntranceGuards", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiStatus openEntranceGuards() {
		drEquipmentService.openEntranceGuards();
		return new ApiStatus(true, "处理中！");
	}

	/**
	 * 一键关闭所有门
	 * @return
	 */
	@RequestMapping(value = "/closeEntranceGuards", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiStatus closeEntranceGuards() {
		drEquipmentService.closeEntranceGuards();
		return new ApiStatus(true, "处理中！");
	}

//	/**
//	 * 校验判断当前一键关闭按钮的状态
//	 * @return
//	 */
//	@RequestMapping(value = "/checkEntranceGuardsStatus", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
//	@ResponseBody
//	public Rstatus checkEntranceGuardsStatus() {
//		DrCardRule drCardRule = drCardRuleService.getDrCardRule();
//		if (drCardRule == null) {
//			drCardRule = new DrCardRule();
//			drCardRule.setIsOpen(CoreSval.NO);
//			drCardRuleService.save(drCardRule);
//		}
//		if (CoreSval.YES.equals(drCardRule.getIsOpen())) {
//			return new Rstatus(true, "所有门禁设备为开启状态！");
//		} else {
//			return new Rstatus(true, "所有门禁设备为关闭状态！");
//		}
//	}

	   /**
     * 获取处理中状态
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDealing", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrEquipment>> ajaxDealing(String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<DrEquipment>>(true, "没有设备需要处理");
        }
        DrEquipment drEquipment = new DrEquipment();
        drEquipment.setIds(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
        List<DrEquipment> drEquipments = drEquipmentService.findList(drEquipment);
        return new ApiTstatus<List<DrEquipment>>(true, "设备处理成功！", drEquipments);
    }

    /**
     * 重置处理中状态
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDealReset", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<DrEquipment>> ajaxDealReset(String ids) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<DrEquipment>>(true, "没有设备需要重置");
        }
        drEquipmentService.updateBatchEquipmentStatusByIds(DrCdealStatus.DCD_FAIL.getKey(), Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
        return new ApiTstatus<List<DrEquipment>>(true, "设备处理成功！");
    }
}