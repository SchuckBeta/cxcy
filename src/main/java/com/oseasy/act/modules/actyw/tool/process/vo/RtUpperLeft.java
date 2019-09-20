/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtUpperLeft_]]文件
 * @date 2017年6月2日 下午2:17:36
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 流程BPMNDi画布左上坐标.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:17:36
 *
 */
public class RtUpperLeft {
  /**
   * x : 0 y : 0 .
   */

  private Double x;
  private Double y;

  public RtUpperLeft() {
    super();
}

public RtUpperLeft(Double x, Double y) {
    super();
    this.x = x;
    this.y = y;
}

public Double getX() {
    return x;
  }

  public void setX(Double x) {
    this.x = x;
  }

  public Double getY() {
    return y;
  }

  public void setY(Double y) {
    this.y = y;
  }
}
