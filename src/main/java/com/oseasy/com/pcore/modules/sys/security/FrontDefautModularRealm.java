/**
 *
 */
package com.oseasy.com.pcore.modules.sys.security;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;

import com.oseasy.com.pcore.modules.sys.security.MyUsernamePasswordToken.LoginType;

/**
 * 系统安全认证实现类

 * @version 2014-7-5
 */
@Service
public class FrontDefautModularRealm extends ModularRealmAuthenticator {
  protected static final Logger LOGGER = Logger.getLogger(FrontDefautModularRealm.class);

    private Map<String, Object> definedRealms;

     /**
      * 多个realm实现
      */
     @Override
     protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
         return super.doMultiRealmAuthentication(realms, token);
     }
     /**
      * 调用单个realm执行操作
      */
     @Override
     protected AuthenticationInfo doSingleRealmAuthentication(Realm realm,AuthenticationToken token) {
         return realm.getAuthenticationInfo(token);
     }


     /**
      * 判断登录类型执行操作
      */
     @Override
     protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)throws AuthenticationException {
         this.assertRealmsConfigured();
         Realm realm = null;
         MyUsernamePasswordToken token = (MyUsernamePasswordToken) authenticationToken;
        //判断前台登录类型
         if (LoginType.SMS.getKey().equals(token.getLoginType())) {//短信
             realm = (Realm) this.definedRealms.get(LoginType.SMS.getUnicode());
         }else if (LoginType.PWD.getKey().equals(token.getLoginType())) {
             realm = (Realm) this.definedRealms.get(LoginType.PWD.getUnicode());
         }else if (LoginType.DSF.getKey().equals(token.getLoginType())) {
             realm = (Realm) this.definedRealms.get(LoginType.DSF.getUnicode());
         }

         if (realm == null) {
           LOGGER.error("前台登录类型未定义 LoginType = "+token.getLoginType());
           return null;
         }
         return this.doSingleRealmAuthentication(realm, authenticationToken);
     }

     /**
      * 判断realm是否为空
      */
     @Override
     protected void assertRealmsConfigured() throws IllegalStateException {
         this.definedRealms = this.getDefinedRealms();
         if (CollectionUtils.isEmpty(this.definedRealms)) {
             throw new AuthenticationException("值传递错误!");
         }
     }

     public Map<String, Object> getDefinedRealms() {
         return this.definedRealms;
     }

     public void setDefinedRealms(Map<String, Object> definedRealms) {
         this.definedRealms = definedRealms;
     }






}
