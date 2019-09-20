/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 卡调用硬件处理状态.
 * @author chenhao
 *
 */
public enum DrCdealStatus {
    DCD_NORMAL(0, true, "正常"), DCD_DEALING(1, true, "处理中"), DCD_FAIL(2, true, "失败");

    public static final String DR_CDSTATUSS = "drCdstatuss";

    private int key;
    private boolean isShow;// 是否允许选择
    private String name;

    private DrCdealStatus(int key, boolean isShow, String name) {
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
     * @return DrCdealStatus
     */
    public static DrCdealStatus getByKey(int key) {
        switch (key) {
        case 0:
            return DCD_NORMAL;
        case 1:
            return DCD_DEALING;
        case 2:
            return DCD_FAIL;
        default:
            return null;
        }
    }

    /**
     * 获取卡处理状态.
     * @return List
     */
    public static List<DrCdealStatus> getAll(Boolean isShow) {
        if(isShow == null){
            isShow = true;
        }

        List<DrCdealStatus> enty = Lists.newArrayList();
        DrCdealStatus[] entitys = DrCdealStatus.values();
        for (DrCdealStatus entity : entitys) {
            if ((entity.getKey() != null) && (entity.getIsShow())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<DrCdealStatus> getAll() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Object> getDrCdealStatuss(Boolean isShow){
        if(isShow == null){
            isShow = true;
        }
        List<Object> list = Lists.newArrayList();
        for (DrCdealStatus drCdealStatus: DrCdealStatus.values()){
            if(isShow == drCdealStatus.getIsShow()){
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("key", drCdealStatus.getKey());
                hashMap.put("isShow", drCdealStatus.getIsShow());
                hashMap.put("name", drCdealStatus.getName());
                list.add(hashMap);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"isShow\":\"" + this.isShow + "\",\"name\":\"" + this.name + "\"}";
    }
}
