/**
 *
 */
package com.oseasy.com.pcore.modules.sys.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.modules.sys.enums.RedisEnum;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.util.common.utils.UrlUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.LogDao;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.entity.Log;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.Exceptions;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 字典工具类

 * @version 2014-11-7
 */
public class LogUtils {

	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

	private static LogDao logDao = SpringContextHolder.getBean(LogDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);

	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title) {
		saveLog(request, null, null, title);
	}

	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title) {
		User user = CoreUtils.getUser();
		if (user != null && user.getId() != null) {
			Log log = new Log();
			log.setTitle(title);
			log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
			log.setRemoteAddr(UrlUtil.getRemoteAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			log.setParams(request.getParameterMap());
			log.setMethod(request.getMethod());
			// 异步保存日志
			new SaveLogThread(log, handler, ex).start();
		}
	}

	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{

		private Log log;
		private Object handler;
		private Exception ex;

		public SaveLogThread(Log log, Object handler, Exception ex) {
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.handler = handler;
			this.ex = ex;
		}

		@Override
		public void run() {
			// 获取日志标题
			if (StringUtil.isBlank(log.getTitle())) {
				String permission = "";
				if (handler instanceof HandlerMethod) {
					Method m = ((HandlerMethod)handler).getMethod();
					RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
					permission = (rp != null ? StringUtil.join(rp.value(), ",") : "");
				}
				log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
			}
			// 如果有异常，设置异常信息
			log.setException(Exceptions.getStackTraceAsString(ex));
			// 如果无标题并无异常日志，则不保存信息
			if (StringUtil.isBlank(log.getTitle()) && StringUtil.isBlank(log.getException())) {
				return;
			}
			// 保存日志信息
			log.preInsert();
			logDao.insert(log);
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	public static String getMenuNamePath(String requestUri, String permission) {
		String href = StringUtil.substringAfter(requestUri, CoreSval.getAdminPath());
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)JedisUtils.hashGet(RedisEnum.MENU.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null) {
			menuMap = Maps.newHashMap();
			List<Menu> menuList = menuDao.findAllList(new Menu());
			for (Menu menu : menuList) {
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null) {
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtil.split(menu.getParentIds(), ",")) {
						if (Menu.getRootId().equals(id)) {
							continue; // 过滤跟节点
						}
						for (Menu m : menuList) {
							if (m.getId().equals(id)) {
								namePathList.add(m.getName());
								break;
							}
						}
					}
					namePathList.add(menu.getName());
					namePath = StringUtil.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtil.isNotBlank(menu.getHref())) {
					menuMap.put(menu.getHref(), namePath);
				}else if (StringUtil.isNotBlank(menu.getPermission())) {
					for (String p : StringUtil.split(menu.getPermission())) {
						menuMap.put(p, namePath);
					}
				}
			}
			JedisUtils.hashSetKey(RedisEnum.MENU.getValue()+StringUtil.MAOH+ TenantConfig.getCacheTenant(),CACHE_MENU_NAME_PATH_MAP,menuMap);
		}
		String menuNamePath = menuMap.get(href);
		if (menuNamePath == null) {
			for (String p : StringUtil.split(permission)) {
				menuNamePath = menuMap.get(p);
				if (StringUtil.isNotBlank(menuNamePath)) {
					break;
				}
			}
			if (menuNamePath == null) {
				return "";
			}
		}
		return menuNamePath;
	}


}
