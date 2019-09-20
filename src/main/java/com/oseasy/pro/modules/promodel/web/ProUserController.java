/**
 *
 */
package com.oseasy.pro.modules.promodel.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.persistence.UserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.promodel.service.ProStudentExpansionService;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.service.StudentExpansionService;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 用户Controller
 *
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class ProUserController extends BaseController {
    @Autowired
    private CoreService coreService;
    @Autowired
    private ActTaskService actTaskService;
//    @Autowired
//    private ScoRapplyService scoRapplyService;
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private ProStudentExpansionService proStudentExpansionService;
    @Autowired
    private TeamUserRelationService teamUserRelationService;
    @Autowired
    private StudentExpansionService studentExpansionService;

    @RequestMapping(value="delUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult delUser(@RequestBody User user){
        try {
            if (UserUtils.getUser().getId().equals(user.getId())) {
                return ApiResult.failed(ApiConst.STATUS_FAIL,"删除用户失败, 不允许删除当前用户");
            } else if (user.getAdmin()) {
                return ApiResult.failed(ApiConst.STATUS_FAIL,"删除用户失败, 不允许删除超级管理员用户");
            } else {
                int todoNum = actTaskService.recordUserId(user.getId());
                if (todoNum > 0) {
                    return ApiResult.failed(ApiConst.STATUS_FAIL,"该用户还有流程任务，不能删除!");
                }
//                //如果该学生已经有学分，不能删除。
//                //申请学分
//                List<ScoRapply> list=scoRapplyService.findScoProjects(user.getId());
//                if (StringUtil.checkNotEmpty(list)) {
//                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"删除失败，此用户已申请过学分，请将学分删除后重新操作。");
//                }
                // 删除对应的学生信息
                if (CoreUtils.checkHasRole(user, RoleBizTypeEnum.XS.getValue())) {
                    TeamUserRelation teamUserRelation = new TeamUserRelation();
                    StudentExpansion studentExpansion = proStudentExpansionService.getByUserId(user.getId());
                    teamUserRelation.setUser(user);
                    teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                    if (teamUserRelation != null) {
                        return ApiResult.failed(ApiConst.STATUS_FAIL,"该学生已加入团队，不能删除!");
                    }
                    studentExpansionService.delete(studentExpansion);
                }
                // 删除对应的老师信息
                if (CoreUtils.checkHasRole(user, RoleBizTypeEnum.DS.getValue())) {
                    TeamUserRelation teamUserRelation = new TeamUserRelation();
                    teamUserRelation.setUser(user);
                    teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                    if (teamUserRelation != null) {
                        return ApiResult.failed(ApiConst.STATUS_FAIL,"该导师已加入团队，不能删除!");
                    }
                    BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService
                            .findTeacherByUserId(user.getId());
                    backTeacherExpansionService.delete(backTeacherExpansion);
                }

                coreService.deleteUser(user); // 删除用户

                return ApiResult.success();
            }
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }


    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "delete")
    public String delete(User user, RedirectAttributes redirectAttributes) {
        if (UserUtils.getUser().getId().equals(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (user.getAdmin()) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            int todoNum = actTaskService.recordUserId(user.getId());
            if (todoNum > 0) {
                addMessage(redirectAttributes, "该用户还有流程任务，不能删除!");
                return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
            }
            // 删除对应的学生信息
            if (CoreUtils.checkHasRole(user, RoleBizTypeEnum.XS.getValue())) {
                TeamUserRelation teamUserRelation = new TeamUserRelation();
                StudentExpansion studentExpansion = proStudentExpansionService.getByUserId(user.getId());
                teamUserRelation.setUser(user);
                teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                if (teamUserRelation != null) {
                    addMessage(redirectAttributes, "该学生已加入团队，不能删除!");
                    return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
                }
                studentExpansionService.delete(studentExpansion);
            }
            // 删除对应的老师信息
            if (CoreUtils.checkHasRole(user, RoleBizTypeEnum.DS.getValue())) {
                TeamUserRelation teamUserRelation = new TeamUserRelation();
                teamUserRelation.setUser(user);
                teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                if (teamUserRelation != null) {
                    addMessage(redirectAttributes, "该导师已加入团队，不能删除!");
                    return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
                }
                BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService
                        .findTeacherByUserId(user.getId());
                backTeacherExpansionService.delete(backTeacherExpansion);
            }

            coreService.deleteUser(user); // 删除用户

            addMessage(redirectAttributes, "删除用户成功");
        }
        return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
    }

    @RequestMapping(value="getUserById", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getUserById(User user, HttpServletRequest request, HttpServletResponse response, Model model){
        try {
            if (StringUtil.isEmpty(user.getId())) {
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":ID不能必填");
            }
            if (StringUtil.isEmpty(user.getUserType())) {
                return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":用户类型必填");
            }

            if((RoleBizTypeEnum.XS.getValue()).equals(user.getUserType())){
                return ApiResult.success(proStudentExpansionService.getByUserId(user.getId()));
            }else if((RoleBizTypeEnum.DS.getValue()).equals(user.getUserType())
                    || (RoleBizTypeEnum.XXZJ.getValue()).equals(user.getUserType())
                    || (RoleBizTypeEnum.XYZJ.getValue()).equals(user.getUserType())){
                return ApiResult.success(backTeacherExpansionService.getByUserId(user.getId()));
            }
            return ApiResult.failed(ApiConst.CODE_PARAM_ERROR_CODE,ApiConst.getErrMsg(ApiConst.CODE_PARAM_ERROR_CODE)+":用户类型未定义");
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "deletePL")
    public String deletePL(User user, RedirectAttributes redirectAttributes) {
        if(StringUtil.checkEmpty(user.getIds())){
            addMessage(redirectAttributes, "请选择用户!");
            return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
        }

        User curUser = null;
        for (String cid : user.getIds()) {
            if (((UserUtils.getUser().getId()).equals(user.getId())) || user.getAdmin()) {
                continue;
            }

            int todoNum = actTaskService.recordUserId(user.getId());
            if (todoNum > 0) {
                continue;
            }
            curUser = new User(cid);
            // 删除对应的学生信息
            if (CoreUtils.checkHasRole(curUser, RoleBizTypeEnum.XS.getValue())) {
                TeamUserRelation teamUserRelation = new TeamUserRelation();
                StudentExpansion studentExpansion = proStudentExpansionService.getByUserId(user.getId());
                teamUserRelation.setUser(user);
                teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                if (teamUserRelation != null) {
                    continue;
                }
                studentExpansionService.delete(studentExpansion);
            }

            // 删除对应的老师信息
            if (CoreUtils.checkHasRole(curUser, RoleBizTypeEnum.DS.getValue())) {
                TeamUserRelation teamUserRelation = new TeamUserRelation();
                teamUserRelation.setUser(user);
                teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                if (teamUserRelation != null) {
                    continue;
                }
                BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.findTeacherByUserId(user.getId());
                backTeacherExpansionService.delete(backTeacherExpansion);
            }
            coreService.deleteUser(user); // 删除用户
        }
        return CoreSval.REDIRECT + adminPath + "/sys/user/list?repage";
    }


    @RequestMapping(value="ajaxDelUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ApiResult ajaxDelUser(@RequestBody User user){
        try {
            User curUser = null;
            List<String> idList=user.getIds();
            for (String cid :idList) {
                if (((UserUtils.getUser().getId()).equals(cid))|| UserEntity.isSupUser(cid)) {
                    return ApiResult.failed(ApiConst.STATUS_FAIL,ApiConst.getErrMsg(ApiConst.STATUS_FAIL)+":删除用户失败, 不允许删除超级管理员用户");
                }

                int todoNum = actTaskService.recordUserId(cid);
                if (todoNum > 0) {
                    return ApiResult.failed(ApiConst.STATUS_FAIL,ApiConst.getErrMsg(ApiConst.STATUS_FAIL)+":该用户还有流程任务，不能删除!");
                }
                curUser = UserUtils.get(cid);
               // 删除对应的学生信息
                if (CoreUtils.checkHasRole(curUser, RoleBizTypeEnum.XS.getValue())) {
                    TeamUserRelation teamUserRelation = new TeamUserRelation();
                    StudentExpansion studentExpansion = proStudentExpansionService.getByUserId(cid);
                    teamUserRelation.setUser(curUser);
                    teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                    if (teamUserRelation != null) {
                        continue;
                    }
                        studentExpansionService.delete(studentExpansion);
                }
               // 删除对应的老师信息
                if (CoreUtils.checkHasRole(curUser, RoleBizTypeEnum.DS.getValue())) {
                    TeamUserRelation teamUserRelation = new TeamUserRelation();
                    teamUserRelation.setUser(curUser);
                    teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                    if (teamUserRelation != null) {
                        continue;
                    }
                    BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.findTeacherByUserId(cid);
                    backTeacherExpansionService.delete(backTeacherExpansion);
                }
                coreService.deleteUser(curUser); // 删除用户
            }
            return ApiResult.success();
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }
}
