/**
 *
 */
package com.oseasy.cms.modules.cms.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 内容管理Controller
 *
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${frontPath}/cms")
public class FrontAuyCmsController extends BaseController {
    @Autowired
    SysAttachmentService sysAttachmentService;

    @RequestMapping(value = "ajaxCheckUser" , method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ApiResult checkFrontInfoPerfect(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (SysUserUtils.checkFrontInfoPerfect(UserUtils.getUser())) {
                return ApiResult.failed(ApiConst.CODE_USER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_USER_ERROR));
            }else{
                return ApiResult.success();
            }
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

}
