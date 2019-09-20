package com.oseasy.pro.modules.gcontest.vo;

import com.oseasy.act.modules.actyw.dao.ActYwGnodeDao;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;

/**
 * Created by zhangzheng on 2017/8/4.
 */
public class GContestNodeVo {
    private static ActYwGnodeDao actYwGnodeDao = SpringContextHolder.getBean(ActYwGnodeDao.class);
    public static final String YW_ID = "706c4e8c747e4c3d948ff604a769a968" ;  //act_yw id
    public static final String YW_FID = "e630a9c65165494bb66eae2f777401c7" ;  //act_group id
    /**
     * Gnode节点ID.
     */
    public static final String G_START_ID = "58c4c039c0ec42939069545087363149";//开始节点
    public static final String G_END_ID = "60db384313df43a797e9d5dacf372daa";//结束节点
    public static final String GNODE_WP_ID = "8fad3440f3314ba9b990d0afea119068" ; //学院专家评分节点
    public static final String GNODE_FIRST_ID = "a3dbb35641d648dcb92fe28c650992f3" ; //学院专家评分节点
    public static final String GNODE_SECOND_ID = "8b1df02110984245951d310aa88eb812" ; //学院秘书审核节点
    public static final String GNODE_THREE_ID = "6b26844cdfb54714863f1378ce0a1aea"; //学校专家评分节点
    public static final String GNODE_FOUR_ID = "1518b3fd1e804a96962f6de802443e21"; //学校秘书审核节点
    public static final String GNODE_LY_ID = "afbf23111aba4b458d3cfea73e9e659b" ; //学院专家评分节点
    public static final String GNODE_FIVE_ID = "ddcdcecfddf445afbb3338efed5155c8"; //学校秘书路演审核节点
    public static final String GNODE_PJ_ID = "ad8b199a588b494fa7f0bdea7b9d0982" ; //学院专家评分节点
    public static final String GNODE_SIX_ID = "8b6097cd298e4a16ba1dd2196894a1b7"; //学校秘书评级审核节点
    /**
     * Node节点ID.
     */
    public static final String GN_START_ID = StenType.ST_START_EVENT_NONE.getId();//开始节点
    public static final String GN_END_ID = StenType.ST_END_EVENT_NONE.getId();//结束节点
    public static final String GNNODE_WP_ID = "50" ; //学院专家评分节点
    public static final String GNNODE_FIRST_ID = "52" ; //学院专家评分节点
    public static final String GNNODE_SECOND_ID = "51" ; //学院秘书审核节点
    public static final String GNNODE_THREE_ID = "54"; //学校专家评分节点
    public static final String GNNODE_FOUR_ID = "53"; //学校秘书审核节点
    public static final String GNNODE_LY_ID = "60" ; //学院专家评分节点
    public static final String GNNODE_FIVE_ID = "61"; //学校秘书路演审核节点
    public static final String GNNODE_PJ_ID = "70" ; //学院专家评分节点
    public static final String GNNODE_SIX_ID = "71"; //学校秘书评级审核节点

   public static String getGNodeIdByNodeId(String nodeId) {
     return nodeId;
   }

//   public static String getGNodeIdByNodeId(String nodeId) {
//     return actYwGnodeDao.findGnodeIDByNode(nodeId,YW_ID);
//   }
}
