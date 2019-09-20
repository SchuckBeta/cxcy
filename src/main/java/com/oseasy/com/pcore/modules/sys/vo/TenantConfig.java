package com.oseasy.com.pcore.modules.sys.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.CookieUtils;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.UrlUtil;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class TenantConfig implements Serializable{
    @JsonIgnore
    private User cuser;//UserUtils.getUser()
    @JsonIgnore
    private HttpServletRequest request;//HttpServletRequest
    @JsonIgnore
    private HttpServletResponse response;//HttpServletResponse
    private String changeTid;//当前切换租户ID
    private boolean isFirst;//是否第一次显示
    private boolean showTenantPop;//是否显示选择租户
    private boolean showActrel;//是否显示流程关联功能
    private String currpn;//当前平台
    private String currWport;//当前域名+端口
    private TenantCvo wwwTcvo;//租户域名缓存配置
    private TenantCvo userTcvo;//租户用户缓存配置
    private TenantCvo changeTcvo;//租户切换缓存配置
    private Boolean isShowTenant; // 是否显示租户下拉框
    private Boolean showOffice; // 是否显示机构

    public TenantConfig() {
        this.showTenantPop = true;
        this.isFirst = false;
        this.showOffice = false;
    }

    public TenantConfig(HttpServletRequest request) {
        this.showTenantPop = true;
        this.isFirst = false;
        this.showOffice = false;
    }

    public String getCurrWport() {
        return currWport;
    }

    public void setCurrWport(String currWport) {
        this.currWport = currWport;
    }

    public String getCurrpn() {
        return currpn;
    }

    public void setCurrpn(String currpn) {
        this.currpn = currpn;
    }

    public boolean getShowActrel() {
        return showActrel;
    }

    public void setShowActrel(boolean showActrel) {
        this.showActrel = showActrel;
    }

    public User getCuser() {
        return cuser;
    }

    public void setCuser(User cuser) {
        this.cuser = cuser;
    }

    public String getChangeTid() {
        return changeTid;
    }

    public void setChangeTid(String changeTid) {
        this.changeTid = changeTid;
    }

    public boolean isShowTenantPop() {
        return showTenantPop;
    }

    public void setShowTenantPop(boolean showTenantPop) {
        this.showTenantPop = showTenantPop;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public TenantCvo getWwwTcvo() {
        return wwwTcvo;
    }

    public void setWwwTcvo(TenantCvo wwwTcvo) {
        this.wwwTcvo = wwwTcvo;
    }

    public TenantCvo getUserTcvo() {
        return userTcvo;
    }

    public void setUserTcvo(TenantCvo userTcvo) {
        this.userTcvo = userTcvo;
    }

    public TenantCvo getChangeTcvo() {
        return changeTcvo;
    }

    public void setChangeTcvo(TenantCvo changeTcvo) {
        this.changeTcvo = changeTcvo;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Boolean getShowTenant() {
        return isShowTenant;
    }

    public void setShowTenant(Boolean showTenant) {
        isShowTenant = showTenant;
    }

    public Boolean getShowOffice() {
        return showOffice;
    }

    public void setShowOffice(Boolean showOffice) {
        this.showOffice = showOffice;
    }

    /**
     * 保存到缓存之前需要重置该属性，否则可能json报错.
     * @param cur TenantConfig
     * @return TenantConfig
     */
    public static TenantConfig reset(TenantConfig cur){
        cur.setRequest(null);
        cur.setResponse(null);
        cur.setCuser(null);
        cur.setChangeTid(null);
        return cur;
    }


    /**
     * 获取域名Key.
     * @param request HttpServletRequest
     * @return String
     */
    public static String genWwwKey(HttpServletRequest request){
        String sessionId = null;
        if(CoreUtils.getSession() != null){
            sessionId = CoreUtils.getSession().getId().toString();
        }

        StringBuffer key = new StringBuffer();
        String www = UrlUtil.wwwPortKey(request);
        if(StringUtil.isNotEmpty(www)){
            key.append(www);
        }else{
            key.append(TenantCvtype.CONFIG.getKey());
        }

        if(StringUtil.isNotEmpty(sessionId)){
            key.append(StringUtil.LINE_D);
            key.append(sessionId);
        }

        key.append(StringUtil.LINE_D);
        key.append(CoreSval.getTenantCurrpn());
        return key.toString();
    }

    /**
     * 获取当前登录用户的缓存Key.
     * @param tcvtype TenantCvtype
     * @param user User
     * @param iswww boolean
     * @return String
     */
    public static String genKey(TenantCvtype tcvtype, User user, boolean iswww){
        String sessionId = null;
        if(CoreUtils.getSession() != null){
            sessionId = CoreUtils.getSession().getId().toString();
        }

        StringBuffer key = new StringBuffer();
        if(tcvtype != null){
            key.append(tcvtype.getKey());
        }else{
            key.append(TenantCvtype.USER.getKey());
        }

        if(StringUtil.isNotEmpty(sessionId)){
            key.append(StringUtil.LINE_D);
            key.append(sessionId);
        }

        key.append(StringUtil.LINE_D);
        key.append(CoreSval.getTenantCurrpn());

        if(iswww) {
            return key.toString();
        }

        key.append(StringUtil.LINE_D);
        key.append(user.getTenantId());

        key.append(StringUtil.LINE_D);
        key.append(user.getId());
        return key.toString();
    }
    public static String genKey(TenantCvtype tcvtype, User user){
        return genKey(tcvtype, user, false);
    }

    /**
     * 获取当前域名配置缓存Key.
     * 1、没有登录 Key + 平台
     * 2、登录成功 Key + 平台 + 登录租户ID + 用户ID
     * @param user User
     * @return String
     */
    public static String genConfigKey(User user, boolean iswww){
        String sessionId = null;
        if(CoreUtils.getSession() != null){
            sessionId = CoreUtils.getSession().getId().toString();
        }

        StringBuffer key = new StringBuffer(TenantCvtype.WWW.getKey());
        if(StringUtil.isNotEmpty(sessionId)){
            key.append(StringUtil.LINE_D);
            key.append(sessionId);
        }

        key.append(StringUtil.LINE_D);
        key.append(CoreSval.getTenantCurrpn());
        if((user == null) || StringUtil.isEmpty(user.getTenantId()) || StringUtil.isEmpty(user.getId()) || iswww){
            return key.toString();
        }

        key.append(StringUtil.LINE_D);
        key.append(user.getTenantId());
        key.append(StringUtil.LINE_D);
        key.append(user.getId());
        return key.toString();
    }
    public static String genConfigKey(User user){
        return genConfigKey(user, false);
    }

    /**
     * 初始化配置.
     * @param tcvtype TenantCvtype
     * @param tconfig TenantConfig
     * @return TenantConfig
     */
    public static TenantConfig initTcvo(TenantCvtype tcvtype, TenantConfig tconfig) {
        TenantCvo tcvo;
        SysTenant sysTenant;
        if((TenantCvtype.WWW).equals(tcvtype)){
            tcvo = tconfig.getWwwTcvo();
            if(tcvo == null){
                tcvo = new TenantCvo();
            }

            if((tconfig.getRequest() != null)){
                tcvo.setKey(genWwwKey(tconfig.getRequest()));
            }

            if(StringUtil.isEmpty(tcvo.getKey())){
                return tconfig;
            }

            sysTenant = CoreUtils.getTenant(UrlUtil.wwwPort(tconfig.getRequest()));
            if((sysTenant != null) && StringUtil.isNotEmpty(sysTenant.getTenantId())){
                tcvo.setVal(sysTenant.getTenantId());
                tcvo.setType(sysTenant.getType());
//                if((Sval.EmPn.NCENTER.getKey()).equals(CoreSval.getTenantCurrpn())){
//                    tconfig.setCurrTenantId(CoreIds.NCE_SYS_TENANT.getId());
//                }else if((Sval.EmPn.NPROVINCE.getKey()).equals(CoreSval.getTenantCurrpn())){
//                    tconfig.setCurrTenantId(CoreIds.NPR_SYS_TENANT.getId());
//                }else if((Sval.EmPn.NSCHOOL.getKey()).equals(CoreSval.getTenantCurrpn())){}
            }
            tconfig.setWwwTcvo(tcvo);
        }else if((TenantCvtype.USER).equals(tcvtype)){
            tcvo = tconfig.getUserTcvo();
            if(tcvo == null){
                tcvo = new TenantCvo();
            }

            if((tconfig.getCuser() != null) && StringUtil.isNotEmpty(tconfig.getCuser().getTenantId())){
                sysTenant = CoreUtils.getTenantById(tconfig.getCuser().getTenantId());
                if((sysTenant != null) && StringUtil.isNotEmpty(sysTenant.getTenantId())){
                    tcvo.setType(sysTenant.getType());
                }
                tcvo.setKey(genKey(TenantCvtype.USER, tconfig.getCuser()));
                tcvo.setVal(tconfig.getCuser().getTenantId());
            }

            tconfig.setUserTcvo(tcvo);
        }else if((TenantCvtype.CHANGE).equals(tcvtype)){
            tcvo = tconfig.getChangeTcvo();
            if(tcvo == null){
                tcvo = new TenantCvo();
            }

            if(StringUtil.isNotEmpty(tconfig.getChangeTid())){
                sysTenant = CoreUtils.getTenantById(tconfig.getChangeTid());
                if((sysTenant != null) && StringUtil.isNotEmpty(sysTenant.getTenantId())){
                    tcvo.setType(sysTenant.getType());
                }

                tcvo.setKey(genKey(TenantCvtype.CHANGE, tconfig.getCuser()));
                tcvo.setVal(tconfig.getChangeTid());
                if(tconfig.getIsFirst()){
                    tconfig.setShowTenantPop(true);
                }else{
                    tconfig.setShowTenantPop(false);
                }
            }
            tconfig.setChangeTcvo(tcvo);
        }

        tconfig.setCurrpn(CoreSval.getTenantCurrpn());
        if(CoreSval.getTenantCurrpn().equals(CoreIds.NCE_SYS_TENANT_TPL.getId())){
            tconfig.setShowTenant(true);
        }else{
            tconfig.setShowTenant(false);
        }

        if((Sval.EmPn.NCENTER.getPrefix()).equals(tconfig.getCurrpn())){
            tconfig.setShowActrel(false);
            if((tconfig.getChangeTcvo() != null) && StringUtil.isNotEmpty(tconfig.getChangeTcvo().getType())) {
//                if((CoreSval.EmTenant.NCE.getKey()).equals(tconfig.getChangeTcvo().getType()) || (CoreSval.EmTenant.NPR.getKey()).equals(tconfig.getChangeTcvo().getType())){
//                    tconfig.setShowActrel(true);
//                }
                if((CoreSval.EmTenant.NPR.getKey()).equals(tconfig.getChangeTcvo().getType())){
                    tconfig.setShowActrel(true);
                }
            }

            if((!tconfig.getShowActrel()) && (tconfig.getUserTcvo() != null) && StringUtil.isNotEmpty(tconfig.getUserTcvo().getType())) {
//                if((CoreSval.EmTenant.NCE.getKey()).equals(tconfig.getUserTcvo().getType()) || (CoreSval.EmTenant.NPR.getKey()).equals(tconfig.getUserTcvo().getType())){
//                    tconfig.setShowActrel(true);
//                }
                if((CoreSval.EmTenant.NPR.getKey()).equals(tconfig.getUserTcvo().getType())){
                    tconfig.setShowActrel(true);
                }
            }

            if((tconfig.getChangeTcvo() == null) || StringUtil.isEmpty(tconfig.getChangeTcvo().getVal()) || tconfig.getIsFirst()){
                tconfig.setShowTenantPop(true);
            }else{
                tconfig.setShowTenantPop(false);
            }
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(tconfig.getCurrpn())){
            tconfig.setShowActrel(true);
            tconfig.setShowTenantPop(false);
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(tconfig.getCurrpn())){
            tconfig.setShowActrel(false);
            tconfig.setShowTenantPop(false);
        }

        if(StringUtil.isEmpty(tconfig.getCurrWport())){
            tconfig.setCurrWport(UrlUtil.wwwPorts(tconfig.getRequest()));
        }
        return tconfig;
    }

    /**
     * 初始化租户登录缓存.
     * @param request ServletRequest
     * @param response ServletResponse
     */
    public static void initLoginCache(ServletRequest request, ServletResponse response) {
        if(request instanceof HttpServletRequest){
            TenantConfig config = TenantConfig.getConfig();
            config.setRequest((HttpServletRequest)request);
            config.setResponse((HttpServletResponse) response);
            config.setCuser(UserUtils.getUser());
            TenantConfig.initCache(config, TenantCvtype.USER);
            if(((Sval.EmPn.NCENTER.getPrefix()).equals(CoreSval.getTenantCurrpn())) && !User.isSuper(config.getCuser())){
                /**
                 * 设置默认切换租户为省租户.
                 */
                config.setIsFirst(true);
                config.setChangeTid(CoreIds.NPR_SYS_TENANT.getId());
                TenantConfig.initCache(config, TenantCvtype.CHANGE);
            }
            System.out.println("===================================================================");
            System.out.println(">>登录前租户缓存JSON为："+ TenantConfig.getConfig().toString());
            System.out.println("===================================================================");
        }
    }

    /**
     * 更新缓存.
     * 1、如果 tcvtype 为空,则更新所有Key的属性.
     * 2、如果 tcvtype 不为空，则更新相应的Key的属性.
     * @param tconfig TenantConfig
     * @param tcvtype TenantCvtype
     * @return TenantConfig
     */
    public static TenantConfig initCache(TenantConfig tconfig, TenantCvtype tcvtype){
        TenantConfig userConfig = TenantConfig.getConfig();
        if((tconfig.getUserTcvo() == null)) {
            tconfig.setUserTcvo(userConfig.getUserTcvo());
        }
        if((tconfig.getChangeTcvo() == null)) {
            tconfig.setChangeTcvo(userConfig.getChangeTcvo());
        }
        if((tconfig.getWwwTcvo() == null)) {
            tconfig.setWwwTcvo(TenantConfig.getConfig(null, true).getWwwTcvo());
        }

        tconfig = initTcvo(tcvtype, tconfig);
        TenantCvo tcvo;
        if((TenantCvtype.WWW).equals(tcvtype)){
            tcvo = tconfig.getWwwTcvo();
            if(StringUtil.isNotEmpty(tcvo.getKey())){
                CacheUtils.remove(tcvo.getKey());
                CacheUtils.put(tcvo.getKey(), tcvo.getVal());
                CookieUtils.setCookie(tconfig.getResponse(), tcvo.getKey(), tcvo.getVal());
            }
        }else if((TenantCvtype.USER).equals(tcvtype)){
            tcvo = tconfig.getUserTcvo();
            if(StringUtil.isNotEmpty(tcvo.getKey())){
                CacheUtils.remove(tcvo.getKey());
                CacheUtils.put(tcvo.getKey(), tcvo.getVal());
                CookieUtils.setCookie(tconfig.getResponse(), tcvo.getKey(), tcvo.getVal());
            }
        }else if((TenantCvtype.CHANGE).equals(tcvtype)){
            tcvo = tconfig.getChangeTcvo();
            if(StringUtil.isNotEmpty(tcvo.getKey())){
                CacheUtils.remove(tcvo.getKey());
                CacheUtils.put(tcvo.getKey(), tcvo.getVal());
                CookieUtils.setCookie(tconfig.getResponse(), tcvo.getKey(), tcvo.getVal());
            }
        }else{
            if((tconfig.getWwwTcvo() != null) && StringUtil.isNotEmpty(tconfig.getWwwTcvo().getKey())){
                CacheUtils.remove(tconfig.getWwwTcvo().getKey());
                CacheUtils.put(tconfig.getWwwTcvo().getKey(), tconfig.getWwwTcvo().getVal());
                CookieUtils.setCookie(tconfig.getResponse(), tconfig.getWwwTcvo().getKey(), tconfig.getWwwTcvo().getVal());
            }

            if((tconfig.getUserTcvo() != null) && StringUtil.isNotEmpty(tconfig.getUserTcvo().getKey())){
                CacheUtils.remove(tconfig.getUserTcvo().getKey());
                CacheUtils.put(tconfig.getUserTcvo().getKey(), tconfig.getUserTcvo().getVal());
                CookieUtils.setCookie(tconfig.getResponse(), tconfig.getUserTcvo().getKey(), tconfig.getUserTcvo().getVal());
            }

            if((tconfig.getChangeTcvo() != null) && StringUtil.isNotEmpty(tconfig.getChangeTcvo().getKey())){
                CacheUtils.remove(tconfig.getChangeTcvo().getKey());
                CacheUtils.put(tconfig.getChangeTcvo().getKey(), tconfig.getChangeTcvo().getVal());
                CookieUtils.setCookie(tconfig.getResponse(), tconfig.getChangeTcvo().getKey(), tconfig.getChangeTcvo().getVal());
            }
        }

        tconfig = updateConfig(tconfig);
        System.out.println("===================================================================");
        System.out.println("当前租户缓存JSON为："+ TenantConfig.getConfig().toString());
        System.out.println("===================================================================");
        return tconfig;
    }

    /**
     * 更新配置.
     * @param tconfig TenantConfig
     * @return TenantConfig
     */
    public static TenantConfig updateConfig(TenantConfig tconfig) {
        TenantConfig cur = new TenantConfig();
        BeanUtils.copyProperties(tconfig, cur);
        cur = TenantConfig.reset(cur);
        CacheUtils.put(TenantConfig.genConfigKey(tconfig.getCuser()), cur);
        CookieUtils.setCookie(tconfig.getResponse(), TenantConfig.genConfigKey(tconfig.getCuser()), cur.toString());
        return cur;
    }

    /**
     * 清理缓存和Cookie.
     * @param request ServletRequest
     * @param sresponse ServletResponse
     * @param tcvtype TenantCvtype
     */
    public static void clearCache(HttpServletRequest request, ServletResponse sresponse, TenantCvtype tcvtype){
        HttpServletResponse response = (HttpServletResponse) sresponse;
        TenantConfig tconfig = getConfig();
        if(tconfig == null){
            tconfig = new TenantConfig();
            tconfig.setCuser(UserUtils.getUser());
            tconfig.setRequest(request);
            tconfig.setResponse(response);
            tconfig = initTcvo(tcvtype, tconfig);
        }else{
            tconfig.setCuser(UserUtils.getUser());
            tconfig.setRequest(request);
            tconfig.setResponse(response);
        }

        boolean clearCookie = false;
        if((tconfig.getResponse() != null)){
            clearCookie = true;
        }

        TenantCvo tcvo;
        if((tcvtype != null)){
            if((TenantCvtype.WWW).equals(tcvtype)){
                tcvo = tconfig.getWwwTcvo();
                if((tcvo != null) && StringUtil.isNotEmpty(tcvo.getKey())){
                    CacheUtils.remove(tcvo.getKey());
                    if(clearCookie){
                        CookieUtils.setCookie(tconfig.getResponse(), tcvo.getKey(), StringUtil.EMPTY);
                    }
                    tconfig.setWwwTcvo(null);
                }
            }else if((TenantCvtype.USER).equals(tcvtype)){
                tcvo = tconfig.getUserTcvo();
                if((tcvo != null) && StringUtil.isNotEmpty(tcvo.getKey())){
                    CacheUtils.remove(tcvo.getKey());
                    if(clearCookie) {
                        CookieUtils.setCookie(tconfig.getResponse(), tcvo.getKey(), StringUtil.EMPTY);
                    }
                    tconfig.setUserTcvo(null);
                }
            }else if((TenantCvtype.CHANGE).equals(tcvtype)) {
                tcvo = tconfig.getChangeTcvo();
                if ((tcvo != null) && StringUtil.isNotEmpty(tcvo.getKey())) {
                    CacheUtils.remove(tcvo.getKey());
                    if(clearCookie) {
                        CookieUtils.setCookie(tconfig.getResponse(), tcvo.getKey(), StringUtil.EMPTY);
                    }
                    tconfig.setChangeTcvo(null);
                }
            }
        }else{
            if((tconfig.getWwwTcvo() != null) && StringUtil.isNotEmpty(tconfig.getWwwTcvo().getKey())){
                CacheUtils.remove(tconfig.getWwwTcvo().getKey());
                if(clearCookie) {
                    CookieUtils.setCookie(tconfig.getResponse(), tconfig.getWwwTcvo().getKey(), StringUtil.EMPTY);
                }
                tconfig.setWwwTcvo(null);
            }

            if((tconfig.getUserTcvo() != null) && StringUtil.isNotEmpty(tconfig.getUserTcvo().getKey())){
                CacheUtils.remove(tconfig.getUserTcvo().getKey());
                if(clearCookie) {
                    CookieUtils.setCookie(tconfig.getResponse(), tconfig.getUserTcvo().getKey(), StringUtil.EMPTY);
                }
                tconfig.setUserTcvo(null);
            }

            if((tconfig.getChangeTcvo() != null) && StringUtil.isNotEmpty(tconfig.getChangeTcvo().getKey())){
                CacheUtils.remove(tconfig.getChangeTcvo().getKey());
                if(clearCookie) {
                    CookieUtils.setCookie(tconfig.getResponse(), tconfig.getChangeTcvo().getKey(), StringUtil.EMPTY);
                }
                tconfig.setChangeTcvo(null);
            }
        }
        updateConfig(tconfig);
    }

    /**
     * 清理缓存.
     * @param tcvtype TenantCvtype
     */
    public static void clearCache(TenantCvtype tcvtype){
        clearCache(null, null, tcvtype);
    }
    public static void clearCache(HttpServletRequest request, ServletResponse sresponse){
        clearCache(request, sresponse, TenantCvtype.CHANGE);
        clearCache(request, sresponse, TenantCvtype.USER);
        clearCache(request, sresponse, TenantCvtype.WWW);
        clearCache(request, sresponse, TenantCvtype.CONFIG);
    }

    /**
     * 获取缓存.
     * @param tcvtype TenantCvtype
     * @return
     */
    public static String getCache(TenantCvtype tcvtype){
        return getCache(tcvtype, CoreUtils.getUser());
    }
    public static String getCache(TenantCvtype tcvtype, User user){
        TenantConfig tconfig = getConfig(user);
        if(tconfig == null){
            return StringUtil.EMPTY;
        }

        if((TenantCvtype.WWW).equals(tcvtype) && (tconfig.getWwwTcvo() != null)){
            return (String)CacheUtils.get(tconfig.getWwwTcvo().getKey());
        }else if((TenantCvtype.USER).equals(tcvtype) && (tconfig.getUserTcvo() != null)){
            return (String)CacheUtils.get(tconfig.getUserTcvo().getKey());
        }else if((TenantCvtype.CHANGE).equals(tcvtype) && (tconfig.getChangeTcvo() != null) && !tconfig.getIsFirst()){
            return (String)CacheUtils.get(tconfig.getChangeTcvo().getKey());
        }
        return StringUtil.EMPTY;
    }


    public static String getCacheTenant() {
            /**
             * 获取切换租户ID.
             */
            String curTenant = TenantConfig.getCache(TenantCvtype.CHANGE);

            /**
             * 获取登录租户ID.
             */
            if(StringUtil.isEmpty(curTenant)){
                curTenant = TenantConfig.getCache(TenantCvtype.USER);
            }

            /**
             * 获取域名租户ID.
             */
            if(StringUtil.isEmpty(curTenant)){
                curTenant = TenantConfig.getCache(TenantCvtype.WWW);
            }
            return curTenant;
        }

        public static String getCacheTenant(String uid) {
            User user = new User(uid);
            /**
             * 获取切换租户ID.
             */
            String curTenant = TenantConfig.getCache(TenantCvtype.CHANGE, user);

            /**
             * 获取登录租户ID.
             */
            if(StringUtil.isEmpty(curTenant)){
                curTenant = TenantConfig.getCache(TenantCvtype.USER, user);
            }

            /**
             * 获取域名租户ID.
             */
            if(StringUtil.isEmpty(curTenant)){
                curTenant = TenantConfig.getCache(TenantCvtype.WWW, user);
            }
            return curTenant;
        }

    /**
     * 从缓存中获取配置.
     * @return TenantConfig
     */
    public static TenantConfig getConfig(User user, boolean iswww) {
        String key = TenantConfig.genConfigKey(user, iswww);
        System.out.println("当前租户缓存Key = "+key);
        Object ocache =  CacheUtils.get(key);
        if(ocache != null){
            TenantConfig config =  (TenantConfig) CacheUtils.get(key);
            return (config == null)?new TenantConfig():config;
        }
        return new TenantConfig();
    }
    public static TenantConfig getConfig(User user) {
        return getConfig(user, false);
    }
    public static TenantConfig getConfig() {
        return getConfig(UserUtils.getUser(), false);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{\"showTenantPop\":\"" + this.showTenantPop);
        if(this.wwwTcvo != null){
            buffer.append("\",\"wwwTcvo\":" + this.wwwTcvo.toString());
        }else{
            buffer.append("\",\"wwwTcvo\":{}");
        }

        if(this.userTcvo != null){
            buffer.append(",\"userTcvo\":" + this.userTcvo.toString());
        }else{
            buffer.append(",\"userTcvo\":{}");
        }
        buffer.append(",\"showOffice\":" + this.showOffice);
        buffer.append(",\"isShowTenant\":" + this.isShowTenant);
        buffer.append(",\"currWport\":" + "\""+ this.currWport +"\"");
        buffer.append(",\"currpn\":" + this.currpn);
        buffer.append(",\"isFirst\":" + this.isFirst);
        buffer.append(",\"showActrel\":" + this.showActrel);
        if(this.changeTcvo != null){
            buffer.append(",\"changeTcvo\":" + this.changeTcvo.toString());
        }else{
            buffer.append(",\"changeTcvo\":{}");
        }

        if(StringUtil.isNotEmpty(this.changeTid)){
            buffer.append(",\"changeTid\":\"" + this.changeTid + "\"}");
        }else{
            buffer.append(",\"changeTid\":\"\"}");
        }
        return buffer.toString();
    }
}
