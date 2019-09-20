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
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyCert;
import com.oseasy.scr.modules.scr.service.ScoRapplyCertService;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 证书管理Controller.
 * @author liangjie
 * @version 2019-01-08
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRapplyCert")
public class ScoRapplyCertController extends BaseController {

	@Autowired
	private ScoRapplyCertService entityService;
	@Autowired
	private ScoRapplyService scoRapplyService;

	@ModelAttribute
	public ScoRapplyCert get(@RequestParam(required=false) String id) {
		ScoRapplyCert entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRapplyCert();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list() {
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyCertList";
	}

	@RequestMapping(value = "listPage", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult listPage(ScoRapplyCert entity, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ScoRapplyCert> sourceList = entityService.findTreeList(entity);
			return ApiResult.success(sourceList);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "form")
	public String form(ScoRapplyCert entity, Model model) {
		model.addAttribute("scoRapplyCert", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRapplyCertForm";
	}

	//保存
	@RequestMapping(value = "ajaxAuditScoRapplyCert", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult save(@RequestBody ScoRapplyCert entity, Model model) {
		try{
			if (!beanValidator(model, entity)){
				return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(), ApiConst.PARAM_ERROR.getMsg());
			}
			entityService.save(entity);
			//保存附件
			entityService.saveScoRapplyCertSysAttachment(entity);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	//保存排序
	@RequestMapping(value="ajaxUpdateSort", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxUpdateSort(@RequestBody ScoRapplyCert entity){
		try{
			for(ScoRapplyCert scoRapplyCert : entity.getScoRapplyCertList()){
				entityService.updateSort(scoRapplyCert);
			}
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	//删除
	@RequestMapping(value = "ajaxDeleteScoRapplyCert")
	@ResponseBody
	public ApiResult ajaxDeleteScoRapplyCert(ScoRapplyCert entity) {
		try{
			//证书已用于申请则不能删除
			ScoRapply scoRapply = new ScoRapply();
			scoRapply.setScoRapplyCert(entity);
			List<ScoRapply> scoRapplyList = scoRapplyService.findList(scoRapply);
			if(scoRapplyList != null && scoRapplyList.size() > 0){
				//学分申请中已存在该证书
				return ApiResult.failed(ApiConst.CERTUSED_ERROR.getCode(), ApiConst.CERTUSED_ERROR.getMsg());
			}else{
				//删除父级时看子类证书是否存在学分申请中
				ScoRapplyCert certEntity = new ScoRapplyCert();
				certEntity.setId(entity.getId());
				certEntity.setQueryStr(Const.YES);
				List<ScoRapplyCert> scoRapplyCertList = entityService.findList(certEntity);
				for(ScoRapplyCert scoRapplyCert : scoRapplyCertList){
					ScoRapply temp = new ScoRapply();
					temp.setScoRapplyCert(scoRapplyCert);
					List<ScoRapply> scoRapplyLists = scoRapplyService.findList(temp);
					for(ScoRapply scoRapply1 : scoRapplyLists){
						if(scoRapplyCert.getId().equals(scoRapply1.getScoRapplyCert().getId())){
							return ApiResult.failed(ApiConst.CERTUSED_ERROR.getCode(), ApiConst.CERTUSED_ERROR.getMsg());
						}
					}
				}
			}
			entity.setDelFlag(Const.YES);
			entityService.delete(entity);
			//删除下级证书
			entityService.deleteChildren(entity);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "checkScoRaCertName", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ApiResult checkScoRaCertName(@RequestBody ScoRapplyCert scoRapplyCert){
		Integer integer = entityService.checkScoRaCertName(scoRapplyCert);
		return ApiResult.success(integer < 1);
	}
}