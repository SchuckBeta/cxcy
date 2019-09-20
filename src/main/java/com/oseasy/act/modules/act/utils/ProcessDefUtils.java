package com.oseasy.act.modules.act.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.tool.apply.AmapStatus;
import com.oseasy.act.modules.actyw.tool.apply.AmapVstatus;
import com.oseasy.act.modules.actyw.tool.apply.IAengine;
import com.oseasy.act.modules.actyw.tool.apply.IAmap;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程定义相关操作的封装
 * @author bluejoe2008@gmail.com
 */
public abstract class ProcessDefUtils {
    public static TaskService taskService = SpringContextHolder.getBean(TaskService.class);
    public static ActTaskService actTaskService = SpringContextHolder.getBean(ActTaskService.class);
    public static ActYwGnodeService actYwGnodeService = SpringContextHolder.getBean(ActYwGnodeService.class);

	public static ActivityImpl getActivity(ProcessEngine processEngine, String processDefId, String activityId) {
		ProcessDefinitionEntity pde = getProcessDefinition(processEngine, processDefId);
		return (ActivityImpl) pde.findActivity(activityId);
	}

	public static ProcessDefinitionEntity getProcessDefinition(ProcessEngine processEngine, String processDefId) {
		return (ProcessDefinitionEntity) ((RepositoryServiceImpl) processEngine.getRepositoryService()).getDeployedProcessDefinition(processDefId);
	}

	public static void grantPermission(ActivityImpl activity, String assigneeExpression, String candidateGroupIdExpressions,
			String candidateUserIdExpressions) throws Exception {
		TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
		taskDefinition.setAssigneeExpression(assigneeExpression == null ? null : new FixedValue(assigneeExpression));
		FieldUtils.writeField(taskDefinition, "candidateUserIdExpressions", ExpressionUtils.stringToExpressionSet(candidateUserIdExpressions), true);
		FieldUtils.writeField(taskDefinition, "candidateGroupIdExpressions", ExpressionUtils.stringToExpressionSet(candidateGroupIdExpressions), true);

		Logger.getLogger(ProcessDefUtils.class).info(
				String.format("granting previledges for [%s, %s, %s] on [%s, %s]", assigneeExpression, candidateGroupIdExpressions,
						candidateUserIdExpressions, activity.getProcessDefinition().getKey(), activity.getProperty("name")));
	}

	/**
	 * 实现常见类型的expression的包装和转换
	 *
	 * @author bluejoe2008@gmail.com
	 *
	 */
	public static class ExpressionUtils {
		public static Expression stringToExpression(ProcessEngineConfigurationImpl conf, String expr) {
			return conf.getExpressionManager().createExpression(expr);
		}

		public static Expression stringToExpression(String expr) {
			return new FixedValue(expr);
		}

		public static Set<Expression> stringToExpressionSet(String exprs) {
			Set<Expression> set = new LinkedHashSet<Expression>();
			for (String expr : exprs.split(";")) {
				set.add(stringToExpression(expr));
			}

			return set;
		}
	}

	public static IAmap initIamap(IAengine engine, IApply apply){
	    IGnode endNode = null;
	    if(StringUtils.isEmpty(apply.iaprop().iendgnid())){
            endNode = engine.gnser().get(apply.iaprop().iendgnid());
        }

	    if((apply == null) || StringUtil.isEmpty(apply.getIid())){
	        IAmap iamap = new IAmap();
	        iamap.setIastatus(AmapStatus.NONE.getId());
	        iamap.setIvstatus(AmapVstatus.VIEW.getId());
            if(endNode != null){
                iamap.setItaskname(endNode.name());
                iamap.setIgnodeid(endNode.id());
            }
	        return iamap;
	    }

		if((apply == null) || apply.iaprop().iend()){
            apply.getIamap().setIastatus(AmapStatus.END.getId());
            apply.getIamap().setIvstatus(AmapVstatus.VIEW.getId());
			if(endNode != null){
				apply.getIamap().setItaskname(endNode.name());
	            apply.getIamap().setIgnodeid(endNode.id());
			}
			return apply.getIamap();
		}

		/**
		 * 查询当前用户有权限查看的数据,如果查询结果为空，则查询的数据只读，无编辑权限.
		 */
		List<Task> todoList = actTaskService.getTaskQueryByAssigneeOrCandidateUser().processVariableValueEquals(CoreJkey.JK_ID, engine.apply().getIid()).active().list();
		if (StringUtil.checkOne(todoList)) {
			Task task = todoList.get(0);
            apply.getIamap().setIastatus(AmapStatus.TODO.getId());
            apply.getIamap().setIvstatus(AmapVstatus.EDIT_VIEW.getId());
			apply.getIamap().setItaskid(task.getId());
			apply.getIamap().setItaskname(task.getName());
			apply.getIamap().setIgnodeid(task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, StringUtils.EMPTY));
		}else{
			todoList = taskService.createTaskQuery().processVariableValueEquals(CoreJkey.JK_ID, engine.apply().getIid()).active().list();
			if (StringUtil.checkOne(todoList)) {
	            Task task = todoList.get(0);
	            apply.getIamap().setIastatus(AmapStatus.TODO.getId());
	            apply.getIamap().setIvstatus(AmapVstatus.VIEW.getId());
	            apply.getIamap().setItaskid(task.getId());
	            apply.getIamap().setItaskname(task.getName());
	            apply.getIamap().setIgnodeid(task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, StringUtils.EMPTY));
	        }else{
	            apply.getIamap().setIastatus(AmapStatus.NONE.getId());
                apply.getIamap().setIvstatus(AmapVstatus.VIEW.getId());
	        }
		}

        return apply.getIamap();
	}

	public static ActYwGnode getActYwGnode(String gnodeId){
		return actYwGnodeService.get(gnodeId);
	}
}