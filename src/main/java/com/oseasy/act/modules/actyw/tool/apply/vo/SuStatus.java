package com.oseasy.act.modules.actyw.tool.apply.vo;

/**
 * 节点审核结果类型.
 * @author chenhao
 */
public class SuStatus {
  /**
   * 状态标识.
   */
  private String key;
  /**
   * 状态说明.
   */
  private String name;

  public SuStatus(String key, String name) {
    super();
    this.key = key;
    this.name = name;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
