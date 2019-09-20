package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

/**
 * 表单排版形式.
 * @author chenhao
 *
 */
public enum FormStyleType {
  FST_LIST("1", "列表"), FST_FORM("2", "数据录入"), FST_VIEW("3", "数据查看");

  private String key;
  private String name;

  private FormStyleType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return FormStyleType
   */
  public static FormStyleType getByKey(String key) {
    if ((key != null)) {
      FormStyleType[] entitys = FormStyleType.values();
      for (FormStyleType entity : entitys) {
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
   * @return FormStyleType
   */
  public static Boolean checkHas(List<FormStyleType> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return FormStyleType
   */
  public static FormStyleType checkRet(List<FormStyleType> fstypes, String key) {
      for (FormStyleType fstype : fstypes) {
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

  public void setKey(String key) {
    this.key = key;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
  }
}
