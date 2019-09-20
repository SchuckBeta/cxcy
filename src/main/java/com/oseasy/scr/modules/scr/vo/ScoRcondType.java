package com.oseasy.scr.modules.scr.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 条件校验类型：默认0：取值, 1、字典值(使用字典key）;2、区间值.
 *
 * @author chenh
 * @version 2018-12-21
 */
public enum ScoRcondType {
    SRC_DEFAULT("0", "取值", true)
    , SRC_DICT("1", "字典值", true)
    , SRC_QJ("2", "区间值", true);

    private String key;
    private String name;
    private boolean enable;

    public static final String SCO_RCONDTYPES = "scoRcondTypes";

    private ScoRcondType(String key, String name, boolean enable) {
        this.key = key;
        this.name = name;
        this.enable = enable;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return ScoRcondType
     */
    public static ScoRcondType getByKey(String key) {
        if ((key != null)) {
            ScoRcondType[] entitys = ScoRcondType.values();
            for (ScoRcondType entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 获取主题 .
     *
     * @return List
     */
    public static List<ScoRcondType> getAll(Boolean enable) {
        if (enable == null) {
            enable = true;
        }

        List<ScoRcondType> enty = Lists.newArrayList();
        ScoRcondType[] entitys = ScoRcondType.values();
        for (ScoRcondType entity : entitys) {
            if ((entity.getKey() != null) && (entity.isEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }

    public static List<ScoRcondType> getAll() {
        return getAll(true);
    }

    public boolean isEnable() {
        return enable;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\",\"enable\":\"" + this.enable + "\"}";
    }
}