/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process
 * @Description [[_ActYwTool_]]文件
 * @date 2017年6月2日 下午4:35:49
 *
 */

package com.oseasy.act.modules.actyw.tool.process;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.RtGcshapes;
import com.oseasy.act.modules.actyw.tool.process.vo.RtModel;
import com.oseasy.act.modules.actyw.tool.process.vo.RtSvl;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;



/**
 * 自定义流程工具类.
 *
 * @author chenhao
 * @date 2017年6月2日 下午4:35:49
 *
 */
public class ActYwTool {
  private static Logger logger = LoggerFactory.getLogger(ActYwTool.class);
  public static final String FLOW_GATEWAY_SUCCESS = "SUCCESS";
  public static final String FLOW_GATEWAY_FAIL = "FAIL";
  public static final String FLOW_GNODE = "GNODE";
  public static final String FLOW_GNODE_END = "GNODEEND";
  public static final String FLOW_LINKS = "LINKS";
  public static final String FLOW_LINKS_FILTER = "LINKSFILTER";
  public static final String FLOW_LINKS_END = "LINKSEND";
  public static final String FLOW_JSON_PATH = "/json/actyw/";
  public static final String FLOW_ID_START = "start";
  public static final String FLOW_ASSIGN_USER = "assignUser";
  public static final String FLOW_ID_PREFIX = "sid-";
  public static final String FLOW_ROLE_ID_PREFIX = "R";
  public static final String FLOW_ROLE_PREFIX = "${";
  public static final String FLOW_ROLE_POSTFIX = "}";
  public static final String FLOW_ROLE_PREFIX_KH = "{";
  public static final String FLOW_ROLE_POSTFIX_S = "s";
  public static final String FLOW_PROP_OBJECT = "{}";
  public static final String FLOW_PROP_NULL = "";
  public static final String FLOW_PROP_LIST = "[]";
  public static final String FLOW_PROP_GATEWAY_STATE = "grade";
  public static final String FLOW_PROP_GATEWAY_REG_EQ = "==";
  public static final String FLOW_PROP_GATEWAY_REG_OR = " || ";
  public static final String FLOW_PROP_TRUE = "true";
  public static final String FLOW_PROP_FALSE = "false";

  public static final Double FLOW_POS_0 = 0.0;
  public static final Double FLOW_POS_50 = 50.0;
  public static final Double FLOW_POS_100 = 100.0;
  public static final Double FLOW_POS_300 = 300.0;

  /*****************************************************************************************************
   ** 历史代码分割线，写新代码请在分割线上面写.
   *****************************************************************************************************/
  /**
   * 初始化nexts和Subs属性.
   * @param sourcelist 源数据
   * @return List
   */
  public static GnodeParam getParam(List<ActYwGnode> sourcelist) {
      GnodeParam gparam = new GnodeParam(sourcelist.size());
      for (ActYwGnode curGnode : sourcelist) {
          if((curGnode.getType()).equals(GnodeType.GT_ROOT_END.getId())){
              gparam.setEndSize(gparam.getEndSize() + 1);
          }
      }
      return gparam;
  }

  /**
   * 初始化nexts和Subs属性.
   * @param sourcelist 源数据
   * @return List
   */
  public static RtGcshapes getCshapes(List<RtGcshapes> gcshapess, ActYwGnode gnode) {
      if((gnode == null) || StringUtil.isEmpty(gnode.getPreId())){
          return null;
      }

      RtGcshapes gcshape = null;
      for (RtGcshapes curGcshapes : gcshapess) {
          if((curGcshapes.getGnode() != null) && StringUtil.isNotEmpty(curGcshapes.getGnode().getId()) && (gnode.getPreId()).contains(curGcshapes.getGnode().getId())){
              if((GnodeType.getIdByFlow()).contains(curGcshapes.getGnode().getType())){
                  gcshape = getCshapes(gcshapess, curGcshapes.getGnode());
              }else{
                  gcshape = curGcshapes;
              }
          }
      }
      return gcshape;
  }

  /**
   * 初始化nexts和Subs属性.
   * @param sourcelist 源数据
   * @return List
   */
  public static List<ActYwGnode> initProps(List<ActYwGnode> sourcelist) {
      //添加过滤，避免出现驳回时，导致死递归循环(出现驳回节点时，New新对象，避免Json异常).
      List<String> filters = Lists.newArrayList();
      for (ActYwGnode curGnode : sourcelist) {
          for (ActYwGnode curNgnode : sourcelist) {
              if(StringUtil.isNotEmpty(curNgnode.getPreId())){
                  ActYwGnode newgnode = null;
                  if((curNgnode.getPreId()).contains(curGnode.getId())){
                      if(curGnode.getNexts() == null){
                          curGnode.setNexts(Lists.newArrayList());
                      }

                      if((filters).contains(curNgnode.getId())){
                          newgnode = new ActYwGnode(curNgnode, true);
                          newgnode.setNexts(Lists.newArrayList());
                      }

                      if(newgnode != null){
                          curGnode.getNexts().add(newgnode);
                      }else{
                          curGnode.getNexts().add(curNgnode);
                      }
                  }

                  if(((curNgnode.getPreId()).contains(StringUtil.DOTH)) && !(filters).contains(curNgnode.getId())){
                      filters.add(curNgnode.getId());
                  }
              }

              if((curGnode.getId()).equals(curNgnode.getParentId())){
                  if(curGnode.getSubs() == null){
                      curGnode.setSubs(Lists.newLinkedList());
                  }
                  curGnode.getSubs().add(curNgnode);
              }
          }
      }
      return sourcelist;
  }

  /**
   * 初始化开始节点的nexts和Subs属性.
   * @param start 开始节点
   * @param sourcelist 源数据
   * @return ActYwGnode
   */
  public static ActYwGnode initStartProps(ActYwGnode start, List<ActYwGnode> sourcelist) {
      if(start == null){
          return null;
      }

      for (ActYwGnode curNgnode : sourcelist) {
          if(StringUtil.isNotEmpty(curNgnode.getPreId()) && (curNgnode.getPreId()).contains(start.getId())){
              if(start.getNexts() == null){
                  start.setNexts(Lists.newArrayList());
              }
              start.getNexts().add(curNgnode);
          }

          if((start.getId()).equals(curNgnode.getParentId())){
              if(start.getSubs() == null){
                  start.setSubs(Lists.newLinkedList());
              }
              start.getSubs().add(curNgnode);
          }
      }
      return start;
  }

  public static void main(String[] args) {
      List<ActYwGnode> gnodes = ActYwTool.initGnodes();
      System.out.println(showActYwGnode(gnodes));
  }
    //运行中节点curnode 遍历中stu
  public static ActYwGnode complainGnode(ActYwGnode stu,ActYwGnode curnode) {
      if ((curnode.getLevel() == null) || (stu.getLevel() == null) || StringUtil.isEmpty(curnode.getParentId()) || StringUtil.isEmpty(stu.getParentId()) || StringUtil.isEmpty(curnode.getType()) || StringUtil.isEmpty(stu.getType())) {
          return null;
      }

      if ((GnodeType.getIdByRoot()).contains(curnode.getType()) && (GnodeType.getIdByRoot()).contains(stu.getType())) {
          if (curnode.getLevel() > stu.getLevel() || curnode.getLevel().equals(stu.getLevel())) {
              return stu;
          }
      } else if ((GnodeType.getIdByRoot()).contains(curnode.getType()) && (GnodeType.getIdByProcess()).contains(stu.getType())) {
          if (curnode.getLevel() > stu.getParent().getLevel() || curnode.getLevel().equals(stu.getParent().getLevel()) ) {
               return stu;
          }
      } else if ((GnodeType.getIdByProcess()).contains(curnode.getType()) && (GnodeType.getIdByRoot()).contains(stu.getType())) {
          if (curnode.getParent().getLevel() > stu.getLevel() || curnode.getParent().getLevel().equals(stu.getLevel())) {
              return stu;
          }
      } else if ((GnodeType.getIdByProcess()).contains(curnode.getType()) && (GnodeType.getIdByProcess()).contains(stu.getType())) {
          if(!curnode.getParent().getId().equals(stu.getParent().getId())){
              if (curnode.getParent().getLevel() > stu.getParent().getLevel()) {
                  return stu;
              }
          }else{
              if (curnode.getLevel() > stu.getLevel() || curnode.getLevel().equals(stu.getLevel())) {
                    return stu;
              }
          }

      } else {
          logger.warn("节点类型错误或为空！");
      }
      return null;
  }



  public static String showActYwGnode(List<ActYwGnode> gnodes) {
      StringBuffer sb = new StringBuffer("[");
      for (ActYwGnode gnode : gnodes) {
          sb.append("{");
          sb.append(gnode.getName());
          sb.append(":");
          sb.append(gnode.getId());
          sb.append("}");
      }
      sb.append("]");
      System.out.println(sb.toString());
      return sb.toString();
  }

  /**
   * 方案0：初始化节点链条(不包含当前结点，包含触发暂停的网关和结束节点-不支持网关节点).
   * @param gnode 当前结点
   * @param sourcelist 源数据（需要处理nexts参数）
   * @param curGwgnodes 当前结点对应的网关节点或开始节点对应的数据
   * @return List
   */
    public static GnodeMap initMap(GnodeMap start, ActYwGnode gnode, List<GnodeGwmap> sourcelist, List<ActYwGnode> filters) {
        if((gnode == null) || StringUtil.checkEmpty(gnode.getNexts())){
            return null;
        }

        if((!(GnodeType.getIdByEnd()).contains(gnode.getType())) && StringUtil.checkEmpty(gnode.getNexts())){
            return null;
        }

        /**
         * 初始化当前结点的filters参数（根开始、网关节点）.
         */
        if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType())){
            for (GnodeGwmap curGmap : sourcelist) {
                if(curGmap.getKey().equals(gnode)){
                    filters = curGmap.getSortList();
                    break;
                }
            }
        }else if((GnodeType.getIdByGateway()).contains(gnode.getType())){
            if(gnode.getGateways() == null){
                gnode.setGateways(Lists.newArrayList());
            }

            for (GnodeGwmap curGmap : sourcelist) {
                if(curGmap.getKey().equals(gnode)){
                    filters = curGmap.getSortList();
                    break;
                }
            }
        }

        /**
         * 非结束节点，filters 不能为空!
         */
        if(StringUtil.checkEmpty(filters) && !(GnodeType.getIdByEnd()).contains(gnode.getType())){
            logger.error("非结束节点，filters 不能为空!");
            return null;
        }

        GnodeMap maps = new GnodeMap(gnode);
        for (ActYwGnode curGnode : filters) {
          if(StringUtil.isEmpty(curGnode.getPreId())){
              //logger.info("只有开始节点的preid为空(110|210 = " + curGnode.getType() + "?)");
              continue;
          }

          if((GnodeType.getIdByGateway()).contains(curGnode.getType())){
              /**
               * 如果遇到网关,链条断开,标记非结束.
               */
              maps.setIsend(false);
              maps.setGend(curGnode);
              break;
          }else{
              if((curGnode.getPreId()).contains(gnode.getId())){
                  maps.getLinks().add(curGnode);
              }
          }

          if((GnodeType.getIdByEnd()).contains(curGnode.getType())){
              /**
               * 如果遇到结束节点,链条断开,标记为结束.
               */
              maps.setIsend(true);
              maps.setGend(curGnode);
              break;
          }
      }

      /**
       * 下一个节点为空，直接返回(可以处理递归时遇到end的时候直接跳出).
       */
      if(StringUtil.checkEmpty(gnode.getNexts())){
          return maps;
      }

      /**
       * 下一个节不为空，根据当前结点类型，可以判断是初始化gateway属性还是初始化links属性.
       */

      for (ActYwGnode curgnode : gnode.getNexts()) {
          GnodeMap curGnodeMap = initMap(start, curgnode, sourcelist, filters);
          if((curGnodeMap == null)){
              continue;
          }

          showActYwGnode(curGnodeMap.getLinks());
          if(StringUtil.checkEmpty(curGnodeMap.getLinks())){
              continue;
          }

          if((GnodeType.getIdByGateway()).contains(gnode.getType())){
              List<ActYwGnode> templinks = Lists.newArrayList(curgnode);
              templinks.addAll(curGnodeMap.getLinks());
              gnode.getGateways().add(templinks);
          }else{
              maps.getLinks().addAll(curGnodeMap.getLinks());
          }
      }

      if((start.getGnode().getId()).contains(gnode.getId())){
          start.setLinks(maps.getLinks());
          showActYwGnode(start.getLinks());
      }
      return maps;
  }

    /**
     * 方案0：根据开始节点或网关节点获取节点链.
     * @param gnode 节点：网关或开始
     * @param sourcelist 源数据
     * @param filters 过滤节点
     * @return GnodeMap
     */
    public static GnodeMap initMaps(ActYwGnode gnode, List<GnodeGwmap> sourcelist, List<ActYwGnode> filters) {
        if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType()) || (GnodeType.getIdByGateway()).contains(gnode.getType())){
            return initMap(new GnodeMap(gnode), gnode, sourcelist, null);
        }else{
            return initMap(new GnodeMap(gnode), gnode, sourcelist, filters);
        }
    }

    public static GnodeMap initMapStart(ActYwGnode start, List<GnodeGwmap> sourcelist) {
        return initMaps(start, sourcelist, null);
    }

    /**
     * 方案0：初始化所有网关节点链.
     * @param start 开始节点
     * @param sourcelist 元数据
     * @param gylist 网关原列表
     * @return
     */
    public static List<GnodeMap> initLinks(ActYwGnode start, List<GnodeGwmap> sourcelist, List<ActYwGnode> gysourcelist) {
        List<GnodeMap> gmaps = Lists.newArrayList();
        gmaps.add(initMapStart(start, sourcelist));
        for (ActYwGnode curGnode : gysourcelist) {
            if((GnodeType.getIdByGateway()).contains(curGnode.getType())){
                gmaps.add(initMaps(curGnode, sourcelist, null));
            }
        }
        return gmaps;
    }


    /********************************************************************
     * 方案一：初始化分段链表.
     * @param sgends 开始、结束、网关节点
     * @param sourcelist 源数据
     * @return List
     */
    public static List<GnodeGsep> initLinks(List<ActYwGnode> sgends, List<ActYwGnode> sourcelist){
        List<GnodeGsep> gnodeGseps = Lists.newArrayList();
        for (ActYwGnode end : sgends) {
            GnodeGsep ggsep = initLink(null, end, sourcelist);
            List<ActYwGnode> list = Lists.newArrayList();
            ActYwGnode.revertList(list, ggsep.getSortList());
            ggsep.setSortList(list);
            gnodeGseps.add(ggsep);
        }
        return gnodeGseps;
    }

    /**
     * 方案一：递归初始化分段链表.
     * @param gnodeGsep 结果集
     * @param sgends 结束节点
     * @param sourcelist 源数据
     * @return List
     */
    public static GnodeGsep initLink(GnodeGsep gnodeGsep, ActYwGnode end, List<ActYwGnode> sourcelist){
        if((end == null) || StringUtil.checkEmpty(sourcelist)){
            logger.warn("参数为空！");
            return gnodeGsep;
        }

        if(gnodeGsep == null){
            gnodeGsep = new GnodeGsep(end, Lists.newArrayList());
        }

        if((GnodeType.getIdByStart()).contains(end.getType())){
            gnodeGsep.setStart(end);
            gnodeGsep.getSortList().add(end);
        }else if((GnodeType.getIdByGateway()).contains(end.getType()) && StringUtil.checkNotEmpty(gnodeGsep.getSortList())){
            gnodeGsep.setStart(end);
            gnodeGsep.getSortList().add(end);
        }else{
            ActYwGnode newend = null;
            for (ActYwGnode curgnode : sourcelist) {
                if((curgnode.getId()).equals(end.getPreId())){
                    gnodeGsep.getSortList().add(end);
                    gnodeGsep.setStart(curgnode);
                    newend = curgnode;
                }
            }
            gnodeGsep = initLink(gnodeGsep, newend, sourcelist);
        }
        return gnodeGsep;
    }

    /********************************************************************
     * 方案二：初始化树链表.
     * @param start 开始
     * @param sourcelist 源数据
     * @return List
     */
    public static GnodeGstree initTrees(ActYwGnode start, List<ActYwGnode> sourcelist){
        if(!(GnodeType.getIdByStart()).contains(start.getType())){
            logger.warn("第一个节点不是开始节点！");
            return null;
        }

        return initTree(new GnodeGstree(start), sourcelist, null, Lists.newArrayList());
    }

    /**
     * 方案二：递归初始化树链表.
     * @param gstree 开始节点
     * @param sourcelist 源数据
     * @param filters 已执行驳回节点ID（preId中含有,分隔符的节点）
     * @return List
     */
    public static GnodeGstree initTree(GnodeGstree gstree, List<ActYwGnode> sourcelist, GnodeGstree prestree, List<String> filters){
        if((gstree.getGnode() == null) || StringUtil.isEmpty(gstree.getGnode().getId())){
            return gstree;
        }

        //添加过滤，避免出现驳回时，导致死递归循环(出现驳回节点时，New新对象，避免Json异常).
        if(filters == null){
            filters = Lists.newArrayList();
        }

        //判断当前结点是否为结束节点，结束节点表示结尾，不用找nexts
        if((GnodeType.getIdByEnd()).contains(gstree.getGnode().getType())){
            return gstree;
        }

        //判断当前结点ID是否在过滤列表，不用找nexts
//        if((filters).contains(gstree.getGnode().getId())){
//            System.out.println("----------------------A----"+filters.size());
//            if(prestree != null){
//                GnodeGstree newgstree = new GnodeGstree(gstree);
//                newgstree.setNexts(Lists.newArrayList());
//                prestree.getNexts().add(gstree);
//            }
//            return gstree;
//        }

        //判断当前结点preId是否含有[,]分隔符,如果有表示当前结点是驳回节点，添加到过滤列表中
        if(StringUtil.isNotEmpty(gstree.getGnode().getPreId()) && ((gstree.getGnode().getPreId()).contains(StringUtil.DOTH))){
            filters.add(gstree.getGnode().getId());
        }

        List<GnodeGstree> gsubtrees = Lists.newArrayList();
        for (ActYwGnode curGnode : sourcelist) {
            if(StringUtil.isNotEmpty(curGnode.getPreId()) && (curGnode.getPreId()).contains(gstree.getGnode().getId())){
                gsubtrees.add(new GnodeGstree(curGnode));
            }
        }

        if(StringUtil.checkNotEmpty(gsubtrees)){
            for (GnodeGstree stree : gsubtrees) {
                if((filters).contains(stree.getGnode().getId())){
                    continue;
                }

                GnodeGstree subGstree = initTree(stree, sourcelist, gstree, filters);
                if(StringUtil.checkNotEmpty(subGstree.getNexts())){
                    stree.setNexts(subGstree.getNexts());
                }
            }

            gstree.setNexts(gsubtrees);
        }
        return gstree;
    }

  /**
   * 生成modelData数据.
   * @author chenhao
   * @param category 类目
   * @param name 流程名称
   * @param key 流程标识
   * @param description 流程描述
   * @param jsonXml 流程json
   * @param repositoryService 流程服务
   * @return Model
   */
  public static Model genModelData(String category, String name, String key, String description, String jsonXml, RepositoryService repositoryService) {
    return genModelData(new RtModel(name, key, description, category, jsonXml, null), repositoryService);
  }

  /**
   * 生成modelData数据.
   * @author chenhao
   * @param rtModel 模型
   * @return  Model
   */
  public static Model genModelData(RtModel rtModel, RepositoryService repositoryService) {
    ObjectMapper objectMapper = new ObjectMapper();
    org.activiti.engine.repository.Model modelData = repositoryService.newModel();
    rtModel.setDescription(StringUtil.defaultString(rtModel.getDescription()));
    modelData.setKey(StringUtil.defaultString(rtModel.getKey()));
    modelData.setName(rtModel.getName());
    modelData.setCategory(rtModel.getCategory());
    modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1)));

    ObjectNode modelObjectNode = objectMapper.createObjectNode();
    modelObjectNode.put(RtSvl.RtModelVal.M_NAME, rtModel.getName());
    modelObjectNode.put(RtSvl.RtModelVal.M_REVISION, modelData.getVersion());
    modelObjectNode.put(RtSvl.RtModelVal.M_DESCRIPTION, rtModel.getDescription());
    modelData.setMetaInfo(modelObjectNode.toString());
    logger.info("模型 [" + rtModel.getName() + "] 数据生成成功！");
    return modelData;
  }

  /**
   * 初始化节点.
   */
  public static List<ActYwGnode> initGnodes() {
      String file = FileUtil.getClassPath(ActYwTool.FLOW_JSON_PATH) + ActYwGnode.class.getSimpleName() + StringUtil.JSON;
      try {
          return JsonAliUtils.readBean(file, ActYwGnode.class);
      } catch (Exception e) {
          logger.warn("ActYwGnode:文件处理失败，使用默认数据生成，路径：" + file);
      }
      return null;
  }
}
