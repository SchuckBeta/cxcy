/**
 * .
 */

package com.oseasy.com.pcore.modules.sys.enums;

import com.oseasy.com.pcore.common.config.CoreSval;

/**
 * 登录注册重定向页面类型.
 * @author chenhao
 */
public enum Retype {
    F_INDEX("100", "前台首页", "/"),
    F_LOGIN("110", "前台登录", "/toLogin"),
    F_REGISTER("120", "前台注册", "/toRegister"),
    F_REGISTER_SUCCESS("121", "前台注册成功", "/register/studentregistersuccessful"),
    F_REGISTER_FAIL("122", "前台注册失败", "/register/registerFail"),
    F_AUTH_ENABLE("140", "前台CAS登录禁止访问页", "/casRcasenable"),
    F_SELECT_UTYPE("170", "前台选择用户类型", "/casRselectUtype"),
    F_R_SUCCESS("180", "前台CAS成功页", "/casRsucess"),
    F_R_ERROR("190", "前台CAS失败页", "/casRerror"),

    B_LOGIN("200", "后台登录", "/"),
    B_INDEX("210", "后台首页", "/"),
    B_REGISTER("220", "后台注册", "/"),

    M_LOGIN("300", "移动端登录", "/login"),

    ALL_LOGIN("90", "统一登录", "login"),
    ALL_LOGIN_FAIL("91", "统一登录失败", "loginFail");

    private String key;//主题
    private String remark;//
    private String url;//路径地址


    private Retype(String key, String remark, String url) {
        this.key = key;
        this.remark = remark;
        this.url = url;
    }

    /**
     * 根据key获取枚举 .
     * @author chenhao
     * @param key
     *            页面标识
     * @return Retype
     */
    public static Retype getByKey(String key) {
        if ((key != null)) {
            Retype[] entitys = Retype.values();
            for (Retype entity : entitys) {
                if ((key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    public String getRemark() {
        return remark;
    }
}
