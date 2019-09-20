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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetail;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.scr.modules.scr.service.ScoRuleDetailService;
import com.oseasy.scr.modules.scr.service.ScoRuleService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学分规则详情Controller.
 * @author chenh
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRuleDetail")
public class ScoRuleDetailController extends BaseController {

	@Autowired
	private ScoRuleDetailService entityService;
	@Autowired
	private ScoRapplyService scoRapplyService;
	@Autowired
	private ScoRuleService scoRuleService;

	@ModelAttribute
	public ScoRuleDetail get(@RequestParam(required=false) String id) {
		ScoRuleDetail entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = entityService.get(id);
		}
		if (entity == null){
			entity = new ScoRuleDetail();
		}
		return entity;
	}


	@RequestMapping(value = {"list", ""})
	public String list(ScoRuleDetail entity, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoRuleDetail> page = entityService.findPage(new Page<ScoRuleDetail>(request, response), entity);
		model.addAttribute(Page.PAGE, page);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRuleDetailList";
	}


	@RequestMapping(value = "form")
	public String form(ScoRuleDetail entity, Model model) {
		model.addAttribute("scoRuleDetail", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRuleDetailForm";
	}


	@RequestMapping(value = "ajaxAuditScoRuleDetail")
	@ResponseBody
	public ApiResult ajaxAuditScoRuleDetail(@RequestBody ScoRuleDetail entity, Model model, RedirectAttributes redirectAttributes) {
		try{
			if(null == entity.getLevel()){
				entity.setLevel(0);
			}
			if(null == entity.getIsLimitm()){
				entity.setIsLimitm(Const.NO);
			}
			if(null == entity.getIsHalf()){
				entity.setIsHalf(Const.NO);
			}
			if(null == entity.getHalfRemarks()){
				entity.setHalfRemarks("");
			}
			if(null == entity.getCondType()){
				entity.setCondType(Const.NO);
			}
			if(null == entity.getJoinType() || StringUtil.isEmpty(entity.getJoinType())){
				entity.setJoinType(Const.NO);
			}
			if(StringUtil.isEmpty(entity.getJoinMax())){
				entity.setJoinMax(null);
			}
			if(null == entity.getIsLowSco() || entity.getIsLowSco().equals(Const.NO)){
				entity.setIsLowSco(Const.NO);
				entity.setLowSco(null);
				entity.setLowScoMax(null);
			}
			if(null == entity.getIsJoin() || entity.getIsJoin().equals(Const.NO)){
				entity.setIsJoin(Const.NO);
			}
			if (!beanValidator(model, entity)){
				return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(), ApiConst.PARAM_ERROR.getMsg());
			}
			//判断标准和父级类别的“计算分值规则”是否一致
			ScoRule scoRule = scoRuleService.findScoRuleSingleDetail(entity.getRule());
			if(!entity.getMaxOrSum().equals(scoRule.getScoRuleDetailMould().getMaxOrSum())){
				return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(), "与"+scoRule.getName()+"的计算分值规则不一致");
			}
			entityService.save(entity);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}


	@RequestMapping(value = "ajaxDeleteScoRuleDetail")
	@ResponseBody
	public ApiResult ajaxDeleteScoRuleDetail(ScoRuleDetail entity) {
		try{
			//判断学分认定申请是否有该标准
			ScoRapply scoRapply = new ScoRapply();
			scoRapply.setRdetail(entity);
			List<ScoRapply> scoRapplyList = scoRapplyService.findList(scoRapply);
			if(scoRapplyList != null && scoRapplyList.size() >0){
				return ApiResult.failed(ApiConst.CERTUSED_ERROR.getCode(), ApiConst.CERTUSED_ERROR.getMsg());
			}
			entityService.delete(entity);
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

	@RequestMapping(value = "ajaxValiScrRuleDetailName")
	@ResponseBody
	public ApiResult ajaxValiScrRuleDetailName(ScoRuleDetail entity){
		try{
			if(entity.getId()!=null){
				ScoRuleDetail sr = entityService.get(entity.getId());
				if(!sr.getName().equals(entity.getName())){
					List<ScoRuleDetail> list = entityService.ajaxValiScrRuleDetailName(entity);
					if(list!= null && list.size() >0){
						return ApiResult.failed(ApiConst.MORE_ERROR.getCode(), ApiConst.MORE_ERROR.getMsg());
					}
				}else{
					return ApiResult.success();
				}
			}
			List<ScoRuleDetail> list = entityService.ajaxValiScrRuleDetailName(entity);
			if(list!= null && list.size() >0){
				return ApiResult.failed(ApiConst.MORE_ERROR.getCode(), ApiConst.MORE_ERROR.getMsg());
			}
			return ApiResult.success();
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
		}
	}

}