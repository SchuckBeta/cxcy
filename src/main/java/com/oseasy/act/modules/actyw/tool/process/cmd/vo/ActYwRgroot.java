/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.cmd.vo
 * @Description [[_ActYwRgroot_]]文件
 * @date 2017年6月18日 下午4:36:32
 *
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwRtpl;
import com.oseasy.com.common.config.ApiTstatus;

/**
 * 业务流程根节点执行结果.
 * @author chenhao
 * @date 2017年6月18日 下午4:36:32
 *
 */
public class ActYwRgroot extends ApiTstatus<ActYwGnode> implements ActYwRtpl {
  private ActYwGnode startGnode;
  private ActYwGnode endGnode;
  private ActYwGnode sflowGnode;

  public ActYwRgroot() {
    super();
  }

  public ActYwRgroot(ActYwGnode startGnode, ActYwGnode endGnode, ActYwGnode sflowGnode) {
    super();
    this.startGnode = startGnode;
    this.endGnode = endGnode;
    this.sflowGnode = sflowGnode;
  }

  public ActYwGnode getStartGnode() {
    return startGnode;
  }
  public void setStartGnode(ActYwGnode startGnode) {
    this.startGnode = startGnode;
  }
  public ActYwGnode getEndGnode() {
    return endGnode;
  }
  public void setEndGnode(ActYwGnode endGnode) {
    this.endGnode = endGnode;
  }
  public ActYwGnode getSflowGnode() {
    return sflowGnode;
  }
  public void setSflowGnode(ActYwGnode sflowGnode) {
    this.sflowGnode = sflowGnode;
  }

  /**
   * 获取排序列表.
   * @author chenhao
   * @param startGnode 开始节点
   * @param sflowGnode 序列线
   * @param endGnode 结束节点
   * @return List
   */
  public static List<ActYwGnode> toSortList(ActYwGnode startGnode, ActYwGnode sflowGnode, ActYwGnode endGnode) {
    List<ActYwGnode> gnodes = Lists.newArrayList();
    gnodes.add(startGnode);
    gnodes.add(sflowGnode);
    gnodes.add(startGnode);
    return gnodes;
  }
}
