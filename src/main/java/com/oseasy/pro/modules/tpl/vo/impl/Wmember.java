package com.oseasy.pro.modules.tpl.vo.impl;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.util.common.utils.StringUtil;

public class Wmember implements Serializable{
  private static final long serialVersionUID = 1L;
  private String name;//姓名
  private String no;//学号
  private String mobile;//手机号
  private String college;//专业、学院
  private String email;//邮箱
  private String work;//项目分工

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getNo() {
    return no;
  }
  public void setNo(String no) {
    this.no = no;
  }
  public String getMobile() {
    return mobile;
  }
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  public String getCollege() {
    return college;
  }
  public void setCollege(String college) {
    this.college = college;
  }
  public String getWork() {
    return work;
  }
  public void setWork(String work) {
    this.work = work;
  }

  public static List<Wmember> init(List<StudentExpansion> students) {
    List<Wmember> wmes = Lists.newArrayList();
    for (StudentExpansion me : students) {
      wmes.add(init(me));
    }
    return wmes;
  }

  public static Wmember init(StudentExpansion student) {
    Wmember wme = new Wmember();
    User su = student.getUser();
    if (su != null) {
      wme.setCollege((su.getOffice() == null) ? "" :StringUtil.getStr(su.getOffice().getName()));
      //wme.setCollege((su.getSubject() == null) ? "" :StringUtil.getStr(su.getSubject().getName()));
      wme.setEmail(StringUtil.getStr(su.getEmail()));
      wme.setMobile(StringUtil.getStr(su.getMobile()));
      wme.setName(StringUtil.getStr(su.getName()));
      wme.setNo(StringUtil.getStr(su.getNo()));
//      wme.setWork(StringUtil.getStr(""));
    }
    return wme;
  }
}
