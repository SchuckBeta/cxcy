/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.act.vo
 * @Description [[_ProcessMapVo_]]文件
 * @date 2017年6月9日 上午10:25:53
 *
 */

package com.oseasy.act.modules.act.vo;

import java.util.List;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

/**
 * 获取流程图结果Vo.
 *
 * @author chenhao
 * @date 2017年6月9日 上午10:25:53
 *
 */
public class ProcessMapVo {
  public static final String PM_PROC_DEF_ID = "procDefId";
  public static final String PM_PRO_INST_ID = "proInstId";
  public static final String PM_ACT_IMPLS = "actImpls";

  private String procDefId;
  private String proInstId;
  private List<ActivityImpl> actImpls;

  public ProcessMapVo() {
    super();
  }

  /**
   * 流程图Vo够构造器.
   * @author chenhao
   * @param procDefId 流程定义ID
   * @param proInstId 流程实例ID
   * @param actImpls 高亮节点
   */
  public ProcessMapVo(String procDefId, String proInstId, List<ActivityImpl> actImpls) {
    super();
    this.procDefId = procDefId;
    this.proInstId = proInstId;
    this.actImpls = actImpls;
  }

  public String getProcDefId() {
    return procDefId;
  }

  public void setProcDefId(String procDefId) {
    this.procDefId = procDefId;
  }

  public String getProInstId() {
    return proInstId;
  }

  public void setProInstId(String proInstId) {
    this.proInstId = proInstId;
  }

  public List<ActivityImpl> getActImpls() {
    return actImpls;
  }

  public void setActImpls(List<ActivityImpl> actImpls) {
    this.actImpls = actImpls;
  }
}
