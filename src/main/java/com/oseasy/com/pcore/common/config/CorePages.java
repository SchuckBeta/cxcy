package com.oseasy.com.pcore.common.config;

import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;

/**
 * 系统固定页面.
 * @author Administrator
 */
public enum CorePages {
    ERROR_404(404, "404", "error/404"),
    ERROR_NOTOPENTENANT(900, "900", "error/notOpenTenant"),
    ERROR_MSG(2000, "2000", "error/msg"),
    BLANK_AHOME(3000, "3000", "error/ablank"),
    BLANK_ABODY(3100, "3100", "error/ablankBody"),
    ERROR_MISS(1000, "1000", "error/formMiss"),

    IDX_BACK_V3(13, "backV3", CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysIndexBackV3"),
    IDX_BACK_V4(14, "backV4", CoreSval.path.vms(CoreEmskey.SYS.k()) + "sysIndexBackV4"),
    ALL_LOGIN(90, "login", CoreSval.path.vms(CoreSval.CoreEmskey.WEBSITE.k()) + "login"),
    ALL_LOGIN_FAIL(91, "loginFail", CoreSval.path.vms(CoreSval.CoreEmskey.WEBSITE.k()) + "loginFail");

    private Integer val;
    private String idx;//url
    private String idxUrl;
    private CorePages(Integer val, String idx, String idxUrl) {
        this.val = val;
        this.idx = idx;
        this.idxUrl = idxUrl;
    }
    public String getIdx() {
        return idx;
    }
    public Integer getVal() {
        return val;
    }
    public String getIdxUrl() {
        return idxUrl;
    }
    public static CorePages getByVal(Integer val) {
        CorePages[] entitys = CorePages.values();
        for (CorePages entity : entitys) {
            if ((val).equals(entity.getVal())) {
                return entity;
            }
        }
        return null;
    }

    public static CorePages getByIdx(String idx) {
        CorePages[] entitys = CorePages.values();
        for (CorePages entity : entitys) {
            if ((entity.getIdx()).equals(idx)) {
                return entity;
            }
        }
        return null;
    }
}
