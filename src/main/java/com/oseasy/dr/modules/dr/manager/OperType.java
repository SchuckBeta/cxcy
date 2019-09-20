/**
 * .
 */

package com.oseasy.dr.modules.dr.manager;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * .
 * @author chenhao
 *
 */
public enum OperType {
    PUBLISH(10),
    LOSS(20),
    BLACK(30),
    BLACK_LIST(40),
    ACTIVIT(50),
    REPUBLISH(60),
    CONNECT(70),
    OPEN_ALL_DOORS(80),
    CLOSE_DOORS(90),
    HOLD_OPEN_DOORS(100)
    ;
    private int key;

    private OperType(int key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return OperType
     */
    public static OperType getByKey(int key) {
        switch (key) {
        case 10:
            return PUBLISH;
        case 20:
            return LOSS;
        case 30:
            return BLACK;
        case 40:
            return BLACK_LIST;
        case 50:
            return ACTIVIT;
        case 60:
            return REPUBLISH;
        default:
            return null;
        }
    }

    /**
     * 获取卡状态.
     * @return List
     */
    public static List<OperType> getAll(Boolean isShow) {
        if(isShow == null){
            isShow = true;
        }

        List<OperType> enty = Lists.newArrayList();
        OperType[] entitys = OperType.values();
        for (OperType entity : entitys) {
            if (entity.getKey() != null) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<OperType> getAll() {
        return getAll(true);
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\"}";
    }
}
