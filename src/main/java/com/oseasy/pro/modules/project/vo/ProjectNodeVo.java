package com.oseasy.pro.modules.project.vo;

import java.util.Map;


import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.dao.ActYwGnodeDao;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;

/**
 * Created by zhangzheng on 2017/8/4.
 */
public class ProjectNodeVo {
//    public static final String START_NODE_ID = "3a920eaf37644efa98637ca08f93fbd7" ; //立项审核标准节点id
//    public static final String MIDDLE_NODE_ID = "14145f3b5f0943ee862aec4e53db9c55" ; //中期检查标准节点id
//    public static final String CLOSE_NODE_ID = "3c78e63aeb8d4d5ebb2b77020d4aff14"; //结项审核标准节点id
//   public static final String  REPLY_NODE_ID = "b6e555da3fd64f85afde1a047e8cc5da"; //答辩评分标准节点id
//    public static final String ASSESS_NODE_ID = "703ff528c7c747788d962448e18a3bec"; //结果评定标准节点id

    private static ActYwGnodeDao actYwGnodeDao = SpringContextHolder.getBean(ActYwGnodeDao.class);
    /**
     * Gnode节点ID.
     */
    public static final String YW_ID = "2d7850ca88324274b0da3c18cb292f96" ;  //act_yw id
    public static final String YW_FID = "87b0264565af4d588e2ece81d4a5b2f0" ;  //act_group id
    public static final String P_START_ID = "0bb9cbc4d2104c70bee38f3e09a7ee35"; //开始节点
    public static final String P_END_ID = "348f7159d179490680d021572ca3bb0a"; //结束节点
    public static final String PNODE_START_ID = "0bb6d4218eff4392931e1dbb2a921b0f" ; //立项审核标准节点id
    public static final String PNODE_MIDDLE_ID = "a9a782a5933d43c185c76ee1f0a51cc6" ; //中期检查标准节点id
    public static final String PNODE_CLOSE_ID = "65fb756bff0c43cfb3e98dd0cd202ed9"; //结项审核标准节点id
    public static final String PNODE_REPLY_ID = "fd4474f1ed234c2ab016e3dbfc9b14ae"; //答辩评分标准节点id
    public static final String PNODE_ASSESS_ID = "4e39f2f04b9d48a6a307f603c458b1ce"; //结果评定标准节点id
    /**
     * Node节点ID.
     */
    public static final String PN_START_ID = StenType.ST_START_EVENT_NONE.getId() ; //开始节点
    public static final String PN_END_ID = StenType.ST_END_EVENT_NONE.getId() ; //结束节点
    public static final String PNNODE_START_ID = "1" ; //立项审核标准节点id
    public static final String PNNODE_MIDDLE_ID = "2" ; //中期检查标准节点id
    public static final String PNNODE_CLOSE_ID = "3"; //结项审核标准节点id
    public static final String PNNODE_REPLY_ID = "35"; //答辩评分标准节点id
    public static final String PNNODE_ASSESS_ID = "36"; //结果评定标准节点id

   public static String getGNodeIdByNodeId(String nodeId) {
     return nodeId;
   }

   /**
    * 大创存放附件的节点信息.
    * @return
    */
   public static ActYwGnode gnodeFiles() {
       return new ActYwGnode(ProjectNodeVo.YW_FID, ProjectNodeVo.P_START_ID, "大创项目");
   }
   public static Map<String, ActYwGnode> gnodes() {
       Map<String, ActYwGnode> gnodes = Maps.newHashMap();
       gnodes.put(ProjectNodeVo.YW_FID, new ActYwGnode(ProjectNodeVo.YW_FID, ProjectNodeVo.PNODE_START_ID, "立项审核"));
       gnodes.put(ProjectNodeVo.YW_FID, new ActYwGnode(ProjectNodeVo.YW_FID, ProjectNodeVo.PNODE_MIDDLE_ID, "结项审核"));
       gnodes.put(ProjectNodeVo.YW_FID, new ActYwGnode(ProjectNodeVo.YW_FID, ProjectNodeVo.PNODE_CLOSE_ID, "结项审核"));
       gnodes.put(ProjectNodeVo.YW_FID, new ActYwGnode(ProjectNodeVo.YW_FID, ProjectNodeVo.PNODE_REPLY_ID, "答辩评分"));
       gnodes.put(ProjectNodeVo.YW_FID, new ActYwGnode(ProjectNodeVo.YW_FID, ProjectNodeVo.PNODE_ASSESS_ID, "结果评定"));
       return gnodes;
   }
}
