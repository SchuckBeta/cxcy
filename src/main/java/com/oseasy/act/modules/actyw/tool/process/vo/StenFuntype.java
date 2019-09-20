/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_StenFuntype_]]文件
 * @date 2017年6月22日 上午10:53:37
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 流程节点业务类型枚举.
 * @author chenhao
 * @date 2017年6月22日 上午10:53:37
 *
 */
public enum StenFuntype {
  SFT_NOT_SELECT("0", "不可选"),
  SFT_SELECT("1", "可选");

  private String val;
  private String remark;

  private StenFuntype(String val, String remark) {
    this.val = val;
    this.remark = remark;
  }

  /**
   * 根据类型获取枚举 .
   *
   * @author chenhao
   * @param val
   *          类型
   * @return StenFuntype
   */
  public static StenFuntype getByType(String val) {
    StenFuntype[] entitys = StenFuntype.values();
    for (StenFuntype entity : entitys) {
      if ((val).equals(entity.getVal())) {
        return entity;
      }
    }
    return null;
  }

  public String getVal() {
    return val;
  }

  public String getRemark() {
    return remark;
  }
}
