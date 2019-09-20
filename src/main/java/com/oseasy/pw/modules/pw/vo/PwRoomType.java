package com.oseasy.pw.modules.pw.vo;

public enum PwRoomType {

    MEETINGROOM("1", "会议室"),
    ROADSHOW("2", "路演大厅"),
    OFFICE("3", "办公室"),
    LABORATORY("4", "实验室"),
    PUBLICROOM("5", "公共办公区");

    private String value;
    private String name;

    PwRoomType(String value, String name) {
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

    public static String getNameByValue(String value) {
        switch (value) {
            case "1":
                return MEETINGROOM.getName();
            case "2":
                return ROADSHOW.getName();
            case "3":
                return OFFICE.getName();
            case "4":
                return LABORATORY.getName();
            case "5":
                return PUBLICROOM.getName();
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "{\"value\":\"" + this.value + "\",\"name\":\"" + this.name + "\"}";
    }
}
