package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;

/**
 * IAurl相关地址的请求传递参数类型.
 * @author chenhao
 *
 */
public enum FlowAuparam {
    ALL("0", "", "所有参数"),
    DEFAULT("0", "", "默认，不需要排序"),
    LIST_AUDIT("0", "", "审核列表，需要排序，不需要角色控制"),

    ADM_R("10", "", "UserUtils.isAdm()"),
    ADM_RSUP("10", "", "UserUtils.isSuper()"),
    ADM_RSYS("10", "", "UserUtils.isAdminSys()"),
    ADM_ROFFICE("10", "", "UserUtils.isNscAdminOffice()"),
    APPLY("40", "获取申请请求", "申请"),
    ACTYW("40", "获取申请请求", "项目流程"),
    GNODE("10", "", "节点"),
    GROUP("10", "", "流程"),
    AURL("10", "", "审核地址"),
    CONFIG("10", "", "配置"),
    ORBY("10", "", "排序"),
    FORM_ID("10", "", "表单ID")
    ;

  private String key;
  private String name;
  private String remark;

  private FlowAuparam(String key, String name, String remark) {
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
  public static FlowAuparam getByKey(String key) {
    if ((key != null)) {
      FlowAuparam[] entitys = FlowAuparam.values();
      for (FlowAuparam entity : entitys) {
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
  public static Boolean checkHas(List<FlowAuparam> fstypes, String key) {
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
  public static FlowAuparam checkRet(List<FlowAuparam> fstypes, String key) {
      for (FlowAuparam fstype : fstypes) {
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
