package com.oseasy.sys.modules.sys.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.common.config.ApiStatus;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.modules.sys.enums.Retype;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.security.MyUsernamePasswordToken;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.SysValCode;
import com.oseasy.com.pcore.modules.sys.web.RegisterController;
import com.oseasy.sys.modules.sys.enums.EuserType;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.SysSystemService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 注册Controller
 * @author zhang
 * @version 2017-3-22
 */

@Controller
@RequestMapping(value = "${frontPath}/register")
public class SysRegisterController extends BaseController {
    @Autowired
    private SysSystemService sysSystemService;
    @Autowired
    private CoreService coreService;

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/saveRegister")
    public String register(User user, Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String regType = request.getParameter(RegisterController.REG_TYPE);//注册类型，手机注册1或2学号工号注册
        String toRegisterMsg = null;
        //校验验证码
        if (regType.equals(RegisterController.REG_TYPE_NO)) {
            String validateCode = request.getParameter(SysValCode.F_LOGIN.getPkey());
            if (StringUtils.isEmpty(validateCode)) {
                toRegisterMsg = "验证码不能为空！";
            }

            if (!SysValCode.checkVerCode(SysValCode.F_LOGIN, request, validateCode)) {
                toRegisterMsg = "验证码输入不正确";
            }

            if (StringUtils.isEmpty(user.getUserType())) {
                toRegisterMsg = "注册用户类型不能为空！";
            }
        } else {
            if ((CoreSval.isSmsHasMvcode())) {
                String sruyazhengma = request.getParameter(SysValCode.F_MOBILE.getPkey());
                if (!SysValCode.checkVerCode(SysValCode.F_MOBILE, request, sruyazhengma)) {
                    toRegisterMsg = "验证码输入不正确";
                }
            }
        }

        //验证登录名、手机号
        ApiStatus validate = sysSystemService.validateUserRegister(user);
        if (!validate.getStatus()) {
            toRegisterMsg = validate.getMsg();
        }

        String password = user.getPassword();
        user.setPassword(CoreUtils.entryptPassword(user.getPassword()));
        user.setCreateBy(user);
        Date date = new Date();
        user.setCreateDate(date);
        user.setUpdateBy(user);
        user.setUpdateDate(date);
        user.setDelFlag("0");
        if (user.getUserType().equals(EuserType.UT_C_TEACHER.getType())) {
            List<Role> roleList=new ArrayList<>();
            roleList.add(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()));
            user.setUserType(RoleBizTypeEnum.DS.getValue());
            user.setRoleList(roleList);
        } else {
            List<Role> roleList=new ArrayList<>();
            roleList.add(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()));
            user.setUserType(RoleBizTypeEnum.XS.getValue());
            user.setRoleList(roleList);
        }

        if(StringUtil.isNotEmpty(toRegisterMsg)){
            redirectAttributes.addAttribute(CoreJkey.JK_MSG, toRegisterMsg);
            redirectAttributes.addAttribute(RegisterController.USER_TYPE, user.getUserType());
            System.out.println(CoreSval.REDIRECT+ CoreSval.getFrontPath() + Retype.F_REGISTER_FAIL.getUrl());
            return CoreSval.REDIRECT+ CoreSval.getFrontPath() + Retype.F_REGISTER_FAIL.getUrl();
        }

        sysSystemService.saveUser(user);
        model.addAttribute("name", user.getLoginName());
        try {
            Subject subject = SecurityUtils.getSubject();
            MyUsernamePasswordToken token = new MyUsernamePasswordToken();
            token.setUsername(user.getLoginName());
            token.setPassword(password.toCharArray());
            token.setLoginType(MyUsernamePasswordToken.LoginType.PWD);
            subject.login(token);
            CoreUtils.dealPassword(password);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        model.addAttribute(RegisterController.USER_TYPE, user.getUserType());
        //更新租户缓存已拿到正确的租户id
        TenantConfig.initLoginCache(request,response);
        return CoreSval.REDIRECT+ CoreSval.getFrontPath() + Retype.F_REGISTER_SUCCESS.getUrl();
    }
}
