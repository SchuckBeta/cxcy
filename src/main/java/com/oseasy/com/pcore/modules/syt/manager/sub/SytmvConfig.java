package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.SysConfig;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvConfig extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 所属机构.
     */
    private SytmvTenant sytmvTenant;

    private List<SysConfig> sysConfigs;

    public SytmvConfig(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }

    public SytmvTenant getSytmvTenant() {
        return sytmvTenant;
    }

    public void setSytmvTenant(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }

    public List<SysConfig> getSysConfigs() {
        if(this.sysConfigs == null){
            this.sysConfigs = Lists.newArrayList();
        }
        return sysConfigs;
    }

    public void setSysConfigs(List<SysConfig> sysConfigs) {
        this.sysConfigs = sysConfigs;
    }
}
