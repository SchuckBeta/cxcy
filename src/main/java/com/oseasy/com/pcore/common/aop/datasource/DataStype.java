package com.oseasy.com.pcore.common.aop.datasource;

import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2019/4/3 0003.
 */
public enum DataStype {
    // 主库
    MASTER("masterDataSource", true),
    // 从库
    SLAVE("slaveDataSource", false);

    // 数据源名称
    private String name;
    // 是否是默认数据源
    private boolean master;

    DataStype(String name, boolean master) {
        this.name = name;
        this.master = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getDefault() {
        String defaultDataSource = null;
        if (StringUtil.isNotEmpty(defaultDataSource)) {
            return defaultDataSource;
        }

        for (DataStype dstype : DataStype.values()) {
            if (dstype.master) {
                defaultDataSource = dstype.getName();
            }
        }
        return defaultDataSource;
    }
}