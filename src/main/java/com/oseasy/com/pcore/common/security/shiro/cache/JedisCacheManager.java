/**
 * 
 */
package com.oseasy.com.pcore.common.security.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.oseasy.com.rediserver.common.utils.JedisUtils;

/**
 * 自定义授权缓存管理类

 * @version 2014-7-20
 */
public class JedisCacheManager implements CacheManager {

	private String cacheKeyPrefix = "shiro_cache_";
	
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new JedisCache<K, V>(cacheKeyPrefix + name);
	}

	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}

	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}
	
	/**
	 * 自定义授权缓存管理类

	 * @version 2014-7-20
	 */
	public class JedisCache<K, V> implements Cache<K, V> {

		private Logger logger = LoggerFactory.getLogger(getClass());

		private String cacheKeyName = null;

		public JedisCache(String cacheKeyName) {
			this.cacheKeyName = cacheKeyName;
//			if (!JedisUtils.exists(cacheKeyName)) {
//				Map<String, Object> map = Maps.newHashMap();
//				JedisUtils.setObjectMap(cacheKeyName, map, 60 * 60 * 24);
//			}
//			logger.debug("Init: cacheKeyName {} ", cacheKeyName);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public V get(K key) throws CacheException {
			if (key == null) {
				return null;
			}

			V value = null;
			try {
				value = (V)JedisUtils.toObject(JedisUtils.hget(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key)));

			} catch (Exception e) {
				logger.error("get {} {} {}", cacheKeyName, key, "", e);
			}

			return value;
		}

		@Override
		public void expire(K key, int timeoutSeconds) {
			if (key == null) {
				return;
			}
			try {
				JedisUtils.expire(JedisUtils.getBytesKey(cacheKeyName), timeoutSeconds);
			} catch (Exception e) {
				logger.error("get {} {} {}", cacheKeyName, key, "", e);
			}
		}

		@Override
		public V put(K key, V value) throws CacheException {
			if (key == null) {
				return null;
			}
			try {
				JedisUtils.hset(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key), JedisUtils.toBytes(value));
				logger.debug("put {} {} = {}", cacheKeyName, key, value);
			} catch (Exception e) {
				logger.error("put {} {}", cacheKeyName, key, e);
			} 
			return value;
		}

		@SuppressWarnings("unchecked")
		@Override
		public V remove(K key) throws CacheException {
			V value = null;
			try {
				value = (V)JedisUtils.toObject(JedisUtils.hget(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key)));
				JedisUtils.hdel(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key));
				logger.debug("remove {} {}", cacheKeyName, key);
			} catch (Exception e) {
				logger.warn("remove {} {}", cacheKeyName, key, e);
			}
			return value;
		}

		@Override
		public void clear() throws CacheException {
			try {
				JedisUtils.hdel(JedisUtils.getBytesKey(cacheKeyName));
				logger.debug("clear {}", cacheKeyName);
			} catch (Exception e) {
				logger.error("clear {}", cacheKeyName, e);
			} 
		}

		@Override
		public int size() {
			int size = 0;
			try {
				size = JedisUtils.hlen(JedisUtils.getBytesKey(cacheKeyName)).intValue();
				logger.debug("size {} {} ", cacheKeyName, size);
				return size;
			} catch (Exception e) {
				logger.error("clear {}",  cacheKeyName, e);
			} 
			return size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<K> keys() {
			Set<K> keys = Sets.newHashSet();
			try {
				Set<byte[]> set = JedisUtils.hkeys(JedisUtils.getBytesKey(cacheKeyName));
				for(byte[] key : set) {
					Object obj = (K)JedisUtils.getObjectKey(key);
					if (obj != null) {
						keys.add((K) obj);
					}
	        	}
				logger.debug("keys {} {} ", cacheKeyName, keys);
				return keys;
			} catch (Exception e) {
				logger.error("keys {}", cacheKeyName, e);
			}
			return keys;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Collection<V> values() {
			Collection<V> vals = new ArrayList<V>();
			try {
				Collection<byte[]> col = JedisUtils.hvals(JedisUtils.getBytesKey(cacheKeyName));
				for(byte[] val : col) {
					Object obj = JedisUtils.toObject(val);
					if (obj != null) {
						vals.add((V) obj);
					}
	        	}
				logger.debug("values {} {} ", cacheKeyName, vals);
				return vals;
			} catch (Exception e) {
				logger.error("values {}",  cacheKeyName, e);
			} 
			return vals;
		}
		/**
		 * 存储REDIS队列 顺序存储
		 * @param byte[] key reids键名
		 * @param byte[] value 键值
		 */
		public V  lpush( V value) throws CacheException{
			if (cacheKeyName == null) {
				return null;
			}
			
			try {
				JedisUtils.lpush(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.toBytes(value));
				logger.debug("lpush {} {} = {}",  cacheKeyName, value);
			} catch (Exception e) {
				logger.error("lpush {} {}",  cacheKeyName, e);
			} 
			return value;
		}
		
		/**
		 * 存储REDIS队列 反向存储
		 * @param byte[] key reids键名
		 * @param byte[] value 键值
		 */
		public  V rpush(V value) throws CacheException{
			if (cacheKeyName == null) {
				return null;
			}
			
			try {
				JedisUtils.rpush(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.toBytes(value));
				logger.debug("rpush {} {} = {}",  cacheKeyName, value);
			} catch (Exception e) {
				logger.error("rpush {} {}",  cacheKeyName, e);
			} 
			return value;
		}
		/**
		 * 将列表 cacheKeyName 中的最后一个元素(尾元素)弹出，并塞入另一个列表key中，并返回给客户端
		 * @param byte[] key reids键名
		 * @param byte[] value 键值
		 */
		@SuppressWarnings("unchecked")
		public V  rpoplpush(K key) throws CacheException{
			
			if (key == null) {
				return null;
			}
			
			V value=null;
			try {
				value=(V)JedisUtils.toObject(JedisUtils.rpoplpush(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.toBytes(key)));
				logger.debug("rpoplpush {} {} = {}",  cacheKeyName, key);
			} catch (Exception e) {
				logger.error("rpoplpush {} {}",  cacheKeyName,key, e);
			} 
			return value;
		}
		/**
		 * 将列表 key 中的最后一个元素(尾元素)弹，并返回给客户端
		 * @param byte[] key reids键名
		 */
		@SuppressWarnings("unchecked")
		public V  rpop() throws CacheException{
			
			if (cacheKeyName == null) {
				return null;
			}
			
			V value=null;
			try {
				value=(V)JedisUtils.toObject(JedisUtils.rpop(JedisUtils.getBytesKey(cacheKeyName)));
				logger.debug("rpop {} {} = {}",  cacheKeyName);
			} catch (Exception e) {
				logger.error("rpop {} {}",  cacheKeyName, e);
			}
			return value;
		}
	}
}
