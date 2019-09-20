package Net.PC15.FC8800.Command.Data;

import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Calendar;

public class CardDetail implements INData, Comparable<CardDetail> {
    public long CardData;
    public String Password;
    public Calendar Expiry;
    private byte[] TimeGroup;
    public int OpenTimes;
    private int Door;
    private int Privilege;
    public byte CardStatus;
    private byte[] Holiday;
    public boolean HolidayUse;
    public int EnterStatus;
    public Calendar RecordTime;

    public int compareTo(CardDetail o) {
        return o.CardData == this.CardData?0:(this.CardData < o.CardData?-1:(this.CardData > o.CardData?1:0));
    }

    public boolean equals(Object o) {
        return o instanceof CardDetail?this.compareTo((CardDetail)o) == 0:false;
    }

    public int GetDataLen() {
        return 33;
    }

    public void SetBytes(ByteBuf data) {
        data.readByte();
        this.CardData = data.readUnsignedInt();
        byte[] btData = new byte[4];
        data.readBytes(btData, 0, 4);
        this.Password = ByteUtil.ByteToHex(btData);
        byte[] btTime = new byte[6];
        data.readBytes(btTime, 0, 5);
        this.Expiry = TimeUtil.BCDTimeToDate_yyMMddhhmm(btTime);
        data.readBytes(btData, 0, 4);

        int bData;
        for(bData = 0; bData < 4; ++bData) {
            this.TimeGroup[bData] = btData[bData];
        }

        this.OpenTimes = data.readUnsignedShort();
        short var6 = data.readUnsignedByte();
        this.Door = var6 >> 4;
        bData = var6 & 15;
        this.Privilege = bData & 7;
        this.HolidayUse = (bData & 8) == 8;
        this.CardStatus = data.readByte();
        data.readBytes(btData, 0, 4);

        for(int i = 0; i < 4; ++i) {
            this.Holiday[i] = btData[i];
        }

        this.EnterStatus = data.readByte();
        data.readBytes(btTime, 0, 6);
        this.RecordTime = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
    }

    public ByteBuf GetBytes() {
        return null;
    }

    public void GetBytes(ByteBuf data) {
        data.writeByte(0);
        data.writeInt((int)this.CardData);
        this.Password = StringUtil.FillHexString(this.Password, 8, "F", true);
        long pwd = Long.parseLong(this.Password, 16);
        data.writeInt((int)pwd);
        byte[] btTime = new byte[6];
        TimeUtil.DateToBCD_yyMMddhhmm(btTime, this.Expiry);
        data.writeBytes(btTime, 0, 5);
        data.writeBytes(this.TimeGroup, 0, 4);
        data.writeShort(this.OpenTimes);
        int bData = (this.Door << 4) + this.Privilege;
        if(this.HolidayUse) {
            bData |= 8;
        }

        data.writeByte(bData);
        data.writeByte(this.CardStatus);
        data.writeBytes(this.Holiday, 0, 4);
        data.writeByte(this.EnterStatus);
        TimeUtil.DateToBCD_yyMMddhhmmss(btTime, this.RecordTime);
        data.writeBytes(btTime, 0, 6);
    }

    public CardDetail() {
        this.CardData = 0L;
        this.Password = null;
        this.Expiry = null;
        this.TimeGroup = new byte[4];
        this.Door = 0;
        this.Privilege = 0;
        this.CardStatus = 0;
        this.Holiday = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1};
        this.RecordTime = null;
        this.EnterStatus = 0;
        this.HolidayUse = false;
    }

    public CardDetail(long data) {
        this();
        this.CardData = data;
    }

    public int GetTimeGroup(int iDoor) {
        if(iDoor >= 0 && iDoor <= 4) {
            return this.TimeGroup[iDoor - 1];
        } else {
            throw new IllegalArgumentException("Door 1-4");
        }
    }

    public void SetTimeGroup(int iDoor, int iNum) {
        if(iDoor >= 0 && iDoor <= 4) {
            if(iNum >= 0 && iNum <= 64) {
                this.TimeGroup[iDoor - 1] = (byte)iNum;
            } else {
                throw new IllegalArgumentException("Num 1-64");
            }
        } else {
            throw new IllegalArgumentException("Door 1-4");
        }
    }

    public boolean GetDoor(int iDoor) {
        if(iDoor >= 0 && iDoor <= 4) {
            --iDoor;
            int iBitIndex = iDoor % 8;
            int iMaskValue = (int)Math.pow(2.0D, (double)iBitIndex);
            int iByteValue = this.Door & iMaskValue;
            if(iBitIndex > 0) {
                iByteValue >>= iBitIndex;
            }

            return iByteValue == 1;
        } else {
            throw new IllegalArgumentException("Door 1-4");
        }
    }

    public void SetDoor(int iDoor, boolean bUse) {
        if(iDoor >= 0 && iDoor <= 4) {
            if(bUse != this.GetDoor(iDoor)) {
                --iDoor;
                int iBitIndex = iDoor % 8;
                int iMaskValue = (int)Math.pow(2.0D, (double)iBitIndex);
                if(bUse) {
                    this.Door |= iMaskValue;
                } else {
                    this.Door ^= iMaskValue;
                }

            }
        } else {
            throw new IllegalArgumentException("Door 1-4");
        }
    }

    public boolean IsNormal() {
        return this.Privilege == 0;
    }

    public void SetNormal() {
        this.Privilege = 0;
    }

    public boolean IsPrivilege() {
        return this.Privilege == 1;
    }

    public void SetPrivilege() {
        this.Privilege = 1;
    }

    public boolean IsTiming() {
        return this.Privilege == 2;
    }

    public void SetTiming() {
        this.Privilege = 2;
    }

    public boolean IsGuardTour() {
        return this.Privilege == 3;
    }

    public void SetGuardTour() {
        this.Privilege = 3;
    }

    public boolean IsAlarmSetting() {
        return this.Privilege == 4;
    }

    public void SetAlarmSetting() {
        this.Privilege = 4;
    }

    public boolean GetHolidayValue(int iIndex) {
        if(iIndex > 0 && iIndex <= 30) {
            --iIndex;
            int iByteIndex = iIndex / 8;
            int iBitIndex = iIndex % 8;
            int iByteValue = this.Holiday[iByteIndex] & 255;
            int iMaskValue = (int)Math.pow(2.0D, (double)iBitIndex);
            iByteValue &= iMaskValue;
            if(iBitIndex > 0) {
                iByteValue >>= iBitIndex;
            }

            return iByteValue == 1;
        } else {
            throw new IllegalArgumentException("iIndex= 1 -- 32");
        }
    }

    public void SetHolidayValue(int iIndex, boolean bUse) {
        if(iIndex > 0 && iIndex <= 30) {
            if(bUse != this.GetHolidayValue(iIndex)) {
                --iIndex;
                int iByteIndex = iIndex / 8;
                int iBitIndex = iIndex % 8;
                int iByteValue = this.Holiday[iByteIndex] & 255;
                int iMaskValue = (int)Math.pow(2.0D, (double)iBitIndex);
                if(bUse) {
                    iByteValue |= iMaskValue;
                } else {
                    iByteValue ^= iMaskValue;
                }

                this.Holiday[iByteIndex] = (byte)iByteValue;
            }
        } else {
            throw new IllegalArgumentException("iIndex= 1 -- 32");
        }
    }

    public static int SearchCardDetail(ArrayList<CardDetail> list, long SearchCard) {
        CardDetail search = new CardDetail();
        search.CardData = SearchCard;
        return SearchCardDetail(list, search);
    }

    public static int SearchCardDetail(ArrayList<CardDetail> list, CardDetail search) {
        int max = list.size() - 1;
        int min = 0;

        while(min <= max) {
            int mid = max + min >> 1;
            CardDetail cd = (CardDetail)list.get(mid);
            int num = cd.compareTo(search);
            if(num > 0) {
                max = mid - 1;
            } else {
                if(num >= 0) {
                    return mid;
                }

                min = mid + 1;
            }
        }

        return -1;
    }


    @Override
    public String toString() {
        return "{\"CardData\":\"" + this.CardData + "\",\"Password\":\"" + this.Password + "\",\"Expiry\":\"" + this.Expiry + "\",\"TimeGroup\":" + this.TimeGroup
                + "\",\"OpenTimes\":" + this.OpenTimes+ "\",\"Door\":" + this.Door+ "\",\"Privilege\":" + this.Privilege+ "\",\"CardStatus\":" + this.CardStatus
                + "\",\"Holiday\":" + this.Holiday+ "\",\"HolidayUse\":" + this.HolidayUse+ "\",\"EnterStatus\":" + this.EnterStatus+ "\",\"RecordTime\":" + this.RecordTime
                + "\"}";
    }
}