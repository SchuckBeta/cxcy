package com.oseasy.pro.modules.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.utils.ProcessDefCache;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.promodel.utils.ProjectUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 国创项目工作流查询Service
 */
@Service
@Transactional(readOnly = true)
public class ProjectActTaskService extends BaseService {
	@Autowired
	private HistoryService historyService;
	@Autowired
	ActTaskService actTaskService;
	@Autowired
	ProjectDeclareService projectDeclareService;

	/**
	 * 2017-6-8  addBy zhangzheng
	 * 获取所有的工作流数据（待审核+待签收+已审核）
	 * @param page
	 * @param act 查询条件封装
	 * @return
	 */
	public Page<Act> allListForPage(Page<Act> page,Act act) {
		String userId = UserUtils.getUser().getId();
		// =============== 待审核任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
		actTaskService.getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap()!=null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				if (StringUtil.isNotBlank(entry.getValue())) {
					todoTaskQuery.processVariableValueLike(entry.getKey(),"%"+entry.getValue() +"%");
				}

			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			todoTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}
		// =============== 待签收任务  ===============
		TaskQuery toClaimQuery = //taskService.createTaskQuery().taskCandidateUser(userId)
		actTaskService.getTaskQueryByCandidateUser()
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap()!=null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				if (StringUtil.isNotBlank(entry.getValue())) {
					toClaimQuery.processVariableValueLike(entry.getKey(),"%"+entry.getValue() +"%");
				}

			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			toClaimQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}

		// =============== 已审核任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = actTaskService.getHistoricTaskInstanceQueryByAssignee()
				//historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap()!=null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				if (StringUtil.isNotBlank(entry.getValue())) {
					histTaskQuery.processVariableValueLike(entry.getKey(),"%"+entry.getValue() +"%");
				}

			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			histTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}

		//待审核、待签收、已审核整合
		// 查询总数
		page.setCount(todoTaskQuery.count()+toClaimQuery.count()+histTaskQuery.count());
		page.setTodoCount(todoTaskQuery.count()+toClaimQuery.count());
		List<Act> actList = Lists.newArrayList();  //所有数据
		// 待审核列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("todo");
			actList.add(e);
		}
		// 待签收列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setTask(task);
			e.setVars(task.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			e.setStatus("claim");
			actList.add(e);
		}
		//已审核列表
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setHistTask(histTask);
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			String procInsId=histTask.getProcessInstanceId();
//			//处理状态
//			String projectId= (String) e.getVars().getMap().get("id");
//			String stateStr=ProjectUtils.getStatus(projectId);
//			if (StringUtil.isNotBlank(stateStr)) {
//				e.setTaskName(stateStr);
//			}else{
//				List<HistoricTaskInstance> hisList2=historyService.createHistoricTaskInstanceQuery()
//						.processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc().list();
//				if (hisList2.size()>0) {
//					HistoricTaskInstance hisTaskIns = hisList2.get(0);
//					e.setTaskName("待"+hisTaskIns.getName());
//				}
//			}
			e.setStatus("finish");
			actList.add(e);
		}

		int pageStart=(page.getPageNo()-1)*page.getPageSize();
		int pageEnd=actList.size();
		if (actList.size()>page.getPageNo()*page.getPageSize()) {
			pageEnd=page.getPageNo()*page.getPageSize();
		}
		List<Act> subList=actList.subList(pageStart,pageEnd);
		for(Act actEnd : subList){
			if("finish".equals(actEnd.getStatus())){

				todoHistStateList(actEnd);
			}
		}
		page.setList(subList);
		return page;
	}
	public Page<Act> allCountForPage(Page<Act> page,Act act) {
		// =============== 待审核任务  ===============
		TaskQuery todoTaskQuery = //taskService.createTaskQuery().taskAssignee(userId)
		actTaskService.getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap()!=null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				if (StringUtil.isNotBlank(entry.getValue())) {
					todoTaskQuery.processVariableValueLike(entry.getKey(),"%"+entry.getValue() +"%");
				}

			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			todoTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}
		// =============== 待签收任务  ===============
		TaskQuery toClaimQuery = //taskService.createTaskQuery().taskCandidateUser(userId)
		actTaskService.getTaskQueryByCandidateUser()
				.includeProcessVariables().active().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap()!=null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				if (StringUtil.isNotBlank(entry.getValue())) {
					toClaimQuery.processVariableValueLike(entry.getKey(),"%"+entry.getValue() +"%");
				}

			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			toClaimQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}

		// =============== 已审核任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = actTaskService.getHistoricTaskInstanceQueryByAssignee()
				//historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap()!=null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				if (StringUtil.isNotBlank(entry.getValue())) {
					histTaskQuery.processVariableValueLike(entry.getKey(),"%"+entry.getValue() +"%");
				}

			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			histTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}

		//待审核、待签收、已审核整合
		// 查询总数
		page.setCount(todoTaskQuery.count()+toClaimQuery.count()+histTaskQuery.count());
		page.setTodoCount(todoTaskQuery.count()+toClaimQuery.count());
		return page;
	}

	public void todoHistStateList(Act e){
		String procInsId=e.getHistTask().getProcessInstanceId();
		String projectId= (String) e.getVars().getMap().get("id");
		String stateStr=ProjectUtils.getStatus(projectId);
		if (StringUtil.isNotBlank(stateStr)) {
			e.setTaskName(stateStr);
		}else{
			List<HistoricTaskInstance> hisList2=historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc().list();
			if (hisList2.size()>0) {
				HistoricTaskInstance hisTaskIns = hisList2.get(0);
				e.setTaskName("待"+hisTaskIns.getName());
			}
		}
	}

    //
    public List<Act> finishedList(Act act) {
		String userId = UserUtils.getUser().getId();
		// =============== 已审核任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = //historyService.createHistoricTaskInstanceQuery().taskAssignee(userId)
				actTaskService.getHistoricTaskInstanceQueryByAssignee()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (act.getMap()!=null) {
			for (Map.Entry<String, String> entry : act.getMap().entrySet()) {
				if (StringUtil.isNotBlank(entry.getValue())) {
					histTaskQuery.processVariableValueLike(entry.getKey(),"%"+entry.getValue() +"%");
				}

			}
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			histTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}
		List<Act> actList = Lists.newArrayList();  //所有数据
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setHistTask(histTask);
			e.setVars(histTask.getProcessVariables());
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			e.setStatus("finish");
			String procInsId=histTask.getProcessInstanceId();
			//处理状态
			String projectId= (String) e.getVars().getMap().get("id");
			String stateStr=ProjectUtils.getStatus(projectId);
			if (StringUtil.isNotBlank(stateStr)) {
				e.setTaskName(stateStr);
			}else{
				List<HistoricTaskInstance> hisList2=historyService.createHistoricTaskInstanceQuery()
						.processInstanceId(procInsId).orderByHistoricTaskInstanceEndTime().asc().list();
				if (hisList2.size()>0) {
					HistoricTaskInstance hisTaskIns = hisList2.get(0);
					e.setTaskName("待"+hisTaskIns.getName());
					if (StringUtil.startsWith(hisTaskIns.getTaskDefinitionKey(),act.getStatus())) {
						actList.add(e);
					}
				}
			}

		}
		return actList;
	}

	public Page<ProjectDeclare> allListByPage(Page<ProjectDeclare> page, Act act, ProjectDeclare projectDeclare) {
		// =============== 待审核任务  ===============
		TaskQuery todoTaskQuery = actTaskService.getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			todoTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}
		// =============== 待签收任务  ===============
		TaskQuery toClaimQuery = actTaskService.getTaskQueryByCandidateUser().includeProcessVariables().active().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			toClaimQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}
		// =============== 已审核任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = actTaskService.getHistoricTaskInstanceQueryByAssignee()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			histTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}

		//待审核、待签收、已审核整合
		// 查询总数

		List<Map<String,Object>> mapList = Lists.newArrayList();  //所有数据
		List<String> ids= Lists.newArrayList();  //所有数据id
		// 待审核列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setVars(task.getProcessVariables());
			e.setTask(task);
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			Map<String,Object> map=new HashMap<>();
			map.put("id",(String)e.getVars().getMap().get("id"));
			map.put((String)e.getVars().getMap().get("id"),"todo");
			map.put("act",e);
			mapList.add(map);
			ids.add((String)e.getVars().getMap().get("id"));
		}
		// 待签收列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setVars(task.getProcessVariables());
			e.setTask(task);
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			Map<String,Object> map=new HashMap<>();
			map.put("id",(String)e.getVars().getMap().get("id"));
			map.put((String)e.getVars().getMap().get("id"),"claim");
			map.put("act",e);
			mapList.add(map);
			if(!ids.contains((String)e.getVars().getMap().get("id"))){
				ids.add((String)e.getVars().getMap().get("id"));
			}
		}
		//已审核列表
		//TODO CHENHAO 这个方法的逻辑能不能优化
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setVars(histTask.getProcessVariables());
			e.setHistTask(histTask);
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			Map<String,Object> map=new HashMap<>();
			map.put("id",(String)e.getVars().getMap().get("id"));
			map.put((String)e.getVars().getMap().get("id"),"finish");
			map.put("act",e);
			mapList.add(map);
			if(!ids.contains((String)e.getVars().getMap().get("id"))){
				ids.add((String)e.getVars().getMap().get("id"));
			}
		}
		projectDeclare.setIds(ids);
		List<ProjectDeclare> proList=projectDeclareService.findListByPro(projectDeclare);
		page.setCount(proList.size());
		page.setTodoCount(todoTaskQuery.count()+toClaimQuery.count());
		int pageStart=(page.getPageNo()-1)*page.getPageSize();
		int pageEnd=proList.size();
		if (proList.size()>page.getPageNo()*page.getPageSize()) {
			pageEnd=page.getPageNo()*page.getPageSize();
		}
		List<ProjectDeclare> subList=proList.subList(pageStart,pageEnd);
		for(ProjectDeclare project : subList){
			for(Map map:mapList){
				String state=(String)map.get(project.getId());
				if(StringUtil.isNotEmpty(state)){
					project.setState(state);
					project.setAct((Act)map.get("act"));
					break;
				}
			}
		}
		page.setList(subList);
		return page;
	}

	public Page<ProjectDeclare> allMidListByPage(Page<ProjectDeclare> page, Act act, ProjectDeclare projectDeclare,
												 List<Act> middleScoreList, List<ProjectDeclare> projectList) {
			// =============== 待审核任务  ===============
			TaskQuery todoTaskQuery = actTaskService.getTaskQueryByAssignee()
					.active().includeProcessVariables().orderByTaskCreateTime().desc();
			// 设置查询条件
			if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
				todoTaskQuery.processDefinitionKey(act.getProcDefKey());
			}
			//遍历act.getVars().getMap() 设置processVariableValueLike
			if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
				todoTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
			}
			// =============== 待签收任务  ===============
			TaskQuery toClaimQuery = actTaskService.getTaskQueryByCandidateUser().includeProcessVariables().active().orderByTaskCreateTime().desc();
			// 设置查询条件
			if (StringUtil.isNotBlank(act.getProcDefKey())) {
				toClaimQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
				toClaimQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
			}
			// =============== 已审核任务  ===============
			HistoricTaskInstanceQuery histTaskQuery = actTaskService.getHistoricTaskInstanceQueryByAssignee()
					.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
			// 设置查询条件
			if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
				histTaskQuery.processDefinitionKey(act.getProcDefKey());
			}
			if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
				histTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
			}

			//待审核、待签收、已审核整合
			// 查询总数

			List<Map<String,Object>> mapList = Lists.newArrayList();  //所有数据
			List<String> ids= Lists.newArrayList();  //所有数据id
			// 待审核列表
			List<Task> todoList = todoTaskQuery.list();
			for (Task task : todoList) {
				Act e = new Act();
				e.setVars(task.getProcessVariables());
				e.setTask(task);
				e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
				Map<String,Object> map=new HashMap<>();
				map.put("id",(String)e.getVars().getMap().get("id"));
				map.put((String)e.getVars().getMap().get("id"),"todo");
				map.put("act",e);
				mapList.add(map);
				ids.add((String)e.getVars().getMap().get("id"));
			}
			// 待签收列表
			List<Task> toClaimList = toClaimQuery.list();
			for (Task task : toClaimList) {
				Act e = new Act();
				e.setVars(task.getProcessVariables());
				e.setTask(task);
				e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
				Map<String,Object> map=new HashMap<>();
				map.put("id",(String)e.getVars().getMap().get("id"));
				map.put((String)e.getVars().getMap().get("id"),"claim");
				map.put("act",e);
				mapList.add(map);
				if(!ids.contains((String)e.getVars().getMap().get("id"))){
					ids.add((String)e.getVars().getMap().get("id"));
				}
			}
			//已审核列表
			List<HistoricTaskInstance> histList = histTaskQuery.list();
			for (HistoricTaskInstance histTask : histList) {
				Act e = new Act();
				e.setVars(histTask.getProcessVariables());
				e.setHistTask(histTask);
				e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
				Map<String,Object> map=new HashMap<>();
				map.put("id",(String)e.getVars().getMap().get("id"));
				map.put((String)e.getVars().getMap().get("id"),"finish");
				map.put("act",e);
				mapList.add(map);
				if(!ids.contains((String)e.getVars().getMap().get("id"))){
					ids.add((String)e.getVars().getMap().get("id"));
				}
			}
			for (Act midAct : middleScoreList) {
				Act e = midAct;
				Map<String,Object> map=new HashMap<>();
				map.put("id",(String)e.getVars().getMap().get("id"));
				map.put((String)e.getVars().getMap().get("id"),"middleScore");
				map.put("act",e);
				mapList.add(map);
				if(!ids.contains((String)e.getVars().getMap().get("id"))){
					ids.add((String)e.getVars().getMap().get("id"));
				}
			}
			projectDeclare.setIds(ids);
			List<ProjectDeclare> proList=projectDeclareService.findListByPro(projectDeclare);
			for(ProjectDeclare addProjectDeclare:projectList){
				boolean ishave=false;
				for(ProjectDeclare pro:proList){
					if(addProjectDeclare.getId().equals(pro.getId())){
						ishave=true;
						break;
					}
				}
				if(!ishave){
					addProjectDeclare.setState("other");
					proList.add(addProjectDeclare);
				}
			}
			//proList.addAll(projectList);
			page.setCount(proList.size());
			int pageStart=(page.getPageNo()-1)*page.getPageSize();
			int pageEnd=proList.size();
			if (proList.size()>page.getPageNo()*page.getPageSize()) {
				pageEnd=page.getPageNo()*page.getPageSize();
			}
			List<ProjectDeclare> subList=proList.subList(pageStart,pageEnd);
			for(ProjectDeclare project : subList){
				for(Map map:mapList){
					String state=(String)map.get(project.getId());
					if(StringUtil.isNotEmpty(state)){
						project.setState(state);
						project.setAct((Act)map.get("act"));
						break;
					}
				}
			}
			page.setList(subList);
			return page;
		}

	public Page<ProjectDeclare> allListAddProListByPage(Page<ProjectDeclare> page, Act act, ProjectDeclare projectDeclare,
													 List<ProjectDeclare> projectList) {
		// =============== 待审核任务  ===============
		TaskQuery todoTaskQuery = actTaskService.getTaskQueryByAssignee()
				.active().includeProcessVariables().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			todoTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		//遍历act.getVars().getMap() 设置processVariableValueLike
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			todoTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}
		// =============== 待签收任务  ===============
		TaskQuery toClaimQuery = actTaskService.getTaskQueryByCandidateUser().includeProcessVariables().active().orderByTaskCreateTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {
			toClaimQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			toClaimQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}
		// =============== 已审核任务  ===============
		HistoricTaskInstanceQuery histTaskQuery = actTaskService.getHistoricTaskInstanceQueryByAssignee()
				.finished().includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		// 设置查询条件
		if (StringUtil.isNotBlank(act.getProcDefKey())) {  //流程名称
			histTaskQuery.processDefinitionKey(act.getProcDefKey());
		}
		if (StringUtil.isNotBlank(act.getTaskDefKey())) {  //addBy zhangzheng 查询阶段
			histTaskQuery.taskDefinitionKeyLike("%"+act.getTaskDefKey()+"%");
		}

		//待审核、待签收、已审核整合
		// 查询总数

		List<Map<String,Object>> mapList = Lists.newArrayList();  //所有数据
		List<String> ids= Lists.newArrayList();  //所有数据id
		// 待审核列表
		List<Task> todoList = todoTaskQuery.list();
		for (Task task : todoList) {
			Act e = new Act();
			e.setVars(task.getProcessVariables());
			e.setTask(task);
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			Map<String,Object> map=new HashMap<>();
			map.put("id",(String)e.getVars().getMap().get("id"));
			map.put((String)e.getVars().getMap().get("id"),"todo");
			map.put("act",e);
			mapList.add(map);
			ids.add((String)e.getVars().getMap().get("id"));
		}
		// 待签收列表
		List<Task> toClaimList = toClaimQuery.list();
		for (Task task : toClaimList) {
			Act e = new Act();
			e.setVars(task.getProcessVariables());
			e.setTask(task);
			e.setProcDef(ProcessDefCache.get(task.getProcessDefinitionId()));
			Map<String,Object> map=new HashMap<>();
			map.put("id",(String)e.getVars().getMap().get("id"));
			map.put((String)e.getVars().getMap().get("id"),"claim");
			map.put("act",e);
			mapList.add(map);
			if(!ids.contains((String)e.getVars().getMap().get("id"))){
				ids.add((String)e.getVars().getMap().get("id"));
			}
		}
		//已审核列表
		List<HistoricTaskInstance> histList = histTaskQuery.list();
		for (HistoricTaskInstance histTask : histList) {
			Act e = new Act();
			e.setVars(histTask.getProcessVariables());
			e.setHistTask(histTask);
			e.setProcDef(ProcessDefCache.get(histTask.getProcessDefinitionId()));
			Map<String,Object> map=new HashMap<>();
			map.put("id",(String)e.getVars().getMap().get("id"));
			map.put((String)e.getVars().getMap().get("id"),"finish");
			map.put("act",e);
			mapList.add(map);
			if(!ids.contains((String)e.getVars().getMap().get("id"))){
				ids.add((String)e.getVars().getMap().get("id"));
			}
		}

		projectDeclare.setIds(ids);
		List<ProjectDeclare> proList=projectDeclareService.findListByPro(projectDeclare);
		for(ProjectDeclare addProjectDeclare:projectList){
			boolean ishave=false;
			for(ProjectDeclare pro:proList){
				if(addProjectDeclare.getId().equals(pro.getId())){
					ishave=true;
					break;
				}
			}
			if(!ishave){
				addProjectDeclare.setState("other");
				proList.add(addProjectDeclare);
			}
		}
		//proList.addAll(projectList);
		page.setCount(proList.size());
		page.setTodoCount(todoTaskQuery.count()+toClaimQuery.count());
		int pageStart=(page.getPageNo()-1)*page.getPageSize();
		int pageEnd=proList.size();
		if (proList.size()>page.getPageNo()*page.getPageSize()) {
			pageEnd=page.getPageNo()*page.getPageSize();
		}

		List<ProjectDeclare> subList=proList.subList(pageStart,pageEnd);
		for(ProjectDeclare project : subList){
			for(Map map:mapList){
				String state=(String)map.get(project.getId());
				if(StringUtil.isNotEmpty(state)){
					project.setState(state);
					project.setAct((Act)map.get("act"));
					break;
				}
			}
		}
		page.setList(subList);
		return page;
	}
}
