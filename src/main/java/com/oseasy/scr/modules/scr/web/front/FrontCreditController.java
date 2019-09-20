package com.oseasy.scr.modules.scr.web.front;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreSval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.sco.entity.ScoApply;
import com.oseasy.scr.modules.sco.service.ScoApplyService;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyCert;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;
import com.oseasy.scr.modules.scr.entity.ScoRapplyValid;
import com.oseasy.scr.modules.scr.entity.ScoRset;
import com.oseasy.scr.modules.scr.entity.ScoRsum;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetail;
import com.oseasy.scr.modules.scr.entity.ScoRulePb;
import com.oseasy.scr.modules.scr.service.ScoRapplyCertService;
import com.oseasy.scr.modules.scr.service.ScoRapplyMemberService;
import com.oseasy.scr.modules.scr.service.ScoRapplyRecordService;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.scr.modules.scr.service.ScoRsetService;
import com.oseasy.scr.modules.scr.service.ScoRsumService;
import com.oseasy.scr.modules.scr.service.ScoRuleDetailService;
import com.oseasy.scr.modules.scr.service.ScoRulePbService;
import com.oseasy.scr.modules.scr.service.ScoRuleService;
import com.oseasy.scr.modules.scr.vo.ScoCreditType;
import com.oseasy.scr.modules.scr.vo.ScoQuery;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;
import com.oseasy.scr.modules.scr.vo.ScoRuleType;
import com.oseasy.util.common.utils.StringUtil;


/**
 * 认定创新创业学分
 * @author liangjie
 */
@Controller
@RequestMapping(value = "${frontPath}/scr")
public class FrontCreditController extends BaseController {

    @Autowired
    private ScoRuleService entityService;
    @Autowired
    private ScoRuleDetailService scoRuleDetailService;
    @Autowired
    private ScoRapplyCertService scoRapplyCertService;
    @Autowired
    private ScoRapplyService scoRapplyService;
    @Autowired
    private ScoApplyService scoApplyService;
    @Autowired
    private ScoRapplyMemberService scoRapplyMemberService;
    @Autowired
    private ScoRapplyRecordService scoRapplyRecordService;
    @Autowired
    private ScoRulePbService scoRulePbService;
    @Autowired
    private ScoRsumService scoRsumService;
    @Autowired
    private ScoRsetService scoRsetService;

    @RequestMapping(value = "toFrontScrApplyForm")
    public String toFrontScrApplyForm(String id, String creditType, Model model, HttpServletRequest request){
        User user = UserUtils.getUser();
        if (null == user.getId()) {
            return UserUtils.toLogin();
        }
        model.addAttribute("id", id);
        model.addAttribute("creditType", creditType);
        //技能学分固定id
        model.addAttribute("SKILLCREDITID", CoreSval.getScoreSkillId());
        //学分类型固定id
        model.addAttribute("ScoCateID", CoreSval.getScoreRootId());
        IApply.iappmodel(model, request);
        return ScrSval.path.vms(ScrEmskey.SCR.k()) + "front/frontScoApplyForm";
    }

    @RequestMapping(value = {"list", ""})
    public String list() {
        return ScrSval.path.vms(ScrEmskey.SCR.k()) + "front/scoQueryList";
    }

    //学分类别list
    @RequestMapping(value = "fronScoRuleList")
    @ResponseBody
    public ApiResult fronScoRuleList(){
        try{
            List<ScoRule> scoRuleList = entityService.scoRuleSingleList(new ScoRule());
            List<ScoRule> list = Lists.newArrayList();
            for(ScoRule scoRule : scoRuleList){
                if(!scoRule.getId().equals( CoreSval.getScoreRootId())){
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
    @RequestMapping(value = "fronScoRuleDetailList")
    @ResponseBody
    public ApiResult fronScoRuleDetailList(){
        try{
            List<ScoRuleDetail> scoRuleDetailList = scoRuleDetailService.findList(new ScoRuleDetail());
            return ApiResult.success(scoRuleDetailList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //修改学分查询的列表
    @RequestMapping(value = "scoCreditQuery/ajaxupdateValList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxupdateValList(ScoRsum scoRsum){
        try{
            List<ScoRsum> scoRsumList = scoRsumService.updateValList(scoRsum);
            return ApiResult.success(scoRsumList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //保存学分申请
    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public ApiResult fronSaveScoRapplyForm(@RequestBody ScoRapply scoRapply, HttpServletRequest request){
        try{
            return scoRapplyService.saveFronSaveScoRapplyForm(scoRapply,request);
        } catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //证书
    @RequestMapping(value = "frontScoRapplyCertList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult frontScoRapplyCertList(ScoRapplyCert entity) {
        try {
            List<ScoRapplyCert> sourceList = scoRapplyCertService.findList(entity);
            return ApiResult.success(sourceList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxScoPbList")
    @ResponseBody
    public ApiResult list(ScoRulePb entity) {
        try{
            List<ScoRulePb> list = scoRulePbService.findList(entity);
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //跳转学分查询页面
    @RequestMapping(value = "toFindScoForm")
    public String toFindScoForm(){
        return ScrSval.path.vms(ScrEmskey.SCR.k()) + "front/scoQueryList";
    }

    //跳转学分查询详情页面
    @RequestMapping(value = "toFrontScoDetailForm")
    public String toFindScoDetailForm(String id, String creditType, Model model){
        model.addAttribute("id", id);
        model.addAttribute("creditType", creditType);
        return ScrSval.path.vms(ScrEmskey.SCR.k()) + "front/frontScoDetailForm";
    }

    //学分查询接口
    @RequestMapping(value = "ajaxFindScoList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxFindScoList(ScoQuery scoQuery, HttpServletRequest request, HttpServletResponse response){
        try{
            //获取当前用户
            User user = CoreUtils.getUser();
            scoQuery.setUserId(user.getId());
            Page<ScoQuery>  page = scoRapplyService.findCreditPage(new Page<ScoQuery>(request, response),scoQuery);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //学分申请校验
    @RequestMapping(value = "ajaxValidScoRapply", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxValidScoRapply(@RequestBody ScoRapplyValid entity){
        try {
            //技能学分：标准 + 证书名称 唯一校验
            List<ScoRapplyValid> scoRapplyValidCertList = scoRapplyService.findDetailRapplyCertList(entity);
            List<ScoRapplyValid> scoRapplyValidNameList = scoRapplyService.findDetailNameList(entity);
            if(StringUtil.checkNotEmpty(scoRapplyValidCertList) || StringUtil.checkNotEmpty(scoRapplyValidNameList)){
                return ApiResult.failed(ApiConst.DETAILCERTMORE_ERROR.getCode(), ApiConst.DETAILCERTMORE_ERROR.getMsg());
            }else{
                return  ApiResult.success();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //个人标准学分总分判定
    @RequestMapping(value = "ajaxFindRdetailPersonalSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxFindRdetailPersonalSum(@RequestBody ScoRapplyValid entity){
        try {
            //标准为累加或者为特例时，累计学分
            Boolean isSpecial = entity.getApply().getRdetail().getMaxOrSum().equals(ScoRuleType.MAX_SCORE.getKey())
                    && entity.getApply().getRdetail().getIsSpecial().equals(Const.YES);
            if(entity.getApply().getRdetail().getMaxOrSum().equals(ScoRuleType.SUM_SCORE.getKey()) || isSpecial){
                ScoRsum temp = scoRsumService.findRdetailPersonalSum(entity);
                if(temp != null && temp.getSum()==1.0){
                    return ApiResult.failed(ApiConst.DETAILSCOREFULL_ERROR.getCode(), ApiConst.DETAILSCOREFULL_ERROR.getMsg());
                }
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }


    //学分查询详情接口
    @RequestMapping(value = "ajaxFindScoDetail", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxFindScoDetail(ScoQuery scoQuery){
        ScoRapply sr = new ScoRapply();
        sr.setId(scoQuery.getId());
        //创新创业学分类型
        if(scoQuery.getCreditType().equals(ScoCreditType.SCO_CREDIT.getKey())){
            sr = scoRapplyService.ajaxFindScoDetail(sr);
            return ApiResult.success(sr);
        }
        //课程学分类型
        sr = scoRapplyService.ajaxFindCourseDetail(sr);
        return ApiResult.success(sr);
    }

    //学分查询--查看详情中审核结果
    @RequestMapping(value = "ajaxFindScoAuditResult", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxFindScoAuditResult(ScoQuery scoQuery){
        try{
            //创新创业学分类型
            if(scoQuery.getCreditType().equals(ScoCreditType.SCO_CREDIT.getKey())){
                List<ScoRapplyRecord> scoRapplyRecordList = scoRapplyService.scoRapplyRecordList(new ScoRapply(scoQuery.getId()));
                return ApiResult.success(scoRapplyRecordList);
            }
            //课程学分类型
            List<ScoRapplyRecord> scoRapplyRecordList = scoRapplyRecordService.findCourseAuditList(new ScoRapplyRecord(scoQuery.getId()));
            return ApiResult.success(scoRapplyRecordList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //删除学分申请接口
    @RequestMapping(value = "ajaxDeleteScoRapply")
    @ResponseBody
    public ApiResult ajaxDeleteScoRapply(ScoQuery scoQuery){
        try{
            //创新创业学分类型申请
            if(scoQuery.getCreditType().equals(ScoCreditType.SCO_CREDIT.getKey())){
                ScoRapply scoRapply = new ScoRapply(scoQuery.getId());
                scoRapply.setDelFlag(Const.YES);
                scoRapplyService.delete(scoRapply);
            }
            //课程学分类型申请
            if(scoQuery.getCreditType().equals(ScoCreditType.SCO_COURSE.getKey())){
                ScoApply scoApply = new ScoApply(scoQuery.getId());
                scoApply.setDelFlag(Const.YES);
                scoApplyService.delete(scoApply);
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }

    }

    /**
     * 学分查询-学分申请状态.
     * @return ApiResult
     */
    @ResponseBody
    @RequestMapping(value = "ajaxFrontScoRstatuss", method = RequestMethod.GET)
    public ApiResult ajaxScoRstatuss(@RequestParam Boolean isAll) {
        try {
            if(isAll == null){
                isAll = false;
            }
            if(isAll){
                return ApiResult.success(Arrays.asList(ScoRstatus.values()).toString());
            }else{
                return ApiResult.success(Arrays.asList(ScoRstatus.getAll()).toString());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //申请人总分
    @RequestMapping(value = "ajaxRapplyUserSum", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxRapplyUserSum(ScoRsum scoRsum){
        try{
            List<Map<String,Double>> map = scoRsumService.findScoSumList(scoRsum);
            ScoRset scoRsetList = scoRsetService.findList(new ScoRset()).get(0);
            ApiResult result = new ApiResult();
            result.setStatus(ApiConst.STATUS_SUCCESS);
            result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
            if(map != null && map.size() >0 && map.get(0).get("总分") >= scoRsetList.getSnumlimit()){
                result.setData(true);
                result.setMsg("您的学分已满，是否继续申请？");
            }else{
                result.setData(false);
                result.setMsg("");
            }
            return  result;
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


}
