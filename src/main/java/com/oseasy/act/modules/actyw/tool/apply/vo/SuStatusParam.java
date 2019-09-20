package com.oseasy.act.modules.actyw.tool.apply.vo;

import java.util.List;

import com.oseasy.act.modules.actyw.tool.apply.IRstatus;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;

/**
 * 审核需要的参数.
 * @author chenhao
 */
public class SuStatusParam implements IRstatus{
  /**
   * 监听者标识.
   */
  private String key;
  /**
   * 流程标识.
   */
  private FlowType flowType;
  private List<SuStatusGrade> status;

  public SuStatusParam() {
    super();
  }

  public SuStatusParam(String key, FlowType flowType) {
    super();
    this.key = key;
    this.flowType = flowType;
  }

  public SuStatusParam(String key, FlowType flowType, List<SuStatusGrade> status) {
    super();
    this.flowType = flowType;
    this.key = key;
    this.status = status;
  }
  public FlowType getFlowType() {
    return flowType;
  }
  public void setFlowType(FlowType flowType) {
    this.flowType = flowType;
  }
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
  public List<SuStatusGrade> getStatus() {
    return status;
  }
  public void setStatus(List<SuStatusGrade> status) {
    this.status = status;
  }

  /**
   * 获取当前流程所有状态.
   * @param suStatuss 状态列表
   * @param flowType 流程类型
   * @param key 业务标识（监听标识）
   * @return List
   */
  public static List<SuStatusGrade> getGradesByFlow(List<SuStatusParam> suStatuss, FlowType flowType, String key) {
    for (SuStatusParam suStatusParam : suStatuss) {
      if ((suStatusParam.getFlowType()).equals(flowType) && (suStatusParam.getKey()).equals(key)) {
        return suStatusParam.getStatus();
      }
    }
    return null;
  }

  /**
   * 获取当前流程某一结点状态.
   * @param suStatuss 状态列表
   * @param flowType 流程类型
   * @param key 业务标识（监听标识）
   * @param gnodeId 流程节点
   * @return SuStatusGrade
   */
  public static SuStatusGrade getGradesByFlowAndGnode(List<SuStatusParam> suStatuss, FlowType flowType, String key, String gnodeId) {
    for (SuStatusParam suStatusParam : suStatuss) {
      if ((suStatusParam.getFlowType()).equals(flowType) && (suStatusParam.getKey()).equals(key)) {
        return SuStatusGrade.getGradeByGnode(suStatusParam.getStatus(), gnodeId);
      }
    }
    return null;
  }
}
