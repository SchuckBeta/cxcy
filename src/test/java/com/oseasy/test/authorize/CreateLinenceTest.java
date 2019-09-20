package com.oseasy.test.authorize;

import com.oseasy.util.common.utils.rsa.Base64;
import com.oseasy.util.common.utils.rsa.RSAEncrypt;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import net.sf.json.JSONArray;

/**
 * 创建证书（包含多机器）
 */
public class CreateLinenceTest {

  //1.生成多机器的机器码
  //2.机器码加密
  //3.生成授权文件

  public static void main(String[] args) {

    //1.生成多机器的机器码
    //[{"cpu":"d2060300fffb8b1f","mac":"fa163e632909"},{"cpu":"bfebfbff000806e9","mac":"d8c4974aa9f8"}]
    String mcds = "[{\"cpu\":\"bfebfbff000806e9\",\"mac\":\"d8c4974aa9f8\"},{\"cpu\":\"bfebfbff000306c3\",\"mac\":\"408f5ce342a3\"},{\"cpu\":\"bfebfbff000906e9\",\"mac\":\"4ccc6a8ca738\"},{\"cpu\":\"bfebfbff000506e3\",\"mac\":\"4ccc6a8b111b\"},{\"cpu\":\"bfebfbff000206a7\",\"mac\":\"c89cdca3509a\"},{\"cpu\":\"bfebfbff000806e9\",\"mac\":\"d8c4974aa9f8\"},{\"cpu\":\"bfebfbff000506e3\",\"mac\":\"1c1b0d79a6c8\"},{\"cpu\":\"d2060300fffb8b1f\",\"mac\":\"fa163e3fd470\"},{\"cpu\":\"d2060300fffb8b1f\",\"mac\":\"fa163e19e901\"},{\"cpu\":\"d2060300fffb8b1f\",\"mac\":\"fa163e632909\"},{\"cpu\":\"d2060300fffb8b1f\",\"mac\":\"fa163e632909\"},{\"cpu\":\"a1060200fffb8b1f\",\"mac\":\"fa163e160023\"},{\"cpu\":\"a1060200fffb8b1f\",\"mac\":\"fa163e576af7\"},{\"cpu\":\"a1060200fffb8b1f\",\"mac\":\"fa163e251cab\"}]";
    System.out.println(mcds);
    JSONArray mcdJson = JSONArray.fromObject(mcds);
    //2.机器码加密  生成硬件码文件  .mcd   目录 d://text0605.mcd
//  W3siY3B1IjoiZDIwNjAzMDBmZmZiOGIxZiIsIm1hYyI6ImZhMTYzZTYzMjkwOSJ9LHsiY3B1IjoiYmZlYmZiZmYwMDA4MDZlOSIsIm1hYyI6ImQ4YzQ5NzRhYTlmOCJ9XQ==
    String  encode = Base64.encode(mcdJson.toString().getBytes());
    System.err.printf(".mcd ：  %s \n", encode);
    CreateLinenceTest.createMACDFile(encode); //

    //3 那mcd文件去报备系统 下单 和 获取授权证书 end
//    测试地址：bb.os-easy.com, act.os-easy.com    测试帐号是tester1, 密码是123321

    //解密下看看是否正确
    CreateLinenceTest.decodeMCD(encode);

    //解密.acvt 文件内容
    String   acvt="CFqO15bBd8ksXsfn/ZfodOOROx4SM5kx8S4w8Q7d9pNIJLngCvp5RSb8pswHc1ebxtND+luh4Ig5aoDZnTYanpg5xUPXv2sBep+KCRFEIEO3GW4XQfbKIjNaacPIocSYXdzSxIh9twHn7PnhQ5iWgluF79Ckjst39gUddMjY1GmIQNPVeCHkjFiBusykxXBp0+bq63eMj4i/gjaOYffyZNe63TO8FdiCf8T9OZDGCFwH/g+L6XWbk/93QJp9ZQGHuNbbfl8lkd2jQ9ndpnVehuwE42nqeX3kRb8cVsyUztvj/E4EUqP0d3tsKah+xD8RbJeAc86KMXoaTMjJka/eq4PysUNNJ4Wrj9fr6Poy5xusfwEsu1s9KVpjg9fykXLj+dPVIVnGiZsEUzOdTol6BTNaPj8Ja+BBX0Xh+OVvNuCz+N0TxqLK7HhNh1YJL/QIdL9E3rrB4OCJej7R5SPFVbKbM3kyvoGc0rU4x/AwaZOHcVTKNsUWkY1q3jZegG7LGVLQO5FpJhO+QkQOGM5LAk1/xs8H0+HjltQ3jzAjr31RmelARROgeqb8XLgGyWpjxwYixThPuJddbIv3LdUAGYGigogjd14yDKa3QB0N1EjtUzQnpuHxHCfLziUEf7EeR17HZaPRJn3bT7uF5RgMyFRSw+g1fi/B5NvnCQJO06g=";
    System.err.printf("解密.acvt 文件内容：%s \n", decrypt(acvt)   );
  }


  public static void  createMACDFile(String res) {
    try {
      File file = new File("d://text0605.mcd");
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      if (file != null && !file.exists()) {
        file.createNewFile();
      }
      file.setWritable(true, false);
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      bw.write(res);
      bw.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }


  /*解码 AuthorizeService
  *   格式：      //{"machineCode":[{"cpu":"bfebfbff000506e3","mac":"1c1b0d79a6c8"}],"client_name":"chuangyela","productName":"创业啦","productId":"1212231242534542","productType":"0","product_key":"kaichuangla","month":"120","expiredDate":"2028-12-12 12:19:19","modules":"111111111"}
  * */
  public static String  decrypt(String  lic) {
    try {
      byte[] res;
      String  path = "D:\\work\\publicKey.keystore";
      res = RSAEncrypt
          .decryptPlus(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(path)), Base64
              .decode(lic));
      String  restr = new String (res, "gbk");
      System.err.println("解码--------------------------------------------------------");
      return restr;
    } catch (Exception e) {
      return null;
    }
  }


  public static void decodeMCD(String  mad) {
    byte[] ssDecodeBytes = Base64.decode(mad);
    String  ssDecode = new String (ssDecodeBytes);
    System.out.println("解密："+ssDecode);
    System.out.println(JSONArray.fromObject(ssDecode)); //校验格式是否是json格式
  }

}
