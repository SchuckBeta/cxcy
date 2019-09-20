package com.oseasy.pw.modules.pw.vo;

import com.oseasy.com.pcore.common.persistence.BaseEntity;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pw.modules.pw.entity.PwAppointment;

import java.util.Date;
import java.util.List;

/**
 * 预约
 */
public class PwAppointmentVo extends DataEntity<PwAppointment> {

    private User user;

    private List<String> roomTypes;

    private String buildingId;

    private String floorId;

    private List<String> roomIds;

    private List<String> status;

    private String roomNumMin;

    private String roomNumMax;

    private Date startDate;

    private Date endDate;

    private String opType;

    private String searchDay;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<String> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public List<String> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<String> roomIds) {
        this.roomIds = roomIds;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public String getRoomNumMin() {
        return roomNumMin;
    }

    public void setRoomNumMin(String roomNumMin) {
        this.roomNumMin = roomNumMin;
    }

    public String getRoomNumMax() {
        return roomNumMax;
    }

    public void setRoomNumMax(String roomNumMax) {
        this.roomNumMax = roomNumMax;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getSearchDay() {
        return searchDay;
    }

    public void setSearchDay(String searchDay) {
        this.searchDay = searchDay;
    }
}
