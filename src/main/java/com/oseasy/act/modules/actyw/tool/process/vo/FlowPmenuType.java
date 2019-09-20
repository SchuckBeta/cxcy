package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 固定菜单页面类型.
 * @author chenhao
 *
 */
public enum FlowPmenuType {
  FPM_HOME("1", "首页");

  private String key;
  private String name;

  private FlowPmenuType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return FlowPcmsType
   */
  public static FlowPmenuType getByKey(String key) {
    if ((key != null)) {
      FlowPmenuType[] entitys = FlowPmenuType.values();
      for (FlowPmenuType entity : entitys) {
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
