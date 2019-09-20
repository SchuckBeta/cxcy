/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.cmd.vo
 * @Description [[_ActYwRchildShapes_]]文件
 * @date 2017年6月18日 下午1:38:57
 *
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.vo;

import java.util.List;

import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwRtpl;
import com.oseasy.act.modules.actyw.tool.process.vo.RtChildShapes;

/**
 * TODO 添加类/接口功能描述.
 * @author chenhao
 * @date 2017年6月18日 下午1:38:57
 *
 */
public class ActYwRchildShapes implements ActYwRtpl{
  private List<RtChildShapes> rtcShapess;

  public ActYwRchildShapes() {
    super();
  }

  public ActYwRchildShapes(List<RtChildShapes> rtcShapess) {
    super();
    this.rtcShapess = rtcShapess;
  }

  public List<RtChildShapes> getRtcShapess() {
    return rtcShapess;
  }

  public void setRtcShapess(List<RtChildShapes> rtcShapess) {
    this.rtcShapess = rtcShapess;
  }
}
