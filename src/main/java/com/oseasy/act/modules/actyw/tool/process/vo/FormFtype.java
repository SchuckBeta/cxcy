package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 表单功能类型.
 * @author chenhao
 *
 */
public enum FormFtype {
    GNODE("0", "审核", "审核节点表单，需要在流程设计时配置"), START("10", "申请", "申请表单，如果流程设计时没有申请节点，则申请表单类型为该类型，否则为FF_GNODE"), QUERY("20", "查询", "流程审核页面"), VIEW("30", "查看", "流程查看页面"), TIMELINE("40", "时间轴", "流程时间轴页面"), WELCOME("90", "统计", "流程欢迎统计页");

  private String key;
  private String name;
  private String remark;

  private FormFtype(String key, String name, String remark) {
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
  public static FormFtype getByKey(String key) {
    if ((key != null)) {
      FormFtype[] entitys = FormFtype.values();
      for (FormFtype entity : entitys) {
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
  public static Boolean checkHas(List<FormFtype> fstypes, String key) {
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
  public static FormFtype checkRet(List<FormFtype> fstypes, String key) {
      for (FormFtype fstype : fstypes) {
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
