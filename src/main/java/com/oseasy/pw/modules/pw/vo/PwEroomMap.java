/**
 * .
 */

package com.oseasy.pw.modules.pw.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.pw.modules.pw.entity.PwEnterRoom;

/**
 * .
 * @author chenhao
 *
 */
public class PwEroomMap {
    private List<String> ids;
    private List<PwEnterRoom> erooms;

    public PwEroomMap() {
        super();
        this.ids = Lists.newArrayList();
    }

    public PwEroomMap(List<String> ids, List<PwEnterRoom> erooms) {
        super();
        this.ids = ids;
        this.erooms = erooms;
    }
    public List<String> getIds() {
        return ids;
    }
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
    public List<PwEnterRoom> getErooms() {
        return erooms;
    }
    public void setErooms(List<PwEnterRoom> erooms) {
        this.erooms = erooms;
    }
}
