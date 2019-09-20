package com.oseasy.pro.modules.promodel.vo;

import java.util.List;

/**
 * 模板类型.
 * @author chenhao
 *
 */
public enum TplType {
  MR("0", "自定义流程"), GJ("1", "国家模板");
    public static final String TPL_TYPE = "tplType";
    public static final String TPL_TYPES = "tplTypes";

  private String key;
  private String name;

  private TplType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TplType
   */
  public static TplType getByKey(String key) {
    if ((key != null)) {
      TplType[] entitys = TplType.values();
      for (TplType entity : entitys) {
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
   * @return TplType
   */
  public static Boolean checkHas(List<TplType> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return TplType
   */
  public static TplType checkRet(List<TplType> fstypes, String key) {
      for (TplType fstype : fstypes) {
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
