package com.oseasy.pw.modules.pw.vo;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class PwEnterInfo {
    private String enterIds;//入驻id串
    private List<String> etypes;//入驻类型
	private String enterTypeStr;//入驻类型字符串描述
    private Date enterStartTime;//入驻日期(最小入驻日期)
	private Date maxEndTime;//最大有效期
    private String enterEndTime;//入驻有效期(最大有效期)
	private List<PwEnterRoomVo> rooms;//入驻分配的场地
    public String getEnterIds() {
        return enterIds;
    }
    public void setEnterIds(String enterIds) {
        this.enterIds = enterIds;
    }
    public List<String> getEtypes() {
        if(this.etypes == null){
            this.etypes = Lists.newArrayList();
        }
        return etypes;
    }
    public void setEtypes(List<String> etypes) {
        this.etypes = etypes;
    }
    public String getEnterTypeStr() {
        return enterTypeStr;
    }
    public void setEnterTypeStr(String enterTypeStr) {
        this.enterTypeStr = enterTypeStr;
    }
    public Date getEnterStartTime() {
        return enterStartTime;
    }
    public void setEnterStartTime(Date enterStartTime) {
        this.enterStartTime = enterStartTime;
    }
    public String getEnterEndTime() {
        return enterEndTime;
    }
    public void setEnterEndTime(String enterEndTime) {
        this.enterEndTime = enterEndTime;
    }
    public Date getMaxEndTime() {
        return maxEndTime;
    }
    public void setMaxEndTime(Date maxEndTime) {
        this.maxEndTime = maxEndTime;
    }
    public List<PwEnterRoomVo> getRooms() {
        return rooms;
    }
    public void setRooms(List<PwEnterRoomVo> rooms) {
        this.rooms = rooms;
    }
}