/**
 *
 */
package com.oseasy.sys.modules.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.sys.common.config.SysSval;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.ServiceException;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.dao.StudentExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.enums.EuserType;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.sys.modules.sys.vo.Utype;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 */
@Service
@Transactional(readOnly = true)
public class SysSystemService extends SystemService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private CoreService coreService;
    @Autowired
    private StudentExpansionDao studentExpansionDao;
    @Autowired
    private BackTeacherExpansionDao backTeacherExpansionDao;
    @Autowired
    private TeamUserHistoryDao teamUserHistoryDao;
    @Autowired
    private BackTeacherExpansionService backTeacherExpansionService;

    /**
     * 查询学生.
     * @param page 分页
     * @param user 用户
     * @return Page
     */
    public Page<User> findListTreeByStudent(Page<User> page, User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        //user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        List<User> list = userDao.findListTreeByStudentNoDomain(user);
        if(list!=null&&list.size()>0){
            List<User> us=teamUserHistoryDao.getUserCurJoinByUsers(list);
            Map<String,String> map=new HashMap<String,String>();
            if(us!=null&&us.size()>0){
                for(User u:us){
                    map.put(u.getId(), u.getCurJoin());
                }
            }
            for(User s:list){
                s.setCurJoin(map.get(s.getId()));
                List<Dict> dicts = DictUtils.getDictListByType(SysSval.DICT_TECHNOLOGY_FIELD);

                //处理技术领域
                if(StringUtil.isEmpty(s.getDomain())){
                    continue;
                }

                if(s.getDomainIdList() == null){
                    s.setDomainIdList(Lists.newArrayList());
                }

                for (Dict dict : dicts) {
                    if(s.getDomain().contains(dict.getValue())){
                        s.getDomainIdList().add(dict.getLabel());
                    }
                }
            }
        }
        page.setList(list);
        return page;
    }

    /**
     * 查询导师.
     *
     * @param page 分页
     * @param user 用户
     * @return Page
     */
    public Page<User> findListTreeByTeacher(Page<User> page, User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        //user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        List<User> list =userDao.findListTreeByTeacher(user);
        if(list!=null&&list.size()>0){
            List<User> us=teamUserHistoryDao.getUserCurJoinByUsers(list);
            Map<String,String> map=new HashMap<String,String>();
            if(us!=null&&us.size()>0){
                for(User u:us){
                    map.put(u.getId(), u.getCurJoin());
                }
            }
            for(User s:list){
                s.setCurJoin(map.get(s.getId()));
            }
        }
        page.setList(list);
        return page;
    }

    /**
     * 查询学生.
     *
     * @param page 分页
     * @param user 用户
     * @return Page
     */
    public Page<User> findListTreeByUser(Page<User> page, User user) {
        // 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
        //user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
        // 设置分页参数
        user.setPage(page);
        // 执行分页查询
        List<User> list =userDao.findListTreeByUser(user);
        if(list!=null&&list.size()>0){
            List<User> us=teamUserHistoryDao.getUserCurJoinByUsers(list);
            Map<String,String> map=new HashMap<String,String>();
            if(us!=null&&us.size()>0){
                for(User u:us){
                    map.put(u.getId(), u.getCurJoin());
                }
            }
            for(User s:list){
                s.setCurJoin(map.get(s.getId()));
            }
        }
        page.setList(list);
        return page;
    }

    @Transactional(readOnly = false)
    public Boolean outUserInRole(Role role, User user) {
        List<Role> roles = user.getRoleList();
        for (Role e : roles) {
            if (e.getId().equals(role.getId())) {
                roles.remove(e);
                saveUser(user);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    public User assignUserToRole(Role role, User user) {
        if (user == null) {
            return null;
        }
        List<String> roleIds = user.getRoleIdList();
        if (roleIds.contains(role.getId())) {
            return null;
        }
        user.getRoleList().add(role);
        saveUser(user);
        return user;
    }

    /**
     * 修改学生.
     * @param st 显示
     * @param nuser 用户参数
     * @param ouser 操作用户
     * @return User
     */
    @Transactional(readOnly = false)
    public User updateStudentByLoginName(StudentExpansion st, User nuser, User ouser) {
        if(nuser != null){
            if(StringUtil.isNotEmpty(nuser.getName())){
                ouser.setName(nuser.getName());
            }
            if(StringUtil.isNotEmpty(nuser.getMobile())){
                ouser.setMobile(nuser.getMobile());
            }
            if(StringUtil.isNotEmpty(nuser.getEmail())){
                ouser.setEmail(nuser.getEmail());
            }
            if(nuser.getBirthday() != null){
                ouser.setBirthday(nuser.getBirthday());
            }
            if(StringUtil.isNotEmpty(nuser.getSex())){
                ouser.setSex(nuser.getSex());
            }
            if(StringUtil.isNotEmpty(nuser.getDegree())){
                ouser.setDegree(nuser.getDegree());
            }
            if(StringUtil.isNotEmpty(nuser.getEducation())){
                ouser.setEducation(nuser.getEducation());
            }
            if(nuser.getOffice() != null){
                ouser.setOffice(nuser.getOffice());
            }
            if(StringUtil.isNotEmpty(nuser.getProfessional())){
                ouser.setProfessional(nuser.getProfessional());
            }
        }

        ouser = User.dealOffice(ouser);
        userDao.update(ouser);
        CoreUtils.clearCache(ouser);
        StudentExpansion ost = studentExpansionDao.getByUserId(ouser.getId());
        if(ost != null){
            if(StringUtil.isNotEmpty(st.getTClass())){
                ost.setTClass(st.getTClass());
            }
            if(st.getTemporaryDate() != null){
                ost.setTemporaryDate(st.getTemporaryDate());
            }
            if(st.getGraduation() != null){
                ost.setGraduation(st.getGraduation());
            }
            if(StringUtil.isNotEmpty(st.getInstudy())){
                ost.setInstudy(st.getInstudy());
            }
            if(StringUtil.isNotEmpty(st.getCurrState())){
                ost.setCurrState(st.getCurrState());
            }
            studentExpansionDao.update(ost);
        }else{
            if(StringUtil.isEmpty(st.getId())){
                st.setId(IdGen.uuid());
            }
            st.setUpdateDate(new Date());
            st.setCreateDate(st.getUpdateDate());
            st.setUser(ouser);
            studentExpansionDao.insert(st);
        }
        return ouser;
    }

    /**
     * 创建学生.
     * @param nuser 用户参数
     * @param user 操作用户
     * @return User
     */
    @Transactional(readOnly = false)
    public User newStudentByLoginName(User nuser, User user) {
        return newStudentByLoginName(new StudentExpansion(), nuser, user);
    }

    @Transactional(readOnly = false)
    public User newStudentByLoginName(StudentExpansion st, User nuser, User user) {
        nuser.setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
        List<Role> roleList=new ArrayList<Role>();
        roleList.add(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()));
        nuser.setRoleList(roleList);
        nuser.setId(IdGen.uuid());
        nuser.setSource("1");
        nuser.setPassc("1");
        if (StringUtils.isNotBlank(user.getId())) {
            nuser.setUpdateBy(user);
            nuser.setCreateBy(user);
        }
        nuser.setUpdateDate(new Date());
        nuser.setCreateDate(nuser.getUpdateDate());
        nuser.setUserType(RoleBizTypeEnum.XS.getValue());
        insert(nuser);
        userDao.insertUserRole(nuser);
        CoreUtils.clearCache(nuser);

        st.setId(IdGen.uuid());
        if (StringUtils.isNotBlank(user.getId())) {
            st.setUpdateBy(user);
            st.setCreateBy(user);
        }
        st.setUpdateDate(new Date());
        st.setCreateDate(st.getUpdateDate());
        st.setUser(nuser);
        studentExpansionDao.insert(st);
        return nuser;
    }

    @Transactional(readOnly = false)
    public String saveUser(User user) {
        if (StringUtil.isBlank(user.getId())) {
            //新增时默认设置密码为123456
            if(StringUtil.isEmpty(user.getPassword())){
                user.setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
            }
            user.preInsert();
            insert(user);

            if (SysUserUtils.checkHasRole(user, CoreSval.Rtype.STUDENT)) {
                StudentExpansion studentExpansion = new StudentExpansion();
                //studentExpansion.setId(IdGen.uuid());
                studentExpansion.setUser(user);
                saveStudentExpansion(studentExpansion);
            } else if (SysUserUtils.checkHasRole(user, CoreSval.Rtype.TEACHER) || SysUserUtils.checkHasRole(user, CoreSval.Rtype.EXPORT)) {
                BackTeacherExpansion backTeacherExpansion = new BackTeacherExpansion();
                //backTeacherExpansion.setId(IdGen.uuid());
                backTeacherExpansion.setUser(user);
                if(StringUtil.isNotEmpty(user.getTeacherType())){
                    backTeacherExpansion.setTeachertype(user.getTeacherType());
                }else{
                    backTeacherExpansion.setTeachertype(TeacherType.TY_XY.getKey());
                }
                backTeacherExpansionService.save(backTeacherExpansion);
            }

//            if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XS)) {
//                StudentExpansion studentExpansion = new StudentExpansion();
//                //studentExpansion.setId(IdGen.uuid());
//                studentExpansion.setUser(user);
//                saveStudentExpansion(studentExpansion);
//            } else if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS) || SysUserUtils.checkHasRole(user, RoleBizTypeEnum.XXZJ)) {
//                BackTeacherExpansion backTeacherExpansion = new BackTeacherExpansion();
//                //backTeacherExpansion.setId(IdGen.uuid());
//                backTeacherExpansion.setUser(user);
//                if(StringUtil.isNotEmpty(user.getTeacherType())){
//                    backTeacherExpansion.setTeachertype(user.getTeacherType());
//                }else{
//                    backTeacherExpansion.setTeachertype(TeacherType.TY_XY.getKey());
//                }
//                backTeacherExpansionService.save(backTeacherExpansion);
//            }

            // 更新用户与角色关联
            userDao.deleteUserRole(user);
            if (user.getRoleList() != null && user.getRoleList().size() > 0) {
                userDao.insertUserRole(user);
            } else {
                throw new ServiceException(user.getLoginName() + "没有设置角色！");
            }
            // 将当前用户同步到Activiti
            coreService.saveActivitiUser(user);
            // 清除用户缓存
            CoreUtils.removeUserAll(user);

            return "1";
        } else {
            // 清除原用户机构用户缓存
            User oldUser = userDao.get(user.getId());
            if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
                CacheUtils.remove(CoreUtils.USER_CACHE, CoreUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
            }
            // 更新用户数据
            user.preUpdate();
            user = User.dealLoginName(user);
            user = User.dealOffice(user);
            userDao.update(user);

            if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
                BackTeacherExpansion backTeacherExpansion = backTeacherExpansionService.findTeacherByUserId(user.getId());
                if (backTeacherExpansion == null) {
                    backTeacherExpansion = new BackTeacherExpansion();
                }
                backTeacherExpansion.setUser(user);
                if(StringUtil.isNotEmpty(user.getTeacherType())){
                    backTeacherExpansion.setTeachertype(user.getTeacherType());
                }else{
                    backTeacherExpansion.setTeachertype(TeacherType.TY_XY.getKey());
                }
                backTeacherExpansionService.save(backTeacherExpansion);
            }
            // 更新用户与角色关联
            userDao.deleteUserRole(user);
            if (user.getRoleList() != null && user.getRoleList().size() > 0) {
                userDao.insertUserRole(user);
            } else {
                throw new ServiceException(user.getLoginName() + "没有设置角色！");
            }
            // 将当前用户同步到Activiti
            coreService.saveActivitiUser(user);
            // 清除用户缓存
            CoreUtils.removeUserAll(user);
            return  "2";

        }
    }

    @Transactional(readOnly = false)
    public User newUserByLoginName(User nuser, User user) {
        nuser.setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
        List<Role> roleList= Lists.newArrayList();
        if(StringUtil.isNotEmpty(nuser.getUserType())){
            if((Utype.STUDENT.getKey()).equals(nuser.getUserType())){
                roleList.add(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()));
            }else if((Utype.TEACHER.getKey()).equals(nuser.getUserType())){
                roleList.add(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()));
            }else{
                logger.error("当前用户类型["+nuser.getUserType()+"]");
            }
        }else{
            logger.error("用户类型未定义，无法创建角色！");
        }
        nuser.setRoleList(roleList);
        nuser.setId(IdGen.uuid());
        nuser.setSource("1");
        nuser.setPassc("1");
        if (StringUtils.isNotBlank(user.getId())) {
            nuser.setUpdateBy(user);
            nuser.setCreateBy(user);
        }
        nuser.setUpdateDate(new Date());
        nuser.setCreateDate(nuser.getUpdateDate());
        insert(nuser);
        if(StringUtil.checkNotEmpty(roleList)){
            userDao.insertUserRole(nuser);
        }
        CoreUtils.clearCache(nuser);
        return nuser;
    }

    @Transactional(readOnly = false)
    public void saveStudentExpansion(StudentExpansion entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            studentExpansionDao.insert(entity);
        } else {
            entity.preUpdate();
            studentExpansionDao.update(entity);
        }
    }

    /**
     * 修改导师.
     * @param tc 导师
     * @param nuser 用户参数
     * @param ouser 操作用户
     * @return User
     */
    @Transactional(readOnly = false)
    public User updateTeacherByLoginName(BackTeacherExpansion tc, User nuser, User ouser) {
        if(nuser != null){
            if(StringUtil.isNotEmpty(nuser.getName())){
                ouser.setName(nuser.getName());
            }
            if(StringUtil.isNotEmpty(nuser.getMobile())){
                ouser.setMobile(nuser.getMobile());
            }
            if(StringUtil.isNotEmpty(nuser.getEmail())){
                ouser.setEmail(nuser.getEmail());
            }
            if(nuser.getBirthday() != null){
                ouser.setBirthday(nuser.getBirthday());
            }
            if(StringUtil.isNotEmpty(nuser.getSex())){
                ouser.setSex(nuser.getSex());
            }
            if(StringUtil.isNotEmpty(nuser.getDegree())){
                ouser.setDegree(nuser.getDegree());
            }
            if(StringUtil.isNotEmpty(nuser.getEducation())){
                ouser.setEducation(nuser.getEducation());
            }
            if(nuser.getOffice() != null){
                ouser.setOffice(nuser.getOffice());
            }
            if(StringUtil.isNotEmpty(nuser.getProfessional())){
                ouser.setProfessional(nuser.getProfessional());
            }
        }

        userDao.update(ouser);
        CoreUtils.clearCache(ouser);
        BackTeacherExpansion ost = backTeacherExpansionDao.getByUserId(ouser.getId());
        if(ost != null){
            if(StringUtil.isNotEmpty(tc.getWorkUnit())){
                ost.setWorkUnit(tc.getWorkUnit());
            }
            if(tc.getDiscipline() != null){
                ost.setDiscipline(tc.getDiscipline());
            }
            if(StringUtil.isNotEmpty(tc.getIndustry())){
                ost.setIndustry(tc.getIndustry());
            }
            if(StringUtil.isNotEmpty(tc.getTechnicalTitle())){
                ost.setTechnicalTitle(tc.getTechnicalTitle());
            }
            if(StringUtil.isNotEmpty(tc.getServiceIntention())){
                ost.setServiceIntention(tc.getServiceIntention());
            }
            if(StringUtil.isNotEmpty(tc.getAddress())){
                ost.setAddress(tc.getAddress());
            }
            if(StringUtil.isNotEmpty(tc.getFirstBank())){
                ost.setFirstBank(tc.getFirstBank());
            }
            if(StringUtil.isNotEmpty(tc.getBankAccount())){
                ost.setBankAccount(tc.getBankAccount());
            }
            backTeacherExpansionDao.update(ost);
        }else{
            if(StringUtil.isEmpty(tc.getId())){
                tc.setId(IdGen.uuid());
            }
            tc.setUser(ouser);
            tc.setUpdateDate(new Date());
            tc.setCreateDate(tc.getUpdateDate());
            backTeacherExpansionDao.insert(tc);
        }
        return ouser;
    }

    /**
     * 创建导师.
     * @param nuser 用户参数
     * @param user 操作用户
     * @return User
     */
    @Transactional(readOnly = false)
    public User newTeacherByLoginName(User nuser, User user) {
        return newTeacherByLoginName(new BackTeacherExpansion(), nuser, user);
    }

    @Transactional(readOnly = false)
    public User newTeacherByLoginName(BackTeacherExpansion tc, User nuser, User user) {
        nuser.setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
        List<Role> roleList=new ArrayList<Role>();
        roleList.add(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()));
        nuser.setRoleList(roleList);
        nuser.setId(IdGen.uuid());
        nuser.setSource("1");
        if (StringUtils.isNotBlank(user.getId())) {
            nuser.setUpdateBy(user);
            nuser.setCreateBy(user);
        }
        nuser.setUpdateDate(new Date());
        nuser.setCreateDate(nuser.getUpdateDate());
        nuser.setUserType(RoleBizTypeEnum.DS.getValue());
        insert(nuser);
        userDao.insertUserRole(nuser);
        CoreUtils.clearCache(nuser);
        tc.setId(IdGen.uuid());
        if (StringUtils.isNotBlank(user.getId())) {
            tc.setUpdateBy(user);
            tc.setCreateBy(user);
        }
        tc.setUser(nuser);
        tc.setUpdateDate(new Date());
        tc.setCreateDate(tc.getUpdateDate());
        backTeacherExpansionDao.insert(tc);
        return nuser;
    }

    public List<User> findUserByProfessionIdAndRoleType(String professionalId,RoleBizTypeEnum roletype) {
        User user = new User();
        user.setProfessional(professionalId);
        List<String> rbts=new ArrayList<String>();
        rbts.add(roletype.getValue());
        user.setRoleBizTypes(rbts);
        return userDao.findUserByProfessionIdAndRoleType(user);
    }

    public ApiStatus validateUserRegister(User user) {
        Map<String, String> result = new HashMap<>();
        if (!coreService.checkLoginNameUnique(user.getLoginName(), user.getId())) {
            return new ApiStatus(false, "登录名已存在");
        }
        if (StringUtils.isNotBlank(user.getMobile())) {
            if (!coreService.checkMobileUnique(user.getMobile(), user.getId())) {
                return new ApiStatus(false, "手机号已存在");
            }
        }

        if (!org.springframework.util.StringUtils.isEmpty(user.getNo())) {
            if (!coreService.checkNoUnique(user.getNo(), user.getId())) {
                String msg = user.getUserType().equals(EuserType.UT_C_STUDENT.getType()) ? "学号" : "工号";
                return new ApiStatus(false, msg + "已存在");
            }
        }
        return new ApiStatus();
    }
}
