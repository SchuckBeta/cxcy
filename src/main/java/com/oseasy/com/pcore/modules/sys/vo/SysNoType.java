package com.oseasy.com.pcore.modules.sys.vo;

public enum SysNoType {
    NO_FLOW("FWNO", "流程惟一编号"),
    NO_PROJECT("PRONO", "项目惟一编号"),
    NO_OFFICE("OFFICE_NO", "机构惟一编号"),
    NO_ORDER("ORDER_NO", "订单惟一编号"),
    NO_YW_APPLY("YW_APPLY_NO", "流程申报唯一标识"),
    NO_PW_ER("PW_ENTER_NO", "入驻申报唯一标识"),
    NO_SITE("SITE_NO", "站点惟一编号");

    private String key;
    private String remarks;

    private SysNoType(String key, String remarks) {
        this.key = key;
        this.remarks = remarks;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public static SysNoType getBykey(String key) {
        SysNoType[] entitys = SysNoType.values();
        for (SysNoType entity : entitys) {
            if ((key).equals(entity.getKey())) {
                return entity;
            }
        }
        return null;
    }
}