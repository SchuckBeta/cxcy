/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.util.List;

/**
 * 流程节点实体接口.
 * @author chenhao
 */
public interface IGnode {
    public static final String IGNODE_ID = "gnodeId";
    /**********************************************************************************
     * 获取流程申请ID.
     * @return String
     */
    String id();
    String id(String id);

    /**
     * 获取流程申请名称.
     * @return String
     */
    String name();

    /**
     * .
     */
    List<? extends IFrorm> igforms();
    List<? extends IFrorm> igforms(List<? extends IFrorm> ifrorms);
}
