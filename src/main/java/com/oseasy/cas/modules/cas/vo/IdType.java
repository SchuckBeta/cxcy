/**
 * .
 */

package com.oseasy.cas.modules.cas.vo;

/**
 * 证件类型.
 * @author chenhao
 */
public enum IdType {
    SFZ("1", "身份证"),
    HZ("2", "护照"),
    OT("10", "其它");

    private String key;//主题
    private String remark;//

    private IdType(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key
     *            页面标识
     * @return IdType
     */
    public static IdType getByKey(String key) {
        if ((key != null)) {
            IdType[] entitys = IdType.values();
            for (IdType entity : entitys) {
                if ((key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getRemark() {
        return remark;
    }
}
