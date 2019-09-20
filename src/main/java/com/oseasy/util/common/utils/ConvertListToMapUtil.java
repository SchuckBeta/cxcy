package com.oseasy.util.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConvertListToMapUtil {	
	
	/**
	 * List 转化成 Map
	 * @param keyName
	 * @param list
	 * @return
	 */
	public static <T> Map<String, T> listToMap(String keyName, List<T> list) {
        Map<String, T> m = new TreeMap<String, T>();
        try {
            for (T t : list) {
                PropertyDescriptor pd = new PropertyDescriptor(keyName,t.getClass());
                Method getMethod = pd.getReadMethod();// 获得get方法
                Object o = getMethod.invoke(t);// 执行get方法返回一个Object
                String strkey=  "source".equals(keyName)?o.toString(): removeStartWith(o.toString());
                m.put(strkey, t);
            }
//            sortMapBykey2(m); //排序
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }
	
	private static String  removeStartWith(String str) {
		if (str.startsWith("0")) {
			str = str.substring(1, str.length());
		}
		return str;
	}
	
	
	public static <T> void sortMapBykey2(Map<String, T> map) { //倒序
		List<Map.Entry<String, T>> infoIds = new ArrayList<Map.Entry<String, T>>(map.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, T>>() {   
		    public int compare(Map.Entry<String, T> o1, Map.Entry<String, T> o2) {  
		    	int result= o1.getKey().compareTo(o2.getKey());
		    	return result;
		    }
		});
	}
	
}

