package com.oseasy.util.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangzheng on 2017/4/5.
 */
public class FileUpUtils {
    public static List<Map<String,String>> getFileListMap(String[] arrUrl,String[] arrNames) {
        if (arrUrl!=null) {
            List<Map<String,String>> list=new ArrayList<Map<String,String>>();
            for(int i=0;i<arrUrl.length;i++) {
                Map<String,String> map=new HashMap<String,String>();
                map.put("arrUrl", arrUrl[i]);
                map.put("arrName", arrNames[i]);
                list.add(map);
            }
            return list;
        }else{
            return null;
        }
    }

}
