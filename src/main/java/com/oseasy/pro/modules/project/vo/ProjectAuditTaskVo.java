package com.oseasy.pro.modules.project.vo;

import java.io.Serializable;

/**
 * Created by PW on 2019/1/25.
 */
public class ProjectAuditTaskVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name;
    private String year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
