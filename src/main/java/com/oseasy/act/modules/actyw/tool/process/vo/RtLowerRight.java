/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtLowerRight_]]文件
 * @date 2017年6月2日 下午2:17:02
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

/**
 * 流程BPMNDi画布右下坐标.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:17:02
 *
 */
public class RtLowerRight {
  /**
   * x : 1200 y : 2348.
   */
  private Double x;
  private Double y;

  public RtLowerRight() {
    super();
}

public RtLowerRight(Double x, Double y) {
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
