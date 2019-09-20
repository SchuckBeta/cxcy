package com.oseasy.pro.modules.workflow.service;

import java.io.File;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.jobs.pro.ProModelTopic;
import com.oseasy.pro.modules.cert.service.ProSysAttachmentService;
import com.oseasy.pro.modules.promodel.utils.ActYwUtils;
import com.oseasy.pro.modules.promodel.utils.ProModelUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.actyw.tool.process.vo.RegType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.cert.dao.SysCertInsDao;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.project.dao.ProjectPlanDao;
import com.oseasy.pro.modules.project.entity.ProjectPlan;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.service.ProjectPlanService;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FormPage;
import com.oseasy.pro.modules.promodel.utils.ProProcessDefUtils;
import com.oseasy.pro.modules.promodel.vo.ExpGnodeFile;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.WorkFlowService;
import com.oseasy.pro.modules.workflow.dao.ProModelHsxmDao;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.handler.DataExpVoHandler;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.sys.utils.NumRuleUtils;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import net.sf.json.JSONObject;

/**
 * 华师大创模板Service.
 * @author zy
 * @version 2018-06-05
 */
@Service
@Transactional(readOnly = false)
public class ProModelHsxmService extends WorkFlowService<ProModelHsxmDao, ProModelHsxm, ExpProModelVo> implements IWorkFlow<ProModelHsxm, ProModel, ExpProModelVo> {

    public final static Logger logger = Logger.getLogger(ProModelHsxmService.class);
	@Autowired
	ProModelService proModelService;
	@Autowired
	ProjectPlanService projectPlanService;
	@Autowired
	private ProModelHsxmDao proModelHsxmDao;
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
    private CommonService commonService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
	TeamUserHistoryDao teamUserHistoryDao;
	@Autowired
	private ExcellentShowService excellentShowService;

	@Autowired
	private CoreService coreService;
	@Autowired
	private ProSysAttachmentService proSysAttachmentService;

    @Override
    public WorkFlowService<ProModelHsxmDao, ProModelHsxm, ExpProModelVo> setWorkService() {
        return this;
    }

    @Override
    public IWorkFlow<ProModelHsxm, ProModel, ExpProModelVo> setIWorkFlow() {
        return this;
    }

    public ProModelHsxm get(String id) {
		return super.get(id);
	}

	public List<ProModelHsxm> findList(ProModelHsxm proModelHsxm) {
		return super.findList(proModelHsxm);
	}

	public Page<ProModelHsxm> findPage(Page<ProModelHsxm> page, ProModelHsxm proModelHsxm) {
		return super.findPage(page, proModelHsxm);
	}

	@Transactional(readOnly = false)
	public void save(ProModelHsxm proModelHsxm) {
		super.save(proModelHsxm);
	}

	@Transactional(readOnly = false)
	public void delete(ProModelHsxm proModelHsxm) {
		super.delete(proModelHsxm);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ProModelHsxm proModelHsxm) {
  	  dao.deleteWL(proModelHsxm);
  	}
	@Override
    public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ProProject proProject, ActYw actYw) {
        User user = UserUtils.getUser();
		ProModelHsxm proModelHsxm = new ProModelHsxm();
        if (!org.springframework.util.StringUtils.isEmpty(proModel.getId())) {
			proModelHsxm = super.getByProModelId(proModel.getId());
        }
		if(proModel.getSubTime()==null) {
			proModel.setSubTime(new Date());
		}
		proModel.setActYwId(actYw.getId());
		//默认选择创新训练
		if(proModel.getProCategory()==null) {
			proModel.setProCategory("1");
		}
		proModelHsxm.setProModel(proModel);
		if(proModelHsxm.getProModel()!=null){
			proModelHsxm.setPlanList(projectPlanService.findListByProjectId(proModelHsxm.getProModel().getId()));
		}

		model.addAttribute("proModel", proModel);
        model.addAttribute("proModelHsxm", proModelHsxm);
        model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), ""));
        model.addAttribute("cuser", user);
        model.addAttribute("isSubmit", 0);
        model.addAttribute("wprefix", IWparam.getFileTplPreFix());
        model.addAttribute("wtypes", Wtype.toJson());
        return super.applayForm(fpageType, model, request, response, proModel, proProject, actYw);
    }


	public Page<ProModelHsxm> findDataPage(Page<ProModelHsxm> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProModelHsxm proModelHsxm) {
		List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
//		List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
//		List<String> recordIds = actTaskService.recordIds(act, gnodeIdList, actywId);
		String glist = actYwGnodes.stream().map(e->"sid-"+e.getId()).collect(Collectors.joining(","));
		ProModel proModel = null;
		if (proModelHsxm.getProModel()==null) {
			proModel = new ProModel();
		} else {
			proModel = proModelHsxm.getProModel();
		}
		proModel.setGnodeId(gnodeId);
		proModel.setGnodeIdList(glist);
		proModelHsxm.setPage(page);
		proModelHsxm.setProModel(proModel);
		if (StringUtils.isBlank(glist)) {
			page.setList(new ArrayList<>(0));
		} else {
			//页面审核条件参数
			if(StringUtil.isNotEmpty(proModel.getProCategory())){
				List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
				proModelHsxm.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
				List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
				proModelHsxm.setProjectLevelList(levelList);
			}
			if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
				proModelHsxm.setOfficeIdList(officeList);
			}
			proModel.setTenantId(TenantConfig.getCacheTenant());
			proModel.setActYwId(actywId);
			getRoleResult(proModel);
			proModelHsxm.setProModel(proModel);
			List<ProModelHsxm> list = dao.findListByIdsOfSt(proModelHsxm);
//			List<ProModelHsxm> list = dao.findListByIds(proModelHsxm);
			//证书
			//int i=0;
			for (ProModelHsxm hsxm : list) {
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
					actYwAuditInfoIn.setPromodelId(hsxmProModel.getId());
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
					actYwAuditInfoIn.setPromodelId(hsxmProModel.getId());
					actYwAuditInfoIn.setGnodeId(lastNode.getId());

					//ActYwAuditInfo lastactYwAuditInfo =actYwAuditInfoService.getLastAudit(actYwAuditInfo);
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getLastAudit(actYwAuditInfoIn);
					ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
					actYwAuditInfo.setPromodelId(proModel.getId());
					actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
					//判断是否存在同批次号数据
					if(StringUtil.isNotEmpty(lastActYwAuditInfoIn.getGnodeVesion())){
						actYwAuditInfo.setGnodeVesion(lastActYwAuditInfoIn.getGnodeVesion());
					}
					String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));


//					String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
					hsxmProModel.setgScore(gscore);
				}


				// 项目结果
				if (StringUtils.isNotBlank(hsxmProModel.getEndGnodeId()) && Const.YES.equals(hsxmProModel.getState())) {// 流程已结束
					getFinalResult(hsxmProModel);
				}
				//添加审核方法参数
				Map<String,String>map=ProProcessDefUtils.getActByPromodelId(hsxmProModel.getId());
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
			model.addAttribute("proModelHsxm", proModelHsxm);
		}
		return page;
	}



//	@Override
	public Page<ProModelHsxm> findDataPage12345(Page<ProModelHsxm> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProModelHsxm proModelHsxm) {
		List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
		List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
		List<String> recordIds = actTaskService.recordIds(act, gnodeIdList, actywId);

		ProModel proModel = null;
		if (proModelHsxm.getProModel()==null) {
			proModel = new ProModel();
		} else {
			proModel = proModelHsxm.getProModel();
		}
		proModel.setGnodeId(gnodeId);
		proModelHsxm.setIds(recordIds);
		proModelHsxm.setPage(page);
		proModelHsxm.setProModel(proModel);
		if (recordIds.isEmpty()) {
			page.setList(new ArrayList<>(0));
		} else {
			//页面审核条件参数
			if(StringUtil.isNotEmpty(proModel.getProCategory())){
				List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
				proModelHsxm.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
				List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
				proModelHsxm.setProjectLevelList(levelList);
			}
			if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
				proModelHsxm.setOfficeIdList(officeList);
			}
			List<ProModelHsxm> list = dao.findListByIds(proModelHsxm);
			//证书
			//int i=0;
			for (ProModelHsxm hsxm : list) {
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
					actYwAuditInfoIn.setPromodelId(hsxmProModel.getId());
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
					actYwAuditInfoIn.setPromodelId(hsxmProModel.getId());
					actYwAuditInfoIn.setGnodeId(lastNode.getId());

					//ActYwAuditInfo lastactYwAuditInfo =actYwAuditInfoService.getLastAudit(actYwAuditInfo);
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getLastAudit(actYwAuditInfoIn);
					ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
					actYwAuditInfo.setPromodelId(proModel.getId());
					actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
					//判断是否存在同批次号数据
					if(StringUtil.isNotEmpty(lastActYwAuditInfoIn.getGnodeVesion())){
						actYwAuditInfo.setGnodeVesion(lastActYwAuditInfoIn.getGnodeVesion());
					}
					String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));


//					String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
					hsxmProModel.setgScore(gscore);
				}


				// 项目结果
				if (StringUtils.isNotBlank(hsxmProModel.getEndGnodeId()) && Const.YES.equals(hsxmProModel.getState())) {// 流程已结束
					getFinalResult(hsxmProModel);
				}
				//添加审核方法参数
				Map<String,String>map=ProProcessDefUtils.getActByPromodelId(hsxmProModel.getId());
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
			model.addAttribute("proModelHsxm", proModelHsxm);
		}
		return page;
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
	public Page<ProModelHsxm> findQueryPage(Page<ProModelHsxm> page, Model model, String actywId, ActYw actYw, ProModelHsxm proModelHsxm) {
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
//		List<String> recordIds = actTaskService.queryRecordIds(act, actywId);
		List<String> recordIds = new ArrayList<>();
		//增加导入的数据
		ProModel proModel = null;
		if (org.springframework.util.StringUtils.isEmpty(proModelHsxm.getProModel())) {
			proModel = new ProModel();
		} else {
			proModel = proModelHsxm.getProModel();
		}
		proModel.setActYwId(actywId);
		proModelHsxm.setProModel(proModel);
       /* List<ProModel> importList = proModelService.findImportList(proModel);
        if (!importList.isEmpty()) {
            List<String> importIds = importList.stream().map(e -> e.getId()).collect(Collectors.toList());
            recordIds.addAll(importIds);
        }*/
		if(model!=null){
			model.addAttribute("proModelHsxm", proModelHsxm);
		}

		return findPage(page, proModelHsxm, recordIds);
	}

	@Override
	public Page<ProModelHsxm> findAssignPage(Page<ProModelHsxm> page, Model model, String actywId, ActYw actYw, ProModelHsxm proModelHsxm) {
		String gnodeId = super.getGnodeId(proModelHsxm, actywId);
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		List<String> gnodeIdList = new ArrayList<String>();
		gnodeIdList.add(gnodeId);
		List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actywId);
		ProModel proModel = null;
		if (org.springframework.util.StringUtils.isEmpty(proModelHsxm.getProModel())) {
			proModel = new ProModel();
		} else {
			proModel = proModelHsxm.getProModel();
		}
		proModel.setActYwId(actywId);
		proModel.setGnodeId(gnodeId);
		proModelHsxm.setProModel(proModel);
		if(model!=null){
			model.addAttribute("proModelHsxm", proModelHsxm);
		}
		return findAssignPage(page, proModelHsxm, recordIds);
	}

	public Page<ProModelHsxm> findAssignPage(Page<ProModelHsxm> page, ProModelHsxm t, List<String> recordIds) {
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
			List<ProModelHsxm> list = proModelHsxmDao.findListByIdsUnAudit(t);
			//证书
			Map<String,String> ass=getTaskAssigns(list, t.getProModel().getGnodeId());
			Map<String,List<SysCertInsVo>> map=getSysCertIns(list);
			for (ProModelHsxm model : list) {
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
					model.getProModel().getTeam().setuName(org.apache.commons.lang3.StringUtils.join(names, ","));
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
				if (org.apache.commons.lang3.StringUtils.isNotBlank(model.getProModel().getEndGnodeId()) && Const.YES.equals(model.getProModel().getState())) {// 流程已结束
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

	private Map<String,String> getTaskAssigns(List<ProModelHsxm> subList, String gnodeid){
     Map<String,String> map=new HashMap<String,String>();
     List<String> param=new ArrayList<String>();
     if(subList.size()==0){
         return map;
     }
     for(ProModelHsxm p:subList){
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

	private Map<String,List<SysCertInsVo>> getSysCertIns(List<ProModelHsxm> subList){
     Map<String,List<SysCertInsVo>> map=new HashMap<String,List<SysCertInsVo>>();
     List<String> param=new ArrayList<String>();
     if(subList.size()==0){
         return map;
     }
     for(ProModelHsxm p:subList){
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

	public Page<ProModelHsxm> findPage(Page<ProModelHsxm> page, ProModelHsxm proModelHsxm, List<String> recordIds) {
//		proModelHsxm.setIds(recordIds);
		proModelHsxm.setPage(page);

			if(StringUtil.isNotEmpty(proModelHsxm.getProModel().getProCategory())){
				List<String> proList=Arrays.asList(proModelHsxm.getProModel().getProCategory().split(","));
				proModelHsxm.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModelHsxm.getProModel().getFinalStatus())){
				List<String> levelList=Arrays.asList(proModelHsxm.getProModel().getFinalStatus().split(","));
				proModelHsxm.setProjectLevelList(levelList);
			}
			if(proModelHsxm.getProModel().getDeuser()!=null && proModelHsxm.getProModel().getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModelHsxm.getProModel().getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModelHsxm.getProModel().getDeuser().getOffice().getId().split(","));
				proModelHsxm.setOfficeIdList(officeList);
			}

			List<ProModelHsxm> list = null;
			getRoleResult(proModelHsxm.getProModel());
			if (proModelHsxm.getProModel().getRoleFlag().equals("2")){
				//专家
				list = dao.findListByIdsOfProfessor(proModelHsxm);
			}else{
				//管理员和秘书
				list = dao.findListByIdsOfBefore(proModelHsxm);
			}

//			List<ProModelHsxm> list = dao.findListByIds(proModelHsxm);
            for (ProModelHsxm model : list) {
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
				Map<String,String>map=ProProcessDefUtils.getActByPromodelId(model.getProModel().getId());
				model.getProModel().setAuditMap(map);
			}
            page.setList(list);

        return page;
    }

	@Override
	public void audit(String gnodeId,String proModelId, Model model) {
		ProModelHsxm proModelHsxm = getByProModelId(proModelId);
		proModelHsxm.setPlanList(projectPlanService.findListByProjectId(proModelId));
		model.addAttribute("proModelHsxm",proModelHsxm);
	}

	@Override
	//新增添加扩展表信息
	public void saveAddPro(ProModel proModel) {
	    ProModelHsxm curProModelHsxm = getByProModelId(proModel.getId());
        if(curProModelHsxm == null){
            ProModelHsxm proModelHsxm = new ProModelHsxm();
            proModelHsxm.setModelId(proModel.getId());
            proModelHsxm.setIsNewRecord(true);
            proModelHsxm.setId(IdGen.uuid());
            save(proModelHsxm);
        }else{
			curProModelHsxm.setIsNewRecord(false);
			curProModelHsxm.setModelId(proModel.getId());
            save(curProModelHsxm);
        }
	}

	@Override
	@Transactional(readOnly = false)
	public void auditByGateWay(ProModel proModel, String gnodeId, HttpServletRequest request) {
		proModelService.auditWithGateWay(proModel, gnodeId);
	}

	@Override
	public String viewForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
		FormTheme formTheme = actYw.getFtheme();
		ProModelHsxm proModelHsxm = getByProModelId(proModel.getId());
		proModelHsxm.setPlanList(projectPlanService.findListByProjectId(proModel.getId()));
		model.addAttribute("proModelHsxm",proModelHsxm);
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
		ProModelHsxm proModelHsxm = getByProModelId(proModel.getId());

		if(proModelHsxm.getProModel()!=null){
			proModelHsxm.setPlanList(projectPlanService.findListByProjectId(proModelHsxm.getProModel().getId()));
		}
		model.addAttribute("proModelHsxm",proModelHsxm);
	}

	@Override
	public void saveFormData(ProModel proModel, HttpServletRequest request) {

	}

	@Override
    public List<ExpProModelVo> exportData(Page<ProModelHsxm> page, ProModelHsxm proModelHsxm) {
		proModelHsxm.setPage(page);
        try {
            return dao.export(proModelHsxm);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


	@Override
	public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, ProModelHsxm proModelHsxm) {
	    proModelHsxm.setIds(pids);
	    proModelHsxm.setActYwId(actyw.getId());
	    Map<String, List<IWorkRes>> map = super.exportDataMap(request, response, tempPath, actyw, gnode, pids, proModelHsxm);
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

	/**
	 * 项目申报保存第一步
	 * @param proModelHsxm proModelHsxm
	 * @return JSONObject
	 */
	@Transactional(readOnly = false)
   public JSONObject saveStep1(ProModelHsxm proModelHsxm) throws Exception {
		JSONObject json = new JSONObject();
		if (StringUtil.isEmpty(proModelHsxm.getProModel().getId())){
			//新数据
			proModelHsxm.getProModel().setId(IdGen.uuid());
			proModelHsxm.getProModel().setIsNewRecord(true);
			proModelHsxm.getProModel().setCreateDate(new Date());
			proModelHsxm.getProModel().setSubStatus(Const.NO);
			proModelHsxm.getProModel().setCreateBy(UserUtils.getUser());
			proModelHsxm.getProModel().setImpdata(Const.NO);
			proModelHsxm.getProModel().setDeclareId(UserUtils.getUserId());
			String num ="";
			try {
				num = NumRuleUtils.getNumberText(proModelHsxm.getProModel().getActYwId(),proModelHsxm.getProModel().getYear());
			}catch(Exception e){
				logger.error("未设置正确编号规则，请联系管理员设置编号规则!");
				throw new GroupErrorException("未设置正确编号规则，请联系管理员设置编号规则!");
			}
			if(StringUtil.isEmpty(num)){
				num=IdUtils.getProjectNumberByDb();
			}
			proModelHsxm.getProModel().setCompetitionNumber(num);
		}
       	ProModel proModel = proModelHsxm.getProModel();
		ActYw actYw = actYwService.get(proModel.getActYwId());
       	if (actYw != null) {
           ProProject proProject = actYw.getProProject();
           if (proProject != null) {
               proModel.setProType(proProject.getProType());
               proModel.setType(proProject.getType());
		   }
       	}
		proModel.setStep(Const.STEP_ONE);
       	proModel.setIsSend(Const.SEND_NO);
		if (StringUtils.isBlank(proModel.getSubStatus()) || StringUtils.equals(proModel.getSubStatus(),"0")){
			proModel.setTenantId(TenantConfig.getCacheTenant());
			JedisUtils.publishMsg(ProModelTopic.TOPIC_ONE,proModel);
//			proModelService.save(proModel);
			/*//缓存项目名称
			if (proModel.getIsNewRecord()){
				JedisUtils.listRightPush(ProSval.ProEmskey.PROMODEL+":name:"+TenantConfig.getCacheTenant()+":"+proModel.getActYwId(),proModel.getpName());
			}*/
		}
		try {
			proModelService.saveProModelLogo(proModel);
		}catch (Exception e){
			logger.error(e.getMessage());
			return json;
		}
		proModelHsxm.setModelId(proModelHsxm.getProModel().getId());
       	super.save(proModelHsxm);
		if(StringUtil.isNotEmpty(proModelHsxm.getId())){
			projectPlanDao.deleteByProjectId(proModel.getId());
		}
		int sort = 0;
		for (ProjectPlan plan : proModelHsxm.getPlanList()) {
			plan.setSort(sort + "");
			plan.setId(IdGen.uuid());
			plan.setProjectId(proModel.getId());
			plan.setCreateDate(new Date());
			projectPlanDao.insert(plan);
			sort++;
		}
       	json.put(CoreJkey.JK_RET, 1);
       	json.put(CoreJkey.JK_MSG, "保存成功");
       	return json;
   }

	@Transactional(readOnly = false)
  	public JSONObject saveStep2(ProModelHsxm proModelHsxm) {
      	JSONObject json = new JSONObject();
      	try{
			ProModel proModel = proModelHsxm.getProModel();
			if (StringUtil.isEmpty(proModel.getPName())) {
				json.put(CoreJkey.JK_RET, 0);
				json.put(CoreJkey.JK_MSG, "保存失败，项目名称为必填项");
				return json;
			}
      /*	if (proModelHsxmDao.checkMdProName(proModel.getPName(), proModel.getId(), proModel.getType()) > 0) {
          	json.put(CoreJkey.JK_RET, 0);
          	json.put(CoreJkey.JK_MSG, "保存失败，该项目名称已经存在");
          	return json;
      	}*/
			List<TeamUserHistory> stus = proModel.getStudentList();
			List<TeamUserHistory> teas = proModel.getTeacherList();
			if ((stus != null && stus.size() > 0) || (teas != null && teas.size() > 0)) {
				commonService.disposeTeamUserHistoryOnSave(stus, teas, proModel.getActYwId(), proModel.getTeamId(), proModel.getId(), proModel.getYear());
			}
			ProModel model = proModelService.get(proModel.getId());
			model.setTeamId(proModelHsxm.getProModel().getTeamId());
			model.setIsNewRecord(false);
			//消息队列第二步
//			JedisUtils.publishMsg(ProModelTopic.TOPIC_TWO,model);
			proModelService.save(model);
			super.save(proModelHsxm);
			json.put(CoreJkey.JK_RET, 1);
			json.put(CoreJkey.JK_MSG, "保存成功");
			return json;
		}catch (Exception e){
      		logger.error("项目申报saveStep2保存失败",e);
			json.put(CoreJkey.JK_RET, 0);
			json.put(CoreJkey.JK_MSG, "保存失败");
		}
		return json;
  	}



    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProModelHsxm proModelHsxm) {
        List<ExpProModelVo> proModelMdGcVos = exportData(new Page<ProModelHsxm>(request, response), proModelHsxm);
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
		ProModelHsxm proModelHsxm = getByProModelId(proModel.getId());
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
		String resultContent=request.getParameter("resultContent");
		String budgetDollar=request.getParameter("budgetDollar");
		String budget=request.getParameter("budget");
		String planStartDate=request.getParameter("planStartDate");
		String planEndDate=request.getParameter("planEndDate");
		String planContent=request.getParameter("planContent");
		String planStep=request.getParameter("planStep");
		String innovation=request.getParameter("innovation");
		proModelHsxm.setSource(source);
		proModelHsxm.setResultType(resultType);
		proModelHsxm.setResultContent(resultContent);
		proModelHsxm.setBudgetDollar(budgetDollar);
		proModelHsxm.setBudget(budget);
		proModelHsxm.setPlanStartDate(DateUtil.parseDate(planStartDate,DateUtil.FMT_YYYYMMDD_ZG));
		proModelHsxm.setPlanEndDate(DateUtil.parseDate(planEndDate,DateUtil.FMT_YYYYMMDD_ZG));
		proModelHsxm.setPlanContent(planContent);
		proModelHsxm.setPlanStep(planStep);
		proModelHsxm.setInnovation(innovation);
		save(proModelHsxm);
		//删除添加任务分工list
		proModelHsxm.setPlanList(proModel.getPlanList());
		if(StringUtil.isNotEmpty(proModelHsxm.getId())){
			projectPlanDao.deleteByProjectId(proModel.getId());
		}
		int sort = 0;
		for (ProjectPlan plan : proModelHsxm.getPlanList()) {
			plan.setSort(sort + "");
			plan.setId(IdGen.uuid());
			plan.setProjectId(proModel.getId());
			plan.setCreateDate(new Date());
			projectPlanDao.insert(plan);
			sort++;
		}
		return proModelService.saveProjectEdit(proModel,request);
	}

    @Transactional(readOnly = false)
    public JSONObject saveHsxmProjectEdit(ProModelHsxm proModelHsxm) throws Exception{
        save(proModelHsxm);
        //删除添加任务分工list
        ProModel proModel = proModelHsxm.getProModel();
//        proModelHsxm.setPlanList(proModel.getPlanList());
        if(StringUtil.isNotEmpty(proModelHsxm.getId())){
            projectPlanDao.deleteByProjectId(proModel.getId());
        }
        int sort = 0;
        for (ProjectPlan plan : proModelHsxm.getPlanList()) {
            plan.setSort(sort + "");
            plan.setId(IdGen.uuid());
            plan.setProjectId(proModel.getId());
            plan.setCreateDate(new Date());
            projectPlanDao.insert(plan);
            sort++;
        }
        return proModelService.saveProModelEdit(proModel);
    }

	@Override
	public void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId) {
		ProModelHsxm proModelHsxm = getByProModelId(proModelId);

		if(proModelHsxm.getProModel()!=null){
			proModelHsxm.setPlanList(projectPlanService.findListByProjectId(proModelHsxm.getProModel().getId()));
		}
		model.addAttribute("proModelHsxm",proModelHsxm);
		model.addAttribute("proModelTypeList", DictUtils.getDictList("project_result_type"));
	}

	@Override
	public void addFrontParam(ProjectDeclareListVo v) {

	}

	@Override
	public void indexTime(Map<String, String> lastpro) {
		if(StringUtil.isNotEmpty(lastpro.get("id"))){
			ProModelHsxm proModelHsxm = getByProModelId(lastpro.get("id"));
			lastpro.put("number",proModelHsxm.getProModel().getCompetitionNumber());
		}
	}
	@Transactional(readOnly = false)
	public void updateFileList(ProModelHsxm proModelHsxm){
		AttachMentEntity attachMentEntity = proModelHsxm.getProModel().getAttachMentEntity();
		if(attachMentEntity != null){
			sysAttachmentService.saveByVo(proModelHsxm.getProModel().getAttachMentEntity(), proModelHsxm.getProModel().getId(), FileTypeEnum.S11,
					FileStepEnum.S1102);
		}else {
			List<SysAttachment> sysAttachmentList = proModelHsxm.getProModel().getFileInfo();
			if(sysAttachmentList.size() > 0){
				sysAttachmentService.saveBySysAttachmentVo(sysAttachmentList, proModelHsxm.getProModel().getId(), FileTypeEnum.S11,
						FileStepEnum.S1102);
			}
		}
	}

	@Transactional(readOnly = false)
  	public void submit(ProModelHsxm proModelHsxm) {
		AttachMentEntity attachMentEntity = proModelHsxm.getProModel().getAttachMentEntity();
		if(attachMentEntity != null){
			sysAttachmentService.saveByVo(proModelHsxm.getProModel().getAttachMentEntity(), proModelHsxm.getProModel().getId(), FileTypeEnum.S11,
					FileStepEnum.S1102);
		}else {
			List<SysAttachment> sysAttachmentList = proModelHsxm.getProModel().getFileInfo();
			if(sysAttachmentList.size() > 0){
				sysAttachmentService.saveBySysAttachmentVo(sysAttachmentList, proModelHsxm.getProModel().getId(), FileTypeEnum.S11,
						FileStepEnum.S1102);
			}
		}

      	teamUserHistoryDao.updateFinishAsStart(proModelHsxm.getProModel().getId());
		//保存优秀展示
		ProModel proModel=proModelHsxm.getProModel();
		String ecxType = ExcellentShow.Type_Project;
		if ("7,".equals(proModel.getProType())) {
			ecxType = ExcellentShow.Type_Gcontest;
		}

      	ActYw actYw = actYwService.get(proModelHsxm.getProModel().getActYwId());
		Date newSubTime=null;
		try {
			Date proModelSubTime=proModelHsxm.getProModel().getSubTime();
			String subTimeStr=DateUtil.formatDate(proModelSubTime);
			String nowTimeStr=DateUtil.getTime();
			subTimeStr = subTimeStr+" "+nowTimeStr;
			newSubTime=DateUtil.parseDate(subTimeStr,DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
		} catch (ParseException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		if(newSubTime!=null){
			proModelHsxm.getProModel().setSubTime(newSubTime);
		}
//		proModelHsxm.getProModel().setSubTime(new Date());
		String num ="";
		try {
			 num = NumRuleUtils.getNumberText(
					 proModelHsxm.getProModel().getActYwId(),
					 proModelHsxm.getProModel().getYear(),
					 proModelHsxm.getProModel().getProCategory(),false);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		if(StringUtil.isEmpty(num)){
			num=IdUtils.getProjectNumberByDb();
		}

		proModelHsxm.getProModel().setCompetitionNumber(num);
		JedisUtils.publishMsg(ProModelTopic.TOPIC_THREE,proModelHsxm.getProModel());
//		proModelService.save(proModelHsxm.getProModel());
      	super.start(proModelHsxm, actYw, "pro_model");
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
    public List<ProModelHsxm> findListByQuery(HttpServletRequest request, ActYw actYw, String year, String officeId,
            String gnodeId) {
        if(actYw == null){
            return null;
        }

        List<ProModelHsxm> list = new ArrayList<>();
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
            list = dao.findPmByIds(new ProModelHsxm(proModel));
        }else{
            proModel = new ProModel();
            proModel.setIds(recordIds);
            list = dao.findPmByIds(new ProModelHsxm(proModel));
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


	public void getRoleResult(ProModel proModel){
		String roleFlag = null;
		List<Role> roleList = UserUtils.getUser().getRoleList();
		List<Role> checkRole = new ArrayList<>();
		if (StringUtil.checkNotEmpty(roleList)){
			Role roleAdmin = coreService.getByRtype(CoreSval.Rtype.ADMIN_SN.getKey());
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
			Role roleMinister = coreService.getByRtype(CoreSval.Rtype.MINISTER.getKey());
			if((roleMinister != null) && StringUtil.isNotEmpty(roleMinister.getId())){
				checkRole = roleList.stream().filter(item -> item.getId().equals(roleMinister.getId())).collect(Collectors.toList());
			}

			if (checkRole.size() > 0){
				roleFlag = "1";
				proModel.setOfficeId(UserUtils.getUser().getOfficeId());
			}else{
				roleFlag = "2";
				proModel.setProfessor(UserUtils.getUserId());
			}
		}
		proModel.setRoleFlag(roleFlag);

	}

}