/**
 * .
 */

package com.oseasy.cas.common.config;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.com.common.utils.IEu;

/**
 * CAS系统模块常量类.
 * @author chenhao
 *
 */
public class CasSval extends Sval{
    public static CasPath path = new CasPath();
    public static CasCkey ck = new CasCkey();

    public static final String CAS_LOGINOPEN = "cas.login.open";
    private static final String CAS_TYPES = "cas.login.types";
    private static final String CAS_TIME_MAX = "cas.login.maxTime";
    private static final String CAS_HASLOGIN = "cas.login.hasLogin";
    private static final String CAS_LOGINURL = "cas.login.url";
    private static final String CAS_LOGOUTURL = "cas.logout.url";
    public static final String CAS_LOGOUT_URL = "logoutUrl";
    public static final String VIEWS_CAS_LOGIN = "/cas/caslogin";
    public static final String CAS_LOGOUT = "http://authserver.uta.edu.cn/authserver/logout";

    public enum CasEmskey implements IEu {
        CAS("cas", "CAS单点登录");

        private String key;//url
        private String remark;
        private CasEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (CasEmskey entity : CasEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (CasEmskey entity : CasEmskey.values()) {
                entitys.add(new CkeyMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }
        public String k() {
            return key;
        }

        public String getRemark() {
            return remark;
        }
    }

    /**
     * 获取CAS平台登录开关.
     */
    public static Boolean getCasLoginOpen() {
        String loginOpen = CoreSval.getConfig(CAS_LOGINOPEN);
        return  ((Const.YES).equals(loginOpen) ? true : false);
    }

    /**
     * 获取CAS平台登录URL.
     */
    public static String getCasLoginUrl() {
        String loginUrl = CoreSval.getConfig(CAS_LOGINURL);
        return StringUtil.isEmpty(loginUrl) ? CoreSval.LOGIN_FRONT : loginUrl;
    }

    /**
     * 获取CAS平台登出URL.
     */
    public static String getCasLogoutUrl() {
        return CoreSval.getConfig(CAS_LOGOUTURL);
    }
    /**
     * 获取CAS平台登录开关.
     */
    public static Boolean getCasHasLogin() {
        if(getCasLoginOpen()){
            String hasLogin = CoreSval.getConfig(CAS_HASLOGIN);
            return ((Const.TRUE).equals(hasLogin)) ? true : false;
        }
        return true;
    }

    /**
     * 获取CAS平台登录最大失败次数.
     */
    public static Integer getCasMaxTime() {
        Integer maxTime = CoreSval.Prop.getInitiateConfigInt(CAS_TIME_MAX);
        return (maxTime != null) ? maxTime : 3;
    }
    /**
     * 获取CAS平台类型.
     */
    public static List<String> getCasTypes() {
        String types = CoreSval.getConfig(CAS_TYPES);
        if(StringUtil.isNotEmpty(types)){
            return Arrays.asList(StringUtil.split(types, StringUtil.DOTH));
        }
        return Lists.newArrayList();
    }
}
