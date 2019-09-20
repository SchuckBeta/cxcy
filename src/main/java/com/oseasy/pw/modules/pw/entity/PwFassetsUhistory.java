package com.oseasy.pw.modules.pw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 固定资产使用记录Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwFassetsUhistory extends DataEntity<PwFassetsUhistory> {

    private static final long serialVersionUID = 1L;
    private PwRoom pwRoom;        // 房间
    private PwFassets pwFassets;        // 固定资产
    private String respName;        // 负责人姓名
    private String respPhone;        // 电话
    private String respMobile;        // 手机
    private Date startDate;        // 开始使用时间
    private Date endDate;        // 结束使用时间
    private String keys; //模糊查询条件

    public PwFassetsUhistory() {
        super();
    }

    public PwFassetsUhistory(String id) {
        super(id);
    }

    public PwRoom getPwRoom() {
        return pwRoom;
    }

    public void setPwRoom(PwRoom pwRoom) {
        this.pwRoom = pwRoom;
    }

    public PwFassets getPwFassets() {
        return pwFassets;
    }

    public void setPwFassets(PwFassets pwFassets) {
        this.pwFassets = pwFassets;
    }

    @Length(min = 0, max = 255, message = "负责人姓名长度必须介于 0 和 255 之间")
    public String getRespName() {
        return respName;
    }

    public void setRespName(String respName) {
        this.respName = respName;
    }

    @Length(min = 0, max = 200, message = "电话长度必须介于 0 和 200 之间")
    public String getRespPhone() {
        return respPhone;
    }

    public void setRespPhone(String respPhone) {
        this.respPhone = respPhone;
    }

    @Length(min = 0, max = 200, message = "手机长度必须介于 0 和 200 之间")
    public String getRespMobile() {
        return respMobile;
    }

    public void setRespMobile(String respMobile) {
        this.respMobile = respMobile;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}