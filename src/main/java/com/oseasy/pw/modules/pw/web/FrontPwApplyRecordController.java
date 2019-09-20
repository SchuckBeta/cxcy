package com.oseasy.pw.modules.pw.web;

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

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.pw.common.config.PwSval.PwEmskey;
import com.oseasy.pw.modules.pw.entity.PwApplyRecord;
import com.oseasy.pw.modules.pw.service.PwApplyRecordService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * pwApplyRecordController.
 * @author zy
 * @version 2018-11-20
 */
@Controller
@RequestMapping(value = "${frontPath}/pw/pwApplyRecord")
public class FrontPwApplyRecordController extends BaseController {

	@Autowired
	private PwApplyRecordService pwApplyRecordService;

	@ModelAttribute
	public PwApplyRecord get(@RequestParam(required=false) String id) {
		PwApplyRecord entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = pwApplyRecordService.get(id);
		}
		if (entity == null){
			entity = new PwApplyRecord();
		}
		return entity;
	}

	@RequiresPermissions("pw:pwApplyRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(PwApplyRecord entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PwApplyRecord> page = pwApplyRecordService.findPage(new Page<PwApplyRecord>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwApplyRecordList";
	}

	@RequiresPermissions("pw:pwApplyRecord:view")
	@RequestMapping(value = "form")
	public String form(PwApplyRecord entity, Model model) {
		model.addAttribute("pwApplyRecord", entity);
		return PwSval.path.vms(PwEmskey.PW.k()) + "pwApplyRecordForm";
	}

	@RequiresPermissions("pw:pwApplyRecord:edit")
	@RequestMapping(value = "save")
	public String save(PwApplyRecord entity, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		pwApplyRecordService.save(entity);
		addMessage(redirectAttributes, "保存pwApplyRecord成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwApplyRecord/?repage";
	}

	@RequiresPermissions("pw:pwApplyRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(PwApplyRecord entity, RedirectAttributes redirectAttributes) {
		pwApplyRecordService.delete(entity);
		addMessage(redirectAttributes, "删除pwApplyRecord成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/pw/pwApplyRecord/?repage";
	}


	@RequestMapping(value = "ajaxDelPwEnterRecord" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxDelPwEnterRecord(@RequestBody String pwEnterId, HttpServletRequest request, HttpServletResponse response){
		try {
			pwApplyRecordService.delete(new PwApplyRecord(pwEnterId));
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "ajaxFindAppList" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxFindAppList(@RequestBody PwApplyRecord pwApplyRecord, HttpServletRequest request, HttpServletResponse response){
		try {
			List<PwApplyRecord> recordList=pwApplyRecordService.getAppList(pwApplyRecord);
			return ApiResult.success(recordList);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "ajaxFindAuditList" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxFindAuditList(@RequestBody PwApplyRecord pwApplyRecord, HttpServletRequest request, HttpServletResponse response){
		try {
			List<PwApplyRecord> recordList=pwApplyRecordService.getFrontAuditList(pwApplyRecord);
			return ApiResult.success(recordList);
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}


	@RequestMapping(value = "ajaxAddPwEnterRecord" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxAddPwEnterRecord(@RequestBody PwApplyRecord pwApplyRecord, HttpServletRequest request, HttpServletResponse response){
		try {
			pwApplyRecordService.save(pwApplyRecord);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}
}