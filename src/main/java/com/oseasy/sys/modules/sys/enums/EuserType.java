package com.oseasy.sys.modules.sys.enums;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户类型枚举.
 * level = 1 表示校级
 * level = 2 表示院级
 * type用户类型，对应User的userType
 * @author chenhao
 */
public enum EuserType {
  UT_S_GLY("1", "6", "校级管理员"),
  UT_S_ZJ("1", "5", "校级专家"),
  UT_C_ZJ("2", "4", "院级专家"),
  UT_C_MS("2", "3", "院级教学秘书"),
  UT_C_TEACHER("2", "2", "院级导师"),
  UT_C_STUDENT("2", "1", "院级学生");

  private String level;
  private String type;
  private String remarks;

  private EuserType(String level, String type, String remarks) {
    this.level = level;
    this.type = type;
    this.remarks = remarks;
  }

  /**
   * 根据类型获取枚举。
   * @param type  用户类型
   * @return EuserType
   */
  public static EuserType getByType(String type) {
    if (StringUtil.isNotEmpty(type)) {
      for(EuserType e: EuserType.values()) {
        if ((e.getType()).equals(type)) {
          return e;
        }
      }
    }
    return null;
  }

  public String getLevel() {
    return level;
  }
  public void setLevel(String level) {
    this.level = level;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getRemarks() {
    return remarks;
  }
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
}
