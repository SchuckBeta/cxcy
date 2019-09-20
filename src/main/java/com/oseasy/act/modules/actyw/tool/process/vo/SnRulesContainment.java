/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnRulesContainment_]]文件
 * @date 2017年6月2日 上午8:53:20
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 流程定义容器.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:53:20
 *
 */
public class SnRulesContainment {
  /**
   * role : BPMNDiagram contains : ["all"].
   */

  private String role;
  private List<String> contains;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public List<String> getContains() {
    return contains;
  }

  public void setContains(List<String> contains) {
    this.contains = contains;
  }
}
