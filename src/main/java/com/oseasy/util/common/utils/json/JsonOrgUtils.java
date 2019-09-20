/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.util.common.utils.json
 * @Description [[_JsonOUtils_]]文件
 * @date 2017年6月2日 上午10:23:41
 *
 */

package com.oseasy.util.common.utils.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * org.json.*
 *
 * @Description JSON工具类.
 * @author chenhao
 * @date 2017年6月2日 上午10:23:41
 *
 */
public class JsonOrgUtils {
  protected static final Logger LOGGER = Logger.getLogger(JsonOrgUtils.class);
  /**
   * Json字符转换为List .
   *
   * @author chenhao
   * @param jsonStr
   *          Json字符串
   */
  @SuppressWarnings("unchecked")
  public static List<Map<String, Object>> parseJsonToList(String jsonStr) {
    net.sf.json.JSONArray jsonArr = net.sf.json.JSONArray.fromObject(jsonStr);
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Iterator<net.sf.json.JSONObject> it = jsonArr.listIterator();
    while (it.hasNext()) {
      net.sf.json.JSONObject json2 = it.next();
      list.add(parseJsonToMap(json2.toString()));
    }
    return list;
  }

  /**
   * json string 转换为 map 对象.
   *
   * @param jsonStr
   *          Json字符串
   */
  @SuppressWarnings("unchecked")
  public static Map<Object, Object> parseJsonToMap(Object jsonStr) {
    net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonStr);
    return (Map<Object, Object>) jsonObject;
  }

  /**
   * json string 转换为 map 对象.
   * @param jsonStr
   *          Json字符串
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> parseJsonToMap(String jsonStr) {
    Map<String, Object> map = new HashMap<String, Object>();
    // 最外层解析
    net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(jsonStr);
    for (Object k : json.keySet()) {
      Object v = json.get(k);
      // 如果内层还是数组的话，继续解析
      if (v instanceof net.sf.json.JSONArray) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Iterator<net.sf.json.JSONObject> it = ((net.sf.json.JSONArray) v).iterator();
        while (it.hasNext()) {
          net.sf.json.JSONObject json2 = it.next();
          list.add(parseJsonToMap(json2.toString()));
        }
        map.put(k.toString(), list);
      } else {
        map.put(k.toString(), v);
      }
    }
    return map;
  }

  /**
   * json string 转换为 对象.
   *
   * @param jsonObj Json对象
   * @param type 转换类
   * @return T
   */
  @SuppressWarnings("unchecked")
  public static <T> T parseJsonToBean(Object jsonObj, Class<T> type) {
    net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonObj);
    return (T) net.sf.json.JSONObject.toBean(jsonObject, type);
  }

  /**
   * 根据Url获取Map.
   * @param url 地址
   * @return List
   */
  public static List<Map<String, Object>> getListByUrl(String url) {
    try {
      // 通过HTTP获取JSON数据
      InputStream in = new URL(url).openStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      return parseJsonToList(sb.toString());
    } catch (Exception e) {
      LOGGER.warn("根据Url获取Map失败！", e);
    }
    return null;
  }

  /**
   * 根据Url获取Map.
   * @param url 地址
   * @return Map
   */
  public static Map<String, Object> getMapByUrl(String url) {
    try {
      // 通过HTTP获取JSON数据
      InputStream in = new URL(url).openStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      return parseJsonToMap(sb.toString());
    } catch (Exception e) {
      LOGGER.warn("根据Url获取Map失败！", e);
    }
    return null;
  }

  /**
   * 将Javabean转换为Map.
   *
   * @param javaBean Bean实体
   * @return Map对象
   */
  public static Map<String, Object> toMap(Object javaBean) {

    Map<String, Object> result = new HashMap<String, Object>();
    Method[] methods = javaBean.getClass().getDeclaredMethods();

    for (Method method : methods) {
      try {
        if (method.getName().startsWith("get")) {
          String field = method.getName();
          field = field.substring(field.indexOf("get") + 3);
          field = field.toLowerCase().charAt(0) + field.substring(1);
          Object value = method.invoke(javaBean, (Object[]) null);
          result.put(field, null == value ? "" : value.toString());
        }
      } catch (Exception e) {
        LOGGER.warn("Javabean转换为Map失败！", e);
      }
    }
    return result;
  }

  /**
   * 将Json对象转换成Map.
   *
   * @param jsonStr Json字符串
   * @return Map对象
   * @throws JSONException Json异常
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> toMap(String jsonStr) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      // 最外层解析
      JSONObject json = new JSONObject(jsonStr);
      for (Iterator<String> keys = json.keys(); keys.hasNext();) {
        String k = keys.next();
        Object v = json.get(k);
        // 如果内层还是数组的话，继续解析
        if (v instanceof JSONArray) {
          List<Object> list = new ArrayList<Object>();
          forJsonArray((JSONArray) v, list);
          map.put(k.toString(), list);
        } else if (v instanceof JSONObject) {
          JSONObject json2 = (JSONObject) v;
          map.put(k, toMap(json2.toString()));
        } else {
          map.put(k, v);
        }
      }
    } catch (JSONException e) {
      LOGGER.warn("Json转换为Map失败！", e);
    }
    return map;
  }

  private static List<Object> forJsonArray(JSONArray array, List<Object> list) {
    try {
      int len = array.length();
      for (int i = 0; i < len; i++) {
        Object object = array.get(i);
        if (object instanceof JSONObject) {
          JSONObject json2 = (JSONObject) object;
          list.add(toMap(json2.toString()));
        } else if (object instanceof JSONArray) {
          List<Object> l = new ArrayList<Object>();
          l = forJsonArray((JSONArray) object, l);
          list.add(l);
        } else {
          list.add(object);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * 将JavaBean转换成JSONObject（通过Map中转）.
   *
   * @param bean 实体
   * @return json对象
   */
  public static JSONObject toJson(Object bean) {
    return new JSONObject(toMap(bean));
  }

  /**
   * 将Map转换成Javabean.
   *
   * @param javabean 实体
   * @param data 参数
   */
  @SuppressWarnings("unchecked")
  public static Object toJavaBean(Object javabean, Map<String, Object> data) {

    Method[] methods = javabean.getClass().getDeclaredMethods();
    for (Method method : methods) {
      try {
        if (method.getName().startsWith("set")) {
          String field = method.getName();
          String field1 = field.substring(field.indexOf("set") + 3);
          field = field1.toLowerCase().charAt(0) + field1.substring(1);
          if (data.get(field) instanceof HashMap<?, ?>) {
            String getName = "get" + field1;
            Method getMethod = javabean.getClass().getMethod(getName);
            Object subBean = getMethod.invoke(javabean);
            toJavaBean(subBean, (HashMap<String, Object>) data.get(field));
          } else {
            method.invoke(javabean, new Object[] { data.get(field) });
          }
        }
      } catch (Exception e) {
        LOGGER.warn("将Map转换成Javabean失败！", e);
      }

    }

    return javabean;

  }

  /**
   * json字符串到JavaBean.

   * @param javabean JSON对象
   * @param jsonString  JSON字符
   * @throws JSONException Json异常
   */
  public static void toJavaBean(Object javabean, String jsonString) throws JSONException {
    JSONObject jsonObject = new JSONObject(jsonString);
    toJavaBean(javabean, jsonObject);
  }

  /**
   * JSONObject到JavaBean.
   *
   * @param javabean JSON对象
   * @param jsonObject JSON对象
   * @throws JSONException Json异常
   */
  public static void toJavaBean(Object javabean, JSONObject jsonObject) throws JSONException {
    Map<String, Object> map = toMap(jsonObject.toString());
    toJavaBean(javabean, map);
  }

  /**
   * 获取json字符串值，屏蔽抛出异常.
   *
   * @param jsonObject JSON对象
   * @param key json Key
   * @return String
   * @throws JSONException Json异常
   */
  public static String getJsonString(JSONObject jsonObject, String key) {
    try {
      if (jsonObject == null) {
        return null;
      }
      if (jsonObject.has(key)) {
        String ret = jsonObject.getString(key);
        return ret;
      }
    } catch (JSONException e) {
      LOGGER.warn("获取json字符串值失败！", e);
    }
    return null;
  }

  /**
   * 把json对象转换成一行字符串.
   *
   * @param json
   *          可以是：JSONObject、JSONArray、json格式的字符串
   * @return String
   */
  public static String toOneLineString(Object json) {
    String line = null;
    try {
      if (json instanceof String) {
        Object o = (new JSONTokener((String) json)).nextValue();
        line = o.toString();
      } else {
        line = json.toString();
      }
    } catch (JSONException e) {
      LOGGER.warn("把json对象转换成一行字符串失败！", e);
    }
    return line;
  }

  /**
   * json字符串的格式化.
   *
   * @param json
   *          需要格式的json串
   * @param fillStringUnit 每一层之前的占位符号比如空格
   *          制表符
   * @return String
   */
  public static String formatJson(String json, String fillStringUnit) {
    if (json == null || json.trim().length() == 0) {
      return null;
    }

    int fixedLenth = 0;
    ArrayList<String> tokenList = new ArrayList<String>();
    {
      String jsonTemp = json;
      // 预读取
      while (jsonTemp.length() > 0) {
        String token = getToken(jsonTemp);
        jsonTemp = jsonTemp.substring(token.length());
        token = token.trim();
        tokenList.add(token);
      }
    }

    for (int i = 0; i < tokenList.size(); i++) {
      String token = tokenList.get(i);
      int length = token.getBytes().length;
      if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
        fixedLenth = length;
      }
    }

    StringBuilder buf = new StringBuilder();
    int count = 0;
    for (int i = 0; i < tokenList.size(); i++) {

      String token = tokenList.get(i);

      if (token.equals(",")) {
        buf.append(token);
        doFill(buf, count, fillStringUnit);
        continue;
      }
      if (token.equals(":")) {
        buf.append(" ").append(token).append(" ");
        continue;
      }
      if (token.equals("{")) {
        String nextToken = tokenList.get(i + 1);
        if (nextToken.equals("}")) {
          i++;
          buf.append("{ }");
        } else {
          count++;
          buf.append(token);
          doFill(buf, count, fillStringUnit);
        }
        continue;
      }
      if (token.equals("}")) {
        count--;
        doFill(buf, count, fillStringUnit);
        buf.append(token);
        continue;
      }
      if (token.equals("[")) {
        String nextToken = tokenList.get(i + 1);
        if (nextToken.equals("]")) {
          i++;
          buf.append("[ ]");
        } else {
          count++;
          buf.append(token);
          doFill(buf, count, fillStringUnit);
        }
        continue;
      }
      if (token.equals("]")) {
        count--;
        doFill(buf, count, fillStringUnit);
        buf.append(token);
        continue;
      }

      buf.append(token);
      // 左对齐
      if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
        int fillLength = fixedLenth - token.getBytes().length;
        if (fillLength > 0) {
          for (int j = 0; j < fillLength; j++) {
            buf.append(" ");
          }
        }
      }
    }
    return buf.toString();
  }

  private static String getToken(String json) {
    StringBuilder buf = new StringBuilder();
    boolean isInYinHao = false;
    while (json.length() > 0) {
      String token = json.substring(0, 1);
      json = json.substring(1);

      if (!isInYinHao && (token.equals(":") || token.equals("{") || token.equals("}")
          || token.equals("[") || token.equals("]") || token.equals(","))) {
        if (buf.toString().trim().length() == 0) {
          buf.append(token);
        }

        break;
      }

      if (token.equals("\\")) {
        buf.append(token);
        buf.append(json.substring(0, 1));
        json = json.substring(1);
        continue;
      }
      if (token.equals("\"")) {
        buf.append(token);
        if (isInYinHao) {
          break;
        } else {
          isInYinHao = true;
          continue;
        }
      }
      buf.append(token);
    }
    return buf.toString();
  }

  private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
    buf.append("\n");
    for (int i = 0; i < count; i++) {
      buf.append(fillStringUnit);
    }
  }
}
