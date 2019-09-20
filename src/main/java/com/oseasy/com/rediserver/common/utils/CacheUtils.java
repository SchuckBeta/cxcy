/**
 *
 */
package com.oseasy.com.rediserver.common.utils;

import java.util.Iterator;
import java.util.Set;

import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;

/**
 * Cache工具类


 */
public class CacheUtils {

	private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);
	private static CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);
	public static final String GcontestImpFile_CACHE = "GcontestImpFileCache";
	public static final String DECLARENOTIFY_VIEWS_QUEUE= "declarenotify_viewsQueue";//申报通知浏览队列
	public static final String USER_LIKES_QUEUE= "user_likesQueue";//导师、学生详情点赞队列
	public static final String USER_VIEWS_QUEUE= "user_viewsQueue";//导师、学生详情浏览队列
	public static final String DYNAMIC_VIEWS_QUEUE= "dynamic_viewsQueue";//双创动态、双创通知、省市动态通知浏览队列
	public static final String GCONTESTHOTS_VIEWS_QUEUE= "gcontesthots_viewsQueue";//大赛热点浏览队列
	public static final String COMMENT_LIKES_QUEUE= "comment_likesQueue";//评论点赞队列
	public static final String EXCELLENT_VIEWS_QUEUE= "excellent_viewsQueue";//优秀展示浏览队列
	public static final String EXCELLENT_LIKES_QUEUE= "excellent_likesQueue";//优秀展示点赞队列
	public static final String EXCELLENT_COMMENT_QUEUE= "excellent_commentQueue";//优秀展示评论队列
	public static final String COURSE_VIEWS_QUEUE= "course_viewsQueue";//名师讲堂浏览队列
	public static final String COURSE_LIKES_QUEUE= "course_likesQueue";//名师讲堂点赞队列
	public static final String COURSE_COMMENT_QUEUE= "course_commentQueue";//名师讲堂评论队列
	public static final String IMPDATA_CACHE = "impdataCache";
	public static final String EXP_STATUS_CACHE = "expStatusCache";
	public static final String EXP_NUM_CACHE = "expNumCache";
	public static final String EXP_INFO_CACHE = "expInfoCache";
	public static final String SYS_CACHE = "sysCache";
	public static final String CERTMAKE_INFO_CACHE = "certmakeInfoCache";//证书下发进度信息
	/**
	 * 获取SYS_CACHE缓存
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	/**
	 * 获取SYS_CACHE缓存
	 * @param key
	 * @return
	 */
	public static String getTenant() {
		return (String)get(CoreUtils.CACHE_TENANT);
	}

	/**
	 * 获取SYS_CACHE缓存
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object get(String key, Object defaultValue) {
		Object value = get(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 写入SYS_CACHE缓存
	 * @param key
	 * @return
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	/**
	 * 从SYS_CACHE缓存中移除
	 * @param key
	 * @return
	 */
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}

	/**
	 * 获取缓存
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		return getCache(cacheName).get(getKey(key));
	}

	/**
	 * 获取缓存
	 * @param cacheName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object get(String cacheName, String key, Object defaultValue) {
		Object value = get(cacheName, getKey(key));
		return value != null ? value : defaultValue;
	}

	/**
	 * 写入缓存
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void put(String cacheName, String key, Object value) {
        logger.info("put:当前缓存Key["+cacheName+"]["+key+"]");
		getCache(cacheName).put(getKey(key), value);
	}

	/**
	 * 从缓存中移除
	 * @param cacheName
	 * @param key
	 */
	public static void remove(String cacheName, String key) {
	    logger.info("remove:当前缓存Key["+cacheName+"]["+key+"]");
		if(key!=null){
			getCache(cacheName).remove(getKey(key));
		}
	}

	/**
	 * 从缓存中移除所有
	 * @param cacheName
	 */
	public static void removeAll(String cacheName) {
		Cache<String, Object> cache = getCache(cacheName);
		Set<String> keys = cache.keys();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			cache.remove(it.next());
		}
		logger.info("清理缓存： {} => {}", cacheName, keys);
	}

	/**
	 * 获取缓存键名，多数据源下增加数据源名称前缀
	 * @param key
	 * @return
	 */
	private static String getKey(String key) {
//		String dsName = DataSourceHolder.getDataSourceName();
//		if (StringUtils.isNotBlank(dsName)) {
//			return dsName + "_" + key;
//		}
		return key;
	}

	/**
	 * 获得一个Cache，没有则显示日志。
	 * @param cacheName
	 * @return
	 */
	public static Cache<String, Object> getCache(String cacheName) {
		Cache<String, Object> cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new RuntimeException("当前系统中没有定义“"+cacheName+"”这个缓存。");
		}
		return cache;
	}

	/**
	 * 设置过期时间
	 * @param cacheName
	 * @param timeoutSeconds
	 */
	public static void expire(String cacheName, int timeoutSeconds) {
	    logger.info("expire:当前缓存Key["+cacheName+"]["+cacheName+"]["+timeoutSeconds+"]");
		getCache(cacheName).expire(cacheName, timeoutSeconds);
	}

	/**
	 * 设置过期时间
	 * @param cacheName 缓存名
	 * @param keyName 缓存Key
	 * @param timeoutSeconds
	 */
	public static void expire(String cacheName, String keyName, int timeoutSeconds) {
        logger.info("expire:当前缓存Key["+cacheName+"]["+keyName+"]["+timeoutSeconds+"]");
	    getCache(cacheName).expire(keyName, timeoutSeconds);
	}
	/**
	 * 存储REDIS队列 顺序存储
	 * @param cacheName
	 * @param value
	 */
	public static void lpush( String cacheName, Object value) {
		getCache(cacheName).lpush(value);
	}
	/**
	 * 存储REDIS队列 反向存储
	 * @param cacheName
	 * @param value
	 */
	public static void rpush( String cacheName, Object value) {
		getCache(cacheName).rpush(value);
	}
	/**
	 * 将列表 cacheName 中的最后一个元素(尾元素)弹，并返回给客户端
	 * @param cacheName
	 * @return
	 */
	public static Object rpop( String cacheName) {
		return getCache(cacheName).rpop();
	}
	/**
	 * 将列表 cacheKeyName 中的最后一个元素(尾元素)弹出，并塞入另一个列表otherCacheName中，并返回给客户端
	 * @param cacheName
	 * @param otherCacheName
	 * @return
	 */
	public static Object rpoplpush(String cacheName,String otherCacheName) {
		return getCache(cacheName).rpoplpush(otherCacheName);
	}
}
