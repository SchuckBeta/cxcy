package com.oseasy.util.common.utils.file;

public enum BrowserType {
  BT_IE("MSIE", "", "IE浏览器")
  ,BT_IE7("MSIE 7", "7", "IE7浏览器")
  ,BT_IE8("MSIE 8", "8", "IE8浏览器")
  ,BT_IE9("MSIE 9", "9", "IE9浏览器")
  ,BT_IE10("MSIE 10", "10", "IE10浏览器")
  ,BT_IE11("MSIE", "11", "IE11浏览器")
  ,BT_IERV11("RV:11", "11", "IE11浏览器")
  ,BT_OPERA("OPERA", "", "OPERA浏览器")
  ,BT_CHROME("CHROME", "", "CHROME浏览器")
  ,BT_FIREFOX("FIREFOX", "", "FIREFOX文件")
  ,BT_WEBKIT("WEBKIT", "", "WEBKIT文件");

 private String key;
 private String version;
 private String name;

 private BrowserType(String key, String version, String name) {
   this.key = key;
   this.version = version;
   this.name=name;
 }

 /**
  * 根据key获取枚举 .
  *
  * @author chenhao
  * @param key 枚举标识
  * @return BrowserType
  */
 public static BrowserType getByKey(String key) {
   if ((key != null)) {
     BrowserType[] entitys = BrowserType.values();
     for (BrowserType entity : entitys) {
       if ((key).equals(entity.getKey())) {
         return entity;
       }
     }
   }
   return null;
 }

  public String getVersion() {
    return version;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }
}
