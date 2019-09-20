package com.oseasy.pro.modules.tpl.vo;
/**
 * Word项目类型.
 * @author chenhao
 */
public enum WproType {
  PT_CYXL("1", "创新训练"), PT_CYSJ("2", "创业训练"), PT_CXXL("3", "创业实践");

  private String key;
  private String name;

  private WproType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key
   *          枚举标识
   * @return WproType
   */
  public static WproType getByKey(String key) {
    if ((key != null)) {
      WproType[] entitys = WproType.values();
      for (WproType entity : entitys) {
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
