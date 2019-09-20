package com.oseasy.sys.modules.sys.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyRecordDao;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.dao.StudentExpansionDao;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.dao.TeamUserRelationDao;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 学生信息表Service
 * @author zy
 * @version 2017-03-27
 */
@Service
@Transactional(readOnly = true)
public class StudentExpansionService extends CrudService<StudentExpansionDao, StudentExpansion> {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserService userService;
	@Autowired
	private OaNotifyRecordDao recordDao;
	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	private TeamUserRelationDao relationDao;
	@Autowired
	private StudentExpansionDao studentExpansionDao;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private TeamUserRelationService teamUserRelationService;
	@Autowired
	private TeamUserHistoryDao teamUserHistoryDao;
	@Autowired
	private CoreService coreService;
	@Transactional(readOnly = false)
	public void  updateCurrState() {
		String graduateMonth=null;
		SysConfigVo scv=SysConfigUtil.getSysConfigVo();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(scv!=null&&StringUtil.isNotEmpty(scv.getGraduateMonth())){
			try {
				sdf.parse("2018-"+scv.getGraduateMonth()+"-01");//校验用
				graduateMonth=scv.getGraduateMonth();
			} catch (ParseException e) {
				logger.error("毕业月份参数错误");
			}
		}
		if(graduateMonth!=null){
			dao.updateCurrStateByConfig(graduateMonth);
		}else{
			dao.updateCurrState();
		}
	}
	@Transactional(readOnly = false)
	public JSONObject toInvite(String userIds,String userType,String teamId) {
		JSONObject js=new JSONObject();
		List<String> list=new ArrayList<String>();
		js.put("ret", 0);
		js.put("msg", "邀请失败");
		int count=0;
		if (StringUtil.isEmpty(userIds)||StringUtil.isEmpty(userType)||StringUtil.isEmpty(teamId)) {
			js.put("msg", "参数错误");
			return js;
		}
		for(String tid:teamId.split(",")) {
			JSONObject tem = teamUserRelationService.frontToInvite(null, userIds, userType, tid);
			Boolean isSuccess = tem.getBoolean("success");
			String res = tem.getString(CoreJkey.JK_RET);
			if((isSuccess == null) || (StringUtil.isEmpty(res) || !StringUtil.isNumeric(res))){
			    continue;
			}

			if (isSuccess && (Integer.parseInt(res) != 0)) {
				count++;
				list.add(tid);
			}
		}
		if (count==0) {
			js.put("msg", "邀请失败(人员已满或者该团队不在建设中或者检查该人员是否已在团队)");
			return js;
		}
		if (count==teamId.split(",").length) {
			js.put("msg", "邀请成功");
			js.put("teamIds",  org.apache.commons.lang3.StringUtils.join(list, ","));
			js.put("ret", 1);
			return js;
		}
		if (count<=teamId.split(",").length) {
			js.put("msg", "成功为"+count+"个团队发出邀请(人员已满或者该团队不在建设中或者检查该人员是否已在团队)");
			js.put("teamIds",  org.apache.commons.lang3.StringUtils.join(list, ","));
			js.put("ret", 1);
			return js;
		}
		return js;
	}
	public StudentExpansion get(String id) {
		return super.get(id);
	}

	public List<StudentExpansion> findList(StudentExpansion studentExpansion) {
		return super.findList(studentExpansion);
	}

	public Page<StudentExpansion> findPage(Page<StudentExpansion> page, StudentExpansion studentExpansion) {
		studentExpansion.setTenantId(TenantConfig.getCacheTenant().toString());
		Page<StudentExpansion> rpage=super.findPage(page, studentExpansion);
		List<StudentExpansion> list=rpage.getList();
		if(list!=null&&list.size()>0){
			List<User> us=teamUserHistoryDao.getUserCurJoinByStus(list);
			Map<String,String> map=new HashMap<String,String>();
			if(us!=null&&us.size()>0){
				for(User u:us){
					map.put(u.getId(), u.getCurJoin());
				}
			}
			Map<String,String> tmap=new HashMap<String,String>();
			if(StringUtil.isNotEmpty(studentExpansion.getTeamLeaderId())){
				List<StudentExpansion> ts=teamUserHistoryDao.getCanInviteTeamIdsByStus(list, studentExpansion.getTeamLeaderId());
				if(ts!=null&&ts.size()>0){
					for(StudentExpansion u:ts){
						tmap.put(u.getUser().getId(), u.getCanInviteTeamIds());
					}
				}
			}
			for(StudentExpansion s:list){
				s.setCurJoin(map.get(s.getUser().getId()));
				s.setCanInviteTeamIds(tmap.get(s.getUser().getId()));
			}
		}
		return rpage;
	}

	@Transactional(readOnly = false)
	public void save(StudentExpansion studentExpansion) {
		super.save(studentExpansion);
	}

	@Transactional(readOnly = false)
	public void delete(StudentExpansion studentExpansion) {
		super.delete(studentExpansion);
	}

	@Transactional(readOnly = false)
	public void saveAll(StudentExpansion studentExpansion) {
		String id = IdGen.uuid();
		studentExpansion.getUser().setId(id);
		studentExpansion.getUser().setCreateBy(UserUtils.getUser());
		Date now = new Date();
		studentExpansion.getUser().setCreateDate(now);
		studentExpansion.getUser().setUpdateBy(UserUtils.getUser());
		studentExpansion.getUser().setUpdateDate(now);
		List<Role> roleList=new ArrayList<Role>();
		//Role role = new Role();
		//role.setRtype(CoreSval.Rtype.STUDENT.getKey());
		roleList.add(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()));
		studentExpansion.getUser().setRoleList(roleList);
		studentExpansion.getUser().setDelFlag("0");
		if(StringUtil.isEmpty(studentExpansion.getUser().getLoginName())){
			String loginName = studentExpansion.getUser().getMobile();
			studentExpansion.getUser().setLoginName(loginName);
		}
		//String password = studentExpansion.getUser().getPassword();
		String password = CoreUtils.USER_PSW_DEFAULT;
		password = CoreUtils.entryptPassword(password);
		studentExpansion.getUser().setPassword(password);
		String companyId;
		if(studentExpansion.getUser().getOffice() != null){
			 companyId = officeService.selelctParentId(studentExpansion.getUser().getOffice().getId());
		}else {
			companyId = officeService.selelctParentId(studentExpansion.getUser().getOfficeId());
		}

		studentExpansion.getUser().setCompany(new Office());
		studentExpansion.getUser().getCompany().setId(companyId);//设置学校id
		studentExpansion.getUser().setUserType("1");
		try {
			logger.info("保存到学生信息表");
			save(studentExpansion);
			logger.info("保存到用户表");
			studentExpansion.getUser().setOffice(officeService.get(studentExpansion.getUser().getOfficeId()));
			userService.saveUser(studentExpansion.getUser());
			userService.insertUserRole(studentExpansion.getUser());
			logger.info("保存完成");
		} catch (Exception e) {
			logger.error("操作异常");
			logger.error(e.getMessage(),e);
		}
	}
	@Transactional(readOnly = false)
	public void updateAll(StudentExpansion studentExpansion) {
		try {
			save(studentExpansion);
			userService.updateUser(studentExpansion.getUser());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	@Transactional(readOnly = false)
	public void invite(StudentExpansion studentExpansion) {
		try {
			OaNotify oaNotify = new OaNotify();
			oaNotify.setTitle("邀请信息");
			oaNotify.setCreateBy(UserUtils.getUser());
			oaNotify.setCreateDate(new Date());
			oaNotify.setUpdateBy(UserUtils.getUser());
			oaNotify.setUpdateDate(new Date());
			oaNotify.setDelFlag("0");
			oaNotifyService.save(oaNotify);
			OaNotifyRecord notifyRecord = new OaNotifyRecord();
			notifyRecord.setCreateBy(UserUtils.getUser());
			String rid = UUID.randomUUID().toString().replaceAll("-", "");
			notifyRecord.setId(rid);
			notifyRecord.setUser(studentExpansion.getUser());
			notifyRecord.setOaNotify(oaNotify);
			notifyRecord.setReadFlag("0");
			notifyRecord.setDelFlag("0");
			recordDao.insert(notifyRecord);
			TeamUserRelation teamUserRelation = new TeamUserRelation();
			if (teamUserRelation != null) {
				teamUserRelation.setState("2");
				teamUserRelation.setUpdateBy(UserUtils.getUser());
				teamUserRelation.setUpdateDate(new Date());
				teamUserRelation.setUser(studentExpansion.getUser());
				teamUserRelation.setTeamId(studentExpansion.getTeam().getId());
				relationDao.update(teamUserRelation);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	public StudentExpansion getByUserId(String userId) {
		return studentExpansionDao.getByUserId(userId);
	}

	public List<StudentExpansion> getStudentByTeamId(String teamId) {
		return studentExpansionDao.getStudentByTeamId(teamId);
	}
}