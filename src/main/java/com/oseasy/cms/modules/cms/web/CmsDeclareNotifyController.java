package com.oseasy.cms.modules.cms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
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
import com.oseasy.cms.modules.cms.entity.CmsDeclareNotify;
import com.oseasy.cms.modules.cms.service.CmsDeclareNotifyService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * declareController.
 * @author 奔波儿灞
 * @version 2018-01-24
 */
@Controller
public class CmsDeclareNotifyController extends BaseController {
	public static final String FRONT_URL = CoreSval.getConfig("sysFrontIp")+CoreSval.getConfig("frontPath");
	@Autowired
	private CmsDeclareNotifyService cmsDeclareNotifyService;

	@ModelAttribute
	public CmsDeclareNotify get(@RequestParam(required=false) String id) {
		CmsDeclareNotify entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = cmsDeclareNotifyService.get(id);
		}
		if (entity == null){
			entity = new CmsDeclareNotify();
		}
		return entity;
	}

	@RequiresPermissions("cms:cmsDeclareNotify:view")
	@RequestMapping(value = {"${adminPath}/cms/cmsDeclareNotify/list"})
	public String list(CmsDeclareNotify cmsDeclareNotify, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsDeclareNotify> page = cmsDeclareNotifyService.findPage(new Page<CmsDeclareNotify>(request, response), cmsDeclareNotify);
		model.addAttribute("page", page);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsDeclareNotifyList";
	}

	@RequestMapping(value = {"${adminPath}/cms/cmsDeclareNotify/getNotifyList"})
	@ResponseBody
	public ApiResult getNotifyList(CmsDeclareNotify cmsDeclareNotify, HttpServletRequest request, HttpServletResponse response){
		try {
			Page<CmsDeclareNotify> page = cmsDeclareNotifyService.findPage(new Page<CmsDeclareNotify>(request, response), cmsDeclareNotify);
			return ApiResult.success(page);
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = {"${adminPath}/cms/cmsDeclareNotify/setRelease"})
	@ResponseBody
	public ApiResult setRelease(CmsDeclareNotify cmsDeclareNotify, HttpServletRequest request, HttpServletResponse response){
		try {
			JSONObject jsonObject = new JSONObject();
			if(cmsDeclareNotify.getIsRelease().equals("1")){
				jsonObject  = cmsDeclareNotifyService.unrelease(cmsDeclareNotify);
			}else {
				jsonObject  = cmsDeclareNotifyService.release(cmsDeclareNotify);
			}
			return ApiResult.success(jsonObject);
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = {"${adminPath}/cms/cmsDeclareNotify/delNotify"})
	@ResponseBody
	public ApiResult delNotify(CmsDeclareNotify cmsDeclareNotify, HttpServletRequest request, HttpServletResponse response){
		try {
			cmsDeclareNotifyService.delete(cmsDeclareNotify);
			return ApiResult.success();
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequiresPermissions("cms:cmsDeclareNotify:view")
	@RequestMapping(value = "${adminPath}/cms/cmsDeclareNotify/form")
	public String form(CmsDeclareNotify cmsDeclareNotify, Model model) {
		model.addAttribute("cmsDeclareNotify", cmsDeclareNotify);
		model.addAttribute("front_url", FRONT_URL);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsDeclareNotifyForm";
	}
	@RequiresPermissions("cms:cmsDeclareNotify:edit")
	@RequestMapping(value = "${adminPath}/cms/cmsDeclareNotify/save")
	@ResponseBody
	public JSONObject save(CmsDeclareNotify cmsDeclareNotify, Model model, RedirectAttributes redirectAttributes) {
		return cmsDeclareNotifyService.saveCdnotify(cmsDeclareNotify);
	}
	@RequiresPermissions("cms:cmsDeclareNotify:edit")
	@RequestMapping(value = "${adminPath}/cms/cmsDeclareNotify/release")
	@ResponseBody
	public JSONObject release(CmsDeclareNotify cmsDeclareNotify, Model model, RedirectAttributes redirectAttributes) {
		return cmsDeclareNotifyService.release(cmsDeclareNotify);
	}
	@RequiresPermissions("cms:cmsDeclareNotify:edit")
	@RequestMapping(value = "${adminPath}/cms/cmsDeclareNotify/unrelease")
	@ResponseBody
	public JSONObject unrelease(CmsDeclareNotify cmsDeclareNotify, Model model, RedirectAttributes redirectAttributes) {
		return cmsDeclareNotifyService.unrelease(cmsDeclareNotify);
	}
	@RequiresPermissions("cms:cmsDeclareNotify:edit")
	@RequestMapping(value = "${adminPath}/cms/cmsDeclareNotify/delete")
	public String delete(CmsDeclareNotify cmsDeclareNotify, RedirectAttributes redirectAttributes) {
		cmsDeclareNotifyService.delete(cmsDeclareNotify);
		addMessage(redirectAttributes, "删除成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/cms/cmsDeclareNotify/list?repage";
	}

	@RequestMapping(value = "${frontPath}/cmsDeclareNotify/preView")
	public String preView(CmsDeclareNotify cmsDeclareNotify, Model model,HttpServletRequest request) {
		if (StringUtil.isNotEmpty(cmsDeclareNotify.getContent())) {
			cmsDeclareNotify.setContent(StringEscapeUtils.unescapeHtml4(cmsDeclareNotify.getContent()));
		}
		model.addAttribute("cmsDeclareNotify", cmsDeclareNotify);
		return CmsSval.path.vms(CmsEmskey.CMS.k()) + "cmsDeclareNotifyView";
	}

}