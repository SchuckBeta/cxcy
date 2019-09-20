package com.oseasy.pw.modules.pw.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 固定资产Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwFassets extends DataEntity<PwFassets> {

    private static final long serialVersionUID = 1L;
    private PwRoom pwRoom;        // 房间编号
    private PwCategory pwCategory;        // 固定资产类别
    private String name;        // 名称
    private String brand;        // 品牌
    private String specification;        // 规格
    private String prname;        // 购买人姓名
    private String phone;        // 电话
    private String mobile;        // 手机
    private Date time;        // 购买日期
    private String price;        // 单价
    private String status;        // 状态
    private String respName;        // 负责人姓名
    private String respPhone;        // 电话
    private String respMobile;        // 手机
    private Date startDate;        // 开始时间

    public PwFassets() {
        super();
    }

    public PwFassets(PwRoom pwRoom) {
        super();
        this.pwRoom = pwRoom;
    }

    public PwFassets(PwCategory pwCategory) {
        super();
        this.pwCategory = pwCategory;
    }

    public PwFassets(String id) {
        super(id);
    }

    public PwFassets(PwRoom pwRoom, String respName, String respPhone, String respMobile) {
        super();
        this.pwRoom = pwRoom;
        this.respName = respName;
        this.respPhone = respPhone;
        this.respMobile = respMobile;
    }

    public PwRoom getPwRoom() {
        return pwRoom;
    }

    public void setPwRoom(PwRoom pwRoom) {
        this.pwRoom = pwRoom;
    }

    public PwCategory getPwCategory() {
        return pwCategory;
    }

    public void setPwCategory(PwCategory pwCategory) {
        this.pwCategory = pwCategory;
    }

    @Length(min = 1, max = 100, message = "名称长度必须介于 1 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 255, message = "品牌长度必须介于 0 和 255 之间")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Length(min = 0, max = 255, message = "规格长度必须介于 0 和 255 之间")
    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    @Length(min = 0, max = 255, message = "购买人姓名长度必须介于 0 和 255 之间")
    public String getPrname() {
        return prname;
    }

    public void setPrname(String prname) {
        this.prname = prname;
    }

    @Length(min = 0, max = 200, message = "电话长度必须介于 0 和 200 之间")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min = 0, max = 200, message = "手机长度必须介于 0 和 200 之间")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = StringUtils.isNotBlank(price) ? price : null;
    }

    @Length(min = 0, max = 1, message = "状态长度必须介于 0 和 1 之间")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}