/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import com.oseasy.pw.modules.pw.entity.PwEnterRoom;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * .
 * @author chenhao
 *
 */
@ExcelTarget("room")
public class PwEnterExpRoom implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    @Excel(name = "名称", orderNum = "1",  width = 30, height = 12)
    private String name; //项目名称
    @Excel(name = "人数", orderNum = "2",  width = 30, height = 12)
    private Integer cnum; //项目简介

    public PwEnterExpRoom(PwEnterRoom eroom) {
        this.name = eroom.getPwRoom().getName();
        this.cnum = eroom.getCnum();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getCnum() {
        return cnum;
    }
    public void setCnum(Integer cnum) {
        this.cnum = cnum;
    }
}
