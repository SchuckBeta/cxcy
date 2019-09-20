package com.oseasy.com.pcore.modules.syt.vo;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.syt.manager.sub.*;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public enum SytStatus {
    DKH("0", "待开户")
    ,YKH("1", "开户成功")
    ,LOCKING("2", "锁定");

    private String key;
    private String name;

    private SytStatus(String key,String name){
        this.key = key;
        this.name = name;
    }

    public static List<SytStatus> getAll() {
        List<SytStatus> enty = Lists.newArrayList();
        SytStatus[] entitys = SytStatus.values();
        for (SytStatus entity : entitys) {
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