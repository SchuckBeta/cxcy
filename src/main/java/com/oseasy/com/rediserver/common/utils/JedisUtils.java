/**
 *
 */
package com.oseasy.com.rediserver.common.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.util.common.utils.ObjectUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Jedis Cache 工具类
 *

 * @version 2014-6-29
 */
@SuppressWarnings("unchecked")
public class JedisUtils {
	@SuppressWarnings("rawtypes")
	private static RedisTemplate redisTemplate= SpringContextHolder.getBean(RedisTemplate.class);
	/**发布消息给订阅者
	 * @param channel
	 * @param message
	 */
	public static void  publishMsg(String channel, Object message) {
		redisTemplate.convertAndSend(channel, message);
	}
	/**设置超时时间
	 * @param cacheName
	 * @param timeoutSeconds
	 * @return
	 */
	public static Long expire(String cacheName,int timeoutSeconds) {
		redisTemplate.expire(cacheName, timeoutSeconds, TimeUnit.SECONDS);
		return  redisTemplate.getExpire(cacheName);
	}

	/**设置超时时间
	 * @param cacheName
	 * @param timeoutSeconds
	 * @return
	 */
	public static Long expire(byte[] cacheName,int timeoutSeconds) {
		redisTemplate.expire(cacheName, timeoutSeconds, TimeUnit.SECONDS);
		return  redisTemplate.getExpire(cacheName);
	}
	/**存储string类型数据
	 * @param cacheName
	 * @param value
	 */
	public static void set(byte[] cacheName,byte[] value) {
		redisTemplate.opsForValue().set(cacheName, value);
	}


	/**存储string类型数据
	 * @param cacheName
	 * @param value
	 */
	public static void setObject(String cacheName,Object value) {
		redisTemplate.opsForValue().set(cacheName, value);
	}


	//hash 得到所有key
	public static List<String> hashGetKeys() {
		RedisOperations list=redisTemplate.opsForHash().getOperations();
		Set<String> list2=redisTemplate.keys("*");
		return redisTemplate.opsForHash().values("*");
	}
	//hash 存储缓存数据
	public static boolean hashSetKey(String cacheName,String key,Object value) {
	try {
			redisTemplate.opsForHash().put(cacheName, key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		return false;
		}
	}
	//hash 根据cache获得下面所有map
	public static Map<String,Object> hashGetKey(String cacheName) {
		return redisTemplate.opsForHash().entries(cacheName);
	}

	//hash 根据cache删除值 可以多个
	public static void hashDel(String cacheName, Object... item){
     	redisTemplate.opsForHash().delete(cacheName,item);
 	}

	public static Long hashDelete(String cacheName, Object... item){
		return redisTemplate.opsForHash().delete(cacheName,item);
	}
	//hash 根据cache删除cache的缓存
	public static void hashDelByKey(String cacheName){
		Map<Object,Object> map=redisTemplate.opsForHash().entries(cacheName);
		for(Object key:map.keySet()){
			redisTemplate.opsForHash().delete(cacheName,key);
		}
	}
	public static void hashDelByKey(String cacheName, String key){
		redisTemplate.opsForHash().delete(cacheName,key);
	}
	//hash 根据cache和key得到缓存具体值
	public static Object hashGet(String key,String item){
     	return redisTemplate.opsForHash().get(key, item);
 	}

	/**
	 * 删除key
	 * @param cacheName
	 */
	public static void delObject(String cacheName) {
		redisTemplate.delete(cacheName);
	}

	public static Object getObject(String cacheName) {
		return redisTemplate.opsForValue().get(cacheName);
	}
	/**存储hash类型数据
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void hset(String cacheName,String key,Object value) {
		redisTemplate.opsForHash().put(cacheName, key, value);
	}
	/**存储hash类型数据
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void hset(byte[] cacheName,byte[] key,byte[] value) {
		redisTemplate.opsForHash().put(cacheName, key, value);
	}
	/**获取hash类型数据
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static byte[] hget(byte[] cacheName,byte[] key) {
		return (byte[])redisTemplate.opsForHash().get(cacheName, key);
	}
	/**根据大小key删除hash类型数据
	 * @param cacheName
	 * @param key
	 */
	public static void hdel(byte[] cacheName,byte[] key) {
		redisTemplate.opsForHash().delete(cacheName, key);
	}
	/**根据大小key删除hash类型数据
	 * @param cacheName
	 * @param key
	 */
	public static void hdel(String cacheName,String key) {
		redisTemplate.opsForHash().delete(cacheName, key);
	}
	/**根据大key删除hash类型数据
	 * @param cacheName
	 */
	public static void hdel(byte[] cacheName) {
		redisTemplate.opsForHash().delete(cacheName);
	}
	/**查询某大key的hash类型数据数量
	 * @param cacheName
	 */
	public static Long hlen(byte[] cacheName) {
		return redisTemplate.opsForHash().size(cacheName);
	}
	/**查询某大key的hash类型数据所有小key
	 * @param cacheName
	 */
	public static Set<byte[]> hkeys(byte[] cacheName) {
		return redisTemplate.opsForHash().keys(cacheName);
	}
	/**查询某大key的hash类型数据所有值
	 * @param cacheName
	 */
	public static List<byte[]> hvals(byte[] cacheName) {
		return redisTemplate.opsForHash().values(cacheName);
	}
	/**在list类型数据首部塞入一个值
	 * @param cacheName
	 * @param value
	 * @return
	 */
	public static Long lpush(byte[] cacheName,byte[] value) {
		return redisTemplate.opsForList().leftPush(cacheName, value);
	}
	/**在list类型数据尾部塞入一个值
	 * @param cacheName
	 * @param value
	 * @return
	 */
	public static Long rpush(byte[] cacheName,byte[] value) {
		return redisTemplate.opsForList().rightPush(cacheName, value);
	}
	/**在list类型数据尾部弹出一个值并存入另一个list的首部，并返回该值
	 * @param cacheName
	 * @return
	 */
	public static byte[] rpoplpush(byte[] cacheName,byte[] othercacheName) {
		return (byte[])redisTemplate.opsForList().rightPopAndLeftPush(cacheName, othercacheName);
	}
	/**弹出list类型数据尾部一个值
	 * @param cacheName
	 * @return
	 */
	public static byte[] rpop(byte[] cacheName) {
		return (byte[])redisTemplate.opsForList().rightPop(cacheName);
	}
	/**取出hash类型数据某大key下所有键和值
	 * @param cacheName
	 * @return
	 */
	public static Map<String,String> hgetAll(String cacheName) {
		return redisTemplate.opsForHash().entries(cacheName);
	}

	/**获取string类型数据的值
	 * @param cacheName
	 * @return
	 */
	public static byte[] get(byte[] cacheName) {
		return (byte[])redisTemplate.opsForValue().get(cacheName);
	}
	/**删除一个值
	 * @param cacheName
	 */
	public static void del(byte[] cacheName) {
		redisTemplate.delete(cacheName);
	}

	/**
	 * 获取byte[]类型Key
	 * @return byte
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtil.getBytes((String)object);
		}else{
			return ObjectUtil.serialize(object);
		}
	}

	/**
	 * 获取byte[]类型Key
	 * @param key
	 * @return Object
	 */
	public static Object getObjectKey(byte[] key) {
		try{
			return StringUtil.toString(key);
		}catch(UnsupportedOperationException uoe) {
			try{
				return JedisUtils.toObject(key);
			}catch(UnsupportedOperationException uoe2) {
				uoe2.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Object转换byte[]类型
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtil.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtil.unserialize(bytes);
	}


	/**
	 * set数据类型添加
	 * @param k 键
	 * @param v 值
	 */
	public static Long storage(Object k,Object v){
		return redisTemplate.opsForSet().add(k,v);
	}

	/**
	 * 获取set集合成员
	 * @param k 键
	 * @return 集合成员
	 */
	public static Set<Object> getMembers(Object k){
		return redisTemplate.opsForSet().members(k);
	}

	/**
	 * 删除集合成员
	 * @param k 键
	 */
	public static void remove(Object k,Object... values){
		redisTemplate.opsForSet().remove(k,values);
	}

	/**
	 *
	 * @param k 健
	 * @return 返回集合元素个数
	 */
	public static Long getSetSize(Object k){
		return redisTemplate.opsForSet().size(k);
	}

	/**
	 * 尾部加入元素
	 * @param k k
	 * @param v v
	 * @return 集合
	 */
	public static Long rightPush(Object k,Object v){
		return redisTemplate.opsForList().rightPush(k,v);
	}

	/**
	 * 获取指定范围元素
	 * @param k k
	 * @param v1 起
	 * @param v2 止
	 * @return 集合
	 */
	public static List<Object> getRange(Object k, Long v1, Long v2){
		return redisTemplate.opsForList().range(k,v1,v2);
	}

	/**
	 * 截取集合
	 * @param k k
	 * @param v1 起
	 * @param v2 止
	 */
	public static void trim(Object k,Long v1,Long v2){
		redisTemplate.opsForList().trim(k,v1,v2);
	}

	/**
	 * 集合大小
	 * @param k k
	 * @return 集合大小
	 */
	public static Long getListSize(Object k){
		return redisTemplate.opsForList().size(k);
	}

	/**
	 * 判断k
	 * @param k
	 * @return
	 */
	public static Boolean hasKey(Object k){
		return redisTemplate.hasKey(k);
	}

	public static void  rightPushAll(Object k, Object v){
		redisTemplate.opsForList().rightPushAll(k,v);
	}

	public static void listRemove(Object k, Object v){
		redisTemplate.opsForList().remove(k,0L,v);
	}

	public static void listRightPush(Object k, Object v){
		redisTemplate.opsForList().rightPush(k,v);
	}

	public static List<String> listAll(Object k){
		return redisTemplate.opsForList().range(k,0L,-1L);
	}

	public static void  main(String[] args){
		List<String> set=redisTemplate.opsForHash().values("*");
		System.out.print(set);
	}
}
