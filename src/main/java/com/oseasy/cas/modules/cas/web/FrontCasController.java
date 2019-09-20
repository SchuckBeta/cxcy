/**
 *
 */
package com.oseasy.cas.modules.cas.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oseasy.cas.common.config.CasSval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.security.shiro.session.SessionDAO;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.security.AdminFormAuthenticationFilter;
import com.oseasy.com.pcore.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.SysValCode;
import com.oseasy.util.common.utils.CookieUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 网站Controller
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class FrontCasController extends BaseController{
	@Autowired
	private SessionDAO sessionDAO;

    public static String getLoginFront() {
        if(CasSval.getCasLoginOpen()){
            return CoreSval.REDIRECT + CasSval.getCasLoginUrl();
        }
        return CoreSval.LOGIN_FRONT;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = CoreUtils.getPrincipal();
//      // 默认页签模式

        if (logger.isDebugEnabled()) {
            logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        // 如果已登录，再次访问主页，则退出原账号。
        if (Const.TRUE.equals(CoreSval.getConfig("notAllowRefreshIndex"))) {
            CookieUtils.setCookie(response, "LOGINED", "false");
        }

        // 如果已经登录，则跳转到管理首页
        if (principal != null && !principal.isMobileLogin()) {
            return CoreSval.REDIRECT + frontPath;
        }
        request.setAttribute("loginType", request.getParameter("loginType"));
        request.setAttribute("loginPage", "1");
        model.addAttribute("isValidateCodeLogin", CoreUtils.isValidateCodeLogin(false, false));
        return getLoginFront();
    }


    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = CoreUtils.getPrincipal();

        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            return CoreSval.REDIRECT + frontPath;
        }

        String username = WebUtils.getCleanParam(request, AdminFormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, AdminFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, AdminFormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String)request.getAttribute(AdminFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String)request.getAttribute(AdminFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtil.isBlank(message) || StringUtil.equals(message, "null")) {
            message = "账号或密码错误, 请重试.";
        }

        model.addAttribute(AdminFormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(AdminFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(AdminFormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(AdminFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(AdminFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        if (logger.isDebugEnabled()) {
            logger.debug("login fail, active session size: {}, message: {}, exception: {}",
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isValidateCodeLogin", CoreUtils.isValidateCodeLogin(true, false));
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(SysValCode.VKEY, IdGen.uuid());

        // 如果是手机登录，则返回JSON字符串
        if (mobile) {
            return renderString(response, model);
        }
        request.setAttribute("loginType", request.getParameter("loginType"));
        request.setAttribute("loginPage", "1");
        model.addAttribute("isValidateCodeLogin", CoreUtils.isValidateCodeLogin(false, false));
        return getLoginFront();
    }

    @RequestMapping(value="toLogin")
    public String toLogin(HttpServletRequest request, HttpServletResponse response,Model model) {
        request.setAttribute("loginType", request.getParameter("loginType"));
        request.setAttribute("loginPage", "1");
        model.addAttribute("isValidateCodeLogin", CoreUtils.isValidateCodeLogin(false, false));
        return getLoginFront();
    }
}
