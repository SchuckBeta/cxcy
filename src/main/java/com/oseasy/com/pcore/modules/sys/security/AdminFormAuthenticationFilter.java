/**
 *
 */
package com.oseasy.com.pcore.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.TenantCvtype;
import com.oseasy.util.common.utils.UrlUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import com.oseasy.com.pcore.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 表单验证（包含验证码）过滤类

 * @version 2014-5-19
 */
@Service
public class AdminFormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
	private String messageParam = DEFAULT_MESSAGE_PARAM;

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		if (password==null) {
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = UrlUtil.getRemoteAddr((HttpServletRequest)request);
		String captcha = getCaptcha(request);
		boolean mobile = isMobileLogin(request);
//		StringBuffer url = new StringBuffer(request.getServerName());
//		url.append(":");
//		url.append(request.getServerPort());
		MyUsernamePasswordToken token=new MyUsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile);
		//MyUsernamePasswordToken token=new MyUsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile,url.toString());
		return token;
	}

	/**
	 * 获取登录用户名
	 */
	protected String getUsername(ServletRequest request, ServletResponse response) {
		String username = super.getUsername(request);
		if (StringUtil.isBlank(username)) {
			username = StringUtil.toString(request.getAttribute(getUsernameParam()), StringUtil.EMPTY);
		}
		return username;
	}

	/**
	 * 获取登录密码
	 */
	@Override
	protected String getPassword(ServletRequest request) {
		String password = super.getPassword(request);
		if (StringUtil.isBlank(password)) {
			password = StringUtil.toString(request.getAttribute(getPasswordParam()), StringUtil.EMPTY);
		}
		CoreUtils.dealPassword(password);
		return password;
	}

	/**
	 * 获取记住我
	 */
	@Override
	protected boolean isRememberMe(ServletRequest request) {
		String isRememberMe = WebUtils.getCleanParam(request, getRememberMeParam());
		if (StringUtil.isBlank(isRememberMe)) {
			isRememberMe = StringUtil.toString(request.getAttribute(getRememberMeParam()), StringUtil.EMPTY);
		}
		return StringUtil.toBoolean(isRememberMe);
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	public String getMobileLoginParam() {
		return mobileLoginParam;
	}

	protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }

	public String getMessageParam() {
		return messageParam;
	}

	/**
	 * 登录成功之后跳转URL
	 */
	public String getSuccessUrl() {
		return super.getSuccessUrl();
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request,
			ServletResponse response) throws Exception {
		TenantConfig.initLoginCache(request, response);

		WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
		ShiroHttpServletRequest sreq=(ShiroHttpServletRequest)request;
		sreq.getSession().setAttribute("notifyShow", "null");//登录后重置消息是否弹出
		// 登录成功后，验证码计算器清零
		Principal principal = CoreUtils.getPrincipal();

		if (principal!=null)CoreUtils.isValidateCodeLogin(false, true);
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request, ServletResponse response) {
		String className = e.getClass().getName(), message = "";
		if (IncorrectCredentialsException.class.getName().equals(className)) {
			message = "密码错误, 请重试.";
		}else if (UnknownAccountException.class.getName().equals(className)) {
			message = "该账号不存在";
		}else if (e.getMessage() != null && StringUtil.startsWith(e.getMessage(), "msg:")) {
			message = StringUtil.replace(e.getMessage(), "msg:", "");
		}
		else{
			message = "系统出现点问题，请稍后再试！";
			e.printStackTrace(); // 输出到控制台
		}
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
	}

}