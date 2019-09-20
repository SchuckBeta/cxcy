/**
 * .
 */

package com.oseasy.pro.modules.course.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.sys.modules.sys.vo.SentuxueyuanConfig;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.UrlUtil;
import com.oseasy.util.common.utils.httpclient.HttpClientUtil;
import com.oseasy.util.common.utils.httpclient.URLConnectionUtil;
import com.oseasy.util.common.utils.rsa.DESUtil;
import com.oseasy.util.common.utils.rsa.MD5Util;
import com.oseasy.util.common.utils.rsa.tool.DES;
import net.sf.json.JSONObject;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * .
 * @author chenhao
 */
public class CourseRes {
    public static final String CONFIT_ST_KEY = SentuxueyuanConfig.CONFIT_ST_KEY;
    private static final String DES_ST_KEY = "sentuxueyuan";
    private static final String OECURL = "oecurl";
    private static final String RETURL = "returl";
    public static final String HTTP_CURDATE = "http://oe.sentuxueyuan.com/?c=oy_interface&m=oe_time";//获取当前时间接口
    public static final String HTTP_WEB = "http://oe.sentuxueyuan.com/";//登录站点地址
    public static final String PID_SSO = "sso";//固定参数（sso）
    public static final String M_USER = "user";//接口模型名称(user)
    public static final String SIGN_JG = "oyyst";//全站当前机构标识统一用 “stayy”,[oyyst,sendto,stayy]
    public static final String SIGN_SY = "sentu_oy";//DES加密算法需使用8位签名私钥，私钥定义为‘sentu_ay’，请在加密时使用该私钥进行加密,[sentu_oy,sentu_nt,sentu_ay]
    public static final String C_NT_INTERFACE = "oy_interface";//接口控制器名称(nt_interface),[oy_interface,nt_interface,nt_interface]
//    public static final String SIGN_JG = "sendto";//全站当前机构标识统一用 “stayy”,[oyyst,sendto,stayy]
//    public static final String SIGN_SY = "sentu_nt";//DES加密算法需使用8位签名私钥，私钥定义为‘sentu_ay’，请在加密时使用该私钥进行加密,[sentu_oy,sentu_nt,sentu_ay]
//    public static final String C_NT_INTERFACE = "nt_interface";//接口控制器名称(nt_interface),[oy_interface,nt_interface,nt_interface]

    private String site;
    private String c;
    private String m;
    private String pid;
    private String uid;
    private String pwd;
    private String sign;
    private String returnurl;
    private String oecurl;
    private String oectype;


    public CourseRes(SentuxueyuanConfig config) {
        super();
        if(config.getIsencrypt()){
            config = CourseRes.decrypt(config);
        }
        this.site = config.getHttpWeb();
        this.c = config.getCntInterface();
        this.m = config.getM();
        this.pid = config.getPidSso();
        this.oecurl = getOecurl(config);
        if(StringUtil.isEmpty(this.oectype)){
            this.oectype = ResCtype.GD.getKey();
        }
    }


    public CourseRes(SentuxueyuanConfig config, HttpServletRequest request) {
        super();
        if(config != null){
            if(config.getIsencrypt()){
                config = CourseRes.decrypt(config);
            }
            this.site = config.getHttpWeb();
            this.c = config.getCntInterface();
            this.m = config.getM();
            this.pid = config.getPidSso();
            this.oecurl = getOecurl(config);
        }

        this.returnurl = getReturl(request);
        if(StringUtil.isEmpty(this.oectype)){
            this.oectype = ResCtype.GD.getKey();
        }
    }

    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public String getC() {
        return c;
    }
    public void setC(String c) {
        this.c = c;
    }
    public String getM() {
        return m;
    }
    public void setM(String m) {
        this.m = m;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getReturnurl() {
        return returnurl;
    }
    public void setReturnurl(String returnurl) {
        this.returnurl = returnurl;
    }
    public String getOectype() {
        return oectype;
    }
    public void setOectype(String oectype) {
        this.oectype = oectype;
    }
    public String getOecurl() {
        return oecurl;
    }
    public void setOecurl(String oecurl) {
        this.oecurl = oecurl;
    }

    /**
     * 加密森途配置.
     * @param config SentuxueyuanConfig
     * @return SentuxueyuanConfig
     */
    public static SentuxueyuanConfig encrypt(SentuxueyuanConfig config){
        DES des = new DES(DES_ST_KEY);
        config.setIsencrypt(true);
        try {
            config.setHttpWeb(des.encrypt(config.getHttpWeb()));
            config.setPidSso(des.encrypt(config.getPidSso()));
            config.setM(des.encrypt(config.getM()));
            config.setSignJg(des.encrypt(config.getSignJg()));
            config.setSignSy(des.encrypt(config.getSignSy()));
            config.setCntInterface(des.encrypt(config.getCntInterface()));
        } catch (Exception e) {
            config.setIsencrypt(false);
            e.printStackTrace();
        }
        return config;
    }

    /**
     * 解密森途配置.
     * @param config SentuxueyuanConfig
     * @return SentuxueyuanConfig
     */
    public static SentuxueyuanConfig decrypt(SentuxueyuanConfig config){
        DES des = new DES(DES_ST_KEY);
        config.setIsencrypt(false);
        try {
            config.setHttpWeb(des.decrypt(config.getHttpWeb()));
            config.setPidSso(des.decrypt(config.getPidSso()));
            config.setM(des.decrypt(config.getM()));
            config.setSignJg(des.decrypt(config.getSignJg()));
            config.setSignSy(des.decrypt(config.getSignSy()));
            config.setCntInterface(des.decrypt(config.getCntInterface()));
        } catch (Exception e) {
            config.setIsencrypt(true);
            e.printStackTrace();
        }
        return config;
    }

    /**
     * 初始化森途配置参数.
     * @return SentuxueyuanConfig
     */
    public static SentuxueyuanConfig genConfig(){
        SentuxueyuanConfig config = new SentuxueyuanConfig();
        config.setHttpWeb(CourseRes.HTTP_WEB);//登录站点地址
        config.setPidSso(CourseRes.PID_SSO);//固定参数（sso）
        config.setM(CourseRes.M_USER);//接口模型名称(user)
        config.setSignJg(CourseRes.SIGN_JG);//全站当前机构标识统一用 “stayy”
        config.setSignSy(CourseRes.SIGN_SY);//DES加密算法需使用8位签名私钥，私钥定义为‘sentu_ay’，请在加密时使用该私钥进行加密
        config.setCntInterface(CourseRes.C_NT_INTERFACE);//接口控制器名称(nt_interface)
        config.setIsencrypt(true);
        config.setOectype(ResCtype.GD.getKey());
        config.setOecurl("http://f.os-easyedu.com:28235/f");
        return config;
    }

    /**
     * 初始化森途配置为Json字符串.
     * @return String
     */
    public static String genConfig(SentuxueyuanConfig config){
        Map<String, Object> map = new HashMap<>();
        map.put(CONFIT_ST_KEY, config);
        return JSONObject.fromObject(map).toString();
    }

    public static String getReturl(HttpServletRequest request) {
        String returl =  UrlUtil.queryString(request);
        if(StringUtil.isEmpty(returl)){
            return StringUtil.LINE;
        }
        returl = returl.replace(RETURL + StringUtil.DENGH, StringUtil.EMPTY);
        try {
            return URLEncoder.encode(returl, StringUtil.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return StringUtil.LINE;
    }

    public static String getOecurl(SentuxueyuanConfig config) {
        if(StringUtil.isNotEmpty(config.getOecurl())){
            try {
                if((ResCtype.GD.getKey()).equals(config.getOectype())){
                    System.out.println("请求的Oecurl地址："+ config.getOecurl());
                    return URLEncoder.encode(config.getOecurl(), StringUtil.UTF_8);
                }else if((ResCtype.DY.getKey()).equals(config.getOectype())){
                    System.out.println("请求的Oecurl地址："+ config.getOecurl());
                    return URLEncoder.encode(config.getOecurl(), StringUtil.UTF_8);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return CoreSval.getSysFrontIp();
    }


    /**
     * 生成签名参数.
     * @param cres CourseRes
     * @return String
     */
    public static String genSign(CourseRes cres) {
        try {
            Date curDate = null;
            try {
                System.out.println("当前系统时间：" + DateUtil.formatDate(new Date(), DateUtil.FMT_YYYYMMDD_HHmmss_ZG));
                if(StringUtil.isNotEmpty(HTTP_CURDATE)){
                    String curDateStr = HttpClientUtil.doGet(HTTP_CURDATE);
                    curDate = DateUtil.formatToDate(DateUtil.FMT_YYYYMMDD_HHmmss_ZG, curDateStr);
                }
            } catch (Exception e) {
                curDate = new Date();
            }
            if(curDate == null){
                curDate = new Date();
            }
            System.out.println("远程系统时间：" + DateUtil.formatDate(curDate, DateUtil.FMT_YYYYMMDD_HHmmss_ZG));
            String curSign = cres.getUid() + StringUtil.DL + SIGN_JG + StringUtil.DL + DateUtil.formatDate(DateUtil.addHours(curDate, -8), DateUtil.FMT_YYYY_MM_DD_HH);

            System.out.println("签名字符串：" + curSign);
            return new DESUtil(SIGN_SY.getBytes(), DESUtil.DESIV).encrypt(curSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理密码、签名、用户信息.
     * @param cres CourseRes
     * @param user User
     * @return String
     */
    public static String gen(SentuHttpType httpType, CourseRes cres, User user) {
        /**
         * 从Session中获取一次加密MD5，检验密码是否存在.
         */
        String pswmw = (String) CoreUtils.getSession().getAttribute(User.PSW_MW);
        System.out.println("--------------------------------------------------分隔符 开始--------------");
        System.out.println("账号：" + user.getLoginName() + StringUtil.LINE_D + user.getTenantId());
        System.out.println("密码：" + pswmw);

        if(StringUtil.isEmpty(pswmw) || StringUtil.isEmpty(user.getPassword())){
            return null;
        }

        /**
         * 从获取的MD5加密与密文比较，确保密文正确才跳转.
         */
        if(((CoreUtils.USER_PSW_DEFAULT).equals(pswmw)) || CoreUtils.validatePassword(pswmw, user.getPassword())){
            cres.setPwd(MD5Util.string2MD5(pswmw));
            cres.setUid(user.getLoginName() + StringUtil.LINE_D + user.getTenantId());
            cres.setSign(CourseRes.genSign(cres));
            try {
                cres.setUid(URLEncoder.encode(cres.getUid(), StringUtil.UTF_8));
            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
                return null;
            }
//           cres.setUid(user.getLoginName() + StringUtil.LINE_D + user.getTenantId());
            System.out.println("签名密文："+ cres.getSign());
            return gen(httpType, cres);
        }
        return null;
    }


    /**
     * 生成最终的Url和参数.
     * @param cres CourseRes
     * @return String
     */
    public static String gen(SentuHttpType httpType, CourseRes cres) {
        StringBuffer url = new StringBuffer(cres.getSite());
        if((SentuHttpType.POST).equals(httpType)){
            url.append("?");
            url.append("c=");
            url.append(cres.getC());

            url.append("&m=");
            url.append(cres.getM());
//            url.append("&pid=");
//            url.append(cres.getPid());
//
//            url.append("&sign=");
//            url.append(cres.getSign());
//
//            url.append("&uid=");
//            url.append(cres.getUid());
//
//            url.append("&pwd=");
//            url.append(cres.getPwd());
//
//            if(StringUtil.isNotEmpty(cres.getReturnurl())){
//                url.append("&returnurl=");
//                url.append(cres.getReturnurl());
//            }else{
//                url.append("&returnurl=/");
//            }
//
//            if(StringUtil.isNotEmpty(cres.getOecurl())){
//                url.append("&oecurl=");
//                url.append(cres.getOecurl());
//            }
        }else if((SentuHttpType.GET).equals(httpType)){
            url.append("?");
            url.append("c=");
            url.append(cres.getC());

            url.append("&m=");
            url.append(cres.getM());

            url.append("&pid=");
            url.append(cres.getPid());

            url.append("&sign=");
            url.append(cres.getSign());

            url.append("&uid=");
            url.append(cres.getUid());

            url.append("&pwd=");
            url.append(cres.getPwd());

            if(StringUtil.isNotEmpty(cres.getReturnurl())){
                url.append("&returnurl=");
                url.append(cres.getReturnurl());
            }else{
                url.append("&returnurl=/");
            }

            if(StringUtil.isNotEmpty(cres.getOecurl())){
                url.append("&oecurl=");
                url.append(cres.getOecurl());
            }
        }
        System.out.println("请求的Url地址："+ url.toString());
        return url.toString();
    }

    /**
     * 处理密码、签名、用户信息，生成参数.
     * @param cres CourseRes
     * @return String
     */
    public static List<BasicNameValuePair> genPostParam(CourseRes cres) {
        List<BasicNameValuePair> param = Lists.newArrayList();
        param.add(new BasicNameValuePair("pid", cres.getPid()));
        param.add(new BasicNameValuePair("sign", cres.getSign()));
        param.add(new BasicNameValuePair("uid", cres.getUid()));
        param.add(new BasicNameValuePair("pwd", cres.getPwd()));
        if(StringUtil.isNotEmpty(cres.getReturnurl())){
            param.add(new BasicNameValuePair("returnurl", cres.getReturnurl()));
        }else{
            param.add(new BasicNameValuePair("returnurl", "/"));
        }

        if(StringUtil.isNotEmpty(cres.getOecurl())){
            param.add(new BasicNameValuePair("oecurl", cres.getOecurl()));
        }

        return param;
    }

    public static Map<String, String> genPostPmap(CourseRes cres) {
        Map<String, String> map = Maps.newHashMap();
        map.put("pid", cres.getPid());
        map.put("sign", cres.getSign());
        map.put("uid", cres.getUid());
        map.put("pwd", cres.getPwd());
        if(StringUtil.isNotEmpty(cres.getReturnurl())){
            map.put("returnurl", cres.getReturnurl());
        }else{
            map.put("returnurl", "/");
        }

        if(StringUtil.isNotEmpty(cres.getOecurl())){
            map.put("oecurl", cres.getOecurl());
        }
        return map;
    }

    public static boolean doPost(String url, List<BasicNameValuePair> param) throws Exception{
        System.out.println("请求的POST参数："+ param);
        String rstatus = HttpClientUtil.doPost(url, param);

        System.out.println("响应结果：" + rstatus);
        if(!(SentuStatus.PES_CG.getKey()).equals(rstatus)){
            SentuStatus sentuStatus =  SentuStatus.getByKey(rstatus);
            if(sentuStatus == null){
                return true;
//                throw new Exception("响应状态未定义.");
            }
            return true;
//            throw new Exception("请求失败，"+sentuStatus.getName());
        }
        return true;
    }


    public static boolean doPost2(String url, Map<String, String> param) throws Exception{
        System.out.println("请求的POST参数："+ param);
        String rstatus = URLConnectionUtil.sendHttpRequest(url, param, StringUtil.GBK);

        System.out.println("响应结果：" + rstatus);
        if(!(SentuStatus.PES_CG.getKey()).equals(rstatus)){
            SentuStatus sentuStatus =  SentuStatus.getByKey(rstatus);
            if(sentuStatus == null){
                return true;
                //throw new Exception("响应状态未定义.");
            }
            return true;
//            throw new Exception("请求失败，"+sentuStatus.getName());
        }
        return true;
    }
}
