package com.oseasy.pro.modules.promodel.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.pro.modules.promodel.service.ProTeamService;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.vo.TeamStudentVo;
import com.oseasy.sys.modules.team.vo.TeamTeacherVo;


/**
 * 团队管理Controller
 *
 * @author 刘波
 * @version 2017-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/team")
public class ProTeamController extends BaseController {
    public static final String teamEdit = "team:audit:edit";
    @Autowired
    private ProTeamService proTeamService;
    @Autowired
    private ProjectDeclareService projectDeclareService;

    @RequestMapping(value="getProjectListByTeamId", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult getProjectListByTeamId(@RequestBody Team team){
        try {
            List<ProjectExpVo> projectExpVos = proTeamService.findProjectByTeamId(team.getId());//查询项目经历
            return ApiResult.success(projectExpVos);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequestMapping(value="getGgcontestListByTeamId", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult getGgcontestListByTeamId(@RequestBody Team team){
        try {
            List<GContestUndergo> gContests = proTeamService.findGContestByTeamId(team.getId());
            return ApiResult.success(gContests);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    /**
     * 查询团队学生成员.
     *
     * @param teamid 团队ID
     * @param proId  项目ID
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxTeamStudent")
    public ApiTstatus<List<TeamStudentVo>> ajaxTeamStudent(@RequestParam(required = true) String teamid, @RequestParam(required = false) String proId) {
        List<TeamStudentVo> list = projectDeclareService.findTeamStudentByTeamId(teamid, proId);
        if ((list == null) || (list.size() <= 0)) {
            return new ApiTstatus<List<TeamStudentVo>>(true, "查询结果为空！");
        }
        return new ApiTstatus<List<TeamStudentVo>>(true, "查询成功", list);
    }

    /**
     * 查询团队导师成员.
     *
     * @param teamid 团队ID
     * @param proId  项目ID
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "ajaxTeamTeacher")
    public ApiTstatus<List<TeamTeacherVo>> ajaxTeamTeacher(@RequestParam(required = true) String teamid, @RequestParam(required = false) String proId) {
        List<TeamTeacherVo> list = projectDeclareService.findTeamTeacherByTeamId(teamid, proId);
        if ((list == null) || (list.size() <= 0)) {
            return new ApiTstatus<List<TeamTeacherVo>>(true, "查询结果为空！");
        }
        return new ApiTstatus<List<TeamTeacherVo>>(true, "查询成功", list);
    }
}
