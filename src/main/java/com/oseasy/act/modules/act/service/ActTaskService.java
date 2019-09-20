/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.oseasy.act.modules.act.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.oseasy.act.modules.act.utils.ThreadUtils;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.HistoricTaskInstanceQueryImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.cmd.CreateAndTakeTransitionCmd;
import com.oseasy.act.modules.act.service.cmd.JumpTaskCmd;
import com.oseasy.act.modules.act.service.creator.ChainedActivitiesCreator;
import com.oseasy.act.modules.act.service.creator.MultiInstanceActivityCreator;
import com.oseasy.act.modules.act.service.creator.RuntimeActivityDefinitionEntityIntepreter;
import com.oseasy.act.modules.act.service.creator.SimpleRuntimeActivityDefinitionEntity;
import com.oseasy.act.modules.act.utils.ActUtils;
import com.oseasy.act.modules.act.utils.ProcessDefCache;
import com.oseasy.act.modules.act.utils.ProcessDefUtils;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.act.modules.actyw.entity.ActYwForm;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwNode;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.FormClientType;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程定义相关Service
 */
@Service
@Transactional(readOnly = true)
public class ActTaskService extends BaseService {
    protected static final Logger logger = Logger.getLogger(ActTaskService.class);
	@Autowired
	private ActDao actDao;
	@Autowired
	private ProcessEngineFactoryBean processEngineFactory;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
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
	ActYwGassignService actYwGassignService;

	/**
	 * 根据节点获得id列表
	 *
	 * @return
	 */
	public List<String> gnodeIdsList(String key, String keyName) {
		List<String> result = new ArrayList<String>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		//对应模块
		todoTaskQuery.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+keyName);
		if (StringUtil.isNotBlank(key)) {
			todoTaskQuery.processDefinitionKey(key);
		}
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setVars(task.getProcessVariables());
			result.add((String) e.getVars().getMap().get("id"));
		}
		// =============== 已经审核的任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().finished()
				.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		histTaskQuery.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+keyName);
		if (StringUtil.isNotBlank(key)) {
			histTaskQuery.processDefinitionKey(key);
		}
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setVars(histTask.getProcessVariables());
			result.add((String) e.getVars().getMap().get("id"));
		}
		return result;
	}

	/**
	 * 根据end节点获得id列表
	 *
	 * @return
	 */
	public List<String> gnodeEndIdsList(String key, String keyName) {
		List<String> result = new ArrayList<String>();
		// =============== 已经审核的任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().finished()
				.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		histTaskQuery.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+keyName);
		if (StringUtil.isNotBlank(key)) {
			histTaskQuery.processDefinitionKey(key);
		}
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setVars(histTask.getProcessVariables());
			result.add((String) e.getVars().getMap().get("id"));
		}
		return result;
	}


	/**
	 * 2017-2-27  addBy zhangzheng
	 * 获取待审核任务 带分页的
	 *
	 * @param page
	 * @param act  查询条件封装
	 * @return
	 */
	public Page<Act> todoListForPage(Page<Act> page, Act act) {
		String userId = UserUtils.getUser().getId();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
				   getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap() != null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				todoTaskQuery.processVariableValueLike(entry.getKey(), "%" + entry.getValue() + "%");
			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			todoTaskQuery.taskDefinitionKeyLike("%" + act.getTaskDefKey() + "%");
		}
		if (act.getBeginDate() != null) {
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null) {
			Date endDate = act.getEndDate();
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(endDate);
			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日期加1天
			todoTaskQuery.taskCreatedBefore(rightNow.getTime());
		}
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery().taskCandidateUser(userId)
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap() != null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				toClaimQuery.processVariableValueLike(entry.getKey(), "%" + entry.getValue() + "%");
			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			toClaimQuery.taskDefinitionKeyLike("%" + act.getTaskDefKey() + "%");
		}
		if (act.getBeginDate() != null) {
			toClaimQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null) {
			Date endDate = act.getEndDate();
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(endDate);
			rightNow.add(Calendar.DAY_OF_YEAR, 1);//日期加1天
			toClaimQuery.taskCreatedBefore(rightNow.getTime());
		}
		// 查询总数
		page.setCount(todoTaskQuery.count() + toClaimQuery.count());
		List<Act> actList = Lists.newArrayList();
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("todo");
			actList.add(e);
		}
		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("claim");
			actList.add(e);
		}
		Collections.sort(actList, new Comparator<Act>() {
			public int compare(Act act1, Act act2) {
				return act2.getTask().getCreateTime().compareTo(act1.getTask().getCreateTime());
			}
		});
		int pageStart = (page.getPageNo() - 1) * page.getPageSize();
		int pageEnd = actList.size();
		if (actList.size() > page.getPageNo() * page.getPageSize()) {
			pageEnd = page.getPageNo() * page.getPageSize();
		}
		List<Act> subList = actList.subList(pageStart, pageEnd);
		page.setList(subList);
		return page;
	}


	/**
	 * 获取已审核任务
	 *
	 * @param page
	 * @param act  查询条件封装
	 * @return
	 */
	public Page<Act> historicList(Page<Act> page, Act act) {
		//String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
		HistoricTaskInstanceQuery histTaskQuery = //historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
			getHistoricTaskInstanceQueryByAssignee()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap() != null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				histTaskQuery.processVariableValueLike(entry.getKey(), "%" + entry.getValue() + "%");
			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			histTaskQuery.taskDefinitionKeyLike("%" + act.getTaskDefKey() + "%");
		}
		if (act.getBeginDate() != null) {
			histTaskQuery.taskCompletedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null) {
			histTaskQuery.taskCompletedBefore(act.getEndDate());
		}
		// 查询总数
		page.setCount(histTaskQuery.count());
		// 查询列表
		List<HistoricTaskInstance> histList = histTaskQuery.listPage(page.getFirstResult(), page.getMaxResults());
		//处理分页问题
		List<Act> actList = Lists.newArrayList();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setHistTask(histTask);
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			e.setStatus("finish");
			actList.add(e);

		}
		page.setList(actList);
		return page;
	}


	/**
	 * @param processDefinitionKey 流程编号
	 * @param taskDefinitionKey    当前用户任务编号
	 * @param proInsId             流程实例id,对应业务表的pro_ins_id字段
	 * @return
	 * @author zhangzheng 多任务实例查询当前任务环节没完成的数量
	 */
	public Integer taskCount(String processDefinitionKey, String taskDefinitionKey, String proInsId) {
		int count = 0;
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().active()
				.includeProcessVariables().orderByTaskCreateTime().desc();
		todoTaskQuery.processDefinitionKey(processDefinitionKey); //流程名称
		todoTaskQuery.taskDefinitionKey(taskDefinitionKey);
		todoTaskQuery.processInstanceId(proInsId);
		count += todoTaskQuery.count();
		return count;
	}

	/**
	 * @param processDefinitionKey 流程编号
	 * @param taskDefinitionKey    当前用户任务编号
	 * @param proInsId             流程实例id,对应业务表的pro_ins_id字段
	 * @return
	 * @author zhangzheng 多任务实例查询当前任务环节是否完成
	 */
	public boolean isMultiFinished(String processDefinitionKey, String taskDefinitionKey, String proInsId) {
		int count = taskCount(processDefinitionKey, taskDefinitionKey, proInsId);
		if (count == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param processDefinitionKey 流程编号
	 * @param taskDefinitionKey    当前用户任务编号
	 * @param proInsId             流程实例id,对应业务表的pro_ins_id字段
	 * @return
	 * @author zhangzheng 多任务实例查询当前任务环节是否是最后一个
	 */
	public boolean isMultiLast(String processDefinitionKey, String taskDefinitionKey, String proInsId) {
		int count = taskCount(processDefinitionKey, taskDefinitionKey, proInsId);
		if (count == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param processDefinitionKey 流程编号
	 * @return
	 * @author 签收和普通节点
	 */
	public ActYwGnode getStartNextGnode(String processDefinitionKey) {
		ActYwGnode actYwGnode = null;
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active();
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);
		List<ProcessDefinition> listProcess = processDefinitionQuery.list();
		String processDefinitionId = null;
		for (ProcessDefinition processDefinition : listProcess) {
			if (processDefinitionKey.equals(processDefinition.getKey())) {
				processDefinitionId = processDefinition.getId();
			}
		}
		if(processDefinitionId == null){
		    return null;
		}

		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		List<Process> processes = bpmnModel.getProcesses();
		Process process = processes.get(0);
		//获取所有的FlowElement信息
		Collection<FlowElement> flowElements = process.getFlowElements();
		for (FlowElement flowElement : flowElements) {
			if (flowElement instanceof SubProcess) {
				SubProcess subProcess = (SubProcess) flowElement;
				Collection<FlowElement> incomingFlows = subProcess.getFlowElements();
				for (FlowElement flowElementsub : incomingFlows) {
					if (flowElementsub instanceof UserTask) {
						UserTask userTask = (UserTask) flowElementsub;
						String gnodeId=userTask.getId().substring(ActYwTool.FLOW_ID_PREFIX.length());
						actYwGnode =actYwGnodeService.getByg(gnodeId);
						break;
					}
				}
				if (actYwGnode!=null) {
					break;
				}
			}else{
				//找到类型userTask节点
				if (flowElement instanceof UserTask) {
					UserTask userTask = (UserTask) flowElement;
					String gnodeId=userTask.getId().substring(ActYwTool.FLOW_ID_PREFIX.length());
					actYwGnode =actYwGnodeService.getByg(gnodeId);
					break;
				}
			}
		}
		return actYwGnode;
	}


	/**
	 * @param processDefinitionKey 流程编号
	 * @return
	 * @author zhangzheng 并行多任务实例获取开始后的一个节点的角色名
	 */
	public String getStartNextRoleName(String processDefinitionKey) {
		String roleName = "";
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active();
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);
		List<ProcessDefinition> listProcess = processDefinitionQuery.list();
		String processDefinitionId = "";
		for (ProcessDefinition processDefinition : listProcess) {
			if (processDefinitionKey.equals(processDefinition.getKey())) {
				processDefinitionId = processDefinition.getId();
			}
		}
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		List<Process> processes = bpmnModel.getProcesses();
		Process process = processes.get(0);
		//获取所有的FlowElement信息
		Collection<FlowElement> flowElements = process.getFlowElements();
		for (FlowElement flowElement : flowElements) {
			//如果是任务节点
			if (flowElement instanceof UserTask) {
				UserTask userTask = (UserTask) flowElement;
				//获取入线信息
				List<SequenceFlow> incomingFlows = userTask.getIncomingFlows();
				SequenceFlow incomingFlow = incomingFlows.get(0);
				if (incomingFlow.getSourceRef().contains("start")) {
					roleName = userTask.getAssignee();
				}
			}
		}
		String[] roleNames = roleName.split("\\}");
		String roleName0 = roleNames[0];
		String[] realName = roleName0.split("\\{");
		return realName[1];
	}

	/**
	 * @param processDefinitionKey 流程编号
	 * @return
	 * @author zhangzheng 流程含子流程，并行多任务实例获取开始后的一个节点的角色名
	 */
	public String getProcessStartRoleName(String processDefinitionKey) {
		String roleName = "";
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active();
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);
		List<ProcessDefinition> listProcess = processDefinitionQuery.list();
		String processDefinitionId = "";
		for (ProcessDefinition processDefinition : listProcess) {
			if (processDefinitionKey.equals(processDefinition.getKey())) {
				processDefinitionId = processDefinition.getId();
			}
		}
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		List<Process> processes = bpmnModel.getProcesses();
		Process process = processes.get(0);
		//获取所有的FlowElement信息
		Collection<FlowElement> flowElements = process.getFlowElements();
		for (FlowElement flowElement : flowElements) {
			//查询第一个子流程
			if (flowElement instanceof SubProcess) {
				SubProcess subProcess = (SubProcess) flowElement;
				Collection<FlowElement> incomingFlows = subProcess.getFlowElements();
				for (FlowElement flowElementsub : incomingFlows) {
					if (flowElementsub instanceof UserTask) {
						UserTask userTask = (UserTask) flowElementsub;
						roleName = userTask.getAssignee();
						if(StringUtil.isEmpty(roleName)){
							roleName=userTask.getCandidateUsers().get(0);
						}
						break;
					}
				}
				if (roleName.length() > 0) {
					break;
				}
			}else{
				//找到类型userTask节点
				if (flowElement instanceof UserTask) {
					UserTask userTask = (UserTask) flowElement;
					roleName = userTask.getAssignee();
					break;
				}
			}
		}
		String[] roleNames = roleName.split("\\}");
		String roleName0 = roleNames[0];
		String[] realName = roleName0.split("\\{");
		return realName[1];
	}

	/**
	 * @param taskDefKey           当前节点编号
	 * @param processDefinitionKey 流程编号
	 * @return
	 * @author zhangzheng 含有子流程并行多任务实例获取下一个节点的角色名
	 */
	public String getProcessNextRoleName(String taskDefKey, String processDefinitionKey) {
		String roleName = ""; //${teacher}
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active();
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);
		List<ProcessDefinition> listProcess = processDefinitionQuery.list();
		String processDefinitionId = listProcess.get(0).getId();
		List<UserTask> userTaskList = getSubProcess(processDefinitionId);
		for (int i = 0; i < userTaskList.size(); i++) {
			if (userTaskList.get(i).getId().equals(taskDefKey)) {
				//如果下一个节点时网关 需要做其他判断 //
				if ((i + 1) == userTaskList.size()) {
					break;
				}
				roleName = userTaskList.get(i + 1).getAssignee();
				break;
			}
		}
		if (StringUtil.isNotBlank(roleName)) {
			String[] roleNames = roleName.split("\\}");
			String roleName0 = roleNames[0];
			String[] realName = roleName0.split("\\{");
			return realName[1];
		} else {
			return "";
		}
	}


	/**
		 * @param taskDefKey           当前节点编号
		 * @param processDefinitionKey 流程编号
		 * @return
		 */
	public String getGateProcessNextRoleName(String taskDefKey, String processDefinitionKey) {
		String roleName = ""; //${teacher}
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active();
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);
		List<ProcessDefinition> listProcess = processDefinitionQuery.list();
		String processDefinitionId = listProcess.get(0).getId();
		//得到当前审核节点gnodeId
		String gnodeId=taskDefKey.substring(ActYwTool.FLOW_ID_PREFIX.length());
		if (StringUtil.isNotBlank(roleName)) {
			String[] roleNames = roleName.split("\\}");
			String roleName0 = roleNames[0];
			String[] realName = roleName0.split("\\{");
			return realName[1];
		} else {
			return "";
		}
	}


	/**
	 * @param taskDefKey           当前节点编号
	 * @param processDefinitionKey 流程编号
	 * @return
	 * @author zhangzheng 含有子流程并行多任务实例获取下一个节点的角色名
	 */
	public String getProcessPreNode(String taskDefKey, String processDefinitionKey) {
		String PreNode = ""; //${teacher}
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active();
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);
		List<ProcessDefinition> listProcess = processDefinitionQuery.list();
		String processDefinitionId = listProcess.get(0).getId();
		List<UserTask> userTaskList = getSubProcess(processDefinitionId);
		for (int i = 0; i < userTaskList.size(); i++) {
			if (userTaskList.get(i).getId().equals(taskDefKey)) {
				if (i == 0) {
					break;
				}
				PreNode = userTaskList.get(i - 1).getId();
				break;
			}
		}
		return PreNode;
	}


	public List<UserTask> getSubProcess(String processDefinitionId) {
		List<UserTask> userTaskList = new ArrayList<UserTask>();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		List<Process> processes = bpmnModel.getProcesses();
		Process process = processes.get(0);
		//获取所有的FlowElement信息
		Collection<FlowElement> flowElements = process.getFlowElements();
		//获取所有任务节点
		for (FlowElement flowElement : flowElements) {
			if (flowElement instanceof SubProcess) {
				SubProcess subProcess = (SubProcess) flowElement;
				Collection<FlowElement> incomingFlows = subProcess.getFlowElements();
				for (FlowElement flowElementsub : incomingFlows) {
					if (flowElementsub instanceof UserTask) {
						UserTask userTask = (UserTask) flowElementsub;
						userTaskList.add(userTask);
					}
				}
			}else{
				if (flowElement instanceof UserTask) {
					UserTask userTask = (UserTask) flowElement;
					userTaskList.add(userTask);
				}
			}
		}
		return userTaskList;
	}

	/**
	 * @param taskDefKey           当前节点编号
	 * @param processDefinitionKey 流程编号
	 * @return
	 * @author zhangzheng 并行多任务实例获取下一个节点的角色名
	 */
	public String getNextRoleName(String taskDefKey, String processDefinitionKey) {
		String assignee = ""; //${teacher}
		String nextTaskDefKey = "";
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active();
		processDefinitionQuery.processDefinitionKey(processDefinitionKey);
		List<ProcessDefinition> listProcess = processDefinitionQuery.list();
		String processDefinitionId = listProcess.get(0).getId();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		List<Process> processes = bpmnModel.getProcesses();
		Process process = processes.get(0);
		//获取所有的FlowElement信息
		Collection<FlowElement> flowElements = process.getFlowElements();
		for (FlowElement flowElement : flowElements) {
			//如果是任务节点
			if (flowElement instanceof UserTask) {
				UserTask userTask = (UserTask) flowElement;
				if (taskDefKey.equals(userTask.getId())) { //如果是当前节点，获取下个节点的名称
					List<SequenceFlow> outGoingFlows = userTask.getOutgoingFlows();
					SequenceFlow outGoingFlow = outGoingFlows.get(0);
					if (outGoingFlow.getTargetRef().contains("audit")) {
						nextTaskDefKey = outGoingFlow.getTargetRef();
						break;
					}
				}
			}
		}
		if (StringUtil.isNotBlank(nextTaskDefKey)) {
			for (FlowElement flowElement : flowElements) {
				if (flowElement instanceof UserTask) {
					UserTask userTask = (UserTask) flowElement;
					if (nextTaskDefKey.equals(userTask.getId())) {
						assignee = userTask.getAssignee();
					}
				}
			}

		}
		if (StringUtil.isNotBlank(assignee)) {
			String[] roleNames = assignee.split("\\}");
			String roleName0 = roleNames[0];
			String[] realName = roleName0.split("\\{");
			return realName[1];
		} else {
			return "";
		}
	}


	/**
	 * @param proInstId 流程实例Id
	 * @return
	 * @author zhangzheng 根据流程实例id得到流程定义id
	 */
	public String getProcessDefinitionIdByProInstId(String proInstId) {
		List<HistoricTaskInstance> hisList = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(proInstId).list();
		if (hisList.size() > 0) {
			HistoricTaskInstance hisTaskIns = hisList.get(0);
			return hisTaskIns.getProcessDefinitionId();
		} else {
			return "";
		}
	}

	/**
	 * 根据大节点获得当前角色审核数据列表
	 * @return
	 */
	public List<String> modelMdtodoList(Act act, String keyName) {
		List<String> result = new ArrayList<String>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
				getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		//对应模块
		todoTaskQuery.taskDefinitionKey(keyName);
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setVars(task.getProcessVariables());
			result.add((String) e.getVars().getMap().get("id"));
		}
		// =============== 已经审核的任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = //historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
				getHistoricTaskInstanceQueryByAssignee()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		histTaskQuery.taskDefinitionKey(keyName);
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setVars(histTask.getProcessVariables());
			result.add((String) e.getVars().getMap().get("id"));
		}
		return result;
	}

	/**
	 * 获取待审核列表
	 *
	 * @return
	 */
	public Page<Act> modeltodoGnodeIdList(Page<Act> page, Act act, List<String> gnodeIds) {
		List<Act> result = new ArrayList<Act>();
		// =============== 已经签收的任务  ===============
		long count = 0;
		for (String gnodeId : gnodeIds) {
			String keyName = ActYwTool.FLOW_ID_PREFIX + gnodeId;
			TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
			getTaskQueryByAssignee()
					.active().includeProcessVariables().orderByTaskCreateTime().desc();
			//对应模块
			todoTaskQuery.taskDefinitionKeyLike("%" + keyName + "%");
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				todoTaskQuery.processDefinitionKey(act.getProcDefKey());
			}
			// 查询列表
			List<Task> todoList = todoTaskQuery.list();
			for (Task task : todoList) {
				Act e = new Act();
				e.setTask(task);
				e.setVars(task.getProcessVariables());
				e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
				e.setStatus("todo");
				e.setGnodeId(gnodeId);
				result.add(e);
			}
			// =============== 等待签收的任务  ===============
			TaskQuery toClaimQuery = //taskService.createTaskQuery().taskCandidateUser(userId)
					       getTaskQueryByCandidateUser()
					.includeProcessVariables().active().orderByTaskCreateTime().desc();
			//对应模块
			toClaimQuery.taskDefinitionKeyLike("%" + keyName + "%");
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				toClaimQuery.processDefinitionKey(act.getProcDefKey());
			}
			// 查询列表
			List<Task> toClaimList = toClaimQuery.list();
			for (Task task : toClaimList) {
				Act e = new Act();
				e.setTask(task);
				e.setVars(task.getProcessVariables());
				e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
				e.setStatus("claim");
				e.setGnodeId(gnodeId);
				result.add(e);
			}
			// =============== 已经审核的任务  ===============
			HistoricTaskInstanceQuery histTaskQuery =// historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
			getHistoricTaskInstanceQueryByAssignee()
					.finished()
					.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
			// 设置查询条件
			histTaskQuery.taskDefinitionKeyLike("%" + keyName + "%");
			List<HistoricTaskInstance> histList = histTaskQuery.list();
			for (HistoricTaskInstance histTask : histList) {
				Act e = new Act();
				e.setHistTask(histTask);
				e.setTaskName(histTask.getName());
				e.setVars(histTask.getProcessVariables());
				e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
				e.setStatus("finish");
				e.setGnodeId(gnodeId);
				result.add(e);
			}
			count = count + todoTaskQuery.count() + histTaskQuery.count();
		}
		//处理分页问题
		// 查询总数
		page.setCount(count);
		int pageStart = (page.getPageNo() - 1) * page.getPageSize();
		int pageEnd = result.size();
		if (result.size() > page.getPageNo() * page.getPageSize()) {
			pageEnd = page.getPageNo() * page.getPageSize();
		}
		List<Act> subList = result.subList(pageStart, pageEnd);
		page.setList(subList);
		return page;
	}
	public List<String> recordIdsTodo(String actYwId) {
		List<String> recordIds = new ArrayList<>();
		ActYw actYw=actYwService.get(actYwId);
		if(actYw==null){
			return null;
		}
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee("assignUser")
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			todoTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}
		List<Task> todoList = todoTaskQuery.list();
		recordIds.addAll(todoList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));

		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery =taskService.createTaskQuery().taskCandidateUser("assignUser").includeProcessVariables().active().orderByTaskCreateTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			toClaimQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}
		List<Task> toClaimList = toClaimQuery.list();
		recordIds.addAll(toClaimList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));
		return recordIds;
	}

	public int recordDeleIdsTodo(String actYwId) {
		ActYw actYw=actYwService.get(actYwId);
		if(actYw==null){
			return 0;
		}
		int delegateNum=0;
		List<ActYwGnode>actYwGnodeList=actYwGnodeService.getDelegateNode(actYw.getId());
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		for(ActYwGnode actYwGnode:actYwGnodeList){
			List<String> recordIds = new ArrayList<>();
			// =============== 已经签收的任务  ===============
			TaskQuery todoTaskQuery = taskService.createTaskQuery()
					.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwGnode.getId())
					.includeProcessVariables().active().orderByTaskCreateTime().desc();
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				todoTaskQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (StringUtils.isNotBlank(actYwId)) {
				todoTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
			}

			List<Task> taskList=todoTaskQuery.list();
			for(Task task:taskList){
				String id=(String)task.getProcessVariables().get("id");
				if(StringUtils.isNotEmpty(id)){
					recordIds.add(id);
				}
			}
			recordIds=StringUtil.removeDup(recordIds);
			if(recordIds.size()>0){
				ActYwEtAssignTaskVo actYwEtAssignTaskVo=new ActYwEtAssignTaskVo();
				actYwEtAssignTaskVo.setActywId(actYwId);
				actYwEtAssignTaskVo.setGnodeId(actYwGnode.getId());
				actYwEtAssignTaskVo.setProIds(recordIds);
				List<String> hasDeleGateList=actYwGassignService.getProListHasDelegate(actYwEtAssignTaskVo);
				delegateNum=delegateNum+recordIds.size()-hasDeleGateList.size();
			}
		}

		return delegateNum;
	}

	public int recordIdsAllAssign(String actYwId) {
		List<String> recordIds = new ArrayList<>();
		ActYw actYw=actYwService.get(actYwId);
		if(actYw==null){
			return 0;
		}
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee("assignUser")
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			todoTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}
		List<Task> todoList = todoTaskQuery.list();
		recordIds.addAll(todoList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));

		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery =taskService.createTaskQuery().taskCandidateUser("assignUser").includeProcessVariables().active().orderByTaskCreateTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			toClaimQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}
		List<Task> toClaimList = toClaimQuery.list();
		recordIds.addAll(toClaimList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));
		return recordIds.size();
	}

	public int recordUserId(String userId) {
		List<String> recordIds = new ArrayList<>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId)
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		List<Task> todoList = todoTaskQuery.list();
		recordIds.addAll(todoList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery =taskService.createTaskQuery().taskCandidateUser(userId).includeProcessVariables().active().orderByTaskCreateTime().desc();
		List<Task> toClaimList = toClaimQuery.list();
		recordIds.addAll(toClaimList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));
		return recordIds.size();
	}

	public List<String> todoPromodelIds(String userId) {
		List<String> recordIds = new ArrayList<>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery().taskAssignee(userId)
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		List<Task> todoList = todoTaskQuery.list();
		recordIds.addAll(todoList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));
		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery =taskService.createTaskQuery().taskCandidateUser(userId).includeProcessVariables().active().orderByTaskCreateTime().desc();
		List<Task> toClaimList = toClaimQuery.list();
		recordIds.addAll(toClaimList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));
		return recordIds;
	}

	public List<String> recordIdsWithoutUser(Act act, List<String> gnodeIds, String actYwId) {
		List<String> recordIds = new ArrayList<>();
		// =============== 已经签收的任务  ===============
		for (String gnodeId : gnodeIds) {
			String keyName = ActYwTool.FLOW_ID_PREFIX + gnodeId;
			TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
					getTaskQueryWithoutAssignee()
					.includeProcessVariables().active().orderByTaskCreateTime().desc();
			todoTaskQuery.taskDefinitionKeyLike("%" + keyName + "%");
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				todoTaskQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (StringUtils.isNotBlank(actYwId)) {
				todoTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
			}
			List<Task> todoList = todoTaskQuery.list();
			recordIds.addAll(todoList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));

			// =============== 等待签收的任务  ===============
			TaskQuery toClaimQuery =//taskService.createTaskQuery().taskCandidateUser(userId)
					getTaskQueryByCandidateUser()
					.includeProcessVariables().active().orderByTaskCreateTime().desc();
			toClaimQuery.taskDefinitionKeyLike("%" + keyName + "%");
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				toClaimQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (StringUtils.isNotBlank(actYwId)) {
				toClaimQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
			}
			List<Task> toClaimList = toClaimQuery.list();
			recordIds.addAll(toClaimList.stream().map(e -> (String)e.getProcessVariables().get("id")).collect(Collectors.toList()));
		}
		// =============== 已经处理过且处于同一个子流程下的任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = //historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
		getHistoricTaskInstanceQueryByAssignee()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			histTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		List<HistoricTaskInstance> history = histList.stream().filter(e ->!recordIds.contains(e.getProcessVariables().get("id"))).collect(Collectors.toList());
		List<String> temp = new ArrayList<>();
		for (HistoricTaskInstance h : history) {
			String id = (String) h.getProcessVariables().get("id");
			List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals("id", id).active().list();//流程当前所处的节点（只有一个）
			if (!tasks.isEmpty()) {//流程未结束
				if (gnodeIds.contains(tasks.get(0).getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))) {//判断当前流转到的节点是否处于本子流程中
					recordIds.add(id);
				}
			}else{//流程已结束
				if (temp.contains(id)) {
					continue;
				}
				temp.add(id);
				if(gnodeIds.contains(h.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))){
					recordIds.add(id);
				}
			}
		}
		StringUtil.removeDup(recordIds);
		List<String> repRecordIds=Lists.newArrayList();
		for(int i=0;i<recordIds.size();i++){
			if(StringUtil.isNotEmpty(recordIds.get(i))){
				repRecordIds.add(recordIds.get(i));
			}else{
				logger.info("垃圾数据 i=========="+i);
			}
		}
		return repRecordIds;
	}
	/**
	 * 审核列表记录的ids
	 * @param act
	 * @param gnodeIds 节点ids
	 * @return
	 */
	public List<String> recordIds(Act act, List<String> gnodeIds, String actYwId) {
		// Long st=System.currentTimeMillis();//记录结束时间
		//String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
		List<String> recordIds = new ArrayList<>();
		// =============== 已经签收的任务  ===============
		for (String gnodeId : gnodeIds) {
			String keyName = ActYwTool.FLOW_ID_PREFIX + gnodeId;
			TaskQuery todoTaskQuery = null;
			if(!UserUtils.isNscAdminOffice(UserUtils.getUser())){
				todoTaskQuery=  getTaskQueryByAssignee().includeProcessVariables().active().orderByTaskCreateTime().desc();
			}else{
				todoTaskQuery=taskService.createTaskQuery().includeProcessVariables().active().orderByTaskCreateTime().desc();
			}

//			TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
//			getTaskQueryByAssignee() .includeProcessVariables().active().orderByTaskCreateTime().desc();
			todoTaskQuery.taskDefinitionKeyLike("%" + keyName + "%");
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				todoTaskQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (StringUtils.isNotBlank(actYwId)) {
				todoTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
			}
			 //st=System.currentTimeMillis();//记录结束时间
			List<Task> todoList = todoTaskQuery.list();
			//System.out.println("\n------------------- todoTaskQuery.list()..执行时间0："+(System.currentTimeMillis()-st)+"ms");
			recordIds.addAll(todoList.stream().map(e -> (String)e.getProcessVariables().get(CoreJkey.JK_ID)).collect(Collectors.toList()));
			// =============== 等待签收的任务  ===============
			TaskQuery toClaimQuery =null;
			//taskService.createTaskQuery().taskCandidateUser(userId)
//					getTaskQueryByCandidateUser()
//					.includeProcessVariables().active().orderByTaskCreateTime().desc();
			if(!UserUtils.isNscAdminOffice(UserUtils.getUser())){
				toClaimQuery=  getTaskQueryByCandidateUser().includeProcessVariables().active().orderByTaskCreateTime().desc();
			}else{
				toClaimQuery=taskService.createTaskQuery().includeProcessVariables().active().orderByTaskCreateTime().desc();
			}
			toClaimQuery.taskDefinitionKeyLike("%" + keyName + "%");
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				toClaimQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (StringUtils.isNotBlank(actYwId)) {
				toClaimQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
			}
			//st=System.currentTimeMillis();//记录结束时间
			List<Task> toClaimList = toClaimQuery.list();
			//System.out.println("\n-------------------toClaimQuery.list()..等待签收的任务："+(System.currentTimeMillis()-st)+"ms");
			recordIds.addAll(toClaimList.stream().map(e -> (String)e.getProcessVariables().get(CoreJkey.JK_ID)).collect(Collectors.toList()));
		}
		  //st=System.currentTimeMillis();//记录结束时间
		// =============== 已经处理过且处于同一个子流程下的任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = null;
		//historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
//		getHistoricTaskInstanceQueryByAssignee()
//				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		if(!UserUtils.isNscAdminOffice(UserUtils.getUser())){
			histTaskQuery=  getHistoricTaskInstanceQueryByAssignee()
							.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		}else{
			histTaskQuery=historyService.createHistoricTaskInstanceQuery()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		}
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			histTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}
		//System.out.println("\n-------------------..执行时间2："+(System.currentTimeMillis()-st)+"ms");
		// st=System.currentTimeMillis();//记录结束时间
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		//System.out.println("\n-------------------histTaskQuery.list() 执行时间："+(System.currentTimeMillis()-st)+"ms");

		List<HistoricTaskInstance> history = histList.stream().filter(e ->!recordIds.contains(e.getProcessVariables().get(CoreJkey.JK_ID))).collect(Collectors.toList());
		List<String> temp = new ArrayList<>();
		for (HistoricTaskInstance h : history) {
			String id = (String) h.getProcessVariables().get(CoreJkey.JK_ID);
			List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals(CoreJkey.JK_ID, id).active().list();//流程当前所处的节点（只有一个）
			if (!tasks.isEmpty()) {//流程未结束
				if (gnodeIds.contains(tasks.get(0).getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))) {//判断当前流转到的节点是否处于本子流程中
					recordIds.add(id);
				}
			}else{//流程已结束
				if (temp.contains(id)) {
					continue;
				}
				temp.add(id);
				if(gnodeIds.contains(h.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))){
					recordIds.add(id);
				}
			}
		}
		return recordIds;
	}

	public Task currentTask(String procDefKey, String actYwId, String proModelId){
		TaskQuery todoTaskQuery = taskService.createTaskQuery().includeProcessVariables().active();
		todoTaskQuery.processDefinitionKey(procDefKey);
		todoTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		todoTaskQuery.processVariableValueEquals(CoreJkey.JK_ID, proModelId);
		List<Task> list = todoTaskQuery.list();
		if(!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 查询列表ids
	 * @param act
	 * @param actYwId  业务ID，用于区分项目
	 * @return
	 */
	public List<String> queryRecordIds(Act act, String actYwId) {
		User user = UserUtils.getUser();
		List<String> recordIds = new ArrayList<>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = taskService.createTaskQuery()
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
//		if (StringUtils.isNotBlank(actYwId)) {
//			todoTaskQuery.processVariableValueEquals("actYwId", actYwId);
//		}

		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = taskService.createTaskQuery()
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			toClaimQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}

		// =============== 已经处理过的任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().finished()
				.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtils.isNotBlank(actYwId)) {
			histTaskQuery.processVariableValueEquals(ActYwGroup.ACT_YW_ID, actYwId);
		}

		if (!UserUtils.isAdminOrSuperAdmin(UserUtils.getUser())) {
			todoTaskQuery.or().taskOwner(user.getId()).taskAssignee(user.getId()).endOr();
			toClaimQuery.taskCandidateUser(user.getId());
			histTaskQuery.taskAssignee(user.getId());
		}
		CompletableFuture<List<Task>> todoFuture = CompletableFuture.supplyAsync(() -> todoTaskQuery.list(), ThreadUtils.newFixedThreadPool());
		CompletableFuture<List<Task>> toClaimFuture = CompletableFuture.supplyAsync(() -> toClaimQuery.list(),ThreadUtils.newFixedThreadPool());
		CompletableFuture<List<HistoricTaskInstance>> histFuture = CompletableFuture.supplyAsync(() -> histTaskQuery.list(),ThreadUtils.newFixedThreadPool());
		CompletableFuture.allOf(todoFuture,toClaimFuture,histFuture).join();
		List<Task> todoList = null;
		List<Task> toClaimList = null;
		List<HistoricTaskInstance> histList = null;
		try {
			todoList = todoFuture.get();
			toClaimList = toClaimFuture.get();
			histList = histFuture.get();
		} catch (InterruptedException e) {
			logger.info("查询线程中断,queryRecordIds");
			e.printStackTrace();
		} catch (ExecutionException e) {
			logger.info("执行异常,queryRecordIds");
			e.printStackTrace();
		}finally {
			ThreadUtils.shutdown();
		}
//		List<Task> todoList = todoTaskQuery.list();
		recordIds.addAll(todoList.stream().map(e -> (String)e.getProcessVariables().get("id")).filter(e-> e != null).collect(Collectors.toList()));

//		List<Task> toClaimList = toClaimQuery.list();
		recordIds.addAll(toClaimList.stream().map(e -> (String)e.getProcessVariables().get("id")).filter(e-> e != null).collect(Collectors.toList()));

//		List<HistoricTaskInstance> histList = histTaskQuery.list();
		Set<String> set = histList.stream().filter(e -> !recordIds.contains(e.getProcessVariables().get("id"))).map(e -> (String) e.getProcessVariables().get("id")).collect(Collectors.toSet());
		recordIds.addAll(set);
		return recordIds;
	}
	public static List removeDuplicate(List list) {
	    HashSet h = new HashSet(list);
	    list.clear();
	    list.addAll(h);
	    return list;
	}

	/**
	 * 获取待审核列表
	 *
	 * @return
	 */
	public Page<Act> modeltodoList(Page<Act> page, Act act, String keyName) {
		String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
		String loginName = UserUtils.getUser().getLoginName();
		List<Act> result = new ArrayList<Act>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery()
		getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		//对应模块
		todoTaskQuery.taskDefinitionKeyLike("%" + keyName + "%");
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("todo");
			result.add(e);
		}
		// =============== 已经审核的任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = //historyService.createHistoricTaskInstanceQuery().or().taskAssignee(userId).taskAssignee(loginName).endOr()
		getHistoricTaskInstanceQueryByAssignee()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		histTaskQuery.taskDefinitionKeyLike("%" + keyName + "%");
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setHistTask(histTask);
			e.setTaskName(histTask.getName());
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			e.setStatus("finish");
			result.add(e);
		}
		//处理分页问题
		// 查询总数
		page.setCount(todoTaskQuery.count() + histTaskQuery.count());
		int pageStart = (page.getPageNo() - 1) * page.getPageSize();
		int pageEnd = result.size();
		if (result.size() > page.getPageNo() * page.getPageSize()) {
			pageEnd = page.getPageNo() * page.getPageSize();
		}
		List<Act> subList = result.subList(pageStart, pageEnd);

		page.setList(subList);
		return page;
	}

    /**
     * 根据角色得到当前节点下的字节点gnodeId
     */
	public String getGnodeName(String gnodeId, String actYwId) {
		String auditGonde = null;
		ActYwGnode actYwGnode = new ActYwGnode();
		actYwGnode.setParent(new ActYwGnode(gnodeId));
		actYwGnode.setGroup(new ActYwGroup(actYwService.get(actYwId).getGroupId()));
		ActYwNode actYwNode = new ActYwNode();
		actYwGnode.setNode(actYwNode);
		//查询当前节点下面任务节点id
		List<ActYwGnode> actYwGnodes = actYwGnodeService.findListBygGparent(actYwGnode);
		//查询当前角色 角色id
		List<String> roleIds = UserUtils.getUser().getRoleIdList();

		for (ActYwGnode curGnode : actYwGnodes) {
			for (int j = 0; j < roleIds.size(); j++) {
			    if ((curGnode).checkRoleByRid(roleIds.get(j))) {
					auditGonde = curGnode.getId();
					break;
				}

			}
			if (StringUtil.isNotEmpty(auditGonde)) {
				break;
			}
		}
		return auditGonde;
	}

	/**
	 * 获取待审核列表
	 *
	 * @return
	 */
	public List<String> getAllTodoId(String actYwId, String gnodeId) {
		String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
		String userName = UserUtils.getUser().getLoginName();
		List<String> result = new ArrayList<String>();
		gnodeId = getGnodeName(gnodeId, actYwId);

		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery().or().taskAssignee(userId).taskAssignee(userName).endOr()
			getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		ActYw actYw = actYwService.get(actYwId);
		String key = ActYw.getPkey(actYw);
		todoTaskQuery.processDefinitionKey(key);
		//对应模块
		todoTaskQuery.taskDefinitionKeyLike("%" + ActYwTool.FLOW_ID_PREFIX + gnodeId + "%");
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("todo");
			result.add((String) e.getVars().getMap().get("id"));
		}

		return result;
	}

	/**
	 * 获取待审核列表
	 *
	 * @return
	 */
	public List<String> getAllDataId(String actYwId, String gnodeId) {
		List<String> result = new ArrayList<String>();
		gnodeId = getGnodeName(gnodeId, actYwId);
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery().or().taskAssignee(userId).taskAssignee(userName).endOr()
			getTaskQueryByAssignee() .active().includeProcessVariables().orderByTaskCreateTime().desc();
		ActYw actYw = actYwService.get(actYwId);
		String key = ActYw.getPkey(actYw);
		todoTaskQuery.processDefinitionKey(key);
		//对应模块
		todoTaskQuery.taskDefinitionKeyLike("%" + ActYwTool.FLOW_ID_PREFIX + gnodeId + "%");
		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("todo");
			result.add((String) e.getVars().getMap().get("id"));
		}

		HistoricTaskInstanceQuery histTaskQuery = //historyService.createHistoricTaskInstanceQuery().or().taskAssignee(userId).taskAssignee(loginName).endOr()
			getHistoricTaskInstanceQueryByAssignee() .finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		histTaskQuery.taskDefinitionKeyLike("%" + ActYwTool.FLOW_ID_PREFIX + gnodeId + "%");
		histTaskQuery.processDefinitionKey(key);
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setHistTask(histTask);
			e.setTaskName(histTask.getName());
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			e.setStatus("finish");
			result.add((String) e.getVars().getMap().get("id"));
		}
		return result;
	}

	/**
	 * 获取待审核列表
	 *
	 * @param act 查询条件封装
	 * @return
	 */
	public List<Act> todoList(Act act) {
		String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
		String userName = UserUtils.getUser().getLoginName();
		List<Act> result = new ArrayList<Act>();
		// =============== 已经签收的任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery().or().taskAssignee(userId).taskAssignee(userName).endOr()
				getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();

		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null) {
			todoTaskQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null) {
			todoTaskQuery.taskCreatedBefore(act.getEndDate());
		}

		// 查询列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("todo");
			result.add(e);
		}

		// =============== 等待签收的任务  ===============
		TaskQuery toClaimQuery = //taskService.createTaskQuery().taskCandidateUser(userId)
				getTaskQueryByCandidateUser()
				.includeProcessVariables().active().orderByTaskCreateTime().desc();

		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (act.getBeginDate() != null) {
			toClaimQuery.taskCreatedAfter(act.getBeginDate());
		}
		if (act.getEndDate() != null) {
			toClaimQuery.taskCreatedBefore(act.getEndDate());
		}

		// 查询列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("claim");
			result.add(e);
		}
		return result;
	}

	/**
	 * 获取流转历史列表
	 *
	 * @param procInsId 流程实例
	 * @param startAct  开始活动节点名称
	 * @param endAct    结束活动节点名称
	 */
	public List<Act> histoicFlowList(String procInsId, String startAct, String endAct) {
		List<Act> actList = Lists.newArrayList();
		List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInsId)
				.orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();

		boolean start = false;
		Map<String, Integer> actMap = Maps.newHashMap();

		for (int i = 0; i < list.size(); i++) {

			HistoricActivityInstance histIns = list.get(i);

			// 过滤开始节点前的节点
			if (StringUtil.isNotBlank(startAct) && startAct.equals(histIns.getActivityId())) {
				start = true;
			}
			if (StringUtil.isNotBlank(startAct) && !start) {
				continue;
			}

			// 只显示开始节点和结束节点，并且执行人不为空的任务
			if (StringUtil.isNotBlank(histIns.getAssignee())
					|| "startEvent".equals(histIns.getActivityType())
					|| "endEvent".equals(histIns.getActivityType())) {

				// 给节点增加一个序号
				Integer actNum = actMap.get(histIns.getActivityId());
				if (actNum == null) {
					actMap.put(histIns.getActivityId(), actMap.size());
				}

				Act e = new Act();
				e.setHistIns(histIns);
				// 获取流程发起人名称
				if ("startEvent".equals(histIns.getActivityType())) {
					List<HistoricProcessInstance> il = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).orderByProcessInstanceStartTime().asc().list();
//					List<HistoricIdentityLink> il = historyService.getHistoricIdentityLinksForProcessInstance(procInsId);
					if (il.size() > 0) {
						if (StringUtil.isNotBlank(il.get(0).getStartUserId())) {
							User user = UserUtils.get(il.get(0).getStartUserId());
							if (user != null) {
								e.setAssignee(histIns.getAssignee());
								e.setAssigneeName(user.getName());
							}
						}
					}
				}
				// 获取任务执行人名称
				if (StringUtil.isNotEmpty(histIns.getAssignee())) {
					User user = UserUtils.get(histIns.getAssignee());
					if (user != null) {
						e.setAssignee(histIns.getAssignee());
						e.setAssigneeName(user.getName());
					}
				}
				// 获取意见评论内容
				if (StringUtil.isNotBlank(histIns.getTaskId())) {
					List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
					if (commentList.size() > 0) {
						e.setComment(commentList.get(0).getFullMessage());
					}
				}
				actList.add(e);
			}

			// 过滤结束节点后的节点
			if (StringUtil.isNotBlank(endAct) && endAct.equals(histIns.getActivityId())) {
				boolean bl = false;
				Integer actNum = actMap.get(histIns.getActivityId());
				// 该活动节点，后续节点是否在结束节点之前，在后续节点中是否存在
				for (int j = i + 1; j < list.size(); j++) {
					HistoricActivityInstance hi = list.get(j);
					Integer actNumA = actMap.get(hi.getActivityId());
					if ((actNumA != null && actNumA < actNum) || StringUtil.equals(hi.getActivityId(), histIns.getActivityId())) {
						bl = true;
					}
				}
				if (!bl) {
					break;
				}
			}
		}
		return actList;
	}

	/**
	 * 获取流程列表
	 *
	 * @param category 流程分类
	 */
	public Page<Object[]> processList(Page<Object[]> page, String category) {
		/*
		 * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		 */
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.latestVersion().active().orderByProcessDefinitionKey().asc();

		if (StringUtil.isNotEmpty(category)) {
			processDefinitionQuery.processDefinitionCategory(category);
		}
		page.setCount(processDefinitionQuery.count());
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getFirstResult(), page.getMaxResults());
		for (ProcessDefinition processDefinition : processDefinitionList) {
			String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			page.getList().add(new Object[]{processDefinition, deployment});
		}
		return page;
	}

	/**
	 * 获取流程表单（首先获取任务节点表单KEY，如果没有则取流程开始节点表单KEY）
	 *
	 * @return
	 */
	public String getFormKey(String procDefId, String taskDefKey) {
		String formKey = "";
		if (StringUtil.isNotBlank(procDefId)) {
			if (StringUtil.isNotBlank(taskDefKey)) {
				try {
					formKey = formService.getTaskFormKey(procDefId, taskDefKey);
				} catch (Exception e) {
					formKey = "";
				}
			}
			if (StringUtil.isBlank(formKey)) {
				formKey = formService.getStartFormKey(procDefId);
			}
			if (StringUtil.isBlank(formKey)) {
				formKey = "/404";
			}
		}
		logger.debug("getFormKey: {}" + formKey);
		return formKey;
	}

	/**
	 * 获取流程实例对象
	 *
	 * @param procInsId
	 * @return
	 */
	@Transactional(readOnly = false)
	public ProcessInstance getProcIns(String procInsId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId)
				.processInstanceTenantId(TenantConfig.getCacheTenant()).singleResult();
	}

	/**
	 * 启动流程
	 *
	 * @param procDefKey    流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId    业务表编号
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId) {
		return startProcess(procDefKey, businessTable, businessId, "");
	}


	/**
	 * 挂起流程
	 *
	 * @param processInstanceId 流程实例id
	 */
	@Transactional(readOnly = false)
	public void suspendProcess(String processInstanceId) {
		runtimeService.suspendProcessInstanceById(processInstanceId);
		runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.processInstanceTenantId(TenantConfig.getCacheTenant()).singleResult();
	}

	/**
	 * 启动流程
	 *
	 * @param procDefKey    流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId    业务表编号
	 * @param title         流程标题，显示在待办任务标题
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title) {
		Map<String, Object> vars = Maps.newHashMap();
		return startProcess(procDefKey, businessTable, businessId, title, vars);
	}

	/**
	 * 启动流程
	 *
	 * @param procDefKey    流程定义KEY
	 * @param businessTable 业务表表名
	 * @param businessId    业务表编号
	 * @param title         流程标题，显示在待办任务标题
	 * @param vars          流程变量
	 * @return 流程实例ID
	 */
	@Transactional(readOnly = false)
	public String startProcess(String procDefKey, String businessTable, String businessId, String title, Map<String, Object> vars) {
		String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId())
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		//后续改成保存用户id
		identityService.setAuthenticatedUserId(userId);
		// 设置流程变量
		if (vars == null) {
			vars = Maps.newHashMap();
		}
		// 设置流程标题
		if (StringUtil.isNotBlank(title)) {
			vars.put("title", title);
		}
		// 启动流程
		ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(procDefKey, businessTable + ":" + businessId, vars,TenantConfig.getCacheTenant());
		// 更新业务表流程实例ID
		Act act = new Act();
		act.setBusinessTable(businessTable);// 业务表名
		act.setBusinessId(businessId);    // 业务表ID
		act.setProcInsId(procIns.getId());
		actDao.updateProcInsIdByBusinessId(act);
		return act.getProcInsId();
	}

	/**
	 * 获取任务
	 *
	 * @param taskId 任务ID
	 */
	public Task getTask(String taskId) {
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	/**
	 * 删除任务
	 *
	 * @param taskId       任务ID
	 * @param deleteReason 删除原因
	 */
	@Transactional(readOnly = false)
	public void deleteTask(String taskId, String deleteReason) {
		taskService.deleteTask(taskId, deleteReason);
	}

	/**
	 * 签收任务
	 *
	 * @param taskId 任务ID
	 * @param userId 签收用户ID（用户登录名）
	 */
	@Transactional(readOnly = false)
	public void claim(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}


	public void claimByProcInsId(String procInsId, List<String> claims) {
		//下个节点自动签收
		if (claims != null && claims.size() == 1) {
			String taskId = getTaskidByProcInsId(procInsId);
			String claimName = claims.get(0);
			taskService.claim(taskId, claimName);
		}
	}

	public void claimByProcInsIdAndUser(String procInsId,String claimName) {
		String taskId = getTaskidByProcInsId(procInsId);
		taskService.claim(taskId, claimName);
	}

	/**
	 * 提交任务, 并保存意见
	 *
	 * @param taskId    任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment   任务提交意见的内容
	 * @param vars      任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, Map<String, Object> vars) {
		complete(taskId, procInsId, comment, "", vars);
	}

	/**
	 * 提交任务, 并保存意见
	 *
	 * @param taskId    任务ID
	 * @param procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param comment   任务提交意见的内容
	 * @param title     流程标题，显示在待办任务标题
	 * @param vars      任务变量
	 */
	@Transactional(readOnly = false)
	public void complete(String taskId, String procInsId, String comment, String title, Map<String, Object> vars) {
		// 添加意见
		if (StringUtil.isNotBlank(procInsId) && StringUtil.isNotBlank(comment)) {
			taskService.addComment(taskId, procInsId, comment);
		}
		// 设置流程变量
		if (vars == null) {
			vars = Maps.newHashMap();
		}
		// 设置流程标题
		if (StringUtil.isNotBlank(title)) {
			vars.put("title", title);
		}
		// 提交任务
		taskService.complete(taskId, vars);
	}

	/**
	 * 完成第一个任务
	 *
	 * @param procInsId
	 */
	@Transactional(readOnly = false)
	public void completeFirstTask(String procInsId) {
		completeFirstTask(procInsId, null, null, null);
	}

	/**
	 * 完成第一个任务
	 *
	 * @param procInsId
	 * @param comment
	 * @param title
	 * @param vars
	 */
	@Transactional(readOnly = false)
	public void completeFirstTask(String procInsId, String comment, String title, Map<String, Object> vars) {
		Task task = //taskService.createTaskQuery().taskAssignee(userId)
				getTaskQueryByAssignee()
				.processInstanceId(procInsId).active().singleResult();
		if (task != null) {
			complete(task.getId(), procInsId, comment, title, vars);
		}
	}


	/**
	 * 添加任务意见
	 */
	public void addTaskComment(String taskId, String procInsId, String comment) {
		taskService.addComment(taskId, procInsId, comment);
	}

	//////////////////  回退、前进、跳转、前加签、后加签、分裂 移植  https://github.com/bluejoe2008/openwebflow  //////////////////////////////////////////////////

	/**
	 * 任务后退一步
	 */
	public void taskBack(String procInsId, Map<String, Object> variables) {
		taskBack(getCurrentTask(procInsId), variables);
	}

	/**
	 * 任务后退至指定活动
	 */
	public void taskBack(TaskEntity currentTaskEntity, Map<String, Object> variables) {
		ActivityImpl activity = (ActivityImpl) ProcessDefUtils
				.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
				.getIncomingTransitions().get(0).getSource();
		jumpTask(currentTaskEntity, activity, variables);
	}

	/**
	 * 任务前进一步
	 */
	public void taskForward(String procInsId, Map<String, Object> variables) {
		taskForward(getCurrentTask(procInsId), variables);
	}

	/**
	 * 任务前进至指定活动
	 */
	public void taskForward(TaskEntity currentTaskEntity, Map<String, Object> variables) {
		ActivityImpl activity = (ActivityImpl) ProcessDefUtils
				.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(), currentTaskEntity.getTaskDefinitionKey())
				.getOutgoingTransitions().get(0).getDestination();

		jumpTask(currentTaskEntity, activity, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	public void jumpTask(String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables) {
		jumpTask(getCurrentTask(procInsId), targetTaskDefinitionKey, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	public void jumpTask(String procInsId, String currentTaskId, String targetTaskDefinitionKey, Map<String, Object> variables) {
		jumpTask(getTaskEntity(currentTaskId), targetTaskDefinitionKey, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 *
	 * @param currentTaskEntity       当前任务节点
	 * @param targetTaskDefinitionKey 目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public void jumpTask(TaskEntity currentTaskEntity, String targetTaskDefinitionKey, Map<String, Object> variables) {
		ActivityImpl activity = ProcessDefUtils.getActivity(processEngine, currentTaskEntity.getProcessDefinitionId(),
				targetTaskDefinitionKey);
		jumpTask(currentTaskEntity, activity, variables);
	}

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 *
	 * @param currentTaskEntity 当前任务节点
	 * @param targetActivity    目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	void jumpTask(TaskEntity currentTaskEntity, ActivityImpl targetActivity, Map<String, Object> variables) {
		CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
		commandExecutor.execute(new JumpTaskCmd(currentTaskEntity, targetActivity, variables));
	}

	/**
	 * 后加签
	 */
	@SuppressWarnings("unchecked")
	public ActivityImpl[] insertTasksAfter(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignees) {
		List<String> assigneeList = new ArrayList<String>();
		assigneeList.add(Authentication.getAuthenticatedUserId());
		assigneeList.addAll(CollectionUtils.arrayToList(assignees));
		String[] newAssignees = assigneeList.toArray(new String[0]);
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
		ActivityImpl prototypeActivity = ProcessDefUtils.getActivity(processEngine, processDefinition.getId(), targetTaskDefinitionKey);
		return cloneAndMakeChain(processDefinition, procInsId, targetTaskDefinitionKey, prototypeActivity.getOutgoingTransitions().get(0).getDestination().getId(), variables, newAssignees);
	}

	/**
	 * 前加签
	 */
	public ActivityImpl[] insertTasksBefore(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignees) {
		ProcessDefinitionEntity procDef = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
		return cloneAndMakeChain(procDef, procInsId, targetTaskDefinitionKey, targetTaskDefinitionKey, variables, assignees);
	}

	/**
	 * 分裂某节点为多实例节点
	 */
	public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, String... assignee) {
		return splitTask(procDefId, procInsId, targetTaskDefinitionKey, variables, true, assignee);
	}

	/**
	 * 分裂某节点为多实例节点
	 */
	@SuppressWarnings("unchecked")
	public ActivityImpl splitTask(String procDefId, String procInsId, String targetTaskDefinitionKey, Map<String, Object> variables, boolean isSequential, String... assignees) {
		SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
		info.setProcessDefinitionId(procDefId);
		info.setProcessInstanceId(procInsId);

		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);

		radei.setPrototypeActivityId(targetTaskDefinitionKey);
		radei.setAssignees(CollectionUtils.arrayToList(assignees));
		radei.setSequential(isSequential);

		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
		ActivityImpl clone = new MultiInstanceActivityCreator().createActivities(processEngine, processDefinition, info)[0];

		TaskEntity currentTaskEntity = this.getCurrentTask(procInsId);

		CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
		commandExecutor.execute(new CreateAndTakeTransitionCmd(currentTaskEntity, clone, variables));

//		recordActivitiesCreation(info);
		return clone;
	}

	private TaskEntity getCurrentTask(String procInsId) {
		return (TaskEntity) taskService.createTaskQuery().processInstanceId(procInsId).active().singleResult();
	}

	private TaskEntity getTaskEntity(String taskId) {
		return (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@SuppressWarnings("unchecked")
	private ActivityImpl[] cloneAndMakeChain(ProcessDefinitionEntity procDef, String procInsId, String prototypeActivityId, String nextActivityId, Map<String, Object> variables, String... assignees) {
		SimpleRuntimeActivityDefinitionEntity info = new SimpleRuntimeActivityDefinitionEntity();
		info.setProcessDefinitionId(procDef.getId());
		info.setProcessInstanceId(procInsId);

		RuntimeActivityDefinitionEntityIntepreter radei = new RuntimeActivityDefinitionEntityIntepreter(info);
		radei.setPrototypeActivityId(prototypeActivityId);
		radei.setAssignees(CollectionUtils.arrayToList(assignees));
		radei.setNextActivityId(nextActivityId);

		ActivityImpl[] activities = new ChainedActivitiesCreator().createActivities(processEngine, procDef, info);

		jumpTask(procInsId, activities[0].getId(), variables);
//		recordActivitiesCreation(info);

		return activities;
	}


	/**
	 * 读取带跟踪的图片
	 *
	 * @param executionId 环节ID
	 * @return 封装了各种节点信息
	 */
	public InputStream tracePhoto(String processDefinitionId, String executionId) {
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

		List<String> activeActivityIds = Lists.newArrayList();
		if (runtimeService.createExecutionQuery().executionId(executionId).count() > 0) {
			activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		}

		// 不使用spring请使用下面的两行代码
		// ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)ProcessEngines.getDefaultProcessEngine();
		// Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

		// 使用spring注入引擎请使用下面的这行代码
		Context.setProcessEngineConfiguration(processEngineFactory.getProcessEngineConfiguration());
//		return ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
		return processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
				.generateDiagram(bpmnModel, "png", activeActivityIds);
	}

	/**
	 * 流程跟踪图信息
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 封装了各种节点信息
	 */
	public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
		Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
		Object property = PropertyUtils.getProperty(execution, "activityId");
		String activityId = "";
		if (property != null) {
			activityId = property.toString();
		}
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceTenantId(TenantConfig.getCacheTenant())
				.processInstanceId(processInstanceId)
				.singleResult();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
		List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

		List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
		for (ActivityImpl activity : activitiList) {

			boolean currentActiviti = false;
			String id = activity.getId();

			// 当前节点
			if (id.equals(activityId)) {
				currentActiviti = true;
			}

			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

			activityInfos.add(activityImageInfo);
		}

		return activityInfos;
	}


	/**
	 * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
	 *
	 * @param activity
	 * @param processInstance
	 * @param currentActiviti
	 * @return
	 */
	private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
														  boolean currentActiviti) throws Exception {
		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);
		setPosition(activity, activityInfo);
		setWidthAndHeight(activity, activityInfo);

		Map<String, Object> properties = activity.getProperties();
		vars.put("节点名称", properties.get(CoreJkey.JK_NAME));
		vars.put("任务类型", ActUtils.parseToZhType(properties.get(CoreJkey.JK_TYPE).toString()));

		ActivityBehavior activityBehavior = activity.getActivityBehavior();
		logger.debug("activityBehavior={}" + activityBehavior);
		if (activityBehavior instanceof UserTaskActivityBehavior) {
			Task currentTask = null;

			// 当前节点的task
			if (currentActiviti) {
				currentTask = getCurrentTaskInfo(processInstance);
			}

			// 当前任务的分配角色
			UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
			TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
			Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
			if (!candidateGroupIdExpressions.isEmpty()) {

				// 任务的处理角色
				setTaskGroup(vars, candidateGroupIdExpressions);

				// 当前处理人
				if (currentTask != null) {
					setCurrentTaskAssignee(vars, currentTask);
				}
			}
		}

		vars.put("节点说明", properties.get("documentation"));

		String description = activity.getProcessDefinition().getDescription();
		vars.put("描述", description);

		logger.debug("trace variables: {}" + vars);
		activityInfo.put("vars", vars);
		return activityInfo;
	}

	/**
	 * 设置任务组
	 *
	 * @param vars
	 * @param candidateGroupIdExpressions
	 */
	private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
		String roles = "";
		for (Expression expression : candidateGroupIdExpressions) {
			String expressionText = expression.getExpressionText();
			String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
			roles += roleName;
		}
		vars.put("任务所属角色", roles);
	}

	/**
	 * 设置当前处理人信息
	 *
	 * @param vars
	 * @param currentTask
	 */
	private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
		String assignee = currentTask.getAssignee();
		if (assignee != null) {
			org.activiti.engine.identity.User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
			vars.put("当前处理人", userInfo);
		}
	}

	/**
	 * 获取当前节点信息
	 *
	 * @param processInstance
	 * @return
	 */
	private Task getCurrentTaskInfo(ProcessInstance processInstance) {
		Task currentTask = null;
		try {
			String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
			logger.debug("current activity id: {}" + activitiId);

			currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
					.singleResult();
			logger.debug("current task for processInstance: {}" + ToStringBuilder.reflectionToString(currentTask));

		} catch (Exception e) {
			logger.error("can not get property activityId from processInstance: {}" + processInstance);
		}
		return currentTask;
	}

	/**
	 * 设置宽度、高度属性
	 *
	 * @param activity
	 * @param activityInfo
	 */
	private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
	}

	/**
	 * 设置坐标位置
	 *
	 * @param activity
	 * @param activityInfo
	 */
	private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}


	/**
	 * 根据数据库表中procInsId得到任务Id
	 *
	 * @param procInsId
	 * @return 任务Id
	 * @author zhangzheng
	 * @version 2017-3-15
	 */
	public String getTaskidByProcInsId(String procInsId) {
		List<HistoricTaskInstance> hisList = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc().list();
		if (hisList.size() > 0) {
			HistoricTaskInstance hisTaskIns = hisList.get(0);
			return hisTaskIns.getId();
		} else {
			return "";
		}
	}


	/**
	 * 根据数据库表中procInsId得到任务Id
	 *
	 * @param procInsId
	 * @return 任务Id
	 * @author zhangzheng
	 * @version 2017-3-15
	 */
	public String getTaskidByProcInsId(String procInsId,String userId) {
		List<HistoricTaskInstance> hisList = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
		//getHistoricTaskInstanceQueryByAssignee()
				.processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc().list();
		if (hisList.size() > 0) {
			HistoricTaskInstance hisTaskIns = hisList.get(0);
			return hisTaskIns.getId();
		} else {
			return "";
		}
	}

	/**
	 * 根据数据库表中procInsId得到任务名称
	 *
	 * @param procInsId
	 * @return string taskName
	 * @author zhangzheng
	 * @version 2017-3-15
	 */
	public String getTaskNameByProcInsId(String procInsId) {
		List<HistoricTaskInstance> hisList = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc().list();
		if (hisList.size() > 0) {
			HistoricTaskInstance hisTaskIns = hisList.get(0);
			return hisTaskIns.getName();
		} else {
			return "";
		}
	}


	public String getProcessDefinitionId(String procInsId) {
		List<HistoricTaskInstance> hisList = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc().list();
		if (hisList.size() > 0) {
			HistoricTaskInstance hisTaskIns = hisList.get(0);
			return hisTaskIns.getProcessDefinitionId();
		} else {
			return "";
		}
	}

	public boolean isEnd(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceTenantId(TenantConfig.getCacheTenant())
				.processInstanceId(processInstanceId).singleResult();
		if (processInstance != null) {
			return processInstance.isEnded();
		}
		return false;
	}

	/**
	 * 查询流程当前节点的下一步节点。用于流程提示时的提示。
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	/*public Map<String, FlowNode> findNextTask(String taskId) throws Exception{
	Map<String, org.activiti.bpmn.model.FlowNode> nodeMap = new HashMap<String, org.activiti.bpmn.model.FlowNode>();
	ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);
	//查询当前节点
	HistoricTaskInstance histask =
			findHistricTaskById(taskId, processInstance.getProcessInstanceId());
	//查询流程定义 。
	BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
	List<org.activiti.bpmn.model.Process> listp = bpmnModel.getProcesses();
	org.activiti.bpmn.model.Process process = listp.get(0);
	//当前节点流定义
	FlowNode sourceFlowElement = ( FlowNode) process.getFlowElement(histask.getTaskDefinitionKey());
	// 找到当前任务的流程变量
	List<HistoricVariableInstance> listVar=historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list() ;
	iteratorNextNodes(process, sourceFlowElement, nodeMap,listVar);
	return nodeMap;
	}

*/

	/**
	 * 查询流程当前节点的下一步节点。用于流程提示时的提示。
	 *
	 * @param process
	 * @param sourceFlowElement
	 * @param nodeMap
	 * @param listVar
	 * @throws Exception
	 */
	private void iteratorNextNodes(org.activiti.bpmn.model.Process process, FlowNode sourceFlowElement, Map<String, FlowNode> nodeMap, List<HistoricVariableInstance> listVar)
			throws Exception {
		List<SequenceFlow> list = sourceFlowElement.getOutgoingFlows();
		for (SequenceFlow sf : list) {
			sourceFlowElement = (FlowNode) process.getFlowElement(sf.getTargetRef());
			if (StringUtils.isNotEmpty(sf.getConditionExpression())) {
				ExpressionFactory factory = new ExpressionFactoryImpl();
				SimpleContext context = new SimpleContext();
				for (HistoricVariableInstance var : listVar) {
					context.setVariable(var.getVariableName(), factory.createValueExpression(var.getValue(), var.getValue().getClass()));
				}
				ValueExpression e = factory.createValueExpression(context, sf.getConditionExpression(), boolean.class);
				if ((Boolean) e.getValue(context)) {
					nodeMap.put(sourceFlowElement.getId(), sourceFlowElement);
					break;
				}
			}
			if (sourceFlowElement instanceof org.activiti.bpmn.model.UserTask) {
				nodeMap.put(sourceFlowElement.getId(), sourceFlowElement);
				break;
			} else if (sourceFlowElement instanceof org.activiti.bpmn.model.ExclusiveGateway) {
				iteratorNextNodes(process, sourceFlowElement, nodeMap, listVar);
			}
		}
	}

	/**
	 * 根据实例编号查找下一个任务节点
	 *
	 * @param procInstId ：实例编号
	 * @return
	 */
	public TaskDefinition nextTaskDefinition(String procInstId) {
		//流程标示
		String processDefinitionId = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult().getProcessDefinitionId();

		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
		//执行实例
		ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(procInstId)
				.processInstanceTenantId(TenantConfig.getCacheTenant())				.singleResult();
		//当前实例的执行到哪个节点
		String activitiId = execution.getActivityId();
		//获得当前任务的所有节点
		List<ActivityImpl> activitiList = def.getActivities();
		String id = null;
		for (ActivityImpl activityImpl : activitiList) {
			id = activityImpl.getId();
			if (activitiId.equals(id)) {
				System.out.println("当前任务：" + activityImpl.getProperty(CoreJkey.JK_NAME));
				return nextTaskDefinition(activityImpl, activityImpl.getId(), "${iscorrect==1}");
				//              System.out.println(taskDefinition.getCandidateGroupIdExpressions().toArray()[0]);
				//              return taskDefinition;
			}
		}
		return null;
	}

	/**
	 * 下一个任务节点
	 *
	 * @param activityImpl
	 * @param activityId
	 * @param elString
	 * @return
	 */
	private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString) {
		if ("userTask".equals(activityImpl.getProperty(CoreJkey.JK_TYPE)) && !activityId.equals(activityImpl.getId())) {
			TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
			//              taskDefinition.getCandidateGroupIdExpressions().toArray();
			return taskDefinition;
		} else {
			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
			List<PvmTransition> outTransitionsTemp = null;
			for (PvmTransition tr : outTransitions) {
				PvmActivity ac = tr.getDestination(); //获取线路的终点节点
				if ("exclusiveGateway".equals(ac.getProperty(CoreJkey.JK_TYPE))) {
					outTransitionsTemp = ac.getOutgoingTransitions();
					if (outTransitionsTemp.size() == 1) {
						return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId, elString);
					} else if (outTransitionsTemp.size() > 1) {
						for (PvmTransition tr1 : outTransitionsTemp) {
							Object s = tr1.getProperty("conditionText");
							if (elString.equals(StringUtils.trim(s.toString()))) {
								return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString);
							}
						}
					}
				} else if ("userTask".equals(ac.getProperty(CoreJkey.JK_TYPE))) {
					return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
				} else {

				}
			}
			return null;
		}
	}

	public boolean getGrateBygondeId(String gondeId) {
		boolean res = false;
		ActYwGnode actYwGnode = actYwGnodeService.get(gondeId);
		List<ActYwGnode> actYwGnodeList = actYwGnodeService.findByYwParentIdsLike(actYwGnode);
		for (ActYwGnode actYwGnodeIndex : actYwGnodeList) {
			List<ActYwGform> formList=actYwGnodeIndex.getGforms();
			if(StringUtil.checkNotEmpty(formList)){
				for(ActYwGform actYwGform:formList){
					ActYwForm actYwForm=actYwGform.getForm();
					if (actYwForm!= null) {
						String path = actYwForm.getPath();
						//判断是否为立项
						if (path.contains("grate")) {
							res = true;
							break;
						}
					}
				}

			}

		}
		return res;
	}

	public ActYwGnode getFrontByGondeId(String groupId,String gondeId) {
		ActYwGnode res = null;
		ActYwGnode actYwGnode = new ActYwGnode();
		actYwGnode.setParent(new ActYwGnode(gondeId));
		actYwGnode.setGroup(new ActYwGroup(groupId));
		//actYwGnodeService.get(gondeId);
		List<ActYwGnode> actYwGnodeList = actYwGnodeService.findByYwParentIdsLike(actYwGnode);
		for (ActYwGnode actYwGnodeIndex : actYwGnodeList) {
			List<ActYwGform> formList=actYwGnodeIndex.getGforms();
			if(StringUtil.checkNotEmpty(formList)) {
				for (ActYwGform actYwGform : formList) {
					ActYwForm actYwForm = actYwGform.getForm();
					if (actYwForm != null) {
						String clientType = actYwForm.getClientType();
						//判断是否为前台表单
						if (clientType.contains(FormClientType.FST_FRONT.getKey())) {
							res = actYwGnodeIndex;
							break;
						}
					}
				}
			}
		}
		return res;
	}

	public ActivityImpl getActivityImplByProInsId(String proInsId) {
		ProcessInstance pro = runtimeService.createProcessInstanceQuery().processInstanceId(proInsId)
				.processInstanceTenantId(TenantConfig.getCacheTenant())
				.singleResult();
		String procDefId = //getProcessDefinitionIdByProInstId(proInsId);
		pro.getProcessDefinitionId();
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
		ActivityImpl activityImplIndex = null;
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
								activityImplIndex=subActImpl;
								break;
							}
						}
					}
				}
				if (activeId.equals(id)) {
					// 获得执行到那个节点
					activityImplIndex=activityImpl;
					break;
				}
			}
			if (activityImplIndex.getId()!=null) {
				break;
			}
		}
		return activityImplIndex;
	}

	public boolean ifOver(String procInsId) {
		ProcessInstance pi = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
		List<Task> task=taskService.createTaskQuery().processInstanceId(procInsId).list();
		if(pi!=null && StringUtil.checkNotEmpty(task)){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 根据userId或loginName获取已签收taskQuery.
	 * @return TaskQuery
	 */
	public TaskQuery getTaskQueryByAssignee() {
        User user = UserUtils.getUser();
        //根据ID查询.
		TaskQuery taskQuery  = taskService.createTaskQuery().
				or().taskAssigneeLikeIgnoreCase(user.getId()).taskAssignee(user.getLoginName()).endOr();
		taskService.createNativeTaskQuery().sql("");
        return taskQuery;
    }
	public TaskQuery getTaskQueryWithoutAssignee() {
        //根据ID查询.
		TaskQuery taskQuery  = taskService.createTaskQuery();
		taskService.createNativeTaskQuery().sql("");
        return taskQuery;
    }
	/**
	 * 根据userId或loginName获取签收taskQuery.
	 * @return TaskQuery
	 */
	public TaskQuery getTaskQueryByCandidateUser() {
	    User user = UserUtils.getUser();
		List<String> list=new ArrayList<String>();
		list.add(user.getLoginName());
	    //根据ID查询.
		TaskQuery taskQuery  = taskService.createTaskQuery().
				or().taskCandidateUser(user.getId()).taskCandidateGroupIn(list).endOr();
		return taskQuery;
	}

	/**
	 * 根据userId或loginName获取历史HistoricTaskInstanceQuery.
	 * @return HistoricTaskInstanceQuery
	 */
	public HistoricTaskInstanceQuery getHistoricTaskInstanceQueryByAssignee() {
	    User user = UserUtils.getUser();
	    //根据ID查询.
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().
				or().taskAssigneeLikeIgnoreCase(user.getId()).taskAssignee(user.getLoginName()).endOr();
	    return histTaskQuery;
	}


	/**
	 * 根据userId或loginName获取已签收taskQuery.
	 * @return TaskQuery
	 */
	public TaskQuery getTaskQueryByAssigneeOrCandidateUser() {
		//根据ID查询.
		TaskQuery taskQuery  = taskService.createTaskQuery().or().taskOwner(UserUtils.getUser().getId())
				.taskCandidateOrAssigned(UserUtils.getUser().getId()).endOr();
		return taskQuery;
	}

	@Transactional(readOnly = false)
	//退回任意节点
	//node 为已经添加的节点对应工作流中节点名称
	public String rollBackFlow(String taskId,String varName,List<String> users,String node) {
		try {
			// 添加出口接口的变量
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put(varName,users);
			variables.put("isChangeNode","1");
			// 取得当前任务.当前任务节点
			HistoricTaskInstance currTask = historyService
					.createHistoricTaskInstanceQuery().taskId(taskId)
					.singleResult();
			// 取得流程实例，流程实例
			ProcessInstance instance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(currTask.getProcessInstanceId())
					.singleResult();
//			variables = instance.getProcessVariables();
			// 取得流程定义
			ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(currTask.getProcessDefinitionId());
			if (definition == null) {
				logger.info("流程定义未找到");
				logger.error("出错啦！流程定义未找到");
				return "ERROR";
			}
			// 取得上一步活动
			ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
					.findActivity(currTask.getTaskDefinitionKey());

			//也就是节点间的连线
			List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
//			 清除当前活动的出口
			List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
			//新建一个节点连线关系集合
			//oriPvmTransitionList=currActivity.getOutgoingTransitions();
			List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
//			//
			for (PvmTransition pvmTransition : pvmTransitionList) {
				oriPvmTransitionList.add(pvmTransition);
			}
			pvmTransitionList.clear();
			ActivityImpl lastActivity=((ProcessDefinitionImpl) definition)
								.findActivity(node);


			// 建立新出口
			List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
//			for (PvmTransition nextTransition : nextTransitionList) {
				lastActivity.setVariables(variables);
				TransitionImpl newTransition = currActivity.createOutgoingTransition();
				newTransition.setDestination(lastActivity);
				newTransitions.add(newTransition);
//			}
			// 完成任务
			List<Task> tasks = taskService.createTaskQuery()
					.processInstanceId(instance.getId()).taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
			for (Task task : tasks) {
				taskService.complete(task.getId(), variables);
				historyService.deleteHistoricTaskInstance(task.getId());
				HistoricTaskInstanceQueryImpl his = new HistoricTaskInstanceQueryImpl();
				his.taskDefinitionKey(node);
				his.processInstanceId(task.getProcessInstanceId());
				List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
						.processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(node).list();
				if (!CollectionUtils.isEmpty(historicTaskInstanceList)) {
					historicTaskInstanceList.forEach(historicTaskInstance -> {
						if(historicTaskInstance.getDeleteReason()!=null
								&&"completed".equals(historicTaskInstance.getDeleteReason())){
							historyService.deleteHistoricTaskInstance(historicTaskInstance.getId());
						}
					});
				}
			}
			// 恢复方向
			for (TransitionImpl transitionImpl : newTransitions) {
				currActivity.getOutgoingTransitions().remove(transitionImpl);
			}
			for (PvmTransition pvmTransition : oriPvmTransitionList) {
				pvmTransitionList.add(pvmTransition);
			}
			logger.info("OK");
			logger.info("流程结束");
			return "SUCCESS";
		} catch (Exception e) {
			logger.error("失败");
			logger.error(e.getMessage());
			return "ERROR";
		}
	}

	public long getByProTypeToAssignCount(String proType) {
		List<ActYw> list=actYwService.findActYwListByProType(proType);
		long count =0;
		if(StringUtil.checkNotEmpty(list)){
			for(ActYw actYw:list){
				int num=recordIdsAllAssign(actYw.getId());
				List<String> todoList=recordIdsTodo(actYw.getId());
				int delegateNum= recordDeleIdsTodo(actYw.getId());

//				todoList=todoList
//				ActYwGassign actYwGassign =new ActYwGassign();
//				actYwGassign.setYwId(actYw.getId());
//				List<String> deleTodoList=actYwGassignService.getTodoProListByActYwGassign(actYwGassign);
				count=count+num+delegateNum;
			}
		}
		return count;
	}
}