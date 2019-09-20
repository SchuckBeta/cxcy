package com.oseasy.pw.modules.pw.vo;

public enum PwAppointmentStatus {

    WAIT_AUDIT("0", "待审核"),
    PASS("1", "审核通过"),
    REJECT("2", "审核拒绝"),
    CANCELED("3", "已取消"),
    LOCKED("4", "已锁定"),
    EXPIRED("5", "已过期");

    private String value;
    private String name;

    PwAppointmentStatus(String value, String name) {
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
