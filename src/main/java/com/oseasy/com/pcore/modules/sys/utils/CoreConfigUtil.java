package com.oseasy.com.pcore.modules.sys.utils;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.SysConfigDao;
import com.oseasy.com.rediserver.common.utils.CacheUtils;

public class CoreConfigUtil {
	public final static Logger logger = Logger.getLogger(CoreConfigUtil.class);
	public static SysConfigDao sysConfigDao = SpringContextHolder.getBean(SysConfigDao.class);
	public static final String SYS_CONFIG_VO = "SysConfig";

    public static void removeCache() {
        CacheUtils.remove(SYS_CONFIG_VO);
    }
}
