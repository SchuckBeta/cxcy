package com.oseasy.scr.modules.scr.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRulePb;
import com.oseasy.scr.modules.scr.service.ScoRulePbService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分规则配比Controller.
 * @author chenh
 * @version 2018-12-26
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRulePb")
public class ScoRulePbController extends BaseController {

	@Autowired
	private ScoRulePbService entityService;

	@ModelAttribute
	public ScoRulePb get(@RequestParam(required=false) String id) {
		ScoRulePb entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRulePb();
		}
		return entity;
	}


	@RequestMapping(value = "ajaxScoPbList")
	@ResponseBody
	public ApiResult list(ScoRulePb entity, HttpServletRequest request, HttpServletResponse response) {
		try{
			List<ScoRulePb> list = entityService.findList(entity);
			return ApiResult.success(list);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}


	@RequestMapping(value = "form")
	public String form(ScoRulePb entity, Model model) {
		model.addAttribute("scoRulePb", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRulePbForm";
	}


	@RequestMapping(value = "ajaxSetScoPb", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult save(@RequestBody List<ScoRulePb> entity, Model model) {
		try{
			entityService.delete(entity.get(0));
			entityService.insertPL(entity);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "ajaxDeleteScoPb")
	@ResponseBody
	public ApiResult delete(ScoRulePb entity) {
		try {
			entityService.delete(entity);
			return ApiResult.success();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg() + ":" + e.getMessage());
		}
	}
}