package com.oseasy.com.pcore.modules.syt.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by PW on 2019/4/19.
 */
public enum TenantSchoolType {

    BSGX("1", "部署高校")
    ,SSBK("2", "省属本科")
    ,GZGZ("3", "高职高专")
    ,DLXY("4", "独立学院");

    private String key;
    private String name;

    private TenantSchoolType(String key,String name){
        this.key = key;
        this.name = name;
    }

    public static List<TenantSchoolType> getAll() {
        List<TenantSchoolType> enty = Lists.newArrayList();
        TenantSchoolType[] entitys = TenantSchoolType.values();
        for (TenantSchoolType entity : entitys) {
            if (entity.getKey() != null) {
                enty.add(entity);
            }
        }
        return enty;
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
