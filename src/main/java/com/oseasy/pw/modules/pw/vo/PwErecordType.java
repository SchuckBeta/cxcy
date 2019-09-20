package com.oseasy.pw.modules.pw.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻记录类型.
 * @author chenhao
 */
public enum PwErecordType {
    ASS("10","分配")
   ,CANCEL("20","取消分配");

    public static final String PW_ERECORDTYPES = "pwErecordTypes";
    private String key;
    private String name;

    private PwErecordType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return PwEnterType
     */
    public static PwErecordType getByKey(String key) {
        if ((key != null)) {
            PwErecordType[] entitys = PwErecordType.values();
            for (PwErecordType entity : entitys) {
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
     * @param key
     *            枚举标识
     * @return PwEnterType
     */
    public static List<PwErecordType> getByKeys(String keys) {
        if ((keys != null)) {
            PwErecordType[] entitys = PwErecordType.values();
            List<PwErecordType> pwEnterStatuss = Lists.newArrayList();
            List<String> keyss = Arrays.asList(StringUtil.split(keys, StringUtil.DOTH));
            for (PwErecordType entity : entitys) {
                for (String key : keyss) {
                    if (StringUtil.isNotEmpty(key) && (key).equals(entity.getKey())) {
                        pwEnterStatuss.add(entity);
                    }
                }
            }
            return pwEnterStatuss;
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