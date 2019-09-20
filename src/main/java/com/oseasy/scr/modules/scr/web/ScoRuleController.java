package com.oseasy.scr.modules.scr.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreSval;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
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
import com.oseasy.scr.modules.scr.vo.ScoRcondType;
import com.oseasy.scr.modules.scr.vo.ScoRptype;
import com.oseasy.scr.modules.scr.vo.ScoRtype;
import com.oseasy.scr.modules.scr.vo.ScoRuleType;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.reg.RegType;

/**
 * 学分规则Controller.
 * @author chenh
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoRule")
public class ScoRuleController extends BaseController {

	@Autowired
	private ScoRuleService entityService;
    @Autowired
    private ScoRuleDetailService scoRuleDetailService;
    @Autowired
    private ScoRapplyService scoRapplyService;
	@RequiresPermissions("scr:scoRule:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
        //技能学分固定id
        model.addAttribute("SKILLCREDITID",CoreSval.getScoreSkillId());
	    return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRuleList";
	}

    @RequestMapping(value ="listpage")
    @ResponseBody
    public ApiResult listpage(ScoRule entity) {
         try{
             List<ScoRule> list = entityService.findTreeList(entity);
             return ApiResult.success(list);
         }catch (Exception e){
             logger.error(e.getMessage());
             return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
         }

    }

	@RequiresPermissions("scr:scoRule:view")
	@RequestMapping(value = "form")
	public String form(ScoRule entity, Model model) {
		model.addAttribute("scoRule", entity);
		return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoRuleForm";
	}

	@RequiresPermissions("scr:scoRule:edit")
	@RequestMapping(value = "ajaxAuditScoRule", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
	public ApiResult save(@RequestBody ScoRule entity, Model model) {
        try{
            if (!beanValidator(model, entity)){
                return ApiResult.failed(ApiConst.PARAM_ERROR.getCode(), ApiConst.PARAM_ERROR.getMsg());
            }
            if(entity.getPtype().equals(ScoRptype.SRP_PERSION.getKey())){
                entity.setIsPb(false);
            }
            if(entity.getPtype().equals(ScoRptype.SRP_TEAM.getKey())){
                entity.setIsPb(true);
            }
            if(entity.getId()!= null && StringUtil.isNotEmpty(entity.getId())) {
                //修改前确认该类别下有没有标准被认定使用
                List<ScoRapply> scoRapplyList = scoRapplyService.findRuleDetailIsApply(entity);
                if (StringUtil.checkNotEmpty(scoRapplyList)) {
                    return ApiResult.failed(ApiConst.CERTUSED_ERROR.getCode(), ApiConst.CERTUSED_ERROR.getMsg());
                }
            }
            entityService.save(entity);
            return ApiResult.success(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
	}

	//学分规则保存排序
    @RequestMapping(value = "saveSort", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult saveSort(@RequestBody List<ScoRule> entity) {
        try{
            if(StringUtil.checkNotEmpty(entity)){
                for(ScoRule scoRule : entity){
                    if(scoRule.getType() == null || StringUtil.isEmpty(scoRule.getType())){
                        ScoRuleDetail scoRuleDetail = new ScoRuleDetail();
                        scoRuleDetail.setId(scoRule.getId());
                        scoRuleDetail.setSort(scoRule.getSort());
                        scoRuleDetailService.updateSort(scoRuleDetail);
                    }else{
                        entityService.updateSort(scoRule);
                    }
                }
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }


    @RequestMapping(value = "ajaxValiScrRuleName")
    @ResponseBody
    public ApiResult ajaxValiScrRuleName(ScoRule entity) {
	    try {
            if(entity.getId()!=null){
                ScoRule temp = entityService.findScoRule(entity);
                if(!temp.getName().equals(entity.getName().replaceAll(StringUtil.KGE,StringUtil.EMPTY))){
                    List<ScoRule> list = entityService.ajaxValiScrRuleName(entity);
                    if(list!= null && list.size() >0){
                        return ApiResult.failed(ApiConst.MORE_ERROR.getCode(), ApiConst.MORE_ERROR.getMsg());
                    }
                }else{
                    return ApiResult.success();
                }
            }
            List<ScoRule> list = entityService.ajaxValiScrRuleName(entity);
            if(list!= null && list.size() >0){
                return ApiResult.failed(ApiConst.MORE_ERROR.getCode(), ApiConst.MORE_ERROR.getMsg());
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }


	@RequiresPermissions("scr:scoRule:edit")
	@RequestMapping(value = "ajaxDeleteScrRule")
    @ResponseBody
	public ApiResult delete(ScoRule entity) {
        try{
            //判断当前选中的类别下有没有标准被认定
            if(entityService.scoRuleInRapply(entity)){
                return ApiResult.failed(ApiConst.CERTUSED_ERROR.getCode(), ApiConst.CERTUSED_ERROR.getMsg());
            }else{
                entityService.deleteScoRule(entity);
                return ApiResult.success();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
	}


    /**
     * 获取学分规则type=ScoRtype.SR_LX列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxListTops")
    public ApiResult ajaxListTops(ScoRule scoRule, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<ScoRule> pag = new Page<ScoRule>(request, response);
            scoRule.setType(ScoRtype.SR_LX.getKey());
            Page<ScoRule> page = entityService.findPage(pag, scoRule);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }

    }
    /**
     * 获取学分规则列表.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxList")
    public ApiResult ajaxList(ScoRule scoRule, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {

            Page<ScoRule> pag = new Page<ScoRule>(request, response);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                // pag.setOrderBy(ScoRule.TABLEA + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + ScoRule.TABLEA + ScoRule.TYPE);
                // pag.setOrderByType(Page.ORDER_ASC);
            }
            Page<ScoRule> page = entityService.findPage(pag, scoRule);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    //学分类别list
    @RequestMapping(value = "getScoRuleList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getScoRuleList(ScoRule entity){
        try{
            List<ScoRule> scoRuleList = entityService.scoRuleSingleList(entity);
            List<ScoRule> list = Lists.newArrayList();
            for(ScoRule scoRule : scoRuleList){
                if(!scoRule.getId().equals(CoreSval.getScoreSkillId())){
                    list.add(scoRule);
                }
            }
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //学分标准list
    @RequestMapping(value = "getScoRuleDetailList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getScoRuleDetailList(){
        try{
            List<ScoRuleDetail> scoRuleDetailList = scoRuleDetailService.findList(new ScoRuleDetail());
            return ApiResult.success(scoRuleDetailList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    /**
     * 获取认定形式.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxScoRptypes", method = RequestMethod.GET)
    public ApiResult ajaxScoRptypes(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(ScoRptype.values()).toString());
            }else{
                return ApiResult.success(ScoRptype.getAll().toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 获取级别.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxScoRtypes", method = RequestMethod.GET)
    public ApiResult ajaxScoRtypes(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(ScoRtype.values()).toString());
            }else{
                return ApiResult.success(ScoRtype.getAll().toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 条件校验类型.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxScoRcondTypes", method = RequestMethod.GET)
    public ApiResult ajaxScoRcondTypes(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(ScoRcondType.values()).toString());
            }else{
                return ApiResult.success(ScoRcondType.getAll().toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 学分标准--计算学分规则.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxScoRuleTypes", method = RequestMethod.GET)
    public ApiResult ajaxScoRuleTypes(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(ScoRuleType.values()).toString());
            }else{
                return ApiResult.success(ScoRuleType.getAll().toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 条件校验类型.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxRegTypes", method = RequestMethod.GET)
    public ApiResult ajaxRegTypes(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(RegType.values()).toString());
            }else{
                return ApiResult.success(RegType.getAll().toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}