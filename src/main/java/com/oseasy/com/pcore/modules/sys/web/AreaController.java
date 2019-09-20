/**
 *
 */
package com.oseasy.com.pcore.modules.sys.web;

import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.service.AreaService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 区域Controller

 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;

	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			return areaService.get(id);
		}else{
			return new Area();
		}
	}
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "areaList";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		/*if (area.getParent()==null||area.getParent().getId()==null) {
			area.setParent(UserUtils.getUser().getOffice().getArea());
		}*/
		if(null != area.getId() && StringUtil.isNotEmpty(area.getId()) ){
			if( area.getParent().getId() !=null && StringUtil.isNotEmpty(area.getParent().getId())){
				area.setParent(areaService.get(area.getParent().getId()));
			}
		}
		model.addAttribute("area", area);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "areaForm";
	}

	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult save(@RequestBody Area area, Model model, RedirectAttributes redirectAttributes) {
		if (CoreSval.isDemoMode()) {
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"演示模式，不允许操作！");
		}
		if (!beanValidator(model, area)) {
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR));
		}
		try{
			areaService.save(area);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
    @ResponseBody
	public ApiResult delete(Area area, RedirectAttributes redirectAttributes) {
		if (CoreSval.isDemoMode()) {
		    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"演示模式，不允许操作！");
		}
		try{
			areaService.delete(area);
            return ApiResult.success();
		}catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++) {
			Area e = list.get(i);
			if (StringUtil.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
