package com.oseasy.pie.common.config;

/**
 * 系统固定ID
 *
 * @author Administrator
 */
public enum PieIds {
    IE_DS("20", "互联网+大赛"),
    IE_DS_GJ("10", "国家互联网+大赛");

    private String id;
    private String remark;

    private PieIds(String id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }


    public String getRemark() {
        return remark;
    }

    public static PieIds getById(String id) {
        PieIds[] entitys = PieIds.values();
        for (PieIds entity : entitys) {
            if ((id).equals(entity.getId())) {
                return entity;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    @Override
    public String toString() {
        if(this != null){
            return "{\"id\":\"" + this.id + ",\"remark\":\"" + this.remark + "\"}";
        }
        return super.toString();
    }
}
