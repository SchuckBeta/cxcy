/**
 * .
 */

package com.oseasy.dr.modules.dr.vo;

/**
 * 控制器门标识.
 * @author chenhao
 */
public enum DrSpaceType {
    DK_SPACE("1", "场地"),
    DK_ROOM("2", "房间");

    public static final String DR_SPACE_TYPE_KEYS = "drSpaceTypes";

    private String key;
    private String name;

    private DrSpaceType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key
     *            枚举标识
     * @return DrSpaceType
     */
    public static DrSpaceType getByKey(String key) {
        switch (key) {
        case "1":
            return DK_SPACE;
        case "2":
            return DK_ROOM;
        default:
            return null;
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
