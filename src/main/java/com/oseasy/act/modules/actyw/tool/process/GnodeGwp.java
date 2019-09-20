/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;

public class GnodeGwp {
    private List<GnodeGwmap> gwmapList;
    private List<ActYwGnode> gyList;

    public GnodeGwp() {
        super();
    }

    public GnodeGwp(List<GnodeGwmap> gwmapList, List<ActYwGnode> gyList) {
        super();
        this.gwmapList = gwmapList;
        this.gyList = gyList;
    }
    public List<GnodeGwmap> getGwmapList() {
        return gwmapList;
    }
    public void setGwmapList(List<GnodeGwmap> gwmapList) {
        this.gwmapList = gwmapList;
    }
    public List<ActYwGnode> getGyList() {
        return gyList;
    }
    public void setGyList(List<ActYwGnode> gyList) {
        this.gyList = gyList;
    }
}
