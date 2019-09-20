package com.oseasy.pro.modules.gcontest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.jobs.pro.ActYwUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.dao.ActYwDao;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.modules.auditstandard.entity.AuditStandardDetailIns;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardDetailInsService;
import com.oseasy.pro.modules.auditstandard.service.AuditStandardDetailService;
import com.oseasy.pro.modules.auditstandard.vo.AsdVo;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;
import com.oseasy.pro.modules.excellent.service.ExcellentShowService;
import com.oseasy.pro.modules.gcontest.dao.GContestDao;
import com.oseasy.pro.modules.gcontest.entity.GAuditInfo;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.entity.GContestAnnounce;
import com.oseasy.pro.modules.gcontest.entity.GContestAward;
import com.oseasy.pro.modules.gcontest.enums.AuditStatusEnum;
import com.oseasy.pro.modules.gcontest.enums.GContestStatusEnum;
import com.oseasy.pro.modules.gcontest.vo.GContestListVo;
import com.oseasy.pro.modules.gcontest.vo.GContestNodeVo;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectStandardDetailVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.team.dao.TeamUserRelationDao;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 大赛信息Service
 * @author zy
 * @version 2017-03-11
 */
@Service
@Transactional(readOnly = true)
public class GContestService extends CrudService<GContestDao, GContest> {
	@Autowired
	ActTaskService actTaskService;
	@Autowired
	ProActTaskService proActTaskService;

	@Autowired
	UserService userService;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	IdentityService identityService;

	@Autowired
	GAuditInfoService gAuditInfoService;

	@Autowired
	GContestAwardService gContestAwardService;

	@Autowired
	private SysAttachmentService sysAttachmentService;

	@Autowired
	private GContestAnnounceService gContestAnnounceService;

	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	private ExcellentShowService excellentShowService;

	@Autowired
	TaskService taskService;

	@Autowired
	ActDao actDao;

	@Autowired
	TeamUserRelationDao teamUserRelationDao;
	@Autowired
	TeamUserHistoryService teamUserHistoryService;

	@Autowired
	private AuditStandardDetailService auditStandardDetailService;
	@Autowired
	private AuditStandardDetailInsService auditStandardDetailInsService;
  	@Autowired
  	ActYwDao actYwDao;
	@Autowired
	ActYwGnodeService actYwGnodeService;
	@Autowired
	private CommonService commonService;
	@Autowired
	ProjectDeclareService projectDeclareService;
	@Autowired
	ProModelService proModelService;
	@Autowired
	HistoryService historyService;
  	public void  getPersonNumForAsdIndexFromModel(AsdVo asdVo) {
		Map<String,Object> map=dao.getPersonNumForAsdIndexFromModel(asdVo);
		Map<String,Long> map2=dao.getProjectNumForAsdIndexFromModel(asdVo);
		if (map!=null) {
			asdVo.setInnovateNum(StringUtil.getString(map.get("st")));
			asdVo.setTeacherNum(StringUtil.getString(map.get("te")));
		}
		if (map2!=null) {
			asdVo.setApplyNum(StringUtil.getString(map2.get("st")));
		}
	}
	public void  getPersonNumForAsdIndex(AsdVo asdVo) {
		Map<String,Object> map=dao.getPersonNumForAsdIndex(asdVo.getDataYear()+"-01-01 00:00:00");
		Map<String,Long> map2=dao.getProjectNumForAsdIndex(asdVo.getDataYear()+"-01-01 00:00:00");
		if (map!=null) {
			asdVo.setInnovateNum(StringUtil.getString(map.get("st")));
			asdVo.setTeacherNum(StringUtil.getString(map.get("te")));
		}
		if (map2!=null) {
			asdVo.setApplyNum(StringUtil.getString(map2.get("st")));
		}
	}
	public GContest get(String id) {
		return super.get(id);
	}

	public List<GContest> findList(GContest gContest) {
		return super.findList(gContest);
	}

	public Page<GContest> findPage(Page<GContest> page, GContest gContest) {
		return super.findPage(page, gContest);
	}
	/**
	 * @param page
	 * @param vo
	 * @return
	 */
	 public Page<GContestListVo>  getMyProjectListPlus(Page<GContestListVo> page,GContestListVo vo) {
			vo.setPage(page);
			page.setList(dao.getMyGcontestListPlus(vo));
			//处理组成员、导师名称
			if (!page.getList().isEmpty()) {
		        List<String> ids=new ArrayList<String>();
		        for(GContestListVo v:page.getList()) {
		          ids.add(v.getId());
		        }
		        List<Map<String,String>> ps=dao.getMyGcontestListPersonPlus(ids);
		        Map<String,String> psm=new HashMap<String,String>();
		        if (ps!=null&&ps.size()>0) {
		          for(Map<String,String> map:ps) {
		            psm.put(map.get("id")+map.get("team_user_type"), map.get("pname"));
		          }
		        }
		        for(GContestListVo v:page.getList()) {
		        	if (psm.size()>0) {
			        	v.setSnames(psm.get(v.getId()+"1"));
			        	v.setTnames(psm.get(v.getId()+"2"));
		        	}
		        	if ("-999".equals(v.getAuditCode())) {
		        		if (!Const.YES.equals(v.getSubStatus())) {
		        			v.setAuditState("未提交");
							if(!Const.YES.equals(v.getState())){
								v.setAuditCode("0");
							}
		        		}else{
							ActYwGnode actYwGnode=null;
							if(v.getProc_ins_id()!=null){
								actYwGnode = proActTaskService.getNodeByProInsId(v.getProc_ins_id());
							}
		        			if (actYwGnode!=null) {

								if (v.getState()!=null && v.getState().equals("1")) {
									v.setAuditState(actYwGnode.getName()+"不通过");
								}else {
									v.setAuditState("待" + actYwGnode.getName());
								}
		        			}else{
								//表明流程走到最后一步
								String actywId=v.getActywId();
//								ActYw actYw=actYwDao.get(actywId);
								String cacheKey = ActSval.ck.cks(ActSval.ActEmskey.ACTYW, TenantConfig.getCacheTenant())+actywId;
								ActYw actYw = null;
								if (JedisUtils.hasKey(cacheKey)){
									actYw = (ActYw)JedisUtils.getObject(cacheKey);
								}
								if (actYw == null){
									actYw = actYwDao.get(actywId);
									if (actYw != null){
										JedisUtils.setObject(cacheKey,actYw);
									}
								}
								if (actYw!=null) {
									String groupId=actYw.getGroupId();
//									ActYwGnode newactYwGnode=actYwGnodeService.getEndPreFun(groupId);
									ActYwGnode newactYwGnode=actYwGnodeService.getEndPreFunOfName(groupId);

									if (v.getGrade() != null && v.getGrade().equals("0")) {
										if(newactYwGnode!=null &&newactYwGnode.getName()!=null){
											v.setAuditState(newactYwGnode.getName() + "不通过");
										}else{
											v.setAuditState("不通过");
										}
									} else {
										if(newactYwGnode!=null &&newactYwGnode.getName()!=null){
											v.setAuditState(newactYwGnode.getName() + "通过");
										}else{
//											ProModel proModel=proModelService.get(v.getId());
//											ProModel proModel = null;
//											String key = ProSval.ck.cks(ProSval.ProEmskey.PROMODEL, TenantConfig.getCacheTenant(), proModel.getId());
//											if (JedisUtils.hasKey(key)){
//												if (JedisUtils.getObject(key)!=null){
//													proModel = (ProModel)JedisUtils.getObject(key);
//												}
//											}
//											if (proModel == null){
											ProModel	proModel=proModelService.get(v.getId());
												if (proModel != null){
//													JedisUtils.setObject(key,proModel);
													if(proModel.getEndGnodeId()!=null){
														proModelService.getFinalResult(proModel);
													}
													v.setAuditState(proModel.getFinalResult());
												}
//											}
										}
									}
								}
							}
		        		}
		        	}
		        }
			}
			return page;
	 }
	public Page<Map<String,String>> getMyGcontestList(Page<Map<String,String>> page,Map<String,Object> param) {
		if (page.getPageNo()<=0) {
			page.setPageNo(1);
		}
		int count=dao.getMyGcontestListCount(param);
		param.put("offset", (page.getPageNo()-1)*page.getPageSize());
		param.put("pageSize", page.getPageSize());
		List<Map<String,String>> list=null;
		if (count>0) {
			list=dao.getMyGcontestList(param);
			if (list!=null) {
				List<String> ids=new ArrayList<String>();
				for(Map<String,String> map:list) {
					ids.add(map.get("id"));
					map.put("auditCode", map.get("auditState"));
					map.put("auditState", GContestStatusEnum.getNameByValue(map.get("auditState")));
				}
				if (ids.size()>0) {
					List<Map<String,String>> ps=dao.getMyGcontestListPerson(ids);
					if (ps!=null&&ps.size()>0) {
						Map<String,String> psm=new HashMap<String,String>();
						for(Map<String,String> map:ps) {
							psm.put(map.get("id")+map.get("team_user_type"), map.get("pname"));
						}
						for(Map<String,String> map:list) {
							map.put("snames", psm.get(map.get("id")+"1"));
							map.put("tnames", psm.get(map.get("id")+"2"));
						}
					}
				}
			}
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}

	public Page<Map<String,String>> getGcontestList(Page<Map<String,String>> page,Map<String,Object> param) {
		if (page.getPageNo()<=0) {
			page.setPageNo(1);
		}
		int count=dao.getGcontestListCount(param);
		param.put("offset", (page.getPageNo()-1)*page.getPageSize());
		param.put("pageSize", page.getPageSize());
		List<Map<String,String>> list=null;
		if (count>0) {
			list=dao.getGcontestList(param);
			if (list!=null) {
				List<String> ids=new ArrayList<String>();
				for(Map<String,String> map:list) {
					ids.add(map.get("id"));

					//判断专家是否已经审核过该数据
					User user= UserUtils.getUser();
					GAuditInfo pai=new GAuditInfo();
			        pai.setGId(map.get("id"));
			        pai.setAuditLevel(map.get("auditState"));
			        pai.setAuditId(user.getId());
			        List<GAuditInfo> infos= gAuditInfoService.getInfo(pai);
					if (infos.size()>0) {
						//该专家已经审核过该数据
						map.put("isHave","1");
					}else{
						map.put("isHave","0");
					}
					GContest gContest=get(map.get("id"));
			    	if (gContest.getAct().getProcInsId()!=null) {
						String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
						Task task=actTaskService.getTask(taskId);
						if (task!=null) {
							map.put("taskDef",task.getProcessDefinitionId());
							map.put("taskIn",task.getProcessInstanceId());
							map.put("taskId",taskId);
						}
					}
			    	map.put("auditCode", map.get("auditState"));
					map.put("auditState", GContestStatusEnum.getNameByValue(map.get("auditState")));
				}
				if (ids.size()>0) {
					List<Map<String,String>> ps=dao.getMyGcontestListPerson(ids);
					if (ps!=null&&ps.size()>0) {
						Map<String,String> psm=new HashMap<String,String>();
						for(Map<String,String> map:ps) {
							psm.put(map.get("id")+map.get("team_user_type"), map.get("pname"));
						}
						for(Map<String,String> map:list) {
							map.put("snames", psm.get(map.get("id")+"1"));
							map.put("tnames", psm.get(map.get("id")+"2"));
						}
					}
				}
			}
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}

	public Page<Map<String,String>> getEndGcontestList(Page<Map<String,String>> page,Map<String,Object> param) {
		if (page.getPageNo()<=0) {
			page.setPageNo(1);
		}
		int count=dao.getEndGcontestListCount(param);
		param.put("offset", (page.getPageNo()-1)*page.getPageSize());
		param.put("pageSize", page.getPageSize());
		List<Map<String,String>> list=null;
		if (count>0) {
			list=dao.getEndGcontestList(param);
			if (list!=null) {
				List<String> ids=new ArrayList<String>();
				for(Map<String,String> map:list) {
					ids.add(map.get("id"));

					//判断专家是否已经审核过该数据
					User user= UserUtils.getUser();
					GAuditInfo pai=new GAuditInfo();
			        pai.setGId(map.get("id"));
			        pai.setAuditLevel(map.get("auditState"));
			        pai.setAuditId(user.getId());
			        /* List<GAuditInfo> infos= gAuditInfoService.getInfo(pai);
					if (infos.size()>0) {
						//该专家已经审核过该数据
						map.put("isHave","1");
					}else{
						map.put("isHave","0");
					}*/
					GContest gContest=get(map.get("id"));
			    	if (gContest.getAct().getProcInsId()!=null) {
						String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
						Task task=actTaskService.getTask(taskId);
						if (task!=null) {
							map.put("taskDef",task.getProcessDefinitionId());
							map.put("taskIn",task.getProcessInstanceId());
						}
					}
			    	map.put("auditCode", map.get("auditState"));
					map.put("auditState", GContestStatusEnum.getNameByValue(map.get("auditState")));
				}
				if (ids.size()>0) {
					List<Map<String,String>> ps=dao.getMyGcontestListPerson(ids);
					if (ps!=null&&ps.size()>0) {
						Map<String,String> psm=new HashMap<String,String>();
						for(Map<String,String> map:ps) {
							psm.put(map.get("id")+map.get("team_user_type"), map.get("pname"));
						}
						for(Map<String,String> map:list) {
							map.put("snames", psm.get(map.get("id")+"1"));
							map.put("tnames", psm.get(map.get("id")+"2"));
						}
					}
				}
			}
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}


	public Page<Map<String,String>> auditContestList(Page<Map<String,String>> page,Map<String,Object> param) {
		if (page.getPageNo()<=0) {
			page.setPageNo(1);
		}
		int count=dao.getAuditListCount(param);
		param.put("offset", (page.getPageNo()-1)*page.getPageSize());
		param.put("pageSize", page.getPageSize());
		List<Map<String,String>> list=null;
		if (count>0) {
			list=dao.getAuditList(param);
			if (list!=null) {
				List<String> ids=new ArrayList<String>();
				for(Map<String,String> map:list) {
					ids.add(map.get("id"));

					//判断专家是否已经审核过该数据
					User user= UserUtils.getUser();
					GAuditInfo pai=new GAuditInfo();
			        pai.setGId(map.get("id"));
			        pai.setAuditLevel(map.get("auditState"));
			        pai.setAuditId(user.getId());
			        List<GAuditInfo> infos= gAuditInfoService.getInfo(pai);
					if (infos.size()>0) {
						//该专家已经审核过该数据
						map.put("isHave","1");
					}else{
						map.put("isHave","0");
					}
					GContest gContest=get(map.get("id"));
			    	if (gContest.getAct().getProcInsId()!=null) {
						String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
						Task task=actTaskService.getTask(taskId);
						if (task!=null) {
							map.put("taskDef",task.getProcessDefinitionId());
							map.put("taskIn",task.getProcessInstanceId());
							map.put("taskId",taskId);
						}
					}
			    	map.put("auditCode", map.get("auditState"));
					map.put("auditState", GContestStatusEnum.getNameByValue(map.get("auditState")));
				}
				if (ids.size()>0) {
					List<Map<String,String>> ps=dao.getMyGcontestListPerson(ids);
					if (ps!=null&&ps.size()>0) {
						Map<String,String> psm=new HashMap<String,String>();
						for(Map<String,String> map:ps) {
							psm.put(map.get("id")+map.get("team_user_type"), map.get("pname"));
						}
						for(Map<String,String> map:list) {
							map.put("snames", psm.get(map.get("id")+"1"));
							map.put("tnames", psm.get(map.get("id")+"2"));
						}
					}
				}
			}
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}


	@Transactional(readOnly = false)
	public List<GContest> getGcontestByName(String gContestName) {
		List<GContest> gContests=dao.getGcontestByName(gContestName);
		return gContests;
	}

	@Transactional(readOnly = false)
	public void save(GContest gContest) {
		User user = UserUtils.getUser();
		gContest.setCompetitionNumber(IdUtils.getGContestNumberByDb());
		gContest.setSubTime(new Date());
		gContest.setUniversityId(user.getOffice().getId());
		gContest.setAuditState("0");
		super.save(gContest);
		sysAttachmentService.saveByVo(gContest.getAttachMentEntity(),gContest.getId(), FileTypeEnum.S2, FileStepEnum.S300);
		//保存团队的学分分配权重
		String year=commonService.getApplyYear(gContest.getActywId());
		commonService.disposeTeamUserHistoryOnSave(gContest.getTeamUserHistoryList(), gContest.getActywId(),gContest.getTeamId(),gContest.getId(),year);
	}

	@Transactional(readOnly = false)
	public void udpate(GContest gContest) {

		super.save(gContest);
	}


	@Transactional(readOnly = false)
	public int submit(GContest gContest) {
		User user = UserUtils.getUser();
		gContest.setAuditState("1");

//		String num = NumRuleUtils.getNumberText(gContest.getActywId(),gContest.getYear());
//		if(StringUtil.isEmpty(num)){
//			return 2;
//		}

		gContest.setCompetitionNumber(IdUtils.getGContestNumberByDb());
		gContest.setSubTime(new Date());
		gContest.setUniversityId(user.getOffice().getId());
		//启动大赛工作流
		String roleName=actTaskService.getStartNextRoleName("gcontest");  //从工作流中查询 下一步的角色集合
		List<String> roles=userService.getCollegeExperts(gContest.getDeclareId());
		if (roles.size()>0) {
			Map<String,Object> vars=new HashMap<String,Object>();
			vars=gContest.getVars();
			vars.put(roleName+"s",roles);
			String key="gcontest";
			String userId = UserUtils.getUser().getId();
			identityService.setAuthenticatedUserId(userId);
			ProcessInstance procIns=runtimeService.startProcessInstanceByKeyAndTenantId(key, "g_contest"+":"+gContest.getId(),vars, TenantConfig.getCacheTenant());

			//流程id返写业务表
			Act act = new Act();
			act.setBusinessTable("g_contest");// 业务表名
			act.setBusinessId(gContest.getId());	// 业务表ID
			act.setProcInsId(procIns.getId());
			actDao.updateProcInsIdByBusinessId(act);
			gContest.setProcInsId(act.getProcInsId());
			gContest.setCurrentSystem("校赛");
			super.save(gContest);
			/*提交时处理TeamUserHistory信息*/
			String year=commonService.getApplyYear(gContest.getActywId());
			commonService.disposeTeamUserHistoryOnSubmit(gContest.getTeamUserHistoryList(), gContest.getActywId(),gContest.getTeamId(),gContest.getId(),year);
			/*提交时生成默认优秀展示*/
			excellentShowService.saveExcellentShow(gContest.getIntroduction(), gContest.getTeamId(),ExcellentShow.Type_Gcontest,gContest.getId(),gContest.getActywId());
			return 1;
		}else{
			return 0;
		}
	}

	@Transactional(readOnly = false)
	public String submitOld(GContest gContest) {
		User user =userService.findUserById(gContest.getDeclareId());
		GContest newGContest=new GContest();
		newGContest.setAuditState("1");
		newGContest.setId("");
		newGContest.setDeclareId(gContest.getDeclareId());
		newGContest.setIntroduction(gContest.getIntroduction());
		newGContest.setCurrentSystem("校赛");
		newGContest.setTeamId(gContest.getTeamId());
		newGContest.setFinancingStat(gContest.getFinancingStat());
		newGContest.setType(gContest.getType());
		newGContest.setLevel(gContest.getLevel());
		newGContest.setpName(gContest.getpName());
		if (gContest.getpId()!=null) {
			newGContest.setpId(gContest.getpId());
		}
		newGContest.setCompetitionNumber(gContest.getCompetitionNumber());
		newGContest.setSubTime(gContest.getSubTime());
		newGContest.setUniversityId(user.getOffice().getId());
		newGContest.setAnnounceId(gContest.getAnnounceId());
		//super.save(newGContest);
		//启动大赛工作流
		String roleName=actTaskService.getStartNextRoleName("gcontest");  //从工作流中查询 下一步的角色集合
		List<String> roles=userService.getCollegeExperts(newGContest.getDeclareId());
		if (roles.size()>0) {
			Map<String,Object> vars=new HashMap<String,Object>();
			vars=newGContest.getVars();
			vars.put(roleName+"s",roles);
			//vars.put("projectName",gContest.getPName());
			//vars.put("collegeName", UserUtils.getUser().getCompany().getName());
			String key="gcontest";
			String userId = user.getId();
			//vars.put("sumbitter",userId);
			identityService.setAuthenticatedUserId(userId);
			ProcessInstance procIns=runtimeService.startProcessInstanceByKeyAndTenantId(key, "g_contest"+":"+newGContest.getId(),vars,TenantConfig.getCacheTenant());

			//流程id返写业务表
			Act act = new Act();
			act.setBusinessTable("g_contest");// 业务表名
			act.setBusinessId(newGContest.getId());	// 业务表ID
			act.setProcInsId(procIns.getId());
			actDao.updateProcInsIdByBusinessId(act);
			newGContest.setProcInsId(act.getProcInsId());

			super.save(newGContest);
		}else{
			return "";
		}
		return newGContest.getId();
	}

	@Transactional(readOnly = false)
	public void delete(GContest gContest, String ftb) {
		//更改完成后团队历史表中的状态

		if ("0".equals(ftb)) {
			super.delete(gContest);
		} else {
			proModelService.delete(gContest.getId());
		}
		teamUserHistoryService.updateDelByProid(gContest.getId());
	}

	@Transactional(readOnly = false)
	public void saveAudit1(GContest gContest,Act act) {
		//完成工作流
		String userId = UserUtils.getUser().getId();
		//String comment=userId+" 的评分："+gContest.getGScore();
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S1.getValue());  //1表示院内2表示校内
		pai.setAuditName(AuditStatusEnum.S1.getName());//网评 审核 路演 后面做成字典表
		pai.setAuditId(userId);
		if (gContest.getgScore()!=null) {
			pai.setScore(Float.parseFloat(gContest.getgScore()));
		}
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		pai.setSuggest(gContest.getComment());
		gAuditInfoService.save(pai);

		List<String> scoreList= gContest.getScoreList();
		//查找审核标准
		List<ProjectStandardDetailVo> asList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
		if (asList!=null && asList.size()>0) {
			String isScore = asList.get(0).getIsEescoreNodes();
			String firstNode = GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_FIRST_ID);
			if (isScore!=null && isScore.contains(firstNode)) {
				if (asList != null && asList.size() > 0 && scoreList != null && scoreList.size() > 0) {
					for (int i = 0; i < asList.size(); i++) {
						AuditStandardDetailIns auditStandardDetailIns = new AuditStandardDetailIns();
						auditStandardDetailIns.setCheckElement(asList.get(i).getCheckElement());
						auditStandardDetailIns.setFid(gContest.getId());
						auditStandardDetailIns.setCheckPoint(asList.get(i).getCheckPoint());
						auditStandardDetailIns.setViewScore(asList.get(i).getViewScore());
						auditStandardDetailIns.setScore(scoreList.get(i));
						auditStandardDetailIns.setSort(asList.get(i).getSort());
						auditStandardDetailIns.setAuditInfoId(pai.getId());
						auditStandardDetailInsService.save(auditStandardDetailIns);
					}
				}
			}
		}

		User rec_User=new User();
		rec_User.setId(gContest.getDeclareId());
//		oaNotifyService.sendOaNotifyByType(apply_User,rec_User,"学院专家审核",
//				"学院专家"+apply_User.getName()+"已对您的作品给出评分；","",gContest.getId());

		gContest=get(gContest.getId());
		Map<String,Object> vars=new HashMap<String,Object>();
		//查询本用户任务环节是否完成，改变业务表对应的状态
		//String taskDefKey = gContest.getAct().getTaskDefKey(); 	// 环节编号
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		//Task task=actTaskService.getTask(taskId);
		boolean isLast=actTaskService.isMultiLast("gcontest",act.getTaskDefKey(),gContest.getAct().getProcInsId());
		if (isLast) { //如果当前任务环节完成了
			//String roleName=actTaskService.getNextRoleName(gContest.getAct().getTaskDefKey(),"gcontest");  //查找下一个环节的 roleName
			List<String> collegeSecs=new ArrayList<String>() ;
			gContest.setAuditState("2");//学院专家打完分
			vars=gContest.getVars();
			collegeSecs=userService.getCollegeSecs(gContest.getCreateBy().getId());

			vars.put("collegeSec",collegeSecs); //给学院教学秘书审批			vars.put("collegeSec",getCollegeSecs(gContest.getId()));
			float average=0;
			GAuditInfo infoSerch=new GAuditInfo();
			infoSerch.setGId(gContest.getId());
			infoSerch.setAuditLevel("1");
			average= gAuditInfoService.getAuditAvgInfo(infoSerch);
			gContest.setgScore(String.valueOf(average));
			gContest.setCollegeExportScore(average);
			gContest.setCollegeScore(average);
			super.dao.updateState(gContest);
			//如果当前任务环节完成了
			taskService.complete(taskId, vars);
			//免签收动作 只有这个角色可以审核。
			actTaskService.claimByProcInsId(gContest.getAct().getProcInsId(),collegeSecs);
		}else{
			taskService.complete(taskId, vars);
		}
	}
	//项目变更跨流程
	@Transactional(readOnly = false)
	public void saveAuditFirst(GContest gContest,Act act,GAuditInfo pai) {
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S1.getValue());  //1表示院内2表示校内
		pai.setAuditName(AuditStatusEnum.S1.getName());//网评 审核 路演 后面做成字典表
		//pai.setAuditId(userId);
		if (gContest.getgScore()!=null) {
			pai.setScore(Float.parseFloat(gContest.getgScore()));
		}
		pai.setSuggest(gContest.getComment());
		gAuditInfoService.saveByOther(pai);
		gContest=get(gContest.getId());
		Map<String,Object> vars=new HashMap<String,Object>();
		//查询本用户任务环节是否完成，改变业务表对应的状态
		//String taskDefKey = gContest.getAct().getTaskDefKey(); 	// 环节编号
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		boolean isLast=actTaskService.isMultiLast("gcontest",act.getTaskDefKey(),gContest.getAct().getProcInsId());
		if (isLast) { //如果当前任务环节完成了
			List<String> collegeSecs=new ArrayList<String>() ;
			gContest.setAuditState("2");//学院专家打完分
			vars=gContest.getVars();
			collegeSecs=userService.getCollegeSecs(gContest.getCreateBy().getId());
			vars.put("collegeSec",collegeSecs); //给学院教学秘书审批			vars.put("collegeSec",getCollegeSecs(gContest.getId()));
			float average=0;
			GAuditInfo infoSerch=new GAuditInfo();
			infoSerch.setGId(gContest.getId());
			infoSerch.setAuditLevel("1");
			average= gAuditInfoService.getAuditAvgInfo(infoSerch);
			gContest.setgScore(String.valueOf(average));
			gContest.setCollegeExportScore(average);
			gContest.setCollegeScore(average);
			super.dao.updateState(gContest);
			//如果当前任务环节完成了
			taskService.complete(taskId, vars);
			//免签收动作 只有这个角色可以审核。
			actTaskService.claimByProcInsId(gContest.getAct().getProcInsId(),collegeSecs);
		}else{
			taskService.complete(taskId, vars);
		}
	}

	@Transactional(readOnly = false)
	public void  saveAudit2(GContest gContest,Act act) {
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S2.getValue());  //1表示院内2表示校内
		pai.setAuditName(AuditStatusEnum.S2.getName());//网评 审核 路演 后面做成字典表
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		String comment=gContest.getComment();
		/*String score=gContest.getgScore();*/
		String grade=gContest.getGrade();
		//保存分数
		gContest=get(gContest.getId());
		if (String.valueOf(gContest.getCollegeScore())!=null) {
			pai.setScore(gContest.getCollegeScore());
		}
		String userId = UserUtils.getUser().getId();
		pai.setAuditId(userId);
		//保存审核意见
		pai.setSuggest(comment);
		gAuditInfoService.save(pai);
		User apply_User=UserUtils.getUser();
		User rec_User=new User();
		rec_User.setId(gContest.getDeclareId());


		List<String> scoreList= gContest.getScoreList();
		//查找审核标准
		List<ProjectStandardDetailVo> asList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
		if (asList!=null && asList.size()>0) {
			String isScore = asList.get(0).getIsEescoreNodes();
			String firstNode = GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_SECOND_ID);
			if (isScore!=null && isScore.contains(firstNode)) {
				if (asList != null && asList.size() > 0 && scoreList != null && scoreList.size() > 0) {
					for (int i = 0; i < asList.size(); i++) {
						AuditStandardDetailIns auditStandardDetailIns = new AuditStandardDetailIns();
						auditStandardDetailIns.setCheckElement(asList.get(i).getCheckElement());
						auditStandardDetailIns.setFid(gContest.getId());
						auditStandardDetailIns.setCheckPoint(asList.get(i).getCheckPoint());
						auditStandardDetailIns.setViewScore(asList.get(i).getViewScore());
						auditStandardDetailIns.setScore(scoreList.get(i));
						auditStandardDetailIns.setSort(asList.get(i).getSort());
						auditStandardDetailIns.setAuditInfoId(pai.getId());
						auditStandardDetailInsService.save(auditStandardDetailIns);
					}
				}
			}
		}

		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		ActYw actyw =actYwDao.get(gContest.getActywId());
		String typeName="";
		if (actyw.getProProject()!=null) {
			typeName=actyw.getProProject().getProjectName();
		}

		if (grade!=null) {
			vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,grade);
			if (grade.equals("1")) {
				List<String> schoolExperts=userService.getSchoolExperts();
				/*gContest.setCollegeScore(Integer.valueOf(score));*/
				gContest.setCollegeResult(grade);
				gContest.setCollegeSug(comment);
				gContest.setAuditState("3");
				vars.put("schoolExperts",schoolExperts);
				taskService.complete(taskId, vars);

				oaNotifyService.sendOaNotifyByType(apply_User,rec_User,"学院秘书审核",
						typeName+" "+gContest.getpName()+"项目，学院审核合格", OaNotify.Type_Enum.TYPE14.getValue(),gContest.getId());

			}else{
				gContest.setCollegeResult(grade);
				gContest.setCollegeSug(comment);
				gContest.setAuditState("9");
				teamUserHistoryService.updateFinishAsClose(gContest.getId());
				oaNotifyService.sendOaNotifyByType(apply_User,rec_User,"学院秘书审核",
						typeName+" "+gContest.getpName()+"项目，学院审核不合格", OaNotify.Type_Enum.TYPE14.getValue(),gContest.getId());

			}
		}else{
			vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,"0");
			gContest.setAuditState("9");
			teamUserHistoryService.updateFinishAsClose(gContest.getId());
		}

		// 改变主表的审核状态
		gContest.preUpdate();
		dao.updateState(gContest);
	}

	@Transactional(readOnly = false)
	public void  saveAuditSecond(GContest gContest,Act act,GAuditInfo pai) {
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S2.getValue());  //1表示院内2表示校内
		pai.setAuditName(AuditStatusEnum.S2.getName());//网评 审核 路演 后面做成字典表
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		String comment=gContest.getComment();
		if (String.valueOf(gContest.getCollegeExportScore())!=null) {
			pai.setScore(gContest.getCollegeExportScore());
		}
		String grade=gContest.getGrade();
		//保存分数
		gContest=get(gContest.getId());
		pai.setId(IdGen.uuid());
		//保存审核意见
		pai.setSuggest(comment);
		gAuditInfoService.saveByOther(pai);

		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());

		if (grade!=null) {
			vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,grade);
			if (grade.equals("1")) {
				List<String> schoolExperts=userService.getSchoolExperts();
				gContest.setCollegeScore(gContest.getCollegeExportScore());
				gContest.setCollegeResult(grade);
				gContest.setCollegeSug(comment);
				gContest.setAuditState("3");
				vars.put("schoolExperts",schoolExperts);
				taskService.complete(taskId, vars);
			}else{
				gContest.setCollegeResult(grade);
				gContest.setCollegeSug(comment);
				gContest.setAuditState("9");
				teamUserHistoryService.updateFinishAsClose(gContest.getId());
			}
		}else{
			vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,"0");
			gContest.setAuditState("9");
			teamUserHistoryService.updateFinishAsClose(gContest.getId());
		}

		// 改变主表的审核状态
		gContest.preUpdate();
		dao.updateState(gContest);
	}

	@Transactional(readOnly = false)
	public void saveAudit3(GContest gContest,Act act) {
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S3.getValue());  //1表示院内2表示校内
		pai.setAuditName(AuditStatusEnum.S3.getName());//网评 审核 路演 后面做成字典表
		if (gContest.getgScore()!=null) {
			pai.setScore(Float.parseFloat(gContest.getgScore()));
		}
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		pai.setSuggest(gContest.getComment());
		String userId = UserUtils.getUser().getId();
		pai.setAuditId(userId);
		gAuditInfoService.save(pai);
		//完成评分标准
		List<String> scoreList= gContest.getScoreList();
		//查找审核标准
		List<ProjectStandardDetailVo> asList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
		if (asList!=null && asList.size()>0) {
			String isScore = asList.get(0).getIsEescoreNodes();
			String firstNode = GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_THREE_ID);
			if (isScore!=null && isScore.contains(firstNode)) {
				if (asList != null && asList.size() > 0 && scoreList != null && scoreList.size() > 0) {
					for (int i = 0; i < asList.size(); i++) {
						AuditStandardDetailIns auditStandardDetailIns = new AuditStandardDetailIns();
						auditStandardDetailIns.setCheckElement(asList.get(i).getCheckElement());
						auditStandardDetailIns.setFid(gContest.getId());
						auditStandardDetailIns.setCheckPoint(asList.get(i).getCheckPoint());
						auditStandardDetailIns.setViewScore(asList.get(i).getViewScore());
						auditStandardDetailIns.setScore(scoreList.get(i));
						auditStandardDetailIns.setSort(asList.get(i).getSort());
						auditStandardDetailIns.setAuditInfoId(pai.getId());
						auditStandardDetailInsService.save(auditStandardDetailIns);
					}
				}
			}
		}

		User rec_User=new User();
		rec_User.setId(gContest.getDeclareId());

		gContest=get(gContest.getId());
		Map<String,Object> vars=new HashMap<String,Object>();
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());

		//查询本用户任务环节是否完成，改变业务表对应的状态
		//String taskDefKey = gContest.getAct().getTaskDefKey(); 	// 环节编号
		if (actTaskService.isMultiLast("gcontest",act.getTaskDefKey(),gContest.getAct().getProcInsId())) { //如果当前任务环节完成了
			List<String> schoolSecs=userService.getSchoolSecs();
			vars=gContest.getVars();
			vars.put("schoolSec",schoolSecs);
			gContest.setAuditState("4");//学院专家打完分
			float average=0;
			GAuditInfo infoSerch=new GAuditInfo();
			infoSerch.setGId(gContest.getId());
			infoSerch.setAuditLevel("3");
			average= gAuditInfoService.getAuditAvgInfo(infoSerch);
			gContest.setgScore(String.valueOf(average));
			gContest.setSchoolExportScore(average);
			gContest.setSchoolScore(average);
			taskService.complete(taskId, vars);
			actTaskService.claimByProcInsId(gContest.getAct().getProcInsId(),schoolSecs);
			dao.updateState(gContest);
		}else{
			taskService.complete(taskId, vars);
		}
	}

	@Transactional(readOnly = false)
	public void saveAuditThree(GContest gContest,Act act,GAuditInfo pai) {
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S3.getValue());  //1表示院内2表示校内
		pai.setAuditName(AuditStatusEnum.S3.getName());//网评 审核 路演 后面做成字典表
		if (gContest.getgScore()!=null) {
			pai.setScore(Float.parseFloat(gContest.getgScore()));
		}
		pai.setSuggest(gContest.getComment());
		gAuditInfoService.saveByOther(pai);
		gContest=get(gContest.getId());
		Map<String,Object> vars=new HashMap<String,Object>();
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		//查询本用户任务环节是否完成，改变业务表对应的状态
		//String taskDefKey = gContest.getAct().getTaskDefKey(); 	// 环节编号
		//isMultiLast判断是否为最后一个审核人
		if (actTaskService.isMultiLast("gcontest",act.getTaskDefKey(),gContest.getAct().getProcInsId())) { //如果当前任务环节完成了
			List<String> schoolSecs=userService.getSchoolSecs();
			vars=gContest.getVars();
			vars.put("schoolSec",schoolSecs);
			gContest.setAuditState("4");//学院专家打完分
			float average=0;
			GAuditInfo infoSerch=new GAuditInfo();
			infoSerch.setGId(gContest.getId());
			infoSerch.setAuditLevel("3");
			average= gAuditInfoService.getAuditAvgInfo(infoSerch);
			gContest.setgScore(String.valueOf(average));
			gContest.setSchoolExportScore(average);
			gContest.setSchoolScore(average);
			taskService.complete(taskId, vars);
			actTaskService.claimByProcInsId(gContest.getAct().getProcInsId(),schoolSecs);
			dao.updateState(gContest);
		}else{
			taskService.complete(taskId, vars);
		}
	}

	@Transactional(readOnly = false)
	public void  saveAudit4(GContest gContest,Act act) {
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S4.getValue());
		pai.setAuditName(AuditStatusEnum.S4.getName());
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		//保存分数
		String comment=gContest.getComment();
		String grade=gContest.getGrade();
		gContest=get(gContest.getId());
		if (String.valueOf(gContest.getSchoolScore())!=null) {
			pai.setScore(gContest.getSchoolScore());
		}
		//保存审核意见
		String userId = UserUtils.getUser().getId();
		pai.setAuditId(userId);
		pai.setSuggest(comment);
		gAuditInfoService.save(pai);

		List<String> scoreList= gContest.getScoreList();
		//查找审核标准
		List<ProjectStandardDetailVo> asList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_WP_ID),GContestNodeVo.YW_ID);
		if (asList!=null && asList.size()>0) {
			String isScore = asList.get(0).getIsEescoreNodes();
			String firstNode = GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_FOUR_ID);
			if (isScore!=null && isScore.contains(firstNode)) {
				if (asList != null && asList.size() > 0 && scoreList != null && scoreList.size() > 0) {
					for (int i = 0; i < asList.size(); i++) {
						AuditStandardDetailIns auditStandardDetailIns = new AuditStandardDetailIns();
						auditStandardDetailIns.setCheckElement(asList.get(i).getCheckElement());
						auditStandardDetailIns.setFid(gContest.getId());
						auditStandardDetailIns.setCheckPoint(asList.get(i).getCheckPoint());
						auditStandardDetailIns.setViewScore(asList.get(i).getViewScore());
						auditStandardDetailIns.setScore(scoreList.get(i));
						auditStandardDetailIns.setSort(asList.get(i).getSort());
						auditStandardDetailIns.setAuditInfoId(pai.getId());
						auditStandardDetailInsService.save(auditStandardDetailIns);
					}
				}
			}
		}

		User apply_User=UserUtils.getUser();
		User rec_User=new User();
		rec_User.setId(gContest.getDeclareId());


		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,grade);
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		ActYw actyw =actYwDao.get(gContest.getActywId());
		String typeName="";
		if (actyw.getProProject()!=null) {
			typeName=actyw.getProProject().getProjectName();
		}
		if (grade!=null) {
			if (grade.equals("1")) {
				List<String> schoolSecs=userService.getSchoolSecs();
				vars.put("schoolSecSec",schoolSecs);
				gContest.setSchoolResult(grade);
				//gContest.setSchoolScore(Integer.valueOf(score));
				gContest.setSchoolSug(comment);
				gContest.setAuditState("5");//网评审核完成
				taskService.complete(taskId, vars);
				oaNotifyService.sendOaNotifyByType(apply_User,rec_User,"学校管理员审核",
					typeName+" "+gContest.getpName()+"项目，学校审核合格", OaNotify.Type_Enum.TYPE14.getValue(),gContest.getId());

				actTaskService.claimByProcInsId(gContest.getProcInsId(),schoolSecs);
			}else{
				gContest.setSchoolResult(grade);
				gContest.setSchoolSug(comment);
				gContest.setAuditState("8");
				teamUserHistoryService.updateFinishAsClose(gContest.getId());
				oaNotifyService.sendOaNotifyByType(apply_User,rec_User,"学校管理员审核",
												typeName+" "+gContest.getpName()+"项目，学校审核不合格", OaNotify.Type_Enum.TYPE14.getValue(),gContest.getId());

				//taskService.complete(taskId, vars);
			}
		}else{
			gContest.setSchoolResult(grade);
			gContest.setSchoolSug(comment);
			gContest.setAuditState("8");
			teamUserHistoryService.updateFinishAsClose(gContest.getId());
			//taskService.complete(taskId, vars);
		}

		gContest.preUpdate();
		dao.updateState(gContest);
	}

	@Transactional(readOnly = false)
	public void  saveAuditFour(GContest gContest,Act act,GAuditInfo pai) {
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S4.getValue());
		pai.setAuditName(AuditStatusEnum.S4.getName());
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		//保存分数
		String comment=gContest.getComment();
		String grade=gContest.getGrade();
		gContest=get(gContest.getId());
		if (String.valueOf(gContest.getSchoolScore())!=null) {
			pai.setScore(gContest.getSchoolScore());
		}
		//保存审核意见
		pai.setSuggest(comment);
		gAuditInfoService.saveByOther(pai);

		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE,grade);
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		if (grade!=null) {
			if (grade.equals("1")) {
				List<String> schoolSecs=userService.getSchoolSecs();
				vars.put("schoolSecSec",schoolSecs);
				gContest.setSchoolResult(grade);
				gContest.setSchoolSug(comment);
				gContest.setAuditState("5");//网评审核完成
				taskService.complete(taskId, vars);
				actTaskService.claimByProcInsId(gContest.getProcInsId(),schoolSecs);
			}else{
				gContest.setSchoolResult(grade);
				gContest.setSchoolSug(comment);
				gContest.setAuditState("8");
				teamUserHistoryService.updateFinishAsClose(gContest.getId());
				//taskService.complete(taskId, vars);
			}
		}else{
			gContest.setSchoolResult(grade);
			gContest.setSchoolSug(comment);
			gContest.setAuditState("8");
			teamUserHistoryService.updateFinishAsClose(gContest.getId());
			//taskService.complete(taskId, vars);
		}
		gContest.preUpdate();
		dao.updateState(gContest);
	}

	@Transactional(readOnly = false)
	public void  saveAudit5(GContest gContest,Act act) {
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S5.getValue());
		pai.setAuditName(AuditStatusEnum.S5.getName());
		//保存打分人员id
		String userId = UserUtils.getUser().getId();
		pai.setAuditId(userId);
		//保存分数
		if (gContest.getgScore()!=null) {
			pai.setScore(Float.parseFloat(gContest.getgScore()));
		}
		//保存审核意见
		pai.setSuggest(gContest.getComment());
		gAuditInfoService.save(pai);

		List<String> scoreList= gContest.getScoreList();
		//查找审核标准
		List<ProjectStandardDetailVo> asList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_LY_ID),GContestNodeVo.YW_ID);
		if (asList!=null && asList.size()>0) {
			String isScore = asList.get(0).getIsEescoreNodes();
			String firstNode = GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_FIVE_ID);

			if (isScore!=null && isScore.contains(firstNode)) {
				if (asList != null && asList.size() > 0 && scoreList != null && scoreList.size() > 0) {
					for (int i = 0; i < asList.size(); i++) {
						AuditStandardDetailIns auditStandardDetailIns = new AuditStandardDetailIns();
						auditStandardDetailIns.setCheckElement(asList.get(i).getCheckElement());
						auditStandardDetailIns.setFid(gContest.getId());
						auditStandardDetailIns.setCheckPoint(asList.get(i).getCheckPoint());
						auditStandardDetailIns.setViewScore(asList.get(i).getViewScore());
						auditStandardDetailIns.setScore(scoreList.get(i));
						auditStandardDetailIns.setSort(asList.get(i).getSort());
						auditStandardDetailIns.setAuditInfoId(pai.getId());
						auditStandardDetailInsService.save(auditStandardDetailIns);
					}
				}
			}
		}
		User rec_User=new User();
		rec_User.setId(gContest.getDeclareId());

		//完成工作流
		/*String roleName=actTaskService.getNextRoleName("audit2","gcontest");  //从工作流中查询 下一步的角色集合
		//List<String> roles=userService.getRolesByName(roleName);*/
		String comment=gContest.getComment();
		String score=gContest.getgScore();
		//String grade=gContest.getGrade();
		gContest=get(gContest.getId());
		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		/*taskService.complete(gContest.getAct().getTaskId(), vars);*/
		List<String> schoolSecs=userService.getSchoolSecs();
		//应该通过act来获取
		vars.put("schoolSecSec",schoolSecs);
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		//gContest.setSchoolluyanResult(grade);
		gContest.setSchoolluyanScore(Integer.valueOf(score));
		gContest.setSchoolluyanSug(comment);
		gContest.setAuditState("6");//路演审核完成
		taskService.complete(taskId, vars);
		actTaskService.claimByProcInsId(gContest.getAct().getProcInsId(),schoolSecs);

		// 改变主表的审核状态
		//gContest.setAuditState("6");//审核完成
		gContest.preUpdate();
		dao.updateState(gContest);
	}

	@Transactional(readOnly = false)
	public void  saveAuditFive(GContest gContest,Act act,GAuditInfo pai) {
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S5.getValue());
		pai.setAuditName(AuditStatusEnum.S5.getName());
		//保存分数
		if (gContest.getgScore()!=null) {
			pai.setScore(Float.parseFloat(gContest.getgScore()));
		}
		//保存审核意见
		pai.setSuggest(gContest.getComment());
		gAuditInfoService.saveByOther(pai);
		//完成工作流
		/*String roleName=actTaskService.getNextRoleName("audit2","gcontest");  //从工作流中查询 下一步的角色集合
		//List<String> roles=userService.getRolesByName(roleName);*/
		String comment=gContest.getComment();
		String score=gContest.getgScore();
		//String grade=gContest.getGrade();
		gContest=get(gContest.getId());
		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		/*taskService.complete(gContest.getAct().getTaskId(), vars);*/
		List<String> schoolSecs=userService.getSchoolSecs();
		//应该通过act来获取
		vars.put("schoolSecSec",schoolSecs);
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		//gContest.setSchoolluyanResult(grade);
		gContest.setSchoolluyanScore(Integer.valueOf(score));
		gContest.setSchoolluyanSug(comment);
		gContest.setAuditState("6");//路演审核完成
		taskService.complete(taskId, vars);
		actTaskService.claimByProcInsId(gContest.getAct().getProcInsId(),schoolSecs);
		// 改变主表的审核状态
		//gContest.setAuditState("6");//审核完成
		gContest.preUpdate();
		dao.updateState(gContest);
	}

	@Transactional(readOnly = false)
	public void  saveAudit6(GContest gContest,Act act) {
		GAuditInfo pai=new GAuditInfo();
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S6.getValue());
		pai.setAuditName(AuditStatusEnum.S6.getName());
		String userId = UserUtils.getUser().getId();
		pai.setAuditId(userId);
		//保存审核意见
		pai.setSuggest(gContest.getComment());
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		String comment=gContest.getComment();
		//String score=gContest.getgScore();
		String grade=gContest.getGrade();
		gContest=get(gContest.getId());
		float m=(gContest.getSchoolScore()+gContest.getSchoolluyanScore())/2;
		pai.setScore(m);
		gAuditInfoService.save(pai);
		List<String> scoreList= gContest.getScoreList();
		//查找审核标准
		List<ProjectStandardDetailVo> asList =auditStandardDetailService.findStandardDetailByNode(GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_PJ_ID),GContestNodeVo.YW_ID);
		if (asList!=null && asList.size()>0) {
			String isScore = asList.get(0).getIsEescoreNodes();
			String firstNode = GContestNodeVo.getGNodeIdByNodeId(GContestNodeVo.GNODE_SIX_ID);
			if (isScore!=null && isScore.contains(firstNode)) {
				if (asList != null && asList.size() > 0 && scoreList != null && scoreList.size() > 0) {
					for (int i = 0; i < asList.size(); i++) {
						AuditStandardDetailIns auditStandardDetailIns = new AuditStandardDetailIns();
						auditStandardDetailIns.setCheckElement(asList.get(i).getCheckElement());
						auditStandardDetailIns.setFid(gContest.getId());
						auditStandardDetailIns.setCheckPoint(asList.get(i).getCheckPoint());
						auditStandardDetailIns.setViewScore(asList.get(i).getViewScore());
						auditStandardDetailIns.setScore(scoreList.get(i));
						auditStandardDetailIns.setSort(asList.get(i).getSort());
						auditStandardDetailIns.setAuditInfoId(pai.getId());
						auditStandardDetailInsService.save(auditStandardDetailIns);
					}
				}
			}
		}
		User apply_User=UserUtils.getUser();
		User rec_User=new User();
		rec_User.setId(gContest.getDeclareId());
		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		//应该通过act来获取
		List<String> schoolSecs=userService.getSchoolSecs();
		vars.put("schoolSecSec",schoolSecs);
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		gContest.setSchoolendResult(grade);
		gContest.setSchoolendSug(comment);
		gContest.setAuditState("7");//审核完成
		gContest.setSchoolendScore(m);
		taskService.complete(taskId, vars);
		ActYw actyw =actYwDao.get(gContest.getActywId());
		String typeName="";
		if (actyw.getProProject()!=null) {
			typeName=actyw.getProProject().getProjectName();
		}
		if (grade.equals("2")||grade.equals("3")||grade.equals("4")) {
			//保存获奖信息
			GContestAward gContestAward=new GContestAward();
			gContestAward.setContestId(gContest.getId());
			gContestAward.setAward(grade);
			gContestAward.setAwardLevel("3");
			String money=DictUtils.getDictLabel(grade,"competition_college_money","");
			if (money!=null) {
				gContestAward.setMoney(money);
			}
			gContestAwardService.save(gContestAward);
		}
		String result=DictUtils.getDictLabel(grade,"competition_college_prise","");
		oaNotifyService.sendOaNotifyByType(apply_User,rec_User,"学校管理员审核",
					typeName+" "+gContest.getpName()+"项目，审核结果为："+result, OaNotify.Type_Enum.TYPE14.getValue(),gContest.getId());

		gContest.preUpdate();
		dao.updateState(gContest);
		//更改完成后团队历史表中的状态
		teamUserHistoryService.updateFinishAsClose(gContest.getId());
		//保存学分

	}

	public void  saveAuditSix(GContest gContest,Act act,GAuditInfo pai) {
		pai.setGId(gContest.getId());  //主表id
		pai.setAuditLevel(AuditStatusEnum.S6.getValue());
		pai.setAuditName(AuditStatusEnum.S6.getName());
		//保存审核意见
		pai.setSuggest(gContest.getComment());
		if (gContest.getGrade()!=null) {
			pai.setGrade(gContest.getGrade());
		}
		String comment=gContest.getComment();
		String grade=gContest.getGrade();
		gContest=get(gContest.getId());
		float m=(gContest.getSchoolScore()+gContest.getSchoolluyanScore())/2;
		pai.setScore(m);
		gAuditInfoService.saveByOther(pai);
		Map<String,Object> vars=new HashMap<String,Object>();
		vars=gContest.getVars();
		//应该通过act来获取
		List<String> schoolSecs=userService.getSchoolSecs();
		vars.put("schoolSecSec",schoolSecs);
		String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
		gContest.setSchoolendResult(grade);
		gContest.setSchoolendSug(comment);
		gContest.setAuditState("7");//审核完成
		gContest.setSchoolendScore(m);
		taskService.complete(taskId, vars);
		if (grade.equals("2")||grade.equals("3")||grade.equals("4")) {
			//保存获奖信息
			GContestAward gContestAward=new GContestAward();
			gContestAward.setContestId(gContest.getId());
			gContestAward.setAward(grade);
			gContestAward.setAwardLevel("3");
			if (grade.equals("2")||grade.equals("3")||grade.equals("4")) {
				String money = DictUtils.getDictLabel(grade, "competition_college_money", "");
				if (money != null) {
					gContestAward.setMoney(money);
				}
			}
			gContestAwardService.save(gContestAward);
		}
		gContest.preUpdate();
		dao.updateState(gContest);
		//更改完成后团队历史表中的状态
		teamUserHistoryService.updateFinishAsClose(gContest.getId());
//		saveScore(gContest.getId(),grade);

	}

	//得到当前项目
	public List<GContest> getGcontestInfo(String gcontestUserId) {
		return dao.getGcontestInfo(gcontestUserId);
	}
	//得到最后一个项目
	public GContest getLastGcontestInfo(String gcontestUserId) {

		return dao.getLastGcontestInfo(gcontestUserId);
	}

	private  GAuditInfo getSortInfoByIdAndState(String gId,String auditStep,String collegeId) {
		GAuditInfo pai=new GAuditInfo();
        pai.setGId(gId);
        pai.setAuditLevel(auditStep);
        pai.setCollegeId(collegeId);
        GAuditInfo infos= gAuditInfoService.getSortInfoByIdAndState(pai);
        return infos;
    }

	private  GAuditInfo getSchoolSortInfoByIdAndState(String gId,String auditStep,String schoolId) {
		GAuditInfo pai=new GAuditInfo();
        pai.setGId(gId);
        pai.setAuditLevel(auditStep);
        pai.setSchoolId(schoolId);
        GAuditInfo infos= gAuditInfoService.getSortInfoByIdAndState(pai);
        return infos;
    }

	public JSONObject getListData(GContest gContest) {
       // User uesr=userService.findUserById(gContest.getDeclareId());
        User user = UserUtils.getUser();
        //jsondata 生产
    	JSONObject obj = new JSONObject();
		//项目基础信息表头
		JSONObject gContestobj = new JSONObject();
		if ( gContest!=null&&gContest.getCompetitionNumber()!=null) {
			gContestobj.put("code", gContest.getCompetitionNumber());
			gContestobj.put("name", gContest.getpName());
		}else{
			gContestobj.put("code", "");
			gContestobj.put("name", "");
		}
		//参赛组别
		//"初创组";
		String gcontestLevel="";
		if ( gContest!=null&&gContest.getLevel()!=null) {
			gcontestLevel=DictUtils.getDictLabel(gContest.getLevel(), "gcontest_level", "");//.getDictList("gcontest_level").;
		}
		//项目内容table_first
		JSONObject gContestContentlist = new JSONObject();
		//list
		JSONArray  gContestContentlistArray = new JSONArray ();
			//报名信息
			JSONObject arraySb= new JSONObject();
			arraySb.put("type", "0");
			//(从大赛申报表里面读取)
			GContestAnnounce gContestAnnounce=new GContestAnnounce();
			if ( gContest!=null&&gContest.getAnnounceId()!=null) {
				 gContestAnnounce=gContestAnnounceService.get(gContest.getAnnounceId());
			}else{
		        gContestAnnounce.setType("1");
		        gContestAnnounce.setStatus("1");
		        gContestAnnounce=gContestAnnounceService.getGContestAnnounce(gContestAnnounce);
			}
			if (gContestAnnounce!=null&&gContestAnnounce.getApplyStart()!=null) {
				arraySb.put("Date", DateUtils.formatDate(gContestAnnounce.getApplyStart(), "yyyy-MM-dd"));
			}else{
				arraySb.put("Date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
			}

			arraySb.put("SpeedOfProgress", "提交报名表");

			//添加附件
			SysAttachment sysAttachment=new SysAttachment();
			JSONArray  arraySbAtt = new JSONArray ();
			if (gContest!=null&&gContest.getId()!=null) {
				sysAttachment.setUid(gContest.getId());
				List<SysAttachment> sysAttachments=sysAttachmentService.findList(sysAttachment);
				for(int i=0;i<sysAttachments.size();i++) {
					JSONObject attachmentobj = new JSONObject();
					attachmentobj.put("link", sysAttachments.get(i).getName());
					attachmentobj.put("url", sysAttachments.get(i).getUrl());
					arraySbAtt.add(attachmentobj);
				}
				arraySb.put("list", arraySbAtt);
			}else{
				arraySb.put("list", arraySbAtt);
			}
		gContestContentlistArray.add(arraySb);
		//院赛信息
		JSONObject arrayYs= new JSONObject();
		arrayYs.put("type", "1");
		//(从大赛申报表里面读取)

		if (gContestAnnounce!=null&&gContestAnnounce.getCollegeStart()!=null) {
			arrayYs.put("Date", DateUtils.formatDate(gContestAnnounce.getCollegeStart(), "yyyy-MM-dd"));
		}else{
			arrayYs.put("Date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		}
		arrayYs.put("SpeedOfProgress", "院级");
		arrayYs.put("type", "1");
		arrayYs.put("group", gcontestLevel);
		if ( user.getOffice()!=null&&user.getOffice().getName()!=null) {
			arrayYs.put("School", user.getOffice().getName());
		}else{
			arrayYs.put("School","");
		}
		int num=gAuditInfoService.getCollegeCount("2",user.getOffice().getId());
		//"30"; //从大赛表中查询出来大赛学院排名
		arrayYs.put("Number_of_entries",num);
		JSONArray  arrayYsList = new JSONArray ();
		//院赛信息排名
		JSONObject arrayPm= new JSONObject();
		if (gContest!=null&&gContest.getId()!=null) {
			GAuditInfo collegeinfos= getSortInfoByIdAndState(gContest.getId(),"2",user.getOffice().getId());
			if (collegeinfos!=null) {
				arrayPm.put("College_score",collegeinfos.getScore());
				arrayPm.put("Proposal", collegeinfos.getSuggest());
				arrayPm.put("ranking", collegeinfos.getSort());
				arrayYsList.add(arrayPm);
				arrayYs.put("list", arrayYsList);
			}else{
				arrayPm.put("College_score","");
				arrayPm.put("Proposal", "");
				arrayPm.put("ranking", "");
				arrayYsList.add(arrayPm);
				arrayYs.put("list", arrayYsList);
			}
		}else{
			arrayPm.put("College_score","");
			arrayPm.put("Proposal", "");
			arrayPm.put("ranking", "");
			arrayYsList.add(arrayPm);
			arrayYs.put("list", arrayYsList);
		}
		gContestContentlistArray.add(arrayYs);

		//校赛
		JSONObject arrayXs= new JSONObject();
		arrayXs.put("type", "2");

		if (gContestAnnounce!=null&&gContestAnnounce.getSchoolStart()!=null) {
			arrayXs.put("Date", DateUtils.formatDate(gContestAnnounce.getSchoolStart(), "yyyy-MM-dd"));
		}else{
			arrayXs.put("Date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
		}
		arrayXs.put("SpeedOfProgress", "校赛");
		arrayXs.put("group", gcontestLevel);
		if ( user.getCompany()!=null&&user.getCompany().getName()!=null) {
			arrayXs.put("School", user.getCompany().getName());
		}else{
			arrayXs.put("School","");
		}

		//arrayXs.put("School", user.getCompany().getName());
		int numXs=gAuditInfoService.getSchoolCount("4",user.getOffice().getId());
		arrayXs.put("Number_of_entries",numXs);
		//从获奖表中获取
		if (gContest!=null&&gContest.getId()!=null) {
			GContestAward gca=gContestAwardService.getByGid(gContest.getId());
			if (gca!=null) {
				arrayXs.put("Awards",
						DictUtils.getDictLabel(gca.getAward(),"competition_college_prise","")
						);
				arrayXs.put("bonus",gca.getMoney());
			}else{
				arrayXs.put("Awards","");
				arrayXs.put("bonus","");
			}

			//String numXs="300"; //从大赛表中查询出来大赛参赛数排名
		}else{
			arrayXs.put("Awards","");
			arrayXs.put("bonus","");
		}
		//校赛信息
		JSONArray  arrayXsList = new JSONArray ();
		JSONObject xsWangpin= new JSONObject();
		if (gContest!=null&&gContest.getId()!=null&&user.getCompany()!=null) {
			GAuditInfo wangpinginfos= getSchoolSortInfoByIdAndState(gContest.getId(),"4",user.getCompany().getId());
			if (wangpinginfos!=null) {
				xsWangpin.put("getScore",wangpinginfos.getScore());
				xsWangpin.put("Review_the_content", wangpinginfos.getAuditName());
				xsWangpin.put("Current_rank", wangpinginfos.getSort());
				xsWangpin.put("advice", wangpinginfos.getSuggest());
				arrayXsList.add(xsWangpin);
			}else{
				xsWangpin.put("getScore","");
				xsWangpin.put("Review_the_content", "");
				xsWangpin.put("Current_rank", "");
				xsWangpin.put("advice", "");
				arrayXsList.add(xsWangpin);
			}
		}else{
			xsWangpin.put("getScore","");
			xsWangpin.put("Review_the_content", "");
			xsWangpin.put("Current_rank", "");
			xsWangpin.put("advice", "");
			arrayXsList.add(xsWangpin);
		}

		JSONObject xsLuyan= new JSONObject();
		if (gContest!=null&&gContest.getId()!=null&&user.getCompany()!=null) {
			GAuditInfo luyaninfos= getSchoolSortInfoByIdAndState(gContest.getId(),"5",user.getCompany().getId());
			if (luyaninfos!=null) {
				xsLuyan.put("getScore",luyaninfos.getScore());
				xsLuyan.put("Review_the_content", luyaninfos.getAuditName());
				xsLuyan.put("Current_rank", luyaninfos.getSort());
				xsLuyan.put("advice", luyaninfos.getSuggest());
				arrayXsList.add(xsLuyan);
			}else{
				xsLuyan.put("getScore","");
				xsLuyan.put("Review_the_content", "");
				xsLuyan.put("Current_rank", "");
				xsLuyan.put("advice", "");
				arrayXsList.add(xsLuyan);
			}
		}else{
			xsLuyan.put("getScore","");
			xsLuyan.put("Review_the_content", "");
			xsLuyan.put("Current_rank", "");
			xsLuyan.put("advice", "");
			arrayXsList.add(xsLuyan);
		}
		arrayXs.put("list", arrayXsList);
		gContestContentlistArray.add(arrayXs);
		gContestContentlist.put("list",gContestContentlistArray);

		obj.put("project_code", gContestobj);
		obj.put("table_first", gContestContentlist);
		return obj;
	}

	public List<GContest> getGcontestByNameNoId(String id, String pName) {
		List<GContest> gContests=dao.getGcontestByNameNoId(id,pName);
		return gContests;
	}

	public Page<Map<String, String>> getGcontestChangeList(Page<Map<String, String>> page, Map<String, Object> param) {

		if (page.getPageNo()<=0) {
			page.setPageNo(1);
		}
		int count=dao.getGcontestChangeListCount(param);
		param.put("offset", (page.getPageNo()-1)*page.getPageSize());
		param.put("pageSize", page.getPageSize());
		param.put("orderBy", page.getOrderBy());
		param.put("orderByType", page.getOrderByType());
		List<Map<String,String>> list=null;
		if (count>0) {
			list=dao.getGcontestChangeList(param);
			if (list!=null) {
				List<String> ids=new ArrayList<String>();
				for(Map<String,String> map:list) {
					ids.add(map.get("id"));
					GContest gContest=get(map.get("id"));
			    	if (gContest.getAct().getProcInsId()!=null) {
						String taskId=actTaskService.getTaskidByProcInsId(gContest.getAct().getProcInsId());
						Task task=actTaskService.getTask(taskId);
						if (task!=null) {
							map.put("taskDef",task.getProcessDefinitionId());
							map.put("taskIn",task.getProcessInstanceId());
						}
					}
			    	map.put("auditCode", map.get("auditState"));
					map.put("auditState", GContestStatusEnum.getNameByValue(map.get("auditState")));
				}
				List<Map<String,String>> ps=dao.getMyGcontestListPerson(ids);
				if (ps!=null&&ps.size()>0) {
					Map<String,String> psm=new HashMap<String,String>();
					for(Map<String,String> map:ps) {
						psm.put(map.get("id")+map.get("team_user_type"), map.get("pname"));
					}
					for(Map<String,String> map:list) {
						map.put("snames", psm.get(map.get("id")+"1"));
						map.put("tnames", psm.get(map.get("id")+"2"));
					}
				}
			}
		}
		page.setCount(count);
		page.setList(list);
		page.initialize();
		return page;
	}

	@Transactional(readOnly = false)
	public void changeCollegeExport(JSONArray myJsonArray, GContest gContest,boolean isAudit) {
		for(int i=0;i<myJsonArray.size();i++) {
			String auditName=(String) myJsonArray.getJSONObject(i).get("auditName");
			String auditId=(String) myJsonArray.getJSONObject(i).get("auditId");
			String auditSuggest=(String) myJsonArray.getJSONObject(i).get("auditSuggest");
			String auditScore=(String) myJsonArray.getJSONObject(i).get("auditScore");
			gContest.setComment(auditSuggest);
		    gContest.setgScore(auditScore);
		    GAuditInfo pai =new GAuditInfo();
		    if (isAudit) {
		    	pai.setId(auditId);
		    	pai.setSuggest(auditSuggest);
		    	pai.setScore(Float.parseFloat(auditScore));
		    	pai.setGId(gContest.getId());
		    	gAuditInfoService.updateData(pai);
		    	float average=0;
				GAuditInfo infoSerch=new GAuditInfo();
				infoSerch.setGId(gContest.getId());
				infoSerch.setAuditLevel("1");
				average= gAuditInfoService.getAuditAvgInfo(infoSerch);
				gContest.setCollegeExportScore(average);
				gContest.setCollegeScore(average);
				super.dao.updateState(gContest);
		    }else{
			    if (!auditId.isEmpty()) {
			    	pai.setId(auditId);
			    	pai.setSuggest(auditSuggest);
			    	pai.setGId(gContest.getId());
			    	pai.setScore(Float.parseFloat(auditScore));
			    	gAuditInfoService.updateData(pai);
			    	float average=0;
					GAuditInfo infoSerch=new GAuditInfo();
					infoSerch.setGId(gContest.getId());
					infoSerch.setAuditLevel("1");
					average= gAuditInfoService.getAuditAvgInfo(infoSerch);
					gContest.setCollegeExportScore(average);
					gContest.setCollegeScore(average);
					super.dao.updateState(gContest);
			    }else{
			    	Act act=new Act();
					act.setProcDefKey("gcontest");  //大赛流程名称
				    act.setTaskDefKey("audit1");   // 表示大赛流程阶段 见流程图的userTask的id
			    	User auditUser =userService.findUserById(auditName);
				    pai.setCreateBy(auditUser);
				    pai.setCreateDate(new Date());
				    pai.setAuditId(auditUser.getId());
				    pai.setId(IdGen.uuid());
					saveAuditFirst(gContest,act,pai);
			    }
		    }
		}
	}

	@Transactional(readOnly = false)
	public void changeCollege(JSONObject collegeObject, GContest gContest,boolean isAudit) {
		String collegeSuggest=(String) collegeObject.get("collegeSuggest");
		String collegeResult=(String) collegeObject.get("collegeResult");
		gContest.setComment(collegeSuggest);
	    gContest.setGrade(collegeResult);
	    //找到学院秘书
    	User collegeUser = userService.getCollegeSecUsers(gContest.getDeclareId());
		if (isAudit) {
			GAuditInfo pai=new GAuditInfo();
			pai.setAuditLevel("2");
			pai.setGId(gContest.getId());
			pai=gAuditInfoService.getGAuditInfoByIdAndState(pai);
			pai.setGrade(collegeResult);
	    	pai.setSuggest(collegeSuggest);
	    	//审核update方法
	    	gAuditInfoService.updateData(pai);
	    	gContest.setCollegeSug(collegeSuggest);
			gContest.setCollegeResult(collegeResult);
			super.dao.updateState(gContest);
		}else{
        	Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit2");   // 表示大赛流程阶段 见流程图的userTask的id
	    	GAuditInfo pai =new GAuditInfo();
	    	pai.setId(IdGen.uuid());
	    	pai.setCreateDate(new Date());
	    	pai.setCreateBy(collegeUser);
	    	pai.setCreateDate(new Date());
		    pai.setAuditId(collegeUser.getId());
			saveAuditSecond(gContest,act,pai);
		}

	}

	@Transactional(readOnly = false)
	public void changeSchoolExport(JSONArray myJsonArray, GContest gContest, boolean isAudit) {
		for(int i=0;i<myJsonArray.size();i++) {
			String auditName=(String) myJsonArray.getJSONObject(i).get("auditName");
			String auditId=(String) myJsonArray.getJSONObject(i).get("auditId");
			String auditSuggest=(String) myJsonArray.getJSONObject(i).get("auditSuggest");
			String auditScore=(String) myJsonArray.getJSONObject(i).get("auditScore");
			gContest.setComment(auditSuggest);
		    gContest.setgScore(auditScore);
		    GAuditInfo pai =new GAuditInfo();
		    if (isAudit) {
		    	pai.setId(auditId);
		    	pai.setSuggest(auditSuggest);
		    	pai.setScore(Float.parseFloat(auditScore));
		       	pai.setGId(gContest.getId());
		    	gAuditInfoService.updateData(pai);
		    	float average=0;
				GAuditInfo infoSerch=new GAuditInfo();
				infoSerch.setGId(gContest.getId());
				infoSerch.setAuditLevel("3");
				average= gAuditInfoService.getAuditAvgInfo(infoSerch);
				gContest.setCollegeExportScore(average);
				gContest.setCollegeScore(average);
				super.dao.updateState(gContest);
		    }else{
			    if (!auditId.isEmpty()) {
			    	pai.setId(auditId);
			    	pai.setSuggest(auditSuggest);
			    	pai.setScore(Float.parseFloat(auditScore));
			    	gAuditInfoService.updateData(pai);
			    	float average=0;
					GAuditInfo infoSerch=new GAuditInfo();
					infoSerch.setGId(gContest.getId());
					infoSerch.setAuditLevel("1");
					average= gAuditInfoService.getAuditAvgInfo(infoSerch);
					gContest.setCollegeExportScore(average);
					gContest.setCollegeScore(average);
					super.dao.updateState(gContest);
			    }else{
			    	Act act=new Act();
					act.setProcDefKey("gcontest");  //大赛流程名称
				    act.setTaskDefKey("audit3");   // 表示大赛流程阶段 见流程图的userTask的id
			    	User auditUser =userService.findUserById(auditName);
			    	pai.setId(IdGen.uuid());
				    pai.setCreateBy(auditUser);
				    pai.setCreateDate(new Date());
				    pai.setAuditId(auditUser.getId());
					saveAuditThree(gContest,act,pai);
			    }
		    }
		}
	}

	@Transactional(readOnly = false)
	public void changeSchool(JSONObject schoolObject, GContest gContest,boolean isAudit) {
		String schoolSuggest=(String)schoolObject.get("schoolSuggest");
		String schoolResult=(String)schoolObject.get("schoolResult");
		gContest.setComment(schoolSuggest);
	    gContest.setGrade(schoolResult);
		if (isAudit) {
			GAuditInfo pai=new GAuditInfo();
			pai.setAuditLevel("4");
			pai.setGId(gContest.getId());
			pai=gAuditInfoService.getGAuditInfoByIdAndState(pai);
			pai.setGrade(schoolResult);
	    	pai.setSuggest(schoolSuggest);
	    	//审核update方法
	    	gAuditInfoService.updateData(pai);
	    	gContest.setSchoolSug(schoolSuggest);
			gContest.setSchoolResult(schoolResult);
			super.dao.updateState(gContest);
		}else{
        	Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit4");   // 表示大赛流程阶段 见流程图的userTask的id
		    //找到学校秘书
	    	User schoolUser = userService.getSchoolSecUsers();
	    	GAuditInfo pai =new GAuditInfo();
	    	pai.setId(IdGen.uuid());
	    	pai.setCreateDate(new Date());
	    	pai.setCreateBy(schoolUser);
	    	pai.setCreateDate(new Date());
		    pai.setAuditId(schoolUser.getId());
			saveAuditFour(gContest,act,pai);
		}
	}

	@Transactional(readOnly = false)
	public void changeSchoolly(JSONObject schoolObject, GContest gContest,boolean isAudit) {
		String schoollySuggest=(String) schoolObject.get("schoollySuggest");
		String schoollyScore=(String) schoolObject.get("schoollyScore");
		gContest.setComment(schoollySuggest);
	    gContest.setgScore(schoollyScore);

		if (isAudit) {
			GAuditInfo pai=new GAuditInfo();
			pai.setAuditLevel("5");
			pai.setGId(gContest.getId());
			pai=gAuditInfoService.getGAuditInfoByIdAndState(pai);
			pai.setScore(Float.parseFloat(gContest.getgScore()));
	    	pai.setSuggest(schoollySuggest);
	    	//审核update方法
	    	gAuditInfoService.updateData(pai);
	    	gContest.setSchoolluyanSug(schoollySuggest);
			gContest.setSchoolluyanScore(Float.parseFloat(gContest.getgScore()));
			super.dao.updateState(gContest);
		}else{
        	Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit5");   	//表示大赛流程阶段 见流程图的userTask的id
	    	GAuditInfo pai =new GAuditInfo();
	    	pai.setId(IdGen.uuid());
	    	pai.setCreateDate(new Date());
	    	 //找到学校秘书
	    	User schoolUser = userService.getSchoolSecUsers();
	    	pai.setCreateBy(schoolUser);
	    	pai.setCreateDate(new Date());
		    pai.setAuditId(schoolUser.getId());
			saveAuditFive(gContest,act,pai);
		}
	}

	@Transactional(readOnly = false)
	public void changeSchoolend(JSONObject schoolObject, GContest gContest,boolean isAudit) {
		String schoolendSuggest=(String) schoolObject.get("schoolendSuggest");
		String schoolendResult=(String) schoolObject.get("schoolendResult");
		gContest.setComment(schoolendSuggest);
	    gContest.setGrade(schoolendResult);

		if (isAudit) {
			GAuditInfo pai=new GAuditInfo();
			pai.setAuditLevel("6");
			pai.setGId(gContest.getId());
			pai=gAuditInfoService.getGAuditInfoByIdAndState(pai);
			pai.setGrade(schoolendResult);
	    	pai.setSuggest(schoolendSuggest);
			//判断学分是否授权
//			saveScore(gContest.getId(),schoolendResult);
	    	//审核update方法
	    	gAuditInfoService.updateData(pai);
	    	gContest.setSchoolendSug(schoolendSuggest);
			gContest.setSchoolendResult(schoolendResult);
			super.dao.updateState(gContest);
			if (schoolendResult.equals("2")||schoolendResult.equals("3")||schoolendResult.equals("4")) {
				//保存获奖信息
				GContestAward gContestAward=gContestAwardService.getByGid(gContest.getId());
				if (gContestAward!=null) {
					gContestAward.setContestId(gContest.getId());
					gContestAward.setAward(schoolendResult);
					gContestAward.setAwardLevel("3");
					if (schoolendResult.equals("2")||schoolendResult.equals("3")||schoolendResult.equals("4")) {
						String money = DictUtils.getDictLabel(schoolendResult, "competition_college_money", "");
						if (money != null) {
							gContestAward.setMoney(money);
						}
					}
					gContestAwardService.save(gContestAward);
				}else{
					gContestAward=new GContestAward();
					gContestAward.setContestId(gContest.getId());
					gContestAward.setAward(schoolendResult);
					gContestAward.setAwardLevel("3");
					if (schoolendResult.equals("2")||schoolendResult.equals("3")||schoolendResult.equals("4")) {
						String money = DictUtils.getDictLabel(schoolendResult, "competition_college_money", "");
						if (money != null) {
							gContestAward.setMoney(money);
						}
					}
					gContestAwardService.save(gContestAward);
				}

			}else{
				GContestAward gContestAward=gContestAwardService.getByGid(gContest.getId());
				if (gContestAward!=null) {
					gContestAwardService.delete(gContestAward);
				}
			}
		}else{
        	Act act=new Act();
			act.setProcDefKey("gcontest");  //大赛流程名称
		    act.setTaskDefKey("audit6");   	//表示大赛流程阶段 见流程图的userTask的id
	    	GAuditInfo pai =new GAuditInfo();
	    	pai.setId(IdGen.uuid());
	    	pai.setCreateDate(new Date());
	    	 //找到学校秘书
	    	User schoolUser = userService.getSchoolSecUsers();
	    	pai.setCreateBy(schoolUser);
	    	pai.setCreateDate(new Date());
		    pai.setAuditId(schoolUser.getId());
			saveAuditSix(gContest,act,pai);
		}
	}

	@Transactional(readOnly = false)
	public void changeFailState(GContest gContest, String oldId) {
		sysAttachmentService.updateAtt(gContest.getId(),oldId);
		gAuditInfoService.deleteByGid(oldId);
		delete(get(oldId));
	}

	@Transactional(readOnly = false)
	public JSONObject changeCollegefail(JSONObject myJSONObject ,GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		//后续添加失败流程
    	JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
		String oldId=gContest.getId();
		//gContest.setId("");
		String gid=submitOld(gContest);
		if (gid==null || gid.equals("")) {
			js.put("ret",0);
			js.put("msg", "该学院专家已被删除，变更失败!");
			return js;
		}
		gContest=get(gid);
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			List<GAuditInfo> collegeinfos= getInfo(oldId,"1");
			if (collegeExport.size()==collegeinfos.size()) {
				for(int i=0;i<collegeExport.size();i++) {
    				((JSONObject) collegeExport.get(i)).put("auditId","");
    			}
				changeCollegeExport(collegeExport,gContest,false);
			}else{
        		for(GAuditInfo gAuditInfo:collegeinfos) {
        			for(int i=0;i<collegeExport.size();i++) {
        				String auditName=(String) collegeExport.getJSONObject(i).get("auditName");
        				User exportUser=userService.findUserById(auditName);
        				if (gAuditInfo.getAuditId().equals(exportUser.getId())) {
        					((JSONObject) collegeExport.get(i)).put("auditId","");
        					break;
        				}
        			}
    				JSONObject exportObject=new JSONObject();
    				exportObject.put("auditScore",String.valueOf(gAuditInfo.getScore()));
    				exportObject.put("auditId", "");
    				exportObject.put("auditName", gAuditInfo.getCreateBy().getId());
    				exportObject.put("auditSuggest", gAuditInfo.getSuggest());
    				collegeExport.add(exportObject);
        		}
        		changeCollegeExport(collegeExport,gContest,false);
			}
		}else{
			JSONArray collegeExport=new JSONArray();
			List<GAuditInfo> collegeinfos= getInfo(oldId,"1");
			for(GAuditInfo gAuditInfo:collegeinfos) {
				JSONObject exportObject=new JSONObject();
				/*float score=gAuditInfo.getScore();
				DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				String auditScore=decimalFormat.format(score);//format 返回的是字符串*/
				exportObject.put("auditScore", String.valueOf(gAuditInfo.getScore()));
				exportObject.put("auditId", "");
				exportObject.put("auditName", gAuditInfo.getCreateBy().getId());
				exportObject.put("auditSuggest", gAuditInfo.getSuggest());
				collegeExport.add(exportObject);
			}
			changeCollegeExport(collegeExport,gContest,false);
		}
		changeCollege(collegeObject,gContest,false);
		changeFailState(gContest,oldId);
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject changeSchoolfail(JSONObject myJSONObject ,GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		//后续添加失败流程
		String oldId=gContest.getId();
		gContest.setId("");
		String gid=submitOld(gContest);
		if (gid.isEmpty()) {

			js.put("ret",0);
			js.put("msg", "该学校专家已被删除，变更失败!");
			return js;
		}
		gContest=get(gid);

		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			List<GAuditInfo> collegeinfos= getInfo(oldId,"1");
			if (collegeExport.size()==collegeinfos.size()) {
				for(int i=0;i<collegeExport.size();i++) {
    				((JSONObject) collegeExport.get(i)).put("auditId","");
    			}
				changeCollegeExport(collegeExport,gContest,false);
			}else{
        		for(GAuditInfo gAuditInfo:collegeinfos) {
        			for(int i=0;i<collegeExport.size();i++) {
        				String auditName=(String) collegeExport.getJSONObject(i).get("auditName");
        				User exportUser=userService.findUserById(auditName);
        				if (gAuditInfo.getAuditId().equals(exportUser.getId())) {
        					((JSONObject) collegeExport.get(i)).put("auditId","");
        					break;
        				}
        			}
    				JSONObject exportObject=new JSONObject();
    				exportObject.put("auditScore",String.valueOf(gAuditInfo.getScore()));
    				exportObject.put("auditName", gAuditInfo.getCreateBy().getId());
    				exportObject.put("auditId", "");
    				exportObject.put("auditSuggest", gAuditInfo.getSuggest());
    				collegeExport.add(exportObject);
        		}
        		changeCollegeExport(collegeExport,gContest,false);
			}
		}else{
			JSONArray collegeExport=new JSONArray();
			List<GAuditInfo> collegeinfos= getInfo(oldId,"1");
			for(GAuditInfo gAuditInfo:collegeinfos) {
				JSONObject exportObject=new JSONObject();
				/*float score=gAuditInfo.getScore();
				DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				String auditScore=decimalFormat.format(score);//format 返回的是字符串*/
				exportObject.put("auditScore", String.valueOf(gAuditInfo.getScore()));
				exportObject.put("auditId", "");
				exportObject.put("auditName", gAuditInfo.getCreateBy().getId());
				exportObject.put("auditSuggest", gAuditInfo.getSuggest());
				collegeExport.add(exportObject);
			}
			changeCollegeExport(collegeExport,gContest,false);
		}
		JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
		if (myJSONObject.get("college")!=null) {
			changeCollege(collegeObject,gContest,false);
		}else{
			List<GAuditInfo> collegeInfo=getInfo(oldId,"2");
			GAuditInfo gAuditInfo=collegeInfo.get(0);
			collegeObject=new JSONObject();
			collegeObject.put("collegeResult", gAuditInfo.getGrade());
			collegeObject.put("collegeSuggest", gAuditInfo.getSuggest());
			changeCollege(collegeObject,gContest,false);
		}
		if (myJSONObject.get("schoolExport")!=null) {
			JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
			List<GAuditInfo> schoolinfos= getInfo(oldId,"3");
			if (schoolExport.size()==schoolinfos.size()) {
				for(int i=0;i<schoolExport.size();i++) {
    				((JSONObject) schoolExport.get(i)).put("auditId","");
    			}
				changeSchoolExport(schoolExport,gContest,false);
			}else{
        		for(GAuditInfo gAuditInfo:schoolinfos) {
        			for(int i=0;i<schoolExport.size();i++) {
        				String auditName=(String) schoolExport.getJSONObject(i).get("auditName");
        				User exportUser=userService.findUserById(auditName);
        				if (gAuditInfo.getAuditId().equals(exportUser.getId())) {
        					((JSONObject) schoolExport.get(i)).put("auditId","");
        					break;
        				}
        			}
    				JSONObject exportObject=new JSONObject();
    				exportObject.put("auditScore",String.valueOf(gAuditInfo.getScore()));
    				exportObject.put("auditId", "");
    				exportObject.put("auditName", gAuditInfo.getCreateBy().getId());
    				exportObject.put("auditSuggest", gAuditInfo.getSuggest());
    				schoolExport.add(exportObject);
        		}
        		changeSchoolExport(schoolExport,gContest,false);
			}
		}else{
			JSONArray schoolExport=new JSONArray();
			List<GAuditInfo> schoolinfos= getInfo(oldId,"3");
			for(GAuditInfo gAuditInfo:schoolinfos) {
				JSONObject exportObject=new JSONObject();
				/*float score=gAuditInfo.getScore();
				DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				String auditScore=decimalFormat.format(score);//format 返回的是字符串*/
				exportObject.put("auditScore", String.valueOf(gAuditInfo.getScore()));
				exportObject.put("auditId","");
				exportObject.put("auditName", gAuditInfo.getCreateBy().getId());
				exportObject.put("auditSuggest", gAuditInfo.getSuggest());
				schoolExport.add(exportObject);
			}
			changeSchoolExport(schoolExport,gContest,false);
		}
		JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
		changeSchool(schoolObject,gContest,false);
		changeFailState(gContest,oldId);
		return js;
	}

	private  List<GAuditInfo> getInfo(String gId,String auditStep) {
		GAuditInfo pai=new GAuditInfo();
	    pai.setGId(gId);
	    pai.setAuditLevel(auditStep);
	    List<GAuditInfo> infos= gAuditInfoService.getInfo(pai);
	    return infos;
	}

	@Transactional(readOnly = false)

	public JSONObject updateFirst(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,false);
			gContest=get(gContest.getId());
			//第一步完成才能进行第二步
			gContest=get(gContest.getId());
			if (gContest.getAuditState().equals("2")&&myJSONObject.get("college")!=null) {
				JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
				String collegeResult=(String) collegeObject.get("collegeResult");
				if (collegeResult.equals("1")) {
					changeCollege(collegeObject,gContest,false);
					//第二步完成才能进行第三步
					gContest=get(gContest.getId());
					if (gContest.getAuditState().equals("3")&&myJSONObject.get("schoolExport")!=null) {
						JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
						changeSchoolExport(schoolExport,gContest,false);
						//第三步完成才能进行第四步
						gContest=get(gContest.getId());
						if (gContest.getAuditState().equals("4")&&myJSONObject.get("school")!=null) {
							JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
							String schoolResult=(String) schoolObject.get("schoolResult");
							if (schoolResult.equals("1")) {
								changeSchool(schoolObject,gContest,false);
								//第四步完成才能进行第五步
								gContest=get(gContest.getId());
								if (gContest.getAuditState().equals("5")&&myJSONObject.get("schoolly")!=null) {
									JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
									changeSchoolly(schoollyObject,gContest,false);
									//第五步完成才能进行第六步
									gContest=get(gContest.getId());
									if (gContest.getAuditState().equals("6")&& myJSONObject.get("schoolend")!=null) {
										JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
										changeSchoolend(schoolendObject,gContest,false);
									}
								}
							}else{
								changeSchool(schoolObject,gContest,false);
								//校赛失败后续添加流程
							}
						}
					}
				}else{
					//院赛失败后续添加流程
					changeCollege(collegeObject,gContest,false);
				}
			}
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateSecond(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				changeCollege(collegeObject,gContest,false);
				//第二步完成才能进行第三步
				gContest=get(gContest.getId());
				if (gContest.getAuditState().equals("3")&&myJSONObject.get("schoolExport")!=null) {
					JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
					changeSchoolExport(schoolExport,gContest,false);
					//第三步完成才能进行第四步
					gContest=get(gContest.getId());
					if (gContest.getAuditState().equals("4")&&myJSONObject.get("school")!=null) {
						JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
						String schoolResult=(String) schoolObject.get("schoolResult");
						if (schoolResult.equals("1")) {
							changeSchool(schoolObject,gContest,false);
							//第四步完成才能进行第五步
							gContest=get(gContest.getId());
							if (gContest.getAuditState().equals("5")&&myJSONObject.get("schoolly")!=null) {
								JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
								changeSchoolly(schoollyObject,gContest,false);
								//第五步完成才能进行第六步
								gContest=get(gContest.getId());
								if (gContest.getAuditState().equals("6")&& myJSONObject.get("schoolend")!=null) {
									JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
									changeSchoolend(schoolendObject,gContest,false);
								}
							}
						}else{
							//校赛失败后续添加流程
							changeSchool(schoolObject,gContest,false);
						}
					}
				}
			}else{
				//后续添加失败流程
				changeCollege(collegeObject,gContest,false);
			}
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateThree(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				changeCollege(collegeObject,gContest,true);
			}else{
				//后续添加失败流程
				js=changeCollegefail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolExport")!=null) {
			JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
			changeSchoolExport(schoolExport,gContest,false);
			//第三步完成才能进行第四步
			gContest=get(gContest.getId());
			if (gContest.getAuditState().equals("4")&&myJSONObject.get("school")!=null) {
				JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
				String schoolResult=(String) schoolObject.get("schoolResult");
				if (schoolResult.equals("1")) {
					changeSchool(schoolObject,gContest,false);
					//第四步完成才能进行第五步
					gContest=get(gContest.getId());
					if (gContest.getAuditState().equals("5")&&myJSONObject.get("schoolly")!=null) {
						JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
						changeSchoolly(schoollyObject,gContest,false);
						//第五步完成才能进行第六步
						gContest=get(gContest.getId());
						if (gContest.getAuditState().equals("6")&& myJSONObject.get("schoolend")!=null) {
							JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
							changeSchoolend(schoolendObject,gContest,false);
						}
					}
				}else{
					//校赛失败后续添加流程
					changeSchool(schoolObject,gContest,false);
				}
			}
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateFour(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				changeCollege(collegeObject,gContest,true);
			}else{
				//后续添加失败流程
				js=changeCollegefail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolExport")!=null) {
			JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
			changeSchoolExport(schoolExport,gContest,true);
		}
		if (myJSONObject.get("school")!=null) {
			JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
			String schoolResult=(String) schoolObject.get("schoolResult");
			if (schoolResult.equals("1")) {
				changeSchool(schoolObject,gContest,false);
				//第四步完成才能进行第五步
				gContest=get(gContest.getId());
				if (gContest.getAuditState().equals("5")&&myJSONObject.get("schoolly")!=null) {
					JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
					changeSchoolly(schoollyObject,gContest,false);
					//第五步完成才能进行第六步
					gContest=get(gContest.getId());
					if (gContest.getAuditState().equals("6")&& myJSONObject.get("schoolend")!=null) {
						JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
						changeSchoolend(schoolendObject,gContest,false);
					}
				}
			}else{
				//校赛失败后续添加流程
				changeSchool(schoolObject,gContest,false);
			}
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateFive(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				changeCollege(collegeObject,gContest,true);
			}else if (collegeResult.equals("0")) {
				//后续添加失败流程
				js=changeCollegefail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolExport")!=null) {
			JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
			changeSchoolExport(schoolExport,gContest,true);
		}
		if (myJSONObject.get("school")!=null) {
			JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
			String schoolResult=(String) schoolObject.get("schoolResult");
			if (schoolResult.equals("1")) {
				changeSchool(schoolObject,gContest,true);
			}else{
				//校赛添加失败流程
				js=changeSchoolfail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolly")!=null) {
			JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
			changeSchoolly(schoollyObject,gContest,false);
			//第五步完成才能进行第六步
			gContest=get(gContest.getId());
			if (gContest.getAuditState().equals("6")&& myJSONObject.get("schoolend")!=null) {
				JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
				changeSchoolend(schoolendObject,gContest,false);
			}
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateSix(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				changeCollege(collegeObject,gContest,true);
			}else if (collegeResult.equals("0")) {
				//后续添加失败流程
				js=changeCollegefail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolExport")!=null) {
			JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
			changeSchoolExport(schoolExport,gContest,true);
		}
		if (myJSONObject.get("school")!=null) {
			JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
			String schoolResult=(String) schoolObject.get("schoolResult");
			if (schoolResult.equals("1")) {
				changeSchool(schoolObject,gContest,true);
			}else{
				//校赛后续添加失败流程
				js=changeSchoolfail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolly")!=null) {
			JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
			changeSchoolly(schoollyObject,gContest,true);
		}
		//第五步完成才能进行第六步
		if (myJSONObject.get("schoolend")!=null) {
			JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
			changeSchoolend(schoolendObject,gContest,false);
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateSeven(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		js.put("ret", 1);
		js.put("msg", "大赛变更成功!");
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				changeCollege(collegeObject,gContest,true);
			}else{
				//后续添加失败流程
				js=changeCollegefail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolExport")!=null) {
			JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
			changeSchoolExport(schoolExport,gContest,true);
		}
		if (myJSONObject.get("school")!=null) {
			JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
			String schoolResult=(String) schoolObject.get("schoolResult");
			if (schoolResult.equals("1")) {
				changeSchool(schoolObject,gContest,true);
			}else{
				//校赛添加失败流程
				js=changeSchoolfail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolly")!=null) {
			JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
			changeSchoolly(schoollyObject,gContest,true);
		}
		//第五步完成才能进行第六步
		if (myJSONObject.get("schoolend")!=null) {
			JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
			changeSchoolend(schoolendObject,gContest,true);
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateSchoolFail(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				changeCollege(collegeObject,gContest,true);
			}else{
				//后续添加失败流程
				js=changeCollegefail(myJSONObject,gContest);
				myJSONObject=new JSONObject();
			}
		}
		if (myJSONObject.get("schoolExport")!=null) {
			JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
			changeSchoolExport(schoolExport,gContest,true);
		}
		if (myJSONObject.get("school")!=null) {
			JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
			String schoolResult=(String) schoolObject.get("schoolResult");
			if (schoolResult.equals("1")) {
				List<GAuditInfo> schoolsecinfos= getInfo(gContest.getId(),"4");
				if (schoolsecinfos.size()>0) {
					gAuditInfoService.delete(schoolsecinfos.get(0));
				}
				changeSchool(schoolObject,gContest,false);
				gContest=get(gContest.getId());
				if (gContest.getAuditState().equals("5")&& myJSONObject.get("schoolly")!=null) {
					JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
					changeSchoolly(schoollyObject,gContest,false);
					//第五步完成才能进行第六步
					gContest=get(gContest.getId());
					if (gContest.getAuditState().equals("6")&& myJSONObject.get("schoolend")!=null) {
						JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
						changeSchoolend(schoolendObject,gContest,false);
					}
				}
			}else{
				changeSchool(schoolObject,gContest,true);
//				delScore(gContest.getId());
				//后续添加失败流程
			}
		}
		return js;
	}

	@Transactional(readOnly = false)
	public JSONObject updateCollegeFail(JSONObject myJSONObject, GContest gContest) {
		JSONObject js=new JSONObject();
		if (myJSONObject.get("collegeExport")!=null) {
			JSONArray collegeExport=(JSONArray) myJSONObject.get("collegeExport");
			changeCollegeExport(collegeExport,gContest,true);
		}
		if (myJSONObject.get("college")!=null) {
			JSONObject collegeObject=(JSONObject) myJSONObject.get("college");
			String collegeResult=(String) collegeObject.get("collegeResult");
			if (collegeResult.equals("1")) {
				List<GAuditInfo> collegeinfos= getInfo(gContest.getId(),"2");
				if (collegeinfos.size()>0) {
					gAuditInfoService.delete(collegeinfos.get(0));
				}
				changeCollege(collegeObject,gContest,false);
				//第二步完成才能进行第三步
				gContest=get(gContest.getId());
				if (gContest.getAuditState().equals("3")&&myJSONObject.get("schoolExport")!=null) {
					JSONArray schoolExport=(JSONArray) myJSONObject.get("schoolExport");
					changeSchoolExport(schoolExport,gContest,false);
					//第三步完成才能进行第四步
					gContest=get(gContest.getId());
					if (gContest.getAuditState().equals("4")&&myJSONObject.get("school")!=null) {
						JSONObject schoolObject=(JSONObject) myJSONObject.get("school");
						String schoolResult=(String) schoolObject.get("schoolResult");
						if (schoolResult.equals("1")) {
							changeSchool(schoolObject,gContest,false);
							//第四步完成才能进行第五步
							gContest=get(gContest.getId());
							if (gContest.getAuditState().equals("5")&&myJSONObject.get("schoolly")!=null) {
								JSONObject schoollyObject=(JSONObject) myJSONObject.get("schoolly");
								changeSchoolly(schoollyObject,gContest,false);
								//第五步完成才能进行第六步
								gContest=get(gContest.getId());
								if (gContest.getAuditState().equals("6")&& myJSONObject.get("schoolend")!=null) {
									JSONObject schoolendObject=(JSONObject) myJSONObject.get("schoolend");
									changeSchoolend(schoolendObject,gContest,false);
								}
							}
						}else if (schoolResult.equals("0")) {
							//校赛失败后续添加流程
							changeSchool(schoolObject,gContest,true);
//							delScore(gContest.getId());
						}
					}
				}
			}else{
				//后续添加失败流程
				changeCollege(collegeObject,gContest,true);
//				delScore(gContest.getId());
			}
		}
		return js;
	}

	public int findGcontestByTeamId(String id) {
		return dao.findGcontestByTeamId(id);
	}

	public int todoCount(Map<String, Object> param) {
		int count=dao.getTodoCount(param);
		return count;
	}

	public int hasdoCount(Map<String, Object> param) {
		int count=dao.getHasdoCount(param);
		return count;
	}


//	//删除学分
//	@Transactional(readOnly = false)
//	public void delScore(String gContestId) {
//		GContest gContest = dao.getScoreConfigure(gContestId);
//		if(gContest == null){
//		    return;
//        }
//		//插入学分前，先根据项目id删除对应的学分信息（sco_affirm表和sco_score表）
//		scoAffirmDao.deleteByProId(gContest.getId());
//		scoScoreDao.deleteProject(gContest.getId());
//	}



	//保存学分
//	@Transactional(readOnly = false)
//	public void saveScore(String gContestId,String grade) {
//		//判断学分是否授权
//		if(CoreUtils.checkMenuByNum(5)) {
//			GContest gContest = dao.getScoreConfigure(gContestId);
//			if(gContest == null){
//			    return;
//			}
//			//插入学分前，先根据项目id删除对应的学分信息（sco_affirm表和sco_score表）
//			scoAffirmDao.deleteByProId(gContest.getId());
//			scoScoreDao.deleteProject(gContest.getId());
//
//			//保存学分到对应的表（先保存到sco_affirm表、再保存到soc_score表）
//			//根据 type（学分类型)、item（学分项）、category（课程、项目、大赛、技能大类）、subdivision（课程、项目、大赛小类）、number(人数)查询后台配比
//			ScoAffirm scoAffirm = new ScoAffirm();
//			scoAffirm.setProId(gContest.getId());
//
//			ScoRatioVo scoRatioVo = new ScoRatioVo();
//			ScoAffrimCriterionVo scoAffrimCriterionVo = new ScoAffrimCriterionVo();
//
//			scoRatioVo.setType("0000000125"); //设置查询的学分类型（素质学分）
//			scoAffrimCriterionVo.setType("0000000125");
//
//			scoRatioVo.setItem("0000000129"); //双创大赛
//			scoAffrimCriterionVo.setItem("0000000129"); //双创大赛
//			scoRatioVo.setCategory("1"); //互联网+大赛
//			scoAffrimCriterionVo.setCategory("1");
//
//			scoRatioVo.setNumber(gContest.getSnumber());
//			ScoRatioVo ratioResult = scoAllotRatioDao.findRatio(scoRatioVo);
//			scoAffrimCriterionVo.setCategory2("1"); //校级初赛
//			scoAffrimCriterionVo.setResult(grade);
//			boolean hasConfig;  //判断后台是否配置了规则
//			if (ratioResult != null) {
//				hasConfig = true;
//			} else {
//				hasConfig = false;
//			}
//			//查找 学分认定标准 scoAffirmCriterion
//			ScoAffrimCriterionVo criterionResult = scoAffirmCriterionDao.findCriter(scoAffrimCriterionVo);
//			if (criterionResult != null) {  //有学分认定标准，则插入值
//				//插入scoAffirm表
//				scoAffirm.setScoreStandard(criterionResult.getScore());
//				scoAffirm.setScoreVal(criterionResult.getScore());
//				scoAffirm.setAffirmDate(new Date());
//				scoAffirm.preInsert();
//				scoAffirmDao.insert(scoAffirm);
//				//List<TeamUserRelation>  studentList=  teamUserRelationDao.getStudents(teamUserRelation);
//				List<Map<String, String>> studentList = projectDeclareService.findTeamStudentFromTUH(gContest.getTeamId(), gContest.getId());
//				if (!hasConfig) { //如果后台没有配比规则、则所有成员一样的分数
//					for (Map<String, String> teamUser : studentList) {
//						ScoScore scoScore = new ScoScore();
//						User user = new User();
//						user.setId(teamUser.get("userId"));
//						scoScore.setUser(user);
//						float score = criterionResult.getScore();
//						if (score > 0 & score < 0.5) {
//							score = 0.5f;
//						}
//						scoScore.setCreditScore((double) score);
//						scoScore.setCreditId(gContestId);
//						scoScore.preInsert();
//						scoScoreDao.insert(scoScore);
//					}
//				} else { //有配比，则查询team_user_relation中的配比
//					//查找学分分配权重之和
//					//int weigthTotal = teamUserRelationDao.getWeightTotalByTeamId(gContest.getTeamId());
//					int weigthTotal = teamUserHistoryService.getWeightTotalByTeamId(gContest.getTeamId(), gContest.getId());
//					for (Map<String, String> teamUser : studentList) {
//						ScoScore scoScore = new ScoScore();
//						User user = new User();
//						user.setId(teamUser.get("userId"));
//						scoScore.setUser(user);
//						String number = String.valueOf(teamUser.get("weightVal"));
//						int weightVal = Integer.parseInt(number);
//						float score = FloatUtils.getOnePoint(((float) weightVal / (float) weigthTotal) * criterionResult.getScore());
//						if (score > 0 & score < 0.5) {
//							score = 0.5f;
//						}
//						scoScore.setCreditScore((double) score);
//						scoScore.setCreditId(gContestId);
//						scoScore.preInsert();
//						scoScoreDao.insert(scoScore);
//					}
//				}
//			}
//		}
//	}

	public List<Map<String,String>>  getInGcontestNameList(String userId) {
		return dao.getInGcontestNameList(userId);
	}


	@Transactional(readOnly = false)
	public void deleteGContest(GContest gContest) {
		if (StringUtils.isNotBlank(gContest.getProcInsId())) {
			try{
				runtimeService.deleteProcessInstance(gContest.getProcInsId(),"");
			}catch (Exception e) {
				System.out.println("该项目的工作流已经走完");
			}
			try{
				historyService.deleteHistoricProcessInstance(gContest.getProcInsId());
			}catch (Exception e) {
				System.out.println("该项目没有历史记录");
			}
		}
		//删除项目
		delete(gContest);
		//删除团队历史记录表
		dao.deleteTeamUserHisByGConId(gContest.getId());
		//删除报优秀项目
		ExcellentShow excellentShow=excellentShowService.getByForid(gContest.getId());
		if (excellentShow!=null) {
			excellentShowService.delete(excellentShow);
		}

	}

	public GContest getExcellentById(String id) {
		return dao.getExcellentById(id);
	}
}