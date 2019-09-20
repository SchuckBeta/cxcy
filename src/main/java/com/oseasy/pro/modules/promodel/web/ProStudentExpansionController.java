package com.oseasy.pro.modules.promodel.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.pro.modules.promodel.service.ProStudentExpansionService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/studentExpansion")
public class ProStudentExpansionController extends BaseController {
    @Autowired
    private ProStudentExpansionService proStudentExpansionService;

    /**
     * 获取当前登录 学生基本信息，查询大赛经历.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value="ajaxGetUserProjectById")
    public ApiTstatus<List<ProjectExpVo>> ajaxGetUserProjectById(String userId) {
        if(StringUtil.isEmpty(userId)){
            userId = UserUtils.getUser().getId();
        }
        List<ProjectExpVo> projectExpVos = proStudentExpansionService.findProjectByStudentId(userId);
        if(StringUtil.checkEmpty(projectExpVos)){
            return new ApiTstatus<List<ProjectExpVo>>(true, "暂无记录");
        }
        return new ApiTstatus<List<ProjectExpVo>>(projectExpVos);
    }

    /**
     * 获取当前登录 学生基本信息，查询项目经历.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value="ajaxGetUserGContestById")
    public ApiTstatus<List<GContestUndergo>> ajaxGetUserGContestById(String userId) {
        if(StringUtil.isEmpty(userId)){
            userId = UserUtils.getUser().getId();
        }
        List<GContestUndergo> gcontestUndergos = proStudentExpansionService.findGContestByStudentId(userId);
        if(StringUtil.checkEmpty(gcontestUndergos)){
            return new ApiTstatus<List<GContestUndergo>>(true, "暂无记录");
        }
        return new ApiTstatus<List<GContestUndergo>>(gcontestUndergos);
    }
}