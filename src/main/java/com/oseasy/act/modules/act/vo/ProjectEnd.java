/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.act.vo
 * @Description [[_ProjectEnd_]]文件
 * @date 2017年6月8日 下午9:03:52
 *
 */

package com.oseasy.act.modules.act.vo;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目开始结束类型枚举.
 *
 * @author chenhao
 * @date 2017年6月8日 下午9:03:52
 *
 */
public enum ProjectEnd {
  PE_GC_8("gc", "8", "国创项目结束8"), PE_GC_9("gc", "9", "国创项目结束9"),

  PE_DS_0("ds", "0", "大赛项目开始0"), PE_DS_7("ds", "7", "大赛项目结束7"), PE_DS_8("ds", "8",
      "大赛项目结束8"), PE_DS_9("ds", "9", "大赛项目结束9");

  private String key;
  private String val;
  private String remark;

  private ProjectEnd(String key, String val, String remark) {
    this.key = key;
    this.val = val;
    this.remark = remark;
  }

  /**
   * 根据等级获取枚举 .
   *
   * @author chenhao
   * @param key
   *          关键字
   * @param val
   *          等级
   * @return ProjectEnd
   */
  public static ProjectEnd getByKeyVal(String key, String val) {
    if (StringUtil.isEmpty(key) || StringUtil.isEmpty(val)) {
      return null;
    }

    ProjectEnd[] entitys = ProjectEnd.values();
    for (ProjectEnd entity : entitys) {
      if ((key).equals(entity.getKey()) && (val).equals(entity.getVal())) {
        return entity;
      }
    }
    return null;
  }

  public String getKey() {
    return key;
  }

  public String getVal() {
    return val;
  }

  public String getRemark() {
    return remark;
  }
}
