package com.oseasy.act.modules.actyw.tool.apply.impl;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.ISuObserverStatus;
import com.oseasy.act.modules.actyw.tool.apply.ISubject;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuRstatus;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuRstatusParam;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatus;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusGrade;
import com.oseasy.act.modules.actyw.tool.apply.vo.SuStatusParam;

/**
 * 审核主题-入驻.
 * @author chenhao
 *
 */
public class SuAstatus implements ISubject<ISuObserverStatus, SuStatusParam> {
  /**
   * 用来存放和记录观察者.
   */
  private List<ISuObserverStatus> suObservers = Lists.newArrayList();

  /**
   * 记录订阅执行状态.
   */
  private String key;
  /**
   * 审核节点.
   */
  private String gnodeId;

  @Override
  public void add(ISuObserverStatus sobj) {
    suObservers.add(sobj);
  }

  @Override
  public void delete(ISuObserverStatus sobj) {
    suObservers.remove(sobj);
  }

  @Override
  public List<SuStatusParam> notifys() {
    List<SuStatusParam> params = Lists.newArrayList();
    for (ISuObserverStatus isuObserver : suObservers) {
      params.add(new SuStatusParam(isuObserver.getId(), isuObserver.getFlowType(), isuObserver.getStatus()));
    }
    return params;
  }

  /**
   * 获取所有审核参数.
   * @return List
   */
  public List<SuStatusParam> dealAstatuss() {
    return this.notifys();
  }

  /**
   * 提供审核参数.
   * @param param
   * @param status
   */
  public SuStatusParam dealAstatus(String key) {
    this.key = key;
    /**
     * TOTD 公共审核业务.
     */
    for (SuStatusParam param : this.notifys()) {
      if ((param.getKey()).equals(key)) {
        return param;
      }
    }
    return null;
  }

  /**
   * 提供审核参数.
   * @param key 流程标识
   * @param gnodeId 审核节点
   * @param status
   */
  public SuStatusGrade dealAstatu(String key, String gnodeId) {
    this.key = key;
    this.gnodeId = gnodeId;
    /**
     * TOTD 公共审核业务.
     */
    for (SuStatusParam param : this.notifys()) {
      if ((param.getKey()).equals(key)) {
        for (SuStatusGrade statu : param.getStatus()) {
          if ((statu.getGnodeId()).equals(gnodeId)) {
            return statu;
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
}
