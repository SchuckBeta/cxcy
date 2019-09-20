package com.oseasy.pw.modules.pw.entity;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.List;

/**
 * 房间Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwRoom extends DataEntity<PwRoom> {
    private static final long serialVersionUID = 1L;
    @Transient
    private List<String> ids;        // 设施ID(用作级联查询)
    private String name;        // 房间名
    private String person;   // 负责人
    private String phone;   // 电话
    private String mobile;    // 手机
    private String alias;        // 别名
    private String type;        // 房间类型
    private Integer num;        // 容纳人数/工位数
    private Integer numtype;        // 房间容纳类型：1：房间容纳人数 2：房间工位数
    private Integer remaindernum;//剩余工位数
    private BigDecimal area; // 房间面积（平方米，保留小数点后4位）
    private String isAllowm;        // 是否允许多团队入驻
    private String isUsable;        // 可否预约
    private String isAssign;        // 可否分配
    private String color;   // 颜色
    private PwSpace pwSpace;        // 楼层编号
    private PwEnterRoom pwEnterRoom;        // 楼层编号
    @Transient
    private String path;       //从学校到楼层整个路径
    private Integer roomordercount; //当前已预约数量
    private Integer hroomordercount; //预约总次数
    private Integer enterteamcount; //当前入驻团队数量
    private Integer querynumbegin; //查询工位数起始值
    private Integer querynumend; //查询工位数结束值
    private Integer querypeoplebegin; //查询容纳人数起始值
    private Integer querypeopleend; //查询容纳人数结束值
    private Integer queryareabegin; //查询面积起始值
    private Integer queryareaend; //查询面积结束值
    private PwAppointment pwAppointment; //预约表
    private String keys; // 查询关键字
    private Integer querystatus;
    private String isEdit; // 是否可编辑
    private int isYY; // 是否预约
    private int isFP; // 是否分配
    private int orderusing; //预约使用中
    private int ordernotuse;//预约未使用
    private int fullroom;//已分配满员
    private int notfullroom;//已分配未满员

    public int getFullroom() {
        return fullroom;
    }

    public void setFullroom(int fullroom) {
        this.fullroom = fullroom;
    }

    public int getNotfullroom() {
        return notfullroom;
    }

    public void setNotfullroom(int notfullroom) {
        this.notfullroom = notfullroom;
    }

    public int getOrderusing() {
        return orderusing;
    }

    public void setOrderusing(int orderusing) {
        this.orderusing = orderusing;
    }

    public int getOrdernotuse() {
        return ordernotuse;
    }

    public void setOrdernotuse(int ordernotuse) {
        this.ordernotuse = ordernotuse;
    }

    public int getIsYY() {
        return isYY;
    }

    public void setIsYY(int isYY) {
        this.isYY = isYY;
    }

    public int getIsFP() {
        return isFP;
    }

    public void setIsFP(int isFP) {
        this.isFP = isFP;
    }

    public Integer getQuerystatus() {
        return querystatus;
    }

    public void setQuerystatus(Integer querystatus) {
        this.querystatus = querystatus;
    }

    public Integer getQuerypeoplebegin() {
        return querypeoplebegin;
    }

    public void setQuerypeoplebegin(Integer querypeoplebegin) {
        this.querypeoplebegin = querypeoplebegin;
    }

    public Integer getQuerypeopleend() {
        return querypeopleend;
    }

    public void setQuerypeopleend(Integer querypeopleend) {
        this.querypeopleend = querypeopleend;
    }

    public Integer getQueryareabegin() {
        return queryareabegin;
    }

    public void setQueryareabegin(Integer queryareabegin) {
        this.queryareabegin = queryareabegin;
    }

    public Integer getQueryareaend() {
        return queryareaend;
    }

    public void setQueryareaend(Integer queryareaend) {
        this.queryareaend = queryareaend;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public PwRoom() {
        super();
    }

    public PwRoom(String id) {
        super(id);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getNumtype() {
        return numtype;
    }

    public void setNumtype(Integer numtype) {
        this.numtype = numtype;
    }

    public Integer getRemaindernum() {
        return remaindernum;
    }

    public void setRemaindernum(Integer remaindernum) {
        this.remaindernum = remaindernum;
    }

    @Length(min = 1, max = 100, message = "房间名长度必须介于 1 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @Length(min = 0, max = 200, message = "电话长度必须介于 0 和 200 之间")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Length(min = 0, max = 1, message = "可否预约长度必须介于 0 和 1 之间")
    public String getIsAssign() {
        return isAssign;
    }

    public void setIsAssign(String isAssign) {
        this.isAssign = isAssign;
    }

    @Length(min = 0, max = 1, message = "可否预约长度必须介于 0 和 1 之间")
    public String getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(String isUsable) {
        this.isUsable = isUsable;
    }

    public String getIsAllowm() {
        return isAllowm;
    }

    public void setIsAllowm(String isAllowm) {
        this.isAllowm = isAllowm;
    }

    public Boolean getIsFull() {
        if ((this.pwEnterRoom != null) && (this.pwEnterRoom.getCnum() != null)) {
            if (((Const.NO).equals(this.isAllowm)) && (this.pwEnterRoom.getCnum() == 1)) {
                return true;
            }
        }
        return false;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public PwSpace getPwSpace() {
        return pwSpace;
    }

    public void setPwSpace(PwSpace pwSpace) {
        this.pwSpace = pwSpace;
    }

    public PwEnterRoom getPwEnterRoom() {
        return pwEnterRoom;
    }

    public void setPwEnterRoom(PwEnterRoom pwEnterRoom) {
        this.pwEnterRoom = pwEnterRoom;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getRoomordercount() {
        return roomordercount;
    }

    public void setRoomordercount(Integer roomordercount) {
        this.roomordercount = roomordercount;
    }

    public Integer getHroomordercount() {
        return hroomordercount;
    }

    public void setHroomordercount(Integer hroomordercount) {
        this.hroomordercount = hroomordercount;
    }

    public PwAppointment getPwAppointment() {
        return pwAppointment;
    }

    public void setPwAppointment(PwAppointment pwAppointment) {
        this.pwAppointment = pwAppointment;
    }

    public Integer getEnterteamcount() {
        return enterteamcount;
    }

    public void setEnterteamcount(Integer enterteamcount) {
        this.enterteamcount = enterteamcount;
    }

    public Integer getQuerynumbegin() {
        return querynumbegin;
    }

    public void setQuerynumbegin(Integer querynumbegin) {
        this.querynumbegin = querynumbegin;
    }

    public Integer getQuerynumend() {
        return querynumend;
    }

    public void setQuerynumend(Integer querynumend) {
        this.querynumend = querynumend;
    }

    public enum Type_Enum {
        TYPE1(1, "容纳人数"),
        TYPE2(2, "工位数");
        public static Integer TYPE_VALUE1= 1;
        public static Integer TYPE_VALUE2=2;
        private Integer value;
        private String name;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private Type_Enum(Integer value, String name) {
            this.value=value;
            this.name=name;
        }

        public static PwRoom.Type_Enum getByValue(String value) {
            PwRoom.Type_Enum[] entitys = PwRoom.Type_Enum.values();
            for (PwRoom.Type_Enum entity : entitys) {
                if ((value).equals(entity.getValue())) {
                    return entity;
                }
            }
            return null;
        }
    }
}