/**
 * .
 */

package com.oseasy.com.pcore.modules.authorize.vo;

/**
 * 环境类型.
 * @author chenhao
 */
public enum EvnType {
    PC("0", "主机环境"),
    DOCKER("1", "Docker容器环境");

    private String key;//主题
    private String remark;//

    private EvnType(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key 页面标识
     * @return EvnType
     */
    public static EvnType getByKey(String key) {
        if ((key != null)) {
            EvnType[] entitys = EvnType.values();
            for (EvnType entity : entitys) {
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
