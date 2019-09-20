package com.oseasy.pro.modules.promodel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.service.ProvinceProModelService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignRule;
import com.oseasy.act.modules.actyw.entity.ActYwEtAuditNum;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwEtAuditNumService;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.vo.EarAtype;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 业务指派表Service.
 * @author zy
 * @version 2018-04-03
 */
@Service
@Transactional(readOnly = true)
public class ProActYwGassignService {
    protected static final Logger logger = Logger.getLogger(ActYwService.class);
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    ActYwGassignService actYwGassignService;
    @Autowired
    ProActTaskService proActTaskService;
    @Autowired
    TaskService taskService;
    @Autowired
    ProModelService proModelService;
    @Autowired
    ProvinceProModelService provinceProModelService;
    @Autowired
    ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    OaNotifyService oaNotifyService;
    @Autowired
    ActYwService actYwService;
    @Autowired
    private ProModelDao proModelDao;
    @Autowired
    ActYwEtAuditNumService actYwEtAuditNumService;
    @Autowired
    ActYwGnodeService actYwGnodeService;

    // 指派节点 项目id 节点属性 审核人
    public Boolean getAssignBoolean(String promodelId, ActYwGassign actYwGassign, List<String> userIdList,
            ProModel proModel) {
        List<Task> currentTaskEntityList2 = taskService.createTaskQuery()
                .taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX + actYwGassign.getGnodeId())
                .processInstanceId(proModel.getProcInsId()).list();
        if (currentTaskEntityList2 == null || currentTaskEntityList2.size() == 0){
            return false;
        }
        TaskEntity currentTaskEntity2 = (TaskEntity) currentTaskEntityList2.get(0);
        // 流程走下下一个节点
        String toGnodeId = "";

        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(promodelId);
        // 判断流程节点最后审核信息
        ActYwAuditInfo lastActYwAuditInfo = actYwAuditInfoService.getLastAuditByPromodel(actYwAuditInfo);
        // 判断流程节点是否变化
        String isChangeNode = (String) taskService.getVariable(currentTaskEntity2.getId(), "isChangeNode");
        if (StringUtil.isNotEmpty(isChangeNode) && "1".equals(isChangeNode)) {
            List<ActYwAuditInfo> lastActYwAuditInfoList = actYwAuditInfoService
                    .getLastAuditListByPromodel(actYwAuditInfo);
            for (int i = 0; i < lastActYwAuditInfoList.size(); i++) {
                ActYwAuditInfo actYwAuditInfoIn = lastActYwAuditInfoList.get(i);
                // 流程节点变化 有没有审核中的数据 如果有 取流程中节点
                if (actYwAuditInfoIn != null && actYwAuditInfoIn.getGnodeId().equals(actYwGassign.getGnodeId())) {
                    if (i + 1 < lastActYwAuditInfoList.size()) {
                        toGnodeId = lastActYwAuditInfoList.get(i + 1).getGnodeId();
                    }
                }
            }
        }
        // 判断流程节点为空 则取最后一次审核的节点
        if (StringUtil.isEmpty(toGnodeId) && lastActYwAuditInfo != null) {
            toGnodeId = lastActYwAuditInfo.getGnodeId();
        }
        Boolean res = false;
        if (lastActYwAuditInfo != null) {
            // 回退上一步节点 并向前走一步
            res = proActTaskService.rollBackWorkFlow(proModel, currentTaskEntity2.getId(), actYwGassign.getGnodeId(),
                    toGnodeId, userIdList);
        } else {
            // 开始第一个节点为起始节点
            res = proActTaskService.rollBackWorkFlowStart(proModel, currentTaskEntity2.getId(), userIdList);
            proModelService.save(proModel);
        }
        return res;
    }

    public Boolean getProvAssignBoolean(String promodelId, ActYwGassign actYwGassign, List<String> userIdList,
                                        ProvinceProModel provinceProModel) {
        List<Task> currentTaskEntityList2 = taskService.createTaskQuery()
                .taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX + actYwGassign.getGnodeId())
                .processInstanceId(provinceProModel.getProcInsId()).list();
        TaskEntity currentTaskEntity2 = (TaskEntity) currentTaskEntityList2.get(0);
        // 流程走下下一个节点
        String toGnodeId = "";

        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(promodelId);
        // 判断流程节点最后审核信息
        ActYwAuditInfo lastActYwAuditInfo = actYwAuditInfoService.getLastAuditByPromodel(actYwAuditInfo);
        // 判断流程节点是否变化
        String isChangeNode = (String) taskService.getVariable(currentTaskEntity2.getId(), "isChangeNode");
        if (StringUtil.isNotEmpty(isChangeNode) && "1".equals(isChangeNode)) {
            List<ActYwAuditInfo> lastActYwAuditInfoList = actYwAuditInfoService
                    .getLastAuditListByPromodel(actYwAuditInfo);
            for (int i = 0; i < lastActYwAuditInfoList.size(); i++) {
                ActYwAuditInfo actYwAuditInfoIn = lastActYwAuditInfoList.get(i);
                // 流程节点变化 有没有审核中的数据 如果有 取流程中节点
                if (actYwAuditInfoIn != null && actYwAuditInfoIn.getGnodeId().equals(actYwGassign.getGnodeId())) {
                    if (i + 1 < lastActYwAuditInfoList.size()) {
                        toGnodeId = lastActYwAuditInfoList.get(i + 1).getGnodeId();
                    }
                }
            }
        }
        // 判断流程节点为空 则取最后一次审核的节点
        if (StringUtil.isEmpty(toGnodeId) && lastActYwAuditInfo != null) {
            toGnodeId = lastActYwAuditInfo.getGnodeId();
        }
        Boolean res = false;
        if (lastActYwAuditInfo != null) {
            // 回退上一步节点 并向前走一步
            res = proActTaskService.rollBackProvWorkFlow(provinceProModel, currentTaskEntity2.getId(), actYwGassign.getGnodeId(),
                    toGnodeId, userIdList);
        } else {
            // 开始第一个节点为起始节点
            res = proActTaskService.rollBackProvWorkFlowStart(provinceProModel, currentTaskEntity2.getId(), userIdList);
            provinceProModelService.save(provinceProModel);
        }
        return res;
    }

    @Transactional(readOnly = false)
    public boolean saveProModelAssignByList(String promodelId, ActYwGassign actYwGassign, List<String> userIdList) {
        String tenantId = TenantConfig.getCacheTenant();
        ProvinceProModel provinceProModel = null;
        ProModel proModel = null;
        if (tenantId.equals("20")){
//            provinceProModel = provinceProModelService.get(promodelId);
//            if (provinceProModel != null){
//                proModel = proModelService.get(provinceProModel.getModelId());
//                //省流程实例
//                proModel.setProcInsId(provinceProModel.getProcInsId());
//                proModel.setActYwId(provinceProModel.getActYwId());
//            }
        }else{
            proModel = proModelService.get(promodelId);
        }
        if(proModel==null){
            return false;
        }
        ActYwGnode actYwGnode = actYwGnodeService.get(actYwGassign.getGnodeId());
        Boolean isDelegate = actYwGnode.getIsDelegate();
        // 判断是否已经被指派过
        actYwGassign.setPromodelId(promodelId);
        Boolean isHasAssign = actYwGassignService.isHasAssign(actYwGassign);
        // 已经被指派过
        if (isHasAssign) {
            if (!isDelegate) {
                actYwGassignService.sendAssignMsg(promodelId, actYwGassign, userIdList, proModel.getpName());
            }
            // 删除旧的指派记录
            actYwGassignService.deleteByAssign(actYwGassign);
        } else {
            if (!isDelegate) {
                oaNotifyService.sendOaNotifyByTypeAndUser(UserUtils.getUser(), userIdList, "指派任务",
                        "管理员指派给你" + proModel.getpName() + "项目审核任务", OaNotify.Type_Enum.TYPE20.getValue(),
                        proModel.getId());
            }
        }
        Boolean res = true;
        if (!isDelegate) {
            res = getAssignBoolean(promodelId, actYwGassign, userIdList, proModel);
        }
        return res;
    }

    @Transactional(readOnly = false)
    public boolean saveProvProModelAssignByList(String promodelId, ActYwGassign actYwGassign, List<String> userIdList) {
        ProvinceProModel provinceProModel = provinceProModelService.get(promodelId);
        if(provinceProModel==null){
            return false;
        }
        ActYwGnode actYwGnode = actYwGnodeService.get(actYwGassign.getGnodeId());
        Boolean isDelegate = actYwGnode.getIsDelegate();
        // 判断是否已经被指派过
        actYwGassign.setPromodelId(promodelId);
        Boolean isHasAssign = actYwGassignService.isHasAssign(actYwGassign);
        // 已经被指派过
        if (isHasAssign) {
            if (!isDelegate) {
//                actYwGassignService.sendAssignMsg(promodelId, actYwGassign, userIdList, proModel.getpName());
            }
            // 删除旧的指派记录
            actYwGassignService.deleteByAssign(actYwGassign);
        } else {
            if (!isDelegate) {
//                actYwGassignService.sendAssignMsg(promodelId, actYwGassign, userIdList, proModel.getpName());
            }
        }
        Boolean res = true;
        if (!isDelegate) {
            res = getProvAssignBoolean(promodelId, actYwGassign, userIdList, provinceProModel);
        }
        return res;
    }

    @Transactional(readOnly = false)
    public boolean saveProModelAssign(String promodelId, ActYwGassign actYwGassign, String userIds) {
        List<String> userIdList = Arrays.asList(userIds.split(","));
        return saveProModelAssignByList(promodelId, actYwGassign, userIdList);
    }

    @Transactional(readOnly = false)
    public Boolean getAssignClearBoolean(String promodelId, ActYwGassign actYwGassign, ProModel proModel) {
        List<Task> currentTaskEntityList2 = taskService.createTaskQuery()
                .taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX + actYwGassign.getGnodeId())
                .processInstanceId(proModel.getProcInsId()).list();
        TaskEntity currentTaskEntity2 = (TaskEntity) currentTaskEntityList2.get(0);
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(promodelId);
        // 判断流程节点最后审核信息
        ActYwAuditInfo lastActYwAuditInfo = actYwAuditInfoService.getLastAuditByPromodel(actYwAuditInfo);
        String toGnodeId = actYwGassign.getGnodeId();
        Boolean res = false;
        if (lastActYwAuditInfo != null) {
            // 回退上一步节点
            res = proActTaskService.rollBackWork(currentTaskEntity2.getId(), actYwGassign.getGnodeId(),
                    toGnodeId, null);
        } else {
            // 开始第一个节点为起始节点
            res = proActTaskService.rollBackWorkStart(proModel, currentTaskEntity2.getId());
            proModelService.save(proModel);
        }
        return res;
    }

    @Transactional(readOnly = false)
    public Boolean getProvAssignClearBoolean(String promodelId, ActYwGassign actYwGassign, ProvinceProModel provinceProModel) {
        List<Task> currentTaskEntityList2 = taskService.createTaskQuery()
                .taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX + actYwGassign.getGnodeId())
                .processInstanceId(provinceProModel.getProcInsId()).list();
        TaskEntity currentTaskEntity2 = (TaskEntity) currentTaskEntityList2.get(0);
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(promodelId);
        // 判断流程节点最后审核信息
        ActYwAuditInfo lastActYwAuditInfo = actYwAuditInfoService.getLastAuditByPromodel(actYwAuditInfo);
        String toGnodeId = actYwGassign.getGnodeId();
        Boolean res = false;
        if (lastActYwAuditInfo != null) {
            // 回退上一步节点
            res = proActTaskService.rollBackWork(currentTaskEntity2.getId(), actYwGassign.getGnodeId(),
                    toGnodeId, null);
        } else {
            // 开始第一个节点为起始节点
            res = proActTaskService.rollBackWorkProvStart(provinceProModel, currentTaskEntity2.getId());
            provinceProModelService.save(provinceProModel);
        }
        return res;
    }


    @Transactional(readOnly = false)
    public void saveAssign(ActYwGassign actYwGassign, String promodelIds, String userIds) {
        actYwGassign = ActYwGassign.initType(actYwGassign, actYwGassignService.getGnode(actYwGassign));
        String[] promodelList = promodelIds.split(",");
        List<String> promodeIdList = Arrays.asList(promodelList);
        List<String> userIdList = Arrays.asList(userIds.split(","));
        List<String> succPromodeIdList = new ArrayList<String>();
        List<String> failPromodeIdList = new ArrayList<String>();
        if (promodelList.length > 0) {
            for (int i = 0; i < promodeIdList.size(); i++) {
                String promodelId = promodeIdList.get(i);
                boolean res = saveProModelAssign(promodelId, actYwGassign, userIds);
                if (res) {
                    logger.info("指派成功项目id：" + promodelId);
                    succPromodeIdList.add(promodelId);
                } else {
                    logger.info("指派失败项目id：" + promodelId);
                    failPromodeIdList.add(promodelId);
                }
            }
        }
        if (StringUtil.checkNotEmpty(succPromodeIdList)) {
            List<ActYwGassign> insertActYwGassignList = new ArrayList<ActYwGassign>();
            for (int i = 0; i < succPromodeIdList.size(); i++) {
                String promodelId = succPromodeIdList.get(i);
                for (int j = 0; j < userIdList.size(); j++) {
                    String userId = userIdList.get(j);
                    ActYwGassign actYwGassignIndex = new ActYwGassign();
                    actYwGassignIndex.setId(IdGen.uuid());
                    actYwGassignIndex.setYwId(actYwGassign.getYwId());
                    actYwGassignIndex.setPromodelId(promodelId);
                    actYwGassignIndex.setRevUserId(userId);
                    actYwGassignIndex.setGnodeId(actYwGassign.getGnodeId());
                    actYwGassignIndex.setYwId(actYwGassign.getYwId());
                    actYwGassignIndex.setType(actYwGassign.getType());
                    insertActYwGassignList.add(actYwGassignIndex);
                }
            }
            actYwGassignService.insertPl(insertActYwGassignList);
        }
    }

    @Transactional(readOnly = false)
    public void ajaxGetManuallyAssgin(ActYwEtAssignRule actYwEtAssignRule) {
        List<String> promodeIdList = actYwEtAssignRule.getProIdList();
        List<String> userIdList = actYwEtAssignRule.getUserIdList();
        // 清除当前项目分配
        clearAssignUserTask(actYwEtAssignRule);
        ActYwGnode gnode = actYwGnodeService.get(actYwEtAssignRule.getGnodeId());
        for (String proId : promodeIdList) {
            ActYwGassign actYwGassign = new ActYwGassign();
            actYwGassign.setYwId(actYwEtAssignRule.getActywId());
            actYwGassign.setGnodeId(actYwEtAssignRule.getGnodeId());
            // 设置指派点
            boolean res = saveProModelAssignByList(proId, actYwGassign, userIdList);
            if (res) {
                List<ActYwGassign> insertActYwGassignList = new ArrayList<ActYwGassign>();
                for (int j = 0; j < userIdList.size(); j++) {
                    String userId = userIdList.get(j);
                    ActYwGassign actYwGassignIndex = new ActYwGassign();
                    actYwGassignIndex.setId(IdGen.uuid());
                    actYwGassignIndex.setYwId(actYwGassign.getYwId());
                    actYwGassignIndex.setPromodelId(proId);
                    if (gnode.getIsDelegate()) {
                        actYwGassignIndex.setType(EarAtype.WP.getKey());
                    } else if (gnode.getIsAssign()) {
                        actYwGassignIndex.setType(EarAtype.ZP.getKey());
                    }
                    actYwGassignIndex.setAssignUserId(UserUtils.getUserId());
                    actYwGassignIndex.setRevUserId(userId);
                    actYwGassignIndex.setGnodeId(actYwGassign.getGnodeId());
                    actYwGassignIndex.setYwId(actYwGassign.getYwId());
                    insertActYwGassignList.add(actYwGassignIndex);
                }
                actYwGassignService.insertPl(insertActYwGassignList);
            }
        }
    }

    @Transactional(readOnly = false)
    public void ajaxGetProvManuallyAssgin(ActYwEtAssignRule actYwEtAssignRule) {
        List<String> promodeIdList = actYwEtAssignRule.getProIdList();
        List<String> userIdList = actYwEtAssignRule.getUserIdList();
        // 清除当前项目分配
        clearProvAssignUserTask(actYwEtAssignRule);
        ActYwGnode gnode = actYwGnodeService.get(actYwEtAssignRule.getGnodeId());
        for (String proId : promodeIdList) {
            ActYwGassign actYwGassign = new ActYwGassign();
            actYwGassign.setYwId(actYwEtAssignRule.getActywId());
            actYwGassign.setGnodeId(actYwEtAssignRule.getGnodeId());
            // 设置指派点
            boolean res = saveProvProModelAssignByList(proId, actYwGassign, userIdList);
            if (res) {
                List<ActYwGassign> insertActYwGassignList = new ArrayList<ActYwGassign>();
                for (int j = 0; j < userIdList.size(); j++) {
                    String userId = userIdList.get(j);
                    ActYwGassign actYwGassignIndex = new ActYwGassign();
                    actYwGassignIndex.setId(IdGen.uuid());
                    actYwGassignIndex.setYwId(actYwGassign.getYwId());
                    actYwGassignIndex.setPromodelId(proId);
                    if (gnode.getIsDelegate()) {
                        actYwGassignIndex.setType(EarAtype.WP.getKey());
                    } else if (gnode.getIsAssign()) {
                        actYwGassignIndex.setType(EarAtype.ZP.getKey());
                    }
                    actYwGassignIndex.setAssignUserId(UserUtils.getUserId());
                    actYwGassignIndex.setRevUserId(userId);
                    actYwGassignIndex.setGnodeId(actYwGassign.getGnodeId());
                    actYwGassignIndex.setYwId(actYwGassign.getYwId());
                    insertActYwGassignList.add(actYwGassignIndex);
                }
                actYwGassignService.insertPl(insertActYwGassignList);
            }
        }
    }

    // 清除已经分配专家的任务
    @Transactional(readOnly = false)
    public void clearAssignUserTask(ActYwEtAssignRule actYwEtAssignRule) {
        List<String> promodelIdList = actYwEtAssignRule.getProIdList();
        if (StringUtil.checkEmpty(promodelIdList)) {
            promodelIdList = getProList(actYwEtAssignRule, promodelIdList);
        }
        for (int i = 0; i < promodelIdList.size(); i++) {
            String promodelId = promodelIdList.get(i);
            ActYwGassign actYwGassign = new ActYwGassign();
            actYwGassign.setPromodelId(promodelId);
            actYwGassign.setGnodeId(actYwEtAssignRule.getGnodeId());
            actYwGassign.setYwId(actYwEtAssignRule.getActywId());
            List<ActYwGassign> assignList = actYwGassignService.getListByActYwGassign(actYwGassign);
            if (StringUtil.checkNotEmpty(assignList)) {
                // 将流程任务删除 停留在待指派状态。
                ProModel proModel = proModelService.get(promodelId);
                ActYwGnode actYwGnode = actYwGnodeService.get(actYwGassign.getGnodeId());
                if (!actYwGnode.getIsDelegate()) {
                    Boolean res = getAssignClearBoolean(promodelId, actYwGassign, proModel);
                    if (res) {
                        actYwGassignService.deleteByAssign(actYwGassign);
                    }
                    // 清除当前节点 当前项目的审核记录
                    ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
                    actYwAuditInfo.setPromodelId(promodelId);
                    actYwAuditInfo.setGnodeId(actYwEtAssignRule.getGnodeId());
                    actYwAuditInfoService.deleteByProIdAndGnodeId(actYwAuditInfo);
                } else {
                    actYwGassignService.deleteByAssign(actYwGassign);
                }
            }

            // 清除指派中专家项目计数
            ActYwEtAuditNum actYwEtAuditNum = new ActYwEtAuditNum();
            actYwEtAuditNum.setProId(promodelId);
            actYwEtAuditNum.setActywId(actYwEtAssignRule.getActywId());
            actYwEtAuditNum.setGnodeId(actYwEtAssignRule.getGnodeId());
            actYwEtAuditNumService.deleteByProId(actYwEtAuditNum);
        }
    }

    // 清除已经分配专家的任务
   @Transactional(readOnly = false)
   public void clearProvAssignUserTask(ActYwEtAssignRule actYwEtAssignRule) {
       List<String> promodelIdList = actYwEtAssignRule.getProIdList();
       if (StringUtil.checkEmpty(promodelIdList)) {
           promodelIdList = getProvProList(actYwEtAssignRule);
       }
       for (int i = 0; i < promodelIdList.size(); i++) {
           String promodelId = promodelIdList.get(i);
           ActYwGassign actYwGassign = new ActYwGassign();
           actYwGassign.setPromodelId(promodelId);
           actYwGassign.setGnodeId(actYwEtAssignRule.getGnodeId());
           actYwGassign.setYwId(actYwEtAssignRule.getActywId());
           List<ActYwGassign> assignList = actYwGassignService.getListByActYwGassign(actYwGassign);
           if (StringUtil.checkNotEmpty(assignList)) {
               // 将流程任务删除 停留在待指派状态。
               ProvinceProModel provinceProModel = provinceProModelService.get(promodelId);
               ActYwGnode actYwGnode = actYwGnodeService.get(actYwGassign.getGnodeId());
               if (!actYwGnode.getIsDelegate()) {
                   Boolean res = getProvAssignClearBoolean(promodelId, actYwGassign, provinceProModel);
                   if (res) {
                       actYwGassignService.deleteByAssign(actYwGassign);
                   }
                   // 清除当前节点 当前项目的审核记录
                   ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
                   actYwAuditInfo.setPromodelId(promodelId);
                   actYwAuditInfo.setGnodeId(actYwEtAssignRule.getGnodeId());
                   actYwAuditInfoService.deleteByProIdAndGnodeId(actYwAuditInfo);
               } else {
                   actYwGassignService.deleteByAssign(actYwGassign);
               }
           }

           // 清除指派中专家项目计数
           ActYwEtAuditNum actYwEtAuditNum = new ActYwEtAuditNum();
           actYwEtAuditNum.setProId(promodelId);
           actYwEtAuditNum.setActywId(actYwEtAssignRule.getActywId());
           actYwEtAuditNum.setGnodeId(actYwEtAssignRule.getGnodeId());
           actYwEtAuditNumService.deleteByProId(actYwEtAuditNum);
       }
   }

	public List<String> getProList(ActYwEtAssignRule actYwEtAssignRule, List<String> promodelIdList) {
		if(StringUtil.checkEmpty(promodelIdList)){
			ActYw actYw=actYwService.get(actYwEtAssignRule.getActywId());
			String key = ActYw.getPkey(actYw);
			Act act = new Act();
			act.setProcDefKey(key);  //流程标识
			List<String> gnodeIdList = new ArrayList<String>();
			gnodeIdList.add(actYwEtAssignRule.getGnodeId());
			List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actYwEtAssignRule.getActywId());
			if(StringUtil.checkEmpty(recordIds)){
				return promodelIdList;
			}
			ProModel proModel =new ProModel();
			proModel.setActYwId(actYwEtAssignRule.getActywId());
			proModel.setGnodeId(actYwEtAssignRule.getGnodeId());
			proModel.setIds(recordIds);
//			List<ProModel> list = proModelDao.findListByIdsAssign(proModel);
			List<ProModel> list = proModelDao.findListByIdsAssignToClear(proModel);
			promodelIdList= Lists.transform(list, proIndex->proIndex.getId());
		}
		return promodelIdList;
	}

	public List<String> getProList(ActYwEtAssignRule actYwEtAssignRule) {
	    List<String> promodelIdList =new ArrayList<String>();
		ActYw actYw=actYwService.get(actYwEtAssignRule.getActywId());
		String key = ActYw.getPkey(actYw);
		Act act = new Act();
		act.setProcDefKey(key);  //流程标识
		List<String> gnodeIdList = new ArrayList<String>();
		gnodeIdList.add(actYwEtAssignRule.getGnodeId());
		List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actYwEtAssignRule.getActywId());
		if(StringUtil.checkEmpty(recordIds)){
			return promodelIdList;
		}
		ProModel proModel =new ProModel();
		proModel.setActYwId(actYwEtAssignRule.getActywId());
		proModel.setGnodeId(actYwEtAssignRule.getGnodeId());
		proModel.setIds(recordIds);
        List<ProModel> list = null;
        String tenantId = TenantConfig.getCacheTenant();
        if (tenantId.equals("20")){
            list = proModelDao.findProvListByIdsAssignOfId(proModel);
        }else{
//            list = proModelDao.findListByIdsAssign(proModel);
            proModel.setTenantId(tenantId);
            list = proModelDao.findListByIdsAssignOfSchool(proModel);
        }

		promodelIdList= Lists.transform(list, proIndex->proIndex.getId());
		return promodelIdList;
	}

    public List<String> getProvProList(ActYwEtAssignRule actYwEtAssignRule) {
        List<String> promodelIdList =new ArrayList<String>();
        ActYw actYw=actYwService.get(actYwEtAssignRule.getActywId());
        String key = ActYw.getPkey(actYw);
        Act act = new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> gnodeIdList = new ArrayList<String>();
        gnodeIdList.add(actYwEtAssignRule.getGnodeId());
        List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actYwEtAssignRule.getActywId());
        if(StringUtil.checkEmpty(recordIds)){
            return promodelIdList;
        }
        ProvinceProModel provinceProModel =new ProvinceProModel();
        provinceProModel.setActYwId(actYwEtAssignRule.getActywId());
        provinceProModel.setGnodeId(actYwEtAssignRule.getGnodeId());
        provinceProModel.setIds(recordIds);
        List<ProvinceProModel> list = provinceProModelService.findProvListByIdsAssign(provinceProModel);
        promodelIdList= Lists.transform(list, proIndex->proIndex.getId());
        return promodelIdList;
    }

}