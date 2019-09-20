package com.oseasy.pw.modules.pw.vo;

import com.oseasy.pw.modules.pw.entity.PwFassets;

/**
 * 批量添加固定资产
 */
public class PwFassetsBatch {

    private PwFassets pwFassets;

    private String idRule;  //编号规则

    private int amount;    //数量

    private String remarks;

    private String roomId;

    public String getIdRule() {
        return idRule;
    }

    public void setIdRule(String idRule) {
        this.idRule = idRule;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PwFassets getPwFassets() {
        return pwFassets;
    }

    public void setPwFassets(PwFassets pwFassets) {
        this.pwFassets = pwFassets;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
