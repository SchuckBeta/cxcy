/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_SnRules_]]文件
 * @date 2017年6月2日 上午8:45:39
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 工作流元素规则.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:45:39
 *
 */
public class SnRules {
  private List<SnRulesCardinality> cardinalityRules;
  private List<SnRulesConnection> connectionRules;
  private List<SnRulesContainment> containmentRules;
  private List<SnRulesMorphing> morphingRules;

  public List<SnRulesCardinality> getCardinalityRules() {
    return cardinalityRules;
  }

  public void setCardinalityRules(List<SnRulesCardinality> cardinalityRules) {
    this.cardinalityRules = cardinalityRules;
  }

  public List<SnRulesConnection> getConnectionRules() {
    return connectionRules;
  }

  public void setConnectionRules(List<SnRulesConnection> connectionRules) {
    this.connectionRules = connectionRules;
  }

  public List<SnRulesContainment> getContainmentRules() {
    return containmentRules;
  }

  public void setContainmentRules(List<SnRulesContainment> containmentRules) {
    this.containmentRules = containmentRules;
  }

  public List<SnRulesMorphing> getMorphingRules() {
    return morphingRules;
  }

  public void setMorphingRules(List<SnRulesMorphing> morphingRules) {
    this.morphingRules = morphingRules;
  }
}
