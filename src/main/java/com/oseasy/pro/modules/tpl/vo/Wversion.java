package com.oseasy.pro.modules.tpl.vo;
/**
 * Word 模板支持版本.
 * @author chenhao
 */
public enum Wversion {
  V_WPS("wps", "WPS"), V_OFFICE("office", "OFFICE");
//V_O2017("2017", "OFFICE 2017"), V_O2016("2016", "OFFICE 2016"), V_O2013("2013", "OFFICE 2013");

  private String key;
  private String name;

  private Wversion(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   * @author chenhao
   * @param key 枚举标识
   * @return Wversion
   */
  public static Wversion getByKey(String key) {
    if ((key != null)) {
      Wversion[] entitys = Wversion.values();
      for (Wversion entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
      }
    }
    return null;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }
}
