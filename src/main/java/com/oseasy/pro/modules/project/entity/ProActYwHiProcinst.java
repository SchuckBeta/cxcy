package com.oseasy.pro.modules.project.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.act.modules.actyw.entity.ActYwHiProcinst;

/**
 * 流程历史实例Entity.
 *
 * @author chenhao
 * @version 2017-06-08
 */
public class ProActYwHiProcinst extends ActYwHiProcinst {
  private static final long serialVersionUID = 1L;
  private ProjectDeclare projectDeclare;

  public ProActYwHiProcinst() {
    super();
  }

  public ProActYwHiProcinst(String id) {
    super(id);
  }

  public ProjectDeclare getProjectDeclare() {
    return projectDeclare;
  }

  public void setProjectDeclare(ProjectDeclare projectDeclare) {
    this.projectDeclare = projectDeclare;
  }
}