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
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRset;
import com.oseasy.scr.modules.scr.service.ScoRsetService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 设置学分规则Controller.
 * @author liangjie
 * @version 2018-12-27
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRset")
public class ScoRsetController extends BaseController {

	@Autowired
	private ScoRsetService entityService;

	@ModelAttribute
	public ScoRset get(@RequestParam(required=false) String id) {
		ScoRset entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRset();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	@ResponseBody
	public ApiResult list(ScoRset entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			List<ScoRset> list = entityService.findList(entity);
			return ApiResult.success(list);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}


	@RequestMapping(value = "form")
	public String form(ScoRset entity, Model model) {
		model.addAttribute("scoRset", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRsetForm";
	}


	@RequestMapping(value = "ajaxAuditScoRset" , method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult ajaxAuditScoRset(@RequestBody ScoRset entity, Model model) {
		try{
			if (!beanValidator(model, entity)){
				return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(), ApiConst.PARAM_ERROR.getMsg());
			}
			if(entity.getIsKeepNpoint().equals(Const.NO)){
				entity.setKeepNpoint(null);
			}
			entityService.save(entity);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public ApiResult delete(ScoRset entity) {
		try{
			entityService.delete(entity);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

}