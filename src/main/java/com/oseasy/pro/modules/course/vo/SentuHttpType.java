package com.oseasy.pro.modules.course.vo;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2019/4/23 0023.
 */
public enum SentuHttpType {
    GET("get", "GET")
    ,POST("post", "POST");


    public static final String SENTU_HTTP_TYPE = "SentuHttpType";
    private String key;
    private String name;

    private SentuHttpType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return PwEnterType
     */
    public static SentuHttpType getByKey(String key) {
        if ((key != null)) {
            SentuHttpType[] entitys = SentuHttpType.values();
            for (SentuHttpType entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return PwEnterType
     */
    public static List<SentuHttpType> getByKeys(String keys) {
        if ((keys != null)) {
            SentuHttpType[] entitys = SentuHttpType.values();
            List<SentuHttpType> SentuStatuss = Lists.newArrayList();
            List<String> keyss = Arrays.asList(StringUtil.split(keys, StringUtil.DOTH));
            for (SentuHttpType entity : entitys) {
                for (String key : keyss) {
                    if (StringUtil.isNotEmpty(key) && (key).equals(entity.getKey())) {
                        SentuStatuss.add(entity);
                    }
                }
            }
            return SentuStatuss;
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
    }
}