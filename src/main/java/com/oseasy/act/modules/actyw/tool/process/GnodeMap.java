/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;

/**
 * .
 * @author chenhao
 *
 */
public class GnodeMap {
    private Boolean isend;
    private ActYwGnode gend;
    private ActYwGnode gnode;
    private List<ActYwGnode> links;
    private List<ActYwGnode> fails;
    private List<ActYwGnode> successs;
    private List<ActYwGnode> templinks;//网关子临时链

    public GnodeMap() {
        super();
        this.isend = false;
        this.links = Lists.newArrayList();
        this.templinks = Lists.newArrayList();
    }

    public GnodeMap(ActYwGnode gnode) {
        super();
        this.gnode = gnode;
        this.isend = false;
        this.links = Lists.newArrayList();
        this.templinks = Lists.newArrayList();
    }

    public GnodeMap(List<ActYwGnode> successs, List<ActYwGnode> fails, ActYwGnode gnode, ActYwGnode gend,
            List<ActYwGnode> links, Boolean isend) {
        super();
        this.successs = successs;
        this.fails = fails;
        this.gnode = gnode;
        this.gend = gend;
        this.links = links;
        this.isend = isend;
    }
    public List<ActYwGnode> getSuccesss() {
        return successs;
    }
    public void setSuccesss(List<ActYwGnode> successs) {
        this.successs = successs;
    }
    public List<ActYwGnode> getFails() {
        return fails;
    }
    public void setFails(List<ActYwGnode> fails) {
        this.fails = fails;
    }
    public ActYwGnode getGnode() {
        return gnode;
    }
    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }
    public ActYwGnode getGend() {
        return gend;
    }
    public void setGend(ActYwGnode gend) {
        this.gend = gend;
    }
    public List<ActYwGnode> getLinks() {
        return links;
    }
    public void setLinks(List<ActYwGnode> links) {
        this.links = links;
    }
    public List<ActYwGnode> getTemplinks() {
        return templinks;
    }
    public void setTemplinks(List<ActYwGnode> templinks) {
        this.templinks = templinks;
    }
    public Boolean getIsend() {
        return isend;
    }
    public void setIsend(Boolean isend) {
        this.isend = isend;
    }
}
