package com.oseasy.pw.modules.pw.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.model.ConvertAnchor;

import com.google.common.collect.Lists;
import com.oseasy.pw.modules.pw.entity.PwAppointment;

/**
 * 预定日历Vo.
 * @author chenhao
 *
 */
public class PwAppCalendarVo implements Serializable{
  private static final long serialVersionUID = 1L;

  private String id;
  private String subject;
  private Date startDate;   // 预约开始时间
  private Date endDate;   // 预约结束时间
  private String status;    // 预约状态
  private String color;   // 颜色
  private String url;   // 地址

  public PwAppCalendarVo() {
    super();
  }

  public PwAppCalendarVo(PwAppointment pwAppointment) {
    super();
    if(pwAppointment != null){
      this.id = pwAppointment.getId();
      this.subject = pwAppointment.getSubject();
      this.startDate = pwAppointment.getStartDate();
      this.endDate = pwAppointment.getEndDate();
      this.status = pwAppointment.getStatus();
    }
  }

  public PwAppCalendarVo(String id, String subject, Date startDate, Date endDate, String status,
      String color, String url) {
    super();
    this.id = id;
    this.subject = subject;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
    this.color = color;
    this.url = url;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
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
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getColor() {
    return color;
  }
  public void setColor(String color) {
    this.color = color;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  public static PwAppCalendarVo convert(PwAppointment pwAppointment) {
    PwAppCalendarVo vo = new PwAppCalendarVo(pwAppointment);
    vo.setUrl("");
    return vo;
  }

  public static List<PwAppCalendarVo> convert(List<PwAppointment> pwAppointments) {
    List<PwAppCalendarVo> pcvo = Lists.newArrayList();
    for (PwAppointment curPwAppointment : pwAppointments) {
      PwAppCalendarVo pvo = convert(curPwAppointment);
      pcvo.add(pvo);
    }
    return pcvo;
  }
}
