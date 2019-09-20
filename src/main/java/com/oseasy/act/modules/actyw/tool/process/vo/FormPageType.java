package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 固定页面类型.
 * @author chenhao
 *
 */
public enum FormPageType {
    FPT_APPLY("10", "前台申报页"),
    FPT_VIEWF("20", "前台查看页"),
    FPT_INDEX("30", "后台首页"),
    FPT_VIEWA("40", "后台查看页");

  private String key;
  private String name;

  private FormPageType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return FormPageType
   */
  public static FormPageType getByKey(String key) {
    if ((key != null)) {
      FormPageType[] entitys = FormPageType.values();
      for (FormPageType entity : entitys) {
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

  public void setKey(String key) {
    this.key = key;
  }

  public void setName(String name) {
    this.name = name;
  }
}
