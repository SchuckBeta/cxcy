package com.oseasy.act.modules.actyw.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 流程运行任务Entity.
 *
 * @author chenhao
 * @version 2017-06-08
 */
public class ActYwRuTask extends DataEntity<ActYwRuTask> {

  private static final long serialVersionUID = 1L;
  private String rev; // rev_
  private String executionId; // execution_id_
  private String procInstId; // proc_inst_id_
  private String procDefId; // proc_def_id_
  private String name; // name_
  private String parentTaskId; // parent_task_id_
  private String description; // description_
  private String taskDefKey; // task_def_key_
  private String owner; // owner_
  private String assignee; // assignee_
  private String delegation; // delegation_
  private String priority; // priority_
  private Date createTime; // create_time_
  private Date dueDate; // due_date_
  private String category; // category_
  private String suspensionState; // suspension_state_
  private String tenantId; // tenant_id_
  private String formKey; // form_key_

  private ActYwHiProcinst hiProcinst;

  public ActYwRuTask() {
    super();
  }

  public ActYwRuTask(String id) {
    super(id);
  }

  @Length(min = 0, max = 11, message = "rev_长度必须介于 0 和 11 之间")
  public String getRev() {
    return rev;
  }

  public void setRev(String rev) {
    this.rev = rev;
  }

  @Length(min = 0, max = 64, message = "execution_id_长度必须介于 0 和 64 之间")
  public String getExecutionId() {
    return executionId;
  }

  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }

  @Length(min = 0, max = 64, message = "proc_inst_id_长度必须介于 0 和 64 之间")
  public String getProcInstId() {
    return procInstId;
  }

  public void setProcInstId(String procInstId) {
    this.procInstId = procInstId;
  }

  @Length(min = 0, max = 64, message = "proc_def_id_长度必须介于 0 和 64 之间")
  public String getProcDefId() {
    return procDefId;
  }

  public void setProcDefId(String procDefId) {
    this.procDefId = procDefId;
  }

  @Length(min = 0, max = 255, message = "name_长度必须介于 0 和 255 之间")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Length(min = 0, max = 64, message = "parent_task_id_长度必须介于 0 和 64 之间")
  public String getParentTaskId() {
    return parentTaskId;
  }

  public void setParentTaskId(String parentTaskId) {
    this.parentTaskId = parentTaskId;
  }

  @Length(min = 0, max = 4000, message = "description_长度必须介于 0 和 4000 之间")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Length(min = 0, max = 255, message = "task_def_key_长度必须介于 0 和 255 之间")
  public String getTaskDefKey() {
    return taskDefKey;
  }

  public void setTaskDefKey(String taskDefKey) {
    this.taskDefKey = taskDefKey;
  }

  @Length(min = 0, max = 255, message = "owner_长度必须介于 0 和 255 之间")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Length(min = 0, max = 255, message = "assignee_长度必须介于 0 和 255 之间")
  public String getAssignee() {
    return assignee;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  @Length(min = 0, max = 64, message = "delegation_长度必须介于 0 和 64 之间")
  public String getDelegation() {
    return delegation;
  }

  public void setDelegation(String delegation) {
    this.delegation = delegation;
  }

  @Length(min = 0, max = 11, message = "priority_长度必须介于 0 和 11 之间")
  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  @Length(min = 0, max = 255, message = "category_长度必须介于 0 和 255 之间")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Length(min = 0, max = 11, message = "suspension_state_长度必须介于 0 和 11 之间")
  public String getSuspensionState() {
    return suspensionState;
  }

  public void setSuspensionState(String suspensionState) {
    this.suspensionState = suspensionState;
  }

  @Length(min = 0, max = 255, message = "tenant_id_长度必须介于 0 和 255 之间")
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  @Length(min = 0, max = 255, message = "form_key_长度必须介于 0 和 255 之间")
  public String getFormKey() {
    return formKey;
  }

  public void setFormKey(String formKey) {
    this.formKey = formKey;
  }

  public ActYwHiProcinst getHiProcinst() {
    return hiProcinst;
  }

  public void setHiProcinst(ActYwHiProcinst hiProcinst) {
    this.hiProcinst = hiProcinst;
  }
}