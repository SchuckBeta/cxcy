package com.oseasy.cms.modules.cms.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.oseasy.auy.common.utils.AuyCmsUtils;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.pcore.common.web.BaseController;
/**
 * Created by zhangzheng on 2017/6/21.
 */
@Controller
@RequestMapping(value = "${frontPath}/frontNotice")
public class FrontAuyNoticeController extends BaseController {
    @RequestMapping(value = "getOaNotifysSC")
    @ResponseBody
    public ApiResult getOaNotifysSC(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ApiResult.success(AuyCmsUtils.getOaNotifysSC());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "getOaNotifysTZ")
    @ResponseBody
    public ApiResult getOaNotifysTZ(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ApiResult.success(AuyCmsUtils.getOaNotifysTZ());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "getOaNotifysSS")
    @ResponseBody
    public ApiResult getOaNotifysSS(HttpServletRequest request, HttpServletResponse response) {
        try {
            return ApiResult.success(AuyCmsUtils.getOaNotifysSS());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value = "getOaNotifys")
    @ResponseBody
    public ApiResult getOaNotifys(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, List<OaNotify>> oaNotifys = Maps.newHashMap();
            oaNotifys.put("sc", AuyCmsUtils.getOaNotifysSC());
            oaNotifys.put("tz", AuyCmsUtils.getOaNotifysTZ());
            oaNotifys.put("ss", AuyCmsUtils.getOaNotifysSS());
            return ApiResult.success(oaNotifys);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
