package com.oseasy.pro.modules.workflow.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class TeamVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Excel(name="姓名", orderNum = "1")
    private String name;
    @Excel(name="学号", orderNum = "2")
    private String no;
    @Excel(name="专业", orderNum = "3")
    private String profes;
    @Excel(name="入学年份", orderNum = "4")
    private String enter;
    @Excel(name="毕业年份", orderNum = "5")
    private String outy;
    @Excel(name="学历", orderNum = "6")
    private String xueli;
    @Excel(name="身份证号码", orderNum = "7")
    private String idnum;
    @Excel(name="手机号码", orderNum = "8")
    private String mobile;
    @Excel(name="邮箱", orderNum = "9")
    private String email;
    @Excel(name="学院", orderNum = "10")
    private String officeName;
    @Excel(name="职称", orderNum = "11")
    private String teacherTecTitle;

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

    public String getProfes() {
        return profes;
    }

    public void setProfes(String profes) {
        this.profes = profes;
    }

    public String getEnter() {
        return enter;
    }

    public void setEnter(String enter) {
        this.enter = enter;
    }

    public String getOuty() {
        return outy;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public void setOuty(String outy) {
        this.outy = outy;
    }

    public String getXueli() {
        return xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeacherTecTitle() {
        return teacherTecTitle;
    }

    public void setTeacherTecTitle(String teacherTecTitle) {
        this.teacherTecTitle = teacherTecTitle;
    }
}
