/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_GnodeType_]]文件
 * @date 2017年6月27日 下午4:02:08
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 业务流程节点类型.
 * @author chenhao
 * @date 2017年6月27日 下午4:02:08
 *
 */
public enum GnodeType {
  GT_ROOT("000", NodeEtype.NET_DPMND, RtSvl.RtLevelVal.RT_LV0, "根节点"),
  GT_ROOT_START("110", NodeEtype.NET_START, RtSvl.RtLevelVal.RT_LV1, "根节点开始"),
  GT_ROOT_FLOW("120", NodeEtype.NET_FLOW, RtSvl.RtLevelVal.RT_LV1, "根节点线"),
  GT_ROOT_END("130", NodeEtype.NET_END, RtSvl.RtLevelVal.RT_LV1, "根节点结束"),
  GT_ROOT_GATEWAY("140", NodeEtype.NET_GATEWAY, RtSvl.RtLevelVal.RT_LV1, "根节点网关"),
  GT_ROOT_TASK("150", NodeEtype.NET_TASK, RtSvl.RtLevelVal.RT_LV1, "根节点业务节点"),
  GT_PROCESS("190", NodeEtype.NET_PROCESS, RtSvl.RtLevelVal.RT_LV1, "子流程"),
  GT_PROCESS_START("210", NodeEtype.NET_START, RtSvl.RtLevelVal.RT_LV2, "子流程开始"),
  GT_PROCESS_FLOW("220", NodeEtype.NET_FLOW, RtSvl.RtLevelVal.RT_LV2, "子流程线"),
  GT_PROCESS_END("230", NodeEtype.NET_END, RtSvl.RtLevelVal.RT_LV2, "子流程结束"),
  GT_PROCESS_GATEWAY("240", NodeEtype.NET_GATEWAY, RtSvl.RtLevelVal.RT_LV2, "子流程网关"),
  GT_PROCESS_TASK("250", NodeEtype.NET_TASK, RtSvl.RtLevelVal.RT_LV2, "子流程业务节点");

  private String id;
  private String level;
  private NodeEtype ntype;
  private String remark;

  private GnodeType(String id, NodeEtype ntype, String level, String remark) {
    this.id = id;
    this.level = level;
    this.ntype = ntype;
    this.remark = remark;
  }

  /**
   * 根据id获取GnodeType .
   * @author chenhao
   * @param id id惟一标识
   * @return GnodeType
   */
  public static GnodeType getById(String id) {
    GnodeType[] entitys = GnodeType.values();
    for (GnodeType entity : entitys) {
      if ((id).equals(entity.getId())) {
        return entity;
      }
    }
    return null;
  }

  /**
   * 根据节点获取命令类型.
   * @param gnode
   * @return GnodeType
   */
  public static GnodeType getByGnode(ActYwGnode gnode) {
    if ((gnode.getNode() == null) || StringUtil.isEmpty(gnode.getParentIds())) {
      return null;
    }

    String level = null;
    if((CoreIds.treePpids()).equals(gnode.getParentIds())){
        level = RtSvl.RtLevelVal.RT_LV0;
    }else if((CoreIds.treeRpids()).equals(gnode.getParentIds())){
        level = RtSvl.RtLevelVal.RT_LV1;
    }else{
        level = RtSvl.RtLevelVal.RT_LV2;
    }

    if(gnode.getNode() == null){
        return getByGnode(level, null);
    }
    return getByGnode(level, gnode.getNode().getType());
  }

  /**
   * 根据节点类型和级别获取命令类型.
   * @param level
   * @param netype
   * @return GnodeType
   */
  public static GnodeType getByGnode(String level, String netype) {
      if (StringUtil.isEmpty(level)) {
          return null;
      }

      if ((RtSvl.RtLevelVal.RT_LV0).equals(level)) {
          return GnodeType.GT_ROOT;
      }

      if (StringUtil.isEmpty(netype)) {
          return null;
      }

      GnodeType[] entitys = GnodeType.values();
      for (GnodeType entity : entitys) {
          if(((level).equals(entity.getLevel())) && (entity.getNtype().getId()).equals(netype)){
              return entity;
          }
      }
      return null;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRemark() {
    return remark;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

    public NodeEtype getNtype() {
        return ntype;
    }

    public void setNtype(NodeEtype ntype) {
        this.ntype = ntype;
    }

    /**
     * 必填校验  流程 group.
     */
    public static String getIdByGroupId() {
      return GnodeType.GT_ROOT_START.getId() + StringUtil.DOTH +
              GnodeType.GT_ROOT_END.getId() + StringUtil.DOTH +
              GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
              GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
              GnodeType.GT_ROOT_FLOW.getId() + StringUtil.DOTH +
              GnodeType.GT_PROCESS_START.getId() + StringUtil.DOTH +
              GnodeType.GT_PROCESS_END.getId() + StringUtil.DOTH +
              GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
              GnodeType.GT_PROCESS_FLOW.getId() + StringUtil.DOTH +
              GnodeType.GT_PROCESS_TASK.getId() + StringUtil.DOTH +
              GnodeType.GT_PROCESS.getId();
    }

    /**
     * 必填校验 前置节点preGnode.
     */
    public static String getIdByPre() {
        return GnodeType.GT_ROOT_END.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_FLOW.getId() + StringUtil.DOTH +
//                GnodeType.GT_PROCESS_START.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_END.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_FLOW.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId();
    }

    /**
     * 必填校验 前置业务节点preFunGnode.
     */
    public static String getIdByPreFun() {
        return GnodeType.GT_ROOT_END.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_FLOW.getId() + StringUtil.DOTH +
//                GnodeType.GT_PROCESS_START.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_END.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_FLOW.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId();
    }


    /**
     * 必填校验 节点名称name.
     */
    public static String getIdByName() {
        return
//                GnodeType.GT_ROOT_FLOW.getId() + StringUtil.DOTH +
//              GnodeType.GT_PROCESS_FLOW.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId();
    }

    /**
     * 必填校验  显示 isShow.
     */
    public static String getIdByShow() {
        return GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId();
    }

    /**
     * 必填校验  图标 iconUrl.
     */
    public static String getIdByIconUrl() {
        return GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId();
    }

    /**
     * 必填校验 执行任务类型taskType.
     */
    public static String getIdByTask() {
        return
//                GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
//                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId();
    }

    /**
     * 必填校验 节点状态 gstatuss.
     */
    public static String getIdByGstatuss() {
        return GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_FLOW.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_FLOW.getId();
    }

    /**
     * 必填校验 角色 groles.
     */
    public static String getIdByRoles() {
        return GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId();
    }

    /**
     * 必填校验 用户 gusers.
     */
    public static String getIdByUsers() {
        return GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId();
    }

    /**
     * 必填校验 表单 gforms.
     */
    public static String getIdByForms() {
        return GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId();
    }

    /**
     * 必填校验 表单 gforms中必须含有列表表单.
     */
    public static String getIdByListForms() {
        return GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId();
    }

    /**
     * 必填校验 表单 gforms中必须含有非列表表单.
     */
    public static String getIdByNlistForms() {
        return GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId();
    }

    /**
     * 校验节点类型为ROOT.
     */
    public static String getIdByRoot() {
        return GnodeType.GT_ROOT_START.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_END.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_TASK.getId() + StringUtil.DOTH +
                GnodeType.GT_ROOT_FLOW.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS.getId();
    }

    /**
     * 校验节点类型为Process子节点.
     */
    public static String getIdByProcess() {
        return GnodeType.GT_PROCESS_START.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_END.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_FLOW.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_TASK.getId();
    }


    /**
     * 必填校验 删除状态 delFlag.
     */
    public static String getIdByDelFlag() {
        return GnodeType.GT_ROOT.getId();
    }

    /**
     * 校验 是否为网关节点GATEWAY.
     */
    public static String getIdByGateway() {
        return GnodeType.GT_ROOT_GATEWAY.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_GATEWAY.getId();
    }

    /**
     * 校验 是否为开始节点START.
     */
    public static String getIdByStart() {
        return GnodeType.GT_ROOT_START.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_START.getId();
    }

    /**
     * 校验 是否为结束节点END.
     */
    public static String getIdByEnd() {
        return GnodeType.GT_ROOT_END.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_END.getId();
    }

    /**
     * 校验 是否为线节点FLOW.
     */
    public static String getIdByFlow() {
        return GnodeType.GT_ROOT_FLOW.getId() + StringUtil.DOTH +
                GnodeType.GT_PROCESS_FLOW.getId();
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"level\":\"" + this.level + "\",\"ntype\":" + this.ntype + ",\"remark\":\"" + this.remark + "\"}";
    }
}
