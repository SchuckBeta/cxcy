package com.oseasy.pie.modules.iep.vo;

import java.util.List;

/**
 * 模板文件操作类型.
 * @author chenhao
 *
 */
public enum TplOperType {
  IMP("10", "导入"), EXP("20", "导出"), DOWNTPL("30", "下载模板");
    public static final String TPL_OPERTYPE = "operType";
    public static final String TPL_OPERTYPES = "tplOtypes";

  private String key;
  private String name;

  private TplOperType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TplOperType
   */
  public static TplOperType getByKey(String key) {
    if ((key != null)) {
      TplOperType[] entitys = TplOperType.values();
      for (TplOperType entity : entitys) {
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
   * @return TplOperType
   */
  public static Boolean checkHas(List<TplOperType> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return TplOperType
   */
  public static TplOperType checkRet(List<TplOperType> fstypes, String key) {
      for (TplOperType fstype : fstypes) {
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
