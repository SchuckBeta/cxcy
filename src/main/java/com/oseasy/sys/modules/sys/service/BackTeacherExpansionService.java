package com.oseasy.sys.modules.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyDao;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyRecordDao;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.RegexUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.dao.TeacherKeywordDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.TeacherKeyword;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.dao.TeamUserRelationDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 导师信息表Service
 * @author chenhao
 * @version 2017-03-31
 */
@Service
@Transactional(readOnly = true)
public class BackTeacherExpansionService extends CrudService<BackTeacherExpansionDao, BackTeacherExpansion> {
	@Autowired
	private UserService userService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private TeamUserRelationDao relationDao;
	@Autowired
	private OaNotifyRecordDao notifyRecordDao;
	@Autowired
	private OaNotifyDao notifyDao;
	@Autowired
	private TeacherKeywordDao teacherKeywordDao;
    @Autowired
    public TeacherKeywordService teacherKeywordService;
	@Autowired
	private TeamUserRelationService teamUserRelationService;
	@Autowired
	private TeamUserHistoryService teamUserHistoryService;
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private TeamUserHistoryDao teamUserHistoryDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private CoreService coreService;

//	@Autowired
//	private ActTaskService actTaskService;

	@Transactional(readOnly = false)
	public JSONObject addTeacherByStu(String name,String no,String mobile,String type,String office,String profes) {
		if (name!=null) {
			name=name.trim();
		}
		if (no!=null) {
			no=no.trim();
		}
		if (mobile!=null) {
			mobile=mobile.trim();
		}
		if (type!=null) {
			type=type.trim();
		}
		JSONObject js=new JSONObject();
		js.put("ret", 0);
		if (StringUtil.isEmpty(name)) {
			js.put("msg", "请填写导师名称");
			return js;
		}
		if (StringUtil.isEmpty(type)) {
			js.put("msg", "请选择导师来源");
			return js;
		}
		if (StringUtil.isEmpty(mobile)) {
			js.put("msg", "请填写手机号");
			return js;
		}
		if ("1".equals(type)&&StringUtil.isEmpty(name)) {
			js.put("msg", "请填写导师工号");
			return js;
		}
		if (!Pattern.matches(RegexUtils.mobileRegex, mobile)) {
			js.put("msg", "手机号格式不正确");
			return js;
		}
		if (StringUtil.isNotEmpty(mobile)&&UserUtils.isExistMobile(mobile)) {
			js.put("msg", "手机号已存在");
			return js;
		}
		if (StringUtil.isNotEmpty(no)&&UserUtils.isExistNo(no)) {
			js.put("msg", "工号已存在");
			return js;
		}
		if (StringUtil.isEmpty(no)) {
			no=getAutoNo();
		}
		BackTeacherExpansion tc=new BackTeacherExpansion();
		tc.setTeachertype(type);
		User nuser=new User();
		nuser.setName(name);
		nuser.setNo(no);
		nuser.setMobile(mobile);
		if (StringUtil.isNotEmpty(office)) {
			nuser.setOffice(new Office(office));
		}
		if (StringUtil.isNotEmpty(profes)) {
			nuser.setProfessional(profes);
		}
		User user = UserUtils.getUser();
		nuser.setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
		List<Role> roleList=new ArrayList<Role>();
		roleList.add(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()));
		nuser.setRoleList(roleList);
		nuser.setId(IdGen.uuid());
		if (StringUtils.isNotBlank(user.getId())) {
			nuser.setUpdateBy(user);
			nuser.setCreateBy(user);
		}
		nuser.setUpdateDate(new Date());
		nuser.setCreateDate(nuser.getUpdateDate());
		systemService.insert(nuser);
		userService.insertUserRole(nuser);
		tc.setUser(nuser);
		tc.setId(IdGen.uuid());
		if (StringUtils.isNotBlank(user.getId())) {
			tc.setUpdateBy(user);
			tc.setCreateBy(user);
		}
		tc.setUpdateDate(new Date());
		tc.setCreateDate(tc.getUpdateDate());
		dao.insert(tc);
		js.put("ret", 1);
		js.put("msg", "添加成功");
		return js;
	}
	private String getAutoNo() {
		String no=IdUtils.getDictNumberByDb();
		if (UserUtils.isExistNo(no)) {
			return getAutoNo();
		}else{
			return no;
		}
	}
	public BackTeacherExpansion getByUserId(String uid) {
		return dao.getByUserId(uid);
	}

	public BackTeacherExpansion get(String id) {
		return super.get(id);
	}

	public List<BackTeacherExpansion> findList(BackTeacherExpansion backTeacherExpansion) {
		return super.findList(backTeacherExpansion);
	}

    public List<BackTeacherExpansion> findIndexTeacherList(BackTeacherExpansion backTeacherExpansion){
		backTeacherExpansion.setTenantId(TenantConfig.getCacheTenant());
	    return dao.findIndexTeacherList(backTeacherExpansion);
    }

	public List<BackTeacherExpansion> findTeacherList(BackTeacherExpansion backTeacherExpansion) {
			return dao.findTeacherList(backTeacherExpansion);
	}
	public List<BackTeacherExpansion> findProvinceTeacherList(BackTeacherExpansion backTeacherExpansion) {
		return dao.findProvinceTeacherList(backTeacherExpansion);
	}

	public List<String> getKeys(String tid){
		return dao.getKeys(tid);
	}
	public Page<BackTeacherExpansion> findPage(Page<BackTeacherExpansion> page,
			BackTeacherExpansion backTeacherExpansion) {
		Page<BackTeacherExpansion> rpage=super.findPage(page, backTeacherExpansion);
		List<BackTeacherExpansion> list=rpage.getList();
		if(list!=null&&list.size()>0){
			List<User> us=teamUserHistoryDao.getUserCurJoinByTeas(list);
			Map<String,String> umap=new HashMap<String,String>();
			if(us!=null&&us.size()>0){
				for(User u:us){
					umap.put(u.getId(), u.getCurJoin());
				}
			}
			List<BackTeacherExpansion>  bs=dao.getServiceIntention(list);
			Map<String,String> bmap=new HashMap<String,String>();
			if(bs!=null&&bs.size()>0){
				for(BackTeacherExpansion u:bs){
					bmap.put(u.getUser().getId(), u.getServiceIntentionStr());
				}
			}
			Map<String,String> tmap=new HashMap<String,String>();
			if(StringUtil.isNotEmpty(backTeacherExpansion.getTeamLeaderId())){
				List<BackTeacherExpansion> ts=teamUserHistoryDao.getCanInviteTeamIdsByTeas(list, backTeacherExpansion.getTeamLeaderId());
				if(ts!=null&&ts.size()>0){
					for(BackTeacherExpansion u:ts){
						tmap.put(u.getUser().getId(), u.getCanInviteTeamIds());
					}
				}
			}
			for(BackTeacherExpansion s:list){
				List<Role> roleList = coreService.findListByUserId(s.getUser().getId());
				List<String> roleIdList= Lists.newArrayList();
				List<Role> expertRoleList = Lists.newArrayList();
				if(StringUtil.checkNotEmpty(roleList)){
					for(Role role:roleList){
						if(role.getId().equals(SysIds.EXPERT_COLLEGE_EXPERT.getId())
								||role.getId().equals(SysIds.EXPERT_SCHOOL_EXPERT.getId())
								||role.getId().equals(SysIds.EXPERT_OUTSCHOOL_EXPERT.getId())
								){
							expertRoleList.add(role);
							roleIdList.add(role.getId());
						}
					}
					s.getUser().setRoleList(expertRoleList);
					s.setRoleIdList(roleIdList);
				}

				s.setCurJoin(umap.get(s.getUser().getId()));
				s.setServiceIntentionStr(bmap.get(s.getUser().getId()));
				s.setCanInviteTeamIds(tmap.get(s.getUser().getId()));
			}
		}
		return rpage;
	}

	@Transactional(readOnly = false)
	public void save(BackTeacherExpansion backTeacherExpansion) {
		super.save(backTeacherExpansion);
		if (StringUtil.isNotEmpty(backTeacherExpansion.getId())) {
			resetAllTeamState(backTeacherExpansion.getUser().getId());
		}
	}

	@Transactional(readOnly = false)
	public void delete(BackTeacherExpansion backTeacherExpansion) {
		super.delete(backTeacherExpansion);
	}
	//根据用户id重置所有加入的团队状态
	@Transactional(readOnly = false)
	public void resetAllTeamState(String userid) {
		List<Team> list=teamDao.findTeamListByUserId(userid);
		if (list!=null&&list.size()>0) {
			for(Team team:list) {
				teamUserRelationService.repTeamstate(team);
			}
		}
	}
	@Transactional(readOnly = false)
	public void updateAll(BackTeacherExpansion backTeacherExpansion) {
		try {
			save(backTeacherExpansion);
			User user = backTeacherExpansion.getUser();
			if(StringUtil.isNotEmpty(user.getOfficeId())){
				user.setOffice(new Office(user.getOfficeId()));
			}
			userService.updateUser(user);
			//UserUtils.clearCache();
			logger.info("保存老师信息");
		} catch (Exception e) {
			logger.info(e.toString());
			logger.info("异常信息");
		}
	}

	@Transactional(readOnly = false)
	public void updateExpertAll(BackTeacherExpansion backTeacherExpansion) {
		try {
			save(backTeacherExpansion);
			User user = backTeacherExpansion.getUser();
			if(StringUtil.isNotEmpty(user.getOfficeId())){
				user.setOffice(new Office(user.getOfficeId()));
			}
			userService.updateUser(user);
//			userDao.deleteUserRole(user);
//			userService.deleteByUserRole(user.getId(),SysIds.EXPERT_COLLEGE_EXPERT.getId());
//			userService.deleteByUserRole(user.getId(),SysIds.EXPERT_SCHOOL_EXPERT.getId());
//			userService.deleteByUserRole(user.getId(),SysIds.EXPERT_OUTSCHOOL_EXPERT.getId());
//			List<String> roleIdList=backTeacherExpansion.getRoleIdList();
//			if(StringUtil.checkNotEmpty(roleIdList)){
//				List<Role> roleList=new ArrayList<Role>();
//				for(String roleId:roleIdList){
//					roleList.add(roleDao.get(roleId));
//				}
//				user.setRoleList(roleList);
//				userService.insertUserRole(user);
//				CoreUtils.clearCache(user);
//			}
			UserUtils.clearCache();
			logger.info("保存专家信息");
		} catch (Exception e) {
			logger.info(e.toString());
			logger.info("异常信息");
		}
	}

	@Transactional(readOnly = false)
	public void saveAll(BackTeacherExpansion backTeacherExpansion) {
		try {
			String uid = IdGen.uuid();
			backTeacherExpansion.getUser().setId(uid);
			backTeacherExpansion.setDelFlag("0");
			save(backTeacherExpansion);
			User user = backTeacherExpansion.getUser();
			if(StringUtil.isNotEmpty(user.getOfficeId())){
				user.setOffice(new Office(user.getOfficeId()));
			}
			//默认密码
			String password = CoreUtils.USER_PSW_DEFAULT;
			password = CoreUtils.entryptPassword(password);
			user.setPassword(password);
			if(StringUtil.isEmpty(user.getLoginName())){
	            user.setLoginName(user.getMobile());
			}
			user.setCreateBy(UserUtils.getUser());
			Date now = new Date();
			user.setUserType("2");
			user.setCreateDate(now);
			user.setDelFlag("0");
			List<Role> roleList=new ArrayList<Role>();
			Role role=new Role();
			role.setRtype(CoreSval.Rtype.TEACHER.getKey());
			roleList.add(roleDao.getByRtype(role));
			user.setRoleList(roleList);
			user.setUpdateBy(UserUtils.getUser());
			user.setUpdateDate(now);
			systemService.insert(user);
			userService.insertUserRole(user);
			logger.info("保存成功");
		} catch (Exception e) {
			logger.info("保存失败");
			logger.error(e.getMessage(),e);
		}
	}

	@Transactional(readOnly = false)
	public void saveExpertAll(BackTeacherExpansion backTeacherExpansion) {
		try {
			String uid = IdGen.uuid();
			backTeacherExpansion.getUser().setId(uid);
			backTeacherExpansion.setDelFlag("0");
			save(backTeacherExpansion);
			User user = backTeacherExpansion.getUser();
			if(StringUtil.isNotEmpty(user.getOfficeId())){
				user.setOffice(new Office(user.getOfficeId()));
			}
			//默认密码
			String password = CoreUtils.USER_PSW_DEFAULT;
			password = CoreUtils.entryptPassword(password);
			user.setPassword(password);
			if(StringUtil.isEmpty(user.getLoginName())){
				user.setLoginName(user.getMobile());
			}
			user.setCreateBy(UserUtils.getUser());
			Date now = new Date();
			user.setUserType("2");
			user.setCreateDate(now);
			user.setDelFlag("0");

			if(StringUtil.checkNotEmpty(backTeacherExpansion.getExpertTypeList())){

 			}
				List<Role> roleList=new ArrayList<Role>();
//				for(String roleId:roleIdList){
				Role role=new Role();
				role.setRtype(CoreSval.Rtype.EXPORT.getKey());
				roleList.add(roleDao.getByRtype(role));
//				}
				user.setRoleList(roleList);
//			}

			user.setUpdateBy(UserUtils.getUser());
			user.setUpdateDate(now);
			userService.insert(user);
			if(StringUtil.checkNotEmpty(user.getRoleList())) {
				userService.insertUserRole(user);
				CoreUtils.clearCache(user);
			}
			logger.info("保存成功");
		} catch (Exception e) {
			logger.info("保存失败");
			logger.error(e.getMessage(),e);
		}
	}

	public List<Office> findOffice() {
		return officeService.findAll();
	}

	@Transactional(readOnly = false)
	//邀请导师加入团队
	public void ivite(BackTeacherExpansion backTeacherExpansion) {
		try {

			//String oid = UUID.randomUUID().toString().replaceAll("-", "");
			String oid = IdGen.uuid();
			OaNotify oaNotify = backTeacherExpansion.getOaNotify();
			oaNotify.setId(oid);
			oaNotify.setTitle("邀请通知");
			oaNotify.setCreateBy(UserUtils.getUser());
			oaNotify.setCreateDate(new Date());
			oaNotify.setUpdateBy(UserUtils.getUser());
			oaNotify.setUpdateDate(new Date());
			oaNotify.setDelFlag("0");
			notifyDao.insert(oaNotify);
			//String rid = UUID.randomUUID().toString().replaceAll("-", "");
			String rid = IdGen.uuid();
			OaNotifyRecord notifyRecord = new OaNotifyRecord();
			notifyRecord.setId(rid);
			notifyRecord.setUser(backTeacherExpansion.getUser());
			notifyRecord.setOaNotify(oaNotify);
			//readFlag：阅读标记0：未读
			notifyRecord.setReadFlag("0");
			notifyRecord.setDelFlag("0");
			notifyRecordDao.insert(notifyRecord);
			TeamUserRelation teamUserRelation = backTeacherExpansion.getTeamUserRelation();
			if (teamUserRelation != null) {
				//state：2 负责人发送邀请
				teamUserRelation.setState("2");
				teamUserRelation.setUpdateBy(UserUtils.getUser());
				teamUserRelation.setUpdateDate(new Date());
				teamUserRelation.setUser(backTeacherExpansion.getUser());
				teamUserRelation.setTeamId(backTeacherExpansion.getTeam().getId());
				relationDao.update(teamUserRelation);
			} else {
				String tid = IdGen.uuid();;
				TeamUserRelation relation = new TeamUserRelation();
				relation.setId(tid);
				relation.setCreateBy(UserUtils.getUser());
				relation.setCreateDate(new Date());
				relation.setDelFlag("0");
				//state：2 负责人发送邀请
				relation.setState("2");
				// usertype：2 导师来源
				relation.setUserType("2");
				relation.setTeamId(backTeacherExpansion.getTeam().getId());
				relation.setUser(backTeacherExpansion.getUser());
				relation.setUpdateBy(UserUtils.getUser());
				relation.setUpdateDate(new Date());
				relationDao.insert(relation);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

	}

	public List<BackTeacherExpansion> findTeacherAward(String userId) {
		return dao.findTeacherAward(userId);
	}

	public  BackTeacherExpansion findTeacherByUserId(String userId) {
		return dao.findTeacherByUserId(userId);
	}
	@Transactional(readOnly = false)
	public void updateKeyword(BackTeacherExpansion backTeacherExpansion) {
		if (StringUtil.isNotEmpty(backTeacherExpansion.getId())) {
			teacherKeywordDao.delByTeacherid(backTeacherExpansion.getId());
		}
		if (backTeacherExpansion.getKeywords()!=null) {
			for(String ek:backTeacherExpansion.getKeywords()) {
				TeacherKeyword tk=new TeacherKeyword();
				tk.setKeyword(ek);
				tk.setTeacherId(backTeacherExpansion.getId());
				tk.preInsert();
				teacherKeywordDao.insert(tk);
			}
		}
	}

	public BackTeacherExpansion findTeacherByTopShow(String teacherType) {
		return dao.findTeacherByTopShow(teacherType);
	}

	public List<BackTeacherExpansion> getQYTeacher(String teamId) {
		return dao.getQYTeacher(teamId);
	}

	public List<BackTeacherExpansion> getXYTeacher(String teamId) {
		return dao.getXYTeacher(teamId);
	}
	@FindListByTenant
	public List<BackTeacherExpansion> findExpertList(BackTeacherExpansion backTeacherExpansion){ return  dao.findExpertList(backTeacherExpansion);}

	@Transactional(readOnly = false)
	public void deleteByUserId(String userId) {
		dao.deleteByUserId(userId);
		CoreUtils.clearCache(new User(userId));
	}

	@Transactional(readOnly = false)
	public void updateExpertChange(BackTeacherExpansion backTeacherExpansion) {
		User user=backTeacherExpansion.getUser();
		List<String> rtypes = new ArrayList<>();
		rtypes.add(CoreSval.Rtype.EXPORT.getKey());
		rtypes.add(CoreSval.Rtype.TEACHER.getKey());
//		Role role = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());

		//userService.deleteUserRole(user);
		userService.deleteUserRole(rtypes,user.getId(),TenantConfig.getCacheTenant());
		List<Role> roleList = new ArrayList<Role>();
		//判断身份含导师角色
		if(backTeacherExpansion.getRoleType().contains(BackTeacherExpansion.isTeacher) ){
			roleList.add(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()));
			BackTeacherExpansion backTeacherExpansionindex=getByUserId(user.getId());
			if(backTeacherExpansionindex==null){
				save(backTeacherExpansion);
			}else{
				dao.updateType(backTeacherExpansion);
			}
		}else{//没有导师身份删除teacherType值
			dao.updateType(backTeacherExpansion);
		}
		//判断身份含专家角色
		if(backTeacherExpansion.getRoleType().contains(BackTeacherExpansion.isExpert)){
			roleList.add(coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey()));
			// 导师来源 1：校园导师  2：企业导师
			//专家来源 1：校内专家  2：校外专家
			BackTeacherExpansion backTeacherExpansionindex=getByUserId(user.getId());
			if(backTeacherExpansionindex==null){
				save(backTeacherExpansion);
			}
		}
		dao.updateExpertType(backTeacherExpansion);
		if(StringUtil.checkNotEmpty(roleList)){
			user.setRoleList(roleList);
			userService.insertUserRole(user);
		}
		dao.updateExpertChange(backTeacherExpansion);
		//转换后清空缓存信息
		CoreUtils.clearCache(user);
	}

	public List<String> findAllExpertList() {
		return dao.findAllExpertList();
	}
	public List<String> findAllExpertListById(String id) {
		return dao.findAllExpertListById(id);
	}

	public List<String> findCollegeExpertListByPro(String proId) {
		return dao.findCollegeExpertListByPro(proId);
	}

	public void deleteByType(User user, String type) {
		//删除角色
		List<Role> oldRoleList = coreService.findListByUserId(user.getId());
		List<Role> newList =new ArrayList<Role>();
		for(Role role:oldRoleList){
			if(BackTeacherExpansion.isExpert.equals(type)){
				if(SysIds.EXPERT_COLLEGE_EXPERT.getId().equals(role.getId())
					||SysIds.EXPERT_SCHOOL_EXPERT.getId().equals(role.getId())
					||SysIds.EXPERT_OUTSCHOOL_EXPERT.getId().equals(role.getId())
					||CoreSval.Rtype.EXPORT.getKey().equals(role.getRtype())
						){
					newList.add(role);
				}
			}else if(BackTeacherExpansion.isTeacher.equals(type)){
				if(CoreSval.Rtype.TEACHER.getKey().equals(role.getRtype())){
					newList.add(role);
				}
			}
		}
		oldRoleList.removeAll(newList);
		//如果没有角色 删除这个用户
		if(StringUtil.checkEmpty(oldRoleList)){
			coreService.deleteUser(user); // 删除用户
			deleteByUserId(user.getId()); // 删除导师专家库
		}else{
			BackTeacherExpansion backTeacherExpansionindex=getByUserId(user.getId());
			//删除了角色 修改相应导师专家库的roleType值
			if(backTeacherExpansionindex!=null){
				if(BackTeacherExpansion.isExpert.equals(type)){
					backTeacherExpansionindex.setRoleType(BackTeacherExpansion.isTeacher);
					save(backTeacherExpansionindex);
				}else if(BackTeacherExpansion.isTeacher.equals(type)){
					backTeacherExpansionindex.setRoleType(BackTeacherExpansion.isExpert);
					save(backTeacherExpansionindex);
				}
			}
			userService.deleteUserRole(user);
			user.setRoleList(oldRoleList);
			userService.insertUserRole(user);
			//转换后清空缓存信息
			CoreUtils.clearCache(user);
		}

	}

	public List<String> findExpertListByType(String auditRole) {
		return dao.findExpertListByType(auditRole);
	}
}