/**
 *
 */
package com.oseasy.com.pcore.modules.sys.security;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户和密码（包含验证码）令牌类
 *
 * @version 2013-5-19
 */
public class MyUsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;
    private boolean mobileLogin;
    private String loginType;//1-短信登录,2-密码登录
    private boolean adopt;//短信验证通过
    private boolean cas;//Cas验证通过
    private  String domianURl;

    public MyUsernamePasswordToken() {
        super();
    }

    public MyUsernamePasswordToken(String username, char[] password,
                                   boolean rememberMe, String host, String captcha, boolean mobileLogin) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.mobileLogin = mobileLogin;
    }
    public MyUsernamePasswordToken(String username, char[] password,
                                   boolean rememberMe, String host, String captcha, boolean mobileLogin,String domianURl) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.mobileLogin = mobileLogin;
        this.domianURl = domianURl;
    }

    public String getDomianURl() {
        return domianURl;
    }

    public void setDomianURl(String domianURl) {
        this.domianURl = domianURl;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType.key;
    }

    public boolean isAdopt() {
        return adopt;
    }

    public void setAdopt(boolean adopt) {
        this.adopt = adopt;
    }

    public boolean isCas() {
        return cas;
    }

    public void setCas(boolean cas) {
        this.cas = cas;
    }

    public enum LoginType {
        SMS("1", "frontAuthorizingRealm", "短信登录"),
        PWD("2", "frontPasswAuthorizingRealm", "密码登录"),
        DSF("3", "frontCasRealm", "第三方CAS登录");

        private String key;
        private String unicode;
        private String value;

        LoginType(String key, String unicode, String value) {
            this.key = key;
            this.unicode = unicode;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUnicode() {
            return unicode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static LoginType getByKey(String key) {
          if (StringUtil.isNotEmpty(key)) {
            for(LoginType e: LoginType.values()) {
              if ((e.getKey()).equals(key)) {
                return e;
              }
            }
          }
          return null;
        }
    }
}