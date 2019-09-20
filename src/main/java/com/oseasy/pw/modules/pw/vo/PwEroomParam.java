/**
 * .
 */

package com.oseasy.pw.modules.pw.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;

/**
 * 房间分配参数.
 * @author chenhao
 *
 */
public class PwEroomParam {
    private String id;
    private List<PwEroom> erooms;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<PwEroom> getErooms() {
        return erooms;
    }
    public void setErooms(List<PwEroom> erooms) {
        this.erooms = erooms;
    }

    public static PwEroomParam converts(String eid, List<PwEnterRoom> erooms) {
        PwEroomParam eparam = new PwEroomParam();
        eparam.setErooms(Lists.newArrayList());
        eparam.setId(eid);
        for (PwEnterRoom pwEnterRoom : erooms) {
            eparam.getErooms().add(new PwEroom(pwEnterRoom.getPwRoom().getId(), pwEnterRoom.getNum()));
        }
        return eparam;
    }
}
