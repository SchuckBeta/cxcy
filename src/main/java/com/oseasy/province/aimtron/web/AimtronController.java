package com.oseasy.province.aimtron.web;

import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.service.ActYwFormService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwGtimeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: QM
 * @date: 2019/4/17 13:21
 * @description: 国创查询
 */
@RestController
@RequestMapping(value = "${adminPath}/aim/aimtron")
public class AimtronController extends BaseController {

	@Autowired
	private ActYwGnodeService actYwGnodeService;


	@Autowired
	private ActYwFormService actYwFormService;
	@Autowired
	private ActYwService actYwService;

	@Autowired
	private ProModelService proModelService;


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

	/**
	 * 项目列表
	 *
	 * @param ywid
	 */
	@RequestMapping(value = "/ajaxGetFirstTaskByYwid/{ywid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public void getProjects(@PathVariable("ywid") String ywid) {
		ActYwGnode task = actYwGnodeService.getFirstTaskByYwid(ywid);
//		if(task != null){
//			return new Rtstatus<ActYwGnode>(true, "查询成功！", task);
//		}
//		return new Rtstatus<ActYwGnode>(false, "查询失败或结果为空！")

	}


	@RequestMapping(value = "/ajaxAimtrons", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public void ajaxProjects(HttpServletRequest request, HttpServletResponse response) {

	}


	@RequestMapping(value = "/viewPromodel", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public String viewPromodel(String actywId, Model model, HttpServletRequest request, HttpServletResponse response) {
		ActYw actYw = actYwService.get(actywId);
		ActYwGroup actYwGroup = actYw.getGroup();
		String flowType = actYwGroup.getFlowType();
		String type = FlowType.getByKey(flowType).getType().getKey();
		Map<String, Object> map = new HashMap<>(16);
		map.put(ActYwGroup.JK_ACTYW_ID, actywId);
		model.addAllAttributes(map);
		return "template/form/" + type + "/common_taskAssignList";
	}

	/**
	 * 打分
	 *
	 * @param proModelId 项目Id
	 * @param score      分数
	 */
	@RequestMapping(value = "/scoreOfProModel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public void scoreOfProModel(String proModelId, Integer score) {

	}

	/**
	 * 审核
	 *
	 * @param proModelId 项目Id
	 * @param state      省平台项目状态: 0：推送错误，1：推荐省级:2：省级:3：推荐国家级
	 */
	@RequestMapping(value = "/checkOfProModel", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public void checkOfProModel(String proModelId, Integer state) {

	}


	/**
	 * 下发资金
	 *
	 * @param proModelId 项目Id
	 * @param capital    资金
	 */
	@RequestMapping(value = "/issuedOfcapital", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public void issuedOfcapital(String proModelId, BigDecimal capital) {

	}

	/**
	 * 推送，先放这里
	 *
	 * @param ids   项目ids
	 * @param state 推送状态
	 */
	@RequestMapping(value = "/push", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public void push(String ids, Integer state) {

	}

	/**
	@RequestMapping(value ="/proModelView", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
	public String proModelView(String actywId,Model model){
		ActYw actYw = actYwService.get(actywId);
		ActYwGroup actYwGroup = actYw.getGroup();
		String flowType = actYwGroup.getFlowType();
		String type = FlowType.getByKey(flowType).getType().getKey();
		Map<String,Object> map = new HashMap<>(16);
		map.put(ActYwGroup.JK_ACTYW_ID, actywId);
		model.addAllAttributes(map);
		return "template/form/" + type + "/common_taskAssignList";
	}*/


	// 参考ProCmsController.java
	@RequestMapping(value = "form/{template}/{pageName}")
	public String modelForm(@PathVariable String pageName, HttpServletRequest request, HttpServletResponse response, Model model) {
		ActYwForm actYwForm = actYwFormService.get(pageName);
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
		Page page = null;
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
