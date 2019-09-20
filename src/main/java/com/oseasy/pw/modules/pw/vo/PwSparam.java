/**
 * .
 */

package com.oseasy.pw.modules.pw.vo;

import com.alibaba.druid.sql.visitor.functions.If;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpace;

/**
 * 场地分配参数.
 * @author chenhao
 *
 */
public class PwSparam {
    private String eid;//入驻ID
    private Integer num;//容纳人数/工位数
    private Boolean isAlone;//是否单独团队使用房间

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Boolean getIsAlone() {
        if(this.isAlone == null){
            this.isAlone = false;
        }
        return isAlone;
    }

    public void setIsAlone(Boolean isAlone) {
        this.isAlone = isAlone;
    }


    /**
     * 转换参数对象为PwSpace对象.
     */
    public static PwSpace convertPwspace(PwSparam pwSparam) {
        PwSpace pwSpace = new PwSpace();
        pwSpace.setSparam(pwSparam);
        return pwSpace;
    }

    /**
     * 转换参数对象为PwSpace对象.
     */
    public static PwEnterRoom convertPeroom(PwSparam pwSparam) {
        PwEnterRoom pwEnterRoom = new PwEnterRoom();
        pwEnterRoom.setPwEnter(new PwEnter(pwSparam.getEid()));
        if(pwEnterRoom.getPwRoom() == null){
            pwEnterRoom.setPwRoom(new PwRoom());
        }
        return pwEnterRoom;
    }
}
