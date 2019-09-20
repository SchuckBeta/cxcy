package com.oseasy.pro.modules.workflow.service;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.entity.ProModelMdGcHistory;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelMdGcHistoryService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.utils.ProProcessDefUtils;
import com.oseasy.pro.modules.promodel.vo.ExpGnodeFile;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.WorkFlowService;
import com.oseasy.pro.modules.workflow.dao.ProModelMdGcDao;
import com.oseasy.pro.modules.workflow.dao.ProvinceProModelGcDao;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModelGc;
import com.oseasy.pro.modules.workflow.handler.DataExpVoHandler;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;
import net.sf.json.JSONObject;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 互联网+大赛模板Service.
 * @author zy
 * @version 2018-06-05
 */
@Service
@Transactional(readOnly = true)
public class provinceProModelGcService extends WorkFlowService<ProvinceProModelGcDao, ProvinceProModelGc, ExpProModelVo> implements IWorkFlow<ProvinceProModelGc, ProModel, ExpProModelVo> {
	@Override
	public void addFrontParam(ProjectDeclareListVo v) {

	}

	@Override
	public void indexTime(Map<String, String> lastpro) {

	}

	public final static Logger logger = Logger.getLogger(provinceProModelGcService.class);
	@Autowired
	 ProModelService proModelService;
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
	private ProModelMdGcHistoryService proModelMdGcHistoryService;

    @Override
    public WorkFlowService<ProvinceProModelGcDao, ProvinceProModelGc, ExpProModelVo> setWorkService() {
        return this;
    }

    @Override
    public IWorkFlow<ProvinceProModelGc, ProModel, ExpProModelVo> setIWorkFlow() {
        return this;
    }
	@Override
	public ProvinceProModelGc get(String id) {
		return super.get(id);
	}

	@Override
	public List<ProvinceProModelGc> findList(ProvinceProModelGc proModelMdGc) {
		return super.findList(proModelMdGc);
	}

	@Override
	public Page<ProvinceProModelGc> findPage(Page<ProvinceProModelGc> page, ProvinceProModelGc proModelMdGc) {
		return super.findPage(page, proModelMdGc);
	}

	@Transactional(readOnly = false)
	public void save(ProvinceProModelGc proModelMdGc) {
		super.save(proModelMdGc);
	}

	@Transactional(readOnly = false)
	public void delete(ProvinceProModelGc proModelMdGc) {
		super.delete(proModelMdGc);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ProvinceProModelGc proModelMdGc) {
  	  dao.deleteWL(proModelMdGc);
  	}

	@Override
    public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ProProject proProject, ActYw actYw) {
        User user = UserUtils.getUser();
		ProvinceProModelGc proModelMdGc = new ProvinceProModelGc();
        if (!org.springframework.util.StringUtils.isEmpty(proModel.getId())) {
			proModelMdGc = super.getByProModelId(proModel.getId());
        }
		model.addAttribute("proModel", proModel);
        model.addAttribute("proModelMdGc", proModelMdGc);
        model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), ""));
        model.addAttribute("cuser", user);
        model.addAttribute("isSubmit", 0);
        model.addAttribute("wprefix", IWparam.getFileTplPreFix());
        model.addAttribute("wtypes", Wtype.toJson());
        return super.applayForm(fpageType, model, request, response, proModel, proProject, actYw);
    }


	@Override
	public Page<ProvinceProModelGc> findDataPage(Page<ProvinceProModelGc> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProvinceProModelGc proModelMdGc) {
		List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
		List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
		List<String> recordIds = actTaskService.recordIds(act, gnodeIdList, actywId);
		ProModel proModel = null;
		if (proModelMdGc.getProModel()==null) {
			proModel = new ProModel();
		} else {
			proModel = proModelMdGc.getProModel();
		}
		proModel.setGnodeId(gnodeId);
		proModelMdGc.setIds(recordIds);
		proModelMdGc.setPage(page);
		proModelMdGc.setProModel(proModel);
		if (recordIds.isEmpty()) {
			page.setList(new ArrayList<>(0));
		} else {
			List<ProvinceProModelGc> list = dao.findListByIds(proModelMdGc);
			//证书
			//int i=0;
			for (ProvinceProModelGc mdGc : list) {
				// 查询团队指导老师的名称，多个以逗号分隔
				ProModel mdGcProModel = mdGc.getProModel();
				List<Team> team = teamService.findTeamUserName(mdGcProModel.getTeamId());
				List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					mdGcProModel.getTeam().setuName(StringUtils.join(names, ","));
				}
				// 项目结果
				if (StringUtils.isNotBlank(mdGcProModel.getEndGnodeId()) && Const.YES.equals(mdGcProModel.getState())) {// 流程已结束
					ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
					actYwAuditInfo.setGnodeId(mdGcProModel.getEndGnodeId());
					actYwAuditInfo.setPromodelId(mdGcProModel.getId());
					ActYwGnode endNode = actYwGnodeService.get(mdGcProModel.getEndGnodeId());
					mdGcProModel.setFinalResult((endNode != null ? endNode.getName() : "")
							+ proModelService.getStateByAuditInfo(actYwAuditInfoService.findList(actYwAuditInfo)));
				}
				//获奖情况
				ProModelMdGcHistory proModelMdGcHistory=new ProModelMdGcHistory();
				proModelMdGcHistory.setGnodeId(gnodeId);
				proModelMdGcHistory.setPromodelId(mdGcProModel.getId());
				ProModelMdGcHistory inProModelMdGcHistory=proModelMdGcHistoryService.getProModelMdGcHistory(proModelMdGcHistory);
				if(inProModelMdGcHistory!=null){
					mdGc.setProModelMdGcHistory(inProModelMdGcHistory);
				}
				// 项目结果
				if (StringUtils.isNotBlank(mdGc.getProModel().getEndGnodeId()) && Const.YES.equals(mdGc.getProModel().getState())) {// 流程已结束
					getFinalResult(mdGc);
				}
				//添加审核方法参数
				Map<String,String>map= ProProcessDefUtils.getActByPromodelId(mdGcProModel.getId());
				mdGcProModel.setAuditMap(map);
				mdGc.setProModel(mdGcProModel);

			}
			page.setList(list);
		}

		//根据gnodeId得到下一个节点是否为网关，是否需要网关状态
		List<ActYwStatus> actYwStatusList = proModelService.getActYwStatus(gnodeId);
		if (actYwStatusList != null&& model!=null) {
			model.addAttribute("actYwStatusList", actYwStatusList);
		}
		if(model!=null){
			model.addAttribute("proModelMdGc", proModelMdGc);
		}
		return page;
	}

	@Override
	public Page<ProvinceProModelGc> findQueryPage(Page<ProvinceProModelGc> page, Model model, String actywId, ActYw actYw, ProvinceProModelGc proModelMdGc) {
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
//		List<String> recordIds = actTaskService.queryRecordIds(act, actywId);
		//增加导入的数据
		ProModel proModel = null;
		if (org.springframework.util.StringUtils.isEmpty(proModelMdGc.getProModel())) {
			proModel = new ProModel();
		} else {
			proModel = proModelMdGc.getProModel();
		}
		proModel.setActYwId(actywId);
		proModelMdGc.setActywId(actywId);
		proModelMdGc.setProModel(proModel);
		if(model!=null){
			model.addAttribute("proModelMdGc", proModelMdGc);
		}
//		return findPage(page, proModelMdGc, recordIds);
		return findPageView(page, proModelMdGc);
	}

	public Page<ProvinceProModelGc> findPageView(Page<ProvinceProModelGc> page, ProvinceProModelGc proModelMdGc) {
		proModelMdGc.setPage(page);
//			List<ProModelMdGc> list = dao.findListByIds(proModelMdGc);
			List<ProvinceProModelGc> list = dao.findListByView(proModelMdGc);
			for (ProvinceProModelGc model : list) {

				// 查询团队指导老师的名称，多个以逗号分隔
				List<Team> team = teamService.findTeamUserName(model.getProModel().getTeamId());
				List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
						.collect(Collectors.toList());
				if (!names.isEmpty()) {
					model.getProModel().getTeam().setuName(StringUtils.join(names, ","));
				}
				// 项目结果
				if (StringUtils.isNotBlank(model.getProModel().getEndGnodeId()) && Const.YES.equals(model.getProModel().getState())) {// 流程已结束
					getFinalResult(model);
				}
				//获奖情况
				ProModelMdGcHistory proModelMdGcHistory=new ProModelMdGcHistory();
				proModelMdGcHistory.setPromodelId(model.getProModel().getId());
				List<ProModelMdGcHistory> proModelMdGcHistoryList=proModelMdGcHistoryService.getProModelMdGcHistoryList(proModelMdGcHistory);
				if(proModelMdGcHistoryList!=null){
					model.setProModelMdGcHistoryList(proModelMdGcHistoryList);
				}
				//添加审核方法参数
				Map<String,String>map= ProProcessDefUtils.getActByPromodelId(model.getProModel().getId());
				model.getProModel().setAuditMap(map);
			}
			page.setList(list);

		return page;
	}

	@Override
	public Page<ProvinceProModelGc> findPage(Page<ProvinceProModelGc> page, ProvinceProModelGc proModelMdGc, List<String> recordIds) {
		proModelMdGc.setIds(recordIds);
		proModelMdGc.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
			List<ProvinceProModelGc> list = dao.findListByIds(proModelMdGc);
            for (ProvinceProModelGc model : list) {

                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(model.getProModel().getTeamId());
                List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!names.isEmpty()) {
                    model.getProModel().getTeam().setuName(StringUtils.join(names, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(model.getProModel().getEndGnodeId()) && Const.YES.equals(model.getProModel().getState())) {// 流程已结束
                    getFinalResult(model);
                }
				//获奖情况
				ProModelMdGcHistory proModelMdGcHistory=new ProModelMdGcHistory();
				proModelMdGcHistory.setPromodelId(model.getProModel().getId());
				List<ProModelMdGcHistory> proModelMdGcHistoryList=proModelMdGcHistoryService.getProModelMdGcHistoryList(proModelMdGcHistory);
				if(proModelMdGcHistoryList!=null){
					model.setProModelMdGcHistoryList(proModelMdGcHistoryList);
				}
				//添加审核方法参数
				Map<String,String>map= ProProcessDefUtils.getActByPromodelId(model.getProModel().getId());
				model.getProModel().setAuditMap(map);
            }
            page.setList(list);
        }
        return page;
    }

	@Override
	public Page<ProvinceProModelGc> findAssignPage(Page<ProvinceProModelGc> page, Model model, String actywId, ActYw actYw, ProvinceProModelGc proModelMdGc) {
		return null;
	}

	@Override
	public void audit(String gnodeId,String proModelId, Model model) {
		ProModel proModel = proModelService.get(proModelId);
		ProModelMdGcHistory proModelMdGcHistory=new ProModelMdGcHistory();
		proModelMdGcHistory.setGnodeId(gnodeId);
		proModelMdGcHistory.setPromodelId(proModelId);
		ProModelMdGcHistory inProModelMdGcHistory=proModelMdGcHistoryService.getProModelMdGcHistory(proModelMdGcHistory);
		if(inProModelMdGcHistory!=null){
			proModel.setResult(inProModelMdGcHistory.getResult());
		}

		model.addAttribute("proModelMdGc", getByProModelId(proModelId));
		model.addAttribute("proModel", proModel);
		proModelService.audit(gnodeId,proModelId,model);
	}

	@Override
	public void saveAddPro(ProModel proModel) {
		ProvinceProModelGc curProModelMdGc = getByProModelId(proModel.getId());
        if(curProModelMdGc == null){
			ProvinceProModelGc proModelMdGc = new ProvinceProModelGc();
            proModelMdGc.setModelId(proModel.getId());
            proModelMdGc.setIsNewRecord(true);
            proModelMdGc.setId(IdGen.uuid());
            proModelMdGc.setMembers(proModel.getMembers());
            proModelMdGc.setTeachers(proModel.getTeachers());
            proModelMdGc.setBusinfos(proModel.getBusinfos());
            save(proModelMdGc);
        }else{
            curProModelMdGc.setIsNewRecord(false);
            curProModelMdGc.setModelId(proModel.getId());
            save(curProModelMdGc);
        }
	}

	@Override
	@Transactional(readOnly = false)
	public void auditByGateWay(ProModel proModel, String gnodeId, HttpServletRequest request) {
		ProModelMdGcHistory proModelMdGcHistory=new ProModelMdGcHistory();
		if(StringUtils.isNotEmpty(proModel.getResult())){
			String type=request.getParameter("awardType");
			proModelMdGcHistory.setGnodeId(gnodeId);
			proModelMdGcHistory.setPromodelId(proModel.getId());
			ProModelMdGcHistory oldProModelMdGcHistory=proModelMdGcHistoryService.getProModelMdGcHistory(proModelMdGcHistory);
			if(oldProModelMdGcHistory!=null){
				proModelMdGcHistory=oldProModelMdGcHistory;
			}
			proModelMdGcHistory.setType(type);
			proModelMdGcHistory.setResult(proModel.getResult());
			proModelMdGcHistoryService.save(proModelMdGcHistory);
			ProvinceProModelGc proModelMdGc=dao.getByProModelId(proModel.getId());
			proModelMdGc.setType(type);
			proModelMdGc.setResult(proModel.getResult());
			save(proModelMdGc);
		}else{
			ProModelMdGcHistory oldProModelMdGcHistory=proModelMdGcHistoryService.getProModelMdGcHistory(proModelMdGcHistory);
			if(oldProModelMdGcHistory!=null){
				proModelMdGcHistoryService.delete(oldProModelMdGcHistory);
			}
			ProvinceProModelGc proModelMdGc=dao.getByProModelId(proModel.getId());
			proModelMdGc.setType("");
			proModelMdGc.setResult("");
			save(proModelMdGc);
		}
		if(StringUtils.isNotEmpty(proModel.getGrade())) {
			proModelService.auditWithGateWay(proModel, gnodeId);
			ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
			actYwAuditInfo.setPromodelId(proModel.getId());
			actYwAuditInfo.setGnodeId(gnodeId);
			ActYwAuditInfo inActYwAuditInfo = actYwAuditInfoService.getInAudit(actYwAuditInfo);
			if(StringUtils.isNotEmpty(proModel.getResult())){
				proModelMdGcHistory.setAuditId(inActYwAuditInfo.getId());
				proModelMdGcHistoryService.save(proModelMdGcHistory);
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void gcontestEdit(ProModel proModel, HttpServletRequest request, Model model){
		//获奖情况
		ProModelMdGcHistory proModelMdGcHistory=new ProModelMdGcHistory();
		proModelMdGcHistory.setPromodelId(proModel.getId());
		List<ProModelMdGcHistory> proModelMdGcHistoryList=proModelMdGcHistoryService.getProModelMdGcHistoryList(proModelMdGcHistory);
		model.addAttribute("proModelMdGcHistoryList",proModelMdGcHistoryList);
		model.addAttribute("proModelMdGc", getByProModelId(proModel.getId()));
	}

	@Override
	public void projectEdit(ProModel proModel, HttpServletRequest request, Model model) {

	}

	@Override
	@Transactional(readOnly = false)
	public void saveFormData(ProModel proModel, HttpServletRequest request) {
		String[] idList=request.getParameterValues("proModelMdGcHistory.id");
		String[] resultList=request.getParameterValues("proModelMdGcHistory.result");
		if(idList!=null &&idList.length>0){
			for(int i=0;i<idList.length;i++){
				proModelMdGcHistoryService.updateAward(idList[i],resultList[i]);
			}

		}
	}

    @Override
    public List<ExpProModelVo> exportData(Page<ProvinceProModelGc> page, ProvinceProModelGc proModelMdGc) {
        proModelMdGc.setPage(page);
        try {
            return dao.export(proModelMdGc);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
	public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, ProvinceProModelGc proModelMdGc) {
		Map<String, List<IWorkRes>> map = super.exportDataMap(request, response, tempPath, actyw, gnode, pids, proModelMdGc);
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
            ExportParams eparams = new ExportParams(expGfile.getFileName(), "大赛汇总", expGfile.getFileType());
            eparams.setDictHandler(new DictHandler());
            eparams.setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
            expGfile.setParam(eparams);
			IWorkFlow.expExcelByOs(expGfile, map.get(key));
		}
	}

    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProvinceProModelGc proModelMdGc) {
        List<ExpProModelVo> proModelMdGcVos = exportData(new Page<ProvinceProModelGc>(request, response), proModelMdGc);
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
		ProvinceProModelGc proModelMdGc=new ProvinceProModelGc();
		proModelMdGc.setModelId(proModel.getId());
		save(proModelMdGc);
		return js;
	}

	@Override
	@Transactional(readOnly = false)
	public JSONObject saveProjectEdit(ProModel proModel, HttpServletRequest request) throws Exception {
		return proModelService.saveProjectEdit(proModel,request);
	}

	@Override
	public void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId) {

	}

//	// 自定义导出学院申报汇总表
//	private void expGnodeFileByOffice(String rootpath, String filepath, String oname, List<ProModelMdGcVO> list) {
//		File dirFile = new File(filepath);
//		if (!dirFile.exists()) {
//			dirFile.mkdirs();
//		}
//		File file = new File(filepath + File.separator + "大学生创新创业训练计划申报汇总表_" + oname + ".xlsx");
//		ExportParams params = new ExportParams("大学生创新创业训练计划申报汇总表", "项目汇总", ExcelType.XSSF);
//		params.setSecondTitle("学院名称：    (盖章)");
//		params.setFreezeCol(2);
//		Map<String, Object> map = new HashMap<>();
//		map.put(NormalExcelConstants.DATA_LIST, list);
//		map.put(NormalExcelConstants.CLASS, ProModelMdGcVO.class);
//		map.put(NormalExcelConstants.PARAMS, params);
//		map.put(NormalExcelConstants.FILE_NAME, "大学生创新创业训练计划申报汇总表_" + oname);
//		try {
//			file.createNewFile();
//			FileOutputStream fos = new FileOutputStream(file);
//			Workbook workbook = ExcelExportUtil.exportExcel(params, ProModelMdGcVO.class, list);
//			workbook.write(fos);
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}