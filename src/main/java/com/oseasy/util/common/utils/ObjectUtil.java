/**
 *
 */
package com.oseasy.util.common.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类

 * @version 2014-6-29
 */
public class ObjectUtil extends ObjectUtils {

	/**
	 * 注解到对象复制，只复制能匹配上的方法。
	 * @param annotation
	 * @param object
	 */
	public static void annotationToObject(Object annotation, Object object) {
		if (annotation != null) {
			Class<?> annotationClass = annotation.getClass();
			if (null == object) {
				return;
			}
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods()) {
				if (StringUtils.startsWith(m.getName(), "set")) {
					try {
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString())) {
							m.invoke(object, obj);
						}
					} catch (Exception e) {
						// 忽略所有设置失败方法
					}
				}
			}
		}
	}

	/**
	 * 序列化对象
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			if (object != null) {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			if (bytes != null && bytes.length > 0) {
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	 /**
   * 对象转Map
   * @param bytes
   * @return
   */
	public static Map<String, Object> obj2Map(Object obj) {
    Map<String, Object> map = new HashMap<String, Object>();
    // System.out.println(obj.getClass());
    // 获取f对象对应类中的所有属性域
    Field[] fields = obj.getClass().getDeclaredFields();
    for (int i = 0, len = fields.length; i < len; i++) {
      String varName = fields[i].getName();
      varName = varName.toLowerCase();//将key置为小写，默认为对象的属性
      try {
        // 获取原来的访问控制权限
        boolean accessFlag = fields[i].isAccessible();
        // 修改访问控制权限
        fields[i].setAccessible(true);
        // 获取在对象f中属性fields[i]对应的对象中的变量
        Object o = fields[i].get(obj);
        if (o != null)
          map.put(varName, o);
//        map.put(varName, o.toString());
        // System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
        // 恢复访问控制权限
        fields[i].setAccessible(accessFlag);
      } catch (IllegalArgumentException ex) {
        ex.printStackTrace();
      } catch (IllegalAccessException ex) {
        ex.printStackTrace();
      }
    }
    return map;
  }

	/**
	 * ToByteArray
	 *
	 * @param obj
	 *            Object
	 * @return byte[]
	 */
	public static byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	/**
	 * ToObject
	 *
	 * @param bytes
	 *            byte[]
	 * @return Object
	 */
	public static Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}
}
