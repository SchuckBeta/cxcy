/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnEdgesIncoming_]]文件
 * @date 2017年6月2日 上午8:50:10
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 自定义流程关联入参.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:50:10
 *
 */
public class SnEdgesIncoming {
  /**
   * role : SequenceFlow maximum : 0.
   */

  private String role;
  private int maximum;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public int getMaximum() {
    return maximum;
  }

  public void setMaximum(int maximum) {
    this.maximum = maximum;
  }
}
