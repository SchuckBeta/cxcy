/**
 * .
 */

package com.oseasy.com.mqserver.modules.oa.vo;

/**
 * .
 * @author chenhao
 *
 */
public enum OaNotype {
    OA_NOMALL("0", "系统通知"),
    OA_OFFICE("1", "院系通知");

    public static final String OAN_KEYS = "oaNotypes";

    private String key;
    private String name;

    private OaNotype(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key
     *            枚举标识
     * @return OaNotype
     */
    public static OaNotype getByKey(String key) {
        switch (key) {
        case "0":
            return OA_NOMALL;
        case "100":
            return OA_OFFICE;
        default:
            return OA_NOMALL;
        }
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
    }
}