/**
 *
 */
package com.oseasy.cms.modules.cms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.ICorePn;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.cms.modules.cms.entity.Site;
import com.oseasy.cms.modules.cms.service.SiteService;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.security.shiro.session.SessionDAO;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.util.common.utils.CookieUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class CmsLoginController extends BaseController{
    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private SiteService siteService;

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "${adminPath}")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        Principal principal = CoreUtils.getPrincipal();

        if (logger.isDebugEnabled()) {
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }
        String token=(String)CoreUtils.getSession().getAttribute("token");
        if(StringUtil.isNotEmpty(token)){
            CookieUtils.setCookie(response, "token", token);
        }


        // 如果已登录，再次访问主页，则退出原账号。
        if (Const.TRUE.equals(CoreSval.getConfig("notAllowRefreshIndex"))) {
            String logined = CookieUtils.getCookie(request, "LOGINED");
            if (StringUtil.isBlank(logined) || "false".equals(logined)) {
                CookieUtils.setCookie(response, "LOGINED", "true");
            }else if (StringUtil.equals(logined, "true")) {
//                CoreUtils.getSubject().logout();
                CoreUtils.logout(CoreUtils.getSubject(), request, response);
                return CoreSval.REDIRECT + adminPath + "/login";
            }
        }

        // 如果是手机登录，则返回JSON字符串
        if (principal.isMobileLogin()) {
            if (request.getParameter("login") != null) {
                return renderString(response, principal);
            }
            if (request.getParameter("index") != null) {
                //return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysMenuIndex";
                return ICorePn.curPt().loginSuccessPage(Sval.EmPt.TM_ADMIN);
            }
            return CoreSval.REDIRECT + adminPath + "/login";
        }

//      // 登录成功后，获取当前站点ID
        Site site=siteService.getAutoSite();
        if(site!=null){
            CoreUtils.putCache("siteId", String.valueOf(site.getId()));
        }
        //return CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysMenuIndex";
        return ICorePn.curPt().loginSuccessPage(Sval.EmPt.TM_ADMIN);
    }
}
