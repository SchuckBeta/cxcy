package com.oseasy.scr.modules.scr.manager.sub;

import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;
import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytmvo;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmTenant;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmvTenant;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvScr extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 机构ID.
     */
    private String id;

    /**
     * 当前租户.
     */
    private SytmvTenant sytmvTenant;

    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/


    public SytmvScr(String id) {
        this.id = id;
    }
    public SytmvScr(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SytmvTenant getSytmvTenant() {
        return sytmvTenant;
    }

    public void setSytmvTenant(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }
}
