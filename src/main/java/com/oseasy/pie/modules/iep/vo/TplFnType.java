package com.oseasy.pie.modules.iep.vo;

import java.util.List;

/**
 * 模板功能类型.
 * @author chenhao
 *
 */
public enum TplFnType {
    DEFAULT("0", "默认"), GNODE_QUERT("10", "流程节点列表"), GNODE_LIST("20", "流程查询列表"), TJ("30", "流程统计页面");
    public static final String TPL_FNTYPE = "tplFntype";
    public static final String TPL_FNTYPES = "tplFntypes";

  private String key;
  private String name;

  private TplFnType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TplFnType
   */
  public static TplFnType getByKey(String key) {
    if ((key != null)) {
      TplFnType[] entitys = TplFnType.values();
      for (TplFnType entity : entitys) {
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
   * @return TplFnType
   */
  public static Boolean checkHas(List<TplFnType> fstypes, String key) {
      if (checkRet(fstypes, key) != null) {
          return true;
      }
      return false;
  }

  /**
   * 检查是否存在，并返回 .
   * @author chenhao
   * @param key 枚举标识
   * @return TplFnType
   */
  public static TplFnType checkRet(List<TplFnType> fstypes, String key) {
      for (TplFnType fstype : fstypes) {
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
