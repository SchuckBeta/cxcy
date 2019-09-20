/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtChildShapes_]]文件
 * @date 2017年6月2日 下午2:18:49
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.ActYwResult;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 流程BPMNDi 画布节点.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:18:49
 *
 */
public class RtChildShapes {
    private static Logger logger = LoggerFactory.getLogger(RtChildShapes.class);
  /**
   * resourceId : sid-E58DA607-553C-4937-BCBB-E5E4AB217966 properties :
   * {"overrideid":"start","name":"申报项目开始","documentation":"","executionlisteners":"","initiator":"apply","formkeydefinition":"","formproperties":""}
   * stencil : {"id":"StartNoneEvent"} childShapes : [] outgoing :
   * [{"resourceId":"sid-EF1E73EA-D33F-4970-8BB4-059B7202490D"}] bounds :
   * {"lowerRight":{"x":681.296875,"y":45},"upperLeft":{"x":651.296875,"y":15}} dockers : [] target
   * : {"resourceId":"sid-2F9D9364-C2AD-45E4-A602-F8037678929F"}
   */

  private String resourceId;
  private IPropertiesX properties;
  private RtStencilX stencil;
  private RtBoundsX bounds;
  private List<RtOutgoing> outgoing;
  private RtTarget target;
  private List<RtChildShapes> childShapes;
  private List<RtDocker> dockers;

  public RtChildShapes() {
    super();
    this.childShapes = Lists.newArrayList();
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public IPropertiesX getProperties() {
    return properties;
  }

  public void setProperties(IPropertiesX properties) {
    this.properties = properties;
  }

  public RtStencilX getStencil() {
    return stencil;
  }

  public void setStencil(RtStencilX stencil) {
    this.stencil = stencil;
  }

  public RtBoundsX getBounds() {
    return bounds;
  }

  public void setBounds(RtBoundsX bounds) {
    this.bounds = bounds;
  }

  public RtTarget getTarget() {
    return target;
  }

  public RtChildShapes setTarget(RtTarget target) {
    this.target = target;
    return this;
  }

  public List<RtChildShapes> getChildShapes() {
    return childShapes;
  }

  public void setChildShapes(List<RtChildShapes> childShapes) {
    this.childShapes = childShapes;
  }

  public List<RtOutgoing> getOutgoing() {
    return outgoing;
  }

  public void setOutgoing(List<RtOutgoing> outgoing) {
    this.outgoing = outgoing;
  }

  public List<RtDocker> getDockers() {
    return dockers;
  }

  public void setDockers(List<RtDocker> dockers) {
    this.dockers = dockers;
  }

  /*****************************************************************************************************
   ** 历史代码分割线，写新代码请在分割线下面写.
   *****************************************************************************************************/
    /**
     * 初始化节点.
     * @param rtcs RtChildShapes
     * @param gnode 业务节点
     * @param rtb 流程边界
     * @return RtChildShapes
     */
    public static RtGcshapes init(ActYwResult rt, RtBounds rtb, RtGcshapes gcses, List<RtGcshapes> gcshapess) {
        ActYwGnode gnode = gcses.getGnode();
        RtChildShapes rtcs = gcses.getCshapes();
        if(rtcs == null){
            rtcs = new RtChildShapes();
        }

        if(gnode != null){
            if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_START + gnode.getId());
            }else if((GnodeType.GT_ROOT_END.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_ROOT_FLOW.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_PROCESS.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_PROCESS_START.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_PROCESS_FLOW.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }else if((GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType())){
                rtcs.setResourceId(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
            }

            if((gnode.getNode() != null) && StringUtil.isNotEmpty(gnode.getNode().getId())){
                StenType stype = StenType.getById(gnode.getNode().getId());
                if(stype != null){
                    rtcs.setStencil(new RtStencilX(stype.getKey()));
                }else{
                    logger.warn("RtChildShapes:[gnode.node.id->stype]未定义！");
                }
            }else{
                logger.warn("RtChildShapes:[gnode.node]不能为空！");
            }

            /**
             * 处理节点的childShapes属性.
             */
            rtcs.setBounds(RtBoundsX.init(gnode, rtb, gcshapess));
            if(StringUtil.checkNotEmpty(gnode.getSubs())){
                List<RtChildShapes> srtcs = Lists.newArrayList();
                for (ActYwGnode sub : gnode.getSubs()) {
                    RtGcshapes subgcshapes = RtChildShapes.init(rt, RtBoundsX.convertRtBounds(rtcs.getBounds()), new RtGcshapes(sub, new RtChildShapes()), gcshapess);
                    srtcs.add(subgcshapes.getCshapes());
                }
                rtcs.setChildShapes(srtcs);
            }else{
                rtcs.setChildShapes(Lists.newArrayList());
            }

            /**
             * 处理节点的Outgoings属性.
             */
            if(StringUtil.isNotEmpty(gnode.getOutgoing())){
                rtcs.setOutgoing(JsonAliUtils.toBean(gnode.getOutgoing(), RtOutgoing.class));
            }
            if(StringUtil.checkEmpty(rtcs.getOutgoing()) && StringUtil.checkNotEmpty(gnode.getNexts())){
                rtcs.setOutgoing(ActYwGnode.convertsOutgoing(ActYwTool.FLOW_ID_PREFIX , gnode.getNexts()));
            }

            rtcs.setProperties(IPropertiesX.init(gnode, rtcs));
            rtcs.setDockers(RtDocker.init(gnode, rtcs));

            /**
             * 处理节点的Target.
             */
            if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_ROOT_END.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_START.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType())){
                rtcs.setTarget(null);
            }else if((GnodeType.GT_ROOT_FLOW.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_FLOW.getId()).equals(gnode.getType())){
                rtcs.setTarget(new RtTarget(ActYwGnode.convertsTarget(ActYwTool.FLOW_ID_PREFIX, gnode.getNexts())));
            }
        }else{
            logger.warn("RtChildShapes:[gnode]不能为空！");
        }
        gcses.setGnode(gnode);
        gcses.setCshapes(rtcs);
        return gcses;
    }
}
