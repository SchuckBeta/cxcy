package com.oseasy.pro.modules.course.vo;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

public enum SentuStatus {
    PES_CG("1", "成功")
    ,PES_IPWSQ("2", "Ip未授权")
    ,PES_RZSB("3", "统一认证授权已失效")
    ,PES_NOPARAM("4", "必要的参数缺失")
    ,PES_ERRORSIGN("8", "数字签名不匹配");


    public static final String SENTU_STATUS = "sentuStatus";
    private String key;
    private String name;

    private SentuStatus(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return PwEnterType
     */
    public static SentuStatus getByKey(String key) {
        if ((key != null)) {
            SentuStatus[] entitys = SentuStatus.values();
            for (SentuStatus entity : entitys) {
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
     * @param keys 枚举标识
     * @return PwEnterType
     */
    public static List<SentuStatus> getByKeys(String keys) {
        if ((keys != null)) {
            SentuStatus[] entitys = SentuStatus.values();
            List<SentuStatus> SentuStatuss = Lists.newArrayList();
            List<String> keyss = Arrays.asList(StringUtil.split(keys, StringUtil.DOTH));
            for (SentuStatus entity : entitys) {
                for (String key : keyss) {
                    if (StringUtil.isNotEmpty(key) && (key).equals(entity.getKey())) {
                        SentuStatuss.add(entity);
                    }
                }
            }
            return SentuStatuss;
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