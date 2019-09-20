package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvRole extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 所属机构.
     */
    private SytmvOffice sytmvOffice;

    /**
     * 需要授权的菜单.
     */
    private List<Menu> menus;
    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/
    /**
     * 所有角色.
     */
    private List<Role> roles;

    public SytmvRole(SytmvOffice sytmvOffice) {
        this.sytmvOffice = sytmvOffice;
    }

    public SytmvOffice getSytmvOffice() {
        return sytmvOffice;
    }

    public void setSytmvOffice(SytmvOffice sytmvOffice) {
        this.sytmvOffice = sytmvOffice;
    }

    public List<Role> getRoles() {
        if(this.roles == null){
            this.roles = Lists.newArrayList();
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
