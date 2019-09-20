package com.oseasy.pro.modules.promodel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.UserEntity;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.dao.ProBackTeacherExpansionDao;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 导师信息表Service
 * @author l
 * @version 2017-03-31
 */
@Service
@Transactional(readOnly = true)
public class ProBackTeacherExpansionService extends CrudService<ProBackTeacherExpansionDao, BackTeacherExpansion> {
    @Autowired
    UserService userService;
    @Autowired
    CoreService coreService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    ProActTaskService proActTaskService;
    @Autowired
    BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    TeamUserHistoryService teamUserHistoryService;
    @Autowired
    TeamUserRelationService teamUserRelationService;
    @Autowired
    BackTeacherExpansionDao backTeacherExpansionDao;

    public List<ProjectExpVo> findProjectByTeacherId(String id) {
        return dao.findProjectByTeacherId(id);
    }

    public List<GContestUndergo> findGContestByTeacherId(String id) {
        return dao.findGContestByTeacherId(id);
    }

    public List<BackTeacherExpansion> getUserToDoTaskList(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
        Role role1 = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
        if (role1 != null){
            actYwEtAssignTaskVo.setRoleId(role1.getId());
        }
        List<BackTeacherExpansion> list=dao.getUserToDoTaskList(actYwEtAssignTaskVo);
        for(BackTeacherExpansion expertIndex:list){
            List<Role> roleList = coreService.findListByUserId(expertIndex.getUser().getId());
            List<String> roleIdList= Lists.newArrayList();
            List<Role> expertRoleList = Lists.newArrayList();
            if(StringUtil.checkNotEmpty(roleList)){
                for(Role role:roleList){
                    if(     //role.getId().equals(SysIds.EXPERT_COLLEGE_EXPERT.getId())
//                          ||role.getId().equals(SysIds.EXPERT_SCHOOL_EXPERT.getId())
//                          ||role.getId().equals(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId())
//                          ||
                            role.getId().equals(coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey()).getId())){
                        expertRoleList.add(role);
                        roleIdList.add(role.getId());
                    }
                }
                expertIndex.getUser().setRoleList(expertRoleList);
                expertIndex.setRoleIdList(roleIdList);
            }
        }

        return list;
    }

    public List<BackTeacherExpansion> getUserTaskList(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
        return dao.getUserTaskList(actYwEtAssignTaskVo);
    }


    public Page<BackTeacherExpansion> findExpertPage(Page<BackTeacherExpansion> page, BackTeacherExpansion backTeacherExpansion) {
        backTeacherExpansion.setPage(page);
        Role role1 = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
        if (role1 != null){
            backTeacherExpansion.setRoleId(role1.getId());
        }
		backTeacherExpansion.setTenantId(TenantConfig.getCacheTenant());
        List<BackTeacherExpansion> list= backTeacherExpansionDao.findExpertList(backTeacherExpansion);
        for(BackTeacherExpansion expertIndex:list){
            List<Role> roleList = coreService.findListByUserId(expertIndex.getUser().getId());
            List<String> roleIdList= Lists.newArrayList();
            List<Role> expertRoleList = Lists.newArrayList();
            if(StringUtil.checkNotEmpty(roleList)){
                for(Role role:roleList){
                    if(
//                          role.getId().equals(SysIds.EXPERT_COLLEGE_EXPERT.getId())
//                          ||role.getId().equals(SysIds.EXPERT_SCHOOL_EXPERT.getId())
//                          ||role.getId().equals(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId())
//                          ||
                            role.getRtype().equals(CoreSval.Rtype.EXPORT.getKey())){
                        expertRoleList.add(role);
                        roleIdList.add(role.getId());
                    }
                }
                expertIndex.getUser().setRoleList(expertRoleList);
                expertIndex.setRoleIdList(roleIdList);
            }
            List<String> proNameList=//actTaskService.getTaskQueryByAssignee(expertIndex.getUser().getId())
                    proActTaskService.getExpertAuditPro(expertIndex.getUser().getId());
            expertIndex.setProNameList(proNameList);
        }

        page.setList(list);
        return page;
    }


    @Transactional(readOnly = false)
    public ApiResult getDelResult(String ids, String type) {
        String[] idStr = ids.split(",");
        int successCount = 0;
        int failCount = 0;
        HashMap<String, List<String>> hashMap= new HashMap<>();
        List<String> failIds = new ArrayList<>();
        List<String> successIds = new ArrayList<>();
        //删除单个人
        if(idStr.length==1){
            Boolean delAble = UserUtils.getUser().getId().equals(idStr[0]);
            Boolean isAdmin = UserEntity.isSupUser(idStr[0]);
            if (isAdmin) {
               return ApiResult.failed(ApiConst.STATUS_FAIL,"删除用户失败, 不允许删除超级管理员用户");
            }else if(delAble){
               return ApiResult.failed(ApiConst.STATUS_FAIL,"删除用户失败, 不允许删除当前用户");
            }
            if(BackTeacherExpansion.isTeacher.equals(type)){
                TeamUserRelation teamUserRelation = new TeamUserRelation();
                teamUserRelation.setUser(new User(idStr[0]));
                teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                if (teamUserRelation != null) {
                    return ApiResult.failed(ApiConst.CODE_INNER_ERROR,"该导师已加入团队，不能删除!");
                }
            }else if(BackTeacherExpansion.isExpert.equals(type)){
                int todoNum = actTaskService.recordUserId(idStr[0]);
                if (todoNum > 0) {
                    return ApiResult.failed(ApiConst.STATUS_FAIL,"该用户还有流程任务，不能删除!");
                }
            }
            backTeacherExpansionService.deleteByType(new User(idStr[0]), type);
            return ApiResult.success();
        }else {
            for (int i = 0; i < idStr.length; i++) {
                Boolean isFalse=false;
                User user = userService.findUserById(idStr[i]);
                Boolean delAble = UserUtils.getUser().getId().equals(idStr[i]);
                Boolean isAdmin = UserEntity.isSupUser(idStr[i]);
                if (isAdmin) {
                    isFalse=true;
                }else if(delAble){
                    isFalse=true;
                }
                if(BackTeacherExpansion.isTeacher.equals(type)){
                    TeamUserRelation teamUserRelation = new TeamUserRelation();
                    teamUserRelation.setUser(new User(idStr[i]));
                    teamUserRelation = teamUserRelationService.findUserById(teamUserRelation);
                    if (teamUserRelation != null) {
                        isFalse=true;
                    }
                }else if(BackTeacherExpansion.isExpert.equals(type)){
                    int todoNum = actTaskService.recordUserId(idStr[i]);
                    if (todoNum > 0) {
                        isFalse=true;
                    }
                }
                if (isFalse) {
                    failCount++;
                    failIds.add(idStr[i]);
                } else {
                    successCount++;
                    backTeacherExpansionService.deleteByType(user, type);
                    successIds.add(idStr[i]);
                }
            }
        }
        hashMap.put("failIds", failIds);
        hashMap.put("successIds", successIds);
        ApiResult.success(hashMap);
        String message;
        String roleName;
        if(BackTeacherExpansion.isTeacher.equals(type)){
            roleName="导师";
        }else{
            roleName="专家";
        }
        if (failCount == 0) {
            message = "成功删除" + successCount + "个"+roleName+"。";
        } else {
            message = "成功删除" + successCount + "个"+roleName+"。" + failCount + "个"+roleName+"有其他任务，不能删除!";
        }
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_SUCCESS);
        result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
        result.setData(hashMap);
        result.setMsg(message);
        return result;
    }

    @Transactional(readOnly = false)
    public String getResMsg(BackTeacherExpansion backTeacherExpansion) {
        int success=0;
        int fail=0;
        for(String userId:backTeacherExpansion.getUserIdList()){
            backTeacherExpansion.setId("");
            boolean isTrue=true;
            BackTeacherExpansion oldBackTeacherExpansion = backTeacherExpansionService.getByUserId(userId);
            User user = UserUtils.get(userId);
            if(oldBackTeacherExpansion!=null){
                backTeacherExpansion.setId(oldBackTeacherExpansion.getId());
            }
          //删除了导师身份 需要做判断。是否有项目进行
            if (!(backTeacherExpansion.getRoleType().contains(BackTeacherExpansion.isTeacher))
                    && teamUserHistoryService.getBuildingCountByUserId(user.getId()) > 0) {//修改时有正在进行的项目大赛
                fail++;
                isTrue=false;
            }
          //删除了专家身份 需要做判断。是否有任务进行
            if (!backTeacherExpansion.getRoleType().contains(BackTeacherExpansion.isExpert)) {//修改时有正在进行的项目大赛
                int todoNum = actTaskService.recordUserId(user.getId());
                if (todoNum > 0) {
                    fail++;
                    isTrue=false;
                }
            }
            if(isTrue){
                backTeacherExpansion.setUser(user);
                backTeacherExpansionService.updateExpertChange(backTeacherExpansion);
                success++;
            }
        }
        return "转换成功"+success+"人,"+"转换失败"+fail+"人";
    }
}