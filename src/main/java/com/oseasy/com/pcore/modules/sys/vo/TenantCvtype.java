package com.oseasy.com.pcore.modules.sys.vo;


/**
 * Created by Administrator on 2019/4/28 0028.
 */
public enum TenantCvtype{
    CONFIG("tc_config", "配置"),
    WWW("tc_www", "域名"),
    USER("tc_user", "登录用户"),
    CHANGE("tc_change", "切换用户");

    private String key;
    private String remarks;

    private TenantCvtype(String key, String remarks) {
        this.key = key;
        this.remarks = remarks;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public static TenantCvtype getBykey(String key) {
        TenantCvtype[] entitys = TenantCvtype.values();
        for (TenantCvtype entity : entitys) {
            if ((key).equals(entity.getKey())) {
                return entity;
            }
        }
        return null;
    }
}
