/**
 * .
 */

package com.oseasy.cas.modules.cas.service;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.common.config.CoreSval;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.common.config.CasSval.CasEmskey;
import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.cas.modules.cas.entity.SysUser;
import com.oseasy.cas.modules.cas.vo.CheckRet;
import com.oseasy.cas.modules.cas.vo.impl.DBAnZhiVo;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.Retype;
import com.oseasy.com.pcore.modules.sys.security.MyUsernamePasswordToken;
import com.oseasy.com.pcore.modules.sys.security.MyUsernamePasswordToken.LoginType;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.service.SysSystemService;
import com.oseasy.sys.modules.sys.vo.Utype;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonNetUtils;

/**
 * .
 * @author chenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class SysUserService {
    public static Logger logger = Logger.getLogger(SysUserService.class);
    @Autowired
    private CoreService coreService;
    @Autowired
    private SysSystemService sysSystemService;
    public static final String CAS_USER = "ruser";

    /**
     * 创建用户.
     * @param nuser 用户参数
     * @param user 操作用户
     * @return User
     */
    @Transactional(readOnly = false)
    public User newUserByLoginName(User nuser, User user, SysCasUser casUser) {
        try {
            List<DBAnZhiVo> dbAnZhiVos = JsonNetUtils.toBeans(casUser.getRjson(), DBAnZhiVo.class);
            if(StringUtil.checkNotEmpty(dbAnZhiVos) && dbAnZhiVos.size() == 1){
                nuser = DBAnZhiVo.dealDbvo(nuser, dbAnZhiVos.get(0));
            }
        } catch (Exception e) {
            logger.error("rjson类型转换异常，拓展数据无法初始化！");
        }
        return sysSystemService.newStudentByLoginName(nuser, user);
    }

    @Transactional(readOnly = false)
    public User newStudentByLoginName(User nuser, User user, SysCasUser casUser) {
        StudentExpansion st = null;
        try {
            List<DBAnZhiVo> dbAnZhiVos = JsonNetUtils.toBeans(casUser.getRjson(), DBAnZhiVo.class);
            if(StringUtil.checkNotEmpty(dbAnZhiVos) && dbAnZhiVos.size() == 1){
                st = DBAnZhiVo.dealStudentExpansion(new StudentExpansion(), dbAnZhiVos.get(0));
                nuser = DBAnZhiVo.dealDbvo(nuser, dbAnZhiVos.get(0));
            }
        } catch (Exception e) {
            logger.error("rjson类型转换异常，拓展数据无法初始化！");
        }
        if(st == null){
            st = new StudentExpansion();
        }
        return sysSystemService.newStudentByLoginName(st, nuser, user);
    }


    @Transactional(readOnly = false)
    public User newTeacherByLoginName(User nuser, User user, SysCasUser casUser) {
        BackTeacherExpansion st = null;
        try {
            List<DBAnZhiVo> dbAnZhiVos = JsonNetUtils.toBeans(casUser.getRjson(), DBAnZhiVo.class);
            if(StringUtil.checkNotEmpty(dbAnZhiVos) && dbAnZhiVos.size() == 1){
                st = DBAnZhiVo.dealBackTeacherExpansion(new BackTeacherExpansion(), dbAnZhiVos.get(0));
                nuser = DBAnZhiVo.dealDbvo(nuser, dbAnZhiVos.get(0));
            }
        } catch (Exception e) {
            logger.error("rjson类型转换异常，拓展数据无法初始化！");
        }
        if(st == null){
            st = new BackTeacherExpansion();
        }
        return sysSystemService.newTeacherByLoginName(st, nuser, user);
    }

    @Transactional(readOnly = false)
    public SysUser newUser(SysUser entity) {
        if(StringUtil.isEmpty(entity.getUser().getNo())){
            entity.getUser().setNo(entity.getUser().getLoginName());
        }
        if((Utype.STUDENT.getKey()).equals(entity.getType())){
            entity.setUser(newStudentByLoginName(entity.getUser(), CoreUtils.getUser(), entity.getCasUser()));
        }else if((Utype.TEACHER.getKey()).equals(entity.getType())){
            entity.setUser(newTeacherByLoginName(entity.getUser(), CoreUtils.getUser(), entity.getCasUser()));
        }else{
            entity.setUser(newUserByLoginName(entity.getUser(), CoreUtils.getUser(), entity.getCasUser()));
        }
        return entity;
    }


    @Transactional(readOnly = false)
    public User updateStudentByLoginName(User nuser, SysCasUser casUser) {
        StudentExpansion st = null;
        try {
            List<DBAnZhiVo> dbAnZhiVos = JsonNetUtils.toBeans(casUser.getRjson(), DBAnZhiVo.class);
            if(StringUtil.checkNotEmpty(dbAnZhiVos) && dbAnZhiVos.size() == 1){
                st = DBAnZhiVo.dealStudentExpansion(new StudentExpansion(), dbAnZhiVos.get(0));
                //nuser = DBAnZhiVo.dealDbvo(nuser, dbAnZhiVos.get(0));
            }
        } catch (Exception e) {
            logger.error("rjson类型转换异常，拓展数据无法初始化！");
        }
        if(st == null){
            st = new StudentExpansion();
        }
        return sysSystemService.updateStudentByLoginName(st, null, nuser);
    }

    @Transactional(readOnly = false)
    public User updateTeacherByLoginName(User nuser, SysCasUser casUser) {
        BackTeacherExpansion st = null;
        try {
            List<DBAnZhiVo> dbAnZhiVos = JsonNetUtils.toBeans(casUser.getRjson(), DBAnZhiVo.class);
            if(StringUtil.checkNotEmpty(dbAnZhiVos) && dbAnZhiVos.size() == 1){
                st = DBAnZhiVo.dealBackTeacherExpansion(new BackTeacherExpansion(), dbAnZhiVos.get(0));
                //nuser = DBAnZhiVo.dealDbvo(nuser, dbAnZhiVos.get(0));
            }
        } catch (Exception e) {
            logger.error("rjson类型转换异常，拓展数据无法初始化！");
        }
        if(st == null){
            st = new BackTeacherExpansion();
        }
        return sysSystemService.updateTeacherByLoginName(st, null, nuser);
    }

    @Transactional(readOnly = false)
    private User updateUserByLoginName(User user, SysCasUser casUser) {
        try {
            List<DBAnZhiVo> dbAnZhiVos = JsonNetUtils.toBeans(casUser.getRjson(), DBAnZhiVo.class);
            if(StringUtil.checkNotEmpty(dbAnZhiVos) && dbAnZhiVos.size() == 1){
                //nuser = DBAnZhiVo.dealDbvo(nuser, dbAnZhiVos.get(0));
            }
        } catch (Exception e) {
            logger.error("rjson类型转换异常，拓展数据无法初始化！");
        }
        return sysSystemService.updateUserByLoginName(null, user);
    }

    /**
     * 修改用户.
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public SysUser updateUser(SysUser entity) {
        if(StringUtil.isEmpty(entity.getUser().getNo())){
            entity.getUser().setNo(entity.getUser().getLoginName());
        }
        if((Utype.STUDENT.getKey()).equals(entity.getType())){
            if(StringUtil.isEmpty(entity.getUser().getUserType())){
                entity.getUser().setUserType(Utype.STUDENT.getKey());
            }

            /**
             * 新增学生角色.
             */
            if((CheckRet.FALSE.getKey()).equals(entity.getCheckRetRole())){
                entity.getUser().setRoleList(Arrays.asList(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey())));
                coreService.insertUserRole(entity.getUser());
            }

            /**
             * 新增学生.
             */
            if((CheckRet.FALSE.getKey()).equals(entity.getCheckRetST())){
                entity.setUser(updateStudentByLoginName(entity.getUser(), entity.getCasUser()));
            }
        }else if((Utype.TEACHER.getKey()).equals(entity.getType())){
            if(StringUtil.isEmpty(entity.getUser().getUserType())){
                entity.getUser().setUserType(Utype.TEACHER.getKey());
            }

            /**
             * 新增导师角色.
             */
            if((CheckRet.FALSE.getKey()).equals(entity.getCheckRetRole())){
                entity.getUser().setRoleList(Arrays.asList(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey())));
                coreService.insertUserRole(entity.getUser());
            }

            /**
             * 新增导师.
             */
            if((CheckRet.FALSE.getKey()).equals(entity.getCheckRetST())){
                entity.setUser(updateTeacherByLoginName(entity.getUser(), entity.getCasUser()));
            }
        }else{
            entity.setUser(updateUserByLoginName(entity.getUser(), entity.getCasUser()));
        }
        return entity;
    }

    public List<SysUser> findList(SysUser entity) {
        return SysUser.convertSysUser(coreService.findUser(entity.getUser()));
    }
    public List<SysUser> getUserByNoAndLoginName(SysUser entity) {
        return SysUser.convertSysUser(coreService.getUserByNoAndLoginName(entity.getUser()));
    }

    /**
     * 检查用户是否存在于sys_user
     * @param entity
     * @return
     */
    public SysUser checkSysUser(SysUser entity) {
        if(entity == null){
            entity = new SysUser();
        }
        entity.setCheckRet(CheckRet.FALSE.getKey());

        if(entity.getUser() == null){
            return entity;
        }

        User user = entity.getUser();
        if(StringUtil.isEmpty(user.getLoginName())){
            return entity;
        }

        User puentity = new User();
        puentity.setLoginName(user.getLoginName());
        if(StringUtil.isEmpty(user.getNo())){
            puentity.setNo(user.getLoginName());
        }else{
            puentity.setNo(user.getNo());
        }

        if(coreService.checkUserByNoAndLoginName(puentity)){
            entity = getUserByNoOrLoginName(puentity);
            entity.setCheckRet(CheckRet.TRUE.getKey());
        }else{
            entity.setCheckRet(CheckRet.FALSE.getKey());
        }
        return entity;
    }

    public SysUser getUserByNoOrLoginName(User user) {
        /**
         * 根据登录名获取用户.
         */
        SysUser entity;
        User puser = new User();
        puser.setLoginName(user.getLoginName());
        puser.setNo(user.getLoginName());
        List<SysUser> entitys = getUserByNoAndLoginName(new SysUser(puser));
        entity = entitys.get(0);
        return entity;
    }

    public String loginTokenPre(HttpServletRequest request, SysUser sysUser, SysCasUser reqSysCasUser) {
        //查询用户表，获取用户信息，处理登录逻辑.
        request.setAttribute(CoreJkey.JK_MSG, "请检查用户是否被删除，请联系管理员!");
        sysUser.setCasUser(reqSysCasUser);
        sysUser.setCas(false);
        sysUser.setRetype(Retype.F_R_ERROR.getKey());
        request.setAttribute(SysUserService.CAS_USER, sysUser);
        return CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_R_ERROR.getUrl();
    }

    public void loginToken(SysUser sysUser) {
        try {
            Subject subject = SecurityUtils.getSubject();
            MyUsernamePasswordToken token = new MyUsernamePasswordToken();
            token.setUsername(sysUser.getUser().getLoginName());
            token.setPassword(sysUser.getUser().getPassword().toCharArray());
            token.setLoginType(LoginType.DSF);
            token.setCas((sysUser.isCas() == null) ? false : sysUser.isCas());
            subject.login(token);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
