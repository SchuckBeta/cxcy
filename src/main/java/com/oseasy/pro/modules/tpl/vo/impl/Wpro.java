package com.oseasy.pro.modules.tpl.vo.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.tpl.vo.WproType;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

public class Wpro implements Serializable{
  private static final long serialVersionUID = 1L;
  private String type;//项目类型：创新训练、创业实践、创业训练
  private String no;//编号
  private String appLevel;//申报级别
  private String name;//项目名称
  private String money;//申请金额
  private String applyDate;//申请时间
  private String startDate;//开始时间
  private String endDate;//结束时间
  private String curDate;//当前时间
  private String subject;//学科

  public String getAppLevel() {
    return appLevel;
  }

  public void setAppLevel(String appLevel) {
    this.appLevel = appLevel;
  }

  public String getCurDate() {
    return curDate;
  }

  public void setCurDate(String curDate) {
    this.curDate = curDate;
  }

  public String getMoney() {
    return money;
  }

  public void setMoney(String money) {
    this.money = money;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setNo(String no) {
    this.no = no;
  }

  public String getNo() {
    return no;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setApplyDate(String applyDate) {
    this.applyDate = applyDate;
  }

  public String getApplyDate() {
    return applyDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getSubject() {
    return subject;
  }

  public static Wpro init(ProModel pro, ProModelMd prom) {
    Wpro wpro = new Wpro();
    wpro.setAppLevel(StringUtil.getStr(prom.getAppLevelName()));
    wpro.setName(StringUtil.getStr(pro.getPName()));
    //wpro.setApplyDate(DateUtil.format(new SimpleDateFormat(DateUtil.FMT_YYYYMMDD), pro.getSubTime()));
    wpro.setCurDate(DateUtil.format(new SimpleDateFormat(DateUtil.FMT_YYYYMMDD), new Date()));
    wpro.setMoney(StringUtil.getFloatStr(prom.getAppAmount()));
    wpro.setSubject(StringUtil.getStr(prom.getSubjectName()));
    WproType wproType = WproType.getByKey(pro.getProCategory());
    wpro.setType((wproType == null) ? "" : wproType.getName());

    ProProject proProject = pro.getActYw().getProProject();
    if (proProject != null) {
      wpro.setStartDate(DateUtil.format(new SimpleDateFormat(DateUtil.FMT_YYYYMMDD), proProject.getStartDate()));
      wpro.setEndDate(DateUtil.format(new SimpleDateFormat(DateUtil.FMT_YYYYMMDD), proProject.getEndDate()));
    }

//    User deuser = pro.getDeuser();
//    if (deuser != null) {
//      wpro.setSubject(StringUtil.getStr(((deuser.getSubject() == null) ? "" :StringUtil.getStr(deuser.getSubject().getName()))));
//    }
    return wpro;
  }
}
