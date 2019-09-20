package com.oseasy.dr.modules.dr.vo;

import Net.PC15.FC8800.Command.Data.CardDetail;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyCard implements Serializable {

    private String no; //卡号
    private String password; //密码
    private Calendar expiry; //有效期
    private Status status; //状态  @see MyCard.Status 枚举类
    private int openTimes; //有效次数  65535无限制
    private Privilege privilege; //特权  @see MyCard.Privilege
    private boolean HolidayUse; //节假日限制
    private Map<String, Object> doorControl; //门控制信息

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getExpiry() {
        return expiry;
    }

    public void setExpiry(Calendar expiry) {
        this.expiry = expiry;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(int openTimes) {
        this.openTimes = openTimes;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public boolean isHolidayUse() {
        return HolidayUse;
    }

    public void setHolidayUse(boolean holidayUse) {
        HolidayUse = holidayUse;
    }

    public Map<String, Object> getDoorControl() {
        return doorControl;
    }

    public void setDoorControl(Map<String, Object> doorControl) {
        this.doorControl = doorControl;
    }

    public MyCard() {
    }

    public MyCard(String no) {
        this.no = no;
    }

    public CardDetail toCardDetail() {
        CardDetail cardDetail = new CardDetail(Long.valueOf(no));//设置卡号
        cardDetail.Password = this.password;
        cardDetail.Expiry = this.expiry;
        if(this.status != null){
            cardDetail.CardStatus = (byte) this.status.getValue();
        }
        cardDetail.OpenTimes = this.openTimes;
        if(this.privilege != null){
            switch (this.privilege.getValue()) {
                case 1:
                    cardDetail.SetPrivilege();
                    break;
                case 2:
                    cardDetail.SetTiming();
                    break;
                case 3:
                    cardDetail.SetGuardTour();
                    break;
                case 4:
                    cardDetail.SetAlarmSetting();
                default:
                    cardDetail.SetNormal();
            }
        }
        cardDetail.HolidayUse = this.HolidayUse;
        if(doorControl != null){//4门控制限制
            cardDetail.SetTimeGroup(1, 1);//每个门都设定为1门
            cardDetail.SetTimeGroup(2, 1);//每个门都设定为1门
            cardDetail.SetTimeGroup(3, 1);//每个门都设定为1门
            cardDetail.SetTimeGroup(4, 1);//每个门都设定为1门

            cardDetail.SetDoor(1, (Boolean) doorControl.get("1"));
            cardDetail.SetDoor(2, (Boolean) doorControl.get("2"));
            cardDetail.SetDoor(3, (Boolean) doorControl.get("3"));
            cardDetail.SetDoor(4, (Boolean) doorControl.get("4"));
        }
        return cardDetail;
    }


    public static MyCard fromCardDetail(CardDetail cardDetail){
        MyCard card = new MyCard();
        card.setNo(cardDetail.CardData + "");
        card.setPassword(cardDetail.Password.replaceAll("F", ""));
        card.setExpiry(cardDetail.Expiry);
        card.setOpenTimes(cardDetail.OpenTimes);
        card.setStatus(MyCard.Status.getStatusByValue(cardDetail.CardStatus));
        if (cardDetail.IsPrivilege()) {
            card.setPrivilege(MyCard.Privilege.SKTQ);
        } else if (cardDetail.IsTiming()) {
            card.setPrivilege(MyCard.Privilege.CKTQ);
        } else if (cardDetail.IsGuardTour()) {
            card.setPrivilege(MyCard.Privilege.XG);
        } else if (cardDetail.IsAlarmSetting()) {
            card.setPrivilege(MyCard.Privilege.FDSZ);
        } else {
            card.setPrivilege(MyCard.Privilege.NORMAL);
        }
        card.setHolidayUse(cardDetail.HolidayUse);
        Map<String, Object> map = new HashMap<>(4);
        map.put("1", cardDetail.GetDoor(1));
        map.put("2", cardDetail.GetDoor(2));
        map.put("3", cardDetail.GetDoor(3));
        map.put("4", cardDetail.GetDoor(4));
        card.setDoorControl(map);
        return card;
    }

    @Override
    public String toString() {
        return "MyCard{" +
                "no='" + no + '\'' +
                ", password='" + password + '\'' +
                ", expiry=" + expiry.getTime() +
                ", status=" + status +
                ", openTimes=" + openTimes +
                ", privilege=" + privilege +
                ", HolidayUse=" + HolidayUse +
                ", doorControl=" + doorControl +
                '}';
    }

    /**
     * 门禁卡的状态，只支持以下3种状态
     */
    public enum Status {
        NORMAL(0, "正常"),
        LOST(1, "挂失"),
        BLACKLIST(2, "黑名单");

        private int value;
        private String text;


        Status(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        public static Status getStatusByValue(int value) {
            switch (value) {
                case 0:
                    return NORMAL;
                case 1:
                    return LOST;
                case 2:
                    return BLACKLIST;
                default:
                    return null;
            }
        }
    }

    /**
     * 门禁卡的类型
     */
    public enum Privilege {
        NORMAL(0, "无"),
        SKTQ(1, "首卡特权卡"),
        CKTQ(2, "常开特权卡"),
        XG(3, "巡更卡"),
        FDSZ(4, "防盗设置卡");

        private int value;
        private String text;

        Privilege(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }
}
