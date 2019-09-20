package com.oseasy.act.modules.actyw.tool.apply.vo;

/**
 * 节点审核参数.
 * @author chenhao
 */
public class SuStatusAparam {
  /**
   * 流程节点标识.
   */
  private String gnodeId;
  /**
   * 审核状态值.
   */
  private String key;

  public SuStatusAparam() {
    super();
  }

  public SuStatusAparam(String gnodeId, String key) {
    super();
    this.gnodeId = gnodeId;
    this.key = key;
  }

  public String getGnodeId() {
    return gnodeId;
  }
  public void setGnodeId(String gnodeId) {
    this.gnodeId = gnodeId;
  }
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
}
