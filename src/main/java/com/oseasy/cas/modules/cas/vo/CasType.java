/**
 * .
 */

package com.oseasy.cas.modules.cas.vo;

/**
 * CAS类型.
 * @author chenhao
 *
 */
public enum CasType {
    ANZHI("10", "安职"),
    KUANGDA("20", "矿大");

    private String key;//主题
    private String remark;//

    private CasType(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key 页面标识
     * @return CasType
     */
    public static CasType getByKey(String key) {
        if ((key != null)) {
            CasType[] entitys = CasType.values();
            for (CasType entity : entitys) {
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
    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"remark\":\"" + this.remark + "\"}";
    }
}
