/**
 * .
 */

package com.oseasy.util.common.utils.reg;

/**
 * 特殊字符处理转义.
 * @author chenhao
 */
public enum RegMtype {
    DOTH(",", ",")
    ,OR("||", "\\u007C\\u007C")
    ,AND("&&", "&&")
    ,KUOHR(")", "\\u0029")
    ,KUOHL("(", "\\u0028")
    ,KUOHZR("]", "\\u005C")
    ,KUOHZL("[", "\\u005B")
    ,KUOHDR("{", "\\u007B")
    ,KUOHDL("}", "\\u007C");

    private String key;
    private String unicode;

    public static final String REG_MTYPES = "regMtypes";

    private RegMtype(String key, String unicode) {
        this.key = key;
        this.unicode = unicode;
    }

    private RegMtype(String key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return RegexType
     */
    public static RegMtype getByKey(String key) {
        if ((key != null)) {
            RegMtype[] entitys = RegMtype.values();
            for (RegMtype entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getUnicode() {
        return unicode;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\"}";
    }
}