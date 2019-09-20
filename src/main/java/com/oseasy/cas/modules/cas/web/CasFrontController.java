/**
 * .
 */

package com.oseasy.cas.modules.cas.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.common.config.CasSval.CasEmskey;
import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.cas.modules.cas.entity.SysUser;
import com.oseasy.cas.modules.cas.service.SysCasUserService;
import com.oseasy.cas.modules.cas.service.SysUserService;
import com.oseasy.cas.modules.cas.utils.CasUtils;
import com.oseasy.cas.modules.cas.vo.CheckRet;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.Retype;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
@Controller
@RequestMapping(value = "${frontPath}/cas")
public class CasFrontController extends BaseController {
    public static Logger logger = Logger.getLogger(CasFrontController.class);
    private static final String CAS_PAGE_ERROR = CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_R_ERROR.getUrl();
    private static final String CAS_PAGE_SUCCESS = CoreSval.REDIRECT + "/f";

    @Autowired
    SysCasUserService sysCasUserService;
    @Autowired
    SysUserService sysUserService;

    @ModelAttribute
    public SysCasUser get(@RequestParam(required=false) String id) {
        SysCasUser entity = null;
        if (StringUtil.isNotBlank(id)){
            entity = sysCasUserService.get(id);
        }
        if (entity == null){
            entity = new SysCasUser();
        }
        return entity;
    }


    @RequestMapping(value = "/caslogin", method = RequestMethod.GET)
    public String caslogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("CAS 登录开始执行!");
        SysUser retUser = null;
        SysCasUser reqSysCasUser = CasUtils.getSysCasUser(request);
        reqSysCasUser.setEnable(true);
        if(StringUtil.isEmpty(reqSysCasUser.getRuid())){
            request.setAttribute(CoreJkey.JK_MSG, "用户登录名不能为空!");
            retUser = new SysUser(reqSysCasUser);
            retUser.setCas(false);
            retUser.setRetype(Retype.F_REGISTER.getKey());
            request.setAttribute(SysUserService.CAS_USER, retUser);
            request.setAttribute(CoreJkey.JK_ROOT, "/"+Sval.Emkey.CAS.getKey());
            return CAS_PAGE_ERROR;
        }

        //检查登录用户是否存在于sys_cas_user处理表中
        SysCasUser sysCasUser = sysCasUserService.checkSysCaseUser(reqSysCasUser);
        if(sysCasUser.getCheckUtype()){
            request.setAttribute(CoreJkey.JK_MSG, "用户类型不能为空!");
            retUser = new SysUser(sysCasUser);
            retUser.setCas(false);
            retUser.setRetype(Retype.F_SELECT_UTYPE.getKey());
            request.setAttribute(SysUserService.CAS_USER, retUser);
            return CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_SELECT_UTYPE.getUrl();
        }

        if(!sysCasUser.getEnable()){
            //查询用户表，获取用户信息，处理登录逻辑.
            request.setAttribute(CoreJkey.JK_MSG, "当前用户被禁止使用Cas登录，请前往登录页登录!");
            retUser = new SysUser(reqSysCasUser);
            retUser.setCas(false);
            retUser.setRetype(Retype.F_AUTH_ENABLE.getKey());
            request.setAttribute(SysUserService.CAS_USER, retUser);
            return CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_AUTH_ENABLE.getUrl();
        }

        if((CheckRet.TRUE.getKey()).equals(sysCasUser.getCheckRet())) {
            logger.info("存在CASUSER = (" + sysCasUser.getLog() + ")！");
            //存在该用户
            if(StringUtil.isEmpty(sysCasUser.getRuid())){
                request.setAttribute(CoreJkey.JK_MSG, "用户登录名不能为空!");
            }

            //查询用户表，获取用户信息，处理登录逻辑.
            //查询User用户
            User puser = new User();
            puser.setLoginName(sysCasUser.getRuid());
            puser.setNo(puser.getLoginName());
            SysUser sysUser = sysUserService.checkSysUser(new SysUser(puser));
            sysUser = SysUser.updateCheck(sysUser, sysCasUser);
            if((CheckRet.TRUE.getKey()).equals(sysCasUser.getCheckRetUser())) {
                logger.info("存在CASUSER.USER = (" + sysUser.getLog() + ")！");
                sysCasUser.setUid(sysUser.getUser().getId());
                //更新SysCasUser用户
                sysCasUser.setTime(sysCasUser.getTime()+1);
                sysCasUser.setLastLoginDate(new Date());
                sysCasUser.setUpdateBy(sysUser.getUser());
                sysUser = sysUserService.updateUser(sysUser);
                sysCasUserService.save(sysCasUser);
                sysUser.setCasUser(sysCasUser);
                //调用登录逻辑
                sysUser.setCas(true);
                sysUser.setRetype(Retype.F_INDEX.getKey());
                retUser = sysUser;
                if((Const.YES).equals(sysUser.getUser().getDelFlag())){
                    return sysUserService.loginTokenPre(request, sysUser, reqSysCasUser);
                }

                if((CheckRet.FALSE.getKey()).equals(sysCasUser.getCheckRetRole())) {

                }

                sysUserService.loginToken(sysUser);
            }else{
                logger.info("不存在CASUSER.USER = (" + sysUser.getLog() + ")！");
                //SysUser创建用户信息
                sysUser = new SysUser(new User());
                sysUser.setCasUser(sysCasUser);
                sysUser.setType(sysCasUser.getRutype());
                sysUser.getUser().setName(sysCasUser.getRcname());
                sysUser.getUser().setLoginName(sysCasUser.getRuid());
                if(StringUtil.isEmpty(sysUser.getUser().getNo())){
                    sysUser.getUser().setNo(sysCasUser.getRuid());
                }
                sysUser.getUser().setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
                sysUser.getUser().setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                sysUser.getUser().setUpdateBy(sysUser.getUser().getCreateBy());
                sysUser = sysUserService.newUser(sysUser);
                //更新SysCasUser用户
                if(StringUtil.isNotEmpty(sysUser.getUser().getId())){
                    sysCasUser.setUid(sysUser.getUser().getId());
                }
                sysCasUser.setTime(sysCasUser.getTime()+1);
                sysCasUser.setLastLoginDate(new Date());
                sysCasUser.setUpdateBy(sysUser.getUser());
                sysCasUserService.save(sysCasUser);
                sysUser.setCasUser(sysCasUser);
                //调用登录逻辑
                sysUser.setCas(true);
                sysUser.setRetype(Retype.F_INDEX.getKey());
                request.setAttribute(CoreJkey.JK_MSG, "当前用户第一次登录，默认密码为：("+CoreUtils.USER_PSW_DEFAULT+")，为了保证您的账户安全，请修改密码!");
                retUser = sysUser;
                if((Const.YES).equals(sysUser.getUser().getDelFlag())){
                    return sysUserService.loginTokenPre(request, sysUser, reqSysCasUser);
                }
                sysUserService.loginToken(sysUser);
            }
        } else {
            logger.info("不存在CASUSER = (" + sysCasUser.getLog() + ")！");
            //不存在该用户
            //查询SysUser用户
            User puser = new User();
            puser.setLoginName(sysCasUser.getRuid());
            puser.setNo(puser.getLoginName());
            SysUser sysUser = sysUserService.checkSysUser(new SysUser(puser));
            sysUser = SysUser.updateCheck(sysUser, sysCasUser);
            if((CheckRet.TRUE.getKey()).equals(sysUser.getCheckRet())) {
                //更新SysCasUser用户
                sysCasUser.setTime(1);
                sysCasUser.setId(IdGen.uuid());
                sysCasUser.setIsNewRecord(true);
                sysCasUser.setLastLoginDate(new Date());
                sysCasUser.setUid(sysUser.getUser().getId());
                sysCasUser.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                sysCasUser.setUpdateBy(sysCasUser.getCreateBy());
                sysUserService.updateUser(sysUser);
                sysCasUserService.save(sysCasUser);
                sysCasUserService.casDeletes(reqSysCasUser);
                //调用登录逻辑
                sysUser.setCas(true);
                sysUser.setRetype(Retype.F_INDEX.getKey());
                retUser = sysUser;
                if((Const.YES).equals(sysUser.getUser().getDelFlag())){
                    return sysUserService.loginTokenPre(request, sysUser, reqSysCasUser);
                }
                sysUserService.loginToken(sysUser);
            }else{
                //SysUser创建用户信息
                sysUser = new SysUser(new User());
                sysUser.setCasUser(sysCasUser);
                sysUser.setType(sysCasUser.getRutype());
                sysUser.getUser().setName(sysCasUser.getRcname());
                sysUser.getUser().setLoginName(sysCasUser.getRuid());
                if(StringUtil.isEmpty(sysUser.getUser().getNo())){
                    sysUser.getUser().setNo(sysCasUser.getRuid());
                }
                sysUser.getUser().setPassword(CoreUtils.entryptPassword(CoreUtils.USER_PSW_DEFAULT));
                sysUser.getUser().setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                sysUser.getUser().setUpdateBy(sysCasUser.getCreateBy());
                sysUserService.newUser(sysUser);
                //更新SysCasUser用户
                if(StringUtil.isNotEmpty(sysUser.getUser().getId())){
                    sysCasUser.setUid(sysUser.getUser().getId());
                }
                sysCasUser.setTime(1);
                sysCasUser.setId(IdGen.uuid());
                sysCasUser.setIsNewRecord(true);
                sysCasUser.setLastLoginDate(new Date());
                sysCasUser.setCreateBy(sysUser.getUser());
                sysCasUser.setUpdateBy(sysCasUser.getCreateBy());
                sysCasUserService.save(sysCasUser);
                sysCasUserService.casDeletes(reqSysCasUser);
                //调用登录逻辑
                sysUser.setCas(true);
                sysUser.setRetype(Retype.F_INDEX.getKey());
                request.setAttribute(CoreJkey.JK_MSG, "当前用户第一次登录，默认密码为：("+CoreUtils.USER_PSW_DEFAULT+")，为了保证您的账户安全，请修改密码!");
                retUser = sysUser;
                if((Const.YES).equals(sysUser.getUser().getDelFlag())){
                    return sysUserService.loginTokenPre(request, sysUser, reqSysCasUser);
                }
                sysUserService.loginToken(sysUser);
            }
        }

        request.setAttribute(SysUserService.CAS_USER, retUser);
        return CAS_PAGE_SUCCESS;
    }

    @RequestMapping(value = "/casRselectUtype", method = RequestMethod.GET)
    public String casRselectUtype(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute(SysUserService.CAS_USER, CasUtils.getSysCasUser(request));
        return CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_SELECT_UTYPE.getUrl();
    }

    @RequestMapping(value = "/casRcasenable", method = RequestMethod.GET)
    public String casRcasenable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute(SysUserService.CAS_USER, CasUtils.getSysCasUser(request));
        return CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_AUTH_ENABLE.getUrl();
    }

    @RequestMapping(value = "/casRerror", method = RequestMethod.GET)
    public String casRerror(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute(SysUserService.CAS_USER, CasUtils.getSysCasUser(request));
        return CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_R_ERROR.getUrl();
    }

    @RequestMapping(value = "/casRsucess", method = RequestMethod.GET)
    public String casRsucess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute(SysUserService.CAS_USER, CasUtils.getSysCasUser(request));
        return CasSval.path.vms(CasEmskey.CAS.k()) + Retype.F_R_SUCCESS.getUrl();
    }

    @RequestMapping(value = "/caslogout", method = RequestMethod.GET)
    public String caslogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("caslogout is success!");
        request.setAttribute(SysUserService.CAS_USER, CasUtils.getSysCasUser(request));
        request.setAttribute(CasSval.CAS_LOGOUT_URL, CasSval.CAS_LOGOUT);
        return CasSval.path.vms(CasEmskey.CAS.k()) + "caslogout";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("info is success!");
        request.setAttribute(SysUserService.CAS_USER, CasUtils.getSysCasUser(request));
        return CasSval.path.vms(CasEmskey.CAS.k()) + "info";
    }
}
