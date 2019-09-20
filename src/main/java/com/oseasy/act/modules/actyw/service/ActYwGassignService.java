package com.oseasy.act.modules.actyw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.oseasy.act.modules.act.utils.ThreadUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.dao.ActYwGassignDao;
import com.oseasy.act.modules.actyw.entity.ActYwEtAssignTaskVo;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.vo.EarAtype;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 业务指派表Service.
 * @author zy
 * @version 2018-04-03
 */
@Service
@Transactional(readOnly = true)
public class ActYwGassignService extends CrudService<ActYwGassignDao, ActYwGassign> {
	private static final String ACT_YW_ID = "actYwId";
	@Autowired
	ActTaskService actTaskService;

	@Autowired
	TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	ActYwAuditInfoService actYwAuditInfoService;
	@Autowired
	OaNotifyService oaNotifyService;
	@Autowired
	ActYwService actYwService;
	@Autowired
	ActYwEtAuditNumService actYwEtAuditNumService;
	@Autowired
	ActYwGnodeService actYwGnodeService;

	@Override
	public ActYwGassign get(String id) {
		return super.get(id);
	}

	@Override
	public List<ActYwGassign> findList(ActYwGassign actYwGassign) {
		return super.findList(actYwGassign);
	}

	@Override
	public Page<ActYwGassign> findPage(Page<ActYwGassign> page, ActYwGassign actYwGassign) {
		return super.findPage(page, actYwGassign);
	}

	@Transactional(readOnly = false)
	public void save(ActYwGassign actYwGassign) {
		super.save(actYwGassign);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwGassign actYwGassign) {
		super.delete(actYwGassign);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwGassign actYwGassign) {
  	  dao.deleteWL(actYwGassign);
  	}

	public Boolean isHasAssign(ActYwGassign actYwGassign) {
		Boolean ishas=false;
		List<ActYwGassign> assignList=dao.getListByActYwGassign(actYwGassign);
		if(StringUtil.checkNotEmpty(assignList)){
			ishas=true;
		}
		return ishas;
	}

	@Transactional(readOnly = false)
	public void deleteByAssign(ActYwGassign actYwGassign){
		dao.deleteByAssign(actYwGassign);
	};

	public void sendAssignMsg(String promodelId, ActYwGassign actYwGassign, List<String> userIdList, String proModelName) {
		actYwGassign.setPromodelId(promodelId);
		List<ActYwGassign> assignList=dao.getListByActYwGassign(actYwGassign);
		//查询之前指派的人
		List<String> oldAssignUserList=new ArrayList<String>();
		List<String> newAssignUserList=new ArrayList<String>();
		//旧的指派记录人
		for(ActYwGassign actYwGassignIndex:assignList){
			oldAssignUserList.add(actYwGassignIndex.getRevUserId());
		}
		//新的指派记录人
		for(String userId:userIdList){
			newAssignUserList.add(userId);
		}
		//需要发消息的人
		List<String> otherSendList=new ArrayList<String>();

		for(int j=0;j<oldAssignUserList.size();j++){
			String oldUserId=oldAssignUserList.get(j);
			boolean isHas =false;
			for(String userId:userIdList){
				if(userId.equals(oldUserId)){
					isHas=true;
				}
			}
			//如果旧记录在新记录中不存在 发送指派改变的信息
			if(!isHas){
				otherSendList.add(oldUserId);
			}else{
			//如果旧记录在新记录中存在 不在发送指派信息
				newAssignUserList.remove(oldUserId);
			}
		}
		if(oldAssignUserList.size()>0){
			oaNotifyService.sendOaNotifyByTypeAndUser(CoreUtils.getUser(), otherSendList,"指派任务",
										"管理员将指派给你"+proModelName+"项目审核任务指派给了其他人", OaNotify.Type_Enum.TYPE20.getValue(), promodelId);
		}
		if(newAssignUserList.size()>0) {
			oaNotifyService.sendOaNotifyByTypeAndUser(CoreUtils.getUser(), newAssignUserList, "指派任务",
					"管理员指派给你" + proModelName + "项目审核任务", OaNotify.Type_Enum.TYPE20.getValue(), promodelId);
		}
	}



	public ActYwGnode getGnode(ActYwGassign gassign) {
	    return getGnode(gassign.getId(), gassign.getType());
	}
    public ActYwGnode getGnode(String gnodeId, String type) {
        if(StringUtil.isEmpty(type)){
	        ActYwGnode gnode = null;
	        if(StringUtil.isEmpty(gnodeId)){
	            gnode = actYwGnodeService.get(gnodeId);
	            return gnode;
	        }
	    }
        return null;
    }

	@Transactional(readOnly = false)
	public void insertPl(List<ActYwGassign> insertActYwGassignList) {
		dao.insertPl(insertActYwGassignList);
	}


	public String getToDoNumByUserId(String userId,String gnodeId) {
		TaskQuery todoTaskQuery = taskService.createTaskQuery().active().taskCandidateOrAssigned(userId)
				.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+gnodeId).includeProcessVariables().orderByTaskCreateTime().desc();
		String num=String.valueOf(todoTaskQuery.count());
		return num;
	}

	public void getDelegateToDoNumByGnodeId(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		TaskQuery todoTaskQuery = taskService.createTaskQuery().active()
				.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwEtAssignTaskVo.getGnodeId()).includeProcessVariables().orderByTaskCreateTime().desc();
		if (StringUtils.isNotBlank(actYwEtAssignTaskVo.getActywId())) {
			todoTaskQuery.processVariableValueEquals(ACT_YW_ID, actYwEtAssignTaskVo.getActywId());
		}
		List<String> ids=Lists.newArrayList();
		List<Task> taskList=todoTaskQuery.list();
		for(Task task:taskList){
			String id=(String)task.getProcessVariables().get("id");
			if(StringUtils.isNotEmpty(id)){
				ids.add(id);
			}
		}
		//查询待审核项目
		Long todoNum=todoTaskQuery.count();
		if(todoNum>0){
			actYwEtAssignTaskVo.setProIds(ids);
			//查询已经委派项目
			CompletableFuture<List<String>> hasDeleGateListFuture = CompletableFuture.supplyAsync(() -> dao.getProListHasDelegate(actYwEtAssignTaskVo), ThreadUtils.newFixedThreadPool());
			//查询待审核项目
			CompletableFuture<List<String>> proIdListFuture = CompletableFuture.supplyAsync(() -> dao.getProListTodoDelegate(actYwEtAssignTaskVo), ThreadUtils.newFixedThreadPool());
			CompletableFuture.allOf(hasDeleGateListFuture,proIdListFuture).join();
			List<String> hasDeleGateList = null;
			List<String> proIdList = null;
			try {
				hasDeleGateList = hasDeleGateListFuture.get();
				proIdList = proIdListFuture.get();
			} catch (InterruptedException e) {
				logger.error("查询线程中断,getDelegateToDoNumByGnodeId");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.error("执行异常,getDelegateToDoNumByGnodeId");
				e.printStackTrace();
			}finally {
				ThreadUtils.shutdown();
			}
//			//查询已经委派项目
//			List<String> hasDeleGateList=dao.getProListHasDelegate(actYwEtAssignTaskVo);
//			//查询待审核项目
//			List<String> proIdList=dao.getProListTodoDelegate(actYwEtAssignTaskVo);
			if((todoNum-proIdList.size())<0){
				actYwEtAssignTaskVo.setTodoNum("0");
			}else {
				actYwEtAssignTaskVo.setTodoNum(String.valueOf(todoNum-hasDeleGateList.size()));
			}
			actYwEtAssignTaskVo.setToauditNum(String.valueOf(proIdList.size()));
		}else{
			actYwEtAssignTaskVo.setTodoNum("0");
			actYwEtAssignTaskVo.setToauditNum("0");
		}
	}

	public Long getToDoNumByGnodeId(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		//带指派数据审核人为 assignUser
		TaskQuery todoTaskQuery = taskService.createTaskQuery().active().taskAssignee("assignUser")
				.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwEtAssignTaskVo.getGnodeId()).includeProcessVariables().orderByTaskCreateTime().desc();
		if (StringUtils.isNotBlank(actYwEtAssignTaskVo.getActywId())) {
			todoTaskQuery.processVariableValueEquals(ACT_YW_ID, actYwEtAssignTaskVo.getActywId());
		}
		return todoTaskQuery.count();
	}

	public int getAuditNumByGnodeId(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
//		TaskQuery auditTaskQuery = taskService.createTaskQuery().active()
//				.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwEtAssignTaskVo.getGnodeId())
//				.includeProcessVariables().orderByTaskCreateTime().desc();
//		if (StringUtils.isNotBlank(actYwEtAssignTaskVo.getActywId())) {
//			auditTaskQuery.processVariableValueEquals(ACT_YW_ID, actYwEtAssignTaskVo.getActywId());
//		}
		List<String> proIdList=getProListByActYwEtAssignTaskVo(actYwEtAssignTaskVo);

		return proIdList.size();
	}

	private List<String> getProListByActYwEtAssignTaskVo(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		return dao.getProListByActYwEtAssignTaskVo(actYwEtAssignTaskVo);
	}

	public Long getProToDoNum(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		TaskQuery todoTaskQuery = taskService.createTaskQuery().active();
		if(StringUtil.isNotEmpty(actYwEtAssignTaskVo.getGnodeId())){
			todoTaskQuery.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwEtAssignTaskVo.getGnodeId());
		}
		if(StringUtil.isNotEmpty(actYwEtAssignTaskVo.getUserId())){
			todoTaskQuery.taskAssignee(actYwEtAssignTaskVo.getUserId());
		}
		if (StringUtils.isNotBlank(actYwEtAssignTaskVo.getActywId())) {
			todoTaskQuery.processVariableValueEquals(ACT_YW_ID, actYwEtAssignTaskVo.getActywId());
		}
		todoTaskQuery.includeProcessVariables().orderByTaskCreateTime().desc();
		List<Task> taskList =todoTaskQuery.list();
		if (taskList.size()>0){
			List<String> newList=new ArrayList<String>();
			newList=Lists.transform(taskList, proIndex->proIndex.getProcessInstanceId());
			newList=removeDuplicate(newList);
			return Long.valueOf(newList.size());
		}
		return todoTaskQuery.count();
	}

	public Long getToDoNum(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		TaskQuery todoTaskQuery = taskService.createTaskQuery().active();
		if(StringUtil.isNotEmpty(actYwEtAssignTaskVo.getGnodeId())){
			todoTaskQuery.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwEtAssignTaskVo.getGnodeId());
		}
		if(StringUtil.isNotEmpty(actYwEtAssignTaskVo.getUserId())){
			todoTaskQuery.taskAssignee(actYwEtAssignTaskVo.getUserId());
		}
		if (StringUtils.isNotBlank(actYwEtAssignTaskVo.getActywId())) {
			todoTaskQuery.processVariableValueEquals(ACT_YW_ID, actYwEtAssignTaskVo.getActywId());
		}
		todoTaskQuery.includeProcessVariables().orderByTaskCreateTime().desc();
		return todoTaskQuery.count();
	}

	public   static   List  removeDuplicate(List list)  {
		for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
		  	for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
			   	if  (list.get(j).equals(list.get(i)))  {
				  	list.remove(j);
				}
			}
		}
		return list;
	}

	public String getHasNum(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().finished();
		if(StringUtil.isNotEmpty(actYwEtAssignTaskVo.getGnodeId())){
			histTaskQuery.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwEtAssignTaskVo.getGnodeId());
		}
		if(StringUtil.isNotEmpty(actYwEtAssignTaskVo.getUserId())){
			histTaskQuery.taskAssignee(actYwEtAssignTaskVo.getUserId());
		}
		if (StringUtils.isNotBlank(actYwEtAssignTaskVo.getActywId())) {
			histTaskQuery.processVariableValueEquals(ACT_YW_ID, actYwEtAssignTaskVo.getActywId());
		}
		histTaskQuery.includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		String num=String.valueOf(histTaskQuery.count());
		return num;
	}

	public Long getHasNumByGnodeId(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		HistoricTaskInstanceQuery histTaskQuery = historyService.createHistoricTaskInstanceQuery().finished()
				.taskDefinitionKey(ActYwTool.FLOW_ID_PREFIX+actYwEtAssignTaskVo.getGnodeId()).includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
		if (StringUtils.isNotBlank(actYwEtAssignTaskVo.getActywId())) {
			histTaskQuery.processVariableValueEquals(ACT_YW_ID, actYwEtAssignTaskVo.getActywId());
		}
		return histTaskQuery.count();
	}

	public ActYwGassign getByActYwGassign(ActYwGassign actYwGassign) {
		return dao.getByActYwGassign(actYwGassign);
	}

	public List<String> delegateIds(List<String> gnodeIdList, String actywId) {
		List<String> recordIds = new ArrayList<>();
		for (String gnodeId : gnodeIdList) {
			ActYwGassign actYwGassign=new ActYwGassign();
			actYwGassign.setGnodeId(gnodeId);
			actYwGassign.setYwId(actywId);
			actYwGassign.setRevUserId(UserUtils.getUserId());
			List<String> proList = getProListByActYwGassign(actYwGassign);
			recordIds.addAll(proList);
		}
		return recordIds;
	}

	public List<String> todoDelegateIds(List<String> gnodeIdList, String actywId) {
			List<String> recordIds = new ArrayList<>();
			for (String gnodeId : gnodeIdList) {
				ActYwGassign actYwGassign=new ActYwGassign();
				actYwGassign.setGnodeId(gnodeId);
				actYwGassign.setYwId(actywId);
				actYwGassign.setRevUserId(UserUtils.getUserId());
				actYwGassign.setType(EarAtype.WP.getKey());
				List<String> proList = getTodoProListByActYwGassign(actYwGassign);
				recordIds.addAll(proList);
			}
			return recordIds;
		}

	private List<String> getProListByActYwGassign(ActYwGassign actYwGassign) {
		return dao.getProListByActYwGassign(actYwGassign);
	}

	public List<String> getTodoProListByActYwGassign(ActYwGassign actYwGassign) {
		return dao.getTodoProListByActYwGassign(actYwGassign);
	}

	@Transactional(readOnly = false)
	public void deleteTodo(ActYwGassign actYwGassign) {
		dao.deleteTodo(actYwGassign);
	}

	public List<String> getTodoDelegateList(ActYwGassign actYwGassign) {
		return dao.getTodoDelegateList(actYwGassign);
	}
	public List<ActYwGassign> findListByPro(ActYwGassign entity) {
	    return dao.findListByPro(entity);
	}

	public List<ActYwGassign> getUserDelegateList(ActYwGassign actYwGassign) {
		return dao.getUserDelegateList(actYwGassign);
	}

	public List<String> getProListHasDelegate(ActYwEtAssignTaskVo actYwEtAssignTaskVo) {
		return dao.getProListHasDelegate(actYwEtAssignTaskVo);
	}

    public List<ActYwGassign> getListByActYwGassign(ActYwGassign actYwGassign) {
        return dao.getListByActYwGassign(actYwGassign);
    }
}