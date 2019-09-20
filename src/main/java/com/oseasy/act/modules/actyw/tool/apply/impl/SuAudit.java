package com.oseasy.act.modules.actyw.tool.apply.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.ISuObserverAudit;
import com.oseasy.act.modules.actyw.tool.apply.ISubject;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuRstatus;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuRstatusParam;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusAparam;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusParam;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;

/**
 * 审核主题-入驻.
 * @author chenhao
 *
 */
public class SuAudit implements ISubject<ISuObserverAudit, SuRstatusParam> {
  /**
   * 用来存放和记录观察者.
   */
  private List<ISuObserverAudit> suObservers = Lists.newArrayList();

  /**
   * 记录订阅执行状态.
   */
  private String key;
  private FlowType flowType;
  private SuStatusAparam aparam;
  private List<SuRstatusParam> suRstatus;

  @Override
  public void add(ISuObserverAudit sobj) {
    suObservers.add(sobj);
  }

  @Override
  public void delete(ISuObserverAudit sobj) {
    suObservers.remove(sobj);
  }

  @Override
  public List<SuRstatusParam> notifys() {
    this.suRstatus = Lists.newArrayList();
    for (ISuObserverAudit isuObserver : suObservers) {
//      SuRstatusParam suStatusParam = isuObserver.audit(aparam);
//      if (suStatusParam != null) {
//
//        return new SuRstatusParam(aparam.getKey(), this.flowType, ssParam.getStatus());
//        suRstatus.add(isuObserver.audit(aparam));
//      }
    }
    return suRstatus;
  }

  /**
   * 获取所有审核结果.
   * @param aparam 审核参数
   * @return List
   */
  public List<SuRstatusParam> dealAuditss(SuStatusAparam aparam) {
    this.aparam = aparam;
    /**
     * TOTD 公共审核业务.
     */
    return this.notifys();
  }

  /**
   * 获取所有审核结果.
   * @param aparam 审核参数
   * @param key 监听者标识.
   * @param flowType 流程标识.
   * @return List
   */
  public SuRstatusParam dealAudits(SuStatusAparam aparam, String key, FlowType flowType) {
    this.key = key;
    this.flowType = flowType;
    this.aparam = aparam;
    /**
     * TOTD 公共审核业务.
     */
    for (SuRstatusParam suRstatuParm : this.notifys()) {
      if ((suRstatuParm.getKey()).equals(key) && (suRstatuParm.getFlowType()).equals(flowType)) {
        return suRstatuParm;
      }
    }
    return null;
  }

  /**
   * 获取所有审核结果.
   * @param aparam 审核参数
   * @param key 监听者标识.
   * @param flowType 流程标识.
   * @return List
   */
  public SuRstatus dealAudit(SuStatusAparam aparam, String key, FlowType flowType) {
    this.key = key;
    this.flowType = flowType;
    this.aparam = aparam;
    /**
     * TOTD 公共审核业务.
     */
    for (SuRstatusParam suRstatuParm : this.notifys()) {
      if ((suRstatuParm.getKey()).equals(key) && (suRstatuParm.getFlowType()).equals(flowType)) {
        for (SuRstatus suRstatu : suRstatuParm.getStatus()) {
          if ((suRstatu.getGnodeId()).equals(aparam.getGnodeId())) {
            return suRstatu;
          }
        }
      }
    }
    return null;
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

  public SuStatusAparam getAparam() {
    return aparam;
  }

  public void setAparam(SuStatusAparam aparam) {
    this.aparam = aparam;
  }
}
