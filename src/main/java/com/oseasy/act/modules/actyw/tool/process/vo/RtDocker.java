/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtDocker_]]文件
 * @date 2017年6月2日 下午2:20:32
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.util.common.utils.StringUtil;

/**
 * RtDocker关联坐标.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:20:32
 *
 */
public class RtDocker {
    private static Logger logger = LoggerFactory.getLogger(RtDocker.class);
    private Double x;
    private Double y;

    public RtDocker() {
        super();
    }

    /**
     * 根据坐标生成RtDocker对象 .
     *
     * @author chenhao
     * @param x
     *            x坐标
     * @param y
     *            y坐标
     */
    public RtDocker(Double x, Double y) {
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


    /**
     * 初始化RtDocker.
     * @param gnode 流程节点
     * @return List
     */
    public static List<RtDocker> init(ActYwGnode gnode, RtChildShapes rtcs) {
        RtBoundsX rtbx = rtcs.getBounds();
        RtUpperLeftX rtbulX = rtbx.getUpperLeft();
        RtLowerRightX rtblrX = rtbx.getLowerRight();
        Double centerx = ((rtblrX.getX() + rtbulX.getX()) / 2);
        Double centery = ((rtblrX.getY() + rtbulX.getY()) / 2);

        List<RtDocker> dockers = Lists.newArrayList();
        //TODO 处理Docker数据
        if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_ROOT_END.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_ROOT_FLOW.getId()).equals(gnode.getType())){
            dockers.add(new RtDocker(centerx, centery));
            dockers.add(new RtDocker(100.0, 100.0));
//          dockers.add(new RtDocker(centerx, centery));
//          dockers.add(new RtDocker(StringUtil.randomDouble(0, centerx/2), StringUtil.randomDouble(0, centery/2)));
        }else if((GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_PROCESS.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_PROCESS_START.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_PROCESS_FLOW.getId()).equals(gnode.getType())){
            dockers.add(new RtDocker(centerx, centery));
            dockers.add(new RtDocker(100.0, 100.0));
//            dockers.add(new RtDocker(centerx, centery));
//            dockers.add(new RtDocker(StringUtil.randomDouble(0, centerx/2), StringUtil.randomDouble(0, centery/2)));
        }else if((GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType())){
        }else if((GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType())){
        }
        dockers = Lists.newArrayList();
        dockers.add(new RtDocker(0.0, 0.0));
        dockers.add(new RtDocker(10.0, 10.0));
        return dockers;
    }
}
