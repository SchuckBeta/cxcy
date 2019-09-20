package com.oseasy.sys.modules.sys.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/7/2 0002.
 */
public class Usvo implements Serializable {
    private String id;
    private String lgType;
    private String loginType;
    private String loginName;
    private String password;
    private String allVcode;

    public String getAllVcode() {
        return allVcode;
    }

    public void setAllVcode(String allVcode) {
        this.allVcode = allVcode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLgType() {
        return lgType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public void setLgType(String lgType) {
        this.lgType = lgType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
