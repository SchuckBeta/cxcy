package com.oseasy.pw.modules.pw.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻说明.
 * @author chenhao
 */
public enum PwEnterRemarks {
    R1("10","初次入驻")
   ,R2("20","初次入驻")
   ,R5("50","变更场地")
   ,R6("60","注册企业")
   ,R7("70","新增项目")
   ,R8("80","变更团队");

    public static final String PW_EBGREMARKS = "pwEnterRemarks";
    private String key;
    private String name;

    private PwEnterRemarks(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return PwEnterType
     */
    public static PwEnterRemarks getByKey(String key) {
        if ((key != null)) {
            PwEnterRemarks[] entitys = PwEnterRemarks.values();
            for (PwEnterRemarks entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return PwEnterType
     */
    public static List<PwEnterRemarks> getByKeys(String keys) {
        if ((keys != null)) {
            PwEnterRemarks[] entitys = PwEnterRemarks.values();
            List<PwEnterRemarks> pwEnterStatuss = Lists.newArrayList();
            List<String> keyss = Arrays.asList(StringUtil.split(keys, StringUtil.DOTH));
            for (PwEnterRemarks entity : entitys) {
                for (String key : keyss) {
                    if (StringUtil.isNotEmpty(key) && (key).equals(entity.getKey())) {
                        pwEnterStatuss.add(entity);
                    }
                }
            }
            return pwEnterStatuss;
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
    }
}