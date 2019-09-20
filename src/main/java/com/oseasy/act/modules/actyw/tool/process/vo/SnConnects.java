/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.connects
 * @Description [[_SnConnects_]]文件
 * @date 2017年6月2日 上午8:52:48
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 流程图连线.
 *
 * @author chenhao
 * @date 2017年6月2日 上午8:52:48
 *
 */
public class SnConnects {
  /**
   * from : sequence_start to : ["sequence_end"].
   */

  private String from;
  private List<String> to;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public List<String> getTo() {
    return to;
  }

  public void setTo(List<String> to) {
    this.to = to;
  }
}
