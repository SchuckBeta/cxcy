package com.oseasy.sys.modules.sys.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.util.common.utils.StringUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Administrator on 2019/4/12 0012.
 */
public class SentuxueyuanConfig implements Serializable {
    public static final String CONFIT_ST_KEY = "sentuxueyuan";
    private Boolean isencrypt;//配置参数是否加密
    private String httpWeb;//登录站点地址
    private String pidSso;//固定参数（sso）
    private String m;//接口模型名称(user)
    private String signJg;//全站当前机构标识统一用 “stayy”
    private String signSy;//DES加密算法需使用8位签名私钥，私钥定义为‘sentu_ay’，请在加密时使用该私钥进行加密
    private String cntInterface;//接口控制器名称(nt_interface)
    private String oecurl;//退出重定向Url
    private String oectype;//退出重定向Url类型：0，固定Url;1，动态域名Url

    @JsonIgnore
    public Boolean getIsencrypt() {
        if(this.isencrypt == null){
            this.isencrypt = true;
        }
        return isencrypt;
    }

    public void setIsencrypt(Boolean isencrypt) {
        this.isencrypt = isencrypt;
    }

    public String getHttpWeb() {
        return httpWeb;
    }

    public void setHttpWeb(String httpWeb) {
        this.httpWeb = httpWeb;
    }

    public String getPidSso() {
        return pidSso;
    }

    public void setPidSso(String pidSso) {
        this.pidSso = pidSso;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getSignJg() {
        return signJg;
    }

    public void setSignJg(String signJg) {
        this.signJg = signJg;
    }

    public String getSignSy() {
        return signSy;
    }

    public void setSignSy(String signSy) {
        this.signSy = signSy;
    }

    public String getCntInterface() {
        return cntInterface;
    }

    public void setCntInterface(String cntInterface) {
        this.cntInterface = cntInterface;
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

    public static void main(String[] args) {
//        /**
//         * 生成字符串.
//         */
//        System.out.println(CourseRes.genConfig(CourseRes.genConfig()));
//        /**
//         * 生成加密字符串.
//         */
//        System.out.println(CourseRes.genConfig(CourseRes.encrypt(CourseRes.genConfig())));
//        /**
//         * 生成解密字符串.
//         */
//        System.out.println(CourseRes.genConfig(CourseRes.decrypt(CourseRes.encrypt(CourseRes.genConfig()))));

//        SentuxueyuanConfig config = CourseRes.genConfig();
//        CourseRes res = new CourseRes(config);
//        res.setUid("张三");
//        res.setPwd("123456");
//        String url = CourseRes.gen(res);
//        System.out.println("url="+url);
//        String rstatus = HttpClientUtil.doPost(url, Lists.newArrayList());
//        System.out.println(rstatus);

        String name = "张三" + StringUtil.LINE_D + "ssssssssssss";
        try {
            System.out.println(URLEncoder.encode(name, StringUtil.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
