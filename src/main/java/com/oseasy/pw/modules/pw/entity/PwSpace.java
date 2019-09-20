package com.oseasy.pw.modules.pw.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.TreeEntity;
import com.oseasy.pw.modules.pw.vo.PwSparam;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 设施Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwSpace extends TreeEntity<PwSpace> {

    private static final long serialVersionUID = 1L;
    private PwSpace parent;        // 父级编号
    private String parentIds;        // 所有父级编号
    private String name;        // 名称
    private String person;        // 负责人
    private String type;        // 设施类型
    private String phone;        // 电话
    private String mobile;        // 手机
    private String openWeek;        // 开放时间:周
    @Transient
    private List<String> openWeeks;        // 开放时间:周
    private String amOpenStartTime;        // 上午开放时间开始
    private String amOpenEndTime;        // 上午开放时间结束
    private String pmOpenStartTime;        // 下午开放时间开始
    private String pmOpenEndTime;        // 下午开放时间结束
    private String floorNum;        // 楼层数
    private String area;        // 占地面积
    private String imageUrl;   //背景图url
    @Transient
    private PwDesignerCanvas pwDesignerCanvas; //楼层设计相关信息
    private List<PwRoom> rooms;
    @Transient
    private PwSparam sparam;

    public PwSpace() {
        super();
    }

    public PwSpace(String id) {
        super(id);
    }

    @JsonBackReference
    @NotNull(message = "父级编号不能为空")
    public PwSpace getParent() {
        return parent;
    }

    public void setParent(PwSpace parent) {
        this.parent = parent;
    }


    public String genNames() {
        if(this.parent != null){
            return fx(this);
        }else{
            return getName();
        }
    }

    /**
     * 递归循环查找父级名称.
     * @param space
     * @return
     */
    private static String fx(PwSpace space) {
        if((space == null) || (space.getParent() == null)){
            return StringUtil.LINE + space.getName();
        }
        StringBuffer buffer = new StringBuffer();
        if((space.parent != null) && StringUtil.isNotEmpty(space.parent.getName())){
            buffer.append(fx(space.parent));
            buffer.append(StringUtil.LINE);
            buffer.append(space.getName());
        }
        return buffer.toString();
    }

    @Length(min = 1, max = 2000, message = "所有父级编号长度必须介于 1 和 2000 之间")
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @Length(min = 1, max = 100, message = "名称长度必须介于 1 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 100, message = "负责人长度必须介于 0 和 100 之间")
    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @Length(min = 0, max = 1, message = "设施类型长度必须介于 0 和 1 之间")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getOpenWeek() {
        return openWeek;
    }

    public void setOpenWeek(String openWeek) {
        this.openWeek = openWeek;
    }

    public String getAmOpenStartTime() {
        return amOpenStartTime;
    }

    public void setAmOpenStartTime(String amOpenStartTime) {
        this.amOpenStartTime = amOpenStartTime;
    }

    public String getAmOpenEndTime() {
        return amOpenEndTime;
    }

    public void setAmOpenEndTime(String amOpenEndTime) {
        this.amOpenEndTime = amOpenEndTime;
    }

    public String getPmOpenStartTime() {
        return pmOpenStartTime;
    }

    public void setPmOpenStartTime(String pmOpenStartTime) {
        this.pmOpenStartTime = pmOpenStartTime;
    }

    public String getPmOpenEndTime() {
        return pmOpenEndTime;
    }

    public void setPmOpenEndTime(String pmOpenEndTime) {
        this.pmOpenEndTime = pmOpenEndTime;
    }

    public Date getOpenStartTimeDate() {
        if (StringUtil.isNotEmpty(amOpenStartTime)) {
            return DateUtil.formatHmsToDate(amOpenStartTime);
        }
        return null;
    }

    public Date getOpenEndTimeDate() {
        if (StringUtil.isNotEmpty(amOpenEndTime)) {
            return DateUtil.formatHmsToDate(amOpenEndTime);
        }
        return null;
    }

    @Length(min = 0, max = 11, message = "楼层数长度必须介于 0 和 11 之间")
    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = StringUtils.isNotBlank(floorNum) ? floorNum : null;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getOpenWeeks() {
        if (StringUtils.isNotBlank(openWeek)) {
            String[] openWeekArray = StringUtil.split(openWeek, StringUtil.DOTH);
            openWeeks = Lists.newArrayList();
            for (String fType : openWeekArray) {
                openWeeks.add(fType);
            }
        }
        return openWeeks;
    }

    public void setOpenWeeks(List<String> openWeeks) {
        this.openWeeks = openWeeks;
        if (openWeeks != null && !openWeeks.isEmpty()) {
            this.openWeek = StringUtils.join(openWeeks, ",");
        } else {
            this.openWeek = null;
        }
    }

    public List<PwRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<PwRoom> rooms) {
        this.rooms = rooms;
    }

    public PwDesignerCanvas getPwDesignerCanvas() {
        return pwDesignerCanvas;
    }

    public void setPwDesignerCanvas(PwDesignerCanvas pwDesignerCanvas) {
        this.pwDesignerCanvas = pwDesignerCanvas;
    }

    public PwSparam getSparam() {
        return sparam;
    }

    public void setSparam(PwSparam sparam) {
        this.sparam = sparam;
    }
}