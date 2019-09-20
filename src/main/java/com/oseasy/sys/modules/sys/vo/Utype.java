/**
 * .
 */

package com.oseasy.sys.modules.sys.vo;

/**
 * 用户类型枚举.
 * @author chenhao
 */
public enum Utype {
    STUDENT("1", "学生"),
    TEACHER("2", "导师"),
    USER("10", "用户");

    private String key;//主题
    private String remark;//

    private Utype(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key
     *            页面标识
     * @return Utype
     */
    public static Utype getByKey(String key) {
        if ((key != null)) {
            Utype[] entitys = Utype.values();
            for (Utype entity : entitys) {
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
