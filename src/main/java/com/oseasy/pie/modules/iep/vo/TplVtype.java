package com.oseasy.pie.modules.iep.vo;

import java.util.List;

/**
 * 模板功能类型.
 * @author chenhao
 *
 */
public enum TplVtype {
    DEF("0", "默认"), EMAIL("10", "邮箱"), MOBILE("20", "手机"), PHONE("30", "电话");
    public static final String TPL_VTYPE = "tplVtype";
    public static final String TPL_VTYPES = "tplVtypes";

  private String key;
  private String name;

  private TplVtype(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TplVtype
   */
  public static TplVtype getByKey(String key) {
    if ((key != null)) {
      TplVtype[] entitys = TplVtype.values();
      for (TplVtype entity : entitys) {
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
   * @return TplVtype
   */
  public static Boolean checkHas(List<TplVtype> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return TplVtype
   */
  public static TplVtype checkRet(List<TplVtype> fstypes, String key) {
      for (TplVtype fstype : fstypes) {
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
