package com.oseasy.com.pcore.modules.sys.utils;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: QM
 * @date: 2019/5/23 18:42
 * @description:
 */
public class DomainUtils {
	/**
	 * 获取域名
	 * @param request
	 * @return
	 */
	public static String getDomain(HttpServletRequest request){
		return request.getServerName();
	}

}
