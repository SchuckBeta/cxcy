package com.oseasy.pro.modules.promodel.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.service.ProBackTeacherExpansionService;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;

/**
 * 专家信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/expert")
public class ProExpertController extends BaseController {
    @Autowired
    ProjectDeclareService projectDeclareService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    BackTeacherExpansionService backTeacherExpansionService ;
    @Autowired
    ProBackTeacherExpansionService proBackTeacherExpansionService ;


    @RequestMapping(value = "delExpert", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult delExpert(@RequestBody User user) {
        try {
            Boolean delAble = UserUtils.getUser().getId().equals(user.getId());
            Boolean isAdmin = user.getAdmin();
            if (isAdmin) {
                return ApiResult.failed(ApiConst.STATUS_FAIL, ApiConst.getErrMsg(ApiConst.STATUS_FAIL) + ":删除用户失败, 不允许删除超级管理员用户");
            }else if(delAble){
                return ApiResult.failed(ApiConst.STATUS_FAIL, ApiConst.getErrMsg(ApiConst.STATUS_FAIL) + ":删除用户失败, 不允许删除当前用户");
            }
            int todoNum = actTaskService.recordUserId(user.getId());
            if (todoNum > 0) {
                return ApiResult.failed(ApiConst.STATUS_FAIL,ApiConst.getErrMsg(ApiConst.STATUS_FAIL)+":该用户还有流程任务，不能删除!");
            }
            //1为专家 0为导师
            backTeacherExpansionService.deleteByType(user,"1");

            return ApiResult.success();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }

    @RequestMapping(value = "getExpertList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getExpertList(BackTeacherExpansion backTeacherExpansion, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<BackTeacherExpansion> page =  proBackTeacherExpansionService.findExpertPage(new Page<BackTeacherExpansion>(request, response), backTeacherExpansion);

            return ApiResult.success(page);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
        }
    }
}