package com.oseasy.pro.modules.promodel.utils;

import java.util.*;
import java.util.stream.Collectors;

import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.manager.sub.SytmAct;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.service.ProvinceProModelService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;

import com.oseasy.act.modules.act.utils.ProcessDefUtils;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;

/**
 * 流程定义相关操作的封装
 * @author bluejoe2008@gmail.com
 */
public abstract class ProProcessDefUtils {
	private static ProModelService proModelService = SpringContextHolder.getBean(ProModelService.class);
	private static ProvinceProModelService provinceProModelService = SpringContextHolder.getBean(ProvinceProModelService.class);
	private static ActYwAuditInfoService actYwAuditInfoService = SpringContextHolder.getBean(ActYwAuditInfoService.class);

	public static Map<String, String> getActByPromodelId(String proModelId){
	    Map<String, String> map = new HashMap<>();
	    ProModel proModel = Optional.ofNullable(proModelService.getProModelId(proModelId)).orElseThrow(()->new GroupErrorException("项目不存在"));
	    if(Const.YES.equals(proModel.getState())){
	        String endGnodeId = proModel.getEndGnodeId();
	        if(StringUtils.isNotBlank(endGnodeId)){
	            ActYwGnode endNode = ProcessDefUtils.actYwGnodeService.get(endGnodeId);
	            if(endNode!=null){
	                map.put("taskName", endNode.getName());
	            }
	        }
	        return map;
	    }
	    List<Task> todoList = //taskService.createTaskQuery().taskCandidateOrAssigned(userId).
	            ProcessDefUtils.actTaskService.getTaskQueryByAssigneeOrCandidateUser().
	            processVariableValueEquals("id", proModelId).active().list();
	    if (!todoList.isEmpty()) {
	        Task task = todoList.get(0);
	        map.put("status", "todo");
	        map.put("taskId", task.getId());
	        map.put("taskName", task.getName());
	        map.put(ActYwGroup.JK_GNODE_ID, task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""));
	    }else{
	        todoList = ProcessDefUtils.taskService.createTaskQuery().processVariableValueEquals("id", proModelId).active().list();
	        if (!todoList.isEmpty()) {
	            Task task = todoList.get(0);
	            map.put("taskName", task.getName());
	            map.put(ActYwGroup.JK_GNODE_ID, task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""));
	        }
	    }
	    map.put("proModelId",proModelId);
	    return map;
	}

	/**
	 *  省流程实例各节点状态
	 * @param procId
	 * @return
	 */
	public static Map<String, String> getActByProcId(String procId){
	    Map<String, String> map = new HashMap<>();
		ProvinceProModel provinceProModel = provinceProModelService.get(procId);
		if (provinceProModel == null){
			map.put("status", "");
			map.put("taskId", "");
			map.put("taskName", "");
			map.put(ActYwGroup.JK_GNODE_ID,"");
			return map;
		}
	    if(Const.YES.equals(provinceProModel.getState())){
	        String endGnodeId = provinceProModel.getEndGnodeId();
	        if(StringUtils.isNotBlank(endGnodeId)){
	            ActYwGnode endNode = ProcessDefUtils.actYwGnodeService.get(endGnodeId);
	            if(endNode!=null){
	                map.put("taskName", "项目已结项");
	            }
	        }
	        return map;
	    }

	    List<Task> todoList = //taskService.createTaskQuery().taskCandidateOrAssigned(userId).
	            ProcessDefUtils.actTaskService.getTaskQueryByAssigneeOrCandidateUser().
	            processVariableValueEquals("id", procId).active().list();
	    if (!todoList.isEmpty()) {
	        Task task = todoList.get(0);

	        map.put("status", "todo");
	        map.put("taskId", task.getId());
	        map.put("taskName", task.getName());
	        String gnodeId = task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, "");
			map.put(ActYwGroup.JK_GNODE_ID, gnodeId);
			getRolesOfGnode(map, gnodeId);
		}else{
	        todoList = ProcessDefUtils.taskService.createTaskQuery().processVariableValueEquals("id", procId).active().list();
	        if (!todoList.isEmpty()) {
	            Task task = todoList.get(0);
	            map.put("taskName", task.getName());
				String gnodeId = task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, "");
				map.put(ActYwGroup.JK_GNODE_ID, gnodeId);
				getRolesOfGnode(map, gnodeId);
	        }
	    }
	    map.put("proModelId",procId);
	    return map;
	}

	/**
	 * 获取节点角色
	 * @param map
	 * @param gnodeId
	 */
	private static void getRolesOfGnode(Map<String, String> map, String gnodeId) {
		List<String> names = SytmAct.getRoleNameByGnodeId(gnodeId);
		String role = null;
		if (names != null){
			role = names.stream().collect(Collectors.joining(","));
		}
		map.put("roleNames",role);
	}
}