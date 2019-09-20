/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_StenEtype_]]文件
 * @date 2017年6月13日 下午6:04:25
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 流程图节点元素类型.
 * @author chenhao
 * @date 2017年6月13日 下午6:04:25
 *
 */
public enum StenEtype {
  SE_NODE(1, "node", "节点"),
  SE_EDGE(2, "edge", "连线");

  /**
   * "1、node|2、edge".
   */
  private Integer type;
  private String key;
  private String remark;

  private StenEtype(Integer type, String key, String remark) {
    this.type = type;
    this.key = key;
    this.remark = remark;
  }

  /**
   * 根据类型获取枚举 .
   *
   * @author chenhao
   * @param type
   *          类型
   * @return StenEtype
   */
  public static StenEtype getByType(Integer type) {
    StenEtype[] entitys = StenEtype.values();
    for (StenEtype entity : entitys) {
      if ((type).equals(entity.getType())) {
        return entity;
      }
    }
    return null;
  }

  /**
   * 根据关键字获取枚举 .
   *
   * @author chenhao
   * @param key
   *          关键字
   * @return StenEtype
   */
  public static StenEtype getByKey(String key) {
    StenEtype[] entitys = StenEtype.values();
    for (StenEtype entity : entitys) {
      if ((key).equals(entity.getKey())) {
        return entity;
      }
    }
    return null;
  }

  public Integer getType() {
    return type;
  }

  public String getKey() {
    return key;
  }

  public String getRemark() {
    return remark;
  }
}
