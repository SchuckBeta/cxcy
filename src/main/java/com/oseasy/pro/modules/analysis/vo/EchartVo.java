package com.oseasy.pro.modules.analysis.vo;

/**
 * Created by zhangzheng on 2017/5/10.
 */
public class EchartVo {
    private String name;
    private int value;

    public EchartVo(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
