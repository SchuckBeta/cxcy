package com.oseasy.dr.common.config;

/**
 * Dr系统固定ID
 *
 * @author Administrator
 */
public enum DrIds {
    DR_CARD_GID("0", "全局GROUP ID");

    private String id;
    private String remark;

    private DrIds(String id, String remark) {
        this.id = id;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }


    public String getRemark() {
        return remark;
    }

    public static DrIds getById(String id) {
        DrIds[] entitys = DrIds.values();
        for (DrIds entity : entitys) {
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
