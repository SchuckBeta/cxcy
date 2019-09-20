package com.oseasy.aop.common.config;

/**
 * @author: QM
 * @date: 2019/3/24 17:25
 * @description: 缓存常量配置
 */
public class CacheConstant {
    public static final String CACHE_ENABLED = "cache.enabled";

    public static final Boolean ENABLE = true;

    public static final String CACHE_TYPE = "cache.type";

    public static final String CACHE_TYPE_REDIS = "redisCache";

    public static final Boolean CACHE_AOP_EXCEPTION_IGNORE = true;

    public static final Long CACHE_EXPIRE = 600000L;

    public static final String CACHE_SCAN_PACKAGES = "com.oseasy";


    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_GBK = "GBK";
    public static final String ENCODING_ISO_8859_1 = "ISO-8859-1";

    public static final String PREFIX = "oseasy";

    public static final Boolean LOG_PRINT = true;
}