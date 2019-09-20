package com.oseasy.dr.modules.dr.vo;

import java.util.Calendar;

public class MyRecord implements Comparable<MyRecord> {

    private String cardNo;  //卡号
    private String doorNo;  //门号
    private boolean enter;  //是否进门读卡
    private Calendar time;  //开门时间
    private String recordType; //记录类型
    private String openType;  //开门类型

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    @Override
    public String toString() {
        return "MyRecord{" +
                "cardNo='" + cardNo + '\'' +
                ", doorNo='" + doorNo + '\'' +
                ", enter=" + enter +
                ", time=" + time.getTime() +
                ", recordType='" + recordType + '\'' +
                ", openType='" + openType + '\'' +
                '}';
    }

    @Override
    public int compareTo(MyRecord o) {
        if(o == null) return -1;
        return o.getTime().compareTo(this.getTime());
    }
}
