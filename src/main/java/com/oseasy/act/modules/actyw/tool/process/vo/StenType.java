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
public enum StenType {
  ST_BPMDI("0", StenEtype.SE_NODE, StenEsubType.SES_CORE, 1, "BPMNDiagram", "BPMN流程图"),

  ST_START_EVENT_NONE("991000", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartNoneEvent", "开始无事件节点"),
  ST_START_EVENT_TIMER("991100", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartTimerEvent", "定时开始事件"),
  ST_START_EVENT_SIGNAL("991200", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartSignalEvent", "信号开始事件"),
  ST_START_EVENT_MESSAGE("991300", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartMessageEvent", "消息开始事件"),
  ST_START_EVENT_ERROR("991400", StenEtype.SE_NODE, StenEsubType.SES_EVENT_START, 2, "StartErrorEvent", "错误开始事件"),

  ST_TASK_USER("993000", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "UserTask", "用户任务节点"),
  ST_TASK_SERVICE("993100", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ServiceTask", "服务任务节点"),
  ST_TASK_SCRIPT("993200", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ScriptTask", "脚本任务节点"),
  ST_TASK_BUSINESS_RULE("993300", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "BusinessRule", "业务规则任务节点"),
  ST_TASK_RECEIVE("993400", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ReceiveTask", "接收任务节点"),
  ST_TASK_MANUAL("993500", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "ManualTask", "人工任务节点"),
  ST_TASK_MAIL("993600", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "MailTask", "邮件任务节点"),
  ST_TASK_CAMEL("993700", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "CamelTask", "骆驼任务节点"),
  ST_TASK_MULE("993800", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "MuleTask", "Mule任务节点"),
  ST_TASK_SEND("993900", StenEtype.SE_NODE, StenEsubType.SES_TASK, 2, "SendTask", "发送任务节点"),

  ST_JG_SUB_PROCESS("994000", StenEtype.SE_NODE, StenEsubType.SES_JG, 2, "SubProcess", "子流程结构"),
  ST_JG_SUB_PROCESS_EVENT("994100", StenEtype.SE_NODE, StenEsubType.SES_JG, 2, "EventSubProcess", "事件子流程结构"),
  ST_JG_CALL_ACTIVITY("994200", StenEtype.SE_NODE, StenEsubType.SES_JG, 2, "CallActivity", "调用活动结构"),

  ST_GATEWAY_EXCLUSIVE("995100", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "ExclusiveGateway", "互斥网关"),
  ST_GATEWAY_PARALLEL("995200", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "ParallelGateway", "并行网关"),
  ST_GATEWAY_INCLUSIVE("995300", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "InclusiveGateway", "包容性网关"),
  ST_GATEWAY_EVENT("995400", StenEtype.SE_NODE, StenEsubType.SES_GATEWAY, 2, "EventGateway", "事件网关"),

  ST_BOUNDARY_EVENT_ERROR("999000", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryErrorEvent", "边界错误事件"),
  ST_BOUNDARY_EVENT_TIMER("999010", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryTimerEvent", "边界定时事件"),
  ST_BOUNDARY_EVENT_SIGNAL("999020", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundarySignalEvent", "边界信号事件"),
  ST_BOUNDARY_EVENT_MESSAGE("999030", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryMessageEvent", "边界消息事件"),
  ST_BOUNDARY_EVENT_CANCEL("999040", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryCancelEvent", "边界消息事件"),
  ST_BOUNDARY_EVENT_COMPENSATION("999050", StenEtype.SE_NODE, StenEsubType.SES_BOUNDARY_EVENT, 2, "BoundaryCompensationEvent", "边界补偿事件"),

  ST_CATCH_EVENT_TIMER("997000", StenEtype.SE_NODE, StenEsubType.SES_CATCH_EVENT, 2, "CatchTimerEvent", "中间定时器捕获事件"),
  ST_CATCH_EVENT_SIGNAL("997100", StenEtype.SE_NODE, StenEsubType.SES_CATCH_EVENT, 2, "CatchSignalEvent", "中间信号捕获事件"),
  ST_CATCH_EVENT_MESSAGE("997200", StenEtype.SE_NODE, StenEsubType.SES_CATCH_EVENT, 2, "CatchMessageEvent", "中间消息事件"),

  ST_CTROW_EVENT_THROW_NONE("998000", StenEtype.SE_NODE, StenEsubType.SES_CTROW_EVENT, 2, "ThrowNoneEvent", "中间无抛出事件"),
  ST_CTROW_EVENT_THROW_SIGNAL("998100", StenEtype.SE_NODE, StenEsubType.SES_CTROW_EVENT, 2, "ThrowSignalEvent", "中间信号抛出事件"),

  ST_END_EVENT_NONE("992000", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndNoneEvent", "结束无事件"),
  ST_END_EVENT_ERROR("992100", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndErrorEvent", "结束错误事件"),
  ST_END_EVENT_CANCEL("992200", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndCancelEvent", "结束取消事件"),
  ST_END_EVENT_TERMINATE("992300", StenEtype.SE_NODE, StenEsubType.SES_EVENT_END, 2, "EndTerminateEvent", "结束终止事件"),

  ST_YD_POOL("999800", StenEtype.SE_NODE, StenEsubType.SES_YD, 2, "Pool", "池"),
  ST_YD_LANE("999810", StenEtype.SE_NODE, StenEsubType.SES_YD, 2, "Lane", "道"),

  ST_ZJ_TEXT_ANNOTATION("999500", StenEtype.SE_NODE, StenEsubType.SES_ZJ, 2, "TextAnnotation", "文本注释"),
  ST_ZJ_DATA_STORE("999510", StenEtype.SE_NODE, StenEsubType.SES_ZJ, 2, "DataStore", "数据存储"),

  ST_FLOW_SEQUENCE("996100", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "SequenceFlow", "流程序列"),
  ST_FLOW_MESSAGE("996200", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "MessageFlow", "消息流程序列"),
  ST_FLOW_ASSOCIATION("996300", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "Association", "联系序列"),
  ST_FLOW_ASSOCIATION_DATA("996400", StenEtype.SE_EDGE, StenEsubType.SES_FLOW, 2, "DataAssociation", "数据联系序列");

  private String id;
  private StenEtype type;
  private StenEsubType subtype;
  private Integer level;
  private String key;
  private String remark;

  private StenType(String id, StenEtype type, StenEsubType subtype, Integer level, String key,
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
  public static StenType getById(String id) {
    StenType[] entitys = StenType.values();
    for (StenType entity : entitys) {
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
  public static StenType getByType(StenEtype type, StenEsubType subtype) {
    StenType[] entitys = StenType.values();
    for (StenType entity : entitys) {
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
  public static List<StenType> getBySubType(StenEsubType subtype) {
    List<StenType> stenTypes = Lists.newArrayList();
    StenType[] entitys = StenType.values();
    for (StenType entity : entitys) {
      if (subtype.equals(entity.getSubtype())) {
        stenTypes.add(entity);
      }
    }
    return stenTypes;
  }

  public static List<StenType> getBySubType(String subtype) {
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
  public static StenType getByLevel(Integer level) {
    StenType[] entitys = StenType.values();
    for (StenType entity : entitys) {
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
  public static StenType getByKey(String key) {
    StenType[] entitys = StenType.values();
    for (StenType entity : entitys) {
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
    return genNodeByStenType(StenType.getByKey(key));
  }

  /**
   * 根据StenType生成节点代码 .
   * @author chenhao
   * @param entity StenType
   * @return String
   */
  public static String genNodeByStenType(StenType entity) {
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
