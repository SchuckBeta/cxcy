package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

public enum FormClientType {
  FST_FRONT("1", "前台"), FST_ADMIN("2", "后台");

  private String key;
  private String name;


  private FormClientType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return FormClientType
   */
  public static FormClientType getByKey(String key) {
    if ((key != null)) {
      FormClientType[] entitys = FormClientType.values();
      for (FormClientType entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
      }
    }
    return null;
  }

  /**
   * 检查是否存在 .
   * @author chenhao
   * @param key 枚举标识
   * @return FormClientType
   */
  public static Boolean checkHas(List<FormClientType> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return FormClientType
   */
  public static FormClientType checkRet(List<FormClientType> fstypes, String key) {
      for (FormClientType fstype : fstypes) {
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
