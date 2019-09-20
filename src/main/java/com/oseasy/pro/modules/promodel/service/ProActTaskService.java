/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.oseasy.pro.modules.promodel.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.utils.ProcessDefCache;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGrole;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.exception.ActYwRuntimeException;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeTaskType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.vo.ProjectAuditTaskVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.tool.process.vo.FlowYwId;
import com.oseasy.pro.modules.promodel.utils.GT_Constant;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.utils.ProModelMdUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 流程定义相关Service
 */
@Service
@Transactional(readOnly = true)
public class ProActTaskService extends BaseService {
    protected static final Logger logger = Logger.getLogger(ProActTaskService.class);
	@Autowired
	private ActDao actDao;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
    @Autowired
    private ActTaskService actTaskService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private ActYwGnodeService actYwGnodeService;
	@Autowired
	private ActYwService actYwService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private UserService userService;
	@Autowired
	private TeamUserHistoryService teamUserHistoryService;
	@Autowired
	ActYwGassignService actYwGassignService;
	@Autowired
	private ProModelService proModelService;
    @Autowired
    private CoreService coreService;
    public ActYwGnode getNodeByProInsId(String proInsId) {
        return getNodeByProInsIdByGroupId(null, proInsId);
    }

    public ActYwGnode getNodeByProInsIdByGroupId(String groupId, String proInsId) {
        ProcessInstance pro = runtimeService.createProcessInstanceQuery()
                .processInstanceTenantId(TenantConfig.getCacheTenant())
                .processInstanceId(proInsId).singleResult();
        if (StringUtil.isEmpty(proInsId) && StringUtil.isNotEmpty(groupId)) {
            return actYwGnodeService.getStart(groupId);
        }

        //处理结束定位问题
        List<ActYwGnode> ends = null;
        if (pro == null) {
            if (((pro == null) || actTaskService.ifOver(proInsId)) && StringUtil.isNotEmpty(groupId)) {
                ProModel proModel = proModelService.getByProInsId(proInsId);
                if((proModel == null) || StringUtil.isEmpty(proModel.getEndGnodeId())){
                    ends = actYwGnodeService.getEndByRoot(groupId);
                    return (StringUtil.checkNotEmpty(ends)) ? ends.get(0) : null;
                }else{
                    List<ActYwGnode> endPres = actYwGnodeService.findListByPre(proModel.getEndGnodeId());
                    if(StringUtil.checkNotEmpty(endPres)){
                        ends = actYwGnodeService.getEndsByPpre(groupId, StringUtil.listIdToList(endPres));
                        return (StringUtil.checkNotEmpty(ends) && (ends.size() == 1)) ? ends.get(0) : null;
                    }
                    return null;
                }
            }else{
                return null;
            }
        }else if ((pro != null) && (FlowYwId.FY_P_MD.getGid()).equals(groupId)){
            ProModel proModel = proModelService.getByProInsId(proInsId);
            ProModelMd proModelMd = ProModelMdUtils.getProModelMdById(proModel.getId());
            if((ProModelMd.NO_PASS).equals(proModelMd.getSetState()) || (ProModelMd.NO_PASS).equals(proModelMd.getMidState()) || (ProModelMd.NO_PASS).equals(proModelMd.getCloseState())){
                ends = actYwGnodeService.getEndByRoot(groupId);
                return (StringUtil.checkNotEmpty(ends)) ? ends.get(0) : null;
            }
        }

        String procDefId = actTaskService.getProcessDefinitionIdByProInstId(proInsId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procDefId).singleResult();
        if (processDefinition == null) {
            return null;
        }
        ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
        String processDefinitionId = pdImpl.getId();// 流程标识
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
        //activitiList.get(0).getId();

        List<String> activeActivityIds = new ArrayList<String>();
                //runtimeService.getActiveActivityIds(proInsId);
        try {
            activeActivityIds = runtimeService.getActiveActivityIds(proInsId);
        } catch (ActivitiObjectNotFoundException e) {
            activeActivityIds = Lists.newArrayList();
        }
        String gnodeId = null;
        //循环所有节点
        for (String activeId : activeActivityIds) {
            for (ActivityImpl activityImpl : activitiList) {
                String id = activityImpl.getId();
                if (activityImpl.isScope()) {
                    if (activityImpl.getActivities().size() > 1) {
                        List<ActivityImpl> subAcList = activityImpl.getActivities();
                        for (ActivityImpl subActImpl : subAcList) {
                            String subid = subActImpl.getId();
                            if (activeId.equals(subid)) {// 获得执行到那个节点
                                gnodeId = subid;
                                break;
                            }
                        }
                    }
                }
                if (activeId.equals(id)) {
                    // 获得执行到那个节点
                    gnodeId = id;
                    break;
                }
            }
            if (StringUtil.isNotEmpty(gnodeId)) {
                break;
            }
        }
        ActYwGnode actYwGnode = new ActYwGnode();
        if (StringUtil.isNotEmpty(gnodeId)) {
            if (gnodeId.contains(ActYwTool.FLOW_ID_PREFIX)) {
                actYwGnode = actYwGnodeService.getByg(gnodeId.substring(ActYwTool.FLOW_ID_PREFIX.length()));
            } else if (gnodeId.contains(ActYwTool.FLOW_ID_START)) {
                actYwGnode = actYwGnodeService.getByg(gnodeId.substring(ActYwTool.FLOW_ID_START.length()));
            }
        }
        if ((actYwGnode != null) && (pro != null)) {
            actYwGnode.setSuspended(pro.isSuspended());
        }
        return actYwGnode;
    }

    /**
     * 列表刷新后刷新菜单待办任务数
     * @param actywId
     * @return
     */
    public String flashTodoCount(String actywId){
        ActYw actYw = actYwService.get(actywId);
        Map<String, Object> result = new HashMap<>();
        JSONObject response = new JSONObject();
        if (actYw != null) {
            TaskQuery taskQuery = //taskService.createTaskQuery().taskCandidateOrAssigned(userName)
                    actTaskService.getTaskQueryByAssigneeOrCandidateUser().includeProcessVariables().active();
            String key = ActYw.getPkey(actYw);
            taskQuery.processDefinitionKey(key);
            taskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actywId);
            List<Task> list = taskQuery.list();
            if (!list.isEmpty()) {
                List<String> proModelIds = list.stream().map(e -> (String) e.getProcessVariables().get("id")).collect(Collectors.toList());
                ProModel proModel = new ProModel();
                proModel.setIds(proModelIds);
                List<String> proModelIdIns = proModelService.findListByIdsWithoutJoin(proModel);
                List<Task> newList = new ArrayList<>();
                for(Task task : list){
                    boolean isIn=false;
                    for(String id:proModelIdIns){
                        if(task.getProcessVariables().get("id").equals(id)){
                            isIn=true;
                            break;
                        }
                    }
                    if(isIn){
                        newList.add(task);
                    }
                }
                for (Task task : newList) {
                    String gnodeId = task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, "");
                    String parentId = actYwGnodeService.get(gnodeId).getParentId();
                    if(parentId.equals("1")){
                        Object o = result.get(gnodeId);
                        if(o != null){
                            int v = (int) o;
                            result.put(gnodeId, ++v);
                        }else{
                            result.put(gnodeId, 1);
                        }
                    }else{
                        Object o = result.get(parentId);
                        if(o != null){
                            int v = (int) o;
                            result.put(parentId, ++v);
                        }else{
                            result.put(parentId, 1);
                        }
                    }
                }
            }
            //查询委派数据
            ActYwGassign actYwGassign=new ActYwGassign();
            actYwGassign.setYwId(actywId);
            actYwGassign.setRevUserId(UserUtils.getUserId());
            List<ActYwGassign> gassignList=actYwGassignService.getUserDelegateList(actYwGassign);
            for(ActYwGassign actYwGassignIndedx:gassignList){
                //判断是否为 待委派数据
                if((actYwGassignIndedx.getIsOver()!=null &&actYwGassignIndedx.getIsOver().equals('0'))||actYwGassignIndedx.getIsOver()==null){
                    String gnodeId=actYwGassignIndedx.getGnodeId();
                    String parentId = actYwGnodeService.get(gnodeId).getParentId();
                    if(parentId.equals("1")){
                        Object o = result.get(gnodeId);
                        if(o != null){
                            int v = (int) o;
                            result.put(gnodeId, ++v);
                        }else{
                            result.put(gnodeId, 1);
                        }
                    }else{
                        Object o = result.get(parentId);
                        if(o != null){
                            int v = (int) o;
                            result.put(parentId, ++v);
                        }else{
                            result.put(parentId, 1);
                        }
                    }
                }
            }
        }
        response.put("status", true);
        if(result.isEmpty()){
            response.put("msg", "所有待处理事件，都结束了");
            response.put("result", null);
        }else {
            response.put("msg", "查找到数据");
            response.put("result", result);
        }
        return response.toString();
    }

    public Page<Act> gtTodoList(Page<Act> page, Act act, String gnodeId) {
        String userName = UserUtils.getUser().getLoginName();
        String userId = UserUtils.getUser().getId();
        List<Act> result = new ArrayList<Act>();
        TaskQuery todoTaskQuery = actTaskService.getTaskQueryByAssignee()
                .active().includeProcessVariables().orderByTaskCreateTime().desc();
        if (StringUtil.isNotBlank(act.getProcDefKey())) {
            todoTaskQuery.processDefinitionKey(act.getProcDefKey());
        }
        if(StringUtils.isNotBlank(act.getTaskDefKey())){
            todoTaskQuery.taskDefinitionKeyLike("%" + act.getTaskDefKey() + "%");
        }
        // 查询列表
        List<Task> todoList = todoTaskQuery.list();
        for (Task task : todoList) {
            ProModel proModel = proModelService.get((String)task.getProcessVariables().get("id"));
            if (proModel == null || Const.YES.equals(proModel.getState())) {//审核未通过的不显示
                continue;
            }
            Act e = new Act();
            e.setTask(task);
            e.setVars(task.getProcessVariables());
            e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
            e.setStatus("todo");
            result.add(e);
        }
        if (GT_Constant.PGNODE1.equals(gnodeId)) {//立项列表显示待提交中期报告的记录
            TaskQuery query1 = taskService.createTaskQuery().active()
                    .includeProcessVariables().orderByTaskCreateTime().desc();
            if (StringUtil.isNotBlank(act.getProcDefKey())) {
                todoTaskQuery.processDefinitionKey(act.getProcDefKey());
            }
            query1.taskDefinitionKey(GT_Constant.S_MID_DEFKEY);
            List<Task> list1 = query1.list();
            for (Task task : list1) {
                Act e = new Act();
                e.setTask(task);
                e.setVars(task.getProcessVariables());
                e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
                result.add(e);
            }
        }else if(GT_Constant.PGNODE2.equals(gnodeId)){// 中期列表显示待提交结项报告的记录
            TaskQuery query2 = taskService.createTaskQuery().active()
                    .includeProcessVariables().orderByTaskCreateTime().desc();
            if (StringUtil.isNotBlank(act.getProcDefKey())) {
                todoTaskQuery.processDefinitionKey(act.getProcDefKey());
            }
            query2.taskDefinitionKey(GT_Constant.S_CLOSE_DEFKEY);
            List<Task> list2 = query2.list();
            for (Task task : list2) {
                Act e = new Act();
                e.setTask(task);
                e.setVars(task.getProcessVariables());
                e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
                result.add(e);
            }
        }
        //处理分页问题
        page.setCount(result.size());
        int pageStart = (page.getPageNo() - 1) * page.getPageSize();
        int pageEnd = result.size();
        if (result.size() > page.getPageNo() * page.getPageSize()) {
            pageEnd = page.getPageNo() * page.getPageSize();
        }
        List<Act> subList = result.subList(pageStart, pageEnd);
        page.setList(subList);
        return page;
    }


    //从回退节点向下一个节点gnodeId走一步
    public void assignRunNext(ProModel proModel,String procInsId,String toId,String gnodeId,List<String> userIdList){
        ActYwGnode actYwGnode =actYwGnodeService.getByg(gnodeId);

        //ActYwGnode backGnode =actYwGnodeService.getByg(toId);
        Map<String, Object> vars = new HashMap<String, Object>();
        vars=proModel.getVars();
        //重新添加审核人 将流程向下走一步
        Task inBackTask=taskService.createTaskQuery()
                .processInstanceId(procInsId).taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+toId).active().singleResult();
        if(StringUtil.isEmpty(inBackTask.getAssignee())){
            actTaskService.claim(inBackTask.getId(),"assignUser");
        }
        List<Role> roleList= actYwGnode.getRoles();
        //多角色配置人员
        String nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
        vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId + "s",userIdList);
        Boolean isGate = proModelService.getNextIsGate(toId);
        if(isGate){
            // 历史相关Service
            //Execution execution2 = processEngine.getRuntimeService().createExecutionQuery().processInstanceId(procInsId).activityId(ActYwTool.FLOW_ID_PREFIX+toId).singleResult();
            //String value = (String) processEngine.getRuntimeService().getVariable(execution2.getId(), "grade");
        //找审核历史中传递的参数
            List<HistoricActivityInstance> execution2 = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                    .processInstanceId(procInsId).activityId(ActYwTool.FLOW_ID_PREFIX+toId).list();
            HistoricVariableInstance hisValue= processEngine.getHistoryService()
                    .createHistoricVariableInstanceQuery()
                    .executionId(execution2.get(0).getExecutionId())
                    .variableName("grade").singleResult();
//          if(list != null && list.size()>0){
//              for(HistoricVariableInstance hiv : list){
//                  System.out.println(hiv.getTaskId()+"  "+hiv.getVariableName()+"     "+hiv.getValue()+"      "+hiv.getVariableTypeName());
//              }
//          }
            if(hisValue!=null){
                vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,hisValue.getValue());
            }else{
//              hisValue= processEngine.getHistoryService()
//                                  .createHistoricVariableInstanceQuery()
//                                  .processInstanceId(procInsId)
//                                  .variableName("grade").singleResult();
                //找审核历史中传递的参数
                List<HistoricVariableInstance> hisValues =processEngine.getHistoryService()
                                    .createHistoricVariableInstanceQuery()
                                    .processInstanceId(procInsId)
                                    .variableName("grade").list();
                if(StringUtil.checkNotEmpty(hisValues)){
                    if(hisValues.get(0)!=null){
                        vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,hisValues.get(0).getValue());
                    }
                }
            }

        }
        String taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
        actTaskService.rollBackFlow(taskId,ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId + "s",userIdList,ActYwTool.FLOW_ID_PREFIX+gnodeId);
        //taskService.complete(inBackTask.getId(), vars);
        historyService.deleteHistoricTaskInstance(inBackTask.getId());
    }

    //从回退节点向下一个节点gnodeId走一步
    public void assignProvRunNext(ProvinceProModel provinceProModel,String procInsId,String toId,String gnodeId,List<String> userIdList){
        ActYwGnode actYwGnode =actYwGnodeService.getByg(gnodeId);

        //ActYwGnode backGnode =actYwGnodeService.getByg(toId);
        Map<String, Object> vars = new HashMap<String, Object>();
        vars=provinceProModel.getVars();
        //重新添加审核人 将流程向下走一步
        Task inBackTask=taskService.createTaskQuery()
                .processInstanceId(procInsId).taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+toId).active().singleResult();
        if(StringUtil.isEmpty(inBackTask.getAssignee())){
            actTaskService.claim(inBackTask.getId(),"assignUser");
        }
        List<Role> roleList= actYwGnode.getRoles();
        //多角色配置人员
        String nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
        vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId + "s",userIdList);
        Boolean isGate = proModelService.getNextIsGate(toId);
        if(isGate){
        //找审核历史中传递的参数
            List<HistoricActivityInstance> execution2 = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
                    .processInstanceId(procInsId).activityId(ActYwTool.FLOW_ID_PREFIX+toId).list();
            HistoricVariableInstance hisValue= processEngine.getHistoryService()
                    .createHistoricVariableInstanceQuery()
                    .executionId(execution2.get(0).getExecutionId())
                    .variableName("grade").singleResult();

            if(hisValue!=null){
                vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,hisValue.getValue());
            }else{
                List<HistoricVariableInstance> hisValues =processEngine.getHistoryService()
                                    .createHistoricVariableInstanceQuery()
                                    .processInstanceId(procInsId)
                                    .variableName("grade").list();
                if(StringUtil.checkNotEmpty(hisValues)){
                    if(hisValues.get(0)!=null){
                        vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,hisValues.get(0).getValue());
                    }
                }
            }
        }
        String taskId = actTaskService.getTaskidByProcInsId(provinceProModel.getProcInsId());
        actTaskService.rollBackFlow(taskId,ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId + "s",userIdList,ActYwTool.FLOW_ID_PREFIX+gnodeId);
        historyService.deleteHistoricTaskInstance(inBackTask.getId());
    }

    //退回上一个节点 上一个节点为正常节点
    //gnodeId 进行中节点
    //toGnodeId 回退节点
    //并且走下一个节点
    @Transactional(readOnly = false)
    public boolean rollBackWorkFlow(ProModel proModel,String taskId,String gnodeId,String toGnodeId,List<String> userIdList) {
        //try {
            Map<String, Object> variables = new HashMap<String, Object>();
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId()).singleResult();
            String procInsId=instance.getId();

            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
            if (definition == null) {
                logger.info("流程定义未找到");
                logger.error("出错啦！流程定义未找到");
                return false;
            }
            // 取得当前任务活动
            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                    .findActivity(currTask.getTaskDefinitionKey());
            // 取得上一步活动
            ActivityImpl lastActivity =((ProcessDefinitionImpl) definition)
                                .findActivity(ActYwTool.FLOW_ID_PREFIX+toGnodeId);
            //也就是节点间的连线
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            //新建一个节点连线关系集合
            List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();
            // 建立新出口 回退到上一步
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(lastActivity);
            newTransitions.add(newTransition);
            //根据回退节点类型找到对应角色
            ActYwGnode BackactYwGnode =actYwGnodeService.getByg(toGnodeId);
            List<Role> roleList= BackactYwGnode.getRoles();
            //多角色配置人员
            String backGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
            List<String> roles = new ArrayList<String>();
            //虚拟一个接收人
            roles.add("assignUser");
            if (BackactYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(BackactYwGnode.getTaskType())) {
                variables.put(ActYwTool.FLOW_ROLE_ID_PREFIX + backGnodeRoleId, roles);
            } else {
                variables.put(ActYwTool.FLOW_ROLE_ID_PREFIX + backGnodeRoleId + "s", roles);
            }
            // 完成当前任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
                //清除历史记录
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
            //rollBackWork(proModel,taskId,gnodeId,toGnodeId,userIdList);
            //根据指派人将流程向下走一步
            assignRunNext(proModel,procInsId,toGnodeId,gnodeId,userIdList);
            logger.info("OK");
            logger.info("流程结束");
            return true;
    }

    @Transactional(readOnly = false)
    public boolean rollBackProvWorkFlow(ProvinceProModel provinceProModel,String taskId,String gnodeId,String toGnodeId,List<String> userIdList) {
        //try {
            Map<String, Object> variables = new HashMap<String, Object>();
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId()).singleResult();
            String procInsId=instance.getId();

            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
            if (definition == null) {
                logger.info("流程定义未找到");
                logger.error("出错啦！流程定义未找到");
                return false;
            }
            // 取得当前任务活动
            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                    .findActivity(currTask.getTaskDefinitionKey());
            // 取得上一步活动
            ActivityImpl lastActivity =((ProcessDefinitionImpl) definition)
                                .findActivity(ActYwTool.FLOW_ID_PREFIX+toGnodeId);
            //也就是节点间的连线
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            //新建一个节点连线关系集合
            List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();
            // 建立新出口 回退到上一步
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(lastActivity);
            newTransitions.add(newTransition);
            //根据回退节点类型找到对应角色
            ActYwGnode BackactYwGnode =actYwGnodeService.getByg(toGnodeId);
            List<Role> roleList= BackactYwGnode.getRoles();
            //多角色配置人员
            String backGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
            List<String> roles = new ArrayList<String>();
            //虚拟一个接收人
            roles.add("assignUser");
            if (BackactYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(BackactYwGnode.getTaskType())) {
                variables.put(ActYwTool.FLOW_ROLE_ID_PREFIX + backGnodeRoleId, roles);
            } else {
                variables.put(ActYwTool.FLOW_ROLE_ID_PREFIX + backGnodeRoleId + "s", roles);
            }
            // 完成当前任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
                //清除历史记录
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
            //根据指派人将流程向下走一步
            assignProvRunNext(provinceProModel,procInsId,toGnodeId,gnodeId,userIdList);
            logger.info("OK");
            logger.info("流程结束");
            return true;
    }


    public List<ProjectAuditTaskVo> getExpertAuditProAndYear(String userId) {
        List<ProjectAuditTaskVo> list=Lists.newArrayList();
        TaskQuery todoTaskQuery  = taskService.createTaskQuery().taskAssignee(userId).active();
        // 待审核列表
        List<Task> todoList = todoTaskQuery.list();
        for (Task task : todoList) {
            String id=task.getProcessInstanceId();
            if(id!=null){
                ProModel proModel=proModelService.getByProInsId(id);
                if(proModel!=null){
                    ActYw actYw= actYwService.get(proModel.getActYwId());
                    ProjectAuditTaskVo projectAuditTaskVo = new ProjectAuditTaskVo();
                    projectAuditTaskVo.setName(actYw.getProProject().getProjectName());
                    projectAuditTaskVo.setYear(actYw.getProProject().getYear());
                    list.add(projectAuditTaskVo);
                }
            }
        }
        list = StringUtil.removeDup(list);
        return list;
    }

    public List<String> getExpertAuditPro(String userId) {
        List<String> list=Lists.newArrayList();
        TaskQuery todoTaskQuery  = taskService.createTaskQuery().taskAssignee(userId).active();
        // 待审核列表
        List<Task> todoList = todoTaskQuery.list();
        for (Task task : todoList) {
            String id=task.getProcessInstanceId();
            if(id!=null){
                ProModel proModel=proModelService.getByProInsId(id);
                if(proModel!=null){
                    ActYw actYw= actYwService.get(proModel.getActYwId());
                    list.add(actYw.getProProject().getProjectName());
                }
            }
        }
        list=StringUtil.removeDup(list);
        return list;
    }

    /**
     * 走到流程下一步
     *
     * @param proModel
     */
    @Transactional(readOnly = false)
    public void runNextProcess(ProModel proModel) {
        ActYw actYw = actYwService.get(proModel.getActYwId());
        Map<String, Object> vars = new HashMap<String, Object>();
        JSONObject js=new JSONObject();
        js.put("ret",1);
        String key = ActYw.getPkey(actYw);
        String taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
        String taskDefinitionKeyaskDefKey = actTaskService.getTask(taskId).getTaskDefinitionKey();
        String nextGnodeRoleId = actTaskService.getProcessNextRoleName(taskDefinitionKeyaskDefKey, key);
        if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
            String nextRoleId = nextGnodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
            Role role = systemService.getNamebyId(nextRoleId);
            //启动节点
            String roleName = role.getName();
            List<String> roles = new ArrayList<String>();
            if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                roles = userService.findListByRoleIdAndOffice(role.getId(), proModel.getDeclareId());
            } else {
                roles= userService.findListByRoleId(role.getId());
            }
            //后台学生角色id
            if (nextRoleId.equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())) {
                roles.clear();
                roles.add(userService.findUserById(proModel.getDeclareId()).getName());
            }
            vars = proModel.getVars();
            //List<String> roles=userService.getCollegeExperts(proModel.getDeclareId());
            vars.put(nextGnodeRoleId + "s", roles);
        } else {
            //更改完成后团队历史表中的状态
            teamUserHistoryService.updateFinishAsClose(proModel.getId());
            //流程没有角色为没有后续流程 将流程表示为已经结束
            proModel.setState("1");
        }
        if (taskId != null) {
            taskService.complete(taskId, vars);
            ProcessInstance pro = runtimeService.createProcessInstanceQuery()
                    .processInstanceTenantId(TenantConfig.getCacheTenant())
                    .processInstanceId(proModel.getProcInsId()).singleResult();
        }
    }


    @Transactional(readOnly = false)
    //退回上一个节点 上一个节点为正常节点
    //gnodeId 进行中节点
    //toGnodeId 回退节点
    public boolean rollBackWork(String taskId,String gnodeId,String toGnodeId,List<String> userIdList) {
        Map<String, Object> variables = new HashMap<String, Object>();
        // 取得当前任务.当前任务节点
        HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        // 取得流程实例，流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceTenantId(TenantConfig.getCacheTenant())
                .processInstanceId(currTask.getProcessInstanceId()).singleResult();
        String procInsId=instance.getId();

        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(currTask.getProcessDefinitionId());
        if (definition == null) {
            logger.info("流程定义未找到");
            logger.error("出错啦！流程定义未找到");
            return false;
        }
        // 取得当前任务活动
        ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                .findActivity(currTask.getTaskDefinitionKey());
        // 取得上一步活动
        ActivityImpl lastActivity =((ProcessDefinitionImpl) definition)
                            .findActivity(ActYwTool.FLOW_ID_PREFIX+toGnodeId);
        //也就是节点间的连线
        List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
        // 清除当前活动的出口
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        //新建一个节点连线关系集合
        List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();
        // 建立新出口 回退到上一步
        List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            TransitionImpl newTransition = currActivity.createOutgoingTransition();
            newTransition.setDestination(lastActivity);
            newTransitions.add(newTransition);
        //根据回退节点类型找到对应角色
        ActYwGnode BackactYwGnode =actYwGnodeService.getByg(toGnodeId);
        String backGnodeRoleId=BackactYwGnode.getRoles().get(0).getId();
        List<String> roles = new ArrayList<String>();
        //虚拟一个接收人
        roles.add("assignUser");
        if (BackactYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(BackactYwGnode.getTaskType())) {
            variables.put(ActYwTool.FLOW_ROLE_ID_PREFIX + backGnodeRoleId, roles);
        } else {
            variables.put(ActYwTool.FLOW_ROLE_ID_PREFIX + backGnodeRoleId + "s", roles);
        }
        // 完成当前任务
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(instance.getId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
        for (Task task : tasks) {
            taskService.complete(task.getId(), variables);
            //清除历史记录
            historyService.deleteHistoricTaskInstance(task.getId());
        }
        // 恢复方向
        for (TransitionImpl transitionImpl : newTransitions) {
            currActivity.getOutgoingTransitions().remove(transitionImpl);
        }
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
//      //根据指派人将流程向下走一步
//      assignRunNext(proModel,procInsId,toGnodeId,gnodeId,userIdList);
        logger.info("OK");
        logger.info("流程结束");
        return true;
    }

    @Transactional(readOnly = false)
    //退回上一个节点 上一个节点为起始节点
    public boolean rollBackWorkFlowStart(ProModel proModel,String taskId,List<String> userIdList) {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            //  删除任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(proModel.getProcInsId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                TaskEntity currentTaskEntity = (TaskEntity) actTaskService.getTask(task.getId());
                currentTaskEntity.setExecutionId(null);
                taskService.saveTask(currentTaskEntity);
                taskService.deleteTask(task.getId(), true);
                //清除历史记录
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            //重新启动 重新走流程
            actAssignStart(proModel,userIdList);
            logger.info("OK");
            logger.info("流程结束");

            return true;
        } catch (Exception e) {
            logger.error("失败");
            logger.error(e.getMessage());
            return false;
        }
    }

    @Transactional(readOnly = false)
    //退回上一个节点 上一个节点为起始节点
    public boolean rollBackProvWorkFlowStart(ProvinceProModel provinceProModel,String taskId,List<String> userIdList) {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            //  删除任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(provinceProModel.getProcInsId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                TaskEntity currentTaskEntity = (TaskEntity) actTaskService.getTask(task.getId());
                currentTaskEntity.setExecutionId(null);
                taskService.saveTask(currentTaskEntity);
                taskService.deleteTask(task.getId(), true);
                //清除历史记录
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            //重新启动 重新走流程
            actProvAssignStart(provinceProModel,userIdList);
            logger.info("OK");
            logger.info("流程结束");

            return true;
        } catch (Exception e) {
            logger.error("失败");
            logger.error(e.getMessage());
            return false;
        }
    }

    @Transactional(readOnly = false)
    //退回上一个节点 上一个节点为起始节点
    public boolean rollBackWorkStart(ProModel proModel,String taskId) {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            //  删除任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(proModel.getProcInsId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                TaskEntity currentTaskEntity = (TaskEntity) actTaskService.getTask(task.getId());
                currentTaskEntity.setExecutionId(null);
                taskService.saveTask(currentTaskEntity);
                taskService.deleteTask(task.getId(), true);
                //清除历史记录
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            //重新启动 重新走流程
            actToAssignStart(proModel);
            logger.info("OK");
            logger.info("流程结束");

            return true;
        } catch (Exception e) {
            logger.error("失败");
            logger.error(e.getMessage());
            return false;
        }
    }


    @Transactional(readOnly = false)
    //退回上一个节点 上一个节点为起始节点
    public boolean rollBackWorkProvStart(ProvinceProModel provinceProModel, String taskId) {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            //  删除任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(provinceProModel.getProcInsId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                TaskEntity currentTaskEntity = (TaskEntity) actTaskService.getTask(task.getId());
                currentTaskEntity.setExecutionId(null);
                taskService.saveTask(currentTaskEntity);
                taskService.deleteTask(task.getId(), true);
                //清除历史记录
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            //重新启动 重新走流程
            actProvToAssignStart(provinceProModel);
            logger.info("OK");
            logger.info("流程结束");

            return true;
        } catch (Exception e) {
            logger.error("失败");
            logger.error(e.getMessage());
            return false;
        }
    }

    //指派启动节点
    @Transactional(readOnly = false)
    public void actAssignStart(ProModel proModel,List<String> userIds) {
        ActYw actYw=actYwService.get(proModel.getActYwId());
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
        String nodeRoleId = actYwNextGnode.getGroles().get(0).getRole().getId();
        //审核人
//      List<String> roles= Arrays.asList(userIds.split(","));
        Map<String, Object> vars = new HashMap<String, Object>();
        vars = proModel.getVars();
        if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, userIds);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + "s", userIds);
        }
        String key = ActYw.getPkey(actYw);
        String userId = UserUtils.getUser().getId();
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key, "pro_model:" + proModel.getId(), vars,TenantConfig.getCacheTenant());
        Act act = new Act();
        act.setBusinessTable("pro_model");// 业务表名
        act.setBusinessId(proModel.getId()); // 业务表ID
        act.setProcInsId(procIns.getId());
        actDao.updateProcInsIdByBusinessId(act);
        proModel.setProcInsId(act.getProcInsId());
    }

    //指派启动节点
    @Transactional(readOnly = false)
    public void actProvAssignStart(ProvinceProModel provinceProModel,List<String> userIds) {
        ActYw actYw=actYwService.get(provinceProModel.getActYwId());
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
        String nodeRoleId = actYwNextGnode.getGroles().get(0).getRole().getId();
        //审核人
//      List<String> roles= Arrays.asList(userIds.split(","));
        Map<String, Object> vars = new HashMap<String, Object>();
        vars = provinceProModel.getVars();
        if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, userIds);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + "s", userIds);
        }
        String key = ActYw.getPkey(actYw);
        String userId = UserUtils.getUser().getId();
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key, "pro_model:" + provinceProModel.getId(), vars,TenantConfig.getCacheTenant());
        Act act = new Act();
        act.setBusinessTable("pro_model");// 业务表名
        act.setBusinessId(provinceProModel.getId()); // 业务表ID
        act.setProcInsId(procIns.getId());
        actDao.updateProcInsIdByBusinessId(act);
        provinceProModel.setProcInsId(act.getProcInsId());
    }



    //指派启动节点 回到待指派状态
    @Transactional(readOnly = false)
    public void actToAssignStart(ProModel proModel) {
        ActYw actYw=actYwService.get(proModel.getActYwId());
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
        String nodeRoleId = actYwNextGnode.getGroles().get(0).getRole().getId();
        //审核人
        List<String> userList= new ArrayList<>();
        userList.add("assignUser");
        Map<String, Object> vars = new HashMap<String, Object>();

        vars = proModel.getVars();
        if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, userList);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + "s", userList);
        }
        String key = ActYw.getPkey(actYw);
        String userId = UserUtils.getUser().getId();
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key, "pro_model:" + proModel.getId(), vars,TenantConfig.getCacheTenant());
        Act act = new Act();
        act.setBusinessTable("pro_model");// 业务表名
        act.setBusinessId(proModel.getId()); // 业务表ID
        act.setProcInsId(procIns.getId());
        actDao.updateProcInsIdByBusinessId(act);
        proModel.setProcInsId(act.getProcInsId());
    }

    //指派启动节点 回到待指派状态
    @Transactional(readOnly = false)
    public void actProvToAssignStart(ProvinceProModel provinceProModel) {
        ActYw actYw=actYwService.get(provinceProModel.getActYwId());
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
        String nodeRoleId = actYwNextGnode.getGroles().get(0).getRole().getId();
        //审核人
        List<String> userList= new ArrayList<>();
        userList.add("assignUser");
        Map<String, Object> vars = new HashMap<String, Object>();

        vars = provinceProModel.getVars();
        if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, userList);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + "s", userList);
        }
        String key = ActYw.getPkey(actYw);
        String userId = UserUtils.getUser().getId();
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key, "pro_model:" + provinceProModel.getId(), vars,TenantConfig.getCacheTenant());
        Act act = new Act();
        act.setBusinessTable("pro_model");// 业务表名
        act.setBusinessId(provinceProModel.getId()); // 业务表ID
        act.setProcInsId(procIns.getId());
        actDao.updateProcInsIdByBusinessId(act);
        provinceProModel.setProcInsId(act.getProcInsId());
    }

    //得到流程中 当前角色的所有审核list
    public List<ActYwGnode> getSubGnodeList(String gnodeId, String groupId) {
        List<ActYwGnode> actYwGnodes =new ArrayList<ActYwGnode> ();
        ActYwGnode actYwGnode=actYwGnodeService.getByg(gnodeId);
        if((actYwGnode != null) && GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())){
            actYwGnodes.add(actYwGnode);
        }else {
            //查询当前节点下面任务节点id
            List<ActYwGnode> actYwGnodeLists = actYwGnodeService.findListBygYwGprocess(groupId,gnodeId);
            for(ActYwGnode actYwGnodeIndex:actYwGnodeLists){
                Boolean isFirst =false;
                if(actYwGnodeIndex.getGroles()!=null){
                    List<ActYwGrole> roles=actYwGnodeIndex.getGroles();
                    for(ActYwGrole atYwGrole:roles){
                        if((atYwGrole.getRole() == null)){
                            continue;
                        }
                        if(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId().equals(atYwGrole.getRole().getId())){
                            isFirst=true;
                            break;
                        }
                    }
                }
                if(!isFirst){
                    actYwGnodes.add(actYwGnodeIndex);
                }

            }
        }
        return actYwGnodes;
    }
}