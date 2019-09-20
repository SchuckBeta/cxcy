package com.oseasy.pw.modules.pw.vo;

public enum PwFassetsStatus {

    UNUSED("0", "闲置"),
    USING("1", "正在使用"),
    BROKEN("2", "损坏");

    private String value;
    private String name;

    PwFassetsStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
