package com.oseasy.pw.modules.pw.vo;

/**
 * 资产(批量)分配
 */
public class PwFassetsAssign {

    private String fassetsIds;      //资产ID(s)

    private String roomId;          //房间ID

    private String respName;        // 负责人姓名

    private String respPhone;        // 电话
    private String respMobile;        // 手机

    public String getFassetsIds() {
        return fassetsIds;
    }

    public void setFassetsIds(String fassetsIds) {
        this.fassetsIds = fassetsIds;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRespMobile() {
      return respMobile;
    }

    public void setRespMobile(String respMobile) {
      this.respMobile = respMobile;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRespName() {
        return respName;
    }

    public void setRespName(String respName) {
        this.respName = respName;
    }

    public String getRespPhone() {
        return respPhone;
    }

    public void setRespPhone(String respPhone) {
        this.respPhone = respPhone;
    }
}
