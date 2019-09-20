package com.oseasy.pw.modules.pw.entity;



/**
 * Created by Administrator on 2018/12/4.
 */
public class RoomUseStatusDto {
    private Integer status;
    private String statusname;
    private Integer roomnum;

    public RoomUseStatusDto() {
        super();
    }
    public RoomUseStatusDto(Integer status,String statusname,Integer roomnum){
        this.status = status;
        this.statusname = statusname;
        this.roomnum = roomnum;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public Integer getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(Integer roomnum) {
        this.roomnum = roomnum;
    }
}
