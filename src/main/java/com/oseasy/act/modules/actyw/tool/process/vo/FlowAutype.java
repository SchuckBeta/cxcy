package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * IAurl相关地址的请求参数类型.
 * @author chenhao
 *
 */
public enum FlowAutype {
    GNODE("10", "节点审核或节点审核列表请求", "申请ID、流程ID、节点ID必填"),
    FLOW("20", "需要获取流程请求", "流程ID必填"),
    FLOWTYPE("30", "需要获取流程请求", "流程ID必填 或 流程类型、项目类型必填"),
    APPLY("40", "获取申请请求", "申请ID必填"),
    AFLOW("50", "获取申请请求", "申请ID、流程ID必填")
    ;

  private String key;
  private String name;
  private String remark;

  private FlowAutype(String key, String name, String remark) {
    this.key = key;
    this.name = name;
    this.remark = remark;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return FormFtype
   */
  public static FlowAutype getByKey(String key) {
    if ((key != null)) {
      FlowAutype[] entitys = FlowAutype.values();
      for (FlowAutype entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
      }
    }
    return null;
  }

  /**
   * 检查是否存在 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return FormFtype
   */
  public static Boolean checkHas(List<FlowAutype> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return FormFtype
   */
  public static FlowAutype checkRet(List<FlowAutype> fstypes, String key) {
      for (FlowAutype fstype : fstypes) {
          if ((fstype.getKey()).equals(key)) {
              return fstype;
          }
      }
      return null;
  }

  public String getKey() {
    return key;
}

public String getName() {
    return name;
}

public String getRemark() {
    return remark;
}

@Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\",\"remark\":\"" + this.remark + "\"}";
  }
}
