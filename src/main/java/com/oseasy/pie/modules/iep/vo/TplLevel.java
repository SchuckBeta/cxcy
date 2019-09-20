package com.oseasy.pie.modules.iep.vo;

import java.util.List;

/**
 * 模板级别:0、顶级；10、组；20、功能；30、操作.
 * @author chenhao
 *
 */
public enum TplLevel {
  DEF(0, "顶级"), GROUP(10, "组"), FUN(20, "功能"), OPER(30, "操作");
    public static final String TPL_LEVEL = "tplLevel";
    public static final String TPL_LEVELS = "tplLevels";

  private Integer key;
  private String name;

  private TplLevel(Integer key, String name) {
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
  public static TplLevel getByKey(Integer key) {
    if ((key != null)) {
      TplLevel[] entitys = TplLevel.values();
      for (TplLevel entity : entitys) {
        if ((key == entity.getKey())) {
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
  public static Boolean checkHas(List<TplLevel> fstypes, Integer key) {
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
  public static TplLevel checkRet(List<TplLevel> fstypes, Integer key) {
      for (TplLevel fstype : fstypes) {
          if ((fstype.getKey() == key)) {
              return fstype;
          }
      }
      return null;
  }

  public Integer getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
  }
}
