/**
 *
 */
package com.oseasy.com.pcore.modules.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.annotation.Annotation;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.annotation.CheckToken;
import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.oseasy.util.common.utils.StringUtil;

/**
 *前后台区分拦截器

 * @version 2014-8-19
 */
public class TokenInterceptor extends BaseService implements HandlerInterceptor {
	private static final String FrontOrAdmin="frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		String index="";
		if(url!=null&&(url.endsWith("/a")||url.indexOf("/a/") > -1)){
			index="/a";
		}
		if(url!=null&&(url.endsWith("/f")||url.indexOf("/f/") > -1||"/".equals(url))){
			index="/f";
		}
		if(StringUtil.isEmpty(UserUtils.getUser().getId())){
			response.sendRedirect(index+"/login");
			return false;
		}
		//有注解走token校验 没有就不走
		if (handler instanceof HandlerMethod) {
			CheckToken requestLimit = findAnnotation((HandlerMethod) handler, CheckToken.class);
			if(requestLimit == null) {
				return true;
			}else{
				if(requestLimit.value().equals(Const.NO)){
					return true;
				}
			}
		}

		String[] keys=new String[]{
			"save","update","edit","del","change","view","list"
		};
		//校验token是否存在并且没有过期

		if(isContains(url,keys)){
			//通过url请求头得到token
			String token = request.getHeader("token");
			//检测token是否为空
			if(token==null) {
				response.sendRedirect(index+"/login");
				return false;
			}else {
				String sissionToken = (String) CoreUtils.getCache(CoreUtils.CACHE_TOKEN);
				if (sissionToken!=null && !sissionToken.equals(token)) {
					logger.info("token不一致");
					response.sendRedirect(index+"/login");
					return false;
				}
			}
		}
		return true;
	}
	private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
		T annotation = handler.getBeanType().getAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		return handler.getMethodAnnotation(annotationType);
	}

	public static boolean isContains(String container, String[] regx) {
		boolean result = false;
		for (int i = 0; i < regx.length; i++) {
			if (container.toUpperCase().indexOf(regx[i].toUpperCase()) != -1) {
				return true;
			}
		}
		return result;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}





}
