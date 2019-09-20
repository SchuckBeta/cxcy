package com.oseasy.pw.modules.pw.vo;

public enum PwSpaceType {

    SCHOOL("0", "学校"),
    CAMPUS("1", "校区"),
    BASE("2", "基地"),
    BUILDING("3", "楼栋"),
    FLOOR("4", "楼层");

    private String value;
    private String name;

    PwSpaceType(String value, String name) {
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
            case "0":
                return SCHOOL.getName();
            case "1":
                return CAMPUS.getName();
            case "2":
                return BASE.getName();
            case "3":
                return BUILDING.getName();
            case "4":
                return FLOOR.getName();
            default:
                return "";
        }
    }
}
