/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnRulesCardinality_]]文件
 * @date 2017年6月2日 上午8:47:27
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;


/**
 * 流程定义规则.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:47:27
 *
 */
public class SnRulesCardinality {
  /**
   * role : Startevents_all .
   * incomingEdges : [{"role":"SequenceFlow","maximum":0}]
   * outgoingEdges : [{"role":"SequenceFlow","maximum":0}]
   */

  private String role;
  private List<SnEdgesIncoming> incomingEdges;
  private List<SnEdgesOutgoing> outgoingEdges;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public List<SnEdgesIncoming> getIncomingEdges() {
    return incomingEdges;
  }

  public void setIncomingEdges(List<SnEdgesIncoming> incomingEdges) {
    this.incomingEdges = incomingEdges;
  }

  public List<SnEdgesOutgoing> getOutgoingEdges() {
    return outgoingEdges;
  }

  public void setOutgoingEdges(List<SnEdgesOutgoing> outgoingEdges) {
    this.outgoingEdges = outgoingEdges;
  }
}
