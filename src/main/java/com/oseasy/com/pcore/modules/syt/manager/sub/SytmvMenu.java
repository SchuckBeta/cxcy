package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvMenu extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 所属机构.
     */
    private SytmvTenant sytmvTenant;
    /**
     * 处理的角色.
     */
    private SytmvRole sytmvRole;

    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/
    /**
     * 所有用户.
     */
    private List<Menu> menus;

//    public SytmvMenu(SytmvTenant sytmvTenant) {
//        this.sytmvTenant = sytmvTenant;
//    }
    public SytmvMenu(SytmvTenant sytmvTenant, SytmvRole sytmvRole) {
        this.sytmvTenant = sytmvTenant;
        this.sytmvRole = sytmvRole;
    }

    public SytmvTenant getSytmvTenant() {
        return sytmvTenant;
    }

    public void setSytmvTenant(SytmvTenant sytmvTenant) {
        this.sytmvTenant = sytmvTenant;
    }

    public SytmvRole getSytmvRole() {
        return sytmvRole;
    }

    public void setSytmvRole(SytmvRole sytmvRole) {
        this.sytmvRole = sytmvRole;
    }

    public List<Menu> getMenus() {
        if(this.menus == null){
            this.menus = Lists.newArrayList();
        }
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
