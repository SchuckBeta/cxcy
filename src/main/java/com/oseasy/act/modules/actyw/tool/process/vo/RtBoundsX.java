/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtBoundsX_]]文件
 * @date 2017年6月2日 下午2:20:32
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 流程节点面板.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:20:32
 *
 */
public class RtBoundsX {
    private static final double TASK_WIDTH = 100.0;
    private static final double TASK_HEIGHT = 100.0;
    private static final double PROCESS_WIDTH = 300.0;
    private static final double PROCESS_HEIGHT = 300.0;
    private static Logger logger = LoggerFactory.getLogger(RtBoundsX.class);
    /**
     * lowerRight : {"x":681.296875,"y":45} upperLeft : {"x":651.296875,"y":15}.
     */

    private RtLowerRightX lowerRight;
    private RtUpperLeftX upperLeft;

    public RtLowerRightX getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(RtLowerRightX lowerRight) {
        this.lowerRight = lowerRight;
    }

    public RtUpperLeftX getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(RtUpperLeftX upperLeft) {
        this.upperLeft = upperLeft;
    }

    /**
     * RtBoundsX对象转换为 RtBounds.
     */
    public static RtBounds convertRtBounds(RtBoundsX rtbx){
        return new RtBounds(rtbx.getLowerRight().getX(), rtbx.getLowerRight().getY(), rtbx.getUpperLeft().getX(), rtbx.getUpperLeft().getY());
    }

    /**
     * 初始化边界.
     * @param gnode 流程节点
     * @return RtBounds
     */
    public static RtBoundsX init(ActYwGnode gnode, RtBounds rtb, List<RtGcshapes> gcshapess) {
        RtBoundsX rtbx = null;
        String file = FileUtil.getClassPath(ActYwTool.FLOW_JSON_PATH) + RtBoundsX.class.getSimpleName() + StringUtil.JSON;
        try {
            rtbx = JsonAliUtils.readBeano(file, RtBoundsX.class);

            //cur
            RtUpperLeft rtbrl = rtb.getUpperLeft();
            RtLowerRight rtblr = rtb.getLowerRight();
            Double centerx = ((rtbrl.getX() + rtblr.getX()) / 2);
            Double centery = ((rtbrl.getY() + rtblr.getY()) / 2);

            //pre
            RtGcshapes gcss = ActYwTool.getCshapes(gcshapess, gnode);
            RtChildShapes prert = (gcss == null) ? null : gcss.getCshapes();
            ActYwGnode pregnode = (gcss == null) ? null : gcss.getGnode();
            if(prert != null){
                RtBoundsX prertb = prert.getBounds();
                RtUpperLeftX prertbrl = prertb.getUpperLeft();
                RtLowerRightX prertblr = prertb.getLowerRight();
                centerx = ((prertbrl.getX() + prertblr.getX()) / 2);
                centery = ((prertbrl.getY() + prertblr.getY()) / 2);
                System.out.println(prertb.getLowerRight().getX()+":"+prertb.getLowerRight().getY()+"|"+ prertb.getUpperLeft().getX()+":"+prertb.getUpperLeft().getX());
            }

            //网关后有多个子元素时定位
            if((pregnode != null) && ((GnodeType.getIdByGateway()).contains(pregnode.getType())) && StringUtil.checkNotEmpty(pregnode.getNexts())){
                int subs = pregnode.getNexts().indexOf(gnode);
                int size = pregnode.getNexts().size();
                if(subs/size > (size/2)){
                    centerx = centerx + ActYwTool.FLOW_POS_300;
                }else if(subs/size < (size/2)){
                    centerx = centerx - ActYwTool.FLOW_POS_300;
                }
            }

//            System.out.println(rtb.getLowerRight().getX()+":"+rtb.getLowerRight().getY()+"|"+ rtb.getUpperLeft().getX()+":"+rtb.getUpperLeft().getX());
            if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType())){
                rtbx.getUpperLeft().setX(centerx);
                rtbx.getUpperLeft().setY(ActYwTool.FLOW_POS_50);
                rtbx.getLowerRight().setX(centerx);
                rtbx.getLowerRight().setY(ActYwTool.FLOW_POS_100);
            }else if((GnodeType.GT_ROOT_END.getId()).equals(gnode.getType())){
                rtbx.getUpperLeft().setX(centerx - ActYwTool.FLOW_POS_100/2);
                rtbx.getUpperLeft().setY(centery + ActYwTool.FLOW_POS_100);
                rtbx.getLowerRight().setX(centerx + ActYwTool.FLOW_POS_100/2);
                rtbx.getLowerRight().setY(centery + ActYwTool.FLOW_POS_100 * 2);
            }else if((GnodeType.GT_ROOT_FLOW.getId()).equals(gnode.getType())){
            }else if((GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType())){
                rtbx.getUpperLeft().setX(centerx - ActYwTool.FLOW_POS_100/2);
                rtbx.getUpperLeft().setY(centery + ActYwTool.FLOW_POS_100);
                rtbx.getLowerRight().setX(centerx + ActYwTool.FLOW_POS_100/2);
                rtbx.getLowerRight().setY(centery + ActYwTool.FLOW_POS_100 * 2);
            }else if((GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType())){
                rtbx.getUpperLeft().setX(centerx - ActYwTool.FLOW_POS_100/2);
                rtbx.getUpperLeft().setY(centery + ActYwTool.FLOW_POS_100);
                rtbx.getLowerRight().setX(centerx + ActYwTool.FLOW_POS_100/2);
                rtbx.getLowerRight().setY(centery + ActYwTool.FLOW_POS_100 * 2);
            }else if((GnodeType.GT_PROCESS.getId()).equals(gnode.getType())){
                rtbx.getUpperLeft().setX(centerx - ActYwTool.FLOW_POS_100/2);
                rtbx.getUpperLeft().setY(centery + ActYwTool.FLOW_POS_100);
                rtbx.getLowerRight().setX(centerx + ActYwTool.FLOW_POS_100/2);
                rtbx.getLowerRight().setY(centery + ActYwTool.FLOW_POS_100 * 2);
            }else if((GnodeType.GT_PROCESS_START.getId()).equals(gnode.getType())){
            }else if((GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType())){
            }else if((GnodeType.GT_PROCESS_FLOW.getId()).equals(gnode.getType())){
            }else if((GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType())){
            }else if((GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType())){
                //TODO
                rtbx.getUpperLeft().setX(centerx - TASK_WIDTH/2);
                rtbx.getUpperLeft().setY(centery);
                rtbx.getLowerRight().setX(centerx + TASK_WIDTH);
                rtbx.getLowerRight().setY(centery + TASK_HEIGHT);
            }
        } catch (Exception e) {
            logger.warn("RtBoundsX:文件处理失败，使用默认数据生成，路径：" + file);
        }
        return rtbx;
    }
}
