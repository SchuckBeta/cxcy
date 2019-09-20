/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;

public class GnodeGstree implements Serializable{
    private static final long serialVersionUID = 1L;

    private ActYwGnode gnode;
    private List<GnodeGstree> nexts;

    public GnodeGstree() {
        super();
        this.nexts = Lists.newArrayList();
    }

    public GnodeGstree(ActYwGnode gnode) {
        super();
        this.gnode = gnode;
        this.nexts = Lists.newArrayList();
    }

    public GnodeGstree(List<GnodeGstree> nexts) {
        super();
        this.nexts = nexts;
    }

    public GnodeGstree(ActYwGnode gnode, List<GnodeGstree> nexts) {
        super();
        this.gnode = gnode;
        this.nexts = nexts;
    }

    public GnodeGstree(GnodeGstree gstree) {
        super();
        this.gnode = gstree.getGnode();
        this.nexts = gstree.getNexts();
    }

    public ActYwGnode getGnode() {
        return gnode;
    }

    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }

    public List<GnodeGstree> getNexts() {
        return nexts;
    }

    public void setNexts(List<GnodeGstree> nexts) {
        this.nexts = nexts;
    }
}
