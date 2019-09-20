package com.oseasy.pro.modules.tpl.vo.impl;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.util.common.utils.StringUtil;

public class Wteacher implements Serializable{
  private static final long serialVersionUID = 1L;
  private String name;//姓名
  private String no;//编号
  private String unit;//单位
  private String title;//职位
  private String mobile;//电话
  private String edu;//学历
  private String email;//邮箱

  public String getNo() {
    return no;
  }
  public void setNo(String no) {
    this.no = no;
  }

  public String getUnit() {
    return unit;
  }
  public void setUnit(String unit) {
    this.unit = unit;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getMobile() {
    return mobile;
  }
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  public String getEdu() {
    return edu;
  }
  public void setEdu(String edu) {
    this.edu = edu;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  public static List<Wteacher> init(List<BackTeacherExpansion> xyteachers) {
    List<Wteacher> wtes = Lists.newArrayList();
    for (BackTeacherExpansion te : xyteachers) {
      wtes.add(init(te));
    }
    return wtes;
  }

  public static Wteacher init(BackTeacherExpansion teacher) {
    Wteacher wte = new Wteacher();
    wte.setTitle(StringUtil.getStr(teacher.getTechnicalTitle()));
    User tu = teacher.getUser();
    if (tu != null) {
      wte.setEdu(StringUtil.getStr(tu.getEducation()));
      wte.setEmail(StringUtil.getStr(tu.getEmail()));
      wte.setMobile(StringUtil.getStr(tu.getMobile()));
      wte.setName((teacher.getUser() == null) ? "" : StringUtil.getStr(teacher.getUser().getName()));
      wte.setNo(StringUtil.getStr(tu.getNo()));
      wte.setUnit((tu.getOffice() == null) ? "" : StringUtil.getStr(tu.getOffice().getName()));
    }
    return wte;
  }
}
