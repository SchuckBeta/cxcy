package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.BaseEntity;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;
import com.oseasy.util.common.utils.PinyinUtil;
import com.oseasy.util.common.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmvUser extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 所属机构.
     */
    private SytmvOffice sytmvOffice;

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

    public SytmvUser(SytmvOffice sytmvOffice) {
        this.sytmvOffice = sytmvOffice;
    }

    public SytmvUser(SytmvOffice sytmvOffice, List<Role> roles) {
        this.roles = roles;
        this.sytmvOffice = sytmvOffice;
    }

    public SytmvOffice getSytmvOffice() {
        return sytmvOffice;
    }

    public void setSytmvOffice(SytmvOffice sytmvOffice) {
        this.sytmvOffice = sytmvOffice;
    }

    public List<Role> getRoles() {
//        if(){}
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
