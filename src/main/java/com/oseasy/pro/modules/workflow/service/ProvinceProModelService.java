package com.oseasy.pro.modules.workflow.service;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.utils.ProcessDefUtils;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.service.*;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.*;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.jobs.pro.ProModelTopic;
import com.oseasy.pro.modules.cert.dao.SysCertInsDao;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;
import com.oseasy.pro.modules.project.dao.ProjectPlanDao;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProRole;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FormPage;
import com.oseasy.pro.modules.promodel.utils.ProProcessDefUtils;
import com.oseasy.pro.modules.promodel.vo.ExpGnodeFile;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.WorkFlowService;
import com.oseasy.pro.modules.workflow.dao.ProvinceProModelDao;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.handler.DataExpVoHandler;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.utils.NumRuleUtils;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import net.sf.json.JSONObject;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 华师大创模板Service.
 * @author zy
 * @version 2018-06-05
 */
@Service
@Transactional(readOnly = false)
public class ProvinceProModelService extends WorkFlowService<ProvinceProModelDao, ProvinceProModel, ExpProModelVo> implements IWorkFlow<ProvinceProModel, ProModel, ExpProModelVo> {

    public final static Logger logger = Logger.getLogger(ProvinceProModelService.class);
	@Autowired
	ProModelService proModelService;
	@Autowired
	ProjectPlanService projectPlanService;
	@Autowired
	private ProvinceProModelDao provinceProModelDao;
	@Autowired
	private ProjectPlanDao projectPlanDao;
	@Autowired
	ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
	ActYwGnodeService actYwGnodeService;
    @Autowired
	ActTaskService actTaskService;
    @Autowired
    ProActTaskService proActTaskService;
    @Autowired
	ActYwService actYwService;
    @Autowired
	UserService userService;
    @Autowired
	SystemService systemService;
    @Autowired
	IdentityService identityService;
    @Autowired
	TaskService taskService;
    @Autowired
	ActDao actDao;
	@Autowired
 	private SysCertInsDao sysCertInsDao;
    @Autowired
    private SysAttachmentService sysAttachmentService;
    @Autowired
    private TeamService teamService;
	@Autowired
 	private ActYwGassignService actYwGassignService;
	@Autowired
 	private ActYwEtAssignRuleService actYwEtAssignRuleService;
    @Autowired
	TeamUserHistoryDao teamUserHistoryDao;
	@Autowired
	BackTeacherExpansionService backTeacherExpansionService;

	@Autowired
	private CoreService coreService;

	@Override
    public WorkFlowService<ProvinceProModelDao, ProvinceProModel, ExpProModelVo> setWorkService() {
        return this;
    }

    @Override
    public IWorkFlow<ProvinceProModel, ProModel, ExpProModelVo> setIWorkFlow() {
        return this;
    }

    @Override
	public ProvinceProModel get(String id) {
		return super.get(id);
	}
	@Override
	public List<ProvinceProModel> findList(ProvinceProModel provinceProModel) {
		return super.findList(provinceProModel);
	}
	@Override
	public Page<ProvinceProModel> findPage(Page<ProvinceProModel> page, ProvinceProModel provinceProModel) {
		return super.findPage(page, provinceProModel);
	}

	@Transactional(readOnly = false)
	public void save(ProvinceProModel provinceProModel) {
		super.save(provinceProModel);
	}

	@Transactional(readOnly = false)
	public void delete(ProvinceProModel provinceProModel) {
		super.delete(provinceProModel);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ProvinceProModel provinceProModel) {
  	  dao.deleteWL(provinceProModel);
  	}


	public Page<ProvinceProModel> findDataPage(Page<ProvinceProModel> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProvinceProModel provinceProModel) {
		List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
		String glist = actYwGnodes.stream().map(e->"sid-"+e.getId()).collect(Collectors.joining(","));
		ProModel proModel = null;
		if (provinceProModel.getProModel()==null) {
			proModel = new ProModel();
		} else {
			proModel = provinceProModel.getProModel();
		}
		proModel.setGnodeId(gnodeId);
		provinceProModel.setGnodeIdList(glist);
		provinceProModel.setPage(page);
		provinceProModel.setProModel(proModel);
		if (StringUtils.isBlank(glist)){
			page.setList(new ArrayList<>(0));
		} else {
			//页面审核条件参数
			if(StringUtil.isNotEmpty(proModel.getProCategory())){
				List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
				provinceProModel.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
				List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
				provinceProModel.setProjectLevelList(levelList);
			}
			if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
				provinceProModel.setOfficeIdList(officeList);
			}
			provinceProModel.setActYwId(actywId);
			getRoleResult(provinceProModel);
			List<ProvinceProModel> list = dao.findListByDataOfSt(provinceProModel);
//			List<ProvinceProModel> list = dao.findListByData(provinceProModel);
			//证书
			//int i=0;
			for (ProvinceProModel hsxm : list) {
				// 查询团队指导老师的名称，多个以逗号分隔
				ProModel hsxmProModel = hsxm.getProModel();
				List<Team> team = teamService.findTeamUserName(hsxmProModel.getTeamId());
				List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					hsxmProModel.getTeam().setuName(StringUtils.join(names, ","));
				}
				List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if(StringUtil.isNotEmpty(hsxmProModel.getDeclareId())){
					User declareUser= systemService.getUser(hsxmProModel.getDeclareId());
					if(declareUser!=null){
						if(stunames.contains(declareUser.getName())){
							stunames.remove(declareUser.getName());
						}
					}
				}
				if (!stunames.isEmpty()) {
					hsxmProModel.getTeam().setEntName(StringUtils.join(stunames, ","));
				}
				ActYwGnode lastNode =null;
				boolean isScoreNode=false;
				int i=0;
				while(i==0){
					ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
					actYwAuditInfoIn.setPromodelId(hsxm.getId());
					actYwAuditInfoIn.setGnodeId(actYwGnodes.get(0).getId());
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getGnodeByNextGnode(actYwAuditInfoIn);
					if(lastActYwAuditInfoIn==null){
						i++;
					}else {
						lastNode = actYwGnodeService.getByg(lastActYwAuditInfoIn.getGnodeId());
						List<ActYwGform> formList=lastNode.getGforms();
						for(ActYwGform actYwGform:formList){
							if(RegType.RT_GE.getId().equals(actYwGform.getForm().getSgtype())){
								if(model!=null) {
									model.addAttribute("isScore", "1");
								};
								isScoreNode = true;
								break;
							}
						}
						i++;
					}
				}
				//如果上一个节点为评分节点 算出平均分
				if(isScoreNode){
					ActYwAuditInfo actYwAuditInfoIn = new ActYwAuditInfo();
					actYwAuditInfoIn.setPromodelId(hsxm.getId());
					actYwAuditInfoIn.setGnodeId(lastNode.getId());
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getLastAudit(actYwAuditInfoIn);
					ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
					actYwAuditInfo.setPromodelId(proModel.getId());
					actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
					//判断是否存在同批次号数据
					if(StringUtil.isNotEmpty(lastActYwAuditInfoIn.getGnodeVesion())){
						actYwAuditInfo.setGnodeVesion(lastActYwAuditInfoIn.getGnodeVesion());
					}
					String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
					hsxmProModel.setgScore(gscore);
				}
				// 项目结果
				if (StringUtils.isNotBlank(hsxm.getEndGnodeId()) && Const.YES.equals(hsxm.getState())) {// 流程已结束
					getFinalResult(hsxmProModel);
				}
				//添加审核方法参数
				Map<String,String>map=ProProcessDefUtils.getActByProcId(hsxm.getId());
				hsxmProModel.setAuditMap(map);
			}
			page.setList(list);
		}

		//根据gnodeId得到下一个节点是否为网关，是否需要网关状态
		List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
		if (actYwStatusList != null) {//批量审核参数
			for(ActYwStatus actYwStatus:actYwStatusList){
				//匹配批量审核通过
				if(actYwStatus.getState().equals("通过")){
					if(model!=null){
						model.addAttribute("isGate", "1");
						model.addAttribute("actYwStatus", actYwStatus);
					}
				}
			}
		}
		if(model!=null){
			model.addAttribute("provinceProModel", provinceProModel);
		}
		return page;
	}


	public Page<ProvinceProModel> findDataPage20190511(Page<ProvinceProModel> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProvinceProModel provinceProModel) {
		List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
		List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
		String glist = actYwGnodes.stream().map(e->"sid-"+e.getId()).collect(Collectors.joining(","));
//		List<String> delegateList = actYwGassignService.delegateIds(gnodeIdList, actywId);
		ProModel proModel = Optional.ofNullable(provinceProModel.getProModel()).orElse(new ProModel());
		proModel.setGnodeId(gnodeId);
		provinceProModel.setGnodeIdList(glist);
		provinceProModel.setPage(page);
		provinceProModel.setProModel(proModel);
		if (StringUtils.isBlank(glist)){
			page.setList(new ArrayList<>(0));
		} else {
			//页面审核条件参数
			if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
				provinceProModel.setOfficeIdList(officeList);
			}
			provinceProModel.setActYwId(actywId);
			List<ProvinceProModel> list = dao.findListByData(provinceProModel);
			//证书
			//int i=0;
			for (ProvinceProModel prov : list) {
				// 查询团队指导老师的名称，多个以逗号分隔
				ProModel provProModel = prov.getProModel();
				List<Team> team = teamService.findTeamUserName(provProModel.getTeamId());
				List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					provProModel.getTeam().setuName(StringUtils.join(names, ","));
				}
				List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if(StringUtil.isNotEmpty(provProModel.getDeclareId())){
					User declareUser= systemService.getUser(provProModel.getDeclareId());
					if(declareUser!=null){
						if(stunames.contains(declareUser.getName())){
							stunames.remove(declareUser.getName());
						}
					}
				}
				if (!stunames.isEmpty()) {
					provProModel.getTeam().setEntName(StringUtils.join(stunames, ","));
				}
				ActYwGnode lastNode =null;
				boolean isScoreNode=false;
				int i=0;
				while(i==0){
					ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
					actYwAuditInfoIn.setPromodelId(prov.getId());
					actYwAuditInfoIn.setGnodeId(actYwGnodes.get(0).getId());
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getGnodeByNextGnode(actYwAuditInfoIn);
					if(lastActYwAuditInfoIn==null){
						i++;
					}else {
						lastNode = actYwGnodeService.getByg(lastActYwAuditInfoIn.getGnodeId());
						List<ActYwGform> formList=lastNode.getGforms();
						for(ActYwGform actYwGform:formList){
							if(RegType.RT_GE.getId().equals(actYwGform.getForm().getSgtype())){
								if(model!=null) {
									model.addAttribute("isScore", "1");
								};
								isScoreNode = true;
								break;
							}
						}
						i++;
					}
				}
				//如果上一个节点为评分节点 算出平均分
				if(isScoreNode){
					ActYwAuditInfo actYwAuditInfoIn = new ActYwAuditInfo();
					actYwAuditInfoIn.setPromodelId(provProModel.getId());
					actYwAuditInfoIn.setGnodeId(lastNode.getId());
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getLastAudit(actYwAuditInfoIn);
					ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
					actYwAuditInfo.setPromodelId(provProModel.getId());
					actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
					//判断是否存在同批次号数据
					if(StringUtil.isNotEmpty(lastActYwAuditInfoIn.getGnodeVesion())){
						actYwAuditInfo.setGnodeVesion(lastActYwAuditInfoIn.getGnodeVesion());
					}
					String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
					provProModel.setgScore(gscore);
				}
				// 项目结果
				if (StringUtils.isNotBlank(prov.getEndGnodeId()) && Const.YES.equals(prov.getState())) {// 流程已结束
					getFinalResult(provProModel);
				}

				//添加审核方法参数
//				addAuditMap(gnodeId, delegateList, prov,provProModel);
				Map<String,String>map=ProProcessDefUtils.getActByProcId(prov.getId());
				provProModel.setAuditMap(map);
			}
			page.setList(list);
		}

		//根据gnodeId得到下一个节点是否为网关，是否需要网关状态
		List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
		if (actYwStatusList != null) {//批量审核参数
			for(ActYwStatus actYwStatus:actYwStatusList){
				//匹配批量审核通过
				if(actYwStatus.getState().equals("通过")){
					if(model!=null){
						model.addAttribute("isGate", "1");
						model.addAttribute("actYwStatus", actYwStatus);
					}
				}
			}
		}
		if(model!=null){
			model.addAttribute("provinceProModel", provinceProModel);
		}
		return page;
	}

//	@Override
	public Page<ProvinceProModel> findDataPage20190613(Page<ProvinceProModel> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProvinceProModel provinceProModel) {
		List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
		List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
		List<String> recordIds = actTaskService.recordIds(act, gnodeIdList, actywId);

		List<String> delegateList = actYwGassignService.delegateIds(gnodeIdList, actywId);

		ProModel proModel = null;
		if (provinceProModel.getProModel()==null) {
			proModel = new ProModel();
		} else {
			proModel = provinceProModel.getProModel();
		}
		proModel.setGnodeId(gnodeId);
		provinceProModel.setIds(recordIds);
		provinceProModel.setPage(page);
		provinceProModel.setProModel(proModel);
		if (recordIds.isEmpty()) {
			page.setList(new ArrayList<>(0));
		} else {
			//页面审核条件参数
			if(StringUtil.isNotEmpty(proModel.getProCategory())){
				List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
				provinceProModel.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
				List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
				provinceProModel.setProjectLevelList(levelList);
			}
			if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
				provinceProModel.setOfficeIdList(officeList);
			}
//			List<ProvinceProModel> list = dao.findListByIds(provinceProModel);
			provinceProModel.setActYwId(actywId);
			List<ProvinceProModel> list = dao.findListByData(provinceProModel);
			//证书
			//int i=0;
			for (ProvinceProModel prov : list) {
				// 查询团队指导老师的名称，多个以逗号分隔
				ProModel provProModel = prov.getProModel();
				List<Team> team = teamService.findTeamUserName(provProModel.getTeamId());
				List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					provProModel.getTeam().setuName(StringUtils.join(names, ","));
				}
				List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if(StringUtil.isNotEmpty(provProModel.getDeclareId())){
					User declareUser= systemService.getUser(provProModel.getDeclareId());
					if(declareUser!=null){
						if(stunames.contains(declareUser.getName())){
							stunames.remove(declareUser.getName());
						}
					}
				}
				if (!stunames.isEmpty()) {
					provProModel.getTeam().setEntName(StringUtils.join(stunames, ","));
				}
				ActYwGnode lastNode =null;
				boolean isScoreNode=false;
				int i=0;
				while(i==0){
					ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
					actYwAuditInfoIn.setPromodelId(prov.getId());
					actYwAuditInfoIn.setGnodeId(actYwGnodes.get(0).getId());
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getGnodeByNextGnode(actYwAuditInfoIn);
					if(lastActYwAuditInfoIn==null){
						i++;
					}else {
						lastNode = actYwGnodeService.getByg(lastActYwAuditInfoIn.getGnodeId());
						List<ActYwGform> formList=lastNode.getGforms();
						for(ActYwGform actYwGform:formList){
							if(RegType.RT_GE.getId().equals(actYwGform.getForm().getSgtype())){
								if(model!=null) {
									model.addAttribute("isScore", "1");
								};
								isScoreNode = true;
								break;
							}
						}
						i++;
					}
				}
				//如果上一个节点为评分节点 算出平均分
				if(isScoreNode){
					ActYwAuditInfo actYwAuditInfoIn = new ActYwAuditInfo();
					actYwAuditInfoIn.setPromodelId(prov.getId());
					actYwAuditInfoIn.setGnodeId(lastNode.getId());
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getLastAudit(actYwAuditInfoIn);
					ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
					actYwAuditInfo.setPromodelId(prov.getId());
					actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
					//判断是否存在同批次号数据
					if(StringUtil.isNotEmpty(lastActYwAuditInfoIn.getGnodeVesion())){
						actYwAuditInfo.setGnodeVesion(lastActYwAuditInfoIn.getGnodeVesion());
					}
					String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
					provProModel.setgScore(gscore);
				}
				// 项目结果
				if (StringUtils.isNotBlank(prov.getEndGnodeId()) && Const.YES.equals(prov.getState())) {// 流程已结束
					getFinalResult(provProModel);
				}
				//添加审核方法参数
				Map<String,String>map=ProProcessDefUtils.getActByProcId(prov.getId());
				provProModel.setAuditMap(map);
			}
			page.setList(list);
		}

		//根据gnodeId得到下一个节点是否为网关，是否需要网关状态
		List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
		if (actYwStatusList != null) {//批量审核参数
			for(ActYwStatus actYwStatus:actYwStatusList){
				//匹配批量审核通过
				if(actYwStatus.getState().equals("通过")){
					if(model!=null){
						model.addAttribute("isGate", "1");
						model.addAttribute("actYwStatus", actYwStatus);
					}
				}
			}
		}
		if(model!=null){
			model.addAttribute("provinceProModel", provinceProModel);
		}
		return page;
	}
	public void addAuditMap(String gnodeId, List<String> delegateList, ProvinceProModel prov,ProModel provProModel) {
//		Map<String,String> map= ProProcessDefUtils.getActByPromodelId(prov.getId());
		Map<String,String> map= ProProcessDefUtils.getActByPromodelId(prov.getModelId());
		provProModel.setAuditMap(map);
		if(delegateList!=null && delegateList.contains(prov.getId())){
			ActYwGassign actYwGassign=new ActYwGassign();
			actYwGassign.setRevUserId(UserUtils.getUserId());
			actYwGassign.setGnodeId(gnodeId);
			actYwGassign.setPromodelId(prov.getId());
			ActYwGassign oldactYwGassign=actYwGassignService.getByActYwGassign(actYwGassign);
			if(oldactYwGassign!=null && (oldactYwGassign.getIsOver()==null || "0".equals(oldactYwGassign.getIsOver()))){
				Map<String,String> mapIndex=new HashMap<String,String>();
				mapIndex.put("status", "todo");
				mapIndex.put("gnodeId", gnodeId);
				mapIndex.put("taskName", "委派审核");
				mapIndex.put("proModelId",prov.getId());
				provProModel.setAuditMap(mapIndex);
			}
		}
	}


	public void getFinalResult(ProModel model) {
		ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
		actYwAuditInfo.setGnodeId(model.getEndGnodeId());
		actYwAuditInfo.setPromodelId(model.getId());
		ActYwGnode endNode = actYwGnodeService.getByg(model.getEndGnodeId());
		model.setFinalResult((endNode != null ? endNode.getName() : "")
				+ proModelService.getStateByAuditInfo(endNode, actYwAuditInfoService.findList(actYwAuditInfo)));
	}

	@Override
	public Page<ProvinceProModel> findQueryPage(Page<ProvinceProModel> page, Model model, String actywId, ActYw actYw, ProvinceProModel provinceProModel) {
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
//		List<String> recordIds = actTaskService.queryRecordIds(act, actywId);
		//增加导入的数据
		ProModel proModel = null;
		if (org.springframework.util.StringUtils.isEmpty(provinceProModel.getProModel())) {
			proModel = new ProModel();
		} else {
			proModel = provinceProModel.getProModel();
		}
		proModel.setActYwId(actywId);
		provinceProModel.setProModel(proModel);
		provinceProModel.setActYw(actYw);
		provinceProModel.setActYwId(actywId);
       /* List<ProModel> importList = proModelService.findImportList(proModel);
        if (!importList.isEmpty()) {
            List<String> importIds = importList.stream().map(e -> e.getId()).collect(Collectors.toList());
            recordIds.addAll(importIds);
        }*/
		if(model!=null){
			model.addAttribute("provinceProModel", provinceProModel);
		}

//		return findPage(page, provinceProModel, recordIds);
		return findPageView(page, provinceProModel);
	}

	@Override
	public Page<ProvinceProModel> findAssignPage(Page<ProvinceProModel> page, Model model, String actywId, ActYw actYw, ProvinceProModel provinceProModel) {
		String gnodeId = super.getGnodeId(provinceProModel, actywId);
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		List<String> gnodeIdList = new ArrayList<String>();
		gnodeIdList.add(gnodeId);
		List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actywId);
		ProModel proModel = null;
		if (org.springframework.util.StringUtils.isEmpty(provinceProModel.getProModel())) {
			proModel = new ProModel();
		} else {
			proModel = provinceProModel.getProModel();
		}
		proModel.setActYwId(actywId);
		proModel.setGnodeId(gnodeId);
		provinceProModel.setProModel(proModel);
		if(model!=null){
			model.addAttribute("provinceProModel", provinceProModel);
		}
		return findAssignPage(page, provinceProModel, recordIds);
	}

	@Override
	public Page<ProvinceProModel> findAssignPage(Page<ProvinceProModel> page, ProvinceProModel t, List<String> recordIds) {
		t.setIds(recordIds);
		t.setPage(page);
		if (recordIds.isEmpty()) {
			page.setList(new ArrayList<>(0));
		} else {
			ProModel proModel = null;
			if (t.getProModel()==null) {
				proModel = new ProModel();
			} else {
				proModel = t.getProModel();
			}
			t.setProModel(proModel);
			//页面审核条件参数
			if(StringUtil.isNotEmpty(proModel.getProCategory())){
				List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
				t.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
				List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
				t.setProjectLevelList(levelList);
			}
			if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
				t.setOfficeIdList(officeList);
			}
			List<ProvinceProModel> list = provinceProModelDao.findListByIdsUnAudit(t);
			//证书
			Map<String,String> ass=getTaskAssigns(list, t.getProModel().getGnodeId());
			Map<String,List<SysCertInsVo>> map=getSysCertIns(list);
			for (ProvinceProModel model : list) {
				//证书
				if(map!=null&&!map.isEmpty()){
					model.getProModel().setScis(map.get(model.getProModel().getId()));
				}
				//指派人
				if(ass!=null&&!ass.isEmpty()){
					model.getProModel().setTaskAssigns(ass.get(model.getProModel().getId()));
				}
				// 查询团队指导老师的名称，多个以逗号分隔
				List<Team> team = teamService.findTeamUserName(model.getProModel().getTeamId());
				List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					model.getProModel().getTeam().setuName(StringUtils.join(names, ","));
				}
				List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if(StringUtil.isNotEmpty(model.getProModel().getDeclareId())){
					User declareUser= systemService.getUser(model.getProModel().getDeclareId());
					if(declareUser!=null){
						if(stunames.contains(declareUser.getName())){
							stunames.remove(declareUser.getName());
						}
					}
				}
				if (!stunames.isEmpty()) {
					model.getProModel().getTeam().setEntName(StringUtils.join(stunames, ","));
				}


				// 项目结果
				if (StringUtils.isNotBlank(model.getProModel().getEndGnodeId()) && Const.YES.equals(model.getProModel().getState())) {// 流程已结束
					getFinalResult(model);
				}
				//添加审核方法参数
				Map<String,String> mapParam=ProProcessDefUtils.getActByPromodelId(model.getProModel().getId());
				model.getProModel().setAuditMap(mapParam);


			}
			page.setList(list);
		}
		return page;
	}

	private Map<String,String> getTaskAssigns(List<ProvinceProModel> subList, String gnodeid){
     Map<String,String> map=new HashMap<String,String>();
     List<String> param=new ArrayList<String>();
     if(subList.size()==0){
         return map;
     }
     for(ProvinceProModel p:subList){
         param.add(p.getProModel().getId());
     }
     List<Map<String,String>> list=actYwGnodeService.getAssignUserNames(param, gnodeid);
     if(list!=null&&list.size()>0){
         for(Map<String,String> s:list){
             map.put(s.get("promodel_id"), s.get("anames"));
         }
     }
     return map;
 	}

	private Map<String,List<SysCertInsVo>> getSysCertIns(List<ProvinceProModel> subList){
     Map<String,List<SysCertInsVo>> map=new HashMap<String,List<SysCertInsVo>>();
     List<String> param=new ArrayList<String>();
     if(subList.size()==0){
         return map;
     }
     for(ProvinceProModel p:subList){
         param.add(p.getProModel().getId());
     }
     List<SysCertInsVo> list=sysCertInsDao.getSysCertIns(param);
     if(list!=null&&list.size()>0){
         for(SysCertInsVo s:list){
             List<SysCertInsVo> tem=map.get(s.getProid());
             if(tem==null){
                 tem=new ArrayList<SysCertInsVo>();
                 map.put(s.getProid(), tem);
             }
             tem.add(s);
         }
     }
		return map;
	}

	//判断是否为当前提交节点
    public boolean checkIsHasReport(String gnodeId, String provinceProModelId) {
		ProvinceProModel provinceProModel=get(provinceProModelId);
        if(provinceProModel==null){
            return false;
        }
        if(provinceProModel.getState()!=null && provinceProModel.getState().equals("1")){
            return true;
        }
        ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(provinceProModel.getProcInsId());
        if(actYwGnode==null){
            return false;
        }
        String currGnodeId=actYwGnode.getId();
        return gnodeId.equals(currGnodeId);
    }

	public String getTaskId(ProvinceProModel provinceProModel, ActYwGnode actYwGnode, boolean isFirst) {
		String taskId;
		try{
			// 当前节点为签收节点 先签收任务
			if (actYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwGnode.getTaskType())) {
				//得到当前节点的审核人
				taskId = actTaskService.getTaskidByProcInsId(provinceProModel.getProcInsId());
				taskService.claim(taskId, UserUtils.getUser().getId());
			} else {
				taskId = actTaskService.getTaskidByProcInsId(provinceProModel.getProcInsId(), UserUtils.getUser().getId());//UserUtils.getUser().getId()
			}
		}catch (Exception e){
			throw new GroupErrorException("该任务已经不存在");
		}
		if(StringUtil.isEmpty(taskId)){
			throw new GroupErrorException("该任务已经不存在");
		}
		return taskId;
	}


	@Transactional(readOnly = false)
    public void auditWithGateWay(ProvinceProModel provinceProModel,ProModel proModel, String gnodeId) throws GroupErrorException {
		Map<String, Object> vars = new HashMap<String, Object>();
			ActYw actYw = actYwService.get(provinceProModel.getActYwId());
			if (actYw == null) {// 根据当前节点gnodeId和网关判断条件得到下一步审核角色
				return;
			}

			boolean isCurr=checkIsHasReport(gnodeId,provinceProModel.getId());
	        if(!isCurr){
	            throw new GroupErrorException("该任务已处理。");
	        }
			String key = ActYw.getPkey(actYw);
			ActYwGnode actYwGnode = actYwGnodeService.getBygGclazz(gnodeId); // 得到当前节点
			Boolean isGate = proModelService.getNextIsGate(gnodeId);//是否是网关
			ActYwGnode nextGnode = null;
			ActYwGnode nextGate = null;
			String nextGnodeRoleId = null;
			if (isGate) {
				nextGate = proModelService.getNextGate(gnodeId);
				nextGate = actYwGnodeService.getBygGclazz(nextGate.getId());
			} else {
				nextGnode = proModelService.getNextGnode(gnodeId);
				nextGnode = actYwGnodeService.getBygGclazz(nextGnode.getId());
				if (nextGnode != null && (GnodeType.GT_PROCESS_TASK.getId().equals(nextGnode.getType())
						|| GnodeType.GT_ROOT_TASK.getId().equals(nextGnode.getType()))) {
					List<Role> roleList= nextGnode.getRoles();
					if (roleList == null) {
						throw new GroupErrorException("审核节点角色配置错误。");
					}
					nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);//多角色配置人员
				}
			}
			boolean isFirst=FormClientType.FST_FRONT.getKey().equals(actYwGnode.getGforms().get(0).getForm().getClientType());
			//获得下一个节点taskId
			String taskId =getTaskId(provinceProModel, actYwGnode,isFirst);

//					actTaskService.getTaskidByProcInsId(provinceProModel.getProcInsId(), UserUtils.getUser().getId());//UserUtils.getUser().getId()
			proModelService.saveProvActYwAuditInfo(provinceProModel,proModel, actYwGnode);//判断审核前后台
			vars = provinceProModel.getVars();//工作流传递携带参数
			ActYwStatus actYwStatusNext = null;
			if (actYwGnode != null) {
				// 节点之间有连接线 根据实例id得到下一个节点 判断下一个节点是否为网关
				Task task = actTaskService.getTask(taskId);


				boolean isEndNode = actTaskService.isMultiLast(key, task.getTaskDefinitionKey(), provinceProModel.getProcInsId());

				if (isGate) {// 判断是网关
					// 根据网关节点得到网关后面连接线
					List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
					if (actYwStatusList != null && actYwStatusList.size() > 0) {
						// 判断审核结果 判断网关类型  为评分还是审核 1是审核2是评分
						Boolean isScoreAudit = proModelService.setScoreAuditStatus(actYwStatusList);
						if (isScoreAudit) { //是否是评分
							if (isEndNode) {// 判断是否最后一个审核任务
								// 如果当前任务环节完成了
								actYwStatusNext = proModelService.getProvActYwStatus(proModel,provinceProModel.getId(), gnodeId, "", "", actYwStatusList);
								if (actYwStatusNext != null) {
									//配置流程所需参数
									vars = getProvVarMap(proModel, gnodeId, vars);
								}
							}
						} else {
							//配置流程所需参数
							vars = getProvVarMap(proModel, gnodeId, vars);
							actYwStatusNext =  proModelService.getGateActYwStatus(proModel, actYwStatusList);
						}
						nextGnode =  proModelService.getNextGnode(proModel, gnodeId);
						//添加下一步网关判断条件
						if(actYwStatusNext!=null){
							vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getStatus());
						}
					}
				} else {
					if (nextGnodeRoleId != null) {
						// 没有网关
						vars =  addProvRoleIn(nextGnode, nextGnodeRoleId, vars, proModel);
						//向下一步审核人发送审核消息
					}
				}
			}
			//流程走下一步
			try {
//
				taskService.complete(taskId, vars);
//				taskService.complete(taskId, vars);
				if(actYwGnode.getIsDelegate()){
					//删除委派。
					ActYwGassign actYwGassign=new ActYwGassign();
					actYwGassign.setPromodelId(proModel.getId());
					actYwGassign.setGnodeId(gnodeId);
					actYwGassignService.deleteTodo(actYwGassign);
				}
				if(nextGnode!=null && nextGnode.getIsDelegate()){
					//删除下个节点委派。
					ActYwGassign actYwGassign=new ActYwGassign();
					actYwGassign.setPromodelId(proModel.getId());
					actYwGassign.setGnodeId(nextGnode.getId());
					actYwGassignService.deleteByAssign(actYwGassign);
			   }
			} catch (Exception e) {
				logger.error(e);
				throw new GroupErrorException("审核节点配置错误。");
			}
			// 判断流程走完后 项目是否结束；
			boolean res = actTaskService.ifOver(provinceProModel.getProcInsId());
			if (res) {
				// 标志流程结束
				provinceProModel.setState("1");
				//项目完成节点
				provinceProModel.setEndGnodeId(gnodeId);
				getProvFinalResult(provinceProModel);
				super.save(provinceProModel);
			}
    }

	//添加流程所需参数
   	public Map<String, Object> getProvVarMap(ProModel proModel, String gnodeId, Map<String, Object> vars) {
       	String nextGnodeRoleId;
       	ActYwGnode  nextGnode = proModelService.getNextGnode(proModel, gnodeId);

       //判断下一个节点不是结束节点
       	if (nextGnode != null
               	&& !GnodeType.GT_PROCESS_END.getId().equals(nextGnode.getType())
               	&& !GnodeType.GT_ROOT_END.getId().equals(nextGnode.getType())) {
           	List<Role> roleList = nextGnode.getRoles();
           	if (roleList == null) {
               	throw new GroupErrorException("审核节点角色配置错误。");
           	}
           	//多角色配置人员
        	nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
           	vars = addProvRoleIn(nextGnode, nextGnodeRoleId, vars, proModel);
       	}
       	return vars;
   	}

	public Map<String, Object> addProvRoleIn(ActYwGnode gnode, String nextGnodeRoleId, Map<String, Object> vars, ProModel proModel) {
        ProvinceProModel provinceProModel=getByProModelId(proModel.getId());
		if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
            List<String> roles = new ArrayList<String>();
            //判断下一个节点是否为自动审核。
            ActYwEtAssignRule actYwEtAssignRule=actYwEtAssignRuleService.getActYwEtAssignRuleByActywIdAndGnodeId((String)vars.get("actYwId"),gnode.getId());
            //设置了自动审核并且持续开启 //计算自动审核人
            if(actYwEtAssignRule!=null && "0".equals(actYwEtAssignRule.getAuditType()) && "1".equals(actYwEtAssignRule.getIsAuto())){
                int gradeSpeEachTotal=Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
                Boolean isAutoAudit=true;
                //删除旧的指派记录
                ActYwGassign actYwGassign = new ActYwGassign();
                actYwGassign.setPromodelId(provinceProModel.getId());
                actYwGassign.setGnodeId(gnode.getId());
                Boolean isHasAssign = actYwGassignService.isHasAssign(actYwGassign);
                if(ActYwEtAssignRule.autoRole.equals(actYwEtAssignRule.getAuditRole())){
                    //0是全部专家
                    List<String> expertList=backTeacherExpansionService.findAllExpertList();
                    //找不到满足条件的专家 自动到下一个节点的处于待指派状态
                    if(expertList.size()<gradeSpeEachTotal){
                        isAutoAudit=false;
                        roles.add("assignUser");
                        //已经被指派过
                        if (isHasAssign) {
                            //删除旧的指派记录
                            actYwGassignService.deleteByAssign(actYwGassign);
                        }
                    }else {
                        roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, provinceProModel.getId(), expertList);
                    }
                }else{//根据专家类型找到的专家
                    List<String> expertList=backTeacherExpansionService.findExpertListByType(actYwEtAssignRule.getAuditRole());
                    if(expertList.size()<gradeSpeEachTotal){
                        isAutoAudit=false;
                        roles.add("assignUser");
                        //删除旧的指派记录  //已经被指派过
                        if (isHasAssign) {
                            //删除旧的指派记录
                            actYwGassignService.deleteByAssign(actYwGassign);
                        }
//                        throw new GroupErrorException("自动审核失败，请联系管理员核对自动审核审核参数");
                    }else {
                        roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, provinceProModel.getId(), expertList);
                    }
                }
                if(StringUtil.checkEmpty(roles)){
                    //找不到专家走指派路线
                    isAutoAudit=false;
                    roles.add("assignUser");
                    //已经被指派过
                    if (isHasAssign) {
                        //删除旧的指派记录
                        actYwGassignService.deleteByAssign(actYwGassign);
                    }
                }
                //如果是自动委派或者自动指派
                if(isAutoAudit){
	            	proModelService.addAssignRule(provinceProModel.getId(), roles, actYwEtAssignRule);
                }
            }else {
                //计算审核人
                if (gnode.getIsAssign() != null && gnode.getIsAssign()) {
                    roles.clear();
                    roles.add("assignUser");
                    //删除旧的指派记录
                    ActYwGassign actYwGassign = new ActYwGassign();
                    actYwGassign.setPromodelId(provinceProModel.getId());
                    actYwGassign.setGnodeId(gnode.getId());
                    Boolean isHasAssign = actYwGassignService.isHasAssign(actYwGassign);
                    //已经被指派过
                    if (isHasAssign) {
                        //删除旧的指派记录
                        actYwGassignService.deleteByAssign(actYwGassign);
                    }
                } else {
                    roles = proModelService.getProvUsersByRoles(nextGnodeRoleId);
                }
            }
            if(StringUtil.checkEmpty(roles)){
                throw new GroupErrorException("审核节点角色没有人");
            }
            if (gnode != null && GnodeTaskType.GTT_NONE.getKey().equals(gnode.getTaskType())) {
                vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId, roles);
            } else {
                vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId + "s", roles);
            }
        }
        return vars;
    }




	@Override
	public Page<ProvinceProModel> findPage(Page<ProvinceProModel> page, ProvinceProModel provinceProModel, List<String> recordIds) {
		provinceProModel.setIds(recordIds);
		provinceProModel.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
			if(StringUtil.isNotEmpty(provinceProModel.getProModel().getProCategory())){
				List<String> proList=Arrays.asList(provinceProModel.getProModel().getProCategory().split(","));
				provinceProModel.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(provinceProModel.getProModel().getFinalStatus())){
				List<String> levelList=Arrays.asList(provinceProModel.getProModel().getFinalStatus().split(","));
				provinceProModel.setProjectLevelList(levelList);
			}
			if(provinceProModel.getProModel().getDeuser()!=null && provinceProModel.getProModel().getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(provinceProModel.getProModel().getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(provinceProModel.getProModel().getDeuser().getOffice().getId().split(","));
				provinceProModel.setOfficeIdList(officeList);
			}

			List<ProvinceProModel> list = dao.findListByIds(provinceProModel);
            for (ProvinceProModel model : list) {
				model.setActYw(provinceProModel.getActYw());
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(model.getProModel().getTeamId());
                List<String> teachnames = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!teachnames.isEmpty()) {
                    model.getProModel().getTeam().setuName(StringUtils.join(teachnames, ","));
                }

				// 查询团队学生的名称，多个以逗号分隔
				List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if(StringUtil.isNotEmpty(model.getProModel().getDeclareId())){
					User declareUser= systemService.getUser(model.getProModel().getDeclareId());
					if(declareUser!=null){
						if(stunames.contains(declareUser.getName())){
							stunames.remove(declareUser.getName());
						}
					}
				}

				if (!stunames.isEmpty()) {
					model.getProModel().getTeam().setEntName(StringUtils.join(stunames, ","));
				}
                // 项目结果
                if (StringUtils.isNotBlank(model.getProModel().getEndGnodeId()) && Const.YES.equals(model.getProModel().getState())) {// 流程已结束
                    getFinalResult(model);
                }
				//添加审核方法参数
				Map<String,String>map=ProProcessDefUtils.getActByProcId(model.getId());
				model.getProModel().setAuditMap(map);
			}
            page.setList(list);
        }
        return page;
    }


	public Page<ProvinceProModel> findPageView(Page<ProvinceProModel> page, ProvinceProModel provinceProModel) {
		provinceProModel.setPage(page);
		if (provinceProModel.getProModel().getDeuser() != null && provinceProModel.getProModel().getDeuser().getOffice() != null
				&& StringUtil.isNotEmpty(provinceProModel.getProModel().getDeuser().getOffice().getId())) {
			List<String> officeList = Arrays.asList(provinceProModel.getProModel().getDeuser().getOffice().getId().split(","));
			provinceProModel.setOfficeIdList(officeList);
		}

		List<ProvinceProModel> list = dao.findListByView(provinceProModel);
		for (ProvinceProModel model : list) {
			model.setActYw(provinceProModel.getActYw());
			// 查询团队指导老师的名称，多个以逗号分隔
			List<Team> team = teamService.findTeamUserName(model.getProModel().getTeamId());
			List<String> teachnames = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
					.collect(Collectors.toList());
			if (!teachnames.isEmpty()) {
				model.getProModel().getTeam().setuName(StringUtils.join(teachnames, ","));
			}

			// 查询团队学生的名称，多个以逗号分隔
			List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
					.collect(Collectors.toList());
			if (StringUtil.isNotEmpty(model.getProModel().getDeclareId())) {
				User declareUser = systemService.getUser(model.getProModel().getDeclareId());
				if (declareUser != null) {
					if (stunames.contains(declareUser.getName())) {
						stunames.remove(declareUser.getName());
					}
				}
			}
			/*ActYwGnode lastNode =null;
			boolean isScoreNode=false;
			int i=0;
			while(i==0){
				ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
				actYwAuditInfoIn.setPromodelId(model.getId());
				actYwAuditInfoIn.setGnodeId(actYwGnodes.get(0).getId());
				ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getGnodeByNextGnode(actYwAuditInfoIn);
				if(lastActYwAuditInfoIn==null){
					i++;
				}else {
					lastNode = actYwGnodeService.getByg(lastActYwAuditInfoIn.getGnodeId());
					List<ActYwGform> formList=lastNode.getGforms();
					for(ActYwGform actYwGform:formList){
						if(RegType.RT_GE.getId().equals(actYwGform.getForm().getSgtype())){
							if(model!=null) {
								model.addAttribute("isScore", "1");
							};
							isScoreNode = true;
							break;
						}
					}
					i++;
				}
			}*/
			/*//如果上一个节点为评分节点 算出平均分
			if(isScoreNode){
				ActYwAuditInfo actYwAuditInfoIn = new ActYwAuditInfo();
				actYwAuditInfoIn.setPromodelId(model.getId());
				actYwAuditInfoIn.setGnodeId(lastNode.getId());
				ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getLastAudit(actYwAuditInfoIn);
				ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
				actYwAuditInfo.setPromodelId(model.getId());
				actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
				//判断是否存在同批次号数据
				if(StringUtil.isNotEmpty(lastActYwAuditInfoIn.getGnodeVesion())){
					actYwAuditInfo.setGnodeVesion(lastActYwAuditInfoIn.getGnodeVesion());
				}
				String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
				model.getProModel().setgScore(gscore);
			}*/

			if (!stunames.isEmpty()) {
				model.getProModel().getTeam().setEntName(StringUtils.join(stunames, ","));
			}
			// 项目结果
			if (StringUtils.isNotBlank(model.getEndGnodeId()) && Const.YES.equals(model.getState())) {// 流程已结束
				getProvFinalResult(model);
			}
			//添加审核方法参数
			Map<String, String> map = ProProcessDefUtils.getActByProcId(model.getId());
			model.getProModel().setAuditMap(map);

		}
		page.setList(list);

		return page;
	}

	@Override
	public void audit(String gnodeId,String proModelId, Model model) {
		ProvinceProModel provinceProModel = getByProModelId(proModelId);

		model.addAttribute("provinceProModel",provinceProModel);
		proModelService.audit(gnodeId,proModelId, model);
	}

	@Override
	//新增添加扩展表信息
	public void saveAddPro(ProModel proModel) {
		ProvinceProModel curprovinceProModel = getByProModelId(proModel.getId());
        if(curprovinceProModel == null){
			ProvinceProModel provinceProModel = new ProvinceProModel();
			provinceProModel.setModelId(proModel.getId());
			provinceProModel.setIsNewRecord(true);
			provinceProModel.setId(IdGen.uuid());
            save(provinceProModel);
        }else{
			curprovinceProModel.setIsNewRecord(false);
			curprovinceProModel.setModelId(proModel.getId());
            save(curprovinceProModel);
        }
	}

	@Override
	public void auditByGateWay(ProModel proModel, String gnodeId, HttpServletRequest request) {

	}

	@Override
	public String viewForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
		FormTheme formTheme = actYw.getFtheme();
		ProvinceProModel provinceProModel = getByProModelId(proModel.getId());
		model.addAttribute("provinceProModel",provinceProModel);
		if (formTheme != null) {
			FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
			//参数实现已经移动至实现类FppMd
			fpage.getParam().init(model, request, response, new Object[]{});
			fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService});
			String url=FormPage.getAbsUrl(actYw, fpageType, null);
			return url;
		}
		return CorePages.ERROR_404.getIdxUrl();
	}


	@Override
	@Transactional(readOnly = false)
	public void gcontestEdit(ProModel proModel, HttpServletRequest request, Model model){

	}

	@Override
	public void projectEdit(ProModel proModel, HttpServletRequest request, Model model) {
		ProvinceProModel provinceProModel = getByProModelId(proModel.getId());

		model.addAttribute("provinceProModel",provinceProModel);
	}

	@Override
	public void saveFormData(ProModel proModel, HttpServletRequest request) {

	}

	@Override
    public List<ExpProModelVo> exportData(Page<ProvinceProModel> page, ProvinceProModel provinceProModel) {
		provinceProModel.setPage(page);
        try {
            return dao.export(provinceProModel);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


	@Override
	public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, ProvinceProModel provinceProModel) {
		provinceProModel.setIds(pids);
		provinceProModel.setActYwId(actyw.getId());
	    Map<String, List<IWorkRes>> map = super.exportDataMap(request, response, tempPath, actyw, gnode, pids, provinceProModel);
        if(map == null){
            logger.warn("exportDataMap 方法处理异常，查询结果不能为空！");
            return;
        }
		String fileName = "";
        if((actyw.getProProject() != null) && StringUtil.isNotEmpty(actyw.getProProject().getProjectName())){
            fileName = actyw.getProProject().getProjectName();
        }
        if((gnode != null) && StringUtil.isNotEmpty(gnode.getName())){
            fileName += StringUtil.LINE_D + gnode.getName();
        }
		for (String key : map.keySet()) {
			// 按学院名称生成项目审核信息
			ExpGnodeFile expGfile = new ExpGnodeFile(tempPath + File.separator + gnode.getName() + File.separator + key, key, fileName);
            expGfile.setFileName(fileName + StringUtil.LINE_D + "大赛汇总表");
			expGfile.setClazz(ExpProModelVo.class);
            expGfile.setReqParam(new ItReqParam(request));
            ExportParams eparams = new ExportParams(expGfile.getFileName(), "大赛汇总", expGfile.getFileType());
            eparams.setDictHandler(new DictHandler());
            eparams.setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
            expGfile.setParam(eparams);
			IWorkFlow.expExcelByOs(expGfile, map.get(key));
		}
	}


    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProvinceProModel provinceProModel) {
        List<ExpProModelVo> proModelMdGcVos = exportData(new Page<ProvinceProModel>(request, response), provinceProModel);
        ExpGnodeFile expGfile = new ExpGnodeFile();
        expGfile.setFileName("大赛申报_汇总查询表");
        expGfile.setClazz(ExpProModelVo.class);
        ExportParams eparams = new ExportParams(expGfile.getFileName(), "大赛申报_汇总查询", expGfile.getFileType());
        eparams.setDictHandler(new DictHandler());
        eparams.setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
        expGfile.setParam(eparams);
        IWorkFlow.expRenderView(request, response, expGfile, proModelMdGcVos);
    }

	@Override
	@Transactional(readOnly = false)
	public JSONObject submit(ProModel proModel, JSONObject js) {

		js=proModelService.submit(proModel,js);
		return js;
	}

	@Override
	@Transactional(readOnly = false)
	public JSONObject saveProjectEdit(ProModel proModel, HttpServletRequest request) throws Exception {
		ProvinceProModel provinceProModel = getByProModelId(proModel.getId());
		String source=request.getParameter("source");
		String[] resultTypeList=request.getParameterValues("resultType");
		String resultType="";
		if(resultTypeList.length>0){
			for(int i=0;i<resultTypeList.length;i++){
				if(i==(resultTypeList.length-1)){
					resultType = resultType+resultTypeList[i];
				}else {
					resultType = resultType+resultTypeList[i] + ",";
				}
			}
		}

		if(StringUtil.isNotEmpty(provinceProModel.getId())){
			projectPlanDao.deleteByProjectId(proModel.getId());
		}

		return proModelService.saveProjectEdit(proModel,request);
	}

	@Override
	public void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId) {
		ProvinceProModel provinceProModel = getByProModelId(proModelId);
		model.addAttribute("provinceProModel",provinceProModel);

	}

	@Override
	public void addFrontParam(ProjectDeclareListVo v) {

	}

	@Override
	public void indexTime(Map<String, String> lastpro) {
		if(StringUtil.isNotEmpty(lastpro.get("id"))){
			ProvinceProModel provinceProModel = getByProModelId(lastpro.get("id"));
			lastpro.put("number",provinceProModel.getProModel().getCompetitionNumber());
		}
	}

	@Transactional(readOnly = false)
  	public void submit(ProvinceProModel provinceProModel) {
      	sysAttachmentService.saveByVo(provinceProModel.getProModel().getAttachMentEntity(), provinceProModel.getProModel().getId(), FileTypeEnum.S11,
              FileStepEnum.S1102);
      	teamUserHistoryDao.updateFinishAsStart(provinceProModel.getProModel().getId());
		//保存优秀展示
		ProModel proModel=provinceProModel.getProModel();
      	ActYw actYw = actYwService.get(provinceProModel.getProModel().getActYwId());
		Date newSubTime=null;
		try {
			Date proModelSubTime=provinceProModel.getProModel().getSubTime();
			String subTimeStr=DateUtil.formatDate(proModelSubTime);
			String nowTimeStr=DateUtil.getTime();
			subTimeStr = subTimeStr+" "+nowTimeStr;
			newSubTime=DateUtil.parseDate(subTimeStr,DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
		} catch (ParseException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		if(newSubTime!=null){
			provinceProModel.getProModel().setSubTime(newSubTime);
		}
		String num ="";
		try {
			 num = NumRuleUtils.getNumberText(
					 provinceProModel.getProModel().getActYwId(),
					 provinceProModel.getProModel().getYear(),
					 provinceProModel.getProModel().getProCategory(),false);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		if(StringUtil.isEmpty(num)){
			num=IdUtils.getProjectNumberByDb();
		}

		provinceProModel.getProModel().setCompetitionNumber(num);
		JedisUtils.publishMsg(ProModelTopic.TOPIC_THREE,provinceProModel.getProModel());
//		proModelService.save(provinceProModel.getProModel());
      	super.start(provinceProModel, actYw, "pro_model");
  	}

    /**
     * .
     * @param request
     * @param actYw
     * @param year
     * @param officeId
     * @param gnodeId
     * @return
     */
    public List<ProvinceProModel> findListByQuery(HttpServletRequest request, ActYw actYw, String year, String officeId,
            String gnodeId) {
        if(actYw == null){
            return null;
        }

        List<ProvinceProModel> list = new ArrayList<>();
        ProModel proModel = genProModel(request, gnodeId, actYw.getId());
        proModel.setIsAll(request.getParameter("isAll"));
        if(!(Const.YES).equals(proModel.getIsAll())){
            proModel.setYear(year);
            proModel.setProCategory(request.getParameter("proCategory"));
            if (StringUtils.isNotBlank(officeId)) {
                User user = new User();
                user.setOffice(new Office(officeId));
                proModel.setDeuser(user);
            }
            proModel.setQueryStr(request.getParameter("queryStr"));
            proModel.setFinalStatus(request.getParameter("finalStatus"));
        }
        String key = ActYw.getPkey(actYw);
        Act act = new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> recordIds = null;
        if(StringUtils.isNotBlank(gnodeId)){
            List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
            List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
            recordIds = actTaskService.recordIds(act, gnodeIdList, actYw.getId());
        }else{
            recordIds = actTaskService.queryRecordIds(act, actYw.getId());
            //增加导入的数据
            ProModel imodel = new ProModel();
            imodel.setActYwId(actYw.getId());
            List<ProModel> importList = proModelService.findImportList(imodel);
            if(!importList.isEmpty()){
                List<String> importIds = importList.stream().map(e -> e.getId()).collect(Collectors.toList());
                recordIds.addAll(importIds);
            }
        }

        if(StringUtil.checkEmpty(recordIds)){
            list = Lists.newArrayList();
            return list;
        }

        if(!(Const.YES).equals(proModel.getIsAll())){
            proModel.setIds(recordIds);
            list = provinceProModelDao.findPmByIds(new ProvinceProModel(proModel));
        }else{
            proModel = new ProModel();
            proModel.setIds(recordIds);
            list = provinceProModelDao.findPmByIds(new ProvinceProModel(proModel));
        }

        if(list == null){
            list = Lists.newArrayList();
        }
        return list;
    }

    /**
     * .
     * @param request
     * @param gnodeId
     * @param actywId
     * @return
     */
    private ProModel genProModel(HttpServletRequest request, String gnodeId, String actywId) {
        String year = request.getParameter("year");
        String officeId = request.getParameter("officeId");
        ProModel proModel = new ProModel();
        proModel.setActYwId(actywId);
        proModel.setGnodeId(gnodeId);
        proModel.setIsAll(request.getParameter("isAll"));
        if(!(Const.YES).equals(proModel.getIsAll())){
            proModel.setYear(year);
            proModel.setProCategory(request.getParameter("proCategory"));
            if (StringUtil.isNotBlank(officeId)) {
                User user = new User();
                user.setOffice(new Office(officeId));
                proModel.setDeuser(user);
            }
            proModel.setQueryStr(request.getParameter("queryStr"));
        }
        return proModel;
    }

	public List<ProvinceProModel> findProvListByIdsAssign(ProvinceProModel provinceProModel) {
		return provinceProModelDao.findProvListByIdsAssign(provinceProModel);
	}

	public ProvinceProModel getByProvinceProModelId(String id){
    	return dao.getByProvinceProModelId(id);
	}


	public void getRoleResult(ProvinceProModel proModel){
		String roleFlag = null;
		List<Role> roleList = UserUtils.getUser().getRoleList();
		List<Role> checkRole = new ArrayList<>();
		if (roleList != null && roleList.size() > 0){
			Role roleAdmin = coreService.getByRtype(CoreSval.Rtype.ADMIN_PN.getKey());
			if (roleAdmin != null){
				checkRole = roleList.stream().filter(item -> item.getId().equals(roleAdmin.getId())).collect(Collectors.toList());
				if (checkRole.size() == 0){
					Role roleSuper = coreService.getByRtype(CoreSval.Rtype.SUPER_SC.getKey());
					if (roleSuper != null){
						checkRole = roleList.stream().filter(item -> item.getId().equals(roleSuper.getId())).collect(Collectors.toList());
						if (checkRole.size() == 0){
							Role roleSysSC = coreService.getByRtype(CoreSval.Rtype.ADMIN_SYS_SC.getKey());
							if (roleSysSC != null){
								checkRole = roleList.stream().filter(item -> item.getId().equals(roleSysSC.getId())).collect(Collectors.toList());
								if (checkRole.size() == 0){
									Role roleYw = coreService.getByRtype(CoreSval.Rtype.ADMIN_YW_SC.getKey());
									if (roleYw != null){
										checkRole = roleList.stream().filter(item -> item.getId().equals(roleYw.getId())).collect(Collectors.toList());
									}
								}
							}
						}
					}
				}
			}
		}
		if (checkRole.size() > 0){
			//管理员
			roleFlag = "0";
		}else{
			Role roleMinister = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
			checkRole = roleList.stream().filter(item -> item.getId().equals(roleMinister.getId())).collect(Collectors.toList());
			if (checkRole.size() > 0){
				roleFlag = "2";
				proModel.setProfessor(UserUtils.getUserId());
			}
		}
		proModel.setRoleFlag(roleFlag);

	}

	public void getProvFinalResult(ProvinceProModel model) {
		ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
		actYwAuditInfo.setGnodeId(model.getEndGnodeId());
		actYwAuditInfo.setPromodelId(model.getId());
		ActYwGnode endNode = actYwGnodeService.get(model.getEndGnodeId());
		model.setFinalResult(
				proModelService.getStateByAuditInfo(endNode, actYwAuditInfoService.findList(actYwAuditInfo)));
	}


	/**
	 * 批量审核
	 * @param idList
	 * @param grade
	 * @param gnodeId
	 * @return
	 */
	public boolean provinceBatchAudit(String[] idList, String grade,String gnodeId) {
		for(int i=0;i<idList.length;i++){
			ProvinceProModel provinceProModel = get(idList[i]);
			try{
				auditWithGateWay(provinceProModel,provinceProModel.getProModel(),gnodeId);
			}catch (Exception e){
				logger.error(e);
				return false;
			}
		}
		return true;
	}

	public ProvinceProModel getByProInsId(String proInsId) {
		return dao.getByProInsId(proInsId);
	}


	public List<String> findListByIdsWithoutJoin(ProvinceProModel provinceProModel) {
		return dao.findListByIdsWithoutJoin(provinceProModel);
	}
}