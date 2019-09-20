/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtStencil_]]文件
 * @date 2017年6月2日 下午2:15:34
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 流程标识类型.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:15:34
 *
 */
public class RtStencil {
  /**
   * id : BPMNDiagram.
   */
  private String id;

  public RtStencil() {
    super();
}

public RtStencil(String id) {
    super();
    this.id = id;
}

public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
