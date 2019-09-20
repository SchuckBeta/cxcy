package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvUserOsub extends SupSytmvo implements ISytMvo {
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 所属机构.
     */
    private SytmvOfficeSub sytmvOfficeSub;

    /**
     * 需要授权的角色.
     */
    private List<Role> roles;
    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/
    /**
     * 所有用户.
     */
    private List<User> users;

    public SytmvUserOsub(SytmvOfficeSub sytmvOfficeSub) {
        this.sytmvOfficeSub = sytmvOfficeSub;
    }

    public SytmvUserOsub(SytmvOfficeSub sytmvOfficeSub, List<Role> roles) {
        this.roles = roles;
        this.sytmvOfficeSub = sytmvOfficeSub;
    }

    public SytmvOfficeSub getSytmvOfficeSub() {
        return sytmvOfficeSub;
    }

    public void setSytmvOfficeSub(SytmvOfficeSub sytmvOfficeSub) {
        this.sytmvOfficeSub = sytmvOfficeSub;
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

    public List<User> getUsers() {
        if(this.users == null){
            this.users = Lists.newArrayList();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
