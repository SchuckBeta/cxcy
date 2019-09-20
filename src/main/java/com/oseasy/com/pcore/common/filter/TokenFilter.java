/**
 *
 */
package com.oseasy.com.pcore.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CoreSval;

import java.io.IOException;

/**

 * @version 2013-8-5
 */
public class TokenFilter implements Filter  {

	public FilterConfig config;

	public static final String FILTERFB = CoreSval.getConfig("filter.frontOrBackground");
	public static final String disabletestfilter = CoreSval.getConfig("filter.disabletestfilter");

    public void destroy() {
        this.config = null;
    }

    public static boolean isContains(String container, String[] regx) {
        boolean result = false;
        for (int i = 0; i < regx.length; i++) {
            if (container.indexOf(regx[i]) != -1) {
                return true;
            }
        }
        return result;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hrequest = (HttpServletRequest)request;
        HttpServletResponse hresponse = (HttpServletResponse)response;

        //前台过滤
        String url=hrequest.getRequestURI();
		String[] keys=new String[]{"save","update","edit"};
		//校验token是否存在并且没有过期
		if(isContains(url,keys)){

		}


        if (FILTERFB.equals("1")) {
        	if (url.endsWith("/a")||url.indexOf("/a/") > -1) {// 只对指定过滤参数后缀进行过滤
        		hresponse.sendRedirect("/f/");
            }else{
            	chain.doFilter(hrequest, hresponse);
            }
        }else if (FILTERFB.equals("2")) {
        	if (url.endsWith("/f")||url.indexOf("/f/") > -1 ||url.equals("/") ) {// 只对指定过滤参数后缀进行过滤
        		hresponse.sendRedirect("/a/login");
        	}else{
        		chain.doFilter(hrequest, hresponse);
           }
        }else{
        	chain.doFilter(hrequest, hresponse);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
    }


}
