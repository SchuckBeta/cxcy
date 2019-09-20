/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_StenType_]]文件
 * @date 2017年6月2日 下午6:08:00
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.process.ActYwSten;
import com.oseasy.util.common.utils.StringUtil;

/**
 * stencil类型枚举.
 *
 * @author chenhao
 * @date 2017年6月2日 下午6:08:00
 *
 */
public enum StenTypeBak {
  ST_BPMDI("990111", StenEtype.SE_NODE, StenEsubType.SES_CORE, 1, "BPMNDiagram", "BPMN流程图"),

  ST_START_EVENT_NONE("990211", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartNoneEvent", "开始无事件节点"),
  ST_START_EVENT_TIMER("990221", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartTimerEvent", "定时开始事件"),
  ST_START_EVENT_SIGNAL("990231", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartSignalEvent", "信号开始事件"),
  ST_START_EVENT_MESSAGE("990241", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartMessageEvent", "消息开始事件"),
  ST_START_EVENT_ERROR("990251", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartErrorEvent", "错误开始事件"),

  ST_TASK_USER("990311", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "UserTask", "用户任务节点"),
  ST_TASK_SERVICE("990321", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ServiceTask", "服务任务节点"),
  ST_TASK_SCRIPT("990331", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ScriptTask", "脚本任务节点"),
  ST_TASK_BUSINESS_RULE("990341", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "BusinessRule", "业务规则任务节点"),
  ST_TASK_RECEIVE("990351", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ReceiveTask", "接收任务节点"),
  ST_TASK_MANUAL("990361", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ManualTask", "人工任务节点"),
  ST_TASK_MAIL("990371", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "MailTask", "邮件任务节点"),
  ST_TASK_CAMEL("990381", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "CamelTask", "骆驼任务节点"),
  ST_TASK_MULE("990391", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "MuleTask", "Mule任务节点"),
  ST_TASK_SEND("990392", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "SendTask", "发送任务节点"),

  ST_JG_SUB_PROCESS("990411", StenEtype.SE_NODE, StenEsubType.SES_JG, 2, "SubProcess", "子流程结构"),
  ST_JG_SUB_PROCESS_EVENT("990421", StenEtype.SE_NODE, StenEsubType.SES_JG, 2, "EventSubProcess", "事件子流程结构"),
  ST_JG_CALL_ACTIVITY("990431", StenEtype.SE_NODE, StenEsubType.SES_JG, 2, "CallActivity", "调用活动结构"),

  ST_GATEWAY_EXCLUSIVE("990511", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "ExclusiveGateway", "互斥网关"),
  ST_GATEWAY_PARALLEL("990521", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "ParallelGateway", "并行网关"),
  ST_GATEWAY_INCLUSIVE("990531", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "InclusiveGateway", "包容性网关"),
  ST_GATEWAY_EVENT("990541", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "EventGateway", "事件网关"),

  ST_BOUNDARY_EVENT_ERROR("990611", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryErrorEvent", "边界错误事件"),
  ST_BOUNDARY_EVENT_TIMER("990621", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryTimerEvent", "边界定时事件"),
  ST_BOUNDARY_EVENT_SIGNAL("990631", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundarySignalEvent", "边界信号事件"),
  ST_BOUNDARY_EVENT_MESSAGE("990641", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryMessageEvent", "边界消息事件"),
  ST_BOUNDARY_EVENT_CANCEL("990651", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryCancelEvent", "边界消息事件"),
  ST_BOUNDARY_EVENT_COMPENSATION("990661", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryCompensationEvent", "边界补偿事件"),

  ST_CATCH_EVENT_TIMER("990711", StenEtype.SE_NODE, StenEsubType.SES_CATCH_EVENT, 2, "CatchTimerEvent", "中间定时器捕获事件"),
  ST_CATCH_EVENT_SIGNAL("990721", StenEtype.SE_NODE, StenEsubType.SES_CATCH_EVENT, 2, "CatchSignalEvent", "中间信号捕获事件"),
  ST_CATCH_EVENT_MESSAGE("990731", StenEtype.SE_NODE, StenEsubType.SES_CATCH_EVENT, 2, "CatchMessageEvent", "中间消息事件"),

  ST_CTROW_EVENT_THROW_NONE("990841", StenEtype.SE_NODE, StenEsubType.SES_CTROW_EVENT, 2, "ThrowNoneEvent", "中间无抛出事件"),
  ST_CTROW_EVENT_THROW_SIGNAL("990851", StenEtype.SE_NODE, StenEsubType.SES_CTROW_EVENT, 2, "ThrowSignalEvent", "中间信号抛出事件"),

  ST_END_EVENT_NONE("990911", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndNoneEvent", "结束无事件"),
  ST_END_EVENT_ERROR("990921", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndErrorEvent", "结束错误事件"),
  ST_END_EVENT_CANCEL("990931", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndCancelEvent", "结束取消事件"),
  ST_END_EVENT_TERMINATE("990941", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndTerminateEvent", "结束终止事件"),

  ST_YD_POOL("991011", StenEtype.SE_NODE, StenEsubType.SES_YD, 2, "Pool", "池"),
  ST_YD_LANE("991021", StenEtype.SE_NODE, StenEsubType.SES_YD, 2, "Lane", "道"),

  ST_ZJ_TEXT_ANNOTATION("991111", StenEtype.SE_NODE, StenEsubType.SES_ZJ, 2, "TextAnnotation", "文本注释"),
  ST_ZJ_DATA_STORE("991121", StenEtype.SE_NODE, StenEsubType.SES_ZJ, 2, "DataStore", "数据存储"),

  ST_FLOW_SEQUENCE("991211", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "SequenceFlow", "流程序列"),
  ST_FLOW_MESSAGE("991221", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "MessageFlow", "消息流程序列"),
  ST_FLOW_ASSOCIATION("991231", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "Association", "联系序列"),
  ST_FLOW_ASSOCIATION_DATA("991241", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "DataAssociation", "数据联系序列");

  private String id;
  private StenEtype type;
  private StenEsubType subtype;
  private Integer level;
  private String key;
  private String remark;

  private StenTypeBak(String id, StenEtype type, StenEsubType subtype, Integer level, String key,
      String remark) {
    this.id = id;
    this.type = type;
    this.subtype = subtype;
    this.level = level;
    this.key = key;
    this.remark = remark;
  }

  /**
   * 根据ID获取枚举 .
   *
   * @author chenhao
   * @param id
   *          枚举标识
   * @return StenType
   */
  public static StenTypeBak getById(String id) {
    StenTypeBak[] entitys = StenTypeBak.values();
    for (StenTypeBak entity : entitys) {
      if ((id != null) && (id).equals(entity.getId())) {
        return entity;
      }
    }
    return null;
  }

  /**
   * 根据类型获取枚举 .
   *
   * @author chenhao
   * @param type
   *          类型
   * @param subtype
   *          子类型
   * @return StenType
   */
  public static StenTypeBak getByType(StenEtype type, StenEsubType subtype) {
    StenTypeBak[] entitys = StenTypeBak.values();
    for (StenTypeBak entity : entitys) {
      if (type.equals(entity.getType()) && subtype.equals(entity.getSubtype())) {
        return entity;
      }
    }
    return null;
  }

  /**
   * 根据类型获取枚举 .
   *
   * @author chenhao
   * @param subtype
   *          子类型
   * @return List
   */
  public static List<StenTypeBak> getBySubType(StenEsubType subtype) {
    List<StenTypeBak> stenTypes = Lists.newArrayList();
    StenTypeBak[] entitys = StenTypeBak.values();
    for (StenTypeBak entity : entitys) {
      if (subtype.equals(entity.getSubtype())) {
        stenTypes.add(entity);
      }
    }
    return stenTypes;
  }

  public static List<StenTypeBak> getBySubType(String subtype) {
    return getBySubType(StenEsubType.getByType(subtype));
  }

  /**
   * 根据等级获取枚举 .
   *
   * @author chenhao
   * @param level
   *          等级
   * @return StenType
   */
  public static StenTypeBak getByLevel(Integer level) {
    StenTypeBak[] entitys = StenTypeBak.values();
    for (StenTypeBak entity : entitys) {
      if ((level).equals(entity.getLevel())) {
        return entity;
      }
    }
    return null;
  }

  /**
   * 根据关键字获取枚举 .
   *
   * @author chenhao
   * @param key
   *          关键字
   * @return StenType
   */
  public static StenTypeBak getByKey(String key) {
    StenTypeBak[] entitys = StenTypeBak.values();
    for (StenTypeBak entity : entitys) {
      if ((StringUtil.isNotEmpty(key)) && (key).equals(entity.getKey())) {
        return entity;
      }
    }
    return null;
  }

  public String getId() {
    return id;
  }

  public StenEtype getType() {
    return type;
  }

  public Integer getLevel() {
    return level;
  }

  public String getKey() {
    return key;
  }

  public String getRemark() {
    return remark;
  }

  public StenEsubType getSubtype() {
    return subtype;
  }

  /**
   * 根据Key生成节点代码 .
   * @author chenhao
   * @param key 唯一标识
   * @return String
   */
  public static String genNodeByType(String key) {
    return genNodeByStenType(StenTypeBak.getByKey(key));
  }

  /**
   * 根据StenType生成节点代码 .
   * @author chenhao
   * @param entity StenType
   * @return String
   */
  public static String genNodeByStenType(StenTypeBak entity) {
    StringBuffer buffer = new StringBuffer();
    ActYwSten actYwResult = ActYwSten.genActYwSten();
    for (SnStencils snStencils : actYwResult.getStencils()) {
      if ((snStencils.getId()).equals(entity.getKey())) {
        buffer.append(snStencils.getLayout());
      }
    }
    return buffer.toString();
  }
}
