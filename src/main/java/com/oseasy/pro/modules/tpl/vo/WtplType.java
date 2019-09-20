package com.oseasy.pro.modules.tpl.vo;
/**
 * Word模板类型.
 * @author chenhao
 */
public enum WtplType {
  TT_APPLY("1", "项目申请书"), TT_REPORT_ZQ("2", "计划中期检查报告"), TT_REPORT_JX("3", "计划项目结项报告");

  private String key;
  private String name;

  private WtplType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key
   *          枚举标识
   * @return WtplType
   */
  public static WtplType getByKey(String key) {
    if ((key != null)) {
      WtplType[] entitys = WtplType.values();
      for (WtplType entity : entitys) {
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