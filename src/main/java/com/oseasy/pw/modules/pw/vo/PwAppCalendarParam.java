package com.oseasy.pw.modules.pw.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 预定日历Vo.
 * @author chenhao
 *
 */
public class PwAppCalendarParam implements Serializable{
  private static final long serialVersionUID = 1L;

  private String subject;
  private String applyName;
  private Date startDate;   // 预约开始时间
  private Date endDate;   // 预约结束时间
  private Integer numMax;   // 最大申报人数
  private Integer numMin;   // 最小申报人数
  private List<String> opTypes;    // 类型
  private List<String> status;    // 预约状态
  private List<String> roomIds;//房间IDS
  private List<String> floorIds;//楼层IDS
  private List<String> buildingIds;//楼栋IDS
  private List<String> baseIds;//基地IDS

  public List<String> getOpTypes() {
    return opTypes;
  }
  public void setOpTypes(List<String> opTypes) {
    this.opTypes = opTypes;
  }
  public String getApplyName() {
    return applyName;
  }
  public void setApplyName(String applyName) {
    this.applyName = applyName;
  }
  public Integer getNumMax() {
    return numMax;
  }
  public void setNumMax(Integer numMax) {
    this.numMax = numMax;
  }
  public Integer getNumMin() {
    return numMin;
  }
  public void setNumMin(Integer numMin) {
    this.numMin = numMin;
  }
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public List<String> getStatus() {
    return status;
  }
  public void setStatus(List<String> status) {
    this.status = status;
  }
  public List<String> getRoomIds() {
    return roomIds;
  }
  public void setRoomIds(List<String> roomIds) {
    this.roomIds = roomIds;
  }
  public List<String> getFloorIds() {
    return floorIds;
  }
  public void setFloorIds(List<String> floorIds) {
    this.floorIds = floorIds;
  }

  public List<String> getBuildingIds() {
    return buildingIds;
  }
  public void setBuildingIds(List<String> buildingIds) {
    this.buildingIds = buildingIds;
  }
  public List<String> getBaseIds() {
    return baseIds;
  }
  public void setBaseIds(List<String> baseIds) {
    this.baseIds = baseIds;
  }
}
