package com.oseasy.pw.common.config;

/**
 * 房间使用状态枚举类
 * Created by Administrator on 2018/12/4.
 */
public enum RoomUseStatus {
    ORDEREDROOM(1,"已预约"),
    ALLOTEDROOM(2,"已分配"),
    OTHERSTATUS(3,"空闲"),
    ORDERUSINGROOM(4,"预约使用中"),
    ORDERNOTUSEROOM(5,"预约未使用"),
    NOTORDERROOM(6,"未预约"),
    ALLOTEDFULLROOM(7,"已分配（满员）"),
    ALLOTEDNOTFULLROOM(8,"已分配（未满员）"),
    NOTALLOTROOM(9,"未分配"),
    EXITROOM(10,"已退孵"),
    USED(11,"使用中"),
    TO_USE(12,"待使用"),
    END_USE(13,"已使用"),
    CANCEL_ORDER(14,"取消预约"),
    CHANGED_ROOM(15,"已更换房间"),
    OTHER_ROOM(16,"其他")
    ;




    private Integer status;
    private String statusname;

    RoomUseStatus(Integer status,String statusname){
        this.status = status;
        this.statusname = statusname;
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

    public static String getMsg(Integer status) {
        for(RoomUseStatus roomUseStatus : RoomUseStatus.values()){
            if(status.equals(roomUseStatus.getStatus())){
                return roomUseStatus.getStatusname();
            }
        }
        return "";
    }
}
