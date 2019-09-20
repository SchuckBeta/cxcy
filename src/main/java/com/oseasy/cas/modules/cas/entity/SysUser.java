/**
 * .
 */

package com.oseasy.cas.modules.cas.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.cas.modules.cas.vo.CheckRet;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.Retype;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 */
public class SysUser extends DataEntity<SysUser> implements IidEntity{
    private static final long serialVersionUID = 1L;
    private String type;    //用户类型，学生、导师（数据新增、修改使用）
    private User user;    //User
    private SysCasUser casUser;    //CasUser
    private Integer checkRet;    //检查用户是否存在结果
    private Integer checkRetRole;    //检查角色是否存在结果
    private Integer checkRetST;    //检查学生、导师是否存在结果
    private String retype;    //Cas重定向类型
    private String returl;    //Cas重定向URL
    private Boolean cas;    //Cas登录校验状态

    public SysUser() {
        super();
    }

    public SysUser(SysCasUser casUser) {
        super();
        this.casUser = casUser;
    }

    public SysUser(User user) {
        super();
        this.user = user;
    }

    public SysUser(User user, Integer checkRet) {
        super();
        this.user = user;
        this.checkRet = checkRet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRetype() {
        return retype;
    }

    public void setRetype(String retype) {
        this.retype = retype;
    }

    public Integer getCheckRetRole() {
        return checkRetRole;
    }

    public void setCheckRetRole(Integer checkRetRole) {
        this.checkRetRole = checkRetRole;
    }

    public Integer getCheckRetST() {
        return checkRetST;
    }

    public void setCheckRetST(Integer checkRetST) {
        this.checkRetST = checkRetST;
    }

    public String getReturl() {
        if(StringUtil.isNotEmpty(this.retype)){
            Retype retype = Retype.getByKey(this.retype);
            this.returl = ((retype == null) ? "" : retype.getUrl());
        }
        return this.returl;
    }

    public void setReturl(String returl) {
        this.returl = returl;
    }

    public SysCasUser getCasUser() {
        return casUser;
    }

    public void setCasUser(SysCasUser casUser) {
        this.casUser = casUser;
    }

    public Integer getCheckRet() {
        return checkRet;
    }
    public void setCheckRet(Integer checkRet) {
        this.checkRet = checkRet;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Boolean isCas() {
        return cas;
    }

    public void setCas(Boolean cas) {
        this.cas = cas;
    }

    public String getLog() {
        return this.user.getLoginName() + StringUtil.MAOH + this.user.getId();
    }

    public static SysUser updateCheck(SysUser suser, SysCasUser casUser) {
        if((casUser.getCheckRetUser() != null)){
            suser.setCheckRet(casUser.getCheckRetUser());
        }

        if((casUser.getCheckRetRole() != null)){
            suser.setCheckRetRole(casUser.getCheckRetRole());
        }

        if((casUser.getCheckRetST() != null)){
            suser.setCheckRetST(casUser.getCheckRetST());
        }

        if(StringUtil.isEmpty(suser.getType())){
            suser.setType(casUser.getRutype());
        }
        return suser;
    }

    public static List<SysUser> convertSysUser(List<User> users) {
        List<SysUser> sysusers = Lists.newArrayList();
        for (User user : users) {
            sysusers.add(new SysUser(user, CheckRet.FALSE.getKey()));
        }
        return sysusers;
    }

    public static List<User> convertUser(List<SysUser> users) {
        List<User> sysusers = Lists.newArrayList();
        for (SysUser user : users) {
            sysusers.add(user.getUser());
        }
        return sysusers;
    }
}
