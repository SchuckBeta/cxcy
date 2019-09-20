package com.oseasy.act.modules.actyw.tool.process.rest;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.common.config.ApiTstatus;

/**
 * Created by Administrator on 2017/8/12 0012.
 */
public class GnodeMargeVo extends ApiTstatus<ActYwGnode>{
    private ActYwGroup group;
    private ActYwGnode gnode;
    private List<ActYwGnode> childs;
    private List<ActYwGnode> allGnodes;
    private List<ActYwGnode> preGnodes;
    private List<ActYwGnode> nextGnodes;

    public GnodeMargeVo() {
      super();
    }

    public GnodeMargeVo(ActYwGroup group) {
      super();
      this.group = group;
    }

    public GnodeMargeVo(ActYwGroup group, Boolean status, String msg) {
      super(status, msg);
      this.group = group;
    }

    public GnodeMargeVo(Boolean status, String msg) {
      super(status, msg);
      // TODO Auto-generated constructor stub
    }

    public ActYwGnode getGnode() {
        return gnode;
    }

    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }

    public List<ActYwGnode> getAllGnodes() {
        return allGnodes;
    }

    public void setAllGnodes(List<ActYwGnode> allGnodes) {
        this.allGnodes = allGnodes;
    }

    public List<ActYwGnode> getPreGnodes() {
      return preGnodes;
    }

    public void setPreGnodes(List<ActYwGnode> preGnodes) {
      this.preGnodes = preGnodes;
    }

    public List<ActYwGnode> getNextGnodes() {
      return nextGnodes;
    }

    public void setNextGnodes(List<ActYwGnode> nextGnodes) {
      this.nextGnodes = nextGnodes;
    }

    public ActYwGroup getGroup() {
      return group;
    }

    public List<ActYwGnode> getChilds() {
      return childs;
    }

    public void setChilds(List<ActYwGnode> childs) {
      this.childs = childs;
    }

    public void setGroup(ActYwGroup group) {
      this.group = group;
    }
}
