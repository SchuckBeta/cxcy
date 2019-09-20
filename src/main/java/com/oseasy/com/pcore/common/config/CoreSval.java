package com.oseasy.com.pcore.common.config;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.modules.authorize.vo.IAuthCheck;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.enums.Retype;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.PropertiesLoader;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统常量
 * @author Administrator
 */
public class CoreSval {
    protected static final Logger logger = LoggerFactory.getLogger(CoreSval.class);
    private static CoreSval coreSval = new CoreSval();
    public static CorePath path = new CorePath();
    public static CoreCkey ck = new CoreCkey();
    public static final String VIEW = "view";
    public static Map<IAuthCheck.AuthCtype, String> achecks = Maps.newHashMap();

    static{
        CoreSval.achecks.put(IAuthCheck.AuthCtype.MENU, "com.oseasy.com.pcore.modules.sys.service.SystemService");
        CoreSval.achecks.put(IAuthCheck.AuthCtype.CTEGROY, "com.oseasy.cms.modules.cms.service.CategoryService");
    }

    public enum CoreEmskey implements IEu {
        WEBSITE("website", "公共页面"),
        AUTHORIZE("authorize", "系统认证"),
        GEN("gen", "代码生成"),
        OFFICE("office", "机构"),
        OFFICELIST("officelist", "机构列表"),
        SYS("sys", "系统登录授权");

        private String key;//url
        private String remark;
        private CoreEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (CoreEmskey entity : CoreEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (CoreEmskey entity : CoreEmskey.values()) {
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
     * 保存全局属性值.
     */
    public static Map<String, String> map = Maps.newHashMap();
    public static Map<String, String> emailmap = Maps.newHashMap();
    private static PropertiesLoader loader = new PropertiesLoader(Prop.INITIATE.key());
    private static PropertiesLoader emailloader = new PropertiesLoader(Prop.P_EMAIL.key());

    /**
     * 重定向关键字
     */
    public static final String REDIRECT = "redirect:";
    public static final String LOGIN = CoreSval.getFrontPath() + "/toLogin";
    public static final String LOGIN_REDIRECT = REDIRECT + CoreSval.LOGIN;
    public static final String LOGIN_FRONT = CoreSval.path.vms(CoreSval.CoreEmskey.WEBSITE.k()) + "frontLogin";

    /**
     * 获取当前对象实例.
     * @return CoreSval
     */
    public static CoreSval getInstance() {
        return coreSval;
    }

    /**
     * 获取管理端根路径.
     * @return String
     */
    public static String getAdminPath() {
        return CoreSval.getConfig(CorePrkey.INITIATE_ADMIN_PATH);
    }
    public static String getRadminPath() {
        return CoreSval.REDIRECT + CoreSval.getAdminPath();
    }

    /**
     * 获取Mobile端根路径.
     */
    public static String getMobilePath() {
        return getConfig("mobilePath");
    }
    public static String getRmobilePath() {
        return CoreSval.REDIRECT + CoreSval.getMobilePath();
    }


    /**
     * 检查是否为App请求.
     * if(url!=null&&(url.endsWith("/m")||url.indexOf("/m/") > -1||"/".equals(url)))
     * @param url
     * @return
     */
    public static boolean checkMobilePath(String url) {
        return StringUtil.isNotEmpty(url) && (url.endsWith(CoreSval.getMobilePath()) || url.indexOf(CoreSval.getMobilePath() + StringUtil.LINE) > -1);
    }
    /**
     * 检查是否为App登录请求.
     * ("m/login").equals(url) || url.indexOf("/m/login?") > -1
     * @param url
     * @return
     */
    public static boolean checkMobileLogin(String url) {
        return ((CoreSval.getMobilePath() + Retype.M_LOGIN).equals(url) || (url.indexOf(CoreSval.getMobilePath() + Retype.M_LOGIN + StringUtil.WENH) > -1));
    }

    /**
     * 获取前端根路径./tf
     * @return String
     */
    public static String getProvinceFrontPath() {
        return CoreSval.getConfig(CorePrkey.INITIATE_WEB_VIEW_INDEX);
    }

    /**
     * 检查是否为管理请求.
     * if(url!=null&&(url.endsWith("/a")||url.indexOf("/a/") > -1||"/".equals(url)))
     * @param url
     * @return
     */
    public static boolean checkAdminPath(String url) {
        return StringUtil.isNotEmpty(url) && (url.endsWith(CoreSval.getAdminPath()) || url.indexOf(CoreSval.getAdminPath() + StringUtil.LINE) > -1);
    }
    /**
     * 检查是否为管理登录请求.
     * ("a/login").equals(url) || url.indexOf("/a/login?") > -1
     * @param url
     * @return
     */
    public static boolean checkAdminLogin(String url) {
        return ((CoreSval.getAdminPath() + Retype.B_LOGIN).equals(url) || (url.indexOf(CoreSval.getAdminPath() + Retype.B_LOGIN + StringUtil.WENH) > -1));
    }

    /**
     * 获取前端根路径.
     * @return String
     */
    public static String getFrontPath() {
        return CoreSval.getConfig(CorePrkey.INITIATE_FRONT_PATH);
    }
    public static String getRfrontPath() {
        return CoreSval.REDIRECT + CoreSval.getFrontPath();
    }
    /**
     * 检查是否为前台请求.
     * if(url!=null&&(url.endsWith("/f")||url.indexOf("/f/") > -1||"/".equals(url)))
     * @param url
     * @return
     */
    public static boolean checkFrontPath(String url) {
        return StringUtil.isNotEmpty(url) && (url.endsWith(CoreSval.getFrontPath())|| url.indexOf(CoreSval.getFrontPath() + StringUtil.LINE) > -1);
    }
    /**
     * 检查是否为前台登录请求.
     * ("f/toLogin").equals(url) || url.indexOf("/f/toLogin?") > -1
     * @param url
     * @return
     */
    public static boolean checkFrontLogin(String url) {
        return ((CoreSval.getFrontPath() + Retype.F_LOGIN).equals(url) || (url.indexOf(CoreSval.getFrontPath() + Retype.F_LOGIN + StringUtil.WENH) > -1));
    }

    /**
     * 获取前端IP地址.
     * @return String
     */
    public static String getSysFrontIp() {
        TenantConfig  config = TenantConfig.getConfig();
        if(config != null){
            return config.getCurrWport() + CoreSval.getFrontPath();
        }
        return CoreSval.getConfig(CorePrkey.INITIATE_SYS_FRONT_IP) + CoreSval.getFrontPath();
    }

    /**
     * 获取管理端IP地址.
     * @return String
     */
    public static String getSysAdminIp() {
        TenantConfig  config = TenantConfig.getConfig();
        if(config != null){
            return config.getCurrWport() + CoreSval.getAdminPath();
        }
        return CoreSval.getConfig(CorePrkey.INITIATE_SYS_ADMIN_IP) + CoreSval.getAdminPath();
    }

    /**
     * 是否开启手机短信验证码.
     */
    public static String getSmsHasMvcode() {
        String hasMvcode = getConfig("sms.hasMvcode");
        return StringUtil.isNotEmpty(hasMvcode) ? hasMvcode : Const.TRUE;
    }
    public static boolean isSmsHasMvcode() {
        return (getSmsHasMvcode()).equals(Const.TRUE);
    }

    /**
    /**
     * 获取URL后缀.
     * @return String
     */
    public static String getUrlSuffix() {
        return CoreSval.getConfig(CorePrkey.INITIATE_URL_SUFFIX);
    }

    /**
     * 获取租户开关.
     * @return Boolean
     */
    public static Boolean getTenantIsopen() {
        return (CoreSval.Const.TRUE).equals(CoreSval.getConfig(CorePrkey.INITIATE_TENANT_ISOPEN));
    }

    /**
     * 获取当前启动平台.
     * @return String
     */
    public static String getTenantCurrpn() {
        return CoreSval.getConfig(CorePrkey.INITIATE_TENANT_CURRPN);
    }

    /**
     * 获取当前平台的模板租户ID.
     * @return String
     */
    public static String getCurrpntplTenant() {
        String cur = CoreSval.getTenantCurrpn();
        if((Sval.EmPn.NCENTER.getPrefix()).equals(cur)){
            return CoreIds.NCE_SYS_TENANT_TPL.getId();
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(cur)){
            return CoreIds.NPR_SYS_TENANT_TPL.getId();
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(cur)){
            return CoreIds.NSC_SYS_TENANT_TPL.getId();
        }
        return null;
    }

    /**
     * 根据缓存配置获取当前平台的模板租户ID.
     * 先根据缓存获取、然后根据平台获取
     * @return String
     */
    public static String getCurrpntplTenantByType(User user) {
        TenantConfig config = TenantConfig.getConfig(user);
        if(config == null){
            return StringUtil.EMPTY;
        }

        String curType = getCurrTenantType(config);

        if((Sval.EmPn.NCENTER.getPrefix()).equals(curType)){
            return CoreIds.NCE_SYS_TENANT_TPL.getId();
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(curType)){
            return CoreIds.NPR_SYS_TENANT_TPL.getId();
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(curType)){
            return CoreIds.NSC_SYS_TENANT_TPL.getId();
        }
        return StringUtil.EMPTY;
    }

    /**
     * 获取当前租户类型.
     * @param config TenantConfig
     * @return String
     */
    public static String getCurrTenantType(TenantConfig config) {
        String curType = null;
        /**
         * 获取切换租户租户类型.
         */
        if(config.getChangeTcvo() != null){
            curType = config.getChangeTcvo().getType();
        }

        /**
         * 获取登录租户租户类型.
         */
        if(StringUtil.isEmpty(curType) && (config.getUserTcvo() != null)){
            curType = config.getUserTcvo().getType();
        }

        /**
         * 获取域名租户租户类型.
         */
        if(StringUtil.isEmpty(curType) && (config.getWwwTcvo() != null)){
            curType = config.getWwwTcvo().getType();
        }

        /**
         * 根据平台配置获取租户类型.
         */
        if(StringUtil.isEmpty(curType)){
            curType = CoreSval.getTenantCurrpn();
        }
        return curType;
    }

    /**
     * 判断是否切换租户为当前登录租户(运营平台租户).
     * @return boolean
     */
    public static boolean isChangeTcvo() {
//        TenantConfig config = TenantConfig.getConfig(CoreUtils.getUser());
//        if(config==null){
//            return false;
//        }
//        if((config.getUserTcvo() == null) || StringUtil.isEmpty(config.getUserTcvo().getVal())){
//            return false;
//        }
//
//        if((config.getChangeTcvo() == null) || StringUtil.isEmpty(config.getChangeTcvo().getVal())){
//            return false;
//        }
//        if((config.getUserTcvo().getVal()).equals(config.getChangeTcvo().getVal())){
//            return true;
//        }
//        return false;
        return isChangeTcvo(Sval.EmPn.NCENTER);
    }

    /**
     * 判断是否切换租户为当前登录租户.
     * 判断是否切换到指定平台类型租户.
     * 1、如果平台参数为空，则不处理平台类型.
     * 2、如果平台参数不为空，则判断平台类型是否为指定类型.
     * @param pn EmPn
     * @return boolean
     */
    public static boolean isChangeTcvo(Sval.EmPn pn) {
        TenantConfig config = TenantConfig.getConfig(CoreUtils.getUser());
        if(config==null){
            return false;
        }
        if((config.getUserTcvo() == null) || StringUtil.isEmpty(config.getUserTcvo().getVal())){
            return false;
        }

        if((config.getChangeTcvo() == null) || StringUtil.isEmpty(config.getChangeTcvo().getVal())){
            return false;
        }

        if((pn != null)){
            if((pn.getPrefix()).equals(config.getChangeTcvo().getType())){
                return true;
            }
        }else{
            if((config.getUserTcvo().getVal()).equals(config.getChangeTcvo().getVal())){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取是否开启前台访问.
     * @return Boolean
     */
    public static Boolean getPnfront() {
        String pnfront = CoreSval.getConfig(CorePrkey.INITIATE_TENANT_PNFRONT);
        if(getTenantIsopen() && StringUtil.isNotEmpty(pnfront) && (pnfront).contains(getTenantCurrpn())){
            return true;
        }
        return false;
    }

    /**
     * 获取是否开启后台访问.
     * @return Boolean
     */
    public static Boolean getPnAdmin() {
        String pnadmin = CoreSval.getConfig(CorePrkey.INITIATE_TENANT_PNADMIN);
        if(getTenantIsopen() && StringUtil.isNotEmpty(pnadmin) && (pnadmin).contains(getTenantCurrpn())){
            return true;
        }
        return false;
    }

    /**
     * 获取统一登录是否开端口配置.
     * @return Boolean
     */
    public static Boolean getAloginProtIsopen() {
        String cur = CoreSval.getConfig(CorePrkey.ALOGIN_PORT_ISOPEN);
        if(StringUtil.isEmpty(cur) || (Const.FALSE).equals(cur)){
            return false;
        }
        return true;
    }

    /**
     * 获取是否开端口配置.
     * @return Boolean
     */
    public static String getAloginProt(Sval.EmPn pn) {
        String cur = null;
        if((Sval.EmPn.NCENTER).equals(pn)){
            cur = CoreSval.getConfig(CorePrkey.ALOGIN_NCE_PORT);
        }else if((Sval.EmPn.NPROVINCE).equals(pn)){
            cur = CoreSval.getConfig(CorePrkey.ALOGIN_NPR_PORT);
        }else if((Sval.EmPn.NSCHOOL).equals(pn)){
            cur = CoreSval.getConfig(CorePrkey.ALOGIN_NSC_PORT);
        }
        if(StringUtil.isEmpty(cur)){
            logger.error("开启统一登录接口后需要配置对应的平台端口");
        }
        return cur;
    }




    /**
     * 获取授权开关.
     * @return Boolean
     */
    public static Boolean getLicense() {
        String hasLicense = CoreSval.getConfig(CorePrkey.INITIATE_HAS_LICENSE);
        return ((Const.TRUE == hasLicense) || StringUtil.isEmpty(hasLicense)) ?  true: false;
    }

    /**
     * 获取前台标题.
     * @return String
     */
    public static String getFrontTitle() {
        return CoreSval.getConfig(CorePrkey.INITIATE_FRONT_TITLE);
    }

    /**
     * 获取后台标题.
     * @return String
     */
    public static String getBackgroundTitle() {
        return CoreSval.getConfig(CorePrkey.INITIATE_BACKGROUND_TITILE);
    }

    /**
     * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权.
     */
    public static Boolean isDemoMode() {
        String dm = CoreSval.getConfig(CorePrkey.INITIATE_DEMO_MODE);
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 在修改系统用户和角色时是否同步到Activiti.
     */
    public static Boolean isSynActivitiIndetity() {
        String dm = CoreSval.getConfig(CorePrkey.INITIATE_ACTIVITI_IS_SYN_ACTIVITI_INDETITY);
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 获取工程路径.
     * @return String 工程路径
     */
    public static String getProjectPath() {
        // 如果配置了工程路径，则直接返回，否则自动获取。
        String projectPath = CoreSval.getConfig(CorePrkey.INITIATE_PROJECT_PATH);
        if (StringUtil.isNotBlank(projectPath)) {
            return projectPath;
        }
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file == null) {
                return null;
            }

            while (true) {
                File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                if (f == null || f.exists()) {
                    break;
                }
                if (file.getParentFile() != null) {
                    file = file.getParentFile();
                } else {
                    break;
                }
            }
            projectPath = file.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }

    /**
     * 获取DB类型.
     */
    public static String getDbName() {
        String dnName = CoreSval.getConfig(CorePrkey.INITIATE_JDBC_TYPE);
        return (StringUtil.isEmpty(dnName)) ? CorePrkey.INITIATE_JDBC_TYPEDEF_MYSQL: dnName;
    }

    public static String getUuid() {
        return IdGen.uuid();
    }

    /**
     * 允许登陆的系统类型 0后台 1前台.
     */
//    public static final String admin = "1";
//    public static final String back = "0";


    /**
     * 页面获取常量.
     * @see "${fns:getConst('YES')}"
     * @return Object
     */
    public static Object getConst(String field) {
        try {
            return CoreSval.Const.class.getField(field).get(null);
        } catch (Exception e) {
            logger.warn("无配置:" + e.getMessage(), e);
            // 异常代表无配置，这里什么也不做
            return null;
        }
    }

    public static String getScoreSkillId(){
        String skillCreditId = "6705a4dcdbce40f9ae282875586a8c91";
        //技能学分固定id
        if(CoreSval.getTenantIsopen()){
            return skillCreditId + TenantConfig.getCacheTenant();
        }else{
            return skillCreditId + CoreSval.Const.DEFAULT_SCHOOL_TENANTID;
        }
    }

    public static String getScoreRootId(){
        String scoreRootId = "1";
        //学分规则rootID
        if(CoreSval.getTenantIsopen()){
            return scoreRootId+ TenantConfig.getCacheTenant();
        }else{
            return scoreRootId+ CoreSval.Const.DEFAULT_SCHOOL_TENANTID;
        }
    }

    /**
     * 获取静态常量.
     * @see "${fns:getConst('YES')}"
     * @author chenhao
     */
    public static class Const {
        /**
         * 单节点校平台默认租户id
         */
        public static final String DEFAULT_SCHOOL_TENANTID="0000";


        /**
         * 正1/负1.
         */
        public static final Integer Z1 = 1;
        public static final Integer F1 = -1;

        /**
         * 显示/隐藏.
         */
        public static final String HIDE = "0";
        public static final String SHOW = "1";

        /**
         * 是/否/隐藏(页面不可见，但是能关联查看相关数据).
         */
        public static final String YHIDE = "2";
        public static final String YES = "1";
        public static final String NO = "0";
        /**
         * 是/否.
         */
        public static final String YES_ZH = "是";
        public static final String NO_ZH = "否";
        /**
         * 对/错.
         */
        public static final String TRUE = "true";
        public static final String FALSE = "false";

        public static final String ADMIN_LOGIN = CoreSval.getAdminPath() + "/login";
        public static final String FRONT_LOGIN = CoreSval.getFrontPath() + "/toLogin";
        /**
         * 发展阶段1：初始 2：发展 3：成熟
         */
        public static final String STEP_ONE = "1";
        public static final String STEP_TWO = "2";
        public static final String STEP_THREE = "3";

        /**
         * 是否推送省平台,1：是 0：否
         */
        public static final String SEND_YES = "1";
        public static final String SEND_NO = "0";

    }

    /**
     * 获取配置.
     * @see "${fns:getConfig('adminPath')}"
     */
    public static String getConfig(String key) {
        return Prop.getConfig(map, loader, Prop.INITIATE, key);
    }

    /**
     * 获取配置文件 Key(Prop.INITIATE).
     * @author chenhao
     */
    public static class CorePrkey {
        private static final String INITIATE_JDBC_TYPE = "jdbc.type";
        private static final String INITIATE_JDBC_TYPEDEF_MYSQL = "mysql";
        private static final String INITIATE_DEMO_MODE = "demoMode";
        private static final String INITIATE_URL_SUFFIX = "urlSuffix";
        private static final String INITIATE_WEB_VIEW_INDEX = "web.view.index";
        private static final String INITIATE_ADMIN_PATH = "adminPath";
        private static final String INITIATE_FRONT_PATH = "frontPath";
        private static final String INITIATE_FRONT_TITLE = "frontTitle";
        private static final String INITIATE_HAS_LICENSE = "hasLicense";
        private static final String INITIATE_PROJECT_PATH = "projectPath";
        private static final String INITIATE_SYS_ADMIN_IP = "sysAdminIp";
        private static final String INITIATE_SYS_FRONT_IP = "sysFrontIp";
        private static final String INITIATE_BACKGROUND_TITILE = "backgroundTitile";
        private static final String INITIATE_ACTIVITI_IS_SYN_ACTIVITI_INDETITY = "activiti.isSynActivitiIndetity";

        private static final String INITIATE_TENANT_ISOPEN = "tenant.isopen";//是否开启租户模式
        private static final String INITIATE_TENANT_CURRPN = "tenant.currpn";//当前运行平台（Sval.EmPn）
        private static final String INITIATE_TENANT_PNFRONT = "tenant.pnfront";//允许前台访问
        private static final String INITIATE_TENANT_PNADMIN = "tenant.pnadmin";//允许后台访问

        /**
         * 配置方案有两种：
         * 方案1：外网带端口映射，例如（85），需要把配置文件的端口统一设置为一致(Nginx必要-针对80端口占用情况)
         *      ALOGIN_ISOPEN = true;
         *      ALOGIN_NCE_PORT = 85;
         *      ALOGIN_NPR_PORT = 85;
         *      ALOGIN_NSC_PORT = 85;
         *
         * 方案2：局域网带端口映射，例如（nce-8080，npr-8180，nsc-8280），需要把配置文件的端口设置对应(Nginx非必要,可选)
         *      ALOGIN_ISOPEN = true;
         *      ALOGIN_NCE_PORT = 8080;
         *      ALOGIN_NPR_PORT = 8180;
         *      ALOGIN_NSC_PORT = 8280;
         */
        private static final String ALOGIN_PORT_ISOPEN = "alogin.isopen";//是否开启统一登录配置
        private static final String ALOGIN_NCE_PORT = "alogin.nce.port";//统一登录运营端口配置
        private static final String ALOGIN_NPR_PORT = "alogin.npr.port";//统一登录省端口配置
        private static final String ALOGIN_NSC_PORT = "alogin.nsc.port";//统一登录学校端口配置
    }

    /**
     * 获取配置文件 properties属性.
     * @author chenhao
     */
    public enum Prop {
        INITIATE("initiate.properties", "全局配置文件"),
        P_EMAIL("properties/email.properties", "邮件配置文件");

        private String key;//主题
        private String remark;//
        private Prop(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static String getEmailConfig(String key) {
            return CoreSval.Prop.getConfig(CoreSval.emailmap, CoreSval.emailloader, P_EMAIL, key);
        }

        public static Integer getInitiateConfigInt(String key) {
            return Prop.getConfigInt(map, loader, Prop.P_EMAIL, key);
        }
        public static Long getInitiateConfigLong(String key) {
            return Prop.getConfigLong(map, loader, Prop.P_EMAIL, key);
        }
        public static Integer getEmailConfigInt(String key) {
            return Prop.getConfigInt(emailmap, emailloader, Prop.P_EMAIL, key);
        }
        public static Long getEmailConfigLong(String key) {
            return Prop.getConfigLong(emailmap, emailloader, Prop.P_EMAIL, key);
        }

        /**
         * 获取配置Integer.
         * @see "${fns:getConfig('adminPath')}"
         */
        public static Integer getConfigInt(Map<String, String> cmap, PropertiesLoader proLoader, Prop prop, String key) {
            String value = Prop.getConfig(cmap, proLoader, prop, key);
            if ((value != null) && StringUtil.isNumeric(value)) {
                return Integer.getInteger(value);
            }
            return null;
        }

        /**
         * 获取配置Long.
         * @see "${fns:getConfig('adminPath')}"
         */
        public static Long getConfigLong(Map<String, String> cmap, PropertiesLoader proLoader, Prop prop, String key) {
            String value = Prop.getConfig(cmap, proLoader, prop, key);
            if ((value != null) && StringUtil.isNumeric(value)) {
                return Long.getLong(value);
            }
            return null;
        }

        /**
         * 获取配置.
         *
         * @see "${fns:getConfig('adminPath')}"
         */
        public static String getConfig(Map<String, String> cmap, PropertiesLoader proLoader, Prop prop, String key) {
            initProp(cmap, proLoader, prop.key());
            String value = cmap.get(key);
            if (value == null) {
                value = proLoader.getProperty(key);
                cmap.put(key, value != null ? value : StringUtil.EMPTY);
            }
            return value;
        }

        /**
         * 初始化配置文件实体.
         */
        public static void initProp(Map<String, String> cmap, PropertiesLoader proLoader, String key) {
            if(cmap == null){
                cmap = Maps.newHashMap();
            }
            if(proLoader == null){
                proLoader = new PropertiesLoader(key);
            }
        }

        public String key() {
            return key;
        }
        public String getRemark() {
            return remark;
        }
    }

    /**
     * 系统租户类型.
     */
    public enum EmTenant {
        NCE("1", "运营租户"),
        NPR("2", "省租户"),
        NSC("3", "校租户");

        private String key;//系统
        private String remark;
        EmTenant(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public String getKey() {
            return key;
        }

        public String getRemark() {
            return remark;
        }
    }

    /**
     * 系统平台租户类型.
     */
    public enum EmPnttype {
        NCE_NCE("10", Sval.EmPn.NCENTER, EmTenant.NCE.getKey(), "运营+运营租户"),
        NCE_NPR("11", Sval.EmPn.NCENTER, EmTenant.NPR.getKey(), "运营+省租户"),
        NCE_NSC("12", Sval.EmPn.NCENTER, EmTenant.NSC.getKey(), "运营+校租户"),

        NPR_NCE("20", Sval.EmPn.NPROVINCE, EmTenant.NCE.getKey(), "省+运营租户"),
        NPR_NPR("21", Sval.EmPn.NPROVINCE, EmTenant.NPR.getKey(), "省+省租户"),
        NPR_NSC("22", Sval.EmPn.NPROVINCE, EmTenant.NSC.getKey(), "省+校租户"),

        NSC_NCE("30", Sval.EmPn.NSCHOOL, EmTenant.NCE.getKey(), "校+运营租户"),
        NSC_NPR("31", Sval.EmPn.NSCHOOL, EmTenant.NPR.getKey(), "校+省租户"),
        NSC_NSC("32", Sval.EmPn.NSCHOOL, EmTenant.NSC.getKey(), "校+校租户");

        private String key;//系统
        private Sval.EmPn pn;//平台类型
        private String prefix;//前缀标识
        private String remark;
        EmPnttype(String prefix, Sval.EmPn pn, String key, String remark) {
            this.key = key;
            this.pn = pn;
            this.prefix = prefix;
            this.remark = remark;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getKey() {
            return key;
        }

        public String getRemark() {
            return remark;
        }
    }

    public enum PassNot {
        PASS("1", "通过"),
        NOT("0", "不通过");

        private String key;//主题
        private String remark;//

        private PassNot(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        /**
         * 根据key获取枚举 .
         * @author chenhao
         * @param key
         *            页面标识
         * @return CheckRet
         */
        public static PassNot getByKey(String key) {
            if ((key != null)) {
                PassNot[] entitys = PassNot.values();
                for (PassNot entity : entitys) {
                    if ((entity.getKey()).equals(key)) {
                        return entity;
                    }
                }
            }
            return null;
        }

        /**
         * 获取主题 .
         * @return List
         */
        public static List<PassNot> getAll() {
            List<PassNot> enty = Lists.newArrayList();
            PassNot[] entitys = PassNot.values();
            for (PassNot entity : entitys) {
                enty.add(entity);
            }
            return enty;
        }

        public String getKey() {
            return key;
        }

        public String getRemark() {
            return remark;
        }

        @Override
        public String toString() {
            return "{\"key\":\"" + this.key + "\",\"remark\":\"" + this.remark + "\"}";
        }
    }

    /**
     * 系统全局角色类型.
     */
    public enum Rtype {
        SUPER_SC("0", "1", "", "超级管理员", new Sval.EmPn[]{Sval.EmPn.NCENTER}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TM_ADMIN}),
        ADMIN_SYS_SC("1", "10", "", "系统管理员", new Sval.EmPn[]{Sval.EmPn.NCENTER}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TM_ADMIN}),
        ADMIN_YW_SC("4", "11", "", "运营管理员", new Sval.EmPn[]{Sval.EmPn.NCENTER}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TM_ADMIN}),
        ADMIN_PN("10", "15", "", "省管理员", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NPROVINCE}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TM_ADMIN}),
        ADMIN_SN("20", "20", "", "校管理员", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NSCHOOL}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TM_ADMIN}),

        MINISTER("30", "30", "", "秘书", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NSCHOOL}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TM_ADMIN}),
        EXPORT("40", "40", "roleZjId", "专家", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NPROVINCE, Sval.EmPn.NSCHOOL}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TM_ADMIN}),
        TEACHER("50", "50", "frontTid", "导师", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NSCHOOL}, new Sval.EmPt[]{Sval.EmPt.TW_FRONT, Sval.EmPt.TM_FRONT}),
        STUDENT("60", "60", "frontUid", "学生", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NSCHOOL}, new Sval.EmPt[]{Sval.EmPt.TW_FRONT, Sval.EmPt.TM_FRONT}),
        OTHER("100", "", "rother", "其它", new Sval.EmPn[]{Sval.EmPn.NCENTER, Sval.EmPn.NPROVINCE, Sval.EmPn.NSCHOOL}, new Sval.EmPt[]{Sval.EmPt.TW_ADMIN, Sval.EmPt.TW_FRONT, Sval.EmPt.TM_ADMIN, Sval.EmPt.TM_FRONT});

        private String key;//主题
        private String pkey;//页面使用的Key
        private String val;//角色权重值
        private String remark;//
        private Sval.EmPn[] pns;//适用平台类型
        private Sval.EmPt[] pts;//适用端类型

        private Rtype(String key, String val, String pkey, String remark, Sval.EmPn[] pns, Sval.EmPt[] pts) {
            this.key = key;
            this.val = val;
            this.pkey = pkey;
            this.pns = pns;
            this.pts = pts;
            this.remark = remark;
        }

        /**
         * 根据key获取枚举 .
         * @author chenhao
         * @param key 标识
         * @return Rtype
         */
        public static Rtype getByKey(String key) {
            if ((key != null)) {
                Rtype[] entitys = Rtype.values();
                for (Rtype entity : entitys) {
                    if ((entity.getKey()).equals(key)) {
                        return entity;
                    }
                }
            }
            return null;
        }

        /**
         * 根据key获取枚举 .
         * @author chenhao
         * @param pn 平台标识
         * @return Rtype
         */
        public static Rtype getByPns(Sval.EmPn pn) {
            List<Rtype> rtypes = Lists.newArrayList();
            if ((pn != null)) {
                Rtype[] entitys = Rtype.values();
                for (Rtype entity : entitys) {
                    if ((Arrays.asList(entity.getPns())).contains(pn)) {
                        return entity;
                    }
                }
            }
            return null;
        }

        public static List<Rtype> getByPns(String pnPrefix) {
            Sval.EmPn pn = Sval.EmPn.getByPrefix(pnPrefix);
            List<Rtype> rtypes = Lists.newArrayList();
            if (pn == null) {
                return rtypes;
            }

            if ((pn != null)) {
                Rtype[] entitys = Rtype.values();
                for (Rtype entity : entitys) {
                    if ((Arrays.asList(entity.getPns())).contains(pn)) {
                        rtypes.add(entity);
                    }
                }
            }
            return rtypes;
        }

        /**
         * 获取主题 .
         * @return List
         */
        public static List<Rtype> getAll() {
            List<Rtype> enty = Lists.newArrayList();
            Rtype[] entitys = Rtype.values();
            for (Rtype entity : entitys) {
                enty.add(entity);
            }
            return enty;
        }


        public static List<Object> getRtype(){
            CoreSval.Rtype[] entitys = CoreSval.Rtype.values();
            List<Object> roleAuthList = Lists.newArrayList();
            for(CoreSval.Rtype entity : entitys){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("label", entity.getRemark());
                hashMap.put("value", entity.getKey());
                hashMap.put("id", entity.getVal());
                roleAuthList.add(hashMap);
            }
            return roleAuthList;
        }

        public String getVal() {
            return val;
        }

        public String getPkey() {
            return pkey;
        }

        public void setPkey(String pkey) {
            this.pkey = pkey;
        }

        public String getKey() {
            return key;
        }

        public String getRemark() {
            return remark;
        }

        public Sval.EmPn[] getPns() {
            return pns;
        }

        public Sval.EmPt[] getPts() {
            return pts;
        }

        public static List<Object> toList(String tenantType, Integer rt){
            List<Object> list = Lists.newArrayList();
            List<String> roleRtypevo = Lists.newArrayList();
            List<Rtype> rtypes =  Rtype.getByPns(tenantType);
            if(StringUtil.checkEmpty(rtypes)){
                return list;
            }
//            if((Sval.EmPn.NCENTER.getPrefix()).equals(tenantType)){
//                roleRtypevo.add(Rtype.ADMIN_PN.getKey());
//                roleRtypevo.add(Rtype.ADMIN_SN.getKey());
//                roleRtypevo.add(Rtype.MINISTER.getKey());
//                roleRtypevo.add(Rtype.EXPORT.getKey());
//                roleRtypevo.add(Rtype.TEACHER.getKey());
//                roleRtypevo.add(Rtype.STUDENT.getKey());
//                roleRtypevo.add(Rtype.OTHER.getKey());
//            }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(tenantType)){
//                roleRtypevo.add(Rtype.ADMIN_SN.getKey());
//                roleRtypevo.add(Rtype.MINISTER.getKey());
//                roleRtypevo.add(Rtype.EXPORT.getKey());
//                roleRtypevo.add(Rtype.TEACHER.getKey());
//                roleRtypevo.add(Rtype.STUDENT.getKey());
//                roleRtypevo.add(Rtype.OTHER.getKey());
//            }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(tenantType)){
//                roleRtypevo.add(Rtype.SUPER_SC.getKey());
//                roleRtypevo.add(Rtype.ADMIN_SYS_SC.getKey());
//                roleRtypevo.add(Rtype.ADMIN_YW_SC.getKey());
//                roleRtypevo.add(Rtype.ADMIN_PN.getKey());
//            }

            for (Rtype rtype: rtypes){
                if(StringUtil.checkNotEmpty(roleRtypevo) && (roleRtypevo).contains(rtype.getKey())){
                    continue;
                }

                if((rt != null) || !StringUtil.isNumeric(rtype.getKey())){
                    if(rt >= Integer.parseInt(rtype.getKey())){
                        continue;
                    }
                }
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key", rtype.getKey());
                hashMap.put("pkey", rtype.getPkey());
                hashMap.put("remark", rtype.getRemark());
                list.add(hashMap);
            }
            return list;
        }

        public static List<Object> toList(){
            return Rtype.toList();
        }


        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("{\"key\":\"" + this.key + "\"");
            buffer.append(",\"val\":" + "\""+ this.val +"\"");
            buffer.append(",\"pkey\":" + "\""+ this.pkey +"\"");

            if(this.pns != null){
                buffer.append(",\"pns\":" + Arrays.asList(this.pns).toString());
            }else{
                buffer.append(",\"pns\":{}");
            }

            if(this.pts != null){
                buffer.append(",\"pts\":" + Arrays.asList(this.pns).toString());
            }else{
                buffer.append(",\"pts\":{}");
            }

            if(StringUtil.isNotEmpty(this.remark)){
                buffer.append(",\"remark\":\"" + this.remark + "\"}");
            }else{
                buffer.append(",\"remark\":\"\"}");
            }
            return buffer.toString();
        }
    }

    /**
     * 系统全局登录、注册类型.
     */
    public enum LrType {
        MOBILE(true, "1", "mobile", "手机号", "手机号注册"),
        EMAIL(true, "2", "email", "邮箱", "只支持网易、QQ邮箱"),
        LOGIN_NAME(true, "3" ,"loginname", "登录名","只支持英文登录名"),
        NO(true, "10", "no", "学号", "只支持学号登录");

        private boolean enable;//开启所有平台的登录注册.
        private String key;//标识
        private String ekey;//e标识
        private String name;//名称
        private String remark;//备注

        private LrType(boolean enable, String key, String ekey, String name, String remark) {
            this.enable = enable;
            this.key = key;
            this.ekey = ekey;
            this.name = name;
            this.remark = remark;
        }

        public boolean isEnable() {
            return enable;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getEkey() {
            return ekey;
        }

        public String getRemark() {
            return remark;
        }

        @Override
        public String toString() {
            return "{\"enable\":\"" + this.enable + "\",\"key\":\"" + this.key+ "\",\"ekey\":\"" + this.ekey+"\",\"name\":\"" + this.name+ "\",\"remark\":\"" + this.remark + "\"}";
        }
    }
}