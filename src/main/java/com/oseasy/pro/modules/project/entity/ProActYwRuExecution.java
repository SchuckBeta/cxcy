package com.oseasy.pro.modules.project.entity;

import com.oseasy.act.modules.actyw.entity.ActYwRuExecution;

/**
 * 流程运行实例Entity.
 * @author chenhao
 * @version 2017-06-08
 */
public class ProActYwRuExecution extends ActYwRuExecution {
  private static final long serialVersionUID = 1L;
  private ProjectDeclare projectDeclare;

  public ProActYwRuExecution() {
    super();
  }

  public ProActYwRuExecution(String id) {
    super(id);
  }

  public ProjectDeclare getProjectDeclare() {
    return projectDeclare;
  }

  public void setProjectDeclare(ProjectDeclare projectDeclare) {
    this.projectDeclare = projectDeclare;
  }
}