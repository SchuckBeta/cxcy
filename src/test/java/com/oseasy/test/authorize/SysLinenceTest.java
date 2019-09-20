package com.oseasy.test.authorize;


import com.oseasy.util.common.utils.rsa.Base64;
import com.oseasy.util.common.utils.rsa.RSAEncrypt;
import net.sf.json.JSONArray;

public class SysLinenceTest {

    public static void main(String[] args) {
        String ss = "W3siY3B1IjoiYmZlYmZiZmYwMDA1MDZlMyIsIm1hYyI6IjFjMWIwZDc5YTZjOCJ9XQ==";
        byte[] ssDecodeBytes = Base64.decode(ss);
        String ssDecode = new String(ssDecodeBytes);
//        System.out.println(ssDecode);
        System.out.println(JSONArray.fromObject(ssDecode));
//        System.out.println(new Date().getTime());

        System.err.println("------------------------------sys_license.license字段");
        String st ="fgppYD3D4P+13Ku8t8ZrIlxdItstXF6pUF5HZio7Ybjcdk/UmFSV6UrgKDrj56J7vPDeR6B7iwz44+s94qTWsdLQ5fo7388XrPD5t39Tg8Ke8gUoi8eewOkADWu044ENP3hj6KAY5ddy0KWrZlyOVzDDhXglN47h4IlwOxrVH2JFtPc/HtPXLHVqumrKXpGxfAAMMN5yH4CfAUEHEGdEd5cewxFxo8Ee2+mngNrlmLmI7TQ+NuhR2zNx8ToubKr1/CvbxtugUz/mffLEoSSodUi/gdY6v0la8EJCdze4IGK3GHKwLhM14JdzVxPQ0RgzxYCojxlz20PT+88jiUaXo2+Aq8hwYNbISPrt5vTL4f5um8PPKET130dWS37ml5d6rs4q8RtqBuNuBXhbm07xxDy3ZUm98/AS3qg//gFY78doCz2XPTKkar3+VyJLK6Ky8VyrdHpR8uSOquiAehKn+7EJuKcwMsF5BGi5Vov905jpisQJIGkdZDMxhu0IQquO";
//        String st ="W3siY3B1IjoiZDIwNjAzMDBmZmZiOGIxZiIsIm1hYyI6ImZhMTYzZTYzMjkwOSJ9LHsiY3B1IjoiYmZlYmZiZmYwMDA4MDZlOSIsIm1hYyI6ImQ4YzQ5NzRhYTlmOCJ9XQ==";
        System.out.println( SysLinenceTest.decrypt(st));
        //{"machineCode":[{"cpu":"bfebfbff000506e3","mac":"1c1b0d79a6c8"}],"client_name":"chuangyela","productName":"创业啦","productId":"1212231242534542","productType":"0","product_key":"kaichuangla","month":"120","expiredDate":"2028-12-12 12:19:19","modules":"111111111"}
        //{"machineCode":[{"cpu":"bfebfbff000506e3","mac":"1c1b0d79a6c8"}],"client_name":"chuangyela","productName":"创业啦","productId":"1212231242534542","productType":"0","product_key":"pub_kaichuangla","month":"120","expiredDate":"2028-12-12 12:19:19","modules":"111111111"}
       // 授权 方式
        //1.授权运营平台
        //2.开户  学校访问。
        //3 登录学校后台 授权 校平台

    }





    /*解码 AuthorizeService*/
    public static String decrypt(String lic) {
        try {
            byte[] res;
            String path ="D:\\work\\publicKey.keystore";
            res = RSAEncrypt.decryptPlus(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(path)), Base64.decode(lic));
            String restr = new String(res, "UTF-8");
            System.err.println("解码--------------------------------------------------------");
            return restr;
        } catch (Exception e) {
            return null;
        }
    }



}
