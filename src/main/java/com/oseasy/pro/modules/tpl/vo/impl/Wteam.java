package com.oseasy.pro.modules.tpl.vo.impl;

import java.io.Serializable;
import java.util.List;

import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.util.common.utils.StringUtil;

public class Wteam implements Serializable{
  private static final long serialVersionUID = 1L;
  private String spname;//负责人姓名
  private String spsex;//负责人性别
  private String spmz;//负责人性别
  private String spcarno;//负责人身份证号
  private String spmobile;//负责人电话
  private String spqq;//负责人QQ
  private String spcollege;//负责人学院
  private String spno;//负责人学号
  private String spclazz;//负责人班级
  private List<Wteacher> te;//校园导师（必填）
  private List<Wteacher> teo;//企业导师（创业实践项目、创业训练项目必填）
  private List<Wmember> tm;//团队成员

  public String getSpname() {
    return spname;
  }
  public void setSpname(String spname) {
    this.spname = spname;
  }
  public String getSpsex() {
    return spsex;
  }
  public void setSpsex(String spsex) {
    this.spsex = spsex;
  }
  public String getSpcarno() {
    return spcarno;
  }
  public void setSpcarno(String spcarno) {
    this.spcarno = spcarno;
  }
  public String getSpmobile() {
    return spmobile;
  }
  public void setSpmobile(String spmobile) {
    this.spmobile = spmobile;
  }
  public String getSpqq() {
    return spqq;
  }
  public void setSpqq(String spqq) {
    this.spqq = spqq;
  }
  public String getSpcollege() {
    return spcollege;
  }
  public String getSpmz() {
    return spmz;
  }
  public void setSpmz(String spmz) {
    this.spmz = spmz;
  }
  public void setSpcollege(String spcollege) {
    this.spcollege = spcollege;
  }
  public String getSpno() {
    return spno;
  }
  public void setSpno(String spno) {
    this.spno = spno;
  }

  public String getSpclazz() {
    return spclazz;
  }
  public void setSpclazz(String spclazz) {
    this.spclazz = spclazz;
  }

  public List<Wteacher> getTeo() {
    return teo;
  }
  public void setTeo(List<Wteacher> teo) {
    this.teo = teo;
  }
  public List<Wteacher> getTe() {
    return te;
  }
  public void setTe(List<Wteacher> te) {
    this.te = te;
  }
  public List<Wmember> getTm() {
    return tm;
  }
  public void setTm(List<Wmember> tm) {
    this.tm = tm;
  }

  public static Wteam init(User deuser, Team team, List<BackTeacherExpansion> xyteachers, List<BackTeacherExpansion> qyteachers, List<StudentExpansion> students) {
    Wteam wte = new Wteam();
    wte.setTe(Wteacher.init(xyteachers));
    wte.setTeo(Wteacher.init(qyteachers));
    wte.setTm(Wmember.init(students));

    if (deuser != null) {
      wte.setSpcarno(StringUtil.getStr(deuser.getIdNumber()));
      wte.setSpclazz(StringUtil.getStr(((deuser.getSubject() == null) ? "" :StringUtil.getStr(deuser.getSubject().getName()))));
      wte.setSpmobile(StringUtil.getStr(deuser.getMobile()));
      wte.setSpcollege((deuser.getOffice() == null) ? "" :StringUtil.getStr(deuser.getOffice().getName()));
      wte.setSpmz(StringUtil.getStr(deuser.getNational()));
      wte.setSpname(StringUtil.getStr(deuser.getName()));
      wte.setSpno(StringUtil.getStr(deuser.getNo()));
      wte.setSpqq(StringUtil.getStr(deuser.getQq()));
      wte.setSpsex(("1".equals(deuser.getSex()))?"男":"女");
    }
    return wte;
  }
}
