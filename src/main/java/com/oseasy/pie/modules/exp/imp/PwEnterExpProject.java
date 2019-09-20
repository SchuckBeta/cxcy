/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import com.oseasy.pw.modules.pw.entity.PwProject;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * .
 * @author chenhao
 *
 */
@ExcelTarget("project")
public class PwEnterExpProject implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    @Excel(name = "名称", orderNum = "1",  width = 30, height = 12)
    private String name; //项目名称
    @Excel(name = "简介", orderNum = "2",  width = 30, height = 12)
    private String remarks; //项目简介

    public PwEnterExpProject(PwProject project) {
        this.name = project.getName();
        this.remarks = project.getRemarks();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
