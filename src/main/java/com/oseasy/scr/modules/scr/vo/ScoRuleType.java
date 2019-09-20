package com.oseasy.scr.modules.scr.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by PW on 2018/12/29.
 */
public enum ScoRuleType {
    MAX_SCORE("1", "取最高级别分值", true)
    ,SUM_SCORE("2", "累计分值", true);

    private String key;
    private String name;
    private boolean enable;

    public static final String SCO_RULE_TYPE = "scoRuletype";

    private ScoRuleType(String key, String name, boolean enable) {
        this.key = key;
        this.name = name;
        this.enable = enable;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return ScoRuleType
     */
    public static ScoRuleType getByKey(String key) {
        if ((key != null)) {
            ScoRuleType[] entitys = ScoRuleType.values();
            for (ScoRuleType entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 获取主题 .
     * @return List
     */
    public static List<ScoRuleType> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<ScoRuleType> enty = Lists.newArrayList();
        ScoRuleType[] entitys = ScoRuleType.values();
        for (ScoRuleType entity : entitys) {
            if ((entity.getKey() != null) && (entity.isEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }

    public static List<ScoRuleType> getAll() {
        return getAll(true);
    }

    public boolean isEnable() {
        return enable;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\",\"enable\":\"" + this.enable + "\"}";
    }
}
