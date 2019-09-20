/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;

public class GnodeGwmap {
    private ActYwGnode key;
    private List<ActYwGnode> sortList;

    public GnodeGwmap() {
        super();
    }

    public GnodeGwmap(ActYwGnode key) {
        super();
        this.key = key;
    }

    public GnodeGwmap(List<ActYwGnode> sortList) {
        super();
        this.sortList = sortList;
    }

    public GnodeGwmap(ActYwGnode key, List<ActYwGnode> sortList) {
        super();
        this.key = key;
        this.sortList = sortList;
    }

    public ActYwGnode getKey() {
        return key;
    }

    public void setKey(ActYwGnode key) {
        this.key = key;
    }

    public List<ActYwGnode> getSortList() {
        return sortList;
    }

    public void setSortList(List<ActYwGnode> sortList) {
        this.sortList = sortList;
    }
}
