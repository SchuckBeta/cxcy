/**
 *
 */
package com.oseasy.com.pcore.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.ICorePn;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.Retype;
import com.oseasy.com.pcore.modules.sys.security.MyUsernamePasswordToken;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.sys.modules.sys.vo.LgType;
import com.oseasy.sys.modules.sys.vo.Urvo;
import com.oseasy.sys.modules.sys.vo.Usvo;
import com.oseasy.util.common.utils.UrlUtil;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

import java.util.List;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
    public static final String LOGIN_ADM = CoreSval.getAdminPath();
    public static final String LOGIN_ADM_REDIRECT = CoreSval.REDIRECT + LOGIN_ADM;
	@Autowired
	private CoreService coreService;
	@Autowired
	private SysTenantService sysTenantService;


	@Autowired
	private SessionDAO sessionDAO;
	@RequestMapping(value = "${adminPath}/sysMenuIndex", method = RequestMethod.GET)
	public String sysMenuIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		return ICorePn.curPt().loginSuccessPage(Sval.EmPt.TM_ADMIN);
	}

	@RequestMapping(value = "${adminPath}/sysOldIndex", method = RequestMethod.GET)
	public String sysOldIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		return ICorePn.curPt().indexPage(Sval.EmPt.TM_ADMIN);
	}

	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = CoreUtils.getPrincipal();

//		// 默认页签模式
//		String tabmode = CookieUtils.getCookie(request, "tabmode");
//		if (tabmode == null) {
//			CookieUtils.setCookie(response, "tabmode", "1");
//		}

		if (logger.isDebugEnabled()) {
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Const.TRUE.equals(CoreSval.getConfig("notAllowRefreshIndex"))) {
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return CoreSval.REDIRECT + adminPath;
		}
		model.addAttribute("isValidateCodeLogin", CoreUtils.isValidateCodeLogin(false, false));
		return ICorePn.curPt().loginPage(Sval.EmPt.TM_ADMIN);
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
//	@CrossOrigin(origins = {"http:/yy.com:8080", "null"})
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String alogin(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = CoreUtils.getPrincipal();

		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			return CoreSval.REDIRECT + adminPath;
		}

		String username = WebUtils.getCleanParam(request, AdminFormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, AdminFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, AdminFormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String)request.getAttribute(AdminFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(AdminFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

		if (StringUtil.isBlank(message) || StringUtil.equals(message, "null")) {
			message = "用户或密码错误, 请重试.";
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
		model.addAttribute("isValidateCodeLogin", CoreUtils.isValidateCodeLogin(false, false));
		return ICorePn.curPt().loginPage(Sval.EmPt.TM_ADMIN);
	}

	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtil.isNotBlank(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
//		}else{
//			theme = CookieUtils.getCookie(request, "theme");
		}
		return CoreSval.REDIRECT+request.getParameter("url");
	}

	/**
	 * 全局登录页面.
	 * @return String
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toAllLogin() {
		return CorePages.ALL_LOGIN.getIdxUrl();
	}

	/**
	 * 全局登录，真正登录的POST请求由Filter完成
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ApiTstatus<Urvo> allLogin(@RequestBody Usvo usvo, HttpServletRequest request, HttpServletResponse response, Model model) {
		ApiTstatus<Urvo> rstatus = new ApiTstatus<>();
		ApiTstatus<SysTenant> sysTenantRstatus = getTenantByLoginName(usvo);
		if(!sysTenantRstatus.getStatus()){
			rstatus.setStatus(false);
			rstatus.setMsg(sysTenantRstatus.getMsg());
			return rstatus;
		}

//		usvo.setLgType(LgType.MANAGER.getKey());
		SysTenant sysTenant = sysTenantRstatus.getDatas();
		if(sysTenant == null){
			rstatus.setStatus(false);
			rstatus.setMsg("请求失败登录名不合法");
			rstatus.getDatas().setUrl(CorePages.ERROR_404.getIdxUrl());
			return rstatus;
		}

		rstatus.setDatas(new Urvo(request, usvo));
		if(StringUtil.isEmpty(usvo.getLgType()) || StringUtil.isEmpty(usvo.getLoginName())){
			rstatus.setStatus(false);
			rstatus.setMsg("登录类型和登录名不能为空");
			rstatus.getDatas().setUrl(CorePages.ERROR_404.getIdxUrl());
			return rstatus;
		}
		LgType lgType = LgType.getByKey(usvo.getLgType());
		if((lgType == null)){
			rstatus.setStatus(false);
			rstatus.setMsg("登录类型不存在");
			rstatus.getDatas().setUrl(CorePages.ERROR_404.getIdxUrl());
			return rstatus;
		}

		if(!lgType.isEnable()){
			rstatus.setStatus(false);
			rstatus.setMsg("登录类型被禁用");
			rstatus.getDatas().setUrl(CorePages.ERROR_404.getIdxUrl());
			return rstatus;
		}

		sysTenant = sysTenantService.getByLoginName(usvo.getLoginName(), usvo.getId());
		if (sysTenant == null) {
			rstatus.setStatus(false);
			rstatus.setMsg("租户不存在或不唯一");
			rstatus.getDatas().setUrl(CorePages.ERROR_404.getIdxUrl());
			return rstatus;
		}

		String posturl = null;
		if(!rstatus.getStatus()){
			rstatus.getDatas().setUrl(CoreSval.REDIRECT+ posturl);
			return rstatus;
		}

		if(LgType.frontFilter(lgType)){
			rstatus.getDatas().getUsvo().setLoginType(MyUsernamePasswordToken.LoginType.PWD.getKey());
			rstatus.getDatas().getUsvo().setLgType(lgType.getKey());
			posturl = postLogin(false, sysTenant, CoreSval.getFrontPath(), "/login");
//			posturl = "http://" + sysTenant.getDomainName() + CoreSval.getFrontPath() + "/login";
//			posturl = "http://" + sysTenant.getDomainName() + ":8080" + CoreSval.getFrontPath() + "/login";
		}else if(LgType.adminFilter(lgType)){
			rstatus.getDatas().getUsvo().setLoginType(MyUsernamePasswordToken.LoginType.PWD.getKey());
			rstatus.getDatas().getUsvo().setLgType(lgType.getKey());
			posturl = postLogin(false, sysTenant, CoreSval.getAdminPath(), "/login");
//			posturl = "http://" + sysTenant.getDomainName() + CoreSval.getAdminPath() + "/login";
//			posturl = "http://" + sysTenant.getDomainName() + ":8080" + CoreSval.getAdminPath() + "/login";
		}

		if(StringUtil.isEmpty(posturl)){
			rstatus.setStatus(false);
			rstatus.setMsg("登录名不存在");
			rstatus.getDatas().setUrl(CoreSval.REDIRECT+ Retype.ALL_LOGIN_FAIL.getUrl());
			return rstatus;
		}
		rstatus.getDatas().setReferer(posturl);
		rstatus.getDatas().setType(Urvo.Urtype.HTML.getKey());
		return rstatus;
	}

	public String postLogin(boolean hasprot, SysTenant sysTenant, String prefix, String loginUrl) {
		StringBuffer buffer = new StringBuffer(UrlUtil.HTTP + UrlUtil.HTTPLINE);
		buffer.append(sysTenant.getDomainName());
		if(hasprot){
			buffer.append("8080");
		}
		buffer.append(prefix);
		buffer.append(loginUrl);
		return buffer.toString();
	}

	/**
	 * 全局登录失败页面.
	 * @return String
	 */
	@RequestMapping(value="/loginFail")
	public String toAllFail() {
		return CorePages.ALL_LOGIN_FAIL.getIdxUrl();
	}

	/**
	 * 获取当前租户.
	 * @param loginName 登录名
	 * @param id 用户ID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTenantByLoginName")
	public ApiTstatus<SysTenant> getTenantByLoginName(Usvo usvo) {
		ApiTstatus<SysTenant> rstatus = new ApiTstatus<>();
		boolean sss = coreService.checkLoginNameOnlyOne(usvo.getLoginName(), usvo.getId());
		if(!sss){
			rstatus.setStatus(false);
			rstatus.setMsg("登录名不存在或已被使用");
			return rstatus;
		}

		SysTenant sysTenant = sysTenantService.getByLoginName(usvo.getLoginName(), usvo.getId());
		if (sysTenant == null) {
			rstatus.setStatus(false);
			rstatus.setMsg("租户不存在或不唯一");
			return rstatus;
		}

		if (SysTenant.checkYKH(sysTenant)) {
			rstatus.setStatus(false);
			rstatus.setMsg("当前用户暂停使用（未开户或暂停使用）");
			return rstatus;
		}

		if (StringUtil.isEmpty(sysTenant.getDomainName())) {
			rstatus.setStatus(false);
			rstatus.setMsg("当前用户域名未定义");
			return rstatus;
		}

		User user = coreService.getUserByLoginNameNt(usvo.getLoginName());
		if(user == null){
			rstatus.setStatus(false);
			rstatus.setMsg("当前用户不存在");
			return rstatus;
		}

		if (!CoreUtils.validatePassword(usvo.getPassword(), user.getPassword())) {
			rstatus.setStatus(false);
			rstatus.setMsg("密码错误");
			return rstatus;
		}

		if(StringUtil.isEmpty(usvo.getLgType())){
			rstatus.setStatus(false);
			rstatus.setMsg("登录类型不能为空");
			return rstatus;
		}

		LgType lgType = LgType.getByKey(usvo.getLgType());
		if((lgType == null)){
			rstatus.setStatus(false);
			rstatus.setMsg("登录类型不存在");
			return rstatus;
		}

		if(!lgType.isEnable()){
			rstatus.setStatus(false);
			rstatus.setMsg("登录类型被禁用");
			return rstatus;
		}

		boolean hasRole = false;
		List<String> lgs = LgType.rtypesToList(lgType);
		if(StringUtil.checkEmpty(lgs)){
			hasRole = true;
		}

		if((!hasRole) && StringUtil.checkNotEmpty(user.getRoleList())){
			for (Role r: user.getRoleList()) {
				if(lgs.contains(r.getRtype())){
					hasRole = true;
					break;
				}
			}
		}

		if(!hasRole){
			rstatus.setStatus(false);
			rstatus.setMsg("该用户不存在或登录类型不正确");
			return rstatus;
		}

		rstatus.setMsg("查询成功");
		rstatus.setDatas(sysTenant);
		return rstatus;
	}

	/**
	 * 获取统一登录页面的登录类型.
	 * @return LgType
	 */
	@ResponseBody
	@RequestMapping(value = "/ajaxLgTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public ApiTstatus<String> ajaxLgTypes() {
		return new ApiTstatus<String>(true, "查询成功！", (LgType.getAll().toString()));
	}

	/**
	 * 校验登录名唯一性
	 * @param loginName
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/checkLoginNameUnique")
	public Boolean checkLoginNameUnique(String loginName, String userId) {
		return coreService.checkLoginNameUnique(loginName, userId);
	}
}
