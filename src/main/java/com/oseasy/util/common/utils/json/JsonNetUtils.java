/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.util.common.utils.json
 * @Description [[_JsonNUtils_]]文件
 * @date 2017年6月2日 上午10:21:05
 *
 */

package com.oseasy.util.common.utils.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * net.sf.json.*.
 *
 * @Description JSON工具类
 * @author chenhao
 * @date 2017年6月2日 上午10:21:05
 *
 */
public class JsonNetUtils {
  protected static final Logger LOGGER = Logger.getLogger(JsonNetUtils.class);
  /**
   * 将List对象序列化为JSON文本.
   * @param list 列表对象
   * @return String
   */
  public static <T> String toJsonString(List<T> list) {
    JSONArray jsonArray = JSONArray.fromObject(list);
    return jsonArray.toString();
  }

  /**
   * 将对象序列化为JSON文本.
   *
   * @param object Json对象
   * @return String
   */
  public static String toJsonString(Object object) {
    JSONArray jsonArray = JSONArray.fromObject(object);
    return jsonArray.toString();
  }

  /**
   * 将JSON对象数组序列化为JSON文本.
   *
   * @param jsonArray Json数组对象
   * @return String
   */
  public static String toJsonString(JSONArray jsonArray) {
    return jsonArray.toString();
  }

  /**
   * 将JSON对象序列化为JSON文本.
   *
   * @param jsonObject Json对象
   * @return String
   */
  public static String toJsonString(JSONObject jsonObject) {
    return jsonObject.toString();
  }

  /**
   * 将对象转换为List对象.
   *
   * @param object 对象
   * @return List
   */
  public static List<Object> toArrayList(Object object) {
    List<Object> arrayList = new ArrayList<Object>();

    JSONArray jsonArray = JSONArray.fromObject(object);

    Iterator<?> it = jsonArray.iterator();
    while (it.hasNext()) {
      JSONObject jsonObject = (JSONObject) it.next();

      Iterator<?> keys = jsonObject.keys();
      while (keys.hasNext()) {
        Object key = keys.next();
        Object value = jsonObject.get(key);
        arrayList.add(value);
      }
    }

    return arrayList;
  }

  /**
   * 将对象转换为Collection对象.
   *
   * @param object 对象
   * @return Collection<?>
   */
  public static Collection<?> toCollection(Object object) {
    JSONArray jsonArray = JSONArray.fromObject(object);

    return JSONArray.toCollection(jsonArray);
  }

  /**
   * 将对象转换为JSON对象数组.
   *
   * @param object 对象
   * @return JSONArray
   */
  public static JSONArray toJsonArray(Object object) {
    return JSONArray.fromObject(object);
  }

  /**
   * 将对象转换为JSON对象.
   *
   * @param object 对象
   * @return JSONObject
   */
  public static JSONObject toJsonObject(Object object) {
    return JSONObject.fromObject(object);
  }

  /**
   * 将对象转换为HashMap.
   *
   * @param object 对象
   * @return Map
   */
  public static Map<String, Object> toHashMap(Object object) {
    Map<String, Object> data = new HashMap<String, Object>();
    JSONObject jsonObject = JsonNetUtils.toJsonObject(object);
    Iterator<?> it = jsonObject.keys();
    while (it.hasNext()) {
      String key = String.valueOf(it.next());
      Object value = jsonObject.get(key);
      data.put(key, value);
    }

    return data;
  }

  /**
   * 将对象转换为List.
   *
   * @param object 对象
   * @return List
   */
  // 返回非实体类型(Map)的List
  public static List<Map<String, Object>> toList(Object object) {
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    JSONArray jsonArray = JSONArray.fromObject(object);
    for (Object obj : jsonArray) {
      JSONObject jsonObject = (JSONObject) obj;
      Map<String, Object> map = new HashMap<String, Object>();
      Iterator<?> it = jsonObject.keys();
      while (it.hasNext()) {
        String key = (String) it.next();
        Object value = jsonObject.get(key);
        map.put((String) key, value);
      }
      list.add(map);
    }
    return list;
  }

  /**
   * 将JSON对象数组转换为传入类型的List.
   *
   * @param jsonArray json对象
   * @param objectClass 实体
   * @return T
   */
  @SuppressWarnings({ "unchecked" })
  public static <T> List<T> toList(JSONArray jsonArray, Class<T> objectClass) {
    return JSONArray.toList(jsonArray, objectClass);
  }

  /**
   * 将对象转换为传入类型的List.
   *
   * @param object 对象
   * @param objectClass 实体
   * @return Y
   */
  @SuppressWarnings({ "unchecked" })
  public static <T> List<T> toList(Object object, Class<T> objectClass) {
    JSONArray jsonArray = JSONArray.fromObject(object);
    return JSONArray.toList(jsonArray, objectClass);
  }

  /**
   * 将JSON对象转换为传入类型的对象.
   *
   * @param jsonObject JSON对象
   * @param beanClass 实体
   * @return T
   */
  @SuppressWarnings({ "unchecked" })
  public static <T> T toBean(JSONObject jsonObject, Class<T> beanClass) {
    return (T) JSONObject.toBean(jsonObject, beanClass);
  }

  /**
   * 将将对象转换为传入类型的对象.
   *
   * @param object 原对象
   * @param beanClass 实体
   * @return T
   */
  @SuppressWarnings({ "unchecked" })
  public static <T> T toBean(Object object, Class<T> beanClass) {
    JSONObject jsonObject = JSONObject.fromObject(object);

    return (T) JSONObject.toBean(jsonObject, beanClass);
  }

  /**
   * 将将对象转换为传入类型的对象列表.
   * @param object 原对象
   * @param beanClass 实体
   * @return List
   */
  public static <T> List<T> toBeans(Object object, Class<T> beanClass) {
      Collection<?> jsonObjects = JsonNetUtils.toCollection(object);
      List<T> retentity = Lists.newArrayList();
      for (Iterator<?> itor = jsonObjects.iterator(); itor.hasNext();) {
          retentity.add(JsonNetUtils.toBean(itor.next(), beanClass));
      }
      return retentity;
  }

  /**
   * 将JSON文本反序列化为主从关系的实体.
   *
   * @param jsonString
   *          JSON文本
   * @param mainClass
   *          主实体类型
   * @param detailName
   *          从实体类在主实体类中的属性名称
   * @param detailClass
   *          从实体类型
   * @return T, D
   */
  public static <T, D> T toBean(String jsonString, Class<T> mainClass, String detailName,
      Class<D> detailClass) {
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    JSONArray jsonArray = (JSONArray) jsonObject.get(detailName);

    T mainEntity = JsonNetUtils.toBean(jsonObject, mainClass);
    List<D> detailList = JsonNetUtils.toList(jsonArray, detailClass);

    try {
      BeanUtils.setProperty(mainEntity, detailName, detailList);
    } catch (Exception ex) {
      throw new RuntimeException("主从关系JSON反序列化实体失败！");
    }

    return mainEntity;
  }

  /**
   * 将JSON文本反序列化为主从关系的实体.
   *
   * @param jsonString
   *          JSON文本
   * @param mainClass
   *          主实体类型
   * @param detailName1
   *          从实体类在主实体类中的属性
   * @param detailClass1
   *          从实体类型
   * @param detailName2
   *          从实体类在主实体类中的属性
   * @param detailClass2
   *          从实体类型
   * @return T, D1, D2
   */
  public static <T, D1, D2> T toBean(String jsonString, Class<T> mainClass, String detailName1,
      Class<D1> detailClass1, String detailName2, Class<D2> detailClass2) {
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    JSONArray jsonArray1 = (JSONArray) jsonObject.get(detailName1);
    JSONArray jsonArray2 = (JSONArray) jsonObject.get(detailName2);

    T mainEntity = JsonNetUtils.toBean(jsonObject, mainClass);
    List<D1> detailList1 = JsonNetUtils.toList(jsonArray1, detailClass1);
    List<D2> detailList2 = JsonNetUtils.toList(jsonArray2, detailClass2);

    try {
      BeanUtils.setProperty(mainEntity, detailName1, detailList1);
      BeanUtils.setProperty(mainEntity, detailName2, detailList2);
    } catch (Exception ex) {
      throw new RuntimeException("主从关系JSON反序列化实体失败！");
    }

    return mainEntity;
  }

  /**
   * 将JSON文本反序列化为主从关系的实体.
   * @param jsonString
   *          JSON文本
   * @param mainClass
   *          主实体类型
   * @param detailName1
   *          从实体类在主实体类中的属性
   * @param detailClass1
   *          从实体类型
   * @param detailName2
   *          从实体类在主实体类中的属性
   * @param detailClass2
   *          从实体类型
   * @param detailName3
   *          从实体类在主实体类中的属性
   * @param detailClass3
   *          从实体类型
   * @return T, D1, D2, D3
   */
  public static <T, D1, D2, D3> T toBean(String jsonString, Class<T> mainClass, String detailName1,
      Class<D1> detailClass1, String detailName2, Class<D2> detailClass2, String detailName3,
      Class<D3> detailClass3) {
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    JSONArray jsonArray1 = (JSONArray) jsonObject.get(detailName1);
    JSONArray jsonArray2 = (JSONArray) jsonObject.get(detailName2);
    JSONArray jsonArray3 = (JSONArray) jsonObject.get(detailName3);

    T mainEntity = JsonNetUtils.toBean(jsonObject, mainClass);
    List<D1> detailList1 = JsonNetUtils.toList(jsonArray1, detailClass1);
    List<D2> detailList2 = JsonNetUtils.toList(jsonArray2, detailClass2);
    List<D3> detailList3 = JsonNetUtils.toList(jsonArray3, detailClass3);

    try {
      BeanUtils.setProperty(mainEntity, detailName1, detailList1);
      BeanUtils.setProperty(mainEntity, detailName2, detailList2);
      BeanUtils.setProperty(mainEntity, detailName3, detailList3);
    } catch (Exception ex) {
      throw new RuntimeException("主从关系JSON反序列化实体失败！");
    }

    return mainEntity;
  }

  /**
   * 将JSON文本反序列化为主从关系的实体.
   *
   * @param 主实体类型
   * @param jsonString
   *          JSON文本
   * @param mainClass
   *          主实体类型
   * @param detailClass
   *          存放了多个从实体在主实体中属性名称和类型
   * @return T
   */
  @SuppressWarnings("rawtypes")
  public static <T> T toBean(String jsonString, Class<T> mainClass,
      HashMap<String, Class> detailClass) {
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    T mainEntity = JsonNetUtils.toBean(jsonObject, mainClass);
    for (Object key : detailClass.keySet()) {
      try {
        Class<?> value = (Class<?>) detailClass.get(key);
        BeanUtils.setProperty(mainEntity, key.toString(), value);
      } catch (Exception ex) {
        throw new RuntimeException("主从关系JSON反序列化实体失败！");
      }
    }
    return mainEntity;
  }
}
