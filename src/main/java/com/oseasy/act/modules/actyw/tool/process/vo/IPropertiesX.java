/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 节点属性接口.
 * @author chenhao
 *
 */
public interface IPropertiesX {
    static Logger logger = LoggerFactory.getLogger(IPropertiesX.class);

    /**
     * 初始化属性X.
     * @param gnode 流程节点
     * @return RtProperties
     */
    public static IPropertiesX init(ActYwGnode gnode, RtChildShapes rtcs) {
        IPropertiesX rtpx = null;
        String file = FileUtil.getClassPath(ActYwTool.FLOW_JSON_PATH) + RtPropertiesX.class.getSimpleName() + StringUtil.JSON;
        try {
            if(((GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType()))){
                rtpx = RtPropertiesX.init(new RtPropertiesX(), gnode, rtcs);
            }else if((GnodeType.GT_ROOT_END.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType())){
                rtpx = RtPropertiesXend.init(rtpx, gnode, rtcs);
            }else{
                rtpx = RtPropertiesX.init(JsonAliUtils.readBeano(file, RtPropertiesX.class), gnode, rtcs);
            }
        } catch (Exception e) {
            logger.warn("RtPropertiesX:文件处理失败，使用默认数据生成，路径：" + file);
            rtpx = RtPropertiesX.init(null, gnode, rtcs);
        }
        return rtpx;
    }
}
