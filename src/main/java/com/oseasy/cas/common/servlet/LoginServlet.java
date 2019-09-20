package com.oseasy.cas.common.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 * 生成随机验证码

 * @version 2014-7-27
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	public LoginServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	        String uid = request.getRemoteUser();
	      String cn = "";
	      Principal principal = request.getUserPrincipal();
	      if(principal!=null && principal instanceof AttributePrincipal){
	          AttributePrincipal aPrincipal = (AttributePrincipal)principal;
	          //获取用户信息中公开的Attributes部分
	          Map<String, Object> map = aPrincipal.getAttributes();
	          // 获取姓名,可以根据属性名称获取其他属性
	          cn = (String)map.get("cn");
	       }
	      response.getWriter().append("Served at: ").append(request.getContextPath());
	}
}