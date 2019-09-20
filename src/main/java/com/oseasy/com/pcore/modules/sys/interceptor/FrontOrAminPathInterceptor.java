/**
 * 
 */
package com.oseasy.com.pcore.modules.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.service.BaseService;

/**
 *前后台区分拦截器

 * @version 2014-8-19
 */
public class FrontOrAminPathInterceptor extends BaseService implements HandlerInterceptor {
	private static final String FrontOrAdmin="frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String url = request.getRequestURI();
		if(url!=null&&(url.endsWith("/a")||url.indexOf("/a/") > -1)){
			request.setAttribute(FrontOrAdmin, CoreSval.getAdminPath());
		}
		if(url!=null&&(url.endsWith("/f")||url.indexOf("/f/") > -1||"/".equals(url))){
			request.setAttribute(FrontOrAdmin, CoreSval.getFrontPath());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}





}
