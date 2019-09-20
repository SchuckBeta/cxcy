/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * .
 * @author chenhao
 *
 */
public enum DrCardType {
    NO_TEMP("1", "正式卡"), TEMP("0", "临时卡");

    public static final String DR_CTYPES = "drCardTypes";

    private String key;
    private String name;

    private DrCardType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return DrCardType
     */
    public static DrCardType getByKey(String key) {
        switch (key) {
        case "1":
            return NO_TEMP;
        case "2":
            return TEMP;
        default:
            return null;
        }
    }

    /**
     * 获取卡状态.
     * @return List
     */
    public static List<DrCardType> getAll(Boolean isShow) {
        if(isShow == null){
            isShow = true;
        }

        List<DrCardType> enty = Lists.newArrayList();
        DrCardType[] entitys = DrCardType.values();
        for (DrCardType entity : entitys) {
            if ((entity.getKey() != null)) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<DrCardType> getAll() {
        return getAll(true);
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
