package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 固定栏目页面类型.
 * @author chenhao
 *
 */
public enum FlowPcmsType {
  FPC_HOME("1", "申报主页");

  private String key;
  private String name;

  private FlowPcmsType(String key, String name) {
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
  public static FlowPcmsType getByKey(String key) {
    if ((key != null)) {
      FlowPcmsType[] entitys = FlowPcmsType.values();
      for (FlowPcmsType entity : entitys) {
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
