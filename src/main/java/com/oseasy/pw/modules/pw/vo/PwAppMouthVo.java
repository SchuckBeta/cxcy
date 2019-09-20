package com.oseasy.pw.modules.pw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pw.modules.pw.entity.PwRoom;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 预约Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwAppMouthVo extends DataEntity<PwAppMouthVo> {

    private static final long serialVersionUID = 1L;
    private PwRoom pwRoom;        // 房间
    private String applyId;        // 申报编号

    private User user;        // 预约申请人
    private Date startDate;        // 预约开始时间
    private Date endDate;        // 预约结束时间
    private String status;        // 预约状态
    private String subject;        // 会议主题
    private String personNum;        // 会议人数
    private String opType;        // 预约类型

    private String mouth;        //月
    private String color;       //颜色
    private String mday;        // 具体哪一天
    private String num;         // 预约状态的数量
    private String url;         // 预约跳转url

    public PwAppMouthVo() {
        super();
    }

    public PwAppMouthVo(String id) {
        super(id);
    }

    public String getMouth() {
        return mouth;
    }

    public void setMouth(String mouth) {
        this.mouth = mouth;
    }

    public String getMday() {
        return mday;
    }

    public void setMday(String mday) {
        this.mday = mday;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PwRoom getPwRoom() {
        return pwRoom;
    }

    public void setPwRoom(PwRoom pwRoom) {
        this.pwRoom = pwRoom;
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
}