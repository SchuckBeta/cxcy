package com.oseasy.act.modules.actyw.entity;

/**
 * 状态节点对象.
 * @author chenhao
 * @version 2017-05-23
 */
public class ActYwGnodeView extends ActYwGnode {
  private static final long serialVersionUID = 1L;
  private Boolean isOver; // 是否执行完成:0、默认（否）；1、是

  public ActYwGnodeView() {
    super();
    this.isOver = false;
  }

  public ActYwGnodeView(Boolean isOver) {
    super();
    this.isOver = isOver;
  }

  public ActYwGnodeView(String id) {
    super(id);
  }

  public Boolean getIsOver() {
    return isOver;
  }

  public void setIsOver(Boolean isOver) {
    this.isOver = isOver;
  }

  /**
   * 转换ActYwGnode对象为ActYwGnodeView对象
   * @param gnodeView 状态节点对象
   * @param gnode 节点对象
   * @return ActYwGnodeView
   */
  public static ActYwGnodeView convert(ActYwGnodeView gnodeView, ActYwGnode gnode) {
//    gnodeView.setChildGnodes(gnode.getChildGnodes());
    gnodeView.setCreateBy(gnode.getCreateBy());
    gnodeView.setCreateDate(gnode.getCreateDate());
    gnodeView.setCurrentUser(gnode.getCurrentUser());
    gnodeView.setDelFlag(gnode.getDelFlag());
//    gnodeView.setFlowGroup(gnode.getFlowGroup());
//    gnodeView.setForm(gnode.getForm());
//    gnodeView.setFormId(gnode.getFormId());
    gnodeView.setGroup(gnode.getGroup());
//    gnodeView.setGroupId(gnode.getGroupId());
    gnodeView.setId(gnode.getId());
    gnodeView.setIsForm(gnode.getIsForm());
    gnodeView.setIsNewRecord(gnode.getIsNewRecord());
    gnodeView.setIsShow(gnode.getIsShow());
    gnodeView.setName(gnode.getName());
//    gnodeView.setNextFunGnode(gnode.getNextFunGnode());
//    gnodeView.setNextFunGnodes(gnode.getNextFunGnodes());
//    gnodeView.setNextFunId(gnode.getNextFunId());
//    gnodeView.setNextGnode(gnode.getNextGnode());
//    gnodeView.setNextGnodes(gnode.getNextGnodes());
//    gnodeView.setNextId(gnode.getNextId());
//    gnodeView.setNextIds(gnode.getNextIds());
//    gnodeView.setNextIdss(gnode.getNextIdss());
    gnodeView.setNode(gnode.getNode());
//    gnodeView.setNodeId(gnode.getNodeId());
//    gnodeView.setOffice(gnode.getOffice());
    gnodeView.setPage(gnode.getPage());
    gnodeView.setParent(gnode.getParent());
    gnodeView.setParentIds(gnode.getParentIds());
//    gnodeView.setPreFunGnode(gnode.getPreFunGnode());
//    gnodeView.setPreFunGnodes(gnode.getPreFunGnodes());
//    gnodeView.setPreFunId(gnode.getPreFunId());
//    gnodeView.setPreGnodes(gnode.getPreGnodes());
//    gnodeView.setPreId(gnode.getPreId());
//    gnodeView.setPreIds(gnode.getPreIds());
//    gnodeView.setPreIdss(gnode.getPreIdss());
//    gnodeView.setProcessGnode(gnode.getProcessGnode());
    gnodeView.setRemarks(gnode.getRemarks());
//    gnodeView.setSort(gnode.getSort());
    gnodeView.setSqlMap(gnode.getSqlMap());
    gnodeView.setType(gnode.getType());
//    gnodeView.setTypefun(gnode.getTypefun());
    gnodeView.setUpdateBy(gnode.getUpdateBy());
    gnodeView.setUpdateDate(gnode.getUpdateDate());
    return gnodeView;
  }
}