package com.oseasy.pw.modules.pw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

/**
 * 预约Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwAppointment extends DataEntity<PwAppointment> {

    private static final long serialVersionUID = 1L;
    private PwRoom pwRoom;        // 房间
    private String applyId;        // 申报编号
    private User user;        // 预约申请人
    private Date startDate;        // 预约开始时间
    private Date endDate;        // 预约结束时间
    private int startTime;        // 预约开始时间（小时分）
    private int endTime;        // 预约结束时间（小时分）

    private String status;        // 预约状态
    private String subject;        // 会议主题
    private String personNum;        // 会议人数
    private String opType;        // 预约类型
    private String attendUser;        // 参会名单
    private String color;           //颜色
    private Integer appointmentstyle; //预约形式（1个人2团队3企业）
    //以下为页面查询相关字段
    private List<String> multiStatus;  //状态（方便多状态查询）
    private String dateFrom;
    private String dateTo;
    private String timeFrom;
    private String timeTo;
    private Date rangeDateFrom;
    private Date rangeDateTo;
    private String capacityMin;
    private String capacityMax;
    private String querySql;
    private Integer querystatus;
    private boolean expired = false;
    private Office office;
    private String keys;
    private int isUsing;
    private int isToUse;
    private int isUsed;
    private int isNoUse;

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public int getIsNoUse() {
        return isNoUse;
    }

    public void setIsNoUse(int isNoUse) {
        this.isNoUse = isNoUse;
    }

    public int getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(int isUsing) {
        this.isUsing = isUsing;
    }

    public int getIsToUse() {
        return isToUse;
    }

    public void setIsToUse(int isToUse) {
        this.isToUse = isToUse;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public PwAppointment() {
        super();
    }

    public PwAppointment(String id) {
        super(id);
    }

    public PwAppointment(PwRoom pwRoom, String applyId, Date startDate, Date endDate, String applyName, String status, String subject,
                         String personNum, String opType) {
        super();
        this.pwRoom = pwRoom;
        this.applyId = applyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.subject = subject;
        this.personNum = personNum;
        this.opType = opType;
    }

    public String getAttendUser() {
        return attendUser;
    }

    public void setAttendUser(String attendUser) {
        this.attendUser = attendUser;
    }

    public PwRoom getPwRoom() {
        return pwRoom;
    }

    public void setPwRoom(PwRoom pwRoom) {
        this.pwRoom = pwRoom;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public Integer getQuerystatus() {
        return querystatus;
    }

    public void setQuerystatus(Integer querystatus) {
        this.querystatus = querystatus;
    }

    public Integer getAppointmentstyle() {
        return appointmentstyle;
    }

    public void setAppointmentstyle(Integer appointmentstyle) {
        this.appointmentstyle = appointmentstyle;
    }

    @Length(min = 1, max = 64, message = "申报编号长度必须介于 1 和 64 之间")
    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Length(min = 0, max = 1, message = "预约状态长度必须介于 0 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Length(min = 1, max = 100, message = "会议主题长度必须介于 1 和 100 之间")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Length(min = 0, max = 11, message = "会议人数长度必须介于 0 和 11 之间")
    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        if (StringUtils.isBlank(personNum)) {
            return;
        }
        this.personNum = personNum;
    }

    @Length(min = 0, max = 1, message = "预约类型长度必须介于 0 和 1 之间")
    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getMultiStatus() {
        return multiStatus;
    }

    public void setMultiStatus(List<String> multiStatus) {
        this.multiStatus = multiStatus;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public Date getRangeDateFrom() {
        return rangeDateFrom;
    }

    public void setRangeDateFrom(Date rangeDateFrom) {
        this.rangeDateFrom = rangeDateFrom;
    }

    public Date getRangeDateTo() {
        return rangeDateTo;
    }

    public void setRangeDateTo(Date rangeDateTo) {
        this.rangeDateTo = rangeDateTo;
    }

    public String getCapacityMin() {
        return capacityMin;
    }

    public void setCapacityMin(String capacityMin) {
        this.capacityMin = capacityMin;
    }

    public String getCapacityMax() {
        return capacityMax;
    }

    public void setCapacityMax(String capacityMax) {
        this.capacityMax = capacityMax;
    }

    public boolean isExpired() {
        if(this.getEndDate() != null){
            return new Date().after(this.getEndDate());
        }
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}