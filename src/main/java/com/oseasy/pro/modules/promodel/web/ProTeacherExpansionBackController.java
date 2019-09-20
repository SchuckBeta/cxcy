package com.oseasy.pro.modules.promodel.web;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.project.vo.ProjectAuditTaskVo;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by PW on 2019/1/26.
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/frontTeacherExpansion")
public class ProTeacherExpansionBackController extends BaseController {
    @Autowired
    private ProActTaskService proActTaskService;

    @ResponseBody
    @RequestMapping(value = "ajaxAuditTask")
    public ApiResult ajaxAuditTask(BackTeacherExpansion backTeacherExpansion){
        try{
            List<ProjectAuditTaskVo> projectAuditTaskVoList = proActTaskService.getExpertAuditProAndYear(backTeacherExpansion.getUser().getId());
            if(StringUtil.checkNotEmpty(projectAuditTaskVoList)){
                //根据年份倒序排序
                Collections.sort(projectAuditTaskVoList, new Comparator<ProjectAuditTaskVo>(){
                    public int compare(ProjectAuditTaskVo o1, ProjectAuditTaskVo o2) {
                        return Integer.valueOf(o2.getYear())-Integer.valueOf(o1.getYear());
                    }});
            }
            return ApiResult.success(projectAuditTaskVoList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.INNER_ERROR.getCode(), ApiConst.INNER_ERROR.getMsg()+":"+e.getMessage());
        }

    }
}
