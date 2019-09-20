package com.oseasy.pro.modules.course.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Administrator on 2019/4/16 0016.
 */
public enum ResCtype {
    GD("0", "固定地址"),
    DY("1", "动态地址");

    private String key;//
    private String remark;//

    private ResCtype(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key 标识
     * @return CheckRet
     */
    public static ResCtype getByKey(String key) {
        if ((key != null)) {
            ResCtype[] entitys = ResCtype.values();
            for (ResCtype entity : entitys) {
                if ((entity.getKey()).equals(key)) {
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
    public static List<ResCtype> getAll() {
        List<ResCtype> enty = Lists.newArrayList();
        ResCtype[] entitys = ResCtype.values();
        for (ResCtype entity : entitys) {
            enty.add(entity);
        }
        return enty;
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