package com.oseasy.pro.modules.proprojectmd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwNode;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormStyleType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.cert.dao.SysCertInsDao;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.entity.ProMidSubmit;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProCloseSubmitService;
import com.oseasy.pro.modules.promodel.service.ProMidSubmitService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FormPage;
import com.oseasy.pro.modules.proprojectmd.dao.ProModelMdDao;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.WtplType;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.tpl.vo.impl.WparamApply;
import com.oseasy.pro.modules.tpl.vo.impl.WparamReport;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.WorkFlowService;
import com.oseasy.pro.modules.workflow.vo.ExpProModelMdVo;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.utils.NumRuleUtils;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * proProjectMdService.
 * @author zy
 * @version 2017-09-18
 */
@Service
@Transactional(readOnly = true)
public class ProModelMdService extends WorkFlowService<ProModelMdDao, ProModelMd, ExpProModelVo> implements IWorkFlow<ProModelMd, ProModel, ExpProModelVo> {
    public final static Logger logger = Logger.getLogger(ProModelMdService.class);
	@Autowired
	private SysCertInsDao sysCertInsDao;
	@Autowired
	private ProModelMdDao proModelMdDao;
	@Autowired
	ProModelService proModelService;
	@Autowired
	ActYwGnodeService actYwGnodeService;
	@Autowired
	ActTaskService actTaskService;
	@Autowired
	ActYwService actYwService;
	@Autowired
	UserService userService;
	@Autowired
	SystemService systemService;
	@Autowired
	private RuntimeService runtimeService;
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
	private ExcellentShowService excellentShowService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ProMidSubmitService proMidSubmitService;

	@Autowired
	private ProCloseSubmitService proCloseSubmitService;
	@Autowired
	private ProjectDeclareService projectDeclareService;

    /* (non-Javadoc)
     * @see com.oseasy.pro.modules.workflow.IWorkFlow#getTClass()
     */
    @Override
    public Class<ProModelMd> getTClass() {
        return ProModelMd.class;
    }

    @Override
    public WorkFlowService<ProModelMdDao, ProModelMd, ExpProModelVo> setWorkService() {
        return this;
    }

    /* (non-Javadoc)
     * @see com.oseasy.pro.modules.workflow.WorkFlowService#setIWorkFlow()
     */
    @Override
    public IWorkFlow<ProModelMd, ProModel, ExpProModelVo> setIWorkFlow() {
        return this;
    }

	@Transactional(readOnly = false)
	public JSONObject saveModify(ProModelMd proModelMd) throws Exception{
		JSONObject js=new JSONObject();
		List<TeamUserHistory> stus=proModelMd.getProModel().getStudentList();
		List<TeamUserHistory> teas=proModelMd.getProModel().getTeacherList();
		String actywId=proModelMd.getProModel().getActYwId();
		String teamId=proModelMd.getProModel().getTeamId();
		String proId=proModelMd.getProModel().getId();
		js=commonService.checkProjectOnModify(stus,teas,proId, actywId,proModelMd.getProModel().getProCategory(), teamId,proModelMd.getProModel().getYear());
		if ("0".equals(js.getString("ret"))) {
			return js;
		}
		proModelService.save(proModelMd.getProModel());
		this.save(proModelMd);
		commonService.disposeTeamUserHistoryForModify(stus, teas, actywId, teamId, proId);
		sysAttachmentService.saveByVo(proModelMd.getProModel().getAttachMentEntity(), proModelMd.getProModel().getId());
		js.put("msg", "保存成功");
		return js;
	}

	//判断是否需要重新保存附件,true 需要保存
	private boolean checkFile(String pid,AttachMentEntity a) {
		SysAttachment s=new SysAttachment();
		s.setUid(pid);
		List<SysAttachment> list= sysAttachmentService.getFiles(s);
		if (list==null||list.size()==0||list.size()>1) {
			return true;
		}else{
			if (a!=null&&a.getFielFtpUrl()!=null&&a.getFielFtpUrl().size()==1&&a.getFielFtpUrl().get(0).equals(list.get(0).getUrl())) {
				return false;
			}else{
				return true;
			}
		}

	}
	public ProModelMd get(String id) {
		return super.get(id);
	}

	public List<ProModelMd> findList(ProModelMd proModelMd) {
		return super.findList(proModelMd);
	}

	public Page<ProModelMd> findPage(Page<ProModelMd> page, ProModelMd proModelMd) {
		return super.findPage(page, proModelMd);
	}

	@Transactional(readOnly = false)
	public JSONObject ajaxSaveProModelMd(ProModelMd proModelMd) {
		JSONObject js=new JSONObject();
		ProModel proModel=proModelMd.getProModel();
		/*if (StringUtil.isEmpty(proModel.getCompetitionNumber())) {
			proModel.setCompetitionNumber(IdUtils.getGContestNumberByDb());
		}*/
		if(StringUtil.isEmpty(proModel.getPName())){
			js.put("ret", 0);
			js.put("msg", "保存失败，项目名称为必填项");
			return js;
		}
		if (proModelMdDao.checkMdProName(proModel.getPName(), proModel.getId(),proModel.getType())>0) {
			js.put("ret", 0);
			js.put("msg", "保存失败，该项目名称已经存在");
			return js;
		}
		proModelService.save(proModel);
		proModelMd.setModelId(proModel.getId());
		super.save(proModelMd);
		js.put("ret", 1);
		js.put("msg", "保存成功");
		return js;
	}


	/**
	 * @param proModelMd
	 * @return
	 */
	@Transactional(readOnly = false)
	public String saveProModelMd(ProModelMd proModelMd) {
		ProModel proModel=proModelMd.getProModel();
		if (checkFile(proModel.getId(), proModel.getAttachMentEntity())) {//附件
			SysAttachment s=new SysAttachment();
			s.setUid(proModel.getId());
			s.setType(FileTypeEnum.S10);
			s.setFileStep(FileStepEnum.S2000);
			sysAttachmentService.deleteByCdn(s);
			Map<String, SysAttachment> map=sysAttachmentService.saveByVo(proModel.getAttachMentEntity(),proModel.getId(), FileTypeEnum.S10, FileStepEnum.S2000);
			if (map!=null&&proModel.getAttachMentEntity()!=null) {
				proModelMd.setFileUrl(map.get(proModel.getAttachMentEntity().getFielFtpUrl().get(0)).getUrl());
				proModelMd.setFileId(map.get(proModel.getAttachMentEntity().getFielFtpUrl().get(0)).getId());
			}
		}else{
			if (proModel.getAttachMentEntity()!=null) {
				proModelMd.setFileUrl(proModel.getAttachMentEntity().getFielFtpUrl().get(0));
			}

		}
		//保存团队的学分分配权重
		commonService.disposeTeamUserHistoryOnSave(proModel.getTeamUserHistoryList(), proModel.getActYwId(),proModel.getTeamId(),proModel.getId(),proModel.getYear());

		//proModelService.save(proModel);
		//proModelMd.setModelId(proModel.getId());
		//super.save(proModelMd);
		return "保存成功";
	}
	@Transactional(readOnly = false)
	public JSONObject submit(ProModelMd proModelMd,JSONObject js) {
		ProModel proModel=proModelMd.getProModel();
		ActYw actYw=actYwService.get(proModel.getActYwId());

		js=commonService.checkProjectApplyOnSubmit(
				proModel.getId(),proModel.getActYwId(), proModel.getProCategory(),proModel.getTeamId(),proModel.getYear());
		if ("0".equals(js.get("ret"))) {
			return js;
		}

		//保存编号根据编号规则来。
		String num ="";
		try {
			 num = NumRuleUtils.getNumberText(proModel.getActYwId(),proModel.getYear(),proModel.getProCategory(),false);
		}catch(Exception e){
			js.put("ret", 0);
			js.put("msg", "未设置正确编号规则，请联系管理员设置编号规则!");
			return js;
		}
		if(StringUtil.isEmpty(num)){
			js.put("ret", 0);
			js.put("msg", "未设置编号规则，请联系管理员设置编号规则!");
			return js;
		}
		proModel.setCompetitionNumber(num);
		List<String> roles=new ArrayList<String>();
		//启动大赛工作流
		ProProject proProject=actYw.getProProject();
		String nodeRoleId=actTaskService.getProcessStartRoleName(ActYw.getPkey(actYw));  //从工作流中查询 下一步的角色集合
		String roleId=nodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
		Role role= systemService.getNamebyId(roleId);
		if (role!=null) {
			//启动节点
			String roleName=role.getName();
			if (roleName.contains(SysIds.ISCOLLEGE.getRemark())||roleName.contains(SysIds.ISMS.getRemark())) {
				roles=userService.findListByRoleIdAndOffice(role.getId(),proModel.getDeclareId());
			}else{
				roles=userService.findListByRoleId(role.getEnname());
			}
			if (roles.size()>0) {
				Map<String,Object> vars=new HashMap<String,Object>();
				vars=proModel.getVars();
				vars.put(nodeRoleId+"s",roles);
				String key= ActYw.getPkey(actYw);
				String userId = UserUtils.getUser().getId();
				identityService.setAuthenticatedUserId(userId);
				ProcessInstance procIns=runtimeService.startProcessInstanceByKeyAndTenantId(key, "pro_model:"+proModel.getId(),vars, TenantConfig.getCacheTenant());
				//流程id返写业务表
				if (procIns!=null) {
					Act act = new Act();
					act.setBusinessTable("pro_model");// 业务表名
					act.setBusinessId(proModel.getId());	// 业务表ID
					act.setProcInsId(procIns.getId());
					actDao.updateProcInsIdByBusinessId(act);
					if (checkFile(proModel.getId(), proModel.getAttachMentEntity())) {//附件
						SysAttachment s=new SysAttachment();
						s.setUid(proModel.getId());
						s.setType(FileTypeEnum.S10);

						s.setFileStep(FileStepEnum.S2000);
						sysAttachmentService.deleteByCdn(s);


						if (proModel.getAttachMentEntity() != null && proModel.getAttachMentEntity().getFielFtpUrl() != null && proModel.getAttachMentEntity().getFielFtpUrl().size() > 0) {
							List<String> titleList = proModel.getAttachMentEntity().getFielTitle();
							if (StringUtil.checkNotEmpty(titleList)) {
								for (String titleName : titleList) {
									int i = 0;
									for (String titleNameIn : titleList) {
										if (titleNameIn.equals(titleName)) {
											i++;
										}
									}
									if (i > 1) {
										js.put("ret",0);
										js.put("msg","上传附件中含有相同附件！");
										return  js;
									}
								}
							}
						}
						sysAttachmentService.saveByVo(proModel.getAttachMentEntity(),proModel.getId(), FileTypeEnum.S10, FileStepEnum.S2000);

					}else{
						proModelMd.setFileUrl(proModel.getAttachMentEntity().getFielFtpUrl().get(0));
					}
					proModel.setProcInsId(act.getProcInsId());
					proModel.setSubTime(new Date());
					proModel.setSubStatus(Const.YES);
					proModel.setProType(proProject.getProType());
					proModel.setType(proProject.getType());
					proModelService.save(proModel);
					Team team=teamService.get(proModel.getTeamId());
					commonService.disposeTeamUserHistoryOnSubmit(proModel.getTeamUserHistoryList(), proModel.getActYwId(),proModel.getTeamId(),proModel.getId(),proModel.getYear());

					excellentShowService.saveExcellentShow(team==null?"":team.getProjectIntroduction(), proModel.getTeamId(),ExcellentShow.Type_Project,proModel.getId(),proModel.getActYwId());//保存优秀展示
					js.put("msg","恭喜您，申报成功！");
					return  js;
				}else{
					js.put("ret",0);
					js.put("msg","流程配置故障（审核流程未启动），请联系管理员");
					return  js;
				}
			}
		}
		js.put("ret",0);
		js.put("msg","流程配置故障（流程角色未配置），请联系管理员");
		return  js;
	}

	@Transactional(readOnly = false)
	public void delete(ProModelMd proModelMd) {
		super.delete(proModelMd);
	}

	/*
	 * 初始化Word参数.
	 * @param xyteachers 校园导师
	 * @param qyteachers 企业导师
	 * @param proModelMd 项目
	 * @param team 团队
	 * @param students 团队成员  @return IWparam
	*/
	public IWparam initIWparam(String type, String vsn, ProModel proModel, ProModelMd proModelMd, Team team, List<BackTeacherExpansion> xytes, List<BackTeacherExpansion> qytes, List<StudentExpansion> tms) {
		Wtype wtype = null;
		if (StringUtil.isNotEmpty(type)) {
			wtype = Wtype.getByKey(type);
			if (wtype == null) {
				logger.info("Word 模板类型未定义！[type = "+type+"]");
			}
		}

		IWparam wordParam = null;
		if (wtype != null) {
			if ((wtype.getTpl()).equals(WtplType.TT_APPLY)) {
				wordParam = WparamApply.init(proModel, proModelMd, team, xytes, qytes, tms);
			}else if ((wtype.getTpl()).equals(WtplType.TT_REPORT_JX) || (wtype.getTpl()).equals(WtplType.TT_REPORT_ZQ)) {
				wordParam = WparamReport.init(proModel, proModelMd, team, xytes, qytes, tms);
			}

			if (wordParam != null) {
				wordParam.setFileName(IWparam.getFileTplPreFix() + wtype.getName());
				wordParam.setTplFileName(IWparam.FILE_TPL_PREFIX + wtype.getName());
			}
		}
		return wordParam;
	}

	public ProModelMd getByDeclareId(String declareId,String actywId) {
		List<ProModelMd> pmd= proModelMdDao.getByDeclareId(declareId,actywId);
		if (pmd!=null&&pmd.size()>0) {
			return pmd.get(0);
		}
		return null;
	}

	public List<String> getAllPromodelMd() {
		List<String> pmd= proModelMdDao.getAllPromodelMd();
		return pmd;
	}


	public ProModelMd getByProModelId(String proModelId) {
		return proModelMdDao.getByProModelId(proModelId);
	}

	@Transactional(readOnly = false)
	public JSONObject midSubmit(ProModelMd proModelMd, JSONObject js,String gnodeId) {
		ProModel proModel=proModelMd.getProModel();
		ActYw actYw=actYwService.get(proModel.getActYwId());
		List<String> roles=new ArrayList<String>();
		if (actYw!=null) {
			if(proModel.getProcInsId() == null){
				js.put("ret",0);
				js.put("msg","流程数据错误，请联系管理员");
				return  js;
			}
			String taskId=actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
			if(null==taskId){
				js.put("ret",1);
				js.put("msg","中期申报成功");
				saveMid(proModel,proModelMd,gnodeId);
				return  js;
			}
			String taskDefinitionKeyaskDefKey=actTaskService.getTask(taskId).getTaskDefinitionKey();
			String key=ActYw.getPkey(actYw);
			//String nextRoleId=actTaskService.getProcessNextRoleName(proModel.getAct().getTaskDefKey(),key);
			String  nextNodeRoleId=actTaskService.getProcessNextRoleName(taskDefinitionKeyaskDefKey,key);  //从工作流中查询 下一步的角色集合
			String nextroleId=nextNodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
			Role role= systemService.getNamebyId(nextroleId);
			if (role!=null) {
				//启动节点
				String roleName=role.getName();
				if (roleName.contains(SysIds.ISCOLLEGE.getRemark())||roleName.contains(SysIds.ISMS.getRemark())) {
					roles=userService.findListByRoleIdAndOffice(role.getId(),proModel.getDeclareId());
				}else{
					roles=userService.findListByRoleId(role.getId());
				}
				if (roles.size()>0) {
					Map<String,Object> vars=new HashMap<String,Object>();
					vars=proModel.getVars();
					vars.put(nextNodeRoleId+"s",roles);
					if ("0".equals(proModelMd.getSetState())) {

					}else {
						taskService.complete(taskId, vars);
					}
					saveMid(proModel,proModelMd,gnodeId);
					js.put("ret",1);
					js.put("msg","中期申报成功");
					return  js;
				}
			}
			js.put("ret",0);
			js.put("msg","流程配置故障（流程角色未配置），请联系管理员");
			return  js;
		}
		js.put("ret",0);
		js.put("msg","流程配置故障（审核流程不存在），请联系管理员");
		return  js;
	}
	public void saveMid(ProModel proModel,ProModelMd proModelMd,String gnodeId){
		//proModelService.save(proModel);
		super.save(proModelMd);
		if (gnodeId!=null) {
			ProMidSubmit proMidSubmit=new ProMidSubmit();
			proMidSubmit.setGnodeId(gnodeId);
			proMidSubmit.setPromodelId(proModel.getId());
			proMidSubmit.setState("0");
			proMidSubmitService.save(proMidSubmit);
		}
		if (checkFile(proModel.getId(), proModel.getAttachMentEntity())) {//附件
			SysAttachment s=new SysAttachment();
			s.setUid(proModel.getId());
			s.setType(FileTypeEnum.S10);
			s.setFileStep(FileStepEnum.S2200);
			sysAttachmentService.deleteByCdn(s);
			if (StringUtil.isNotEmpty(gnodeId)) {
				proModel.getAttachMentEntity().setGnodeId(gnodeId);
			}
			sysAttachmentService.saveByVo(proModel.getAttachMentEntity(),proModel.getId(), FileTypeEnum.S10, FileStepEnum.S2200);
		}else{
			proModelMd.setFileUrl(proModel.getAttachMentEntity().getFielFtpUrl().get(0));
		}
	}


	@Transactional(readOnly = false)
	public JSONObject closeSubmit(ProModelMd proModelMd, JSONObject js,String gnodeId) {
		ProModel proModel=proModelMd.getProModel();
		ActYw actYw=actYwService.get(proModel.getActYwId());
		List<String> roles=new ArrayList<String>();
		if (actYw!=null) {
			String taskId=actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
			if(null==taskId){
				js.put("ret",1);
				js.put("msg","结项申报成功");
				saveClose(proModel,proModelMd,gnodeId);
				return  js;
			}
			String taskDefinitionKeyaskDefKey=actTaskService.getTask(taskId).getTaskDefinitionKey();
			String key=ActYw.getPkey(actYw);
			//String nextRoleId=actTaskService.getProcessNextRoleName(proModel.getAct().getTaskDefKey(),key);
			String  nextNodeRoleId=actTaskService.getProcessNextRoleName(taskDefinitionKeyaskDefKey,key);  //从工作流中查询 下一步的角色集合
			String nextroleId=nextNodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
			Role role= systemService.getNamebyId(nextroleId);
			if (role!=null) {
				//启动节点
				String roleName=role.getName();
				if (roleName.contains(SysIds.ISCOLLEGE.getRemark())||roleName.contains(SysIds.ISMS.getRemark())) {
					roles=userService.findListByRoleIdAndOffice(role.getId(),proModel.getDeclareId());
				}else{
					roles=userService.findListByRoleId(role.getId());
				}
				if (roles.size()>0) {
					Map<String,Object> vars=new HashMap<String,Object>();
					vars=proModel.getVars();
					vars.put(nextNodeRoleId+"s",roles);
					if ("0".equals(proModelMd.getSetState()) || "0".equals(proModelMd.getMidState())) {

					}else {
						taskService.complete(taskId, vars);
					}
					saveClose(proModel,proModelMd,gnodeId);
//					proModelService.save(proModel);
//					super.save(proModelMd);
//					if (gnodeId!=null) {
//						ProCloseSubmit proCloseSubmit=new ProCloseSubmit();
//						proCloseSubmit.setGnodeId(gnodeId);
//						proCloseSubmit.setPromodelId(proModel.getId());
//						proCloseSubmit.setState("0");
//						proCloseSubmitService.save(proCloseSubmit);
//					}
//					if (checkFile(proModel.getId(), proModel.getAttachMentEntity())) {//附件
//						SysAttachment s=new SysAttachment();
//						s.setUid(proModel.getId());
//						s.setType(FileTypeEnum.S10);
//						s.setFileStep(FileStepEnum.S2300);
//						sysAttachmentService.deleteByCdn(s);
//						if (StringUtil.isNotEmpty(gnodeId)) {
//							proModel.getAttachMentEntity().setGnodeId(gnodeId);
//						}
//						sysAttachmentService.saveByVo(proModel.getAttachMentEntity(),proModel.getId(), FileTypeEnum.S10, FileStepEnum.S2300);
//					}else{
//						proModelMd.setFileUrl(proModel.getAttachMentEntity().getFielFtpUrl().get(0));
//					}
					js.put("ret",1);
					js.put("msg","结项申报成功");
					return  js;
				}
			}
			js.put("ret",0);
			js.put("msg","流程配置故障（流程角色未配置），请联系管理员");
			return  js;
		}
		js.put("ret",0);
		js.put("msg","流程配置故障（审核流程不存在），请联系管理员");
		return  js;
	}
	public void saveClose(ProModel proModel,ProModelMd proModelMd,String gnodeId){
		//proModelService.save(proModel);
		super.save(proModelMd);
		if (gnodeId!=null) {
			ProMidSubmit proMidSubmit=new ProMidSubmit();
			proMidSubmit.setGnodeId(gnodeId);
			proMidSubmit.setPromodelId(proModel.getId());
			proMidSubmit.setState("0");
			proMidSubmitService.save(proMidSubmit);
		}
		if (checkFile(proModel.getId(), proModel.getAttachMentEntity())) {//附件
			SysAttachment s=new SysAttachment();
			s.setUid(proModel.getId());
			s.setType(FileTypeEnum.S10);
			s.setFileStep(FileStepEnum.S2300);
			sysAttachmentService.deleteByCdn(s);
			if (StringUtil.isNotEmpty(gnodeId)) {
				proModel.getAttachMentEntity().setGnodeId(gnodeId);
			}
			sysAttachmentService.saveByVo(proModel.getAttachMentEntity(),proModel.getId(), FileTypeEnum.S10, FileStepEnum.S2300);
		}else{
			proModelMd.setFileUrl(proModel.getAttachMentEntity().getFielFtpUrl().get(0));
		}
	}


	public Page<ProModelMd> modelMdTodoList(Page<ProModelMd> page, String gnodeId,Act act, String keyName) {
		//通过流程获得ids
		List<String> actIds= actTaskService.modelMdtodoList(act,keyName);
		ActYwGnode actYwGnode=actYwGnodeService.get(gnodeId);
		//通过立项未通过获得ids
		if (actYwGnode!=null&& actYwGnode.getName().contains("立项")) {
			List<String> mdNoPassIds=proModelMdDao.getBySetNoPassList();
			actIds.addAll(mdNoPassIds);
		}
		if (actYwGnode!=null&& actYwGnode.getName().contains("中期")) {
			List<String> mdNoPassIds=proModelMdDao.getByMidNoPassList();
			actIds.addAll(mdNoPassIds);
		}
		if (actYwGnode!=null&& actYwGnode.getName().contains("结项")) {
			List<String> mdNoPassIds=proModelMdDao.getByCloseNoPassList();
			actIds.addAll(mdNoPassIds);
		}
		page.setCount(actIds.size());
		int pageStart=(page.getPageNo()-1)*page.getPageSize();
		int pageEnd=actIds.size();
		if (actIds.size()>page.getPageNo()*page.getPageSize()) {
			pageEnd=page.getPageNo()*page.getPageSize();
		}
		List<String> idsList=actIds.subList(pageStart,pageEnd);
		/*String ids="";
		for(String id:idsList) {
			ids=ids+id+",";
		}
		ids=ids.substring(0,ids.lastIndexOf(","));*/
		if (idsList.size()>0) {
			List<ProModelMd> subList=proModelMdDao.getListByModelIds(idsList);
			page.setList(subList);
		}

		return page;

	}

	public Page<ProModelMd> findMdPage(Page<ProModelMd> page, ProModel proModel) {

		if(page.getPageSize()<0){
			page.setPageSize(10);
		}
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("offset", (page.getPageNo()-1)*page.getPageSize());
		param.put("pageSize", page.getPageSize());
		param.put("actywId", proModel.getActYwId());
		param.put("queryStr",proModel.getQueryStr());
		param.put("proCategory",proModel.getProCategory());
		int count=proModelMdDao.getListByModelCount(param);
		page.setCount(count);
		if(count<1){
			return page;
		}

		List<ProModelMd> subList=proModelMdDao.getListByModel(param);
		if(subList!=null&&subList.size()>0){
			Map<String,List<SysCertInsVo>> map=getSysCertIns(subList);
			if(map!=null&&!map.isEmpty()){
				for(ProModelMd p:subList){
					p.getProModel().setScis(map.get(p.getProModel().getId()));
				}
			}
		}
		page.setList(subList);
		return page;
	}
	private Map<String,List<SysCertInsVo>> getSysCertIns(List<ProModelMd> subList){
		Map<String,List<SysCertInsVo>> map=new HashMap<String,List<SysCertInsVo>>();
		List<String> param=new ArrayList<String>();
		if(subList.size()==0){
			return map;
		}
		for(ProModelMd p:subList){
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

	@Override
	public Page<ProModelMd> findDataPage(Page<ProModelMd> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProModelMd proModelMd) {
		String auditGonde = getGnodeName(gnodeId, actYw, model);
		return modelMdTodoList(page, gnodeId, act, ActYwTool.FLOW_ID_PREFIX + auditGonde);
	}

	/**
	 * 根据gnodeId得到每个流程下面子节点，根据角色找到对应的节点
	 */
	public String getGnodeName(String gnodeId, ActYw actYw, Model model) {
		String auditGonde = null;
		ActYwGnode actYwGnode = new ActYwGnode(new ActYwGroup(actYw.getGroupId()));
		actYwGnode.setParent(new ActYwGnode(gnodeId));
		ActYwNode actYwNode = new ActYwNode();
		actYwGnode.setNode(actYwNode);
		//查询当前节点下面任务节点id
		List<ActYwGnode> actYwGnodes = actYwGnodeService.findListBygGparent(actYwGnode);
		//查询当前角色 角色id
		List<String> roleIds = UserUtils.getUser().getRoleIdList();
		//遍历循环所有审核节点 返回节点list
		for (ActYwGnode curGnode : actYwGnodes) {
			for (int j = 0; j < roleIds.size(); j++) {
				if ((curGnode).checkRoleByRid(roleIds.get(j))) {
					//model.addAttribute("actYwGnode",actYwGnodes.get(i));
					List<ActYwGform> actYwFormList = curGnode.getGforms();
					for (ActYwGform actYwGform : actYwFormList) {
						if (actYwGform.getForm() != null && FormStyleType.FST_FORM.getKey().equals(actYwGform.getForm().getStyleType())) {
							model.addAttribute("path", actYwGform.getForm().getPath());
						}
					}
					auditGonde = curGnode.getId();
					model.addAttribute("auditGonde", curGnode);
					break;
				}
			}
			if (StringUtil.isNotEmpty(auditGonde)) {
				break;
			}
		}
		return auditGonde;
	}

	@Override
	public Page<ProModelMd> findQueryPage(Page<ProModelMd> page, Model model, String actywId, ActYw actYw, ProModelMd proModelMd) {
		ProModel proModel = proModelMd.getProModel();
		if (StringUtils.isEmpty(proModel)) {
			proModel = new ProModel();
			proModel.setActYwId(actywId);
		}else{
			proModel.setActYwId(actywId);
		}
		return findMdPage(page, proModel);
	}

	@Override
	public Page<ProModelMd> findAssignPage(Page<ProModelMd> page, Model model, String actywId, ActYw actYw, ProModelMd proModelMd) {
		String gnodeId = super.getGnodeId(proModelMd, actywId);
		String key = ActYw.getPkey(actYw);
		Act act= new Act();
		act.setProcDefKey(key);  //流程标识
		List<String> gnodeIdList = new ArrayList<String>();
		gnodeIdList.add(gnodeId);
		List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actywId);
		ProModel proModel = proModelMd.getProModel();
		if (StringUtils.isEmpty(proModel)) {
			proModel = new ProModel();
			proModel.setActYwId(actywId);
			proModel.setGnodeId(gnodeId);
		}
		proModelMd.setProModel(proModel);
		return super.findAssignPage(page, proModelMd, recordIds);
	}

	@Override
	public void audit(String gnodeId,String proModelId, Model model) {
		ProModelMd proModelMd = getByProModelId(proModelId);
		SysAttachment sa = new SysAttachment();
		sa.setUid(proModelMd.getProModel().getId());
		sa.setType(FileTypeEnum.S10);
		sa.setFileStep(FileStepEnum.S2000);
		List<SysAttachment> fileListMap = sysAttachmentService.getFiles(sa);
		if (fileListMap != null) {
			model.addAttribute("sysAttachments", fileListMap);
		}
		model.addAttribute("proModelMd", proModelMd);
	}

	@Override
	public void saveAddPro(ProModel proModel) {

	}

	@Override
	public void auditByGateWay(ProModel proModel, String gnodeId, HttpServletRequest request) {
		proModelService.auditWithGateWay(proModel, gnodeId);
	}

	@Override
	public void gcontestEdit(ProModel proModel, HttpServletRequest request, Model model) {

	}

	@Override
	public void projectEdit(ProModel proModel, HttpServletRequest request, Model model) {

	}

	@Override
	public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ProProject proProject, ActYw actYw) {
		User user = UserUtils.getUser();
		ProModelMd proModelMd = new ProModelMd();
		proModelMd.setCreateDate(new Date());
		proModelMd.setProModel(proModel);
		proModel.setActYw(actYw);
		proModel.setActYwId(actYw.getId());
		model.addAttribute("proModelMd", proModelMd);
		model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), ""));
		model.addAttribute("sse", user);
		model.addAttribute("cuser", user);
		model.addAttribute("showStepNumber", 1);
		model.addAttribute("isSubmit", 0);
		model.addAttribute("wprefix", IWparam.getFileTplPreFix());
		model.addAttribute("wtypes", Wtype.toJson());
		return super.applayForm(fpageType, model, request, response, proModel, proProject, actYw);
	}

	@Override
	public String viewForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
		FormTheme formTheme = actYw.getFtheme();
		if(formTheme != null) {
			FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
			//参数实现已经移动至实现类FppMd
			fpage.getParam().init(model, request, response, new Object[]{});
			fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, sysAttachmentService, this});
			return FormPage.getAbsUrl(actYw, fpageType, null);
		}
		return CorePages.ERROR_404.getIdxUrl();
	}

	@Override
	public void saveFormData(ProModel proModel, HttpServletRequest request) {

	}

    @Override
    public List<ExpProModelVo> exportData(Page<ProModelMd> page, ProModelMd proModelMd) {
        proModelMd.setPage(page);
        try {
            return dao.export(proModelMd);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

    @Override
    public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, ProModelMd t) {
        logger.info("开始：生成数据！");
        Page<ProModelMd> page = new Page<ProModelMd>(request, response);
        page.setPageSize(Page.MAX_PAGE_SIZE);
        t.setIds(pids);
        exportData(page, t);
    }

    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProModelMd t) {
        // TODO Auto-generated method stub

    }
	@Override
	public JSONObject submit(ProModel proModel, JSONObject js) {
		js=proModelService.submit(proModel,js);
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

	@Override
	public void addFrontParam(ProjectDeclareListVo v) {

	}

	@Override
	public void indexTime(Map<String, String> lastpro) {

	}

    public List<ExpProModelMdVo> exportMdQuery(Page<ProModelMd> page, ProModelMd proModelMd) {
        proModelMd.setPage(page);
        try {
            return dao.exportMdQuery(proModelMd);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return null;
    }

	public String getMdResult(String id) {
		ProModelMd proModelMd = getByProModelId(id);
		if(StringUtil.isNotEmpty(proModelMd.getCloseState())){
			if("1".equals(proModelMd.getCloseState())){
				return "通过";
			}else{
				return "不通过";
			}
		}else {
			if(StringUtil.isNotEmpty(proModelMd.getMidState())){
				if("1".equals(proModelMd.getMidState())){
					return "通过";
				}else{
					return "不通过";
				}
			}else{
				if(StringUtil.isNotEmpty(proModelMd.getSetState())){
					if("1".equals(proModelMd.getSetState())){
						return "通过";
					}else{
						return "不通过";
					}
				}
			}
		}
		return "";
	}
}