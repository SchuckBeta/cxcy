package com.oseasy.scr.modules.scr.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.scr.common.config.ScrSval.ScrEmskey;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;
import com.oseasy.scr.modules.scr.entity.ScoRsum;
import com.oseasy.scr.modules.scr.service.ScoRapplyPbService;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.scr.modules.scr.service.ScoRsumService;
import com.oseasy.scr.modules.scr.vo.ScoQuery;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;

/**
 * 后台学分查询Controller.
 * @author liangjie
 * Created by PW on 2019/1/16.
 */
@Controller
@RequestMapping(value = "${adminPath}/scr/scoCreditQuery")
public class ScoCreditQueryController extends BaseController {

    @Autowired
    private ScoRapplyService scoRapplyService;
    @Autowired
    private ScoRsumService scoRsumService;
    @Autowired
    private ScoRapplyPbService scoRapplyPbService;

    //列表跳转
    @RequestMapping(value = {"list", ""})
    public String list() {
        return ScrSval.path.vms(ScrEmskey.SCR.k()) + "scoCreditQueryList";
    }

    //列表接口
    @RequestMapping(value = "listPage", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult listPage(ScoQuery scoQuery, HttpServletRequest request, HttpServletResponse response){
        try{
            scoQuery.setStatus(ScoRstatus.SRS_PASS.getKey());
            Page<ScoQuery> page = scoRapplyService.findScoCreditQueryPage(new Page<ScoQuery>(request, response), scoQuery);
            return ApiResult.success(page);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //查看详情
    @RequestMapping(value = "ajaxFindScoDetail", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxFindScoDetail(ScoQuery scoQuery){
        try{
            return ApiResult.success(scoRapplyService.ajaxFindScoDetail(new ScoRapply(scoQuery.getId())));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //查看详情里审核记录
    @RequestMapping(value = "ajaxFindScoAuditResult", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxFindScoAuditResult(ScoQuery scoQuery){
        try{
            List<ScoRapplyRecord> scoRapplyRecordList = scoRapplyService.scoRapplyRecordList(new ScoRapply(scoQuery.getId()));
            return ApiResult.success(scoRapplyRecordList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //修改学分查询的列表
    @RequestMapping(value = "ajaxupdateValList", method = RequestMethod.GET)
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

    //修改学分
    @RequestMapping(value = "ajaxupdateVal", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxupdateVal(@RequestBody ScoRsum entity){
        try{
            scoRsumService.ajaxupdateVal(entity);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //删除学分
    @RequestMapping(value = "ajaxDeleteScoCredit", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult ajaxDeleteScoCredit(ScoQuery scoQuery){
        try{
            scoRapplyService.deleteScoCredit(scoQuery);
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }

    //删除学分
    @RequestMapping(value = "ajaxPLDeleteScoCredit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult ajaxPLDeleteScoCredit(@RequestBody List<ScoQuery> scoQuery){
        try{
            for(ScoQuery scoQuery1 : scoQuery){
                scoRapplyService.deleteScoCredit(scoQuery1);
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }
    }


}
