package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvDict extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 所属机构.
     */
    private SytmvTenant sytmvTenant;

    private List<Dict> menus;

    public SytmvDict(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }

    public SytmvTenant getSytmvTenant() {
        return sytmvTenant;
    }

    public void setSytmvTenant(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }



    public List<Dict> getMenus() {
        if(this.menus == null){
            this.menus = Lists.newArrayList();
        }
        return menus;
    }

    public void setMenus(List<Dict> menus) {
        this.menus = menus;
    }
}
