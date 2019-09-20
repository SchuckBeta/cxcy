package com.oseasy.com.pcore.modules.sys.interceptor;

import com.oseasy.com.pcore.common.service.BaseService;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.sys.vo.TenantCvtype;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by PW on 2019/4/10.
 */
public class TenantInterceptor extends BaseService implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String www = TenantConfig.getCache(TenantCvtype.WWW);
        if(StringUtil.isEmpty(www)){
            TenantConfig config = TenantConfig.getConfig();
            config.setRequest(request);
            config.setResponse(response);
            TenantConfig.initCache(config, TenantCvtype.WWW);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
