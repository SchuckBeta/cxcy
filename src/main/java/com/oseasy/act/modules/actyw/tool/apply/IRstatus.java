package com.oseasy.act.modules.actyw.tool.apply;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;

public interface IRstatus {
  /**
   * 获取监听器标识.
   * @return String
   */
  public String getKey();

  /**
   * 设置监听器标识.
   * @param key 监听器标识
   */
  public void setKey(String key);

  /**
   * 获取流程标识.
   * @return String
   */
  public FlowType getFlowType();

  /**
   * 设置流程标识.
   * @param flowType 流程标识
   */
  public void setFlowType(FlowType flowType);
}
