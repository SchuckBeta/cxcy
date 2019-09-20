/**
 * .
 */

package com.oseasy.com.pcore.modules.sys.vo;

import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.StringUtil;
import org.apache.shiro.session.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码分类.
 * @author chenhao
 */
public enum SysValCode {
    ALL_NOMARL("0001", "0", "", "所有平台通用验证码"),
    ALL_EMAIL("0100", "0", "", "所有平台邮件修改验证码"),
    VCODE_LOGIN("0200", "0", "loginVcode", "验证码登录"),
    F_LOGIN("1010", "1", "validateCode", "前台登录验证码"),
    F_MOBILE("1020", "1", "mobileVcode", "前台手机验证码"),
    F_MOBILE_ACCEPT("1050", "1", "mobile", "前台手机验证码"),
    F_ACCONUNT("1030", "1", "accountVcode", "前台账号找回验证码"),
    F_ACCONUNTMOBILE("1040", "1", "accountMvcode", "前台账号找回手机验证码"),

    A_LOGIN("2010", "2", "validateCode", "后台登录验证码"),
    ALL_LOGIN("3000", "0", "allVcode", "后台登录验证码");

    private String key;
    private String type;//平台分类：0：所有平台，1前台，2后台
    private String pkey;//参数Key
    private String name;//验证码说明

    public static final String VKEY = "validateCode";

    private SysValCode(String key, String type, String pkey, String name) {
        this.key = key;
        this.pkey = pkey;
        this.type = type;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return SysValCode
     */
    public static SysValCode getByKey(String key) {
        if ((key != null)) {
            SysValCode[] entitys = SysValCode.values();
            for (SysValCode entity : entitys) {
                if ((key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 根据Session 生成验证码登录key
     * @param session Session
     * @return String
     */
    public static String genVcodeLogin(Session session) {
        return SysValCode.VCODE_LOGIN.getPkey() + session.getId();
    }

    /**
     * 获取Session 生成验证码登录code
     * @param session Session
     * @return String
     */
    public static String getVcodeLogin(Session session) {
        return (String) CacheUtils.get(genVcodeLogin(session));
    }
    /**
     * 设置Session 生成验证码登录code
     * @param session Session
     * @return String
     */
    public static boolean putVcodeLogin(Session session, String code) {
        String cname = SysValCode.getVcodeLogin(session);
        CacheUtils.put(cname, code);
        CacheUtils.expire(cname, 600);
        return true;
    }

    /**
     * 获取验证码惟一标识Key.
     */
    public static String genVcodeKey(Session session) {
        return (String)session.getId();
    }
    public static String genVcodeKey(HttpServletRequest request) {
        return request.getSession().getId();
    }

    public static String genVcodeKey(SysValCode svcode, Session session) {
        return svcode.getKey() + StringUtil.SEPARATOR + session.getId();
    }
    public static String genVcodeKey(SysValCode svcode, HttpServletRequest request) {
        return svcode.getKey() + StringUtil.SEPARATOR + request.getSession().getId();
    }

    /**
     * 从缓存或Session中获取验证码
     * @param request HttpServletRequest
     * @param svcode SysValCode
     * @return String
     */
    public static String getVerCode(SysValCode svcode, HttpServletRequest request) {
        return (String) CacheUtils.get(genVcodeKey(svcode, request), genVcodeKey(request));
    }
    public static String getVerCode(SysValCode svcode, Session session) {
        return (String) CacheUtils.get(genVcodeKey(svcode, session), genVcodeKey(session));
    }
    public static boolean putVerCode(SysValCode svcode, HttpServletRequest request, String code) {
        String cname = SysValCode.genVcodeKey(svcode, request);
        String ckey = SysValCode.genVcodeKey(request);
        CacheUtils.put(cname, ckey, code);
        CacheUtils.expire(cname, ckey, 600);
        return true;
    }

    public static boolean putVerCode(SysValCode svcode, Session session, String code) {
        String ckey = SysValCode.genVcodeKey(session);
        String cname = SysValCode.genVcodeKey(svcode, session);
        CacheUtils.put(cname, ckey, code);
        CacheUtils.expire(cname, ckey, 600);
        return true;
    }

    public static boolean checkVerCode(SysValCode svcode, HttpServletRequest request, String vcode) {
        String cvcode = getVerCode(svcode, request);
        if(StringUtil.isEmpty(cvcode) || StringUtil.isEmpty(vcode)){
            return false;
        }
        return StringUtil.equals(cvcode.toUpperCase(), vcode.toUpperCase());
    }
    public static boolean checkVerCode(SysValCode svcode, Session session, String vcode) {
        String cvcode = getVerCode(svcode, session);
        if(StringUtil.isEmpty(cvcode) || StringUtil.isEmpty(vcode)){
            return false;
        }
        return StringUtil.equals(cvcode.toUpperCase(), vcode.toUpperCase());
    }

    public String getPkey() {
        return pkey;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"type\":\"" + this.type+ "\",\"pkey\":\"" + this.pkey + "\",\"name\":\"" + this.name + "\"}";
    }
}