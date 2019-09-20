package com.oseasy.act.modules.actyw.tool.apply.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 节点审核结果类型.
 * @author chenhao
 */
public class SuStatusGrade {
  /**
   * 流程节点标识.
   */
  private String gnodeId;
  /**
   * 当前结点的审核状态类型.
   */
  private List<SuStatus> grades;

  public SuStatusGrade(String gnodeId) {
    super();
    this.gnodeId = gnodeId;
  }

  public SuStatusGrade(String gnodeId, List<SuStatus> grades) {
    super();
    this.gnodeId = gnodeId;
    this.grades = grades;
  }

  public String getGnodeId() {
    return gnodeId;
  }

  public void setGnodeId(String gnodeId) {
    this.gnodeId = gnodeId;
  }

  public List<SuStatus> getGrades() {
    return grades;
  }
  public void setGrades(List<SuStatus> grades) {
    this.grades = grades;
  }

  /**
   * 获取当前结点所有状态.
   * @param grades 状态节点列表
   * @param gnodeId 节点ID
   * @return List
   */
  public static List<SuStatusGrade> getGradesByGnode(List<SuStatusGrade> grades, String gnodeId) {
    List<SuStatusGrade> applyGrades = Lists.newArrayList();
    for (SuStatusGrade applyGrade : grades) {
      if ((applyGrade.getGnodeId()).equals(gnodeId)) {
        applyGrades.add(applyGrade);
      }
    }
    return applyGrades;
  }

  /**
   * 获取当前结点状态.
   * @param grades 状态节点列表
   * @param gnodeId 节点ID
   * @return SuStatusGrade
   */
  public static SuStatusGrade getGradeByGnode(List<SuStatusGrade> grades, String gnodeId) {
    for (SuStatusGrade applyGrade : grades) {
      if ((applyGrade.getGnodeId()).equals(gnodeId)) {
        return applyGrade;
      }
    }
    return null;
  }
}
