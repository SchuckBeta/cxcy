package com.oseasy.pro.modules.project.entity;

import com.oseasy.act.modules.actyw.entity.ActYwRuTask;

/**
 * 流程运行任务Entity.
 *
 * @author chenhao
 * @version 2017-06-08
 */
public class ProActYwRuTask extends ActYwRuTask {
  private static final long serialVersionUID = 1L;
  private ProjectDeclare projectDeclare;

  public ProActYwRuTask() {
    super();
  }

  public ProActYwRuTask(String id) {
    super(id);
  }

  public ProjectDeclare getProjectDeclare() {
    return projectDeclare;
  }

  public void setProjectDeclare(ProjectDeclare projectDeclare) {
    this.projectDeclare = projectDeclare;
  }
}