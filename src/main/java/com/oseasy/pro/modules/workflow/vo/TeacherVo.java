package com.oseasy.pro.modules.workflow.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class TeacherVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Excel(name="姓名", orderNum = "1")
    private String name;
    @Excel(name="工号", orderNum = "2")
    private String no;
    @Excel(name="学院", orderNum = "3")
    private String office;
    @Excel(name="手机", orderNum = "4")
    private String mobile;
    @Excel(name="邮箱", orderNum = "5")
    private String email;
    @Excel(name="职称", orderNum = "6")
    private String zhicheng;

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

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
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

    public String getZhicheng() {
        return zhicheng;
    }

    public void setZhicheng(String zhicheng) {
        this.zhicheng = zhicheng;
    }
}
