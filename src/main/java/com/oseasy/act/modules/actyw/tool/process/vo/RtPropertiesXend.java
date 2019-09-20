/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 结束节点属性.
 * @author chenhao
 *
 */
public class RtPropertiesXend implements IPropertiesX{
    private static Logger logger = LoggerFactory.getLogger(RtPropertiesXend.class);
    public static final String FLOW_END_TERMINATE_TYPE = "http://b3mn.org/stencilset/bpmn2.0#EndNoneEvent";


    private String overrideid;
    private String name;
    private String documentation;
    private String executionlisteners;
    private String type;
    private Boolean terminateall;//事件结束节点特有属性

    public String getOverrideid() {
        return overrideid;
    }
    public void setOverrideid(String overrideid) {
        this.overrideid = overrideid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDocumentation() {
        return documentation;
    }
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }
    public String getExecutionlisteners() {
        return executionlisteners;
    }
    public void setExecutionlisteners(String executionlisteners) {
        this.executionlisteners = executionlisteners;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Boolean getTerminateall() {
        return terminateall;
    }
    public void setTerminateall(Boolean terminateall) {
        this.terminateall = terminateall;
    }
    public static IPropertiesX init(IPropertiesX irtpx, ActYwGnode gnode, RtChildShapes rtcs) {
        RtPropertiesXend rtpx = null;
        if(irtpx == null){
            rtpx = new RtPropertiesXend();
        }else{
            rtpx = (RtPropertiesXend) irtpx;
        }

        if (gnode == null) {
            return rtpx;
        }

        if (StringUtil.isNotEmpty(gnode.getName())) {
            rtpx.setName(gnode.getName());
        } else {
            rtpx.setName(ActYwTool.FLOW_PROP_NULL);
            logger.warn("gnode.name is undefind");
        }

        if (StringUtil.isNotEmpty(gnode.getRemarks())) {
            rtpx.setDocumentation(gnode.getRemarks());
        } else {
            rtpx.setDocumentation(ActYwTool.FLOW_PROP_NULL);
        }

        /**
         * Overrideid与rtcs.resourceId保持一致.
         */
        if(rtcs != null){
            rtpx.setOverrideid(rtcs.getResourceId());
        }else{
            rtpx.setOverrideid(ActYwTool.FLOW_PROP_NULL);
            logger.warn("gnode.overrideid is undefind");
        }

        if(StringUtil.isEmpty(gnode.getNode().getNodeKey())){
            logger.warn("初始化属性 RtPropertiesX 时,[gnode.node.nodeKey]不能为空！");
            return rtpx;
        }

        StenType stenType = StenType.getByKey(gnode.getNode().getNodeKey());
        if(stenType == null){
            logger.warn("初始化属性 RtPropertiesX 时,[gnode]对应的 stenType 不能为空！");
            return rtpx;
        }

        if((StenType.ST_END_EVENT_TERMINATE).equals(stenType)){
            rtpx.setTerminateall(true);
            rtpx.setType(FLOW_END_TERMINATE_TYPE);
        }else{
            rtpx.setTerminateall(false);
            rtpx.setType(ActYwTool.FLOW_PROP_NULL);
        }
        rtpx.setExecutionlisteners(ActYwTool.FLOW_PROP_NULL);
        return rtpx;
    }
}
