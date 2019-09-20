/**
 *
 */
package com.oseasy.pro.modules.promodel.web;

import com.alibaba.fastjson.JSONObject;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.service.ActYwFormService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.pro.entity.ProProject;

import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容管理Controller
 *
 * @version 2013-4-21
 */
@Controller
@RequestMapping(value = "${adminPath}/cms")
public class ProCmsController extends BaseController {
	@Autowired
	private ActYwFormService actYwFormService;
	@Autowired
	private ActYwService actYwService;
	@Autowired
	private ActYwGnodeService actYwGnodeService;
	@Autowired
	private ProModelService proModelService;
	@Autowired
	private CoreService coreService;
    @ModelAttribute
    public Object setModel(@RequestParam String actywId) {
        ActYw actYw = actYwService.get(actywId);

        IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(actYw);
        if (StringUtils.isEmpty(workFlow)) {
        	return new ProModel();
		}
        Class clazz = workFlow.getTClass();
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } catch (IllegalAccessException e) {
			logger.error(ExceptionUtil.getStackTrace(e));
        }
        return new Object();
    }


	@RequestMapping(value = "form/{template}/{pageName}")
	public String modelForm(@PathVariable String pageName, HttpServletRequest request, HttpServletResponse response, Model model) {

    	ActYwForm actYwForm = actYwFormService.getActYwForm(pageName,TenantConfig.getCacheTenant());
		if ((actYwForm == null) || StringUtils.isEmpty(actYwForm.getPath())) {
		    return CorePages.ERROR_MISS.getIdxUrl();
		}
        String gnodeId = request.getParameter(ActYwGroup.JK_GNODE_ID);
        String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
        model.addAttribute(ProModel.JK_PROMODEL, proModelService.gen(request, gnodeId, actywId));
 		ActYw actYw = actYwService.get(actywId);
		//根据匹配传页面需要参数，数据
		ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
		if (actYwGnode != null) {
			model.addAttribute("menuName", actYwGnode.getName());
		}
		if (UserUtils.isAdminOrSuperAdmin(UserUtils.getUser())) {
			model.addAttribute("isAdmin", "1");
		}

		String actionUrl = pageName;
		model.addAttribute("actionUrl", actionUrl);
		model.addAttribute(ActYwGroup.JK_GNODE_ID, gnodeId);
		model.addAttribute(ActYwGroup.JK_ACTYW_ID, actywId);
		model.addAttribute(ActYwGroup.JK_GROUP_ID, actYw.getGroupId());
		if (actYw == null) {
            return CorePages.ERROR_MISS.getIdxUrl();
		}

		ProProject proProject = actYw.getProProject();
		model.addAttribute(ProProject.PRO_PROJECT, proProject);
		if (proProject != null) {
			//后台审核结果
			showBankMessage(proProject, model);
		}
		String key = ActYw.getPkey(actYw);
		Act act = new Act();
		act.setProcDefKey(key);  //流程标识
		ActYwGtime actYwGtime = new ActYwGtime();
		actYwGtime.setGnodeId(gnodeId);
		actYwGtime.setGrounpId(actYw.getGroupId());
		actYwGtime.setProjectId(actYw.getRelId());
		Page page =null;
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
        Map parameters = request.getParameterMap();
		if (StringUtils.isEmpty(workFlow)) {
			page = proModelService.findDataPage(new Page(request, response), model, actywId, gnodeId, actYw, act, (ProModel) WorkFlowUtil.getParameters(parameters, workFlow));
		} else {
			page = workFlow.findDataPage(new Page(request, response), model, actywId, gnodeId, actYw, act, WorkFlowUtil.getParameters(parameters, workFlow));
		}
		model.addAttribute("page", page);
		return actYwForm.getPath();
	}


	// ajax 请求 返回列表參數
   	@RequestMapping(value = "ajax/listForm")
   	@ResponseBody
	public JSONObject ajaxModelForm(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
		String gnodeId = request.getParameter(ActYwGroup.JK_GNODE_ID);
		String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
		ActYw actYw = actYwService.get(actywId);

		String key = ActYw.getPkey(actYw);
		Act act = new Act();
		act.setProcDefKey(key);  //流程标识
		ActYwGtime actYwGtime = new ActYwGtime();
		actYwGtime.setGnodeId(gnodeId);
		actYwGtime.setGrounpId(actYw.getGroupId());
		actYwGtime.setProjectId(actYw.getRelId());
		Page page =null;
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
		Map parameters = request.getParameterMap();
		if (StringUtils.isEmpty(workFlow)) {
			page = proModelService.findDataPage(new Page(request, response), null, actywId, gnodeId, actYw, act, (ProModel) WorkFlowUtil.getParameters(parameters, workFlow));
		} else {
			page = workFlow.findDataPage(new Page(request, response), null, actywId, gnodeId, actYw, act, WorkFlowUtil.getParameters(parameters, workFlow));
		}
		js.put("page",page);
		return js;
	}

	@RequestMapping(value = "form/{template}/")
    /*	@Log(operationType="访问类别",operationName="test")*/
	public String modelFormMiss(@PathVariable String template,
								HttpServletRequest request, HttpServletResponse response, Model model) {
	    return CorePages.ERROR_MISS.getIdxUrl();
	}

	@RequestMapping(value = "form/queryMenuList")
    /*	@Log(operationType="访问类别",operationName="test")*/
	public String queryMenuList(Page<ProModel> page, HttpServletRequest request, HttpServletResponse response, Model model) {
		String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
		String cacheKey = ActSval.ck.cks(ActSval.ActEmskey.ACTYW, TenantConfig.getCacheTenant())+actywId;
		ActYw actYw = actYwService.get(actywId);
        Map parameters = request.getParameterMap();
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
		ActYwGroup actYwGroup = actYw.getGroup();
		model.addAttribute("groupId", actYw.getGroupId());
		String flowType = actYwGroup.getFlowType();
		String type = FlowType.getByKey(flowType).getType().getKey();

		if (UserUtils.isAdminOrSuperAdmin(UserUtils.getUser())) {
			model.addAttribute("isAdmin", "1");
		}

		Page result = null;
		if (StringUtils.isEmpty(workFlow)) {
			result = proModelService.findQueryPage(new Page<>(request, response), model, actywId, actYw, (ProModel) WorkFlowUtil.getParameters(parameters, workFlow));
		} else {
			result = workFlow.findQueryPage(new Page<>(request, response), model, actywId, actYw, WorkFlowUtil.getParameters(parameters, workFlow));
		}
		model.addAttribute("page", result);
		model.addAttribute("menuName", "项目查询");
		model.addAttribute(ActYwGroup.JK_ACTYW_ID, actywId);

		if (StringUtil.isNotEmpty(actYw.getKeyType())) {
			String path = "template/form/" + type + "/" + actYw.getKeyType() + "queryList";
			System.out.println(path);
			return "template/form/" + type + "/" + actYw.getKeyType() + "queryList";
		}
		return "template/form/" + type + "/common_queryList";
	}


	@RequestMapping(value = "ajax/ajaxQueryMenuList")
	@ResponseBody
    /*	@Log(operationType="访问类别",operationName="test")*/
	public JSONObject ajaxQueryMenuList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
		String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
		ActYw actYw = actYwService.get(actywId);
        Map parameters = request.getParameterMap();

		//需在pro_project表中新增serviceType字段记录当前流程对应后台的service
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);

//		ActYwGroup actYwGroup = actYw.getGroup();
		js.put("groupId", actYw.getGroupId());
		if (UserUtils.isAdminOrSuperAdmin(UserUtils.getUser())) {
			js.put("isAdmin", "1");
		}

		Page result = null;
		if (StringUtils.isEmpty(workFlow)) {
			result = proModelService.findQueryPage(new Page<>(request, response), null, actywId, actYw, (ProModel) WorkFlowUtil.getParameters(parameters, workFlow));
		} else {
			result = workFlow.findQueryPage(new Page<>(request, response), null, actywId, actYw, WorkFlowUtil.getParameters(parameters, workFlow));
		}
		js.put("page", result);
		js.put("menuName", "项目查询");
		js.put(ActYwGroup.JK_ACTYW_ID, actywId);
		return js;
	}

	//任务指派列表
	@RequestMapping(value = "form/taskAssignList")
	public String taskAssignList(Page<ProModel> page, HttpServletRequest request, HttpServletResponse response, Model model) {

		String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
		Map parameters = request.getParameterMap();
		ActYw actYw = actYwService.get(actywId);
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);

		ActYwGroup actYwGroup = actYw.getGroup();
		model.addAttribute("groupId", actYw.getGroupId());

		String flowType = actYwGroup.getFlowType();
		String type = FlowType.getByKey(flowType).getType().getKey();
		Page result = null;
		if (StringUtils.isEmpty(workFlow)) {
			result = proModelService.findAssignPage(new Page<>(request, response), model, actywId, actYw, (ProModel) WorkFlowUtil.getParameters(parameters, workFlow));
		} else {
			result = workFlow.findAssignPage(new Page<>(request, response), model, actywId, actYw, WorkFlowUtil.getParameters(parameters, workFlow));
		}

		model.addAttribute("page", result);
		model.addAttribute("menuName", "专家任务指派");

		model.addAttribute(ActYwGroup.JK_ACTYW_ID, actywId);
		if (StringUtil.isNotEmpty(actYw.getKeyType())) {
			return "template/form/" + type + "/" + actYw.getKeyType() + "taskAssignList";
		}
		return "template/form/" + type + "/common_taskAssignList";
	}

	//任务指派列表
	@RequestMapping(value = "form/ajaxTaskAssignList")
	@ResponseBody
	public JSONObject ajaxTaskAssignList(Page page,HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
		String actywId = request.getParameter(ActYwGroup.JK_ACTYW_ID);
		Map parameters = request.getParameterMap();
		ActYw actYw = actYwService.get(actywId);
		String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
		IWorkFlow workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
		ActYwGroup actYwGroup = actYw.getGroup();
		js.put("groupId", actYw.getGroupId());
		Page result = workFlow.findAssignPage(page, null, actywId, actYw, WorkFlowUtil.getParameters(parameters, workFlow));
		js.put("page", result);
		js.put("menuName", "项目指派");
		js.put(ActYwGroup.JK_ACTYW_ID, actywId);
		return js;
	}


	private void showBankMessage(ProProject proProject, Model model) {
		List<Dict> finalStatusMap = new ArrayList<Dict>();
		if (proProject.getFinalStatus() != null) {
			String finalStatus = proProject.getFinalStatus();
			if (finalStatus != null) {
				String[] finalStatuss = finalStatus.split(",");
				if (finalStatuss.length > 0) {
					for (int i = 0; i < finalStatuss.length; i++) {
						Dict dict = new Dict();
						dict.setValue(finalStatuss[i]);
						if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(finalStatuss[i], "competition_college_prise", ""));
						} else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(finalStatuss[i], "project_result", ""));
						}
						finalStatusMap.add(dict);
					}
				}
				model.addAttribute("finalStatusMap", finalStatusMap);
			}
		}
		//前台项目类型
		List<Dict> proTypeMap = new ArrayList<Dict>();
		if (proProject.getType() != null) {
			String proType = proProject.getType();
			if (proType != null) {
				String[] proTypes = proType.split(",");
				if (proTypes.length > 0) {
					for (int i = 0; i < proTypes.length; i++) {
						Dict dict = new Dict();

						dict.setValue(proTypes[i]);
						if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(proTypes[i], "competition_type", ""));
						} else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
							dict.setLabel(DictUtils.getDictLabel(proTypes[i], "project_style", ""));
						}
						//proCategoryMap.add(map);
						proTypeMap.add(dict);

					}
				}
				model.addAttribute("proTypeMap", proTypeMap);
			}
		}
		//前台项目类别
		/*List<Map<Dict>> proCategoryMap=new ArrayList<Map<String, String>>();*/
		List<Dict> proCategoryMap = new ArrayList<Dict>();
		if (proProject.getProCategory() != null) {
			String proCategory = proProject.getProCategory();
			if (proCategory != null) {
				String[] proCategorys = proCategory.split(",");
				if (proCategorys.length > 0) {
					for (int i = 0; i < proCategorys.length; i++) {
						Map map = new HashMap<String, String>();
						Dict dict = new Dict();
						map.put("value", proCategorys[i]);
						dict.setValue(proCategorys[i]);
						if (proProject.getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
							map.put("label", DictUtils.getDictLabel(proCategorys[i], "competition_net_type", ""));
							dict.setLabel(DictUtils.getDictLabel(proCategorys[i], "competition_net_type", ""));
						} else if (proProject.getProType().contains(FlowProjectType.PMT_XM.getKey())) {
							map.put("label", DictUtils.getDictLabel(proCategorys[i], "project_type", ""));
							dict.setLabel(DictUtils.getDictLabel(proCategorys[i], "project_type", ""));
						}
						//proCategoryMap.add(map);
						proCategoryMap.add(dict);
					}
				}
				model.addAttribute("proCategoryMap", proCategoryMap);
			}
		}
		//前台项目类别
		List<Dict> prolevelMap = new ArrayList<Dict>();
		if (proProject.getLevel() != null) {
			String proLevel = proProject.getLevel();
			if (proLevel != null) {
				String[] proLevels = proLevel.split(",");
				if (proLevels.length > 0) {
					for (int i = 0; i < proLevels.length; i++) {
						Dict dict = new Dict();
						dict.setValue(proLevels[i]);
						dict.setLabel(DictUtils.getDictLabel(proLevels[i], "competition_format", ""));
						prolevelMap.add(dict);
					}
				}
				model.addAttribute("prolevelMap", prolevelMap);
			}
		}
	}
}
