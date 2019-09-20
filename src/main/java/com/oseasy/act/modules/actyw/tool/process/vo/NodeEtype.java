/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 节点操作元素类型.
 * @author chenhao
 */
public enum NodeEtype {
    NET_DPMND("0", false,"Diagram", "根节点"),
    NET_START("10", true,"开始", "开始(E)"),
    NET_END("20", true, "结束", "结束(E)"),
    NET_TASK("30", true, "任务", "任务(E)"),
    NET_PROCESS("40", true, "子流程", "结构(E)"),
    NET_GATEWAY("50", true, " 网关", " 网关(G)"),
    NET_FLOW("60", true, "连接", "连接"),
    NET_E_CATCH("70", false, "捕捉事件", "捕捉(E)"),
    NET_E_THROW("80", false, "抛出事件", "抛出(E)"),
    NET_E_BOUNDARY("90", false, "边界事件", "边界(E)"),
    NET_PLUG("95", false, "组件", "组件"),
    NET_PL("98", false, "泳道", "泳道");

    private String id;//标识
    private String name;//名称
    private Boolean isShow;//是否显示
    private String remark;//说明

    private NodeEtype(String id, Boolean isShow, String name, String remark) {
      this.id = id;
      this.isShow = isShow;
      this.name = name;
      this.remark = remark;
    }

    /**
     * 根据id获取NodeEtype .
     * @author chenhao
     * @param id id惟一标识
     * @return NodeEtype
     */
    public static NodeEtype getById(String id) {
      NodeEtype[] entitys = NodeEtype.values();
      for (NodeEtype entity : entitys) {
        if ((id).equals(entity.getId())) {
          return entity;
        }
      }
      return null;
    }

    /**
     * 查询所有节点元素.
     * @param isShow 是否显示，为null时查询所有
     * @return List
     */
    public static List<NodeEtype> getAll(Boolean isShow) {
        NodeEtype[] entitys = NodeEtype.values();
        if (isShow == null) {
            return Arrays.asList(entitys);
        }else{
            List<NodeEtype> nodeetypes = Lists.newArrayList();
            for (NodeEtype entity : entitys) {
                if ((isShow).equals(entity.getIsShow())) {
                  nodeetypes.add(entity);
                }
            }
            return nodeetypes;
        }
    }
    public static List<NodeEtype> getAll() {
        return getAll(null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @SuppressWarnings("unused")
    @Override
    public String toString() {
        if(this != null){
            return "{\"id\":\"" + this.id + "\",\"name\":\"" + this.name + "\",\"isShow\":" + this.isShow + ",\"remark\":\"" + this.remark + "\"}";
        }
        return super.toString();
    }
}
