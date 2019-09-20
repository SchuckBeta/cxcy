package com.oseasy.pro.modules.workflow.service;

import static com.oseasy.pro.modules.promodel.service.ProModelService.removeDuplicate;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.jobs.pro.ProModelTopic;
import com.oseasy.pro.modules.cert.service.ProSysAttachmentService;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
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
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.cert.dao.SysCertInsDao;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;
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
import com.oseasy.pro.modules.workflow.dao.ProModelTlxyDao;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.handler.DataExpPmTlxyVoHandler;
import com.oseasy.pro.modules.workflow.handler.DataExpVoHandler;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pro.modules.workflow.vo.ExpProModelTlxyVo;
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
 * 互联网+大赛模板Service.
 * @author zy
 * @version 2018-06-05
 */
@Service
@Transactional(readOnly = false)
public class ProModelTlxyService extends WorkFlowService<ProModelTlxyDao, ProModelTlxy, ExpProModelTlxyVo> implements IWorkFlow<ProModelTlxy, ProModel, ExpProModelTlxyVo> {
    /**todo
     * 铜陵学院级别，仅限铜陵学院使用，其它模块禁止调用.
     */
    private static final String TLXY_PRO_LEVEL_0000000263 = "0000000263";
	//校赛值
    public static final String TLXY_PRO_LEVEL_0000000264 = "0000000264";
	//省赛值
	private static final String TLXY_PRO_LEVEL_0000000265 = "0000000265";
	//国赛值
	private static final String TLXY_PRO_LEVEL_0000000266= "0000000266";
    public final static Logger logger = Logger.getLogger(ProModelTlxyService.class);
	@Autowired
	private ProModelService proModelService;
	@Autowired
	private ProjectPlanService projectPlanService;
	@Autowired
	private ProModelTlxyDao proModelTlxyDao;
	@Autowired
	private ProjectPlanDao projectPlanDao;
	@Autowired
	private ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    private ActYwService actYwService;
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
    private TeamUserHistoryDao teamUserHistoryDao;
	@Autowired
	private ActYwGassignService actYwGassignService;
	@Autowired
	private ProSysAttachmentService proSysAttachmentService;

    @Override
    public WorkFlowService<ProModelTlxyDao, ProModelTlxy, ExpProModelTlxyVo> setWorkService() {
        return this;
    }

    @Override
    public IWorkFlow<ProModelTlxy, ProModel, ExpProModelTlxyVo> setIWorkFlow() {
        return this;
    }

    public ProModelTlxy get(String id) {
		return super.get(id);
	}

	public List<ProModelTlxy> findList(ProModelTlxy proModelTlxy) {
		return super.findList(proModelTlxy);
	}

	public Page<ProModelTlxy> findPage(Page<ProModelTlxy> page, ProModelTlxy proModelTlxy) {
		return super.findPage(page, proModelTlxy);
	}

	@Transactional(readOnly = false)
	public void save(ProModelTlxy proModelTlxy) {
		super.save(proModelTlxy);
	}

	@Transactional(readOnly = false)
	public void delete(ProModelTlxy proModelTlxy) {
		super.delete(proModelTlxy);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ProModelTlxy proModelTlxy) {
  	  dao.deleteWL(proModelTlxy);
  	}

	@Override
    public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ProProject proProject, ActYw actYw) {
        User user = UserUtils.getUser();
		ProModelTlxy proModelTlxy = new ProModelTlxy();
        if (!org.springframework.util.StringUtils.isEmpty(proModel.getId())) {
			proModelTlxy = super.getByProModelId(proModel.getId());
        }
		if(proModel.getSubTime()==null) {
			proModel.setSubTime(new Date());
		}
		proModel.setActYwId(actYw.getId());
		//默认选择创新训练
		if(proModel.getProCategory()==null) {
			proModel.setProCategory("1");
		}
		proModelTlxy.setProModel(proModel);
		if(proModelTlxy.getProModel()!=null){
			proModelTlxy.setPlanList(projectPlanService.findListByProjectId(proModelTlxy.getProModel().getId()));
		}

		model.addAttribute("proModel", proModel);
        model.addAttribute("proModelTlxy", proModelTlxy);
        model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), ""));
        model.addAttribute("cuser", user);
        model.addAttribute("isSubmit", 0);
        model.addAttribute("wprefix", IWparam.getFileTplPreFix());
        model.addAttribute("wtypes", Wtype.toJson());
        return super.applayForm(fpageType, model, request, response, proModel, proProject, actYw);
    }


	@Override
	public Page<ProModelTlxy> findDataPage(Page<ProModelTlxy> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProModelTlxy proModelTlxy) {
		List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
		List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
		List<String> recordIds = actTaskService.recordIds(act, gnodeIdList, actywId);

		List<String> delegateList = actYwGassignService.delegateIds(gnodeIdList, actywId);
		if(StringUtil.checkNotEmpty(delegateList)){
			recordIds.addAll(delegateList);
		}
		recordIds=removeDuplicate(recordIds);
		ProModel proModel = null;
		if (proModelTlxy.getProModel()==null) {
			proModel = new ProModel();
		} else {
			proModel = proModelTlxy.getProModel();
		}
		proModel.setGnodeId(gnodeId);
		proModelTlxy.setIds(recordIds);
		proModelTlxy.setPage(page);
		proModelTlxy.setProModel(proModel);
		if (recordIds.isEmpty()) {
			page.setList(new ArrayList<>(0));
		} else {
			//页面审核条件参数
			if(StringUtil.isNotEmpty(proModel.getProCategory())){
				List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
				proModelTlxy.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
				List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
				proModelTlxy.setProjectLevelList(levelList);
			}
			if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
				proModelTlxy.setOfficeIdList(officeList);
			}
			List<ProModelTlxy> list = dao.findListByIds(proModelTlxy);
			//证书
			boolean isScore=false;
			for (ProModelTlxy tlxy : list) {
				// 查询团队指导老师的名称，多个以逗号分隔
				ProModel tlxyProModel = tlxy.getProModel();
				List<Team> team = teamService.findTeamUserName(tlxyProModel.getTeamId());
				List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					tlxyProModel.getTeam().setuName(StringUtils.join(names, ","));
				}
				List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					tlxyProModel.getTeam().setEntName(StringUtils.join(stunames, ","));
				}
				ActYwGnode lastNode =null;
				boolean isScoreNode=false;
				if(!isScore){
					ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
					actYwAuditInfoIn.setPromodelId(tlxyProModel.getId());
					actYwAuditInfoIn.setGnodeId(actYwGnodes.get(0).getId());
					ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getGnodeByNextGnode(actYwAuditInfoIn);
					if(lastActYwAuditInfoIn==null){
						isScore=true;
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
						isScore=true;
					}
				}
				//如果上一个节点为评分节点 算出平均分
				getAcgScore(proModel, tlxyProModel, lastNode, isScoreNode);
				// 项目结果
				if (StringUtils.isNotBlank(tlxyProModel.getEndGnodeId()) && Const.YES.equals(tlxyProModel.getState())) {// 流程已结束
					getFinalResult(tlxyProModel);
				}
				if(tlxyProModel.getProcInsId()!=null){
					ActYwGnode actYwGnodeIndex = proActTaskService.getNodeByProInsId(tlxyProModel.getProcInsId());
					if(actYwGnodeIndex!=null){
						gnodeId=actYwGnodeIndex.getId();
					}
				}
				ActYwGnode actYwGnode=actYwGnodeService.get(gnodeId);
				if(actYwGnode.getIsDelegate()){
					ActYwGassign actYwGassign=new ActYwGassign();
					actYwGassign.setGnodeId(gnodeId);
					actYwGassign.setPromodelId(tlxyProModel.getId());
					//判断项目是否已经指派
					List<String> todoList = actYwGassignService.getTodoDelegateList(actYwGassign);
					if(StringUtil.checkNotEmpty(todoList)){
						tlxyProModel.setDelegateState(Const.YES);
					}else{
						tlxyProModel.setDelegateState(Const.NO);
					}
				}
				//添加审核方法参数
				addAuditMap(gnodeId, delegateList, tlxyProModel);

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
			//model.addAttribute("actYwStatusList", actYwStatusList);
		}
		if(model!=null){
			model.addAttribute("levelDict",TLXY_PRO_LEVEL_0000000263);
			model.addAttribute("autoShow",TLXY_PRO_LEVEL_0000000264);
			model.addAttribute("proModelTlxy", proModelTlxy);
		}
		return page;
	}

	public void addAuditMap(String gnodeId, List<String> delegateList, ProModel tlxyProModel) {
		Map<String,String> map= ProProcessDefUtils.getActByPromodelId(tlxyProModel.getId());
		tlxyProModel.setAuditMap(map);
		if(delegateList.contains(tlxyProModel.getId())){

			ActYwGassign actYwGassign=new ActYwGassign();
			actYwGassign.setRevUserId(UserUtils.getUserId());
			actYwGassign.setGnodeId(gnodeId);
			actYwGassign.setPromodelId(tlxyProModel.getId());
			ActYwGassign oldactYwGassign=actYwGassignService.getByActYwGassign(actYwGassign);
			if(oldactYwGassign!=null && (oldactYwGassign.getIsOver()==null || "0".equals(oldactYwGassign.getIsOver()))){
				Map<String,String> mapIndex=new HashMap<String,String>();
				mapIndex.put("status", "todo");
				mapIndex.put("gnodeId", gnodeId);
				mapIndex.put("taskName", "委派审核");
				mapIndex.put("proModelId",tlxyProModel.getId());
				tlxyProModel.setAuditMap(mapIndex);
			}
		}
	}

	public void getAcgScore(ProModel proModel, ProModel tlxyProModel, ActYwGnode lastNode, boolean isScoreNode) {
		if(isScoreNode){
			ActYwAuditInfo actYwAuditInfoIn = new ActYwAuditInfo();
			actYwAuditInfoIn.setPromodelId(tlxyProModel.getId());
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
			tlxyProModel.setgScore(gscore);
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
	public Page<ProModelTlxy> findQueryPage(Page<ProModelTlxy> page, Model model, String actywId, ActYw actYw, ProModelTlxy proModelTlxy) {
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		List<String> recordIds = actTaskService.queryRecordIds(act, actywId);
		//增加导入的数据
		ProModel proModel = null;
		if (org.springframework.util.StringUtils.isEmpty(proModelTlxy.getProModel())) {
			proModel = new ProModel();
		} else {
			proModel = proModelTlxy.getProModel();
		}
		proModel.setActYwId(actywId);
        proModelTlxy.setProModel(proModel);
        List<ProModel> importList = proModelService.findImportList(proModel);
        if (!importList.isEmpty()) {
            List<String> importIds = importList.stream().map(e -> e.getId()).collect(Collectors.toList());
            recordIds.addAll(importIds);
        }
		if(model!=null){
			model.addAttribute("levelDict",TLXY_PRO_LEVEL_0000000263);
			model.addAttribute("proModelTlxy", proModelTlxy);
		}

		return findPage(page, proModelTlxy, recordIds);
	}

	@Override
	public Page<ProModelTlxy> findAssignPage(Page<ProModelTlxy> page, Model model, String actywId, ActYw actYw, ProModelTlxy proModelTlxy) {
		String gnodeId = super.getGnodeId(proModelTlxy, actywId);
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		List<String> gnodeIdList = new ArrayList<String>();
		gnodeIdList.add(gnodeId);
		List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actywId);
		ProModel proModel = null;
		if (org.springframework.util.StringUtils.isEmpty(proModelTlxy.getProModel())) {
			proModel = new ProModel();
		} else {
			proModel = proModelTlxy.getProModel();
		}
		proModel.setActYwId(actywId);
		proModel.setGnodeId(gnodeId);
		proModelTlxy.setProModel(proModel);
		if(model!=null){
			model.addAttribute("levelDict",TLXY_PRO_LEVEL_0000000263);
			model.addAttribute("proModelTlxy", proModelTlxy);
		}
		return findAssignPage(page, proModelTlxy, recordIds);
	}

	public Page<ProModelTlxy> findAssignPage(Page<ProModelTlxy> page, ProModelTlxy t, List<String> recordIds) {
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
			List<ProModelTlxy> list = proModelTlxyDao.findListByIdsUnAudit(t);
			//证书
			Map<String,String> ass=getTaskAssigns(list, t.getProModel().getGnodeId());
			Map<String,List<SysCertInsVo>> map=getSysCertIns(list);
			for (ProModelTlxy model : list) {
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
				if (!names.isEmpty()) {
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

	private Map<String,String> getTaskAssigns(List<ProModelTlxy> subList, String gnodeid){
     Map<String,String> map=new HashMap<String,String>();
     List<String> param=new ArrayList<String>();
     if(subList.size()==0){
         return map;
     }
     for(ProModelTlxy p:subList){
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

	private Map<String,List<SysCertInsVo>> getSysCertIns(List<ProModelTlxy> subList){
     Map<String,List<SysCertInsVo>> map=new HashMap<String,List<SysCertInsVo>>();
     List<String> param=new ArrayList<String>();
     if(subList.size()==0){
         return map;
     }
     for(ProModelTlxy p:subList){
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

	public Page<ProModelTlxy> findPage(Page<ProModelTlxy> page, ProModelTlxy proModelTlxy, List<String> recordIds) {
		proModelTlxy.setIds(recordIds);
		proModelTlxy.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
			if(StringUtil.isNotEmpty(proModelTlxy.getProModel().getProCategory())){
				List<String> proList=Arrays.asList(proModelTlxy.getProModel().getProCategory().split(","));
				proModelTlxy.setProCategoryList(proList);
			}
			if(StringUtil.isNotEmpty(proModelTlxy.getProModel().getFinalStatus())){
				List<String> levelList=Arrays.asList(proModelTlxy.getProModel().getFinalStatus().split(","));
				proModelTlxy.setProjectLevelList(levelList);
			}
			if(proModelTlxy.getProModel().getDeuser()!=null && proModelTlxy.getProModel().getDeuser().getOffice()!=null
					&&StringUtil.isNotEmpty(proModelTlxy.getProModel().getDeuser().getOffice().getId())){
				List<String> officeList=Arrays.asList(proModelTlxy.getProModel().getDeuser().getOffice().getId().split(","));
				proModelTlxy.setOfficeIdList(officeList);
			}

			List<ProModelTlxy> list = dao.findListByIds(proModelTlxy);
            for (ProModelTlxy model : list) {
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
        }
        return page;
    }

	@Override
	public void audit(String gnodeId,String proModelId, Model model) {
		ProModelTlxy proModelTlxy = getByProModelId(proModelId);
		proModelTlxy.setPlanList(projectPlanService.findListByProjectId(proModelId));
		model.addAttribute("levelDict",TLXY_PRO_LEVEL_0000000263);
		model.addAttribute("proModelTlxy",proModelTlxy);
	}

	@Override
	//新增添加扩展表信息
	public void saveAddPro(ProModel proModel) {
	    ProModelTlxy curProModelTlxy = getByProModelId(proModel.getId());
        if(curProModelTlxy == null){
            ProModelTlxy proModelTlxy = new ProModelTlxy();
            proModelTlxy.setModelId(proModel.getId());
            proModelTlxy.setIsNewRecord(true);
            proModelTlxy.setId(IdGen.uuid());
            save(proModelTlxy);
        }else{
            curProModelTlxy.setIsNewRecord(false);
            curProModelTlxy.setModelId(proModel.getId());
            save(curProModelTlxy);
        }
	}

	@Override
	@Transactional(readOnly = false)
	public void auditByGateWay(ProModel proModel, String gnodeId, HttpServletRequest request) {

//		User user=UserUtils.getUser();
//		//根据节点查委派任务 该专家如果有委派任务 直接审核 写入审核记录表。
//		Boolean isHaveDelege=false;
//		if(isHaveDelege){
//			proModelService.auditDelegate(proModel, gnodeId);
//		}else {
			proModelService.auditWithGateWay(proModel, gnodeId);
//		}
	}

	@Override
	public String viewForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
		FormTheme formTheme = actYw.getFtheme();
		ProModelTlxy proModelTlxy = getByProModelId(proModel.getId());
		proModelTlxy.setPlanList(projectPlanService.findListByProjectId(proModel.getId()));
		model.addAttribute("levelDict",TLXY_PRO_LEVEL_0000000263);
		model.addAttribute("proModelTlxy",proModelTlxy);
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
		ProModelTlxy proModelTlxy = getByProModelId(proModel.getId());
		proModelTlxy.setProModel(proModel);
		if(proModelTlxy.getProModel()!=null){
			proModelTlxy.setPlanList(projectPlanService.findListByProjectId(proModelTlxy.getProModel().getId()));
		}
		model.addAttribute("proModelTlxy",proModelTlxy);
		model.addAttribute("proModelTypeList", DictUtils.getDictList("project_result_type"));
		model.addAttribute("levelDict",TLXY_PRO_LEVEL_0000000263);

	}

	@Override
	public void saveFormData(ProModel proModel, HttpServletRequest request) {

	}

	@Override
    public List<ExpProModelTlxyVo> exportData(Page<ProModelTlxy> page, ProModelTlxy proModelTlxy) {
		proModelTlxy.setPage(page);
        try {
            return dao.export(proModelTlxy);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


	@Override
	public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, ProModelTlxy proModelTlxy) {
	    proModelTlxy.setIds(pids);
	    proModelTlxy.setActYwId(actyw.getId());
	    Map<String, List<IWorkRes>> map = super.exportDataMap(request, response, tempPath, actyw, gnode, pids, proModelTlxy);
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
			expGfile.setClazz(ExpProModelTlxyVo.class);
            expGfile.setReqParam(new ItReqParam(request));
            ExportParams eparams = new ExportParams(expGfile.getFileName(), "大赛汇总", expGfile.getFileType());
            eparams.setDictHandler(new DictHandler());
            eparams.setDataHandler(new DataExpPmTlxyVoHandler(expGfile.getReqParam()));
            expGfile.setParam(eparams);
			IWorkFlow.expExcelByOs(expGfile, map.get(key));
		}
	}

	@Transactional(readOnly = false)
   public JSONObject saveStep1(ProModelTlxy proModelTlxy) {
		JSONObject json = new JSONObject();

       	ProModel proModel = proModelTlxy.getProModel();
       	ActYw actYw = actYwService.get(proModel.getActYwId());
       	if (actYw != null) {
           ProProject proProject = actYw.getProProject();
           if (proProject != null) {
               proModel.setProType(proProject.getProType());
               proModel.setType(proProject.getType());
			   proModel.setProjectLevelDict(TLXY_PRO_LEVEL_0000000263);
		   }
       	}
		proModel.setTenantId(TenantConfig.getCacheTenant());
		JedisUtils.publishMsg(ProModelTopic.TOPIC_ONE,proModel);
//       	proModelService.save(proModel);

		if (StringUtil.isNotEmpty(proModel.getLogoUrl())) {// logo有变动
			ProModel old=proModelService.get(proModel.getId());
			if (old == null){
				old = proModel;
			}
			String url = StringEscapeUtils.unescapeHtml4(proModel.getLogoUrl());
			String dblogo = sysAttachmentService.getLogoByProModelId(proModel.getId());
			if (dblogo == null){
				try {
					proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
				} catch (Exception e) {
					logger.error(ExceptionUtil.getStackTrace(e));
				}
			}
			if (dblogo != null && !dblogo.equals(proModel.getLogoUrl())){
				proSysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
				try {
					proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
				} catch (Exception e) {
					logger.error(ExceptionUtil.getStackTrace(e));
				}
			}
			/*String url = StringEscapeUtils.unescapeHtml4(proModel.getLogoUrl());
			if (old.getLogo() != null) {
				sysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
			} else {
				old.setLogo(new SysAttachment());
			}
			try {
				sysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
			} catch (Exception e) {
				e.printStackTrace();
			}*/

		}


		proModelTlxy.setModelId(proModel.getId());

       	super.save(proModelTlxy);
		if(StringUtil.isNotEmpty(proModelTlxy.getId())){
			projectPlanDao.deleteByProjectId(proModel.getId());
		}
		int sort = 0;
		for (ProjectPlan plan : proModelTlxy.getPlanList()) {
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
  	public JSONObject saveStep2(ProModelTlxy proModelTlxy) {
      	JSONObject json = new JSONObject();
      	ProModel proModel = proModelTlxy.getProModel();
      	if (StringUtil.isEmpty(proModel.getPName())) {
          	json.put(CoreJkey.JK_RET, 0);
          	json.put(CoreJkey.JK_MSG, "保存失败，项目名称为必填项");
			return json;
      	}
      	if (proModelTlxyDao.checkMdProName(proModel.getPName(), proModel.getId(), proModel.getType()) > 0) {
          	json.put(CoreJkey.JK_RET, 0);
          	json.put(CoreJkey.JK_MSG, "保存失败，该项目名称已经存在");
          	return json;
      	}
		List<TeamUserHistory> stus = proModel.getStudentList();
		List<TeamUserHistory> teas = proModel.getTeacherList();
		if ((stus != null && stus.size() > 0) || (teas != null && teas.size() > 0)) {
			commonService.disposeTeamUserHistoryOnSave(stus, teas, proModel.getActYwId(), proModel.getTeamId(), proModel.getId(), proModel.getYear());
		}
		ProModel model=proModelService.get(proModel.getId());
//		model.setpName(proModelTlxy.getProModel().getpName());
//		model.setProCategory(proModelTlxy.getProModel().getProCategory());
//		model.setIntroduction(proModelTlxy.getProModel().getIntroduction());
		model.setTeamId(proModelTlxy.getProModel().getTeamId());
		JedisUtils.publishMsg(ProModelTopic.TOPIC_TWO,model);
//		proModelService.save(model);
      	super.save(proModelTlxy);


		json.put(CoreJkey.JK_RET, 1);
		json.put(CoreJkey.JK_MSG, "保存成功");
      	return json;
  	}



    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProModelTlxy proModelTlxy) {
        List<ExpProModelTlxyVo> proModelMdGcVos = exportData(new Page<ProModelTlxy>(request, response), proModelTlxy);
        ExpGnodeFile expGfile = new ExpGnodeFile();
        expGfile.setFileName("大赛申报_汇总查询表");
        expGfile.setClazz(ExpProModelTlxyVo.class);
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
		ProModelTlxy proModelTlxy = getByProModelId(proModel.getId());
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
		if(proModel.getFinalStatus()!=null && proModel.getFinalStatus().equals(TLXY_PRO_LEVEL_0000000264)){
			proModelTlxy.setxCompetitionNumber(proModel.getCompetitionNumber());
		}else if(proModel.getFinalStatus()!=null && proModel.getFinalStatus().equals(TLXY_PRO_LEVEL_0000000265)){
			proModelTlxy.setpCompetitionNumber(proModel.getCompetitionNumber());
		}else if(proModel.getFinalStatus()!=null && proModel.getFinalStatus().equals(TLXY_PRO_LEVEL_0000000266)){
			proModelTlxy.setgCompetitionNumber(proModel.getCompetitionNumber());
		}
		String resultContent=request.getParameter("resultContent");
		String budgetDollar=request.getParameter("budgetDollar");
		String budget=request.getParameter("budget");
		String planStartDate=request.getParameter("planStartDate");
		String planEndDate=request.getParameter("planEndDate");
		String planContent=request.getParameter("planContent");
		String planStep=request.getParameter("planStep");
		String innovation=request.getParameter("innovation");
		proModelTlxy.setSource(source);
		proModelTlxy.setResultType(resultType);
		proModelTlxy.setResultContent(resultContent);
		proModelTlxy.setBudgetDollar(budgetDollar);
		proModelTlxy.setBudget(budget);
		proModelTlxy.setPlanStartDate(DateUtil.parseDate(planStartDate,DateUtil.FMT_YYYYMMDD_ZG));
		proModelTlxy.setPlanEndDate(DateUtil.parseDate(planEndDate,DateUtil.FMT_YYYYMMDD_ZG));
		proModelTlxy.setPlanContent(planContent);
		proModelTlxy.setPlanStep(planStep);
		proModelTlxy.setInnovation(innovation);
		save(proModelTlxy);
		//删除添加任务分工list
		proModelTlxy.setPlanList(proModel.getPlanList());
		if(StringUtil.isNotEmpty(proModelTlxy.getId())){
			projectPlanDao.deleteByProjectId(proModel.getId());
		}
		int sort = 0;
		for (ProjectPlan plan : proModelTlxy.getPlanList()) {
			plan.setSort(sort + "");
			plan.setId(IdGen.uuid());
			plan.setProjectId(proModel.getId());
			plan.setCreateDate(new Date());
			projectPlanDao.insert(plan);
			sort++;
		}
		return proModelService.saveProjectEdit(proModel,request);
	}

	@Override
	public void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId) {
		ProModelTlxy proModelTlxy = getByProModelId(proModelId);

		if(proModelTlxy.getProModel()!=null){
			proModelTlxy.setPlanList(projectPlanService.findListByProjectId(proModelTlxy.getProModel().getId()));
		}
		model.addAttribute("proModelTlxy",proModelTlxy);
		model.addAttribute("proModelTypeList", DictUtils.getDictList("project_result_type"));
		model.addAttribute("levelDict",TLXY_PRO_LEVEL_0000000263);
	}

	@Override
	public void addFrontParam(ProjectDeclareListVo v) {
//		ProModelTlxy proModelTlxy = getByProModelId(v.getId());
//
//		if(proModelTlxy.getProModel().getFinalStatus()!=null && proModelTlxy.getProModel().getFinalStatus().equals(TLXY_PRO_LEVEL_0000000264)){
//			v.setNumber(proModelTlxy.getProModel().getCompetitionNumber());
//		}else if(proModelTlxy.getProModel().getFinalStatus()!=null && proModelTlxy.getProModel().getFinalStatus().equals(TLXY_PRO_LEVEL_0000000265)){
//			v.setNumber(proModelTlxy.getpCompetitionNumber());
//		}else if(proModelTlxy.getProModel().getFinalStatus()!=null && proModelTlxy.getProModel().getFinalStatus().equals(TLXY_PRO_LEVEL_0000000266)){
//			v.setNumber(proModelTlxy.getgCompetitionNumber());
//		}

	}

	@Override
	public void indexTime(Map<String, String> lastpro) {
		if(StringUtil.isNotEmpty(lastpro.get("id"))){
			ProModelTlxy proModelTlxy = getByProModelId(lastpro.get("id"));
			if(proModelTlxy.getProModel().getFinalStatus()!=null && proModelTlxy.getProModel().getFinalStatus().equals(TLXY_PRO_LEVEL_0000000264)){
				lastpro.put("number",proModelTlxy.getProModel().getCompetitionNumber());
			}else if(proModelTlxy.getProModel().getFinalStatus()!=null && proModelTlxy.getProModel().getFinalStatus().equals(TLXY_PRO_LEVEL_0000000265)){
				lastpro.put("number",proModelTlxy.getpCompetitionNumber());
			}else if(proModelTlxy.getProModel().getFinalStatus()!=null && proModelTlxy.getProModel().getFinalStatus().equals(TLXY_PRO_LEVEL_0000000266)){
				lastpro.put("number",proModelTlxy.getgCompetitionNumber());
			}
		}

	}

	@Transactional(readOnly = false)
  	public void submit(ProModelTlxy proModelTlxy) {
      	sysAttachmentService.saveByVo(proModelTlxy.getProModel().getAttachMentEntity(), proModelTlxy.getProModel().getId(), FileTypeEnum.S11,
              FileStepEnum.S1102);
      	teamUserHistoryDao.updateFinishAsStart(proModelTlxy.getProModel().getId());
		//保存优秀展示
//		ProModel proModel=proModelTlxy.getProModel();
//		String ecxType = ExcellentShow.Type_Project;
//		if ("7,".equals(proModel.getProType())) {
//			ecxType = ExcellentShow.Type_Gcontest;
//		}
//		excellentShowService.saveExcellentShow(proModel.getIntroduction(), proModel.getTeamId(),
//				ecxType, proModel.getId(), proModel.getActYwId());

      	ActYw actYw = actYwService.get(proModelTlxy.getProModel().getActYwId());
		Date newSubTime=null;
		try {
			Date proModelSubTime=proModelTlxy.getProModel().getSubTime();
			String subTimeStr=DateUtil.formatDate(proModelSubTime);
			String nowTimeStr=DateUtil.getTime();
			subTimeStr = subTimeStr+" "+nowTimeStr;
			newSubTime=DateUtil.parseDate(subTimeStr,DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
		} catch (ParseException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		if(newSubTime!=null){
			proModelTlxy.getProModel().setSubTime(newSubTime);
		}
//		proModelTlxy.getProModel().setSubTime(new Date());
		String num ="";
		try {
			 num = NumRuleUtils.getNumberText(proModelTlxy.getProModel().getActYwId(),proModelTlxy.getProModel().getYear()
					 ,proModelTlxy.getProModel().getSubTime(),false);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		if(StringUtil.isEmpty(num)){
			num=IdUtils.getProjectNumberByDb();
		}
		//申报后默认为校级
		proModelTlxy.getProModel().setFinalStatus(TLXY_PRO_LEVEL_0000000264);
		proModelTlxy.getProModel().setCompetitionNumber(num);
		proModelService.save(proModelTlxy.getProModel());
		proModelTlxyDao.updateXcompetitionnumber(proModelTlxy.getProModel().getId(),num);
      	super.start(proModelTlxy, actYw, "pro_model");
  	}
	@Transactional(readOnly = false)
	public JSONObject ajaxSaveNum(String proModelId, String num, String type) {
		JSONObject js=new JSONObject();
		if(TLXY_PRO_LEVEL_0000000266.equals(type)){
			int k=dao.checkGcompetitionnumber(num);
			if(k>0){
				js.put(CoreJkey.JK_RET,0);
				js.put(CoreJkey.JK_MSG,"该编号已经存在");
				return js;
			}
			proModelTlxyDao.updateGcompetitionnumber(proModelId,num);
		}else if(TLXY_PRO_LEVEL_0000000265.equals(type)){
			int k=dao.checkPcompetitionnumber(num);
			if(k>0){
				js.put(CoreJkey.JK_RET,0);
				js.put(CoreJkey.JK_MSG,"该编号已经存在");
				return js;
			}
			proModelTlxyDao.updatePcompetitionnumber(proModelId,num);
		}
		proModelTlxyDao.updatePnum(num,proModelId);
		js.put(CoreJkey.JK_RET,1);
		return js;
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
    public List<ProModelTlxy> findListByQuery(HttpServletRequest request, ActYw actYw, String year, String officeId,
            String gnodeId) {
        if(actYw == null){
            return null;
        }

        List<ProModelTlxy> list = new ArrayList<>();
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
            list = dao.findPmByIds(new ProModelTlxy(proModel));
        }else{
            proModel = new ProModel();
            proModel.setIds(recordIds);
            list = dao.findPmByIds(new ProModelTlxy(proModel));
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

}