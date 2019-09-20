/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtPropertiesX_]]文件
 * @date 2017年6月2日 下午2:19:27
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程节点基本属性.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:19:27
 */
public class RtPropertiesX implements IPropertiesX{
    private static Logger logger = LoggerFactory.getLogger(RtPropertiesX.class);
    private String overrideid;
    private String name;
    private String documentation;
    private String executionlisteners;
    private String initiator;
    private String formkeydefinition;
    private RtPxFormproperties formproperties;

    private String asynchronousdefinition;// fasle
    private String exclusivedefinition;// true
    private String multiinstance_type;// Parallel

    private String multiinstance_cardinality;// ""
    private String multiinstance_collection;// ${managers}
    private String multiinstance_variable;//
    private String multiinstance_condition;// ""
    private String isforcompensation;// false
    private RtPxUsertaskassignment usertaskassignment;// ""
    private String duedatedefinition;// ""
    private String prioritydefinition;// ""
    private RtPxTasklisteners tasklisteners;// ""
    private String conditionsequenceflow;//条件
    private String defaultflow;
    private String showdiamondmarker;
    private Boolean terminateAll;//事件结束节点特有属性
    private String sequencefloworder; //网关特有属性.

    public RtPropertiesX() {
        super();
    }

    /**
     * 根据 overrideid和名称生成RtPropertiesX对象.
     *
     * @author chenhao
     * @param overrideid
     *            标识ID
     * @param name
     *            名称
     */
    public RtPropertiesX(String overrideid, String name) {
        super();
        this.overrideid = overrideid;
        this.name = name;
    }

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

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getFormkeydefinition() {
        return formkeydefinition;
    }

    public void setFormkeydefinition(String formkeydefinition) {
        this.formkeydefinition = formkeydefinition;
    }

    public RtPxFormproperties getFormproperties() {
        return formproperties;
    }

    public void setFormproperties(RtPxFormproperties formproperties) {
        this.formproperties = formproperties;
    }

    public Boolean getTerminateAll() {
        return terminateAll;
    }

    public void setTerminateAll(Boolean terminateAll) {
        this.terminateAll = terminateAll;
    }

    public String getAsynchronousdefinition() {
//        if (StringUtil.isEmpty(this.asynchronousdefinition)) {
//            this.asynchronousdefinition = ActYwTool.FLOW_PROP_FALSE;
//        }
        return asynchronousdefinition;
    }

    public void setAsynchronousdefinition(String asynchronousdefinition) {
        this.asynchronousdefinition = asynchronousdefinition;
    }

    public String getExclusivedefinition() {
//        if (StringUtil.isEmpty(this.exclusivedefinition)) {
//            this.exclusivedefinition = ActYwTool.FLOW_PROP_TRUE;
//        }
        return exclusivedefinition;
    }

    public void setExclusivedefinition(String exclusivedefinition) {
        this.exclusivedefinition = exclusivedefinition;
    }

    public String getMultiinstance_type() {
//        if (StringUtil.isEmpty(this.multiinstance_type)) {
//            this.multiinstance_type = GnodeTaskType.GTT_PARALLEL.getKey();
//        }
        return multiinstance_type;
    }

    public void setMultiinstance_type(String multiinstance_type) {
        this.multiinstance_type = multiinstance_type;
    }

    public String getMultiinstance_cardinality() {
        return multiinstance_cardinality;
    }

    public void setMultiinstance_cardinality(String multiinstance_cardinality) {
        this.multiinstance_cardinality = multiinstance_cardinality;
    }

    public String getMultiinstance_collection() {
        return multiinstance_collection;
    }

    public void setMultiinstance_collection(String multiinstance_collection) {
        this.multiinstance_collection = multiinstance_collection;
    }

    public String getMultiinstance_variable() {
        return multiinstance_variable;
    }

    public void setMultiinstance_variable(String multiinstance_variable) {
        this.multiinstance_variable = multiinstance_variable;
    }

    public String getMultiinstance_condition() {
        return multiinstance_condition;
    }

    public void setMultiinstance_condition(String multiinstance_condition) {
        this.multiinstance_condition = multiinstance_condition;
    }

    public String getShowdiamondmarker() {
        return showdiamondmarker;
    }

    public void setShowdiamondmarker(String showdiamondmarker) {
        this.showdiamondmarker = showdiamondmarker;
    }

    public String getIsforcompensation() {
//        if (StringUtil.isEmpty(this.isforcompensation)) {
//            this.isforcompensation = ActYwTool.FLOW_PROP_FALSE;
//        }
        return isforcompensation;
    }

    public void setIsforcompensation(String isforcompensation) {
        this.isforcompensation = isforcompensation;
    }

    public RtPxUsertaskassignment getUsertaskassignment() {
        return usertaskassignment;
    }

    public void setUsertaskassignment(RtPxUsertaskassignment usertaskassignment) {
        this.usertaskassignment = usertaskassignment;
    }

    public String getDuedatedefinition() {
        return duedatedefinition;
    }

    public void setDuedatedefinition(String duedatedefinition) {
        this.duedatedefinition = duedatedefinition;
    }

    public String getPrioritydefinition() {
        return prioritydefinition;
    }

    public void setPrioritydefinition(String prioritydefinition) {
        this.prioritydefinition = prioritydefinition;
    }

    public RtPxTasklisteners getTasklisteners() {
        return tasklisteners;
    }

    public void setTasklisteners(RtPxTasklisteners tasklisteners) {
        this.tasklisteners = tasklisteners;
    }

    public String getConditionsequenceflow() {
        return conditionsequenceflow;
    }

    public void setConditionsequenceflow(String conditionsequenceflow) {
        this.conditionsequenceflow = conditionsequenceflow;
    }

    public String getSequencefloworder() {
        return sequencefloworder;
    }

    public void setSequencefloworder(String sequencefloworder) {
        this.sequencefloworder = sequencefloworder;
    }

    public String getDefaultflow() {
        return defaultflow;
    }

    public void setDefaultflow(String defaultflow) {
        this.defaultflow = defaultflow;
    }

    /**
     * 初始化节点属性 .
     * @author chenhao
     * @param gnode  节点
     * @return RtPropertiesX
     */
    public static RtPropertiesX init(IPropertiesX irtpx, ActYwGnode gnode, RtChildShapes rtcs) {
        RtPropertiesX rtpx = null;
        if(irtpx == null){
            rtpx = new RtPropertiesX();
        }else{
            rtpx = (RtPropertiesX) irtpx;
        }

        if (gnode != null) {
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
                if (StringUtil.isNotEmpty(gnode.getId())) {
                    if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType())){
                        rtpx.setOverrideid(ActYwTool.FLOW_ID_START + gnode.getId());
                    }else{
                        rtpx.setOverrideid(ActYwTool.FLOW_ID_PREFIX + gnode.getId());
                    }
                } else {
                    rtpx.setOverrideid(ActYwTool.FLOW_PROP_NULL);
                    logger.warn("gnode.overrideid is undefind");
                }
            }

            /**
             * 处理 sequencefloworder 属性.
             */
            if((GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType())){
                rtpx.setSequencefloworder(ActYwTool.FLOW_PROP_NULL);
            }

            /**
             * 处理defaultflow属性.
             */
            if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_ROOT_END.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_START.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType())){
                rtpx.setDefaultflow(ActYwTool.FLOW_PROP_NULL);
            }else if((GnodeType.GT_ROOT_FLOW.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_FLOW.getId()).equals(gnode.getType())){
                rtpx.setDefaultflow(ActYwTool.FLOW_PROP_FALSE);
            }

            /**
             * 处理节点的任务类型:None或并行.
             */
            if((GnodeType.GT_ROOT_START.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_ROOT_END.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_ROOT_FLOW.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_START.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType()) ||
                    (GnodeType.GT_PROCESS_FLOW.getId()).equals(gnode.getType())){
                rtpx.setMultiinstance_type(GnodeTaskType.GTT_PARALLEL.getKey());
            }else if((GnodeType.GT_PROCESS.getId()).equals(gnode.getType())){
                rtpx.setMultiinstance_type(GnodeTaskType.GTT_NONE.getKey());
            }else if((GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType())){
                if (StringUtil.isNotEmpty(gnode.getTaskType())) {
                    rtpx.setMultiinstance_type(gnode.getTaskType());
                } else {
                    logger.warn("初始化属性 RtPropertiesX 时,任务节点[gnode.taskType]不能为空");
                }
            }

            /**
             * 处理用户及角色属性(需要提前设置 multiinstance_type 属性值).
             * 条件：任务节点需要设置并行还是默认。
             *  1、当 multiinstance_type = NONE 时，只需要设置setMultiinstance_collection属性
             *  2、当 multiinstance_type = PARALLEL 时，需要设置setMultiinstance_collection、Multiinstance_variable、Usertaskassignment属性
             *  3、当 multiinstance_type = SEQUENTIAL 时，需要设置???属性
             */
            if (((GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType()))) {
                if (StringUtil.checkNotEmpty(gnode.getRoles()) && ((GnodeType.GT_ROOT_TASK.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_TASK.getId()).equals(gnode.getType()))) {
                    String roleIds = StringUtil.listIdToStr(gnode.getRoles(), StringUtil.LINE_D);
                    //String rids = StringUtil.JSP_VAL_PREFIX + ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds + ActYwTool.FLOW_ROLE_POSTFIX;
                    if((rtpx.getMultiinstance_type()).equals(GnodeTaskType.GTT_NONE.getKey())){
                        RtPxAssignment<String> rtPxAssignment = new RtPxAssignment<String>();
                        rtPxAssignment.setAssignee(ActYwTool.FLOW_PROP_NULL);
                        //原设置方式
                        //rtPxAssignment.setCandidateUsers(RtPxCusers.convert(gnode.getRoles()));
                        //rtPxAssignment.setCandidateGroups(RtPxCroles.convert(gnode.getRoles()));
                        //签收用户设置传参值
                        List<RtPxCusers> cusers=new ArrayList<RtPxCusers>();
                        cusers.add(new RtPxCusers(StringUtil.JSP_VAL_PREFIX + ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds + StringUtil.JSP_VAL_POSTFIX));
                        rtPxAssignment.setCandidateUsers(cusers);

                        rtpx.setUsertaskassignment(new RtPxUsertaskassignment(rtPxAssignment));
                        rtpx.setMultiinstance_variable(ActYwTool.FLOW_PROP_NULL);
                        rtpx.setMultiinstance_collection(ActYwTool.FLOW_PROP_NULL);
                    }else if((rtpx.getMultiinstance_type()).equals(GnodeTaskType.GTT_PARALLEL.getKey())){
                        RtPxAssignment<String> rtPxAssignment = new RtPxAssignment<String>();
                        rtPxAssignment.setAssignee(StringUtil.JSP_VAL_PREFIX + ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds + ActYwTool.FLOW_ROLE_POSTFIX);

                        rtpx.setUsertaskassignment(new RtPxUsertaskassignment(rtPxAssignment));
                        rtpx.setMultiinstance_variable(ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds);
                        /**
                         * 当存在多个角色ID时，该处会导致发布失败(_下划线分割).
                         */
                        rtpx.setMultiinstance_collection(StringUtil.JSP_VAL_PREFIX + ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds + ActYwTool.FLOW_ROLE_POSTFIX_S + ActYwTool.FLOW_ROLE_POSTFIX);
                    }else if((rtpx.getMultiinstance_type()).equals(GnodeTaskType.GTT_SEQUENTIAL.getKey())){
                        RtPxAssignment<String> rtPxAssignment = new RtPxAssignment<String>();
                        rtPxAssignment.setAssignee(StringUtil.JSP_VAL_PREFIX + ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds + ActYwTool.FLOW_ROLE_POSTFIX);

                        //TODO 暂时不知道业务逻辑，暂时给默认值，后续实现
                        rtpx.setUsertaskassignment(new RtPxUsertaskassignment());
                        rtpx.setMultiinstance_variable(ActYwTool.FLOW_PROP_NULL);
                        rtpx.setMultiinstance_collection(ActYwTool.FLOW_PROP_NULL);
                    }else{
                        logger.warn("初始化属性 RtPropertiesX 时,任务节点[gnode.taskType]类型未定义");
                    }
                }
            } else {
                rtpx.setUsertaskassignment(new RtPxUsertaskassignment());
                rtpx.setMultiinstance_variable(ActYwTool.FLOW_PROP_NULL);
                rtpx.setMultiinstance_collection(ActYwTool.FLOW_PROP_NULL);
            }


            /**
             * 处理表单及表单属性.
             */
            if (StringUtil.checkNotEmpty(gnode.getGforms())) {
                boolean isFirst = true;
                StringBuffer sb = new StringBuffer();
                List<RtPxFormPropertie> formProperties = Lists.newArrayList();
                for (ActYwGform gforms : gnode.getGforms()) {
                    RtPxFormPropertie rtPxFormPropertie = new RtPxFormPropertie();
                    if (isFirst) {
                        sb.append(gforms.getId());
                        isFirst = false;
                    } else {
                        sb.append(StringUtil.DOTH);
                        sb.append(gforms.getId());
                    }

                    if (gforms.getForm() != null) {
                        rtPxFormPropertie.setId(gforms.getForm().getId());
                        rtPxFormPropertie.setReadable(ActYwTool.FLOW_PROP_TRUE);
                        //rtPxFormPropertie.setType(gforms.getForm().getType());
                        rtPxFormPropertie.setWritable(ActYwTool.FLOW_PROP_TRUE);
                        formProperties.add(rtPxFormPropertie);
                    }
                }
                rtpx.setFormkeydefinition(sb.toString());
//                rtpx.setFormproperties(new RtPxFormproperties(formProperties));
                rtpx.setFormproperties(new RtPxFormproperties());
            } else {
                rtpx.setFormkeydefinition(ActYwTool.FLOW_PROP_NULL);
                rtpx.setFormproperties(new RtPxFormproperties());
            }

            /**
             * 处理节点状态属性，网关后的线节点，排除网关节点.
             */
            if (StringUtil.checkNotEmpty(gnode.getGstatuss())) {
                if(StringUtil.checkNotEmpty(gnode.getGstatuss()) && (((GnodeType.GT_ROOT_FLOW.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_FLOW.getId()).equals(gnode.getType())))){
                    boolean isFirst = true;
                    StringBuffer sb = new StringBuffer();
                    for (ActYwGstatus gstatus : gnode.getGstatuss()) {
                        if (gstatus.getStatus() != null) {
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                sb.append(ActYwTool.FLOW_PROP_GATEWAY_REG_OR);
                            }
                            sb.append(ActYwTool.FLOW_PROP_GATEWAY_STATE);
                            sb.append(ActYwTool.FLOW_PROP_GATEWAY_REG_EQ);
                            sb.append(gstatus.getStatus().getStatus());
                        }
                    }
                    rtpx.setConditionsequenceflow(StringUtil.JSP_VAL_PREFIX + sb.toString() + ActYwTool.FLOW_ROLE_POSTFIX);
                }else{
                    rtpx.setConditionsequenceflow(ActYwTool.FLOW_PROP_NULL);
                }
            } else {
                rtpx.setConditionsequenceflow(ActYwTool.FLOW_PROP_NULL);
            }

            /**
             * 处理terminateAll属性，结束终止事件节点terminateAll=true,否则为null.
             */
            if((GnodeType.GT_PROCESS_END.getId()).equals(gnode.getType())){
                if(StringUtil.isEmpty(gnode.getNode().getNodeKey())){
                    logger.warn("初始化属性 RtPropertiesX 时,[gnode.node.nodeKey]不能为空！");
                }
                StenType stenType = StenType.getByKey(gnode.getNode().getNodeKey());
                if(stenType == null){
                    logger.warn("初始化属性 RtPropertiesX 时,[gnode]对应的 stenType 不能为空！");
                }else{
                    if((StenType.ST_END_EVENT_TERMINATE).equals(stenType)){
                        rtpx.setTerminateAll(true);
                    }
                }

                return rtpx;
            }else{
                rtpx.setTerminateAll(null);
            }
        }else{
            logger.warn("初始化属性 RtPropertiesX 时,[gnode]不能为空！");
            rtpx.setOverrideid(ActYwTool.FLOW_PROP_NULL);
            rtpx.setName(ActYwTool.FLOW_PROP_NULL);
            rtpx.setDocumentation(ActYwTool.FLOW_PROP_NULL);
            if(((GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType()))){
                rtpx.setSequencefloworder(ActYwTool.FLOW_PROP_NULL);
            }else{
                rtpx.setFormkeydefinition(ActYwTool.FLOW_PROP_NULL);
                rtpx.setFormproperties(new RtPxFormproperties());
                rtpx.setUsertaskassignment(new RtPxUsertaskassignment());
                rtpx.setMultiinstance_variable(ActYwTool.FLOW_PROP_NULL);
                rtpx.setMultiinstance_collection(ActYwTool.FLOW_PROP_NULL);
                rtpx.setConditionsequenceflow(ActYwTool.FLOW_PROP_NULL);
                rtpx.setMultiinstance_type(GnodeTaskType.GTT_PARALLEL.getKey());
            }
        }

        /**
         * 没有做处理直接使用默认值.
         */

        if(((GnodeType.GT_ROOT_GATEWAY.getId()).equals(gnode.getType()) || (GnodeType.GT_PROCESS_GATEWAY.getId()).equals(gnode.getType()))){
            rtpx.setSequencefloworder(ActYwTool.FLOW_PROP_NULL);
        }else{
            rtpx.setTasklisteners(new RtPxTasklisteners());
            rtpx.setExecutionlisteners(ActYwTool.FLOW_PROP_NULL);
            rtpx.setMultiinstance_cardinality(ActYwTool.FLOW_PROP_NULL);
            rtpx.setMultiinstance_condition(ActYwTool.FLOW_PROP_NULL);
            rtpx.setAsynchronousdefinition(ActYwTool.FLOW_PROP_FALSE);
            rtpx.setExclusivedefinition(ActYwTool.FLOW_PROP_TRUE);
            rtpx.setDuedatedefinition(ActYwTool.FLOW_PROP_NULL);
            rtpx.setPrioritydefinition(ActYwTool.FLOW_PROP_NULL);
            rtpx.setDefaultflow(ActYwTool.FLOW_PROP_NULL);
            rtpx.setShowdiamondmarker(ActYwTool.FLOW_PROP_NULL);
            rtpx.setIsforcompensation(ActYwTool.FLOW_PROP_FALSE);
            rtpx.setInitiator(ActYwTool.FLOW_PROP_NULL);
        }
        return rtpx;
    }
}
