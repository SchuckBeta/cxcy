/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnRulesMorphing_]]文件
 * @date 2017年6月2日 上午8:53:45
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 流程定义角色图像.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:53:45
 *
 */
public class SnRulesMorphing {
  /**
   * role : ActivitiesMorph baseMorphs : ["UserTask"] preserveBounds : true .
   */

  private String role;
  private boolean preserveBounds;
  private List<String> baseMorphs;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public boolean isPreserveBounds() {
    return preserveBounds;
  }

  public void setPreserveBounds(boolean preserveBounds) {
    this.preserveBounds = preserveBounds;
  }

  public List<String> getBaseMorphs() {
    return baseMorphs;
  }

  public void setBaseMorphs(List<String> baseMorphs) {
    this.baseMorphs = baseMorphs;
  }
}
