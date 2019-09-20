package com.oseasy.com.pcore.common.web;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.exception.ApiException;
import com.oseasy.com.pcore.common.exception.PageException;
import com.oseasy.com.pcore.common.exception.TenantException;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.UrlUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常处理器.
 * @author chenhao
 */
@Controller
@ControllerAdvice
public class ExceptionController{
	private Logger logger = LoggerFactory.getLogger(getClass());

	@ResponseBody
	@ExceptionHandler(ApiException.class)
	public ApiTstatus handleApiException(ApiException e) {
		logger.error(e.getMessage(), e);
		return ApiTstatus.error(e.getMessage());
	}

	@ExceptionHandler(PageException.class)
	public ModelAndView handlePageException(HttpServletRequest request, HttpServletResponse response, PageException e) {
		return showException(request, response, e, ApiTstatus.error(e.getMessage()));
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ModelAndView handleDuplicateKeyException(HttpServletRequest request, HttpServletResponse response, DuplicateKeyException e) {
		return showException(request, response, e, ApiTstatus.error("数据库中已存在该记录，请联系管理员授权"));
	}

	@ExceptionHandler(NullPointerException.class)
	public ModelAndView handleNullPointException(HttpServletRequest request, HttpServletResponse response, NullPointerException e) {
		return showException(request, response, e, ApiTstatus.error("数据异常，请联系管理员授权"));
	}

	@ExceptionHandler(AuthorizationException.class)
	public ModelAndView handleAuthorizationException(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) {
		return showException(request, response, e, ApiTstatus.error("平台权限认证失败，请联系管理员授权"));
	}

	@ExceptionHandler(value = TenantException.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		return showException(request, response, e, ApiTstatus.error("租户未授权，请联系管理员"));
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView MethodArgumentNotValidHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		return showException(request, response, e, ApiTstatus.error("没有权限，请联系管理员授权"));
	}

	/**
	 *显示异常信息.
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @param e Exception
	 * @param r ApiTstatus
     * @return ModelAndView
     */
	public ModelAndView showException(HttpServletRequest request, HttpServletResponse response, Exception e, ApiTstatus r) {
		// 记录异常日志
		logger.error(e.getMessage(), e);

		if(r == null){
			r = ApiTstatus.error("没有权限，请联系管理员授权");
		}

		if (UrlUtil.isAjax(request)) {
			// JSON格式返回
			response.setCharacterEncoding(StringUtil.UTF_8);
			response.setContentType("application/json; charset=utf-8");
			try {
				response.getWriter().write(JSONObject.fromObject(r).toString());
				response.getWriter().flush();
				return null;
			} catch (IOException ie) {
				logger.error("IOException 捕获的异常：", ie);
			}
		}
		return new ModelAndView(CorePages.ERROR_MSG.getIdxUrl()).addObject(CoreJkey.JK_MSG, r.getMsg()).addObject(r);
	}
}