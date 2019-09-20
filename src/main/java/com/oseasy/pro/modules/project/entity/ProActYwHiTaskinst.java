package com.oseasy.pro.modules.project.entity;

import com.oseasy.act.modules.actyw.entity.ActYwHiTaskinst;

/**
 * 流程历史任务Entity.
 * @author chenhao
 * @version 2017-06-08
 */
public class ProActYwHiTaskinst extends ActYwHiTaskinst {
  private static final long serialVersionUID = 1L;
  private ProjectDeclare projectDeclare;

  public ProActYwHiTaskinst() {
    super();
  }

  public ProActYwHiTaskinst(String id) {
    super(id);
  }

  public ProjectDeclare getProjectDeclare() {
    return projectDeclare;
  }

  public void setProjectDeclare(ProjectDeclare projectDeclare) {
    this.projectDeclare = projectDeclare;
  }
}