package com.oseasy.act.modules.actyw.tool.apply.vo;

import java.util.List;

import com.oseasy.act.modules.actyw.tool.apply.IRstatus;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;

/**
 * 节点审核结果返回状态.
 * @author chenhao
 */
public class SuRstatusParam implements IRstatus{
  /**
   * 监听者标识.
   */
  private String key;
  /**
   * 流程标识.
   */
  private FlowType flowType;
  private List<SuRstatus> status;

  public SuRstatusParam() {
    super();
  }

  public SuRstatusParam(String key, FlowType flowType, List<SuRstatus> status) {
    super();
    this.key = key;
    this.flowType = flowType;
    this.status = status;
  }

  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
  public FlowType getFlowType() {
    return flowType;
  }
  public void setFlowType(FlowType flowType) {
    this.flowType = flowType;
  }
  public List<SuRstatus> getStatus() {
    return status;
  }
  public void setStatus(List<SuRstatus> status) {
    this.status = status;
  }
}
