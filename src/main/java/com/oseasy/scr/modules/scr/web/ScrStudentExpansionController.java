package com.oseasy.scr.modules.scr.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.service.ScoRapplyService;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/studentExpansion")
public class ScrStudentExpansionController extends BaseController {
    @Autowired
    private UserService  userService;
    @Autowired
    private ScoRapplyService scoRapplyService;
    @Autowired
    private TeamUserRelationService teamUserRelationService;
    @Autowired
    private StudentExpansionService studentExpansionService;
    @ModelAttribute
    public StudentExpansion get(@RequestParam(required = false) String id, Model model) {
        StudentExpansion entity = null;
        if (StringUtil.isNotBlank(id)) {
            entity = studentExpansionService.get(id);
        }
        if (entity == null) {
            entity = new StudentExpansion();
        }
        return entity;
    }
    //删除查询学生接口
    @RequestMapping(value="deleteStu", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult deleteStu(StudentExpansion studentExpansion){
        TeamUserRelation teamUserRelation = new TeamUserRelation();
        teamUserRelation.setUser(studentExpansion.getUser());
        teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
        try {
            if (teamUserRelation!=null) {
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"该学生已加入团队，不能删除!");
            }
            //申请学分
            List<ScoRapply> list=scoRapplyService.findScoProjects(studentExpansion.getUser().getId());
            if (StringUtil.checkNotEmpty(list)) {
                return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"删除失败，此用户已申请过学分，请将学分删除后重新操作。");
            }
            studentExpansionService.delete(studentExpansion);
            //删除用户表
            User user= UserUtils.get(studentExpansion.getUser().getId());
            userService.delete(user);
            return ApiResult.success();

        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}