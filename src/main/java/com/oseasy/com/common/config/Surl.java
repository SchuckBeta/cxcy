package com.oseasy.com.common.config;

/**
 * 系统固定URL.
 * @author Administrator
 */
public enum Surl {
    F_ROOT("/f", "前台"),
    A_ROOT("/a", "后台");

    private String url;
    private String remark;
    private Surl(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }

    public String getUrl() {
        return url;
    }

    public String getRemark() {
        return remark;
    }

    public static Surl getByUrl(String val) {
        Surl[] entitys = Surl.values();
        for (Surl entity : entitys) {
            if ((val).equals(entity.getUrl())) {
                return entity;
            }
        }
        return null;
    }
}
