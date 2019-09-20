package com.oseasy.auy.modules.act.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.exception.ApplyException;
import com.oseasy.act.modules.actyw.exception.ProTimeException;
import com.oseasy.act.modules.actyw.service.ActYwApplyService;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.vo.ActYwApplyVo;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.jobserver.modules.task.entity.TaskScheduleJob;
import com.oseasy.com.jobserver.modules.task.service.TaskScheduleJobService;
import com.oseasy.com.pcore.common.config.CoreSval.PassNot;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pw.modules.pw.entity.PwAppointment;
import com.oseasy.pw.modules.pw.entity.PwAppointmentRule;
import com.oseasy.pw.modules.pw.service.PwAppointmentRuleService;
import com.oseasy.pw.modules.pw.service.PwAppointmentService;
import com.oseasy.pw.modules.pw.vo.PwAppointmentStatus;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程申请Service.
 *
 * @author zy
 * @version 2017-12-05
 */
@Service
@Transactional(readOnly = true)
public class AuyActYwApplyService {
    @Autowired
    ActYwApplyService actYwApplyService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    ProActTaskService proActTaskService;
    @Autowired
    ActYwService actYwService;
    @Autowired
    SystemService systemService;
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Autowired
    ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    IdentityService identityService;
    @Autowired
    ActDao actDao;
    @Autowired
    TaskScheduleJobService taskScheduleJobService;
    @Autowired
    PwAppointmentRuleService pwAppointmentRuleService;
    @Autowired
    PwAppointmentService pwAppointmentService;
@Autowired
private CoreService coreService;
    @Transactional(readOnly = false)
    public ApiTstatus<Object> audit(ActYwApplyVo actYwApply) {
        Map<String, Object> vars = new HashMap<String, Object>();
        if ((actYwApply.getActYw() == null) || StringUtil.isEmpty(actYwApply.getActYw().getId())) {
            return new ApiTstatus<Object>(false, "流程ID不能为空");
        }

        if ((actYwApply.getApplyUser() == null) || StringUtil.isEmpty(actYwApply.getApplyUser().getId())) {
            return new ApiTstatus<Object>(false, "申报人ID不能为空");
        }

        if (StringUtil.isEmpty(actYwApply.getProcInsId())) {
            return new ApiTstatus<Object>(false, "流程实例ID不能为空");
        }
        if (StringUtil.isEmpty(actYwApply.getGrade())) {
            return new ApiTstatus<Object>(false, "审核结果不能为空");
        }
        ActYw actYw = actYwService.get(actYwApply.getActYw().getId());

        if (actYw != null) {
        String taskId = actTaskService.getTaskidByProcInsId(actYwApply.getProcInsId());
        if (StringUtil.isEmpty(taskId)) {
            return new ApiTstatus<Object>(false, "审核节点不能为空");
            }
            String key = ActYw.getPkey(actYw);
            String taskDef = actTaskService.getTask(taskId).getTaskDefinitionKey();
            String nextGnodeRoleId = actTaskService.getProcessNextRoleName(taskDef, key);

            if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
                String nextRoleId = nextGnodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
                Role role = systemService.getNamebyId(nextRoleId);
                //启动节点
                String roleName = role.getName();
                List<String> roles = new ArrayList<String>();
                //判断角色是否含有学院级别信息
                if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                    roles = userService.findListByRoleIdAndOffice(role.getId(), actYwApply.getApplyUser().getId());
                } else {
                    roles = userService.findListByRoleId(role.getId());
                }
                if (StringUtil.checkEmpty(roles)) {
                    return new ApiTstatus<Object>(false, "流程配置故障（流程角色未配置），请联系管理员");
                }
                //后台学生角色id
                if (nextRoleId.equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())) {
                    roles.clear();
                    roles.add(userService.findUserById(actYwApply.getApplyUser().getId()).getName());
                }
                vars.put(nextGnodeRoleId + "s", roles);
            } else {
                //流程结束
            }

            //判断审核结果
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(actYwApply.getProcInsId());
            actYwAuditInfo.setPromodelId(actYwApply.getId());
            actYwAuditInfo.setGnodeId(actYwGnode.getId());
            if (actYwApply.getGrade() != null) {
                actYwAuditInfo.setGrade(actYwApply.getGrade());
            }
            actYwAuditInfo.setAuditName(actYwGnode.getName());
            actYwAuditInfoService.save(actYwAuditInfo);

            if ((PassNot.PASS.getKey()).equals(actYwApply.getGrade())) {
                taskService.complete(taskId, vars);
            }
        }else{
            return new ApiTstatus<Object>(false, "流程配置故障");
        }
        return new ApiTstatus<Object>(true, "流程审核成功");
    }

    @Transactional(readOnly = false)
    public void saveApplyAndSubmit(ActYwApply actYwApply) {
        if (actYwApply.getIsNewRecord()) {
            actYwApplyService.saveApply(actYwApply);
            ApiTstatus<?> rstatus = submit(new ActYwApplyVo(actYwApply.getId(), actYwApply.getActYw(), actYwApply.getApplyUser()));
            if (!rstatus.getStatus()) {
                throw new ApplyException(rstatus.getMsg(), null);
            }
        }else{
            actYwApplyService.saveApply(actYwApply);
        }
    }

    @Transactional(readOnly = false)
    public ApiTstatus<Object> submit(ActYwApplyVo actYwApply) throws ProTimeException{
    /**********************************************************************
       * 处理申报参数是否合法.
     * 1、申报ID不能为空.
     * 2、申报人ID不能为空.
     * 3、申报流程ID不能为空.
       */
      if ((actYwApply == null) || StringUtil.isEmpty(actYwApply.getId())) {
      return new ApiTstatus<Object>(false, "申报ID不能为空");
    }

    if ((actYwApply.getApplyUser() == null) || StringUtil.isEmpty(actYwApply.getApplyUser().getId())) {
      return new ApiTstatus<Object>(false, "申报人ID不能为空");
    }

    if ((actYwApply.getActYw() == null) || StringUtil.isEmpty(actYwApply.getActYw().getId())) {
      return new ApiTstatus<Object>(false, "流程ID不能为空");
    }

        ActYw actYw = actYwService.get(actYwApply.getActYw().getId());

        /**********************************************************************
     * 处理申报流程配置是否合法.
     * 1、流程周期.
     * 2、申报有效期.
     * 3、节点有效期.
     */
      if (actYw != null && actYw.getProProject() != null) {
      /**
       * 判断周期.
       */
        if (actYwApplyService.checkValidDate(actYw.getProProject().getStartDate(), actYw.getProProject().getEndDate())) {
          return new ApiTstatus<Object>(false, "提交失败，不在有效时间内");
        }

        /**
         * 判断申报时间.
         */
        //TODO 申报时间
        /*if (actYw.getProProject().getNodeState()) {
          if (checkValidDate(actYw.getProProject().getNodeStartDate(), actYw.getProProject().getNodeEndDate())) {
          return new ActYwRstatus(false, "提交失败，不在申报有效时间内");
        }
        }*/
        }else{
          return new ApiTstatus<Object>(false, "项目流程配置信息不能为空");
        }

          /**********************************************************************
           * 处理申报流程角色和业务.
           */
        List<String> roles = new ArrayList<String>();
        //启动工作流
        if (actYw != null) {
            String nodeRoleId = actTaskService.getProcessStartRoleName(ActYw.getPkey(actYw));  //从工作流中查询 下一步的角色集合
            String roleId = nodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
            Role role = systemService.getNamebyId(roleId);
            if (role != null) {
                //启动节点
                String roleName = role.getName();
                if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                    roles = userService.findListByRoleIdAndOffice(role.getId(), actYwApply.getApplyUser().getId());
                } else {
                    roles= userService.findListByRoleId(role.getId());
                }
                if (roles.size() > 0) {
                    Map<String, Object> vars = new HashMap<String, Object>();
                    vars.put("id", actYwApply.getId());
                    vars.put(nodeRoleId + "s", roles);
                    String key = ActYw.getPkey(actYw);
                    String userId = UserUtils.getUser().getId();
                    identityService.setAuthenticatedUserId(userId);
                    ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key, "act_yw_apply:" + actYwApply.getId(), vars, TenantConfig.getCacheTenant());
                    //流程id返写业务表
                    if (procIns != null) {
                        Act act = new Act();
                        act.setBusinessTable("pro_model");// 业务表名
                        act.setBusinessId(actYwApply.getId());    // 业务表ID
                        act.setProcInsId(procIns.getId());
                        actDao.updateProcInsIdByBusinessId(act);
                        actYwApply.setProcInsId(act.getProcInsId());
                        actYwApplyService.updateProcInsId(actYwApply);
                        return new ApiTstatus<Object>(true, "恭喜您，申报成功！");
                    } else {
                        return new ApiTstatus<Object>(false, "流程配置故障（审核流程未启动），请联系管理员");
                    }
                }
            }
            return new ApiTstatus<Object>(false, "流程配置故障（流程角色未配置），请联系管理员");
        }
        return new ApiTstatus<Object>(false, "流程配置故障（审核流程不存在），请联系管理员");
    }



    //反射机制自动审核
    @Transactional(readOnly = false)
    public ApiTstatus<Object> timeAudit(TaskScheduleJob job) {
//      ActYwApplyService actYwApplyServiceRo = SpringUtils.getBean("ActYwApplyService");
//      PwAppointmentService pwAppointmentServiceRo = SpringUtils.getBean("PwAppointmentService");
        String actYwApplyId=job.getJobName();
        ActYwApply actYwApply = actYwApplyService.get(actYwApplyId);
        String type=actYwApply.getType();
        //预约流程
        if (FlowType.FWT_APPOINTMENT.getKey().equals(type)) {
            PwAppointment pwAppointment =pwAppointmentService.get(actYwApply.getRelId());
            //判断是否已经审核 没有自动审核通过
            if (!pwAppointment.getStatus().equals(PwAppointmentStatus.WAIT_AUDIT.getValue())) {
                return new ApiTstatus<Object>(false, "预约申请不是待审核状态");
            }
            if (pwAppointment.getEndDate().before(new Date())) {
                return new ApiTstatus<Object>(true, "已过期，无需审核");
            }
//          if (StringUtil.isEmpty(pwAppointment.getStatus())) {
                ActYwApplyVo actYwApplyVo=new ActYwApplyVo();
                actYwApplyVo.setId(actYwApply.getId());
                actYwApplyVo.setGrade(PassNot.PASS.getKey());
                actYwApplyVo.setProcInsId(actYwApply.getProcInsId());
                actYwApplyVo.setApplyUser(actYwApply.getApplyUser());
                ActYw actYw = actYwService.get(actYwApply.getActYw().getId());
                actYwApplyVo.setActYw(actYw);
                actYwApplyVo.setType(actYwApply.getType());
                ApiTstatus<Object> actYwRstatus = audit(actYwApplyVo);
                if (actYwRstatus.getStatus()) {
                    pwAppointmentService.changeStatus(actYwApply.getRelId(), PwAppointmentStatus.PASS);
//                  return new ActYwRstatus(true, "审核通过");
                }
                return actYwRstatus;
//          }
//          return new ActYwRstatus(true, "该预约已经被审核");
        }else {
            return new ApiTstatus<Object>(false, "不是预约流程");
        }
    }

    /**
     * 审核预约
     * @param actYwApply
     * @param grade
     */
    @Transactional(readOnly = false)
    public void pwAppointAudit(ActYwApply actYwApply, String grade) {
        ActYwApplyVo actYwApplyVo = new ActYwApplyVo();
        actYwApplyVo.setId(actYwApply.getId());
        actYwApplyVo.setGrade(grade);
        actYwApplyVo.setProcInsId(actYwApply.getProcInsId());
        actYwApplyVo.setApplyUser(actYwApply.getApplyUser());
        ActYw actYw = actYwService.get(actYwApply.getActYw().getId());
        actYwApplyVo.setActYw(actYw);
        actYwApplyVo.setType(actYwApply.getType());
        ApiTstatus<Object> actYwRstatus = audit(actYwApplyVo);
        if (actYwRstatus.getStatus()) {
            PwAppointmentStatus status = (PassNot.PASS.getKey()).equals(grade) ? PwAppointmentStatus.PASS : PwAppointmentStatus.REJECT;
            pwAppointmentService.changeStatus(actYwApply.getRelId(), status);
        }
    }
    //启动任务 自动审核
    @Transactional(readOnly = false)
    public ApiTstatus<Object> timeSubmit(ActYwApplyVo actYwApply) {
        ApiTstatus<Object> actYwRstatus= submit(actYwApply);
        if (actYwRstatus.getStatus()) {
            try {
                //添加任务（设置类和方法。）
                TaskScheduleJob job=new TaskScheduleJob();
                job.setJobGroup("appAudit");
                job.setJobName(actYwApply.getId());
                job.setMethodName("timeAudit");
//              job.setBeanClass("com.oseasy.act.modules.actyw.service.ActYwApplyService");
                job.setSpringId("actYwApplyService");
                PwAppointmentRule pwAppointmentRule =pwAppointmentRuleService.getPwAppointmentRule();
                //设置延迟审核时间
                if (pwAppointmentRule!=null && StringUtil.isNotEmpty(pwAppointmentRule.getAutoTime())) {
                    job.setAftertMin(Integer.valueOf(pwAppointmentRule.getAutoTime()));
                }
                taskScheduleJobService.addJobByOther(job);
                taskScheduleJobService.addTask(job);
                return new ApiTstatus<Object>(true, "提交成功");

            } catch (SchedulerException e) {
                e.printStackTrace();
                return new ApiTstatus<Object>(false, "流程配置故障（审核流程不存在），请联系管理员");
            }
        }else{
            return actYwRstatus;
        }
    }
}