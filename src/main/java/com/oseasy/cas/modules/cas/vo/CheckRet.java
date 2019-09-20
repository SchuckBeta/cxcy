/**
 * .
 */

package com.oseasy.cas.modules.cas.vo;

/**
 * 检查用户是否在sys_User和sys_cas_user表存在文在则为0否则为1.
 * @author chenhao
 *
 */
public enum CheckRet {
    TRUE(0, "存在"),
    FALSE(1, "不存在"),
    FALSE_MANY(9, "查询数据结果大于一条");

    private Integer key;//主题
    private String remark;//

    private CheckRet(Integer key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key
     *            页面标识
     * @return CheckRet
     */
    public static CheckRet getByKey(Integer key) {
        if ((key != null)) {
            CheckRet[] entitys = CheckRet.values();
            for (CheckRet entity : entitys) {
                if (key == entity.getKey()) {
                    return entity;
                }
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public String getRemark() {
        return remark;
    }
}
