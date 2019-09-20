/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnRulesConnection_]]文件
 * @date 2017年6月2日 上午8:51:50
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;


/**
 * 流程定义角色.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:51:50
 *
 */
public class SnRulesConnection {
  /**
   * role : SequenceFlow connects : [{"from":"sequence_start","to":["sequence_end"]}].
   */

  private String role;
  private List<SnConnects> connects;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public List<SnConnects> getConnects() {
    return connects;
  }

  public void setConnects(List<SnConnects> connects) {
    this.connects = connects;
  }
}
