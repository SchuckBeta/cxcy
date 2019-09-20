/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.act.vo
 * @Description [[_ActYwRstatus_]]文件
 * @date 2017年6月30日 上午11:07:07
 *
 */

package com.oseasy.act.modules.act.vo;

import java.io.Serializable;

/**
 * 获取流程处理状态信息.
 * @author chenhao
 * @date 2017年6月30日 上午11:07:07
 *
 */
public class ActRstatus implements Serializable{
  private Boolean status;
  private String msg;
  private String id;
  private String deploymentId;
  private String key;

  public ActRstatus() {
    super();
    this.status = true;
    this.msg = "执行成功";
  }

  public ActRstatus(Boolean status, String msg) {
    super();
    this.status = status;
    this.msg = msg;
  }

  public String getId() {
    return id;
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public void setDeploymentId(String deploymentId) {
    this.deploymentId = deploymentId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getStatus() {
    return status;
  }
  public void setStatus(Boolean status) {
    this.status = status;
  }
  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
}
