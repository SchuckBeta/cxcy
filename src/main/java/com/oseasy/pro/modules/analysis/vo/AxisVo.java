package com.oseasy.pro.modules.analysis.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/5/12.
 */
public class AxisVo {
    private String name;
    private List<Integer> data;
    private String type;
    private Integer barWidth;
    private String stack;


    public AxisVo(String name) {
        this.name = name;
        this.data = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public void addData(int val) {
        this.data.add(val);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(Integer barWidth) {
        this.barWidth = barWidth;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }
}
