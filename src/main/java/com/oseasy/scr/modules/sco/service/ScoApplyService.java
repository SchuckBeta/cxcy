package com.oseasy.scr.modules.sco.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.com.fileserver.modules.attachment.dao.SysAttachmentDao;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.scr.modules.sco.dao.ScoApplyDao;
import com.oseasy.scr.modules.sco.dao.ScoCourseDao;
import com.oseasy.scr.modules.sco.entity.ScoAffirmConf;
import com.oseasy.scr.modules.sco.entity.ScoAffirmCriterionCouse;
import com.oseasy.scr.modules.sco.entity.ScoApply;
import com.oseasy.scr.modules.sco.entity.ScoAuditing;
import com.oseasy.scr.modules.sco.entity.ScoCourse;
import com.oseasy.scr.modules.sco.entity.ScoScore;
import com.oseasy.scr.modules.scr.vo.ScoRstatus;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 学分课程申请Service.
 * @author zhangzheng
 * @version 2017-07-13
 */
@Service
@Transactional(readOnly = true)
public class ScoApplyService extends CrudService<ScoApplyDao, ScoApply> {

	@Autowired
	SysAttachmentDao sysAttachmentDao;
	@Autowired
	private ScoAuditingService scoAuditingService;
	@Autowired
	private ScoScoreService scoScoreService;
	@Autowired
	private ScoAffirmConfService scoAffirmConfService;
	@Autowired
	private ScoAffirmCriterionCouseService scoAffirmCriterionCouseService;
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
 	private OaNotifyService oaNotifyService;
	@Autowired
	private ScoCourseService scoCourseService;
	@Autowired
	private SysAttachmentService sysAttachmentService;
	@Autowired
	private ScoCourseDao scoCourseDao;

	@Transactional(readOnly = false)
	public Long getCountToAudit() {
		ScoApply p=new ScoApply();
		p.setAuditStatus(ScoApply.auditStatus2);
		Long c=dao.getCount(p);
		return c==null?0L:c;
	}
	public ScoApply get(String id) {
		return super.get(id);
	}

	public List<ScoApply> findList(ScoApply scoApply) {
		return super.findList(scoApply);
	}
	@Transactional(readOnly = false)
	public Page<ScoApply> findPage(Page<ScoApply> page, ScoApply scoApply) {
		//保持原有数据不变，添加未申请的课程
		ScoApply entity = new ScoApply();
		entity.setUserId(scoApply.getUserId());
		List<ScoApply> scoApplyList = dao.findList(entity);
		List<ScoCourse> scoCourseList = scoCourseDao.findList(new ScoCourse());
		if(scoApplyList != null && scoCourseList != null && scoApplyList.size() != scoCourseList.size()){
			for(ScoCourse scoCourse : scoCourseList){
				ScoApply temp = new ScoApply();
				temp.setUserId(scoApply.getUserId());
				temp.setCourseId(scoCourse.getId());
				List<ScoApply> tempList = dao.findList(temp);
				if(tempList==null || tempList.size()==0){
					temp.setAuditStatus("0");
					add(temp);
				}
			}
		}
		return super.findPage(page, scoApply);
	}
	//添加学分认定
	@Transactional(readOnly = false)
	public void add(ScoApply scoApply) {
		super.save(scoApply);
	}

	@Transactional(readOnly = false)
	public void save(ScoApply scoApply) {

		super.save(scoApply);
		//向后台管理员发送申请消息
		List<String> roles = new ArrayList<String>();
		Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
		if(role!=null){
			roles = userService.findListByRoleId(role.getId());
			if(roles!=null && roles.size()>0){
				ScoCourse scoCourse = scoCourseService.get(scoApply.getCourseId());
				User user=UserUtils.getUser();
				oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "课程学分申请", user.getName()+"申请"+
						scoCourse.getName()+"课程的学分", OaNotify.Type_Enum.TYPE21.getValue(), scoApply.getId());
			}
		}
		//附件
		saveCreditResultApply(scoApply);
		//走工作流  学分类型的0000000127

		//User user = UserUtils.getUser();
		//ActYw actYw=actYwService.get(scoApply.getActYwId());
		delRun(scoApply);
		startRun(scoApply);
	}


	@Transactional(readOnly = false)
	public void saveCreditResultApply(ScoApply scoRapply){
		sysAttachmentService.deleteByUid(scoRapply.getId());
		//保存成果物附件
		sysAttachmentService.saveBySysAttachmentVo(scoRapply.getSysAttachmentList(), scoRapply.getId(), FileTypeEnum.S5, FileStepEnum.S5);
	}

	@Transactional(readOnly = false)
	public void delRun(ScoApply scoApply) {

	}

	//开启新审核流程
	@Transactional(readOnly = false)
	public void startRun(ScoApply scoApply) {
		ScoAffirmConf scoAffirmConf =scoAffirmConfService.getByItem("0000000127");
		// 根据学分配置得到审核节点
		ActYw actYw=actYwService.get(scoAffirmConf.getProcId());
		//启动工作流
		if (actYw!=null) {
			//得到流程下一步审核角色id
			String roleId=actTaskService.getProcessStartRoleName(ActYw.getPkey(actYw));  //从工作流中查询 下一步的角色集合
			//actTaskService.getNextRoleName()
			Role role= systemService.getNamebyId(roleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length()));
			//启动节点
			String roleName=role.getName();
			List<String> roles=new ArrayList<String>();
			if (roleName.contains(SysIds.ISCOLLEGE.getRemark())||roleName.contains(SysIds.ISMS.getRemark())) {
				roles=userService.findListByRoleIdAndOffice(role.getId(),scoApply.getUserId());
			}else{
				roles=userService.findListByRoleId(role.getId());
			}
			//根据角色id获得审核人
			//List<String> roles=userService.findListByRoleId(roleId);
			if (roles.size()>0) {
				//super.save(proModel);
				Map<String,Object> vars=new HashMap<String,Object>();
				vars=scoApply.getVars();
				vars.put(roleId+"s",roles);
				//vars.put(scoAffirmConf.getProcId(),roles);
				String key=ActYw.getPkey(actYw);
				String userId = UserUtils.getUser().getLoginName();
				identityService.setAuthenticatedUserId(userId);
				//启动流程节点
				//流程加tenant_id
				ProcessInstance procIns=runtimeService.startProcessInstanceByKeyAndTenantId(key, "score"+":"+scoApply.getId(),vars, TenantConfig.getCacheTenant());
				//流程id返写业务表
				Act act = new Act();
				act.setBusinessTable("sco_apply");// 业务表名
				act.setBusinessId(scoApply.getId());	// 业务表ID
				act.setProcInsId(procIns.getId());
				actDao.updateProcInsIdByBusinessId(act);
				scoApply.setProcInsId(act.getProcInsId());
				super.save(scoApply);
			}
		}
	}


	@Transactional(readOnly = false)
	public void delete(ScoApply scoApply) {
		super.delete(scoApply);
	}

	@Transactional(readOnly = false)
	public void saveAudit(ScoApply scoApply,String suggest) {
		ScoAuditing scoAuditing=new ScoAuditing();
		scoAuditing.setApplyId(scoApply.getId());
		scoAuditing.setType("1");
		scoAuditing.setScoreVal(String.valueOf(scoApply.getScore()));
		scoAuditing.setUser(UserUtils.getUser());
		scoAuditing.setProcInsId(scoApply.getProcInsId());
		if (StringUtil.isNotEmpty(suggest)) {
			scoAuditing.setSuggest(suggest);
		}
		scoAuditingService.save(scoAuditing);
		super.save(scoApply);
		//向前台发送消息告诉学生已经审核完成。
		User user=UserUtils.getUser();
		User rec_User=systemService.getUser(scoApply.getUserId());
		ScoCourse scoCourse = scoCourseService.get(scoApply.getCourseId());
		oaNotifyService.sendOaNotifyByType(user, rec_User, "课程学分审核", rec_User.getName()+"申请"+
				scoCourse.getName()+"课程的学分已经被审核", OaNotify.Type_Enum.TYPE32.getValue(), scoApply.getId());
		//告诉其他管理员  该申请学分已经审核完成。
		List<String> roles = new ArrayList<String>();
		Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
		if(role!=null){
			roles = userService.findListByRoleId(role.getId());
			roles.remove(user.getId());
			if(roles.size()>0){
				oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "课程学分审核", rec_User.getName()+"申请"+
						scoCourse.getName()+"课程的学分已经被其他人审核", OaNotify.Type_Enum.TYPE32.getValue(), scoApply.getId());
			}
		}

		Map<String,Object> vars=new HashMap<String,Object>();
		vars=scoApply.getVars();
		String taskId=actTaskService.getTaskidByProcInsId(scoApply.getProcInsId());
		taskService.complete(taskId, vars);
		if (scoApply.getAuditStatus().equals(ScoRstatus.SRS_PASS.getKey())) {
			saveScore(scoApply);
		}else{
			scoApply.setScore(0f);
			super.save(scoApply);
		}
	}

	@Transactional(readOnly = false)
	private void saveScore(ScoApply scoApply) {
		ScoScore scoScore =new ScoScore ();
		scoScore.setCourseId(scoApply.getCourseId());
		String userId=scoApply.getUserId();
		scoScore.setUser(userService.findUserById(userId));
		float score=scoApply.getScore();
		if (score>0&score<0.5) {
			score = 0.5f;
		}
		scoScore.setCourseScore((double)score);
		scoScoreService.save(scoScore);
	}
	@Transactional(readOnly = false)
	public JSONObject saveAuditPl(String[] idList) {
		JSONObject js=new JSONObject();
		for(int i=0;i<idList.length;i++){
			ScoApply scoApply=get(idList[i]);
			scoApply.setAuditStatus(ScoRstatus.SRS_PASS.getKey());

			//查询相应课程课时列表
			List<ScoAffirmCriterionCouse> couseNumList = scoAffirmCriterionCouseService.findListByFidCouseNum(scoApply.getCourseId());
			if (couseNumList!=null) {
				String autoScore=null;
				for(ScoAffirmCriterionCouse index:couseNumList) {
					int begin=Integer.valueOf(index.getStart());
					int end=Integer.valueOf(index.getEnd());
					if ((scoApply.getRealTime()<end || scoApply.getRealTime()== end) && (scoApply.getRealTime()>begin||scoApply.getRealTime()==begin)) {
						List<ScoAffirmCriterionCouse> couseScoreList = scoAffirmCriterionCouseService.findListByParentId(index.getId());
						if (couseScoreList!=null) {
							for(ScoAffirmCriterionCouse indexScore:couseScoreList) {
								int beginScore=Integer.valueOf(indexScore.getStart());
								int endScore=Integer.valueOf(indexScore.getEnd());
								if ((scoApply.getRealScore()<endScore || scoApply.getRealScore()== endScore) &&
										(scoApply.getRealScore()>beginScore|| scoApply.getRealScore()== beginScore)) {
									autoScore=indexScore.getScore();
								}
								if (StringUtil.isNotEmpty(autoScore)) {
									break;
								}
							}
						}
					}
					if (StringUtil.isNotEmpty(autoScore)) {
						break;
					}
				}
				if(autoScore!=null){
					scoApply.setScore(Float.valueOf(autoScore));
				}else{
					scoApply.setScore(0);
				}
			}
			try {
				saveAudit(scoApply,"");
			}catch (Exception e){
				logger.error(e.toString());
				js.put(CoreJkey.JK_RET,0);
				js.put(CoreJkey.JK_MSG,"审核失败");
				return js;
			}
		}
		js.put(CoreJkey.JK_RET,1);
		return js;
	}
}