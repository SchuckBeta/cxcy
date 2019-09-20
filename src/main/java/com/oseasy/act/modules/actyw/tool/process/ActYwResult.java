/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process
 * @Description [[_ActYwResult_]]文件
 * @date 2017年6月2日 下午2:05:19
 *
 */

package com.oseasy.act.modules.actyw.tool.process;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwRtpl;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.RtBounds;
import com.oseasy.act.modules.actyw.tool.process.vo.RtChildShapes;
import com.oseasy.act.modules.actyw.tool.process.vo.RtGcshapes;
import com.oseasy.act.modules.actyw.tool.process.vo.RtModel;
import com.oseasy.act.modules.actyw.tool.process.vo.RtProperties;
import com.oseasy.act.modules.actyw.tool.process.vo.RtStencil;
import com.oseasy.act.modules.actyw.tool.process.vo.RtStencilset;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 工作流结果集(Json).
 * @author chenhao
 * @date 2017年6月2日 下午2:05:19
 */
public class ActYwResult implements ActYwRtpl{
    /**
     * . resourceId : b0f5e4b9748c416ba206a672dd013086 properties :
     * {"process_id":"state_project_audit","name":"国创项目审核流程","documentation":"","process_author":"zhangzheng","process_version":"","process_namespace":"http://www.activiti.org/processdef","executionlisteners":"","eventlisteners":"","signaldefinitions":"","messagedefinitions":"","conditionsequenceflow":"${pass==1}"}
     * stencil : {"id":"BPMNDiagram"}
     * childShapes : 子节点
     * bounds : {"lowerRight":{"x":1200,"y":2348},"upperLeft":{"x":0,"y":0}} stencilset :
     * {"url":"stencilsets/bpmn2.0/bpmn2.0.json","namespace":"http://b3mn.org/stencilset/bpmn2.0#"}
     * ssextensions : []
     */

    private String resourceId;
    private RtProperties properties;
    private RtStencil stencil;
    private RtBounds bounds;
    private RtStencilset stencilset;
    private List<RtChildShapes> childShapes;
    private List<?> ssextensions;

    public ActYwResult() {
        super();
    }

    public ActYwResult(String resourceId, RtProperties properties, RtStencil stencil, RtBounds bounds,
            RtStencilset stencilset, List<RtChildShapes> childShapes, List<?> ssextensions) {
        super();
        this.resourceId = resourceId;
        this.properties = properties;
        this.stencil = stencil;
        this.bounds = bounds;
        this.stencilset = stencilset;
        this.childShapes = childShapes;
        this.ssextensions = ssextensions;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public RtProperties getProperties() {
        return properties;
    }

    public void setProperties(RtProperties properties) {
        this.properties = properties;
    }

    public RtStencil getStencil() {
        return stencil;
    }

    public void setStencil(RtStencil stencil) {
        this.stencil = stencil;
    }

    public RtBounds getBounds() {
        return bounds;
    }

    public void setBounds(RtBounds bounds) {
        this.bounds = bounds;
    }

    public RtStencilset getStencilset() {
        return stencilset;
    }

    public void setStencilset(RtStencilset stencilset) {
        this.stencilset = stencilset;
    }

    public List<RtChildShapes> getChildShapes() {
        return childShapes;
    }

    public void setChildShapes(List<RtChildShapes> childShapes) {
        this.childShapes = childShapes;
    }

    public List<?> getSsextensions() {
        return ssextensions;
    }

    public void setSsextensions(List<?> ssextensions) {
        this.ssextensions = ssextensions;
    }

    /**
     * 根据json生成对象.
     * @author chenhao
     * @param jsons json
     * @return ActYwResult
     */
    public static ActYwResult genActYwResult(String jsons) {
      return genActYwResults(jsons).get(0);
    }

    /**
     * 根据json生成对象.
     * @author chenhao
     * @param json json
     * @return List
     */
    public static List<ActYwResult> genActYwResults(String json) {
      return JsonAliUtils.toBean("[" + dealJsonException(json) + "]", ActYwResult.class);
    }

    /**
     * 处理json中对象为""导致的异常.
     * @param json json
     * @return String
     */
    public static String dealJsonException(String json) {
        json = json.replaceAll(" ", "");
        json = json.replaceAll("\"tasklisteners\":\"\"", "\"usertaskassignment\":{}");
        json = json.replaceAll("\"usertaskassignment\":\"\"", "\"usertaskassignment\":{}");
        json = json.replaceAll("\"formproperties\":\"\"", "\"formproperties\":{}");
        return json.replaceAll("\"assignment\":\"\"", "\"assignment\":{}");
    }


    /**
     * 根据流程、流程节点及模型生成流程JSON对象.
     * @param rt 结果集对象
     * @param group 流程
     * @param gnodes 流程节点
     * @param gends 流程结束或网关节点
     * @param rtModel 模型
     * @return ActYwResult
     */
    public static ActYwResult init(ActYwResult rt, RtModel rtModel, ActYwGroup group, List<ActYwGnode> gnodes) {
        if (rt == null) {
            rt = new ActYwResult();
        }
        rt.setSsextensions(Lists.newArrayList());
        rt.setStencil(new RtStencil(StenType.ST_BPMDI.getKey()));
        rt.setStencilset(RtStencilset.init(group));
        rt.setResourceId(group.getFlowType() + group.getId());
        rt.setProperties(RtProperties.init(group, rtModel));
        rt.setBounds(RtBounds.init(group, gnodes));
        List<RtChildShapes> childShapes = Lists.newArrayList();

        /**
         * 循环节点时需要注意子流程内部节点可能会被重复执行，所有当前循环，只循环parent_id = 1.
         */
        List<RtGcshapes> gcshapesss = Lists.newArrayList();
        for (ActYwGnode gnode : gnodes) {
            if((GnodeType.getIdByRoot()).contains(gnode.getType())){
                RtGcshapes gcshapes = RtChildShapes.init(rt, rt.getBounds(), new RtGcshapes(gnode, new RtChildShapes()), gcshapesss);
                childShapes.add(gcshapes.getCshapes());
                gcshapesss.add(gcshapes);
            }
        }

        rt.setChildShapes(childShapes);
        return rt;
    }
    public static ActYwResult init(RtModel rtModel, ActYwGroup group, List<ActYwGnode> gnodes) {
        return init(null, rtModel, group, gnodes);
    }

    public static void main(String[] args) {
        RtModel model = new RtModel();
        ActYwGroup group = new ActYwGroup();
        model.setKey("KAAAA");
        model.setName("NAAAA");
        group.setAuthor("chenhao");
        group.setVersion("v1.0");
        group.setFlowType(FlowType.FWT_XM.getKey());
        group.setId("1111111111111111111");
        List<ActYwGnode> ls = Lists.newArrayList();
        System.out.println(JsonMapper.toJsonString(init(model, group, ls)));
    }
}
