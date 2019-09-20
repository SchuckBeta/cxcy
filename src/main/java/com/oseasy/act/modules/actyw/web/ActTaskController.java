/**
 *
 */
package com.oseasy.act.modules.actyw.web;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.utils.ActUtils;
import com.oseasy.act.modules.act.utils.ProcessMapUtil;
import com.oseasy.act.modules.act.vo.ProcessMapVo;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程个人任务相关Controller


 */
@Controller
@RequestMapping(value = "${adminPath}/act/task")
public class ActTaskController extends BaseController {
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;

	/**
	 * 获取待办列表
	 * @param //procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = {"todo", ""})
	public String todoList(Act act, HttpServletResponse response, Model model) throws Exception {
		List<Act> list = actTaskService.todoList(act);
		model.addAttribute("list", list);
		if (CoreUtils.getPrincipal().isMobileLogin()) {
			return renderString(response, list);
		}
		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskTodoList";
	}

	/**
	 * 获取已办任务
	 * @param //page
	 * @param //procDefKey 流程定义标识
	 * @return
	 */
	@RequestMapping(value = "historic")
	public String historicList(Act act, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<Act> page = new Page<Act>(request, response);
		page = actTaskService.historicList(page, act);
		model.addAttribute(Page.PAGE, page);
		if (CoreUtils.getPrincipal().isMobileLogin()) {
			return renderString(response, page);
		}
		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskHistoricList";
	}

	/**
	 * 获取流转历史列表
	 * @param //procInsId 流程实例
	 * @param startAct 开始活动节点名称
	 * @param endAct 结束活动节点名称
	 */
	@RequestMapping(value = "histoicFlow")
	public String histoicFlow(Act act, String startAct, String endAct, Model model) {
		if (StringUtils.isNotBlank(act.getProcInsId())) {
			List<Act> histoicFlowList = actTaskService.histoicFlowList(act.getProcInsId(), startAct, endAct);
			model.addAttribute("histoicFlowList", histoicFlowList);
		}
		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskHistoricFlow";
	}

	/**
	 * 获取流程列表
	 * @param category 流程分类
	 */
	@RequestMapping(value = "process")
	public String processList(String category, HttpServletRequest request, HttpServletResponse response, Model model) {
	    Page<Object[]> page = new Page<Object[]>(request, response);
	    page = actTaskService.processList(page, category);
		model.addAttribute(Page.PAGE, page);
		model.addAttribute("category", category);
		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskProcessList";
	}

	/**
	 * 获取流程表单
	 * @param //taskId	任务ID
	 * @param //taskName	任务名称
	 * @param //taskDefKey 任务环节标识
	 * @param //procInsId 流程实例ID
	 * @param //procDefId 流程定义ID
	 */
	@RequestMapping(value = "form")
	public String form(Act act, HttpServletRequest request, Model model) {

		// 获取流程XML上的表单KEY
		String formKey = actTaskService.getFormKey(act.getProcDefId(), act.getTaskDefKey());

		// 获取流程实例对象
		if (act.getProcInsId() != null) {
			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
		}
		String url=ActUtils.getFormUrl(formKey, act);
		System.out.println(url);

		return CoreSval.REDIRECT + url;

//		// 传递参数到视图
//		model.addAttribute("act", act);
//		model.addAttribute("formUrl", formUrl);
//		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskForm";
	}

	@RequestMapping(value = "auditform")
	public String auditform(Act act, HttpServletRequest request, Model model) {
		// 获取流程XML上的表单KEY
		String formKey= "/promodel/proModel/auditForm";
//		String	urlPath=request.getParameter("path");
		String	pathUrl=request.getParameter("pathUrl");
		String	gnodeId=request.getParameter(ActYwGroup.JK_GNODE_ID);
		String	taskName=request.getParameter("taskName");
		if (StringUtils.isNotBlank(taskName)) {
			try {
				taskName = URLEncoder.encode(taskName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				//DO nothing
			}
		}
		String	proModelId=request.getParameter("proModelId");
		// 获取流程实例对象
		if (act.getProcInsId() != null) {
			act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
		}
		String url="";
		if (proModelId!=null) {
			StringBuilder formUrl = new StringBuilder();

			String formServerUrl = CoreSval.getConfig("activiti.form.server.url");
			if (StringUtil.isBlank(formServerUrl)) {
				formUrl.append(CoreSval.getAdminPath());
			}else{
				formUrl.append(formServerUrl);
			}

			url=formUrl.toString()+formKey+"?actionPath="+pathUrl+"&gnodeId="+gnodeId+"&proModelId="+proModelId + "&taskName=" + taskName;
		}else{
			url=ActUtils.getFormUrl(formKey, act);
			url=url+"&actionPath="+pathUrl+"&gnodeId="+gnodeId + "&taskName=" + taskName;
		}
		return CoreSval.REDIRECT + url;
	}

	/**
	 * 启动流程
	 * @param //procDefKey 流程定义KEY
	 * @param //businessTable 业务表表名
	 * @param //businessId	业务表编号
	 */
	@RequestMapping(value = "start")
	@ResponseBody
	public String start(Act act, String table, String id, Model model) throws Exception {
		actTaskService.startProcess(act.getProcDefKey(), act.getBusinessId(), act.getBusinessTable(), act.getTitle());
		return "true";//adminPath + "/act/task";
	}

	/**
	 * 签收任务
	 * @param //taskId 任务ID
	 */
	@RequestMapping(value = "claim")
	@ResponseBody
	public String claim(Act act) {
		String userId = UserUtils.getUser().getId();//ObjectUtils.toString(UserUtils.getUser().getId());
		actTaskService.claim(act.getTaskId(), userId);
		return "true";//adminPath + "/act/task";
	}

	/**
	 * 完成任务
	 * @param //taskId 任务ID
	 * @param //procInsId 流程实例ID，如果为空，则不保存任务提交意见
	 * @param //comment 任务提交意见的内容
	 * @param //vars 任务流程变量，如下
	 * 		vars.keys=flag,pass
	 * 		vars.values=1,true
	 * 		vars.types=S,B  @see com.oseasy.act.modules.act.utils.PropertyType
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	public String complete(Act act) {
		actTaskService.complete(act.getTaskId(), act.getProcInsId(), act.getComment(), act.getVars().getVariableMap());
		return "true";//adminPath + "/act/task";
	}

	/**
	 * 读取带跟踪的图片
	 */
	@RequestMapping(value = "trace/photo/{procDefId}/{execId}")
	public void tracePhoto(@PathVariable("procDefId") String procDefId, @PathVariable("execId") String execId, HttpServletResponse response) throws Exception {
		InputStream imageStream = actTaskService.tracePhoto(procDefId, execId);

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 输出跟踪流程信息
	 *
	 * @param proInsId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "trace/info/{proInsId}")
	public List<Map<String, Object>> traceInfo(@PathVariable("proInsId") String proInsId) throws Exception {
		List<Map<String, Object>> activityInfos = actTaskService.traceProcess(proInsId);
		return activityInfos;
	}

	//显示流程图

	@RequestMapping(value = "processPic")
	public void processPic(String procDefId, HttpServletResponse response) throws Exception {
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
		String diagramResourceName = procDef.getDiagramResourceName();
		InputStream imageStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), diagramResourceName);
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	//获取跟踪信息
	@RequestMapping(value = "processMapByType")
	public String processMap(String procDefId, String proInstId, String type, String status, Model model) throws Exception {
    ProcessMapVo vo = ProcessMapUtil.processMap(repositoryService, actTaskService, runtimeService, proInstId, type, status);
    model.addAttribute(ProcessMapVo.PM_PROC_DEF_ID, vo.getProcDefId());
    model.addAttribute(ProcessMapVo.PM_PRO_INST_ID, vo.getProInstId());
    model.addAttribute(ProcessMapVo.PM_ACT_IMPLS, vo.getActImpls());
    return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskMap";
	}

	//获取跟踪信息
	@RequestMapping(value = "processActMap")
	public String processActMap(String proInstId, String type, String status, Model model) throws Exception {
		ProcessMapVo vo = ProcessMapUtil.processMap(repositoryService, actTaskService, runtimeService, proInstId, type, status);
		model.addAttribute(ProcessMapVo.PM_PROC_DEF_ID, vo.getProcDefId());
		model.addAttribute(ProcessMapVo.PM_PRO_INST_ID, vo.getProInstId());
		model.addAttribute(ProcessMapVo.PM_ACT_IMPLS, vo.getActImpls());
		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskMap";
	}

  //获取跟踪信息
  @RequestMapping(value = "processMap")
  public String processMap(String procDefId, String proInstId, Model model) throws Exception {
	    ProcessMapVo vo = ProcessMapUtil.processMap(repositoryService, actTaskService, runtimeService, proInstId, null, null);
	    model.addAttribute(ProcessMapVo.PM_PROC_DEF_ID, vo.getProcDefId());
	    model.addAttribute(ProcessMapVo.PM_PRO_INST_ID, vo.getProInstId());
	    model.addAttribute(ProcessMapVo.PM_ACT_IMPLS, vo.getActImpls());

//		List<ActivityImpl> actImpls = new ArrayList<ActivityImpl>();
//		ProcessDefinition processDefinition = repositoryService
//				.createProcessDefinitionQuery().processDefinitionId(procDefId)
//				.singleResult();
//		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
//		String processDefinitionId = pdImpl.getId();// 流程标识
//		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//				.getDeployedProcessDefinition(processDefinitionId);
//		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
//		List<String> activeActivityIds = runtimeService.getActiveActivityIds(proInstId);
//		for (String activeId : activeActivityIds) {
//			for (ActivityImpl activityImpl : activitiList) {
//				String id = activityImpl.getId();
//				if (activityImpl.isScope()) {
//					if (activityImpl.getActivities().size() > 1) {
//						List<ActivityImpl> subAcList = activityImpl
//								.getActivities();
//						for (ActivityImpl subActImpl : subAcList) {
//							String subid = subActImpl.getId();
//							System.out.println("subImpl:" + subid);
//							if (activeId.equals(subid)) {// 获得执行到那个节点
//								actImpls.add(subActImpl);
//								break;
//							}
//						}
//					}
//				}
//				if (activeId.equals(id)) {// 获得执行到那个节点
//					actImpls.add(activityImpl);
//					System.out.println(id);
//				}
//			}
//		}
//		model.addAttribute("procDefId", procDefId);
//		model.addAttribute("proInstId", proInstId);
//		model.addAttribute("actImpls", actImpls);
		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskMap";
	}


	//获取跟踪信息
	@RequestMapping(value = "processMap2")
	public String processMap2(String proInsId, Model model) throws Exception {
    ProcessMapVo vo = ProcessMapUtil.processMap(repositoryService, actTaskService, runtimeService, proInsId, null, null);
    model.addAttribute(ProcessMapVo.PM_PROC_DEF_ID, vo.getProcDefId());
    model.addAttribute(ProcessMapVo.PM_PRO_INST_ID, vo.getProInstId());
    model.addAttribute(ProcessMapVo.PM_ACT_IMPLS, vo.getActImpls());

//		String procDefId=actTaskService.getProcessDefinitionIdByProInstId(proInsId);
//		List<ActivityImpl> actImpls = new ArrayList<ActivityImpl>();
//		ProcessDefinition processDefinition = repositoryService
//				.createProcessDefinitionQuery().processDefinitionId(procDefId)
//				.singleResult();
//		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
//		String processDefinitionId = pdImpl.getId();// 流程标识
//		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//				.getDeployedProcessDefinition(processDefinitionId);
//		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
//		List<String> activeActivityIds = runtimeService.getActiveActivityIds(proInsId);
//		for (String activeId : activeActivityIds) {
//			for (ActivityImpl activityImpl : activitiList) {
//				String id = activityImpl.getId();
//				if (activityImpl.isScope()) {
//					if (activityImpl.getActivities().size() > 1) {
//						List<ActivityImpl> subAcList = activityImpl
//								.getActivities();
//						for (ActivityImpl subActImpl : subAcList) {
//							String subid = subActImpl.getId();
//							System.out.println("subImpl:" + subid);
//							if (activeId.equals(subid)) {// 获得执行到那个节点
//								actImpls.add(subActImpl);
//								break;
//							}
//						}
//					}
//				}
//				if (activeId.equals(id)) {// 获得执行到那个节点
//					actImpls.add(activityImpl);
//					System.out.println(id);
//				}
//			}
//		}
//		model.addAttribute("procDefId", procDefId);
//		model.addAttribute("proInstId", proInsId);
//		model.addAttribute("actImpls", actImpls);
		return ActSval.path.vms(ActEmskey.ACT.k()) + "actTaskMap";
	}


	/**
	 * 删除任务
	 * @param taskId 流程实例ID
	 * @param reason 删除原因
	 */
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "deleteTask")
	public String deleteTask(String taskId, String reason, RedirectAttributes redirectAttributes) {
		if (StringUtils.isBlank(reason)) {
			addMessage(redirectAttributes, "请填写删除原因");
		}else{
			actTaskService.deleteTask(taskId, reason);
			addMessage(redirectAttributes, "删除任务成功，任务ID=" + taskId);
		}
		return CoreSval.REDIRECT + adminPath + "/act/task";
	}
}
