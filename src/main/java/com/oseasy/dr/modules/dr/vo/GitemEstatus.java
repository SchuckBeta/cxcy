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
public enum GitemEstatus {
    GES_NORMAL("9", true, "进出"), GES_ENTER("1", true, "进"), GES_EXIT("0", false, "出");

    public static final String GE_STATUSS = "gitemEstatuss";

    private String key;
    private boolean isShow;// 是否允许选择
    private String name;

    private GitemEstatus(String key, boolean isShow, String name) {
        this.key = key;
        this.isShow = isShow;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return GitemEstatus
     */
    public static GitemEstatus getByKey(String key) {
        switch (key) {
        case "9":
            return GES_NORMAL;
        case "0":
            return GES_ENTER;
        case "1":
            return GES_EXIT;
        default:
            return null;
        }
    }

    /**
     * 获取卡状态.
     * @return List
     */
    public static List<GitemEstatus> getAll(Boolean isShow) {
        if(isShow == null){
            isShow = true;
        }

        List<GitemEstatus> enty = Lists.newArrayList();
        GitemEstatus[] entitys = GitemEstatus.values();
        for (GitemEstatus entity : entitys) {
            if ((entity.getKey() != null) && (entity.getIsShow())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<GitemEstatus> getAll() {
        return getAll(true);
    }


    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"isShow\":\"" + this.isShow + "\",\"name\":\"" + this.name + "\"}";
    }
}
