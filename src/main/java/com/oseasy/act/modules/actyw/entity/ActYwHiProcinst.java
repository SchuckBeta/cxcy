package com.oseasy.act.modules.actyw.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 流程历史实例Entity.
 *
 * @author chenhao
 * @version 2017-06-08
 */
public class ActYwHiProcinst extends DataEntity<ActYwHiProcinst> {

  private static final long serialVersionUID = 1L;
  private String procInstId; // proc_inst_id_
  private String businessKey; // business_key_
  private String procDefId; // proc_def_id_
  private Date startTime; // start_time_
  private Date endTime; // end_time_
  private Long duration; // duration_
  private String startUserId; // start_user_id_
  private String startActId; // start_act_id_
  private String endActId; // end_act_id_
  private String superProcessInstanceId; // super_process_instance_id_
  private String deleteReason; // delete_reason_
  private String tenantId; // tenant_id_

  public ActYwHiProcinst() {
    super();
  }

  public ActYwHiProcinst(String id) {
    super(id);
  }

  @Length(min = 1, max = 64, message = "proc_inst_id_长度必须介于 1 和 64 之间")
  public String getProcInstId() {
    return procInstId;
  }

  public void setProcInstId(String procInstId) {
    this.procInstId = procInstId;
  }

  @Length(min = 0, max = 255, message = "business_key_长度必须介于 0 和 255 之间")
  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  @Length(min = 1, max = 64, message = "proc_def_id_长度必须介于 1 和 64 之间")
  public String getProcDefId() {
    return procDefId;
  }

  public void setProcDefId(String procDefId) {
    this.procDefId = procDefId;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "start_time_不能为空")
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  @Length(min = 0, max = 255, message = "start_user_id_长度必须介于 0 和 255 之间")
  public String getStartUserId() {
    return startUserId;
  }

  public void setStartUserId(String startUserId) {
    this.startUserId = startUserId;
  }

  @Length(min = 0, max = 255, message = "start_act_id_长度必须介于 0 和 255 之间")
  public String getStartActId() {
    return startActId;
  }

  public void setStartActId(String startActId) {
    this.startActId = startActId;
  }

  @Length(min = 0, max = 255, message = "end_act_id_长度必须介于 0 和 255 之间")
  public String getEndActId() {
    return endActId;
  }

  public void setEndActId(String endActId) {
    this.endActId = endActId;
  }

  @Length(min = 0, max = 64, message = "super_process_instance_id_长度必须介于 0 和 64 之间")
  public String getSuperProcessInstanceId() {
    return superProcessInstanceId;
  }

  public void setSuperProcessInstanceId(String superProcessInstanceId) {
    this.superProcessInstanceId = superProcessInstanceId;
  }

  @Length(min = 0, max = 4000, message = "delete_reason_长度必须介于 0 和 4000 之间")
  public String getDeleteReason() {
    return deleteReason;
  }

  public void setDeleteReason(String deleteReason) {
    this.deleteReason = deleteReason;
  }

  @Length(min = 0, max = 255, message = "tenant_id_长度必须介于 0 和 255 之间")
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }
}