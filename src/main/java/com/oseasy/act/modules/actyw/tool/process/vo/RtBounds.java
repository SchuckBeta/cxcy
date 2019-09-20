/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtBounds_]]文件
 * @date 2017年6月2日 下午2:16:17
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.GnodeParam;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 流程BPMNDi 画布边界.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:16:17
 *
 */
public class RtBounds {
    private static Logger logger = LoggerFactory.getLogger(RtBounds.class);
  /**
   * lowerRight : {"x":1200,"y":2348} upperLeft : {"x":0,"y":0}.
   */

  private RtLowerRight lowerRight;
  private RtUpperLeft upperLeft;

  public RtBounds() {
    super();
}

public RtBounds(Double lrx, Double lry, Double rlx, Double rly) {
    super();
    this.lowerRight = new RtLowerRight(lrx, lry);
    this.upperLeft = new RtUpperLeft(rlx, rly);
}

public RtBounds(RtLowerRight lowerRight, RtUpperLeft upperLeft) {
    super();
    this.lowerRight = lowerRight;
    this.upperLeft = upperLeft;
}

public RtLowerRight getLowerRight() {
    return lowerRight;
  }

  public void setLowerRight(RtLowerRight lowerRight) {
    this.lowerRight = lowerRight;
  }

  public RtUpperLeft getUpperLeft() {
    return upperLeft;
  }

  public void setUpperLeft(RtUpperLeft upperLeft) {
    this.upperLeft = upperLeft;
  }

  /**
   * 初始化边界.
   * @param gnode 流程节点
   * @return RtBounds
   */
  public static RtBounds init(ActYwGroup group, List<ActYwGnode> gnodes) {
      RtBounds rtb = null;
      String file = FileUtil.getClassPath(ActYwTool.FLOW_JSON_PATH) + RtBounds.class.getSimpleName() + StringUtil.JSON;
      try {
          rtb = JsonAliUtils.readBeano(file, RtBounds.class);

//          GnodeParam gparam = ActYwTool.getParam(gnodes);
//          double maxWidth = ActYwTool.FLOW_POS_300 * gparam.getEndSize();
//          double maxHeight = ActYwTool.FLOW_POS_100 * gparam.getSize();
//          if((maxWidth > rtb.getLowerRight().getX())){
//              rtb.getUpperLeft().setX(ActYwTool.FLOW_POS_0);//自适应画布宽度
//              rtb.getLowerRight().setX(maxWidth);//自适应画布宽度
//          }
//
//          if((maxHeight > rtb.getLowerRight().getY())){
//              rtb.getUpperLeft().setY(ActYwTool.FLOW_POS_0);//自适应画布宽度
//              rtb.getLowerRight().setY(maxHeight);//自适应画布宽度
//          }
      } catch (Exception e) {
          logger.warn("RtBounds:文件处理失败，使用默认数据生成，路径：" + file);
      }
      return rtb;
  }
}
