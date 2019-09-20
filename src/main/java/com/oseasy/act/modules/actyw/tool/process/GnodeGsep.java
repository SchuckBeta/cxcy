/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;

public class GnodeGsep {
    private ActYwGnode start;
    private ActYwGnode end;
    private List<ActYwGnode> sortList;

    public GnodeGsep() {
        super();
    }

    public GnodeGsep(ActYwGnode end) {
        super();
        this.end = end;
    }

    public GnodeGsep(List<ActYwGnode> sortList) {
        super();
        this.sortList = sortList;
    }

    public GnodeGsep(ActYwGnode end, List<ActYwGnode> sortList) {
        super();
        this.end = end;
        this.sortList = sortList;
    }

    public GnodeGsep(ActYwGnode start, ActYwGnode end, List<ActYwGnode> sortList) {
        super();
        this.start = start;
        this.end = end;
        this.sortList = sortList;
    }

    public ActYwGnode getStart() {
        return start;
    }

    public void setStart(ActYwGnode start) {
        this.start = start;
    }

    public ActYwGnode getEnd() {
        return end;
    }

    public void setEnd(ActYwGnode end) {
        this.end = end;
    }

    public List<ActYwGnode> getSortList() {
        return sortList;
    }

    public void setSortList(List<ActYwGnode> sortList) {
        this.sortList = sortList;
    }
}
