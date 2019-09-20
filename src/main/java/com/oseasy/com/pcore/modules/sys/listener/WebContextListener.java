package com.oseasy.com.pcore.modules.sys.listener;

import java.net.URISyntaxException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.WebApplicationContext;

import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.util.common.utils.FileUtil;

public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {
    private static final String WEB_INF = "WEB-INF";
    private static final String WEBROOT = "webroot";

    @Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		if (!CoreService.printKeyLoadMessage()) {
			return null;
		}
		return super.initWebApplicationContext(servletContext);
	}

	@Override
    public void contextInitialized(ServletContextEvent event) {
        String log4jPath = "";
        try {
            log4jPath = this.getClass().getResource(FileUtil.LINE).toURI().getPath();
            log4jPath = log4jPath.substring(0, log4jPath.indexOf(WEB_INF)) + WEB_INF;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.setProperty(WEBROOT, log4jPath);
        super.contextInitialized(event);
    }

    @Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}

	@Override
	public void closeWebApplicationContext(ServletContext arg0) {
		super.closeWebApplicationContext(arg0);
	}

}
