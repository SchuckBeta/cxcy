package com.oseasy.pw.modules.pw.entity;

public class PwSpaceRoom {

    private PwSpace pwSpace;

    private PwRoom pwRoom;

    private String roomId;

    private String roomName;

    private String roomType;

    private String usable;

    private String roomDelFlag;

    private String respName;

    public PwSpace getPwSpace() {
        return pwSpace;
    }

    public void setPwSpace(PwSpace pwSpace) {
        this.pwSpace = pwSpace;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getRoomDelFlag() {
        return roomDelFlag;
    }

    public void setRoomDelFlag(String roomDelFlag) {
        this.roomDelFlag = roomDelFlag;
    }

    public String getRespName() {
        return respName;
    }

    public void setRespName(String respName) {
        this.respName = respName;
    }

    public PwRoom getPwRoom() {
        return pwRoom;
    }

    public void setPwRoom(PwRoom pwRoom) {
        this.pwRoom = pwRoom;
    }
}
