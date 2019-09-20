/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 门禁卡状态.
 *
 * @author chenhao
 *
 */
public enum DrCstatus {
    DC_NORMAL(0, true, "开卡/激活", "已开卡/激活"), DC_LOSS(1, true, "挂失", "已挂失"), DC_BLACK_LIST(2, false, "黑名单", "已列入黑名单"), DC_BLACK(9, true, "退卡", "已退卡"), DC_TEMP(10, true, "未注册授权", "未注册/授权");

    public static final String DR_CSTATUSS = "drCstatuss";

    private int key;
    private boolean isShow;// 是否允许选择
    private String name;
    private String sname;

    private DrCstatus(int key, boolean isShow, String name, String sname) {
        this.key = key;
        this.isShow = isShow;
        this.name = name;
        this.sname = sname;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return DrCstatus
     */
    public static DrCstatus getByKey(int key) {
        switch (key) {
        case 0:
            return DC_NORMAL;
        case 1:
            return DC_LOSS;
        case 2:
            return DC_BLACK_LIST;
        default:
            return null;
        }
    }

    /**
     * 获取卡状态.
     * @return List
     */
    public static List<DrCstatus> getAll(Boolean isShow) {
        if(isShow == null){
            isShow = true;
        }

        List<DrCstatus> enty = Lists.newArrayList();
        DrCstatus[] entitys = DrCstatus.values();
        for (DrCstatus entity : entitys) {
            if ((entity.getKey() != null) && (entity.getIsShow())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<DrCstatus> getAll() {
        return getAll(true);
    }


    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getSname() {
        return sname;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"isShow\":\"" + this.isShow + "\",\"sname\":\"" + this.sname + "\",\"name\":\"" + this.name + "\"}";
    }
}
